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
import edu.harvard.med.hip.flex.query.QueryException;

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
    
    
    /**
     * Retrive the sequences from FLEXGene database. Populate foundList
     * and noFoundList. Return a list of GIs that are found in the database.
     *
     * @return A list of GIs that are found in the database.
     * @exception QueryException
     */
    public List retrieveSequence() throws QueryException {
        if(giList == null) {
            throw new QueryException("No query list provided.");
        }
        
        List foundGi = new ArrayList();
        
        for(int i=0; i<noFoundList.size(); i++) {
            String element = (String)(giList.get(i));
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
                    GiRecord giRecord = new GiRecord(Integer.parseInt(element), genbank, element, start, stop);
                    giRecord.setSequenceText((String)(searchResult.get("sequencetext")));
                    foundList.add(giRecord);
                    foundGi.add(element);
                }
            } catch (FlexUtilException ex) {
                NoFound nf = new NoFound(element, NoFound.GI_NOT_IN_NCBI);
                noFoundList.add(nf);
            }
        }
        
        return foundGi;
    }
}
