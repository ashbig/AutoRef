/*
 * TraceFileNameFormat.java
 *
 * Created on March 31, 2005, 2:41 PM
 */

package edu.harvard.med.hip.bec.sampletracking.mapping;

import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import java.io.*;
import  edu.harvard.med.hip.bec.database.*;
import java.sql.*;
/**
 *
 * @author  htaycher
 */
public class TraceFileNameFormat 
{
    public static final int         READING_LEFT_TO_RIGHT = 0;
     public static final int         READING_RIGHT_TO_LEFT = 1;
    
    private int         m_id  =  BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private String      m_format_name = null;//FORMAT_NAME> 
    private String      m_plate_separator = null;//</SEPARATOR>
     private int        m_plate_label_column = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//PLATE_LABEL_COLUMN>
     private int        m_plate_label_start = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//PLATE_LABEL_START>
     private int        m_plate_label_length = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//PLATE_LABEL_LENGTH>
     private int        m_position_column = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//POSITION_COLUMN>
     private int        m_position_start = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//POSITION_START> 
     private int        m_position_length = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//POSITION_LENGTH>
     private String     m_direction_forward = "F";//DIRECTION_FORWARD>
     private String     m_direction_reverse = "R";//DIRECTION_REVERSE>
     private int        m_direction_column = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//DIRECTION_COLUMN>
     private int        m_direction_length = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//DIRECTION_START>
     private int        m_direction_start = BecIDGenerator.BEC_OBJECT_ID_NOTSET;//DIRECTION_START>
    private String      m_position_separator = null;//</SEPARATOR>
    private String      m_direction_separator = null;//</SEPARATOR>
    private int         m_direction_of_file_name_reading = READING_LEFT_TO_RIGHT;
    
    /** Creates a new instance of TraceFileNameFormat */
    public TraceFileNameFormat() {    }
    
    public int          getId(){ return m_id;}
     public String     getFormatName (){ return m_format_name    ;}//FORMAT_NAME> 
    public String       getPlateSeparator (){ return m_plate_separator    ;}//</SEPARATOR>
    public String       getPositionSeparator (){ return m_position_separator    ;}//</SEPARATOR>
    public String       getDirectionSeparator (){ return m_direction_separator    ;}//</SEPARATOR>
     public int         getPlateLabelColumn (){ return m_plate_label_column    ;}//PLATE_LABEL_COLUMN>
     public int         getPlateLabelStart (){ return m_plate_label_start    ;}//PLATE_LABEL_START>
     public int         getPlateLabelLength (){ return m_plate_label_length    ;}//PLATE_LABEL_LENGTH>
     public int         getPositionColumn (){ return m_position_column    ;}//POSITION_COLUMN>
     public int         getPositionStart (){ return m_position_start    ;}//POSITION_START> 
     public int         getPositionLength (){ return m_position_length    ;}//POSITION_LENGTH>
     public String      getDirectionForward (){ return m_direction_forward    ;}//DIRECTION_FORWARD>
     public String      getDirectionReverse (){ return m_direction_reverse    ;}//DIRECTION_REVERSE>
     public int         getDirectionColumn (){ return m_direction_column    ;}//DIRECTION_COLUMN>
     public int         getDirectionLength (){ return m_direction_length    ;}//DIRECTION_START>
     public int         getDirectionStart(){ return m_direction_start;}
     public int         getFileNameReadingDirection(){ return m_direction_of_file_name_reading;}
     
     
    public void         setFormatName (String v){   m_format_name    = v;}//FORMAT_NAME> 
      public void       setPlateSeparator (String v){   m_plate_separator   = v ;}//</SEPARATOR>
    public void       setPositionSeparator (String v){   m_position_separator  = v  ;}//</SEPARATOR>
    public void       setDirectionSeparator (String v){   m_direction_separator= v    ;}//</SEPARATOR>
   public void         setPlateLabelColumn (int v){   m_plate_label_column    = v;}//PLATE_LABEL_COLUMN>
    public void         setPlateLabelStart (int v){   m_plate_label_start    = v;}//PLATE_LABEL_START>
    public void         setPlateLabelLength (int v){   m_plate_label_length    = v;}//PLATE_LABEL_LENGTH>
    public void         setPositionColumn (int v){   m_position_column    = v;}//POSITION_COLUMN>
    public void         setPositionStart (int v){   m_position_start    = v;}//POSITION_START> 
    public void         setPositionLength (int v){   m_position_length    = v;}//POSITION_LENGTH>
    public void         setDirectionForward (String v){   m_direction_forward    = v;}//DIRECTION_FORWARD>
    public void         setDirectionReverse (String v){   m_direction_reverse    = v;}//DIRECTION_REVERSE>
    public void         setDirectionColumn (int v){   m_direction_column    = v;}//DIRECTION_COLUMN>
    public void         setDirectionLength (int v){   m_direction_length    = v;}//DIRECTION_START>
    public void         setDirectionStart(int v){ m_direction_start = v;}
     public void        setId(int v){ m_id = v;}
     public void        setFileNameReadingDirection(int v){  m_direction_of_file_name_reading = v;}
     
     
     
