/*
 * $Id: ProcessQueue.java,v 1.3 2001-05-24 10:55:37 dongmei_zuo Exp $
 *
 * File     : ProcessQueue.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
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
	 * @return A LinkedList of QueueItem objects.
	 * @exception FlexDatabaseException.
 	 */
	public LinkedList getQueueItems(Protocol protocol) throws FlexDatabaseException;

	/**
 	 * Retrieve the batch of queued items which are waiting for the
	 * next workflow process on a particular date from the Queue table
	 *
	 * @param protocol The protocol object.
	 * @param date The date added to the queue in yyyy-mm-dd format. 
	 * @return A LinkedList of QueueItem objects.
	 * @exception FlexDatabaseException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, String Date) throws FlexDatabaseException;

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The Connection object that talks to database.
	 * @exception FlexDatabaseException.
 	 */
	public void removeQueueItems (LinkedList items, Connection c) throws FlexDatabaseException;

	/**
 	 * Insert all the selected process objects into the Queue   
	 * table of the database as QueueItems.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The Connection object that talks to database.
	 * @exception FlexDatabaseException.
 	 */
	public void addQueueItems(LinkedList items, Connection c) throws FlexDatabaseException;

	/**
	 * Update the queued items.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The Connection object that talks to database.
	 * @exception FlexDatabaseException.
	 */
	public void updateQueueItems (LinkedList items, Connection c) throws FlexDatabaseException;
}
