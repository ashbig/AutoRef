/*
 * RearrayerForPlates.java
 *
 * Created on November 19, 2002, 1:10 PM
 */

package edu.harvard.med.hip.flex.process;


import java.io.*;
import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author  htaycher
 */
public class RearrayerForPlates
{
    private static final String DILIM = "\t";
    private static final String filePath = "/tmp/";
    private static final String REARRAYEDPLATETYPE = "96 WELL PLATE";
    public static final int smallCDSLimit = 2000;
    public static final int mediumCDSLimit = 4000;
    public static final int largeCDSLimit = 100000;
    public static final int LIMITS[] = {0, smallCDSLimit, mediumCDSLimit, largeCDSLimit};
    
    
    private int             m_projectid = -1;
    private int             m_workflowid = -1;
    private int             m_protocolid = -1;
    private InputStream     m_fileinput = null;
    private Connection      m_conn = null;
    private ArrayList       m_rearrayed_containers = null;
    private String          m_username = null;
    private String          m_platetype = null;
    
    private ArrayList       m_fileList = null;
    private Protocol        m_protocol = null;
    private Project         m_project = null;
    private Workflow        m_workflow = null;
    private int             m_wells_on_plate = 96;
    private int             m_number_of_controls = 2;
    private boolean         m_isArrangeBySize = true;
    
    /** Creates a new instance of RearrayerForPlates */
    public RearrayerForPlates(Connection con, int prid, int wfid, int protid,
                                InputStream filein, String user)
                                throws FlexDatabaseException
    {
        m_projectid = prid;
        m_workflowid = wfid;
        m_protocolid = protid;
        m_fileinput = filein;
        m_conn = con ;
        m_username = user;
        m_platetype = REARRAYEDPLATETYPE;
        m_project = new Project(m_projectid);
        m_workflow = new Workflow (m_workflowid);
        m_protocol = new Protocol(m_protocolid);
        m_rearrayed_containers = new ArrayList();
    }
    
    
    public boolean createNewPlates() throws FlexDatabaseException
    {
        
        m_fileList = new ArrayList();
        
        ArrayList samples = readFile();
        Hashtable sample_by_sequenceid = new Hashtable();
        ArrayList flex_sequences = getAllSequences(samples);
        Hashtable org_containers = getAllContainers(samples);
        
        if (m_isArrangeBySize)
        {
            ArrayList small_sequences = getSequencesBySize(LIMITS[0],LIMITS[1], flex_sequences);
            createContainers(small_sequences, org_containers, samples);
            ArrayList middle_sequences = getSequencesBySize(LIMITS[1],LIMITS[2], flex_sequences);
            createContainers(middle_sequences, org_containers, samples);
            ArrayList large_sequences = getSequencesBySize(LIMITS[2],LIMITS[3],flex_sequences);
            createContainers(large_sequences, org_containers, samples);
        }
        else
        {
            createContainers( flex_sequences, org_containers, samples);
        }
          
        try{
            sendRobotFiles(m_username);
        }
        catch(Exception e)
        {
            throw new FlexDatabaseException("Cannot send file to user");
        }
        return false;
    }
    
    
    
    
    public ArrayList getRearrayContainers()    {  return m_rearrayed_containers;    }
    public Project              getProject()    { return m_project;}
    public Workflow             getWorkflow()    { return m_workflow;}
    public Protocol             getProtocol()    { return m_protocol;}
    public String               getUserName()    { return m_username;}
    
