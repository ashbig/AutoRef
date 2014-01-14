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

    public boolean insertVectorAuthors(List as) {
        if (as == null) {
            return true;
        }
        String sql = "insert into vectorauthor (vectorid, authorid, authortype, creationdate) values (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Date d = new Date(a.getCreationdate());
            for (int i = 0; i < as.size(); i++) {
                Authorinfo a = (Authorinfo) as.get(i);
                stmt.setInt(1, a.getVectorid());
                stmt.setInt(2, a.getAuthorid());
                stmt.setString(3, a.getAuthortype());
                stmt.setString(4, a.getCreationdate());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORAUTHOR table");
            return false;
        }
        return true;
    }

    public boolean insertVectorAuthor(Authorinfo a) {
        if (a == null) {
            return true;
        }
        String sql = "insert into vectorauthor (vectorid, authorid, authortype, creationdate) values (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Date d = new Date(a.getCreationdate());

            stmt.setInt(1, a.getVectorid());
            stmt.setInt(2, a.getAuthorid());
            stmt.setString(3, a.getAuthortype());
            stmt.setString(4, a.getCreationdate());

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORAUTHOR table");
            return false;
        }
        return true;
    }

    public boolean insertVectorAuthor(int vectorid, int authorid, String authortype, String creationdate) {
        if ((vectorid < 1) || (authorid < 1) || (authortype == null) || (authortype.length() < 1) || (creationdate == null) || (creationdate.length() < 1)) {
            return true;
        }
        String sql = "insert into vectorauthor (vectorid, authorid, authortype, creationdate) values (?, ?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Date d = new Date(a.getCreationdate());

            stmt.setInt(1, vectorid);
            stmt.setInt(2, authorid);
            stmt.setString(3, authortype);
            stmt.setString(4, creationdate);

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORAUTHOR table");
            return false;
        }
        return true;
    }

    public boolean insertAuthor(Authorinfo a) {
        if (a == null) {
            return true;
        }
        String sql = "insert into authorinfo" +
                " (authorid, authorname, firstname, lastname, tel, fax, authoremail, address, www, description)" +
                " values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

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
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into AUTHORINFO table");
            return false;
        }
        return true;
    }

    public boolean insertAuthors(List authors) {
        if (authors == null) {
            return true;
        }
        String sql = "insert into authorinfo" +
                " (authorid, authorname, firstname, lastname, tel, fax, authoremail, address, www, description)" +
                " values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < authors.size(); i++) {
                Authorinfo a = (Authorinfo) authors.get(i);
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
            if (rs.next()) {
                id = rs.getInt(1);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return id;
    }

    public List getAuthorsByName(String name) {
        if (name == null) {
            return null;
        }
        name = name.trim().toUpperCase();
        if (name.length() < 1) {
            return null;
        }
        String sql = "select authorid, authorname, lastname, firstname, tel, fax, authoremail, address, www, description from authorinfo where upper(authorname) like ?";
        List va = new ArrayList();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + name.trim().toUpperCase() + "%");
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                Authorinfo ai = new Authorinfo(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10));
                va.add(ai);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return va;
    }

    public List getVectorAuthorsByID(int vid) {
        if (vid < 1) {
            return null;
        }
        String sql = "select a.vectorid, a.creationdate, a.authortype, a.authorid, b.authorname from vectorauthor a, authorinfo b where a.vectorid=? and a.authorid=b.authorid";
        List va = new ArrayList();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vid);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                Authorinfo ai = new Authorinfo(rs.getInt(4), rs.getString(5),
                        "", "", "", "", "", "", "", "",
                        rs.getInt(1), rs.getString(2), rs.getString(3));
                va.add(ai);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return va;
    }

    public boolean updateVectorAuthors(List as) {
        if (as == null) {
            return true;
        }
        String sql1 = "delete from vectorauthor where vectorid=?";
        String sql2 = "insert into vectorauthor (vectorid, authorid, authortype) values (?, ?, ?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            Authorinfo a = (Authorinfo) as.get(0);
            stmt1.setInt(1, a.getVectorid());
            DatabaseTransaction.executeUpdate(stmt1);
            DatabaseTransaction.closeStatement(stmt1);
            
            for (int i = 0; i < as.size(); i++) {
                a = (Authorinfo) as.get(i);
                stmt2.setInt(1, a.getVectorid());
                stmt2.setInt(2, a.getAuthorid());
                stmt2.setString(3, a.getAuthortype());

                DatabaseTransaction.executeUpdate(stmt2);
            }
            DatabaseTransaction.closeStatement(stmt2);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORAUTHOR table");
            return false;
        }
        return true;
    }
}
