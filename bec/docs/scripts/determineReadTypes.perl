#!/usr/bin/perl -w
#  determineReadTypes.perl
#  
#  Purpose:  to write into the phd file information about the template 
#            name and forward/reverse information in order to 
#            help phrap and consed/autofinish.   This came about when
#            we found that we couldn't force labs to change their 
#            read naming convention to that expected by phrap.  So we
#            allow you to have any consistent naming convention you want,
#            but you have to do a little perl programming to customize
#            this script to fit your naming convention.  
#
#  If you don't want to do perl programming:
#            If you don't want to do any perl programming, you have
#            the option of using the St Louis naming convention as is.
#            But what is the St Louis naming convention?  Most of it
#            (but not all) is explaned in the phrap documentation.  In
#            addition, you must never use an underscore in the name if
#            the read is a universal primer forward or universal
#            primer reverse read.  If the read is a walk, then you
#            must have an underscore (_) follow the template name and
#            then have a number (the oligo number). 
#
# Examples of reads in the St Louis naming convention:
#
# read eeq03a01.g1.phd.1 is univ rev template: eeq03a01 library: eeq03
# read eeq03a02.b1.phd.1 is univ fwd template: eeq03a02 library: eeq03
# read eeq03a02.g1.phd.1 is univ rev template: eeq03a02 library: eeq03
# read eeq03a03.b1.phd.1 is univ fwd template: eeq03a03 library: eeq03
# read eej45h07_2.i1.phd.1 is walk template: eej45h07 library: eej45
# read eej46c12_1.i1.phd.1 is walk template: eej46c12 library: eej46
#
#            BAC reads are not used at St Louis so the St Louis convention
#            doesn't provide for them.
#
#  If you are using Autofinish for cDNA finishing:
#            I suggest you subscribe to the Vancouver format (see below).
#
#  How to use it:  
#     determineReadTypes.perl -justView
#        Just use this when you are testing--it is read-only and does not
#        modify the phd files.
#
#     determineReadTypes.perl (no arguments)
#        This will examine every phd file and add to the ones that do not
#        already have primer and template WR items
#        
#
#     determineReadTypes.perl -justThisPhdFile <name of phd file with ../phd_dir/>
#        This will just examine and add primer and template WR items
#        to the phd file specified, unless primer and template WR items
#        are already in this phd file
#
#  How to modify it:
#     First of all, you must know a little perl, especially regular 
#     expressions..  If you don't know perl, 
#     don't dispair--it will just take you a couple of hours to learn it.
#     I suggest Randal Schwartz, Learning Perl.  On the other hand, if you
#     don't want to take the time to learn perl, you can always name your
#     reads the same as one of the large genome centers below:
#
#     There are 4 models of interpreting read names:  
#          findTemplateNameAndTemplateTypeStLouisFormat
#          findTemplateNameAndTemplateTypeSeattleFormat
#          findTemplateNameAndTemplateTypeMITFormat
#          findTemplateNameAndTemplateTypeCodonCodeFormat
#
#
#
#     You can start with either one of these (found below), whichever
#     is closest to your naming convention, and modify it.  Or, you
#     can use one of these as is.  To change to one of these instead
#     of St Louis format, go to the line in the subroutine sub
#     processOnePhdFile that looks like:
#
#   ($bErrorFlag, $templateName, $templateType, $primerType ) = &findTemplateNameAndTemplateTypeStLouisFormat( $phdFileName );
#
#     and change it to the format you want.  For example, if you
#     want to use CodonCode format, change this line to:
#
#   ($bErrorFlag, $templateName, $templateType, $primerType ) = &findTemplateNameAndTemplateTypeCodonCodeFormat( $phdFileName );
#
#
#     
#     As you are debugging your script, run it like this:
#     determineReadTypes.perl -justView
#     That will not modify the phd files--it will just tell you what 
#     it thinks each read is!
#
#  What it writes into the phd file:
#            whole read info items of the following types:
#
#  WR{
#  template phredPhrap 990224:045110
#  name: (template name)  
#  type: (bac, cos, puc, pbc, pcr, or fake)  (this line is optional)
#  lib: (name of library) (this line is optional)
#  }
# 
#  Currently, this program will typically just put the "name:" line
#  into the phd file.  In the case of bac or cosmid sequencing reads,
#  it will write the "type:" line with "bac" or "cos" on the line.  The
#  "lib:" line is necessary if you have different subclone libraries
#  that have different insert sizes--such as a large insert library and
#  a short insert library.
#
#
#  WR{
#  primer phredPhrap 990224:045110
#  type: univ fwd
#  }
# 
#  In the above, "univ fwd" could be replaced by:
#
#  univ rev
#  pcr end
#  walk
#
#  If the read is with a custom primer and is not the end of a pcr product,
#  then this program puts "walk" into the "type:" line.
#  
#  If this program determines that the read is a universal primer read,
#  then it tries to determine whether it is a universal primer forward or 
#  universal primer reverse read. 
#
#  Note: It is ok to run this program over and over again on the same
#  project since it will not add anything further to a phd file if it
#  finds it has already added template and primer WR items to that phd
#  file.  However, if you find you made a mistake in
#  determineReadTypes.perl, then you must delete all the phd files
#  before you run phredPhrap again.  Otherwise determineReadTypes.perl
#  will see that it has been run before on the phd files and not write
#  anything new so your changes to determineReadTypes.perl will have
#  no effect.
#
#  Once you have completed and tested your changes, run this on one of
#  your projects.  Bring up consed on that project.  In the Main Consed Window,
#  point to the Info menu, hold down the left mouse button, and release on
#  'Show Info for Each Read'.  Check that the information presented is correct.
#  If not, it is probably due to a problem with your determineReadTypes.perl
#  for your site.
#
#  For Autofinish, Autofinish will be helped by knowing which read in the
#  assembly corresponds to a read that it previously suggested.  You can
#  tell it this information by putting a expid WR item into the phd file:
#
#  WR{
#  expid determineReadTypes 990224:045110
#  25
#  }
#
#  where the 25 should be replaced by the experiment ID from the
#  autofinish output
#
#  If you are doing cDNA finishing:
#            There are some special challenges in cDNA finishing that
#            the Vancouver format meets (see below for Vancouver format):
#            1)  3' EST reads should be labelled as reverse universal primer 
#                reads (see below)
#            2)  5' EST reads should be labelled as forward universal primer 
#                reads (see below)
#            3)  all other reads (walks and transposon reads) should be
#                labelled as type: bac  (see below)
#            4)  the template name should be different for each walking read
#                (Otherwise, phrap will have problems if it gets thousands
#                of reads that apparently all come from the same template)
#            


