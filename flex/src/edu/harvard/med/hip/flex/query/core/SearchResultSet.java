/*
 * SearchResultSet.java
 *
 * Created on November 4, 2003, 10:10 AM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class SearchResultSet {
    protected List searchResults;
    
    /** Creates a new instance of SearchResultSet */
    public SearchResultSet(List searchResults) {
        this.searchResults = searchResults;
    }
    
    public List getSearchTerms() {
        List terms = new ArrayList();
        for (int i=0; i<searchResults.size(); i++) {
            SearchResult result = (SearchResult)searchResults.get(i);
            String term = result.getSearchTerm();
            terms.add(term);
        }
        return terms;
    }
    
    public static List getSearchTerms(List searchResults) {
        List terms = new ArrayList();
        for (int i=0; i<searchResults.size(); i++) {
            SearchResult result = (SearchResult)searchResults.get(i);
            String term = result.getSearchTerm();
            terms.add(term);
        }
        return terms;
    }
    
    public void persist(Connection conn, int searchid) throws FlexDatabaseException, SQLException {        
        if(searchResults == null || searchResults.size() == 0)
            return;
        
        String sql = "insert into searchresult(searchresultid, searchterm, searchid, isfound)"+
                    " values(?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<searchResults.size(); i++) {
            SearchResult result = (SearchResult)searchResults.get(i);
            stmt.setInt(1, result.getSearchResultid());
            stmt.setString(2, result.getSearchTerm());
            stmt.setInt(3, searchid);
            stmt.setString(4, result.getIsGenbankFound());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);        
    }
    
    public static void main(String args[]) {
        List searchResults = new ArrayList();
        searchResults.add(new SearchResult(1, "33469916", SearchResult.GENBANK_FOUND, null, null, 1));
        searchResults.add(new SearchResult(2, "21961206", SearchResult.GENBANK_NOT_FOUND, null, null, 1));
        SearchResultSet srs = new SearchResultSet(searchResults);
       
        for(int i=0; i<searchResults.size(); i++) {
            SearchResult sr = (SearchResult)searchResults.get(i);
            System.out.println(sr.getIsGenbankFound());
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            srs.persist(conn, 1);
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
