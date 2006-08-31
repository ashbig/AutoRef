/*
 * AbstractAgarContainerMapper.java
 *
 * This class implements the common methods for generating agar plates.
 *
 * Created on November 7, 2001, 5:52 PM
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
public abstract class AbstractAgarContainerMapper extends OneToOneContainerMapper {

    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public AbstractAgarContainerMapper() throws FlexProcessException {
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
        
        Enumeration enu = containers.elements();
        while (enu.hasMoreElements()) {
            Container container = (Container)enu.nextElement(); 
            container.restoreSample();
            String processCodes[] = getProcessCodes();
            int samplesLeft = container.getSamples().size();
            
            for(int i=0; i<processCodes.length; i++) {
                if(samplesLeft <= 0)
                    break;
                
                String processCode = processCodes[i];
                String newBarcode = Container.getLabel(projectCode, processCode, container.getThreadid(), getSubThread(container));        
                Container newContainer = new Container(newContainerType, null, newBarcode, container.getThreadid());
                mappingSamples(container, newContainer, protocol, i); 
                newContainers.addElement(newContainer);
                
                samplesLeft -= getWell();
            }
        }
        
        return newContainers;
    }

    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container container, Container newContainer, Protocol protocol, int i) throws FlexDatabaseException { 
        String type;
        Vector oldSamples = container.getSamples();
        
        int position;
        int expectedWellNum = getWell();
        int actualWellNum = oldSamples.size();
        
        for(int n=i*expectedWellNum; n<i*expectedWellNum+expectedWellNum; n++) {
            if(n >= actualWellNum)
                break;
            
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
            
            position = s.getPosition();
            if(position > expectedWellNum)
                position = position - expectedWellNum;
            
            Sample newSample = new Sample(type, position, newContainer.getId(), s.getConstructid(), s.getOligoid(), Sample.GOOD);
            newSample.setCloneid(s.getCloneid());
            newContainer.addSample(newSample);
            sampleLineageSet.addElement(new SampleLineage(s.getId(), newSample.getId()));
        }
    }
    
    /**
     * Return the all the process codes in an array.
     *
     * @return All the process codes in an array.
     */
    abstract public String[] getProcessCodes();
    
    /**
     * Return the well number.
     *
     * @return The well number.
     */
    abstract public int getWell();
}
