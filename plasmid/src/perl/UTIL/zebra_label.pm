#!/bin/perl -w

my $kcr = "\n";

my %kLISTS = (
   _MEMBERS_LABEL  => {
      LH_X       => 'NATURAL',
      LH_Y       => 'NATURAL',
      OPORT      => 'TEXT',
      IPORT      => 'TEXT',
      LABELTYPE  => 'LIST',
      FO_Y_INCR  => 'NATURAL',
      FO_Y_CUR   => 'NATURAL'
   },
   _DEFAULTS_LABEL => {
      LH_X       => '100',
      LH_Y       => '40',
      LHEIGHT    => '200',
      LWIDTH     => '475',
      OPORT      => '| lpr -P lp1',
      IPORT      => '',
      LABELTYPE  => 'Full',
      FO_Y_INCR  => '5',
      FO_Y_CUR   => '5'
   },
   _REQUIRED_LABEL => {
      LH_X       => '1',
      LH_Y       => '1',
      OPORT      => '1',
      LABELTYPE  => '1',
      FO_Y_INCR  => '1'
   },
   _MEMBERS_LINE   => {
      FD         => 'TEXT',
      FONT       => 'LIST',
      FR         => 'YESNO',
      FO_X       => 'NATURAL',
      FO_Y       => 'NATURAL',
      BARHEIGHT  => 'NATURAL',
      INTLINE    => 'LIST',
      CHECKDIGIT => 'YESNO'
   },
   _DEFAULTS_LINE   => {
      FONT       => 'AD',
      BARHEIGHT  => '35',
      INTLINE    => 'Below',
      FO_X       => '10',
      CHECKDIGIT => 'Y'
                  },
   _REQUIRED_LINE   => {
      FD         => 'TEXT',
      FONT       => 'LIST',
      FO_X       => 'NATURAL',
      FO_Y       => 'NATURAL'
   },
   FONT       => {
      A0         => '17',
      AA         => '10',
      AB         => '12',
      AC         => '20',
      AD         => '20',
      AE         => '23',
      AF         => '28',
      AG         => '63',
      AH         => '19' ,
      BC         => 'Code 128',
      B3         => 'Code 39'
   },
   INTLINE    => {
      None       => 'No interpretation line',
      Above      => 'Interpretation line above barcode',
      Below      => 'Interpretation line below barcode'
   },
   LABELTYPE     => {
      Full       => 'Labels for reagent containers',
      Half       => 'Labels for 384-well plates'
   }
);

##############################
package UTIL::zebra_base;
##############################

use strict;
use UTIL::gf;

######################################################################
sub new {
# zebra_label Constructor:
######################################################################

   # basics

   my $class = shift;
   my $self = { };
   bless $self, $class;

   $self->{_ERRORSTACK} = [ ];
   $self->new_subclass( );

   # set defaults

   $self->reset_hash;

   # override defaults with arguments

   $self->set_hash( @_ ) if ( scalar( @_) );

   $self;
}

######################################################################
sub new_subclass {
# function to override for subclass-specific initializations
######################################################################
   my $self = shift;
   $self->{_MEMBERS}  = { };
   $self->{_REQUIRED} = { };
   $self->{_DEFAULTS} = { };
   $self->{_MODULE}   = q(zebra_base);
   1;
}

######################################################################
sub get_member_type {
######################################################################
   my $self = shift;
   my $member = shift;
   my $h_m = $self->{_MEMBERS};
   return $h_m->{$member};
}

######################################################################
sub get_member_value {
######################################################################
   my $self = shift;
   my $member = shift;
   return $self->{$member};
}

