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
    private boolean         m_isSort = true;
    private boolean         m_isSmall = false;
    private boolean         m_isMeddium = false;
    private boolean         m_isLarge = false;
    private boolean         m_isConttrols = true;
    private boolean         m_isPutOnQueue = true;
    private boolean         m_isFullPlate = true;
    private String          m_sample_type = "";
    
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
    
    
    public boolean createNewPlates(int mode ) throws FlexUtilException,FlexDatabaseException
    {
        
        m_fileList = new ArrayList();
        
        ArrayList samples = readFile();
        Hashtable sample_by_sequenceid = new Hashtable();
        ArrayList flex_sequences = null;
        Hashtable org_containers = null;
    
        ArrayList samples_arranged_by_format =new ArrayList();
        if (mode == 1)
        {
             fourPlatesMode( samples);
        }
        else
        {
             flex_sequences = getAllSequences(samples);
             org_containers = getAllContainers(samples);
             //separate samples by format
             samples_arranged_by_format = arrangeSamplesByFormat(samples);
            
        }
       for (int sample_format = 0; sample_format < samples_arranged_by_format.size(); sample_format++)
             {
                ArrayList current_samples = (ArrayList)samples_arranged_by_format.get(sample_format);
                if (m_isArrangeBySize)
                {
                
                    ArrayList sequences =  new ArrayList();
                    if (m_isSmall)
                    {
                        sequences = getSequencesBySize(LIMITS[0],LIMITS[1], flex_sequences,current_samples);
                        createContainers(sequences, org_containers, current_samples);
                        sequences = new ArrayList();
                    }
                    else
                    {
                        sequences.addAll(getSequencesBySize(LIMITS[0],LIMITS[1], flex_sequences,current_samples));
                    }
                    if (m_isMeddium)
                    {
                        sequences = getSequencesBySize(LIMITS[1],LIMITS[2], flex_sequences,current_samples);
                        createContainers(sequences, org_containers, current_samples);
                        sequences = new ArrayList();
                    }
                    else
                    {
                        sequences.addAll(getSequencesBySize(LIMITS[1],LIMITS[2], flex_sequences,current_samples));
                    }
                    if (m_isLarge)
                    {
                        sequences = getSequencesBySize(LIMITS[2],LIMITS[3], flex_sequences,current_samples);
                        createContainers(sequences, org_containers, current_samples);
                        sequences = new ArrayList();
                    }
                    else
                    {
                        sequences.addAll(getSequencesBySize(LIMITS[2],LIMITS[3], flex_sequences,current_samples));
                    }
                    if (sequences.size() != 0)
                    createContainers(sequences, org_containers, current_samples);
                }
                else
                {
                      createContainers( flex_sequences, org_containers, current_samples);
                }
            
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
    public void                 setSampleType(String s){ m_sample_type = s;}
    public void                 setSort(boolean b){ m_isSort = b;}
    public void                 isPutOnQueue(boolean b){m_isPutOnQueue=b;}
    public void                 isFullPlates(boolean b){m_isFullPlate=b;}
    public void                 isControls(boolean b)
    { 
        m_isConttrols = b;
        if (b)
            m_number_of_controls = 2;
        else
            m_number_of_controls = 0;
    }
    public void                 isArrangeBySize(boolean b){ m_isArrangeBySize=b;}
    public void                 isSmall(boolean b){ m_isSmall=b;}
    public void                 isMeddium(boolean b){ m_isMeddium=b;}
    public void                 isLarge(boolean b){ m_isLarge =b;}
    //-----------------------------------------
    
    
    //function works for mode ==1 where
    //first sample from 4 put on one container
    //second ffrom group on second and so on
    private void fourPlatesMode(ArrayList samples) throws FlexDatabaseException
    {
         ArrayList samples1 = new ArrayList();
         ArrayList flex_sequences = null;
        Hashtable org_containers = null;
        ArrayList samples2 = new ArrayList();
        ArrayList samples3 =new ArrayList();
        ArrayList samples4 =new ArrayList();
        for (int count = 0; count < samples.size(); count++)
        {

            samples1.add(samples.get(count++));
            samples2.add(samples.get(count++));
            samples3.add(samples.get(count++));
            samples4.add(samples.get(count));
        }
         flex_sequences = getAllSequences(samples1);
        org_containers = getAllContainers(samples1);
         createContainers( flex_sequences, org_containers, samples1);
        createContainers( flex_sequences, org_containers, samples2);
        createContainers( flex_sequences, org_containers, samples3);
        createContainers( flex_sequences, org_containers, samples4);
    }
    
    
    //read file with info for the rearray 
    //file should have the following structure
    //flexseqid - org plate id - well - status (+/+1)
    private ArrayList readFile() throws FlexUtilException
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
            catch(Exception e1) 
            {  
                System.out.println(e1.getMessage()); 
                throw new FlexUtilException("Can not read file"); 
            }
             System.out.println(e.getMessage()); 
            throw new FlexUtilException("Can not read file");   
        }
    }
    
    //function gets  all flex sequences from db for samples that go into rearray
    private ArrayList getAllSequences(ArrayList samples)                throws FlexDatabaseException
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
            System.out.println(e.getMessage());
            throw new FlexDatabaseException("Cannot retrive sequences from database");
        }
        return seq;
    }
    
    
     //function gets  all containers from db for samples that go into rearray
    private Hashtable getAllContainers(ArrayList samples) throws FlexDatabaseException
    {
        Hashtable containers = new Hashtable();
        Hashtable containers_type = new Hashtable();
         String c_type = null;
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
                    if (s.getConstructid() != -1)
                    {
                        Construct c = Construct.findConstruct( s.getConstructid());
                        c_type = c.getType();
                        containers_type.put(pls.getPlateIdName(), c_type);
                        pls.setFormat(c_type);
                    }
                }
                else
                {
                    Container old_container = (Container)containers.get( pls.getPlateIdName());
                    Sample s = old_container.getSample(pls.getWellId());
                    pls.setSampleId( s.getId() );
                    if ( containers_type.containsKey (pls.getPlateIdName() ) )
                    {
                        pls.setFormat( (String) containers_type.get(pls.getPlateIdName() ));
                    }
                }
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            throw new FlexDatabaseException("Cannot retrive containers from database");
        }
        return containers;
    }
    
    
    //functions returns all sequences of size between param1 and param2
    //that belongs to the samples
    private ArrayList getSequencesBySize(int from, int to, ArrayList flex_sequences, ArrayList samples)
    {
        ArrayList seq  = new ArrayList();
        Hashtable seq_ids = new Hashtable();
       
        //create hash sequences
        for (int count = 0 ; count < samples.size(); count++)
        {
            PlateSample s = (PlateSample)samples.get(count);
            seq_ids.put(new Integer( s.getSequenceId() ),"");
        }
        for (int count = 0 ; count < flex_sequences.size(); count++)
        {
            FlexSequence fl = (FlexSequence) flex_sequences.get(count);
            if (fl.getCdslength() >= from && fl.getCdslength() < to && seq_ids.containsKey(new Integer(fl.getId())))
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
           
            if (( count > 0 && (count + 1) % (m_wells_on_plate - m_number_of_controls) == 0)
                        || count == ( seq.size() - 1) )
            {
               
                if (m_isFullPlate && plate_sequences.size() == (m_wells_on_plate - m_number_of_controls) || !m_isFullPlate  )
                {
                    //create new plate
                    if (m_isSort)
                        Algorithms.rearangeSawToothPatternInFlexSequence(plate_sequences);
                    ArrayList file_entries = new ArrayList();
                    try
                    {
                        Container newcontainer = createContainer( plate_sequences, containers, samples, file_entries);
                        if (newcontainer != null ) m_rearrayed_containers.add(newcontainer);
                        plate_sequences = new ArrayList();
                    }catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                        throw new FlexDatabaseException("Cannot create container.");
                    }
                    try
                    {
                        File robot_file = createRearrayFile( file_entries);
                        m_fileList.add( robot_file );
                    }
                    catch(Exception e)
                    {
                        System.out.println(e.getMessage());
                        throw new FlexDatabaseException("Cannot create file.");
                    }
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
        int position = 1;
        if (m_isConttrols)
        {
            Sample control_positive = new Sample(Sample.CONTROL_POSITIVE, position++,contId);
            cont.addSample(control_positive);
        }
  
        String type = null;
        if (!m_sample_type.equals(""))
            type = m_sample_type;
        else
             type = Sample.getType(m_protocol.getProcessname());

        for (int count = 0; count < plate_sequences.size(); count ++)
        {
            FlexSequence fl = (FlexSequence) plate_sequences.get(count);
            //get sample we create it from
             PlateSample pl_sample = getPlateSampleForMapping( samples,  fl.getId() );
             //reset label
             if (count == 0)     cont.setLabel(cont.getLabel()+ pl_sample.getPlatePostfix() );
             
             
            Container org_container = (Container)org_containers.get(pl_sample.getPlateIdName());
            if ( ! org_containers_for_this_container.contains(org_container))
            {
                org_containers_for_this_container.add(org_container);
     
            }
            Sample org_sample = org_container.getSample(pl_sample.getWellId());
            Sample new_sample = new Sample(type, position++, cont.getId(),  org_sample.getConstructid(), org_sample.getOligoid(), Sample.GOOD);
            cont.addSample(new_sample);
            sampleLineageSet.addElement(new SampleLineage( org_sample.getId(), new_sample.getId()));
    
            FileEntry sample_entry = new FileEntry(org_container.getLabel(),org_sample.getPosition(), cont.getLabel(), new_sample.getPosition() );
            file_entries.add(sample_entry);
         }
        if (m_isConttrols)
        {
            Sample control_negative = new Sample(Sample.CONTROL_NEGATIVE, position++,contId);
            cont.addSample(control_negative);
        }
        cont.insert(m_conn);
        //process sample linerage
        //rearrayed plate is output container
        createSampleLinerageForContainer( cont, org_containers_for_this_container,sampleLineageSet );
        if (m_isPutOnQueue) putPlateOnQueue(cont);
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
    
    //function put on queueu plates 
    private void putPlateOnQueue(Container cont) throws FlexDatabaseException
    {
        LinkedList queueItems = new LinkedList();
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
         Vector protocols = m_workflow.getNextProtocol(m_protocol);
        //put on queue
        for (int i = 0; i < protocols.size(); i++)
        {
            Protocol pr = (Protocol) protocols.get(i);
            QueueItem queueItem = new QueueItem( cont, pr, m_project, m_workflow);
            queueItems.add(queueItem);
        }
     
        containerQueue.addQueueItems(queueItems, m_conn);
        
    }
    
    
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
    //function arrange samples by format
    private ArrayList arrangeSamplesByFormat(ArrayList samples)
    {
      //separate samples by format
         ArrayList samples_closed = new ArrayList();
         ArrayList samples_open = new ArrayList();
         ArrayList samples_none =new ArrayList();
         ArrayList samples_result =new ArrayList();
         for (int count = 0; count < samples.size(); count++)
         {
             PlateSample s = (PlateSample) samples.get(count);
             if (s.getFormat().equalsIgnoreCase( PlateSample.FORMAT_CLOSED ))
             {
                 samples_closed.add(s);
             }
             else if (s.getFormat().equalsIgnoreCase( PlateSample.FORMAT_OPEN))
             {
                 samples_open.add(s);
             }
             else if (s.getFormat().equalsIgnoreCase( PlateSample.FORMAT_NOT_KNOWN ))
             {
                 samples_none.add(s);
             }
         }
         if (samples_closed.size() != 0) samples_result.add(samples_closed);
         if (samples_open.size() != 0)samples_result.add(samples_open);
         if (samples_none.size() != 0)samples_result.add(samples_none);
         return samples_result;
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
       
        public static final String FORMAT_CLOSED = "CLOSED";
        public static final String FORMAT_OPEN = "FUSION";
        
        public static final String FORMAT_NOT_KNOWN = "NO";
        
        private int i_seqid = -1;
        private int i_plateid = -1;
        private int i_wellid = -1;
        private int i_sampleid = -1;
        private String i_format = FORMAT_NOT_KNOWN;//closed
        private String  i_seqid_str = null;
        private String  i_plateid_str = null;
        private boolean i_state_used_for_mapping = false;
       
        public PlateSample(String seqid, String plateid, String wellid )
        {
            i_seqid = Integer.parseInt(seqid);
            i_plateid = Integer.parseInt(plateid);
            i_wellid = Integer.parseInt(wellid);
            i_seqid_str = seqid;
            i_plateid_str = plateid;
  
        }
        
        public void             setSampleId(int i){ i_sampleid = i;}
        public void             setState(boolean i ){ i_state_used_for_mapping = i;}
        public void             setFormat(String i){  i_format = i;}
        
        public String           getPlatePostfix()
        {
            if (i_format.equalsIgnoreCase( FORMAT_CLOSED ))
                return "-C";
            else if ( i_format.equalsIgnoreCase( FORMAT_OPEN))
                return "-F";
            else if  (i_format.equalsIgnoreCase( FORMAT_NOT_KNOWN ))
               return "";
            return "";
        }
        public boolean          getState(){ return i_state_used_for_mapping;}
        public String           getSequenceidName(){ return i_seqid_str;}
        public int              getSampleId(){ return i_sampleid;}
        public int              getSequenceId(){ return i_seqid;}
        public int              getPlateId(){ return i_plateid;}
        public String           getPlateIdName(){ return i_plateid_str;}
        public int              getWellId(){ return i_wellid;}
        public String              getFormat(){ return i_format;}
        
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
        String fname = "E:\\HTaycher\\Yeast\\rearraytest.txt";
        InputStream input = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            int projectid = 2;
            int workflowid = 11;
            int protocolid = 42;
            input = new FileInputStream(fname);
            RearrayerForPlates rearrayer = new RearrayerForPlates(c,projectid, workflowid,protocolid,input,"elena_taycher@hms.harvard.edu");
           rearrayer.setWellsNumbers(96);
            rearrayer.isArrangeBySize(true);
            rearrayer.isControls(false);
           // rearrayer.setSampleType(sampletype);
            rearrayer.setSort(true);
            rearrayer.isSmall(false);
            rearrayer.isMeddium(false);
            rearrayer.isLarge(false);
            rearrayer.isFullPlates(false);
            rearrayer.isPutOnQueue(false);
            rearrayer.createNewPlates(0);
            c.rollback();
           // c.commit();
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
