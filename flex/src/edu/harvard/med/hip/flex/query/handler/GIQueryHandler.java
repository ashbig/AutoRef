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
import edu.harvard.med.hip.flex.query.bean.SearchDatabase;
import edu.harvard.med.hip.flex.export.FastaFileGenerator;
import edu.harvard.med.hip.flex.workflow.Project;
import edu.harvard.med.hip.flex.core.FlexSequence;

/**
 *
 * @author  dzuo
 */
public class GIQueryHandler extends QueryHandler {
    protected String db;
    
    public void setDb(String db) {this.db = db;}
    public String getDb() {return db;}
    
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
     *  foundList:      GiRecord => ArrayList of MatchFlexSequence objects
     *  noFoundList:    GiRecord => NoFound object
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
        
        String s = "";
        if(FastaFileGenerator.HUMANDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                " where genusspecies = 'Homo sapiens')";
        }        
        if(FastaFileGenerator.MGCDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from mgcclone)";
        }      
        if(FastaFileGenerator.PSEUDOMONASDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                " where genusspecies = 'Pseudomonas aeruginosa')";
        }     
        if(FastaFileGenerator.YEASTDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                " where genusspecies = 'Saccharomyces cerevisiae')";
        }   
        if(FastaFileGenerator.YPDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                " where genusspecies = 'Yersinia pestis')";
        }  
        if(FastaFileGenerator.BCDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from requestsequence"+
                " where projectid="+Project.BREASTCANCER+")";
        }
        if(FastaFileGenerator.NIDDKDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from requestsequence"+
                " where projectid="+Project.NIDDK+")";
        }
        if(FastaFileGenerator.CLONTECHDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from requestsequence"+
                " where projectid="+Project.CLONTECH+")";
        }
        if(FastaFileGenerator.RZPDWALLDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from requestsequence"+
                " where projectid="+Project.RZPD_WALL+")";
        }   
        if(FastaFileGenerator.FTDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                " where genusspecies = 'Francisella tularensis')";
        } 
        if(FastaFileGenerator.KINASEDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from requestsequence"+
                " where projectid="+Project.KINASE+")";
        }
        if(FastaFileGenerator.SEQVERIFIEDDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                "  where flexstatus='"+FlexSequence.OBTAINED+"')";
        }
        if(FastaFileGenerator.VERIFIEDBCDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select f.sequenceid from flexsequence f, requestsequence r"+
                "  where f.sequenceid=r.sequenceid"+
                " and r.projectid="+Project.BREASTCANCER+
                " and f.flexstatus='"+FlexSequence.OBTAINED+"')";
        }
        if(FastaFileGenerator.VERIFIEDKINASEDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select f.sequenceid from flexsequence f, requestsequence r"+
                "  where f.sequenceid=r.sequenceid"+
                " and r.projectid="+Project.KINASE+
                " and f.flexstatus='"+FlexSequence.OBTAINED+"')";
        }
        if(FastaFileGenerator.VERIFIEDHUMANDB.equals(db)) {
            s = " and g.sequenceid in "+
                " (select sequenceid from flexsequence"+
                "  where genusspecies = 'Homo sapiens'"+
                " and flexstatus='"+FlexSequence.OBTAINED+"')";
        }
            
        String sql = "select distinct b.namevalue as genbank,"+
        " g.namevalue as gi,"+
        " g.sequenceid as sequenceid"+
        " from genbankvu b, givu g"+
        " where b.sequenceid=g.sequenceid"+
        " and g.namevalue = ?"+s+
        " order by gi";
        String sql2 = "select namevalue from name where nametype='LOCUS_ID'"+
                    " and sequenceid=?";
        String sql3 = "select namevalue from name where nametype='UNIGENE_SID'"+
                    " and sequenceid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        
        for(int i=0; i<searchTerms.size(); i++) {
            String searchTerm = (String)searchTerms.get(i);
            stmt.setString(1, searchTerm);
            rs = DatabaseTransaction.executeQuery(stmt);
            
            List matchList = new ArrayList();
            String genbank = null;
            String locusid = null;
            String unigene = null;
            while (rs.next()) {
                genbank = rs.getString("GENBANK");
                String gi = rs.getString("GI");
                int sequenceid = rs.getInt("SEQUENCEID");
                               
                MatchFlexSequence mfs = new MatchFlexSequence(MatchFlexSequence.MATCH_BY_GI, sequenceid, null);
                matchList.add(mfs);
                
                stmt2.setInt(1, sequenceid);
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                if(rs2.next()) {
                    locusid = rs2.getString(1);
                }
                                
                stmt3.setInt(1, sequenceid);
                rs3 = DatabaseTransaction.executeQuery(stmt3);
                if(rs3.next()) {
                    locusid = rs3.getString(1);
                }
            }
            
            if(matchList.size() == 0) {
                NoFound nf = new NoFound(searchTerm, NoFound.GI_NOT_IN_FLEX);
                noFoundList.put(new GiRecord(searchTerm,genbank,null, locusid, unigene), nf);
            } else {
                foundList.put(new GiRecord(searchTerm,genbank,null, locusid, unigene), matchList);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);  
        DatabaseTransaction.closeResultSet(rs2);  
        DatabaseTransaction.closeResultSet(rs3);  
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
        DatabaseTransaction.closeConnection(conn);
    }
    
    protected void setQueryParams(List params) {
        db = BlastQueryHandler.DEFAULT_DB;
        
        if(params == null)
            return;
        
        for(int i=0; i<params.size(); i++) {
            Param p = (Param)params.get(i);
            if(Param.BLASTDB.equals(p.getName()) && p.getValue() != null && p.getValue().length()!=0) {
                setDb(SearchDatabase.getDbByName(p.getValue()));
            }
        }
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
            GiRecord gr = (GiRecord)iter.next();
            System.out.println("search term acc: "+gr.getGenbankAccession());
            System.out.println("search term gi: "+gr.getGi());
            List matchList = (List)found.get(gr);
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
            GiRecord gr = (GiRecord)iter.next();
            System.out.println("search term acc: "+gr.getGenbankAccession());
            System.out.println("search term gi: "+gr.getGi());
            NoFound nf = (NoFound)noFound.get(gr);
            System.out.println("\t"+nf.getSearchTerm());
            System.out.println("\t"+nf.getReason());
        }
        
        System.exit(0);
    }  
}
