/*
 * ContainerTypeProperties.java
 *
 * Created on July 9, 2001, 6:24 PM
 */

package edu.harvard.med.hip.flex.util;

import java.io.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ContainerTypeProperties extends FlexProperties {    
    // The name of the properties file holding the system config info.
    public final static String CONTAINER_TYPE_FILE ="config/ContainerType.properties";

    protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(CONTAINER_TYPE_FILE));
    }
    
      /**
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static FlexProperties getInstance() {
        if(instance == null) {
            instance = new ContainerTypeProperties();
        }
        return instance;
    } 
    
    public static void main(String [] args) {
        FlexProperties sysProps = StaticPropertyClassFactory.makePropertyClass("ContainerTypeProperties");
        System.out.println("Test: " + sysProps.getProperty("generate agar plates"));
    }   
}
