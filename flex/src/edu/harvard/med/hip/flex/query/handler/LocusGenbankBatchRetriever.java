/*
 * LocusGenbankBatchRetriever.java
 *
 * Created on February 19, 2004, 5:58 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class LocusGenbankBatchRetriever extends GenbankBatchRetriever {
    
    /** Creates a new instance of LocusGenbankBatchRetriever */
    public LocusGenbankBatchRetriever() {
    }
    
    public LocusGenbankBatchRetriever(List locusList) {
        super(locusList);
    }
    
    /** Retrive the Genbank record from FLEXGene database for a list of locus ID numbers.
     * Populate foundList and noFoundList.
     *      foundList:      locus ID => list of SequenceRecord object
     *      noFoundList:    locus ID => NoFound object
     *
     * @exception Exception
     *
     */
    public void retrieveGenbank() throws Exception {        
        if(genbankList == null || genbankList.size() == 0) {
            return;
        }
        
        String sql = "select * from sequencerecord where locusid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        for(int i=0; i<genbankList.size(); i++) {
            String locusid = (String)genbankList.get(i);
            stmt.setString(1, locusid);
            rs = DatabaseTransaction.executeQuery(stmt);
            List matchs = new ArrayList();
            while(rs.next()) {
                String accession = rs.getString(1);
                String gi = rs.getString(2);
                String locus = rs.getString(3);
                String type = rs.getString(4);
                SequenceRecord sr = new SequenceRecord(accession, gi, locus, type);
                matchs.add(sr);
            }
            
            if(matchs.size()>0) {
                foundList.put(locusid, matchs);
            } else {
                NoFound nf = new NoFound(locusid, NoFound.LOCUSID_NOT_FOUND);
                noFoundList.put(locusid, nf);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);            
    }
    
    public static void main(String args[]) {
        List locusList = new ArrayList();
        locusList.add("1012");
        
        GenbankBatchRetriever retriever = new LocusGenbankBatchRetriever(locusList);
        try {
            retriever.retrieveGenbank();
            Map founds = retriever.getFoundList();
            
            System.out.println("================ Found ================");
            Set terms = founds.keySet();
            Iterator iter = terms.iterator();
            while(iter.hasNext()) {
                String term = (String)iter.next();
                System.out.println("Search Term: "+term);
                
                List matchs = (List)founds.get(term);
                for(int i=0; i<matchs.size(); i++) {
                    SequenceRecord sr = (SequenceRecord)matchs.get(i);
                    System.out.println("\tAccession: "+sr.getGenbank());
                    System.out.println("\tGi: "+sr.getGi());
                    System.out.println("\tLocus: "+sr.getLocusid());
                    System.out.println("\tType: "+sr.getType());
                }
            }
            
            Map noFounds = retriever.getNoFoundList();
            System.out.println("=============== No Found ================");
            terms = noFounds.keySet();
            iter = terms.iterator();
            while(iter.hasNext()) {
                String term = (String)iter.next();
                System.out.println("Search Term: "+term);
                
                NoFound nf = (NoFound)noFounds.get(term);
                System.out.println("\tterm: "+nf.getSearchTerm());
                System.out.println("\treason: "+nf.getReason());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        System.exit(0);
    }    
}
