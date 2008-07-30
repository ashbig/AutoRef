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
import transfer.ContainercellTO;
import transfer.LayoutcontainerTO;
import transfer.ProgramcontainerTO;
import transfer.ReagentTO;
import transfer.SlidecelllineageTO;
import transfer.SlidelayoutTO;
import transfer.SlidetemplateTO;

/**
 *
 * @author dzuo
 */
public class SlidedesignDAO {

    private Connection conn;

    public SlidedesignDAO() {
    }

    public SlidedesignDAO(Connection c) {
        setConn(c);
    }

    public void addSlidelayout(SlidelayoutTO layout) throws DaoException {
        String sql = "insert into slidelayout values(?,?,sysdate,?,?,?,?)";
        String sql2 = "insert into controlcontainers(layoutname,containername,containerorder,containerlevel,containerid) values(?,?,?,?,templatecontainerid.nextval)";

        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, layout.getName());
            stmt.setString(2, layout.getDescription());
            stmt.setString(3, layout.getResearcher());
            stmt.setString(4, layout.getStatus());
            stmt.setString(5, layout.getProgram1());
            stmt.setString(6, layout.getProgram2());
            DatabaseTransaction.executeUpdate(stmt);

            stmt2 = conn.prepareStatement(sql2);
            List<LayoutcontainerTO> containers = layout.getControls();
            for (LayoutcontainerTO c : containers) {
                stmt2.setString(1, layout.getName());
                stmt2.setString(2, c.getName());
                stmt2.setInt(3, c.getOrder());
                stmt2.setInt(4, c.getLevel());
                DatabaseTransaction.executeUpdate(stmt2);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding slide layout to the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
    }

    public SlidelayoutTO getSlidelayout(String name) throws DaoException {
        String sql = "select description, to_char(createdate, 'Mon-DD-YYYY'), researcher, status,program1,program2 from slidelayout where name=?";
        String sql2 = "select containername,containerorder,containerlevel,containerid from controlcontainers where layoutname=?";

        DatabaseTransaction t = null;
        Connection conn1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        SlidelayoutTO layout = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn1 = t.requestConnection();
            stmt = conn1.prepareStatement(sql);
            stmt.setString(1, name);
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                String desc = rs.getString(1);
                String date = rs.getString(2);
                String researcher = rs.getString(3);
                String status = rs.getString(4);
                String program1 = rs.getString(5);
                String program2 = rs.getString(6);
                layout = new SlidelayoutTO(name, desc, date, researcher, status, program1, program2);

                stmt2 = conn1.prepareStatement(sql2);
                stmt2.setString(1, name);
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                List<LayoutcontainerTO> controls = new ArrayList<LayoutcontainerTO>();
                while (rs2.next()) {
                    String containername = rs2.getString(1);
                    int order = rs2.getInt(2);
                    int level = rs2.getInt(3);
                    int containerid = rs2.getInt(4);
                    LayoutcontainerTO c = new LayoutcontainerTO(name, containername, null, ReagentTO.getTYPE_CONTROL(), order, level, ProgramcontainerTO.INPUT);
                    c.setContainerid(containerid);
                    controls.add(c);
                }
                layout.setControls(controls);
            } else {
                throw new DaoException("Cannot get slide layout from database with name: " + name);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting slide layout from the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeConnection(conn1);
        }

