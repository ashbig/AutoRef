#!/usr/bin/perl -w
#
#   Purpose:  called by consed in order to add new reads to an
#       existing assembly without reassemblying
#
#   How to Use It:  
#       addReads2Consed.perl (name of existing ace file) (name of fof of reads)
#
#
#   Note:  If you attempt to modify this script, you are on your own
#          If you attempt to use portions of this script for other
#          purposes, you are on your own
#
#   How it works:
#          It will call phred to create phd files for all the files in
#          the phd directory    
#
#   What to customize:
#          You must change $szCrossMatchExe, $szPhredExe, $szPhd2FastaExe
#          and #!/usr/bin/perl (at the top) to reflect where these
#          executables are on your system.
#   
#   Rev: 980817 (David Gordon)
#   Rev: 980818 (David Gordon) fix warnings for perl 5.004_04
#   Rev: 981002 (David Gordon) fix for labs using compressed chromats
#   Rev: 020813 (Jim Sloan) to optionally be used with Polyphred
#   Rev: 021121 (Don Bovee) to handle case in which there is a phd
#                     file but no chromat


$szVersion = "021121";
if ( $#ARGV >= 0 ) {
  if ( $ARGV[0] eq "-V" || $ARGV[0] eq "-v" ) {
    print "$szVersion\n";
    exit( 1 );
  }
}

$szUsage = "Usage:  addReads2Consed.perl (name of existing ace file) (name of fof of reads) (name of file of alignments)";
$szCrossMatchExe = "d:/programs_bio/assembler/exe/cross_match";

#$szPhredExe = $szConsedHome . "/bin/phred";
$szPhredExe = "d:/programs_bio/assembler/exe/phred";

#$szPhd2FastaExe = $szConsedHome . "/bin/phd2fasta";
$szPhd2FastaExe = "d:/programs_bio/assembler/exe/phd2fasta";

#$szDetermineReadTypes = $szConsedHome . "/bin/determineReadTypes.perl";
$szDetermineReadTypes = "d:/programs_bio/assembler/scripts/determineReadTypes.perl";

$szPHDDir = "../phd_dir";
$szChromatDir = "../chromat_dir";

# PolyPhred users need to change the 0 in the next line to 1
$bUsingPolyPhred = 0;

if ( $bUsingPolyPhred ) {
  $szPolyPhredOption = "-dd ../poly_dir";
}
else {
  $szPolyPhredOption = "";
}

