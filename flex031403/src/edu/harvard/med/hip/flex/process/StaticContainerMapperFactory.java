/*
 * StaticContainerMapperFactory.java
 *
 * Create different ContainerMapper objects based on the process name.
 *
 * Created on July 3, 2001, 2:14 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 *
 * @author  dzuo
 * @version 
 */
public class StaticContainerMapperFactory {

    /** Creates new StaticContainerMapperFactory */
    public StaticContainerMapperFactory() {
    }

    /**
     * Static method to create the new ConainerMapper object.
     *
     * @param processname The name of the process.
     * @return The ContainerMapper object.
     * @exception FlexProcessException.
     */
    public static ContainerMapper makeContainerMapper(String processname) throws FlexProcessException {
        if(Protocol.GENERATE_AGAR_PLATES.equals(processname)) {
            return new AgarContainerMapper();
        } else if(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES.equals(processname)) {
            return new AgarToCultureMapper();
        } else {
            return new OneToOneContainerMapper();
        }
    }
}