######################################################################
sub reset_hash {
# sets specified defaults for the current object
######################################################################

   my $self = shift;

   my $h_defaults = $self->{_DEFAULTS};
   my $h_members  = $self->{_MEMBERS};

   # create a hash element for each defined key
   # populate with default value if defined

   my $i = 0;
   my $key;

   foreach $key ( keys ( %{$h_members } ) ) {

      # set hash element to default value if defined

      if ( defined($h_defaults->{$key}) ) {
         $self->set_hash_key( $key, $h_defaults->{$key} );
      }

      # else initialize hash element to null string

      else { $self->{$key} = q(); } 

      $i++; 
   }
   $i; # number of parameters set
}

######################################################################
sub set_hash {
# sets recognized arguments within a label field from
# parameters
######################################################################

   my $fn        = q(set_hash);
   my $self      = shift;
   my %H         = @_;

   # cycle through arguments and add to object

   my $i;
   my ($key, $val);

   foreach $key ( keys %H ) {

      # check for defined key and value
      # set hash

      if ( defined($key) and length($key) ) {

         if ( $self->set_hash_key($key,$H{$key}) ) {
            $i++;
         }
         else {
            $self->push_err(
            qq($fn: failed to set_hash_key value to key: $key: $H{$key}));
         }
      }
   }

   $i; # number of parameters set
}

######################################################################
sub set_hash_key {
# validates and set_hash_keys a value to a key
######################################################################
   my $fn   = q(set_hash_key);

   my $self = shift;
   my $key  = shift;
   my $val  = shift;

   # if the key value pair is valid then updatee the hash

   if ( (length($val) eq 0) or $self->valid_key_val($key,$val) ) {
      $self->{$key} = $val;
      return 1;
   }

   else {
      $self->push_err( qq($fn: Failed to set_hash_key $key=$val) );
      return 0;
   }
}

######################################################################
sub valid_key_val {
# returns 1 iff a key and value are appropriate
######################################################################
   my $fn = q(valid_key_value);
   my $self = shift;
   my $key  = shift;
   my $val  = shift;

   my $h_members  =  $self->{_MEMBERS};
   my $h_required =  $self->{_REQUIRED};
   
   # check for null key

   if ( length($key ) eq 0 ) {
      $self->push_err( qq($fn: null key) );
      return 0;
   }

   # check for unknown key

   elsif (not(defined($h_members->{$key}))) {
      $self->push_err( qq($fn: unknown key: $key) );
      return 0;
   }

   # allow blank value

   elsif ( length($val) eq 0 ) {
      if ( defined( $h_required->{$key} ) ) {
         $self->push_err(qq($fn: blank value provided for required key: $key) );
         return 0;
      }
      else {
         return 1;
      }
   }

   # check for valid value

   elsif (not(&g_testvalue($val, $h_members->{$key},
                           $self->get_list_hash($key)))) {
      $self->push_err( qq($fn: invalid value for key: $key=$val) );
      return 0;
   }

   # passed all tests: return ok

   else {
      return 1; 
   }
}

######################################################################
sub verify_object {
# verify_object: returns true if the object is legitimately printable
######################################################################

   my $fn         = q(verify_object);
   my $self       = shift;

   my $rc         = 1; # return code: innocent until proven guilty

   # step through keys and verify_object values

   my $key;
   foreach $key ( keys ( %{$self} ) ) {
      if ( $key =~ /^_/ ) { 1; } # skip internal keys
      elsif ( $self->valid_key_val( $key, $self->{$key} ) ) { 1; }
      else {
         $self->push_err( 
         qq($fn: invalid key=value pair: $key=$self->{$key} ) );
         $rc = 0;
      }
   }
   return $rc;
}

######################################################################
sub get_list_hash {
# returns a reference to the list hash if defined
######################################################################


   my $self_or_class = shift;
   my $list_name     = shift;     # name of list to return

   if (defined($kLISTS{$list_name})) { return $kLISTS{$list_name}; }
   else { return undef; }
}

######################################################################
sub push_err {
# push_err: adds error message strings to the error stack
######################################################################
   my $self = shift;

   my $err;
   while ( $err = shift ) {
      push ( @{$self->{_ERRORSTACK}}, qq($self->{_MODULE}: $err) );
   }
}

