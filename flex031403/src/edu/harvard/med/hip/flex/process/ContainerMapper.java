/*
 * ContainerMapper.java
 *
 * Created on June 19, 2001, 12:41 PM
 *
 * This class handles the sample mapping when generating the new container
 * from the old container.
 */

package edu.harvard.med.hip.flex.process;

import java.util.Properties;
import java.io.*;
import java.util.Vector;
import java.util.Enumeration;

import edu.harvard.med.hip.flex.process.FlexProcessException;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.process.Result;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ContainerMapper {
    public final static String containerTypeFile = "E:\\flexDev/flex/src/edu/harvard/med/hip/flex/process/ContainerType.properties";
//    public final static String containerTypeFile = "edu/harvard/med/hip/flex/process/ContainerType.properties";
    
//    PropertyResourceBundle containerType = null;
    Properties containerType = null;

    
    
    
    
    /** Creates new ContainerMapper */
/*
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
*/

    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public ContainerMapper() throws FlexProcessException {      
        try {
            FileInputStream fis = new FileInputStream(containerTypeFile);
            containerType = new Properties();
            containerType.load(fis);
            fis.close ();
        } catch (IOException ex) {
            System.out.println(ex);
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
    public String getContainerType(String processname) {
        return (containerType.getProperty(processname));
    }

    /**
     * Mapping the samples from one container to a new container.
     *
     * @param sourceContainer The container gets to be mapped.
     * @param protocol The new protocol used for the new container.
     * @param descLocation The location for the new container.
     * @return The new Container object.
     * @exception FlexDatabaseException.
     */    
    public Container mapContainer(Container sourceContainer, Protocol protocol, Location destLocation) throws FlexDatabaseException {
        String newContainerType = getContainerType(protocol.getProcessname());
        String newBarcode = Container.getLabel(protocol.getProcesscode(), sourceContainer.getPlatesetid(), getSubThread(sourceContainer));        
        Container newContainer = new Container(newContainerType, destLocation, newBarcode);
        sourceContainer.restoreSample();
        mappingSamples(sourceContainer, newContainer, protocol); 
        return newContainer;
    }
 
    // Parse the barcode to get the sub thread.
    private String getSubThread(Container c) {
        String barcode = c.getLabel();
        int index = barcode.indexOf("-")+1;
        return (barcode.substring(index));
    }

    // Creates the new samples from the samples of the previous plate.
    private void mappingSamples(Container container, Container newContainer, Protocol protocol) throws FlexDatabaseException { 
        String type;
        Vector oldSamples = container.getSamples();
        Enumeration enum = oldSamples.elements();
        while(enum.hasMoreElements()) {
            Sample s = (Sample)enum.nextElement();
            
            if(edu.harvard.med.hip.flex.core.Sample.CONTROL_POSITIVE.equals(s.getType())) {
                type = edu.harvard.med.hip.flex.core.Sample.CONTROL_POSITIVE;
            } else if(edu.harvard.med.hip.flex.core.Sample.CONTROL_NEGATIVE.equals(s.getType())) {
                type = edu.harvard.med.hip.flex.core.Sample.CONTROL_NEGATIVE;
            } else if(edu.harvard.med.hip.flex.core.Sample.GEL.equals(s.getType())) {
                type = getSampleType(container, s, protocol);
            } else if(edu.harvard.med.hip.flex.core.Sample.TRANSFORMATION.equals(s.getType())) {
                type = getSampleType(container, s, protocol);
            } else {
                type = Sample.getType(protocol.getProcessname());
            }
            
            Sample newSample = new Sample(type, s.getPosition(), s.getContainerid(), s.getOligoid(), edu.harvard.med.hip.flex.core.Sample.GOOD);
            newContainer.addSample(newSample);
        }
    }   
    
    private String getSampleType(Container container, Sample s, Protocol protocol) throws FlexDatabaseException {
        String type = null;
        edu.harvard.med.hip.flex.process.Process p = 
        edu.harvard.med.hip.flex.process.Process.findProcess(container, protocol);
        Result result = Result.findResult(s, p);
        if(edu.harvard.med.hip.flex.process.Result.CORRECT.equals(result.getValue()) || edu.harvard.med.hip.flex.process.Result.MUL_W_CORRECT.equals(result.getValue())
            || edu.harvard.med.hip.flex.process.Result.MANY.equals(result.getValue()) || edu.harvard.med.hip.flex.process.Result.FEW.equals(result.getValue())) {
            type = Sample.getType(protocol.getProcessname());
        } else {
            type = edu.harvard.med.hip.flex.core.Sample.EMPTY;
        }
        
        return type;
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
        } 
    }
}
