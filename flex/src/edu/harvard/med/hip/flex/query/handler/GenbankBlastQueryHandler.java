/*
 * GenbankBlastQueryHandler.java
 *
 * Created on October 22, 2003, 11:13 AM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;

import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class GenbankBlastQueryHandler extends QueryHandler {
    private boolean isRelatedSeq = false;
    private QueryHandler giBlastQueryHandler;
    
    /** Creates a new instance of GenbankBlastQueryHandler */
    public GenbankBlastQueryHandler(List params) {
        super(params);
        giBlastQueryHandler = new GiBlastQueryHandler(params);
    }
    
    /**
     * Query FLEXGene database for a list of genbank accession numbers and populate foundList and
     * noFoundList. It first query FLEXGene database or NCBI to get lists of SequenceRecord objects
     * which contains GI numbers. It then use GiBlastQueryHandler to query for a list of GI numbers.
     *
     *  foundList:      Genbank Accession => ArrayList of MatchGenbankRecord objects
     *  noFoundList:    Genbank Accession => NoFound object
     *
     * @param searchTerms A list of GI numbers as search terms.
     * @exception Exception
     */    
    public void handleQuery(List searchTerms) throws Exception {
        foundList = new Hashtable();
        noFoundList = new Hashtable();
        
        if(searchTerms == null || searchTerms.size() == 0) {
            return;
        }
        
        //search FLEXGene database to get all gene records.
        GenbankBatchRetriever retriever = new FlexGenbankBatchRetriever(searchTerms);
        retriever.retrieveGenbank();
        Map founds = retriever.getFoundList();
        Map noFounds = retriever.getNoFoundList();
        
        //put terms not found into a list
        Set noFoundTerms = noFounds.keySet();
        List noFoundTermsList = new ArrayList();
        noFoundTermsList.addAll(noFoundTerms);
        
        //search NCBI to get all gene records for terms not found in FLEXGene database.
        retriever = new PublicGenbankBatchRetriever(noFoundTermsList);
        retriever.retrieveGenbank();
        founds.putAll(retriever.getFoundList());
        noFoundList.putAll(retriever.getNoFoundList());
        
        //get all the GI numbers for the found gene records.
        Set giTerms = new TreeSet();
        Collection values = founds.values();
        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            List matchs = (List)iter.next();
            for(int i=0; i<matchs.size(); i++) {
                SequenceRecord sr = (SequenceRecord)matchs.get(i);
                String gi = sr.getGi();
                giTerms.add(gi);
            }
        }
        List giTermsList = new ArrayList();
        giTermsList.addAll(giTerms);
        
        //Query by GI terms
        giBlastQueryHandler.handleQuery(giTermsList);
        
        //handle found.
        Map foundByGi = giBlastQueryHandler.getFoundList();
        Set terms = founds.keySet();
        iter = terms.iterator();
        Map leftTerms = new HashMap();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            List matchs = (List)founds.get(term);
            List matchGenbankRecords = new ArrayList();
            for(int i=0; i<matchs.size(); i++) {
                SequenceRecord sr = (SequenceRecord)matchs.get(i);
                String gi = sr.getGi();
                if(foundByGi.containsKey(gi)) {
                    List mgrs = (List)foundByGi.get(gi);
                    
                    for(int n=0; n<mgrs.size(); n++) {
                        MatchGenbankRecord mgr = (MatchGenbankRecord)mgrs.get(n);
                        if(sr.getLocusid() != null) {
                            mgr.setLocusid(sr.getLocusid());
                        }
                    }
                    
                    matchGenbankRecords.addAll(mgrs);
                } 
            }
            if(matchGenbankRecords.size() > 0) {
                foundList.put(term, matchGenbankRecords);
            } else {
                leftTerms.put(term, matchs);
            }
        }

        //handle no found
        Map noFoundByGi = giBlastQueryHandler.getNoFoundList();
        terms = leftTerms.keySet();
        iter = terms.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            List matchs = (List)leftTerms.get(term);
            String reason = NoFound.NO_MATCH_GI_QUERY;
            for(int i=0; i<matchs.size(); i++) {
                SequenceRecord sr = (SequenceRecord)matchs.get(i);
                String gi = sr.getGi();
                if(noFoundByGi.containsKey(gi)) {
                    NoFound nf = (NoFound)noFoundByGi.get(gi);
                    reason = reason+". Query by genbank accession: "+sr.getGenbank()+" - "+nf.getReason();
                } else {
                    reason = reason+". Query by genbank accession: "+sr.getGenbank()+" - "+NoFound.UNKNOWN;
                }
            }
            noFoundList.put(term, new NoFound(term, reason));
            reason = NoFound.NO_MATCH_GI_QUERY;
        }
    }    
    
    protected void setQueryParams(List params) {
        if(params == null || params.size() == 0)
            return;
        
        for(int i=0; i<params.size(); i++) {
            Param p = (Param)params.get(i);
            if(Param.ISRELATEDSEQ.equals(p.getName()) && p.getValue() != null && p.getValue().length()!=0) {
                if(Param.ISRELATEDSEQ_YES.equals(p.getValue().toString())) {
                    isRelatedSeq = true;
                } else {
                    isRelatedSeq = false;
                }
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
        
        List params = new ArrayList();
        
        GenbankBlastQueryHandler handler = new GenbankBlastQueryHandler(params);
        try {
            handler.handleQuery(genbanks);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        }
        
        Map found = handler.getFoundList();
        System.out.println("=========== Found =============");
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String gi = (String)iter.next();
            System.out.println("search term: "+gi);
            
            List mgrs = (ArrayList)found.get(gi);
            for(int i=0; i<mgrs.size(); i++) {
                MatchGenbankRecord mgr = (MatchGenbankRecord)mgrs.get(i);
                System.out.println("Genbank Acc: "+mgr.getGanbankAccession());
                System.out.println("GI: "+mgr.getGi());
                System.out.println("Search Method: "+mgr.getSearchMethod());
                List mfss = (List)mgr.getMatchFlexSequence();
                
                for(int n=0; n<mfss.size(); n++) {
                    MatchFlexSequence mfs = (MatchFlexSequence)mfss.get(n);
                    System.out.println("\tFlex ID: "+mfs.getFlexsequenceid());
                    System.out.println("\tIs match by GI: "+mfs.getIsMatchByGi());
                    BlastHit bh = mfs.getBlastHit();
                    if(bh != null) {
                        System.out.println("\t\tFlex ID: "+bh.getMatchFlexId());
                        System.out.println("\t\tQuery length: "+bh.getQueryLength());
                        System.out.println("\t\tSub length: "+bh.getSubjectLength());
                        
                        List alignments = bh.getAlignments();
                        for(int j=0; j<alignments.size(); j++) {
                            BlastAlignment ba = (BlastAlignment)alignments.get(j);
                            System.out.println("\t\t\tE value: "+ba.getEvalue());
                            System.out.println("\t\t\tGap: "+ba.getGap());
                            System.out.println("\t\t\tID: "+ba.getId());
                            System.out.println("\t\t\tIdentity: "+ba.getIdentity());
                            System.out.println("\t\t\tFlex ID: "+ba.getMatchFlexId());
                            System.out.println("\t\t\tQuery start: "+ba.getQueryStart());
                            System.out.println("\t\t\tQuery end: "+ba.getQueryEnd());
                            System.out.println("\t\t\tScore: "+ba.getScore());
                            System.out.println("\t\t\tStrand: "+ba.getStrand());
                            System.out.println("\t\t\tSub start: "+ba.getSubStart());
                            System.out.println("\t\t\tSub end: "+ba.getSubEnd());
                        }
                    } else {
                        System.out.println("\tNo blast hits");
                    }
                }
            }
        }
        
        System.out.println("=============== No Found List ===================");
        Map noFounds = handler.getNoFoundList();
        Set noFoundTerms = noFounds.keySet();
        iter = noFoundTerms.iterator();
        while(iter.hasNext()) {
            String gi = (String)iter.next();
            System.out.println("Search term: "+gi);
            NoFound nf = (NoFound)noFounds.get(gi);
            System.out.println("\tReason: "+nf.getReason());
            System.out.println("\tSearch term: "+nf.getSearchTerm());
        }
        
        System.exit(0);
    }
}
