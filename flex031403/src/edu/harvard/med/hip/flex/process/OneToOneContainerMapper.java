/*
 * OneToOneContainerMapper.java
 *
 * Created on June 19, 2001, 12:41 PM
 *
 * This class handles one to one container mapping.
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
import edu.harvard.med.hip.flex.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class OneToOneContainerMapper implements ContainerMapper {
    public static final String DAUGHTER_OLIGO_PLATE = "D";
    
    protected FlexProperties containerType = null;    
    protected Vector sampleLineageSet = new Vector();

    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     */
    public OneToOneContainerMapper() {             
        containerType = StaticPropertyClassFactory.makePropertyClass("ContainerTypeProperties");
    }

    /**
     * Return the sample lineage set.
     *
     * @return The sample lineage set.
     */
    public Vector getSampleLineageSet() {
        return sampleLineageSet;
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
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @return The new containers.
     * @exception FlexDatabaseException.
     */
    public Vector doMapping(Vector containers, Protocol protocol) 
                            throws FlexDatabaseException { 
        String newContainerType = getContainerType(protocol.getProcessname());
        Vector newContainers = new Vector();
        
        Enumeration enum = containers.elements();
        while (enum.hasMoreElements()) {
            Container container = (Container)enum.nextElement();    
            String newBarcode = null;
            
            //For diluting oligo plate, we need to get the new label in a different way.
            if(Protocol.DILUTE_OLIGO_PLATE.equals(protocol.getProcessname())) {
                newBarcode = DAUGHTER_OLIGO_PLATE+container.getLabel().substring(1);
            } else {
                newBarcode = Container.getLabel(protocol.getProcesscode(), container.getThreadid(), getSubThread(container));        
            }
            
            Container newContainer = new Container(newContainerType, null, newBarcode, container.getThreadid());
            container.restoreSample();
            mappingSamples(container, newContainer, protocol); 
            newContainers.addElement(newContainer);
        }
        
        return newContainers;
    }
 
    // Parse the barcode to get the sub thread.
    protected String getSubThread(Container c) {
        String barcode = c.getLabel();        
        int index = barcode.indexOf("-");
        
        if(index<0)
            return null;
        
        return (barcode.substring(index+1));
    }

    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol) throws FlexDatabaseException { 
        String type;
        Vector oldSamples = container.getSamples();
        Enumeration enum = oldSamples.elements();
        while(enum.hasMoreElements()) {
            Sample s = (Sample)enum.nextElement();
            if(Sample.CONTROL_POSITIVE.equals(s.getType())) {
                type = Sample.CONTROL_POSITIVE;
            } else if(Sample.CONTROL_NEGATIVE.equals(s.getType())) {
                type = Sample.CONTROL_NEGATIVE;
            } else if(Sample.EMPTY.equals(s.getType())) {
                type = Sample.EMPTY;
            } else if(Sample.GEL.equals(s.getType())) {
                type = getGelSampleType(container, s, protocol);
            } else if(Protocol.DILUTE_OLIGO_PLATE.equals(protocol.getProcessname())) {
                type = s.getType();
            } else {
                type = Sample.getType(protocol.getProcessname());
            }
            
            Sample newSample = new Sample(type, s.getPosition(), newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
            newContainer.addSample(newSample);
            sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
        }
    }   
    
    protected String getGelSampleType(Container container, Sample s, Protocol newProtocol) throws FlexDatabaseException {
        Protocol protocol = new Protocol(Protocol.ENTER_PCR_GEL_RESULTS);
        String type = null;
        edu.harvard.med.hip.flex.process.Process p = 
        edu.harvard.med.hip.flex.process.Process.findCompleteProcess(container, protocol);    
        Result result = Result.findResult(s, p);
        if(Result.CORRECT.equals(result.getValue()) || Result.MUL_W_CORRECT.equals(result.getValue())) {
            type = Sample.getType(newProtocol.getProcessname());
        } else {
            type = Sample.EMPTY;
        }
        
        return type;
    }
            
    public static void main(String [] args) {
        ContainerMapper mapper = new OneToOneContainerMapper();
        String containerType = mapper.getContainerType("generate DNA plates");

        if("96 WELL PLATE".equals(containerType))
            System.out.println("Testing static method getContainerType - OK");
        else
            System.out.println("Testing static method getContainerType - ERROR");
    }
}
