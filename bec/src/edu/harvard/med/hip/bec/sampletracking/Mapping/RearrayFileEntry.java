/*
 * RearrayFileEntry.java
 *
 *class represents entry for rearray file
 *fi;e format : org plate label |org well id| dest palte name | dest palte label
 * Created on April 2, 2003, 3:22 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;

import java.util.*;
import java.io.*;
/**
 *
 * @author  htaycher
 */
public class RearrayFileEntry
{
    private static final String DILIM = "\t";
    private static final String FILE_PATH = "/tmp/";
    
        private String  m_org_plate_label = null;
        private String  m_dest_plate_label = null;
        private int     m_org_well = -1;
        private int     m_dest_well = -1;
        private int     m_clone_id = -1;
        
        
        private static String  m_filePath = null;
       
        public RearrayFileEntry(int cloneid,String org,int  orgwellid,  String dest, int destwellid)
        {
            m_clone_id= cloneid;
            m_org_plate_label = org;
            m_dest_plate_label = dest;
            m_org_well = orgwellid;
            m_dest_well = destwellid;
        }
        
       
        public String           getOrgPlateLabel(){ return m_org_plate_label;}
        public int              getOrgWellId(){ return m_org_well;}
        public String           getDestPlateLabel(){ return m_dest_plate_label;}
        public int              getDestWellId(){ return m_dest_well;}
        public int              getCloneId(){ return m_clone_id;}
        public String           toString(String delim)
        {
            return m_clone_id + delim+m_org_plate_label+delim+m_org_well+delim+m_dest_plate_label+delim+m_dest_well;
        }
  
        
        //function writes robot file
    // source plate barcode, sourcewell, destination plate barcode, destination well
    public static File createRearrayFile(ArrayList file_entries, String filePath) throws IOException
    {
        m_filePath = filePath;
        return createRearrayFile( file_entries);
    }
    public static File createRearrayFile(ArrayList file_entries) throws IOException
    {
        
        File fl = null;
        String temp = null;
        FileWriter fr = null;
        if (m_filePath == null) m_filePath = FILE_PATH;
        try{
            for (int count = 0; count < file_entries.size(); count++)
            {
                RearrayFileEntry fe= (RearrayFileEntry) file_entries.get(count);
                if (count == 0)
                {
                    fl =   new File(m_filePath + fe.getDestPlateLabel()+ ".txt");
                    fr =  new FileWriter(fl);
                    fr.write("Original plate label"+DILIM+"Original plate well"+DILIM+"Destination plate label"+ DILIM+  "Destination plate well\n");
                }

                fr.write(fe.toString(DILIM)+"\n");

            }
            fr.flush();
            fr.close();
             return fl;
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
        return null;
    }
    
    
    
    
    
}