    public void                 setPlateType(String s){ m_platetype = s;}
    public void                 setWellsNumbers(int s){ m_wells_on_plate = s;}
    //-----------------------------------------
    //read file with info for the rearray 
    //file should have the following structure
    //flexseqid - org plate id - well - status (+/+1)
    private ArrayList readFile() throws FlexDatabaseException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(m_fileinput));
        String line = null;
        String gi = null;
        ArrayList samples = new ArrayList();
        try
        {
            while((line = in.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String info[] = new String[4];
                int i = 0;
                while(st.hasMoreTokens())
                {
                    info[i] = st.nextToken();
                    i++;
                }
                if (info[3].equals("+") || info[3].equals("1") )
                {
                    samples.add(new PlateSample(info[0],info[1], info[2]));
                }
                
            }
            in.close();
            return samples;
        }
        catch(Exception e)
        {
            
            try
            {
                in.close();
            }
            catch(Exception e1)  {     throw new FlexDatabaseException("Can not read file");   }
             throw new FlexDatabaseException("Can not read file");   
        }
    }
    
    //function gets  all flex sequences from db for samples that go into rearray
    private ArrayList getAllSequences(ArrayList samples)
                     throws FlexDatabaseException
    {
        ArrayList seq = new ArrayList();
        try
        {
            for (int count = 0; count < samples.size(); count++)
            {
                PlateSample pls = (PlateSample) samples.get(count);
                int id = pls.getSequenceId();
                FlexSequence fl = new FlexSequence(id);

                seq.add(fl);
            
            }
        }
        catch(Exception e)
        {
            throw new FlexDatabaseException("Cannot retrive sequences from database");
        }
        return seq;
    }
    
    
     //function gets  all containers from db for samples that go into rearray
    private Hashtable getAllContainers(ArrayList samples) throws FlexDatabaseException
    {
        Hashtable containers = new Hashtable();
        try
        {
            for (int count = 0; count < samples.size(); count++)
            {
                PlateSample pls = (PlateSample) samples.get(count);
                int id = pls.getPlateId();
                if ( ! containers.keySet().contains(pls.getPlateIdName()) )
                {
                    Container new_cont = new Container(id);
                    containers.put(pls.getPlateIdName(), new_cont);
                    new_cont.restoreSample();
                    Sample s = new_cont.getSample(pls.getWellId());
                    pls.setSampleId( s.getId() );
                }
                else
                {
                    Container old_container = (Container)containers.get( pls.getPlateIdName());
                    Sample s = old_container.getSample(pls.getWellId());
                    pls.setSampleId( s.getId() );
                }
                
            }
        }
        catch(Exception e)
        {
            throw new FlexDatabaseException("Cannot retrive containers from database");
        }
        return containers;
    }
    
    
    //functions returns all sequences of size between param1 and param2
    private ArrayList getSequencesBySize(int from, int to, ArrayList flex_sequences)
    {
        ArrayList seq  = new ArrayList();
        for (int count = 0 ; count < flex_sequences.size(); count++)
        {
            FlexSequence fl = (FlexSequence) flex_sequences.get(count);
            if (fl.getCdslength() >= from && fl.getCdslength() < to)
                seq.add(fl);
        }
        
        return seq;
    }
    
    //function takes in all sequences of one group and create plates from them
    private void createContainers(ArrayList seq, Hashtable containers, ArrayList samples)
        throws FlexDatabaseException
    {
        ArrayList plate_sequences = new ArrayList();
        for (int count = 0 ; count < seq.size(); count++)
        {
            plate_sequences.add(seq.get(count));
            if (( count > 0 && (count + 1) % (m_wells_on_plate - m_number_of_controls) == 0) || count == seq.size() - 1)
            {
                //create new plate
                Algorithms.rearangeSawToothPatternInFlexSequence(plate_sequences);
                ArrayList file_entries = new ArrayList();
                try
                {
                    Container newcontainer = createContainer( plate_sequences, containers, samples, file_entries);
                   
                    if (newcontainer != null ) m_rearrayed_containers.add(newcontainer);
                }catch(Exception e)
                {
                    throw new FlexDatabaseException("Cannot create container.");
                }
                try
                {
                    File robot_file = createRearrayFile( file_entries);
                    m_fileList.add( robot_file );
                }
                catch(Exception e)
                {
                    throw new FlexDatabaseException("Cannot create file.");
                }
                
            }
            
        }
      
    }
    
      //**********************************container creation ***************************
    
    
    //function creates rearray container
    private Container createContainer(
                                        ArrayList plate_sequences,
                                        Hashtable org_containers,
                                        ArrayList samples,
                                        ArrayList file_entries
                                        )
                                            throws FlexDatabaseException, FlexCoreException, 
                                            FlexWorkflowException, FlexProcessException
    {
        
        Location location = new Location(Location.UNAVAILABLE);
        //get thread
        int threadId = FlexIDGenerator.getID("threadid");
        //get label for destination plate
        ProjectWorkflow flow = new ProjectWorkflow(m_projectid,m_workflowid);
        String projectCode = flow.getCode();
        String processcode = m_protocol.getProcesscode();
        String plate_label = Container.getLabel( projectCode, processcode, threadId, null)   ;
        
        Container cont = new Container(m_platetype, location, plate_label, threadId );
        
        LinkedList org_containers_for_this_container = new LinkedList(); 
        //add positive control samples
        int contId = cont.getId();
        Vector sampleLineageSet = new Vector();
        
        Sample control_positive = new Sample(Sample.CONTROL_POSITIVE,1,contId);
        cont.addSample(control_positive);
        
        String type = Sample.getType(m_protocol.getProcessname());
        for (int count = 0; count < plate_sequences.size(); count ++)
        {
            FlexSequence fl = (FlexSequence) plate_sequences.get(count);
            //get sample we create it from
            
            PlateSample pl_sample = getPlateSampleForMapping( samples,  fl.getId() );
            Container org_container = (Container)org_containers.get(pl_sample.getPlateIdName());
            if ( ! org_containers_for_this_container.contains(org_container))
            {
                org_containers_for_this_container.add(org_container);
            }
            Sample org_sample = org_container.getSample(pl_sample.getWellId());
            
            Sample new_sample = new Sample(type, count + 2, cont.getId(),  org_sample.getConstructid(), org_sample.getOligoid(), Sample.GOOD);
            cont.addSample(new_sample);
            sampleLineageSet.addElement(new SampleLineage( org_sample.getId(), new_sample.getId()));
            
            FileEntry sample_entry = new FileEntry(org_container.getLabel(),org_sample.getPosition(), cont.getLabel(), new_sample.getPosition() );
            file_entries.add(sample_entry);
        }
        Sample control_negative = new Sample(Sample.CONTROL_NEGATIVE, plate_sequences.size() + 2,contId);
        cont.addSample(control_negative);
        cont.insert(m_conn);
        
        //process sample linerage
        //rearrayed plate is output container
        createSampleLinerageForContainer( cont, org_containers_for_this_container,sampleLineageSet );
        return cont;
    }
    
    // process sample linerage
    //rearrayed plate is output container
    private void createSampleLinerageForContainer(
                                                Container cont, 
                                                LinkedList orgContainers,
                                                Vector sampleLineageSet 
                                                )
                                                throws FlexDatabaseException, FlexCoreException, 
                                                FlexWorkflowException, FlexProcessException
    {
        //process sample linerage
        //rearrayed plate is output container
        LinkedList newContainers = new LinkedList();
        newContainers.add(cont);
        String executionStatus = edu.harvard.med.hip.flex.process.Process.SUCCESS;
        ArrayList newCont = new ArrayList();
        newCont.add(cont);
        WorkflowManager manager = new WorkflowManager(m_project, m_workflow, "ProcessPlateManager");
        manager.createProcessRecord(executionStatus, m_protocol, new Researcher(Researcher.SYSTEM),
                                    null, orgContainers, newCont,
                                    null, sampleLineageSet, m_conn);
    }
    
    
    //get sample for mapping that was not used
    private PlateSample getPlateSampleForMapping(ArrayList samples, int flexseqid)
    {
        for (int count = 0; count < samples.size(); count++)
        {
            PlateSample ps = (PlateSample) samples.get(count);
            if (ps.getSequenceId() == flexseqid && ! ps.getState())
            {
                ps.setState(true);
                return ps;
            }
        }
        return null;
    }
    /*
    //function put on queueu plates 
    private void putPlateOnQueue(Container cont) throws FlexDatabaseException
    {
        LinkedList queueItems = new LinkedList();
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
         Vector protocols = m_workflow.getNextProtocol(m_protocol);
        //put on queue
        for (int i = 0; i < prot.size(); i++)
        {
            Protocol pr = (Protocol) protocols.get(i);
            QueueItem queueItem = new QueueItem( cont, pr, project, workflow);
            queueItems.add(queueItem);
        }
     
        containerQueue.addQueueItems(queueItems, m_conn);
        
    }
     **/
    
    //function writes robot file
    // source plate barcode, sourcewell, destination plate barcode, destination well
    public File createRearrayFile(ArrayList file_entries) throws IOException
    {
        
        File fl = null;
        String temp = null;
        FileWriter fr = null;
       
        
        for (int count = 0; count < file_entries.size(); count++)
        {
            FileEntry fe= (FileEntry) file_entries.get(count);
            if (count == 0)
            {
                fl =   new File(filePath + fe.getDestPlateLabel()+ ".txt");
                fr =  new FileWriter(fl);
                fr.write("Original plate label"+DILIM+"Original plate well"+DILIM+"Destination plate label"+ DILIM+  "Destination plate well\n");
            }
            
            fr.write(fe.toString(DILIM)+"\n");
            
        }
        fr.flush();
        fr.close();
        return fl;
    }
    
    
    //send notification to the user
    public void sendRobotFiles(String username) throws Exception
    {
        
        String to = "etaycher@hms.harvard.edu";
        String from = "etaycher@hms.harvard.edu";
        String cc = username;
        String subject = "Rearraed plates for the project - "+m_project.getName();
        String msgText = "The attached files are robot file(s) for rearrayed plates.\n";
        Mailer.sendMessage(to, from, cc, subject, msgText, m_fileList);
         
    }
    
    
    //inner class to hold plate sample from rearray file
    protected class PlateSample
    {
        private int i_seqid = -1;
        private int i_plateid = -1;
        private int i_wellid = -1;
        private int i_sampleid = -1;
        private String  i_seqid_str = null;
        private String  i_plateid_str = null;
        private boolean i_state_used_for_mapping = false;
       
        public PlateSample(String seqid, String plateid, String wellid)
        {
            i_seqid = Integer.parseInt(seqid);
            i_plateid = Integer.parseInt(plateid);
            i_wellid = Integer.parseInt(wellid);
            i_seqid_str = seqid;
            i_plateid_str = plateid;
        }
        
        public void             setSampleId(int i){ i_sampleid = i;}
        public void             setState(boolean i ){ i_state_used_for_mapping = i;}
        
        public boolean          getState(){ return i_state_used_for_mapping;}
        public String           getSequenceidName(){ return i_seqid_str;}
        public int              getSampleId(){ return i_sampleid;}
        public int              getSequenceId(){ return i_seqid;}
        public int              getPlateId(){ return i_plateid;}
        public String           getPlateIdName(){ return i_plateid_str;}
        public int              getWellId(){ return i_wellid;}
    }
    
    
     //inner class to hold file entry for robot file
    protected class FileEntry
    {
        private String  i_org_plate_label = null;
        private String  i_dest_plate_label = null;
        private int     i_org_well = -1;
        private int     i_dest_well = -1;
       
        public FileEntry(String org,int  orgwellid,  String dest, int destwellid)
        {
            i_org_plate_label = org;
            i_dest_plate_label = dest;
            i_org_well = orgwellid;
            i_dest_well = destwellid;
        }
        
       
        public String           getOrgPlateLabel(){ return i_org_plate_label;}
        public int              getOrgWellId(){ return i_org_well;}
        public String           getDestPlateLabel(){ return i_dest_plate_label;}
        public int              getDestWellId(){ return i_dest_well;}
        public String           toString(String delim)
        {
            return i_org_plate_label+delim+i_org_well+delim+i_dest_plate_label+delim+i_dest_well;
        }
    }
        
    
    
    
       //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) throws Exception
    {
        Connection c = null;
        Project p = null;
        Workflow w = null;
        String fname = "e:\\htaycher\\yeast\\rearray.txt";
        InputStream input = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            int projectid = 2;
            int workflowid = 10;
            int protocolid = 40;
            input = new FileInputStream(fname);
            RearrayerForPlates rearrayer = new RearrayerForPlates(c,projectid, workflowid,protocolid,input,"elena_taycher@hms.harvard.edu");
            rearrayer.setWellsNumbers(94);
            rearrayer.createNewPlates();
            c.commit();
        } catch (Exception ex)
        {
            System.out.println(ex);
        } finally
        {
            input.close();
            DatabaseTransaction.closeConnection(c);
        }
        System.exit(0);
    } //main
}
