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
     * @param protocolId The protocol ID.
     * @cdsSize The integer indicating small, medium or large cds size class
     * @return A LinkedList of Sequence objects
     */
    
    public LinkedList getQueueItems(int protocolId, int cdsSize) throws FlexDatabaseException {
        //The result list stores a list of sequence objects
        LinkedList result = new LinkedList();
        
        String sql = "SELECT s.sequenceid as seqid, s.cdsstart, s.cdsstop \n"+
        "FROM FLEXSEQUENCE s, QUEUE q\n" +
        "WHERE s.sequenceid = q.sequenceid\n" +
        "AND q.protocolid = "+ protocolId + "\n" +
        "AND s.cdslength <= " + cdsSize;
        result = restore(sql);
        
        return result; //return the LinkedList
    } // getQueueItems
    
    /**
     * Retrieve the batch of queued items which are waiting for the
     * next workflow process on a particular date from the Queue table
     *
     * @param protocolId The protocol ID.
     * @param date The date added to the queue in yyyy-mm-dd format.
     * @return A LinkedList of sequence objects.
     * @exception FlexDatabaseException.
     */
    public LinkedList getQueueItems(int protocolId, String date) throws FlexDatabaseException {
        
        LinkedList result = new LinkedList();
        String sql = "SELECT s.sequenceid as seqid, s.cdsstart, s.cdsstop"+
        "to_char(q.dateadded, 'fmYYYY-MM-DD') as dateadded\n" +
        "FROM FLEXSEQUENCE s, QUEUE q\n" +
        "WHERE s.sequenceid = q.sequenceid\n" +
        "AND q.protocolid = "+ protocolId + ",\n" +
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
    public void removeQueueItems(LinkedList seqList, int protocolId, Connection c) throws FlexDatabaseException {
        if (seqList == null)
            return;
        
        String sql = "DELETE FROM queue\n" +
        "WHERE protocolid = "+ protocolId  + "\n" +
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
            throw new FlexDatabaseException(sqlE);
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
                System.out.println("sequence ID from rs: "+id);
                start = rs.getInt(2);
                stop = rs.getInt(3);
                seq = new Sequence(id,start,stop);
                itemlist.add(seq);
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
        int protocolId = 3; //protocol ID for 'Design Construct' is 3
        try {
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            //get a list of sequence objects
            LinkedList items = new LinkedList();
            items = queue.getQueueItems(protocolId, 2000);
            ListIterator iter = items.listIterator();
            System.out.println("Get small sequences from queue:");
            while (iter.hasNext()) {
                Sequence seq = (Sequence) iter.next();
                System.out.print("Sequence ID: "+seq.getId()+ "\n");
                System.out.println("CDS: "+seq.getCDSLength());
                System.out.println("Start: "+ seq.getStart());
                System.out.println("Stop: "+ seq.getStop());
            } //while
        } catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}
