/*
 * SearchManager.java
 *
 * Created on July 29, 2003, 11:56 AM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.mail.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.user.*;

/**
 *
 * @author  dzuo
 */
public class SearchManager {
    public static final String FILEPATH = FlexProperties.getInstance().getProperty("tmp");
    
    protected SearchRecord searchRecord;
    protected List params;
    protected List searchResults;
    protected List searchTerms;
    protected String error = "";
    
    public void setSearchRecord(SearchRecord s) {this.searchRecord = s;}
    
    public SearchRecord getSearchRecord() {return searchRecord;}
    
    public void setError(String s) {
        this.error = s;
    }
    
    public String getError() {
        return error;
    }
    
    public List getSearchResults() {return searchResults;}
    
    /** Creates a new instance of SearchManager */
    public SearchManager(List searchTerms, String searchType, List params, String searchName, String user) {
        this.searchRecord = new SearchRecord(searchName,searchType,SearchRecord.INPROCESS,user);
        this.params = params;
        this.searchTerms = searchTerms;
    }
    
    public SearchManager(SearchRecord searchRecord, List params, List searchTerms) {
        this.searchRecord = searchRecord;
        this.params = params;
        this.searchTerms = searchTerms;
    }
    
    public synchronized void insertSearchRecord(Connection conn) throws FlexDatabaseException, SQLException {
        int searchid = FlexIDGenerator.getMaxid("search", "searchid");
        //       int searchResultid = FlexIDGenerator.getMaxid("searchresult", "searchresultid");
        
        searchRecord.setSearchid(searchid+1);
        
        //        for(int i=0; i<searchResults.size(); i++) {
        //           SearchResult result = (SearchResult)searchResults.get(i);
        //           result.setSearchResultid(++searchResultid);
        //       }
        
        searchRecord.persist(conn);
        
        if(params != null) {
            ParamSet paramset = new ParamSet(params);
            paramset.persist(conn, searchRecord.getSearchid());
        }
        
        //        SearchResultSet searchResultSet = new SearchResultSet(searchResults);
        //        searchResultSet.persist(conn, searchRecord.getSearchid());
    }
    
    public synchronized void updateSearchRecord(Connection conn, String status) throws FlexDatabaseException, SQLException {
        searchRecord.setSearchStatus(status);
        searchRecord.updateStatus(conn);
    }
    
