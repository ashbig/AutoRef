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

    private static FlexProperties containerInstance = null;
    
    protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(CONTAINER_TYPE_FILE));
    }
    
    /**
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static FlexProperties getInstance() {
        if(containerInstance == null) {
            containerInstance = new ContainerTypeProperties();
        }
        return containerInstance;
    } 
    
    public static void main(String [] args) {
        FlexProperties sysProps = StaticPropertyClassFactory.makePropertyClass("ContainerTypeProperties");
        FlexProperties flexProps = StaticPropertyClassFactory.makePropertyClass("FlexProperties");
        System.out.println("container: " + sysProps.getProperty("generate agar plates"));
        System.out.println("system: " + flexProps.getProperty("flex.repository.gel.relativedir"));
    }   
}
