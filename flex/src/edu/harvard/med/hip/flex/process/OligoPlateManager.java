/*
 * OligoPlateManager.java
 * This class manages the oligo calculation, construct generation
 * and oligo plate generation
 * Created on May 31, 2001, 3:41 PM
 * @author  Wendy
 * @version
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;

/**
 *
 *
 */
public class OligoPlateManager {
    private Connection conn;
    /**
     * Constructor
     * Creates new OligoPlateManager
     */
    public OligoPlateManager() throws FlexDatabaseException {
        try{
            this.conn = DatabaseTransaction.getInstance().requestConnection();
        } catch(FlexDatabaseException sqlex){
            throw new FlexDatabaseException(sqlex);
        }
    }
    
    /**
     * Check the total number of sequences in the Queue table which are
     * waiting for Design_Construct protocol
     * @return The total number of sequences.
     */
    protected int totalSeqQueue(int cdsSizeGroup) throws FlexDatabaseException {
        int count = 0;
        ResultSet rs = null;
        
        // get the protocol id for "DESIGN_CONSTRUCT" protocol
        int protocolId = (ProcessProtocol.DESIGN_CONSTRUCT).getId();
        protocolId = 3; //for testing
        String sql = "SELECT COUNT(q.sequenceid) " +
        "FROM queue q, flexsequence s \n" +
        "WHERE q.sequenceid = s.sequenceid \n" +
        "AND protocolId =" + protocolId + "\n" +
        "AND s.cdslength <= " + cdsSizeGroup;
        
        try {
            rs = DatabaseTransaction.getInstance().executeQuery(sql);
            while (rs.next()) {
                count = rs.getInt(1);
            } // while
            
        } catch (SQLException sqlex) {
            throw new FlexDatabaseException(sqlex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return count;
    } //
    
    /**
     * If there are 94 sequences or 94 X sequence of the same size class
     * in the queue, retrieve queue records and related sequence records
     * where protocol = 'design construct'
     * @return LinkedList A list of sequence objects
     */
    private LinkedList getQueueSequence(int cdsSizeGroup) throws FlexDatabaseException {
        int protocolId = 3;  // to be modified
        //int cdsSizeGroup = 2000;
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocolId, cdsSizeGroup);
        
        //delete sequence queue record from the queue table
        //seqQueue.removeQueueItems(seqList, protocolId, conn);
              
        return seqList;
    }
    
    public void createOligoAndConstruct() throws FlexDatabaseException {
        int smallCDSLimit = 2000;
        int mediumCDSLimit = 4000;
        int totalWells = 94;
       // if ((totalSeqQueue(smallCDSLimit) % 94)== 0){
            LinkedList seqList = getQueueSequence(smallCDSLimit);
            ConstructGenerator cg = new ConstructGenerator(seqList, conn);
            cg.generateOligoAndConstructs();
          //  LinkedList oligoPatternList = new LinkedList();
          //  oligoPatternList = cg.getOligoPatternList();
          //  int platesetId = cg.getPlatesetId();
          //  OligoPlater plater = new OligoPlater(oligoPatternList,platesetId,conn);
          //  plater.createOligoPlates();
            
      //  } else if ((totalSeqQueue(mediumCDSLimit) % 94)== 0) {
            
            
      //  } else {
            
      //  }
    } //createOligoAndConstruct
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
        int totalQueue = 0;
        try {
            OligoPlateManager om = new OligoPlateManager();
            totalQueue = om.totalSeqQueue(2000);
            System.out.println("There are total of " + totalQueue + " small sequences in the queue");
            
            System.out.println("Calculating oligos...");
            om.createOligoAndConstruct();
            
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } finally {
            //  DatabaseTransaction.closeConnection(conn);
        }
        
        
    } //main
    
}
