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

/**
 *
 * @author  DZuo
 */
public class CollectionManager extends TableManager {
    
    /** Creates a new instance of CollectionManager */
    public CollectionManager(Connection conn) {
        super(conn);
    }
    
    public static List getAllCollections(String status) {
        String sql = "select name,description,price,status,restriction"+
        " from collection";
        
        if(status != null) {
            sql += " where status="+status;
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
                CollectionInfo info = new CollectionInfo(name, description, price, s, restriction);
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
    
    public static CollectionInfo getCollection(String name) {
        String sql = "select description,price,status,restriction"+
        " from collection where name='"+name+"'";
        String sql1 = "select a.authorid, a.authorname, ca.authortype"+
        " from authorinfo a, collectionauthor ca"+
        " where a.authorid=ca.authorid and ca.collectionname='"+name+"'";
        String sql2 = "select p.title, p.pmid, p.publicationid"+
        " from publication p, collectionpublication cp"+
        " where p.publicationid=cp.publicationid and cp.collectionname='"+name+"'";
        String sql3 = "select p.name, p.filename, p.description"+
        " from protocol p, collectionprotocol cp"+
        " where p.name=cp.protocolname and cp.collectionname='"+name+"'";
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        CollectionInfo info = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String description = rs.getString(1);
                double price = rs.getDouble(2);
                String s = rs.getString(3);
                String restriction = rs.getString(4);
                info = new CollectionInfo(name, description, price, s, restriction);
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
        }
        
        return info;
    }
}
