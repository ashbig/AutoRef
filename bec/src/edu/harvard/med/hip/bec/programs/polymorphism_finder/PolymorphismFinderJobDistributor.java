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
        Hashtable orf_index = null;
        PolymorphismFinderJob job = null;
        try
        {
           orf_index = readORFIndexInMemory();
           if (orf_index == null || orf_index.size() == 0)
           {
               error_messages.add("Cannot get ORF index");
               return;
           }
           input = new BufferedReader(new FileReader(Utils.getSystemProperty("INPUT_DISCREPANCY_DATA_FILENAME")));
           while ((line = input.readLine()) != null)
           {
               
               discrepancies_per_job[count] = line;
               count++;
               if ( count == m_number_of_discrepancies_in_one_job - 1 )
               {
                   // create new job
                   count = 0;
   ////@@@@@@@@@@@@@@@???????????????   start new bk job        how        
                   job = new PolymorphismFinderJob();
                   job.run_job(discrepancies_per_job, orf_index);
                  
               }
                  
           }
           if ( count > 0)
           {
                job = new PolymorphismFinderJob();
                   job.run_job(discrepancies_per_job, orf_index);
           
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
    
    
    
     private   Hashtable readORFIndexInMemory( ) throws Exception
    {
        long[] coordinates = {-1,-1};
        Hashtable ORF_index = new Hashtable();
        int id = 0; long coord1 =0 ; long coord2 =0;
          
        FileInputStream fis = null;
        DataInputStream dis = null;
        try
        {
            File fn = new File(Utils.getSystemProperty("INPUT_ORF_INDEX_FILENAME"));
            fis = new FileInputStream( fn);
            dis = new DataInputStream( fis );
            while ( true )
            {
                id = dis.readInt();
               coordinates[0] = dis.readLong();
                coordinates[1] = dis.readLong();
                ORF_index.put(String.valueOf(id), coordinates);
                coordinates = new long[2];
            }
            
        }
        catch (Exception eof)
        {  }
        
        return ORF_index;
     }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
        // first argument(requered) path to input file
        // second arg - number of discrepancies in one job
        String[] ar={"C:\\BEC\\bec\\src\\edu\\harvard\\med\\hip\\bec\\programs\\polymorphism_finder\\ModuleSettings.properties", "200"};
        if ( ar.length < 1) return;
        else 
        {
            int number_of_discrepancies_in_one_job = 500;
             
            PolymorphismFinderJobDistributor job_distributor =new   PolymorphismFinderJobDistributor();
          
            try
            {
                Utils.getSystemProperties(ar[0]);
            }
            catch(Exception e){System.out.println("Cannot load properties");}
            if ( ar.length == 2)
            {
                job_distributor.setNumberOfDiscrepanciesInJob( Integer.parseInt(ar[1]));
            }
            job_distributor.readInputFileAndStartJobs();
        }
    }
    
}
