/*
 * File : SystemProperties.java
 * Classes : SystemProperties
 *
 * Description :
 *
 *      Singleton class that hold system level properties.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 * 
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.1 $
 * $Date: 2001-07-06 21:48:58 $
 * $Author: dongmei_zuo $
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


package edu.harvard.med.hip.flex.util;

import java.io.*;
import java.util.*;

/**
 * Holds sytem level properties.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.1 $ $Date: 2001-07-06 21:48:58 $
 */

public class SystemProperties {
    // The properties
    private Properties properties;

    // The instance
    private static SystemProperties instance;
    
    // The name of the properties file holding the system config info.
    public final static String SYSTEM_FILE_LOC="config/SystemConfig.properties";
    /**
     * Protected constructor.
     *
     */
    protected SystemProperties() {
        this.properties = new Properties();
        //try to get the properties file from the class path
        InputStream iStream = 
            Thread.currentThread().getContextClassLoader().getResourceAsStream(SYSTEM_FILE_LOC);
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
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static SystemProperties getInstance() {
        if(instance == null) {
            instance = new SystemProperties();
        }
        return SystemProperties.instance;
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
    
    public static void main(String [] args) {
        SystemProperties sysProps = SystemProperties.getInstance();
        System.out.println("Test: " + sysProps.getProperty("Test"));
    }
} // End class SystemProperties


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
*/
