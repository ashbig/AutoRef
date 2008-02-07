/*
 * SubmissionProperties.java
 *
 * Created on January 31, 2008, 4:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package psi_data_converter.util;


import java.util.*;
import java.io.*;
import java.net.*;
/**
 *
 * @author htaycher
 */
public class SubmissionProperties
{
    // The name of the properties file holding the system config info.
   public final  String SUBMISSION_PROPERTIES="C:\\Projects\\PSI_data_converter\\PSI_DATA_CONVERTER\\src\\config\\SubmissionProperties.properties";
   
    // The properties
    private Properties properties;

    // The instance
    private static SubmissionProperties instance = null;

    /**
     * Protected constructor.
     *
     */
    protected SubmissionProperties() 
    {
        this.properties = new Properties();
        //try to get the properties file from the class path
        InputStream iStream = getInputStream();
        
        if(iStream !=null) {
            try {
            Properties prop = new Properties();
            prop.load(iStream);
            setProperties(prop);
            } catch (IOException ioE) {
                System.err.println("Unable to load properites file");
                try {
                    iStream.close();
                } catch(Throwable th){}
            }
        }
    
    }
    
    /**
     * Method to set the system properties.
     * This should only be called when the properties file is not accessable
     * through the class path such as a web application.
     */
    public void setProperties(Properties props) {        this.properties = props;    }
            
    /**
     * Retrieves the system property for the provided key.
     *
     * @param key THe key of the property in question.
     *
     * @return The property requested.
     */
    public String getProperty(String key) {                return properties.getProperty(key);    }
    
    /**
     * Gets the instance of FlexProperties.
     *
     * @return the single FlexProperties instance.
     */
    public static SubmissionProperties getInstance() 
    {
        if(instance == null) {
            instance = new SubmissionProperties();
        }
        return instance;
    } 
    
    protected InputStream getInputStream()
    {
        if ( SUBMISSION_PROPERTIES == null) return null;
        try{return new FileInputStream(SUBMISSION_PROPERTIES);}catch(Exception e){return null;}
       //return (Thread.currentThread().getContextClassLoader().getResourceAsStream(SUBMISSION_PROPERTIES));
    }  
    
    public static void main(String [] args) {}
     
}
