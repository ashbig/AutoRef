/*
 * MgcOligoPlateManager.java
 * This class manages the oligo calculation, construct generation
 * and oligo plates generation processes
 *it does not change order of sequences, and does not impose limit of samples on plate
 
 * @author  Helen Taycher
 * @version
 *
 * Modified the class to extends from OligoPlateManager to eliminate
 * some duplicate logic. - dzuo
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



public class MgcOligoPlateManager extends OligoPlateManager
{
    protected static final String REARRAYEDPLATETYPE = "96 WELL PLATE";
    protected static final String filePath = "/tmp/";
    protected static final String DELIM = "\t";
    
    private   String                        m_UserName = null;
    private   LinkedList                    m_notDuplicatedSequences = null;
    //used for allowing user set container location & print label
    private   ArrayList                     m_rearrayed_containers = null;
    
    /**
     * Constructor
     * Creates new OligoPlateManager
     */
    public MgcOligoPlateManager(Connection conn, String user) throws FlexDatabaseException
    {
        super(conn);
        this.isGroupBySize = false;
        this.m_isReorderSequences = false;
        m_UserName = user;
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
    public MgcOligoPlateManager(Connection conn, Project project, Workflow workflow, String user)
    throws FlexDatabaseException
    {
        super(conn, project, workflow);
        this.isGroupBySize = false;
        this.m_isReorderSequences = false;
        m_UserName = user;
    }
    
    public MgcOligoPlateManager(Connection conn, Project project, Workflow workflow,
    int totalWells, boolean isFull, Protocol protocol, String user)  throws FlexDatabaseException
    {
        //m_plateType
        super(conn,project,workflow,totalWells,isFull,false,protocol);
        this.m_isReorderSequences = false;
        m_UserName = user;
    }
    
    public MgcOligoPlateManager(Connection conn, Project project, Workflow workflow,
    boolean isFull, Protocol protocol, String user)  throws FlexDatabaseException
    {
        this(conn, project,  workflow, 94,  isFull,  protocol, user);
        m_isReorderSequences = false;
        
    }
    
    public MgcOligoPlateManager(LinkedList seq, Connection conn, Project project, Workflow workflow,
    boolean isFull, Protocol protocol, String user)  throws FlexDatabaseException
    {
        this(conn, project,  workflow, 94,  isFull,  protocol, user);
        m_isReorderSequences = false;
        m_notDuplicatedSequences = seq ;
        
    }
    
    protected void setProtocol() throws FlexDatabaseException
    {
        this.protocol = new Protocol(Protocol.MGC_DESIGN_CONSTRUCTS);
    }
    
    
    public    ArrayList     getRearrayContainers()
    { 
        if (m_rearrayed_containers == null || m_rearrayed_containers.size() == 0)
            return new ArrayList();
        else
            return m_rearrayed_containers;}
    
    
    /* function overwrites createOligoPlates method for OligoPlater
     * 1. if only full plates are requered removes trailing sequences;
     * 2. sort sequences by marker/ number of sequences on container
     * 3. checks if all culture blocks plates for destination plate are exist
     * 4. call create OligoPlate per each plate
     * 5. write robot file per destination plate
     */
    protected void createOligoPlates(LinkedList seqList) throws FlexDatabaseException, FlexCoreException, IOException, Exception
    {
        
        int number_of_destination_plates = 0;
        
        
        //arrange sequences by marker / number of sequences on mgc plate
        // and put them in destination plate groups
        if (seqList == null || seqList.size() == 0) return;
        //working with duplicates
        if (m_notDuplicatedSequences != null) seqList = m_notDuplicatedSequences;
        Rearrayer ra = new Rearrayer(new ArrayList(seqList), totalWells);
        
        ArrayList plates = ra.getPlates( );
        ArrayList messages = ra.getMessages();
        Mailer.notifyUser(m_UserName,"rearrayMGC.log","MGC: Primer design and rearray",
        "MGC: Primer design and rearray", new Vector( messages ) );
        m_rearrayed_containers = new ArrayList();
        
        //finnaly create oligos
        for (int plate_count = 0; plate_count < plates.size(); plate_count++)
        {
            //get label for destination plate
            ProjectWorkflow flow = new ProjectWorkflow(project.getId(),workflow.getId());
            String projectCode = flow.getCode();
            String processcode = protocol.getProcesscode();
            int threadId = FlexIDGenerator.getID("threadid");
            String plate_label = Container.getLabel( projectCode, processcode, threadId, null)   ;
            //list of Sequence objects
            PlateDescription plate = (PlateDescription)plates.get(plate_count);
            
            //do not process not  full plates
            if (isOnlyFullPlate)//only full plates: delete trailing sequences
            {
                if (plate.getSequenceDescriptions().size() != totalWells) continue;
            }
            //do not set not availabel plate
            if ( !plate.getStatus() ) continue;
            //new rearrayed container
            
            Container rearrayed_cont = createContainer(plate_label, threadId,plate.getSequences(), plate.getSequenceDescriptions());
            DatabaseTransaction.commit(conn);
            m_rearrayed_containers.add(rearrayed_cont);
            
            createOligoPlate(plate.getSequences(), rearrayed_cont.getId());
           
            File robot_file = createRearrayFile(plate.getSequenceDescriptions(), plate_label);
            
            fileList.add( robot_file );
            //put on queue plates for creating DNA plate & Glycerol stock
            //putPlateOnQueue(rearrayed_cont);
            //DatabaseTransaction.commit(conn);
            //delete culture blocks that have been used for this request
           /** 
            for (int culture_count = 0; culture_count < plate.getContainers().size() ; culture_count++)
            {
                ContainerDescription  cont =( ContainerDescription)plate.getContainers().get(culture_count);
                if ( cont.getCultureId() != -1)    Container.updateLocation(Location.CODE_DESTROYED, cont.getCultureId(), conn) ;
            }*/
            DatabaseTransaction.commit(conn);
        }
    }
    
    
    
    //**********************************mgc specific ***************************
    
    
    //function creates rearray container
    private Container createContainer(String label, int threadId,LinkedList seq, ArrayList seqDesc)
    throws FlexDatabaseException, FlexCoreException, FlexWorkflowException, FlexProcessException
    {
        
        Location location = new Location(Location.FREEZER);
        Container cont = new Container(  REARRAYEDPLATETYPE,  location, label, threadId );
        LinkedList orgContainers = new LinkedList();
        int orgContainer_id = -1;
        //add positive control samples
        int contId = cont.getId();
        Vector sampleLineageSet = new Vector();
        
        Sample control_positive = new Sample(Sample.CONTROL_POSITIVE,1,contId);
        cont.addSample(control_positive);
        
        String type = Sample.getType(protocol.getProcessname());
        for (int count = 0; count < seq.size(); count ++)
        {
            SequenceDescription sd = (SequenceDescription) seqDesc.get(count);
            if ( orgContainer_id != sd.getContainerDescription().getId() )
            { //input containers - all original mgc/glycerol containers
                //create fake container
                orgContainer_id = sd.getContainerDescription().getId() ;
                Container ct= new Container(  orgContainer_id , null,
                location, sd.getContainerDescription().getLabel());
                orgContainers.add(ct);
            }
            
            Sample s = new Sample(type, count+2, contId);
            s.setStatus(Sample.GOOD);
            cont.addSample(s);
            sampleLineageSet.addElement(new SampleLineage(sd.getSampleId(), s.getId()));
        }
        Sample control_negative = new Sample(Sample.CONTROL_NEGATIVE,seq.size() + 2,contId);
        cont.addSample(control_negative);
        cont.insert(conn);
        
        //process sample linerage
        //rearrayed plate is output container
        createSampleLinerageForContainer( cont,  orgContainers,   sampleLineageSet );
        return cont;
    }
    
    // process sample linerage
    //rearrayed plate is output container
    private void createSampleLinerageForContainer(Container cont, LinkedList orgContainers,
    Vector sampleLineageSet )
    throws FlexDatabaseException, FlexCoreException, FlexWorkflowException, FlexProcessException
    {
        //process sample linerage
        //rearrayed plate is output container
        LinkedList newContainers = new LinkedList();
        newContainers.add(cont);
        String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
        
        WorkflowManager manager = new WorkflowManager(project, workflow, "ProcessPlateManager");
        manager.createProcessRecord(executionStatus, protocol, new Researcher(Researcher.SYSTEM),
        null, orgContainers, newContainers,
        null, sampleLineageSet, conn);
    }
    //function put on queueu plates for creating DNA plates and Glycerol stock
    private void putPlateOnQueue(Container cont) throws FlexDatabaseException
    {
        LinkedList queueItems = new LinkedList();
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
        Protocol createDNA = new Protocol(Protocol.CREATE_DNA_FROM_REARRAYED_CULTURE);
        Protocol createGlycerol = new Protocol(Protocol.CREATE_GLYCEROL_FROM_REARRAYED_CULTURE);
        //put on queue
        QueueItem queueItem = new QueueItem( cont, createDNA, project, workflow);
        QueueItem queueItem1 = new QueueItem( cont, createGlycerol, project, workflow);
        queueItems.add(queueItem);
        queueItems.add(queueItem1);
        
        containerQueue.addQueueItems(queueItems, conn);
        
    }
    
    //function writes robot file
    //image_id, source plate barcode, sourcewell, destination plate barcode, destination well
    public File createRearrayFile(ArrayList plate, String label) throws IOException
    {
        
        File      fl =   new File(filePath + label+ ".txt");
        String temp = null;
        
        FileWriter fr =  new FileWriter(fl);
        fr.write("Image Id"+DELIM+ "Mgc plate label"+DELIM+
        "Mgc plate position"+DELIM+  "Destination plate"+ DELIM+  "Destination plate position" +"\n");
        for (int count_seq = 0; count_seq < plate.size(); count_seq++)
        {
            SequenceDescription sd = (SequenceDescription) plate.get(count_seq);
            temp = sd.getImageId()+DELIM+ ((ContainerDescription)sd.getContainerDescription()).getDnaLabel()+DELIM+
            sd.getPosition() +DELIM+  label+ DELIM+  (count_seq + 2) +"\n";
            
            fr.write(temp);
            
        }
        fr.flush();
        fr.close();
        return fl;
    }
    
    
    
    
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) throws Exception
    {
        Connection c = null;
        Project p = null;
        Workflow w = null;
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            p = new Project(5);
            w = new Workflow(8);
            
            MgcOligoPlateManager om = new MgcOligoPlateManager(c, p, w, 94, false, new Protocol(Protocol.MGC_DESIGN_CONSTRUCTS), "htaycher");
            
            System.out.println("About to start thread");
            om.orderOligo();
            System.out.println("finished calling thread");
        } catch (Exception ex)
        {
            System.out.println(ex);
        } finally
        {
            DatabaseTransaction.closeConnection(c);
        }
    } //main
    
}
