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
    
    /**
     * Query FLEXGene database for a list of GI numbers and populate foundList and
     * noFoundList. 
     *  foundList:      gi => ArrayList of MatchFlexSequence objects
     *  noFoundList:    gi => NoFound object
     *
     * @param searchTerms A list of GI numbers as search terms.
     * @exception Exception
     */
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
                NoFound nf = new NoFound(searchTerm, NoFound.GI_NOT_IN_FLEX);
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
    
    public static void main(String args[]) {
        List searchTerms = new ArrayList();
        searchTerms.add("10835090");
        searchTerms.add("4502422");
        searchTerms.add("16306536");
        searchTerms.add("4557368");
        searchTerms.add("4508004");
        searchTerms.add("5453803");
        searchTerms.add("4502420");
        searchTerms.add("1234");
        searchTerms.add("2345");

        GIQueryHandler handler = new GIQueryHandler();
        try {
            handler.handleQuery(searchTerms);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        }
        
        Map found = handler.getFoundList();
        Map noFound = handler.getNoFoundList();
        
        System.out.println("========== Found ==========");
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            System.out.println("search term: "+term);
            List matchList = (List)found.get(term);
            for(int i=0; i<matchList.size(); i++) {
                MatchFlexSequence mfs = (MatchFlexSequence)matchList.get(i);
                System.out.println("\t"+mfs.getFlexsequenceid());
                System.out.println("\t"+mfs.getIsMatchByGi());
                System.out.println("\t"+mfs.getMatchGenbankId());
                BlastHit bh = mfs.getBlastHit();
                if(bh == null) {
                    System.out.println("\tNo blast");
                }
            }
        }
        
        System.out.println("========== No Found ==========");
        keys = noFound.keySet();
        iter = keys.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            System.out.println("search term: "+term);
            NoFound nf = (NoFound)noFound.get(term);
            System.out.println("\t"+nf.getSearchTerm());
            System.out.println("\t"+nf.getReason());
        }
        
        System.exit(0);
    }  
}
