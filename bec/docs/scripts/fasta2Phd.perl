#!/usr/bin/perl -w
#
# fasta2Phd.perl
#
# PURPOSE:  allows phrap to utilize a sequence that may not come from
#   a read (such as a consensus from an overlapping cosmid or a genbank
#   sequence).  This is NOT to be used for normal sequence assembly. 
#   For that, you should use the phredPhrap script which will give much
#   more accurate results.
#
# INPUT:  fasta file containing the sequence
#
# OUTPUT:  a dummy phd file with the bases, 
#    quality 20 (or the quality you specified), and peak position 0
#    Note:  you might want to play around the with quality score, depending
#    on how certain you are of the base calls.  You might want them to be
#    as low as 5.
#
# Revisions: Nov 2001, DG, to allow for read names with dots in them
#            June 2002, Bill Gilliland to allow the user to specify 
#                the quality value

use Getopt::Long;
GetOptions( "quality=i" => \$qualityValue);
 
if(defined($qualityValue)) {
 
     # Sanity check
     if($qualityValue < 0) {
         $qualityValue = 0;
     }
     if($qualityValue > 99) {
         $qualityValue = 99;
     }
 
     $nQuality = $qualityValue;
} else {
     $nQuality = 20;
}
 
$szUsage = "Usage: fasta2Phd.perl [optional: --quality=n, default 20] <name of file with fasta>";

if ($#ARGV != 0) {
    die "$szUsage\n";
}


$szFastaFile = $ARGV[0];

open( filFasta, "$szFastaFile" ) || 
    die "couldn't open file $szFastaFile\n";

# strip off the pathname


$szBase1 = `basename $szFastaFile`;
chop( $szBase1 );

# strip off extension

$nFindDot = rindex( $szBase1, "." );

if ($nFindDot == -1 ) {
    $szFastaSequenceName = $szBase1;
}
else {
    $szFastaSequenceName = substr( $szBase1, 0, $nFindDot );
}


$szPHDFileName = $szFastaSequenceName . ".phd.1";

print( "creating file $szPHDFileName\n" );

open( filPHD, ">$szPHDFileName" ) || die "couldn't open $szPHDFileName\n";

print( filPHD "BEGIN_SEQUENCE $szFastaSequenceName\n");
print( filPHD "\n" );


print( filPHD "BEGIN_COMMENT\n" );
print( filPHD "\n" );
print( filPHD "CHROMAT_FILE: none\n");
print( filPHD "ABI_THUMBPRINT: none\n");
print( filPHD "PHRED_VERSION: not called by phred\n");
print( filPHD "CALL_METHOD: fasta2Phd.perl\n" );
print( filPHD "QUALITY_LEVELS: 99\n" );

$szTemp3 = `date`;

$szTemp4 = substr($szTemp3, 0, length( "Fri Dec  1 14:22:38 " ));
$szTemp5 = substr($szTemp3, length( "Fri Dec  1 14:22:38 PST "), length("1995"));

$szDate = $szTemp4 . $szTemp5;

print( filPHD "TIME: $szDate\n" );

print( filPHD "END_COMMENT\n" );
print( filPHD "\n" );
print( filPHD "BEGIN_DNA\n" );

$szLine = <filFasta> || die "couldn't read line 1 of $szFastaFile\n";
($szLine =~ /^>/) || die "wasn't fasta format--first line was $szLine\n";
$szBigLine = "";
while( $szLine = <filFasta> ) {
    chomp( $szLine );
    $szLine =~ s/\s//g;
    $szBigLine .= $szLine;
}

close( filFasta );

$cLast = substr($szBigLine, length($szBigLine)-1, 1);

if ($cLast eq "\n" ) {
    chop( $szBigLine );
}

for( $n = 0; $n < length( $szBigLine ); ++$n ) {
    $cBase = substr( $szBigLine, $n, 1 );

    $cBase =~ tr/A-Z/a-z/;

    if ( $cBase eq '-' ) {
        $cBase = 'n';
    }

# base, quality, peak position

    print( filPHD "$cBase $nQuality 0\n" );
}



print( filPHD "END_DNA\n" );
print( filPHD "\n" );
print( filPHD "END_SEQUENCE\n" );

close( filPHD );


