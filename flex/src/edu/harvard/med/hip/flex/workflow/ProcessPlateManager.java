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
public class ProcessPlateManager implements TaskManager {
    
    /** Creates new ProcessPlateManager */
    public ProcessPlateManager() {
    }
    
    /**
     * Process the queue items. Remove the old queue items and add the new queue items.
     *
     * @param removedItems The queue items to be removed.
     * @param addedItems The containers to be added.
     * @param protocol The current protocol.
     * @param conn The database connection.
     * @param workflow The current workflow.
     * @exception FlexDatabaseException.
     */
    public void processQueue(List removedItems, List containers, Protocol protocol, Connection conn, Workflow workflow) throws FlexDatabaseException {
        // Remove the container from the queue.
        ContainerProcessQueue queue = new ContainerProcessQueue();
        queue.removeQueueItems(removedItems, conn);
        
        // Get the next protocols from the workflow.
        Vector nextProtocols = workflow.getNextProtocol(protocol.getProcessname());
        
        // Add the new containers to the queue for each protocol.
        for(int i=0; i<nextProtocols.size(); i++) {
            List newItems = new LinkedList();
            Iterator iter = containers.iterator();
            while(iter.hasNext()) {
                Container newContainer = (Container)iter.next();
                newItems.add(new QueueItem(newContainer, new Protocol((String)nextProtocols.elementAt(i))));
            }
            queue.addQueueItems(newItems, conn);
        }        
    }    
}
