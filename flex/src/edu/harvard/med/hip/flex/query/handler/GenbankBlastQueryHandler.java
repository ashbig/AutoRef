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
        giBlastQueryHandler = new GIQueryHandler(params);
    }
    
    public void handleQuery(List searchTerms) throws Exception {
        foundList = new Hashtable();
        noFoundList = new Hashtable();
        
        if(searchTerms == null || searchTerms.size() == 0) {
            return;
        }
        
        Hashtable foundGenbanks = getFoundGenbanks(searchTerms);
        List genbankNotInFlex = new ArrayList();
        genbankNotInFlex.addAll(searchTerms);
        //For genbanks that are not found in FLEX or public, create NoFound records.
        genbankNotInFlex.removeAll(foundGenbanks.keySet());
        addToNoFound(genbankNotInFlex, NoFound.NO_MATCH_GENBANK_ENTRY);
        
        List genomics = new ArrayList();
        Map newSearchTerms = new HashMap();
        Set keys = foundGenbanks.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            List values = (ArrayList)foundGenbanks.get(key);
            List newValues = new ArrayList();
            for(int i=0; i<values.size(); i++) {
                SequenceRecord sr = (SequenceRecord)values.get(i);
                if(!SequenceRecord.GENOMIC.equals(sr.getType())) {
                    newValues.add(sr);
                }
            }
            if(newValues.size() == 0) {
                genomics.add(key);
            } else {
                newSearchTerms.put(key, newValues);
            }
        }
        
        if(isRelatedSeq) {
            FlexGenbankBatchRetriever r = new FlexGenbankBatchRetriever();
            Hashtable relatedSearchTerms = r.retrieveRelatedCodingGenbank(genomics);
            handleGiQuery(relatedSearchTerms, MatchGenbankRecord.RELATED_SEARCH);
        } else {
            addToNoFound(genomics, NoFound.GENOMIC_SEQ);
        }
        
        handleGiQuery(newSearchTerms, MatchGenbankRecord.DIRECT_SEARCH);            
    }
    
    protected void setQueryParams(List params) {
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
    
    private Hashtable getFoundGenbanks(List searchTerms) throws Exception {        
        Hashtable foundGenbanks = new Hashtable();
        GenbankBatchRetriever retriever = new FlexGenbankBatchRetriever();
        Hashtable found = retriever.retrieveGenbank(searchTerms);
        foundGenbanks.putAll(found);
        
        Set genbankInFlex = found.keySet();
        List genbankNotInFlex = new ArrayList();
        genbankNotInFlex.addAll(searchTerms);
        genbankNotInFlex.removeAll(genbankInFlex);
        
        retriever = new PublicGenbankBatchRetriever();
        found = retriever.retrieveGenbank(genbankNotInFlex);
        foundGenbanks.putAll(found);
        
        return foundGenbanks;
    }
    
    private void addToNoFound(List terms, String reason) {    
        for(int i=0; i<terms.size(); i++) {
            String genbank = (String)terms.get(i);
            NoFound nf = new NoFound(genbank, reason);
            noFoundList.put(genbank, nf);
        }
    }
    
    private void handleGiQuery(Map newSearchTerms, String searchMethod) throws Exception {        
        Set giSet = new TreeSet();
        Set genbanks = newSearchTerms.keySet();
        Iterator iter = genbanks.iterator();
        while(iter.hasNext()) {
            String genbank = (String)iter.next();
            List values = (ArrayList)newSearchTerms.get(genbank);
            for(int i=0; i<values.size(); i++) {
                SequenceRecord sr = (SequenceRecord)values.get(i);
                giSet.add((new Integer(sr.getGi())).toString());
            }
        }
        List gis = new ArrayList(giSet);
        giBlastQueryHandler.handleQuery(gis);
        
        Map foundGiList = giBlastQueryHandler.getFoundList();
        Map noFoundGiList = giBlastQueryHandler.getNoFoundList();
        handleFoundResult(foundGiList, newSearchTerms, searchMethod);
        handleNoFound(noFoundGiList, newSearchTerms);
    }
    
    private void handleFoundResult(Map foundGiList, Map newSearchTerms, String searchMethod) {        
        Set keys = foundGiList.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String k = (String)iter.next();
            List matchs = (List)foundGiList.get(k);
            
            Set keyset = newSearchTerms.keySet();
            Iterator it = keyset.iterator();
            while(it.hasNext()) {
                String term = (String)it.next();
                SequenceRecord sr = (SequenceRecord)newSearchTerms.get(term);
                if(sr.getGi() == Integer.parseInt(k)) {
                    List matchGenbanks = new ArrayList();
                    for(int i=0; i<matchs.size(); i++) {
                        MatchGenbankRecord mgr = (MatchGenbankRecord)matchs.get(i);
                        mgr.setSearchMethod(searchMethod);
                        matchGenbanks.add(mgr);
                    }
                    foundList.put(term, matchGenbanks);
                    break;
                }
            }
        }
    }
    
    private void handleNoFound(Map noFoundGiList, Map newSearchTerms) {
        Set keys = noFoundGiList.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String gi = (String)iter.next();
            NoFound nf = (NoFound)noFoundGiList.get(gi);
            
            Set ks = newSearchTerms.keySet();
            Iterator it = ks.iterator();
            while(it.hasNext()) {
                String genbank = (String)it.next();
                SequenceRecord sr = (SequenceRecord)newSearchTerms.get(genbank);
                if(sr.getGi() == Integer.parseInt(gi)) {
                    NoFound value = new NoFound(genbank,  nf.getReason());
                    noFoundList.put(genbank, value);
                    break;
                }
            }
        }
    }
}
