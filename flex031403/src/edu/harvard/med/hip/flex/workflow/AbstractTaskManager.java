/*
 * AbstractTaskManager.java
 *
 * This class serves as the base class for all specific task managers.
 *
 * Created on August 1, 2001, 11:56 AM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public abstract class AbstractTaskManager implements TaskManager {
    
    /** Creates new AbstractTaskManager */
    public AbstractTaskManager() {
    }
    
    /**
     * Process the queue items. Remove the old queue items and add the new queue items.
     *
     * @param removedItems The queue items to be removed.
     * @param addedItems The objects to be added.
     * @param protocol The current protocol.
     * @param conn The database connection.
     * @param workflow The current workflow.
     * @exception FlexDatabaseException.
     */
    public void processQueue(List removedItems, List objects, Protocol protocol, Connection conn, Workflow workflow) throws FlexDatabaseException {
        // Remove the container from the queue.
        ProcessQueue queue = makeRemovedItemQueue();
        queue.removeQueueItems(removedItems, conn);
        
        // Get the next protocols from the workflow.
        Vector nextProtocols = workflow.getNextProtocol(protocol.getProcessname());
        
        // Add the new items to the queue for each protocol.
        queue = makeAddedItemsQueue();
        for(int i=0; i<nextProtocols.size(); i++) {
            List newItems = new LinkedList();
            Iterator iter = objects.iterator();
            while(iter.hasNext()) {
                Object o = iter.next();
                newItems.add(new QueueItem(o, new Protocol((String)nextProtocols.elementAt(i))));
            }
            queue.addQueueItems(newItems, conn);
        }
    }
    
    public void createProcessRecord(String executionStatus, Protocol protocol,
                                    Researcher researcher, SubProtocol subprotocol,
                                    List iObjects, List oObjects, List ioObjects,
                                    Vector sampleLineageSet, Connection conn)
                                    throws FlexDatabaseException {
        // Create a process, process object and sample lineage record.
        Process process = new Process(protocol, executionStatus, researcher);
        
        if(subprotocol != null) {
            process.setSubprotocol(subprotocol.getName());
        }
        
        if(iObjects != null)
            addInputObjects(process, iObjects);
        
        if(oObjects != null)
            addOutputObjects(process, oObjects);
        
        if(ioObjects != null)
            addIOObjects(process, ioObjects);
        
        if(sampleLineageSet != null)
            process.setSampleLineageSet(sampleLineageSet);
        
        // Insert the process and process objects into database.
        process.insert(conn);
    }
    
    /**
     * Make the queue for removed items.
     *
     * @return The ProcessQueue object.
     */
    public abstract ProcessQueue makeRemovedItemQueue();
    
    /**
     * Make the queue for added items.
     *
     * @return The ProcessQueue object.
     */
    public abstract ProcessQueue makeAddedItemsQueue();
    
    public abstract void addInputObjects(Process process, List iObjects);
    
    public abstract void addOutputObjects(Process process, List oObjects);
    
    public abstract void addIOObjects(Process process, List ioObjects);       
}
