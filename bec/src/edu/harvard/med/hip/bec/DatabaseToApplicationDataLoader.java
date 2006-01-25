/*
 * DataLoader.java
 *
 * Created on January 18, 2005, 3:57 PM
 */


package edu.harvard.med.hip.bec;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.util.*;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.apache.xerces.parsers.SAXParser;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.programs.parsers.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
import edu.harvard.med.hip.bec.coreobjects.endreads.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.modules.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.util_objects.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;
import edu.harvard.med.hip.bec.file.*;



/**
 *
 * @author  htaycher
 *load additiona definitions
 */
public class DatabaseToApplicationDataLoader 
{
    public   static  final int SPECIES_NOT_SET  = 0;
    
     
    private static Hashtable      m_project_definition = null;
    private static Hashtable      m_process_definition = null;
    private static Hashtable      m_species_definition = null;
    private static Hashtable      m_trace_file_formats = null;
  
    public synchronized static void            loadDefinitionsFromDatabase()
    {
        loadProjectDefinition();
        loadBiologicalSpeciesNames();
        loadProcessDefinition();
        loadTraceFileFormats();
    }
    
    /*
    public static void            loadNotDatabaseDefinitions() throws Exception
    {
        loadTraceFileFormatsFromXML();
    }
    
     */
    public static void            loadTraceFileFormats()
    {
      
        ResultSet rs = null; 
        TraceFileNameFormat format = null;
        m_trace_file_formats = new Hashtable();
        String sql = "select FORMATNAME,READING_DIRECTION,FORMATID ,PLATE_SEPARATOR,"
                 +" PLATE_LABEL_COLUMN ,PLATE_LABEL_START  ,PLATE_LABEL_LENGTH ,"
                 +" POSITION_COLUMN,POSITION_START  ,POSITION_LENGTH  ,DIRECTION_FORWARD "
                 +" ,DIRECTION_REVERSE  ,DIRECTION_COLUMN,DIRECTION_LENGTH  ," 
                 + "DIRECTION_START ,POSITION_SEPARATOR  ,DIRECTION_SEPARATOR, EXAMPLE_FILE_NAME  from TRACEFILEFORMAT ";
       
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while ( rs.next())
            {
                 format = new TraceFileNameFormat();
                 format.setId(rs.getInt("FORMATID"));
                 format.setFormatName (rs.getString("FORMATNAME"));  //FORMAT_NAME> 
                format.setPlateSeparator (rs.getString("PLATE_SEPARATOR"));//</SEPARATOR>
                format.setPositionSeparator (rs.getString("POSITION_SEPARATOR"));//</SEPARATOR>
                format.setDirectionSeparator (rs.getString("DIRECTION_SEPARATOR"));//</SEPARATOR>
                format.setPlateLabelColumn (rs.getInt("PLATE_LABEL_COLUMN")); //PLATE_LABEL_COLUMN>
                format.setPlateLabelStart (rs.getInt("PLATE_LABEL_START"));  //PLATE_LABEL_START>
                format.setPlateLabelLength (rs.getInt("PLATE_LABEL_LENGTH"));    //PLATE_LABEL_LENGTH>
                format.setPositionColumn (rs.getInt("POSITION_COLUMN"));    //POSITION_COLUMN>
                format.setPositionStart (rs.getInt("POSITION_START"));   //POSITION_START> 
                format.setPositionLength (rs.getInt("POSITION_LENGTH"));   //POSITION_LENGTH>
                format.setDirectionForward (rs.getString("DIRECTION_FORWARD"));   //DIRECTION_FORWARD>
                format.setDirectionReverse (rs.getString("DIRECTION_REVERSE"));   //DIRECTION_REVERSE>
                format.setDirectionColumn (rs.getInt("DIRECTION_COLUMN"));   //DIRECTION_COLUMN>
                format.setDirectionLength (rs.getInt("DIRECTION_LENGTH"));  //DIRECTION_START>
                format.setDirectionStart(rs.getInt("DIRECTION_START"));
                format.setFileNameReadingDirection(rs.getInt("READING_DIRECTION"));
                format.setExampleFileName(rs.getString("EXAMPLE_FILE_NAME"));
                m_trace_file_formats.put(format.getFormatName (),format);
                }
           
        }
        catch(Exception e)
        {
            System.out.println( "Cannot load trace file formats definition");
        }

    }
    /** Creates a new instance of DataLoad */
    private static void            loadProjectDefinition()
    {
      
        ResultSet rs = null; 
        m_project_definition = new Hashtable();
        
        String sql = "SELECT PROJECTNAME , PROJECTCODE from PROJECTDEFINITION ";
       
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while ( rs.next())
            {
                m_project_definition.put( rs.getString("PROJECTCODE"), rs.getString("PROJECTNAME"));
            }
        }
        catch(Exception e)
        {
            System.out.println( "Cannot load projects definition");
        }

    }
     
    private static void      loadProcessDefinition()
    {
        String sql = "select  PROCESSDEFINITIONID,PROCESSNAME from processdefinition ";
        ResultSet rs = null;
        m_process_definition = new Hashtable();
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next() )
            {
                m_process_definition.put ( rs.getString("PROCESSNAME"), new ProcessDefinition(rs.getInt("PROCESSDEFINITIONID"),  rs.getString("PROCESSNAME"), null));
            }
            
        } catch (Exception e)
        {
            System.out.println( "Cannot load  processdefinition " + e.getMessage());
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    
    private static void loadBiologicalSpeciesNames()
    {
        ResultSet rs = null; 
        m_species_definition = new Hashtable();
        String sql = "SELECT speciesId, speciesname,IDNAME  from speciesDEFINITION ";
       SpeciesDefinition sd = null;
        try
        {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while ( rs.next())
            {
                sd = new SpeciesDefinition(rs.getInt("speciesId"), rs.getString("speciesname"), rs.getString("IDNAME"));
                m_species_definition.put( String.valueOf( sd.getCode()), sd);
            }
        }
        catch(Exception e)
        {
            System.out.println( "Cannot load biological species definition");
        }
    }
    
    
     public static Hashtable getSpecies(){ return m_species_definition;}
     public static void      addSpecies(SpeciesDefinition sd){m_species_definition.put( String.valueOf( sd.getCode()), sd);}
    public static Hashtable getProcessDefinitions(){ return m_process_definition;}
    
    public static Hashtable        getProjectDefinitions(){ return m_project_definition;}
    public static void        addProject(String PROJECTNAME, String PROJECTCODE){ m_project_definition.put( PROJECTCODE,PROJECTNAME);}
          
    
    //----------------------------------------------------
    public static String getSpeciesName(int species_code)
    {
        SpeciesDefinition sd = (SpeciesDefinition) m_species_definition.get( String.valueOf(species_code ));
        if ( m_species_definition == null || m_species_definition.size() == 0 || sd == null)
            return "Not known species";
        else 
            return  sd.getName();
    }
    
    
    public static int getSpeciesId(String  species_name)
    {
        if ( m_species_definition == null || m_species_definition.size() == 0) return SPECIES_NOT_SET;
        SpeciesDefinition sd = null;
        for (Enumeration e = m_species_definition.keys() ; e.hasMoreElements() ;)
        {
            sd = (SpeciesDefinition)m_species_definition.get( e.nextElement() );
            if ( sd.getName().trim().equalsIgnoreCase(species_name))
                return sd.getCode();
        }
        return SPECIES_NOT_SET;
    }
    
     public static String getSpeciesUniqueId(int species_code)
    {
        SpeciesDefinition sd = (SpeciesDefinition) m_species_definition.get( String.valueOf(species_code ));
        if ( m_species_definition == null || m_species_definition.size() == 0 || sd == null)
            return "Not known species";
        else 
            return  sd.getIdName();
    }
     public static String getProjectName( String project_code) 
    {
        String project_name = (String) m_project_definition.get( project_code );
        if ( m_project_definition == null || m_project_definition.size() == 0 || project_name == null)
            return "Not known project";
        else 
            return  project_name;
    }
    
     
     public static String getProjectCodeForLabel(String label) 
    {
        String code = null;
        for ( Enumeration e = m_project_definition.keys(); e.hasMoreElements() ;)
        {
            code = (String)  e.nextElement();
            if ( label.startsWith(code)) return code;
        }
       return  "";
    }
     
     
     
     // deprecated
     /*
     public static void loadTraceFileFormatsFromXML()throws Exception
     {
            String xml_file =  edu.harvard.med.hip.bec.util.BecProperties.getInstance().getProperty("TRACE_FILES_FORMAT_FILE") ;
            if ( xml_file == null || !FileOperations.isFileExists(xml_file ) )
            {
                throw new BecDatabaseException("File with Trace File Formats not found");
            }
            TraceFileNameFormatParser SAXHandler = new TraceFileNameFormatParser();
            SAXParser parser = new SAXParser();
            parser.setContentHandler(SAXHandler);
            parser.setErrorHandler(SAXHandler);
            InputStream m_input_stream = new FileInputStream(xml_file);
            parser.parse(new org.xml.sax.InputSource(m_input_stream));
                       
            ArrayList m_trace_file_formats= SAXHandler.getFormats();

         
     }
      **/
   public static Hashtable          getTraceFileFormats(){ return m_trace_file_formats;}
   public static void               addTraceFileFormat(TraceFileNameFormat format){if ( m_trace_file_formats == null) m_trace_file_formats=new Hashtable(); if (format != null)m_trace_file_formats.put(format.getFormatName(),format);}
   public static TraceFileNameFormat          getTraceFileFormat(String format_name)
   { 
       if ( m_trace_file_formats == null || m_trace_file_formats.size()==0) return null;
       else return (TraceFileNameFormat)m_trace_file_formats.get(format_name);
   }
     
     
     
     //00000000000000000000000000
      public static void main(String args[])
    {
           BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
      
        TraceFileNameFormat format = null;   String    item_value =null;
    DatabaseToApplicationDataLoader.loadTraceFileFormats();
   for(Enumeration e=  DatabaseToApplicationDataLoader.getTraceFileFormats().elements(); e.hasMoreElements();)
   {
        format = (TraceFileNameFormat)e.nextElement();
         item_value = "<a href="+ BecProperties.getInstance().getProperty("JSP_REDIRECTION") +"Seq_GetItem.do?forwardName="
                    + Constants.PROCESS_DELETE_TRACE_FILE_FORMAT +"&ID="+ format.getId() +" onclick=\"return confirm('Are you sure you want to delete format "+ format.getFormatName() +"?');\">Delete </a>";
                  
   }
      }
}

    

