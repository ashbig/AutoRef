/*
 * GeneQueryHandler.java
 *
 * Created on April 15, 2005, 2:10 PM
 */

package plasmid.query.handler;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.Constants;

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
    
    protected Map found;
    protected List nofound;
    protected List terms;
    protected Map foundCounts;
    protected int foundCloneCount;
    
    /** Creates a new instance of GeneQueryHandler */
    public GeneQueryHandler() {
    }
    
    public GeneQueryHandler(List terms) {
        this.terms = terms;
        this.found = new HashMap();
        this.nofound = new ArrayList();
        this.foundCounts = new HashMap();
    }
    
    public Map getFound() {return found;}
    public List getNofound() {return nofound;}
    public List getTerms() {return terms;}
    public Map getFoundCounts() {return foundCounts;}
    public int getFoundCloneCount() {return foundCloneCount;}
    
    public int getNumOfClones(String term) {
        int n = 0;
        List clones = (List)found.get(term);
        if(clones != null)
            n=clones.size();
        
        return n;
    }
    
    public abstract void doQuery() throws Exception;
    
    public abstract void doQuery(List restrictions, List clonetypes, String species) throws Exception;
    
    public int getNumOfFoundClones() {
        return found.size();
    }
    
    public int getNumOfNoFoundClones() {
        return nofound.size();
    }
    
    protected void executeQuery(String sql) throws Exception {
        executeQuery(sql, null, null, null);
    }
    
    protected void executeQuery(String sql, List restrictions, List clonetypes, String species) throws Exception {
        if(terms == null || terms.size() == 0)
            return;
        
        foundCloneCount = 0;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        Set cloneids = new TreeSet();
        for(int i=0; i<terms.size(); i++) {
            String term = (String)terms.get(i);
            stmt.setString(1, term);
            rs = DatabaseTransaction.executeQuery(stmt);
            List clones = new ArrayList();
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                clones.add(new Integer(cloneid).toString());
                cloneids.add(new Integer(cloneid).toString());
            }
            if(clones.size() > 0)
                found.put(term, clones);
            else
                nofound.add(term);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        
        CloneManager manager = new CloneManager(conn);
        Map foundClones = manager.queryAvailableClonesByCloneid(new ArrayList(cloneids), true, true, false,restrictions,clonetypes,species);
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
            } else {
                nofound.add(k);
            }
        }
        
        found = newFound;
        DatabaseTransaction.closeConnection(conn);
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
}
