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
import edu.harvard.med.hip.flex.util.*;
import javax.mail.*;
import java.sql.*;
import java.util.*;
import java.io.*;


public class OligoPlateManager {
    public static final String DESIGN_CONSTRUCTS= "design constructs";
    public static final int smallCDSLimit = 2000;
    public static final int mediumCDSLimit = 4000;
    public static final int largeCDSLimit = 10000;
    public static final int totalWells = 94;
    private Connection conn;
    private LinkedList fileList;
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
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocol, cdsSizeGroup);
        
        //delete sequence queue record from the queue table
        //seqQueue.removeQueueItems(seqList, protocol, conn);
        
        return seqList;
    }
    
    private LinkedList getQueueSequence(int cdsSizeGroupLowerLimit,
    int cdsSizeGroupUpperLimit) throws FlexDatabaseException {
        
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocol, cdsSizeGroupLowerLimit,
        cdsSizeGroupUpperLimit);        
        
        return seqList;
    }
    
    private void removeQueueSequence(LinkedList seqList) throws FlexDatabaseException{
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqQueue.removeQueueItems(seqList, protocol, conn);
    }
    
    public void createOligoPlates() throws FlexDatabaseException, FlexCoreException, IOException{
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
                
                try{
                    cg = new ConstructGenerator(seqList,conn);
                    cg.generateOligoAndConstructs();
                    cg.insertProcessInputOutput();
                    cg.insertConstructQueue();
                    LinkedList oligoPatternList = cg.getOligoPatternList();
                    System.out.println("Items in the oligoPatternList: "+ oligoPatternList.size());
                    
                    //all of the oligo plate header and sample info are inserted in DB
                    //three text files for order oligos will be generated
                    plater = new OligoPlater(oligoPatternList, cg.getConstructList(), conn);
                    plater.createOligoPlates();
                    plater.insertProcessInputOutput();
                    plater.insertReceiveOligoQueue();
                    System.out.println("receive oligo plates queue inserted.");
                    plater.removeOrderOligoQueue();
                    System.out.println("order oligo queue removed.");
                    
                    //remove sequences from queue
                    removeQueueSequence(seqList);
                    
                    DatabaseTransaction.commit(conn);
                } catch(FlexDatabaseException sqlex){
                    //System.out.println(sqlex);
                    System.out.println(sqlex.getMessage());
                    DatabaseTransaction.rollback(conn);
                    
                }finally {
                    DatabaseTransaction.closeConnection(conn);
                }
                
            } // if
        } // for
        
        fileList = plater.generateOligoOrderFiles();
        
    } //createOligoAndConstruct
    
    public void sendOligoOrders() throws MessagingException{
        String to = "wmar@hms.harvard.edu";
        String from = "wmar@hms.harvard.edu";
        String cc = "allison_halleck@hms.harvard.edu";
        String subject = "Testing Oligo Order";
        String msgText = "The attached files are our oligo order.\n"+
        "Thank you!";
        try{
            Mailer.sendMessage(to, from, cc, subject, msgText, fileList);
        } catch(MessagingException ex){
            ex.printStackTrace();
        }
        
    }
    
    
    public void orderOligo(){
        java.lang.Thread thread = new OligoThread();
        thread.start();
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) throws Exception {
        OligoPlateManager om = new OligoPlateManager();
        System.out.println("About to start thread");
        om.orderOligo();
        System.out.println("finished calling thread");
        
    } //main
    
    public class OligoThread extends java.lang.Thread {
        public void run() {
            System.out.println("Thread started");
            int totalQueue = 0;
            try {
                
                totalQueue = totalSeqQueue(smallCDSLimit);
                System.out.println("There are total of " + totalQueue + " small sequences in the queue");
                if (totalQueue >= 94){
                    System.out.println("Calculating oligos...");
                    createOligoPlates();
                    sendOligoOrders();
                    System.out.println("Oligo order files have been mailed!");
                } else {
                    System.out.println("There are not enough sequences in queue to create an oligo plate!");
                }
            } catch (FlexDatabaseException e) {
                e.printStackTrace();
            } catch (FlexCoreException ex){
                ex.printStackTrace();
            } catch (IOException IOex){
                IOex.printStackTrace();
            } catch (MessagingException msgex){
                msgex.printStackTrace();
            }finally {
                //   DatabaseTransaction.closeConnection(conn);
            }
            
            System.out.println("Thread finished");
        }
    }
}
