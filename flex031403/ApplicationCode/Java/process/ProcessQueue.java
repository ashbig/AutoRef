/*
 * $Id: ProcessQueue.java,v 1.1 2001-04-20 14:51:50 dongmei_zuo Exp $
 *
 * File     : ProcessQueue.java 
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package flex.process;

import java.util.*;
import flex.database.*;

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
	 * @exception FlexProcessException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, DatabaseTransaction t) throws FlexProcessException;

	/**
 	 * Retrieve the batch of queued items which are waiting for the
	 * next workflow process on a particular date from the Queue table
	 *
	 * @param protocol The protocol object.
	 * @param date The date added to the queue in yyyy-mm-dd format. 
	 * @param t The DatabaseTransaction object that talks to database.
	 * @return A LinkedList of QueueItem objects.
	 * @exception FlexProcessException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, String date, DatabaseTransaction t) throws FlexProcessException;

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexProcessException.
 	 */
	public void removeQueueItems (LinkedList items, DatabaseTransaction t) throws FlexProcessException;

	/**
 	 * Insert all the selected process objects into the Queue   
	 * table of the database as QueueItems.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexProcessException.
 	 */
	public void addQueueItems(LinkedList items, DatabaseTransaction t) throws FlexProcessException;

	/**
	 * Update the queued items.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexProcessException.
	 */
	public void updateQueueItems (LinkedList items, DatabaseTransaction t) throws FlexProcessException;
}
