/*
 * ContainerTypeProperties.java
 *
 * Created on July 9, 2001, 6:24 PM
 */

package edu.harvard.med.hip.bec.util;

import java.io.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ContainerTypeProperties extends BecProperties {    
    // The name of the properties file holding the system config info.
    public final static String CONTAINER_TYPE_FILE ="config/ContainerType.properties";

    private static BecProperties containerInstance = null;
    
    protected InputStream getInputStream() {
        return (Thread.currentThread().getContextClassLoader().getResourceAsStream(CONTAINER_TYPE_FILE));
    }
    
    /**
     * Gets the instance of systemproperties.
     *
     * @return the single SystemProperites instance.
     */
    public static BecProperties getInstance() {
        if(containerInstance == null) {
            containerInstance = new ContainerTypeProperties();
        }
        return containerInstance;
    } 
    
    public static void main(String [] args) {
        BecProperties sysProps = StaticPropertyClassFactory.makePropertyClass("ContainerTypeProperties");
        BecProperties flexProps = StaticPropertyClassFactory.makePropertyClass("FlexProperties");
        System.out.println("container: " + sysProps.getProperty("generate agar plates"));
        System.out.println("system: " + flexProps.getProperty("flex.repository.gel.relativedir"));
    }   
}
