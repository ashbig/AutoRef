/*
 * CollectionManager.java
 *
 * Created on November 3, 2005, 2:59 PM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.DatabaseTransaction;
import plasmid.Constants;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CollectionManager extends TableManager {
    
    /** Creates a new instance of CollectionManager */
    public CollectionManager(Connection conn) {
        super(conn);
    }
    
    public static List getAllCollections(String status, List restrictions) {
        String sql = "select name,description,price,status,restriction,memberprice"+
        " from collection";
        
        boolean isWhere = true;
        if(status != null) {
            sql += " where status='"+status+"'";
            isWhere = false;
        }
        
        if(restrictions != null && restrictions.size()>0) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            if(isWhere) {
                sql += " where restriction in ("+s+")";
            } else {
                sql = sql+" and restriction in ("+s+")";
            }
        }
        
        List infos = new ArrayList();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                double price = rs.getDouble(3);
                String s = rs.getString(4);
                String restriction = rs.getString(5);
                double memberprice = rs.getDouble(6);
                CollectionInfo info = new CollectionInfo(name, description, price, memberprice, s, restriction);
                infos.add(info);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Database error occured.");
                System.out.println(ex);
                return null;
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return infos;
    }
    
    public Map getCollections(Connection conn, List names) {
        String sql = "select description,price,status,restriction,memberprice"+
        " from collection where name=?";
        PreparedStatement stmt = null;
        Map collections = new HashMap();
        String currentName = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<names.size(); i++) {
                String name = (String)names.get(i);
                currentName = name;
                stmt.setString(1, name);
                ResultSet rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    String description = rs.getString(1);
                    double price = rs.getDouble(2);
                    String s = rs.getString(3);
                    String restriction = rs.getString(4);
                    double memberprice = rs.getDouble(5);
                    CollectionInfo info = new CollectionInfo(name, description, price, memberprice, s, restriction);
                    collections.put(name, info);
                    DatabaseTransaction.closeResultSet(rs);
                } else {
                    throw new Exception("Cannot get collection by name: "+name);
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while query collection by name: "+currentName);
            return null;
        }
        return collections;
    }
    
    public static CollectionInfo getCollection(String name, boolean isCloneRestore, boolean isCloneStorage) {
        String sql = "select description,price,status,restriction,memberprice"+
        " from collection where name='"+name+"'";
        
        return CollectionManager.doGetCollection(sql, name, isCloneRestore, isCloneStorage);
    }
    
    public static CollectionInfo getCollection(String name, String status, List restrictions, boolean isCloneRestore, boolean isCloneStorage) {
        String sql = "select description,price,status,restriction,memberprice"+
        " from collection where name='"+name+"'";
        
        if(status != null) {
            sql += " and status='"+status+"'";
        }
        
        if(restrictions != null && restrictions.size()>0) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql+" and restriction in ("+s+")";
        }
        
        return CollectionManager.doGetCollection(sql, name, isCloneRestore, isCloneStorage);
    }
    
    private static CollectionInfo doGetCollection(String sql, String name, boolean isCloneRestore, boolean isCloneStorage) {
        String sql1 = "select a.authorid, a.authorname, ca.authortype"+
        " from authorinfo a, collectionauthor ca"+
        " where a.authorid=ca.authorid and ca.collectionname='"+name+"'";
        String sql2 = "select p.title, p.pmid, p.publicationid"+
        " from publication p, collectionpublication cp"+
        " where p.publicationid=cp.publicationid and cp.collectionname='"+name+"'";
        String sql3 = "select p.name, p.filename, p.description"+
        " from protocol p, collectionprotocol cp"+
        " where p.name=cp.protocolname and cp.collectionname='"+name+"'";
        String sql4 = "select cloneid from clonecollection where name='"+name+"'";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        CollectionInfo info = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String description = rs.getString(1);
                double price = rs.getDouble(2);
                String s = rs.getString(3);
                String restriction = rs.getString(4);
                double memberprice = rs.getDouble(5);
                info = new CollectionInfo(name, description, price, memberprice, s, restriction);
            } else {
                if(Constants.DEBUG) {
                    System.out.println("Cannot get collection from database using collection name: "+name);
                }
                return null;
            }
            
            List authors = new ArrayList();
            rs1 = t.executeQuery(sql1);
            while(rs1.next()) {
                int authorid = rs1.getInt(1);
                String authorname = rs1.getString(2);
                String authortype = rs1.getString(3);
                CollectionAuthor author = new CollectionAuthor(name,authorid,authorname,authortype);
                authors.add(author);
            }
            
            List publications = new ArrayList();
            rs2 = t.executeQuery(sql2);
            while(rs2.next()) {
                String title = rs2.getString(1);
                String pmid = rs2.getString(2);
                int pubid = rs2.getInt(3);
                Publication p = new Publication(pubid,title,pmid);
                publications.add(p);
            }
            
            List protocols = new ArrayList();
            rs3 = t.executeQuery(sql3);
            while(rs3.next()) {
                String protocolname = rs3.getString(1);
                String filename = rs3.getString(2);
                String desc = rs3.getString(3);
                Protocol p = new Protocol(protocolname,filename,desc);
                protocols.add(p);
            }
            
            if(isCloneRestore) {
                List cloneids = new ArrayList();
                rs4 = t.executeQuery(sql4);
                while(rs4.next()) {
                    int cloneid = rs4.getInt(1);
                    cloneids.add((new Integer(cloneid)).toString());
                }
                
                CloneManager m = new CloneManager(conn);
                Map clones = m.queryClonesByCloneid(cloneids, true, true, isCloneStorage);
                if(clones == null) {
                    throw new Exception("Cannot get clones from database.");
                }
                info.setClones(new ArrayList(clones.values()));
            }
            
            info.setAuthors(authors);
            info.setPublications(publications);
            info.setProtocols(protocols);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Database error occured.");
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs1);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeResultSet(rs4);
            DatabaseTransaction.closeConnection(conn);
        }
        
        return info;
    }
}
