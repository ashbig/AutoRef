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
/**
 *
 * @author htaycher
 */
public class CloneInfoVerificator extends Verifier
{
    
    /** Creates a new instance of CloneInfoVerificator */
    public CloneInfoVerificator(String fname) { super(fname);    }
    
    public void            checkCDSCoordinates( ArrayList er_messages, String cds_start_header, 
            String cds_stop_header, String cloneid)
    throws Exception
    {
        int cds_start_column_num = -1;
        int cds_stop_column_num = -1;
        int clone_id = -1;
      //  String column_name = ImportFlexSequence.PROPERTY_NAME_SPECIES;
        BufferedReader output = null ; 
        String line ; String[] items;
        // read author info and stor author 
        int cds_start ; int cds_stop;
        try
        {
            output = new BufferedReader(new FileReader(m_file_name));
            line = output.readLine();// read header
            cds_start_column_num = defineColumnNumber(line, cds_start_header);
            cds_stop_column_num = defineColumnNumber(line, cds_stop_header);
            clone_id = defineColumnNumber(line, cloneid);
            if (cds_start_column_num < 0 || cds_stop_column_num < 0 || clone_id < 0 )
            {
                er_messages.add("Cannot define cds stop/start column number "+m_file_name);
                return ;
            }
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                cds_stop = Integer.parseInt(items[cds_stop_column_num]);
                cds_start = Integer.parseInt(items[cds_start_column_num]);
                if ( (cds_stop - cds_start) % 3 != 2 )
                {
                    er_messages.add("Clone "+ items[clone_id]+"has problem with cds start/stop: " + cds_stop+" "+cds_start);
                }
            }
            output.close();
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot verify cds start , cds stop " + m_file_name);
        }
        finally
        {
            if (output != null) output.close();
        }
        
    }
    
    
    
     public ArrayList    getCloningStrategyInfo(ArrayList er_messages,
             String linker5_header, String vector_header, String linker3_header)throws Exception
    {
        ArrayList cloning_strategies = new ArrayList();
        int linker5_column_num = -1;
        int vector_column_num = -1;
        int linker3_column_num = -1;
      //  String column_name = ImportFlexSequence.PROPERTY_NAME_SPECIES;
        BufferedReader output = null ; 
        String line ; String[] items;
        // read author info and stor author 
        String cur_cloning_strategy;
        try
        {
            output = new BufferedReader(new FileReader(m_file_name));
            line = output.readLine();// read header
            linker5_column_num = defineColumnNumber(line, linker5_header);
            vector_column_num = defineColumnNumber(line, vector_header);
            linker3_column_num = defineColumnNumber(line, linker3_header);
            
            if (linker5_column_num < 0 || linker3_column_num < 0 || vector_column_num < 0)
            {
                er_messages.add("Cannot define cloning strategy columns "+m_file_name);
                return null;
            }
            while ( (line = output.readLine() ) != null)
            {
                items = line.split("\t");
                if ( linker5_column_num > items.length -1
                        || linker3_column_num > items.length -1 || vector_column_num > items.length -1 )
                {
                        er_messages.add("Cannot find species value in file "+m_file_name + "\n"+line);
                }
                else
                { 
                    cur_cloning_strategy = items[linker5_column_num].trim()+" "
                      +items[vector_column_num].trim()+" "+ items[linker3_column_num].trim();
                    if (! cloning_strategies.contains(cur_cloning_strategy))
                        cloning_strategies.add(cur_cloning_strategy);
                }
            }
            output.close();
            return cloning_strategies;
        }
        catch(Exception e)
        {
            throw new Exception ("Cannot read gene info file info file " + m_file_name);
        }
        finally
        {
            if (output != null) output.close();
        }
        
    }
    
    
}
