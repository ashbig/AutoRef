/*
 * ContainerMapper.java
 *
 * Created on June 19, 2001, 12:41 PM
 *
 * This class handles the sample mapping when generating the new container
 * from the old container.
 */

package edu.harvard.med.hip.flex.process;

import java.util.PropertyResourceBundle;
import java.util.MissingResourceException;
import java.io.*;

import edu.harvard.med.hip.flex.process.FlexProcessException;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ContainerMapper {
//    public final static String containerTypeFile = "E:\\flexDev/flex/src/edu/harvard/med/hip/flex/process/ContainerType.properties";
    public final static String containerTypeFile = "edu/harvard/med/hip/flex/process/ContainerType.properties";
    
    PropertyResourceBundle containerType = null;
    
    /** Creates new ContainerMapper */
    public ContainerMapper() throws FlexProcessException {
        
        try {
            InputStream fis = ClassLoader.getSystemResourceAsStream(containerTypeFile);            
            containerType = new PropertyResourceBundle(fis);
            fis.close ();
        } catch (Exception ex) {
            throw new FlexProcessException(
            "Problem loading ContainerType Property File : "       
            + ex.getMessage());
        }   
    }
    
    /**
     * Returns the type based on the process name.
     *
     * @param processname The name of the processname.
     * @return The container type.
     */    
    public String getContainerType(String processname) throws MissingResourceException {
        return (containerType.getString(processname));
    }

    public static void main(String [] args) {
        try {
            ContainerMapper mapper = new ContainerMapper();
            String containerType = mapper.getContainerType("generate DNA plates");

            if("96 WELL PLATE".equals(containerType))
                System.out.println("Testing static method getContainerType - OK");
            else
                System.out.println("Testing static method getContainerType - ERROR");
        } catch (FlexProcessException e) {
            System.out.println(e);
        } catch (MissingResourceException e) {
            System.out.println(e);
        }
    }
}
