/*

 * $Id: Queue.java,v 1.1 2001-05-23 15:40:06 dongmei_zuo Exp $

 *

 * File     : Queue.java 

 * Date     : 04162001

 * Author	: Dongmei Zuo

 */



package edu.harvard.med.hip.flex.process;



import java.util.*;



/**

 * Represents the queue which contains all the item 

 * that is waiting for the next lab process.

 */

public interface Queue {

	/**

 	 * Get all the queued items that is waiting for the 

	 * next given step. Needs interaction with the database.

	 *

	 * @return A list of objects.

 	 */

	public Vector getQueueItems ();



	/**

	 * Delete all the queued items from the database.

	 */

	public void removeQueueItems ();



	/**

	 * Update the queued items.

	 */

	public void updateQueueItems ();



	/**

	 * Insert the queue record into database.

	 */

	public void insertQueueItems();



	/**

	 * Add an object to this queue.

	 *

	 * @param item The object that will be added to the queue.

	 */

	public void addItem (QueueItem item);



	/**

	 * Delete the queued items.

	 *

	 * @param item The object that will be deleted from the queue.

	 */

	public boolean removeItem (QueueItem item);

}

