/*
 * GiBlastQueryHandler.java
 *
 * Created on October 10, 2003, 3:00 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;

import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  DZuo
 */
public class GiBlastQueryHandler extends QueryHandler {
    QueryHandler giHandler;
    QueryHandler blastHandler;
    
    /** Creates a new instance of GiBlastQueryHandler */
    public GiBlastQueryHandler(List params) {
        super(params);
        giHandler = new GIQueryHandler(params);
        blastHandler = new BlastQueryHandler(params);
    }
    
    public void handleQuery(List searchTerms) throws Exception {
        foundList = new HashMap();
        noFoundList = new HashMap();
        
        giHandler.handleQuery(searchTerms);
        
        //For GIs that found in DB, create MatchGenbankRecord object and store in foundList.
        Map found = giHandler.getFoundList();
        storeFound(found, foundList);
        
        //For GIs that not found in DB, using BlastQueryHandler to handle query.
        Map nofound = giHandler.getNoFoundList();
        Set noFoundGis = nofound.keySet();
        List terms = new ArrayList();
        terms.addAll(noFoundGis);
        blastHandler.handleQuery(terms);
        found = blastHandler.getFoundList();
        storeFound(found, foundList);
        
        //For GIs that not found by blast, add to noFoundList
        nofound = blastHandler.getNoFoundList();
        Set keySet = nofound.keySet();
        Iterator iter = keySet.iterator();
        while(iter.hasNext()) {
            String gi = (String)iter.next();
            NoFound nf = (NoFound)nofound.get(gi);
            noFoundList.put(gi, nf);
        }
    }
    
    protected void setQueryParams(List params) {
    }
    
    private void storeFound(Map found, Map foundList) {
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String gi = (String)iter.next();
            ArrayList matchFlexSequences = (ArrayList)found.get(gi);
            MatchGenbankRecord mgr = new MatchGenbankRecord(null, gi, MatchGenbankRecord.DIRECT_SEARCH, matchFlexSequences);
            foundList.put(gi, mgr);
        }
    }
    
    public static void main(String args[]) {
        List searchTerms = new ArrayList();
        searchTerms.add("33469916");
        searchTerms.add("21961206");
        searchTerms.add("33469967");
        searchTerms.add("1234");
        searchTerms.add("345");
        
        List params = new ArrayList();
        params.add(new Param(Param.BLASTPID, "90"));
        params.add(new Param(Param.BLASTLENGTH, "100"));
        
        GiBlastQueryHandler handler = new GiBlastQueryHandler(params);
        try {
            handler.handleQuery(searchTerms);
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        }
        
        Map found = handler.getFoundList();
        System.out.println("=========== Found =============");
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            System.out.println("search term: "+ term);
            
            MatchGenbankRecord mgr = (MatchGenbankRecord)found.get(term);
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
        
        System.out.println("=============== No Found List ===================");
        Map noFounds = handler.getNoFoundList();
        Set noFoundTerms = noFounds.keySet();
        iter = noFoundTerms.iterator();
        while(iter.hasNext()) {
            String t = (String)iter.next();
            System.out.println("Search term: "+t);
            NoFound nf = (NoFound)noFounds.get(t);
            System.out.println("\tReason: "+nf.getReason());
            System.out.println("\tSearch term: "+nf.getSearchTerm());
        }
        
        System.exit(0);
    }
}