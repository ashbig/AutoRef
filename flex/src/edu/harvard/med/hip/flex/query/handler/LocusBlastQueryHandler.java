/*
 * LocusBlastQueryHandler.java
 *
 * Created on February 19, 2004, 5:50 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;
import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class LocusBlastQueryHandler extends QueryHandler {
    private QueryHandler genbankBlastQueryHandler;
    
    /** Creates a new instance of LocusBlastQueryHandler */
    public LocusBlastQueryHandler() {
    }
       
    /** Creates a new instance of GenbankBlastQueryHandler */
    public LocusBlastQueryHandler(List params) {
        super(params);
        genbankBlastQueryHandler = new GenbankBlastQueryHandler(params);
    }
        
    /**
     * Query FLEXGene database for a list of locus id numbers and populate foundList and
     * noFoundList. It first query FLEXGene database or NCBI to get lists of SequenceRecord objects
     * which contains genbank accession numbers. It then use GenbankBlastQueryHandler to query 
     * for a list of accession numbers.
     *
     *  foundList:      locusid => ArrayList of MatchGenbankRecord objects
     *  noFoundList:    locusid => NoFound object
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

        GenbankBatchRetriever retriever = new LocusGenbankBatchRetriever(searchTerms);
        retriever.retrieveGenbank();
        Map founds = retriever.getFoundList();
        noFoundList = retriever.getNoFoundList();
        
        //get all the accession numbers for the found gene records.
        Set genbanks = new TreeSet();
        Collection values = founds.values();
        Iterator iter = values.iterator();
        while(iter.hasNext()) {
            List matchs = (List)iter.next();
            for(int i=0; i<matchs.size(); i++) {
                SequenceRecord sr = (SequenceRecord)matchs.get(i);
                String genbank = sr.getGenbank();
                genbanks.add(genbank);
            }
        }
        List genbankTermsList = new ArrayList();
        genbankTermsList.addAll(genbanks);
        
        //Query by GI terms
        genbankBlastQueryHandler.handleQuery(genbankTermsList);
        
        //handle found.
        Map foundByGenbank = genbankBlastQueryHandler.getFoundList();
        Set terms = founds.keySet();
        iter = terms.iterator();
        Map leftTerms = new HashMap();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            List matchs = (List)founds.get(term);
            List matchGenbankRecords = new ArrayList();
            for(int i=0; i<matchs.size(); i++) {
                SequenceRecord sr = (SequenceRecord)matchs.get(i);
                String genbank = sr.getGenbank();
                if(foundByGenbank.containsKey(genbank)) {
                    List mgrs = (List)foundByGenbank.get(genbank);
                    
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
        Map noFoundByGenbank = genbankBlastQueryHandler.getNoFoundList();
        terms = leftTerms.keySet();
        iter = terms.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            List matchs = (List)leftTerms.get(term);
            String reason = NoFound.NO_MATCH_GI_QUERY;
            for(int i=0; i<matchs.size(); i++) {
                SequenceRecord sr = (SequenceRecord)matchs.get(i);
                String genbank = sr.getGenbank();
                if(noFoundByGenbank.containsKey(genbank)) {
                    NoFound nf = (NoFound)noFoundByGenbank.get(genbank);
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
    }
   
    public static void main(String args[]) {
        List locusList = new ArrayList();
        locusList.add("1012");
        //locusList.add("1");
        //locusList.add("10");
        //locusList.add("abc");
        
        List params = new ArrayList();
        
        QueryHandler handler = new LocusBlastQueryHandler(params);
        try {
            handler.handleQuery(locusList);
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
                System.out.println("GI: "+mgr.getGi());System.out.println("Locus: "+mgr.getLocusid());
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
