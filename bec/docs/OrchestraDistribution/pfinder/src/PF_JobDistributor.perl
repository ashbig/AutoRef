#!/usr/bin/perl

$data_file=@ARGV[0];
$discr_number_in_one_job = @ARGV[1];
$temp_dir =  @ARGV[2];
$setting_file = @ARGV[3];


#read new job file
open(IN_DATA, $data_file) || die("Could not open input file! $data_file");
@raw_data=<IN_DATA>;
close(IN_DATA);

$discr_count = 0;
@discr_description_for_job;


$total_count=0;
foreach $discrepancy_description (@raw_data)
{
    chop($discrepancy_description);
    @discr_description_for_job=(@discr_description_for_job, $discrepancy_description);  
$total_count++;
    if ( $discr_count == $discr_number_in_one_job || $total_count == $#raw_data)
    {
       $random = int(rand( $2000000-1000 ) ) + 1000; 
  
        $new_job_data = "./polymorphismfinder/data/input_job_data".$random.".txt";
        $discr_count = 0;
        #write into new file
        open(DAT,">$new_job_data") || die("Cannot Open File");
       # print DAT @discr_description_for_job; 
        $ending_value = scalar(@discr_description_for_job) ;

        for($counter=0 ; $counter < $ending_value ; $counter++)
        {
            print DAT "$discr_description_for_job[$counter]\n" ;
        }


        close(DAT);
        @discr_description_for_job = ();

        system ("bsub -q shared_2h \"java -cp ./polymorphismfinder/ src.PolymorphismFinderJob ./polymorphismfinder/src/ModuleSettings.properties $new_job_data $random\"");
    }
    
    $discr_count++;
}






