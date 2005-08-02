#!/bin/perl -w

use strict;

package UTIL::gf;

use IO::File;
use Exporter;
use vars qw( @ISA @EXPORT );
@ISA    = qw(Exporter);
@EXPORT = qw( 
   g_testvalue 
   g_escape 
   g_unescape 
   g_parsequery 
   g_concatquery 
   g_system 
   g_comparefiles
   g_checksemaphore 
   g_removesemaphore 
   g_checksemaphoreprocess 
   g_dumpstruct
   g_greater
   g_lesser
   g_gethomedir
   g_findvalueinarray
   g_formatdatevalue
   g_buildhtmlselectlist
   g_substitutetags
);

# my $kSEMAPHOREPATH = "$ENV{SEMAPHORE_DIR}" || "./";

my $kSEMAPHOREPATH = '/home/pavonis/pavdb/semaphore';
my $kDEBUG = 1;

# SEMAPHORE CONFLICT definition
my $kSEMAPHORENOCONFLICT   = 1;
my $kSEMAPHORECONFLICT     = 0;
my $kSEMAPHOREWAIT         = -1;
my $kSEMAPHOREPAUSE        = -2;

######################################################################
sub g_testvalue( ) {
# verifies that a value is, in fact, a type:
# NATURAL: natural number [012345789]+
# INTEGER: [+-] natural
# YESNO:   [YN]
# BOOLEAN: [01YNTF]
# NUMBER:  [+-]0.0
# TEXT:    \w+
# LIST:    implies a hash handle; looks for value in keys
# DIR_READ
# DIR_WRITE
# FILE_READ
# FILE_NEW
# ARRAY_REF
# HASH_REF
# FILE_REF
# REF
# ANY
######################################################################

   my $p = shift;
   my $types = shift;

   my $type;
   my $hlist;
   my $text;
   my $rvalue = 0;

   foreach $type ( split( ',', $types  ) ) {

      if (

      ( $type eq q(ANY) )

      or

      ( ($type eq q(NATURAL))
      and ( $p =~ /^\d+$/ ) )

      or

      ( ($type eq q(INTEGER))
      and ( $p =~ /[+-]?^\d+$/ ) )

      or

      ( ($type eq q(YESNO))
      and ( $p =~ /^[YN]$/ ) )

      or

      ( ($type eq q(BOOLEAN))
      and ( $p =~ /^[YN01TF]$/ ) )

      or

      ( ($type eq q(NUMBER))
      and ( $p =~ /^[+-]?(?:\d+(?:\.\d*)?|\.\d+)$/ ) )

      or

      ( ($type eq q(SCIENTIFIC))
#     and ( $p =~ /^[+-]?(?:\d+(?:\.\d*)?|\.\d+)$/ ) )
      and ( $p =~ /^[+-e.0-9]+$/ ) )

      or

      ( ($type eq q(TEXT))
      and ( length($p) ) )

      or

      ( ($type eq q(LIST)) 
      and ($hlist=shift) 
      and (defined($hlist->{$p})) )

      or

      ( ($type =~ /DIR.*_READ/ )
      and ( -d $p )
      and ( -r $p ) )

      or

      ( ($type =~ /DIR.*_WRITE/ )
      and ( -d $p )
      and ( -w $p ) )

      or

      ( ($type eq q(FILE_READ))
      and ( -f $p )
      and ( -r $p ) )

      or

      ( ($type eq q(FILE_NEW))
      and ( ( not ( -f $p ) ) or ( -w $p ) )
      and ( $text = $p )
      and ( $text =~ s/\/[^\/]+$/\// )
      and ( -d $text )
      and ( -w $text ) )

      or

      ( ($type eq q(ARRAY_REF))
      and ( ref($p) eq q(ARRAY) ) )

      or

      ( ($type eq q(HASH_REF))
      and ( ref($p) eq q(HASH) ) )

      or

      ( ($type eq q(FILE_REF))
      and ( ref($p) =~ /IO::File|GLOB/ ) )

      or

      ( ($type eq q(REF))
      and ( ref($p) ) )

      ) { 
         return 1; 
      }
   }

   return 0;
}

############################################################
sub g_unescape {
# g_unescape: translates coded characters in location str
############################################################

  my $todecode = shift;
  return undef unless defined($todecode);
  $todecode =~ tr/+/ /;       # pluses become spaces
  $todecode =~ s/%([0-9a-fA-F]{2})/pack("c",hex($1))/ge;
  return $todecode;
}

############################################################
sub g_escape {
# g_escape: translates value into a coded location str
############################################################

    my $toencode = shift;
    return undef unless defined($toencode);
    $toencode=~s/ /+/g;
    $toencode=~s/([^a-zA-Z0-9_.+-])/uc sprintf("%%%02x",ord($1))/eg;
    return $toencode;
}

############################################################
sub g_parsequery {
# g_parsequery: parses a query string into a hash
# assuming KEY1=value1&KEY2=value2&...
############################################################

   my $h_h = { };
   my ( $arg, $key, $val );

   # get the whole query string

   my $qstr='';

   if ( defined($ENV{REQUEST_METHOD}) ) {

      if ($kDEBUG) { $h_h->{_REQUEST_METHOD}=$ENV{REQUEST_METHOD}; }

      if ( $ENV{REQUEST_METHOD} eq 'GET' ) {
         $qstr=$ENV{QUERY_STRING} if defined($ENV{QUERY_STRING});

         foreach $arg ( split( '&', $qstr ) ) {
            ($key, $val) = split( '=', $arg, 2 );
            if ( length($key) and length($val) ) {
               $h_h->{$key}=&g_unescape($val);
            }
         }
         if ($kDEBUG) { $h_h->{_QSTR}=$qstr; }
      }
      elsif ( ( $ENV{REQUEST_METHOD} eq 'POST' ) 
      and     defined($ENV{CONTENT_LENGTH})
      and     ( $ENV{CONTENT_LENGTH} > 0 ) ) {

         if ($kDEBUG) { 
            $h_h->{_QSTR}=''; 
            $h_h->{_CONTENT_LENGTH}=$ENV{CONTENT_LENGTH};
         }

         while ( $arg = <> ) {
            if ($kDEBUG) { $h_h->{_QSTR}.=$arg; }
            chomp $arg;
            ($key, $val) = split( '=', $arg, 2 );
            if ( length($key) and length($val) ) {
               $h_h->{$key}=&g_unescape($val);
            }
         }

      }

   }
   else {
      if ($kDEBUG) { $h_h = \%ENV; }
   }

   $h_h;
}

############################################################
sub g_concatquery {
# converts a query hash into a query string
############################################################

   my $h = shift; # hash

   my $key; 
   my $qstr = ''; 
   foreach $key ( keys ( %{$h} ) ) {
      $qstr .= qq($key=) . &g_escape($h->{$key}) . q(&);
   }
   chop $qstr;
   $qstr;
}

#############################################################
sub g_system {
# Calls external program(s)
# Acceptable arguments:
#    reference to array or scalar to receive output
#    OUT=[10YNTF] to turn output on / off
#    command
#############################################################

   my $cmd ;

   my $out   = 0;    # turn on to capture output
   my $h_out = undef;
   my $xstat = 0;
   my $rstat = 0;
   my $output_buffer = '';

   $! = '';
   $? = '';

   while ( $cmd = shift ) { # WHILE

      if ( ref ( $cmd ) =~ /ARRAY|SCALAR|HASH|File|GLOB/ ) {
         # set an output array pointer to receive output
         $h_out = $cmd;
         $out   = 1;
      }

      elsif ( $cmd =~ /OUT=[01YNTF]/ ) { 
         # set output on or off
         my  ( $key, $val ) = split( '=', $cmd );
         if    ( $val =~ /[1YT]/ ) { $out = 1; }
         elsif ( $val =~ /[0NF]/ ) { $out = 0; }
      } 

      else { # CMD
         # log command
         if ( $out ) { 
            &l_system_log( $h_out, "executing: $cmd" ); 
         }
         # execute command
         if ( open ( T1, "$cmd 2>&1 | " ) ) {
            my $output;
            while ( $output = <T1> ) {
               $output_buffer .= $output;
            }
            if ( $xstat = close( T1 ) ) {
               &l_system_log( $h_out, "close: $xstat: $cmd" );
               &l_system_log( $h_out, $output_buffer ); 
            }
            elsif ( $out ) {
               &l_system_log( $h_out, $output_buffer ); 
            }
            $rstat++;
         }
         else {
            # failed to execute command
            if ( $out ) {
               &l_system_log( $h_out, "open failed"); 
            }
         }
         # print any shell messages
         if ( $out ) {
            if ( $? ) { 
               &l_system_log( $h_out, "SHELL: $?"); 
            }
            if ( $! ) { 
               &l_system_log( $h_out, "SHELL: $!"); 
            }
         }
      } # CMD

   } # WHILE

   return $rstat;
}

######################################################################
sub l_system_log {
######################################################################
   my $h_out = shift;
   my $msg;
   
   if ( ref($h_out) eq 'SCALAR' ) {
      while ( $msg = shift ) {
         ${$h_out} .= "$msg\n";
      }
   }
   elsif ( ref($h_out) eq 'ARRAY' ) {
      while ( $msg = shift ) {
         push( @{$h_out}, "$msg" );
      }
   }
   elsif ( ref($h_out) =~ /File|GLOB/ ) {
      while ( $msg = shift ) {
         print $h_out "$msg\n";
      }
   }
   else {
      while ( $msg = shift ) {
         print "$msg\n";
      }
   }
}

######################################################################
sub g_comparefiles {
# purpose:
#    used to tell whether two files are the same
# calling convention:
#    &g_comparefiles( filename1, filename2 )
# returns:
#    -2 if file 2 not readable
#    -1 if file 1 not readable
#     0 if files are the same
#     3 if files differ in size
#     4 if files differ in content not size
######################################################################
   my $file1 = shift;
   my $file2 = shift;

   my @stat1;
   my @stat2;

   # check for readable files

   unless ( -r $file1 ) { return -1; }
   unless ( -r $file2 ) { return -2; }

   # check file sizes

   @stat1 = stat($file1);
   @stat2 = stat($file2);

   unless ( $stat1[7] eq $stat2[7] ) { return 3; }

   # check file contents

   if ( system( "cmp -s $file1 $file2" ) ) { return 4; }

   return 0;
}

######################################################################
# Some notes on semaphores:
#
# Semaphores only work if all servers involved share a single
# semaphore directory.
#
# Semaphore path is hardcoded so that multiple processes on a single
# server use a single semaphore path.  Environment may be subject
# to differences in initialization or process
# 
# Semaphores may be used to ask other processes to stop or to pause
# 
# Semaphores are non-preemptive; once a stop or pause request has
# been issued, it is up to the dependent process to checksemaphore
# on every transaction loop.
######################################################################

######################################################################
sub g_checksemaphore {
######################################################################
   my %h   = @_;
   my $rcode = $kSEMAPHORECONFLICT;
   my $lpid = '';

   # servername
   # check for pid using ps

   if (
   defined( $h{-SEMAPHORE_NAME} )
   and ( length( $h{-SEMAPHORE_NAME} ) > 0 ) ) {

      if (
      defined( $h{-PID} )
      and ( length( $h{-PID} ) > 0 ) ) {
         $lpid = $h{-PID};
      }
      else {
         $lpid = $$;
      }
      my $lhostname = `hostname`;
      chomp $lhostname;

      # check for a semaphore conflict

      if ( ($rcode = &l_checksemaphoreconflict( %h )) > 0 ) {

         # check for a single matching semaphore file

         if ( -r "$kSEMAPHOREPATH/$h{-SEMAPHORE_NAME}" ) {
            my ( $fpid, $fhostname );
            $fpid = `cat $kSEMAPHOREPATH/$h{-SEMAPHORE_NAME}`;
            ($fpid,$fhostname) = split( "\n", $fpid, 2 );
            chomp $fhostname;
            if ( ( $fpid eq $lpid ) and ( $fhostname eq $lhostname ) ) {
               $rcode = $kSEMAPHORENOCONFLICT;
            }
            else {
               $rcode = $kSEMAPHORECONFLICT;
            }
         }

         # implied return 0 for an unreadable semaphore file, although this
         # should probably raise an alert!

         elsif ( ! -e "$kSEMAPHOREPATH/$h{-SEMAPHORE_NAME}" ) {
            # write a new semaphore file
            g_newsemaphore( \%h  );
            $rcode = $kSEMAPHORENOCONFLICT;
         }
      }
   }
   return $rcode;
}

######################################################################
sub g_newsemaphore {
######################################################################

   my $hh = shift;
   my $lpid = '';

   if (
   defined( $hh->{-PID} )
   and ( length( $hh->{-PID} ) > 0 ) ) {
      $lpid = $hh->{-PID};
   }
   else {
      $lpid = $$;
   }
   my $lhostname = `hostname`;
   chomp $lhostname;
 
   `echo $lpid       > $kSEMAPHOREPATH/$hh->{-SEMAPHORE_NAME}`;
   `echo $lhostname >> $kSEMAPHOREPATH/$hh->{-SEMAPHORE_NAME}`;

}

######################################################################
sub g_removesemaphore {
######################################################################
   my %h   = @_;
   my $rcode = 0;
   my $lpid = '';

   if ( defined( $h{-SEMAPHORE_NAME} )
   and ( length( $h{-SEMAPHORE_NAME} ) > 0 ) ) {

      if ( defined( $h{-PID} )
      and ( length( $h{-PID} ) > 0 ) ) {
         $lpid = $h{-PID};
      }
      else {
         $lpid = $$;
      }
      my $lhostname = `hostname`;
      chomp $lhostname;

      if ( -r "$kSEMAPHOREPATH/$h{-SEMAPHORE_NAME}" ) {
         my ( $fpid, $fhostname );
         $fpid = `cat $kSEMAPHOREPATH/$h{-SEMAPHORE_NAME}`;
         ($fpid,$fhostname) = split( "\n", $fpid, 2 );
         chomp $fhostname;
         if ( ( $fpid eq $lpid ) and ( $fhostname eq $lhostname ) ) {
            $rcode = unlink "$kSEMAPHOREPATH/$h{-SEMAPHORE_NAME}";
         }
      }
   }

   $rcode;
}

######################################################################
sub l_checksemaphoreconflict {
######################################################################
   my %hh     = @_;
   my $rcode = $kSEMAPHORENOCONFLICT;

   # semaphore path

   if ( -e "$kSEMAPHOREPATH/stopall" ) {
      $rcode = $kSEMAPHORECONFLICT;
   } 

   ##############################################
   ### specific conflicts may be entered here ###
   ##############################################

   elsif ( ( $hh{-SEMAPHORE_NAME} eq 'dbbackup' ) 
      and ( ( -e "$kSEMAPHOREPATH/sequence_run_retrieval" ) 
         or ( -e "$kSEMAPHOREPATH/export_sequences" ) 
         or ( -e "$kSEMAPHOREPATH/fetch_genbank_subsections" ) 
         or ( -e "$kSEMAPHOREPATH/import_blastn_results" ) )) {
      if ( ! -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
         g_newsemaphore( \%hh );
      }
      $rcode = $kSEMAPHOREWAIT;
   }

   elsif ( ( $hh{-SEMAPHORE_NAME} eq 'sequence_run_retrieval' ) and ( -e "$kSEMAPHOREPATH/dbbackup" ) ) {
      if ( -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
         g_removesemaphore( %hh );
      }
      $rcode = $kSEMAPHOREPAUSE;
   }

   elsif ( ( $hh{-SEMAPHORE_NAME} eq 'import_blastn_results' ) and ( -e "$kSEMAPHOREPATH/dbbackup" ) ) {
      if ( -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
         g_removesemaphore( %hh );
      }
      $rcode = $kSEMAPHOREPAUSE;
   }

   elsif ( ( $hh{-SEMAPHORE_NAME} eq 'export_sequences' ) and ( -e "$kSEMAPHOREPATH/dbbackup" ) ) {
      if ( -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
         g_removesemaphore( %hh );
      }
      $rcode = $kSEMAPHOREPAUSE;
   }

   elsif ( ( $hh{-SEMAPHORE_NAME} eq 'read_identification' ) and ( -e "$kSEMAPHOREPATH/fetch_genbank_subsections" ) ) {
      if ( -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
         g_removesemaphore( %hh );
      }
      $rcode = $kSEMAPHOREPAUSE;
   }

   elsif ( $hh{-SEMAPHORE_NAME} eq 'fetch_genbank_subsections' ) {
      if ( -e "$kSEMAPHOREPATH/dbbackup" ) {
         if ( -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
            g_removesemaphore( %hh );
         }
         $rcode = $kSEMAPHOREPAUSE;
      }
      elsif ( -e "$kSEMAPHOREPATH/read_identification" ) {
         if ( ! -e "$kSEMAPHOREPATH/$hh{-SEMAPHORE_NAME}" ) {
            g_newsemaphore( \%hh );
         }
         $rcode = $kSEMAPHOREPAUSE;
      }
   }
   $rcode;
}

######################################################################
sub g_checksemaphoreprocess {
# Purpose:
#    checks whether a semaphore process is active
# Calling convention:
#    hash reference or hash containing -SEMAPHORE_NAME key
# Return values:
#    -1 if call failed
#     0 if semaphore does not exist
#     0 if semaphore exists but process is not active
#     1 if semaphore exists and process is active
# Notes:
#    It might be better to separate return codes for semaphore-not-found
#    and process-not-active.
######################################################################
   my $rcode = -1;

   if ( scalar(@_) ) {

      # process arguments
      # calling convention: (a) hash reference, or (b) hash
      # semaphore name is required

      my %h;
      my $hh;
      if ( ref( $_[0] ) eq 'HASH' ) {
         $hh = shift;
      }
      else {
         %h = @_;
         $hh = \%h;
      }

      # check for required arguments

      if ( defined ( $hh->{-SEMAPHORE_NAME} ) ) {

         my $semaphore_file = "$kSEMAPHOREPATH/$hh->{-SEMAPHORE_NAME}";

         # look for a readable semaphore file

         if ( -r $semaphore_file ) {

            # get current process and hostname

            my $lpid = $$;
            my $lhostname = `hostname`;

            # read file process and hostname

            my $fpid = `cat $semaphore_file`;
            my $fhostname;
            ($fpid,$fhostname) = split( "\n", $fpid, 2 );
            chomp $fhostname;

            # check hostname against file

            if ( $fhostname eq $lhostname ) {

               # operating on process host
               # check process id against file

               if ( $fpid eq $lpid ) {
                  # process id is file id
                  $rcode = 1;
               }
               else {
                  # process id is different from file id, hostname matches
                  $rcode = `ps ax | grep "^ *$fpid +" | wc -l`;
               }

            }
            else {
               # process on remote server
               $rcode = `rsh $fhostname ps ax | grep "^ *$fpid +" | wc -l`;
            }
         }
         elsif ( -e $semaphore_file ) {
            # WARNING
            # semaphore file can not be read
            $rcode = -1;
         }
         else {
            # semaphore file does not exist
            $rcode = 0;
         }
      }
      else {
         # WARNING
         # missing argument
         $rcode = -1;
      }
   }
   else {
      # WARNING
      # no argument
      $rcode = -1;
   }

   return $rcode;
}

######################################################################
sub g_dumpstruct {
######################################################################
  unless ( scalar(@_) > 0 ) { return; }

  # interpret arguments

  my %a = @_;
  my $h = $a{_OBJECT}          || undef;
  my $fh = $a{_FILEHANDLE}     || \*STDOUT;
  my $maxlevel = $a{_MAXLEVEL} || 0;
  my $level = $a{_LEVEL}       || 0;
  my $h_visited = $a{_VISITED} || { };
  %a = ( );

  my $indent = ' ' x $level;

  my $ref;
  if ( $ref = ref($h) ) {

    # check for maximum level reached

    if ( ( $maxlevel > 0 ) and ( $maxlevel < $level ) ) {
      print $fh "MAXIMUM DESCEND LEVEL REACHED:$maxlevel\n";
      return;
    }

    # check for object already visited

    if ( defined($h_visited->{$h}) ) {
        print $fh "OBJECT REVISITED: $h\n";
        return;
    }
    $h_visited->{$h} = 1;

    # dump object

    if ( ( $ref eq 'HASH' ) or ( UNIVERSAL::isa( $h, 'HASH' ) ) ) {

      my @keys = keys %{$h};
      print $fh "[$ref] nkeys=" . scalar(@keys) . " addr=$h\n";
      my $key;
      foreach $key ( @keys ) {
        print $fh "$indent$key=";
        if ( $key =~ /^_/ ) {
          my $hh = $h->{$key};
          $ref = ref( $hh );
          my ($sz,$addr) = ( '', '' );
          if (($ref eq 'HASH') or (UNIVERSAL::isa($hh,'HASH'))) {
            $sz = "nkeys=" . scalar(keys %{$hh});
            $addr = "addr=$hh";
          }
          elsif ( ($ref eq 'ARRAY') or (UNIVERSAL::isa($hh,'ARRAY'))) {
            $sz = "nelements=" . scalar(@{$hh});
            $addr = "addr=$hh";
          }
          print $fh "[masked] $sz $addr\n";
        }
        else {
          &g_dumpstruct(
            _OBJECT => $h->{$key},
            _FILEHANDLE => $fh, 
            _MAXLEVEL => $maxlevel, 
            _LEVEL => $level+1,
            _VISITED => $h_visited
          );
        }
      }
    }

    elsif ( ( $ref eq 'ARRAY' )  or ( UNIVERSAL::isa( $h, 'ARRAY' ) ) ) {

       print $fh "[$ref] nelements=" . scalar(@{$h}) . " addr=$h\n";
       my $index;
       for ( $index=0; $index<scalar(@{$h}); $index++ ) {
            print $fh "$indent$index=";
            &g_dumpstruct( 
              _OBJECT => $h->[$index], 
              _FILEHANDLE => $fh, 
              _MAXLEVEL => $maxlevel, 
              _LEVEL => $level+1,
              _VISITED => $h_visited
            );
       }
    }
    else {
       # unknown objct type
       print $fh "[$ref]\n";
    }
  }
  else {
    # scalar
    print $fh "$h\n";
  }
}

# ----------------------------------------------------------------------
sub g_greater {
  my $a = shift; my $b = shift;
  return $a > $b ? $a : $b;
}

# ----------------------------------------------------------------------
sub g_lesser {
  my $a = shift; my $b = shift;
  return $a < $b ? $a : $b;
}

# ----------------------------------------------------------------------
sub g_gethomedir {
  my $username = shift;
  my $line = `cat /etc/passwd | grep "$username:"`;
  if ( length( $line ) ) {
    my ($user,$homedir);
    ($user,undef,undef,undef,undef,$homedir,undef) = split( ':', $line, 7 );
    if ( $user eq $username ) {
      return $homedir;
    }
  }
  return '';
}

# ----------------------------------------------------------------------
sub g_findvalueinarray {

  my $value   = shift;
  my $h_array = shift;
  my $stop    = shift || 1000;

  my $array_size = scalar( @{$h_array} );

  my $rvalue  = -1;
  my $index   =  0;

CHK: while ( ( $index < $array_size ) and ( $index < $stop ) ) {
    if ( $h_array->[$index] eq $value ) {
      $rvalue = $index;
      last CHK;
    }
    else {
      $index++;
    }
  }

  return $rvalue;
}

# ----------------------------------------------------------------------
# rg 04-25-2000 - reads a user supplied date and formats as MM/DD/YYYY
# expected formats:
# m/d/yy m-d-yy
# m/d m-d
# m/d/yyyy m-d-yyyy
# mmm d yy
# not expected:
# any d/m/yy formats, as in 4 Jan
sub g_formatdatevalue {

  my $p_value = shift;
  my $h_value ;
  my $r_value  = '';

  if ( ref($p_value) ) {
    $h_value = $p_value;
    $p_value = ${$h_value};
  }

  my ( $mm, $dd, $yy );

  ($mm,$dd,$yy) = split( /[ ,\/-]*/, $p_value );
  # check month
  unless ( &g_testvalue( $mm, 'INTEGER' ) ) {
    $mm = &g_findvalueinarray( substr( uc($mm), 0, 3 ), 
    ['JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC'],
    12 ) + 1;
  }
  if ( ( $mm > 0 ) and ( $mm < 13 ) ) {
    if ( ( $dd > 0 ) and ( $dd < 32 ) ) {
      if ( $yy eq '' ) { $yy = 2000; }
      elsif ( $yy < 100 ) { $yy += 2000; }
      if ( ( $yy > 1980 ) and ( $yy < 2100 ) ) {
        $r_value = substr( "0$mm", -2 ) . '/' . substr( "0$dd", -2 ) . '/' . $yy;
      }
    }
  }

  if ( $h_value ) { ${$h_value} = $r_value; }
  return $r_value;
}

# ----------------------------------------------------------------------
sub g_buildhtmlselectlist {
# arguments:
# (1) listname
# depends on calling option; 
# (2..) hashref or arrayref or two array refs or array (simple list).
#
# calling options:
# 1. hash ref: keys become option names, values become text
# 2. single array ref: values become option names and text
# 3. two array refs: first array becomes names, second text
# 4. simple list: values become option names and text

# action:

  my $listname = shift;   # list name
  my $h_options = shift;  # option names
  my $h_descr ;           # displayed option descr

  my $select_list = '';

  if ( not ( ref( $h_options ) ) ) {
    # 4. simple list: values become option names and text
    $select_list = qq(<SELECT NAME="$listname">) .
    qq(<OPTION NAME="$h_options">$h_options</OPTION>) ;
    while ( $h_options = shift ) {
      $select_list .= qq(<OPTION NAME="$h_options">$h_options</OPTION>) ;
    }
    $select_list .= '</SELECT>';
  }
  elsif ( ref( $h_options ) eq 'HASH' ) {
    # 1. hash ref: keys become option names, values become text
    my $key;
    $select_list = qq(<SELECT NAME="$listname">) ;
    foreach $key( keys( %{$h_options} ) ) {
      $select_list .= qq(<OPTION NAME="$key">$h_options->{$key}</OPTION>) ;
    }
    $select_list .= '</SELECT>';
  }
  elsif ( ref( $h_options ) eq q(ARRAY) ) {
    if ( ( $h_descr = shift ) && ( ref( $h_descr ) eq q(ARRAY) ) ) {
      # 3. two array refs: first array becomes names, second text
      my $n = scalar( @{$h_options} ) ;
      my $m = scalar( @{$h_descr} ) ;
      if ( $m < $n ) { $n = $m; }
      if ( $n ) {
        $select_list = qq(<SELECT NAME="$listname">);
        for ( $m=0; $m<$n; $m++ ) {
          $select_list .= qq(<OPTION NAME="$h_options->[$m]">$h_descr->[$m]</OPTION>);
        }
        $select_list .= '</SELECT>';
      }
    }
    else {
      # 2. single array ref: values become option names and text
      my $key;
      $select_list = qq(<SELECT NAME="$listname">);
      foreach $key ( @{$h_options} ) {
        $select_list .= qq(<OPTION NAME="$key">$key</OPTION>);
      }
      $select_list .= '</SELECT>';
    }
  }
  return $select_list;
}

# ----------------------------------------------------------------------
sub g_substitutetags {
# args:
#    source string: string with embedded [:TAG:]'s
#    ref to hash
# action:
#    replaces [:TAG:] with value in hash or ''
# returns:
#    string with replaced tags
#

  my $source = shift;
  my $h_hash = shift;

  my $key;
  foreach $key ( keys( %{$h_hash} ) ) {
    $source =~ s/\[:$key:\]/$h_hash->{$key}/g;
  }

  $source =~ s/\[:[^:\]]*:\]//g;
  return $source;
}

1;

