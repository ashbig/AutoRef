/*
 * AuthorManager.java
 *
 * Created on April 13, 2005, 3:15 PM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class AuthorManager extends TableManager {
    
    /** Creates a new instance of AuthorManager */
    public AuthorManager(Connection conn) {
       super(conn);
    }
    
    public boolean insertAuthors(List authors) {
        if(authors == null)
            return true;
        
        String sql = "insert into authorinfo"+
        " (authorid, authorname, firstname, lastname, tel, fax, authoremail, address, www, description)"+
        " values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<authors.size(); i++) {
                Authorinfo a = (Authorinfo)authors.get(i);
                stmt.setInt(1, a.getAuthorid());
                stmt.setString(2, a.getName());
                stmt.setString(3, a.getFirstname());
                stmt.setString(4, a.getLastname());
                stmt.setString(5, a.getTel());
                stmt.setString(6, a.getFax());
                stmt.setString(7, a.getEmail());
                stmt.setString(8, a.getAddress());
                stmt.setString(9, a.getWww());
                stmt.setString(10, a.getDescription());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into AUTHORINFO table");
            return false;
        }
        return true;
    }
    
    public int getAuthorid(String name) {
        String sql = "select authorid from authorinfo where authorname=?";
        int id = 0;
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            if(rs.next()) {
                id = rs.getInt(1);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        }
        
        return id;
    }
}
