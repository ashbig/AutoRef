#!/usr/bin/perl -w

# PURPOSE: concatenate all .1 phd file into a single phd.ball 
#          for the purpose of fast startup of consed
#
# HOW TO USE IT:
#          go to the directory where the ace file is and type:
#          catPhdFiles.perl
#
# March 2000, David Gordon


$szPhdDirectory = "../phd_dir";
$szPhdBall = "phd.ball";

if ( -e $szPhdBall ) {
    unlink( $szPhdBall ) || die "couldn't delete $szPhdBall";
}


$nFilesCopied = 0;

opendir( filDir, $szPhdDirectory ) || die "couldn't open $szPhdDirectory";

while( defined( $szNextFile = readdir( filDir ) ) ) {
    if ( $szNextFile =~ /\.phd\.1$/ ) {
        $szCommand = "cat ../phd_dir/$szNextFile >>$szPhdBall";
        !system( $szCommand ) || die "couldn't execute $szCommand";
        ++$nFilesCopied;
        if ( 
             (( $nFilesCopied % 100 ) == 0 ) || 
             ( $nFilesCopied < 20 ) 
             ) {
            print "$nFilesCopied files copied\n";
        }
    }
}

closedir( filDir );

	

	
