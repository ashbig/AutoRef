/*
 * TraceFileNameFormatParser.java
 *
 * Created on March 31, 2005, 3:16 PM
 */



package edu.harvard.med.hip.bec.programs.parsers;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;
import java.io.*;
import java.util.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;

/**
 *
 * @author  htaycher
 */
public class TraceFileNameFormatParser extends DefaultHandler
{
    private static final String FORMAT_START = "format";
    private static final String FORMAT_NAME = "FORMAT_NAME";
    private static final String PLATE_SEPARATOR = "PLATE_SEPARATOR";

    private static final String PLATE_LABEL_COLUMN = "PLATE_LABEL_COLUMN";
    private static final String PLATE_LABEL_START = "PLATE_LABEL_START";
    private static final String PLATE_LABEL_LENGTH = "PLATE_LABEL_LENGTH";
   private static final String POSITION_SEPARATOR = "POSITION_SEPARATOR";
  
    private static final String POSITION_COLUMN ="POSITION_COLUMN";
    private static final String POSITION_LENGTH = "POSITION_LENGTH";
    private static final String POSITION_START="POSITION_START"; 
    private static final String DIRECTION_FORWARD ="DIRECTION_FORWARD";
    private static final String DIRECTION_REVERSE = "DIRECTION_REVERSE";
    private static final String DIRECTION_START = "DIRECTION_START";
    private static final String DIRECTION_COLUMN = "DIRECTION_COLUMN";
     private static final String DIRECTION_SEPARATOR = "DIRECTION_SEPARATOR";

     
   private static final int FORMAT_START_STATUS =   1 ;
    private static final int FORMAT_NAME_STATUS =   2 ;
    private static final int PLATE_SEPARATOR_STATUS =   3 ;

    private static final int PLATE_LABEL_COLUMN_STATUS =   4 ;
    private static final int PLATE_LABEL_START_STATUS =   5 ;
    private static final int PLATE_LABEL_LENGTH_STATUS =   6 ;
     
    private static final int POSITION_COLUMN_STATUS =  7 ;
    private static final int POSITION_LENGTH_STATUS =   8 ;
    private static final int POSITION_START_STATUS =  9 ; 
    private static final int DIRECTION_FORWARD_STATUS =  10 ;
    private static final int DIRECTION_REVERSE_STATUS =   11 ;
    private static final int DIRECTION_START_STATUS =   12 ;
    private static final int DIRECTION_COLUMN_STATUS =   13 ;
private static final int POSITION_SEPARATOR_STATUS =   14 ;
private static final int DIRECTION_SEPARATOR_STATUS =   15 ;
    
      private ArrayList i_formats = null;
      private TraceFileNameFormat i_current_format = null;
      private int  i_current_status = -1;
      
     
     public void startDocument(){ i_formats = new ArrayList();}

