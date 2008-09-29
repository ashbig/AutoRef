/*
 * ReagentDAO.java
 *
 * Created on July 18, 2007, 1:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dao;

import core.ReagentInfo;
import database.DatabaseException;
import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transfer.ReagentTO;

/**
 *
 * @author dzuo
 */
public class ReagentDAO {
    
    protected Connection conn = null;

    /** Creates a new instance of ReagentDAO */
    public ReagentDAO() {
    }

    public ReagentDAO(Connection conn) {
        this.conn = conn;
    }

    public void setReagentids(List reagents) throws DaoException {
        int id = SequenceDAO.getNextid("Reagent", "reagentid");
        for (int i = 0; i < reagents.size(); i++) {
            ReagentTO r = (ReagentTO) reagents.get(i);
            r.setReagentid(id);
            id++;
        }
    }

    public Map getReagents(List<ReagentInfo> info) throws DaoException {
        String sql = "select * from reagent where name=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map reagents = new HashMap();

        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            for (ReagentInfo r : info) {
                stmt.setString(1, r.getName());
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    String type = rs.getString(3);
                    String desc = rs.getString(4);
                    ReagentTO reagent = new ReagentTO(id, name, type, desc);
                    reagents.put(r, reagent);
                } else {
                    reagents.put(r, null);
                }
            }
        } catch (DatabaseException ex) {
            throw new DaoException("Cannot get connection: " + ex.getMessage());
        } catch (Exception ex) {
            throw new DaoException("Cannot get reagents from the database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }

        return reagents;
    }

    public static List<ReagentTO> getReagentList(List<String> names) throws DaoException {
        String sql = "select * from reagent where name=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List reagents = new ArrayList<ReagentTO>();

        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            for (String n : names) {
                stmt.setString(1, n);
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String name = rs.getString(2);
                    String type = rs.getString(3);
                    String desc = rs.getString(4);
                    ReagentTO reagent = new ReagentTO(id, name, type, desc);
                    reagents.add(reagent);
                } else {
                    throw new DaoException("Cannot get reagent for " + n);
                }
            }
        } catch (DatabaseException ex) {
            throw new DaoException("Cannot get connection: " + ex.getMessage());
        } catch (Exception ex) {
            throw new DaoException("Cannot get reagents from the database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        return reagents;
    }

    public static ReagentTO getReagent(String name) throws DaoException {
        String sql = "select reagentid,type,description from reagent where lower(name)=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ReagentTO reagent = null;

        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            {
                stmt.setString(1, name.toLowerCase());
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String type = rs.getString(2);
                    String desc = rs.getString(3);
                    reagent = new ReagentTO(id, name, type, desc);
                }
            }
        } catch (DatabaseException ex) {
            throw new DaoException("Cannot get connection: " + ex.getMessage());
        } catch (Exception ex) {
            throw new DaoException("Cannot get reagents from the database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }

        return reagent;
    }

    public static ReagentTO getReagent(int id) throws DaoException {
        String sql = "select name,type,description from reagent where name=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ReagentTO reagent = null;

        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            {
                stmt.setInt(1, id);
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    String name = rs.getString(1);
                    String type = rs.getString(2);
                    String desc = rs.getString(3);
                    reagent = new ReagentTO(id, name, type, desc);
                }
            }
        } catch (DatabaseException ex) {
            throw new DaoException("Cannot get connection: " + ex.getMessage());
        } catch (Exception ex) {
            throw new DaoException("Cannot get reagents from the database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }

        return reagent;
    }

    public static List<ReagentTO> getReagents(String type) throws DaoException {
        String sql = "select reagentid,name,description from reagent where type=?";
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List reagents = new ArrayList<ReagentTO>();

        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            stmt.setString(1, type);
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String desc = rs.getString(3);
                ReagentTO reagent = new ReagentTO(id, name, type, desc);
                reagents.add(reagent);
            }
        } catch (DatabaseException ex) {
            throw new DaoException("Cannot get connection: " + ex.getMessage());
        } catch (Exception ex) {
            throw new DaoException("Cannot get reagents from the database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        return reagents;
    }

    public void addReagents(List reagents) throws DaoException {
        setReagentids(reagents);

        String sql = "insert into reagent values(?, ?, ?, ?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < reagents.size(); i++) {
                ReagentTO r = (ReagentTO) reagents.get(i);
                stmt.setInt(1, r.getReagentid());
                stmt.setString(2, r.getName());
                stmt.setString(3, r.getType());
                stmt.setString(4, r.getDesc());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding reagents to the database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        addSpecificReagents(reagents);
    }

    public void addSpecificReagents(List reagents) throws DaoException {
    }

    public static void main(String args[]) {
        ReagentDAO dao = new CloneDAO();
        List<ReagentTO> reagents = new ArrayList<ReagentTO>();
        reagents.add(new ReagentTO("A", ReagentTO.getTYPE_CLONE(), "test"));
        reagents.add(new ReagentTO("B", ReagentTO.getTYPE_CLONE(), "test"));
        reagents.add(new ReagentTO("C", ReagentTO.getTYPE_CLONE(), "test"));

        try {
            dao.addReagents(reagents);
            ReagentTO r = dao.getReagent("B");
            System.out.println(r.getName());
            System.out.println(r.getType());
            System.out.println(r.getDesc());
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
