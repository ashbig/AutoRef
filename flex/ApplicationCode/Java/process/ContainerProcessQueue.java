/* $Id: ContainerProcessQueue.java,v 1.4 2001-04-26 22:26:08 dongmei_zuo Exp $ 
 *
 * File     	: ContainerProcessQueue.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo, Wendy Mar
 */

package flex.ApplicationCode.Java.process;

import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.util.*;

import java.util.*;
import java.math.*;

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
	 * @exception FlexDatabaseException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, DatabaseTransaction t) throws FlexDatabaseException {
		int protocolid = protocol.getId();
		String sql = new String("select c.containerid as id, "+
						"c.containertype as type, " +
						"c.locationid as locationid, " +
						"c.label as label, " +
						"l.locationtype as locationtype, "+
						"l.locationdescription as description, "+
						"to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
						"from containerheader c, containerlocation l, queue q\n" +
						"where c.containerid = q.containerid\n" +
						"and c.locationid = l.locationid\n"+
						"and q.protocolid = "+protocolid);

		LinkedList items = restore(protocol, sql, t);
		return items;

	}

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
	public LinkedList getQueueItems(Protocol protocol, String date, DatabaseTransaction t) throws FlexDatabaseException {
		int protocolid = protocol.getId();
		String sql = new String("select c.containerid as id, "+
						"c.containertype as type, " +
						"c.locationid as locationid, " +
						"l.locationtype as locationtype," +
						"l.locationdescription as description,"+
						"c.label as label, " +
						"to_char(q.dateadded, 'fmMM-DD-YYYY') as dateadded\n" +
						"from containerheader c, containerlocation l, queue q\n" +
						"where c.containerid = q.containerid\n" +
						"and c.locationid = l.locationid\n"+
						"and q.protocolid = "+protocolid+
						"and to_char(dateadded, 'fmYYYY-MM-DD') = "+date);

		LinkedList items = restore(protocol, sql, t);
		return items;
	}			

	/**
 	 * Insert all the selected process objects into the Queue   
	 * table of the database as QueueItems.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexDatabaseException.
 	 */
	public void addQueueItems(LinkedList items, DatabaseTransaction t) throws FlexDatabaseException {
		if (items == null)
			return;

		String sql = new String("insert into queue\n" +
						"(protocolid, dateadded, containerid)\n" +
						"values(?, sysdate, ?)");

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
	
		t.executePreparedSql(sql, v);		
	}		

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexDatabaseException.
 	 */
	public void removeQueueItems (LinkedList items, DatabaseTransaction t) throws FlexDatabaseException {
		if (items == null) 
			return;
		
		String sql = "delete from queue\n" +
				 "where protocolid = ?\n" +
				 "and to_char(dateadded, 'fmYYYY-MM-DD') = ?\n" +
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
	
		t.executePreparedSql(sql, v);		
	}

	/**
	 * Update the queued items.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param t The DatabaseTransaction object that talks to database.
	 * @exception FlexDatabaseException.
	 */
	public void updateQueueItems (LinkedList items, DatabaseTransaction t) throws FlexDatabaseException {
	}

	/**
	 * Get all the queued items that from the database.
	 */
	protected LinkedList restore(Protocol protocol, String sql, DatabaseTransaction t) throws FlexDatabaseException {
		Vector results = t.executeSql(sql);
		Enumeration enum = results.elements();
		StaticContainerFactory factory = new StaticContainerFactory();
		LinkedList items = new LinkedList();

		while(enum.hasMoreElements()) {
			Hashtable h = (Hashtable)enum.nextElement();
			int id = ((BigDecimal)h.get("ID")).intValue();
			String type = (String)h.get("TYPE");
			int locationid = ((BigDecimal)h.get("LOCATIONID")).intValue();
			String locationtype = (String)h.get("LOCATIONTYPE");
			String description = (String)h.get("DESCRIPTION");
			String label = (String)h.get("LABEL");
			String date = (String)h.get("DATEADDED");
			Location location = new Location(locationid, locationtype,description);

			Container c = new Container(id, type, location, label);
			QueueItem item = new QueueItem(c, protocol, date);
			items.addLast(item);
		}
		return items;
	}

	//******************************************************//
	//			Test				//
	//******************************************************//
	
	public static void main(String [] args) {
		try {
			QueueFactory factory = new StaticQueueFactory();
			ContainerProcessQueue queue = (ContainerProcessQueue)factory.makeQueue("ContainerProcessQueue");
			Protocol protocol = new Protocol(10, "test", "test");
		
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			t.executeSql("insert into containerlocation values (1, 'test',null)");
			t.executeSql("insert into containertype values ('PCR')");
			for(int i=1; i<6; i++) 
				t.executeSql("insert into containerheader values ("+i+",'PCR', 1, 'PCR1')");
			t.executeSql("insert into processprotocol values (10, 'test', 'test', null)");
	
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
