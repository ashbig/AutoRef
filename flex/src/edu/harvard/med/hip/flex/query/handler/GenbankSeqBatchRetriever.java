/*
 * GenbankSeqBatchRetriever.java
 *
 * Created on March 24, 2003, 2:35 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import javax.sql.*;
import java.io.*;

import edu.harvard.med.hip.flex.util.GenbankGeneFinder;
import edu.harvard.med.hip.flex.util.FlexUtilException;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.QueryException;
import edu.harvard.med.hip.flex.infoimport.locuslinkdb.*;

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
     * Retrive the sequences from FLEXGene database for a list of GI numbers.
     * Populate foundList and noFoundList.
     *      foundList:      GI => GiRecord object
     *      noFoundList:    GI => NoFound object
     *
     * @exception Exception
     */
    public void retrieveSequence() throws Exception {
        if(giList == null || giList.size() == 0) {
            return;
        }
        
        for(int i=0; i<giList.size(); i++) {
            String element = (String)(giList.get(i));
            GenbankGeneFinder finder = new GenbankGeneFinder();
            
            try {
                Hashtable searchResult = finder.searchDetail(element);
                int start = ((Integer)(searchResult.get("start"))).intValue();
                int stop = ((Integer)(searchResult.get("stop"))).intValue();
                
                //genomic sequence
                if(start == -1 || stop == -1) {
                    NoFound nofound = new NoFound(element, NoFound.GENOMIC_SEQ);
                    noFoundList.put(element, nofound);
                } else {
                    String genbank = (String)(searchResult.get("accession"));
                    GiRecord giRecord = new GiRecord(Integer.parseInt(element), genbank, (String)(searchResult.get("sequencetext")), start, stop);
                    foundList.put(element, giRecord);
                }
            } catch (FlexUtilException ex) {
                NoFound nf = new NoFound(element, NoFound.GI_NOT_IN_NCBI);
                noFoundList.put(element, nf);
            }
        }
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
  /**  
        SeqBatchRetriever retriever = new GenbankSeqBatchRetriever(giList);
        
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
            System.out.println("\t"+key+"\t"+gr.getCdsStart()+"\t"+gr.getCdsStop()+"\t"+gr.getGenbankAccession()+"\t"+gr.getSequenceText());
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
*/ 
        
        Map noFoundList = new HashMap();
        
        SeqBatchRetriever retriever = new FlexSeqBatchRetriever(giList);
        try {
            retriever.retrieveSequence();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Map foundList = retriever.getFoundList();
        Map noFound = retriever.getNoFoundList();
        
        List leftSearchTerms = new ArrayList();
        Set noFoundTerms = noFound.keySet();
        Iterator iter = noFoundTerms.iterator();
        while(iter.hasNext()) {
            String noFoundTerm = (String)iter.next();
            NoFound nf = (NoFound)noFound.get(noFoundTerm);
            if(NoFound.INVALID_GI.equals(nf.getReason())) {
                noFoundList.put(noFoundTerm, nf);
            } else {
                leftSearchTerms.add(noFoundTerm);
            }
        }
        
        retriever = new GenbankSeqBatchRetriever(leftSearchTerms);
        try {
            retriever.retrieveSequence();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        foundList.putAll(retriever.getFoundList());
        noFoundList.putAll(retriever.getNoFoundList());
      
        System.out.println("Found List: ");
        Set keys = foundList.keySet();
        iter = keys.iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            GiRecord gr = (GiRecord)foundList.get(key);
            System.out.println("\t"+key+"\t"+gr.getCdsStart()+"\t"+gr.getCdsStop()+"\t"+gr.getGenbankAccession()+"\t"+gr.getSequenceText()+"\t"+gr.getSequenceFile());
        }
        
        System.out.println("\nNo Found:");
        keys = noFoundList.keySet();
        iter = keys.iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            NoFound nf = (NoFound)noFoundList.get(key);
            System.out.println("\t"+key+"\t"+nf.getReason());
        }       
    
      // ThreadedGiRecordPopulator populator = new ThreadedGiRecordPopulator((List)(retriever.getFoundList().values()));
      // populator.persistRecords();        
    }
}
