/* $Id: SequenceProcessQueue.java,v 1.2 2001-05-08 18:54:42 dongmei_zuo Exp $ 
 *
 * File     	: SequenceProcessQueue.java 
 * Date     	: 05072001
 * Author	: Dongmei Zuo
 */

package flex.ApplicationCode.Java.process;

import flex.ApplicationCode.Java.core.*;
import flex.ApplicationCode.Java.database.*;
import flex.ApplicationCode.Java.util.*;

import java.util.*;
import java.math.*;

/**
 * The sequence queue that contains all the queued sequences
 * belong to the same protocol.
 */
public class SequenceProcessQueue implements ProcessQueue {
	/**
	 * Constructor.
	 *
	 * @return The SequenceProcessQueue object.
	 */
	public SequenceProcessQueue () {}

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
		String sql = "select q.sequenceid as id, "+
						"to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
						"from queue q\n" +
						"where q.protocolid = "+protocolid;

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
		String sql = "select q.sequenceid as id, "+
						"to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
						"from queue q\n" +
						"where q.protocolid = "+protocolid+
						"and to_char(dateadded, 'fmYYYY-MM-DD') = '"+date+"'";

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
						"(protocolid, dateadded, sequenceid)\n" +
						"values(?, sysdate, ?)");

		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			FlexSequence s = (FlexSequence)item.getItem();
			int sequenceid = s.getId();

			Vector params = new Vector();
			Hashtable param1 = ParamHashtable.getParam(1, "int", new Integer(protocolid));
			Hashtable param2 = ParamHashtable.getParam(2, "int", new Integer(sequenceid));
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
				 "and sequenceid = ?";

		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			String date = item.getDate();
			FlexSequence s = (FlexSequence)item.getItem();
			int sequenceid = s.getId();

			Vector params = new Vector();
			Hashtable param1 = ParamHashtable.getParam(1, "int", new Integer(protocolid));
			Hashtable param2 = ParamHashtable.getParam(2, "string", date);
			Hashtable param3 = ParamHashtable.getParam(3, "int", new Integer(sequenceid));
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
		LinkedList items = new LinkedList();

		while(enum.hasMoreElements()) {
			Hashtable h = (Hashtable)enum.nextElement();
			int id = ((BigDecimal)h.get("ID")).intValue();
			String date = (String)h.get("DATEADDED");
			FlexSequence s = new FlexSequence(id);
			QueueItem item = new QueueItem(s, protocol, date);
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
			SequenceProcessQueue queue = (SequenceProcessQueue)factory.makeQueue("SequenceProcessQueue");
			Protocol protocol = new Protocol(10, "test", "test");
		
			DatabaseTransaction t = DatabaseTransaction.getInstance();
			t.executeSql("insert into species values('Test Species')");
			t.executeSql("insert into flexstatus values('NEW')");
			t.executeSql("insert into flexstatus values('REJECTED')");			
			t.executeSql("insert into flexstatus values('QUESTIONABLE')");			
			t.executeSql("insert into processprotocol values (10, 'test', 'test', null)");
			
			for (int i=1; i<5; i++) {
				t.executeSql("insert into flexsequence(sequenceid, flexstatus, genusspecies) values("+i+", 'REJECTED', 'Test Species')");
			}
			
			System.out.println("Insert into queue:");
			for(int i=1; i<5; i++) {
				System.out.println("Sequence ID: "+i);
				t.executeSql("insert into queue(protocolid, dateadded, sequenceid) values(10, sysdate,"+i+")");
			}
System.out.println("OK");
			LinkedList items = queue.getQueueItems(protocol, t);
			ListIterator iter = items.listIterator();

			System.out.println("Get items from queue:");
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				FlexSequence c = (FlexSequence)item.getItem();
				System.out.println("Sequence ID: "+c.getId());
			}

			System.out.println("Remove items from queue:");
			queue.removeQueueItems(items, t);
			
			System.out.println("Get items from queue:");
			LinkedList newitems = queue.getQueueItems(protocol, t);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				FlexSequence c = (FlexSequence)item.getItem();
				System.out.println("Sequence ID: "+c.getId());
			}

			System.out.println("Add items to queue:");
			queue.addQueueItems(items, t);
			System.out.println("Get items from queue:");
			newitems = queue.getQueueItems(protocol, t);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				FlexSequence c = (FlexSequence)item.getItem();
				System.out.println("Sequence ID: "+c.getId());
			}

			t.abort();
		} catch (FlexDatabaseException exception) {
			System.out.println(exception.getMessage());
		} catch (FlexProcessException exception) {
			System.out.println(exception.getMessage());
		}
	}
}
