/*
 * LocusSymbolBatchRetriever.java
 *
 * Created on February 23, 2004, 3:44 PM
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
public class LocusSymbolBatchRetriever extends GenbankBatchRetriever {
    
    /** Creates a new instance of LocusSymbolBatchRetriever */
    public LocusSymbolBatchRetriever() {
    }
    
    public LocusSymbolBatchRetriever(List symbols) {
        super(symbols);
    }
    
    /** Retrive the locus record from FLEXGene database for a list of gene symbols.
     * Populate foundList and noFoundList.
     *      foundList:      gene symbol => list of SequenceRecord object
     *      noFoundList:    gene symbol => NoFound object
     *
     * @exception Exception
     *
     */
    public void retrieveGenbank() throws Exception {        
        if(genbankList == null || genbankList.size() == 0) {
            return;
        }
        
        String sql = "select * from generecord where locusid in"+
                    " (select locusid from genesymbol where symbol=?)";
        doRetrieve(genbankList, sql);
    }
    
    private void doRetrieve(List genbankList, String sql) throws Exception {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        for(int i=0; i<genbankList.size(); i++) {
            String symbol = (String)genbankList.get(i);
            stmt.setString(1, symbol);
            rs = DatabaseTransaction.executeQuery(stmt);
            List matchs = new ArrayList();
            while(rs.next()) {
                String locusid = rs.getString(1);
                String isConfirmed = rs.getString(2);
                String organism = rs.getString(3);
                String status = rs.getString(4);
                String geneName = rs.getString(5);
                String unigene = rs.getString(6);
                GeneRecord gr = new GeneRecord(locusid,isConfirmed,organism,status,geneName,unigene);
                matchs.add(gr);
            }
            
            if(matchs.size()>0) {
                foundList.put(symbol, matchs);
            } else {
                NoFound nf = new NoFound(symbol, NoFound.SYMBOL_NOT_FOUND);
                noFoundList.put(symbol, nf);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);            
    }   

    public static void main(String args[]) {
        List symbols = new ArrayList();
        symbols.add("BCR1");
        symbols.add("CDK2");
        symbols.add("ABCDE");
        
        GenbankBatchRetriever retriever = new LocusSymbolBatchRetriever(symbols);
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
                    GeneRecord gr = (GeneRecord)matchs.get(i);
                    System.out.println("\tLocusID: "+gr.getLocusid());
                    System.out.println("\tIs confirmed: "+gr.getIsconfirmed());
                    System.out.println("\tStatus: "+gr.getStatus());
                    System.out.println("\tOrganism: "+gr.getOrganism());
                    System.out.println("\tgene name: "+gr.getGenename());
                    System.out.println("\tUnigene: "+gr.getUnigeneid());
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
