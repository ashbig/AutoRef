/*
 * FlexGenbankBatchRetriever.java
 *
 * Created on October 21, 2003, 5:42 PM
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
public class FlexGenbankBatchRetriever extends GenbankBatchRetriever {
    
    /** Creates a new instance of FlexGenbankBatchRetriever */
    public FlexGenbankBatchRetriever() {
    }
    
    public FlexGenbankBatchRetriever(List genbankList) {
        super(genbankList);
    }
    
    /** 
     * Retrive the sequences from FLEXGene database for a list of genbank accession numbers.
     * Populate foundList and noFoundList.
     *      foundList:      accession => list of SequenceRecord object
     *      noFoundList:    accession => NoFound object
     *
     * @exception Exception
     *
     */
    public void retrieveGenbank() throws Exception {     
        String sql = "select * from sequencerecord where accession=?";
        doRetrieve(genbankList, sql);
    }
  
    public void retrieveRelatedGenbank(List genbanks) throws Exception {        
        String sql = "select * from sequencerecord where locusid = "+
                    " (select locusid from sequencerecord where accession=?)";
        doRetrieve(genbankList, sql);
    }    
      
    public void retrieveRelatedCodingGenbank(List genbanks) throws Exception {        
        String sql = "select * from sequencerecord where locusid = "+
                    " (select locusid from sequencerecord where accession=?)"+
                    " and type in ('m', 'e')";
        doRetrieve(genbankList, sql);
    } 

    /** 
     * Retrive the sequences from FLEXGene database for a list of genbank accession numbers.
     * Populate foundList and noFoundList.
     *      foundList:      accession => list of SequenceRecord object
     *      noFoundList:    accession => NoFound object
     *
     * @exception Exception
     *
     */    
    protected void doRetrieve(List genbanks, String sql) throws Exception {
        if(genbanks == null || genbanks.size() == 0) {
            return;
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        t = DatabaseTransaction.getInstance();
        conn = t.requestConnection();
        stmt = conn.prepareStatement(sql);
        
        for(int i=0; i<genbanks.size(); i++) {
            String genbank = (String)genbanks.get(i);
            stmt.setString(1, genbank);
            rs = DatabaseTransaction.executeQuery(stmt);
            List matchs = new ArrayList();
            while(rs.next()) {
                String accession = rs.getString(1);
                String gi = rs.getString(2);
                int locus = rs.getInt(3);
                String type = rs.getString(4);
                SequenceRecord sr = new SequenceRecord(accession, gi, locus, type);
                matchs.add(sr);
            }
            
            if(matchs.size()>0) {
                foundList.put(genbank, matchs);
            } else {
                NoFound nf = new NoFound(genbank, NoFound.ACCESSION_NOT_IN_FLEX);
                noFoundList.put(genbank, nf);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);        
    }     
    
    public static void main(String args[]) {
        List genbanks = new ArrayList();
        genbanks.add("NM_130786");           
        genbanks.add("AC010642");            
        genbanks.add("AF414429");            
        genbanks.add("AK055885");             
        genbanks.add("X68728");              
        genbanks.add("Z11711"); 
        genbanks.add("BC001874.1");                                                                      
        genbanks.add("BC001875.1");                                                                      
        genbanks.add("BC001878.1");                                                                      
        genbanks.add("BC001880.1");                                                                      
        genbanks.add("BC001881.1");                                                                      
        genbanks.add("BC001882.1");
        genbanks.add("12345");
        genbanks.add("abc");
        
        GenbankBatchRetriever retriever = new FlexGenbankBatchRetriever(genbanks);
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
