#!/usr/bin/perl -w
#
# lib2Phd.perl
#
# PURPOSE:  allows phrap to utilize a sequences that may not come from
#   reads (such as a consensus from an overlapping cosmid or a genbank
#   sequence).  This is NOT to be used for normal sequence assembly. 
#   For that, you should use the phredPhrap script which will give much
#   more accurate results.  This program is the equivalent of
#   fasta2Phd.perl except that it allows more than one sequence in
#   the fasta file
#
# INPUT:  fasta "library" file containing the sequence(s)
#
# OUTPUT:  for each sequence in the fasta file (above), a dummy phd
#    file with the bases, quality 20, and peak position 0 
#    Note:  you might want to play around the with quality score, depending
#    on how certain you are of the base calls.  You might want them to be
#    as low as 5.
#
# Original code from David Gordon at UWGC.
# Modified by Gerry Bouffard (bouffard@nhgri.nih.gov) 20 Nov 1998 to
#    accept fasta library files as input.  The output file is named 
#    according to the first, non-space block of text on each fasta defline. 

$nQuality = 20;

$szUsage = "Usage: lib2Phd.perl <name of file with fasta library>";

if ($#ARGV != 0) {
    die "$szUsage\n";
}

$szLibFile = $ARGV[0];

open( filLib, "$szLibFile" ) || 
    die "couldn't open file $szLibFile\n";

# strip off the pathname
$szBase1 = `basename $szLibFile`;
chop( $szBase1 );

#| # strip off extension
#| $nFindDot = index( $szBase1, "." );
#| 
#| if ($nFindDot == -1 ) {
#|     $szFastaSequenceName = $szBase1;
#| }
#| else {
#|     $szFastaSequenceName = substr( $szBase1, 0, $nFindDot );
#| }

$szFastaSequenceName = "";

while (<filLib>) {
    # Check for fasta defline, otherwise expect sequence data
    if (/^\s*>\s*(\S*)/) {  # fasta type defline
	$tempSequenceName = $1;

	#If this is not the first fasta file, close out the previous one.
	unless ($szFastaSequenceName eq "") {
	    print( filPHD "END_DNA\n" );
	    print( filPHD "\n" );
	    print( filPHD "END_SEQUENCE\n" );

	    close( filPHD );	    
	}

	$szFastaSequenceName = $tempSequenceName;
	$szPHDFileName = $szFastaSequenceName . ".phd.1";

	#test for pre-existing phd file
	if (-e $szPHDFileName) {
	    print STDERR "Warning: overwriting previous $szPHDFileName!\n";
	}
	else {
	    print STDERR "... creating file $szPHDFileName\n";
	}

	open( filPHD, ">$szPHDFileName" ) || 
	    die "couldn't open $szPHDFileName\n";

	print( filPHD "BEGIN_SEQUENCE $szFastaSequenceName\n");
	print( filPHD "\n" );

	print( filPHD "BEGIN_COMMENT\n" );
	print( filPHD "\n" );
	print( filPHD "CHROMAT_FILE: none\n");
	print( filPHD "ABI_THUMBPRINT: none\n");
	print( filPHD "PHRED_VERSION: not called by phred\n");
	print( filPHD "CALL_METHOD: fasta2Phd.perl\n" );
	print( filPHD "QUALITY_LEVELS: 2\n" );

	$szTemp3 = `date`;
	$szTemp4 = substr($szTemp3, 0, length( "Fri Dec  1 14:22:38 " ));
	$szTemp5 = substr($szTemp3, length( "Fri Dec  1 14:22:38 PST "), 
			  length("1995"));
	$szDate = $szTemp4 . $szTemp5;

	print( filPHD "TIME: $szDate\n" );
	print( filPHD "END_COMMENT\n" );
	print( filPHD "\n" );
	print( filPHD "BEGIN_DNA\n" );

    } # end of fasta type defline

    else { # line of sequence data (we hope)
	if ($szFastaSequenceName eq "") {
	    print STDERR "Ignoring text preceeding first fasta defline.\n";
	    next; # no file to write to
	}
	else { # convert the sequence data to single line quality scores
	    chomp; # removing any trailing blanks or returns
	    $szBigLine = $_;

	    $szBigLine =~ tr/A-Z/a-z/; # switch to lower case, if necessary
	    if ($szBigLine =~ /[^acgtrymkswbdhvnx]/) {
		print STDOUT "Warning: Unexpected characters found in ".
		    "$szFastaSequenceName sequence.\n";
	    }

	    for( $n = 0; $n < length( $szBigLine ); ++$n ) {
		$cBase = substr( $szBigLine, $n, 1 );

		# base, quality, peak position
		print( filPHD "$cBase $nQuality 0\n" );
	    }
	}
    } # end of line of sequence data
} # end of while <filLib> loop

#----------------------------------------------------------

#Close out the last file, if there is one.
unless ($szFastaSequenceName eq "") {
    print( filPHD "END_DNA\n" );
    print( filPHD "\n" );
    print( filPHD "END_SEQUENCE\n" );

    close( filPHD );	    
}

close( filLib );

die "\nlib2Phd program completed successfully.\n";

