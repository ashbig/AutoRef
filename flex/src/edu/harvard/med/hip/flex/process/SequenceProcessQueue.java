/* $Id: SequenceProcessQueue.java,v 1.13 2001-08-28 17:36:25 dzuo Exp $
 *
 * File     	: SequenceProcessQueue.java
 * Date     	: 05072001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.workflow.*;

import java.sql.*;
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
    public SequenceProcessQueue() {}
    
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
        
        LinkedList items = restore(protocol, null, null, sql);
        return items;
        
    }

    /**
     * Retrieve all of the queued items which are waiting for the
     * next workflow process from the Queue table
     *
     * @param protocol The protocol object.
     * @param project The project to work with.
     * @param workflow The workflow to work with.
     * @return A List of QueueItem objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItems(Protocol protocol, Project project, Workflow workflow) 
    throws FlexDatabaseException {
        return null;
    }
    
    /**
     * Gets queue items for the protocol starting at offset, with length length
     *
     * @param protocol the protocol to get items from the queue with.
     * @param project The project to get items from the queue with.
     * @param offset The offset to get at
     * @param legnth The number of rows to get back.
     */
    public LinkedList getQueueItems(Protocol protocol, Project project, Workflow workflow, int offset, int length)
    throws FlexDatabaseException {
        String sql = "select * from "+
        "(select q.sequenceid as id, " +
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded " +
        "from queue q " +
        "where q.protocolid =  " + protocol.getId() + " " +
        "and q.projectid = " + project.getId() + " " +
        "and q.workflowid = " + workflow.getId() + " " +
        "order by q.dateadded) " +
        "where ROWNUM <  "+ (offset + 1 + length) +" " +
        "Minus ( " +
        "select * from ( "+
        "select q.sequenceid as id, "+
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded "+
        "from queue q "+
        "where q.protocolid =  "+ protocol.getId() + " "+
        "and q.projectid = " + project.getId() + " " +
        "and q.workflowid = " + workflow.getId() + " " +
        "order by q.dateadded " +
        ") " +
        "where rownum < " + (offset + 1) +
        ")";
        LinkedList items = restore(protocol, project, workflow, sql);
        return items;
    }
    
    /**
     * Finds the number of items in the queue for a protocol.
     *
     * @param protocol Protocol to count items for.
     * @param project The project to count items for.
     *
     * @return number of items in the queue for the given protocol and project.
     */
    public int getQueueSize(Protocol protocol, Project project, Workflow workflow) throws FlexDatabaseException{
        String sql = "select count(*) as queue_size " +
        "from queue q " +
        "where q.protocolid =  " + protocol.getId()+
        " and q.workflowid = " + workflow.getId() + 
        " and q.projectid = "+project.getId();
        int size =0;
        
        try {
            
            ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
            if (rs.next()) {
                size = rs.getInt("queue_size");
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        }
        
        return size;
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
    public LinkedList getQueueItems(Protocol protocol, String date) throws FlexDatabaseException {
        int protocolid = protocol.getId();
        String sql = "select q.sequenceid as id, "+
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "from queue q\n" +
        "where q.protocolid = "+protocolid+
        "and to_char(dateadded, 'fmYYYY-MM-DD') = '"+date+"'";
        
        LinkedList items = restore(protocol, null, null, sql);
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
    public void addQueueItems(List items, Connection c) throws FlexDatabaseException {
        if (items == null)
            return;
        
        String sql = new String("insert into queue\n" +
        "(protocolid, dateadded, sequenceid, projectid, workflowid)\n" +
        "values(?, sysdate, ?, ?, ?)");
        PreparedStatement stmt = null ;
        try {
            stmt= c.prepareStatement(sql);
            Vector v = new Vector();
            Iterator iter = items.iterator();
            
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                Protocol protocol = item.getProtocol();
                Project project = item.getProject();
                Workflow workflow = item.getWorkflow();

                FlexSequence s = (FlexSequence)item.getItem();
                int sequenceid = s.getId();
                
                stmt.setInt(1, protocol.getId());
                stmt.setInt(2, sequenceid);
                stmt.setInt(3, project.getId());
                stmt.setInt(4, workflow.getId());
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
     * @param c The database Connectin object.
     * @exception FlexDatabaseException.
     */
    public void removeQueueItems(List items, Connection c) throws FlexDatabaseException {
        if (items == null)
            return;
        
        String sql = "delete from queue\n" +
        "where protocolid = ?\n" +
        "and to_char(dateadded, 'fmYYYY-MM-DD') = ?\n" +
        "and sequenceid = ?\n" +
        "and projectid = ?\n" +
        "and workflowid = ?";
        
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
                FlexSequence s = (FlexSequence)item.getItem();
                int sequenceid = s.getId();
                
                stmt.setInt(1, protocolid);
                stmt.setString(2, date);
                stmt.setInt(3, sequenceid);
                stmt.setInt(4, item.getProject().getId());
                stmt.setInt(5, item.getWorkflow().getId());
                
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
    protected LinkedList restore(Protocol protocol, Project project, Workflow workflow, String sql)
    throws FlexDatabaseException {
        ResultSet rs = null;
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            LinkedList items = new LinkedList();
            
            while(rs.next()) {
                int id = rs.getInt("ID");
                String date = rs.getString("DATEADDED");
                FlexSequence s = new FlexSequence(id);
                QueueItem item = new QueueItem(s, protocol, date, project, workflow);
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
    
    public static void main(String [] args) {
        Connection c = null;
        try {
            QueueFactory factory = new StaticQueueFactory();
            SequenceProcessQueue queue = (SequenceProcessQueue)factory.makeQueue("SequenceProcessQueue");
            Protocol protocol = new Protocol(1, null, "identify sequences from unigene");
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            //DatabaseTransaction.executeUpdate("insert into species values('Test Species')", c);
            //DatabaseTransaction.executeUpdate("insert into flexstatus values('NEW')", c);
            //DatabaseTransaction.executeUpdate("insert into flexstatus values('REJECTED')", c);
            //DatabaseTransaction.executeUpdate("insert into flexstatus values('QUESTIONABLE')", c);
            //DatabaseTransaction.executeUpdate("insert into processprotocol values (10, 'test', 'test', null)", c);
            
            //for (int i=1; i<5; i++) {
            //    DatabaseTransaction.executeUpdate("insert into flexsequence(sequenceid, flexstatus, genusspecies) values("+i+", 'REJECTED', 'Test Species')", c);
            //}
            
            System.out.println("Insert into queue:");
            for(int i=1; i<5; i++) {
                System.out.println("Sequence ID: "+i);
                DatabaseTransaction.executeUpdate("insert into queue(protocolid, dateadded, sequenceid) values(1, sysdate,"+i+")", c);
            }
            c.commit();
            
            System.out.println("OK");
            LinkedList items = queue.getQueueItems(protocol);
            ListIterator iter = items.listIterator();
            
            System.out.println("Get items from queue:");
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                FlexSequence seq = (FlexSequence)item.getItem();
                System.out.println("Sequence ID: "+seq.getId());
            }
            
            System.out.println("Remove items from queue:");
            queue.removeQueueItems(items, c);
            c.commit();
            
            System.out.println("Get items from queue:");
            LinkedList newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                FlexSequence seq = (FlexSequence)item.getItem();
                System.out.println("Sequence ID: "+seq.getId());
            }
            
            System.out.println("Add items to queue:");
            queue.addQueueItems(items, c);
            c.commit();
            System.out.println("Get items from queue:");
            newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                FlexSequence seq = (FlexSequence)item.getItem();
                System.out.println("Sequence ID: "+seq.getId());
            }
            
            System.out.println("Remove items from queue:");
            queue.removeQueueItems(items, c);
            c.commit();
            
            System.out.println("Get items from queue:");
            newitems = queue.getQueueItems(protocol);
            iter = newitems.listIterator();
            while (iter.hasNext()) {
                QueueItem item = (QueueItem) iter.next();
                FlexSequence seq = (FlexSequence)item.getItem();
                System.out.println("Sequence ID: "+seq.getId());
            }
        } catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        } catch (FlexProcessException exception) {
            System.out.println(exception.getMessage());
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    }
}
