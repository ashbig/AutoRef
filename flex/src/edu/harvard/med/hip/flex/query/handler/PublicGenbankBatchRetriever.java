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
    
    public Hashtable retrieveGenbank(List genbanks) throws Exception {
        Hashtable found = new Hashtable();
        if(genbanks == null || genbanks.size() == 0) {
            return found;
        }
        
        GenbankGeneFinder finder = new GenbankGeneFinder();
        for(int i=0; i<genbanks.size(); i++) {
            String genbank = (String)genbanks.get(i);
            Vector ret = finder.search(genbank);
            List matchs = new ArrayList();
            for(int j=0; j<ret.size(); j++) {
                GenbankSequence seq = (GenbankSequence)ret.get(j);
                SequenceRecord sr = new SequenceRecord(seq.getAccession(), Integer.parseInt(seq.getGi()), 0, null);
                matchs.add(sr);
            } 
            found.put(genbank, matchs);
        }
        
        return found;
    }
    
}
