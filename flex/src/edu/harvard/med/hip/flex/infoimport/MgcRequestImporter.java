/*
 * MgcRequestImporter.java
 *
 * Created on May 17, 2002, 3:41 PM
 */


/**
 *
 * @author  HTaycher
 */

/*
 * MgcRequestImporter.java
 *
 * This class  creates Request object from user requested GI numbers file 
  * and imports it into the database.
 *
 */

package edu.harvard.med.hip.flex.infoimport;

import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import sun.jdbc.rowset.*;
import javax.mail.*;


public class MgcRequestImporter
{
    public static final String DILIM = "\t!";
    public static final String DEFAULT = "NA";
    public static final String BLASTABLE_DATABASE_NAME = "MGC/genes";
    //public static final String BLASTABLE_DATABASE_NAME = "E:\\Users\\HIP\\HTaycher\\MGC\\genes";
    private Project             m_Project = null;
    private Workflow                 m_workflow = null;
  
    private String              m_UserName = null;
    
    private Request             m_Request = null;
    private int                 m_SuccessCount = 0;
    private int                 m_FailCount = 0;
    private int                 m_TotalCountRequests = 0;
    
    private Vector              m_messages = null;
    
    
    /** Creates new MgcRequestImporter */
    public MgcRequestImporter(Project project, Workflow wf, String user_name)
    {
        m_Project = project;
        m_workflow = wf;
        m_UserName = user_name;
        m_Request = new Request(m_UserName, m_Project);
        m_messages = new Vector();
    }
    
    public Project              getProject(){ return m_Project;}
    public Workflow             getWorkflow(){ return m_workflow;}
    public Request              getRequest(){ return m_Request;}
    public String               getUserName(){ return m_UserName;}
    
