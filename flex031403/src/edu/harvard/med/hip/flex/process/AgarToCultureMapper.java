/*
 * AgarToCultureMapper.java
 *
 * Create a new Culture block from four agar plates.
 *
 * Created on July 3, 2001, 7:43 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AgarToCultureMapper extends OneToOneContainerMapper {
    public static final int COLONYNUM = 4;
    public static final int AGARWELLNUM = 6;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public AgarToCultureMapper() throws FlexProcessException {
        super();
    }

    /**
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @return The new containers.
     * @exception FlexDatabaseException, FlexCoreException
     */
    public Vector doMapping(Vector containers, Protocol protocol) 
                            throws FlexDatabaseException { 
        Container f1 = (Container)containers.elementAt(0);
        Vector newContainers = new Vector();
        String newContainerType = getContainerType(protocol.getProcessname());
        String subthread = getSubThread(f1);
        String newBarcode = Container.getLabel(protocol.getProcesscode(), f1.getPlatesetid(), subthread);     
        Container newContainer = new Container(newContainerType, null, newBarcode, f1.getPlatesetid());

        int index = 0;
        Enumeration enum = containers.elements();
        while(enum.hasMoreElements()) {
            Container c = (Container)enum.nextElement();
            c.restoreSample();
            mappingSamples(c, newContainer, protocol, index); 
            index = index+COLONYNUM*AGARWELLNUM;
        }
        newContainers.addElement(newContainer);
        
        return newContainers;
    }   

    // Return the subthreadid for culture plate from agar plate.
    protected String getSubThread(Container c) {        
        String processcode = c.getLabel().substring(0, 2);
        
        if(processcode.equals("AA")) {
            return new String("1");
        }
        if(processcode.equals("AC")) {
            return new String("2");
        }
        if(processcode.equals("AE")) {
            return new String("3");
        } 
        if(processcode.equals("AG")) {
            return new String("4");
        }
        if(processcode.equals("AI")) {
            return new String("5");
        }
        if(processcode.equals("AK")) {
            return new String("6");
        }
        if(processcode.equals("AM")) {
            return new String("7");
        }
        if(processcode.equals("AO")) {
            return new String("8");
        }
        return null;
    } 
    
    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol, int index) throws FlexDatabaseException { 
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
            } else {
                type = Sample.getType(protocol.getProcessname());
            }
            
            for(int i=1; i<=COLONYNUM; i++) {
                Sample newSample = new Sample(type, index+i, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
                newContainer.addSample(newSample);
                sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
            }            
            index = index+COLONYNUM;
        }
    }      
}