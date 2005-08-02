/*
 * OligoPlateManager.java
 * This class manages the oligo calculation, construct generation
 * and oligo plates generation processes
 * Created on May 31, 2001, 3:41 PM
 * @author  Wendy Mar
 * @version
 *
 * Removed some duplicate logic. Removed the full plate limitation.
 * Removed the group-by-size limitation. - dzuo.
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
    public static final int smallCDSLimit = 2000;
    public static final int mediumCDSLimit = 4000;
    public static final int largeCDSLimit = 100000;
    public static final int LIMITS[] = {0, smallCDSLimit, mediumCDSLimit, largeCDSLimit};
    public static String PROTOCOL = Protocol.DESIGN_CONSTRUCTS;
    
    protected Connection conn;
    protected LinkedList fileList;
    protected Project project = null;
    protected Workflow workflow = null;
    protected boolean isOnlyFullPlate = true;
    protected int totalWells = 94;
    protected Protocol protocol = null;
    protected boolean isGroupBySize = true;
    protected boolean     m_isReorderSequences = true;
    protected String      m_plateType = "96 WELL OLIGO PLATE";
    
    protected boolean isSmall = false;
    protected boolean isMedium = false;
    protected boolean isLarge = false;
    
    protected boolean isSetOpenClose = false;
    protected boolean isOpenOnly = false;
    protected boolean isCloseOnly = false;
    
    protected OligoPlater plater = null;
    
    public OligoPlater getOligoPlater() {return plater;}
    
    /**
     * Constructor
     * Creates new OligoPlateManager
     * @param conn The database connection object.
     */
    public OligoPlateManager(Connection conn) throws FlexDatabaseException {
        this.conn = conn;
        setProtocol();
    }
    
    /**
     * Constructor.
     *
     * @param conn The database connection.
     * @project The project that the oligo manager work with.
     * @workflow The workflow that the oligo manager work with.
     *
     * @return The OligoPlateManager object.
     *
     * @exception FlexDatabaseException.
     */
    public OligoPlateManager(Connection conn, Project project, Workflow workflow)
    throws FlexDatabaseException {
        this.conn = conn;
        this.project = project;
        this.workflow = workflow;
        setProtocol();
        setOpenClose(project, workflow);
    }
    
    /**
     * Constructor.
     *
     * @param project The project that the oligo manager works with.
     * @param workflow The workflow that the oligo manager works with.
     * @param totalWells The number of total wells on the oligo plate.
     * @param isFull Whether the full plate require to generate the oligos.
     * @param isGroupBySize Whether the oligoes should be grouped by size limit.
     * @param protocol The protocol that the oligo manager works with.
     *
     * @return The OligoPlateManager object.
     *
     * @exception FlexDatabaseException.
     */
    public OligoPlateManager(Connection conn, Project project, Workflow workflow,
    int totalWells, boolean isFull, boolean isGroupBySize, Protocol protocol)
    throws FlexDatabaseException {
        this( conn, project, workflow);
        this.totalWells = totalWells;
        // m_plateType =
        this.isOnlyFullPlate = isFull;
        this.isGroupBySize = isGroupBySize;
        this.protocol = protocol;
        
        if(protocol == null)            setProtocol();
        setOpenClose(project, workflow);
    }
    
    /**
     * Constructor.
     *
     * @param project The project that the oligo manager works with.
     * @param workflow The workflow that the oligo manager works with.
     * @param totalWells The number of total wells on the oligo plate.
     * @param isFull Whether the full plate require to generate the oligos.
     * @param isGroupBySize Whether the oligoes should be grouped by size limit.
     * @param isSmall Whether the oligoes should be grouped by small genes.
     * @param isMedium Whether the oligoes should be grouped by medium genes.
     * @param isLarge Whether the oligoes should be grouped by large genes.
     * @param protocol The protocol that the oligo manager works with.
     *
     * @return The OligoPlateManager object.
     *
     * @exception FlexDatabaseException.
     */
    public OligoPlateManager(Connection conn, Project project, Workflow workflow,
    int totalWells, boolean isFull, boolean isGroupBySize, boolean isSmall,
    boolean isMedium, boolean isLarge, Protocol protocol) throws FlexDatabaseException {
        this( conn, project, workflow);
        this.totalWells = totalWells;
        this.isOnlyFullPlate = isFull;
        this.isGroupBySize = isGroupBySize;
        this.isSmall = isSmall;
        this.isMedium = isMedium;
        this.isLarge = isLarge;
        this.protocol = protocol;
        
        if(protocol == null)
            setProtocol();
        
        setOpenClose(project, workflow);
    }
    
    public void setIsSetOpenClose(boolean b) {this.isSetOpenClose = b;}
    public void setIsOpenOnly(boolean b) {this.isOpenOnly = b;}
    public void setIsCloseOnly(boolean b) {this.isCloseOnly = b;}
    
    /**
     * Set the protocol.
     */
    protected void setProtocol() throws FlexDatabaseException {
        this.protocol = new Protocol(PROTOCOL);
    }
    
    /**
     *Set isResort parameter (whether sequences will be reordered by OligoPlater
     */
    protected void setIsReorder(boolean isReorder) {m_isReorderSequences = isReorder;}
    
    protected void setOpenClose(Project project, Workflow workflow) {
        if(project.getId() == Project.PSEUDOMONAS || project.getId() == Project.KINASE || project.getId() == Project.VC || project.getId() == Project.KINASE_MUT || project.getId() == Project.Bacillus_anthracis) {
            setIsOpenOnly(true);
            setIsSetOpenClose(true);
        } else if(project.getId() == Project.YEAST 
            || project.getId() == Project.YP 
            || project.getId() == Project.FT
            || workflow.getId() == Workflow.MGC_GATEWAY_CLOSED
            || project.getId() == Project.Yersinia_pseudotuberculosis) {
            setIsCloseOnly(true);
            setIsSetOpenClose(true);
        }
    }
    
    /**
     * Check the total number of sequences in the Queue table belong to the
     * same project and workflow waiting for Design_Construct protocol.
     * @return The total number of sequences.
     */
    protected int totalSeqQueue() throws FlexDatabaseException {
        int count = 0;
        ResultSet rs = null;
        int protocolId = protocol.getId();
        
        String sql = "SELECT COUNT(q.sequenceid) " +
        "FROM queue q, flexsequence s \n" +
        "WHERE q.sequenceid = s.sequenceid \n" +
        "AND q.projectId =" + project.getId() + "\n" +
        "AND q.workflowId =" + workflow.getId() + "\n" +
        "AND q.protocolId =" + protocolId ;
        
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
    }
    
    /**
     * Check the total number of sequences in the Queue table belong to the
     * same project and workflow waiting for Design_Construct protocol.
     * @return The total number of sequences.
     */
    protected int totalSeqQueue(int cdsSizeGroup) throws FlexDatabaseException {
        int count = 0;
        ResultSet rs = null;
        
        // get the protocol id for "DESIGN_CONSTRUCT" protocol
        int protocolId = protocol.getId();
        
        String sql = "SELECT COUNT(q.sequenceid) " +
        "FROM queue q, flexsequence s \n" +
        "WHERE q.sequenceid = s.sequenceid \n" +
        "AND q.projectId =" + project.getId() + "\n" +
        "AND q.workflowId =" + workflow.getId() + "\n" +
        "AND q.protocolId =" + protocolId + "\n" +
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
     * retrieve queue records and related sequence records of the same project
     * where protocol is design construct.
     * @return LinkedList A list of sequence objects
     */
    protected LinkedList getQueueSequence() throws FlexDatabaseException {
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        return seqQueue.getQueueItems(protocol, project, workflow);
    }
    
    /**
     * retrieve queue records and related sequence records of the same project
     * where protocol = 'design construct'
     * @return LinkedList A list of sequence objects
     */
    protected LinkedList getQueueSequence(int cdsSizeGroup) throws FlexDatabaseException {
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        seqList = seqQueue.getQueueItems(protocol, cdsSizeGroup);
        
        return seqList;
    }
    
    /**
     * retrieve queue records and related sequence records of the same project
     * and workflow where protocol = 'design construct'
     * @return LinkedList A list of sequence objects
     */
    
    protected LinkedList getQueueSequence(int cdsSizeGroupLowerLimit,
    int cdsSizeGroupUpperLimit) throws FlexDatabaseException {
        
        LinkedList seqList = new LinkedList();
        SequenceOligoQueue seqQueue = new SequenceOligoQueue(project, workflow);
        seqList = seqQueue.getQueueItems(protocol, cdsSizeGroupLowerLimit,
        cdsSizeGroupUpperLimit);
        
        return seqList;
    }
    
    protected void removeQueueSequence(LinkedList seqList) throws FlexDatabaseException{
        SequenceOligoQueue seqQueue = new SequenceOligoQueue(project, workflow);
        seqQueue.removeQueueItems(seqList, protocol, conn);
    }
    
    protected void createOligoPlates(LinkedList seqList) throws FlexDatabaseException, FlexCoreException, IOException, Exception{
        if(isOnlyFullPlate) {
            int numPlatesToProcess = seqList.size() / totalWells;
            int numSeqsToProcess = totalWells*numPlatesToProcess;
            
            if (numSeqsToProcess <= 0) {
                return;
            }
            
            int numSeqsToTruncate = seqList.size() - numSeqsToProcess;
            
            for (int j = 0; j < numSeqsToTruncate; ++j) {
                seqList.removeLast();
            } // for
            //System.out.println("There are "+ seqList.size()+" sequences going to be processed");
        }
        
        createOligoPlate(seqList, -1);
    } //createOligoAndConstruct
    
    protected  void createOligoPlate(LinkedList seqList, int mgcContainerId)
    throws FlexDatabaseException, FlexCoreException, IOException, Exception {
        ConstructGenerator cg = null;
        plater = null;
       
        try{
            cg = new ConstructGenerator(seqList,conn, project, workflow, protocol);
            
            if(isSetOpenClose) {
                cg.setIsOpenOnly(isOpenOnly);
                cg.setIsCloseOnly(isCloseOnly);
            }
            
            cg.generateOligoAndConstructs();
            cg.insertProcessInputOutput();
            cg.insertConstructQueue();
            LinkedList oligoPatternList = cg.getOligoPatternList();
            
            //all of the oligo plate header and sample info are inserted in DB
            //three text files for order oligos will be generated
            plater = new OligoPlater(oligoPatternList, cg.getConstructList(),
            conn, project, workflow);
            plater.setIsOnlyClose(isCloseOnly);
            plater.setIsOnlyOpen(isOpenOnly);
            plater.setTotalWells( totalWells );
            plater.setPlateType( m_plateType);
            plater.setReorderRequest(m_isReorderSequences);
            plater.setMgcContainerId(mgcContainerId);
            
            
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
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        
        if (fileList == null || fileList.size() == 0)
            fileList = plater.generateOligoOrderFiles() ;
        else
            fileList.addAll( plater.generateOligoOrderFiles() );
    }
    
    public void sendOligoOrders() throws MessagingException{
        String to = "dongmei_zuo@hms.harvard.edu";
        String from = "dongmei_zuo@hms.harvard.edu";
        //String cc = "dongmei_zuo@hms.harvard.edu";
        String cc = "flexgene_manager@hms.harvard.edu";
        String subject = "Oligo order for project - "+project.getName();
        String msgText = "The attached files are our oligo order.\n"+
        "Thank you!";
        Mailer.sendMessage(to, from, cc, subject, msgText, fileList);
    }
    
    
    public synchronized void orderOligo() {
        //        java.lang.Thread thread = new OligoThread();
        //        thread.start();
        System.out.println("Thread started");
        LinkedList seqList = null;
        
        try {
            if(isGroupBySize) {
                seqList = new LinkedList();
                
                if(isSmall) {
                    seqList.addAll(getQueueSequence(LIMITS[0], LIMITS[1]));
                } else {
                    LinkedList l = getQueueSequence(LIMITS[0], LIMITS[1]);
                    performOligoOrder(l);
                }
                
                if(isMedium) {
                    seqList.addAll(getQueueSequence(LIMITS[1], LIMITS[2]));
                } else {
                    LinkedList l = getQueueSequence(LIMITS[1], LIMITS[2]);
                    performOligoOrder(l);
                }
                
                if(isLarge) {
                    seqList.addAll(getQueueSequence(LIMITS[2], LIMITS[3]));
                } else {
                    LinkedList l = getQueueSequence(LIMITS[2], LIMITS[3]);
                    performOligoOrder(l);
                }
                
                if(seqList.size() != 0)
                    performOligoOrder(seqList);
            } else {
                seqList = getQueueSequence();
                performOligoOrder(seqList);
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
        } catch (Exception ex){
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
        }
        
        System.out.println("Thread finished");
    }
    
    private void performOligoOrder(LinkedList seqList) throws FlexDatabaseException, IOException, MessagingException, Exception {
        int totalQueue = seqList.size();
        
        if(fileList != null)
            fileList.clear();
        
        System.out.println("There are total of " + totalQueue + " sequences belong to the same size group in the queue");
        createOligoPlates(seqList);
        
        //avoid sending out empty email without files attached
        if (fileList != null && fileList.size() >= 1){
            sendOligoOrders();
            System.out.println("Oligo order files have been mailed!");
            DatabaseTransaction.commit(conn);
        } //inner if
        else{
            System.out.println("File error, no order is mailed!");
            DatabaseTransaction.rollback(conn);
        }
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) throws Exception {
        Connection c = null;
        Project p = null;
        Workflow w = null;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            p = new Project(5);
            w = new Workflow(6);
            
            //OligoPlateManager om = new OligoPlateManager(c, p, w);
            OligoPlateManager om = new OligoPlateManager(c, p, w, 10, false, true, null);
            System.out.println("About to start thread");
            om.orderOligo();
            System.out.println("finished calling thread");
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            if(c != null)
                DatabaseTransaction.closeConnection(c);
        }
        
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
            } catch (Exception ex) {
                DatabaseTransaction.rollback(conn);
                ex.printStackTrace();
            }finally {
                //   DatabaseTransaction.closeConnection(conn);
            }
            
            System.out.println("Thread finished");
        }
    }
}
