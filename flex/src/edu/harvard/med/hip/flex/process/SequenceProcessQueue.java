/* $Id: SequenceProcessQueue.java,v 1.3 2001-05-24 10:56:44 dongmei_zuo Exp $ 
 *
 * File     	: SequenceProcessQueue.java 
 * Date     	: 05072001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

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
	public LinkedList getQueueItems(Protocol protocol) throws FlexDatabaseException {
		int protocolid = protocol.getId();
		String sql = "select q.sequenceid as id, "+
						"to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
						"from queue q\n" +
						"where q.protocolid = "+protocolid;

		LinkedList items = restore(protocol, sql);
		return items;

	}

	/**
 	 * Retrieve the batch of queued items which are waiting for the
	 * next workflow process on a particular date from the Queue table
	 *
	 * @param protocol The protocol object.
	 * @param date The date added to the queue in yyyy-mm-dd format. 
	 * @return A LinkedList of QueueItem objects.
	 * @exception FlexDatabaseException.
 	 */
	public LinkedList getQueueItems(Protocol protocol, String Date) throws FlexDatabaseException {
		int protocolid = protocol.getId();
		String sql = "select q.sequenceid as id, "+
						"to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
						"from queue q\n" +
						"where q.protocolid = "+protocolid+
						"and to_char(dateadded, 'fmYYYY-MM-DD') = '"+date+"'";

		LinkedList items = restore(protocol, sql);
		return items;
	}			

	/**
 	 * Insert all the selected process objects into the Queue   
	 * table of the database as QueueItems.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The Connection object.
	 * @exception FlexDatabaseException.
 	 */
	public void addQueueItems(LinkedList items, Connection c) throws FlexDatabaseException {
		if (items == null)
			return;

		String sql = new String("insert into queue\n" +
						"(protocolid, dateadded, sequenceid)\n" +
						"values(?, sysdate, ?)");
		PreparedStatement stmt = c.prepareStatement(sql);
		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			FlexSequence s = (FlexSequence)item.getItem();
			int sequenceid = s.getId();
			
			stmt.setInt(1, protocolid);
			stmt.setInt(1, sequenceid);
			DatabaseTransaction.executeUpdate(stmt);
		}
		stmt.close();	
	}		

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The database Connectin object.
	 * @exception FlexDatabaseException.
 	 */
	public void removeQueueItems (LinkedList items, Connection c) throws FlexDatabaseException {
		if (items == null) 
			return;
		
		String sql = "delete from queue\n" +
				 "where protocolid = ?\n" +
				 "and to_char(dateadded, 'fmYYYY-MM-DD') = ?\n" +
				 "and sequenceid = ?";
		PreparedStatement stmt = c.prepareStatement(sql);
		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			String date = item.getDate();
			FlexSequence s = (FlexSequence)item.getItem();
			int sequenceid = s.getId();

			stmt.setInt(1, protocolid);
			stmt.setString(2, date);
			stmt.setInt(3, sequenceid);
			DatabaseTransaction.executeUpdate(stmt);
		}
	
		stmt.close();	
	}

	/**
	 * Update the queued items.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The Connection object.
	 * @exception FlexDatabaseException.
	 */
	public void updateQueueItems (LinkedList items, Connection c) throws FlexDatabaseException {
	}

	/**
	 * Get all the queued items that from the database.
	 */
	protected LinkedList restore(Protocol protocol, String sql) throws FlexDatabaseException {
		CachedRowSet rs = DatabaseTransaction.executeQuery(sql);
		LinkedList items = new LinkedList();

		while(rs.next()) {
			int id = rs.getInt("ID");
			String date = rs.getString("DATEADDED");
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
			Connection c = t.requestConnection();
			DatabaseTransaction.executeUpdate("insert into species values('Test Species')", c);
			DatabaseTransaction.executeUpdate("insert into flexstatus values('NEW')", c);
			DatabaseTransaction.executeUpdate("insert into flexstatus values('REJECTED')", c);			
			DatabaseTransaction.executeUpdate("insert into flexstatus values('QUESTIONABLE')", c);			
			DatabaseTransaction.executeUpdate("insert into processprotocol values (10, 'test', 'test', null)", c);
			
			for (int i=1; i<5; i++) {
				DatabaseTransaction.executeUpdate("insert into flexsequence(sequenceid, flexstatus, genusspecies) values("+i+", 'REJECTED', 'Test Species')", c);
			}
			
			System.out.println("Insert into queue:");
			for(int i=1; i<5; i++) {
				System.out.println("Sequence ID: "+i);
				DatabaseTransaction.executeUpdate("insert into queue(protocolid, dateadded, sequenceid) values(10, sysdate,"+i+")", c);
			}
System.out.println("OK");
			LinkedList items = queue.getQueueItems(protocol);
			ListIterator iter = items.listIterator();

			System.out.println("Get items from queue:");
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				FlexSequence c = (FlexSequence)item.getItem();
				System.out.println("Sequence ID: "+c.getId());
			}

			System.out.println("Remove items from queue:");
			queue.removeQueueItems(items, c);
			
			System.out.println("Get items from queue:");
			LinkedList newitems = queue.getQueueItems(protocol);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				FlexSequence c = (FlexSequence)item.getItem();
				System.out.println("Sequence ID: "+c.getId());
			}

			System.out.println("Add items to queue:");
			queue.addQueueItems(items, c);
			System.out.println("Get items from queue:");
			newitems = queue.getQueueItems(protocol);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				FlexSequence c = (FlexSequence)item.getItem();
				System.out.println("Sequence ID: "+c.getId());
			}

			c.rollback();
			c.close();
		} catch (FlexDatabaseException exception) {
			System.out.println(exception.getMessage());
		} catch (FlexProcessException exception) {
			System.out.println(exception.getMessage());
		}
	}
}