    public  String  toXML()
    {
        return toXML(this);
    }
    public static String  toXML(TraceFileNameFormat format)
    {
        StringBuffer sf = new StringBuffer();
        String separator = System.getProperty("line.separator");
        sf.append(Constants.TAB_DELIMETER + "<format>" + separator);
		sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<FORMAT_NAME>"+format.getFormatName()+"</FORMAT_NAME>" + separator);
                if ( format.getPlateSeparator()!= null)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<PLATE_SEPARATOR>"+format.getPlateSeparator()+"</PLATE_SEPARATOR>" + separator);
		if ( format.getPlateLabelColumn() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<PLATE_LABEL_COLUMN>"+format.getPlateLabelColumn()+"</PLATE_LABEL_COLUMN>" + separator);
		if ( format.getPlateLabelLength() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<PLATE_LABEL_START>"+format.getPlateLabelLength()+"</PLATE_LABEL_START>" + separator);
		if ( format.getPlateLabelLength() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<PLATE_LABEL_LENGTH>"+format.getPlateLabelLength()+"</PLATE_LABEL_LENGTH>" + separator);
		if ( format.getPositionSeparator()!= null)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<POSITION_SEPARATOR>"+format.getPositionSeparator()+"</PPOSITION_SEPARATOR>" + separator);
		
                if ( format.getPositionColumn() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<POSITION_COLUMN>"+format.getPositionColumn()+"</POSITION_COLUMN>" + separator);
		if ( format.getPositionStart() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<POSITION_START>"+format.getPositionStart()+"</POSITION_START>" + separator);
		if ( format.getPositionLength() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<POSITION_LENGTH>"+format.getPositionLength()+"</POSITION_LENGTH>" + separator);
		if ( format.getDirectionSeparator()!= null)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<DIRECTION_SEPARATOR>"+format.getDirectionSeparator()+"</DIRECTION_SEPARATOR>" + separator);
		
                sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<DIRECTION_FORWARD>"+format.getDirectionForward()+"</DIRECTION_FORWARD>" + separator);
		sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<DIRECTION_REVERSE>"+format.getDirectionReverse()+"</DIRECTION_REVERSE>" + separator);
		if ( format.getDirectionColumn() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<DIRECTION_COLUMN>"+format.getDirectionColumn()+"</DIRECTION_COLUMN>" + separator);
		if ( format.getDirectionStart() != BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                    sf.append(Constants.TAB_DELIMETER + Constants.TAB_DELIMETER + "<DIRECTION_START>"+format.getDirectionStart()+"</DIRECTION_START>" + separator);
	sf.append(Constants.TAB_DELIMETER + "</format>" + separator);

        return sf.toString();
    }
 
    
    public void insert (Connection conn)throws BecDatabaseException
    {
        Statement stmt = null;
        if (m_format_name == null || m_format_name.length()==0) return;
        String sql = null;
        try
        {
            if (m_id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
                     m_id = BecIDGenerator.getID("oligoid");
   
           sql = "INSERT INTO TRACEFILEFORMAT (  FORMATNAME,FORMATID ,PLATE_SEPARATOR,"
                 +" PLATE_LABEL_COLUMN ,PLATE_LABEL_START  ,PLATE_LABEL_LENGTH ,"
                 +" POSITION_COLUMN,POSITION_START  ,POSITION_LENGTH  ,DIRECTION_FORWARD "
                 +" ,DIRECTION_REVERSE  ,DIRECTION_COLUMN,DIRECTION_LENGTH  ," 
                 + "DIRECTION_START ,POSITION_SEPARATOR  ,DIRECTION_SEPARATOR, READING_DIRECTION ) values ('"
                 +m_format_name +"',"+m_id+",'"+m_plate_separator+"',"+m_plate_label_column+","+m_plate_label_start+
                 ","+m_plate_label_length+","+m_position_column+","+m_position_start+","+m_position_length
+",'"+m_direction_forward+"','"+m_direction_reverse+"',"+m_direction_column+","+m_direction_length+
","+m_direction_start+",'"+m_position_separator+"','"+m_direction_separator+"',"+m_direction_of_file_name_reading+")";
       // System.out.println(sql);
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } 
        catch (Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
   
    public static void    writeXMLFile(String xml_file_name, ArrayList formats)throws Exception
    {
        TraceFileNameFormat format = null;
         String separator = System.getProperty("line.separator");
       
        FileWriter format_file = new FileWriter(new File(xml_file_name));
        format_file.write("<?xml version='1.0' encoding='ISO-8859-1'?><!DOCTYPE web-app");
        format_file.write("PUBLIC '-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN'    'http://java.sun.com/dtd/web-app_2_3.dtd'>");
        format_file.write(separator+"<trace_file_format-info>"+separator);
        format_file.flush();    
        // straiforward 
        for (int count = 0; count < formats.size(); count++)
        {
            format = (TraceFileNameFormat)formats.get(count);
            format_file.write(format.toXML());
            
        }
      format_file.write(separator+"</trace_file_format-info>"+separator);
      format_file.flush();
      format_file.close();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
