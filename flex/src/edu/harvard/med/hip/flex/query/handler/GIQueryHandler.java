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

/**
 *
 * @author  dzuo
 */
public class GIQueryHandler {
    protected List noFoundList;
    protected List matchGenbankList;
    protected String error;
    
    /** Creates a new instance of GIQueryHandler */
    public GIQueryHandler() {
    }
    
    public List getNoFoundList() {
        return noFoundList;
    }
    
    public List getMatchGenbankList() {
        return matchGenbankList;
    }
    
    public String getError() {
        return error;
    }
    
    public boolean queryFlex(List queryList) {
        if (queryList == null) {
            error = "Input parameter is null.";
            return false;
        }
        
        noFoundList = new ArrayList();
        matchGenbankList = new ArrayList();
        noFoundList.addAll(queryList);
        List foundList = new ArrayList();
        String sql = "select distinct b.namevalue as genbank,"+
        " g.namevalue as gi,"+
        " g.sequenceid as sequenceid"+
        " from genbankvu b, givu g"+
        " where b.sequenceid=g.sequenceid"+
        " and g.namevalue = ?"+
        " order by gi";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<noFoundList.size()-1; i++) {
                String element = (String)(noFoundList.get(i));
                stmt.setString(1, element);
                rs = DatabaseTransaction.executeQuery(stmt);
                
                MatchGenbankRecord mgr = null;
                while (rs.next()) {
                    String genbank = rs.getString("GENBANK");
                    String gi = rs.getString("GI");
                    int sequenceid = rs.getInt("SEQUENCEID");
                    
                    if(mgr == null) {
                        mgr = new MatchGenbankRecord(genbank, gi, true, true);
                        foundList.add(element);
                    }
                    
                    MatchFlexSequence mfs = new MatchFlexSequence(true, sequenceid);
                    mgr.addMatchFlexSequence(mfs);
                }
                matchGenbankList.add(mgr);
            }
            
            noFoundList.removeAll(foundList);
            
            return true;
        } catch (FlexDatabaseException ex) {
            error = ex.getMessage();
            return false;
        } catch (SQLException e) {
            error = e.getMessage();
            return false;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
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
}
