/**
 * $Id: Sequence.java,v 1.1 2001-07-06 19:28:43 jmunoz Exp $
 * The Sequence class serves as a base class for any type of DNA sequence
 * Start and Stop reprensent the position of the ATG start and the stop codon
 * @File     	: Sequence.java
 * @Date     	: 04052001
 * @author	: Wendy Mar
 *
 * modified :   05302001 wmar
 * modified :   the constructor, added getSeqText method.
 * deleted :    getDescription method
 *
 * modified:    06122001 wmar
 *              Added testing method
 */

package edu.harvard.med.hip.flex.core;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;

public class Sequence {
    private static final int seqFragmentLength = 60;
    protected int start = -1;
    protected int stop = -1;
    protected String text = null;
    protected int seqID = -1;  //for testing
    /**
     * Constructor.  Returns a Sequence object
     * @param seqID  Unique identifier for each sequence
     * @param start  An integer represents the start of the coding region
     * @param stop   An integer represents the stop of the coding region
     * @param seqText An String object represents the text of the sequence
     * @return  A Sequence object
     */
    public Sequence(int seqId, int start, int stop) throws FlexDatabaseException {
        this.seqID = seqId;
        this.start = start;
        this.stop = stop;
        this.text = getSeqText(seqId);
    }
    
    /**
     * Return the sequence id
     * @return the sequence id
     */
    public int getSeqID() {
        return seqID;
    }
    
    /**
     * Return the sequence id
     * @return the sequence id
     */
    public int getId() {
        return seqID;
    }
    
    /**
     * Return the integer position where the coding region starts
     * @return the start position of the coding region
     */
    public int getStart() {
        return start;
    }
    
    /**
     * Return the integer position where the coding region stops
     * @return the stop position of the coding region
     */
    public int getStop() {
        return stop;
    }
    
    /**
     * Return the integer that represents the length of the coding region
     * @return the length of the coding region
     */
    public int getCDSLength() {
        return (stop - start + 1);
    }
    
    /**
     * Return the integer that represents the length of the entire sequence
     * @return the length of the entire sequence text
     */
    public int getSeqLength() {
        return text.length();
    }
    
    /**
     * Return the sequence fragment which is 60 bases downstream
     * from the start position for five prime oligo calculation
     * @return A String which represents the first 60 bases of
     * of the coding region
     */
    public String getSeqFragmentStart() {
        int endPos = start + seqFragmentLength;
        return (text.substring(start - 1, endPos));
    }
    
    /**
     * Return the sequence fragment which is 60 bases uptream
     * from the stop position for three prime oligo calculation
     * @return A String which represents the last 60 bases of
     * of the coding region including the stop codon
     */
    public String getSeqFragmentStop() {
        int startPos = stop-(seqFragmentLength-1); 
        return (text.substring(startPos, stop));
    }
    
    /**
     * Return the full-length DNA sequence text retrieved from the
     * SEQUENCETEXT table for a FLEX sequence
     * @param seqid the SequenceID
     * @return the sequence text as String
     */
    public String getSeqText(int seqid) throws FlexDatabaseException {
        String text = null;
        int order;
        String sql = null;
        String result = "";
        ResultSet rs = null;
        
        sql = "SELECT t.sequenceorder, t.sequencetext, t.sequenceid" +
        " FROM sequencetext t" +
        " WHERE sequenceid =" + seqid +
        " ORDER BY t.sequenceorder";
        
        rs = DatabaseTransaction.getInstance().executeQuery(sql);
        try {
            while (rs.next()) {
                //order = rs.getInt(1);
                text = rs.getString("SEQUENCETEXT");
                result = result + text;
            } //while
        }catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        }
        finally {
            DatabaseTransaction.closeResultSet(rs);
        }       
        return result;
    } //getSeqText
    
    public static void main(String [] args) {
        Connection c = null;
        int seqId = 22; // for testing...
        String seqText;
        
        try {
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            
            Sequence seq = new Sequence(seqId, 247,618);
            seqText = seq.getSeqText(seqId);
            System.out.println("Sequence ID :" + seqId);
            System.out.println(seqText);
            System.out.println("CDS: "+seq.getCDSLength());
            System.out.println("Sequence length: " + seq.getSeqLength());
            System.out.println("Start pos: " + seq.getStart());
            System.out.println("SeqFragmentstart: "+seq.getSeqFragmentStart());
            System.out.println("seqFragmentStop: " +seq.getSeqFragmentStop());
           
        } catch (FlexDatabaseException exception) {
            System.out.println(exception.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}