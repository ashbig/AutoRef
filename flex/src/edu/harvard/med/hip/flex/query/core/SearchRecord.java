/*
 * SearchRecord.java
 *
 * Created on March 13, 2003, 2:25 PM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class SearchRecord {
    public static final String GI = "GI";
    public static final String GENBANK = "GENBANK";
    public static final String GENESYMBOL = "GENESYMBOL";
    public static final String LOCUSID = "LOCUSID";
    
    public static final String INPROCESS = "IN PROCESS";
    public static final String COMPLETE = "COMPLETE";
    public static final String FAIL = "FAIL";
    
    private int searchid;
    private String searchName;
    private String searchDate;
    private String searchType;
    private String searchStatus;
    private String username;
    
    /** Creates a new instance of SearchRecord */
    public SearchRecord() {
    }
    
    public SearchRecord(String searchName, String searchType, String searchStatus, String username) {
        this.searchName = searchName;
        this.searchType = searchType;
        this.searchStatus = searchStatus;
        this.username = username;
    }
    
    public void setSearchid(int searchid) {
        this.searchid = searchid;
    }
    
    public int getSearchid() {
        return searchid;
    }
    
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    
    public String getSearchName() {
        return searchName;
    }
    
    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }
    
    public String getSearchDate() {
        return searchDate;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
    
    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchStatus(String s) {
        this.searchStatus = s;
    }
    
    public String getSearchStatus() {
        return searchStatus;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void persist(Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into search (searchid, searchname, searchdate, searchtype, searchstatus, username)"+
        " values (?, ?, sysdate, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, searchid);
        stmt.setString(2, searchName);
        stmt.setString(3, searchType);
        stmt.setString(4, searchStatus);
        stmt.setString(5, username);
        DatabaseTransaction.executeUpdate(stmt);
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public void updateStatus(Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "update search set searchstatus=? where searchid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, searchStatus);
        stmt.setInt(2, searchid);
        DatabaseTransaction.executeUpdate(stmt);
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public static void main(String args[]) {
        SearchRecord sr = new SearchRecord("Test1", SearchRecord.GI, SearchRecord.INPROCESS, "dzuo");
        System.out.println("searchName: "+sr.getSearchName());
        System.out.println("searchType: "+sr.getSearchType());
        System.out.println("searchStatus: "+sr.getSearchStatus());
        System.out.println("username: "+sr.getUsername());
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            sr.setSearchid(1);
            sr.persist(conn);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}