######################################################################
# pop_err: pops an error message string from the top of the stack
######################################################################

sub pop_err {
   my $self = shift;
   my $err;
   pop @{$self->{_ERRORSTACK}};
}

######################################################################
# pop_err_all: returns and clears the error stack array
######################################################################

sub pop_err_all {
   my $self = shift;
   my @la = @{$self->{_ERRORSTACK}};
   $self->{_ERRORSTACK} = [ ];
   @la;
}

######################################################################
sub DESTROY {
# automatic destructor
######################################################################

   my $self = shift;
   my $err;
   while ( $err = $self->pop_err ) {
      print "$err\n";
   }
}

#########################
#########################
package UTIL::zebra_label;
#########################
#########################

use strict;
use IO::File;
use UTIL::gf;

use vars qw(@ISA);
@ISA=qw(UTIL::zebra_base);

######################################################################
sub new_subclass {
# function to override for subclass-specific initializations
######################################################################
   my $self = shift;
   $self->{_MEMBERS}  = $kLISTS{_MEMBERS_LABEL};
   $self->{_REQUIRED} = $kLISTS{_REQUIRED_LABEL};
   $self->{_DEFAULTS} = $kLISTS{_DEFAULTS_LABEL};
   $self->{_MODULE}   = q(zebra_label);
   1;
}

######################################################################
sub add_line {
# adds a line to the barcode
######################################################################
   my $fn = q(add_line);
   my $self = shift;
   my $h_f;

   if ($h_f=UTIL::zebra_line->new(
   FO_Y => $self->{FO_Y_CUR},
   @_) ) {
      unless (defined($self->{_LINES})) {
         $self->{_LINES} = [ ];
      }
      push @{$self->{_LINES}}, $h_f;
      $self->{FO_Y_CUR} += ( $h_f->get_height + $self->{FO_Y_INCR} );
      return 1;
   }
   else {
      $self->push_err(qq($fn: failed to create new zebra_line));
      return 0;
   }
}

######################################################################
# sub send_zebra_label: output a label to a stream
######################################################################

sub send_zebra_label {
   my $fn = q(send_zebra_label);
   my $self = shift;

   if ( not ( $self->verify_object ) ) {
      $self->push_err("$fn: failed to verify_object label");
   }
   else {

      unless ( defined( $self->{_OPORT} ) ) {
         $self->open_ports( );
      }

      if ( defined( $self->{_OPORT} ) ) {

         my $ofh = $self->{_OPORT};

         print $ofh qq(^XA$kcr);

         # label home

         print $ofh qq(^LH$self->{LH_X},$self->{LH_Y}$kcr);

         # label details

         my $line;
         foreach $line ( @{$self->{_LINES}} ) {
            print $ofh $line->get_zebra_line( );
         }

         print $ofh  qq(^XZ$kcr); # end of lable

         return 1;
      }
      else {
         push_err("$fn: unable to open output port");
         return 0;
      }
   }
}

######################################################################
# test_oport sends a test label to the output port
######################################################################

sub test_oport {
   my $self = shift;
   1;
}

######################################################################
sub open_ports {
# opens the output and input ports required for 
# communicating with the printer
######################################################################
   my $fn   = q(open_ports);
   my $self = shift;

   my $fh;
   if ( ( length($self->{OPORT}) )
   and ( $fh = IO::File->new( "$self->{OPORT}" ) ) ) {
      $self->{_OPORT} = $fh;
      return 1;
   }
   else {
      $self->push_err("$fn: failed to open output port: $self->{OPORT}");
      $self->push_err("$fn: $!") if ( $! );
      $self->push_err("$fn: $?") if ( $? );
      return 0;
   }
}

######################################################################
sub get_default_value
# returns a default value to go with the key
######################################################################
{
   my $self_or_class = shift;
   my $key = shift;
   my $hd = $kLISTS{_DEFAULTS_LABEL};
   return $hd->{$key};
}

