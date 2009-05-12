//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->

/*
 * File : BecProperties.java
 * Classes : BecProperties
 *
 * 
 * The following information is used by CVS
 * $Revision: 1.15 $
 * $Date: 2009-05-12 18:53:29 $
 * $Author: et15 $
 *
 ******************************************************************************
 *
 *
 *
 */


package edu.harvard.med.hip.bec.util;

import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.sql.*;
import sun.jdbc.rowset.*;
/**
 * Holds sytem level properties.
 *
 * @author     $Author: et15 $
 * @version    $Revision: 1.15 $ $Date: 2009-05-12 18:53:29 $
 */

public class BecProperties
{
    // The name of the properties file holding the system config info.
    public final static String APPLICATION_PROPERTIES = "ApplicationHostSettings.properties";
    //for back compatability
    public final static String PATH = "config/";
   
    // The properties
    private Properties m_properties = null;

    // The instance
    private static BecProperties m_instance = null;
    private  ArrayList       m_Error_messages = null;
    private  int      m_isSettingsVerified = 0;
    private Hashtable  m_blastable_db = null;
    private Hashtable  m_polymorffinder_blastable_db = null;
    private Hashtable  m_vector_libraries = null;
    private boolean     m_isDebuggingMode = false;
    private int          m_isWindowsOS = 0;
    private boolean     m_isHipInternalVersion = false;
    private int         m_plate_naming_type = edu.harvard.med.hip.bec.sampletracking.objects.Container.PLATE_TYPE_96_A1_H12;
    private String      m_ace_email_address = null;
    private Hashtable   m_start_codons = null;
    private Hashtable   m_stop_codons_closed = null;
    private Hashtable   m_stop_codons_open = null;
    /**
     * Protected constructor.
     *
     */
    private BecProperties(){m_Error_messages = new ArrayList();}
    private BecProperties(String name) 
    {
        //try to get the properties file from the class path
        InputStream iStream = null;
        m_Error_messages = new ArrayList();
        Properties prop = null;
        try 
        {
            iStream = getInputStream(name);
            if (m_properties == null) m_properties = new Properties();
             m_properties.load(iStream);
        } 
        catch (Exception ioE) 
        {
         //   System.err.println("Unable to load properites file"+ ioE.getMessage());
            try {  iStream.close();   } catch(Throwable th){}
            m_Error_messages.add( "Unable to load property file "+name );
        }
    }
    
