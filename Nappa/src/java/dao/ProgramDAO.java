/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import transfer.FilereferenceTO;
import transfer.ProgramcontainerTO;
import transfer.ProgramdefinitionTO;
import transfer.ProgrammappingTO;

/**
 *
 * @author dzuo
 */
public class ProgramDAO {

    private Connection conn;

    public ProgramDAO() {
    }

    public ProgramDAO(Connection conn) {
        this.setConn(conn);
    }

    public void addProgramdefinition(ProgramdefinitionTO p) throws DaoException {
        String sql = "insert into programdefinition(name,description,type,status,sourcenum,destnum,createdate,researcher)" +
                " values(?,?,?,?,?,?,sysdate,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setString(3, p.getType());
            stmt.setString(4, p.getStatus());
            stmt.setInt(5, p.getSourcenum());
            stmt.setInt(6, p.getDestnum());
            stmt.setString(7, p.getResearcher());
            DatabaseTransaction.executeUpdate(stmt);

            addProgrammapping(p.getName(), p.getMappings());
            addProgramcontainers(p.getName(), p.getContainers());
            addFiles(p.getName(), p.getFiles());
        } catch (Exception ex) {
            throw new DaoException("Error occured while add program to the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void addProgrammapping(String programname, List<ProgrammappingTO> mappings) throws DaoException {
        String sql = "insert into programmapping(mapid, programname,srcplate,srcpos,srcwellx,srcwelly,destplate,destpos," +
                " destwellx,destwelly,destblocknum,destblockrow,destblockcol,destblockposx,destblockposy,destblockwellx,destblockwelly)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        setProgrammappingids(mappings);

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            for (ProgrammappingTO m : mappings) {
                stmt.setInt(1, m.getMapid());
                stmt.setString(2, programname);
                stmt.setString(3, m.getSrcplate());
                stmt.setInt(4, m.getSrcpos());
                stmt.setString(5, m.getSrcwellx());
                stmt.setString(6, m.getSrcwelly());
                stmt.setString(7, m.getDestplate());
                stmt.setInt(8, m.getDestpos());
                stmt.setString(9, m.getDestwellx());
                stmt.setString(10, m.getDestwelly());
                stmt.setInt(11, m.getDestblocknum());
                stmt.setInt(12, m.getDestblockrow());
                stmt.setInt(13, m.getDestblockcol());
                stmt.setInt(14, m.getDestblockposx());
                stmt.setInt(15, m.getDestblockposy());
                stmt.setInt(16, m.getDestblockwellx());
                stmt.setInt(17, m.getDestblockwelly());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding mappings to the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void addProgramcontainers(String programname, List<ProgramcontainerTO> containers) throws DaoException {
        String sql = "insert into programcontainer(programname,name,type,orders,ioflag) values(?,?,?,?,?)";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            for (ProgramcontainerTO m : containers) {
                stmt.setString(1, programname);
                stmt.setString(2, m.getName());
                stmt.setString(3, m.getType());
                stmt.setInt(4, m.getOrder());
                stmt.setString(5, m.getIoflag());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding containers to the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void addFiles(String programname, List<FilereferenceTO> files) throws DaoException {
        String sql = "insert into programfile(programname, filereferenceid) values(?,?)";

        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);

            for (FilereferenceTO file : files) {
                stmt.setString(1, programname);
                stmt.setInt(2, file.getFilereferenceid());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding program files to the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void setProgrammappingids(List<ProgrammappingTO> mappings) throws DaoException {
        int id = SequenceDAO.getNextid("mapid");
        for (ProgrammappingTO m : mappings) {
            m.setMapid(id);
            id++;
        }
    }

    public List<ProgramdefinitionTO> getAllPrograms() throws DaoException {
        String sql = "select name, description,type,status,sourcenum,destnum,createdate,researcher from programdefinition order by name";
        DatabaseTransaction t = null;
        List<ProgramdefinitionTO> programs = new ArrayList<ProgramdefinitionTO>();
        try {
            t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                String type = rs.getString(3);
                String status = rs.getString(4);
                int srcnum = rs.getInt(5);
                int destnum = rs.getInt(6);
                String date = (rs.getDate(7)).toString();
                String researcher = rs.getString(8);
                ProgramdefinitionTO program = new ProgramdefinitionTO(name, description, type, status, srcnum, destnum, date, researcher);
                programs.add(program);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting programs.\n" + ex.getMessage());
        } 

        return programs;
    }

    public ProgramdefinitionTO getProgram(String name) throws DaoException {
        String sql = "select description,type,status,sourcenum,destnum,createdate,researcher" +
                " from programdefinition where name=?";
        String sql2 = "select mapid,srcplate,srcpos,srcwellx,srcwelly,destplate,destpos," +
                " destwellx,destwelly,destblocknum,destblockrow,destblockcol,destblockposx,destblockposy,destblockwellx,destblockwelly" +
                " from programmapping where programname=? order by destblocknum, destblockwellx, destblockwelly, destplate, destpos, srcplate, srcpos";
        String sql3 = "select filereferenceid from programfile where programname=?";
        String sql4 = "select name, type, orders, ioflag from programcontainer where programname=? order by ioflag, orders";

        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;

        ProgramdefinitionTO program = null;
        DatabaseTransaction t = null;
        Connection c = null;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();

            stmt = c.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                String description = rs.getString(1);
                String type = rs.getString(2);
                String status = rs.getString(3);
                int srcnum = rs.getInt(4);
                int destnum = rs.getInt(5);
                String date = (rs.getDate(6)).toString();
                String researcher = rs.getString(7);
                program = new ProgramdefinitionTO(name, description, type, status, srcnum, destnum, date, researcher);
                DatabaseTransaction.closeResultSet(rs);
                DatabaseTransaction.closeStatement(stmt);

                stmt2 = c.prepareStatement(sql2);
                stmt2.setString(1, name);
                ResultSet rs2 = DatabaseTransaction.executeQuery(stmt2);
                while (rs2.next()) {
                    int mapid = rs2.getInt(1);
                    String srcplate = rs2.getString(2);
                    int srcpos = rs2.getInt(3);
                    String srcwellx = rs2.getString(4);
                    String srcwelly = rs2.getString(5);
                    String destplate = rs2.getString(6);
                    int destpos = rs2.getInt(7);
                    String destwellx = rs2.getString(8);
                    String destwelly = rs2.getString(9);
                    int destblocknum = rs2.getInt(10);
                    int destblockrow = rs2.getInt(11);
                    int destblockcol = rs2.getInt(12);
                    int destblockposx = rs2.getInt(13);
                    int destblockposy = rs2.getInt(14);
                    int destblockwellx = rs2.getInt(15);
                    int destblockwelly = rs2.getInt(16);
                    ProgrammappingTO mapping = new ProgrammappingTO(mapid, name, srcplate, srcpos, srcwellx, srcwelly, destplate, destpos, destwellx, destwelly, destblocknum, destblockrow, destblockcol, destblockposx, destblockposy, destblockwellx, destblockwelly);
                    program.addProgrammapping(mapping);
                }
                DatabaseTransaction.closeResultSet(rs2);
                DatabaseTransaction.closeStatement(stmt2);

                stmt3 = c.prepareStatement(sql3);
                stmt3.setString(1, name);
                ResultSet rs3 = DatabaseTransaction.executeQuery(stmt3);
                while (rs3.next()) {
                    int fileid = rs3.getInt(1);
                    FilereferenceTO file = new FilereferenceTO(fileid, null, null, null);
                    program.addFile(file);
                }
                DatabaseTransaction.closeResultSet(rs3);
                DatabaseTransaction.closeStatement(stmt3);

                stmt4 = c.prepareStatement(sql4);
                stmt4.setString(1, name);
                ResultSet rs4 = DatabaseTransaction.executeQuery(stmt4);
                while (rs4.next()) {
                    String containername = rs4.getString(1);
                    String containertype = rs4.getString(2);
                    int order = rs4.getInt(3);
                    String io = rs4.getString(4);
                    ProgramcontainerTO container = new ProgramcontainerTO(name, containername, containertype, order, io);
                    program.addProgramcontainer(container);
                }
                DatabaseTransaction.closeResultSet(rs4);
                DatabaseTransaction.closeStatement(stmt4);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DaoException("Error occured while getting program: " + name + "\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeConnection(c);
        }

        return program;
    }

    public boolean programnameExist(String name) throws DaoException {
        String sql = "select count(*) from programdefinition where name=?";
        
        PreparedStatement stmt = null;
        DatabaseTransaction t = null;
        Connection c = null;
        int count=0;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();

            stmt = c.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                count = rs.getInt(1);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DaoException("Error occured while checking program name: " + name + "\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        
        if(count>0)
            return true;
        else
            return false;
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
