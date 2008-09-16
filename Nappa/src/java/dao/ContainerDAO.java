/*
 * ContainerDAO.java
 *
 * Created on April 30, 2007, 1:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package dao;

import core.Block;
import database.DatabaseTransaction;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import transfer.CloneTO;
import transfer.ContainercellTO;
import transfer.ContainerheaderTO;
import transfer.ContainerpropertyTO;
import transfer.ContainertypeTO;
import transfer.ProcessexecutionTO;
import transfer.ReagentTO;
import transfer.ResulttypeTO;
import transfer.SampleTO;
import transfer.SamplepropertyTO;
import transfer.SlideTO;
import transfer.SlidecellTO;

/**
 *
 * @author dzuo
 */
public class ContainerDAO {

    private Connection conn;

    /** Creates a new instance of ContainerDAO */
    public ContainerDAO() {
    }

    public ContainerDAO(Connection c) {
        this.conn = c;
    }

    public void setContainerAndSampleids(Collection<ContainerheaderTO> containers) throws DaoException {
        int containerid = SequenceDAO.getNextid("containerheader", "containerid");
        int sampleid = SequenceDAO.getNextid("Sample", "sampleid");
        for (ContainerheaderTO c : containers) {
            c.setContainerid(containerid);
            Collection<SampleTO> samples = c.getSamples();
            for (SampleTO s : samples) {
                s.setSampleid(sampleid);
                sampleid++;
            }
            containerid++;
        }
    }

    public void setSampleids(Collection<ContainerheaderTO> containers) throws DaoException {
        int sampleid = SequenceDAO.getNextid("Sample", "sampleid");
        for (ContainerheaderTO c : containers) {
            Collection<SampleTO> samples = c.getSamples();
            for (SampleTO s : samples) {
                s.setSampleid(sampleid);
                sampleid++;
            }
        }
    }

    public void setSlideids(Collection<SlideTO> slides) throws DaoException {
        int slideid = SequenceDAO.getNextid("Slide", "slideid");
        for (SlideTO s : slides) {
            s.setSlideid(slideid);
            slideid++;
        }
    }

    public void addContainerset(ProcessexecutionTO p) throws DaoException {
        //add to tables: containerset, containerheader, containerheadermap, containercell, sample, containercelllineage, slidecell, slideheader
    }

    public void addContainers(Collection<ContainerheaderTO> containers) throws DaoException {
        addContainers(containers, false, false);
    }

