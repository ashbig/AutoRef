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
import edu.harvard.med.hip.flex.util.FlexIDGenerator;
import edu.harvard.med.hip.flex.util.Mailer;
import edu.harvard.med.hip.flex.user.*;

/**
 *
 * @author  dzuo
 */
public class SearchManager {
    public static final String FILEPATH = "/tmp/";
    
    protected SearchRecord searchRecord;
    protected List params;
    protected List searchResults;
    protected List searchTerms;
    protected String error = "";
    protected User user;
    
    public void setSearchRecord(SearchRecord s) {this.searchRecord = s;}
    
    public SearchRecord getSearchRecord() {return searchRecord;}
    
    public void setError(String s) {
        this.error = error;
    }
    
    public String getError() {
        return error;
    }
    
    /** Creates a new instance of SearchManager */
    public SearchManager(List searchTerms, String searchType, List params, String searchName, User user) {
        searchRecord = new SearchRecord(searchName,searchType,SearchRecord.INPROCESS,user.getUsername());
        params = params;
        searchTerms = searchTerms;
        user = user;
    }
    
    public SearchManager(SearchRecord searchRecord, List params, List searchTerms) {
        this.searchRecord = searchRecord;
        this.params = params;
        this.searchTerms = searchTerms;
    }
    
    public static List parseSearchFile(InputStream searchFile) throws FileNotFoundException, IOException {
        List searchTerms = new ArrayList();
        BufferedReader in = new BufferedReader(new InputStreamReader(searchFile));
        String line = null;
        while((line = in.readLine()) != null) {
            searchTerms.add(line.trim());
        }
        in.close();
        
        return searchTerms;
    }
    
    public void insertSearchRecord(Connection conn) throws FlexDatabaseException, SQLException {
        int searchid = FlexIDGenerator.getMaxid("search", "searchid");
        //       int searchResultid = FlexIDGenerator.getMaxid("searchresult", "searchresultid");
        
        searchRecord.setSearchid(searchid+1);
        
        //        for(int i=0; i<searchResults.size(); i++) {
        //           SearchResult result = (SearchResult)searchResults.get(i);
        //           result.setSearchResultid(++searchResultid);
        //       }
        
        searchRecord.persist(conn);
        
        ParamSet paramset = new ParamSet(params);
        paramset.persist(conn, searchRecord.getSearchid());
        
        //        SearchResultSet searchResultSet = new SearchResultSet(searchResults);
        //        searchResultSet.persist(conn, searchRecord.getSearchid());
    }
    
    public void updateSearchRecord(Connection conn) throws FlexDatabaseException, SQLException {
        searchRecord.updateStatus(conn);
    }
    
    public void updateSearchResults(Connection conn) throws FlexDatabaseException, SQLException {
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
    
    public void doSearch() {
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
        } catch (Exception ex) {
            error = ex.getMessage();
            searchRecord.setSearchStatus(SearchRecord.FAIL);
        }
    }
    
    public void sendEmail() throws IOException, MessagingException {
        String message = null;
        if(SearchRecord.COMPLETE.equals(searchRecord.getSearchStatus())) {
            message = "Your search has been finished successfully. You can find your search results in our database.";
            Mailer.sendMessage(user.getUserEmail(), "dzuo@hms.harvard.edu", "dzuo@hms.harvard.edu","FLEXGene search - "+searchRecord.getSearchName());
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
            
            Mailer.sendMessage(user.getUserEmail(), "dzuo@hms.harvard.edu",
            "dzuo@hms.harvard.edu","FLEXGene search - "+searchRecord.getSearchName(), message, fileCol);
        }
    }
}
