/*
 * $Id: ProcessQueue.java,v 1.6 2001-07-31 19:40:48 dzuo Exp $
 *
 * File     : ProcessQueue.java
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 * Represents the queue which contains all the item
 * that is waiting for the next lab process.
 */
public interface ProcessQueue {
    /**
     * Retrieve all of the queued items which are waiting for the
     * next workflow process from the Queue table
     *
     * @param protocol The protocol object.
     * @return A List of QueueItem objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItems(Protocol protocol) throws FlexDatabaseException;
    
    /**
     * Retrieve the batch of queued items which are waiting for the
     * next workflow process on a particular date from the Queue table
     *
     * @param protocol The protocol object.
     * @param date The date added to the queue in yyyy-mm-dd format.
     * @return A List of QueueItem objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItems(Protocol protocol, String Date) throws FlexDatabaseException;
    
    /**
     * Retrieve the batch of queued items which are waiting for the
     * next workflow process on a particular date from the Queue table,
     * and have a certain execution status.
     *
     * @param protocol The protocol object.
     * @param executionstatus The status of the queue items.
     * @return A List of QueueItem objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItemsWithStatus(Protocol protocol, String executionstatus)
    throws FlexDatabaseException;
    
    /**
     * Delete all the selected QueueItem objects from the Queue
     * table of the database.
     *
     * @param List The List of QueueItem objects.
     * @param c The Connection object that talks to database.
     * @exception FlexDatabaseException.
     */
    public void removeQueueItems(List items, Connection c) throws FlexDatabaseException;
    
    /**
     * Insert all the selected process objects into the Queue
     * table of the database as QueueItems.
     *
     * @param List The List of QueueItem objects.
     * @param c The Connection object that talks to database.
     * @exception FlexDatabaseException.
     */
    public void addQueueItems(List items, Connection c) throws FlexDatabaseException;
    
    /**
     * Update the queued items.
     *
     * @param List The List of QueueItem objects.
     * @param c The Connection object that talks to database.
     * @exception FlexDatabaseException.
     */
    public void updateQueueItems(List items, Connection c) throws FlexDatabaseException;
}
