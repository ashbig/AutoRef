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

import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

/**
 *
 * @author  dzuo
 */
public class BlastQueryHandler extends FlexQueryHandler {
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
    
    public BlastQueryHandler(SearchRecord search) {
        super(search);
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
    protected List doQuery() throws Exception {
        foundList = new Hashtable();
        noFoundList = new Hashtable();
        
        Blaster blaster = new Blaster();
        setBlastParams(blaster, searchRecord);

        Date d = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("MM_dd_yyyy");
        String output = null;
        BlastParser parser = null;
    /**    
        
        for(int i=0; i<queryList.size(); i++) {
            GiRecord gr = (GiRecord)queryList.get(i);
            int gi = gr.getGi();
            output = FILEPATH+gi+"_"+db+"_"+hits+"_"+f.format(d);
            blaster.blast(gr.getSequenceFile(), output);
            parser = new BlastParser(output);
            parser.parseBlast();
            
            int queryLength = Integer.parseInt(parser.getQuerySeqLength());
            ArrayList homologs = parser.getHomologList();
            
            ArrayList hits = new ArrayList();
            ArrayList matchHits = new ArrayList();
            
            //evaluate all alignments of each hit to see if it meets the criteria
            for(int j=0; j<homologs.size(); j++) {
                BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(j);
                int sequenceid = Integer.parseInt(homologItem.getSubID());
                int subLength = homologItem.getSubLen();
                
                ArrayList alignments = new ArrayList();
                boolean found = false;
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
                    
                    if(n == 0 || !found) {
                        StringTokenizer st = new StringTokenizer(identity);
                        int numerator = Integer.parseInt((st.nextToken(" /")).trim());
                        int denomenator = Integer.parseInt((st.nextToken(" /")).trim());
                        double percentIdentity = numerator/(double)denomenator;
                        //meet the criteria
                        if (percentIdentity>=blastPid && numerator >= alignLength) {
                            found = true;
                        }
                    }
                }
                                        
                    BlastHit hit = new BlastHit(sequenceid, queryLength, subLength, alignments);
                    MatchFlexSequence mfs = new MatchFlexSequence("F", "F", sequenceid, hit);
                /**
                if(searchType == DIRECT_SEARCH) {
                    mfs.setSearchResultId(k);
                } else {
                    mfs.setMatchGenbankId(k);
                }
                hits.add(mfs);
                
                if(found) {
                    mfs.setIsMatchByGi("T");
                    matchHits.add(mfs);
                }
            }
            
            hitList.put(gi, hits);
            
            if(matchHits.size() > 0) {
                matchList.put(gi, matchHits);
            } else {
                noMatchList.put(key, gi);
            }
        }*/
        return null;
    }
    
    public boolean persistBlastHit(Hashtable hitList, Connection conn) {
        if(hitList == null) {
            return false;
        }
        
        String blastHitSql = "insert into blasthit values"+
        " (?, ?, ?, ?)";
        String blastAlignmentSql = "insert into blastalignment values"+
        " (blastalignmentid.nextval,?, ?,?,?,?,?,?,?,?,?)";
        String matchFlexSql = "insert into matchflexsequence values"+
        " (matchflexid.nextval, 'F', ?, ?, ?, ?, ?)";
        String sql = "select blasthit.nextval from dual";
      
        PreparedStatement blastHitStmt = null;
        PreparedStatement blastAlignmentStmt = null;
        PreparedStatement stmt = null;
        PreparedStatement matchFlexStmt = null;
        
        try {
            blastHitStmt = conn.prepareStatement(blastHitSql);
            blastAlignmentStmt = conn.prepareStatement(blastAlignmentSql);
            stmt = conn.prepareStatement(sql);
            
            Collection allhits = hitList.values();
            Iterator iter = allhits.iterator();
            while(iter.hasNext()) {
                ArrayList hits = (ArrayList)iter.next();
                for(int i=0; i<hits.size(); i++) {
                    MatchFlexSequence mfs = (MatchFlexSequence)hits.get(i);
                    BlastHit hit = mfs.getBlastHit();
                    
                    //get the next primary key.
                    int hitId = -1;
                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()) {
                        hitId = rs.getInt(1);
                    }
                    
                    hit.setId(hitId);
                    
                    //insert into blasthit table.
                    blastHitStmt.setInt(1, hitId);
                    blastHitStmt.setInt(2, hit.getSequenceid());
                    blastHitStmt.setInt(3, hit.getQueryLength());
                    blastHitStmt.setInt(4, hit.getSubjectLength());
                    blastHitStmt.execute();
                    
                    //get all the alignments and insert into blastalignment table.
                    List alignments = hit.getAlignments();
                    for(int j=0; j<alignments.size(); j++) {
                        BlastAlignment alignment = (BlastAlignment)alignments.get(j);
                        blastAlignmentStmt.setInt(1, hitId);
                        blastAlignmentStmt.setString(2, alignment.getEvalue());
                        blastAlignmentStmt.setString(3, alignment.getGap());
                        blastAlignmentStmt.setString(4,  alignment.getIdentity());
                        blastAlignmentStmt.setInt(5, alignment.getQueryStart());
                        blastAlignmentStmt.setInt(6, alignment.getQueryEnd());
                        blastAlignmentStmt.setInt(7, alignment.getSubStart());
                        blastAlignmentStmt.setInt(8, alignment.getSubEnd());
                        blastAlignmentStmt.setString(9, alignment.getScore());
                        blastAlignmentStmt.setString(10, alignment.getStrand());
                        blastAlignmentStmt.execute();
                    }
                    
                    //insert into MATCHFLEXSEQUENCE table
                    matchFlexStmt.setString(1, mfs.getMeetBlastCriteria());
                    matchFlexStmt.setInt(2, mfs.getMatchGenbankId());
                    matchFlexStmt.setInt(3, mfs.getSearchResultId());
                    matchFlexStmt.setInt(4, mfs.getFlexsequenceid());
                    matchFlexStmt.setInt(5, hitId);
                    matchFlexStmt.execute();
                }
            }            
        } catch (SQLException ex) {
            return false;
        }
        
        return true;
    }
    
    protected void setBlastParams(Blaster blaster, SearchRecord search) {
                List params = search.getParams();
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