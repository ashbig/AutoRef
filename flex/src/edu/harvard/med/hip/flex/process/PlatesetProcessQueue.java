/* $Id $
 *
 * File     	: PlatesetProcessQueue.java
 * Date     	: 05292001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;

import java.util.*;
import java.sql.*;
import java.math.*;

import javax.sql.*;

import sun.jdbc.rowset.*;

/**
 * The plateset queue that contains all the queued plate sets
 * belong to the same protocol.
 */
public class PlatesetProcessQueue implements ProcessQueue {
    /**
     * Constructor.
     *
     * @return The PlatesetProcessQueue object.
     */
    public PlatesetProcessQueue() {}
    
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
        String sql = new String("select ps.platesetid as id, "+
        "ps.containerid_5p as oligo5p, " +
        "ps.containerid_3pfusion as oligo3pf, " +
        "ps.containerid_3pclosed as oligo3pc, " +
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "from plateset ps, queue q\n" +
        "where ps.platesetid = q.platesetid\n" +
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
    public LinkedList getQueueItems(Protocol protocol, String date)
    throws FlexDatabaseException {
        int protocolid = protocol.getId();
        String sql = new String("select ps.platesetid as id, "+
        "ps.containerid_5p as oligo5p, " +
        "ps.containerid_3pfusion as oligo3pf, " +
        "ps.containerid_3pclosed as oligo3pc, " +
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "from plateset ps, queue q\n" +
        "where ps.platesetid = q.platesetid\n" +
        "and q.protocolid = "+protocolid+
        "and to_char(dateadded, 'fmYYYY-MM-DD') = '"+date+"'");
        
        LinkedList items = restore(protocol, sql);
        return items;
    }
 
    /**
     * Retrieve the batch of queued items which are waiting for the
     * next workflow process on a particular date from the Queue table,
     * and have a certain execution status.
     *
     * @param protocol The protocol object.
     * @param executionstatus The status of the queue items.
     * @return A LinkedList of QueueItem objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItemsWithStatus(Protocol protocol, String executionstatus)
    throws FlexDatabaseException {
        return null;
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
        "(protocolid, dateadded, platesetid)\n" +
        "values(?, sysdate, ?)");
        PreparedStatement stmt = null;
        try {
            stmt = c.prepareStatement(sql);
            
            Vector v = new Vector();
            ListIterator iter = items.listIterator();
            
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Protocol protocol = item.getProtocol();
                int protocolid = protocol.getId();
                Plateset ps = (Plateset)item.getItem();
                int psid = ps.getId();
                
                stmt.setInt(1, protocolid);
                stmt.setInt(2, psid);
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Delete all the selected QueueItem objects from the Queue
     * table of the database.
     *
     * @param LinkedList The List of QueueItem objects.
     * @param c The database Connection object.
     * @exception FlexDatabaseException.
     */
    public void removeQueueItems(LinkedList items, Connection c) throws FlexDatabaseException {
        if (items == null)
            return;
        
        String sql = "delete from queue\n" +
        "where protocolid = ?\n" +
        "and to_char(dateadded, 'fmYYYY-MM-DD') = ?\n" +
        "and platesetid = ?";
        PreparedStatement stmt = null;
        try {
            
            stmt = c.prepareStatement(sql);
            
            Vector v = new Vector();
            ListIterator iter = items.listIterator();
            
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Protocol protocol = item.getProtocol();
                int protocolid = protocol.getId();
                String date = item.getDate();
                Plateset ps = (Plateset)item.getItem();
                int psid = ps.getId();
                
                stmt.setInt(1, protocolid);
                stmt.setString(2, date);
                stmt.setInt(3, psid);
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Update the queued items.
     *
     * @param LinkedList The List of QueueItem objects.
     * @param c The Connection object.
     * @exception FlexDatabaseException.
     */
    public void updateQueueItems(LinkedList items, Connection c) throws FlexDatabaseException {
    }
    
    /**
     * Get all the queued items that from the database.
     */
    protected LinkedList restore(Protocol protocol, String sql) throws FlexDatabaseException {
        RowSet rs = null;
        try {
        rs = DatabaseTransaction.getInstance().executeQuery(sql);
       LinkedList items = new LinkedList();
        
        while(rs.next()) {
            int id = rs.getInt("ID");
            int oligo5p = rs.getInt("OLIGO5P");
            int oligo3pf = rs.getInt("OLIGO3PF");
            int oligo3pc = rs.getInt("OLIGO3PC");
            String date = rs.getString("DATEADDED");
            Plateset ps = new Plateset(id, oligo5p, oligo3pf, oligo3pc);
            QueueItem item = new QueueItem(ps, protocol, date);
            items.addLast(item);
        }
        
        return items;
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    
    public static void main(String [] args) throws Exception {
        try {
            QueueFactory factory = new StaticQueueFactory();
            ContainerProcessQueue queue = (ContainerProcessQueue)factory.makeQueue("PlatesetProcessQueue");
            Protocol protocol = new Protocol(1, null, "identify sequences from unigene");
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection c = t.requestConnection();
            
            System.out.println("Insert into queue:");
            for(int i=1; i<6; i++) {
                System.out.println("Plateset ID: "+i);
                DatabaseTransaction.executeUpdate("insert into queue values(1, sysdate, null, null, null,"+i+")", c);
            }
            c.commit();
            
            LinkedList items = queue.getQueueItems(protocol);
            ListIterator iter = items.listIterator();
            
            System.out.println("Get items from queue:");
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Plateset ps = (Plateset)item.getItem();
                System.out.println("Plateset ID: "+ps.getId());
            }
            
            System.out.println("Remove items from queue:");
            queue.removeQueueItems(items, c);
            c.commit();
            
            System.out.println("Get items from queue:");
            LinkedList newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Plateset ps = (Plateset)item.getItem();
                System.out.println("Plateset ID: "+ps.getId());
            }
            
            System.out.println("Add items to queue:");
            queue.addQueueItems(items, c);
            c.commit();
            
            System.out.println("Get items from queue:");
            newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Plateset ps = (Plateset)item.getItem();
                System.out.println("Plateset ID: "+ps.getId());
            }
            
            System.out.println("Remove items from queue:");
            queue.removeQueueItems(items, c);
            c.commit();   
            
            System.out.println("Get items from queue:");
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Plateset ps = (Plateset)item.getItem();
                System.out.println("Plateset ID: "+ps.getId());
            }
             
            c.close();
        } catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        } catch (FlexProcessException exception) {
            System.out.println(exception.getMessage());
        } finally {
            System.exit(0);
        }
    }
}
