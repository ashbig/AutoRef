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
        seqQueue.removeQueueItems(seqList, protocol, conn);
        
        return seqList;
    }
    
    private LinkedList getQueueSequence(int cdsSizeGroupLowerLimit,
    int cdsSizeGroupUpperLimit) throws FlexDatabaseException {
        
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocol, cdsSizeGroupLowerLimit,
        cdsSizeGroupUpperLimit);
        
        //delete sequence queue record 
        //seqQueue.removeQueueItems(seqList, protocol, conn);
        //System.out.println("removing sequences in queue with design construct protocl...");
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
                    System.out.println("inserting the receive oligo plates protocol into queue.");
                    
                    DatabaseTransaction.commit(conn);
                } catch(FlexDatabaseException sqlex){
                    System.out.println(sqlex);
                    DatabaseTransaction.rollback(conn);
                
                }finally {
                    DatabaseTransaction.closeConnection(conn);
                }
                                
            } // if
        } // for
        
         fileList = plater.generateOligoOrderFiles();        
        
    } //createOligoAndConstruct
    
    public void sendOligoOrders() throws MessagingException{
        String to = "wenhongm@hotmail.com";
        String from = "wmar@hms.harvard.edu";
        String cc = "jmunoz@3rdmill.com";
        String subject = "Testing Oligo Order";
        String msgText = "The attached files are our oligo order.\n"+
                        "Thank you!";
        try{
        Mailer.sendMessage(to, from, cc, subject, msgText, fileList);
        } catch(MessagingException ex){
            ex.printStackTrace();
        }
        
    }
    
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
            om.sendOligoOrders();
            System.out.println("Oligo order files have been mailed!");
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
        
        
    } //main
    
}
