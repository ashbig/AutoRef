#!/bin/perl -w
#
# File: print_lable.pl
#
# Date: 07/02/2001
#
	
# comment the first line and uncomment the second before deploy
#use lib '/kotel/data/home/jmunoz/flex/ApplicationCode/Perl';
use lib '/usr/local/jakarta-tomcat-4.0.6/webapps/FLEX/WEB-INF/perl';
use UTIL::zebra_label;
use strict;

# the input parameters
my ($barcode, $printer, $width, $height) = @ARGV;

# the return status message
my $msg = 'Label not printed';

# the label format
my $label = UTIL::zebra_label->new(LH_X => "$width", LH_Y => "$height");

$label->add_line( FD => "$barcode", FONT => 'BC', INTLINE => 'Below')
if (defined($barcode) && length($barcode) );

# set up printer
if ( length ($printer) and 
   ( $label->set_hash ( OPORT => "| lpr -P $printer") eq 1) ) {
	$label->send_zebra_label;
	$msg = "Label Printed";
}

# show all error message
$msg = join("\n", $msg, $label->pop_err_all);

print "$msg";