    public synchronized void insertSearchResults(Connection conn) throws FlexDatabaseException, SQLException {
        int searchResultid = FlexIDGenerator.getMaxid("searchresult", "searchresultid");
        int matchGenbankid = FlexIDGenerator.getMaxid("matchgenbankrecord", "matchgenbankid");
        int matchFlexid = FlexIDGenerator.getMaxid("matchflexsequence", "matchflexid");
        int blastAlignmentid = FlexIDGenerator.getMaxid("blastalignment", "blastalignmentid");
        
        List matchGenbankList = new ArrayList();
        List matchFlexList = new ArrayList();
        List blastList = new ArrayList();
        List blastAlignmentList = new ArrayList();
        List noFoundList = new ArrayList();
        
        for(int i=0; i<searchResults.size(); i++) {
            SearchResult result = (SearchResult)searchResults.get(i);
            result.setSearchResultid(++searchResultid);
            
            List matchGenbanks = result.getFound();
            if(matchGenbanks != null) {
                for (int j=0; j<matchGenbanks.size(); j++) {
                    MatchGenbankRecord mgr = (MatchGenbankRecord)matchGenbanks.get(j);
                    
                    mgr.setSearchResultid(result.getSearchResultid());
                    mgr.setMatchGenbankId(++matchGenbankid);
                    matchGenbankList.add(mgr);
                    
                    List matchFlexSequences = mgr.getMatchFlexSequence();
                    if(matchFlexSequences != null) {
                        for(int k=0; k<matchFlexSequences.size(); k++) {
                            MatchFlexSequence mfs = (MatchFlexSequence)matchFlexSequences.get(k);
                            mfs.setMatchGenbankId(mgr.getMatchGenbankId());
                            mfs.setMatchFlexId(++matchFlexid);
                            matchFlexList.add(mfs);
                            
                            BlastHit blastHit = mfs.getBlastHit();
                            if(blastHit != null) {
                                blastHit.setMatchFlexId(mfs.getMatchFlexId());
                                blastList.add(blastHit);
                                
                                List alignments = blastHit.getAlignments();
                                if(alignments != null) {
                                    for(int n=0; n<alignments.size(); n++) {
                                        BlastAlignment alignment = (BlastAlignment)alignments.get(n);
                                        alignment.setMatchFlexId(mfs.getMatchFlexId());
                                        alignment.setId(++blastAlignmentid);
                                        blastAlignmentList.add(alignment);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            NoFound nf = result.getNoFound();
            if(nf != null) {
                nf.setSearchResultId(result.getSearchResultid());
                noFoundList.add(nf);
            }
        }
        
        SearchResultSet searchResultSet = new SearchResultSet(searchResults);
        searchResultSet.persist(conn, searchRecord.getSearchid());
        
        MatchGenbankRecordSet mgrs = new MatchGenbankRecordSet(matchGenbankList);
        mgrs.persist(conn);
        
        MatchFlexSequenceSet mfss = new MatchFlexSequenceSet(matchFlexList);
        mfss.persist(conn);
        
        BlastHitSet bhs = new BlastHitSet(blastList);
        bhs.persist(conn);
        
        BlastAlignmentSet bas = new BlastAlignmentSet(blastAlignmentList);
        bas.persist(conn);
        
        NoFoundSet nfs = new NoFoundSet(noFoundList);
        nfs.persist(conn);
    }
    
    /**
     * Perform the appropriate search for all terms stored in searchTerms by initiating
     * the correct QueryHandler object. It populates searchResults if search if
     * successful. If search is failed, it stores the error message in error.
     *
     * @return true if search is successful; false otherwise.
     */
    public boolean doSearch() {        
        QueryHandler handler = StaticQueryHandlerFactory.makeQueryHandler(searchRecord.getSearchType(), params);
        try {
            handler.handleQuery(searchTerms);
            Map founds = handler.getFoundList();
            Map noFounds = handler.getNoFoundList();
            
            searchResults = new ArrayList();
            for(int i=0; i<searchTerms.size(); i++) {
                String searchTerm = (String)searchTerms.get(i);
                SearchResult result = null;
                if(founds.containsKey(searchTerm)) {
                    result = new SearchResult(searchTerm, SearchResult.GENBANK_FOUND, (ArrayList)founds.get(searchTerm), null);
                } else if(noFounds.containsKey(searchTerm)) {
                    result = new SearchResult(searchTerm, SearchResult.GENBANK_NOT_FOUND, null, (NoFound)noFounds.get(searchTerm));
                } else {
                    result = new SearchResult(searchTerm, SearchResult.GENBANK_NOT_FOUND, null, new NoFound(searchTerm, NoFound.UNKNOWN));
                }
                searchResults.add(result);
            }
            
            searchRecord.setSearchStatus(SearchRecord.COMPLETE);
            return true;
        } catch (Exception ex) {
            error = ex.getMessage();
            searchRecord.setSearchStatus(SearchRecord.FAIL);
            return false;
        }
    }

    public void sendEmail(String userEmail) throws IOException, MessagingException {
        String message = null;
        if(SearchRecord.COMPLETE.equals(searchRecord.getSearchStatus())) {
            message = "Your search has been finished successfully. You can find your search results in our database.";
            Mailer.sendMessage(userEmail, "dongmei_zuo@hms.harvard.edu","FLEXGene search - "+searchRecord.getSearchName(), message);
        } else {
            message = "Your search failed at the following reason. We have attached your search file to this email.\n\n"+error;
            String file = FILEPATH+searchRecord.getSearchName();
            PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            for(int i=0; i<searchTerms.size(); i++) {
                String searchTerm = (String)searchTerms.get(i);
                pr.println(searchTerm);
            }
            pr.close();
            
            Collection fileCol = new ArrayList();
            fileCol.add(new File(file));
            
            Mailer.sendMessage(userEmail, "dongmei_zuo@hms.harvard.edu",
            "dongmei_zuo@hms.harvard.edu","FLEXGene search - "+searchRecord.getSearchName(), message, fileCol);
        }
    }
    
    public void sendEmail() throws IOException, MessagingException, FlexDatabaseException {
        AccessManager manager = AccessManager.getInstance();
        String userEmail = manager.getEmail(searchRecord.getUsername());
        sendEmail(userEmail);
    }
    
    public static void main(String args[]) {
        SearchRecord searchRecord = new SearchRecord("Test search", SearchRecord.LOCUSID, SearchRecord.INPROCESS, "dzuo");
        List searchTerms = new ArrayList();
        //searchTerms.add("33469916");
        //searchTerms.add("21961206");
        //searchTerms.add("33469967");
        //searchTerms.add("1234");
        //searchTerms.add("345");
        //searchTerms.add("ABCA2");
        //searchTerms.add("DRG2");
        //searchTerms.add("DSCR8");
        //searchTerms.add("EEF1G");
        //searchTerms.add("EGFR");
        searchTerms.add("10");
        
        SearchManager manager = new SearchManager(searchRecord, null, searchTerms);
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            manager.insertSearchRecord(conn);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        if(manager.doSearch()) {
            List searchResults = manager.getSearchResults();
            
            for(int i=0; i<searchResults.size(); i++) {
                SearchResult result = (SearchResult)searchResults.get(i);
                System.out.println("found genbank: "+result.getIsGenbankFound());
                System.out.println("search term: "+result.getSearchTerm());
                
                List matchGenbanks = result.getFound();
                System.out.println("==========found======");
                if(matchGenbanks != null) {
                    for (int j=0; j<matchGenbanks.size(); j++) {
                        MatchGenbankRecord mgr = (MatchGenbankRecord)matchGenbanks.get(j);
                        System.out.println("\tGenbank Acc: "+mgr.getGanbankAccession());
                        System.out.println("\tGI: "+mgr.getGi());
                        System.out.println("\tLocus: "+mgr.getLocusid());
                        System.out.println("\tSearch Method: "+mgr.getSearchMethod());
                        
                        List matchFlexSequences = mgr.getMatchFlexSequence();
                        if(matchFlexSequences != null) {
                            for(int k=0; k<matchFlexSequences.size(); k++) {
                                MatchFlexSequence mfs = (MatchFlexSequence)matchFlexSequences.get(k);
                                System.out.println("\t\tFlex ID: "+mfs.getFlexsequenceid());
                                System.out.println("\t\tIs match by GI: "+mfs.getIsMatchByGi());
                                
                                BlastHit blastHit = mfs.getBlastHit();
                                if(blastHit != null) {
                                    System.out.println("\t\t\tFlex ID: "+blastHit.getMatchFlexId());
                                    System.out.println("\t\t\tQuery length: "+blastHit.getQueryLength());
                                    System.out.println("\t\t\tSub length: "+blastHit.getSubjectLength());
                                    
                                    List alignments = blastHit.getAlignments();
                                    if(alignments != null) {
                                        for(int n=0; n<alignments.size(); n++) {
                                            BlastAlignment ba = (BlastAlignment)alignments.get(n);
                                            System.out.println("\t\t\tE value: "+ba.getEvalue());
                                            System.out.println("\t\t\t\tGap: "+ba.getGap());
                                            System.out.println("\t\t\t\tID: "+ba.getId());
                                            System.out.println("\t\t\t\tIdentity: "+ba.getIdentity());
                                            System.out.println("\t\t\t\tFlex ID: "+ba.getMatchFlexId());
                                            System.out.println("\t\t\t\tQuery start: "+ba.getQueryStart());
                                            System.out.println("\t\t\t\tQuery end: "+ba.getQueryEnd());
                                            System.out.println("\t\t\t\tScore: "+ba.getScore());
                                            System.out.println("\t\t\t\tStrand: "+ba.getStrand());
                                            System.out.println("\t\t\t\tSub start: "+ba.getSubStart());
                                            System.out.println("\t\t\t\tSub end: "+ba.getSubEnd());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                
                System.out.println("\t=========== no found=========");
                NoFound nf = result.getNoFound();
                if(nf != null) {
                    System.out.println("\tReason: "+nf.getReason());
                    System.out.println("\tSearch term: "+nf.getSearchTerm());
                }
            }
            
            try {
                manager.insertSearchResults(conn);
                manager.updateSearchRecord(conn, SearchRecord.COMPLETE);
                DatabaseTransaction.commit(conn);
                manager.sendEmail("dongmei_zuo@hms.harvard.edu");
            } catch (Exception ex) {
                System.out.println(ex);
                DatabaseTransaction.rollback(conn);
            } finally {
                DatabaseTransaction.closeConnection(conn);
            }
        } else {
            System.out.println("search failed");
            System.out.println("errors: "+manager.getError());
        }
    }
}
