/*
 * MgcOligoPlateManager.java
 * This class manages the oligo calculation, construct generation
 * and oligo plates generation processes
 *it does not change order of sequences, and does not impose limit of samples on plate
 
 * @author  Helen Taycher
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


public class MgcOligoPlateManager
{
    private String m_DESIGN_CONSTRUCTS = null;
    private int m_totalWells = 94;
    private boolean m_isOnlyFullPlates = true;
    
    
    private Connection m_conn = null;
    private LinkedList m_fileList = null;
    private Project m_project = null;
    private Workflow m_workflow = null;
    private Protocol m_protocol = null;
    private int      m_orderSequencesOutside = -1;
    
    /**
     * Constructor
     * Creates new OligoPlateManager
     */
    public MgcOligoPlateManager(Connection conn) throws FlexDatabaseException
    {
        if ( m_DESIGN_CONSTRUCTS == null)
            m_DESIGN_CONSTRUCTS = Protocol.MGC_DESIGN_CONSTRUCTS;
        m_protocol = new Protocol(m_DESIGN_CONSTRUCTS);
        m_conn = conn;
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
    public MgcOligoPlateManager(Connection conn, Project project, Workflow workflow)
    throws FlexDatabaseException
    {
        this(conn);
        m_project = project;
        m_workflow = workflow;
        
    }
    
    public MgcOligoPlateManager(Connection conn, Project project, Workflow workflow,
    int totalWells, boolean isFull, String protocol)
    throws FlexDatabaseException
    {
        
        m_conn = conn;
        m_project = project;
        m_workflow = workflow;
        m_DESIGN_CONSTRUCTS = protocol;
        m_totalWells = totalWells;
        m_isOnlyFullPlates = isFull;//if only full plates go into experiment
        m_protocol = new Protocol(m_DESIGN_CONSTRUCTS);
    }
    
   public MgcOligoPlateManager(Connection conn, Project project, Workflow workflow,
                 boolean isFull, String protocol) throws  FlexDatabaseException
   {
          this(conn, project,  workflow, 94,  isFull,  protocol);
   }
    
    public MgcOligoPlateManager(Project project, Workflow workflow, String protocol)
    throws FlexDatabaseException
    {
        
        try
        {
            this.m_conn = DatabaseTransaction.getInstance().requestConnection();
            m_project = project;
            m_workflow = workflow;
            m_DESIGN_CONSTRUCTS = protocol;
            m_protocol = new Protocol(m_DESIGN_CONSTRUCTS);
        } catch(FlexDatabaseException sqlex)
        {            throw new FlexDatabaseException(sqlex);       }
   }
    
    
    
    public void setOrderRequest(int order)
    {
        m_orderSequencesOutside = order;
    }
    
    public int getOrderRequest()
    {
        return m_orderSequencesOutside;
    }
    
    
    /**
     * Check the total number of sequences in the Queue table belong to the
     * same project and workflow waiting for Design_Construct protocol. 
     * @return The total number of sequences.
     */
    public int totalSeqQueue() throws FlexDatabaseException {
        int count = 0;
        ResultSet rs = null;
        
       
        int protocolId = m_protocol.getId();
        
        String sql = "SELECT COUNT(q.sequenceid) " +
        "FROM queue q, flexsequence s \n" +
        "WHERE q.sequenceid = s.sequenceid \n" +
        "AND q.projectId =" + m_project.getId() + "\n" +
        "AND q.workflowId =" + m_workflow.getId() + "\n" +
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
    } //
    
    /**
     * retrieve queue records and related sequence records of the same project
     * where protocol = m_design_construct'
     * @return LinkedList A list of sequence objects
     */
    private LinkedList getQueueSequence() throws FlexDatabaseException
    {
        SequenceOligoQueue seqQueue = new SequenceOligoQueue();
        return seqQueue.getQueueItems( m_protocol, m_project, m_workflow);
        
    }
    
   
    
    private void removeQueueSequence(LinkedList seqList) throws FlexDatabaseException
    {
        SequenceOligoQueue seqQueue = new SequenceOligoQueue(m_project, m_workflow);
        seqQueue.removeQueueItems(seqList, m_protocol, m_conn);
    }
    
    private void createOligoPlates(LinkedList seqList) throws FlexDatabaseException, FlexCoreException, IOException, Exception
    {
        
     
        ArrayList plates = new ArrayList();
        /*
        
        if (m_isOnlyFullPlates)//only full plates
        {
            int numSeqToProcess = m_totalWells * ( (int)Math.ceil(seqList.size() / m_totalWells));
            if (numSeqToProcess < 0) return;
            int numberOfSequencesToTruncate = seqList.size() - numSeqToProcess;
            for (int count = 0; count < numberOfSequencesToTruncate; count++)
            {
                seqList.removeLast();
            }
        }
        
        if (m_DESIGN_CONSTRUCTS.equals(Protocol.MGC_DESIGN_CONSTRUCTS))
        {
            //arrange sequences by marker/plate/saw-tooth
            Rearrayer ra = new Rearrayer(new ArrayList(seqList));
            ra.findOriginalMgcContainers();
            ArrayList orderedSeq = ra.getSequencesOrderedByMarkerContainerSTP(m_totalWells);
            //put them in destination plate groups
            ArrayList destination_plate  =  new ArrayList();
            for (int seq_count = 0; seq_count <= orderedSeq.size(); ++seq_count)
            {
                 destination_plate.add(orderedSeq.get(seq_count));
                 if ((seq_count % m_totalWells) == 0 || seq_count == orderedSeq.size() )//plate filled
                 {
                     plates.add(destination_plate);
                     destination_plate  =  new ArrayList();
                 }
            }
      
            //check if all mgc culture plates for this destination plate 
            //are available
            //1. get all available culture blocks and build hashtable of culture - mgc
            ArrayList mc_available = new ArrayList();
            ArrayList cultureBlocks = new ArrayList();
            //!!!!!!!!!!!! define here
            for (int cont_count = 0; cont_count < cultureBlocks.size(); cont_count++)
            {
                MgcContainer mc = MgcContainer.findMGCContainerFromCulture((Container) cultureBlocks.get(cont_count));
                mc_available.add(mc);
            }
            
            
            /* destination plate
             *all culture plate are available
            //1. design primers & constracts
            //2. generate rearray file
            //3.group oligo plates and rearrayed culture block into one plateset
             */
        
        /*
            boolean plate_ok = false;
            for (int plate_count = 0; plate_count < plates.size(); plate_count++)
            {
                plate_ok = false;
                for (int seq_count = 0; seq_count < m_totalWells; seq_count++)
                {
                }
                if ( plate_ok )
                {
                    createOligoPlate((LinkedList)plates.get(plate_count));
                    createRearrayFile();
                }
            }
        }
         **/
    }
    
    
   private  void createOligoPlate(LinkedList seqList) throws FlexDatabaseException, FlexCoreException, IOException, Exception
    {
           ConstructGenerator cg = null;
        OligoPlater plater = null;
        try
        {
            cg = new ConstructGenerator(seqList, m_conn, m_project, m_workflow, m_protocol);
            cg.generateOligoAndConstructs();
            cg.insertProcessInputOutput();
            cg.insertConstructQueue();
            LinkedList oligoPatternList = cg.getOligoPatternList();

            //all of the oligo plate header and sample info are inserted in DB
            //three text files for order oligos will be generated
            plater = new OligoPlater(oligoPatternList, cg.getConstructList(), m_conn, m_project, m_workflow);
            ///remove this 
            plater.generateOligoOrder();
            plater.removeOrderOligoQueue();

            //remove sequences from queue
            removeQueueSequence(seqList);
            DatabaseTransaction.commit(m_conn);
        } catch(FlexDatabaseException sqlex)
            {
                System.out.println(sqlex.getMessage());
                DatabaseTransaction.rollback(m_conn);
            } catch(IOException ioe)
            {
                System.out.println("Error occurred while writing to oligo order files");
                System.out.println(ioe.getMessage());
                DatabaseTransaction.rollback(m_conn);
            }
            finally
            {
                DatabaseTransaction.closeConnection(m_conn);
            }
  
        m_fileList = plater.generateOligoOrderFiles();
        
    } //createOligoAndConstruct
    
    public void sendOligoOrders() throws MessagingException
    {
        String to = "htaycher@hms.harvard.edu";
        String from = "htaycher@hms.harvard.edu";
        String cc = "flexgene_manager@hms.harvard.edu";
        String subject = "New Oligo Order";
        String msgText = "The attached files are our oligo order.\n"+
        "Thank you!";
        Mailer.sendMessage(to, from, cc, subject, msgText, m_fileList);
    }
    
    
    public synchronized void orderOligo() throws Exception
    {
        int totalQueue_sequences = 0;
     
        LinkedList seqList = getQueueSequence();
        totalQueue_sequences = seqList.size();
        System.out.println("There are total of " + totalQueue_sequences + " sequences belong to the same size group in the queue");
        createOligoPlates(seqList);
        //avoid sending out empty email without files attached
        if (m_fileList.size() >= 1)
        {
            sendOligoOrders();
            System.out.println("Oligo order files have been mailed!");
            DatabaseTransaction.commit(m_conn);
        } //inner if
        else
        {
            System.out.println("File error, no order is mailed!");
            DatabaseTransaction.rollback(m_conn);
        } 
   
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) throws Exception
    {
        OligoPlateManager om = new OligoPlateManager();
        System.out.println("About to start thread");
        om.orderOligo();
        System.out.println("finished calling thread");
        
    } //main
    
}
  