/*
 * $Id: ProcessQueue.java,v 1.3 2001-04-26 22:26:08 dongmei_zuo Exp $
 *
 * File     : ProcessQueue.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import java.util.*;
import flex.ApplicationCode.Java.database.*;

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
	public LinkedList getQueueItems(Protocol protocol, DatabaseTransaction t) throws FlexDatabaseException;

	/**
 	 * Retrieve the batch of queued items which are waiting for the
	 * next workflow process on a particular date from the Queue table
	 *
	 * @param protocol The protocol object.
	 * @param date The date added to the queue in yyyy-mm-dd format. 
	 * @param t The DatabaseTransaction object that talks to database.
	 * @return A LinkedList of QueueItem objects.
	 * @exception FlexDatabaseException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, String date, DatabaseTransaction t) throws FlexDatabaseException;

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexDatabaseException.
 	 */
	public void removeQueueItems (LinkedList items, DatabaseTransaction t) throws FlexDatabaseException;

	/**
 	 * Insert all the selected process objects into the Queue   
	 * table of the database as QueueItems.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexDatabaseException.
 	 */
	public void addQueueItems(LinkedList items, DatabaseTransaction t) throws FlexDatabaseException;

	/**
	 * Update the queued items.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexDatabaseException.
	 */
	public void updateQueueItems (LinkedList items, DatabaseTransaction t) throws FlexDatabaseException;
}
