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
        String newContainerType = getContainerType(protocol.getProcessname());
        Container f1 = (Container)containers.elementAt(0);
        Container c1 = (Container)containers.elementAt(1);
        Container f2 = (Container)containers.elementAt(2);
        Container c2 = (Container)containers.elementAt(3);

        Vector newContainers = new Vector();
        return newContainers;
    }   

}