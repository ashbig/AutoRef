/*
 * GeneQueryHandler.java
 *
 * Created on April 15, 2005, 2:10 PM
 */

package plasmid.query.handler;

import java.util.*;
import java.sql.*;

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.Constants;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public abstract class GeneQueryHandler {
    public static final String GENBANK = RefseqNameType.GENBANK;
    public static final String GI = RefseqNameType.GI;
    public static final String GENEID = RefseqNameType.GENEID;
    public static final String SYMBOL = RefseqNameType.SYMBOL;
    public static final String DIRECT_GENBANK = "Direct Genbank";
    public static final String DIRECT_GI = "Direct GI";
    public static final String PA = RefseqNameType.PA;
    public static final String PRO_GENBANK = RefseqNameType.PRO_GENBANK;
    public static final String PRO_GI = RefseqNameType.PRO_GI;
    public static final String SGD = RefseqNameType.SGD;
    public static final String VCNUMBER = RefseqNameType.VCNUMBER;
    public static final String FTNUMBER = RefseqNameType.FTNUMBER;
    public static final String FBID = RefseqNameType.FBID;
    public static final String WBGENEID = RefseqNameType.WBGENEID;
    public static final String TAIRID = RefseqNameType.TAIR;
    public static final String PLASMIDCLONEID = Constants.CLONE_SEARCH_PLASMIDCLONEID;
    public static final String OTHERCLONEID = Constants.CLONE_SEARCH_OTHERCLONEID;
    public static final String GENETEXT = "Gene Text Exact Match";
    public static final String GENETEXTCONTAIN = "Gene Text Match";
    public static final String VECTORNAMETEXT = "Vector Name Exact Match";
    public static final String VECTORNAMECONTAIN = "Vector Name Match";
    public static final String VECTORFEATURETEXT = "Vector Feature Exact Match";
    public static final String VECTORFEATURECONTAIN = "Vector Feature Match";
    public static final String AUTHORTEXT = "Author Name Exact Match";
    public static final String AUTHORCONTAIN = "Author Name Contain";
    public static final String PMIDMATCH = "PMID Exact Match";
    public static final String CLONEPROPERTY = "Clone Property";
    
    protected Map found;
    protected Map totalFoundCloneids;
    protected List nofound;
    protected List terms;
    protected Map foundCounts;
    protected int foundCloneCount;
    // protected int noFoundTotal;
    // protected List totalFoundCloneids;
    private boolean isClonename;
    
    /** Creates a new instance of GeneQueryHandler */
    public GeneQueryHandler() {
    }
    
    public GeneQueryHandler(List terms) {
        this.terms = terms;
        this.found = new TreeMap();
        this.totalFoundCloneids = new TreeMap();
        this.nofound = new ArrayList();
        this.foundCounts = new HashMap();
        //this.totalFoundCloneids = new ArrayList();
    }
    
    public Map getFound() {return found;}
    public List getNofound() {return nofound;}
    public List getTerms() {return terms;}
    public Map getFoundCounts() {return foundCounts;}
    public int getFoundCloneCount() {return foundCloneCount;}
    // public int getNoFoundTotal() {return noFoundTotal;}
    public Map getTotalFoundCloneids() {return totalFoundCloneids;}
    
    public int getNumOfClones(String term) {
        int n = 0;
        List clones = (List)found.get(term);
        if(clones != null)
            n=clones.size();
        
        return n;
    }
    
    public abstract void doQuery() throws Exception;
    
    public abstract void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception;
    
    public abstract void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception;
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
    }
    
    public int getNumOfFoundClones() {
        return found.size();
    }
    
    public int getNumOfNoFoundClones() {
        return nofound.size();
    }
    
    protected void executeQuery(String sql) throws Exception {
        executeQuery(sql, null, null, null, null);
    }
    
    protected void executeQuery(String sql, List restrictions, List clonetypes, String species, int start, int end, String sortColumn, String status) throws Exception {
        executeQuery(sql, restrictions, clonetypes, species, start, end, sortColumn, status, 1, false);
    }
    
    protected void executeQuery(String sql, List restrictions, List clonetypes, String species, int start, int end, String sortColumn, String status, int num, boolean isLike) throws Exception {
        executeQuery(sql,restrictions,clonetypes,species,start,end,sortColumn,status,num,isLike,true);
    }
    
    protected void executeQuery(String sql, List restrictions, List clonetypes, String species, int start, int end, String sortColumn, String status, int num, boolean isLike, boolean isGrowth) throws Exception {
        if(terms == null || terms.size() == 0)
            return;
        
        foundCloneCount = 0;
        nofound.addAll(terms);
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = null;
        Map foundClones = null;
        
        try {
            conn = t.requestConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            Set cloneids = new TreeSet();
            int n = 0;
            for(int i=0; i<terms.size(); i++) {
                String term = (String)terms.get(i);
                if(isLike)
                    term = "%"+term+"%";
                for(int m=0; m<num; m++) {
                    stmt.setString(m+1, term);
                }
                rs = DatabaseTransaction.executeQuery(stmt);
                List clones = new ArrayList();
                List totalClones = new ArrayList();
                while(rs.next()) {
                    String cloneid = new Integer(rs.getInt(1)).toString();
                    if((n>=start && n<end) || (start==-1 && end==-1)) {
                        clones.add(cloneid);
                        cloneids.add(cloneid);
                    }
                    totalClones.add(cloneid);
                    n++;
                }
                if(clones.size() > 0)
                    found.put(term, clones);
                
                if(totalClones.size() > 0)
                    totalFoundCloneids.put(term, totalClones);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            CloneManager manager = new CloneManager(conn);
            foundClones = manager.queryClonesByCloneid(new ArrayList(cloneids), true, true, false, isGrowth, restrictions,clonetypes,species,status,isIsClonename());
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        /**
         * Set ks = foundClones.keySet();
         * Iterator it = ks.iterator();
         * while(it.hasNext()) {
         * String s = (String)it.next();
         * Clone c = (Clone)foundClones.get(s);
         * }
         */
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        Map newFound = new HashMap();
        while(iter.hasNext()) {
            String k = (String)iter.next();
            List clones = (ArrayList)found.get(k);
            List newClones = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                String cid = (String)clones.get(i);
                Clone c = (Clone)foundClones.get(cid);
                if(c != null)
                    newClones.add(c);
            }
            if(newClones.size()>0) {
                newFound.put(k, newClones);
                foundCounts.put(k, new Integer(newClones.size()));
                foundCloneCount += newClones.size();
            }
        }
        
        found = newFound;
        nofound.removeAll(found.keySet());
    }
    
    protected void executeQuery(String sql, List restrictions, List clonetypes, String species, String status) throws Exception {
        executeQuery(sql,restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void filterFoundByClonetype(List clonetypes) {
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        Map newFound = new HashMap();
        foundCounts = new HashMap();
        foundCloneCount = 0;
        while(iter.hasNext()) {
            String k = (String)iter.next();
            List clones = (ArrayList)found.get(k);
            List newClones = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                Clone c = (Clone)clones.get(i);
                String clonetype = c.getType();
                if(clonetypes.contains(clonetype)) {
                    newClones.add(c);
                }
            }
            
            if(newClones.size()>0) {
                newFound.put(k, newClones);
                foundCounts.put(k, new Integer(newClones.size()));
                foundCloneCount += newClones.size();
            } else {
                nofound.add(k);
            }
        }
        
        found = newFound;
    }
    
    public List convertFoundToCloneinfo() {
        if(found == null)
            return null;
        
        List cloneinfos = new ArrayList();
        
        Set keys = found.keySet();
        Iterator iter = keys.iterator();
        while(iter.hasNext()) {
            String term = (String)iter.next();
            List clones = (List)found.get(term);
            for(int i=0; i<clones.size(); i++) {
                Clone c = (Clone)clones.get(i);
                CloneInfo ci = new CloneInfo(term, c);
                ci.setIsAddedToCart(false);
                cloneinfos.add(ci);
            }
        }
        
        return cloneinfos;
    }
    
    protected List executeQueryClones(String sql) {
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                String clonename = rs.getString(2);
                String clonetype = rs.getString(3);
                String status = rs.getString(4);
                String verified = rs.getString(5);
                String vermethod = rs.getString(6);
                String domain = rs.getString(7);
                String subdomain = rs.getString(8);
                String restriction = rs.getString(9);
                String comments = rs.getString(10);
                String mapfilename = rs.getString(11);
                int vectorid = rs.getInt(12);
                String vectorname = rs.getString(13);
                String specialtreatment = rs.getString(14);
                String source = rs.getString(15);
                String description = rs.getString(16);
                Clone c = new Clone(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, mapfilename, status, specialtreatment, source, description);
                clones.add(c);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return clones;
    }
    
    public int queryTotalFoundCloneCounts(List restrictions, List clonetypes, String species, String status) {
        String sql = "select count(*) from clone where cloneid=?";
        
        if(clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql+" and clonetype in ("+s+")";
        }
        
        if(restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql+" and restriction in ("+s+")";
        }
        
        if(species != null) {
            sql = sql+" and domain='"+species+"'";
        }
        
        if(status != null)
            sql += " and status='"+status+"'";
        
        return CloneManager.queryCloneCounts(totalFoundCloneids, nofound, sql);
    }

    public boolean isIsClonename() {
        return isClonename;
    }

    public void setIsClonename(boolean isClonename) {
        this.isClonename = isClonename;
    }
}
