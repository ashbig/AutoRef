/* $Id: ContainerProcessQueue.java,v 1.3 2001-05-24 10:55:37 dongmei_zuo Exp $ 
 *
 * File     	: ContainerProcessQueue.java 
 * Date     	: 04162001
 * Author	: Dongmei Zuo, Wendy Mar
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

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
	public LinkedList getQueueItems(Protocol protocol) throws FlexDatabaseException {
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
						"and to_char(dateadded, 'fmYYYY-MM-DD') = '"+date+"'");

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
						"(protocolid, dateadded, containerid)\n" +
						"values(?, sysdate, ?)");
		PreparedStatement stmt = c.prepareStatement(sql);

		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			Container c = (Container)item.getItem();
			int containerid = c.getId();

			stmt.setInt(1, protocolid);
			stmt.setInt(2, containerid);
			DatabaseTransaction.executeUpdate(stmt);
		}
		stmt.close();	
	}		

	/**
 	 * Delete all the selected QueueItem objects from the Queue   
	 * table of the database.
	 *
	 * @param LinkedList The List of QueueItem objects.
	 * @param c The database Connection object.
	 * @exception FlexDatabaseException.
 	 */
	public void removeQueueItems (LinkedList items, Connection c) throws FlexDatabaseException {
		if (items == null) 
			return;
		
		String sql = "delete from queue\n" +
				 "where protocolid = ?\n" +
				 "and to_char(dateadded, 'fmYYYY-MM-DD') = ?\n" +
				 "and containerid = ?";
		PreparedStatement stmt = c.prepareStatement(sql);

		Vector v = new Vector();
		ListIterator iter = items.listIterator();

		while (iter.hasNext()) {
			QueueItem item = (QueueItem) iter.next();
			Protocol protocol = item.getProtocol();
			int protocolid = protocol.getId();
			String date = item.getDate();
			Container c = (Container)item.getItem();
			int containerid = c.getId();

			stmt.setInt(1, protocolid);
			stmt.setString(2, date);
			stmt.setInt(3, containerid);
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
			String type = rs.getString("TYPE");
			int locationid = rs.getInt("LOCATIONID");
			String locationtype = rs.getString("LOCATIONTYPE");
			String description = rs.getString("DESCRIPTION");
			String label = rs.getString("LABEL");
			String date = rs.getString("DATEADDED");
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
			Connection c = t.requestConnection();
			DatabaseTransaction.executeUpdate("insert into containerlocation values (1, 'test',null)", c);
			DatabaseTransaction.executeUpdate("insert into containertype values ('PCR')", c);
			for(int i=1; i<6; i++) 
				DatabaseTransaction.executeUpdate("insert into containerheader values ("+i+",'PCR', 1, 'PCR1')", c);
			DatabaseTransaction.executeUpdate("insert into processprotocol values (10, 'test', 'test', null)", c);
	
			System.out.println("Insert into queue:");
			for(int i=1; i<6; i++) {
				System.out.println("Container ID: "+i);
				DatabaseTransaction.executeUpdate("insert into queue values(10, sysdate, null, null, null,"+i+")", c);
			}

			LinkedList items = queue.getQueueItems(protocol);
			ListIterator iter = items.listIterator();

			System.out.println("Get items from queue:");
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				Container c = (Container)item.getItem();
				System.out.println("Container ID: "+c.getId());
			}

			System.out.println("Remove items from queue:");
			queue.removeQueueItems(items, c);
			
			System.out.println("Get items from queue:");
			LinkedList newitems = queue.getQueueItems(protocol);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				Container c = (Container)item.getItem();
				System.out.println("Container ID: "+c.getId());
			}

			System.out.println("Add items to queue:");
			queue.addQueueItems(items, c);
			System.out.println("Get items from queue:");
			newitems = queue.getQueueItems(protocol);
			iter = newitems.listIterator();
			while (iter.hasNext()) {
				QueueItem item = (QueueItem) iter.next();
				Container c = (Container)item.getItem();
				System.out.println("Container ID: "+c.getId());
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
