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
import edu.harvard.med.hip.flex.core.Container;

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
     * @exception FlexDatabaseException.
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
                  
        Container newContainer = new Container(newContainerType, null, newBarcode);
        c1.restoreSample();
        c2.restoreSample();
        mappingSamples(c1, newContainer, protocol); 
        mappingSamples(c2, newContainer, protocol); 
        
        Vector newContainers = new Vector();
        newContainers.addElement(newContainer);
        return newContainers;
    }   
}
