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
 * This class imports the user requests for Mgc clones into the database.
 *
 */

package edu.harvard.med.hip.flex.infoimport;

import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.process.Request;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.user.User;
import edu.harvard.med.hip.flex.util.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import sun.jdbc.rowset.*;
import javax.mail.*;


public class MgcRequestImporter {
    public static final String DILIM = "\t!";
    public static final String DEFAULT = "NA";
    public static final String BLASTABLE_DATABASE_NAME = "MGC/genes";
    
    private Project m_Project = null;
    private String m_UserName = null;
    private Request m_Request = null;
    private int m_SuccessCount = 0;
    private int m_FailCount = 0;
    private int m_TotalCountRequests = 0;
    
    
    
    /** Creates new MgcRequestImporter */
    public MgcRequestImporter(Project project, String user_name) {
        m_Project = project;
        m_UserName = user_name;
        m_Request = new Request(m_UserName, m_Project);
    }
    
    
    
    /**
     * Import the requests into the database.
     *
     * @param requestInput The InputStream object containing the sequence requests.
     * @param conn The Connection object for insert.
     * @return vector of sequences from request.
     */
    public Vector performImport(InputStream requestInput, Connection conn)
    throws Exception {
        
        Vector request_sequences = new Vector();
        ArrayList sequencesMatchedByBlast = new ArrayList();
        ArrayList sequencesNotMatchedByBlast = new ArrayList();
        //parse file and get list of GI;
        ArrayList requestGI = parseRequestFile( requestInput  ) ;
        //search db for the sequences( GI ) of MgcClones;
        //add matching sequences to request;
        ArrayList notMatchedGI = matchGINumbersToMgcClones(requestGI);
        //get sequence for not matching GI
        Hashtable sequencesToBlat = readSequences(notMatchedGI);
        //blast sequences for not matching GI;
        blastSequences(sequencesToBlat, sequencesMatchedByBlast, sequencesNotMatchedByBlast);
        //save request to db
        m_Request.insert(conn);
        notifyUser(requestGI, sequencesMatchedByBlast, sequencesNotMatchedByBlast);
        //parse results for not matching GI
        return m_Request.getSequences();
        
    }
    
    
    /*Function parses request file and returns list of GI numbers
     */
    private ArrayList  parseRequestFile(InputStream requestInput) {
        BufferedReader in = new BufferedReader(new InputStreamReader(requestInput));
        String line = null;
        ArrayList gi_numbers = new ArrayList();
        
        try {
            while((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, DILIM);
                while(st.hasMoreTokens()) {
                    gi_numbers.add( st.nextToken().trim() );
                }
            }
        }
        catch(Exception e) {}
        m_TotalCountRequests = gi_numbers.size();
        return gi_numbers;
    }
    
     /* Function 1. queries database for GI numbers of imported MGC clones
      *2. tries to match GI numbers from request to the one in DB
      * 3. adds matching sequences to  Request
      *@return ArrayList of not matched GI numbers
      */
    private  ArrayList  matchGINumbersToMgcClones(ArrayList requestGI) {
        ArrayList not_matching_gi_numbers = new ArrayList();
        String sql = "select n.namevalue as gi, n.sequenceid as sequence_id "+
        "from  mgcclone mc , name n \n" +
        "where ( mc.sequenceid = n.sequenceid and n.nametype = 'GI' and mc.sequenceid <> NULL ) ";
        int current_gi = 0;
        int current_seq_id = 0;
        CachedRowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            
            if(crs.size()==0) {
                //write to log file
            }
            
            while(crs.next()) {
                current_seq_id = crs.getInt("SEQUENCE_ID");
                current_gi = crs.getInt("GI");
                if ( requestGI.contains( Integer.toString(current_gi)) ) {//create dummy flexsequence and add it to request
                    FlexSequence fc = new FlexSequence( current_seq_id, FlexSequence.GOOD, null, null, null, 0, 0, 0, 0, null, null, null);
                    m_Request.addSequence(fc);
                    requestGI.remove( requestGI.indexOf( Integer.toString(current_gi) ) );
                }
                else {
                    not_matching_gi_numbers.add(Integer.toString(current_gi));
                }
            }
            
        } catch (Exception e) {
            // throw new FlexDatabaseException("Error occured while  " +sql);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        return not_matching_gi_numbers;
    }
    
    
    
     /* Function queries ncbi for sequences for GI that was not matched to DB
      * @param ArrayList of GI numbers
      *@return Hashtable of FlexSequences , key - GI
      */
    private Hashtable  readSequences(ArrayList not_matching_gi_numbers) {
        Hashtable sequences = new Hashtable();
        GenbankGeneFinder gb = new GenbankGeneFinder();
        Vector genBankSeq = new Vector();
        Hashtable seqData = new Hashtable();
        String current_gi = null;
        MgcMasterListImporter ml = new MgcMasterListImporter();
        Vector gi_data = new Vector();
        try{
            for (int gi_count = 0; gi_count < not_matching_gi_numbers.size(); gi_count++) {
                {
                    current_gi = (String)not_matching_gi_numbers.get(gi_count);
                    seqData = gb.searchDetail( current_gi );
                    gi_data.add(current_gi);
                    FlexSequence fs = ml.createFlexSequence( seqData, gi_data);
                    sequences.put( current_gi, fs );
                    current_gi = null;
                }
            }
        }catch(Exception e) {System.out.println("Ncbi search: can not get sequence for GI: " + current_gi ); }
        return sequences;
    }
    
    
        /* Function loops through all not matched sequences and blast them
         *against blastable db contains MGC clone sequences
         */
    
    private boolean blastSequences(Hashtable notMatchedSequences,
    ArrayList sequencesMatchedByBlast,
    ArrayList sequencesNotMatchedByBlast) {
        Enumeration en = notMatchedSequences.keys();
        FlexSequence fc = null;
        FlexSeqAnalyzer fanalyzer = null;
        int seq_id = -1;
        try{
            while (en.hasMoreElements()) {
                fc = (FlexSequence) en.nextElement();
                fanalyzer = new FlexSeqAnalyzer(fc);
                
                seq_id = fanalyzer.findExactCdsMatch( BLASTABLE_DATABASE_NAME );
                if (seq_id != -1)//match found
                {
                    fc.setId(seq_id);
                    m_Request.addSequence(fc);
                    sequencesMatchedByBlast.add(fc.getGi());
                }
                else {
                    sequencesNotMatchedByBlast.add(fc.getGi());
                }
                seq_id = -1;
                
            }
        }catch(Exception e){return false;}
        return true;
    }
    
    //send e-mail to the user with all GI separated to three groups
    private void notifyUser(ArrayList requestGI, ArrayList sequencesMatchedByBlast, ArrayList sequencesNotMatchedByBlast) throws MessagingException {
        String to = "etaycher@hms.harvard.edu";
        String from = "etaycher@hms.harvard.edu";
        String subject = "User Notification: your request was uploaded";
        String msgText = null;
        
        msgText = "Sequences matched to Mgc clones by GI number: \n";
        for (int count = 0; count< requestGI.size(); count++) {
            msgText = requestGI.get(count) + "\t";
        }
        for (int count = 0; count< sequencesMatchedByBlast.size(); count++) {
            msgText = sequencesMatchedByBlast.get(count) + "\t";
        }
        for (int count = 0; count< sequencesNotMatchedByBlast.size(); count++) {
            msgText = sequencesNotMatchedByBlast.get(count) + "\t";
        }
        
        
        //match thr GI, match thr cds sequence, not matched
        Mailer.sendMessage( to,  from,  subject, msgText)  ;
    }
    
    
}