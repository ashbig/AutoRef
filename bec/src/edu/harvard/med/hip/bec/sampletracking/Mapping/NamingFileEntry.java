/*
 * NamingFileEntry.java
 *
 * Created on April 24, 2003, 4:04 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;
import java.util.*;
import java.io.*;
/**
 *
 * @author  htaycher
 */
public class NamingFileEntry
{
    public static final String ORIENTATION_FORWARD = "F";
    public static final String ORIENTATION_REVERSE = "R";
    private static final String DILIM = "_";
    private static final String FILE_PATH = "/tmp/";
    
    private int     m_plateid = -1;
    private int     m_wellid = -1;
    private int     m_cloneid = -1;
    private int     m_sequenceid = -1;
    private int     m_readnum = -1;
    private String  m_orientation = null;
    
    private static String  m_filePath =FILE_PATH;
       
        public NamingFileEntry(int cloneid,String orientation,int  plateid, int wellid, int sequenceid,
        int readnum)
        {
            m_plateid = plateid;
            m_wellid = wellid;
            m_cloneid = cloneid;
            m_sequenceid = sequenceid;
            m_readnum = readnum;
            m_orientation = orientation;

        }
        
       
       public int     getPlateId (){ return m_plateid  ;}
        public int     getWellId (){ return m_wellid  ;}
        public int     getCloneID (){ return m_cloneid  ;}
        public int     getSequenceId (){ return m_sequenceid  ;}
        public int     getReadNum (){ return m_readnum  ;}
        public String  getOrientation (){ return m_orientation  ;}
     
        public String toString()
        {
            return m_plateid+DILIM+m_wellid+DILIM+m_sequenceid+DILIM+m_cloneid+"."+m_orientation+m_readnum;
        }
        
        //function writes robot file
    // source plate barcode, sourcewell, destination plate barcode, destination well
    public static File createNamingFile(ArrayList file_entries, String filePath) throws IOException
    {
        m_filePath = filePath;
        return createNamingFile( file_entries);
    }
    public static File createNamingFile(ArrayList file_entries) throws IOException
    {
        
        
        File fl = null;
        String temp = null;
        FileWriter fr = null;
        if (m_filePath == null) m_filePath = FILE_PATH;
        try{
            for (int count = 0; count < file_entries.size(); count++)
            {
                NamingFileEntry fe= (NamingFileEntry) file_entries.get(count);
                if (count == 0)
                {
                    fl =   new File(m_filePath + fe.getPlateId()+ ".txt");
                    fr =  new FileWriter(fl);
                    fr.write("Plate number"+DILIM+"well"+DILIM+"sequence id"+ DILIM+  "clone id\n");
                }

                fr.write(fe.toString()+"\n");

            }
            fr.flush();
            fr.close();
             return fl;
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
        return null;
    }
    
    
    
    
}
