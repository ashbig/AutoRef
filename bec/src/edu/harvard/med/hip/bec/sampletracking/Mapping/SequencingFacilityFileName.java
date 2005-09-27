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
    public final static int      SEQUENCING_FACILITY_KOLODNER = 4;
   
     
    private String          m_file_name = null;
    private String          m_plate_name = null;
    private String          m_well_name = null;
    private int             m_well_number = -1;
    private String          m_version = null;
    private String          m_orientation = null;
    private String          m_extention = null;
    private boolean         m_isProperName = false;
    
    private String          m_additional_info = null;
    
     public SequencingFacilityFileName(String file_name, TraceFileNameFormat format, boolean isEndReads)
    {
       if ( format == null ) return;
       m_file_name = file_name;
       m_extention = file_name.substring( file_name.lastIndexOf('.')+1);
       
       ArrayList items = null;
       String file_name_without_extention = file_name.substring(0,  file_name.lastIndexOf('.'));
       // extract plate
       m_plate_name = setField(format.getPlateSeparator(), format.getPlateLabelColumn(), 
                                format.getPlateLabelStart(), format.getPlateLabelLength(), 
                                format.getFileNameReadingDirection(),    file_name_without_extention);
       if ( m_plate_name == null )  return;
       
       //extract well
       m_well_name = setField(format.getPositionSeparator(), format.getPositionColumn(),  
                            format.getPositionStart(), format.getPositionLength(), 
                            format.getFileNameReadingDirection(), file_name_without_extention);
       if ( m_well_name == null ) return;
     
       try
       {
            m_well_number = edu.harvard.med.hip.bec.sampletracking.objects.Container.convertPositionFrom_alphanumeric_to_int(m_well_name);
            if ( m_well_number < 0 ){m_isProperName=false; return;}
       }
       catch(Exception e)
       {
           m_isProperName=false; return;
       }
       
       if ( isEndReads )
       {
           //extract direction
           m_orientation = setField(format.getDirectionSeparator(), format.getDirectionColumn(),
                        format.getDirectionStart(), format.getDirectionLength(), 
                        format.getFileNameReadingDirection(), file_name_without_extention);
           if ( m_orientation == null ) return;
           if (m_orientation.equalsIgnoreCase( format.getDirectionForward() ))m_orientation="F";
           else if (m_orientation.equalsIgnoreCase( format.getDirectionReverse() ))m_orientation="R";
       }
       else
           m_orientation="I";
       //verify
       if (m_plate_name == null || m_plate_name.trim().length()==0){m_isProperName=false; return;}
       if ( m_well_number < 0){m_isProperName=false; return;}
       if (isEndReads && !(m_orientation.equals("F") || m_orientation.equals("R") )) {m_isProperName=false; return;}
       
       m_isProperName=true; 
       
    }
     
            
     private String setField(String separator, int column_number,
                                int item_start, int item_length,
                                int read_direction, String file_name)
     {
           ArrayList items = null;
           String temp = null;
           if ( separator != null && separator.length() > 0)
           {
               items = Algorithms.splitString(file_name, separator);
               if (items != null && items.size()>0 && column_number > 0 && column_number - 1 < items.size() )
               {
                   temp = (String) items.get( column_number -1);
                   //System.out.println(column_number +" "+ items.size());
                   if ( item_length > 0)
                   {
                        if (read_direction == TraceFileNameFormat.READING_RIGHT_TO_LEFT)
                            return temp.substring( temp.length() - 1 - item_length);
                        else
                            return temp.substring(0, item_length);
                   }
                   else
                       return (String) items.get( column_number -1);
               }
               else{ return null;}
           }
           else
           {
                if ( file_name.length() < item_start + item_length ) return null;
                 if (read_direction == TraceFileNameFormat.READING_RIGHT_TO_LEFT)
                 {
                     temp=""; int start = -1; int stop = -1;
                     char[] name = file_name.toCharArray();
                     if ( item_length  < 1 )
                     {
                         temp = file_name.substring(0,  file_name.length() - item_start +1);
                     }
                     else 
                     {
                         temp = file_name.substring(file_name.length() - item_start  - item_length +1, file_name.length() - item_start +1);
                     }
                 }
                 else
                 {
                     if ( item_length < 1 ) temp =  file_name.substring(item_start - 1);
                     else temp = file_name.substring(item_start - 1,item_start - 1+ item_length);
                 }
                if ( temp != null && temp.length() > 0) return temp;
                else return null;
           }
           
     }
    /** Creates a new instance of BroadFileName */
  /*  public SequencingFacilityFileName(String file_name, int seq_facility_code)
    {
         switch ( seq_facility_code)
         {
             case SEQUENCING_FACILITY_BROAD : {broadFileName( file_name);break;}
             case SEQUENCING_FACILITY_AGENCORD :{agencortFileName(file_name);break;}
             case SEQUENCING_FACILITY_HTMBC :{htmbcFileName(file_name);break;}
             case SEQUENCING_FACILITY_KOLODNER: {kolodnerFileName(file_name); break;}
         }
   
    }
    */
    
   
    public String          getFileName (){ return m_file_name  ;}
    public String          getPlateName (){ return m_plate_name  ;}
    public String          getWellName (){ return m_well_name  ;}
    public int             getWellNumber (){ return m_well_number ;}
    public String          getVersion (){ return m_version  ;}
    public String          getOrientation (){ return m_orientation ;}
    public String          getExtension(){ return m_extention;}
    public boolean         isWriteFormat (){ return m_isProperName ;}
