#!/usr/bin/perl -w
#
# Purpose:  makes a contigs file out of an ace file

$szUsage = "Usage:  ace2Contigs.perl (ace file)";


die "$szUsage" if ( $#ARGV != 0 );

$szAceFile = $ARGV[0];

die "Can't read $szAceFile" if (! -r $szAceFile );

# should check that have write permission to this directory before start


# now construct root filenames

$szFileRoot = $szAceFile;

$szFileRoot =~ s/\.fasta\.screen\..*$//;

# strip off any directory
$nLastSlash = rindex( $szFileRoot, "/" );
if ( $nLastSlash != -1 ) {
    $szFileRoot = substr( $szFileRoot, $nLastSlash + 1 );
}



($nFormatOfAceFile, %aContigsInAceFile ) = 
    &bDetermineAceFileFormat( $szAceFile );

# now construct contigs files
print "constructing contigs files...\n";

$szContigsFile = $szFileRoot . ".contigs";
if ( $nFormatOfAceFile == 2) {
    &constructContigsFile( $szAceFile, $szContigsFile );
}
else {
    &constructContigsFileAceFormat1( $szAceFile, $szContigsFile,
                                    %aContigsInAceFile );
}


# subroutines


    
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

