#/usr/bin/perl

use LWP::UserAgent;

my $ENTREZ_URL = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=search&db=nucleotide&term=";
# Caller has to make sure the filename is unique
my $page_name = shift @ARGV;

my $url = "";
foreach (@ARGV) {
    $url  .= ($_."+");
}
chop $url;

#$url =~ s/\s/\+/g;
$url = $ENTREZ_URL.$url;
get_page($url, $page_name);
parse_page($page_name);
unlink $page_name;

## sub implementation

# definition of the first page
sub parse_page {
	my $page = shift;
	my ($acc, $gi, $desc);
	my $line;
	my $count = 0;
	
	open PAGE, "$page"         or return 1; #die "1: Error: $!\n";
	open OUTPUT, ">$page.txt"  or return 1; #die "2: Error: $!\n";
	while ($line = <PAGE>) {
		$line =~ s/<[^<>]*>//g;
		#print $line;
		if ($line =~ m#^([^|]*)gi\|(\d+)\|(ref|gb)\|([^|]+)#) { 					
			#print ++$count, "\n";
			print OUTPUT "$2!!$4!!$1\n";
			print OUTPUT "//\n";
		} 
	}
	close OUTPUT;
	close PAGE;

	return 0;
}


sub get_page {
	my $url = shift;
	my $fn = shift;
	
	my $ua = new LWP::UserAgent or die "No object\n";
	$ua->timeout(180);  # timeout is 6 minutes
	my $header = new HTTP::Headers;
	my $req = new HTTP::Request(GET => $url, $header);
	my $res = $ua->request($req, $fn);
	unless ($res->is_success) {
		print "Search failed because of NCBI server problem.";
		exit(1);
	}
	print $res->content;
	return 0;
}











