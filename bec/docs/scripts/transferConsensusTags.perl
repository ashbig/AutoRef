#!/usr/bin/perl -w
#
# Purpose:  Transfers consensus tags from an old assembly to
#     a new assembly.  
# How to Use It:  Both the old and new ace file must be present. 
#     Tags are moved from the old ace file to the new one.  There is
#     no check to see if the tags are already in the new one.  Thus you
#     must not run this more than once for the same new ace file.
#
# INPUT:  command line:   <old ace file> <new ace file>
#
# 
#    If you attempt to modify this script, you are on your own.
# If you attempt to use portions of this script for other purposes, you
# are on your own.  
#
# How it works:  Construct a mapping from oldPadded oldUnpadded.  Then
#     run cross_match between the old and new contigs to construct a 
#     mapping from old unpadded and new unpadded.  Then construct a mapping
#     from new unpadded to new padded.  The composite of these mappings
#     is a mapping from old padded to new padded.  Use this mapping to
#     transfer consensus tags.  Note that in general, note only the positions
#     will have changed, but the contigs as well.
#
# DG, December 1997
# March 1998 DG, to read from format1 ace files in addition to format2
# June 1998 DG, to handle perl version 5.004_04 (pickier)
# July 1998 DG, to fix bug in which the tag as a whole goes unambiguously
#  to a particular location, but one end point of the tag may go 
#  ambiguously to several locations
# Nov 1998, DG to not transfer consensus tags which have NoTrans qualifier
# Aug 1999, DG to correctly handle complementing 1 base tags, including
#  autofinish tags
# Apr 2001, DG to fix bug with orientation of oligo and autoFinishExp
#  tags
# Nov 2001, DG to handle cloneEnd tags which have orientation

$szUsage = "Usage:  transferConsensusTags.perl (old ace file) (new ace file)";

if ( $ARGV[0] eq "-V" || $ARGV[0] eq "-v" ) {
    print "transferConsensusTags.perl Version 011130\n";
    exit( 1 );
}


$szCrossMatchExe = "d:/programs_bio/assembler/exe/cross_match";


