/*
 * TraceFilesOrderFile.java
 *
 * Created on February 26, 2004, 3:57 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;

/**
 *
 * @author  HTaycher
 */

import java.util.*;
import java.io.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
/**
 *
 * @author  htaycher
 */
public class TraceFilesOrderFile
{
    //file format
    /*
     Clone Id 
     *flex sequence id
b.	Hip Plate Label
c.	Hip Well (A01)
     *plate id hip
d.	Hip trace file Name 
e.	Read type – Forward / Reverse
f.	Read location – Internal / End Read
g.	SF Plate Number/name
h.	SF well (A01)
i.	SF Trace file name
     **/

    
    
   

    private int     m_cloneid = -1;
    private int     m_sequenceid_hip = -1;
    private String  m_platelabel_hip = null;
    private int  m_plateid_hip = -1;
    private String  m_wellid_hip = null;  
    private String  m_filename_hip = null;

    private String  m_read_direction = null;//forward /reverse
    private String  m_read_type = null;//internal/end read

    private String  m_platenumber_sf = null;
    private String  m_wellid_sf = null;
    private String  m_filename_sf = null;

    
    private static String  m_filePath =Constants.getTemporaryFilesPath();
       
    public TraceFilesOrderFile()    
    {     
            m_cloneid = 0;
        m_sequenceid_hip = 0;
        m_platelabel_hip = "";
        m_plateid_hip = 0;
        m_wellid_hip = "";  
        m_filename_hip = "";

        m_read_direction = "";//forward /reverse
        m_read_type = "";//internal/end read

        m_platenumber_sf = "";
        m_wellid_sf = "";
        m_filename_sf = "";
    }
     
    public int     getCloneId () { return m_cloneid  ; }
    public int     getFlexSequenceId () { return m_sequenceid_hip  ; }
    public String  getHipPlateLabel () { return m_platelabel_hip  ; }
    public int  getHipPlateId () { return m_plateid_hip  ; }
    public String  getHipWellId () { return m_wellid_hip  ; }  
    public String  getHipFilename () { return m_filename_hip  ; }

    public String  getReadDirection () { return m_read_direction  ; }//forward /reverse
    public String  getReadType () { return m_read_type  ; }//internal/end read

    public String  getSFPlateLabel () { return m_platenumber_sf  ; }
    public String  getSFWellId () { return m_wellid_sf  ; }
    public String  getSFFilenae () { return m_filename_sf  ; }
    
    
    public void     setCloneId (int v) {   m_cloneid  = v ; }
    public void     setFlexSequenceId (int v) {   m_sequenceid_hip  = v ; }
    public void  setHipPlateLabel (String v) {   m_platelabel_hip  = v ; }
    public void  setHipPlateId (int v) {   m_plateid_hip  = v ; }
    public void  setHipWellId (String v) {   m_wellid_hip  = v ; }  
    public void  setHipFilename (String v) {   m_filename_hip  = v ; }
    public void  setReadDirection (String v) {   m_read_direction  = v ; }//forward /reverse
    public void  setReadType (String v) {   m_read_type  = v ; }//internal/end read
    public void  setSFPlateLabel (String v) {   m_platenumber_sf  = v ; }
    public void  setSFWellId (String v) {   m_wellid_sf  = v ; }
    public void  setSFFilenae (String v) {   m_filename_sf  = v ; }


       
     
        
        public String toString()
        {
           return  m_cloneid  + "\t"+ m_sequenceid_hip     + "\t"+ 
            m_platelabel_hip      + "\t"+  m_plateid_hip      + "\t"+  
            m_wellid_hip   + "\t"+  m_filename_hip     + "\t"+  m_read_direction   
            + "\t"+  m_read_type    + "\t"+  m_platenumber_sf      + "\t"+  m_wellid_sf 
            + "\t"+  m_filename_sf  ;
         }
        
        //function writes robot file
    // source plate barcode, sourcewell, destination plate barcode, destination well
    public static File createTraceFilesOrderFile(ArrayList file_entries, String filePath) throws IOException
    {
        m_filePath = filePath;
        return createTraceFilesOrderFile( file_entries);
    }
    public static File createTraceFilesOrderFile(ArrayList file_entries) throws IOException
    {
        
        
        File fl = null;
        String temp = null;
        if ( file_entries == null || file_entries.size() < 1) return null;
        FileWriter fr = null; TraceFilesOrderFile fe= null;
        if (m_filePath == null) m_filePath = Constants.getTemporaryFilesPath();
        
        try{
            fl =   new File(m_filePath + ((TraceFilesOrderFile) file_entries.get(0)).getHipPlateLabel()+ ".txt");
            fr =  new FileWriter(fl);
            fr.write("Clone Id\tFlexSequenceId\tHip Plate Label\tHip Plate Id\tHip Well Id\t" 
            + "Hip File Name\tRead Direction\tRead type\tSF Plate Label\tSF Well Id\tSF File name\n"); 
              
            for (int count = 0; count < file_entries.size(); count++)
            {
                fe= (TraceFilesOrderFile) file_entries.get(count);
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