###############################################################
#
# main program start
#
###############################################################


# you can remove or comment out this line when you are satisfied with
# your customized version of this file.

die "You must edit determineReadTypes.perl--please read README.txt that came with consed.  Look under INSTALLING CONSED.  Also look at the comments at the beginning of determineReadTypes.perl  Then, and only then, comment out the line in determineReadTypes.perl that begins with \"die\" and has this error message\n by putting a # in front of that line";

$szVersion = "001205";


# do not change $phdDirPath!
# consed expects the following directory structure:
# foo/phd_dir contains phd files
# foo/chromat_dir contains chromats
# foo/edit_dir contains ace file
# You can make these links, if you like.

$phdDirPath = "../phd_dir";

# the -justView option is used to help debug your modifications of this
# script.  Type:
# determineReadTypes.perl -justView
# and no changes will take place, but this script will tell you
# what it thinks about each read.

$bJustView = 0;
if ( $#ARGV >= 0 ) {
   if ( $ARGV[0] eq "-V" || $ARGV[0] eq "-v" ) {
      print "version: $szVersion\n";
      exit( 0 );
   }
   elsif ( $ARGV[0] eq "-justView" ) {
     $bJustView = 1;
     shift @ARGV;
   }
}


if ( $#ARGV == -1 ) {
  # case of no arguments so examine all phd files
  
  $nNumberOfFilesProcessed = 0;
  $nNumberOfFilesExamined = 0;
  opendir( dirPhdDir, $phdDirPath ) || die "could not even open $phdDirPath";
  
  while( defined( $szPhdFile = readdir( dirPhdDir ) ) ) {
    if ( index( $szPhdFile, ".phd." ) >= 0 ) {

      $bIsPhdFileAlreadyProcessedd = &bIsPhdFileAlreadyProcessed( $szPhdFile );

      if ( ( $bIsPhdFileAlreadyProcessedd == 0) || $bJustView  ) {
        &processOnePhdFile( $szPhdFile );
        ++$nNumberOfFilesProcessed;
      }
      ++$nNumberOfFilesExamined;

      if ( $nNumberOfFilesExamined % 100 == 0 || $nNumberOfFilesExamined < 10 ) {
        
        print "files examined: $nNumberOfFilesExamined, files processed: $nNumberOfFilesProcessed\n";
      }
    }
  }
  
  closedir( dirPhdDir );
}
elsif ( $ARGV[0] eq "-justThisPhdFile" ) {
  die "the phd filename must follow -justThisPhdFile" if ( $#ARGV < 1 );

  $szFullPath = $ARGV[1];

  die "couldn't open $szFullPath for reading" if (!-r $szFullPath );
  die "couldn't open $szFullPath for writing" if (!-w $szFullPath );

  # cut off the path

  use File::Basename;

  my $szPhdFile = basename( $szFullPath );

  $bIsPhdFileAlreadyProcessedd = &bIsPhdFileAlreadyProcessed( $szPhdFile );
  
  if ( ( $bIsPhdFileAlreadyProcessedd == 0 ) || $bJustView ) {
    &processOnePhdFile( $szPhdFile );
    print "$szPhdFile processed\n";
  }
  else {
    print "$szPhdFile already processed\n";
  }
}
else {
  die "unrecognized arguments";
}


exit(0);

## end of program


sub findTemplateNameAndTemplateTypeStLouisFormat {
  die "usage: &findTemplateNameAndTemplateTypeStLouisFormat( <phd file name> );"
    if ($#_ != 0 );

  my $phdFileName = $_[0];

  
  my $readName;
  ( $readName = $phdFileName ) =~ s/\.phd\.[0-9]+$//;

  $firstDot = index( $readName, "." );
  if ( $firstDot == -1 ) {
    print STDERR "Can't figure out template and primer type for $phdFileName since no dot in filename\n";
    return( (1) );
  }


  if ( $firstDot == length( $readName ) ) {
    print STDERR "Can't figure out template and primer type for $phdFileName since no letter after the dot\n";
    return( (1) );
  }

  
  if ( $firstDot == 0 ) {
    print STDERR "Can't figure out template for $phdFileName since file starts with a dot\n";
    return( (1) );
  }

  # at St Louis, template names never have underscores in them, except
  # for .c and .a which are ignored by autofinish anyway

  # If there is a walking read, it looks like this:
  # das42f09_10.x1.phd.1
  # das42f09_10.y1.phd.1
  # das42f09_10.b1.phd.1
  # das42f09_10.i1.phd.1
  # das42f09_10.g1.phd.1
  # 
  # Special chemistry custom primer reads are:
  # das42f09_a_10.b1.phd.1
  # das42f09_a_10.g1.phd.1
  # das42f09_a_10.x1.phd.1
  # das42f09_a_10.y1.phd.1

  # Special chemistry universal primer reads are:
  # das42f09_a.b1.phd.1
  # das42f09_a.g1.phd.1
  # das42f09_a.x1.phd.1
  # das42f09_a.y1.phd.1


  my $nameUpToDot = substr( $readName, 0, $firstDot );

  my $firstUnderscore = index( $nameUpToDot, "_" );
  my $templateName;
  my $readType;
  my $universalPrimer = 1;
  my $customPrimer = 2;
  if ( $firstUnderscore == -1 ) {
    # there is no underscore in the name
    # like this: das42f09.x1.phd.1

    $templateName = $nameUpToDot;
    $readType = $universalPrimer;
  }
  else {
    # there is an underscore in the name

    $templateName = $nameUpToDot;
    # cut off from underscore to end
    # das42f09_10.x1.phd.1 has template name das42f09

    $templateName =~ s/_.*//;


    # if there is an underscore
    ( $afterUnderscore = $nameUpToDot ) =~ s/^[^_]*_//;

    #changed by syang
    if ( $afterUnderscore =~ /e[0-9]+/ ) {
	#autofinsh reads with exp id
	#extract the expid and put it in the WR later by sayng
	($autofinishExpID) = $afterUnderscore =~ /e([0-9]+)/;
	if ($afterUnderscore =~ /^e[0-9]+$/)
	{
	    $readType = $universalPrimer;
	}
	elsif ($afterUnderscore =~ /^[0-9]+e[0-9]+$/)
	{
	    $readType = $customPrimer;
	}
	elsif ($afterUnderscore =~ /^g[0-9]+e[0-9]+$/)
	{
	    $readType = $customPrimer;
	}
	elsif ($afterUnderscore =~ /^ge[0-9]+$/)
	{
	    $readType = $universalPrimer;
	}
	else
	{
	    print STDERR "1: Can't figure out read type for $phdFileName since after the underscore is $afterUnderscore\n";
       return( (1) );
	}
	
    }
    elsif ( $afterUnderscore =~ /^[0-9]+$/ ) {
	# e.g., sl77h06_6.z2.phd.3
      # sl76c01_19.g1.phd.1
      # ot50d12_30.i1.phd.1

      $readType = $customPrimer;
    }
    elsif ( $afterUnderscore =~ /^[0-9]+_[a-zA-Z]+$/ ) {
      # added by syang, e.g., fax80c05_33_g.b1,fax80c05_33_c.b1
      $readType = $customPrimer;
    }
    elsif ( $afterUnderscore =~ /^[0-9]+_[0-9]+$/ ) {
      # added by syang, e.g., fax80c05_33_5.b1
      $readType = $customPrimer;
    }
    elsif ( $afterUnderscore =~ /^[a-zA-Z]+$/ ) {
      # e.g., wv81h09_c.g1.phd.1 
      $readType = $universalPrimer;
    }
    elsif ( $afterUnderscore =~ /^[0-9]+[a-zA-Z]+$/ ) {
      # old style oligo walks with special chemistry, like this:
      # e.g., ot54e02_32g.i1.phd.1 oy47e12_31g.i1.phd.1 sl76c01_12g.b1.phd.1
      $readType = $customPrimer;
    }
    elsif ( $afterUnderscore =~ /^[a-zA-Z]+_[0-9]+$/ ) {
      # new style oligo walks with special chemistry
      $readType = $customPrimer;
    }
    else {
       print STDERR "1: Can't figure out read type for $phdFileName since after the underscore is $afterUnderscore\n";
       return( (1) );
    } # if ( $afterUnderscore ...
  } # if ( $firstUnderscore ...

  # if reached here, know whether the read is a universal primer or
  # custom primer
  # In the case of universal primer, we need to know whether it is 
  # universal forward or universal reverse

  $letterAfterFirstDot = substr( $readName, $firstDot + 1, 1 );

  my $templateType = "";
  if ( $letterAfterFirstDot eq 'a' ||
       $letterAfterFirstDot eq 'c' ) {
    $templateType = 'fake';
  }


  my $orientation;
  if ( $letterAfterFirstDot =~ /[ryg]/ ) {
    $orientation = "rev";
  }
  else {
    $orientation = "fwd";
  }


  my $primerType;
  if ( $readType == $universalPrimer ) {
    if ( $orientation eq "fwd" ) {
      $primerType = "univ fwd";
    }
    else {
      $primerType = "univ rev";
    }
  }
  elsif ( $readType == $customPrimer ) {
      if ( $templateType eq "pcr" ) {
         $primerType = "pcr end";
      }
      else {
         $primerType = "walk";
      }
  }
  else {
    die "read $phdFileName should either be a custom primer or universal primer";
  }


  #get $libraryName here
  ($libraryName) = $templateName =~ /([a-z]{2,3}[0-9][0-9])[a-p][0-9][0-9]/;

  # this is needed because perl will undefined $libraryName
  # above if the pattern isn't found
  if ( !defined( $libraryName ) ) {
      $libraryName = "";
  }

  return( (0, $templateName, $templateType, $primerType) );
}





sub findTemplateNameAndTemplateTypeMITFormat {
  die "usage: &findTemplateNameAndTemplateTypeMITFormat( <phd file name> );"
    if ($#_ != 0 );

  my $phdFileName = $_[0];

  
  # L719P601RB2.0.scf.phd.1 $readName is L719P601RB2.0.scf

  my $readName;
  ( $readName = $phdFileName ) =~ s/\.phd\.[0-9]+$//;

  # L719P601RB2.0.scf.phd.1 $readNameWithoutSCF is L719P601RB2.0

  my $readNameWithoutSCF;
  ( $readNameWithoutSCF = $readName ) =~ s/\.scf$//;

  
  # L719P601RB2.0.scf.phd.1 $choppedOffFinalExtension is  L719P601RB2

  my $choppedOffFinalExtension;
  ( $choppedOffFinalExtension = $readNameWithoutSCF ) =~ s/\.[^\.]+$//;

  my $primerType = "";

  if ( $choppedOffFinalExtension =~ /p[0-9]+$/ ) {
    $primerType = "walk";
  }
  else {
    # must be a universal primer read
    # We need to decide whether it is a forward or reverse universal primer
    # read

  
    # looking for L719P601FH2.0.scf.phd.1
    if ( $choppedOffFinalExtension =~ /F[A-Z][0-9][0-9]?$/ ) {
      $primerType = "univ fwd";
    }
    # looking for L719P601RA6.0.scf.phd.1
    elsif ( $choppedOffFinalExtension =~ /R[A-Z][0-9][0-9]?$/ ) {
      $primerType = "univ rev";
    }
    else {
      print "I don't understand read type for $readName\n";
    }
  }


  
  # Now we need to find the template name

  my $templateName;
  if ( $primerType eq "walk" ) {
    ( $templateName = $choppedOffFinalExtension ) =~ s/p[0-9]+$//;
  }
  else {
    $templateName = $choppedOffFinalExtension;
  }

  # so we've chopped off the p9 now:  L719P608FB7p2.T0.scf has become
  # L719P608FB7

  $templateName =~ s/[FR]([A-Z][0-9][0-9]?)?//;

  # now $templateName  L719P608FB7p2.T0.scf has become L719P608
  # We've chopped off the F or R, but we need to add the B7 (row/column 
  # designator) back on

  $templateName .= $1;

  my $templateType = "";

  return( (0, $templateName, $templateType, $primerType) );
}





sub findTemplateNameAndTemplateTypeSeattleFormat {

   die "usage: &findTemplateNameAndTemplateTypeSeattleFormat( <phd file name> );"
    if ( $#_ != 0 );

   my $phdFileName = $_[0];


   $firstDot = index( $phdFileName, "." );
   if ( $firstDot == -1 ) {
      print STDERR "Can't figure out template and primer type for $phdFileName since no dot in filename\n";
      return( (1) );
   }

   
   if ( $firstDot == length( $phdFileName )  ) {
      print STDERR "Can't figure out template and primer type for $phdFileName since no letter after the dot\n";
      return( (1) );
   }

   if ( $firstDot == 0 ) {
      print STDERR "Can't figure out template for $phdFileName since filename starts with a dot\n";
      return( (1) );
   }
           

   $templateName = substr( $phdFileName, 0, $firstDot );

   $firstUnderscore = index( $templateName, "_" );
   if ( $firstUnderscore != -1 ) {

      if ( $firstUnderscore == 0 ) {
         print STDERR "Filename must not start with an underscore in phd file $phdFileName\n";
         return( (1) );
      }

      if ( $firstUnderscore == ( length( $templateName ) - 1 ) ) {
         print STDERR "Strange phd file $phdFileName with underscore just before the dot\n";
         return( (1) );
      }

      $lastPartOfTemplateName = substr( $templateName, $firstUnderscore + 1, 
                           length( $templateName ) - $firstUnderscore - 1 );
   

      if ( $lastPartOfTemplateName =~ /^pcr/ ) {
         $templateType = "pcr";
      }
      elsif ($lastPartOfTemplateName eq "bac" ) {
         $templateType = "bac";
      }
      elsif ($lastPartOfTemplateName eq "cos" ) {
         $templateType = "cos";
      }
      else {
         # M13 or plasmid, but can't tell which oen
         $templateType = "";
      }
   }
   else {
      $templateType = "";
   }


   $afterDot = substr( $phdFileName, 
                       $firstDot + 1,
                       length( $phdFileName ) - $firstDot - 1 );


   # cut off the .phd.# part of the name

   $afterDot =~ s/\.phd.[0-9]+$//;


   $universalPrimer = 1;
   $customPrimer = 2;


   if ( $afterDot =~ /^[a-z][0-9]+u[0-9]+_/ ) {
      $readType = $universalPrimer;
      print "found $phdFileName\n";
   }
   elsif ( $afterDot =~ /^[a-z][0-9]+u[0-9]+$/ ) {
      # e.g., read name djs233_2255.x1u2
      $readType = $universalPrimer;
   }
   elsif( $afterDot =~ /^[a-z][0-9]+_up/ ) {
      $readType = $universalPrimer;
   }
   elsif( $afterDot =~ /^[a-z][0-9]+r[0-9]+p[0-9]+_/ ) {
      # e.g., read name 
      $readType = $customPrimer;
      print "found $phdFileName\n";
   }
   elsif( $afterDot =~ /^[a-z][0-9]+r[0-9]+p[0-9]+$/ ) {
      # e.g., read name djs233_2857.x1r2p5
      $readType = $customPrimer;
   }
   elsif( $afterDot =~ /^[a-z][0-9]+$/ ) {
      # e.g., read name djs233_1401.s1
      # is this true?  Cindy says "yes".
      $readType = $universalPrimer;
   }
   elsif( $afterDot =~ /^[a-z][0-9][\x7f]$/ ) {
      # e.g., read name djs77_3073.s1^?
      # Cindy says that these are all universal primer reads
      $readType = $universalPrimer;
   }
   elsif( $afterDot =~ /^[a-z][0-9]+_/ ) {
     if( $afterDot =~ /^[a-z][0-9]+_[0-9]+$/ ) {
       # e.g., read name djs233_1357.x1_7
       # is this true?  Cindy says yes.
       $readType = $customPrimer;
     }
     elsif( $afterDot =~ /^[a-z][0-9]+_[0-9]+[abc]$/ ) {
       # e.g., read name djs77_1377.x1_02b
       # is a custom primer, according to Cindy
       $readType = $customPrimer;
     }
     else {
       # e.g., read name djs10_776.x1_excellent
       $readType = $universalPrimer;
     }
   }
   else {
      print STDERR "unrecognized read type $phdFileName with afterDot $afterDot\n";
      print "afterDot length = ", length( $afterDot ), "\n";

      return( (1) );
   }
   

   $letterAfterDot = substr( $afterDot, 0, 1 );

   if ( $letterAfterDot =~ /[sfxzibtped]/ ) {
      $orientation = "fwd";
   }
   elsif( $letterAfterDot =~ /[ryg]/ ) {
      $orientation = "rev";
   }
   elsif( $letterAfterDot =~ /[ac]/ ) {
      # the cases that don't make sense
      $orientation = "fwd";
   }
   else {
      print STDERR "unknown orientation for read $phdFileName\n";
      return( (1) );
   }



   if ( $readType == $universalPrimer ) {
      if ( $orientation eq "fwd" ) {
         $primerType = "univ fwd";
      }
      else {
         $primerType = "univ rev";
      }
   }
   elsif ( $readType == $customPrimer ) {
      if ( $templateType eq "pcr" ) {
         $primerType = "pcr end";
      }
      else {
         $primerType = "walk";
      }
   }
   else {
      die "read $phdFileName should either be a custom primer or universal primer";
   }


  return( (0, $templateName, $templateType, $primerType) );
}
   





# A simple routine to determine template name and type.
# Written so that it's easy to understand for non-Perl programmers.
# More forgiving than other naming schemes - only restrictions are
# that names cannot start with a dot or an underscore.
# Rules: 
# a. The letter after the first dot indicates primer type & direction:
#    fsx  means forward universal primer
#    rgy  means reverse universal primer
#    w    means walking primer
# b. If a name contains "_bac", "_cos", or "_pcr", its from a BAC,
#    cosmid, or PCR template.
# c. PCR templates (identified by "_pcr") are assumed to be 
#    PCR end sequences.
# d. Everything not identified by the rules above is assumed to be
#    the default (universal forward reads from M13 or plasmid templates).
# e. The template name is the everything before the first dot.

sub findTemplateNameAndTemplateTypeCodonCodeFormat {
  # This routine must be called with exactly one argument ($#_ == 0)
  if ($#_ != 0 ) {
     die "usage: &findTemplateNameAndTemplateTypeCodonCodeFormat( <phd file name> )";
  }
  
  my $phdFileName = $_[0];
  # remove everything after the first underscore from the template name?
  my $removeUnderscorePart = 0; # 0 = don't remove; change to 1 to remove.
  
  my $readName;	           # the name without the ".phd.n" extension
  my $nameUpToDot;         #	the name up to the first "." 
  my $nameAfterDot;        # the name after the first "." (but without the .phd.1)
  my $letterAfterDot;      # the first letter after the first "." (but without the .phd.1)
  my $templateName;
  my $templateType;
  my $primerType;

  
  # Defaults for template type and  primer type
  my $defaultTemplateType = "";             # "" means M13 or plasmid
  my $defaultPrimerType   = "univ fwd";     # 


  $readName = $phdFileName;
  
  # Strip the ".phd.1" (or .phd.2, etc) extension
  $readName  =~ s/\.phd\.[0-9]+$//;

  # Get the name parts - up to the first dot & after the first dot
  $firstDot = index( $readName, "." );
  if ( $firstDot == 0 ) {   # name starts with a dot - that's not allowed!
    print STDERR "*** Error: Filename must not start with a dot in phd file '$phdFileName'\n";
    return( (1) );  
  } elsif ( $firstDot == -1 ) {   # no do in the name
    $nameUpToDot    = $readName;
    $nameAfterDot   = "";
    $letterAfterDot = "";
  } else {
    $nameUpToDot    = substr($readName, 0, $firstDot);
    $letterAfterDot = substr($readName, $firstDot + 1, 1);
    $nameAfterDot   = substr($readName, $firstDot + 1, length($readName) - $firstDot - 1);
  }
  
  # Verify that the name does not start with an underscore:
  $firstUnderscore = index( $nameUpToDot, "_" );
  if ( $firstUnderscore == 0 ) {   # name starts with a '_' - that's not allowed!
    print STDERR "*** Error: Filename must not start with an underscore in phd file '$phdFileName'\n";
    return( (1) );  
  }
  
  
  # 1. Determine the template type.
  # Sequences from BAC templates contain a "_bac" somewhere in the name,
  # sequences from cosmid templates a "_cos",
  # from PCR products a "_pcr".
  # Everything else is M13 or plasmid (which are currently treated the same)
  my $lookIn;
  $lookIn = $readName;          # if you want to allow _bac etc. anywhere in the name
  # $lookIn = $nameUpToDot;       # if you want to allow _bac etc. only before the first dot
  # $lookIn = $nameAfterDot;      # if you want to allow _bac etc. only after the first dot
  if        (index( $lookIn, "_bac" ) > -1) {
         $templateType = "bac";
  } elsif   (index( $lookIn, "_cos" ) > -1) {
         $templateType = "cosmid";
  } elsif   (index( $lookIn, "_pcr" ) > -1) {
         $templateType = "pcr";
  } else {
         $templateType = $defaultTemplateType;
  }
  
  
  # 2. Extract the template name.
  # That's everything before the first dot.
  # if the $removeUnderscorePart flag is set and
  # there's an underscore in the name, we use only the part 
  # before the first underscore.
  if ( ($removeUnderscorePart > 0) && ($firstUnderscore > 0) ) {
    $templateName = substr($nameUpToDot, 0, $firstUnderscore);
  } else {
    $templateName = $nameUpToDot;
  }


  # 3. Determine whether it's a universal primer read
  # or a walking primer.
  # If it's a universal primer read, also determine the orientation.
  # Universal forward primer reads have one of the letters
  #    'f', 's', or 'x' after the first dot;
  # Universal reverse primer reads have one of the letters
  #    'r', 'g', or 'y' after the first dot
  # Walking primers have a 'w' after the first dot.
  # PCR templates are assumed to be "pcr end" sequences.
  # Every other letter reverts to the default setting, as set
  #  at the top of this subroutine.
  
  if ( $letterAfterDot =~ /[fsxFSX]/ ) {
      $primerType = "univ fwd";
  } elsif ( $letterAfterDot =~ /[rygRYG]/ ){
      $primerType = "univ rev";
  } elsif ( $letterAfterDot =~ /[wW]/ ){
      $primerType = "walk";
  } elsif ( $templateType eq "pcr" ) {
      $primerType = "pcr end";
  } else {
      $primerType = $defaultPrimerType;
  }

  return( (0, $templateName, $templateType, $primerType) );
}




#
# S. Zuyderduyn (31 Oct 2000; 1 Dec 2000)
# For transposon reads:
#    From D3700 sequencers: 
#       TL0016c.BA_G03___024.ab1 
#       TL0016a.BB_H11___096.ab1
#    From MegaBACE sequencers: 
#       TL0018bD01.EA.abd 
#       TL0011aC03.EB.abd
#    (The 'TL' at the beginning indicates that the read is not either
#    a 3' nor a 5' cDNA read--it is a read that starts somewhere within
#    the cDNA.)
# 
# For 3' cDNA reads:
#    From D3700 sequencers: 
#       LL0052c.B7_H06___060.ab1 
#       LL0051c.B7_E09___071.ab1
#    From MegaBACE sequencers: 
#       LL0053dF10.E7.abd 
#       LL0052cD09.E7.abd
#    (The '7' indicates it is on the 3' end of the cDNA.)
# 
# For 5' cDNA reads:
#    From D3700 sequencers: 
#       LL0052d.B21_H11___095.ab1 
#       LL0051a.B21_A02___017.ab1
#    From MegaBACE sequencers: 
#       LL0051aH06.E21.abd 
#       LL0052bA12.E21.abd
#    (The '21' indicates it is on the 5' end of the cDNA.)


sub findTemplateNameAndTemplateTypeVancouverFormat {
	die "usage: &findTemplateNameAndTemplateTypeVancouverFormat( <phd file name> );"
	if( $#_ != 0 );

	my $phdFileName = $_[0];

	my $readName;
	( $readName = $phdFileName ) =~ s/\.phd\.[0-9]+$//;

	$firstDot = index( $readName, "." );
	if( $firstDot == -1 ) {
		print STDERR "Can't figure out template and primer type for $phdFileName since no dot in filename\n";
		return(  (1) );
	}

	if( $firstDot == length( $readName ) ) {
		print STDERR "Can't figure out template and primer type for $phdFileName since no letter after the dot\n";
		return( (1) );
	}

	my $nameUpToDot = substr( $readName, 0, $firstDot );

	my $templateName;
	my $chemTag;
	my $library;

	if( $phdFileName =~ /^(\w{5})(\w+\w{3})\.(\w+)\.\d?\d?\.?abd\.phd\.\d+$/ ) {
		# file is of format like TL05A8dF03.EA.abd.phd.1 or TL05A8dF03.EA.1.abd.phd.1
		$library = $1;
		$templateName = "$1$2";
		$chemTag = $3;

	} elsif( $phdFileName =~ /^(\w{5})(\w+)\.(\w+)\.?\d?\d?_(\w+)___\d+\.ab1\.phd\.\d+$/ ) {
		# file is of format like TL05A8d.BA_F03___031.ab1.phd.1 or TL05A8d.BA.1_F03___031.ab1.phd.1
		$library = $1;
		$templateName = "$1$2$4";
		$chemTag = $3;
	} else {
		print "Can't figure out file format!\n";
		die "\n";
	}

	$chemTag =~ /^(\w)(\w+)/;

	my $sub_chemtype = $1;
	my $priming_site = $2;

	my $primerType;

	if( $priming_site eq "7" ) {
		$primerType = "univ fwd";
	} elsif( $priming_site eq "21" ) {
		$primerType = "univ rev";
	} elsif( $priming_site =~ /^A/ ) {
		$primerType = "walk";
	} elsif( $priming_site =~ /^B/ ) {
		$primerType = "walk";
	} else {
		print "Can't figure out primer information from tag: \"$chemTag\"!\n";
		die "\n";
	}

	my $templateType = "pcr";

	# kludge for transposon cDNA
	if( $phdFileName =~ /^TL/ )
	{
		$primerType = "walk";
		#$templateName = "TLxxx"; #$library;	# "TL05A"
		$templateType = "bac";
	}

	return( (0, $templateName, $templateType, $primerType) );
}





sub bIsPhdFileAlreadyProcessed {
   die "usage: &bIsPhdFileAlreadyProcessed( <phd filename> );" 
     if ( $#_ != 0 );
          
   my $szPhdFile = $_[0];
   my $szFullPath = $phdDirPath . "/" . $szPhdFile;
          
   $bSuccess = open( filPhd, $szFullPath );
   if (!$bSuccess ) {
      print STDERR "couldn't open $szFullPath\n";
      # don't try to process this file--something is wrong with it
      return 1;
   }
          
   while( <filPhd> ) {
      if (/^END_DNA/ ) {
         last;
      }
   }

   while( <filPhd> ) {
      if ( /^WR\{/ ) {
         if ( /^WR\{[\s]*$/ ) {
            # found a whole read item
            # let's see if it is a primer or template 
            defined( $_ = <filPhd> ) || die "premature end of phd file $szPhdFile while inside a WR{ item";

            @aWords = split;

            if ( ( $aWords[0] eq "primer" ) ||
                 ( $aWords[0] eq "template" ) ) {
               return( 1 );
            }
         }
      }
   }

   
   # if reached here, there must be no whole read item of type primer or template

   return( 0 );
}



          
          
   
   

   
      
sub szGetDateForTag {
  my $szDate;
  ($nSecond, $nMinute, $nHour, $nDayInMonth, $nMonth, $nYear, $wday, $yday, $isdst ) = localtime;

  undef $isdst;
  undef $yday;
  undef $wday;
  
  if ( $nYear >= 100 ) {
    $nYear = $nYear % 100;
  }

  $szDate = sprintf( "%02d%02d%02d:%02d%02d%02d",
           $nYear,
           $nMonth + 1,
           $nDayInMonth,
           $nHour,
           $nMinute,
           $nSecond );

  return( $szDate );
}


sub processOnePhdFile {

   die "usage: &processOnePhdFile( <phd filename> )"
     if ( $#_ != 0 );


   my $phdFileName = $_[0];

   my $templateName;
   my $templateType;
   my $primerType;

   
   $autofinishExpID = "";
   $libraryName = "";


   # This is the line that determines which format is used.  You
   # can change it to a different format, if you wish.

   ($bErrorFlag, $templateName, $templateType, $primerType ) = &findTemplateNameAndTemplateTypeStLouisFormat( $phdFileName );

   return if ( $bErrorFlag );
   
   if ( $bJustView ) {
     print "read $phdFileName is $primerType template: $templateName $templateType library is $libraryName\n";
   }
   else {

     $szDate = &szGetDateForTag();

     my $szPhdFileFullPath = $phdDirPath . "/" . $phdFileName;

     open( filPhd, ">>$szPhdFileFullPath" ) || die "couldn't open $szPhdFileFullPath for append";

     print( filPhd "\n" );
     print( filPhd "WR{\n" );
     print( filPhd "template determineReadTypes $szDate\n" );
     print( filPhd "name: $templateName\n" );
     
     if ( $libraryName ne "" ) {
       print( filPhd "lib: $libraryName\n" );
     }
     if ( $templateType ne "" ) {
       print( filPhd "type: $templateType\n" );
     }
     print( filPhd "}\n" );
 
     print( filPhd "\n" );
     print( filPhd "WR{\n" );
     print( filPhd "primer determineReadTypes $szDate\n" );
     print( filPhd "type: $primerType\n" );
     print( filPhd "}\n" );
     print( filPhd "\n" );
     if ($autofinishExpID ne "" ) {
       print( filPhd "WR{\n" );
       print( filPhd "expid determineReadTypes $szDate\n" );
       print( filPhd "$autofinishExpID\n" );
       print( filPhd "}\n" );
     }
     close( filPhd );
   }
}





