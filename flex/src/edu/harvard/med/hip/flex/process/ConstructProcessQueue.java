/* $Id $
 *
 * File     	: ConstructProcessQueue.java
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
 * The construct queue that contains all the queued plate sets
 * belong to the same protocol.
 */
public class ConstructProcessQueue implements ProcessQueue {
    /**
     * Constructor.
     *
     * @return The ConstructProcessQueue object.
     */
    public ConstructProcessQueue() {}
    
    /**
     * Retrieve all of the queued items which are waiting for the
     * next workflow process from the Queue table
     *
     * @param protocol The protocol object.
     * @return A List of QueueItem objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItems(Protocol protocol) throws FlexDatabaseException {
        int protocolid = protocol.getId();
        String sql = new String("select c.constructid as id, "+
        "c.sequenceid as sequenceid, " +
        "c.oligoid_5p as oligo5p, " +
        "c.oligoid_3p as oligo3p, " +
        "c.constructtype as type, " +
        "c.constructsizeclass as size, " +
        "c.constructpairid as pairid, " +
        "c.platesetid as platesetid, " +
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "from constructdesign c, queue q\n" +
        "where c.constructid = q.platesetid\n" +
        "and q.protocolid = "+protocolid);
        
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
        String sql = new String("select c.constructid as id, "+
        "c.sequenceid as sequenceid, " +
        "c.oligoid_5p as oligo5p, " +
        "c.oligoid_3p as oligo3p, " +
        "c.constructtype as type, " +
        "c.constructsizeclass as size, " +
        "c.constructpairid as pairid, " +
        "c.platesetid as platesetid, " +
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "from constructdesign c, queue q\n" +
        "where c.constructid = q.platesetid\n" +
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
    public void addQueueItems(List items, Connection c) throws FlexDatabaseException {
        if (items == null)
            return;
        
        String sql = new String("insert into queue\n" +
        "(protocolid, dateadded, constructid)\n" +
        "values(?, sysdate, ?)");
        PreparedStatement stmt = null;
        try {
            stmt = c.prepareStatement(sql);
            
            Vector v = new Vector();
            Iterator iter = items.iterator();
            
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Protocol protocol = item.getProtocol();
                int protocolid = protocol.getId();
                Construct construct = (Construct)item.getItem();
                int constructid = construct.getId();
                
                stmt.setInt(1, protocolid);
                stmt.setInt(2, constructid);
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
    public void removeQueueItems(List items, Connection c) throws FlexDatabaseException {
        if (items == null)
            return;
        
        String sql = "delete from queue\n" +
        "where protocolid = ?\n" +
        "and to_char(dateadded, 'fmYYYY-MM-DD') = ?\n" +
        "and constructid = ?";
        PreparedStatement stmt = null;
        try {
            
            stmt = c.prepareStatement(sql);
            
            Vector v = new Vector();
            Iterator iter = items.iterator();
            
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Protocol protocol = item.getProtocol();
                int protocolid = protocol.getId();
                String date = item.getDate();
                Construct construct = (Construct)item.getItem();
                int constructid = construct.getId();
                
                stmt.setInt(1, protocolid);
                stmt.setString(2, date);
                stmt.setInt(3, constructid);
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
    public void updateQueueItems(List items, Connection c) throws FlexDatabaseException {
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
            int sequenceid = rs.getInt("SEQUENCEID");
            int oligo5p = rs.getInt("OLIGO5P");
            int oligo3p = rs.getInt("OLIGO3P");
            String type = rs.getString("TYPE");
            String size = rs.getString("SIZE");
            int pairid = rs.getInt("PAIRID");
            int platesetid = rs.getInt("PLATESETID");
            String date = rs.getString("DATEADDED");
            Construct construct = new Construct(id, sequenceid, oligo5p, oligo3p, type, size, pairid, platesetid);
            QueueItem item = new QueueItem(construct, protocol, date);
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
            ContainerProcessQueue queue = (ContainerProcessQueue)factory.makeQueue("ConstructProcessQueue");
            Protocol protocol = new Protocol(1, null, "identify sequences from unigene");
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection c = t.requestConnection();
            
            System.out.println("Insert into queue:");
            for(int i=1; i<6; i++) {
                System.out.println("Construct ID: "+i);
                DatabaseTransaction.executeUpdate("insert into queue values(1, sysdate, null, null, null,"+i+")", c);
            }
            c.commit();
            
            LinkedList items = queue.getQueueItems(protocol);
            ListIterator iter = items.listIterator();
            
            System.out.println("Get items from queue:");
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Construct construct = (Construct)item.getItem();
                System.out.println("Construct ID: "+construct.getId());
            }
            
            System.out.println("Remove items from queue:");
            queue.removeQueueItems(items, c);
            c.commit();
            
            System.out.println("Get items from queue:");
            LinkedList newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Construct construct = (Construct)item.getItem();
                System.out.println("Construct ID: "+construct.getId());
           }
            
            System.out.println("Add items to queue:");
            queue.addQueueItems(items, c);
            c.commit();
            
            System.out.println("Get items from queue:");
            newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Construct construct = (Construct)item.getItem();
                System.out.println("Construct ID: "+construct.getId());
           }
            
            System.out.println("Remove items from queue:");
            queue.removeQueueItems(items, c);
            c.commit();   
            
            System.out.println("Get items from queue:");
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Construct construct = (Construct)item.getItem();
                System.out.println("Construct ID: "+construct.getId());
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
