/*
 * NamingFileEntry.java
 *
 * Created on April 24, 2003, 4:04 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;
import java.util.*;
import java.io.*;
import edu.harvard.med.hip.utility.*;
import edu.harvard.med.hip.bec.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
/**
 *
 * @author  htaycher
 */
public class NamingFileEntry
{
   
    private static final String DILIM = "_";
   
    private int     m_plateid = -1;
    private String  m_plate_label = null;
    private String  m_wellid = null;
    private int     m_well_number = -1;
    private int     m_cloneid = -1;
    private int     m_sequenceid = -1;
    private int     m_readnum = -1;
    private String  m_orientation = null;
    private int     m_read_type = -1;//internal/end read
    private String  m_file_name = null;
    
    private static String  m_filePath =Constants.getTemporaryFilesPath();
       
    public NamingFileEntry(){}
        public NamingFileEntry(int cloneid,String orientation,int  plateid, String wellid, int sequenceid,
        int readnum)
        {
            m_plateid = plateid;
            m_wellid = wellid;
            m_cloneid = cloneid;
            m_sequenceid = sequenceid;
            m_readnum = readnum;
            m_orientation = orientation;

        }
        
        public String   getPlateLabel(){ return   m_plate_label ;}
        public int      getWellNumber(){ return      m_well_number;}
        public int      getReadType(){ return  m_read_type;}
        public String   getFileName(){ return m_file_name ;}
        public int      getPlateId (){ return m_plateid  ;}
        public String   getWellId (){ return m_wellid  ;}
        public int     getCloneID (){ return m_cloneid  ;}
        public int     getSequenceId (){ return m_sequenceid  ;}
        public int     getReadNum (){ return m_readnum  ;}
        public String  getOrientation (){ return m_orientation  ;}
        
        
        public void   setPlateLabel(String v ){     m_plate_label = v ;}
        public void      setWellNumber(int v ){        m_well_number= v ;}
        public void      setReadType(int v ){    m_read_type= v ;}
        public void   setFileName(String v ){   m_file_name = v ;}
        public void      setPlateId (int v ){   m_plateid  = v ;}
        public void   setWellId (String v ){   m_wellid  = v ;}
        public void     setCloneID (int v ){   m_cloneid  = v ;}
        public void     setSequenceId (int v ){   m_sequenceid  = v ;}
        public void     setReadNum (int v ){   m_readnum  = v ;}
        public void  setOrientation (String v ){   m_orientation  = v ;}
        
        public static String       getOrientation(int v) 
        { 
            if ( v == Constants.ORIENTATION_FORWARD)
                return Constants.READ_DIRECTION_FORWARD;
            else if ( v == Constants.ORIENTATION_REVERSE)
                return Constants.READ_DIRECTION_REVERSE;;
            return "Not known";
        }
        public String toString()
        {
            if ( m_sequenceid != -1)
                 return m_plateid+DILIM+m_wellid+DILIM+m_sequenceid+DILIM+m_cloneid+DILIM+m_orientation+m_readnum+".ab1";
            else
                return m_plateid+DILIM+m_wellid+DILIM+"0"+DILIM+"0"+DILIM+m_orientation+m_readnum+".ab1";
        }
         public String getNamingFileEntryInfo()
        {
            if ( m_sequenceid != -1)
                 return m_plateid+DILIM+m_wellid+DILIM+m_sequenceid+DILIM+m_cloneid+DILIM+m_orientation+m_readnum;
            else
                return m_plateid+DILIM+m_wellid+DILIM+"0"+DILIM+"0"+DILIM+m_orientation+m_readnum;
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
        if ( file_entries == null || file_entries.size() < 1) return null;
        FileWriter fr = null; NamingFileEntry fe= null;
        if (m_filePath == null) m_filePath = Constants.getTemporaryFilesPath();
        
        try{
            fl =   new File(m_filePath);
            fr =  new FileWriter(fl);
            fr.write("Plate number"+DILIM+"well"+DILIM+"sequence id"+ DILIM+  "clone id\n");
             
            for (int count = 0; count < file_entries.size(); count++)
            {
                fe= (NamingFileEntry) file_entries.get(count);
                fr.write(fe.toString()+"\n");
            }
            fr.flush();
            fr.close();
             return fl;
        }
        catch(Exception e){ try { fr.close();}catch(Exception n){} }
        return null;
    }
    
    
     public static ArrayList sortByPlateLabelWell(ArrayList file_names)
    {
           //sort array by cds length
        Collections.sort(file_names, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                NamingFileEntry bfn1= (NamingFileEntry)o1;
                NamingFileEntry bfn2= (NamingFileEntry)o2;
                int compare = bfn1.getPlateLabel().compareTo( bfn2.getPlateLabel());
                if ( compare != 0 ) return compare;
                return ( bfn1.getWellNumber() - bfn2.getWellNumber() );
             }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)            {      return false;  }
            // compare
        } );
        return file_names;
    }
     public static ArrayList sortByPlateIdWell(ArrayList file_names)
    {
           //sort array by cds length
        Collections.sort(file_names, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                NamingFileEntry bfn1= (NamingFileEntry)o1;
                NamingFileEntry bfn2= (NamingFileEntry)o2;
                int compare = bfn1.getPlateId() -  bfn2.getPlateId();
                if ( compare != 0 ) return compare;
                return ( bfn1.getWellNumber() - bfn2.getWellNumber() );
             }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)            {      return false;  }
            // compare
        } );
        return file_names;
    }
    
}
