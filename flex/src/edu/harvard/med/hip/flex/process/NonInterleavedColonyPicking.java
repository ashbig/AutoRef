/*
 * NonInterleavedColonyPicking.java
 *
 * Created on August 19, 2002, 5:05 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 *
 * @author  dzuo
 * @version 
 */
public class NonInterleavedColonyPicking extends GridPlateToCultureMapper {
    protected int platenumOnDest = 1;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public NonInterleavedColonyPicking() throws FlexProcessException {
        super();
    }

    protected int getPlatenumOnDest() {
        return platenumOnDest;
    }
}
