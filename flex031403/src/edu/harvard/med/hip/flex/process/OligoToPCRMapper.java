/*
 * OligoToPCRMapper.java
 *
 * This class creates the PCR plates from the oligo plates.
 *
 * Created on June 27, 2001, 11:03 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.Vector;
import java.lang.String;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class OligoToPCRMapper extends OneToOneContainerMapper {
    public static final char CLOSED = 'C';
    public static final char FUSION = 'F';
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public OligoToPCRMapper() throws FlexProcessException {
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
        String newContainerType = getContainerType(protocol.getProcessname());
        Container c1 = (Container)containers.elementAt(0);
        Container c2 = (Container)containers.elementAt(1);
        
        // Get the new container barcode.
        String newBarcode = Container.getLabel(protocol.getProcesscode(), c1.getPlatesetid(), getSubThread(c1));        
        if(c1.getLabel().charAt(1) == FUSION || c2.getLabel().charAt(1) == FUSION) {
            newBarcode = newBarcode+"-"+FUSION;
        }
        if(c1.getLabel().charAt(1) == CLOSED || c2.getLabel().charAt(1) == CLOSED) {
            newBarcode = newBarcode+"-"+CLOSED;
        }
                  
        Container newContainer = new Container(newContainerType, null, newBarcode, c1.getPlatesetid());
        c1.restoreSample();
        c2.restoreSample();       
        mappingSamples(c1, c2, newContainer, protocol); 
        
        Vector newContainers = new Vector();
        newContainers.addElement(newContainer);
        return newContainers;
    }   
    
    // Creates the new samples from the samples of the previous plate.
    protected void mappingSamples(Container c1, Container c2, Container newContainer, Protocol protocol) throws FlexDatabaseException { 
        try {
            String type;
     
            for(int i=0; i<c1.getSamples().size(); i++) {
                Sample s1 = c1.getSample(i+1);
                Sample s2 = c2.getSample(i+1);
 
                if(Sample.CONTROL_POSITIVE.equals(s1.getType())) {
                    type = Sample.CONTROL_POSITIVE;
                } else if(Sample.CONTROL_NEGATIVE.equals(s1.getType())) {
                    type = Sample.CONTROL_NEGATIVE;
                } else if(Sample.EMPTY.equals(s1.getType())) {
                    type = Sample.EMPTY;
                } else {
                    type = Sample.getType(protocol.getProcessname());
                }
            
                Sample newSample = new Sample(type, i+1, newContainer.getId(), -1, Sample.GOOD);
                newContainer.addSample(newSample);
                sampleLineageSet.addElement(new SampleLineage(s1.getId(), newSample.getId()));
                sampleLineageSet.addElement(new SampleLineage(s2.getId(), newSample.getId()));
            }
        } catch (FlexCoreException e) {
            throw new FlexDatabaseException(e.getMessage());
        }
    }       
}
