/*
 * BroadFileName.java
 *
 * Created on April 22, 2004, 12:19 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;
import java.util.*;
import java.io.*;

import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  HTaycher
 */
public class SequencingFacilityFileName
{
    public final static int      SEQUENCING_FACILITY_BROAD = 1;
    public final static int      SEQUENCING_FACILITY_AGENCORD = 2;
    public final static int      SEQUENCING_FACILITY_HTMBC = 3;
   
     
    private String          m_file_name = null;
    private String          m_plate_name = null;
    private String          m_well_name = null;
    private int             m_well_number = -1;
    private String          m_version = null;
    private String          m_orientation = null;
    private String          m_extention = null;
    private boolean         m_isProperName = false;
    
    /** Creates a new instance of BroadFileName */
    public SequencingFacilityFileName(String file_name, int seq_facility_code)
    {
         switch ( seq_facility_code)
         {
             case SEQUENCING_FACILITY_BROAD : {broadFileName( file_name);break;}
             case SEQUENCING_FACILITY_AGENCORD :{agencortFileName(file_name);break;}
             case SEQUENCING_FACILITY_HTMBC :{htmbcFileName(file_name);break;}
         }
   
    }
    
    
    
    public String          getFileName (){ return m_file_name  ;}
    public String          getPlateName (){ return m_plate_name  ;}
    public String          getWellName (){ return m_well_name  ;}
    public int             getWellNumber (){ return m_well_number ;}
    public String          getVersion (){ return m_version  ;}
    public String          getOrientation (){ return m_orientation ;}
    public String          getExtension(){ return m_extention;}
    public boolean         isWriteFormat (){ return m_isProperName ;}

    public static ArrayList sortByPlateWell(ArrayList file_names)
    {
           //sort array by cds length
        Collections.sort(file_names, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                SequencingFacilityFileName bfn1= (SequencingFacilityFileName)o1;
                SequencingFacilityFileName bfn2= (SequencingFacilityFileName)o2;
                int compare = bfn1.getPlateName().compareTo( bfn2.getPlateName());
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
    
    
    ///D248P100FA1.T0.scf
     private  void broadFileName(String broad_file_name)
     {
        int plate_name_end_f = 0; int plate_name_end_r = 0;
        int well_info_start = 0;
        m_file_name = broad_file_name;
        
        plate_name_end_f = broad_file_name.indexOf("F");
        if ( plate_name_end_f == -1 )
        {
            plate_name_end_r = broad_file_name.indexOf("R");
            m_orientation = "R";
            m_plate_name = broad_file_name.substring(0, plate_name_end_r).toUpperCase();
            well_info_start = 1 + plate_name_end_r;
        }
        else
        {
            m_orientation = "F";
            m_plate_name = broad_file_name.substring(0, plate_name_end_f).toUpperCase();
            well_info_start = 1 + plate_name_end_f;
        }
        
        m_well_name = broad_file_name.substring(well_info_start, broad_file_name.indexOf('.'));
        m_well_number = Algorithms.convertWellFromA8_12toInt(m_well_name);
        m_version = broad_file_name.substring(broad_file_name.indexOf('.') + 1, broad_file_name.lastIndexOf('.'));
        m_extention = broad_file_name.substring(broad_file_name.lastIndexOf('.') + 1);
    
        if (m_well_number > 0 && m_plate_name != null &&  m_version != null &&  m_orientation != null)
            m_isProperName = true;
    }
     private  void htmbcFileName(String htmbc_file_name)
     {
         // IP#_JasonWell_order_hipPlateName_HipWellName_F/R_number.ab1
        ArrayList items = Algorithms.splitString(htmbc_file_name,"_");
         if ( items.size() != 7) return;
        m_file_name = htmbc_file_name;
        
        m_version = (String)items.get(0);//run plate id
        m_well_name = (String)items.get(4);
        m_well_number = Algorithms.convertWellFromA8_12toInt(m_well_name);
        m_extention = htmbc_file_name.substring(htmbc_file_name.indexOf('.') + 1);
        m_orientation = ((String)items.get(5)).toUpperCase();
        if (m_well_number != -1 && ( m_orientation.equalsIgnoreCase("F") || m_orientation.equalsIgnoreCase("R")))
             m_isProperName = true;
        
         m_plate_name = (String)items.get(3);
    }
//    PRG000548_A01_F.ab1
      private  void agencortFileName(String agnc_file_name)
     {
         ArrayList items = Algorithms.splitString(agnc_file_name,"_");
         if ( items.size() != 3) return;
        m_file_name = agnc_file_name;
        m_plate_name = (String)items.get(0);
        m_well_name = (String)items.get(1);
        m_well_number = Algorithms.convertWellFromA8_12toInt((String)items.get(1));
        m_extention = agnc_file_name.substring(agnc_file_name.indexOf('.') + 1);
    
      //  m_version = ;
        m_orientation = ((String)items.get(2)).substring(0,1).toUpperCase();
        if (m_well_number != -1 && ( m_orientation.equalsIgnoreCase("F") ||
        m_orientation.equalsIgnoreCase("R")))
             m_isProperName = true;

    }
      public static void main(String args[])
    {
        SequencingFacilityFileName br= new SequencingFacilityFileName("000000262271_H10_F.ab1", SequencingFacilityFileName.SEQUENCING_FACILITY_AGENCORD);
      SequencingFacilityFileName br1= new SequencingFacilityFileName("D248P100FA1.T0.scf", SequencingFacilityFileName.SEQUENCING_FACILITY_BROAD);
      
        System.exit(0);
    }
}
