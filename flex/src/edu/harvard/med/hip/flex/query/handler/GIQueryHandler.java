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
public class GIQueryHandler {
    public static final String GENOMIC = "g";
    
    protected List noFoundList;
    protected List foundList;
    
    /** Creates a new instance of GIQueryHandler */
    public GIQueryHandler() {
    }
    
    public List getNoFoundList() {
        return noFoundList;
    }
    
    public List getFoundList() {
        return foundList;
    }
   
    /**
     * This method handles entire GI query workflow. It first querys Flex by calling
     * queryFlex() method. For GIs that are not found by queryFlex(), it gets sequences
     * from Flex or Genbank by GI number and blast against Flex. It populates foundList
     * and noFoundList. foundList contains a list of MatchGenbankRecord objects; noFoundList
     * contains a list of NoFound objects. It returns a list of GI numbers.
     *
     * @prarm queryList All the GI numbers for query.
     * @return A list of matched GI numbers.
     * @exception QueryException.
     * @exception FlexDatabaseException.
     * @exception SQLException.
     */    
   public List doQuery(List queryList) throws QueryException,FlexDatabaseException,SQLException {
        List matchList = queryFlex(queryList);
 //       List leftList = matchList.removeAll(matchList);
     /**       
            SeqBatchRetriever retriever = new FlexSeqBatchRetriever(leftList);
            matchList = retriever.retrieveSequence();
            List found = retriever.getFoundList();
//            leftList = leftList.removeAll(matchList);
            
                
                List blastList = new ArrayList();
                noFoundList = new ArrayList();
                for (int i=0; i<found.size(); i++) {
                    GiRecord gi = (GiRecord)found.get(i);
                    String type = gi.getType();
                    if(GENOMIC.equals(type)) {
                        NoFound nf = new NoFound((new Integer(gi.getGi())).toString(), NoFound.GENOMIC_SEQ);
                        noFoundList.add(nf);
                    } else {
                        blastList.add(gi);
                    }
                }
 
            retriever = new GenbankSeqBatchRetriever(leftList);
            matchList = retriever.retrieveSequence();
            blastList.addAll(retriever.getFoundList());
            noFoundList.addAll(retriever.getNoFoundList());
            
                    
                    BlastQueryHandler bhandler = new BlastQueryHandler();
 */                   
                    return null;

    }
    
    /**
     * Query flex database to see if the given GI in the list exists in database.
     * Populates foundList and noFoundList. foundList contains a list of MatchGenbankRecord
     * objects; nofoundList contains a list of NoFound objects. Return a list of GIs 
     * that exist in the database.
     *
     * @param queryList All the GI numbers for query.
     * @return List All the matched GI numbers.
     * @exception QueryException.
     * @exception FlexDatabaseException.
     * @exception SQLException.
     */
    public List queryFlex(List queryList) throws QueryException,FlexDatabaseException,SQLException {
        if (queryList == null) {
            throw new QueryException("Input parameter is null.");
        }
        
        noFoundList = new ArrayList();
        foundList = new ArrayList();
        List matchGiList = new ArrayList();
        
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
        
        for(int i=0; i<queryList.size(); i++) {
            String element = (String)queryList.get(i);
            stmt.setString(1, element);
            rs = DatabaseTransaction.executeQuery(stmt);
            
            MatchGenbankRecord mgr = null;
            while (rs.next()) {
                String genbank = rs.getString("GENBANK");
                String gi = rs.getString("GI");
                int sequenceid = rs.getInt("SEQUENCEID");
                
                if(mgr == null) {
                    mgr = new MatchGenbankRecord(genbank, gi, true, true);
                }
                
                MatchFlexSequence mfs = new MatchFlexSequence(true, sequenceid);
                mgr.addMatchFlexSequence(mfs);
            }
            if(mgr != null) {
                matchGiList.add(element);
                foundList.add(mgr);
            } else {
                NoFound nf = new NoFound(element, NoFound.GI_NOT_IN_FLEX);
                noFoundList.add(nf);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        return matchGiList;
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
    
    public static void main(String args[]) {
        List gis = new ArrayList();
        gis.add("1234567");
        gis.add("1235678");
        
        GIQueryHandler handler = new GIQueryHandler();
        
        
    }
}
