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
 * $Revision: 1.1 $
 * $Date: 2003-03-14 21:13:30 $
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

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/


package edu.harvard.med.hip.bec.util;

import java.io.*;
import java.util.*;

/**
 * Holds sytem level properties.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.1 $ $Date: 2003-03-14 21:13:30 $
 */

public class BecProperties {
    // The name of the properties file holding the system config info.
    public final static String SYSTEM_FILE_LOC="config/SystemConfig.properties";
    
    // The properties
    private Properties properties;

    // The instance
    private static BecProperties instance = null;

    /**
     * Protected constructor.
     *
     */
    protected BecProperties() {
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
    public void setProperties(Properties props) {
        this.properties = props;
    }
            
    /**
     * Retrieves the system property for the provided key.
     *
     * @param key THe key of the property in question.
     *
     * @return The property requested.
     */
    public String getProperty(String key) {
        
        return properties.getProperty(key);
    }
    
    /**
     * Gets the instance of BecProperties.
     *
     * @return the single BecProperties instance.
     */
    public static BecProperties getInstance() {
        if(instance == null) {
            instance = new BecProperties();
        }
        return instance;
    } 
    
    protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(SYSTEM_FILE_LOC));
    }  
    
    public static void main(String [] args) {
        BecProperties sysProps = StaticPropertyClassFactory.makePropertyClass("BecProperties");
        System.out.println("Test: " + sysProps.getProperty("flex.repository.baseurl"));
    }         
} // End class BecProperties


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
