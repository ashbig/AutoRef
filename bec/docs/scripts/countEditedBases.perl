#!/usr/bin/perl -w
#
# countEditedBases.perl
#
# PURPOSE:  counts how many bases are edited in each contig
#
# REV: Dec 1998 (David Gordon)
#
# HOW TO USE IT:  countedEditedBases.perl <(name of ace file)
#
#
#

while(<>) {
	if ( /^CO / ) {
		@aWords = split;
		$szContig = $aWords[1];
		$nEditedBases = 0;
		while(<>) {
			if ( /^BQ/ ) {
				last;
			}
		}

		while(<>) {
			if ( /^\s$/ ) {
				last;
			}

			@aQualities = split;
			for( $n = 0; $n <= $#aQualities; ++$n ) {
				$nQuality = $aQualities[ $n ];
				if ( $nQuality == 98 || $nQuality == 99 ) {
					++$nEditedBases;
				}
			}
		}

		print "Contig $szContig has $nEditedBases edited bases\n";
	}
}


