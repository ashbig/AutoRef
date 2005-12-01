/*
 * ResultProcessManager.java
 *
 * Created on October 11, 2005, 11:32 AM
 */

package plasmid.process;

import java.util.*;
import java.io.*;
import java.sql.*;

import plasmid.coreobject.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.*;
import plasmid.util.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class ResultProcessManager {
    
    /** Creates a new instance of ResultProcessManager */
    public ResultProcessManager() {
    }
    
    /**
     * Return a list of result types from the database.
     *
     * @return A list of result types.
     */
    public List getResultTypes() {
        DefTableManager manager = new DefTableManager();
        return manager.getVocabularies("resulttype", "resulttype");
    }
    
    public Map uploadAgarResults(InputStream input) throws Exception {/**
     * ColonyPickLogFileParser parser = new ColonyPickLogFileParser(input);
     * if(!parser.parseFile()) {
     * throw new Exception(parser.getErrorMessage());
     * }
     *
     * return parser.getColonyInfo();*/
        return null;
    }
    
    public List uploadCultureResults(InputStream input, Container container) throws Exception {
        CultureResultConvertor converter = new CultureResultConvertor(input, container.getSamples().size());
        if(!converter.parseFile()) {
            throw new Exception(converter.getErrorMessage());
        }
        
        if(!converter.convertResults()) {
            throw new Exception("Colony picking log file is empty");
        }
        
        return converter.getResultList();
    }
    
    public boolean persistData(ProcessExecution process, List results) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return false;
        }
        
        ProcessManager m = new ProcessManager(conn);
        if(!m.insertProcess(process)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot insert process.");
                System.out.println(m.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
        
        PlateManager m1 = new PlateManager(conn);
        if(!m1.updateSampleResults(results)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot update sample result.");
                System.out.println(m1.getErrorMessage());
            }
            DatabaseTransaction.rollback(conn);
            DatabaseTransaction.closeConnection(conn);
            return false;
        }
                
        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        return true;
    }
}
