/* $Id: ContainerProcessQueue.java,v 1.1 2001-04-20 14:51:50 dongmei_zuo Exp $ 
 *
 * File     : ContainerProcessQueue.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo, Wendy Mar
 */

package flex.process;

import flex.core.*;
import flex.database.*;
import flex.util.*;

import java.util.*;

/**
 * The container queue that contains all the queued containers
 * belong to the same protocol.
 */
public class ContainerProcessQueue implements ProcessQueue {
	/**
	 * Constructor.
	 *
	 * @return The ContainerProcessQueue object.
	 */
	public ContainerProcessQueue () {}

	/**
 	 * Retrieve all of the queued items which are waiting for the
	 * next workflow process from the Queue table
	 *
	 * @param protocol The protocol object. 
	 * @return A LinkedList of QueueItem objects.
	 * @exception FlexProcessException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, DatabaseTransaction t) throws FlexProcessException {
		int protocolid = protocol.getId();
		String sql = new String("select c.containerid as id, "+
						"c.containertype as type, " +
						"c.locationid as locationid, " +
						"c.label as label, " +
						"to_char(q.dateadded, 'fmMM-DD-YYYY') as dateadded\n" +
						"from containerheader c, queue q\n" +
						"where c.containerid = q.containerid\n" +
						"and q.protocolid = "+protocolid);

		try {
			LinkedList items = restore(protocol, sql, t);
			return items;
		} catch (FlexProcessException e) {
			throw e;
		}
	}

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
	public LinkedList getQueueItems(Protocol protocol, String date, DatabaseTransaction t) throws FlexProcessException {
		int protocolid = protocol.getId();
		String sql = new String("select c.containerid as id, "+
						"c.containertype as type, " +
						"c.locationid as locationid, " +
						"c.label as label, " +
						"to_char(q.dateadded, 'fmMM-DD-YYYY') as dateadded\n" +
						"from containerheader c, queue q\n" +
						"where c.containerid = q.containerid\n" +
						"and q.protocolid = "+protocolid+
						"and dateadded = "+date);
		try {
			LinkedList items = restore(protocol, sql, t);
			return items;
		} catch (FlexProcessException e) {
			throw e;
		}
	}			

	/**
 	 * Insert all the selected process objects into the Queue   
	 * table of the database as QueueItems.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexProcessException.
 	 */
	public void addQueueItems(LinkedList items, DatabaseTransaction t) throws FlexProcessException {
		if (items == null)
			return;

		String sql = new String("insert into queue\n" +
						"(protocolid, dateadded, containerid\n" +
						"values(?, sysdate, ?");

		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			Container c = (Container)item.getItem();
			int containerid = c.getId();

			Vector params = new Vector();
			Hashtable param1 = ParamHashtable.getParam(1, "int", new Integer(protocolid));
			Hashtable param2 = ParamHashtable.getParam(2, "int", new Integer(containerid));
			params.addElement(param1);
			params.addElement(param2);

			v.addElement(params);
		}
	
		try {	
			t.executePreparedSql(sql, v);
		} catch (FlexDatabaseException exception) {
			throw new FlexProcessException(exception.getMessage());
		}		
	}		

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexProcessException.
 	 */
	public void removeQueueItems (LinkedList items, DatabaseTransaction t) throws FlexProcessException {
		if (items == null) 
			return;
		
		String sql = "delete from queue\n" +
				 "where protocolid = ?\n" +
				 "and dateadded = ?\n" +
				 "and containerid = ?";

		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			String date = item.getDate();
			Container c = (Container)item.getItem();
			int containerid = c.getId();

			Vector params = new Vector();
			Hashtable param1 = ParamHashtable.getParam(1, "int", new Integer(protocolid));
			Hashtable param2 = ParamHashtable.getParam(2, "string", date);
			Hashtable param3 = ParamHashtable.getParam(3, "int", new Integer(containerid));
			params.addElement(param1);
			params.addElement(param2);
			params.addElement(param3);

			v.addElement(params);
		}
	
		try {	
			t.executePreparedSql(sql, v);
		} catch (FlexDatabaseException exception) {
			throw new FlexProcessException(exception.getMessage());
		}		
	}

	/**
	 * Update the queued items.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexProcessException.
	 */
	public void updateQueueItems (LinkedList items, DatabaseTransaction t) throws FlexProcessException {
	}

	/**
	 * Get all the queued items that from the database.
	 */
	protected LinkedList restore(Protocol protocol, String sql, DatabaseTransaction t) throws FlexProcessException {
		try {
			Vector results = t.executeSql(sql);
			Enumeration enum = results.elements();
			StaticContainerFactory factory = new StaticContainerFactory();
			LinkedList items = new LinkedList();

			while(enum.hasMoreElements()) {
				Hashtable h = (Hashtable)enum.nextElement();
				int id = ((Integer)h.get(new Object("id"))).intValue();
System.out.println(id);
				String type = (String)h.get("type");
				int locationid = ((Integer)h.get("locationid")).intValue();
				String label = (String)h.get("label");
				String date = (String)h.get("dateadded");

				try {
					Container c = factory.getContainer(type);
					c.setId(id);
					c.setLocation(locationid);
					c.setLabel(label);
					QueueItem item = new QueueItem(c, protocol, date);
					items.addLast(item);
				} catch (FlexCoreException exception) {
					throw new FlexProcessException(exception.getMessage());
				}
			}
			return items;
		} catch (FlexDatabaseException exception) {
			throw new FlexProcessException(exception.getMessage());
		}
	}

	public static void main(String [] args) {
		ContainerProcessQueue queue = new ContainerProcessQueue();
		Protocol protocol = new Protocol(10, "test", "test");

		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			t.executeSql("insert into containerlocation values (1, 'test',null)");
			t.executeSql("insert into containertype values ('PCR')");
			for(int i=1; i<6; i++) 
				t.executeSql("insert into containerheader values ("+i+",'PCR', 1, 'PCR1')");
			t.executeSql("insert into processprotocol values (10, 'test', 'test')");
	
			System.out.println("Insert into queue:");
			for(int i=1; i<6; i++) {
				System.out.println("Container ID: "+i);
				t.executeSql("insert into queue values(10, sysdate, null, null, null,"+i+")");
			}

			LinkedList items = queue.getQueueItems(protocol, t);
			ListIterator iter = items.listIterator();

			System.out.println("Get items from queue:");
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				Container c = (Container)item.getItem();
				System.out.println("Container ID: "+c.getId());
			}

			System.out.println("Remove items from queue:");
			queue.removeQueueItems(items, t);
			
			System.out.println("Get items from queue:");
			LinkedList newitems = queue.getQueueItems(protocol, t);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				Container c = (Container)item.getItem();
				System.out.println("Container ID: "+c.getId());
			}

			System.out.println("Add items to queue:");
			queue.addQueueItems(items, t);
			System.out.println("Get items from queue:");
			newitems = queue.getQueueItems(protocol, t);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				Container c = (Container)item.getItem();
				System.out.println("Container ID: "+c.getId());
			}

			t.abort();
		} catch (FlexDatabaseException exception) {
			System.out.println(exception.getMessage());
		} catch (FlexProcessException exception) {
			System.out.println(exception.getMessage());
		}
	}
}