    /**
     * Import the requests into the database.
     *
     * @param requestInput The InputStream object containing the sequence requests.
     * @param conn The Connection object for insert.
     * @return vector of sequences from request.
     */
    public boolean performImport(InputStream requestInput, Connection conn)
    throws Exception
    {
        
        Vector request_sequences = new Vector();
        ArrayList sequencesMatchedByBlast = new ArrayList();
        ArrayList sequencesNotMatchedByBlast = new ArrayList();
        ArrayList  requestGI = new ArrayList();
        ArrayList  sequencesMatchedByGI = new ArrayList();
        boolean prev_step = true;
        //parse file and fill list of GI;
        prev_step =  parseRequestFile( requestInput  , requestGI) ;
        //search db for the sequences of MgcClones;
        //add matching sequences to request GI numbers;
        ArrayList notMatchedGI = new ArrayList();
        if (prev_step ) prev_step = matchGINumbersToMgcClones(requestGI, sequencesMatchedByGI, notMatchedGI);
        //get sequence for not matching GI
        Hashtable sequencesToBlat = new Hashtable();
        if (prev_step) prev_step = readSequences(notMatchedGI, sequencesToBlat) ;
        //blast sequences for not matching GI;
        if (prev_step) prev_step = blastSequences(sequencesToBlat, sequencesMatchedByBlast, sequencesNotMatchedByBlast);
        //save request to db
        m_Request.insert(conn);
        DatabaseTransaction.commit(conn);
        notifyUser(requestGI, sequencesMatchedByGI, sequencesMatchedByBlast, sequencesNotMatchedByBlast);
        m_Request = new Request(m_Request.getId());
        if (prev_step) prev_step = putOnQueue(conn)  ;
        DatabaseTransaction.commit(conn);
        
        //somthing went wrong notify user and myself
        if (! prev_step) 
                Algorithms.notifyUser(m_UserName, "importrequest.log", m_messages);
        //parse results for not matching GI
        return true;
        
    }
    
 
    /* Function queries for all mgc containers needed for request
     *put on Queue all containers and sequences
     */
    private boolean putOnQueue(Connection conn)
    {
         //get next protocol 
        Vector protocols = null;
        try{
            Protocol current_protocol = new Protocol(Protocol.IMPORT_MGC_REQUEST);
            protocols = m_workflow.getNextProtocol(current_protocol);
        }catch(Exception e){m_messages.add("Can not get protocol: " + Protocol.IMPORT_MGC_REQUEST); return false;}
        
        Protocol next_protocol = null;
        //put containers on queue
        //get requesred mgc containers 
        Rearrayer re = new Rearrayer( new ArrayList(m_Request.getSequences()) );
        ArrayList mgc_containers = null;
        try
        {
            re.findMgcContainers( );
            mgc_containers = re.getContainers();
        } catch (Exception e)
        {
            return false;
        }
//put mgc containers on queue
        Protocol protocol = null;
        Protocol protocolSeq = null;
        try{
            protocol = new Protocol(  Protocol.CREATE_CULTURE_FROM_MGC);
            protocolSeq = new Protocol(  Protocol.MGC_DESIGN_CONSTRUCTS);
        }catch(FlexDatabaseException ex)
        {
            m_messages.add("Can not get protocol for CREATE_CULTURE_FROM_MGC");
            return false;
        }
       // Protocol nextProtocol = workflow.getNextProtocol(protocol).get(0);
        QueueItem queueItem = null;
        LinkedList queueItems = new LinkedList();
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
        for (int cont_count = 0; cont_count < mgc_containers.size(); cont_count++)
        {
            queueItem = new QueueItem((MgcContainer) mgc_containers.get(cont_count),protocol, m_Project, m_workflow);
            queueItems.add(queueItem);
        }
        try{
            containerQueue.addQueueItems(queueItems, conn);
        }
        catch(Exception e)
        {
            m_messages.add("Can not put containers on queue for CREATE_CULTURE_FROM_MGC");
            return false;
        }
        queueItems.clear();
        
        
        //put sequences for oligo design on queue
        
        SequenceProcessQueue cloneQueue = new SequenceProcessQueue();
        
        for (int clone_count = 0; clone_count < m_Request.getSequences().size(); clone_count++)
        {
            queueItem = new QueueItem((FlexSequence) m_Request.getSequences().get(clone_count),protocolSeq, m_Project, m_workflow);
            queueItems.add(queueItem);
        }
         try{
             cloneQueue.addQueueItems(queueItems, conn);
        }
        catch(Exception e)
        {
            m_messages.add("Can not put sequences on queue for MGC_DESIGN_CONSTRUCTS");
            return false;
        }
       
        return true;
        
    }
    /*   
    private boolean putOnQueue(Connection conn)
    {
        Vector sequences = m_Request.getSequences();
        Rearrayer re = new Rearrayer( new ArrayList(sequences) );
        ArrayList mgc_containers = null;
        try
        {
            re.findMgcContainers( );
            mgc_containers = re.getContainers();
        } catch (Exception e)
        {
            return false;
        }
        
        //put containers on Queue
     *
        Protocol protocol = null;
        Protocol protocolSeq = null;
        try{
            protocol = new Protocol(  Protocol.CREATE_CULTURE_FROM_MGC);
            protocolSeq = new Protocol(  Protocol.MGC_DESIGN_CONSTRUCTS);
        }catch(FlexDatabaseException ex)
        {
            m_messages.add("Can not get protocol for CREATE_CULTURE_FROM_MGC");
            return false;
        }
       // Protocol nextProtocol = workflow.getNextProtocol(protocol).get(0);
        QueueItem queueItem = null;
        LinkedList queueItems = new LinkedList();
        ContainerProcessQueue containerQueue = new ContainerProcessQueue();
        
        for (int cont_count = 0; cont_count < mgc_containers.size(); cont_count++)
        {
            queueItem = new QueueItem((MgcContainer) mgc_containers.get(cont_count),protocol, m_Project, m_workflow);
            queueItems.add(queueItem);
        }
        try{
            containerQueue.addQueueItems(queueItems, conn);
        }
        catch(Exception e)
        {
            m_messages.add("Can not put containers on queue for CREATE_CULTURE_FROM_MGC");
            return false;
        }
        queueItems.clear();
        
        
        //put sequences for oligo design on queue
        
        SequenceProcessQueue cloneQueue = new SequenceProcessQueue();
        
        for (int clone_count = 0; clone_count < m_Request.getSequences().size(); clone_count++)
        {
            queueItem = new QueueItem((FlexSequence) m_Request.getSequences().get(clone_count),protocolSeq, m_Project, m_workflow);
            queueItems.add(queueItem);
        }
         try{
             cloneQueue.addQueueItems(queueItems, conn);
        }
        catch(Exception e)
        {
            m_messages.add("Can not put sequences on queue for MGC_DESIGN_CONSTRUCTS");
            return false;
        }
       
        return true;
        
    }
    
    
      */  
         
