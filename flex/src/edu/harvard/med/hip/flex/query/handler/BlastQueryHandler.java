/*
 * BlastQueryHandler.java
 *
 * Created on March 25, 2003, 1:57 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import edu.harvard.med.hip.flex.util.FlexSeqAnalyzer;
import edu.harvard.med.hip.flex.blast.*;
import edu.harvard.med.hip.flex.query.core.*;

import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.*;

/**
 *
 * @author  dzuo
 */
public class BlastQueryHandler {
    public static final int DEFAULT_HITS = 5;
    public static final String DEFAULT_DB = FlexSeqAnalyzer.HUMANDB;
    public static final double DEFAULT_PID = 0.9;
    public static final int DEFAULT_LENGTH = 70;
    public static final int DIRECT_SEARCH = 1;
    public static final int SEARCH_BY_GI = 2;
    
    protected int hits = DEFAULT_HITS;
    protected String db = DEFAULT_DB;
    protected double blastPid = DEFAULT_PID;
    protected int alignLength = DEFAULT_LENGTH;
    
    protected Hashtable hitFlexList;
    protected Hashtable matchFlexList;
    protected Hashtable hitList;
    protected Hashtable matchList;
    protected Hashtable noMatchList;
    protected String error;
    protected int searchType = DIRECT_SEARCH;
    
    /** Creates a new instance of BlastQueryHandler */
    public BlastQueryHandler() {
    }
    
    public BlastQueryHandler(int hits, String db) {
        this.hits = hits;
        this.db = db;
    }
    
    public BlastQueryHandler(int hits, String db, double blastPid, int alignLength) {
        this.hits = hits;
        this.db = db;
        this.blastPid = blastPid;
        this.alignLength = alignLength;
    }
    
    public Hashtable getMatchList() {
        return matchList;
    }
    
    public Hashtable getNoMatchList() {
        return noMatchList;
    }
    
    public String getError() {
        return error;
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
    
    public boolean doBatchBlast(Hashtable queryList) {
        if(queryList == null) {
            error = "Input parameter is null.";
            return false;
        }
        
        hitList = new Hashtable();
        matchList = new Hashtable();
        noMatchList = new Hashtable();
        
        Blaster blaster = new Blaster();
        blaster.setHits(hits);
        blaster.setDBPath(db);
        Date d = new java.util.Date();
        SimpleDateFormat f = new SimpleDateFormat("MM_dd_yyyy");
        String output = null;
        BlastParser parser = null;
        
        Enumeration keys = queryList.keys();
        while(keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String queryItem = (String)queryList.get(key);
            output = queryItem+"_"+db+"_"+hits+"_"+f.format(d);
            blaster.blast(queryItem, output);
            parser = new BlastParser(output);
            try {
                parser.parseBlast();
            } catch (ParseException ex) {
                error = ex.getMessage();
                return false;
            }
            
            int queryLength = Integer.parseInt(parser.getQuerySeqLength());
            ArrayList homologs = parser.getHomologList();
            
            ArrayList hits = new ArrayList();
            ArrayList matchHits = new ArrayList();
            boolean found = false;
            
            //evaluate the first alignment of each hit to see if it meets the criteria
            for(int j=0; j<homologs.size(); j++) {
                BlastParser.HomologItem homologItem = (BlastParser.HomologItem)homologs.get(j);
                int sequenceid = Integer.parseInt(homologItem.getSubID());
                int subLength = homologItem.getSubLen();
                
                ArrayList alignments = new ArrayList();
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
                    
                    if(n == 0) {
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
                
                int k = Integer.parseInt(key);
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
            
            hitList.put(queryItem, hits);
            
            if(matchHits.size() > 0) {
                matchList.put(queryItem, matchHits);
            } else {
                noMatchList.put(key, queryItem);
            }
        }
        
        return true;
    }
    
    public boolean persistBlastHit(Hashtable hitList, Connection conn) {
        if(hitList == null) {
            error = "Input parameter is null.";
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
            error = ex.getMessage();
            return false;
        }
        
        return true;
    }
}