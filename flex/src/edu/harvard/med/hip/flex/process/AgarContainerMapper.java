/*
 * AgarContainerMapper.java
 *
 * This class creates 16 agar plates from one transformation plate
 * and mapping the samples from transformation plate to agar plate.
 *
 * Created on July 3, 2001, 12:00 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class AgarContainerMapper extends OneToOneContainerMapper {
    private String processCodes[] = {"AA", "AB", "AC", "AD", "AE", "AF", "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP"};

    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public AgarContainerMapper() throws FlexProcessException {
        super();       
    }
    
    /**
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @return The new containers.
     * @exception FlexDatabaseException.
     */
    public Vector doMapping(Vector containers, Protocol protocol, Project project,
    Workflow workflow) throws FlexDatabaseException {
        String newContainerType = getContainerType(protocol.getProcessname());
        Vector newContainers = new Vector();
        String projectCode = getProjectCode(project, workflow);
        
        Enumeration enum = containers.elements();
        while (enum.hasMoreElements()) {
            Container container = (Container)enum.nextElement(); 
            container.restoreSample();
            
            for(int i=0; i<processCodes.length; i++) {
                String processCode = processCodes[i];
                String newBarcode = Container.getLabel(projectCode, processCode, container.getThreadid(), getSubThread(container));        
                Container newContainer = new Container(newContainerType, null, newBarcode, container.getThreadid());
                mappingSamples(container, newContainer, protocol, i); 
                newContainers.addElement(newContainer);
            }
        }
        
        return newContainers;
    }

    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol, int i) throws FlexDatabaseException { 
        String type;
        Vector oldSamples = container.getSamples();
        
        int position = 1;
        for(int n=i*6; n<i*6+6; n++) {
            Sample s = (Sample)oldSamples.elementAt(n);
            if(Sample.CONTROL_POSITIVE.equals(s.getType())) {
                type = Sample.CONTROL_POSITIVE;
            } else if(Sample.CONTROL_NEGATIVE.equals(s.getType())) {
                type = Sample.CONTROL_NEGATIVE;
            } else if(Sample.EMPTY.equals(s.getType())) {
                type = Sample.EMPTY;
            } else {
                type = Sample.getType(protocol.getProcessname());   
            }
            
            Sample newSample = new Sample(type, position, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
            newContainer.addSample(newSample);
            sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
            position++;
        }
    }
}
