
package edu.harvard.med.hip.bec.sampletracking;

import edu.harvard.med.hip.bec.util.*;

/**
 * ContainerFactory defines the interface for all container makers.
 */
public interface ContainerFactory
{
    /**
     * Constructs a Container object.
     *
     * @param type A string representation for container type.
     *
     * @return A Container object.
     * @exception FlexCoreException This exception is thrown when the
     * 		container type is not found.
     */
    public Container getContainer(String type) throws BecUtilException;
}