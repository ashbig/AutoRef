/*
 * ThreadedSearchManager.java
 *
 * Created on November 6, 2003, 11:47 AM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.lang.*;
import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.user.User;

/**
 *
 * @author  DZuo
 */
public class ThreadedSearchManager extends SearchManager implements Runnable {
    
    /** Creates a new instance of ThreadedSearchManager */
    public ThreadedSearchManager(SearchRecord searchRecord, List params, List searchTerms) {
        super(searchRecord, params, searchTerms);
    }
    
    /** When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     java.lang.Thread#run()
     *
     */
    public void run() {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
        
        try {
            insertSearchRecord(conn);
            DatabaseTransaction.commit(conn);
            if(doSearch()) {
                try {
                    insertSearchResults(conn);
                    DatabaseTransaction.commit(conn);
                } catch (Exception ex) {
                    DatabaseTransaction.rollback(conn);
                    error += "\nError occured while updating the database: "+ex.getMessage();
                    searchRecord.setSearchStatus(SearchRecord.FAIL);
                }
            } else {
                error += "\nError occured while doing search.";
                searchRecord.setSearchStatus(SearchRecord.FAIL);
            }
            
            try {
                updateSearchRecord(conn, searchRecord.getSearchStatus());
                DatabaseTransaction.commit(conn);
            } catch (Exception ex) {
                DatabaseTransaction.rollback(conn);
                error += "\nError occured while updating the database: "+ex.getMessage();
                searchRecord.setSearchStatus(SearchRecord.FAIL);
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            error += "\nError occured while updating the database: "+ex.getMessage();
            searchRecord.setSearchStatus(SearchRecord.FAIL);
        }
        
        try {
            sendEmail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static void main(String args[]) {
        SearchRecord searchRecord = new SearchRecord("Test search", SearchRecord.GI, SearchRecord.INPROCESS, "dzuo");
        List searchTerms = new ArrayList();
        searchTerms.add("33469916");
        searchTerms.add("21961206");
        searchTerms.add("33469967");
        searchTerms.add("1234");
        searchTerms.add("345");
        
        ThreadedSearchManager manager = new ThreadedSearchManager(searchRecord, null, searchTerms);
        new Thread(manager).start();
    }
}
