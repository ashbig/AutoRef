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
import java.util.Vector;
import java.sql.Connection;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class WorkflowManager {
    //The current workflow.
    private Workflow workflow = null;
    
    //The current project.
    private Project project = null;
    
    //The current TaskManager object.
    private TaskManager manager = null;
    
    /**
     * Creates new WorkflowManager using the default workflow.
     *
     * @param taskName The class name of the TaskManager class.
     * @return The WorkflowManager object.
     * @exception FlexWorkflowException
     */
    public WorkflowManager(String taskName) throws FlexWorkflowException {
        this.workflow = new Workflow();
        this.manager = StaticTaskManagerFactory.createTaskManager(taskName);
    }
    
    /**
     * Create new WorkflowManager using the default workflow.
     *
     * @param manager The TaskManager object.
     * @return The WorkflowManager object.
     */
    public WorkflowManager(TaskManager manager) {
        this.workflow = new Workflow();
        this.manager = manager;
    }

    /**
     * Constructor. 
     *
     * @param project The current project object.
     * @param workflow The current workflow.
     * @param taskName The class name of the TaskManager class.
     * @return The WorkflowManager object.
     */
    public WorkflowManager(Project project, Workflow workflow, String taskName) throws FlexWorkflowException {
        this.project = project;
        this.workflow = workflow;
        this.manager = StaticTaskManagerFactory.createTaskManager(taskName);
    }
    
    /**
     * Constructor. 
     *
     * @param project The current project object.
     * @param workflow The current workflow.
     * @param manager The TaskManager object.
     * @return The WorkflowManager object.
     */
    public WorkflowManager(Project project, Workflow workflow, TaskManager manager) {
        this.project = project;
        this.workflow = workflow;
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
    
    public Process createProcessRecord(String executionStatus, Protocol protocol,
    Researcher researcher, SubProtocol subprotocol,
    List iObjects, List oObjects, List ioObjects,
    Vector sampleLineageSet, Connection conn) throws FlexDatabaseException {
        return manager.createProcessRecord(executionStatus, protocol,
        researcher, subprotocol,
        iObjects, oObjects, ioObjects,
        sampleLineageSet, conn);
    }
}
