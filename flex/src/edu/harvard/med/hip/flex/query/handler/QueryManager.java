/*
 * QueryManager.java
 *
 * Created on February 9, 2004, 12:05 PM
 */

package edu.harvard.med.hip.flex.query.handler;

import java.sql.*;
import java.util.*;
import java.sql.Date;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.core.*;
import edu.harvard.med.hip.flex.query.bean.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  DZuo
 */
public class QueryManager {
    String error = null;
    
    /** Creates a new instance of QueryManager */
    public QueryManager() {
    }
    
    public String getError() {return error;}
    
    public SearchRecord getSearchRecord(int searchid) {
        String sql = "select * from search where searchid="+searchid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        SearchRecord searchRecord = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                int id = rs.getInt(1);
                String searchName = rs.getString(2);
                Date searchDate = rs.getDate(3);
                String searchType = rs.getString(4);
                String searchStatus = rs.getString(5);
                String username = rs.getString(6);
                searchRecord = new SearchRecord(id, searchName,searchDate.toString(),searchType,searchStatus,username);
            }
            
            return searchRecord;
        } catch (Exception ex) {
            error = new String(ex.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public List getSearchRecords(String username) {
        List searchRecords = new ArrayList();
        String sql = "select * from search where username=? order by searchid desc";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                int searchid = rs.getInt(1);
                String searchName = rs.getString(2);
                Date searchDate = rs.getDate(3);
                String searchType = rs.getString(4);
                String searchStatus = rs.getString(5);
                SearchRecord searchRecord = new SearchRecord(searchid, searchName,searchDate.toString(),searchType,searchStatus,username);
                searchRecords.add(searchRecord);
            }
            
            return searchRecords;
        } catch (Exception ex) {
            error = new String(ex.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public int getNumOfResults(int searchid) {
        String sql = "select count(*) from searchresult"+
        " where searchid="+searchid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        int num = 0;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                num = rs.getInt(1);
            }
        } catch (Exception ex) {
            error = new String(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return num;
    }
    
    public int getNumOfFounds(int searchid) {
        String sql = "select count(*) from searchresult"+
        " where isfound='"+SearchResult.GENBANK_FOUND+"'"+
        " and searchid="+searchid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        int num = 0;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                num = rs.getInt(1);
            }
        } catch (Exception ex) {
            error = new String(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return num;
    }
    
    public int getNumOfNoFounds(int searchid) {
        String sql = "select count(*) from searchresult"+
        " where isfound='"+SearchResult.GENBANK_NOT_FOUND+"'"+
        " and searchid="+searchid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        int num = 0;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                num = rs.getInt(1);
            }
        } catch (Exception ex) {
            error = new String(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return num;
    }
    
    public List getFounds(int searchid) {
        List founds = new ArrayList();
        String sql = "select * from searchresult where isfound='"+SearchResult.GENBANK_FOUND+"' and searchid="+searchid;
        String sql2 = "select * from matchgenbankrecord where searchresultid=?";
        String sql3 = "select * from matchflexsequence where matchgenbankid=?";
        String sql4 = "select * from blasthit where matchflexid=?";
        String sql5 = "select * from flexsequence where sequenceid=?";
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt4 = conn.prepareStatement(sql4);
            stmt5 = conn.prepareStatement(sql5);
            
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int searchresultid = rs.getInt(1);
                String searchTerm = rs.getString(2);
                String isFound = rs.getString(3);
                
                stmt2.setInt(1,  searchresultid);
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                List mgss = new ArrayList();
                while(rs2.next()) {
                    int matchgenbankid = rs2.getInt(1);
                    String accession = rs2.getString(2);
                    String gi = rs2.getString(3);
                    String method = rs2.getString(4);
                    String locusid = rs2.getString(6);
                    String unigene = rs2.getString(7);
                    
                    stmt3.setInt(1, matchgenbankid);
                    rs3 = DatabaseTransaction.executeQuery(stmt3);
                    List mfss = new ArrayList();
                    while(rs3.next()) {
                        int matchflexid = rs3.getInt(1);
                        String isMatchByGi = rs3.getString(2);
                        int flexsequenceid = rs3.getInt(4);
                        
                        stmt4.setInt(1, matchflexid);
                        rs4 = DatabaseTransaction.executeQuery(stmt4);
                        BlastHit bh = null;
                        if(rs4.next()) {
                            int querylength = rs4.getInt(2);
                            int sublength = rs4.getInt(3);
                            String output = rs4.getString(4);
                            bh = new BlastHit(querylength,sublength,null,output);
                        }
                        
                        stmt5.setInt(1, flexsequenceid);
                        rs5 = DatabaseTransaction.executeQuery(stmt5);
                        FlexSequence sequence = null;
                        if(rs5.next()) {
                            int sequenceid = rs5.getInt(1);
                            String flexStatus = rs5.getString(2);
                            String species = rs5.getString(3);
                            int start = rs5.getInt(4);
                            int stop = rs5.getInt(5);
                            int length = rs5.getInt(6);
                            int gccontent = rs5.getInt(7);
                            String source = rs5.getString(8);
                            String chromosome = rs5.getString(9);
                            sequence = new FlexSequence(sequenceid,flexStatus,species,null,null,start,stop,length,gccontent,null,source,chromosome);
                        }
                        
                        MatchFlexSequence mfs = new MatchFlexSequence(matchflexid, isMatchByGi, flexsequenceid, sequence, bh);
                        mfss.add(mfs);
                    }
                    
                    MatchGenbankRecord mgs = new MatchGenbankRecord(matchgenbankid,accession,gi, method, mfss, locusid, unigene);
                    mgss.add(mgs);
                }
                
                SearchResult result = new SearchResult(searchresultid,searchTerm,isFound,mgss,null,searchid);
                founds.add(result);
            }
            
            return founds;
        } catch (Exception ex) {
            error = new String(ex.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeResultSet(rs4);
            DatabaseTransaction.closeResultSet(rs5);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeStatement(stmt5);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getFounds(int searchid, int startRecord, int endRecord) {
        List founds = new ArrayList();
        String sql = "select * from searchresult "+
        " where isfound='"+SearchResult.GENBANK_FOUND+"' and searchid="+searchid+
        " and rownum<="+endRecord+" minus "+
        " (select * from searchresult "+
        " where isfound='"+SearchResult.GENBANK_FOUND+"' and searchid="+searchid+
        " and rownum<="+startRecord+")";
        String sql2 = "select * from matchgenbankrecord where searchresultid=?";
        String sql3 = "select * from matchflexsequence where matchgenbankid=?";
        String sql4 = "select * from blasthit where matchflexid=?";
        String sql5 = "select * from flexsequence where sequenceid=?";
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt4 = conn.prepareStatement(sql4);
            stmt5 = conn.prepareStatement(sql5);
            
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int searchresultid = rs.getInt(1);
                String searchTerm = rs.getString(2);
                String isFound = rs.getString(3);
                
                stmt2.setInt(1,  searchresultid);
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                List mgss = new ArrayList();
                while(rs2.next()) {
                    int matchgenbankid = rs2.getInt(1);
                    String accession = rs2.getString(2);
                    String gi = rs2.getString(3);
                    String method = rs2.getString(4);
                    String locusid = rs2.getString(6);
                    String unigene = rs2.getString(7);
                    
                    stmt3.setInt(1, matchgenbankid);
                    rs3 = DatabaseTransaction.executeQuery(stmt3);
                    List mfss = new ArrayList();
                    while(rs3.next()) {
                        int matchflexid = rs3.getInt(1);
                        String isMatchByGi = rs3.getString(2);
                        int flexsequenceid = rs3.getInt(4);
                        
                        stmt4.setInt(1, matchflexid);
                        rs4 = DatabaseTransaction.executeQuery(stmt4);
                        BlastHit bh = null;
                        if(rs4.next()) {
                            int querylength = rs4.getInt(2);
                            int sublength = rs4.getInt(3);
                            String output = rs4.getString(4);
                            bh = new BlastHit(querylength,sublength,null,output);
                        }
                        
                        stmt5.setInt(1, flexsequenceid);
                        rs5 = DatabaseTransaction.executeQuery(stmt5);
                        FlexSequence sequence = null;
                        if(rs5.next()) {
                            int sequenceid = rs5.getInt(1);
                            String flexStatus = rs5.getString(2);
                            String species = rs5.getString(3);
                            int start = rs5.getInt(4);
                            int stop = rs5.getInt(5);
                            int length = rs5.getInt(6);
                            int gccontent = rs5.getInt(7);
                            String source = rs5.getString(8);
                            String chromosome = rs5.getString(9);
                            sequence = new FlexSequence(sequenceid,flexStatus,species,null,null,start,stop,length,gccontent,null,source,chromosome);
                        }
                        
                        MatchFlexSequence mfs = new MatchFlexSequence(matchflexid, isMatchByGi, flexsequenceid, sequence, bh);
                        mfss.add(mfs);
                    }
                    
                    MatchGenbankRecord mgs = new MatchGenbankRecord(matchgenbankid,accession,gi, method, mfss, locusid, unigene);
                    mgss.add(mgs);
                }
                
                SearchResult result = new SearchResult(searchresultid,searchTerm,isFound,mgss,null,searchid);
                founds.add(result);
            }
            
            return founds;
        } catch (Exception ex) {
            error = new String(ex.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeResultSet(rs4);
            DatabaseTransaction.closeResultSet(rs5);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeStatement(stmt5);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getConstructInfo(List seqids) {        
        List infos = new ArrayList();
        
        String sql = "select cd.constructid, cd.oligoid_5p, cd.oligoid_3p, cd.constructtype,"+
        " p.projectid, p.name, w.workflowid, w.name, cs.type"+
        " from constructdesign cd, cloningprogress cl, cloningstatus cs, project p, workflow w"+
        " where cd.constructid=cl.constructid(+)"+
        " and cl.statusid=cs.statusid(+)"+
        " and cd.projectid=p.projectid"+
        " and cd.workflowid=w.workflowid"+
        " and cd.sequenceid=?";
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<seqids.size(); i++) {
                int seqid = Integer.parseInt((String)seqids.get(i));
                stmt.setInt(1, seqid);
                rs = DatabaseTransaction.executeQuery(stmt);
                
                List constructInfoList = new ArrayList();
                while(rs.next()) {
                    int constructid = rs.getInt(1);
                    int oligoid5p = rs.getInt(2);
                    int oligoid3p = rs.getInt(3);
                    String constructType = rs.getString(4);
                    int projectid = rs.getInt(5);
                    String projectName = rs.getString(6);
                    int workflowid = rs.getInt(7);
                    String workflowName = rs.getString(8);
                    String status = rs.getString(9);
                    
                    if(status == null) {
                        status = ConstructInfo.IN_CLONING_PROCESS;
                    }
                    
                    ConstructInfo info = new ConstructInfo(constructid,oligoid5p,oligoid3p, constructType, projectid,projectName, workflowid, workflowName, status);
                    constructInfoList.add(info);
                }
                
                ConstructInfoBean constructInfo = new ConstructInfoBean(seqid, constructInfoList);
                infos.add(constructInfo);
            }
            
            CloneInfoSet infoSet = new CloneInfoSet();
            infoSet.restoreBySequenceid(seqids);
            List allClones = infoSet.getAllCloneInfo();
            for(int i=0; i<allClones.size(); i++) {
                CloneInfo cloneInfo = (CloneInfo)allClones.get(i);
                setCloneInfo(infos, cloneInfo);
            }
            
            return infos;
        } catch (Exception ex) {
            error = new String(ex.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getNoFounds(int searchid) {
        List noFounds = new ArrayList();
        String sql = "select * from searchresult where isfound='"+SearchResult.GENBANK_NOT_FOUND+"' and searchid="+searchid;
        String sql2 = "select * from notfound where searchresultid=?";
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement stmt = null;
        Connection conn = null;
        DatabaseTransaction t = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql2);
            
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int searchresultid = rs.getInt(1);
                String searchTerm = rs.getString(2);
                
                stmt.setInt(1, searchresultid);
                rs2 = DatabaseTransaction.executeQuery(stmt);
                if(rs2.next()) {
                    String reason = rs2.getString(2);
                    NoFound nf = new NoFound(searchTerm, reason);
                    noFounds.add(nf);
                }
            }
            
            return noFounds;
        } catch (Exception ex) {
            error = new String(ex.getMessage());
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getParams(int searchid) {
        String sql = "select * from param where searchid="+searchid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List params = new ArrayList();
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String paramName = rs.getString(1);
                String paramValue = rs.getString(2);
                Param param = new Param(paramName, paramValue);
                params.add(param);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return params;
    }
    
    private void setCloneInfo(List infos, CloneInfo cloneInfo) {
        for(int i=0; i<infos.size(); i++) {
            ConstructInfoBean info = (ConstructInfoBean)infos.get(i);
            if(info.getSequenceid() == cloneInfo.getRefsequenceid()) {
                List infoList = info.getConstructInfos();
                
                for(int k=0; k<infoList.size(); k++) {
                    ConstructInfo c = (ConstructInfo)infoList.get(k);
                    if(c.getConstructid() == cloneInfo.getConstructid()) {
                        c.addClone(cloneInfo);
                    }
                }
            }
        }
    }
    
    public static void main(String args[]) {
        QueryManager manager = new QueryManager();
        List seqids = new ArrayList();
        
        System.out.println("=============== test getSearchRecord ==============");
        SearchRecord record = manager.getSearchRecord(1);
        System.out.println(record.getSearchid());
        System.out.println(record.getSearchName());
        System.out.println(record.getSearchDate());
        System.out.println(record.getSearchStatus());
        System.out.println(record.getSearchType());
        
        System.out.println("============== test getSearchRecords ===============");
        List records = manager.getSearchRecords("dzuo");
        if(records == null) {
            System.out.println(manager.getError());
        } else {
            for(int i=0; i<records.size(); i++) {
                record = (SearchRecord)records.get(i);
                System.out.println(record.getSearchid());
                System.out.println(record.getSearchName());
                System.out.println(record.getSearchDate());
                System.out.println(record.getSearchStatus());
                System.out.println(record.getSearchType());
            }
        }
        
        System.out.println("================ Test getNumOfFounds, getNumOfNoFounds, getNumOfResults==============");
        System.out.println("Number of founds: "+manager.getNumOfFounds(1));
        System.out.println("Number of no founds: "+manager.getNumOfNoFounds(1));
        System.out.println("Number of results: "+manager.getNumOfResults(1));
        
        System.out.println("=============== Test getFounds ================");
        List founds = manager.getFounds(1);
        if(founds == null) {
            System.out.println(manager.getError());
        } else {
            for (int i=0; i<founds.size(); i++) {
                SearchResult result = (SearchResult)founds.get(i);
                System.out.println("search result id: "+result.getSearchResultid());
                System.out.println("term: "+result.getSearchTerm());
                System.out.println("is found: "+result.getIsGenbankFound());
                
                List mgss = result.getFound();
                for(int j=0; j<mgss.size(); j++) {
                    MatchGenbankRecord mgr = (MatchGenbankRecord)mgss.get(j);
                    System.out.println("\tmatch genbank id: "+mgr.getMatchGenbankId());
                    System.out.println("\tgenbank acc: "+mgr.getGanbankAccession());
                    System.out.println("\tgi: "+mgr.getGi());
                    System.out.println("\tsearch method: "+mgr.getSearchMethod());
                    
                    List mfss = mgr.getMatchFlexSequence();
                    for(int k=0; k<mfss.size(); k++) {
                        MatchFlexSequence mfs = (MatchFlexSequence)mfss.get(k);
                        seqids.add((new Integer(mfs.getFlexsequenceid())).toString());
                        System.out.println("\t\tmatch flex id: "+mfs.getMatchFlexId());
                        System.out.println("\t\tis match by gi: "+mfs.getIsMatchByGi());
                        System.out.println("\t\tflex sequence id: "+mfs.getFlexsequenceid());
                        
                        BlastHit bh = mfs.getBlastHit();
                        if(bh != null) {
                            System.out.println("\t\tquery length: "+bh.getQueryLength());
                            System.out.println("\t\tsub length: "+bh.getSubjectLength());
                        }
                        
                        FlexSequence seq = mfs.getFlexSequence();
                        System.out.println("\t\tsequenceid: "+seq.getId());
                        System.out.println("\t\tstatus: "+seq.getFlexstatus());
                        System.out.println("\t\tspecies: "+seq.getSpecies());
                        System.out.println("\t\tstart: "+seq.getCdsstart());
                        System.out.println("\t\tstop: "+seq.getCdsstop());
                        System.out.println("\t\tlength: "+seq.getCdslength());
                        System.out.println("\t\tgc content: "+seq.getGccontent());
                    }
                }
            }
        }
        
        System.out.println("=============== test getNoFounds ==============");
        List noFounds = manager.getNoFounds(1);
        if(noFounds== null) {
            System.out.println(manager.getError());
        } else {
            for(int n=0; n<noFounds.size(); n++) {
                NoFound nf = (NoFound)noFounds.get(n);
                System.out.println("term: "+nf.getSearchTerm());
                System.out.println("reason: "+nf.getReason());
            }
        }
        
        System.out.println("================ test getConstructInfo ============");
        List sequenceids = new ArrayList();
        sequenceids.add("36476");
        List constructInfos = manager.getConstructInfo(sequenceids);
        if(constructInfos == null) {
            System.out.println(manager.getError());
            System.exit(0);
        }
        
        for(int n=0; n<constructInfos.size(); n++) {
            ConstructInfoBean constructInfo = (ConstructInfoBean)constructInfos.get(n);
            int seqid = constructInfo.getSequenceid();
            System.out.println("seqid: "+seqid);
            List infoList = constructInfo.getConstructInfos();
            for(int i=0; i<infoList.size(); i++) {
                ConstructInfo info = (ConstructInfo)infoList.get(i);
                System.out.println("\t"+info.getConstructid());
                System.out.println("\t"+info.getConstructType());
                System.out.println("\t"+info.getOligoid5p());
                System.out.println("\t"+info.getOligoid3p());
                System.out.println("\t"+info.getProjectName());
                System.out.println("\t"+info.getWorkflowName());
                System.out.println("\t"+info.getStatus());
                
                List clones = info.getClones();
                for(int k=0; k<clones.size(); k++) {
                    CloneInfo clone = (CloneInfo)clones.get(k);
                    System.out.println("\t\t"+clone.getConstructid());
                    System.out.println("\t\t"+clone.getCloneid());
                    System.out.println("\t\t"+clone.getClonename());
                    System.out.println("\t\t"+clone.getClonetype());
                    System.out.println("\t\t"+clone.getMastercloneid());
                    System.out.println("\t\t"+clone.getStatus());
                    
                    CloningStrategy cs = clone.getCloningstrategy();
                    System.out.println("\t\t"+cs.getId());
                    System.out.println("\t\t"+cs.getName());
                    
                    NameInfo names = clone.getNameinfo();
                    System.out.println("\t\t"+names.getGenbank());
                }
            }
        }
        
        List params = manager.getParams(1);
        for(int i=0; i<params.size(); i++) {
            Param p = (Param)params.get(i);
            System.out.println(p.getName()+"\t"+p.getValue());
        }
    }
}
