/*
 * ContainerMapper.java
 *
 * Defines the interface for all the container mappings.
 *
 * Created on June 27, 2001, 11:08 AM
 */

package edu.harvard.med.hip.flex.process;

import java.util.Vector;
import edu.harvard.med.hip.flex.database.FlexDatabaseException;
import edu.harvard.med.hip.flex.workflow.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public interface ContainerMapper {
    /**
     * Creates new containers based on given containers and protocol.
     *
     * @param containers Source containers for mapping.
     * @param protocol The protocol for destination containers.
     * @param project The project used for mapping.
     * @param workflow The workflow related to the mapping container.
     * @return The new containers.
     * @exception FlexDatabaseException.
     */
    public Vector doMapping(Vector containers, Protocol protocol, Project project,
    Workflow workflow) throws FlexDatabaseException; 

    /**
     * Return the sample lineage.
     *
     * @return The sample lineage as a Vector.
     */
    public Vector getSampleLineageSet();
    
    /**
     * Return the container type based on process name.
     *
     * @param processname The name of the process protocol.
     * @return The container type for given process name.
     */
    public String getContainerType(String processname);
}