######################################################################
sub get_lines
# returns reference to the array of lines so that other programs
# may iterate through the lines on a label
######################################################################
{
   my $self = shift;
   return $self->{_LINES};
}

#########################
#########################
package UTIL::zebra_line;  
#########################
#########################

use strict;
use UTIL::gf;

use vars qw(@ISA);
@ISA=qw(UTIL::zebra_base);

######################################################################
sub new_subclass {
# function to override for subclass-specific initializations
######################################################################
   my $self = shift;
   $self->{_MEMBERS}  = $kLISTS{_MEMBERS_LINE};
   $self->{_REQUIRED} = $kLISTS{_REQUIRED_LINE};
   $self->{_DEFAULTS} = $kLISTS{_DEFAULTS_LINE};
   $self->{_MODULE}   = q(zebra_line);
   1;
}

######################################################################
sub get_height {
# returns height of line
######################################################################
   my $self = shift;
   my $hfonts = $self->get_list_hash('FONT');

   my $height = 0;

   if ( $self->{FONT} =~ /^A.$/ ) {
      if ( defined ( $hfonts->{$self->{FONT}} ) ) {
         $height = $hfonts->{$self->{FONT}};
      }
   }
   elsif (  $self->{FONT} =~ /^B.$/ ) {
      if ( length( $self->{BARHEIGHT} ) ) {
         $height =  $self->{BARHEIGHT};
      }
      else {
         $height += 15; # untested
      }
      if ( ( $self->{INTLINE} eq q(Above) )
      or   ( $self->{INTLINE} eq q(Below) ) ) {
         $height += 15; # untested
      }
   }

   $height;
}

######################################################################
sub get_zebra_line {
# returns a formatted line for a zebra printer
######################################################################
   my $fn=q(get_zebra_line);
   my $self = shift;

   if ( not ( $self->verify_object ) ) {
      $self->push_err("$fn: failed to verify_object label");
      return undef;
   }
   else {

      # font

      my $font;
      my ($h,$f,$g,$e) = ($self->{BARHEIGHT},'','',$self->{CHECKDIGIT} );

      if ( $self->{INTLINE} eq q(Above) ) {
         ($f,$g) = ('Y','Y');
         $self->{FO_Y} += 20;
      }
      elsif ( $self->{INTLINE} eq q(Below) ) { ($f,$g) = ('Y','N'); }
      elsif ( $self->{INTLINE} eq q(None) ) { ($f,$g) = ('N','N'); }

      if ( $self->{FONT} eq 'BC' ) {
         $font = qq(^BC,$h,$f,$g,$e);
      }
      elsif ( $self->{FONT} eq 'B3' ) {
         $font = qq(^B3,$e,$h,$f,$g);
      }
      elsif ( $self->{FONT} =~ /^A.$/ ) {
         $font = qq(^$self->{FONT});
      }
      else {
         $self->push_err("$fn: unrecognized font: $self->{FONT}");
         return undef;
      }

      # field origin

      my $fo = qq(^FO$self->{FO_X},$self->{FO_Y});

      # field reverse

      my $fr = '';
      my $gb = '';
      if ( $self->{FR} eq q(Y) )  {
         my $w = UTIL::zebra_label->get_default_value('LWIDTH');
         my $y = UTIL::zebra_label->get_default_value('FO_Y_INCR');
         my $h = $self->get_height( ) + $y;
         my $dif = int ( ( $y / 2 ) + 0.5 );
         my $fo_y = $self->{FO_Y} - $dif;
         $gb = qq(^FO0,$fo_y^GB$w,$h,$h^FS$kcr);
         $fr = q(^FR);
      };

      # value

      my $val = qq(^FD$self->{FD}^FS);

      return qq($gb$fo$font$fr$val$kcr);
   }
}

1;
