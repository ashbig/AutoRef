/*
 * PublicGenbankBatchRetriever.java
 *
 * Created on October 21, 2003, 5:42 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;

import edu.harvard.med.hip.flex.util.GenbankGeneFinder;
import edu.harvard.med.hip.flex.core.GenbankSequence;
import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class PublicGenbankBatchRetriever extends GenbankBatchRetriever {
    
    /** Creates a new instance of PublicGenbankBatchRetriever */
    public PublicGenbankBatchRetriever() {
    }
    
    public PublicGenbankBatchRetriever(List genbankList) {
        super(genbankList);
    }
    
    /** 
     * Retrive the sequences from NCBI genbank for a list of genbank accession numbers.
     * Populate foundList and noFoundList.
     *      foundList:      accession => list of SequenceRecord object
     *      noFoundList:    accession => NoFound object
     *
     * @exception Exception
     *
     */
    public void retrieveGenbank() throws Exception {     
        if(genbankList == null || genbankList.size() == 0) {
            return;
        }
        
        GenbankGeneFinder finder = new GenbankGeneFinder();
        for(int i=0; i<genbankList.size(); i++) {
            String genbank = (String)genbankList.get(i);
            Vector ret = finder.search(genbank);
            List matchs = new ArrayList();
            for(int j=0; j<ret.size(); j++) {
                GenbankSequence seq = (GenbankSequence)ret.get(j);
                SequenceRecord sr = new SequenceRecord(seq.getAccession(), Integer.parseInt(seq.getGi()), 0, null);
                matchs.add(sr);
            } 
                        
            if(matchs.size()>0) {
                foundList.put(genbank, matchs);
            } else {
                NoFound nf = new NoFound(genbank, NoFound.ACCESSION_NOT_IN_NCBI);
                noFoundList.put(genbank, nf);
            }
        }
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
        
        GenbankBatchRetriever retriever = new PublicGenbankBatchRetriever(genbanks);
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