        return layout;
    }

    public List<SlidelayoutTO> getSlidelayouts(String name) throws DaoException {
        String sql = "select NVL(description, ''), to_char(createdate, 'Mon-DD-YYYY'), researcher, status,program1,program2,name from slidelayout where name like '%" + name + "%' order by name";
        // String sql2 = "select containername,containerorder,containerlevel from controlcontainers where layoutname=?";

        DatabaseTransaction t = null;
        //Connection conn1 = null;
        //PreparedStatement stmt2 = null;
        ResultSet rs = null;
        // ResultSet rs2 = null;
        List<SlidelayoutTO> layouts = new ArrayList<SlidelayoutTO>();

        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String desc = rs.getString(1);
                String date = rs.getString(2);
                String researcher = rs.getString(3);
                String status = rs.getString(4);
                String program1 = rs.getString(5);
                String program2 = rs.getString(6);
                String layoutname = rs.getString(7);
                SlidelayoutTO layout = new SlidelayoutTO(layoutname, desc, date, researcher, status, program1, program2);
                layouts.add(layout);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting slide layout from the database.\n" + ex.getMessage());
        } 

        return layouts;
    }

    public void addSlideTemplate(SlidetemplateTO template) throws DaoException {
        String sql = "insert into slidetemplate(name,description,createdate,researcher,status,layoutname) values(?,?,sysdate,?,?,?)";
        String sql2 = "insert into templatecontainercell(containerid,pos,posx,posy,type,reagentname,templatename) values(?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, template.getName());
            stmt.setString(2, template.getDescription());
            stmt.setString(3, template.getResearcher());
            stmt.setString(4, template.getStatus());
            stmt.setString(5, template.getLayout().getName());
            DatabaseTransaction.executeUpdate(stmt);

            stmt2 = conn.prepareStatement(sql2);
            List<LayoutcontainerTO> containers = template.getLayout().getControls();
            for (LayoutcontainerTO container : containers) {
                List<SlidecelllineageTO> cells = container.getCells();
                for (SlidecelllineageTO cell : cells) {
                    ContainercellTO c = cell.getCell();
                    String controlreagent = c.getControlreagent();
                    if(ReagentTO.NOT_SELECTED.equals(controlreagent)) {
                        controlreagent = null;
                    }
                    stmt2.setInt(1, container.getContainerid());
                    stmt2.setInt(2, c.getPos());
                    stmt2.setString(3, c.getPosx());
                    stmt2.setString(4, c.getPosy());
                    stmt2.setString(5, c.getType());
                    stmt2.setString(6, controlreagent);
                    stmt2.setString(7, template.getName());
                    DatabaseTransaction.executeUpdate(stmt2);
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding slide template to the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
    }

    public SlidetemplateTO getSlidetemplate(String name) throws DaoException {
        String sql = "select description, to_char(createdate, 'Mon-DD-YYYY'), researcher, status, layoutname from slidetemplate where name=?";
        String sql3 = "select pos,posx,posy,type,reagentname from templatecontainercell where containerid=? order by pos";

        DatabaseTransaction t = null;
        Connection conn1 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs3 = null;
        SlidetemplateTO template = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn1 = t.requestConnection();
            stmt = conn1.prepareStatement(sql);
            stmt.setString(1, name);
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                String desc = rs.getString(1);
                String date = rs.getString(2);
                String researcher = rs.getString(3);
                String status = rs.getString(4);
                String layoutname = rs.getString(5);
                SlidelayoutTO layout = getSlidelayout(layoutname);
                template = new SlidetemplateTO(layout, name, desc, date, researcher, status);

                List<LayoutcontainerTO> controls = layout.getControls();
                for(LayoutcontainerTO c:controls) {
                    int containerid = c.getContainerid();
                    stmt3 = conn1.prepareStatement(sql3);
                    stmt3.setInt(1, containerid);
                    rs3 = DatabaseTransaction.executeQuery(stmt3);
                    while(rs3.next()) {
                        int pos = rs3.getInt(1);
                        String x = rs3.getString(2);
                        String y = rs3.getString(3);
                        String type = rs3.getString(4);
                        String reagentname = rs3.getString(5);
                        ContainercellTO cell = new ContainercellTO(pos,x,y,type);
                        cell.setContainerid(containerid);
                        cell.setControlreagent(reagentname);
                        SlidecelllineageTO lineage = new SlidecelllineageTO(cell);
                        c.addCell(lineage);
                    }
                }
            } else {
                throw new DaoException("Cannot get slide template from database with name: " + name);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting slide template from the database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeConnection(conn1);
        }

        return template;
    }

    public List<SlidetemplateTO> getSlidetemplates(String name) throws DaoException {
        String sql = "select NVL(description, ''), to_char(createdate, 'Mon-DD-YYYY'), researcher, status,name,layoutname from slidetemplate where name like '%" + name + "%' order by name";

        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<SlidetemplateTO> templates = new ArrayList<SlidetemplateTO>();

        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String desc = rs.getString(1);
                String date = rs.getString(2);
                String researcher = rs.getString(3);
                String status = rs.getString(4);
                String templatename = rs.getString(5);
                String layoutname = rs.getString(6);
                SlidelayoutTO layout = new SlidelayoutTO(layoutname, null,null,null,null,null,null);
                SlidetemplateTO template = new SlidetemplateTO(layout,templatename,desc,researcher,date,status);
                templates.add(template);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting slide templates from the database.\n" + ex.getMessage());
        } 

        return templates;
    }
    
    public boolean isLayoutexist(String name) throws DaoException {
        String sql = "select count(*) from slidelayout where name=?";

        DatabaseTransaction t = null;
        Connection conn1 = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean b = false;

        try {
            t = DatabaseTransaction.getInstance();
            conn1 = t.requestConnection();
            stmt = conn1.prepareStatement(sql);
            stmt.setString(1, name);
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                int i = rs.getInt(1);
                if(i>0) 
                    b = true;
            } 
        } catch (Exception ex) {
            throw new DaoException("Error occured while checking slide layout name.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn1);
        }

        return b;
    }
    
    public boolean isTemplateExist(String name) throws DaoException {
        String sql = "select count(*) from slidetemplate where name=?";

        DatabaseTransaction t = null;
        Connection conn1 = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean b = false;

        try {
            t = DatabaseTransaction.getInstance();
            conn1 = t.requestConnection();
            stmt = conn1.prepareStatement(sql);
            stmt.setString(1, name);
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                int i = rs.getInt(1);
                if(i>0) 
                    b = true;
            } 
        } catch (Exception ex) {
            throw new DaoException("Error occured while checking slide template name.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn1);
        }

        return b;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection c) {
        this.conn = c;
    }
}
