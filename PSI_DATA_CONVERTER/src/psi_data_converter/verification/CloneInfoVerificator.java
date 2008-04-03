/*
 * CloneInfoVerificator.java
 *
 * Created on February 5, 2008, 3:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.verification;

import java.util.*;
import java.io.*;

import psi_data_converter.filemanagment.*;
import psi_data_converter.util.*;

/**
 *
 * @author htaycher
 */
public class CloneInfoVerificator extends Verifier
{
  /*  public HashMap pr_soluble_values;
    public HashMap pr_expressed_values ;
    public HashMap pr_purified_values ;
    {
          SubmissionProperties subprop = SubmissionProperties.getInstance();
         String tmp =  subprop.getProperty("PR_EXPRESSED") ;
         String[] record = tmp.split(":");
          pr_expressed_values= new HashMap(record.length);
     
         for ( String item: record)         { pr_expressed_values.put(item, item);}
         
         tmp =  subprop.getProperty("PR_SOLUBLE") ;record = tmp.split(":");
         pr_soluble_values= new HashMap(record.length);
         for ( String item: record){ pr_soluble_values.put(item, item);}
         
         tmp =  subprop.getProperty("PR_PURIFIED") ;record = tmp.split(":");
         pr_purified_values= new HashMap(record.length);
         for ( String item: record){ pr_purified_values.put(item, item);}
       
    }*/
    
    
    /** Creates a new instance of CloneInfoVerificator */
    public CloneInfoVerificator(String fname) { super(fname);    }
    
    public void            checkCDSCoordinates( ArrayList er_messages, String cds_start_header, 
            String cds_stop_header, String cloneid,
               boolean mode_is_read_from_file, List<String[]> records ,
             String file_header)
   
    {
        String[] header_items  = file_header.split("\t");
   
       
        int cds_start_column_num = defineColumnNumber(file_header, cds_start_header);
        int     cds_stop_column_num = defineColumnNumber(file_header, cds_stop_header);
        int     clone_id = defineColumnNumber(file_header, cloneid);
         if (cds_start_column_num < 0 || cds_stop_column_num < 0 || clone_id < 0 
                )
        {
            er_messages.add("Cannot define cds stop/start column number "+m_file_name);
            return ;
        }
            
        if ( mode_is_read_from_file )
        {
            int[] header_items_index = new int[header_items.length ]   ;
            for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
            try
            {
                records =   FileManager.readFileIntoStringArray(m_file_name, header_items_index,"\t", true);
            }
            catch(Exception e){er_messages.add("Cannot read file: "+m_file_name); return;}
        }
        int cds_stop; int cds_start; 
        for ( String[] items: records)
        {
            if (cds_start_column_num > items.length-1 || cds_stop_column_num > items.length-1                )
            {
                er_messages.add("Clone information was not properly read "+m_file_name);
                return ;
            }
            cds_stop = Integer.parseInt(items[cds_stop_column_num]);
            cds_start = Integer.parseInt(items[cds_start_column_num]);
            if ( (cds_stop - cds_start) % 3 != 2 )
            {
                er_messages.add("Clone "+ items[clone_id]+"has problem with cds start/stop: " + cds_stop+" "+cds_start);
            }
        }
            
       
        
    }
    
    
    
     public ArrayList    getCloningStrategyInfo(ArrayList er_messages,
             String linker5_header, String vector_header, String linker3_header,
             boolean mode_is_read_from_file, List<String[]> records ,
             String file_header)
    {
        ArrayList cloning_strategies = new ArrayList();
         String[] header_items  = file_header.split("\t");
      //  String column_name = ImportFlexSequence.PROPERTY_NAME_SPECIES;
     
        String cur_cloning_strategy;
        int linker5_column_num = defineColumnNumber(file_header, linker5_header);
        int    vector_column_num = defineColumnNumber(file_header, vector_header);
        int    linker3_column_num = defineColumnNumber(file_header, linker3_header);
            
        if (linker5_column_num < 0 || linker3_column_num < 0 || vector_column_num < 0)
        {
            er_messages.add("Cannot define cloning strategy columns "+m_file_name);
            return null;
        }
            
        if ( mode_is_read_from_file )
        {
            int[] header_items_index = new int[header_items.length ]   ;
            for ( int count = 0; count < header_items.length; count++){ header_items_index[count]=count;}
            try
            {
                records =   FileManager.readFileIntoStringArray(m_file_name, header_items_index,"\t", true);
            }
            catch(Exception e){ er_messages.add("Cannot read file: "+m_file_name);}
        }
        for ( String[] items: records)
        {
            if ( linker5_column_num > items.length -1
                    || linker3_column_num > items.length -1 || vector_column_num > items.length -1 )
            {
                    er_messages.add("Problem with cloning strategy definition: check vector & linker columns "+m_file_name + "\n"+items[0]);
            }
            else
            { 
                cur_cloning_strategy = items[linker5_column_num].trim()+" "
                  +items[vector_column_num].trim()+" "+ items[linker3_column_num].trim();
                if (! cloning_strategies.contains(cur_cloning_strategy))
                    cloning_strategies.add(cur_cloning_strategy);
            }
        }
        return cloning_strategies;
     
    }
    
     
     public ArrayList getSpeciesNames(List<String[]> records, int species_column)
     {
         ArrayList species_names = new ArrayList();
         for (  String[] record : records)
         {
             if ( !species_names.contains( record[species_column]))
                 species_names.add(record[species_column]);
         }
         return species_names;
     }
    
}