      public void startElement(String uri, String localName, String rawName,
                               Attributes attributes)
      {
         if (localName.equalsIgnoreCase(FORMAT_START) )
        {
            i_current_format = new TraceFileNameFormat();
            i_formats.add(i_current_format);
        }
        else  if (localName.equalsIgnoreCase(PLATE_SEPARATOR) )i_current_status = PLATE_SEPARATOR_STATUS;
         else  if (localName.equalsIgnoreCase(POSITION_SEPARATOR) )i_current_status = POSITION_SEPARATOR_STATUS;
        else  if (localName.equalsIgnoreCase(DIRECTION_SEPARATOR) )i_current_status = DIRECTION_SEPARATOR_STATUS;
        
         
        else  if (localName.equalsIgnoreCase(PLATE_LABEL_COLUMN ) )i_current_status =PLATE_LABEL_COLUMN_STATUS;
        else  if (localName.equalsIgnoreCase(PLATE_LABEL_START ) )i_current_status=PLATE_LABEL_START_STATUS;
        else  if (localName.equalsIgnoreCase(PLATE_LABEL_LENGTH ) )i_current_status=PLATE_LABEL_LENGTH_STATUS;
    
        else  if (localName.equalsIgnoreCase(FORMAT_NAME) )i_current_status = FORMAT_NAME_STATUS;
        else  if (localName.equalsIgnoreCase(POSITION_START ) )i_current_status =POSITION_START_STATUS;
        else  if (localName.equalsIgnoreCase(DIRECTION_REVERSE ) )i_current_status=DIRECTION_REVERSE_STATUS;
        else  if (localName.equalsIgnoreCase(DIRECTION_START ) )i_current_status=DIRECTION_START_STATUS;
        else  if (localName.equalsIgnoreCase(DIRECTION_COLUMN ) )i_current_status=DIRECTION_COLUMN_STATUS;
        else  if (localName.equalsIgnoreCase(DIRECTION_FORWARD ) )i_current_status=DIRECTION_FORWARD_STATUS;
        else  if (localName.equalsIgnoreCase(POSITION_LENGTH ) )i_current_status=POSITION_LENGTH_STATUS;
        else  if (localName.equalsIgnoreCase(POSITION_COLUMN)) i_current_status= POSITION_COLUMN_STATUS;
        
      }

     
      public void characters(char characters[], int start, int length)
      {
          String chData = (new String(characters, start, length)).trim();
           if (chData == null || chData.length() < 1) return;
         
          switch(i_current_status)
          {
             case  FORMAT_NAME_STATUS : { i_current_format.setFormatName (chData); break;}
            case  PLATE_SEPARATOR_STATUS : { i_current_format.setPlateSeparator(chData); break;}
            case  POSITION_SEPARATOR_STATUS : { i_current_format.setPositionSeparator(chData); break;}
            case  DIRECTION_SEPARATOR_STATUS : { i_current_format.setDirectionSeparator(chData); break;}

            case  PLATE_LABEL_COLUMN_STATUS : { i_current_format.setPlateLabelColumn(Integer.parseInt(chData)); break;}
            case  PLATE_LABEL_START_STATUS : { i_current_format.setPlateLabelStart(Integer.parseInt(chData)); break;}
            case  PLATE_LABEL_LENGTH_STATUS : { i_current_format.setPlateLabelLength(Integer.parseInt(chData)); break;}

            case  POSITION_COLUMN_STATUS : { i_current_format.setPositionColumn(Integer.parseInt(chData)); break;}
            case  POSITION_LENGTH_STATUS : { i_current_format.setPositionLength(Integer.parseInt(chData)); break;}
            case  POSITION_START_STATUS : { i_current_format.setPositionStart(Integer.parseInt(chData)); break;} 
            case  DIRECTION_FORWARD_STATUS : { i_current_format.setDirectionForward(chData); break;}
            case  DIRECTION_REVERSE_STATUS : { i_current_format.setDirectionReverse(chData); break;}
            case  DIRECTION_START_STATUS : { i_current_format.setDirectionStart(Integer.parseInt(chData)); break;}
            case  DIRECTION_COLUMN_STATUS : { i_current_format.setDirectionColumn(Integer.parseInt(chData)); break;}
        }
        
      }

      
    
      public ArrayList     getFormats(){ return i_formats;}

   public static void main(String[] args)
  {
     try{
         TraceFileNameFormatParser SAXHandler = new TraceFileNameFormatParser();
        SAXParser parser = new SAXParser();
        parser.setContentHandler(SAXHandler);
        parser.setErrorHandler(SAXHandler);
        InputStream m_input_stream = new FileInputStream("C:\\BEC\\bec\\docs\\ApplicationSetup\\TraceFileFormat.xml");
        parser.parse(new org.xml.sax.InputSource(m_input_stream));
                       
         ArrayList formats= SAXHandler.getFormats();
         String xml_file_name ="C:\\BEC\\bec\\docs\\ApplicationSetup\\TraceFileFormatReWrite.xml";
        TraceFileNameFormat format =new TraceFileNameFormat () ;
         format.setFormatName ("new");
format.setPlateSeparator (".");
format.setPositionSeparator ("_");//</SEPARATOR>
format.setDirectionSeparator ("_");//</SEPARATOR>
format.setPlateLabelColumn (1);//PLATE_LABEL_COLUMN>
format.setPositionColumn (3);//POSITION_COLUMN>
format.setDirectionForward ("F");//DIRECTION_FORWARD>
format.setDirectionReverse ("R");//DIRECTION_REVERSE>
format.setDirectionColumn (5);//DIRECTION_COLUMN>
         TraceFileNameFormat.writeXMLFile( xml_file_name, formats);
    
            
  }
  catch(Exception e){
     e.printStackTrace(System.err);
  }
   }


}
