/**
 * $Id: Request.java,v 1.5 2001-07-19 14:48:10 dzuo Exp $
 *
 * File     	: Request.java
 * Date     	: 05032001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.sql.*;

/**
 * This class corresponds to the request table in the database.
 * It represents the user's cloning request.
 */
public class Request {
    private int id = -1;
    private String username;
    private String date;
    private Vector sequences;
    
    /**
     * Constructor. It takes an ID and queries the database to get the data.
     *
     * @return The Request object.
     */
    public Request(int id) throws FlexDatabaseException {        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        String sql = "select sequenceid from requestsequence\n"+
                     "where requestid = "+id;
        ResultSet sequenceRs = null;
        
        try {
            sequenceRs = t.executeQuery(sql);
        
            Vector seqs = new Vector();
            while (sequenceRs.next()) {            
                int seqid = sequenceRs.getInt("SEQUENCEID");
                FlexSequence sequence = new FlexSequence(seqid);
                seqs.addElement(sequence);
            }
            this.id = id;
            this.sequences = seqs;
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(sequenceRs);
        }
    }
    
    /**
     * Constructor. Data is constructed directly from database.
     */
    public Request(int id, String username, String date, Vector sequences) {
        this.id = id;
        this.username = username;
        this.date = date;
        this.sequences = sequences;
    }
    
    /**
     * Constructor. Data is constructed directly from database.
     */
    public Request(int id, String username, String date) {
        this.id = id;
        this.username = username;
        this.date = date;
    }
    
    /**
     * Constructor.
     */
    public Request(String username, Vector sequences) {
        this.username = username;
        this.sequences = sequences;
    }
    
    /**
     * Constructor.
     */
    public Request(String username) {
        this.username = username;
        this.sequences = new Vector();
    }
    
    /**
     * Return request id.
     *
     * @return The request id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the username.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username to the given value.
     *
     * @param username The value to be set to.
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Return the request date.
     *
     * @return The request date.
     */
    public String getDate() {
        return date;
    }
    
    /**
     * Set the date to the given value.
     *
     * @param date The value to be set to.
     */
    public void setDate(String date) {
        this.date = date;
    }
    
    /**
     * Return the request sequences.
     *
     * @return The requested sequence.
     */
    public Vector getSequences() {
        return sequences;
    }
    
    /**
     * Return the total number of requested sequences.
     *
     * @return The total number of requested sequences.
     */
    public int getNumSequences() {
        return getSequences().size();
    }
    
    /**
     * Return the total number of non-processed sequences.
     *
     * @return The total number of non-processed sequences.
     */
    public int getPendingSequences() {
        Vector seqs = this.getSequences();
        int count = 0;
        
        for(int i=0; i<seqs.size(); i++) {
            FlexSequence seq = (FlexSequence)seqs.elementAt(i);
            String status = seq.getFlexstatus();
            if(FlexSequence.PENDING.equals(status)) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Return the total number of processed sequences.
     *
     * @return The total number of processed sequences.
     */
    public int getProcessedSequences() {
        return (this.getNumSequences() - this.getPendingSequences());
    }
    
    /**
     * Add the sequence to the request.
     *
     * @param sequence The FlexSequence object to be added.
     */
    public void addSequence(FlexSequence sequence) {
        sequences.addElement(sequence);
    }
    
    /**
     * Insert the request into request table. Also insert the
     * requested sequence records.
     *
     * @param conn <code>Connection</code> to user for insert
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        if(sequences.size() == 0)
            return;
        
        if (id == -1) {
            id = FlexIDGenerator.getID("requestid");
        }
        
        String sql = "insert into request\n"+
        "(requestid, username, requestdate)\n"+
        "values("+id+",'"+username+"',sysdate)";
        
        DatabaseTransaction.executeUpdate(sql, conn);
        
        // Insert the sequence record.
        Enumeration enum = sequences.elements();
        while(enum.hasMoreElements()) {
            FlexSequence sequence = (FlexSequence)enum.nextElement();
            if(FlexSequence.NEW.equals(sequence.getFlexstatus()))
                sequence.insert(conn);
            String reqSql = "insert into requestsequence\n"+
            "values ("+id+","+sequence.getId()+")";
            DatabaseTransaction.executeUpdate(reqSql,conn);
            
        }
    }
    
    public static void main(String [] args) {
        Request r = new Request("Larry Shumway");
        for(int i=1; i<5; i++) {
            FlexSequence seq = new FlexSequence(i);
            seq.setFlexstatus(FlexSequence.NEW);
            r.addSequence(seq);
        }
        Connection conn = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            r.insert(conn);
            conn.rollback();
        } catch (FlexDatabaseException e) {
            System.out.println(e);
            
        } catch (SQLException sqlE) {
            System.out.println(sqlE);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}




