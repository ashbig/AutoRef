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
   
    
    
    /** Creates a new instance of PolymorphismFinder */
    public   void    readInputFileAndStartJobs(String file_name, int number_of_discrepancies_in_one_job)
    {
        BufferedReader input = null;
        ArrayList error_messages = new ArrayList();
        String[] discrepancies_per_job = new String[number_of_discrepancies_in_one_job];
        String line = null; int count = 0;
        PolymorphismFinderJob job = null;
        try
        {
           File job_file = new File(file_name);
           if ( ! job_file.exists() ) return;
           input = new BufferedReader(new FileReader(file_name));
           while ((line = input.readLine()) != null)
           {
               
               discrepancies_per_job[count] = line;
               count++;
               if ( count == number_of_discrepancies_in_one_job - 1)
               {
                   // create new job
                   count = 0;
   ////@@@@@@@@@@@@@@@???????????????   start new bk job                
                //   job = new PolymorphismFinderJob(discrepancies_per_job);
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
            if ( args.length == 2)
            {
                number_of_discrepancies_in_one_job = Integer.parseInt(args[1]);
            }
        }
    }
    
}