die "$szUsage" if ( $#ARGV != 1 );

$szAceFileOld = $ARGV[0];
$szAceFileNew = $ARGV[1];

die "Can't read $szAceFileOld" if (! -r $szAceFileOld );
die "Can't read $szAceFileNew" if (! -r $szAceFileNew );

# should check that have write permission to this directory before start

$szTempFile = "temp" . $$;
open( filTemp, ">$szTempFile" ) || die "don't have write permission to this directory";
close( filTemp );
unlink( $szTempFile ) || die "couldn't delete a file in this directory";

# now construct root filenames for old and new temporary tables

$szFileRootOld = $szAceFileOld;

$szFileRootOld =~ s/\.fasta\.screen\..*$//;

# strip off any directory
$nLastSlash = rindex( $szFileRootOld, "/" );
if ( $nLastSlash != -1 ) {
    $szFileRootOld = substr( $szFileRootOld, $nLastSlash + 1 );
}

$szFileRootOld .= "Old";

$szFileRootNew = $szAceFileNew;

$szFileRootNew =~ s/\.fasta\.screen\..*$//;

# strip off any directory
$nLastSlash = rindex( $szFileRootNew, "/" );
if ( $nLastSlash != -1 ) {
    $szFileRootNew = substr( $szFileRootNew, $nLastSlash + 1 );
}

$szFileRootNew .= "New";


($nFormatOfOldAceFile, %aContigsInOldAceFile ) = 
    &bDetermineAceFileFormat( $szAceFileOld );

($nFormatOfNewAceFile, %aContigsInNewAceFile ) =
    &bDetermineAceFileFormat( $szAceFileNew );


# now construct tables of pads
print "constructing pads table...\n";

$szPadsTableOld = $szFileRootOld . ".pads";
if ( $nFormatOfOldAceFile == 2 ) {
    &constructPadsTable( $szAceFileOld, $szPadsTableOld );
}
else {
    &constructPadsTableAceFormat1( $szAceFileOld, $szPadsTableOld, 
                                  %aContigsInOldAceFile );
}

$szPadsTableNew = $szFileRootNew . ".pads";
if ( $nFormatOfNewAceFile == 2 ) {
    &constructPadsTable( $szAceFileNew, $szPadsTableNew );
}
else {
    &constructPadsTableAceFormat1( $szAceFileNew, $szPadsTableNew,
                        %aContigsInNewAceFile );
}

# now construct contigs files
print "constructing contigs files...\n";

$szContigsFileOld = $szFileRootOld . ".contigs";
if ( $nFormatOfOldAceFile == 2) {
    &constructContigsFile( $szAceFileOld, $szContigsFileOld );
}
else {
    &constructContigsFileAceFormat1( $szAceFileOld, $szContigsFileOld,
                                    %aContigsInOldAceFile );
}

$szContigsFileNew = $szFileRootNew . ".contigs";
if ( $nFormatOfNewAceFile == 2 ) {
    &constructContigsFile( $szAceFileNew, $szContigsFileNew );
}
else {
    &constructContigsFileAceFormat1( $szAceFileNew, $szContigsFileNew,
                                   %aContigsInNewAceFile );
}

&buildUnpaddedFromPaddedOldTable;

&buildPaddedFromUnpaddedNewTable;

$szCrossMatchOutputFilename = 
    $szFileRootOld . "_to_" . $szFileRootNew . ".cross";

$szCommand = "$szCrossMatchExe $szContigsFileOld $szContigsFileNew -minmatch 50 -tags -discrep_lists >$szCrossMatchOutputFilename";

print "running cross_match...\n\n";
!system( $szCommand ) || die "cross_match failed running $szCommand";

# clean up after ourselves
unlink( $szContigsFileOld );
unlink( $szContigsFileNew );
# cross_match leaves this around
unlink( $szContigsFileOld . ".log" );


print "Building unpadded old to unpadded new translation table\n";

$szUnpaddedNewFromUnpaddedOldTable = 
    $szFileRootNew . "_from_" . $szFileRootOld . ".unpadded_transfer_table";

&readAndParseCrossMatchOutput();

&readParsedCrossMatchOutput();


# make table of which old contigs were complemented with respect to the
# way they were created by phrap

&constructTableOfOldContigsComplemented( $szAceFileOld, 
                                         \%aOldContigsWereComplemented );

print "now transferring consensus tags...\n";

open( filAceFileOld, $szAceFileOld ) || die "couldn't open $szAceFileOld ";
open( filAceFileNew, ">>$szAceFileNew" ) || die "couldn't append to new ace file $szAceFileNew";

$szErrorFile = 
    $szFileRootNew . "_from_" . $szFileRootOld . ".err";

open( filErrLog, ">$szErrorFile" ) || die "couldn't open error file $szErrorFile";

$bThereWereErrors = 0;

while( <filAceFileOld> ) {
    if ($_ eq "CT{\n" ) {
        # found a consensus tag 
        $_ = <filAceFileOld>;
        if(!defined $_) {
            die "premature end of file while reading consensus tag";
        }
        $szMainTagLine = $_;

        @aWords = split;
        $szOldContig = $aWords[0];
        $szTagType = $aWords[1];
        $szTagSource = $aWords[2];
        $nPaddedOldStart = $aWords[3];
        $nPaddedOldEnd = $aWords[4];
        $szDate = $aWords[5];


        $bNoTrans = 0;
        # if this tag should not be transferred, ignore it
        if ( $#aWords > 5 ) {
            $szNoTrans = $aWords[6];
            if ( $szNoTrans eq "NoTrans" ) {
                $bNoTrans = 1;
            }
        }


        @aAdditionalLines = ();
        while( 1 ) {
            $_ = <filAceFileOld>;
            if(!defined $_) {
                die "premature end of file while reading consensus tag";
            }
            if ( $_ eq "}\n" ) {
                last;
            }
            
            push( @aAdditionalLines, $_ );
        }

        # we wanted to finish reading that tag before we skipped to 
        # the next tag

        if ( $bNoTrans ) {
            next;
        }

        # convert to unpadded
        
        $nUnpaddedOldStart = &nUnpaddedFromPaddedOld( $szOldContig, $nPaddedOldStart );
        $nUnpaddedOldEnd = &nUnpaddedFromPaddedOld( $szOldContig, $nPaddedOldEnd );

        # now use the cross_match table to translate to new contig and new
        # unpadded coordinates

        my $bHadToBeComplemented;

        ($bOK, $szNewContig, $nUnpaddedNewStart, $nUnpaddedNewEnd, 
        $bHadToBeComplemented )  = 
           &oldConsensusTagCanBeTransferredToNewAssembly( 
                                            $szOldContig,
                                            $nUnpaddedOldStart,
                                            $nUnpaddedOldEnd );

        if ( ! $bOK ) {
            $bThereWereErrors = 1;
            print filErrLog "CT{\n";
            print filErrLog $szMainTagLine;
            foreach $szAdditionalLine (@aAdditionalLines ) {
                print filErrLog $szAdditionalLine;
            }
            print filErrLog "}\n";
        }
        else {
            $nPaddedNewStart = &nPaddedFromUnpaddedNew( 
                                      $szNewContig,
                                      $nUnpaddedNewStart );

            $nPaddedNewEnd = &nPaddedFromUnpaddedNew(
                                      $szNewContig,
                                      $nUnpaddedNewEnd );

            print filAceFileNew "\nCT{\n";
            $szMainTagLine = "$szNewContig $szTagType $szTagSource $nPaddedNewStart $nPaddedNewEnd $szDate\n";

            print filAceFileNew $szMainTagLine;

            $nUorC = 1;
            if ( $bHadToBeComplemented ) {
                $nUorC *= -1;
            }
            if ( exists( $aOldContigsWereComplemented{ $szOldContig } ) ) {
                if (  $aOldContigsWereComplemented{ $szOldContig } eq "C" ) {
                    $nUorC *= -1;
                }
            }

            if ( $szTagType eq "oligo" ) {

                # example:
                # CT{
                # Contig5 oligo consed 308 324 000418:132409
                # djs218.118 ggcacaatctcggctc 56 U
                # djs218_336 djs218_384
                # }              

                my $szFirstLine = shift @aAdditionalLines;

                # clip off final U or C and save it
                chomp( $szFirstLine );
                $szFirstLine =~ s/([CU])$//;
                $cUorC = $1;

                if ( $cUorC eq "C" ) {
                    $nUorC *= -1;
                }

                if ( $nUorC == -1 ) {
                    $szFirstLine .= "C\n";
                }
                else {
                    $szFirstLine .= "U\n";
                }

                print filAceFileNew $szFirstLine;
            }
            elsif ( $szTagType eq "autoFinishExp" ) {

                my $szFirstLine = shift @aAdditionalLines;

                chomp( $szFirstLine );

                
                if ( $szFirstLine eq "C" ) {
                    $nUorC *= -1;
                }

                if ( $nUorC == -1 ) {
                    print filAceFileNew "C\n";
                }
                else {
                    print filAceFileNew "U\n";
                }
            }
            elsif ( $szTagType eq "cloneEnd" ) {
                
                my $szFirstLine = shift @aAdditionalLines;

                chomp( $szFirstLine );

                # example:
                #CT{
                #Contig1 cloneEnd consed 1 1 011119:145925
                #->
                #}

                if ( $bHadToBeComplemented ) {
                    if ( $szFirstLine eq "->" ) {
                        $szFirstLine = "<-";
                    }
                    else {
                        $szFirstLine = "->";
                    }
                }
                else {
                    # just leave $szFirstLine the way it is.
                }
                
                print filAceFileNew "$szFirstLine\n";
            }
            elsif( $szTagType eq "contigEndPair" && $bHadToBeComplemented ) {
              # I don't see any reason to do this parsing, unless
              # we have to change the gap-> pointer

              # get the contigEndPair ID
              my $szFirstLine = shift @aAdditionalLines;
              print filAceFileNew $szFirstLine;
              my $szSecondLine = shift @aAdditionalLines;
              chomp( $szSecondLine );
              if ( $szSecondLine eq "gap->" ) {
                $szSecondLine = "<-gap";
              }
              elsif( $szSecondLine eq "<-gap" ) {
                $szSecondLine = "gap->";
              }
              else {
                $szSecondLine = "gap???";
              }

              print filAceFileNew "$szSecondLine\n";
            }
              


            foreach $szAdditionalLine (@aAdditionalLines ) {
                print filAceFileNew $szAdditionalLine;
            }
            print filAceFileNew "}\n";

        }

    }
}

close( filAceFileOld );
close( filAceFileNew );
close( filErrLog );


if ($bThereWereErrors ) {
    print "\n\nThere were some consensus tags that could not be transferred.\n";
    print "See $szErrorFile for details.\n";
}
else {
    print "All consensus tags were successfully transferred.\n";
}

print "Successfully transferred consensus tags are now in the new ace file: $szAceFileNew\n";

exit;



# subroutines



            
            







# sub numerically { $a <=> $b; }

# @aContigs = sort keys( %unpaddedFromPaddedOld );

# foreach $szContig ( @aContigs ) {
#     print "$szContig\n";
#     $refArray = $unpaddedFromPaddedOld{ $szContig };
#     foreach $szPadded ( @$refArray ) {
#         print "$szPadded ";
#     }
#     print "\n\n";
# }



# while(1) {
#     print "Convert unpadded to padded for which contig:  ";
#     $szContig = <stdin>;
#     chomp( $szContig );
#     print "Give unpadded position:  ";
#     $szUnpaddedPos = <stdin>;
#     chomp( $szUnpaddedPos );
#     $nUnpaddedPos = $szUnpaddedPos;

#     $nLocalPadded = &nPaddedFromUnpaddedNew( $szContig, $nUnpaddedPos );

#     print "padded pos = $nLocalPadded\n";
# }
# while(1) {
#     print "Convert padded to unpadded for which contig:  ";
#     $szContig = <stdin>;
#     chomp( $szContig );
#     print "Give padded position:  ";
#     $szPaddedPos = <stdin>;
#     chomp( $szPaddedPos );
#     $nPaddedPos = $szPaddedPos;

#     $nLocalUnpadded = &nUnpaddedFromPaddedOld( $szContig, $nPaddedPos );

#     print "unpadded pos = $nLocalUnpadded\n";
# }


            
sub nPaddedFromUnpaddedNew {
    die "usage: &nPaddedFromUnpaddedNew( <contigname>, <unpadded position> );" 
        if ( $#_ != 1 );

    $szContig = $_[0];
    $nUnpaddedPos = $_[1];

    if (!exists $paddedFromUnpaddedNew{ $szContig } ) {
        print "Contig $szContig doesn't exist";
        return -10000;
    }
    
    # now convert to padded:
    
    $refArray = $paddedFromUnpaddedNew{ $szContig };
    if ( $#$refArray == -1 ) {
        # case in which there are no pads
        $nNumberOfPadsBeforeBase = 0;
    }
    elsif ( $nUnpaddedPos <= $$refArray[ 0 ] ) {
        # base is before the first pad
        $nNumberOfPadsBeforeBase = 0;
    }
    else {
        $bFoundUnpaddedMax = 0;
        for( $nRange = 0; ($nRange <= $#$refArray) && !$bFoundUnpaddedMax;
            ++$nRange ) {
        
            $nUnpaddedTopOfRange = $$refArray[ $nRange ];
            if ( $nUnpaddedPos <= $nUnpaddedTopOfRange ) {
                $bFoundUnpaddedMax = 1;
                $nNumberOfPadsBeforeBase = $nRange;
                                #  the unpadded pos goes in the
                                #  previous range
            }
        }

        if (!$bFoundUnpaddedMax ) {
            # This is the case in which the unpadded pos is 
            # after the last pad.  So convert to padded just
            # by adding the total number of pads.

            $nNumberOfPadsBeforeBase = $#$refArray + 1;
                                # $#$refArray is the subscript of 
                                # the last pad, and the subscript of 
                                # the first pad is 0, hence the number
                                # of pads is $#$refArray + 1
        }
    }

    
    $nPaddedPos = $nUnpaddedPos + $nNumberOfPadsBeforeBase;

    return $nPaddedPos;
}
            
    

sub nUnpaddedFromPaddedOld {
    die "usage: &nUnpaddedFromPaddedOld( <contigname>, <padded position> );"
        if ( $#_ != 1 );


    $szContig = $_[0];
    $nPadded = $_[1];

    if (!exists $unpaddedFromPaddedOld{ $szContig } ) {
        print "Contig $szContig doesn't exist";
        return -10000;
    }

    # now convert to unpadded

    $refArray = $unpaddedFromPaddedOld{ $szContig };
    if ($#$refArray == -1 ) {
        # case in which there are no pads
        $nNumberOfPadsBeforeBase = 0;
    }
    elsif ($nPadded <= $$refArray[ 0 ] ) {
        # case in which the base is before the first pad
        $nNumberOfPadsBeforeBase = 0;
    }
    else {
        # find first range where the base is less than the top of the range
        $bFoundPaddedMax = 0;
        for( $nRange = 0; ( $nRange <= $#$refArray ) && !$bFoundPaddedMax;
            ++$nRange ) {

            $nPaddedTopOfRange = $$refArray[ $nRange ];
            if ( $nPadded <= $nPaddedTopOfRange ) {
                $bFoundPaddedMax = 1;
                $nNumberOfPadsBeforeBase = $nRange;
            }
        }

        if (!$bFoundPaddedMax ) {
            # This is the case in which the base is after the last pad.
            # So convert to unpadded by subtracting the total number of pads.
            
            $nNumberOfPadsBeforeBase = $#$refArray + 1;
                                # $#$refArray is the subscript of 
                                # the last pad, and the subscript of 
                                # the first pad is 0, hence the number
                                # of pads is $#$refArray + 1
        }
    }

    $nUnpaddedPos = $nPadded - $nNumberOfPadsBeforeBase;
    return $nUnpaddedPos;
}




sub constructPadsTable {
    ( $szAceFile, $szPadsTable ) = @_;
# INPUT:  consed new_ace file 
# OUTPUT: table of pad positions


    
    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
    open( filPads, ">$szPadsTable" ) || die "couldn't open $szPadsTable for writing";

    while( <filAce>) {
        if ( length( $_ ) > 3 ) {
            if ( substr( $_, 0, 3 ) eq "CO " ) {
                # found a CO line
                @aWords = split;
                $szContigName = $aWords[1];

                print( filPads  ">$szContigName" );
                
                $nPaddedPos = 0;
                $nNumberOfPads = 0;
                while( <filAce>) {
                    last if ( length( $_ ) == 1 );
                    chop;

                    # do an efficient check in case someone
                    # has edited the ace file and put 
                    # some whitespace on the line
                    if ( (substr( $_, 0, 1 ) eq " ") ||
                        (substr( $_, 0, 1 ) eq "\t" )) {
                        last if ( $_ =~ /^\s*$/ );
                    }
                    for( $nPosOnLine = 0; $nPosOnLine < length( $_ ); ++$nPosOnLine ) {
                        ++$nPaddedPos;
                        if ( substr( $_, $nPosOnLine, 1 ) eq "*" ) {
                            ++$nNumberOfPads;
                            if ( ($nNumberOfPads % 10) == 1 ) {
                                print( filPads  "\n$nPaddedPos" );
                            }
                            else {
                                print( filPads  " $nPaddedPos" );
                            }
                        }
                    }
                }

                # end of that contig
                print( filPads  "\n\n" );
            }
        }
    }
    close( filAce );
    close( filPads );
}


    
sub constructPadsTableAceFormat1 {
    ( $szAceFile, $szPadsTable, %aContigs ) = @_;
# INPUT:  consed new_ace file 
# OUTPUT: table of pad positions

    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
    open( filPads, ">$szPadsTable" ) || die "couldn't open $szPadsTable for writing";

    while( <filAce>) {
        if ( length( $_ ) > 4 ) {
            if ( substr( $_, 0, 4 ) eq "DNA " ) {
                # found a DNA line.  Check if it is for a contig.
                @aWords = split;
                $szContigName = $aWords[1];

                if ( !exists( $aContigs{ $szContigName } )) {
                    next;
                }

                print( filPads  ">$szContigName" );
                
                $nPaddedPos = 0;
                $nNumberOfPads = 0;
                while( <filAce>) {
                    last if ( length( $_ ) == 1 );
                    chop;

                    # do an efficient check in case someone
                    # has edited the ace file and put 
                    # some whitespace on the last line
                    if ( (substr( $_, 0, 1 ) eq " ") ||
                        (substr( $_, 0, 1 ) eq "\t" )) {
                        last if ( $_ =~ /^\s*$/ );
                    }
                    for( $nPosOnLine = 0; $nPosOnLine < length( $_ ); ++$nPosOnLine ) {
                        ++$nPaddedPos;
                        if ( substr( $_, $nPosOnLine, 1 ) eq "*" ) {
                            ++$nNumberOfPads;
                            if ( ($nNumberOfPads % 10) == 1 ) {
                                print( filPads  "\n$nPaddedPos" );
                            }
                            else {
                                print( filPads  " $nPaddedPos" );
                            }
                        }
                    }
                }

                # end of that contig
                print( filPads  "\n\n" );
            }
        }
    }
    close( filAce );
    close( filPads );
}


    
sub constructContigsFile {
    ( $szAceFile, $szContigsFile ) = @_;

    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
    open( filContigs, ">$szContigsFile" ) || 
        die "couldn't open $szContigsFile for writing";

    while( <filAce>) {
        if ( length( $_ ) > 3 ) {
            if ( substr( $_, 0, 3 ) eq "CO " ) {
                # found a CO line
                @aWords = split;
                $szContigName = $aWords[1];

                print( filContigs ">$szContigName\n" );
            
                while( <filAce>) {
                    last if ( length( $_ ) == 1 );

                    # do an efficient check in case someone
                    # has edited the ace file and put 
                    # some whitespace on the line
                    if ( (substr( $_, 0, 1 ) eq " ") ||
                        (substr( $_, 0, 1 ) eq "\t" )) {
                        last if ( $_ =~ /^\s*$/ );
                    }
                    # remove any pads
                    s/\*//g;
                    print( filContigs $_ );
                }
                
                # end of that contig
                print( filContigs "\n" );
            }
        }
    }
    close( filAce );
    close( filContigs );
}


 


sub constructContigsFileAceFormat1 {
    ( $szAceFile, $szContigsFile, %aContigs ) = @_;

    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
    open( filContigs, ">$szContigsFile" ) || 
        die "couldn't open $szContigsFile for writing";

    
    while( <filAce>) {
        if ( length( $_ ) > 4 ) {
            if ( substr( $_, 0, 4 ) eq "DNA " ) {
                # found a DNA line.  See if it is for a contig
                @aWords = split;
                $szContigName = $aWords[1];
                if ( !exists( $aContigs{ $szContigName } ) ) {
                    next;
                }

                print( filContigs ">$szContigName\n" );
            
                while( <filAce>) {
                    last if ( length( $_ ) == 1 );

                    # do an efficient check in case someone
                    # has edited the ace file and put 
                    # some whitespace on the last line
                    if ( (substr( $_, 0, 1 ) eq " ") ||
                        (substr( $_, 0, 1 ) eq "\t" )) {
                        last if ( $_ =~ /^\s*$/ );
                    }
                    # remove any pads
                    s/\*//g;
                    print( filContigs $_ );
                }
                
                # end of that contig
                print( filContigs "\n" );
            }
        }
    }
    close( filAce );
    close( filContigs );
}


 


sub readAndParseCrossMatchOutput {
    open( filCrossMatchOutput, $szCrossMatchOutputFilename ) || 
        die "couldn't open cross match output file $szCrossMatchOutputFilename";

    open( filUnpaddedNewFromUnpaddedOld, 
         ">$szUnpaddedNewFromUnpaddedOldTable" ) ||
             die "couldn't open $szUnpaddedNewFromUnpaddedOldTable for writing";


    $bFoundAlignmentLine = 0;
    while( <filCrossMatchOutput> ) {
        next if ( $_ !~ /^ALIGNMENT/ );
        $bFoundAlignmentLine = 1;
        last;
    }

    # handle case in which there are no alignments at all between the old
    # and new assemblies    
    if (!$bFoundAlignmentLine ) {
        die "couldn't find any alignments";
    }

    &parseAlignmentLine;
    
    # now it is possible to find either an ALIGNMENT line or a DISCREPANCY 
    # line

    while( <filCrossMatchOutput> ) {
        chomp;
        $bIdentifiedLine = 0;
        if ( $_ =~ /^ALIGNMENT/ ) {
            $bIdentifiedLine = 1;
            &print_leftover_alignment;
            &parseAlignmentLine;
        }

        if (!$bIdentifiedLine ) {
            if ( $_ =~ /^DISCREPANCY/ ) {
                $bIdentifiedLine = 1;
                &handleDiscrepancyLine;
            }
        }
    }
    
    &print_leftover_alignment;

    close( filCrossMatchOutput );
    close( filUnpaddedNewFromUnpaddedOld );
    unlink( $szCrossMatchOutputFilename );
}

    
# Typical examples are:
# DISCREPANCY   D-3   160  T(-1)    240  atccctTtgctcc
# DISCREPANCY   S     335  C(-1)     63  tttgaaCggcact
# DISCREPANCY   I-2    28  TT(0)    425  ggcacgTTgttggc
# DISCREPANCY   D     588  A(-1)    987  aaatggAtataga
# The meaning of these are as follows:
#  D means take right sequence (on command line), make deletion, and
#    the result is the left sequence on command line
#    In this case the sequence listed on the line "aaatggAtataga" 
#    is the left sequence.
#    (For purposes of transferConsensusTags.perl, the left sequence
#    on the command line is the old consensus and the right sequence
#    on the command line is the new consensus.)
# The first number "588" indicates the position in the left sequence
# on the command line.  The second number "987" indicates the 
# the position in the right sequence on the command line.  The 
# 987 is the position of the base that will be deleted.  The 588
# is the position of the base just before the deletion (where 'before' is
# in terms of the left sequence).
# If complemented sequence, the positions are still numbered with respect
# to the beginning (top left) of the sequence--not the end.
# D-3 means that there are 3 deleted bases.  I-2 means two inserted
# bases.




sub handleDiscrepancyLine {

    @aWords = split;
    
    $szTemp = $aWords[1];
    ( length( $szTemp ) > 0 ) || die "2nd token in DISCREPANCY line $_ must be at least 1 character long";


    $cSDI = substr( $szTemp, 0, 1 );

    if ( length( $szTemp ) == 1 ) {
        $nRepeat = 1;
    }
    else {
        ( length( $szTemp ) >= 3 ) || die "2nd token in DISCREPANCY line must be 1 or 3 or more characters";
        ( substr( $szTemp, 1, 1 ) eq "-" ) || die "2nd token in DISCREPANCY line $_ is unrecognized";
        $nRepeat = substr( $szTemp, 2 );
        ( $nRepeat =~ /^\d+$/ ) || die "2nd token in DISCREPANCY line $_ must be D-(digits) or I-(digits)";
    }


    $nOldPos = $aWords[2];
    ( $nOldPos =~ /^-?\d+$/ ) || die "3rd token in DISCREPANCY line $_ must be a number";
    $nNewPos = $aWords[4];
    ( $nNewPos =~ /^-?\d+$/ ) || die "5th token in DISCREPANCY line $_ must be a number";

    # now split the old chunk with this discrepancy
    

    # This check used to be:
    #( ($nOldAlignLeft <= $nOldPos) &&
#      ($nOldPos <= $nOldAlignRight)  ) || die "the discrepancy $_ must lie within the alignment which goes from $nOldAlignLeft to $nOldAlignRight";
    # but there was the possibility of an S line and a D line both with
    # the same base, such as aaaccaT tatgga
    #                        aaaccaGccatgga
    # in this case $nOldAlignLeft would first be set to the t after the
    # T, and then the next discrepancy would be a D discrepancy pointing
    # again to the T
    # Actually, this example wouldn't cause this problem--the problem
    # only occurs if the second sequence is complemented with respect
    # to the first because if not complemented, cross_match will do this:
    #                        aaacca Ttatgga
    #                        aaaccaGccatgga
    # in which case it will give first a D line pointing to the 'a' and
    # then an S line pointing to the T and then an S line pointing to the
    # 't'.
    # The following will demonstrate the problem:
    # ACCCAGGAAAACCATTATGGACCCCGGGCCGATGGGGTCCTTGAGCACAAACAACAAGTTCCAACCCAGGA
    # for the left sequence and
    # TCCTGGGTTGGAACTTGTTGTTTGTGCTCAAGGACCCCATCGGCCCGGGGTCCATGGCTG
    # GTTTTCCTGGGT
    # for the right sequence

    ( (($nOldAlignLeft - 1) <= $nOldPos) &&
      ($nOldPos <= $nOldAlignRight)  ) || die "the discrepancy $_ must lie within the alignment which goes from $nOldAlignLeft to $nOldAlignRight";

    $nOldChunkletStart = $nOldAlignLeft;
    $nNewChunkletStart = $nNewAlignLeft;
    # should check with complementing in these cases, also
    if ( $cSDI eq "I" ) {
        # $nOldPos is then the position of the inserted base in the old contig
        # $nNewPos is the position of the base to the left of the insertion
        # pos in the new contig.  This is true whether complemented or not.
        $nOldChunkletEnd = $nOldPos - 1;
        $nNewChunkletEnd = $nNewPos;
        # adjust rest of alignment
        $nOldAlignLeft = $nOldPos + $nRepeat;
        if ($cCompFlag eq "U") {
            $nNewAlignLeft = $nNewPos + 1;
        }
        else {
            $nNewAlignLeft = $nNewPos - 1;
        }
    }
    elsif ( $cSDI eq "D" ) {
        $nOldChunkletEnd = $nOldPos;
        
        $nOldAlignLeft = $nOldPos + 1;
        if ($cCompFlag eq "U" ) {
            $nNewChunkletEnd = $nNewPos - 1;
            $nNewAlignLeft = $nNewPos + $nRepeat;
        }
        else {
            $nNewChunkletEnd = $nNewPos + 1;
            $nNewAlignLeft = $nNewPos - $nRepeat;
        }
    }
    elsif ( $cSDI eq "S" ) {
        $nOldChunkletEnd = $nOldPos - 1;
        $nOldAlignLeft = $nOldPos + 1;
        if ($cCompFlag eq "U" ) {
            $nNewChunkletEnd = $nNewPos - 1;
            # skip over the substituted base
            $nNewAlignLeft = $nNewPos + 1;
        }
        else {
            $nNewChunkletEnd = $nNewPos + 1;
            # skip over the substituted base
            $nNewAlignLeft = $nNewPos - 1;
        }
    }
    else {
        die "DISCREPANCY line not recognized: $_";
    }

    &print_chunklet;
}




sub print_chunklet {
    # handle the case in which there are 2 substitutions in a row,
    # leaving a chunklet that is of length < 0
    return if ( $nOldChunkletStart > $nOldChunkletEnd );

    print( filUnpaddedNewFromUnpaddedOld 
        "L $szOldContigName $nOldChunkletStart $nOldChunkletEnd $cCompFlag $szNewContigName $nNewChunkletStart $nNewChunkletEnd\n" );
}




sub print_leftover_alignment {    
    # write whatever is remaining of the last alignment
    print( filUnpaddedNewFromUnpaddedOld
          "L $szOldContigName $nOldAlignLeft $nOldAlignRight $cCompFlag $szNewContigName $nNewAlignLeft $nNewAlignRight\n" );
}

sub parseAlignmentLine {
    
# if reached here, the first token is ALIGNMENT
# Typical examples:
# uncomplemented:
# ALIGNMENT   772  0.49 0.37 0.61  Contig2        5   822 (0)    NewContig2      403 1218 (0)  
# complemented:
# ALIGNMENT   382  0.25 0.76 0.00  Contig1        1   397 (248)  C NewContig2   (818)   400     1  

    @aWords = split;
    $szOldContigName = $aWords[5];
    $nOldAlignLeft = $aWords[6];
    $nOldAlignRight = $aWords[7];
    $cCompFlag = $aWords[9];
    if ( $cCompFlag eq "C" ) {
        
        $szNewContigName = $aWords[10];
        $nNewAlignLeft = $aWords[12];
        $nNewAlignRight = $aWords[13];
    }
    else {
        $cCompFlag = "U";
        $szNewContigName = $aWords[9];
        $nNewAlignLeft = $aWords[10];
        $nNewAlignRight = $aWords[11];
    }

    print( filUnpaddedNewFromUnpaddedOld
          "B $szOldContigName $nOldAlignLeft $nOldAlignRight $cCompFlag $szNewContigName $nNewAlignLeft $nNewAlignRight\n" );
}
    
        




sub oldConsensusTagCanBeTransferredToNewAssembly {

    my ( $szContigName, $nUnpaddedNewStart, $nUnpaddedNewEnd );
    my ( $szOldContig, $nUnpaddedOldStart, $nUnpaddedOldEnd ) = @_;

    my $bFound = 0;
    my $nStartIndex;
    for( $n = 0; $n <= $#aBigAndLittleChunks; ++$n ) {
      $szChunk = $aBigAndLittleChunks[ $n ];
      if ( substr( $szChunk, 0, 2 ) eq "B " ) {
        my ( $szBorL, 
             $szChunkOldContig, 
             $nChunkOldAlignLeft, 
             $nChunkOldAlignRight,
             $cChunkCompFlag,
             $szChunkNewContigName,
             $nChunkNewAlignLeft,
             $nChunkNewAlignRight ) =
               split( ' ', $szChunk );
        
        if ( $szChunkOldContig eq $szOldContig ) {
          if ( ( $nChunkOldAlignLeft <= $nUnpaddedOldStart ) &&
               ( $nUnpaddedOldEnd <= $nChunkOldAlignRight ) ) {
            $bFound = 1;
            $nStartIndex = $n + 1; # start on the next line
            # since it will be a chunklet ("L" line ) for this chunk
            # "B" line 
            last;
          }
        }
      } # if ( substr( $szChunk, 0, 2 ) eq "B " )  
    } # for( $n = 0 ...
          

    if ( !$bFound ) {
        return( ( 0, "", 0, 0, ) );
    }

    
    # so our consensus tag is nicely found within an alignment block
    # now we just need to transfer the positions

    my $cLocalCompFlag1;
    ( $szNewContigName, $nUnpaddedNewStart, $cLocalCompFlag1 ) = 
        &newUnpaddedFromOldUnpadded( $szOldContig, $nUnpaddedOldStart, 
                                   $nStartIndex );

    my $cLocalCompFlag2;

    ( $szNewContigName2, $nUnpaddedNewEnd, $cLocalCompFlag2 ) =
        &newUnpaddedFromOldUnpadded( $szOldContig, $nUnpaddedOldEnd,
                                   $nStartIndex );

    
    die "Different parts of the tags should go to the same contig $szNewContigName but the end is going to $szNewContigName2" 
        if ( $szNewContigName ne $szNewContigName2 );


    # there are several situations in which the tag positions could
    # get out of order:  1) if the new assembly is complemented with
    # respect to the old and 2) if there are lots of inserted bases
    # in the old assembly that have no corresponding bases in the 
    # new assembly and the tag has an endpoint on one of those bases

    # using $nUnpaddedNewStart > $nUnpaddedNewEnd is more reliable than
    # $ $cLocalCompFlag1 and $cLocalCompFlag2 because it is theoretically
    # possible to have some local complementing, but the overall large
    # picture stays the same--the ends of the tag are still in the same 
    # order.  

    my $bComplemented = 0;
    if ( $nUnpaddedNewStart > $nUnpaddedNewEnd ) {
        my $nTemp = $nUnpaddedNewStart;
        $nUnpaddedNewStart = $nUnpaddedNewEnd;
        $nUnpaddedNewEnd = $nTemp;
        $bComplemented = 1;
    }
    elsif ( $nUnpaddedNewStart == $nUnpaddedNewEnd ) {
      if ( ( $cLocalCompFlag1 eq 'C' ) && ( $cLocalCompFlag2 eq 'C' )) {
        # this is used especially for autofinish tags, which
        # are one base and thus the $nUnpaddedNewStart > $nUnpaddedNewEnd
        # test is useless for determining whether the tag should be
        # complemented

        $bComplemented = 1;
      }
    }


    return( ( 1, $szNewContigName, $nUnpaddedNewStart, $nUnpaddedNewEnd, 
            $bComplemented ) );
}

            


sub readParsedCrossMatchOutput {


# Build table of alignments and subsections of alignments

    open( filUnpaddedNewFromUnpaddedOld, $szUnpaddedNewFromUnpaddedOldTable)
    || die "Couldn't open $szUnpaddedNewFromUnpaddedOldTable";

    @aBigAndLittleChunks = ();

    while( <filUnpaddedNewFromUnpaddedOld> ) {
        chop;
        push( @aBigAndLittleChunks, $_ );
    }

    close( filUnpaddedNewFromUnpaddedOld );
}



sub newUnpaddedFromOldUnpadded {
    
    # returns ( $szNewContig, $nNewUnpadded )
    my ( $szOldContig, $nOldUnpadded, $nStartIndex ) = @_;

    my $bFound = 0;
    my $szPreviousChunklet;
    my $szUseChunklet;
    my $n;

    for( $n = $nStartIndex; $n <= $#aBigAndLittleChunks; ++$n ) {
        $szChunklet = $aBigAndLittleChunks[ $n ];
        my ( $szBorL, 
             $szChunkletOldContigName, 
             $nChunkletOldStart, 
             $nChunkletOldEnd, $cCompFlag,
             $szChunkletNewContigName, $nChunkletNewStart,
             $nChunkletNewEnd ) = 
               split( ' ', $szChunklet );

        die "No L line found for tag from old contig $szOldContig unpadded $nOldUnpadded at line $n starting at $nStartIndex in file  $szUnpaddedNewFromUnpaddedOldTable" if ( $szBorL ne "L" );

        die "Looking for $szOldContig unpadded $nOldUnpadded from $nStartIndex but found $szChunkletOldContigName at line $n in $szUnpaddedNewFromUnpaddedOldTable" if ( $szOldContig ne $szChunkletOldContigName );

        # if reached here, this is an L line with the same contig name

        if ( $nOldUnpadded < $nChunkletOldStart ) {
          # this means we have just passed the needed chunklet
          # since there was a gap between chunklets and $nOldUnpadded
          # fell into the gap

          $bFound = 1;
          $szUseChunklet = $szPreviousChunklet;
          last;
        }

        if ( ( $nChunkletOldStart <= $nOldUnpadded ) &&
             ( $nOldUnpadded <= $nChunkletOldEnd ) ) {
          # normal case--$nOldUnpadded lies nicely within a chunklet
          $bFound = 1;   
          $szUseChunklet = $szChunklet;
          last;
        }

        $szPreviousChunklet = $szChunklet;
    }

    die "something wrong if couldn't find chunklet" if (!$bFound );

    
    my ( $szBorL, 
         $szChunkletOldContigName, 
         $nChunkletOldStart, 
         $nChunkletOldEnd, 
         $cCompFlag,
         $szChunkletNewContigName, 
         $nChunkletNewStart,
         $nChunkletNewEnd ) = 
           split( ' ', $szUseChunklet );

    
    my $nAmountToAdd = $nOldUnpadded - $nChunkletOldStart;

    if ( $cCompFlag eq "C" ) {
        $nAmountToAdd = -$nAmountToAdd;
    }
                
    $nUnpaddedNew = $nChunkletNewStart + $nAmountToAdd;
    $szNewContigName  = $szChunkletNewContigName;
    
    return( ( $szNewContigName, $nUnpaddedNew, $cCompFlag ) );
}




sub buildUnpaddedFromPaddedOldTable {
# Build the unpaddedFromPadded and paddedFromUnpadded tables


    %unpaddedFromPaddedOld = ();

    open( filPadsTableOld, "$szPadsTableOld" ) || 
        die "Couldn't open old pads table $szPadsTableOld";

    while( <filPadsTableOld> ) {
        chomp;
        if ( length( $_ ) > 1 ) {
            if ( substr( $_, 0, 1 ) eq ">" ) {
                # found a start of a contig
                # skip over the ">" and take the rest
                $szContig = substr( $_, 1 );                        


                # create an anonymous array with []
                # assign this reference to both 
                # $refArray and $unpaddedFromPaddedOld{ $szContig }
                $refArray = $unpaddedFromPaddedOld{ $szContig } = [];

                while( <filPadsTableOld> ) {
                    chomp;
                    last if ( length( $_ ) == 0 );
                    @aPadPositions = split;
                    push( @$refArray, @aPadPositions );
                }
            }
        }
    }

    close( filPadsTableOld );
    # now that we have this in memory, there is no longer a need for the file
    unlink( $szPadsTableOld );
}





sub buildPaddedFromUnpaddedNewTable {
# now deal with pads in new assembly



    %paddedFromUnpaddedNew = ();


    open( filPadsTableNew, "$szPadsTableNew" ) ||
        die "Couldn't open new pads table $szPadsTableNew";

    while( <filPadsTableNew> ) {
        chop;
        if ( length( $_ ) > 1 ) {
            if ( substr( $_, 0, 1 ) eq ">" ) {
                # found a start of a contig
                # skip over the ">" and take the contig name                
                $szContig = substr( $_, 1 );
                
                $refArray = $paddedFromUnpaddedNew{ $szContig } = [];
                
                while( <filPadsTableNew> ) {
                    chop;
                    last if ( length( $_ ) == 0 );
                    @aPadPositions = split;
                    push( @$refArray, @aPadPositions );
                }

                # now we have finished collecting the pads for this contig
                # convert to the unpadded array by subtracting 
                # [0] subtract 1
                # [1] subtract 2
                # etc.
                # Thus we will have the upper bound in unpadded positions 
                # that a particular range is useful for.  Alternatively, this
                # gives the unpadded position base just before each pad.  Hence
                # to find the padded position, find the largest element such 
                # that the unpadded pos is <= the value of the element.  
                # Then add the number of pads before that element.  This will
                # be the padded position.  You can easily find the number of
                # pads before the unpadded pos since it will be the index of the 
                # element.

                for( $nPos = 0; $nPos <= $#$refArray; ++$nPos ) {
                    $$refArray[ $nPos ] -= ($nPos + 1 );
                }
            }
        }
    }

    close( filPadsTableNew );
    unlink( $szPadsTableNew );
}



sub bDetermineAceFileFormat {
    ( $szAceFile ) = @_;

    my %aContigs = ();

    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
    $szFirstLine = <filAce> || die "0 length file $szAceFile";

    if ( length( $szFirstLine ) > 3 ) {
        if (substr( $szFirstLine, 0, 3 ) eq "AS " ) {
            return( (2, %aContigs ) );
        }
    }
    
    seek( filAce, 0, 0 );  # reposition to beginning of file

    my @aListOfContigsAndReads = ();
    my %aListOfReads = ();

    while( <filAce> ) {
        if ( length($_) >= 4 ) {
            if ( substr( $_, 0, 4 ) eq "DNA " ) {
                @aWords = split;
                push( @aListOfContigsAndReads, $aWords[1] );
            }
            elsif( length( $_ ) >= 16 ) {
                if ( substr( $_, 0, 16 ) eq "Assembled_from* " ) {
                    @aWords = split;
                    $aListOfReads{$aWords[1] } = "";
                }
            }
        }
    }

    foreach $szContigOrRead (@aListOfContigsAndReads ) {
        if ( !exists( $aListOfReads{ $szContigOrRead } ) ) {
            $aContigs{ $szContigOrRead } = "";
        }
    }

    return( (1, %aContigs ) );
}



sub constructTableOfOldContigsComplemented {

# this funny business is to pass a pointer to the associative array %aContigs
# and then modify the associative array

    ( $szAceFile, $refaContigs ) = @_;

    %$refaContigs = ();

    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";

    # look for lines that look like this:
    # CO Contig1 2702 33 1067 U
    # or this:
    # CO Contig1 2702 33 1067 C
    

    while( <filAce> ) {
        if ( length( $_ ) > 3 ) {
            if ( substr( $_, 0, 3 ) eq "CO " ) {
                @aWords = split;
                # I can't think of how this could happen,
                # but let's handle it.
                if ( $#aWords < 5 ) {
                    next;
                }
                $szContig = $aWords[1];
                $cCorU = $aWords[5];
                if ( $cCorU eq "C" ) {
                    $$refaContigs{ $szContig } = "C";
                }
                elsif ($cCorU eq "U" ) {
                    $$refaContigs{ $szContig } = "U";
                }
                else {
                    # perhaps this was a line that started with
                    # "CO " but wasn't a CO line
                    next;
                }
            }
        }
    }

    close( filAce );
}

            


