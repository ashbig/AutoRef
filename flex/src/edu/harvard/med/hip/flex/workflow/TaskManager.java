/*
 * TaskManager.java
 * 
 * This interface represents individual control object used to manage each
 * task in the current workflow.
 *
 * Created on July 31, 2001, 11:48 AM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.List;
import java.util.Vector;
import java.sql.Connection;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.process.Process;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public interface TaskManager {
    /**
     * Process the queue items. Remove the old queue items and add the new queue items.
     *
     * @param removedItems The queue items to be removed.
     * @param addedItems The queue items to be added.
     * @param protocol The current protocol.
     * @param conn The database connection.
     * @param project The project to work with.
     * @param workflow The current workflow to work with.
     *
     * @return Process for the parameters provided
     *
     * @exception FlexDatabaseException
     */
    public void processQueue(List removedItems, List addedItems, Protocol protocol, Connection conn, Project project, Workflow workflow) throws FlexDatabaseException;

    public Process createProcessRecord(String executionStatus, Protocol protocol,
                                    Researcher researcher, SubProtocol subprotocol,
                                    List iObjects, List oObjects, List ioObjects,
                                    Vector sampleLineageSet, Connection conn,
                                    Project project, Workflow workflow)
                                    throws FlexDatabaseException;    
}

