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

/**
 *
 * @author  DZuo
 */
public class ThreadedSearchManager extends SearchManager implements Runnable {
    Connection conn;
    
    /** Creates a new instance of ThreadedSearchManager */
    public ThreadedSearchManager(Connection conn, SearchRecord searchRecord, List params, List searchTerms) {
        super(searchRecord, params, searchTerms);
        this.conn = conn;
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
        doSearch();
        
        if(SearchRecord.COMPLETE.equals(searchRecord.getSearchStatus())) {
            try {
                updateSearchResults(conn);
            } catch (Exception ex) {
                error += "\nError occured while updating the database: "+ex.getMessage();
                searchRecord.setSearchStatus(SearchRecord.FAIL);
            }
        } else {
            error += "\nError occured while doing search.";
        }
        
        try {
            updateSearchRecord(conn);
        } catch (Exception ex) {
            error += "\nError occured while updating the database: "+ex.getMessage(); 
            searchRecord.setSearchStatus(SearchRecord.FAIL);
        }
        
        try {
            sendEmail();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