    public int    verifyApplicationSettings()
    {
       // System.out.println("start verifying" + m_properties == null);
        String value = null;
        if ( m_isSettingsVerified == 0 )//run verification
        {
            value = m_properties.getProperty("JSP_REDIRECTION");
            if ( value == null  )m_Error_messages.add("JSP_REDIRECTION not set");
            
            //check if directory exists
            value = m_properties.getProperty("BLAST_EXE_COMMON_PATH");//=/c/blastnew/
            if ( value == null || !isFileExsist(value ) )m_Error_messages.add("Blast (blastall) exe not found");
            
            value = m_properties.getProperty("TEMPORARY_FILES_FILE_PATH");// =/c/tmp/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for temporary files is not set properly");
            
            value =m_properties.getProperty("NEEDLE_OUTPUT_TMP_PATH"); //=/c/tmp_assembly/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for needele output temporary files is not set properly");
            value =m_properties.getProperty("MOVE_TRACE_FILES_BASE_DIR");// =/f/trace_files_root/trace_files_temporary_removed/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for trace files moved from common tree is not set properly");
            
            value= m_properties.getProperty("VECTOR_LIBRARIES_ROOT");
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for vector libraries location is not set properly");
           
            value =m_properties.getProperty("NEEDLE_OUTPUT_PATH");//=/c/needleoutput/     
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for needle output files is not set properly");
            value =m_properties.getProperty("TRACE_FILES_OUTPUT_PATH_ROOT");//=/f/trace_files_root/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for trace files root directory is not set properly");
            value =m_properties.getProperty("TRACE_FILES_INPUT_PATH_DIR");// d:\\trace_files_dump
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for trace files direcory dump is not set properly");
            value =m_properties.getProperty("NEEDLE_EXE_PATH");//=/c/EMBOSS-2.5.1/emboss/needle.exe
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for needle.exe is not set properly");
            value =m_properties.getProperty("PHREDPHAP_SCRIPT_PATH");// =/c/programs_bio/biolocal/phredPhrap
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for phredPhrap script is not set properly");
            value =m_properties.getProperty("PHRED_EXE_PATH");//=/c/bio/phred/Phred.exe
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for phred.exe is not set properly");
            value =m_properties.getProperty("BLAST_OUTPUT");//=/output/blastoutput/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for blast output director is not set properly");
            value =m_properties.getProperty("PRIMER3_EXE_PATH"); //=/c/blast/primer3.exe
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for primer3.exe is not set properly");
            
            value =m_properties.getProperty("TRACE_FILES_TRANSFER_INPUT_DIR");//=/F/Sequences for BEC/files_to_transfer
             if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for original trace files directory is not set properly");
            
            value =m_properties.getProperty("POLYMORPHISM_FINDER_DATA_DIRECTORY");//=/F/Sequences for BEC/files_to_transfer
             if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for polymorphism finder directory is not set properly");
           
            value =m_properties.getProperty("mail.smtp.host");//=/F/Sequences for BEC/files_to_transfer
            if ( value == null )m_Error_messages.add("SMTP host is not set. No e-mail can be send by ACE");
           
            value =m_properties.getProperty("IS_HIP_VERSION");//=/F/Sequences for BEC/files_to_transfer
            if ( value == null  )   m_Error_messages.add("HIP/External version not defined. Setting to work as HIP internal application.");
            else if ( Integer.parseInt(value) != 0 )  m_isHipInternalVersion  = true;
            
       //    value = m_properties.getProperty("TRACE_FILES_FORMAT_FILE");//=/c/blastnew/
      //      if ( value == null || !isFileExsist(value ) )m_Error_messages.add("File with Trace File Formats not found");
           
            
            value =m_properties.getProperty("PERL_PATH");//=/F/Sequences for BEC/files_to_transfer
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for perl exe not set properly");
            
            value =m_properties.getProperty("ACE_FROM_EMAIL_ADDRESS");//=/F/Sequences for BEC/files_to_transfer
            if ( value == null || value.indexOf("@") == -1 ) m_Error_messages.add("Problem with ACE e-mail address");
            else m_ace_email_address = (String) value;
            
            //verify settings for FLEX connection for HIP version
            if ( m_isHipInternalVersion )
            {
                 if ( m_properties.getProperty("FLEX_URL") == null || 
                        m_properties.getProperty("FLEX_USERNAME")== null || 
                        m_properties.getProperty("FLEX_PASSWORD") == null )
                        m_Error_messages.add("FLEX coonection properties are not set");
                try
                {
                    edu.harvard.med.hip.bec.database.DatabaseTransactionLocal db = 
                        edu.harvard.med.hip.bec.database.DatabaseTransactionLocal.getInstance
                            ( BecProperties.getInstance().getProperty("FLEX_URL") , 
                            BecProperties.getInstance().getProperty("FLEX_USERNAME"), 
                            BecProperties.getInstance().getProperty("FLEX_PASSWORD"));
                     java.sql.Connection conn =db.requestConnection();
                  
                    conn.close();
                } 
                catch(Exception fde)
                {
                    m_Error_messages.add("Cannot establish connection to FLEX. Check settings.");
                }
                
            }        
                    
                    
            m_blastable_db = new Hashtable();
            m_vector_libraries = new Hashtable();
            m_polymorffinder_blastable_db = new Hashtable();
            m_start_codons = new Hashtable();
            m_stop_codons_closed = new Hashtable();
            m_stop_codons_open = new Hashtable();
            String key = null;
           for (Enumeration e = m_properties.keys() ; e.hasMoreElements() ;)
           {
                key = (String) e.nextElement();
                  if ( key.indexOf("CODON_START_") != -1 && key.indexOf("_NAME") == -1 )
                {
                    m_start_codons.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
                }
                 if ( key.indexOf("CODON_STOP_CLOSED_") != -1 && key.indexOf("_NAME") == -1 )
                {
                    m_stop_codons_closed.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
                }
                 if ( key.indexOf("CODON_STOP_OPEN_") != -1 && key.indexOf("_NAME") == -1 )
                {
                    m_stop_codons_open.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
                }
                if ( key.indexOf("VECTOR_FN_") != -1 && key.indexOf("_NAME") == -1 )
                {
                    m_vector_libraries.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
                }
                else if ( key.indexOf("BLASTABLE_DB_") != -1 && key.indexOf("_NAME") == -1)
                {
                    m_blastable_db.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
     
                }
                else if ( key.indexOf("PF_DB_") != -1 && key.indexOf("_NAME") == -1)
                {
                    m_polymorffinder_blastable_db.put(m_properties.getProperty( key+"_NAME"), m_properties.getProperty(key));
     
                }
           }
           if ( !m_properties.getProperty("IS_DEBUGING").equalsIgnoreCase( "0" ))
                    m_isDebuggingMode = true;
            
            value =m_properties.getProperty("PLATE_TYPE_WELL_NAMING");//=/F/Sequences for BEC/files_to_transfer
            if ( value != null  ) 
            {
                int plate_naming = Integer.parseInt(value) ;
                if (edu.harvard.med.hip.bec.sampletracking.objects.Container.isValidNamingPlateType(plate_naming))
                    m_plate_naming_type = plate_naming;
            }
   
            
     }

             
        
        if ( m_Error_messages.size() == 0 )m_isSettingsVerified = 1;
        else if (m_Error_messages.size() > 0 )m_isSettingsVerified = -1;
       
        for (int i = 0; i < m_Error_messages.size() ; i++)
        { System.out.println((String)m_Error_messages.get(i));}
        return m_isSettingsVerified;
    }
    
