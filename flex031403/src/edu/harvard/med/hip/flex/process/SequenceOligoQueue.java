/*
 * SequenceOligoQueue.java
 *
 * Created on May 30, 2001, 5:54 PM
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;

/**
 * @date  5/30/01
 * @author  Wendy Mar
 * @version
 */
public class SequenceOligoQueue {
    
    /** Creates new SequenceOligoQueue */
    public SequenceOligoQueue() {
    }
    
    /**
     * Retrieve all the queued sequences which are waiting for
     * oligo primer calculation
     *
     * @param protocol The process protocol.
     * @cdsSize The integer indicating small, medium or large cds size class
     * @return A LinkedList of Sequence objects
     */
    
    public LinkedList getQueueItems(Protocol protocol, int cdsSize) throws FlexDatabaseException {
        //The result list stores a list of sequence objects
        LinkedList result = new LinkedList();
        
        String sql = "SELECT s.sequenceid as seqid, s.cdsstart, s.cdsstop \n"+
        "FROM FLEXSEQUENCE s, QUEUE q\n" +
        "WHERE s.sequenceid = q.sequenceid\n" +
        "AND q.protocolid = "+ protocol.getId() + "\n" +
        "AND s.cdslength <= " + cdsSize;
        result = restore(sql);
        
        return result; //return the LinkedList
    } // getQueueItems
    
    
    /**
     * Retrieve all the queued sequences which are waiting for
     * oligo primer calculation (construct design)
     *
     * @param protocol The process protocol.
     * @cdsSizeLowerLimit The integer indicates the lower limit of the cds size class
     * @cdsSizeUpperLimit The integer indicates the upper limit of the cds size class
     * @return A LinkedList of Sequence objects
     */
        public LinkedList getQueueItems(Protocol protocol, int cdsSizeLowerLimit, 
         int cdsSizeUpperLimit) throws FlexDatabaseException {
        //The result list stores a list of sequence objects
        LinkedList result = new LinkedList();
        
        String sql = "SELECT s.sequenceid as seqid, s.cdsstart, s.cdsstop \n"+
        "FROM FLEXSEQUENCE s, QUEUE q\n" +
        "WHERE s.sequenceid = q.sequenceid\n" +
        "AND q.protocolid = "+ protocol.getId() + "\n" +
        "AND s.cdslength >= " + cdsSizeLowerLimit + "\n" +
        "AND s.cdslength < " + cdsSizeUpperLimit;
        result = restore(sql);
        
        return result; //return the LinkedList
    } // getQueueItems
       
    /**
     * Retrieve the batch of queued items which are waiting for the
     * next workflow process on a particular date from the Queue table
     *
     * @param protocol The protocol.
     * @param date The date added to the queue in yyyy-mm-dd format.
     * @return A LinkedList of sequence objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItems(Protocol protocol, String date) throws FlexDatabaseException {
        
        LinkedList result = new LinkedList();
        String sql = "SELECT s.sequenceid as seqid, s.cdsstart, s.cdsstop"+
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "FROM FLEXSEQUENCE s, QUEUE q\n" +
        "WHERE s.sequenceid = q.sequenceid\n" +
        "AND q.protocolid = "+ protocol.getId() + ",\n" +
        "AND to_char(dateadded, 'fmYYYY-MM-DD') = '"+date+"'";
        
        
        result = restore(sql);
        return result;
    }
    
    /**
     * Delete all of the selected sequence queue records from the
     * Queue table of the database.
     *
     * @param LinkedList The List of QueueItem objects.
     * @param c The database Connectin object.
     * @exception FlexDatabaseException.
     */
    public void removeQueueItems(LinkedList seqList, Protocol protocol, Connection c) throws FlexDatabaseException {
        if (seqList == null)
            return;
        
        String sql = "DELETE FROM queue\n" +
        "WHERE protocolid = "+ protocol.getId()  + "\n" +
        "AND sequenceid = ?";
        PreparedStatement stmt = null;
        try {
            stmt = c.prepareStatement(sql);
            Vector v = new Vector();
            ListIterator iter = seqList.listIterator();
            
            while (iter.hasNext()) {
                Sequence seq = (Sequence) iter.next();
                //String date = item.getDate();
                int sequenceId = seq.getId();
                stmt.setInt(1, sequenceId);
                DatabaseTransaction.executeUpdate(stmt);
            }
            
        } catch(SQLException sqlE) {
           // throw new FlexDatabaseException(sqlE);
            throw new FlexDatabaseException("Error occured while deleting sequences from queue\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    /**
     * Get all the queued items from the database.
     * @param sql the SQL String
     * @return A LinkedList of sequence objects
     */
    protected LinkedList restore(String sql) throws FlexDatabaseException {
        Sequence seq = null;
        LinkedList itemlist = new LinkedList();
        ResultSet rs = null;
        int id, start, stop;
        
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next()) {
                id = rs.getInt(1);
                start = rs.getInt(2);
                stop = rs.getInt(3);
                seq = new Sequence(id,start,stop);
                itemlist.add(seq);
                System.out.println("sequence ID read from queue: "+id);
            } // while
            
        } catch (SQLException sqlex) {
            throw new FlexDatabaseException(sqlex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return itemlist;
    } //restore
    
    public static void main(String [] args) {
        Connection c = null;
        SequenceOligoQueue queue = new SequenceOligoQueue();
        
        try {
            Protocol protocol = new Protocol("design constructs");
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            
            //get a list of sequence objects
            LinkedList items = queue.getQueueItems(protocol, 2000);
            ListIterator iter = items.listIterator();
            System.out.println("Get small sequences from queue:");
            while (iter.hasNext()) {
                Sequence seq = (Sequence) iter.next();
                System.out.print("Sequence ID: "+seq.getId()+ "\n");
                System.out.println("CDS: "+seq.getCDSLength());
                System.out.println("Start: "+ seq.getStart());
                System.out.println("Stop: "+ seq.getStop());
            } //while
            
            //remove the sequence from queue
            System.out.println("removing sequences from queue...");
            queue.removeQueueItems(items,protocol,c);
            System.out.println("sequences removed!");
            DatabaseTransaction.commit(c);
        } catch (FlexDatabaseException exception) {
            DatabaseTransaction.rollback(c);
            System.out.println(exception.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}
