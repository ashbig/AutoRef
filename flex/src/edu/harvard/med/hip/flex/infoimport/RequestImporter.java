/*
 * RequestImporter.java
 *
 * This class imports the user requests into the database. It is usually used
 * with SequenceImporter and is called after import the sequences.
 *
 * Created on October 26, 2001, 1:04 PM
 */

package edu.harvard.med.hip.flex.infoimport;

import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.process.Request;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.user.User;
import java.util.*;
import java.io.*;
import java.sql.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class RequestImporter {
    public static final String DILIM = "\t!";
    public static final String DEFAULT = "NA";
    
    private Project project;
    private int successCount = 0;
    private int failCount = 0;
    private int totalCount = 0;
    private Hashtable requests;
    private Vector results;
    
    /** Creates new RequestImporter */
    public RequestImporter(Project project) {
        this.project = project;
        requests = new Hashtable();
        results = new Vector();
    }
    
    public void setRequests(Hashtable r) {this.requests = r;}
    
    /**
     * Import the requests into the database.
     *
     * @param requestInput The InputStream object containing the sequence requests.
     * @param sequenceResults The Vector containing the results of sequence import.
     * @param conn The Connection object for insert.
     * @return True if import successful; false otherwise.
     */
    public boolean performImport(InputStream requestInput, Vector sequenceResults, Connection conn) {
        if(sequenceResults == null) {
            return false;
        }
        
        if(!readRequest(requestInput, sequenceResults)) {
            return false;
        }
        
        return performImport(conn);
    }
    
    public boolean performImport(Connection conn) {
        Enumeration users = requests.keys();
        while(users.hasMoreElements()) {
            String user = (String)users.nextElement();
            Request request = (Request)requests.get(user);
            
            try {
                request.insert(conn);
            } catch (FlexDatabaseException ex) {
                System.out.println(ex);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Read the request information from the input stream.
     *
     * @param requestInput The InputStream object containing the sequence requests.
     * @param sequenceResults The result of the sequence import.
     * @return True if import successful; false otherwise.
     */
    public boolean readRequest(InputStream requestInput, Vector sequenceResults) {
        BufferedReader in = new BufferedReader(new InputStreamReader(requestInput));
        String line = null;
        
        try {
            while((line = in.readLine()) != null) {
                totalCount++;
                StringTokenizer st = new StringTokenizer(line, DILIM);
                String [] info = new String[2];
                int i = 0;
                
                try {
                    while(st.hasMoreTokens()) {
                        info[i] = st.nextToken();
                        
                        if(DEFAULT.equals(info[i].trim()))
                            info[i] = "";
                        
                        i++;
                    }
                } catch (NoSuchElementException ex) {}
                
                SequenceImporterLogger logger = found(sequenceResults, info[1]);
                if(logger == null || !logger.getSuccessful() || logger.getFlexid() == -1) {
                    RequestImporterLogger result = new RequestImporterLogger
                    (info[1], -1, false, "Sequence not found in the database.", info[0]);
                    results.addElement(result);
                    failCount++;
                    continue;
                }
                
                //validate user.
                if(!User.exists(info[0])) {
                    RequestImporterLogger result = new RequestImporterLogger
                    (info[1], logger.getFlexid(), false, "User not registered to FLEXGene", info[0]);
                    results.addElement(result);
                    failCount++;
                    continue;
                }
                
                Request request = (Request)requests.get(info[0]);
                if(request == null) {
                    request = new Request(info[0], project);
                }
                
                FlexSequence s = new FlexSequence(logger.getFlexid());
                request.addSequence(new FlexSequence(logger.getFlexid()));
                requests.put(info[0], request);
                RequestImporterLogger result = new RequestImporterLogger
                (info[1], logger.getFlexid(), true, null, info[0]);
                results.addElement(result);
                successCount++;
            }
            
            return true;
        }catch (IOException ex) {
            return false;
        }
    }
    
    /**
     * Return the result of the import.
     *
     * @return The result of the import.
     */
    public Vector getResult() {
        return results;
    }

    /**
     * Return the totalCount.
     *
     * @return The total count of the requested sequences.
     */
    public int getTotalCount() {
        return totalCount;
    }
    
    /**
     * Return the successful count.
     *
     * @return The successful count.
     */
    public int getSuccessfulCount() {
        return successCount;
    }
    
    /**
     * Return the failed count.
     *
     * @return The failed count.
     */
    public int getFailedCount() {
        return failCount;
    }
    
    //Find and return the match for a given sequenceid.
    private SequenceImporterLogger found(Vector loggers, String id) {
        SequenceImporterLogger ret = null;
        
        Enumeration enu = loggers.elements();
        while(enu.hasMoreElements()) {
            SequenceImporterLogger logger = (SequenceImporterLogger)enu.nextElement();
            if(id.equals(logger.getSequenceid())) {
                ret = logger;
                break;
            }
        }
        
        return ret;
    }
    
    public static void main(String [] args) {
        String sequenceFile = "C:\\TEMP\\FlexSequenceFile";
        String nameFile = "C:\\TEMP\\FlexNameFile";
        String requestFile = "C:\\TEMP\\FlexRequest";
        InputStream sequenceInput = null;
        InputStream nameInput = null;
        InputStream requestInput = null;
        Project project = null;
        
        try {
            sequenceInput = new FileInputStream(sequenceFile);
            nameInput = new FileInputStream(nameFile);
            requestInput = new FileInputStream(requestFile);
            project = new Project(1);
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            return;
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        }
        
        SequenceImporter importer = new SequenceImporter(project);
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            if(importer.performImport(sequenceInput, nameInput, conn)) {
                Vector results = importer.getResults();
                Enumeration enu = results.elements();
                while(enu.hasMoreElements()) {
                    SequenceImporterLogger logger = (SequenceImporterLogger)enu.nextElement();
                    System.out.println("Import Sequence: "+logger.getSequenceid()+"\t"+logger.getFlexid()+"\t"+logger.getSuccessful()+"\t"+logger.getMessage());
                }
                
                RequestImporter requestImporter = new RequestImporter(project);
                if(requestImporter.performImport(requestInput, results, conn)) {
                    DatabaseTransaction.commit(conn);
                    
                    Vector requestResults = requestImporter.getResult();
                    Enumeration en = requestResults.elements();
                    while(en.hasMoreElements()) {
                        RequestImporterLogger logger = (RequestImporterLogger)en.nextElement();
                        System.out.println("Request Import: "+logger.getSequenceid()+"\t"+logger.getFlexid()+"\t"+logger.getSuccessful()+"\t"+logger.getMessage()+"\t"+logger.getUsername());
                    }
                    System.out.println("Import finished.");
                } else {
                    DatabaseTransaction.rollback(conn);
                    System.out.println("Import aborted.");
                    
                }
            } else {
                DatabaseTransaction.rollback(conn);
                System.out.println("Import aborted.");
            }
        } catch (FlexDatabaseException ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}
