/*
 * GIQueryHandler.java
 *
 * Created on March 17, 2003, 2:30 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.QueryException;

/**
 *
 * @author  dzuo
 */
public class GIQueryHandler extends QueryHandler {
    
    public GIQueryHandler() {super();}
    
    /** Creates a new instance of GIQueryHandler */
    public GIQueryHandler(List params) {
        super(params);
    }

    //convert a list of elements into the format of "(e1, e2, ...)".
    protected String createGiList(List l) {
        if (l.size() == 0)
            return null;
        
        String rt = "(";
        
        for(int i=0; i<l.size()-1; i++) {
            rt = rt+l.get(i)+",";
        }
        
        rt = rt+l.get(l.size()-1)+")";
        
        return rt;
    }
    
    public void handleQuery(List searchTerms) throws Exception {
        foundList = new HashMap();
        noFoundList = new HashMap();
        
        if (searchTerms == null) {
           return;
        }
        
        String sql = "select distinct b.namevalue as genbank,"+
        " g.namevalue as gi,"+
        " g.sequenceid as sequenceid"+
        " from genbankvu b, givu g"+
        " where b.sequenceid=g.sequenceid"+
        " and g.namevalue = ?"+
        " order by gi";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        for(int i=0; i<searchTerms.size(); i++) {
            String searchTerm = (String)searchTerms.get(i);
            stmt.setString(1, searchTerm);
            rs = DatabaseTransaction.executeQuery(stmt);
            
            List matchList = new ArrayList();
            while (rs.next()) {
                String genbank = rs.getString("GENBANK");
                String gi = rs.getString("GI");
                int sequenceid = rs.getInt("SEQUENCEID");
                               
                MatchFlexSequence mfs = new MatchFlexSequence(MatchFlexSequence.MATCH_BY_GI, sequenceid, null);
                matchList.add(mfs);
            }
            
            if(matchList.size() == 0) {
                NoFound nf = new NoFound(null, NoFound.GI_NOT_IN_FLEX);
                noFoundList.put(searchTerm, nf);
            } else {
                foundList.put(searchTerm, matchList);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);  
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
    }
    
    protected void setQueryParams(List params) {
    }   
}
