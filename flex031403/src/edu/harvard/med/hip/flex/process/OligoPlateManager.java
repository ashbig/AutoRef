/*
 * OligoPlateManager.java
 * This class manages the oligo calculation, construct generation
 * and oligo plates generation processes
 * Created on May 31, 2001, 3:41 PM
 * @author  Wendy Mar
 * @version
 */

package edu.harvard.med.hip.flex.process;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import java.sql.*;
import java.util.*;
import java.io.*;


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
     * retrieve queue records and related sequence records
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

        private LinkedList getQueueSequence(int cdsSizeGroupLowerLimit, 
         int cdsSizeGroupUpperLimit) throws FlexDatabaseException {
        int protocolId = 3;  // to be modified
        //int cdsSizeGroup = 2000;
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocolId, cdsSizeGroupLowerLimit,
        cdsSizeGroupUpperLimit);
        
        //delete sequence queue record from the queue table
        //seqQueue.removeQueueItems(seqList, protocolId, conn);
        
        return seqList;
    }
    
    public void createOligoPlates() throws FlexDatabaseException, FlexCoreException, IOException{
        int smallCDSLimit = 2000;
        int mediumCDSLimit = 4000;
        int largeCDSLimit = 10000;
        int totalWells = 94;
        int platesetId = -1;
        int numSeqsInQueue = 0;
        int[] limits = new int[4];
        limits[0] = 0;
        limits[1] = smallCDSLimit;
        limits[2] = mediumCDSLimit;
        limits[3] = largeCDSLimit;
        int numPlatesToProcess = 0;
        int numSeqsToTruncate = 0;
        LinkedList seqList = null;
        ConstructGenerator cg = null;
        OligoPlater plater = null;
        int i = 0;
        int j = 0;
        int numSeqsToProcess = 0;
        for (i = 0; i < 3; ++i) {
            //numSeqsInQueue = totalSeqQueue(limits[i]);
            //numPlatesToProcess = numSeqsInQueue / totalWells;
            seqList = getQueueSequence(limits[i],limits[i+1]);
            numPlatesToProcess = seqList.size() / totalWells;
            numSeqsToProcess = totalWells*numPlatesToProcess;
            if (numSeqsToProcess > 0) {
                numSeqsToTruncate = seqList.size() - numSeqsToProcess;
                System.out.println("There are total of "+seqList.size()+" sequence in queue");
                for (j = 0; j < numSeqsToTruncate; ++j) {
                    seqList.removeLast();
                } // for
                System.out.println("There are "+ seqList.size()+" sequences going to be processed");
                cg = new ConstructGenerator(seqList,conn);
                
                cg.generateOligoAndConstructs();
                cg.insertProcessInputOutput();
                cg.insertConstructQueue();
                LinkedList oligoPatternList = cg.getOligoPatternList();
                platesetId = cg.getPlatesetId();
                System.out.println("Items in the oligoPatternList: "+ oligoPatternList.size());
                System.out.println("The new plateset ID is: " + platesetId);
                //all of the oligo plate header and sample info are inserted in DB
                //three text files for order oligos will be generated
                plater = new OligoPlater(oligoPatternList, cg.getConstructList(),platesetId,conn);
                plater.createOligoPlates();
                plater.insertProcessInputOutput();
                plater.insertReceiveOligoQueue();
                
                
            } // if
        } // for
        
        
        
        
        
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
            om.createOligoPlates();
            
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } catch (FlexCoreException ex){
            System.out.println(ex);
        } catch (IOException IOex){
            System.out.println(IOex);
        }finally {
            //  DatabaseTransaction.closeConnection(conn);
        }
        
        
    } //main
    
}
