/*
 * FlexSeqBatchRetriever.java
 *
 * Created on March 18, 2003, 4:32 PM
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
public class FlexSeqBatchRetriever extends SeqBatchRetriever {       
    public FlexSeqBatchRetriever(List giList) {
        super(giList);
    }
    
    /**
     * Retrive the sequences from FLEXGene database for a list of GI numbers.
     *  Populate foundList and noFoundList. 
     *      foundList:      GI => GiRecord object
     *      noFoundList:    GI => NoFound object
     *
     * @exception Exception
     */
    public void retrieveSequence() throws Exception {
        if(giList == null || giList.size() == 0) {
            return;
        }
        
        String sql = "select * from girecord where gi=?";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        for(int i=0; i<giList.size(); i++) {
            String element = (String)giList.get(i);
            
            try {
                stmt.setInt(1, Integer.parseInt(element));
                rs = DatabaseTransaction.executeQuery(stmt);
            } catch (NumberFormatException ex) {
                NoFound nf = new NoFound(element, NoFound.INVALID_GI);
                noFoundList.put(element,  nf);
                continue;
            }
                        
            if (rs.next()) {
                String genbank = rs.getString("ACCESSION");
                String gi = rs.getString("GI");
                String sequenceFile = rs.getString("SEQUENCEFILE");
                GiRecord giRecord = new GiRecord(gi, genbank, sequenceFile);
                giRecord.setCdsStart(rs.getInt("CDSSTART"));
                giRecord.setCdsStop(rs.getInt("CDSSTOP"));
                foundList.put(element, giRecord);
            } else {
                NoFound nf = new NoFound(element, NoFound.GI_NOT_IN_FLEX);
                noFoundList.put(element, nf);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
    }
    
    public static void main(String args[]) {
        List giList = new ArrayList();
        giList.add("32450632");
        giList.add("21961206");
        giList.add("33869456");
        giList.add("33469967");
        giList.add("33469918");
        giList.add("33469916");
        giList.add("16936529");
        giList.add("37550355");
        giList.add("16923985");
        giList.add("34851998");
        
        SeqBatchRetriever retriever = new FlexSeqBatchRetriever(giList);
        
        try {
            retriever.retrieveSequence();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        Map founds = retriever.getFoundList();
        System.out.println("Found List: ");
        Set keys = founds.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            GiRecord gr = (GiRecord)founds.get(key);
            System.out.println("\t"+key+"\t"+gr.getCdsStart()+"\t"+gr.getCdsStop()+"\t"+gr.getGenbankAccession()+"\t"+gr.getSequenceFile());
        }
        
        Map noFounds = retriever.getNoFoundList();
        System.out.println("\nNo Found:");
        keys = noFounds.keySet();
        iter = keys.iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            NoFound nf = (NoFound)noFounds.get(key);
            System.out.println("\t"+key+"\t"+nf.getReason());
        }
    }    
}