die "$szUsage" if ( $#ARGV != 2 );

$szAceFile = $ARGV[0];
$szNewReadsFOFOriginal = $ARGV[1];
$szAlignmentsFile = $ARGV[2];

# change this to reflect wherever you put you fasta file of vector sequences
$szVectorFile = "d:/programs_bio/assembler/vectors/vector.seq";

# change this to reflect wherever you put the phred parameter file
$szPhredParameterFile = "d:/programs_bio/assembler/scripts/phredpar.dat";
#$szPhredParameterFile = "/usr/local/common/lib/PhredPar/phredpar.dat";
#$szPhredParameterFile = "/usr/local/etc/PhredPar/phredpar.dat";



die "cannot find $szVectorFile specifying the pathname of the vector sequences file" if (! -e $szVectorFile );

die "Can't read $szAceFile" if (! -r $szAceFile );
die "Can't read $szNewReadsFOFOriginal" if (! -r $szNewReadsFOFOriginal );

die "Can't execute $szCrossMatchExe" if (! -x $szCrossMatchExe );
die "Can't execute $szPhredExe" if (! -x $szPhredExe );


die "could not read $szPhredParameterFile" if  (!-r $szPhredParameterFile );


$ENV{'PHRED_PARAMETER_FILE'} = $szPhredParameterFile;

die "Can't execute $szDetermineReadTypes" if (! -x $szDetermineReadTypes );


if ( -e $szAlignmentsFile ) {
  unlink( $szAlignmentsFile );
}


($szUniqueChars = $szAlignmentsFile ) =~ s/^addReadsAlignments//;


$szNewReadsFOF = $szNewReadsFOFOriginal . $szUniqueChars;

&transferReadsToNewFOFFromOriginal( $szNewReadsFOF, $szNewReadsFOFOriginal );

&phredNewReads( $szNewReadsFOF );

print "\n\n--------------------------------------------------------\n";
print "Now running determineReadTypes.perl...\n";
print "--------------------------------------------------------\n\n\n";

!system( "$szDetermineReadTypes" ) || die "some problem running determineReadTypes.perl\n";

$szFastaFile = "newReads" . $szUniqueChars . ".fasta";

&makeFastaFile( $szFastaFile, $szNewReadsFOF );

$szScreenFile = $szFastaFile . ".screen";

&crossMatchAgainstVector( $szScreenFile, $szFastaFile );


# now constructing contigs file

($nFormatOfAceFile, %aContigs )  = &bDetermineAceFileFormat( $szAceFile );

$szAceFileRoot = $szAceFile;
$szAceFileRoot =~ s/\.fasta\.screen\..*$//;

# adding the $$ just to make the filename unique in case this is 
# run more than once at the same time or is run more once, crashes,
# and then someone tries to run it again
$szContigsFile = $szAceFileRoot . $szUniqueChars . ".contigs";

if ( $nFormatOfAceFile == 2 ) {
  print "using ace file format 2 to construct contigs\n";
  &constructContigsFile( $szAceFile, $szContigsFile );
}
else {
  # if using the old ace format, it helps to know in advance which
  # DNA lines are contigs
  &constructContigsFileAceFormat1( $szAceFile, $szContigsFile, %aContigs );
}

&runCrossMatchBetweenNewReadsAndContigs( $szScreenFile, $szContigsFile );

&cleanUp();


# normal exit
exit(0);



    
sub constructContigsFile {
    ( $szAceFile, $szContigsFile ) = @_;

    open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
    open( filContigs, ">$szContigsFile" ) || 
        die "couldn't open $szContigsFile for writing";

    while(<filAce>) {
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


# Purpose:  strip .exp extension, if it is there
#   Check that chromat files exist in ../chromat_dir
sub transferReadsToNewFOFFromOriginal {
  my ( $szNewReadsFOF, $szNewReadsFOFOriginal ) = @_;

  open( filFOFOriginal, $szNewReadsFOFOriginal ) || die "could not open $szNewReadsFOFOriginal";
  open( filFOFNew, ">$szNewReadsFOF" ) || die "could not open $szNewReadsFOF for write--do you have write access to this directory?";

  while( <filFOFOriginal> ) {
    chomp;
    s/\.exp$//;
    my $szChromat = $_;
    my $szChromatPath = $szChromatDir . "/" . $szChromat;
    my $szPhredPath = $szPHDDir . "/" . $szChromat;

    if ( ! -r $szChromatPath ) {
      my $szGZ = $szChromatPath . ".gz";
      my $szZ = $szChromatPath . ".Z";
      my $szPhd = $szPhredPath . ".phd.1";

      if ( ( ! -r $szGZ ) && ( ! -r $szZ ) && ( ! -r $szPhd ) ) {
         die "Could not find chromat or phred file $szChromatPath--sorry" if (! -r $szChromatPath );
      }
    }

    print filFOFNew "$szChromat\n";
  }

  close( filFOFOriginal );
  close( filFOFNew );
}


  


sub phredNewReads {

  my ( $szNewReadsFOF ) = @_;

  
  # get list of all files in the phd directory in a hash so we can
  # easily see which files need to be phred'ed

  my %aExistingPHDFiles = ();
  
  opendir( dirPHD, $szPHDDir ) || die "couldn't open phd directory $szPHDDir";
  while( defined( $szPHDFile = readdir( dirPHD ) ) ) {
    next if ( $szPHDFile eq ".." );
    next if ( $szPHDFile eq "." );
    
    $aExistingPHDFiles{ $szPHDFile } = "";
  }
  closedir( dirPHD );

  

  $szChromatsToPhred = "chromatsToPhred" . $szUniqueChars . ".txt";
  open( filChromatsToPhred, ">$szChromatsToPhred" ) || die "couldn't open $szChromatsToPhred for writing";
  open( filNewReads, "$szNewReadsFOF" ) || die "couldn't open $szNewReadsFOF";
  
  my $nChromatsToPhred = 0;

  while( <filNewReads> ) {
    chomp;
    my $szNewChromat = $_;
    my $szPHDFile = $szNewChromat . ".phd.1";
    if (! exists( $aExistingPHDFiles{ $szPHDFile } ) ) {
      print filChromatsToPhred "$szChromatDir/$szNewChromat\n";
      ++$nChromatsToPhred;
    }
  }

  close( filChromatsToPhred );
  close( filNewReads );

  if ( $nChromatsToPhred > 0 ) {
    $szCommand = "$szPhredExe -if $szChromatsToPhred -pd $szPHDDir $szPolyPhredOption";

    print "running phred...\n";
    $nReturnedValue = system( $szCommand );
    die "could not run $szCommand\n" if ( $nReturnedValue != 0 );
    print "done running phred\n";
  }
}


  

  
  
  

sub bDetermineAceFileFormat() {
  
  ( $szAceFile ) = @_;

  my %aContigs = ();
  
  open( filAce, "$szAceFile" ) || die "couldn't open $szAceFile for reading";
  $szFirstLine = <filAce> || die "0 length file $szAceFile";
  
  if ( length( $szFirstLine ) > 3 ) {
    if ( substr( $szFirstLine, 0, 3 ) eq "AS " ) {
      # hurray--new ace format!
      # Thus it is easy to see which are contigs and which are reads
      
      return( ( 2, %aContigs ) );
    }
  }
  
  
  # so we are using old ace format.
  # Thus we need to make a list of contigs
  # We can tell which are contigs because they HAVE DNA lines
  # and DO NOT HAVE Assembled_from* lines
  
  seek( filAce, 0, 0 );         # reposition to beginning of file
  
  
  my @aListOfContigsAndReads = ();
  my %aListOfReads = {};
  
  while( <filAce> ) {
    if ( length( $_ ) >= 4 ) {
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
  
  # now subtract the lists:
  # @aListOfContigsAndReads - %aListOfReads = %aContigs
  
  foreach $szContigOrRead ( @aListOfContigsAndReads ) {
    if ( !exists( $aListOfReads{ $szContigOrRead } ) ) {
      $aContigs{ $szContigOrRead } = "";
    }
  }
  
  return( (1, %aContigs ) );
}






sub makeFastaFile() {

  ( $szFastaFile, $szNewReadsFOF ) = @_;

  open( filNewReadsFOF, $szNewReadsFOF ) || die "couldn't open $szNewReadsFOF for reading";

  $szPhdFilesToMakeFastaFile = "phdFilestoMakeFastaFile" . $szUniqueChars .
    ".fof";
  
  open( filPhdFilesToMakeFastaFile, ">$szPhdFilesToMakeFastaFile" ) ||
    die "couldn't open szPhdFilesToMakeFastaFile for write";

  while( <filNewReadsFOF> ) {
    chomp;
    my $szPhdFile = $szPHDDir . "/" . $_ . ".phd.1";
    print filPhdFilesToMakeFastaFile "${szPhdFile}\n";
  }

  close( filNewReadsFOF );
  close( filPhdFilesToMakeFastaFile );

  my $szCommand = "$szPhd2FastaExe -if $szPhdFilesToMakeFastaFile -os $szFastaFile";
  my $nReturnedValue = system( $szCommand );
  die "something wrong running $szCommand" if ( $nReturnedValue != 0 );
}


  


sub crossMatchAgainstVector {
  my ( $szScreenFile, $szFastaFile ) = @_;
  
  $szScreenOut = $szFastaFile . ".screen.out";

  my $szCommand = "$szCrossMatchExe $szFastaFile $szVectorFile -minmatch 12 -penalty -2 -minscore 20 -screen -tags >$szScreenOut";

  my $nReturnedValue = system( $szCommand );

  die "something wrong running $szCommand" if ( $nReturnedValue != 0 );


  # add tags to the phd files indicating the vector

  &tagVector( $szScreenOut );
}






  
sub runCrossMatchBetweenNewReadsAndContigs {
  my ( $szScreenFile, $szContigsFile ) = @_;


  my $szCommand = "$szCrossMatchExe $szScreenFile $szContigsFile -alignments -tags -masklevel 0 >$szAlignmentsFile";

  print "now running cross_match between the new reads and the existing contigs\n";
  my $nReturnedValue = system( $szCommand );

  die "something wrong running $szCommand" if ( $nReturnedValue != 0 );

  print "done running cross_match\n";

}


sub cleanUp {

  unlink( $szNewReadsFOF );
  unlink( $szContigsFile );
  unlink( $szFastaFile );
  unlink( $szFastaFile . ".log" );
  unlink( $szChromatsToPhred );
  unlink( $szPhdFilesToMakeFastaFile );
  unlink( $szScreenOut );
  unlink( $szScreenFile );
  unlink( $szScreenFile . ".log" );
}


sub tagVector {

  my ($szScreenCrossMatchOutputFilename) = @_;

  open( filCrossMatchOutput, $szScreenCrossMatchOutputFilename ) ||
    die "couldn't open cross match output file $szScreenCrossMatchOutputFilename";

  while( <filCrossMatchOutput> ) {
    chomp;
    if ( $_ =~ /^ALIGNMENT/ ) {
      &parseVectorAlignmentLine;
    }
  }

  close( filCrossMatchOutput );
}



sub parseVectorAlignmentLine {

# if reached here, the first token is ALIGNMENT
# Typical examples:
# uncomplemented:
# ALIGNMENT   772  0.49 0.37 0.61  Contig2        5   822 (0)    NewContig2      403 1218 (0)  
# complemented:
# ALIGNMENT   382  0.25 0.76 0.00  Contig1        1   397 (248)  C NewContig2   (818)   400     1  


  @aWords = split;

  $szReadName = $aWords[5];
  $nReadAlignLeft = $aWords[6];
  $nReadAlignRight = $aWords[7];
  $cCompFlag = $aWords[9];
  if ( $cCompFlag eq "C" ) {

    $szThingToTagName = $aWords[10];
    $nThingToTagAlignLeft = $aWords[12];
    $nThingToTagAlignRight = $aWords[13];
  }
  else {
    $cCompFlag = "U";
    $szThingToTagName = $aWords[9];
    $nThingToTagAlignLeft = $aWords[10];
    $nThingToTagAlignRight = $aWords[11];
  }

  
  # we only care about the read positions


  # read the phd file to see if there is already of tag 
  # there just like this

  if ( bIsThereATagLikeThisInThePhdFileAlready( $szReadName, 
                                                $nReadAlignLeft,
                                                $nReadAlignRight ) ) {
    return;
  }


  # if reached here, there is no such tag already in the phd file

  appendVectorTagToPhdFile( $szReadName,
                            $nReadAlignLeft,
                            $nReadAlignRight );



}

sub appendVectorTagToPhdFile {
  my ($szReadName, $nReadAlignLeft, $nReadAlignRight ) = @_;

  my $szPhdFile = $szPHDDir . "/" . $szReadName . ".phd.1";

  open( filPhdFile, ">>$szPhdFile" ) || die "couldn't open phd file $szPhdFile for appending";

  print filPhdFile "\n";
  print filPhdFile "BEGIN_TAG\n";
  print filPhdFile "TYPE: vector\n";
  print filPhdFile "SOURCE: addReads2Consed.perl\n";
  print filPhdFile "UNPADDED_READ_POS: $nReadAlignLeft $nReadAlignRight\n";
  $szDateTime = &szGetDateForReadTag;
  print filPhdFile "DATE: $szDateTime\n";
  print filPhdFile "END_TAG\n";
  print filPhdFile "\n";
  
  close( filPhdFile );
}



sub szGetDateForReadTag {
  my $szDate;
  ($nSecond, $nMinute, $nHour, $nDayInMonth, $nMonth, $nYear, $wday, $yday, $isdst ) = localtime;

  undef $isdst;
  undef $wday;
  undef $yday;
  
  if ( $nYear >= 100 ) {
    $nYear = $nYear % 100;
  }

  $szDate = sprintf( "%02d/%02d/%02d %02d:%02d:%02d",
           $nYear,
           $nMonth + 1,
           $nDayInMonth,
           $nHour,
           $nMinute,
           $nSecond );

  return( $szDate );
}



sub bIsThereATagLikeThisInThePhdFileAlready {

  my ( $szReadName, 
       $nReadAlignLeft,
       $nReadAlignRight ) = @_;

  my $szPhdFile = $szPHDDir . "/" . $szReadName . ".phd.1";

  open( filPhdFile, "$szPhdFile" ) || die "couldn't open phd file $szPhdFile for reading";


  # look for a vector tag of these same positions
  # tags look like this:
  
  # BEGIN_TAG
  # TYPE: sequencingVector
  # SOURCE: consed
  # UNPADDED_READ_POS: 83 90
  # DATE: 00/07/28 17:23:07
  # END_TAG

  while( <filPhdFile> ) {

    if ( ! /^BEGIN_TAG/ )  {
      next;
    }

    if ( !defined( $_ = <filPhdFile> ) ) {
      close( filPhd );
      return( 0 );
    }

    if (! /^TYPE:[\s]*vector/ ) {
      next;
    }

    # now look for SOURCE

    if (! defined( $_ = <filPhdFile> ) ) {
      close( filPhd );
      return( 0 );
    }
 
    # now look for UNPADDED_READ_POS:

    if ( !defined( $_ = <filPhdFile> ) ) {
      close( filPhd );
      return( 0 );
    }

    @aWords = split;

    if ( $aWords[0] ne "UNPADDED_READ_POS:" ) {
      next; # tag screwed up--go find another tag
    }

    $nExistingTagStart = $aWords[1];
    $nExistingTagEnd = $aWords[2];


    if ( ( $nExistingTagStart <= $nReadAlignLeft ) &&
         ( $nReadAlignRight <= $nExistingTagEnd  ) ) {

      close( filPhd );
      return( 1 );
    }
  }


  # got to end of phd file and no vector tag that
  # encompasses this one

  close( filPhd );
  return( 0 );
}