    /*Function parses request file and returns list of GI numbers
     */
    private boolean  parseRequestFile(InputStream requestInput, ArrayList gi_numbers)
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(requestInput));
        String line = null;
               
        try
        {
            while((line = in.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                while(st.hasMoreTokens())
                {
                    gi_numbers.add( st.nextToken().trim() );
                }
            }
            requestInput.close();
        }
        catch(Exception e)
        {
            m_messages.add("Can not read request file");
            try  
            { 
                requestInput.close(); 
            }catch(Exception e1)  
            {  
  
                m_messages.add("Can not read request file");
                return false;
            }
            return false;  
        }
        
       m_TotalCountRequests = gi_numbers.size();
       m_messages.add("Request file contains :" +m_TotalCountRequests +" GI numbers");
       return true;
    }
    
     /* Function 
      1. queries database for GI numbers of imported MGC clones
      *2. tries to match GI numbers from request to the one in DB
      * 3. adds matching sequences to  Request
      *@return ArrayList of not matched GI numbers
      */
    private  boolean  matchGINumbersToMgcClones(ArrayList requestGI, 
                    ArrayList sequencesMatchedByGI, ArrayList not_matching_gi_numbers)
    {
        
        ArrayList temp = new ArrayList(requestGI);
       
        String sql = "select n.namevalue as gi, n.sequenceid as sequence_id "+
        "from  mgcclone mc , name n \n" +
        "where ( mc.sequenceid = n.sequenceid and n.nametype = 'GI'  ) ";
        int current_gi = 0;
        int current_seq_id = 0;
        CachedRowSet crs = null;
       
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
     
            while(crs.next())
            {
                current_seq_id = crs.getInt("SEQUENCE_ID");
                current_gi = crs.getInt("GI");
                if ( temp.contains( Integer.toString(current_gi)) )
                {//create dummy flexsequence and add it to request
                    FlexSequence fc = new FlexSequence( current_seq_id, FlexSequence.GOOD, null, null, null, 0, 0, 0, 0, null, null, null);
                    m_Request.addSequence(fc);
                    sequencesMatchedByGI.add(new Integer(current_gi)  );
                    temp.remove( temp.indexOf( Integer.toString(current_gi) ) );
                }
            }
            
        } catch (Exception e)
        {
            m_messages.add("Can not get mgc clones sequence id from database, request import aborted");
            return false;
        } finally
        {
            DatabaseTransaction.closeResultSet(crs);
        }
        not_matching_gi_numbers.addAll(temp);
       
        return true;
    }
    
    
    
     /* Function queries ncbi for sequences for GI that was not matched to DB
      * @param ArrayList of GI numbers
      *@return Hashtable of FlexSequences , key - GI
      */
    private boolean  readSequences(ArrayList not_matching_gi_numbers, Hashtable sequences)
    {
        
        GenbankGeneFinder gb = new GenbankGeneFinder();
        Vector genBankSeq = new Vector();
        Hashtable seqData = new Hashtable();
        String current_gi = null;
        MgcMasterListImporter ml = new MgcMasterListImporter();
        Vector gi_data = new Vector();
        FlexSequence fs = null;
        MgcMasterListImporter ms = new MgcMasterListImporter();

        for (int gi_count = 0; gi_count < not_matching_gi_numbers.size(); gi_count++)
        {
            try
            {
                {
                    current_gi = (String)not_matching_gi_numbers.get(gi_count);
                    genBankSeq = gb.search(current_gi );
                    if (genBankSeq.isEmpty() ) continue;
                    for (int countGS = 0; countGS < genBankSeq.size(); countGS++)
                    {// human and not human have different format, human can come not first
                        current_gi = ((GenbankSequence)genBankSeq.get(countGS)).getGi();
                        if (current_gi == null || current_gi.equals("") ) continue;
                        seqData = gb.searchDetail(current_gi);
                        if (((String)seqData.get("species")).indexOf("sapiens") != -1)
                        {
                            fs = ms.createFlexSequence( seqData, genBankSeq);
                            continue;
                        }
                   }
                    if (fs == null)//can not create sequence
                    {
                        m_messages.add("Can not find sequence for MGC : " + current_gi);
                        continue;
                    }
                    sequences.put( current_gi, fs );
                    current_gi = null;
                   
                        
                }
            
        }catch(Exception e)
        {
            m_messages.add("Can not find sequence for GI : " + current_gi); 
            
        }
    }
        return true;
    }
    
    
        /* Function loops through all not matched sequences and blast them
         *against blastable db contains MGC clone sequences
         */
    
    private boolean blastSequences(Hashtable notMatchedSequences,
    ArrayList sequencesMatchedByBlast,
    ArrayList sequencesNotMatchedByBlast)
    {
        
        FlexSequence fc = null;
        FlexSeqAnalyzer fanalyzer = null;
        int seq_id = -1;
        
         for (Enumeration en = notMatchedSequences.elements() ; en.hasMoreElements() ;) 
         {

            try
            {
                fc = (FlexSequence) en.nextElement();
                fanalyzer = new FlexSeqAnalyzer(fc);

                seq_id = fanalyzer.findExactCdsMatch( BLASTABLE_DATABASE_NAME );
                if (seq_id != -1)//match found
                {
                    FlexSequence fc_restored = new FlexSequence(seq_id);
                    m_Request.addSequence(fc_restored);
                    sequencesMatchedByBlast.add(fc.getGi());
                }
                else
                {
                    sequencesNotMatchedByBlast.add(fc.getGi());
                }
                seq_id = -1;
            }catch(Exception e)
            {
                m_messages.add("Error gettting blast sequence for sequence id: " + seq_id);
                return false;
            }
        }
        
        return true;
    }
    
    //send e-mail to the user with all GI separated to three groups
    private void notifyUser(ArrayList requestGI, ArrayList seqMatchedByGI, ArrayList sequencesMatchedByBlast, ArrayList sequencesNotMatchedByBlast) throws Exception
    {
        AccessManager am = AccessManager.getInstance();
        String to = am.getEmail( m_UserName );
        String cc = "etaycher@hms.harvard.edu";
        String from = "etaycher@hms.harvard.edu";
        String subject = "User Notification: your request was uploaded";
        String msgText = null;
        
        msgText = "Request Id: " + m_Request.getId() + "\n";
        
        msgText += "\nGI numbers from request: \n";
        for (int count = 0; count< requestGI.size(); count++)
        {
            msgText += requestGI.get(count) + "\t";
        }
        
        msgText += "\nSequences matched to Mgc clones by GI number: \n";
        for (int count = 0; count< seqMatchedByGI.size(); count++)
        {
            msgText += seqMatchedByGI.get(count) + "\t";
        }
        msgText += "\nSequences matched to Mgc clones by blast: \n";
        for (int count = 0; count< sequencesMatchedByBlast.size(); count++)
        {
            msgText += sequencesMatchedByBlast.get(count) + "\t";
        }
        msgText += "\nSequences not matched to Mgc clones: \n";
        for (int count = 0; count< sequencesNotMatchedByBlast.size(); count++)
        {
            msgText += sequencesNotMatchedByBlast.get(count) + "\t";
        }
        
        
        //match thr GI, match thr cds sequence, not matched
        Mailer.sendMessage( to,  from,  cc, subject, msgText)  ;
    }
    
    
    //****************************Testing*******************************
    
    public static void main(String args[])
    {
        String file = "c:\\request.txt";
        InputStream input;
        
        try
        {
            input = new FileInputStream(file);
            
        } catch (FileNotFoundException ex)
        {
            System.out.println(ex);
            return;
        }
        DatabaseTransaction t = null;
        Connection conn = null;
        
        
        try
        {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            MgcRequestImporter importer = new MgcRequestImporter(new Project(5),new Workflow(7),"dzuo");
            importer.performImport(input,conn) ;
            
        }
         
            catch (Exception e) {}
            finally { DatabaseTransaction.closeConnection(conn); }
        System.exit(0);
    }
    
    
}