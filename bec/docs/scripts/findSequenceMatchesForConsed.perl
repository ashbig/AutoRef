#!/usr/bin/perl -w

# findSequenceMatchesForConsed.perl

# PURPOSE:  to find sequence matches that can be displayed in Assembly 
#    View in Consed
#
# HOW TO USE IT:  findSequenceMatchesForConsed.perl (ace file) (any crossmatch parameters...)
#
# INPUT: ace file
#
# OUTPUT: the crossmatch output file name (ace file).aview
#    If there is already a file by that name, it is overwritten.
#    A fasta file is also created with name (project).YYMMDD.HHMMSS.fasta
#
# REVISIONS: 021127 (DG) to write (project).(date).(time).fasta instead of
#    (project).fasta.screen.ace.1.(date).(time).fasta

$szVersion = "021127";

$szUsage = "Usage: findSequenceMatchesForConsed.perl (ace file) (crossmatch parameters, if any)";

if ( $#ARGV < 0 ) {
  die $szUsage;
}

if ( $ARGV[0] eq "-V" || $ARGV[0] eq "-v" ) {
    print "version: $szVersion\n";
    exit( 1 );
}

#defined( $szConsedHome = $ENV{'CONSED_HOME'} ) ||   ( $szConsedHome = "/usr/local/genome" );


$szCrossMatchExe = "d:/programs_bio/assembler/exe/cross_match";


$szAceFile = shift( @ARGV );

@aCrossMatchOptions = @ARGV;

die "Can't read $szAceFile $!" if (! -r $szAceFile );

# check that have write permission to this directory before start

$szTempFile = "temp" . $$;
open( filTemp, ">$szTempFile" ) || die "don't have write permission to this directory $!";
close( filTemp );
unlink( $szTempFile ) || die "couldn't delete a file in this directory $!";

# construct name of fasta file--strip off everything starting with
# .fasta.screen...

( $szProjectName = $szAceFile ) =~ s/\.fasta\.screen\..*$//;

$szFastaFile = $szProjectName . "." . &szGetDateForTag . ".fasta";

($nFormatOfAceFile, %aContigsInAceFile ) =
  &bDetermineAceFileFormat( $szAceFile );

# now construct fasta files
print "writing file $szFastaFile\n";

if ( $nFormatOfAceFile == 2 ) {
  &constructContigsFile( $szAceFile, $szFastaFile );
}
else {
  &constructContigsFileAceFormat1( $szAceFile, $szFastaFile,
                                   %aContigsInAceFile );
}

# construct crossmatch output file

$szCrossMatchOutput = $szAceFile . ".aview";

if ( -e $szCrossMatchOutput ) {
  print "Warning from findSequenceMatchesForConsed.perl:  $szCrossMatchOutput already exists but will be overwritten\n";
}

unlink( $szCrossMatchOutput );


# run crossmatch against this file

$szCommand = $szCrossMatchExe . " $szFastaFile $szFastaFile -tags -masklevel 101";
foreach $szOption (@aCrossMatchOptions ) {
  $szCommand .= " ";
  $szCommand .= $szOption;
}

$szCommand .= " >$szCrossMatchOutput";

!system( $szCommand ) || die "some problem running crossmatch: $!";

print "results are in $szCrossMatchOutput\n";





# subroutines



sub szGetDateForTag {
  my $szDate;
  ($nSecond, $nMinute, $nHour, $nDayInMonth, $nMonth, $nYear, $wday, $yday, $isdst ) = localtime;
 
  undef $isdst;
  undef $wday;
  undef $yday;
  
  if ( $nYear >= 100 ) {
    $nYear = $nYear % 100;
  }
 
  $szDate = sprintf( "%02d%02d%02d.%02d%02d%02d",
           $nYear,
           $nMonth + 1,
           $nDayInMonth,
           $nHour,
           $nMinute,
           $nSecond );
 
  return( $szDate );
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

