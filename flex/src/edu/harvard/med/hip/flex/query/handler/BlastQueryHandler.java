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
import edu.harvard.med.hip.flex.Constants;

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
    public static final String FILEPATH = Constants.TMPDIR;
    
    protected int hits = DEFAULT_HITS;
    protected String db = DEFAULT_DB;
    protected double blastPid = DEFAULT_PID;
    protected int alignLength = DEFAULT_LENGTH;
    
    protected int searchType = DIRECT_SEARCH;
    
    public BlastQueryHandler() {
        super();
    }
    
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
        noFoundList.putAll(noFound);
        
        ThreadedGiRecordPopulator populator = new ThreadedGiRecordPopulator((Collection)(retriever.getFoundList().values()));
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
            output = FILEPATH+gi+"_"+hits+"_"+f.format(d);
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
                int sequenceid = Integer.parseInt(homologItem.getSubID().trim());
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
        
        QueryHandler handler = new BlastQueryHandler();
        try {
            handler.handleQuery(giList);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Map founds = handler.getFoundList();
        Map noFounds = handler.getNoFoundList();
        System.out.println("Number of found: "+founds.size());
        System.out.println("Number of no found: "+noFounds.size());
        
        System.out.println("======================== Found List ==============================");
        Set terms = founds.keySet();
        Iterator iter = terms.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            System.out.println("Search term: "+term);
            List hits = (List)founds.get(term);
            for(int i=0; i<hits.size(); i++) {
                MatchFlexSequence mfs = (MatchFlexSequence)hits.get(i);
                System.out.println("\tFlexsequence ID: "+mfs.getFlexsequenceid());
                System.out.println("\tIs match by GI: "+mfs.getIsMatchByGi());
                System.out.println("\tGenbank Acc: "+mfs.getMatchGenbankId());
                
                BlastHit bh = mfs.getBlastHit();
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
            }
        }
        
        System.out.println("=============== No Found List ===================");
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