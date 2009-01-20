/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action.norgenrearray;

import java.util.*;
import java.io.*;

import org.apache.struts.action.*;
import org.apache.struts.upload.*;
/**
 *
 * @author htaycher
 */
public class NorgenLogFile {
    private ArrayList<NorgenLogFileRecord>  i_records= null;
     private ArrayList<NorgenDestinationPlate>  i_dest_plates=null;
     
    
    public NorgenLogFile()
    {
         
    }
    public void         parseLogFile(InputStream input ) throws Exception
    {
        String line;ArrayList recordItems;
        NorgenLogFileRecord record;
        
         BufferedReader in = new BufferedReader(new InputStreamReader(input));
         i_records = new ArrayList<NorgenLogFileRecord>();
         NorgenDestinationPlate plate=null;
         String cur_plate_label=null;
         i_dest_plates=new ArrayList<NorgenDestinationPlate>();
         while ((line = in.readLine())!= null)
         {
             record = new NorgenLogFileRecord(line);
             i_records.add(record);
             if (cur_plate_label == null || 
                     !cur_plate_label.equalsIgnoreCase(record.getDestLabel()))
             {
                 plate = new NorgenDestinationPlate ();
                 plate.setLabel(record.getDestLabel());
                 i_dest_plates.add(plate);
                 cur_plate_label = plate.getLabel();
             }
             plate.addWellRecord(record);
         }
         
    }
    
    public ArrayList<NorgenLogFileRecord>  getRecords(){ return i_records ;}
      public ArrayList<NorgenDestinationPlate>  getDestinationPlates(){ return i_dest_plates;}
     /////////////////////////////////////////////
   /* public String     verifyLogFile()
    {
        NorgenLogFileRecord prev_record=null;
        for (NorgenLogFileRecord record : i_records)
        {
            if (prev_record != null  && !prev_record.getDestLabel().equals(record.getDestLabel()))
            {prev_record=null;}
            if ( prev_record != null && i_all_wells_seq
                    && (record.getDestPosition()-prev_record.getDestPosition()!=1))
            {
                return "Wells are not in sequence: "+record.toString();
            }
            
            if ( record.getDestPosition() > i_max_number_of_wells_per_plate)
                return "Well number over the limit: "+record.toString();
            prev_record = record;
        }
        return null;
    }*/
      
       public static void main(String args[]) {
         try
         {
                  NorgenLogFile parced_file = new NorgenLogFile();
            String fn="c:\\bio\\test\\rearray.log";
            parced_file.parseLogFile(new FileInputStream(fn));
            ArrayList<NorgenDestinationPlate> dest_plates =parced_file.getDestinationPlates();
            
         }catch(Exception e){}
         System.exit(0);
     }
     
}
