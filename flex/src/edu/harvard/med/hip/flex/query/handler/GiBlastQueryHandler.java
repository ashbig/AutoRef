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
            String reason = (String)nofound.get(gi);
            NoFound nf = new NoFound(gi, reason);
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
}
