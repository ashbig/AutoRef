#!c:\perl\bin\perl

#use strict;
use LWP::UserAgent;

$| = 1;

# List of gb pages to be retrieved
my $result_file = shift @ARGV;
open RESULT, ">$result_file" or die "Error: $!\n";

my @gb_list = @ARGV;

## declare vars for data field used in database tables
my ($acc,			# GenBank accession number six or eight chars, never changed
	$org,			# organism
	$start, 		# start codon position, first nt
	$stop, 			# stop codon position, last nt
	$seq,			# only cds sequence
);
local $gi;

my $cds_v = 1;		
my $NCBI_URL = "http://www.ncbi.nlm.nih.gov:80/entrez/query.fcgi?cmd=Retrieve&db=Nucleotide&dopt=GenBank&list_uids=";
my $status = "OK";

foreach $gi (@gb_list) {
	if (download_page($gi, $NCBI_URL, $gi.".html") == 1) {
		if (parse_gb($gi, $gi.".html") != 1) {
			$status = "Bad $gi";
			$acc = "none"; $org = "none"; $start = "none"; $stop = "none"; $seq = "none";
		}
	} else {
		print "No page\n";
		#$acc = "none"; $org = "none"; $start = "none"; $stop = "none"; $seq = "none";
	}
	write_result();
	init_data_fields();
	cleanup();
}
close RESULT;

####### SUBS #########
sub write_result() {
	print RESULT "$status!!";
	print RESULT "$gi!!";
	print RESULT "$acc!!";
	print RESULT "$org!!";
	print RESULT "$start!!";
	print RESULT "$stop!!";
	print RESULT "$seq\n";
	print RESULT "//\n";
}

sub download_page {
	my ($gi, $url, $fn) = @_;
	$url .= $gi;
	my $ua = new LWP::UserAgent or die "No object\n";
	$ua->timeout(180);  # timeout is 6 minutes
	my $header = new HTTP::Headers;
	my $req = new HTTP::Request(GET => "$url", $header);
	my $res = $ua->request($req, $fn);
	unless ($res->is_success) {
		$status = "Failed downloading $gi";
		return 0;
	}
	return 1;
}

sub parse_gb {
	my $gi = shift;
	my $page_name = $gi.'.html';
	open HTML, $page_name or die "Error: $!\n";
	my $cds_cnt = 0;  
	while (<HTML>) {
		chomp;
		s/<[^<>]*>//g;
		return 1 if (/^\/\/$/);
		if (/(No items found)|(Wrong UID 000)/) {
			print "No idemt\n";
			close HTML;
			unlink $page_name;			
			return 0;
		}
		if (/^DEFINITION\s+(.*)/) {
			$g_name = $1; 
			next;
		} elsif (/^VERSION\s+([A-Za-z0-9_.]+)\s+GI:(\d+)$/) {
			$acc = $1;
			$gi = $2; 
			next;
		} elsif (/ORGANISM\s+([\w\s]+)/) {
			$org = $1; 
			next;
		} elsif (/CDS\s+/) {
			$cds_cnt++;
			if ($cds_v == $cds_cnt) { 
				my $ft_url = "http://www.ncbi.nlm.nih.gov:80/cgi-bin/Entrez/getfeat?id=$cds_v&entity=1&gi=";
				download_page($gi, $ft_url, 'cds.'.$page_name);
				parse_cds('cds.'.$page_name, $gi);
			}
		} 
	}
	close HTML;
	unlink $page_name;
	return 1;
}

sub parse_cds {
	my $page_name = shift;
	my $gi = shift;
	open CDS, $page_name or die "Error: $!\n";
	# parse cds for g_sym, seq, start, stop and cds length
	my $seq_sec = 0;
	while (<CDS>) {
		chomp;
		s/<[^<>]*>//g;
		# print; print br;
		if (/\/gene=\"(.*)\"/) {
			$g_sym = $1;		# print "SYMBOL = $g_sym", br;
		} elsif (/\/codon_start=(\d+)/) {
			$start = $1;		# print "Start = $start", br;
		} elsif (/BASE COUNT\s+(\d+)\sa\s+(\d+)\sc\s+(\d+)\sg\s+(\d+)\st/) {
			$A = $1; $C = $2; $G = $3; $T = $4;
			$stop = $cds_len = $A + $C + $G + $T;
								# print "A C G T Len Stop = ($A, $C, $G, $T, $cds_len, $stop)", br;
		} elsif (/ORIGIN/) {
			$seq_sec = 1; 		# print "ORIGIN = ", br;
		} elsif ($seq_sec && /\d+\s([a-zA-Z ]+)/) {
			my $str = $1; 		# print "SEQ= $str", br;
			$str =~ s/\s//g;	# print "SEQ= $str", br;
			$seq .= $str;		# print "SEQ= $str", br;
		}
	}
	close CDS;
	# check sequence for correctness
	$seq = uc $seq; 			# print "SEQ= $seq", br;
}

sub init_data_fields {
	undef($acc);			# GenBank accession number six or eight chars, never changed
	undef($gi); 			# GenBank ig number, uniquely identify each sequence
	undef($org);			# 
	undef($start);	 		# start codon position, first nt
	undef($stop); 			# stop codon position, last nt
	undef($seq);			# entire sequence
}

sub cleanup {
    unlink <*.html>;
}




