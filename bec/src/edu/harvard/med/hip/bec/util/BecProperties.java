/*
 * File : BecProperties.java
 * Classes : BecProperties
 *
 * Description :
 *
 *      Abstract singleton class that hold flex properties.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.2 $
 * $Date: 2005-01-20 16:37:59 $
 * $Author: Elena $
 *
 ******************************************************************************
 *
 * Revision history (Started on July 6, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    Jul-07-2001 : JMM - Class created. 
 *
 */


package edu.harvard.med.hip.bec.util;

import java.io.*;
import java.util.*;

/**
 * Holds sytem level properties.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.2 $ $Date: 2005-01-20 16:37:59 $
 */

public class BecProperties
{
    // The name of the properties file holding the system config info.
    public final static String[]  APPLICATION_SETTINGS =
    {"SystemConfig.properties",
     "ContainerType.properties",
     "ApplicationHostSettings.properties"};
    public final static String PATH = "config/";
    // The properties
    private Properties m_properties = null;

    // The instance
    private static BecProperties m_instance = null;
    private  ArrayList       m_Error_messages = null;
    private  int      m_isSettingsVerified = 0;
    private Hashtable  m_blastable_db = null;
    private Hashtable  m_vector_libraries = null;
    private boolean     m_isDebuggingMode = false;
    private boolean     m_isWindowsOS = false;
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
            if ( value == null || !isFileExsist(value +File.separator +"blastall.exe") )m_Error_messages.add("Blast exe not found");
            
            value = m_properties.getProperty("TEMPORARY_FILES_FILE_PATH");// =/c/tmp/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for temporary files is not set properly");
            
            value =m_properties.getProperty("NEEDLE_OUTPUT_TMP_PATH"); //=/c/tmp_assembly/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for needele output temporary files is not set properly");
            value =m_properties.getProperty("MOVE_TRACE_FILES_BASE_DIR");// =/f/trace_files_root/trace_files_temporary_removed/
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for trace files moved from common tree is not set properly");
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
            
            value =m_properties.getProperty("TRACE_FILES_TRANCFER_INPUT_DIR");//=/F/Sequences for BEC/files_to_transfer
             if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for original trace files directory is not set properly");
           
            value =m_properties.getProperty("IS_WINDOWS");//=/F/Sequences for BEC/files_to_transfer
            if ( value == null  )   m_Error_messages.add("OS type not defined");
            else if ( Integer.parseInt(value) == 1 )   m_isWindowsOS = true;
            
            
            value =m_properties.getProperty("PERL_PATH");//=/F/Sequences for BEC/files_to_transfer
            if ( value == null || !isFileExsist(value) )m_Error_messages.add("Path for original trace files directory is not set properly");
            
            m_blastable_db = new Hashtable();
            m_vector_libraries = new Hashtable();
            String key = null;
           for (Enumeration e = m_properties.keys() ; e.hasMoreElements() ;)
           {
                key = (String) e.nextElement();
                if ( key.indexOf("VECTOR_FN_") != -1 && key.indexOf("_NAME") == -1 )
                {
                    m_vector_libraries.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
                }
                else if ( key.indexOf("BLASTABLE_DB_") != -1 && key.indexOf("_NAME") == -1)
                {
                    m_blastable_db.put(m_properties.getProperty(key+"_NAME"), m_properties.getProperty(key));
     
                }
           }
           if (! m_properties.getProperty("IS_DEBUGING").equalsIgnoreCase( "1" ))
                    m_isDebuggingMode = true;
     }

             
        
        if ( m_Error_messages.size() == 0 )m_isSettingsVerified = 1;
        else if (m_Error_messages.size() > 0 )m_isSettingsVerified = -1;
       
        for (int i = 0; i < m_Error_messages.size() ; i++){ System.out.println((String)m_Error_messages.get(i));}
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
    public String getProperty(String key) {
        
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
            for (int count = 0 ; count < APPLICATION_SETTINGS.length; count++)
            {
               iStream = m_instance.getInputStream(file_path+APPLICATION_SETTINGS[count] );
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
                    m_instance.addErrorMessage("Unable to load property file "+file_path+APPLICATION_SETTINGS[count]);
                }
              
            }
        }
        return m_instance;
    } 
  
    public static BecProperties getInstance() 
    {
        if(m_instance == null) { m_instance = new BecProperties(); }//should never be executed
        return m_instance;
    } 
    
     
    public          boolean         isSettingsVerified(){ return ( m_isSettingsVerified > 0 );}
    public           Hashtable  getBlastableDatabases(){ return m_blastable_db ;}
    public           Hashtable  getVectorLibraries(){ return m_vector_libraries ;}
    public           boolean    isInDebugMode(){ return  m_isDebuggingMode ;}
    public          boolean    isWindowsOS(){ return m_isWindowsOS;}
    
    private  InputStream getInputStream(String name) {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
    }  
    
    public static void main(String [] args) 
    {
        try
        {
        BecProperties sysProps =  BecProperties.getInstance( BecProperties.PATH);
        sysProps.verifyApplicationSettings();
          System.out.println("database " +  BecProperties.getInstance().getProperty("IS_EVALUATION_VERSION"));
          System.out.println("database " +  BecProperties.getInstance().isSettingsVerified());
      
         }catch(Exception e){}
       
    }         
} 