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
import edu.harvard.med.hip.flex.workflow.*;
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
    public static final int LIMITS[] = {0, smallCDSLimit, mediumCDSLimit, largeCDSLimit};
    
    private Connection conn;
    private LinkedList fileList;
    private Project project = null;
    private Workflow workflow = null;
    
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
     * Constructor.
     *
     * @project The project that the oligo manager work with.
     * @workflow The workflow that the oligo manager work with.
     *
     * @return The OligoPlateManager object.
     *
     * @exception FlexDatabaseException.
     */
    public OligoPlateManager(Project project, Workflow workflow)
    throws FlexDatabaseException {
        this.project = project;
        this.workflow = workflow;
        
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
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        int protocolId = protocol.getId();
        
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
        
        return seqList;
    }
    
    private LinkedList getQueueSequence(int cdsSizeGroupLowerLimit,
    int cdsSizeGroupUpperLimit) throws FlexDatabaseException {
        
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocol, project, workflow, cdsSizeGroupLowerLimit,
        cdsSizeGroupUpperLimit);
        
        return seqList;
    }
    
    private void removeQueueSequence(LinkedList seqList) throws FlexDatabaseException{
        Protocol protocol = new Protocol(DESIGN_CONSTRUCTS);
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqQueue.removeQueueItems(seqList, protocol, project, workflow, conn);
    }
    
    public void createOligoPlates(LinkedList seqList) throws FlexDatabaseException, FlexCoreException, IOException{
        int platesetId = -1;
        int numSeqsInQueue = 0;
        int numPlatesToProcess = 0;
        int numSeqsToTruncate = 0;
        ConstructGenerator cg = null;
        OligoPlater plater = null;
        int i = 0;
        int j = 0;
        int numSeqsToProcess = 0;
        
        numPlatesToProcess = seqList.size() / totalWells;
        numSeqsToProcess = totalWells*numPlatesToProcess;
        if (numSeqsToProcess > 0) {
            numSeqsToTruncate = seqList.size() - numSeqsToProcess;
            
            for (j = 0; j < numSeqsToTruncate; ++j) {
                seqList.removeLast();
            } // for
            //System.out.println("There are "+ seqList.size()+" sequences going to be processed");
            
            try{
                cg = new ConstructGenerator(seqList,conn, project, workflow);
                cg.generateOligoAndConstructs();
                cg.insertProcessInputOutput();
                cg.insertConstructQueue();
                LinkedList oligoPatternList = cg.getOligoPatternList();
                
                //all of the oligo plate header and sample info are inserted in DB
                //three text files for order oligos will be generated
                plater = new OligoPlater(oligoPatternList, cg.getConstructList(), conn, project, workflow);
                
                plater.generateOligoOrder();
                plater.removeOrderOligoQueue();
                
                //remove sequences from queue
                removeQueueSequence(seqList);
                
                DatabaseTransaction.commit(conn);
            } catch(FlexDatabaseException sqlex){
                
                System.out.println(sqlex.getMessage());
                DatabaseTransaction.rollback(conn);
            } catch(IOException ioe){
                System.out.println("Error occurred while writing to oligo order files");
                System.out.println(ioe.getMessage());
                
                DatabaseTransaction.rollback(conn);
            }
            finally {
                DatabaseTransaction.closeConnection(conn);
            }
            
        } // if
        
        fileList = plater.generateOligoOrderFiles();
        
    } //createOligoAndConstruct
    
    public void sendOligoOrders() throws MessagingException{
        String to = "wmar@hms.harvard.edu";
        String from = "wmar@hms.harvard.edu";
        String cc = "flexgene_manager@hms.harvard.edu";
        String subject = "New Testing Oligo Order";
        String msgText = "The attached files are our oligo order.\n"+
        "Thank you!";
        Mailer.sendMessage(to, from, cc, subject, msgText, fileList);
    }
    
    
    public synchronized void orderOligo(){
//        java.lang.Thread thread = new OligoThread();
//        thread.start();
            System.out.println("Thread started");
            int totalQueue = 0;
            try {
                for (int i = 0; i < LIMITS.length-1; ++i) {
                    LinkedList seqList = getQueueSequence(LIMITS[i],LIMITS[i+1]);
                    totalQueue = seqList.size();
                    System.out.println("There are total of " + totalQueue + " sequences belong to the same size group in the queue");
                    if (totalQueue >= 94){
                        createOligoPlates(seqList);
                        //avoid sending out empty email without files attached
                        if (fileList.size() >= 1){
                            sendOligoOrders();
                            System.out.println("Oligo order files have been mailed!");
                            DatabaseTransaction.commit(conn);
                        } //inner if
                        else{
                            System.out.println("File error, no order is mailed!");
                            DatabaseTransaction.rollback(conn);
                        } //inner else
                    } else {
                        System.out.println("There are not enough sequences in queue to create an oligo plate!");
                    }
                }
            } catch (FlexDatabaseException e) {
                e.printStackTrace();
            } catch (FlexCoreException ex){
                ex.printStackTrace();
            } catch (IOException IOex){
                DatabaseTransaction.rollback(conn);
                IOex.printStackTrace();
            } catch (MessagingException msgex){
                DatabaseTransaction.rollback(conn);
                msgex.printStackTrace();
            }finally {
                //   DatabaseTransaction.closeConnection(conn);
            }
            
            System.out.println("Thread finished");        
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
                for (int i = 0; i < LIMITS.length-1; ++i) {
                    LinkedList seqList = getQueueSequence(LIMITS[i],LIMITS[i+1]);
                    totalQueue = seqList.size();
                    System.out.println("There are total of " + totalQueue + " sequences belong to the same size group in the queue");
                    if (totalQueue >= 94){
                        createOligoPlates(seqList);
                        //avoid sending out empty email without files attached
                        if (fileList.size() >= 1){
                            sendOligoOrders();
                            System.out.println("Oligo order files have been mailed!");
                            DatabaseTransaction.commit(conn);
                        } //inner if
                        else{
                            System.out.println("File error, no order is mailed!");
                            DatabaseTransaction.rollback(conn);
                        } //inner else
                    } else {
                        System.out.println("There are not enough sequences in queue to create an oligo plate!");
                    }
                }
            } catch (FlexDatabaseException e) {
                e.printStackTrace();
            } catch (FlexCoreException ex){
                ex.printStackTrace();
            } catch (IOException IOex){
                DatabaseTransaction.rollback(conn);
                IOex.printStackTrace();
            } catch (MessagingException msgex){
                DatabaseTransaction.rollback(conn);
                msgex.printStackTrace();
            }finally {
                //   DatabaseTransaction.closeConnection(conn);
            }
            
            System.out.println("Thread finished");
        }
    }
}
