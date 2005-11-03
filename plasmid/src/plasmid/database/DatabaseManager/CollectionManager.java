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
        String sql = "select name,description,protocolname,protocolfile,price,status"+
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
                String protocolname = rs.getString(3);
                String protocolfile = rs.getString(4);
                double price = rs.getDouble(5);
                String s = rs.getString(6);
                CollectionInfo info = new CollectionInfo(name, description, protocolname, protocolfile, price, s);
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
        String sql = "select description,protocolname,protocolfile,price,status"+
        " from collection where name='"+name+"'";

        DatabaseTransaction t = null;
        ResultSet rs = null;
        CollectionInfo info = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                String description = rs.getString(1);
                String protocolname = rs.getString(2);
                String protocolfile = rs.getString(3);
                double price = rs.getDouble(4);
                String s = rs.getString(5);
                info = new CollectionInfo(name, description, protocolname, protocolfile, price, s);
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Database error occured.");
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return info;
    }
}
