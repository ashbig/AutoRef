#!/usr/bin/perl -w
#
# ace2Oligos.perl
#
#
# Purpose:  Scans the ace file looking for new oligos so they can
#   be ordered.
#
# How it works:  Keeps a file of already-ordered oligos
#   First reads this file so it knows what oligos are already in it.
#   Then reads the ace file.  If there are any oligos in the ace file
#   that aren't already in the oligo file, it adds the new oligos to
#   the oligo file.
#
# Rev: 980427 (David Gordon)
# Rev: 000330 to handle comments in oligo tags
#

$szUsage = "Usage:  ace2Oligos.perl (name of ace file) (name of oligo file)";

if ( $ARGV[0] eq "-V" || $ARGV[0] eq "-v" ) {
    print "ace2Oligos.perl Version 000330\n";
    exit( 1 );
}

die "$szUsage" if  ( $#ARGV != 1 );



$szAceFile = $ARGV[0];
$szOligosFile = $ARGV[1];

%aExistingOligos = ();

# check what oligos are already in the oligo file
if ( -e "$szOligosFile" ) {
    open( filOligos, "$szOligosFile" ) || die "could not open $szOligosFile for reading";


    while( <filOligos> ) {
        if ( length( $_ ) > length("name=") ) {
            if ( substr( $_, 0, 5 ) eq "name=" ) {
                $szName = substr( $_, 5 );
                chomp( $szName );
                $aExistingOligos{ $szName } = "";  # just make it exist
            }
        }
    }
}

close( filOligos );

open( filOligos, ">>$szOligosFile" ) || die "could not open $szOligosFile for append";

open( filAce, "$szAceFile" ) || die "could not open ace file $szAceFile for reading";

# Here is a typical example of some consensus tags:
# 
# CT{
# Contig1 oligo consed 1043 1062 980408
# standard.3 aaaaattagcggaatgtagt 51 U
# seq from clone
# }
# standard.3 is the name
# aaaaa... are the bases
# 51 is the melting temperature
# U or C is whether the contig was complemented or uncomplemented 
# with respect to the way phrap originally oriented the contig
 
# CT{
# Contig1 comment consed 44 53 980408
# Comment for bases
# from cccaggatgt
# }
#
# CT{
# Contig1 oligo consed 2118 2140 000330:111803
# COMMENT{
# comment about oligo
# C}
# standard.2 caccctaagacactgagaaaatc 57 U
# djs74_690 djs74_1803 djs74_1861
# }


$nOligosFound = 0;
$nOligosWritten = 0;

while( <filAce> ) {
    if ( length( $_ ) > 3 ) {
        if ( substr( $_, 0, 3) eq "CT{" ) {
            $_ = <filAce> || die "end of file while reading consensus tag";
            @aWords = split;
            if ( $aWords[1] eq "oligo" ) {
                ++$nOligosFound;
                # we now have an oligo tag
                $szDate = $aWords[5];

                $_ = <filAce> || die "end of file while reading oligo tag";

                if ( $_ =~ /^COMMENT{/ ) {
                    # we are in a comment--continue reading lines until
                    # we get out of the comment
                    while(1 ) {
                        $_ = <filAce>  ||
                            die "premature end of file while in CT block";
                        
                        last if ( $_ =~ /^C\}/ );
                    }
                    # read the next line, which contains the oligo bases
                    $_ = <filAce> || 
                        die "premature end of file while in oligo tag";
                }

                @aWords2 = split;
                $szOligoName = $aWords2[0];
                $szOligoBases = $aWords2[1];
                $szMeltingTemp = $aWords2[2];

                # only put a new oligo in the oligo file if it
                # isn't there already!

                if ( !exists( $aExistingOligos{ $szOligoName } ) ) {
                
                    $szTemplate = <filAce> || die "end of file while reading oligo tag template";

                
                    print filOligos "name=$szOligoName\n";
                    print filOligos "sequence=$szOligoBases\n";
                    print filOligos "template=$szTemplate";
                    print filOligos "date=$szDate temp=$szMeltingTemp\n";
                    print filOligos "\n";
                    ++$nOligosWritten;
                }
            }
        }
    }
}

close( filAce );

print "$nOligosFound oligos found and $nOligosWritten new oligos found\n";


            







