/*
 * BlastQueryHandler.java
 *
 * Created on March 25, 2003, 1:57 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import edu.harvard.med.hip.flex.util.FlexSeqAnalyzer;
import edu.harvard.med.hip.flex.blast.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.QueryException;
import edu.harvard.med.hip.flex.infoimport.locuslinkdb.ThreadedGiRecordPopulator;

import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author  dzuo
 */
public class BlastQueryHandler extends QueryHandler {
    public static final int DEFAULT_HITS = 5;
    public static final String DEFAULT_DB = FlexSeqAnalyzer.HUMANDB;
    public static final double DEFAULT_PID = 0.9;
    public static final int DEFAULT_LENGTH = 70;
    public static final int DIRECT_SEARCH = 1;
    public static final int SEARCH_BY_GI = 2;
    public static final String FILEPATH = "G:\\";
    
    protected int hits = DEFAULT_HITS;
    protected String db = DEFAULT_DB;
    protected double blastPid = DEFAULT_PID;
    protected int alignLength = DEFAULT_LENGTH;
    
    protected int searchType = DIRECT_SEARCH;
    
    public BlastQueryHandler(List params) {
        super(params);
    }
    
    public void setHits(int hits) {
        this.hits = hits;
    }
    
    public void setDb(String db) {
        this.db = db;
    }
    
    public void setPercentPid(double blastPid) {
        this.blastPid = blastPid;
    }
    
    public void setAlignLength(int alignLength) {
        this.alignLength = alignLength;
    }
    
    public int getSearchType() {
        return searchType;
    }
    
    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
    
    /**
     * Blast a list of input sequences against the blastable database. Return a list of objects
     * that found match by blast.
     *
     * @param queryList A list of GiRecord objects.
     * @return A list of GiRecord that found match by blast.
     * @exception QueryException.
     */
    public void handleQuery(List searchTerms) throws Exception {
        foundList = new HashMap();
        noFoundList = new HashMap();
        
        if(searchTerms == null || searchTerms.size()==0) {
            return;
        }
        
        SeqBatchRetriever retriever = new FlexSeqBatchRetriever(searchTerms);
        retriever.retrieveSequence();
        Map found = retriever.getFoundList();
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
        retriever.retrieveSequence();
        found.putAll(retriever.getFoundList());
        noFound.putAll(retriever.getNoFoundList());
        
        ThreadedGiRecordPopulator populator = new ThreadedGiRecordPopulator((List)(retriever.getFoundList().values()));
        populator.persistRecords();
        
        Blaster blaster = new Blaster();
        blaster.setDBPath(db);
        blaster.setHits(hits);
        
        Date d = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("MM_dd_yyyy");
        String output = null;
        BlastParser parser = null;
        
        Set newSearchTerms = found.keySet();
        iter = newSearchTerms.iterator();
        while(iter.hasNext()) {
            String searchTerm = (String)iter.next();
            GiRecord gr = (GiRecord)found.get(searchTerm);
            int gi = gr.getGi();
            output = FILEPATH+gi+"_"+db+"_"+hits+"_"+f.format(d);
            String sequenceFile = gr.getSequenceFile();
            blaster.blast(sequenceFile, output);
            parser = new BlastParser(output);
            parser.parseBlast();
            
            int queryLength = Integer.parseInt(parser.getQuerySeqLength());
            ArrayList homologs = parser.getHomologList();
            
            List hits = new ArrayList();
            
            //evaluate all alignments of each hit to see if it meets the criteria
            for(int j=0; j<homologs.size(); j++) {
                BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(j);
                int sequenceid = Integer.parseInt(homologItem.getSubID());
                int subLength = homologItem.getSubLen();
                
                ArrayList alignments = new ArrayList();
                boolean isFound = false;
                for(int n=0; n<homologItem.getSize(); n++) {
                    BlastParser.Alignment y = (BlastParser.Alignment)homologItem.getAlignItem(n);
                    String evalue = y.getEvalue();
                    String gap = y.getGap();
                    String identity = y.getIdentity();
                    int queryStart = y.getQryStart();
                    int queryEnd = y.getQryEnd();
                    int subStart = y.getSbjStart();
                    int subEnd = y.getSbjEnd();
                    String score = y.getScore();
                    String strand = y.getStrand();
                    BlastAlignment ba = new BlastAlignment(evalue, gap, identity, queryStart, queryEnd, subStart, subEnd, score, strand);
                    alignments.add(ba);
                    
                    if(n == 0 || !isFound) {
                        StringTokenizer st = new StringTokenizer(identity);
                        int numerator = Integer.parseInt((st.nextToken(" /")).trim());
                        int denomenator = Integer.parseInt((st.nextToken(" /")).trim());
                        double percentIdentity = numerator/(double)denomenator;
                        //meet the criteria
                        if (percentIdentity>=blastPid && numerator >= alignLength) {
                            isFound = true;
                        }
                    }
                }
                
                if(isFound) {
                    BlastHit hit = new BlastHit(sequenceid, queryLength, subLength, alignments);
                    MatchFlexSequence mfs = new MatchFlexSequence("F", sequenceid, hit);
                    hits.add(mfs);
                }
            }
            
            if(hits.size() > 0) {
                foundList.put(searchTerm, hits);
            } else {
                NoFound nofound = new NoFound((new Integer(gi)).toString(), NoFound.NO_MATCH_BLAST);
                noFoundList.put(searchTerm, nofound);
            }
        }
    }

    protected void setQueryParams(List params) {
        for(int i=0; i<params.size(); i++) {
            Param p = (Param)params.get(i);
            if(Param.BLASTDB.equals(p.getName()) && p.getValue() != null && p.getValue().length()!=0) {
                setDb(p.getValue());
            }
            if(Param.BLASTHIT.equals(p.getName()) && p.getValue() != null && p.getValue().length()!=0) {
                setHits(Integer.parseInt(p.getValue()));
            }
            if(Param.BLASTLENGTH.equals(p.getName()) && p.getValue() != null && p.getValue().length()!=0) {
                setAlignLength(Integer.parseInt(p.getValue()));
            }
            if(Param.BLASTPID.equals(p.getName()) && p.getValue() != null && p.getValue().length()!=0) {
                setPercentPid(Integer.parseInt(p.getValue()));
            }
        }
    }

}