    private boolean isFileExsist(String file_name)
    {
        File file= new File(file_name);
        return file.exists();
    }
    
    public String     getErrorMessage()
    {
        String errors = null;
        for (int count= 0; count < m_Error_messages.size() ; count++)
        {
            errors += "\n"+(String) m_Error_messages.get(count);
        }
        return errors;
    }
    public void     addErrorMessage(String err)
    {
        m_Error_messages.add(err);
    }
    /**
     * Method to set the system properties.
     * This should only be called when the properties file is not accessable
     * through the class path such as a web application.
     */
    public void setProperties(Properties props)
    {
        if (m_properties == null) m_properties = new Properties();
        
        m_properties = props;
    }
   public Properties getProperties()    {        return m_properties ;    }         
    /**
     * Retrieves the system property for the provided key.
     *
     * @param key THe key of the property in question.
     *
     * @return The property requested.
     */
    public String getProperty(String key)
    {
        if (m_properties == null) return null;
        String property = m_properties.getProperty(key);
        if ( property == null) return null;
        else         return property.trim();
    }
    
    /**
     * Gets the instance of BecProperties.
     *
     * @return the single BecProperties instance.
     */
    public static BecProperties getInstance(String file_path) 
    {
        Properties prop = null;
        InputStream iStream = null;
        if(m_instance == null) 
        {
            m_instance = new BecProperties();
            iStream = m_instance.getInputStream(PATH+APPLICATION_PROPERTIES );
               if ( iStream == null)System.err.println("Unable to load properites file");
                    
               try 
                {
                    prop= new Properties();
                    prop.load(iStream);
                    m_instance.setProperties(prop);
                } 
                catch (Exception ioE) 
                {
          //          System.err.println("Unable to load properites file"+ ioE.getMessage());
                    try {
                        iStream.close();
                    } catch(Throwable th){}
                    m_instance.addErrorMessage("Unable to load property file "+file_path+APPLICATION_PROPERTIES);
                }
              
            
        }
        return m_instance;
    } 
  
    public static BecProperties getInstance() 
    {
        if(m_instance == null) { m_instance = new BecProperties(); }//should never be executed
        return m_instance;
    } 
    
    public          int             getPlateTypePositionNaming(){ return m_plate_naming_type;} 
    public          boolean         isSettingsVerified(){ return ( m_isSettingsVerified > 0 );}
    public           Hashtable  getBlastableDatabases(){ return m_blastable_db ;}
    public           Hashtable  getPolymFinderBlastableDatabases(){ return m_polymorffinder_blastable_db;}
    public           Hashtable  getVectorLibraries(){ return m_vector_libraries ;}
    public           boolean    isInDebugMode(){ return  m_isDebuggingMode ;}
    public           boolean    isWindowsOS()
    {
        if (m_isWindowsOS == 0)
        {
           String os = System.getProperty("os.name").toLowerCase();
           m_isWindowsOS = ( os.indexOf("win") > -1) ? 1:-1;
        }
        return m_isWindowsOS == 1;
    }
    public          boolean    isInternalHipVersion()
    { 
       return  m_isHipInternalVersion ;
            
    }
    public          String getACEEmailAddress(){ return m_ace_email_address;}
    
     public Hashtable   getStartCodons(){ return m_start_codons ;}
    public Hashtable    getStopClosedCodons(){ return m_stop_codons_closed ;}
    public Hashtable    getStopFusionCodons(){ return m_stop_codons_open ;}
  
    
    private  InputStream getInputStream(String name) {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
    }  
    
    
    
    public static void main(String [] args) 
    {
        try
        {
            String dbname = null;String dbpath = null;
        BecProperties sysProps =  BecProperties.getInstance( BecProperties.APPLICATION_PROPERTIES);
        sysProps.verifyApplicationSettings();
        edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.loadTraceFileFormats();
     
      Hashtable ft =   edu.harvard.med.hip.bec.DatabaseToApplicationDataLoader.getTraceFileFormats();
     edu.harvard.med.hip.bec.sampletracking.mapping.TraceFileNameFormat frm = null;
     
     try
     {
      for (Enumeration e = ft.elements() ; e.hasMoreElements() ;)
{
    
      frm = (edu.harvard.med.hip.bec.sampletracking.mapping.TraceFileNameFormat)e.nextElement();
     
  	System.out.println(frm.getFormatName());
        }
     }
     catch(Exception e)
     {
         System.out.println(e.getMessage());
     }
      /* Hashtable ft =  BecProperties.getInstance().getPolymFinderBlastableDatabases();     
        for (Enumeration e = ft .keys() ; e.hasMoreElements() ;)
{
	 dbname = (String) e.nextElement();
   	 dbpath = (String)ft.get(dbname);
        }*/
         }catch(Exception e){}
       
    }         
} 