/*
 * GenbankSeqBatchRetriever.java
 *
 * Created on March 24, 2003, 2:35 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.util.GenbankGeneFinder;
import edu.harvard.med.hip.flex.util.FlexUtilException;
import edu.harvard.med.hip.flex.query.core.*;

/**
 *
 * @author  dzuo
 */
public class GenbankSeqBatchRetriever extends SeqBatchRetriever {
    
    /** Creates a new instance of GenbankSeqBatchRetriever */
    public GenbankSeqBatchRetriever() {
    }
    
    public GenbankSeqBatchRetriever(List giList) {
        super(giList);
    }
    
    public boolean retrieveSequence() {
        if(giList == null || giList.size() == 0) {
            error = "No query list provided.";
            return false;
        }
        
        noFoundList.addAll(giList);
        List foundGi = new ArrayList();
        
        for(int i=0; i<noFoundList.size(); i++) {
            String element = (String)(noFoundList.get(i));
            GenbankGeneFinder finder = new GenbankGeneFinder();
            
            try {
                Hashtable searchResult = finder.searchDetail(element);
                int start = ((Integer)(searchResult.get("start"))).intValue();
                int stop = ((Integer)(searchResult.get("stop"))).intValue();
                
                //genomic sequence
                if(start == -1 || stop == -1) {
                    NoFound nofound = new NoFound(element, NoFound.GENOMIC_SEQ);
                    noFoundList.add(nofound);
                } else {
                    String genbank = (String)(searchResult.get("accession"));
                    int genbankVersion = -1;
                    GiRecord giRecord = new GiRecord(element, genbank, genbankVersion, element, start, stop);
                    giRecord.setSequenceText((String)(searchResult.get("sequencetext")));
                    foundList.add(giRecord);
                    foundGi.add(element);
                }
            } catch (FlexUtilException ex) {
                error = ex.getMessage();
                return false;
            }
        }
        
        noFoundList.removeAll(foundGi);        
        return true;
    }
}
