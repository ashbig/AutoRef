/*
 * MatchGenbankRecord.java
 *
 * Created on March 13, 2003, 11:10 AM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import edu.harvard.med.hip.flex.core.ConstructInfo;

/**
 *
 * @author  dzuo
 */
public class MatchGenbankRecord {
    public static final String DIRECT_SEARCH = "DIRECT_SEARCH";
    public static final String RELATED_SEARCH = "RELATED_SEARCH";
    
    private int matchGenbankId;
    private String genbankAccession;
    private String gi;
    private List matchFlexSequence;
    private String searchMethod;
    private int searchResultid;
    private String locusid;
    private String unigene;
    
    /** Creates a new instance of MatchGenbankRecord */
    public MatchGenbankRecord() {
    }

    public MatchGenbankRecord(String genbank, String gi, String searchMethod, List matchFlexSequence, String locusid, String unigene) {
        this.genbankAccession = genbank;
        this.gi = gi;
        this.searchMethod = searchMethod;
        this.matchFlexSequence = matchFlexSequence;
        this.locusid = locusid;
        this.unigene = unigene;
    }
 
    public MatchGenbankRecord(int id, String genbank, String gi, String searchMethod, List matchFlexSequence, String locusid, String unigene) {
        this.matchGenbankId = id;
        this.genbankAccession = genbank;
        this.gi = gi;
        this.searchMethod = searchMethod;
        this.matchFlexSequence = matchFlexSequence;
        this.locusid = locusid;
        this.unigene = unigene;
    }
    
    public void setMatchGenbankId(int id) {
        this.matchGenbankId = id;
    }
    
    public int getMatchGenbankId() {
        return matchGenbankId;
    }
    
    public void setGenbankAccession(String genbankAccession) {
        this.genbankAccession = genbankAccession;
    }
    
    public String getGanbankAccession() {
        return genbankAccession;
    }
    
    public void setGi(String gi) {
        this.gi = gi;
    }
    
    public String getGi() {
        return gi;
    }
    
    public List getMatchFlexSequence() {
        return matchFlexSequence;
    }
    
    public String getSearchMethod() {
        return searchMethod;
    }
    
    public void setSearchMethod(String s) {
        this.searchMethod = s;
    }
    
    public void setMatchFlexSequence(List l) {
        this.matchFlexSequence = l;
    }
    
    public void addMatchFlexSequence(MatchFlexSequence f) {
        matchFlexSequence.add(f);
    }        
    
    public void setSearchResultid(int id) {
        this.searchResultid = id;
    }
    
    public int getSearchResultid() {
        return searchResultid;
    }
    
    public int getNumOfMatchFlexSequence() {
        return matchFlexSequence.size();
    }
    
    public int getNumOfMatchClones() {
        if(matchFlexSequence == null || matchFlexSequence.size() == 0) {
            return 1;
        }
        
        int num = 0;
        for(int i=0; i<matchFlexSequence.size(); i++) {
            MatchFlexSequence mfs = (MatchFlexSequence)matchFlexSequence.get(i);
            int n = mfs.getNumOfClones();
            if(n > 0) {
                num = num + n;
            } else {
                n++;
            }
        }
        return num;
    }
    
    public String getLocusid() {return locusid;}
    public String getUnigene() {return unigene;}
    
    public List getLocusidList() {
        List locusidList = new ArrayList();
        if(locusid == null || locusid.length() == 0) {
            return locusidList;
        }
        
        StringTokenizer st = new StringTokenizer(locusid, ",");
        while(st.hasMoreTokens()) {
            String s = st.nextToken();
            locusidList.add(s);
        }
        return locusidList;
    }
    
    public List getUnigeneList() {
        if(unigene == null || unigene.length() == 0) {
            return null;
        }
        
        List unigeneList = new ArrayList();
        StringTokenizer st = new StringTokenizer(unigene, ",");
        while(st.hasMoreTokens()) {
            String s = st.nextToken();
            unigeneList.add(s);
        }
        return unigeneList;
    }
    
    public void setLocusid(String s) {this.locusid=s;}
    public void setUnigene(String s) {this.unigene = s;}
}
