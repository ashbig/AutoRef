/*
 * PolymorphismFinder.java
 *
 * Created on June 14, 2005, 12:26 PM
 */

package edu.harvard.med.hip.bec.programs.polymorphism_finder;

import java.util.*;
import java.io.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public class PolymorphismFinderJobDistributor
{
    private int         m_number_of_discrepancies_in_one_job = 500;
   
    public PolymorphismFinderJobDistributor(){}
    public void         setNumberOfDiscrepanciesInJob(int i){ m_number_of_discrepancies_in_one_job=i;}
    
    
    
    
    /** Creates a new instance of PolymorphismFinder */
    public   void    readInputFileAndStartJobs()
    {
        BufferedReader input = null;
        ArrayList error_messages = new ArrayList();
        String[] discrepancies_per_job = new String[m_number_of_discrepancies_in_one_job];
        String line = null; int count = 0;
        PolymorphismFinderJob job = null;
        try
        {
           input = new BufferedReader(new FileReader(Utils.getSystemProperty("INPUT_DISCREPANCY_DTATA")));
           while ((line = input.readLine()) != null)
           {
               
               discrepancies_per_job[count] = line;
               count++;
               if ( count == m_number_of_discrepancies_in_one_job - 1)
               {
                   // create new job
                   count = 0;
   ////@@@@@@@@@@@@@@@???????????????   start new bk job        how        
                   job = new PolymorphismFinderJob();
                   job.run_job(discrepancies_per_job);
                  
               }
                  
           }
        }
        catch(Exception e)
        {
            error_messages.add(e.getMessage());
        }
        finally
        {
      ////@@@@@@@@@@@@@@@???????????????   send error messages               
                
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        // first argument(requered) path to input file
        // second arg - number of discrepancies in one job
        if ( args.length < 1) return;
        else 
        {
            int number_of_discrepancies_in_one_job = 500;
              File check_file = new File( args[1]);     if ( ! check_file.exists() ) return;
            check_file = new File( args[2]);     if ( ! check_file.exists() ) return;
            check_file = new File( args[3]);     if ( ! check_file.exists() ) return;
            check_file = new File( args[4]);     if ( ! check_file.exists() ) return;
            
            
            PolymorphismFinderJobDistributor job_distributor =new   PolymorphismFinderJobDistributor();
          
            try
            {
                Utils.getSystemProperties(args[0]);
            }
            catch(Exception e){}
            if ( args.length == 2)
            {
                job_distributor.setNumberOfDiscrepanciesInJob( Integer.parseInt(args[1]));
            }
            job_distributor.readInputFileAndStartJobs();
        }
    }
    
}
