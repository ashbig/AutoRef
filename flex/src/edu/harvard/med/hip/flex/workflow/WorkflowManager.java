/*
 * WorkflowManager.java
 *
 * This class manages the whole workflow. The workflow consists of individual
 * TaskManager object. 
 *
 * Created on July 31, 2001, 11:44 AM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.List;
import java.sql.Connection;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class WorkflowManager {
    //The current workflow.
    private Workflow workflow = null;
    
    //The current TaskManager object. 
    private TaskManager manager = null;
    
    /** 
     * Creates new WorkflowManager using the default workflow.
     * 
     * @param taskName The name of the task in the workflow.
     * @return The WorkflowManager object. 
     * @exception FlexWorkflowException
     */
    public WorkflowManager(String taskName) throws FlexWorkflowException {
        workflow = new Workflow();
        manager = StaticTaskManagerFactory.createTaskManager(taskName);
    }

    /**
     * Create new WorkflowManager using the default workflow.
     *
     * @param manager The TaskManager object.
     * @return The WorkflowManager object.
     */
    public WorkflowManager(TaskManager manager) {
        workflow = new Workflow();
        this.manager = manager;
    }
    
    /**
     * Remove the old queue items from queue and added the new queue items on the queue.
     * 
     * @param removedItems The queue items to be removed.
     * @param addedItems The containers to be added.
     * @param protocol The current protocol.
     * @param conn The database connection.
     * @exception FlexDatabaseException.
     */
    public void processQueue(List removedItems, List addedItems, Protocol protocol, Connection conn) throws FlexDatabaseException {
        manager.processQueue(removedItems, addedItems, protocol, conn, workflow);
    }
}
