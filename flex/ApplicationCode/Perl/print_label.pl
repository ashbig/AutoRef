#!/usr/bin/perl -w
#
# File: print_lable.pl
#
# Args:	label barcode (for all labels)
#	user name (only for container, sample-container and reagent-container)
#	expiration date (only for sample-container)
#
# Date: 03/06/2001
#
	
use UTIL::zebra_label;
use strict;

# the input parameters
my ($barcode,$printer, $width, $height, $username,$expirationdate) = @ARGV;

# the return status message
my $msg = 'Label not printed';

# the label format
my $label = UTIL::zebra_label->new(LH_X => "$width", LH_Y => "$height");

$label->add_line( FD => "$barcode", FONT => 'BC', INTLINE => 'Below')
if (defined($barcode) && length($barcode) );

$label->add_line( FD => " ", FONT => 'AD');

$label->add_line( FD => "Created by: $username", FONT => 'AD' )
if (defined($username) && length($username) );

$label->add_line( FD => "Exp. date: $expirationdate", FONT => 'AD')
if (defined($expirationdate) && length($expirationdate) );

# set up printer
if ( length ($printer) and 
   ( $label->set_hash ( OPORT => "| lpr -P $printer") eq 1) ) {
	$label->send_zebra_label;
	$msg = "Label Printed";
}

# show all error message
$msg = join("\n", $msg, $label->pop_err_all);

print "$msg";

