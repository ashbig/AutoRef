/*
 * CreateOligoForBC.java
 *
 * Created on April 7, 2003, 11:31 AM
 */

package edu.harvard.med.hip.flex.special_projects;

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
 * @author  dzuo
 */
public class CreateOligoForBC {
     private static final String PLATETYPE = "96 WELL OLIGO PLATE";
     private static final String DILIM = "\t";
    private static final String filePath = "/tmp/";
     public final static int P5_OLIGO = 5;
        public final static int P3_OLIGO = 3;     
    
        
        
    private int             m_projectid = -1;
    private int             m_workflowid = -1;
    
    private InputStream     m_fileinput = null;
    private Connection      m_conn = null;
   
    private String          m_username = null;
    
    private ArrayList       m_fileList = null;
    
    private Project         m_project = null;
    private Workflow        m_workflow = null;
   
    
    /** Creates a new instance of RearrayerForPlates */
    public CreateOligoForBC(Connection con, int prid, int wfid,
                                InputStream filein, String user)
                                throws FlexDatabaseException
    {
        m_projectid = prid;
        m_workflowid = wfid;
       
        m_fileinput = filein;
        m_conn = con ;
        m_username = user;
       
        m_project = new Project(m_projectid);
        m_workflow = new Workflow (m_workflowid);
        
    }
    
   
 
    
    
    
    //-----------------------------------------
    //read file with info for the rearray 
    //file should have the following structure
    //flexseqid - org plate id - well - status (+/+1)
    public void readFile() throws FlexDatabaseException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(m_fileinput));
        String line = null;
        String plateid_prev = null;
        String gi = null;
        
        Oligo ol_5 = null;
        Oligo ol_3 = null;
        Oligo ol_3f = null;
        FlexSequence seq = null;
        Sequence sequence = null;
        Construct construct = null;
        Construct construct_fusion = null;
        Container cont_5p = null;
        Container cont_3p = null;
        Container cont_3f = null;
        Container cont_temp = null;
        ArrayList constructs = new ArrayList();
        //ArrayList sequences = new ArrayList();
        //ArrayList constructs_c = new ArrayList();
        //ArrayList constructs_f = new ArrayList();
        Plateset plateset = null;
        int count1 = 0;
        //create plate location
        Location location = new Location(Location.CODE_UNAVAILABLE,Location.UNAVAILABLE, "");
        Location location1 = new Location(Location.CODE_FREEZER,Location.FREEZER, "");
        
        try
        {
            while((line = in.readLine()) != null)
            {
                System.out.println(line);
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String info[] = new String[9];
                int i = 0;
                //sequenceid, plateid, well id, 5oligo seq, 3 oligo seq, 3 oligo fusion seq
                while(st.hasMoreTokens())
                {
                    info[i] = st.nextToken();
                    i++;
                }
                //create 2 oligo
                /**
                if (info[0].equals("0"))
                {
                
                     ol_5 = new Oligo( "OLIGO_5P", "NNNNN", 0.0);
                     ol_3 = new Oligo( "OLIGO_3P","NNNNN" , 0.0);
                     Hashtable name = new Hashtable();
                     name.put(FlexSequence.NAMETYPE, "SGD");
                     name.put(FlexSequence.NAMEVALUE, info[5]);
                     name.put(FlexSequence.NAMEURL, "");
                     name.put(FlexSequence.DESCRIPTION, "");
                     Vector publicInfo = new Vector();
                     publicInfo.add(name);
                     seq = new FlexSequence(-1, 
                                "INPROCESS", 
                                "Saccharomyces cerevisiae", 
                                null, 
                                "NNNNN", 0, 1, 0, 1, 
                                publicInfo);
                     seq.insert(m_conn);
                }
                 **/
                //else
                //{
                    ol_5 = new Oligo("five", info[3], Double.parseDouble(info[4]));
                    ol_5.setTagSequence_5p(m_project, m_workflow);
                    ol_3 = new Oligo( "three", info[7], Double.parseDouble(info[8]));
                    ol_3.setTagSequence_3p_Close(m_project, m_workflow);
                    ol_3f = new Oligo("threeopen", info[5], Double.parseDouble(info[6]));
                    ol_3f.setTagSequence_3p_Fusion(m_project, m_workflow);
                    seq = new FlexSequence(Integer.parseInt(info[0]));
                    
                //}
                 sequence = new Sequence(seq.getId(), seq.getCdsstart(), seq.getCdsstop());
                 int pairid = FlexIDGenerator.getID("pairid");
                 construct = new Construct(sequence, ol_5,ol_3, "CLOSED", pairid, m_project, m_workflow);
                 construct_fusion = new Construct(sequence, ol_5, ol_3f, "FUSION",pairid, m_project,m_workflow);
                 constructs.add(construct);
                 constructs.add(construct_fusion);
                 //sequences.add(sequence);
                 //constructs_c.add(construct);
                 //constructs_f.add(construct_fusion);
                 /**
                 if (info[0].equals("0"))
                 {
                     ol_3.insert(m_conn);
                     ol_5.insert(m_conn);
                     
                 }
                  **/
                 ol_5.insert(m_conn);
                 ol_3.insert(m_conn);
                 ol_3f.insert(m_conn);
                 construct.insert(m_conn);
                 construct_fusion.insert(m_conn);
                 
                 insertProcessInputOutput(construct.getId(), construct_fusion.getId(), sequence.getId());
  
                 //plate new plate?
                 if ( plateid_prev == null || ( plateid_prev != null && ! plateid_prev.equals(info[1]))  )
                 {
                     //insert previous containers
                     if (cont_5p != null )
                     {
                        createNewContainers( cont_5p,  cont_3p,  cont_3f, cont_temp,plateset, constructs);
                     }
                     int threadId = FlexIDGenerator.getID("threadid");
                     String label_5p = Container.getLabel("B","OU",threadId,null);
                     String label_3p = Container.getLabel("B","OC",threadId,null);
                     String label_3f = Container.getLabel("B","OF",threadId,null);
                     String label_temp = Container.getLabel("B", "RD", threadId, null);
                     cont_5p = new Container(PLATETYPE,location,label_5p,threadId);
                     cont_3p = new Container(PLATETYPE,location,label_3p,threadId);
                     cont_3f = new Container(PLATETYPE,location,label_3f,threadId);
                     cont_temp = new Container(PLATETYPE,location1,label_temp, threadId);
                     /*
                     Sample pos_control_5p = new Sample(Sample.CONTROL_POSITIVE, 1, cont_5p.getId());
                     cont_5p.addSample(pos_control_5p);
                     Sample pos_control_3p = new Sample(Sample.CONTROL_POSITIVE, 1, cont_3p.getId());
                     cont_3p.addSample(pos_control_3p);
                     Sample neg_control_5p = new Sample(Sample.CONTROL_NEGATIVE, 1, cont_5p.getId());
                     cont_5p.addSample(neg_control_5p);
                     Sample neg_control_3p = new Sample(Sample.CONTROL_NEGATIVE, 1, cont_3p.getId());
                     cont_3p.addSample(neg_control_3p);
                      **/
                     plateset = new Plateset(cont_5p.getId(), cont_3f.getId(), cont_3p.getId(), cont_temp.getId());
                      plateid_prev = info[1];
                 }
                 int wellid = getPosition(info[2]);
                 Sample ol5 = new Sample("OLIGO_5P", wellid, cont_5p.getId(), construct.getId(), ol_5.getOligoID(),"G" );
                 cont_5p.addSample(ol5);
                 Sample ol3 = new Sample("OLIGO_3C", wellid, cont_3p.getId(), construct.getId(), ol_3.getOligoID(),"G");
                 cont_3p.addSample(ol3);
                 Sample ol3f = new Sample("OLIGO_3F", wellid, cont_3f.getId(), construct.getId(), ol_3f.getOligoID(),"G");
                 cont_3f.addSample(ol3f);
                 Sample s_temp = new Sample("DNA", wellid, cont_temp.getId(), construct.getId(), -1, "G");
                 cont_temp.addSample(s_temp);
                 
                System.out.println(info[0]);
              
                
            }
            if (cont_5p != null )
            {
                createNewContainers( cont_5p,  cont_3p,  cont_3f,cont_temp, plateset, constructs);
            }
            
            //insertProcessInputOutput(constructs_c,constructs_f,sequences);
            
            in.close();
            
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            try
            {
                in.close();
            }
            catch(Exception e1)  {    
                System.out.println(e1.getMessage());
                throw new FlexDatabaseException("Can not close file");   
            }
             throw new FlexDatabaseException("Can not read file");   
        }
    }
    

    private void createNewContainers(Container cont_5p, Container cont_3p, Container cont_3f,Container cont_temp, Plateset plateset, ArrayList constructs)
    throws Exception
    {
        //insert previous containers
               QueueItem queueItem = null;
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        LinkedList queueItems = new LinkedList();      
         cont_5p.insert(m_conn);
         cont_3p.insert(m_conn);
         cont_3f.insert(m_conn);
         cont_temp.insert(m_conn);
         plateset.insert(m_conn);
         //process input
         insertInputOutputContainer(cont_5p.getId(), cont_3p.getId(), cont_3f.getId(),cont_temp.getId(), constructs);
         constructs = new ArrayList();
         //put on queue
         Protocol protocol = new Protocol(Protocol.RECEIVE_OLIGO_PLATES);
         QueueItem queueItem_5p = new QueueItem(cont_5p, protocol, m_project,m_workflow);
         QueueItem queueItem_3p = new QueueItem(cont_3p, protocol, m_project,m_workflow);
         QueueItem queueItem_3f = new QueueItem(cont_3f, protocol, m_project,m_workflow);
         queueItems.add(queueItem_5p);
         queueItems.add(queueItem_3p);
         queueItems.add(queueItem_3f);
         //queueItem = new QueueItem(cont_3p, protocol, m_project,m_workflow);
         //queueItems.add(queueItem);
         containerQueue.addQueueItems(queueItems, m_conn);
          
}
    private int getPosition( String well) 
    {
        int position = -1;
        well = well.toLowerCase();
        int row = (int)well.charAt(0);
        int column = Integer.parseInt(well.substring(1));
        int a_value = (int) 'a';
        int first_char_value = 0;
        int second_char_value = 0;
        int row_value = 0;
               
        
        row_value = row - a_value + 1;
     
        return (column - 1) * 8 +  row_value ;
        
  
    }
     /**
     * Insert Process Execution record for Design Construct protocol
     * Insert one process object input record for each input sequence
     */
    protected void insertProcessInputOutput(int constr_id, int constr_id_fusion, int seqid) throws FlexDatabaseException
  
    {
        
        Researcher researcher = new Researcher();
        int userId = researcher.getId("SYSTEM");
        researcher = new Researcher(userId);
        
        String status = "S"; //SUCCESS
        // insert process execution record into process execution table
        Protocol protocol = new Protocol(Protocol.DESIGN_CONSTRUCTS);
        edu.harvard.med.hip.flex.process.Process process = 
            new edu.harvard.med.hip.flex.process.Process(
                protocol, "S",researcher, m_project, m_workflow);
        int executionId = process.getExecutionid();
        process.insert(m_conn);
     
            
            //insert sequence input process objects
            SequenceProcessObject spo = new SequenceProcessObject(seqid,executionId,"I");
            spo.insert(m_conn);
        
            //Insert one process output records for each output construct
            //(two per sequence)
            ConstructProcessObject cpo = new ConstructProcessObject(constr_id,executionId,"O");
            ConstructProcessObject cpo_fusion = new ConstructProcessObject(constr_id_fusion,executionId,"O");

            cpo.insert(m_conn);
            cpo_fusion.insert(m_conn);
                
    }
    
    
    private void insertInputOutputContainer(int cont_5pid, int cont_3pid, int cont_3fid, int cont_tempid, ArrayList constructs) throws Exception
    {
        Researcher researcher = new Researcher();
        int userId = researcher.getId("SYSTEM");
        researcher = new Researcher(userId);
        
        Protocol protocol = new Protocol(Protocol.GENERATE_OLIGO_ORDERS);
        edu.harvard.med.hip.flex.process.Process process = 
            new edu.harvard.med.hip.flex.process.Process(
                protocol, "S",researcher, m_project, m_workflow);
        int executionId = process.getExecutionid();
       
        
        //insert input for container
        Construct construct = null;
        ConstructProcessObject cpo = null;
        for (int count = 0; count < constructs.size(); count++)
        {
            construct = (Construct)constructs.get(count);
            
            cpo = new ConstructProcessObject(construct.getId(),executionId,"I");
            process.addProcessObject(cpo);
        } //while
        process.insert(m_conn);
        //insert output
       
        ContainerProcessObject platepo_5p = new ContainerProcessObject(cont_5pid,executionId,"O");
        platepo_5p.insert(m_conn);
        
        ContainerProcessObject platepo_3s = new ContainerProcessObject(cont_3pid,executionId,"O");
        ContainerProcessObject platepo_3f = new ContainerProcessObject(cont_3fid,executionId,"O");
        ContainerProcessObject platepo_temp = new ContainerProcessObject(cont_tempid, executionId, "O");
        platepo_3s.insert(m_conn);
        platepo_3f.insert(m_conn);
        platepo_temp.insert(m_conn);
       
    }
    
     
        
     
    
   
    
        //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) throws Exception
    {
        Connection c = null;
        Project p = null;
        Workflow w = null;
        String fname = "C:\\Documents and Settings\\dzuo\\My Documents\\work\\production\\fusion_1_7.txt";
        InputStream input = null;
         CreateOligoForBC rearrayer =null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            if(c==null)
                System.out.println("connection is null");
            int projectid = 5;
            int workflowid = 9;
         //   int protocolid = 40;
            
             input = new FileInputStream(fname);
            rearrayer = new CreateOligoForBC(c,projectid, workflowid,input,"dzuo@hms.harvard.edu");
           
            rearrayer.readFile();c.commit();

        } catch (Exception ex)
        {
            System.out.println(ex);
            c.rollback();
        } finally
        {
            input.close();
            DatabaseTransaction.closeConnection(c);
        }
        System.exit(0);
    } //main    
}