//used as temporary holder
     public void          setAdditionalInfo (String s){  m_additional_info =s  ;}
     public String        getAdditionalInfo ( ){return  m_additional_info  ;}
     
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
    
   
        
    /*
    
    ///D248P100FA1.T0.scf
    // assumption : broad name 8 letters/digits followed by F/R followed by well names
     private  void broadFileName(String broad_file_name)
     {
        int plate_name_end_f = 8; int plate_name_end_r = 0;
        int well_info_start = 0;
        m_file_name = broad_file_name;
        
       // plate_name_end_f = broad_file_name.indexOf("F");
        char orientation = broad_file_name.charAt(8);
        if ( broad_file_name.length() < 11 || !(orientation == 'R' || orientation =='r' 
            ||   orientation== 'F' || orientation =='f')) return;
        //if ( plate_name_end_f == -1 )
        if ( orientation == 'r' || orientation == 'R')
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
         // IP#_JasonWell_order_hipPlateName_HipWellNam`e_F/R_number.ab1
        ArrayList items = Algorithms.splitString(htmbc_file_name,"_");
         if ( items.size() != 7) return;
        m_file_name = htmbc_file_name;
        
        m_additional_info = (String)items.get(0);//run plate id
        m_well_name = (String)items.get(4);
        m_well_number = Algorithms.convertWellFromA8_12toInt(m_well_name);
        m_extention = htmbc_file_name.substring(htmbc_file_name.indexOf('.') + 1);
        m_orientation = ((String)items.get(5)).toUpperCase();
        char orientation_first_char = m_orientation.charAt( 0 );
        if (m_well_number != -1 && 
        ( orientation_first_char == 'I' || orientation_first_char == 'F' || 
            orientation_first_char == 'R' || orientation_first_char == 'G'))
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
      
      
      
      //YSG1438.379_A03.ab1
      
      private  void kolodnerFileName(String kolodner_file_name)
     {
      //YSG1438.379_A03.ab1   ->   hip plate name with 0 droped . oplate thread _ well . extension
       m_file_name = kolodner_file_name;
        int index_platename_separator = kolodner_file_name.indexOf('.');
        int index_well_separator = kolodner_file_name.indexOf('_');
        int index_extension_separator = kolodner_file_name.lastIndexOf('.');
        
        m_well_name = kolodner_file_name.substring(index_well_separator + 1,index_extension_separator);
        m_well_number = Algorithms.convertWellFromA8_12toInt(m_well_name);
        m_extention = kolodner_file_name.substring(index_extension_separator + 1);
      
        m_plate_name = kolodner_file_name.substring(0, index_platename_separator);
        m_orientation = "I";
        /*
         String tmp = kolodner_file_name.substring(0, index_platename_separator);
         if (tmp.length() < 9)// add 0 placeholders
        {
            char arr[] = tmp.toCharArray();m_plate_name="";
            for (int char_count = 0 ; char_count < tmp.length(); )
            {
                if ( char_count < 3 || m_plate_name.length() >= ( 9 -  tmp.length() + 3) )
                {
                    m_plate_name += arr[char_count];
                    char_count++;
                }
                else
                {
                    while (m_plate_name.length() <  (9 -  tmp.length() + 3) )
                    {
                        m_plate_name +=  '0';
                    }
                }
            }
        }
        else
            m_plate_name = tmp;*/
       // if (m_well_number != -1 &&    m_plate_name != null  )
         //    m_isProperName = true;
       
   // }
      
      
      public static void main(String args[])
    {
        SequencingFacilityFileName br= null;
              TraceFileNameFormat format = null;
    edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadTraceFileFormats();
       // br= new SequencingFacilityFileName("6356_h02_jlha_ksg001756_h02_i_002.ab1", edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getTraceFileFormat("test 2"), false);
        br= new SequencingFacilityFileName("YSG01438A03F.ab1", edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getTraceFileFormat("RL1"), true);
       br= new SequencingFacilityFileName("YSG01438A03F.ab1", edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getTraceFileFormat("RL"), true);
       br= new SequencingFacilityFileName("YSG01438A3F1.ab1", edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getTraceFileFormat("RL2"), true);
      
        
        //  br= new SequencingFacilityFileName("YSG01438.379_A03.ab1", edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getTraceFileFormat("Kolodner"), false);
  
       /* SequencingFacilityFileName br= new SequencingFacilityFileName("YSG1438.379_A03.ab1", SequencingFacilityFileName.SEQUENCING_FACILITY_KOLODNER);
        br= new SequencingFacilityFileName("YSG01438.379_A03.ab1", SequencingFacilityFileName.SEQUENCING_FACILITY_KOLODNER);
        br= new SequencingFacilityFileName("YSG001438.379_A03.ab1", SequencingFacilityFileName.SEQUENCING_FACILITY_KOLODNER);
     
      SequencingFacilityFileName br1= new SequencingFacilityFileName("D248P100FA1.T0.scf", SequencingFacilityFileName.SEQUENCING_FACILITY_BROAD);
      */
        System.exit(0);
    }
}