    public void addContainers(Collection<ContainerheaderTO> containers, boolean isUpdate, boolean isSampleproperty) throws DaoException {
        if (isUpdate) {
            setSampleids(containers);
        } else {
            setContainerAndSampleids(containers);
        }

        List allslides = new ArrayList<SlideTO>();
        for (ContainerheaderTO c : containers) {
            allslides.addAll(c.getSlides());
        }
        if (allslides.size() > 0) {
            setSlideids(allslides);
        }

        String sql = null;
        if (isUpdate) {
            sql = "update containerheader set status='" + ContainerheaderTO.getSTATUS_GOOD() + "' where containerid=?";
        } else {
            sql = "insert into containerheader(containerid,barcode,type,format,status,location,labware,threadid,category)" +
                    " values(?,?,?,?,?,?,?,?,?)";
        }
        String sql2 = "insert into containercell(containerid,pos,posx,posy,type,sampleid)" +
                " values(?,?,?,?,?,?)";
        String sql3 = "insert into sample(sampleid,name,description,volume,quantity,unit,type,form,status,containerid,pos)" +
                " values(?,?,?,?,?,?,?,?,?,?,?)";
        String sql4 = "insert into samplereagent (sampleid,reagentid) values(?,?)";
        String sql5 = "insert into containerproperty (containerid,propertytype,propertyvalue) values(?,?,?)";
        String sql6 = "insert into slidecell(blocknum,blockrow,blockcol,blockposx,blockposy,blockwellx,blockwelly,containerid,pos) values(?,?,?,?,?,?,?,?,?)";
        String sql7 = "insert into slide(slideid,surfacechem,program,startdate,containerid,printorder,barcode) values(?,?,?,?,?,?,?)";
        String sql8 = "insert into sampleproperty(sampleid,type,value) values(?,?,?)";

        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        PreparedStatement stmt7 = null;
        PreparedStatement stmt8 = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt4 = conn.prepareStatement(sql4);
            stmt5 = conn.prepareStatement(sql5);
            stmt6 = conn.prepareStatement(sql6);
            stmt7 = conn.prepareStatement(sql7);
            if (isSampleproperty) {
                stmt8 = conn.prepareStatement(sql8);
            }
            for (ContainerheaderTO c : containers) {
                if (isUpdate) {
                    stmt.setInt(1, c.getContainerid());
                } else {
                    stmt.setInt(1, c.getContainerid());
                    stmt.setString(2, c.getBarcode());
                    stmt.setString(3, c.getType());
                    stmt.setString(4, c.getFormat());
                    stmt.setString(5, c.getStatus());
                    stmt.setString(6, c.getLocation());
                    stmt.setString(7, c.getLabware());
                    stmt.setInt(8, c.getThreadid());
                    stmt.setString(9, c.getCategory());
                }
                DatabaseTransaction.executeUpdate(stmt);

                Collection<SlideTO> slides = c.getSlides();
                for (SlideTO slide : slides) {
                    stmt7.setInt(1, slide.getSlideid());
                    stmt7.setString(2, slide.getSurfacechem());
                    stmt7.setString(3, slide.getProgram());
                    stmt7.setString(4, slide.getStartdate());
                    stmt7.setInt(5, slide.getContainer().getContainerid());
                    stmt7.setInt(6, slide.getPrintorder());
                    stmt7.setString(7, slide.getBarcode());
                    DatabaseTransaction.executeUpdate(stmt7);
                }

                Collection<SampleTO> samples = c.getSamples();
                for (SampleTO s : samples) {
                    stmt3.setInt(1, s.getSampleid());
                    stmt3.setString(2, s.getName());
                    stmt3.setString(3, s.getDescription());
                    stmt3.setInt(4, s.getVolume());
                    stmt3.setInt(5, s.getQuantity());
                    stmt3.setString(6, s.getUnit());
                    stmt3.setString(7, s.getType());
                    stmt3.setString(8, s.getForm());
                    stmt3.setString(9, s.getStatus());
                    stmt3.setInt(10, c.getContainerid());
                    stmt3.setInt(11, s.getPosition());
                    DatabaseTransaction.executeUpdate(stmt3);

                    stmt2.setInt(1, c.getContainerid());
                    stmt2.setInt(2, s.getCell().getPos());
                    stmt2.setString(3, s.getCell().getPosx());
                    stmt2.setString(4, s.getCell().getPosy());
                    stmt2.setString(5, s.getCell().getType());
                    stmt2.setInt(6, s.getSampleid());
                    DatabaseTransaction.executeUpdate(stmt2);

                    //insert into slidecell if container type is slide
                    if (c.getContainertype().getType().equals(ContainertypeTO.TYPE_SLIDE)) {
                        SlidecellTO scell = (SlidecellTO) s.getCell();
                        stmt6.setInt(1, scell.getBlocknum());
                        stmt6.setInt(2, scell.getBlockx());
                        stmt6.setInt(3, scell.getBlocky());
                        stmt6.setInt(4, scell.getBlockposx());
                        stmt6.setInt(5, scell.getBlockposy());
                        stmt6.setInt(6, scell.getBlockwellx());
                        stmt6.setInt(7, scell.getBlockwelly());
                        stmt6.setInt(8, c.getContainerid());
                        stmt6.setInt(9, scell.getPos());
                        DatabaseTransaction.executeUpdate(stmt6);
                    }

                    Collection<ReagentTO> reagents = s.getReagents();
                    for (ReagentTO r : reagents) {
                        stmt4.setInt(1, s.getSampleid());
                        stmt4.setInt(2, r.getReagentid());
                        DatabaseTransaction.executeUpdate(stmt4);
                    }

                    if (isSampleproperty) {
                        Collection<SamplepropertyTO> properties = s.getProperties();
                        for (SamplepropertyTO p : properties) {
                            stmt8.setInt(1, s.getSampleid());
                            stmt8.setString(2, p.getType());
                            stmt8.setString(3, p.getValue());
                            DatabaseTransaction.executeUpdate(stmt8);
                        }
                    }
                }

                Collection<ContainerpropertyTO> properties = c.getProperties();
                for (ContainerpropertyTO property : properties) {
                    stmt5.setInt(1, c.getContainerid());
                    stmt5.setString(2, property.getType());
                    stmt5.setString(3, property.getValue());
                    DatabaseTransaction.executeUpdate(stmt5);
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while inserting containers into database." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeStatement(stmt5);
            if (stmt6 != null) {
                DatabaseTransaction.closeStatement(stmt6);
            }
            if (stmt7 != null) {
                DatabaseTransaction.closeStatement(stmt7);
            }
            if (stmt8 != null) {
                DatabaseTransaction.closeStatement(stmt8);
            }
        }
    }

    public void addSampleproperties(List<SamplepropertyTO> properties) throws DaoException {
        String sql = "insert into sampleproperty(sampleid,type,value) values(?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (SamplepropertyTO p : properties) {
                stmt.setInt(1, p.getSampleid());
                stmt.setString(2, p.getType());
                stmt.setString(3, p.getValue());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while inserting into sampleproperty table." + ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public static SlideTO getSlideWithBlocks(int slideid) throws DaoException {
        String sql = "select barcode, printorder, surfacechem, program, startdate, containerid from slide where slideid=" + slideid;

        DatabaseTransaction t = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        SlideTO slide = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                String barcode = rs.getString(1);
                int printorder = rs.getInt(2);
                String surfacechem = rs.getString(3);
                String program = rs.getString(4);
                String startdate = rs.getString(5);
                int containerid = rs.getInt(6);
                slide = new SlideTO(slideid, printorder, barcode, surfacechem, program, startdate);
                ContainerheaderTO c = ContainerDAO.getContainer(containerid, false, false, false, false);
                slide.setContainer(c);

                String sql2 = "select distinct blocknum,blockrow,blockcol from slidecell where containerid=" + containerid + " order by blocknum";
                rs2 = t.executeQuery(sql2);
                while (rs2.next()) {
                    int blocknum = rs2.getInt(1);
                    int blockrow = rs2.getInt(2);
                    int blockcol = rs2.getInt(3);
                    Block b = new Block();
                    b.setNum(blocknum);
                    b.setX(blockrow);
                    b.setY(blockcol);
                    slide.addBlock(b);
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting containers from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return slide;
    }

    public static SlideTO getSlide(int slideid, boolean isSample, boolean isReagent, boolean isClone, boolean isType) throws DaoException {
        String sql = "select barcode, printorder, surfacechem, program, startdate, containerid from slide where slideid=" + slideid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        SlideTO slide = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                String barcode = rs.getString(1);
                int printorder = rs.getInt(2);
                String surfacechem = rs.getString(3);
                String program = rs.getString(4);
                String startdate = rs.getString(5);
                int containerid = rs.getInt(6);
                slide = new SlideTO(slideid, printorder, barcode, surfacechem, program, startdate);
                ContainerheaderTO c = ContainerDAO.getContainer(containerid, isSample, isReagent, isClone, isType);
                slide.setContainer(c);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting containers from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return slide;
    }

    public static ContainerheaderTO getContainer(int containerid, boolean isSample, boolean isReagent, boolean isClone, boolean isType) throws DaoException {
        String sql = "select barcode,type,format,status,location,labware,threadid,category from containerheader where containerid=?";
        String sql2 = "select pos,posx,posy,type from containercell where containerid=? and sampleid=?";
        String sql3 = "select sampleid,name,description,volume,quantity,unit,type,form,status,pos from sample where containerid=?";
        String sql4 = "select reagentid, name, type, description from reagent where reagentid in (select reagentid from samplereagent where sampleid=?)";
        String sql5 = "select vectorname, growthname, srccloneid, source, genbank, gi, geneid, symbol from clone where cloneid=?";
        String sql6 = "select blocknum,blockrow,blockcol,blockposx,blockposy,blockwellx,blockwelly from slidecell where containerid=? and pos=?";
        String sql7 = "select description, numofrow, numofcol from containertype where type=?";
        String sql8 = "select type,value from sampleproperty where sampleid=?";

        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ResultSet rs6 = null;
        ResultSet rs7 = null;
        ResultSet rs8 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        PreparedStatement stmt7 = null;
        PreparedStatement stmt8 = null;

        ContainerheaderTO container = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            if (isSample) {
                stmt2 = conn.prepareStatement(sql2);
                stmt3 = conn.prepareStatement(sql3);
                stmt6 = conn.prepareStatement(sql6);
                stmt8 = conn.prepareStatement(sql8);
            }
            if (isReagent) {
                stmt4 = conn.prepareStatement(sql4);
            }
            if (isClone) {
                stmt5 = conn.prepareStatement(sql5);
            }
            if (isType) {
                stmt7 = conn.prepareStatement(sql7);
            }

            stmt.setInt(1, containerid);
            rs = t.executeQuery(stmt);
            if (rs.next()) {
                String barcode = rs.getString(1);
                String type = rs.getString(2);
                String format = rs.getString(3);
                String status = rs.getString(4);
                String location = rs.getString(5);
                String labware = rs.getString(6);
                int threadid = rs.getInt(7);
                String category = rs.getString(8);
                container = new ContainerheaderTO(containerid, barcode, new ContainertypeTO(type), format, status, location, labware, threadid, category);

                if (isType) {
                    stmt7.setString(1, type);
                    rs7 = t.executeQuery(stmt7);
                    if (rs7.next()) {
                        String desc = rs7.getString(1);
                        int numofrow = rs7.getInt(2);
                        int numofcol = rs7.getInt(3);
                        ContainertypeTO ctype = new ContainertypeTO(type, desc, numofrow, numofcol);
                        container.setContainertype(ctype);
                    }
                }

                if (isSample) {
                    stmt3.setInt(1, containerid);
                    rs3 = t.executeQuery(stmt3);
                    while (rs3.next()) {
                        int sampleid = rs3.getInt(1);
                        String name = rs3.getString(2);
                        String description = rs3.getString(3);
                        int volume = rs3.getInt(4);
                        int quantity = rs3.getInt(5);
                        String unit = rs3.getString(6);
                        String stype = rs3.getString(7);
                        String form = rs3.getString(8);
                        String sstatus = rs3.getString(9);
                        int pos = rs3.getInt(10);
                        SampleTO sample = new SampleTO(sampleid, name, description, volume, quantity, unit, stype, form, sstatus, containerid, pos);

                        stmt2.setInt(1, containerid);
                        stmt2.setInt(2, sampleid);
                        rs2 = t.executeQuery(stmt2);
                        if (rs2.next()) {
                            int position = rs2.getInt(1);
                            String posx = rs2.getString(2);
                            String posy = rs2.getString(3);
                            String celltype = rs2.getString(4);

                            if (type.equals(ContainertypeTO.TYPE_SLIDE)) {
                                stmt6.setInt(1, containerid);
                                stmt6.setInt(2, position);
                                rs6 = t.executeQuery(stmt6);
                                if (rs6.next()) {
                                    int blocknum = rs6.getInt(1);
                                    int blockrow = rs6.getInt(2);
                                    int blockcol = rs6.getInt(3);
                                    int blockposx = rs6.getInt(4);
                                    int blockposy = rs6.getInt(5);
                                    int blockwellx = rs6.getInt(6);
                                    int blockwelly = rs6.getInt(7);
                                    SlidecellTO cell = new SlidecellTO(position, posx, posy, celltype, containerid, sampleid, blocknum, blockrow, blockcol, blockposx, blockposy, blockwellx, blockwelly);
                                    sample.setCell(cell);
                                } else {
                                    ContainercellTO cell = new ContainercellTO(position, posx, posy, celltype, containerid, sampleid);
                                    sample.setCell(cell);
                                }
                            } else {
                                ContainercellTO cell = new ContainercellTO(position, posx, posy, celltype, containerid, sampleid);
                                sample.setCell(cell);
                            }
                        }

                        stmt8.setInt(1, sampleid);
                        rs8 = DatabaseTransaction.executeQuery(stmt8);
                        while (rs8.next()) {
                            String ptype = rs8.getString(1);
                            String pvalue = rs8.getString(2);
                            SamplepropertyTO property = new SamplepropertyTO(sampleid, ptype, pvalue);
                            sample.addProperty(property);
                        }

                        if (isReagent) {
                            stmt4.setInt(1, sampleid);
                            rs4 = t.executeQuery(stmt4);
                            while (rs4.next()) {
                                int reagentid = rs4.getInt(1);
                                String reagentname = rs4.getString(2);
                                String reagenttype = rs4.getString(3);
                                String rdescription = rs4.getString(4);
                                ReagentTO reagent = new ReagentTO(reagentid, reagentname, reagenttype, rdescription);

                                if (isClone) {
                                    stmt5.setInt(1, reagentid);
                                    rs5 = t.executeQuery(stmt5);
                                    if (rs5.next()) {
                                        String vector = rs5.getString(1);
                                        String growth = rs5.getString(2);
                                        String srccloneid = rs5.getString(3);
                                        String source = rs5.getString(4);
                                        String genbank = rs5.getString(5);
                                        String gi = rs5.getString(6);
                                        String geneid = rs5.getString(7);
                                        String symbol = rs5.getString(8);
                                        reagent = new CloneTO(reagent, vector, growth, srccloneid, source, genbank, gi, geneid, symbol);
                                    }
                                }
                                sample.addReagent(reagent);
                            }
                        }
                        container.addSample(sample);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting containers from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            if (rs2 != null) {
                DatabaseTransaction.closeResultSet(rs2);
            }
            if (rs3 != null) {
                DatabaseTransaction.closeResultSet(rs3);
            }
            if (rs4 != null) {
                DatabaseTransaction.closeResultSet(rs4);
            }
            if (rs5 != null) {
                DatabaseTransaction.closeResultSet(rs5);
            }
            if (rs6 != null) {
                DatabaseTransaction.closeResultSet(rs6);
            }
            if (rs7 != null) {
                DatabaseTransaction.closeResultSet(rs7);
            }
            if (rs8 != null) {
                DatabaseTransaction.closeResultSet(rs8);
            }
            DatabaseTransaction.closeStatement(stmt);
            if (stmt2 != null) {
                DatabaseTransaction.closeStatement(stmt2);
            }
            if (stmt3 != null) {
                DatabaseTransaction.closeStatement(stmt3);
            }
            if (stmt4 != null) {
                DatabaseTransaction.closeStatement(stmt4);
            }
            if (stmt5 != null) {
                DatabaseTransaction.closeStatement(stmt5);
            }
            if (stmt6 != null) {
                DatabaseTransaction.closeStatement(stmt6);
            }
            if (stmt7 != null) {
                DatabaseTransaction.closeStatement(stmt7);
            }
            if (stmt8 != null) {
                DatabaseTransaction.closeStatement(stmt8);
            }
            DatabaseTransaction.closeConnection(conn);
        }

        return container;
    }

    public static List getSamplesForBlock(int containerid, int blocknum, boolean isReagent, boolean isClone) throws DaoException {
        String sql = "select pos,blockrow,blockcol,blockposx,blockposy,blockwellx,blockwelly from slidecell where containerid=? and blocknum=? order by blockwellx, blockwelly";
        String sql2 = "select posx,posy,type,sampleid from containercell where containerid=? and pos=?";
        String sql3 = "select name,description,volume,quantity,unit,type,form,status from sample where sampleid=?";
        String sql4 = "select reagentid, name, type, description from reagent where reagentid in (select reagentid from samplereagent where sampleid=?)";
        String sql5 = "select vectorname, growthname, srccloneid, source, genbank, gi, geneid, symbol from clone where cloneid=?";
        String sql6 = "select type,value from sampleproperty where sampleid=?";

        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ResultSet rs6 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;

        List<SampleTO> samples = new ArrayList<SampleTO>();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt6 = conn.prepareStatement(sql6);

            if (isReagent) {
                stmt4 = conn.prepareStatement(sql4);
            }
            if (isClone) {
                stmt5 = conn.prepareStatement(sql5);
            }

            stmt.setInt(1, containerid);
            stmt.setInt(2, blocknum);
            rs = t.executeQuery(stmt);
            while (rs.next()) {
                int position = rs.getInt(1);
                int blockrow = rs.getInt(2);
                int blockcol = rs.getInt(3);
                int blockposx = rs.getInt(4);
                int blockposy = rs.getInt(5);
                int blockwellx = rs.getInt(6);
                int blockwelly = rs.getInt(7);

                stmt2.setInt(1, containerid);
                stmt2.setInt(2, position);
                rs2 = t.executeQuery(stmt2);
                if (rs2.next()) {
                    String posx = rs2.getString(1);
                    String posy = rs2.getString(2);
                    String celltype = rs2.getString(3);
                    int sampleid = rs2.getInt(4);
                    SlidecellTO cell = new SlidecellTO(position, posx, posy, celltype, containerid, sampleid, blocknum, blockrow, blockcol, blockposx, blockposy, blockwellx, blockwelly);

                    stmt3.setInt(1, sampleid);
                    rs3 = t.executeQuery(stmt3);
                    if (rs3.next()) {
                        String name = rs3.getString(1);
                        String description = rs3.getString(2);
                        int volume = rs3.getInt(3);
                        int quantity = rs3.getInt(4);
                        String unit = rs3.getString(5);
                        String stype = rs3.getString(6);
                        String form = rs3.getString(7);
                        String sstatus = rs3.getString(8);
                        SampleTO sample = new SampleTO(sampleid, name, description, volume, quantity, unit, stype, form, sstatus, containerid, position);
                        sample.setCell(cell);

                        if (isReagent) {
                            stmt4.setInt(1, sampleid);
                            rs4 = t.executeQuery(stmt4);
                            while (rs4.next()) {
                                int reagentid = rs4.getInt(1);
                                String reagentname = rs4.getString(2);
                                String reagenttype = rs4.getString(3);
                                String rdescription = rs4.getString(4);
                                ReagentTO reagent = new ReagentTO(reagentid, reagentname, reagenttype, rdescription);

                                if (isClone) {
                                    stmt5.setInt(1, reagentid);
                                    rs5 = t.executeQuery(stmt5);
                                    if (rs5.next()) {
                                        String vector = rs5.getString(1);
                                        String growth = rs5.getString(2);
                                        String srccloneid = rs5.getString(3);
                                        String source = rs5.getString(4);
                                        String genbank = rs5.getString(5);
                                        String gi = rs5.getString(6);
                                        String geneid = rs5.getString(7);
                                        String symbol = rs5.getString(8);
                                        reagent = new CloneTO(reagent, vector, growth, srccloneid, source, genbank, gi, geneid, symbol);
                                    }
                                }
                                sample.addReagent(reagent);
                            }
                        }

                        stmt6.setInt(1, sampleid);
                        rs6 = DatabaseTransaction.executeQuery(stmt6);
                        while (rs6.next()) {
                            String ptype = rs6.getString(1);
                            String pvalue = rs6.getString(2);
                            SamplepropertyTO p = new SamplepropertyTO(sampleid, ptype, pvalue);
                            sample.addProperty(p);
                        }

                        samples.add(sample);
                    }
                }

            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting containers from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            if (rs2 != null) {
                DatabaseTransaction.closeResultSet(rs2);
            }
            if (rs3 != null) {
                DatabaseTransaction.closeResultSet(rs3);
            }
            if (rs4 != null) {
                DatabaseTransaction.closeResultSet(rs4);
            }
            if (rs5 != null) {
                DatabaseTransaction.closeResultSet(rs5);
            }
            if (rs6 != null) {
                DatabaseTransaction.closeResultSet(rs6);
            }
            DatabaseTransaction.closeStatement(stmt);
            if (stmt2 != null) {
                DatabaseTransaction.closeStatement(stmt2);
            }
            if (stmt3 != null) {
                DatabaseTransaction.closeStatement(stmt3);
            }
            if (stmt4 != null) {
                DatabaseTransaction.closeStatement(stmt4);
            }
            if (stmt5 != null) {
                DatabaseTransaction.closeStatement(stmt5);
            }
            if (stmt6 != null) {
                DatabaseTransaction.closeStatement(stmt6);
            }
            DatabaseTransaction.closeConnection(conn);
        }

        return samples;
    }

    public static ContainerheaderTO getContainer(String label, boolean isSample, boolean isReagent, boolean isClone, boolean isType) throws DaoException {
        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        String sql = "select containerid from containerheader where barcode=?";
        ContainerheaderTO container = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, label);
            rs = t.executeQuery(stmt);
            if (rs.next()) {
                int containerid = rs.getInt(1);
                container = getContainer(containerid, isSample, isReagent, isClone, isType);
            } else {
                throw new DaoException("Cannot find container: " + label);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting container from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        return container;
    }

    public ContainerheaderTO getContainer(String label) throws DaoException {
        String sql = "select containerid,barcode,type,format,status,location,labware,threadid,category from containerheader where barcode=?";
        String sql2 = "select pos,posx,posy,type from containercell where containerid=? and sampleid=?";
        String sql3 = "select sampleid,name,description,volume,quantity,unit,type,form,status,pos from sample where containerid=?";

        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;

        ContainerheaderTO container = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);

            stmt.setString(1, label);
            rs = t.executeQuery(stmt);
            if (rs.next()) {
                int containerid = rs.getInt(1);
                String barcode = rs.getString(2);
                String type = rs.getString(3);
                String format = rs.getString(4);
                String status = rs.getString(5);
                String location = rs.getString(6);
                String labware = rs.getString(7);
                int threadid = rs.getInt(8);
                String category = rs.getString(9);
                container = new ContainerheaderTO(containerid, barcode, new ContainertypeTO(type), format, status, location, labware, threadid, category);

                stmt3.setInt(1, containerid);
                rs3 = t.executeQuery(stmt3);
                while (rs3.next()) {
                    int sampleid = rs3.getInt(1);
                    String name = rs3.getString(2);
                    String description = rs3.getString(3);
                    int volume = rs3.getInt(4);
                    int quantity = rs3.getInt(5);
                    String unit = rs3.getString(6);
                    String stype = rs3.getString(7);
                    String form = rs3.getString(8);
                    String sstatus = rs3.getString(9);
                    int pos = rs3.getInt(10);
                    SampleTO sample = new SampleTO(sampleid, name, description, volume, quantity, unit, stype, form, sstatus, containerid, pos);

                    stmt2.setInt(1, containerid);
                    stmt2.setInt(2, sampleid);
                    rs2 = t.executeQuery(stmt2);
                    if (rs2.next()) {
                        int position = rs2.getInt(1);
                        String posx = rs2.getString(2);
                        String posy = rs2.getString(3);
                        String celltype = rs2.getString(4);
                        ContainercellTO cell = new ContainercellTO(position, posx, posy, celltype, containerid, sampleid);
                        sample.setCell(cell);
                    }
                    container.addSample(sample);
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting containers from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeConnection(conn);
        }

        return container;
    }

    public Collection<ContainerheaderTO> getContainers(Collection labels, boolean isReagent) throws DaoException {
        return getContainers(labels, null, true, false, isReagent);
    }

    public Collection<ContainerheaderTO> getContainers(Collection labels, String containerStatus, boolean isSample, boolean isContainerType, boolean isReagent) throws DaoException {
        String sql = "select containerid,barcode,type,format,status,location,labware,threadid,category from containerheader where barcode=?";
        String sql2 = "select pos,posx,posy,type from containercell where containerid=? and sampleid=?";
        String sql3 = "select sampleid,name,description,volume,quantity,unit,type,form,status,pos from sample where containerid=?";
        String sql4 = "select description, numofrow, numofcol from containertype where type=?";
        String sql5 = "select reagentid from samplereagent where sampleid=?";
        String sql6 = "select s.slideid,s.barcode,c.type,c.format,c.status,c.location,c.labware,c.threadid,c.category,c.containerid from containerheader c, slide s where c.containerid=s.containerid and s.barcode=?";
        String sql7 = "select type,value from sampleproperty where sampleid=?";

        if (containerStatus != null) {
            sql += " and status='" + containerStatus + "'";
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        ResultSet rs4 = null;
        ResultSet rs5 = null;
        ResultSet rs6 = null;
        ResultSet rs7 = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        PreparedStatement stmt7 = null;

        Collection<ContainerheaderTO> containers = new ArrayList<ContainerheaderTO>();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            if (isSample) {
                stmt2 = conn.prepareStatement(sql2);
                stmt3 = conn.prepareStatement(sql3);
                stmt7 = conn.prepareStatement(sql7);
            }
            if (isContainerType) {
                stmt4 = conn.prepareStatement(sql4);
            }
            if (isReagent) {
                stmt5 = conn.prepareStatement(sql5);
            }
            stmt6 = conn.prepareStatement(sql6);

            Iterator iter = labels.iterator();
            while (iter.hasNext()) {
                int containerid = 0;
                ContainerheaderTO container = null;
                String label = (String) iter.next();
                stmt.setString(1, label);
                rs = t.executeQuery(stmt);
                if (rs.next()) {
                    containerid = rs.getInt(1);
                    String barcode = rs.getString(2);
                    String type = rs.getString(3);
                    String format = rs.getString(4);
                    String status = rs.getString(5);
                    String location = rs.getString(6);
                    String labware = rs.getString(7);
                    int threadid = rs.getInt(8);
                    String category = rs.getString(9);

                    boolean isContainer = false;
                    if (!ContainertypeTO.TYPE_SLIDE.equals(type)) {
                        isContainer = true;
                    } else if (ContainerheaderTO.getSTATUS_EMPTY().equals(status)) {
                        isContainer = true;
                    }
                    if (isContainer) {
                        container = new ContainerheaderTO(containerid, barcode, new ContainertypeTO(type), format, status, location, labware, threadid, category);
                    }
                } else {
                    stmt6.setString(1, label);
                    rs6 = t.executeQuery(stmt6);
                    if (rs6.next()) {
                        int slideid = rs6.getInt(1);
                        String barcode = rs6.getString(2);
                        String type = rs6.getString(3);
                        String format = rs6.getString(4);
                        String status = rs6.getString(5);
                        String location = rs6.getString(6);
                        String labware = rs6.getString(7);
                        int threadid = rs6.getInt(8);
                        String category = rs6.getString(9);
                        containerid = rs6.getInt(10);
                        container = new ContainerheaderTO(slideid, barcode, new ContainertypeTO(type), format, status, location, labware, threadid, category);
                    }
                }

                if (container == null) {
                    continue;
                }
                containers.add(container);

                if (isSample) {
                    stmt3.setInt(1, containerid);
                    rs3 = t.executeQuery(stmt3);
                    while (rs3.next()) {
                        int sampleid = rs3.getInt(1);
                        String name = rs3.getString(2);
                        String description = rs3.getString(3);
                        int volume = rs3.getInt(4);
                        int quantity = rs3.getInt(5);
                        String unit = rs3.getString(6);
                        String stype = rs3.getString(7);
                        String form = rs3.getString(8);
                        String sstatus = rs3.getString(9);
                        int pos = rs3.getInt(10);
                        SampleTO sample = new SampleTO(sampleid, name, description, volume, quantity, unit, stype, form, sstatus, containerid, pos);

                        stmt2.setInt(1, containerid);
                        stmt2.setInt(2, sampleid);
                        rs2 = t.executeQuery(stmt2);
                        if (rs2.next()) {
                            int position = rs2.getInt(1);
                            String posx = rs2.getString(2);
                            String posy = rs2.getString(3);
                            String celltype = rs2.getString(4);
                            ContainercellTO cell = new ContainercellTO(position, posx, posy, celltype, containerid, sampleid);
                            sample.setCell(cell);
                        }

                        stmt7.setInt(1, sampleid);
                        rs7 = DatabaseTransaction.executeQuery(stmt7);
                        while (rs7.next()) {
                            String ptype = rs7.getString(1);
                            String pvalue = rs7.getString(2);
                            SamplepropertyTO property = new SamplepropertyTO(sampleid, ptype, pvalue);
                            sample.addProperty(property);
                        }

                        if (isReagent) {
                            stmt5.setInt(1, sampleid);
                            rs5 = DatabaseTransaction.executeQuery(stmt5);
                            while (rs5.next()) {
                                int reagentid = rs5.getInt(1);
                                ReagentTO reagent = new ReagentTO(reagentid);
                                sample.addReagent(reagent);
                            }
                        }
                        container.addSample(sample);
                    }
                }
                if (isContainerType) {
                    String type = container.getType();
                    stmt4.setString(1, type);
                    rs4 = t.executeQuery(stmt4);
                    if (rs4.next()) {
                        String desc = rs4.getString(1);
                        int row = rs4.getInt(2);
                        int col = rs4.getInt(3);
                        ContainertypeTO ctype = new ContainertypeTO(type, desc, row, col);
                        container.setContainertype(ctype);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting containers from database.\n" + ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            if (rs2 != null) {
                DatabaseTransaction.closeResultSet(rs2);
            }
            if (rs3 != null) {
                DatabaseTransaction.closeResultSet(rs3);
            }
            if (rs4 != null) {
                DatabaseTransaction.closeResultSet(rs4);
            }
            if (rs5 != null) {
                DatabaseTransaction.closeResultSet(rs5);
            }
            if (rs6 != null) {
                DatabaseTransaction.closeResultSet(rs6);
            }
            if (rs7 != null) {
                DatabaseTransaction.closeResultSet(rs7);
            }
            DatabaseTransaction.closeStatement(stmt);
            if (stmt2 != null) {
                DatabaseTransaction.closeStatement(stmt2);
            }
            if (stmt3 != null) {
                DatabaseTransaction.closeStatement(stmt3);
            }
            if (stmt4 != null) {
                DatabaseTransaction.closeStatement(stmt4);
            }
            if (stmt5 != null) {
                DatabaseTransaction.closeStatement(stmt5);
            }
            if (stmt6 != null) {
                DatabaseTransaction.closeStatement(stmt6);
            }
            if (stmt7 != null) {
                DatabaseTransaction.closeStatement(stmt7);
            }
            DatabaseTransaction.closeConnection(conn);
        }

        return containers;
    }

    public static Collection<ContainerheaderTO> checkContainers(Collection<String> labels, boolean isReagent) throws DaoException {
        return checkContainers(labels, null, false, false, isReagent);
    }

    public static Collection<ContainerheaderTO> checkContainers(Collection labels, String containerStatus, boolean isContainertype, boolean isSample, boolean isReagent) throws DaoException {
        ContainerDAO dao = new ContainerDAO();
        return dao.getContainers(labels, containerStatus, isSample, isContainertype, isReagent);
    }

    public static List<ResulttypeTO> getResulttypes() throws DaoException {
        String sql = "select type, description from resulttype";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<ResulttypeTO> types = new ArrayList<ResulttypeTO>();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String type = rs.getString(1);
                String desc = rs.getString(2);
                ResulttypeTO rtype = new ResulttypeTO(type, desc);
                types.add(rtype);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while getting result type from database.");
        }
        return types;
    }

    public void addSampleReagents(List<SampleTO> samples) throws DaoException {
        String sql = "insert into samplereagent (sampleid,reagentid) values(?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (SampleTO sample : samples) {
                Collection<ReagentTO> reagents = sample.getReagents();
                for (ReagentTO r : reagents) {
                    //System.out.println("ContainerDAO:addSampleReagents:"+sample.getSampleid()+"\t"+r.getReagentid());
                    stmt.setInt(1, sample.getSampleid());
                    stmt.setInt(2, r.getReagentid());
                    DatabaseTransaction.executeUpdate(stmt);
                }
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding reagents to the database.\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void addReagents(List<ReagentTO> reagents) throws DaoException {
        String sql = "insert into samplereagent (sampleid,reagentid) values(?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for (ReagentTO r:reagents) {
                stmt.setInt(1, r.getSample().getSampleid());
                    stmt.setInt(2, r.getReagentid());
                    DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while adding reagents to the database.\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public void updateCelltype(List<ContainercellTO> cells, String type) throws DaoException {
        String sql = "update containercell set type=? where containerid=? and pos=?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(ContainercellTO cell:cells) {
                stmt.setString(1, type);
                stmt.setInt(2, cell.getContainerid());
                stmt.setInt(3, cell.getPos());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while updating container cell type.\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    public void updateSampletype(List<SampleTO> samples, String type) throws DaoException {
        String sql = "update sample set type=? where sampleid=?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(SampleTO s:samples) {
                stmt.setString(1, type);
                stmt.setInt(2, s.getSampleid());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while updating sample type.\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
}
