/*
 * ProcessPlateManager.java
 *
 * Created on July 31, 2001, 12:46 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class ProcessPlateManager extends AbstractTaskManager {
    
    /** Creates new ProcessPlateManager */
    public ProcessPlateManager() {
        super();
    }
 
    /**
     * Make the queue for added items.
     *
     * @return The ProcessQueue object.
     */
    public ProcessQueue makeAddedItemsQueue() {
        return new ContainerProcessQueue();
    }
    
    /**
     * Make the queue for removed items.
     *
     * @return The ProcessQueue object.
     */
    public ProcessQueue makeRemovedItemQueue() {
        return new ContainerProcessQueue();
    }
    
}
