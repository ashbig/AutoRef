#!/usr/bin/perl -w
#
# phd2Ace.perl
#
# PURPOSE: create an ace file from a single phd file so consed can
#	view the read, pick primers from it, show the chromatograms, etc. 
#
# INPUT:  phd file
#       This program assumes that you are in the directory where the
#	ace file is to be created.  It assumes that the phd file is in
#	../phd_dir
#
# REV:  981002 (David Gordon) for new ace format
#       981210 (DG) to eliminate warning message

$szPhdDirPath = "../phd_dir";

$szUsage = "Usage: phd2Ace.perl <filename (without directory) of phd file>\nThe phd file is assumed to reside in $szPhdDirPath";

if ($#ARGV != 0) {
    die "$szUsage\n";
}

$szPhdFilename = $ARGV[0]; 
# untaint the name - it follows same rule as projects 
unless ($szPhdFilename =~ /^([a-zA-Z0-9\-\._]+)\.phd.([0-9]+)$/) { 
	die "Name of phd file must be only letters, numbers, -, _, and .\n"; 
}

$szReadName = $szPhdFilename;
$szReadName =~ s/\.phd\.([0-9]+)$//;


$szAceFilename = $szReadName . ".ace";

#if (-e $szAceFilename ) {
#    die "$szAceFilename already exists";
#}

open( filAce, ">$szAceFilename" ) || die "Couldn't open $szAceFilename for output";

$szPHDPathname = $szPhdDirPath . "/" . $szPhdFilename;
open( filPHD, "$szPHDPathname" ) || die "Couldn't open $szPHDPathname for input";

# phd file looks like this:

 
#BEGIN_COMMENT
 
#CHROMAT_FILE: K26-394c
#ABI_THUMBPRINT: 050332254322000055354034266141
#PHRED_VERSION: 0.960828
#CALL_METHOD: phred
#QUALITY_LEVELS: 99
#TIME: Thu Sep 12 15:42:32 1996
 
#END_COMMENT
 
#BEGIN_DNA
#c 6 1
#t 6 9
#g 6 18
#c 6 26
#g 8 40
#t 4 61
#a 4 66
#END_DNA
 
#END_SEQUENCE




# looking for CHROMAT_FILE: K26-394c line

$bFoundCHROMAT_FILE = 0;
while( ! $bFoundCHROMAT_FILE ) {
	$szLine = <filPHD> || die "premature end of file while looking for CHROMAT_FILE:";
	if ($szLine =~ /^CHROMAT_FILE:/	) {
		chop( $szLine );
		$szChromatLine = $szLine;
		$bFoundCHROMAT_FILE = 1;
	}
}

$bFoundTime = 0;
while( ! $bFoundTime ) {
	$szLine = <filPHD> || die "premature end of file while looking for TIME:";
	if ($szLine =~ /^TIME:/ ) {
		chop( $szLine );
		$szTimeStamp = $szLine;
		$bFoundTime = 1;
	}
}



$bFoundBeginDna = 0;
while( ! $bFoundBeginDna ) {
	$szLine = <filPHD> || die "premature end of file while looking for BEGIN_DNA";
	if ( $szLine =~ /^BEGIN_DNA/ ) {
		$bFoundBeginDna = 1;
	}
}


@aBases = ();
@aQualities = ();

$bFoundEndDna = 0;
while( ! $bFoundEndDna ) {
        $szLine = <filPHD> || die "premature end of file while looking for END_DNA";
	if ($szLine =~ /^END_DNA/ ) {
		$bFoundEndDna = 1;
	}
	else {
		@aPhdLine= split( /\s+/, $szLine );
        $cBase = $aPhdLine[0];
        $nQuality = $aPhdLine[1];
		push( @aBases, $cBase );
		push( @aQualities, $nQuality );
	}
}

$szContigName = $szReadName . "-Contig";

print filAce "AS 1 1\n";
print filAce "\n";
$nNumberOfBases = $#aBases + 1;
# the 1 1 below is for 1 read in contig, 1 base segment in contig
print filAce "CO $szContigName $nNumberOfBases 1 1 U\n";

print_bases();
print filAce "\n";


print filAce "BQ\n";

$nMaxNumberOfQualitiesOnLine = 25;
$nNextQualityPosition = 0;
while( ( $#aQualities - $nNextQualityPosition ) >= $nMaxNumberOfQualitiesOnLine ) {
	for( $nQualityOnLine = 0; $nQualityOnLine < $nMaxNumberOfQualitiesOnLine; 
		++$nQualityOnLine ) {
		print filAce " ", $aQualities[ $nNextQualityPosition + $nQualityOnLine ];
	}
	print filAce "\n";
	$nNextQualityPosition += $nMaxNumberOfQualitiesOnLine;
}

if ($#aQualities >= $nNextQualityPosition ) {
	while( $#aQualities >= $nNextQualityPosition ) {
		print filAce " ", $aQualities[ $nNextQualityPosition ];
		++$nNextQualityPosition;
	}

	print filAce "\n";
}

print filAce "\n";

print filAce "AF $szReadName U 1\n";
print filAce "BS 1 $nNumberOfBases $szReadName\n";
print filAce "\n";

# The 0 0 below is for the # of whole read info items and the
# # of read tags
print filAce "RD $szReadName $nNumberOfBases 0 0\n";
print_bases();

print filAce "\n";
# This says basically that nothing is clipped off
print filAce "QA 1 $nNumberOfBases 1 $nNumberOfBases\n";
print filAce "DS $szChromatLine PHD_FILE: $szPhdFilename $szTimeStamp\n";

sub print_bases {

$nMaxNumberOfBasesOnLine = 50;
$nCurrentBasePosition = 0;
while( ( $#aBases - $nCurrentBasePosition ) >= $nMaxNumberOfBasesOnLine ) {
        for( $nLineIndex = 0; $nLineIndex < $nMaxNumberOfBasesOnLine; ++$nLineIndex
) {
                print filAce $aBases[ $nCurrentBasePosition + $nLineIndex ];
        }
        print filAce "\n";
        $nCurrentBasePosition += $nMaxNumberOfBasesOnLine;
}
 
if ( $#aBases >= $nCurrentBasePosition ) {
        while( $#aBases >= $nCurrentBasePosition ) {
                print filAce $aBases[ $nCurrentBasePosition ];
                ++$nCurrentBasePosition;
        }
 
        print filAce "\n";
}

} # end sub print_bases


