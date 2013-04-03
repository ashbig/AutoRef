/*
 * CloneManager.java
 *
 * Created on April 14, 2005, 11:19 AM
 */
package plasmid.database.DatabaseManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CloneManager extends TableManager {

    /** Creates a new instance of CloneManager */
    public CloneManager(Connection conn) {
        super(conn);
    }

    public CloneManager() {
        super();
    }

    public boolean insertClones(List clones) {
        if (clones == null) {
            return true;
        }

        String sql = "insert into clone"
                + " (cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename, status, specialtreatment, source, description,importdate)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < clones.size(); i++) {
                Clone c = (Clone) clones.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getName());
                stmt.setString(3, c.getType());
                stmt.setString(4, c.getVerified());
                stmt.setString(5, c.getVermethod());
                stmt.setString(6, c.getDomain());
                stmt.setString(7, c.getSubdomain());
                stmt.setString(8, c.getRestriction());
                stmt.setString(9, c.getComments());
                stmt.setInt(10, c.getVectorid());
                stmt.setString(11, c.getVectorname());
                stmt.setString(12, c.getClonemap());
                stmt.setString(13, c.getStatus());
                stmt.setString(14, c.getSpecialtreatment());
                stmt.setString(15, c.getSource());
                stmt.setString(16, c.getDescription());
                // System.out.println(c.getVectorname()+","+c.getRestriction());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONE table");
            return false;
        }
        return true;
    }

    public boolean insertCloneGrowths(List growths) {
        if (growths == null) {
            return true;
        }

        String sql = "insert into clonegrowth"
                + " (cloneid,growthid,isrecommended)"
                + " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < growths.size(); i++) {
                CloneGrowth c = (CloneGrowth) growths.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getGrowthid());
                stmt.setString(3, c.getIsrecommended());
                //System.out.println(c.getCloneid()+"\t"+c.getGrowthid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEGROWTH table");
            return false;
        }
        return true;
    }

    public boolean insertCloneGrowths(int cloneid, List growths) {  // With cloneid and vectorgrowthconditions

        if ((cloneid < 1) || (growths == null)) {
            return true;
        }

        String sql = "insert into clonegrowth"
                + " (cloneid,growthid,isrecommended)"
                + " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < growths.size(); i++) {
                VectorGrowthCondition c = (VectorGrowthCondition) growths.get(i);
                stmt.setInt(1, cloneid);
                stmt.setInt(2, c.getGrowthid());
                stmt.setString(3, c.getIsrecommended());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEGROWTH table");
            return false;
        }
        return true;
    }

    public boolean insertCloneSelections(List selections) {
        if (selections == null) {
            return true;
        }

        String sql = "insert into cloneselection"
                + " (cloneid,hosttype,marker)"
                + " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < selections.size(); i++) {
                CloneSelection c = (CloneSelection) selections.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getHosttype());
                stmt.setString(3, c.getMarker());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONESELECTION table");
            return false;
        }
        return true;
    }

    public boolean insertCloneSelections(int cloneid, List selections) {
        if ((cloneid < 1) || (selections == null)) {
            return true;
        }

        String sql = "insert into cloneselection"
                + " (cloneid,hosttype,marker)"
                + " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < selections.size(); i++) {
                VectorSelectMarker c = (VectorSelectMarker) selections.get(i);
                stmt.setInt(1, cloneid);
                stmt.setString(2, c.getHosttype());
                stmt.setString(3, c.getMarker());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONESELECTION table");
            return false;
        }
        return true;
    }

    public boolean insertCloneHosts(List hosts) {
        if (hosts == null) {
            return true;
        }

        String sql = "insert into host"
                + " (cloneid,hoststrain,isinuse,description)"
                + " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < hosts.size(); i++) {
                CloneHost c = (CloneHost) hosts.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getHoststrain());
                stmt.setString(3, c.getIsinuse());
                stmt.setString(4, c.getDescription());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into HOST table");
            return false;
        }
        return true;
    }

    public boolean insertCloneHosts(int cloneid, List hosts) {
        if ((cloneid < 1) || (hosts == null)) {
            return true;
        }

        String sql = "insert into host"
                + " (cloneid,hoststrain,isinuse,description)"
                + " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < hosts.size(); i++) {
                VectorHostStrain c = (VectorHostStrain) hosts.get(i);
                stmt.setInt(1, cloneid);
                stmt.setString(2, c.getHoststrain());
                stmt.setString(3, c.getIsinuse());
                stmt.setString(4, c.getDescription());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into HOST table");
            return false;
        }
        return true;
    }

    public boolean insertCloneNameTypes(List types) {
        if (types == null) {
            return true;
        }

        String sql = "insert into clonenametype"
                + " (clonetype, nametype, use)"
                + " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < types.size(); i++) {
                CloneNameType c = (CloneNameType) types.get(i);
                stmt.setString(1, c.getClonetype());
                stmt.setString(2, c.getNametype());
                stmt.setString(3, c.getUse());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONENAMETYPE table");
            return false;
        }
        return true;
    }

    public boolean insertCloneNames(List names) {
        if (names == null) {
            return true;
        }

        String sql = "insert into clonename"
                + " (cloneid, nametype, namevalue, nameurl)"
                + " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < names.size(); i++) {
                CloneName c = (CloneName) names.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getValue());
                stmt.setString(4, c.getUrl());
                //System.out.println(c.getType()+"\t"+c.getValue());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONENAME table");
            return false;
        }
        return true;
    }

    public boolean insertCloneInserts(List inserts) {
        if (inserts == null) {
            return true;
        }

        String sql = "insert into dnainsert"
                + " (insertid,insertorder,sizeinbp,species,format,source,geneid,name,description,targetseqid,targetgenbank,hasdiscrepancy,hasmutation,refseqid,region)"
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String sql1 = "insert into cloneinsert (cloneid, insertid) values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            PreparedStatement stmt1 = conn.prepareStatement(sql1);

            for (int i = 0; i < inserts.size(); i++) {
                DnaInsert c = (DnaInsert) inserts.get(i);
                stmt.setInt(1, c.getInsertid());
                stmt.setInt(2, c.getOrder());

                int size = c.getSize();
                if (size > 0) {
                    stmt.setInt(3, size);
                } else {
                    stmt.setString(3, null);
                }

                stmt.setString(4, c.getSpecies());
                stmt.setString(5, c.getFormat());
                stmt.setString(6, c.getSource());
                stmt.setString(7, c.getGeneid());
                stmt.setString(8, c.getName());
                stmt.setString(9, c.getDescription());
                stmt.setString(10, c.getTargetseqid());
                stmt.setString(11, c.getTargetgenbank());
                stmt.setString(12, c.getHasdiscrepancy());
                stmt.setString(13, c.getHasmutation());
                stmt.setString(15, c.getRegion());

                int refseqid = c.getRefseqid();
                if (refseqid > 0) {
                    stmt.setInt(14, refseqid);
                } else {
                    stmt.setString(14, null);
                }
                //System.out.println(c.getSpecies());
                DatabaseTransaction.executeUpdate(stmt);

                stmt1.setInt(1, c.getCloneid());
                stmt1.setInt(2, c.getInsertid());
                DatabaseTransaction.executeUpdate(stmt1);
            }
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt1);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into DNAINSERT table");
            return false;
        }
        return true;
    }

    public boolean insertCloneInsertsWithoutInsertInfo(List inserts) {
        if (inserts == null) {
            return true;
        }

        String sql1 = "insert into cloneinsert (cloneid, insertid) values(?,?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);

            for (int i = 0; i < inserts.size(); i++) {
                DnaInsert c = (DnaInsert) inserts.get(i);
                stmt1.setInt(1, c.getCloneid());
                stmt1.setInt(2, c.getInsertid());
                DatabaseTransaction.executeUpdate(stmt1);
            }
            DatabaseTransaction.closeStatement(stmt1);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into DNAINSERT table");
            return false;
        }
        return true;
    }

    public boolean insertCloneAuthors(List authors) {
        if (authors == null) {
            return true;
        }

        String sql = "insert into cloneauthor"
                + " (cloneid, authorid, authortype)"
                + " values(?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < authors.size(); i++) {
                CloneAuthor c = (CloneAuthor) authors.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getAuthorid());
                stmt.setString(3, c.getAuthortype());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEAUTHOR table");
            return false;
        }
        return true;
    }

    public boolean insertClonePublications(List pubs) {
        if (pubs == null) {
            return true;
        }

        String sql = "insert into clonepublication"
                + " (cloneid, publicationid)"
                + " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < pubs.size(); i++) {
                ClonePublication c = (ClonePublication) pubs.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setInt(2, c.getPublicationid());
                //System.out.println(c.getCloneid()+"\t"+c.getPublicationid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEPUBLICATION table");
            return false;
        }
        return true;
    }

    public boolean insertCloneProperties(List properties) {
        if (properties == null) {
            return true;
        }

        String sql = "insert into cloneproperty"
                + " (cloneid, propertytype,propertyvalue,extrainfo)"
                + " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < properties.size(); i++) {
                CloneProperty c = (CloneProperty) properties.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getValue());
                stmt.setString(4, c.getExtrainfo());
                //System.out.println(c.getCloneid());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONEPROPERTY table");
            return false;
        }
        return true;
    }

    public boolean insertCloneCollections(List collections) {
        if (collections == null) {
            return true;
        }

        String sql = "insert into clonecollection"
                + " (cloneid, name)"
                + " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < collections.size(); i++) {
                CloneCollection c = (CloneCollection) collections.get(i);
                stmt.setInt(1, c.getCloneid());
                stmt.setString(2, c.getName());
                //System.out.println(c.getName());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CLONECOLLECTION table");
            return false;
        }
        return true;
    }

    public boolean insertInsertProperties(List properties) {
        if (properties == null) {
            return true;
        }

        String sql = "insert into insertproperty"
                + " (insertid, propertytype,propertyvalue,extrainfo)"
                + " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < properties.size(); i++) {
                InsertProperty c = (InsertProperty) properties.get(i);
                stmt.setInt(1, c.getInsertid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getValue());
                stmt.setString(4, c.getExtrainfo());

                //System.out.println(c.getType()+"\t"+c.getValue());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into INSERTPROPERTY table");
            return false;
        }
        return true;
    }

    public List queryClonesByOrderids(String orderidString) {
        String sql = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname,"
                + " clonemapfilename,status,specialtreatment,source,description,c.cloneid,o.orderid"
                + " from clone c, orderclones o where c.cloneid=o.cloneid and o.orderid in (" + orderidString + ")"
                + " order by o.orderid";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List clones = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String clonename = rs.getString(1);
                String clonetype = rs.getString(2);
                String verified = rs.getString(3);
                String vermethod = rs.getString(4);
                String domain = rs.getString(5);
                String subdomain = rs.getString(6);
                String restriction = rs.getString(7);
                String comments = rs.getString(8);
                int vectorid = rs.getInt(9);
                String vectorname = rs.getString(10);
                String clonemap = rs.getString(11);
                String status = rs.getString(12);
                String specialtreatment = rs.getString(13);
                String src = rs.getString(14);
                String des = rs.getString(15);
                int cloneid = rs.getInt(16);
                int orderid = rs.getInt(17);
                Clone c = new Clone(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemap, status, specialtreatment, src, des);
                c.setOrderid(orderid);
                clones.add(c);
            }
            return performQueryClones(clones, false, false, true, true, false, false);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into INSERTPROPERTY table");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    /**
     * Query the database to get a list of Clone objects for given list of cloneid.
     * @param cloneids A list of cloneid as String.
     * @param isInsert Insert will be queried if it is true.
     * @param isSelection Selection will be queried if it is true.
     * @param isWorkingStorage Working storage location will be queried if it is true.
     * @return A map with cloneid as the key and Clone object as the value. Return null if
     *  error occured.
     */
    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, List restrictions, List clonetypes, String species, String status, boolean isInsertseq, boolean isClonename) {
        String sq = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname,"
                + " clonemapfilename,status,specialtreatment,source,description,cloneid"
                + " from clone where cloneid =?";

        if (clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sq = sq + " and clonetype in (" + s + ")";
        }

        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sq = sq + " and restriction in (" + s + ")";
        }

        if (species != null) {
            sq = sq + " and domain='" + species + "'";
        }

        if (status != null) {
            sq += " and status='" + status + "'";
        }

        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, sq, isInsertseq, isClonename);
    }

    /**
     * Query the database to get a list of Clone objects for given list of cloneid.
     * @param cloneids A list of cloneid as String.
     * @param isInsert Insert will be queried if it is true.
     * @param isSelection Selection will be queried if it is true.
     * @param isWorkingStorage Working storage location will be queried if it is true.
     * @return A map with cloneid as the key and Clone object as the value. Return null if
     *  error occured.
     */
    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, boolean isInsertseq, boolean isClonename) {
        String sq = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname,"
                + " clonemapfilename,status,specialtreatment,source,description,cloneid"
                + " from clone where cloneid =? and status='" + Clone.AVAILABLE + "'";

        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, sq, isInsertseq, isClonename);
    }

    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, true);
    }

    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, boolean isClonename) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, false, isClonename);
    }

    public Map queryClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, false, false);
    }

    public Map queryAvailableClonesByCloneid(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage) {
        return queryClonesByCloneid(cloneids, isInsert, isSelection, isWorkingStorage);
    }

    public String queryCloneSequenceByCloneid(int cloneid) {
        String sql = "select insertid from cloneinsert where cloneid=" + cloneid;
        String seq = "";

        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
            String insertid = null;
            while (rs.next()) {
                if (insertid == null) {
                    insertid = rs.getString(1);
                } else {
                    insertid = insertid + "," + rs.getString(1);
                }
            }
            DatabaseTransaction.closeResultSet(rs);

            if (insertid == null) {
                return seq;
            }
            sql = "select seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid in (" + insertid + ") order by seqorder";
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String seqtext = rs.getString(1);
                seq += seqtext;
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while query clone sequence by cloneid: " + cloneid);
        }
        return seq;
    }

    public String queryCloneSequenceByCloneid(DatabaseTransaction t, int cloneid) {
        String sql = "select insertid from cloneinsert where cloneid=" + cloneid;
        String seq = "";

        try {
            ResultSet rs = t.executeQuery(sql);
            String insertid = null;
            while (rs.next()) {
                if (insertid == null) {
                    insertid = rs.getString(1);
                } else {
                    insertid = insertid + "," + rs.getString(1);
                }
            }
            DatabaseTransaction.closeResultSet(rs);

            if (insertid == null) {
                return seq;
            }
            sql = "select seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid in (" + insertid + ") order by seqorder";
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String seqtext = rs.getString(1);
                seq += seqtext;
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while query clone sequence by cloneid: " + cloneid);
        }
        return seq;
    }

    public String queryReferenceSequenceByCloneid(int cloneid) {
        String sql = "select refseqid from dnainsert where insertid in"
                + "(select insertid from cloneinsert where cloneid=" + cloneid + ")";
        String sql2 = "select seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.referenceid =? order by seqorder";

        String seq = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        PreparedStatement stmt = null;
        Connection c = null;

        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql2);

            rs = t.executeQuery(sql);
            int refseqid = -1;
            while (rs.next()) {
                refseqid = rs.getInt(1);

                if (refseqid != -1) {
                    stmt.setInt(1, refseqid);
                    rs2 = DatabaseTransaction.executeQuery(stmt);
                    while (rs2.next()) {
                        String seqtext = rs2.getString(1);
                        seq += seqtext;
                    }
                    if (seq.length() > 0) {
                        return seq;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while query clone sequence by cloneid: " + cloneid);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(c);
        }
        return seq;
    }

    public static void queryCloneSequenceForValidation(CloneOrder order) {
        List<OrderClones> clones = order.getClones();
        DatabaseTransaction t = null;
        String error = null;
        try {
            t = DatabaseTransaction.getInstance();

            for (OrderClones orderClone : clones) {
                Clone clone = orderClone.getClone();
                error = clone.getName();
                String seq = "";
                String sql2 = "select insertid from cloneinsert where cloneid=" + clone.getCloneid();
                ResultSet rs2 = t.executeQuery(sql2);
                String insertid = null;
                while (rs2.next()) {
                    if (insertid == null) {
                        insertid = rs2.getString(1);
                    } else {
                        insertid = insertid + "," + rs2.getString(1);
                    }
                }
                DatabaseTransaction.closeResultSet(rs2);
                if (insertid == null) {
                    String refseqid = null;
                    String sql4 = "select refseqid from dnainsert where insertid in"
                            + "(select insertid from cloneinsert where cloneid=" + clone.getCloneid() + ")";
                    ResultSet rs4 = t.executeQuery(sql4);
                    while (rs4.next()) {
                        if (refseqid == null) {
                            refseqid = rs4.getString(1);
                        } else {
                            refseqid = refseqid + "," + rs4.getString(1);
                        }
                    }
                    DatabaseTransaction.closeResultSet(rs4);
                    if (refseqid != null) {
                        String sql5 = "select seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.referenceid in (" + refseqid + ") order by seqorder";
                        ResultSet rs5 = t.executeQuery(sql5);
                        while (rs5.next()) {
                            String seqtext = rs5.getString(1);
                            seq += seqtext;
                        }
                        DatabaseTransaction.closeResultSet(rs5);
                    }
                } else {
                    String sql3 = "select seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid in (" + insertid + ") order by seqorder";
                    ResultSet rs3 = t.executeQuery(sql3);
                    while (rs3.next()) {
                        String seqtext = rs3.getString(1);
                        seq += seqtext;
                    }
                    DatabaseTransaction.closeResultSet(rs3);
                }

                clone.setSequence(seq);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage() + "\n" + "Error occured while query clone sequence by cloneid: " + error);
        }
    }

    public Clone queryCloneByCloneid(int cloneid) {
        return queryCloneByCloneid(cloneid, false);
    }

    public Clone queryCloneByCloneid(int cloneid, boolean querySubmission) {
        if (cloneid < 1) {
            return null;
        }
        String sql = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname, clonemapfilename, status,specialtreatment,source,description"
                + " from clone where cloneid=" + cloneid;
        String sql2 = "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format,"
                + " d.source,d.geneid,d.name,d.description,d.targetseqid,d.targetgenbank,"
                + " d.hasdiscrepancy,d.hasmutation,d.refseqid,d.region,d.annotation"
                + " from dnainsert d, cloneinsert cd, clone c"
                + " where c.cloneid=cd.cloneid and cd.insertid=d.insertid and c.cloneid=" + cloneid;
        String sql3 = "select hosttype, marker from cloneselection where cloneid=" + cloneid;
        String sql4 = "select c.cloneid,c.authorid,a.authorname,c.authortype"
                + " from cloneauthor c, authorinfo a"
                + " where c.authorid=a.authorid"
                + " and c.cloneid=" + cloneid;
        String sql5 = "select g.growthid,name, hosttype,antibioticselection,growthcondition,comments"
                + " from clonegrowth c, growthcondition g"
                + " where c.growthid=g.growthid"
                + " and c.cloneid=" + cloneid
                + " and c.isrecommended='" + CloneGrowth.YES + "'";
        String sql6 = "select p.publicationid,p.title, p.pmid"
                + " from publication p, clonepublication c"
                + " where p.publicationid=c.publicationid"
                + " and c.cloneid = " + cloneid;
        String sql7 = "select h.cloneid, h.hoststrain, h.isinuse, h.description"
                + " from host h where h.cloneid=" + cloneid;
        String sql8 = "select cloneid,nametype,namevalue,nameurl"
                + " from clonename where cloneid=" + cloneid;
        String sql9 = "select vectorid,name,description,form,type,sizeinbp,mapfilename,sequencefilename,comments"
                + " from vector where vectorid=?";
        String sql10 = "select propertytype from vectorproperty where vectorid=?";
        String sql11 = "select propertytype,propertyvalue,extrainfo from cloneproperty where cloneid=" + cloneid;
        String sql12 = "select propertytype,propertyvalue,extrainfo from insertproperty where insertid=?";
        String sql13 = "select seqorder,seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid=? order by seqorder";
        String sql14 = "select submitter, TO_CHAR(submitdate, 'DD-MON-YYYY'), receiver, TO_CHAR(receivedate, 'DD-MON-YYYY') from clonesubmission where cloneid=" + cloneid + " order by submissionid desc";

        Clone c = null;

        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
            if (rs.next()) {
                String clonename = rs.getString(1);
                String clonetype = rs.getString(2);
                String verified = rs.getString(3);
                String vermethod = rs.getString(4);
                String domain = rs.getString(5);
                String subdomain = rs.getString(6);
                String restriction = rs.getString(7);
                String comments = rs.getString(8);
                int vectorid = rs.getInt(9);
                String vectorname = rs.getString(10);
                String clonemap = rs.getString(11);
                String status = rs.getString(12);
                String specialtreatment = rs.getString(13);
                String src = rs.getString(14);
                String d = rs.getString(15);

                String submitter = "", receiver = "";
                String submitdate = "", receivedate = "";
                if (querySubmission) {
                    ResultSet rs14 = t.executeQuery(sql14);
                    if (rs14.next()) {
                        submitter = rs14.getString(1);
                        submitdate = rs14.getString(2);
                        receiver = rs14.getString(3);
                        receivedate = rs14.getString(4);
                    }
                }
                c = new Clone(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemap, status, specialtreatment, src, d, submitter, submitdate, receiver, receivedate);

                List inserts = new ArrayList();
                ResultSet rs2 = t.executeQuery(sql2);
                while (rs2.next()) {
                    int insertid = rs2.getInt(1);
                    int insertorder = rs2.getInt(2);
                    int size = rs2.getInt(3);
                    String species = rs2.getString(4);
                    String format = rs2.getString(5);
                    String source = rs2.getString(6);
                    String geneid = rs2.getString(7);
                    String name = rs2.getString(8);
                    String description = rs2.getString(9);
                    String targetseqid = rs2.getString(10);
                    String targetgenbank = rs2.getString(11);
                    String hasdiscrepancy = rs2.getString(12);
                    String hasmutation = rs2.getString(13);
                    int refseqid = rs2.getInt(14);
                    String region = rs2.getString(15);
                    String annotation = rs2.getString(16);
                    DnaInsert insert = new DnaInsert(insertid, insertorder, size, species, format, source, cloneid, geneid, name, description, targetseqid, targetgenbank, hasdiscrepancy, hasmutation, region, refseqid);
                    insert.setAnnotation(annotation);

                    PreparedStatement stmt1 = conn.prepareStatement(sql12);
                    stmt1.setInt(1, insertid);
                    ResultSet rs12 = DatabaseTransaction.executeQuery(stmt1);
                    List properties = new ArrayList();
                    while (rs12.next()) {
                        String type = rs12.getString(1);
                        String value = rs12.getString(2);
                        String extrainfo = rs12.getString(3);
                        InsertProperty p = new InsertProperty(insertid, type, value, extrainfo);
                        properties.add(p);
                    }
                    DatabaseTransaction.closeResultSet(rs12);
                    DatabaseTransaction.closeStatement(stmt1);

                    PreparedStatement stmt2 = conn.prepareStatement(sql13);
                    stmt2.setInt(1, insertid);
                    ResultSet rs13 = DatabaseTransaction.executeQuery(stmt2);
                    String seq = "";
                    while (rs13.next()) {
                        int seqorder = rs13.getInt(1);
                        String seqtext = rs13.getString(2);
                        seq += seqtext;
                    }
                    DatabaseTransaction.closeResultSet(rs13);
                    DatabaseTransaction.closeStatement(stmt2);

                    insert.setProperties(properties);
                    insert.setSequence(seq);
                    inserts.add(insert);
                }
                c.setInserts(inserts);
                DatabaseTransaction.closeResultSet(rs2);

                List selections = new ArrayList();
                ResultSet rs3 = t.executeQuery(sql3);
                while (rs3.next()) {
                    String hosttype = rs3.getString(1);
                    String marker = rs3.getString(2);
                    CloneSelection selection = new CloneSelection(cloneid, hosttype, marker);
                    selections.add(selection);
                }
                c.setSelections(selections);
                DatabaseTransaction.closeResultSet(rs3);

                List authors = new ArrayList();
                ResultSet rs4 = t.executeQuery(sql4);
                while (rs4.next()) {
                    int id = rs4.getInt(1);
                    int authorid = rs4.getInt(2);
                    String authorname = rs4.getString(3);
                    String authortype = rs4.getString(4);
                    CloneAuthor a = new CloneAuthor(id, authorid, authorname, authortype);
                    authors.add(a);
                }
                c.setAuthors(authors);
                DatabaseTransaction.closeResultSet(rs4);

                ResultSet rs5 = t.executeQuery(sql5);
                if (rs5.next()) {
                    int growthid = rs5.getInt(1);
                    String growthname = rs5.getString(2);
                    String hosttype = rs5.getString(3);
                    String antibioticselection = rs5.getString(4);
                    String condition = rs5.getString(5);
                    String com = rs5.getString(6);
                    GrowthCondition gc = new GrowthCondition(growthid, growthname, hosttype, antibioticselection, condition, com);
                    c.setRecommendedGrowthCondition(gc);
                }
                DatabaseTransaction.closeResultSet(rs5);

                ResultSet rs6 = t.executeQuery(sql6);
                List publications = new ArrayList();
                while (rs6.next()) {
                    int publicationid = rs6.getInt(1);
                    String title = rs6.getString(2);
                    String pmid = rs6.getString(3);
                    Publication p = new Publication(publicationid, title, pmid);
                    publications.add(p);
                }
                c.setPublications(publications);
                DatabaseTransaction.closeResultSet(rs6);

                ResultSet rs7 = t.executeQuery(sql7);
                List hosts = new ArrayList();
                while (rs7.next()) {
                    int id = rs7.getInt(1);
                    String strain = rs7.getString(2);
                    String isinuse = rs7.getString(3);
                    String description = rs7.getString(4);
                    CloneHost h = new CloneHost(id, strain, isinuse, description);
                    hosts.add(h);
                }
                c.setHosts(hosts);
                DatabaseTransaction.closeResultSet(rs7);

                ResultSet rs8 = t.executeQuery(sql8);
                List names = new ArrayList();
                while (rs8.next()) {
                    int id = rs8.getInt(1);
                    String nametype = rs8.getString(2);
                    String namevalue = rs8.getString(3);
                    String nameurl = rs8.getString(4);
                    CloneName n = new CloneName(id, nametype, namevalue, nameurl);
                    names.add(n);
                }
                c.setNames(names);
                DatabaseTransaction.closeResultSet(rs8);

                PreparedStatement stmt = conn.prepareStatement(sql9);
                stmt.setInt(1, c.getVectorid());
                ResultSet rs9 = DatabaseTransaction.executeQuery(stmt);
                if (rs9.next()) {
                    int vid = rs9.getInt(1);
                    String vname = rs9.getString(2);
                    String desc = rs9.getString(3);
                    String form = rs9.getString(4);
                    String type = rs9.getString(5);
                    int size = rs9.getInt(6);
                    String mapfile = rs9.getString(7);
                    String seqfile = rs9.getString(8);
                    String vcomments = rs9.getString(9);
                    CloneVector v = new CloneVector(vid, vname, desc, form, type, size, mapfile, seqfile, vcomments);

                    PreparedStatement stmt1 = conn.prepareStatement(sql10);
                    stmt1.setInt(1, vid);
                    ResultSet rs10 = DatabaseTransaction.executeQuery(stmt1);
                    List properties = new ArrayList();
                    while (rs10.next()) {
                        String p = rs10.getString(1);
                        properties.add(p);
                    }
                    v.setProperty(properties);
                    c.setVector(v);

                    DatabaseTransaction.closeResultSet(rs10);
                    DatabaseTransaction.closeStatement(stmt1);
                }
                DatabaseTransaction.closeResultSet(rs9);
                DatabaseTransaction.closeStatement(stmt);

                ResultSet rs11 = t.executeQuery(sql11);
                List cloneproperties = new ArrayList();
                while (rs11.next()) {
                    String type = rs11.getString(1);
                    String value = rs11.getString(2);
                    String extrainfo = rs11.getString(3);
                    CloneProperty n = new CloneProperty(cloneid, type, value, extrainfo);
                    cloneproperties.add(n);
                }
                c.setProperties(cloneproperties);
                DatabaseTransaction.closeResultSet(rs11);
            } else {
                handleError(null, "No clone record found for cloneid: " + cloneid);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while query clones by cloneid: " + cloneid);
        }
        return c;
    }

    private Map performQueryClones(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, String sql) {
        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, sql, false);
    }

    private Map performQueryClones(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, String sql, boolean isInsertseq) {
        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, isGrowth, sql, isInsertseq, false);
    }

    private Map performQueryClones(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, boolean isGrowth, String sql, boolean isInsertseq, boolean isClonename) {
        Map clones = new TreeMap();
        if (cloneids == null || cloneids.size() == 0) {
            return clones;
        }

        String sql2 = getInsertSql();
        String sql3 = getSelectionSql();
        String sql4 = getStorageSql();
        String sql5 = getGrowthSql();
        String sql6 = getInsertseqSql();
        String sql7 = getClonenameSql();

        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        PreparedStatement stmt7 = null;

        ResultSet rs = null;
        int currentCloneid = 0;
        try {
            stmt = conn.prepareStatement(sql);
            if (isInsert) {
                stmt2 = conn.prepareStatement(sql2);
            }
            if (isSelection) {
                stmt3 = conn.prepareStatement(sql3);
            }
            if (isWorkingStorage) {
                stmt4 = conn.prepareStatement(sql4);
            }
            if (isGrowth) {
                stmt5 = conn.prepareStatement(sql5);
            }
            if (isClonename) {
                stmt7 = conn.prepareStatement(sql7);
            }

            for (int i = 0; i < cloneids.size(); i++) {
                String cid = (String) cloneids.get(i);
                currentCloneid = Integer.parseInt(cid);
                stmt.setInt(1, currentCloneid);
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    String clonename = rs.getString(1);
                    String clonetype = rs.getString(2);
                    String verified = rs.getString(3);
                    String vermethod = rs.getString(4);
                    String domain = rs.getString(5);
                    String subdomain = rs.getString(6);
                    String restriction = rs.getString(7);
                    String comments = rs.getString(8);
                    int vectorid = rs.getInt(9);
                    String vectorname = rs.getString(10);
                    String clonemap = rs.getString(11);
                    String status = rs.getString(12);
                    String specialtreatment = rs.getString(13);
                    String src = rs.getString(14);
                    String des = rs.getString(15);
                    int cloneid = rs.getInt(16);
                    CloneInfo c = new CloneInfo(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemap, status, specialtreatment, src, des);

                    if (isInsert) {
                        stmt2.setInt(1, cloneid);
                        List inserts = getInserts(stmt2, cloneid);
                        if (isInsertseq) {
                            if (inserts != null && inserts.size() > 0) {
                                stmt6 = conn.prepareStatement(sql6);
                                for (int n = 0; n < inserts.size(); n++) {
                                    DnaInsert insert = (DnaInsert) inserts.get(n);
                                    stmt6.setInt(1, insert.getInsertid());
                                    ResultSet rs6 = DatabaseTransaction.executeQuery(stmt6);
                                    String seq = "";
                                    while (rs6.next()) {
                                        int seqorder = rs6.getInt(1);
                                        String seqtext = rs6.getString(2);
                                        seq += seqtext;
                                    }
                                    insert.setSequence(seq);
                                    DatabaseTransaction.closeResultSet(rs6);
                                }
                                DatabaseTransaction.closeStatement(stmt6);
                            }
                        }
                        c.setInserts(inserts);
                    }

                    if (isSelection) {
                        stmt3.setInt(1, cloneid);
                        List selections = getSelections(stmt3, cloneid);
                        c.setSelections(selections);
                    }

                    if (isWorkingStorage) {
                        stmt4.setInt(1, cloneid);
                        stmt4.setString(2, Sample.WORKING_GLYCEROL);
                        setStorage(stmt4, c);
                    }

                    if (isGrowth) {
                        stmt5.setInt(1, cloneid);
                        GrowthCondition g = getRecommendedGrowth(stmt5, cloneid);
                        c.setRecommendedGrowthCondition(g);
                    }

                    if (isClonename) {
                        stmt7.setInt(1, cloneid);
                        List names = getClonenames(stmt7, cloneid);
                        c.setNames(names);
                    }

                    clones.put(cid, c);
                } else {
                    clones.put(cid, null);
                }
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while query clones");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
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
        }
        return clones;
    }

    private Map performQueryClones(List cloneids, boolean isInsert, boolean isSelection, boolean isWorkingStorage, String sql) {
        return performQueryClones(cloneids, isInsert, isSelection, isWorkingStorage, false, sql);
    }

    public List performQueryClones(List clones, boolean isInsert, boolean isSelection, boolean isGrowth, boolean isWorkingStorage, boolean isClonename) {
        return performQueryClones(clones, isInsert, isSelection, isGrowth, isWorkingStorage, false, isClonename);
    }

    public List performQueryClones(List clones, boolean isInsert, boolean isSelection, boolean isGrowth, boolean isWorkingStorage, boolean isInsertseq, boolean isClonename) {
        List infos = new ArrayList();
        if (clones == null || clones.size() == 0) {
            return infos;
        }

        String sql2 = getInsertSql();
        String sql3 = getSelectionSql();
        String sql4 = getStorageSql();
        String sql5 = getGrowthSql();
        String sql6 = getInsertseqSql();
        String sql7 = getClonenameSql();

        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        PreparedStatement stmt5 = null;
        PreparedStatement stmt6 = null;
        PreparedStatement stmt7 = null;

        int currentCloneid = 0;
        try {
            if (isInsert) {
                stmt2 = conn.prepareStatement(sql2);
            }
            if (isSelection) {
                stmt3 = conn.prepareStatement(sql3);
            }
            if (isWorkingStorage) {
                stmt4 = conn.prepareStatement(sql4);
            }
            if (isGrowth) {
                stmt5 = conn.prepareStatement(sql5);
            }
            if (isClonename) {
                stmt7 = conn.prepareStatement(sql7);
            }

            for (int i = 0; i < clones.size(); i++) {
                Clone clone = (Clone) clones.get(i);
                CloneInfo c = new CloneInfo(null, clone);
                int cloneid = clone.getCloneid();
                currentCloneid = cloneid;

                if (isInsert) {
                    stmt2.setInt(1, cloneid);
                    List inserts = getInserts(stmt2, cloneid);
                    if (isInsertseq) {
                        if (inserts != null && inserts.size() > 0) {
                            stmt6 = conn.prepareStatement(sql6);
                            for (int n = 0; n < inserts.size(); n++) {
                                DnaInsert insert = (DnaInsert) inserts.get(n);
                                stmt6.setInt(1, insert.getInsertid());
                                ResultSet rs6 = DatabaseTransaction.executeQuery(stmt6);
                                String seq = "";
                                while (rs6.next()) {
                                    int seqorder = rs6.getInt(1);
                                    String seqtext = rs6.getString(2);
                                    seq += seqtext;
                                }
                                insert.setSequence(seq);
                                DatabaseTransaction.closeResultSet(rs6);
                            }
                            DatabaseTransaction.closeStatement(stmt6);
                        }
                    }
                    c.setInserts(inserts);
                }

                if (isSelection) {
                    stmt3.setInt(1, cloneid);
                    List selections = getSelections(stmt3, cloneid);
                    c.setSelections(selections);
                }

                if (isGrowth) {
                    stmt5.setInt(1, cloneid);
                    GrowthCondition g = getRecommendedGrowth(stmt5, cloneid);
                    c.setRecommendedGrowthCondition(g);
                }

                if (isWorkingStorage) {
                    stmt4.setInt(1, cloneid);
                    stmt4.setString(2, Sample.WORKING_GLYCEROL);
                    setStorage(stmt4, c);
                }

                if (isClonename) {
                    stmt7.setInt(1, cloneid);
                    List names = getClonenames(stmt7, cloneid);
                    c.setNames(names);
                }
                infos.add(c);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones by cloneid: " + currentCloneid);
            return null;
        } finally {
            if (stmt4 != null) {
                DatabaseTransaction.closeStatement(stmt4);
            }
            if (stmt2 != null) {
                DatabaseTransaction.closeStatement(stmt2);
            }
            if (stmt3 != null) {
                DatabaseTransaction.closeStatement(stmt3);
            }
            if (stmt5 != null) {
                DatabaseTransaction.closeStatement(stmt5);
            }
            if (stmt7 != null) {
                DatabaseTransaction.closeStatement(stmt7);
            }
        }
        return infos;
    }

    private String getInsertSql() {
        return "select d.insertid, d.insertorder, d.sizeinbp, d.species, d.format,"
                + " d.source,d.geneid,d.name,d.description,d.targetseqid,d.targetgenbank,"
                + " d.hasdiscrepancy,d.hasmutation,d.refseqid,d.region,d.annotation"
                + " from dnainsert d, cloneinsert cd, clone c"
                + " where d.insertid=cd.insertid and c.cloneid=cd.cloneid and c.cloneid=?";
    }

    private String getInsertseqSql() {
        return "select seqorder,seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid=? order by seqorder";
    }

    private String getSelectionSql() {
        return "select hosttype, marker from cloneselection where cloneid=?";
    }

    private String getStorageSql() {
        return "select containerlabel, position, positionx, positiony from sample where cloneid=? and sampletype=? order by sampleid desc";
    }

    private String getGrowthSql() {
        String s = "select g.growthid, g.name, g.hosttype, g.antibioticselection, g.growthcondition, g.comments"
                + " from growthcondition g, clonegrowth c where g.growthid=c.growthid"
                + " and c.isrecommended='" + CloneGrowth.YES + "' and c.cloneid=?";
        return s;
    }

    private String getClonenameSql() {
        String s = "select nametype,namevalue,nameurl"
                + " from clonename where cloneid=?";
        return s;
    }

    private List getInserts(PreparedStatement stmt, int cloneid) throws Exception {
        List inserts = new ArrayList();
        ResultSet rs2 = DatabaseTransaction.executeQuery(stmt);
        while (rs2.next()) {
            int insertid = rs2.getInt(1);
            int insertorder = rs2.getInt(2);
            int size = rs2.getInt(3);
            String species = rs2.getString(4);
            String format = rs2.getString(5);
            String source = rs2.getString(6);
            String geneid = rs2.getString(7);
            String name = rs2.getString(8);
            String description = rs2.getString(9);
            String targetseqid = rs2.getString(10);
            String targetgenbank = rs2.getString(11);
            String hasdiscrepancy = rs2.getString(12);
            String hasmutation = rs2.getString(13);
            int refseqid = rs2.getInt(14);
            String region = rs2.getString(15);
            String annotation = rs2.getString(16);
            DnaInsert insert = new DnaInsert(insertid, insertorder, size, species, format, source, cloneid, geneid, name, description, targetseqid, targetgenbank, hasdiscrepancy, hasmutation, region, refseqid);
            insert.setAnnotation(annotation);
            inserts.add(insert);
        }
        DatabaseTransaction.closeResultSet(rs2);

        return inserts;
    }

    private List getSelections(PreparedStatement stmt, int cloneid) throws Exception {
        List selections = new ArrayList();
        ResultSet rs3 = DatabaseTransaction.executeQuery(stmt);
        while (rs3.next()) {
            String hosttype = rs3.getString(1);
            String marker = rs3.getString(2);
            CloneSelection selection = new CloneSelection(cloneid, hosttype, marker);
            selections.add(selection);
        }
        DatabaseTransaction.closeResultSet(rs3);

        return selections;
    }

    private List getClonenames(PreparedStatement stmt, int cloneid) throws Exception {
        List names = new ArrayList();
        ResultSet rs = DatabaseTransaction.executeQuery(stmt);
        while (rs.next()) {
            String nametype = rs.getString(1);
            String namevalue = rs.getString(2);
            String nameurl = rs.getString(3);
            CloneName name = new CloneName(cloneid, nametype, namevalue, nameurl);
            names.add(name);
        }
        DatabaseTransaction.closeResultSet(rs);
        return names;
    }

    private void setStorage(PreparedStatement stmt, CloneInfo c) throws Exception {
        ResultSet rs4 = DatabaseTransaction.executeQuery(stmt);
        if (rs4.next()) {
            String label = rs4.getString(1);
            int position = rs4.getInt(2);
            String x = rs4.getString(3);
            String y = rs4.getString(4);
            c.setPlate(label);
            c.setPosition(position);
            c.setWell(x, y);
        }
        DatabaseTransaction.closeResultSet(rs4);
    }

    private GrowthCondition getRecommendedGrowth(PreparedStatement stmt, int cloneid) throws Exception {
        GrowthCondition g = null;
        ResultSet rs = DatabaseTransaction.executeQuery(stmt);
        while (rs.next()) {
            int growthid = rs.getInt(1);
            String name = rs.getString(2);
            String hosttype = rs.getString(3);
            String selection = rs.getString(4);
            String growthcondition = rs.getString(5);
            String comments = rs.getString(6);
            g = new GrowthCondition(growthid, name, hosttype, selection, growthcondition, comments);
        }
        DatabaseTransaction.closeResultSet(rs);

        return g;
    }

    public List findRestrictedClones(List cloneidStrings, List restrictions) {
        String sql = "select clonename, restriction from clone where cloneid=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List restrictedClones = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < cloneidStrings.size(); i++) {
                String cloneid = (String) cloneidStrings.get(i);
                stmt.setInt(1, Integer.parseInt(cloneid));
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    String clonename = rs.getString(1);
                    String restriction = rs.getString(2);

                    boolean match = false;
                    for (int k = 0; k < restrictions.size(); k++) {
                        String res = (String) restrictions.get(k);
                        if (restriction.equals(res)) {
                            match = true;
                            break;
                        }
                    }

                    if (!match) {
                        restrictedClones.add(clonename);
                    }
                } else {
                    handleError(null, "Cannot find clone for cloneid: " + cloneid);
                    return null;
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }

        return restrictedClones;
    }

    public List findShippingRestrictedClones(List cloneidStrings) {
        String sql = "select clonename, domain from clone where cloneid=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List restrictedClones = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < cloneidStrings.size(); i++) {
                String cloneid = (String) cloneidStrings.get(i);
                stmt.setInt(1, Integer.parseInt(cloneid));
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    String clonename = rs.getString(1);
                    String species = rs.getString(2);
                    if (species.equals(DnaInsert.BA) && !"BaCD00071996".equals(clonename)) {
                        restrictedClones.add(clonename);
                    }
                } else {
                    handleError(null, "Cannot find clone for cloneid: " + cloneid);
                    return null;
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Database error occured.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }

        return restrictedClones;
    }

    public List queryCloneidsByCloneType(List clonetypes) {
        return queryCloneidsByCloneType(clonetypes, false);
    }

    public List queryCloneidsByCloneType(List clonetypes, boolean isPSI) {
        String sql = "select cloneid from clone";

        if (clonetypes != null && clonetypes.size() > 0) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql + " where clonetype in (" + s + ")";
        }

        if (isPSI) {
            sql = sql + " and cloneid in (select cloneid from cloneproperty where propertytype='Collection' and propertyvalue='PSI')";
        }

        PreparedStatement stmt = null;

        List cloneids = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                int cloneid = rs.getInt(1);
                cloneids.add((new Integer(cloneid)).toString());
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones.");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        return cloneids;
    }

    public static int queryCloneCounts(Map foundList, List nofound, String sql) {
        if (foundList == null || foundList.size() == 0) {
            return 0;
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String currentCloneid = "";
        int totalCount = 0;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            Set terms = foundList.keySet();
            Iterator iter = terms.iterator();
            while (iter.hasNext()) {
                String term = (String) iter.next();
                List cloneids = (List) foundList.get(term);
                int count = 0;
                for (int i = 0; i < cloneids.size(); i++) {
                    String cloneid = (String) cloneids.get(i);
                    currentCloneid = cloneid;
                    stmt.setInt(1, Integer.parseInt(cloneid));
                    rs = DatabaseTransaction.executeQuery(stmt);
                    if (rs.next()) {
                        int n = rs.getInt(1);
                        count += n;
                    }
                }
                if (count > 0) {
                    totalCount += count;
                } else {
                    nofound.add(term);
                }
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println("Error occured while query clone counts by cloneid: " + currentCloneid);
                ex.printStackTrace();
            }
            return 0;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
        return totalCount;
    }

    public List queryCloneSamples(List clones) {
        if (clones == null) {
            return null;
        }

        String sql = "select s.sampletype, s.position, s.containerlabel, s.result, s.sampleid,"
                + " s.cloneid, s.containerid, s.positionx, s.positiony, c.containertype"
                + " from sample s, containerheader c where s.containerid=c.containerid"
                + " and s.sampletype<>'" + Sample.CONTAMINATED + "' and s.cloneid in (select cloneid from clone where clonename=?)";

        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        List m = new ArrayList();
        String currentClone = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            ResultSet rs = null;

            for (int i = 0; i < clones.size(); i++) {
                String clonename = (String) clones.get(i);
                Clone clone = new Clone();
                clone.setName(clonename);
                currentClone = clonename;
                stmt.setString(1, clonename);
                rs = DatabaseTransaction.executeQuery(stmt);
                List samples = new ArrayList();
                while (rs.next()) {
                    String type = rs.getString(1);
                    int position = rs.getInt(2);
                    String label = rs.getString(3);
                    String result = rs.getString(4);
                    int sampleid = rs.getInt(5);
                    int cloneid = rs.getInt(6);
                    int containerid = rs.getInt(7);
                    String x = rs.getString(8);
                    String y = rs.getString(9);
                    String containertype = rs.getString(10);
                    Sample s = new Sample(sampleid, type, null, cloneid, position, x, y, containerid, label);
                    s.setResult(result);
                    s.setContainerType(containertype);
                    clone.setCloneid(cloneid);
                    s.setClone(clone);
                    samples.add(s);
                }
                clone.setSamples(samples);
                m.add(clone);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println("Error occured while query sample by clone " + currentClone);
                ex.printStackTrace();
            }
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }

        return m;
    }

    public List getMTAs(List cloneids, List collectionnames, User user) {
        String sql = "select distinct specialtreatment from clone where cloneid in (select cloneid from clonecollection where name=?)";
        String sql2 = "select distinct specialtreatment from clone where cloneid=?";
        String sql3 = "select mtaid from mtarule where institutionname=? and mtacode=?";
        String sql4 = "select name, textfile, description, filename, filepath from MTA where id=?";

        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List mtalist = new ArrayList();
        try {
            Set mtas = new TreeSet();
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            for (int i = 0; i < collectionnames.size(); i++) {
                String cname = (String) collectionnames.get(i);
                stmt.setString(1, cname);
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    String s = rs.getString(1);
                    if (s == null || s.trim().length() == 0) {
                        s = MTA.MTA_NONE;
                    }
                    mtas.add(s);
                }
                DatabaseTransaction.closeResultSet(rs);
            }
            DatabaseTransaction.closeStatement(stmt);

            stmt = c.prepareStatement(sql2);
            for (int i = 0; i < cloneids.size(); i++) {
                String id = (String) cloneids.get(i);
                stmt.setInt(1, Integer.parseInt(id));
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    String s = rs.getString(1);
                    if (s == null || s.trim().length() == 0) {
                        s = MTA.MTA_NONE;
                    }
                    mtas.add(s);
                }
                DatabaseTransaction.closeResultSet(rs);
            }
            DatabaseTransaction.closeStatement(stmt);

            Set mtaids = new TreeSet();
            stmt = c.prepareStatement(sql3);
            Iterator iter = mtas.iterator();
            while (iter.hasNext()) {
                String mtacode = (String) iter.next();
                stmt.setString(1, user.getInstitution());
                stmt.setString(2, mtacode);
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    int id = rs.getInt(1);
                    mtaids.add(new Integer(id));
                }
                DatabaseTransaction.closeResultSet(rs);
            }
            DatabaseTransaction.closeStatement(stmt);

            stmt = c.prepareStatement(sql4);
            iter = mtaids.iterator();
            while (iter.hasNext()) {
                int id = ((Integer) iter.next()).intValue();
                stmt.setInt(1, id);
                rs = DatabaseTransaction.executeQuery(stmt);
                if (rs.next()) {
                    String mtaname = rs.getString(1);
                    String text = rs.getString(2);
                    String desc = rs.getString(3);
                    String filename = rs.getString(4);
                    String filepath = rs.getString(5);
                    MTA mta = new MTA(id, mtaname, text, desc, filename, filepath);
                    mta.readTextFromFile();
                    mtalist.add(mta);
                }
                DatabaseTransaction.closeResultSet(rs);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while querying MTA from database.");
            return null;
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
        return mtalist;
    }

    public List getMTAs(List cloneids, List collectionnames) {
        String sql = "select distinct specialtreatment from clone where cloneid in (select cloneid from clonecollection where name=?)";
        String sql2 = "select distinct specialtreatment from clone where cloneid=?";

        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List mtalist = new ArrayList();
        Set mtas = new TreeSet();
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            stmt = c.prepareStatement(sql);
            for (int i = 0; i < collectionnames.size(); i++) {
                String cname = (String) collectionnames.get(i);
                stmt.setString(1, cname);
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    String s = rs.getString(1);
                    if (s != null && s.trim().length() > 0) {
                        mtas.add(s);
                    }
                }
                DatabaseTransaction.closeResultSet(rs);
            }
            DatabaseTransaction.closeStatement(stmt);

            stmt = c.prepareStatement(sql2);
            for (int i = 0; i < cloneids.size(); i++) {
                String id = (String) cloneids.get(i);
                stmt.setInt(1, Integer.parseInt(id));
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    String s = rs.getString(1);
                    if (s != null && s.trim().length() > 0) {
                        mtas.add(s);
                    }
                }
                DatabaseTransaction.closeResultSet(rs);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while querying MTA from database.");
            return null;
        } finally {
            DatabaseTransaction.closeConnection(c);
        }
        mtalist.addAll(mtas);
        return mtalist;
    }

    public Clone getCloneInfoByCloneid(int cloneid) {
        if (cloneid < 1) {
            return null;
        }

        Clone c = null;
        String sql = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname,"
                + " clonemapfilename,status,specialtreatment,source,description,cloneid"
                + " from clone where cloneid =?";

        String sql2 = "select h.hoststrain, h.isinuse, h.description"
                + " from host h where h.cloneid=?";

        ResultSet rs = null, rs2 = null;
        PreparedStatement stmt = null, stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);

            stmt.setInt(1, cloneid);
            stmt2.setInt(1, cloneid);
            rs = DatabaseTransaction.executeQuery(stmt);
            rs2 = DatabaseTransaction.executeQuery(stmt2);
            if (rs.next()) {
                String clonename = rs.getString(1);
                String clonetype = rs.getString(2);
                String verified = rs.getString(3);
                String vermethod = rs.getString(4);
                String domain = rs.getString(5);
                String subdomain = rs.getString(6);
                String restriction = rs.getString(7);
                String comments = rs.getString(8);
                int vectorid = rs.getInt(9);
                String vectorname = rs.getString(10);
                String clonemap = rs.getString(11);
                String status = rs.getString(12);
                String specialtreatment = rs.getString(13);
                String src = rs.getString(14);
                String des = rs.getString(15);
                c = new Clone(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemap, status, specialtreatment, src, des);
                List hs = new ArrayList();
                while (rs2.next()) {
                    CloneHost h = new CloneHost(cloneid, rs2.getString(1), rs2.getString(2), rs2.getString(3));
                    hs.add(h);
                }
                if (hs.size() > 0) {
                    c.setHosts(hs);
                }
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        return c;
    }

    public List getCloneInfoByCloneids(List cloneids) {
        if ((cloneids == null) || (cloneids.size() < 1)) {
            return null;
        }
        List cs = new ArrayList();
        String sql = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname,"
                + " clonemapfilename,status,specialtreatment,source,description,cloneid"
                + " from clone where cloneid =?";

        String sql2 = "select h.hoststrain, h.isinuse, h.description"
                + " from host h where h.cloneid=?";

        ResultSet rs = null, rs2 = null;
        PreparedStatement stmt = null, stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < cloneids.size(); i++) {
                int cid = Integer.parseInt((String) cloneids.get(i));
                stmt.setInt(1, cid);
                stmt2.setInt(1, cid);
                rs = DatabaseTransaction.executeQuery(stmt);
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                if (rs.next()) {
                    String clonename = rs.getString(1);
                    String clonetype = rs.getString(2);
                    String verified = rs.getString(3);
                    String vermethod = rs.getString(4);
                    String domain = rs.getString(5);
                    String subdomain = rs.getString(6);
                    String restriction = rs.getString(7);
                    String comments = rs.getString(8);
                    int vectorid = rs.getInt(9);
                    String vectorname = rs.getString(10);
                    String clonemap = rs.getString(11);
                    String status = rs.getString(12);
                    String specialtreatment = rs.getString(13);
                    String src = rs.getString(14);
                    String des = rs.getString(15);
                    int cloneid = rs.getInt(16);
                    Clone c = new Clone(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemap, status, specialtreatment, src, des);
                    List hs = new ArrayList();
                    while (rs2.next()) {
                        if (rs2.getString(2).equals("Y")) {
                            c.setHs(rs2.getString(1));
                        }
                        CloneHost h = new CloneHost(cid, rs2.getString(1), rs2.getString(2), rs2.getString(3));
                        hs.add(h);
                    }
                    if (hs.size() > 0) {
                        c.setHosts(hs);
                    }
                    cs.add(c);
                }
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        if (cs.size() < 1) {
            cs = null;
        }
        return cs;
    }

    public List getCloneInfoByClonenames(List clonenames) {
        if ((clonenames == null) || (clonenames.size() < 1)) {
            return null;
        }
        List cs = new ArrayList();
        String sql = "select clonename, clonetype, verified, vermethod,"
                + " domain, subdomain, restriction, comments, vectorid, vectorname,"
                + " clonemapfilename,status,specialtreatment,source,description,cloneid"
                + " from clone where clonename=?";

        String sql2 = "select h.hoststrain, h.isinuse, h.description"
                + " from host h, clone c where h.cloneid=c.cloneid and c.clonename=?";

        ResultSet rs = null, rs2 = null;
        PreparedStatement stmt = null, stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < clonenames.size(); i++) {
                String cname = (String) clonenames.get(i);
                stmt.setString(1, cname);
                stmt2.setString(1, cname);
                rs = DatabaseTransaction.executeQuery(stmt);
                rs2 = DatabaseTransaction.executeQuery(stmt2);
                if (rs.next()) {
                    String clonename = rs.getString(1);
                    String clonetype = rs.getString(2);
                    String verified = rs.getString(3);
                    String vermethod = rs.getString(4);
                    String domain = rs.getString(5);
                    String subdomain = rs.getString(6);
                    String restriction = rs.getString(7);
                    String comments = rs.getString(8);
                    int vectorid = rs.getInt(9);
                    String vectorname = rs.getString(10);
                    String clonemap = rs.getString(11);
                    String status = rs.getString(12);
                    String specialtreatment = rs.getString(13);
                    String src = rs.getString(14);
                    String des = rs.getString(15);
                    int cloneid = rs.getInt(16);
                    Clone c = new Clone(cloneid, clonename, clonetype, verified, vermethod, domain, subdomain, restriction, comments, vectorid, vectorname, clonemap, status, specialtreatment, src, des);
                    List hs = new ArrayList();
                    while (rs2.next()) {
                        if (rs2.getString(2).equals("Y")) {
                            c.setHs(rs2.getString(1));
                        }
                        CloneHost h = new CloneHost(cloneid, rs2.getString(1), rs2.getString(2), rs2.getString(3));
                        hs.add(h);
                    }
                    if (hs.size() > 0) {
                        c.setHosts(hs);
                    }
                    cs.add(c);
                }
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query clones");
            return null;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }

        if (cs.size() < 1) {
            cs = null;
        }
        return cs;
    }

    public boolean updateCloneSFD(int cloneid, String verified, String vermethod, String restriction, String comments, String source) {
        if ((cloneid < 1) || (verified == null) || (verified.length() < 1) || (source == null) || (source.length() < 1)) {
            return true;
        }

        String sql = "update clone set verified=?, vermethod=?, restriction=?, comments=?, source=? where cloneid=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, verified);
            stmt.setString(2, vermethod);
            stmt.setString(3, restriction);
            stmt.setString(4, comments);
            stmt.setString(5, source);
            stmt.setInt(6, cloneid);
            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating CLONE table");
            return false;
        }

        return true;
    }

    public boolean updateCloneSubmission(int csid, int cloneid, int userid,
            String st, String hs, String re, String mta,
            String s, String sd, String r, String rd) {
        if ((csid < 1) || (cloneid < 1) || (userid < 1) || (s == null) || (s.length() < 1) || (sd == null) || (sd.length() < 1) || (r == null) || (r.length() < 1) || (rd == null) || (rd.length() < 1) || (st == null) || (st.length() < 1) || (hs == null) || (hs.length() < 1)) {
            return false;
        }

        if ((re == null) || (re.length() < 1)) {
            re = "";
        }
        if ((mta == null) || (mta.length() < 1)) {
            mta = "";
        }
        String sql1 = "update clone set status=?, restriction=?, specialtreatment=? where cloneid=?";
        String sql2 = "update host set isinuse='Y' where cloneid=? and hoststrain=?";
        String sql3 = "insert into clonesubmission (cloneid, userid, submitter, submitdate, receiver, receivedate, submissionid, submissiondate, submissionstatus) values (?,?,?,?,?,?,?,SYSDATE,'RECEIVED')";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            PreparedStatement stmt3 = conn.prepareStatement(sql3);

            stmt1.setString(1, st);
            stmt1.setString(2, re);
            stmt1.setString(3, mta);
            stmt1.setInt(4, cloneid);

            stmt2.setInt(1, cloneid);
            stmt2.setString(2, hs);

            stmt3.setInt(1, cloneid);
            stmt3.setInt(2, userid);
            stmt3.setString(3, s);
            stmt3.setString(4, sd);
            stmt3.setString(5, r);
            stmt3.setString(6, rd);
            stmt3.setInt(7, csid);
            DatabaseTransaction.executeUpdate(stmt1);
            DatabaseTransaction.closeStatement(stmt1);
            DatabaseTransaction.executeUpdate(stmt2);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.executeUpdate(stmt3);
            DatabaseTransaction.closeStatement(stmt3);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating CLONE,HOST,CLONESUBMISSION table");
            return false;
        }

        return true;
    }

    public boolean updateCloneSubmission(int userid, List clones) {
        if ((clones == null) || (clones.size() < 1) || (userid < 1)) {
            return false;
        }
        String sql1 = "update clone set status=?, restriction=?, specialtreatment=? where cloneid=?";
        String sql2 = "update host set isinuse='Y' where cloneid=? and hoststrain=?";
        String sql3 = "insert into clonesubmission (userid, submitter, submitdate, receiver, receivedate, submissionid, submissiondate, submissionstatus) values (?,?,?,?,?,?,SYSDATE,'RECEIVED')";
        String sql4 = "insert into clonereceive (clonereceiveid, submissionid, cloneid) values (?,?,?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            PreparedStatement stmt3 = conn.prepareStatement(sql3);
            PreparedStatement stmt4 = conn.prepareStatement(sql4);

            DatabaseTransaction t = null;
            t = DatabaseTransaction.getInstance();
            DefTableManager dm = new DefTableManager();
            Clone c = (Clone) clones.get(0);

            int csid = dm.getNextid("submissionid", t);
            stmt3.setInt(1, userid);
            stmt3.setString(2, c.getSender());
            stmt3.setString(3, c.getSdate());
            stmt3.setString(4, c.getReceiver());
            stmt3.setString(5, c.getRdate());
            stmt3.setInt(6, csid);
            DatabaseTransaction.executeUpdate(stmt3);

            for (int i = 0; i < clones.size(); i++) {
                c = (Clone) clones.get(i);
                int crid = dm.getNextid("clonereceiveid", t);
                int cloneid = c.getCloneid();
                stmt1.setString(1, c.getStatus());
                stmt1.setString(2, c.getRestriction());
                stmt1.setString(3, c.getSpecialtreatment());
                stmt1.setInt(4, cloneid);
                DatabaseTransaction.executeUpdate(stmt1);

                stmt2.setInt(1, cloneid);
                stmt2.setString(2, c.getHs());
                DatabaseTransaction.executeUpdate(stmt2);

                stmt4.setInt(1, crid);
                stmt4.setInt(2, csid);
                stmt4.setInt(3, cloneid);
                DatabaseTransaction.executeUpdate(stmt4);
            }

            DatabaseTransaction.closeStatement(stmt1);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating CLONE,HOST,CLONESUBMISSION table");
            return false;
        }

        return true;
    }

    public void updateCloneStatus(List clonenames, String status, String comments) throws Exception {
        String sql = "update clone set status='" + status + "', comments=comments||? where clonename=?";
        PreparedStatement stmt = null;
        String errorClone = "";
        try {
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < clonenames.size(); i++) {
                String clonename = (String) clonenames.get(i);
                errorClone = clonename;
                if (comments == null || comments.trim().length() == 0) {
                    stmt.setString(1, "");
                } else {
                    stmt.setString(1, " " + comments);
                }
                stmt.setString(2, clonename);
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error occured while updating clone status for: " + errorClone);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public boolean checkNoInsertCloneExistByVectorid(int vectorid) {
        if (vectorid < 1) {
            return false;
        }
        boolean bResult = false;

        String sql = "select * from clone where vectorid=? and clonetype='" + Clone.NOINSERT + "'";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vectorid);
            ResultSet rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                bResult = true;
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while query CLONE table");
            return false;
        }

        return bResult;
    }

    public void removeShoppingCartItem(List clones) throws Exception {
        String sql = "delete from shoppingcartitem where userid=? and cloneid=?";
        String sql2 = "delete from shoppingcartitem where userid=? and collectionname=?";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            for (int i = 0; i < clones.size(); i++) {
                ShoppingCartItem s = (ShoppingCartItem) clones.get(i);
                if (s.getType().equals(ShoppingCartItem.CLONE)) {
                    stmt.setInt(1, s.getUserid());
                    stmt.setString(2, s.getItemid());
                    DatabaseTransaction.executeUpdate(stmt);
                } else {
                    stmt2.setInt(1, s.getUserid());
                    stmt2.setString(2, s.getItemid());
                    DatabaseTransaction.executeUpdate(stmt2);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Error occured while updating shopping cart.");
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
    }

    public List<CloneInfo> queryVectorsByProperty(String property, String status, List restrictions, boolean isFeature, boolean isProperty) {
        String sql = "select cloneid,clonename,restriction,vectorid,vectorname,specialtreatment,description"
                + " from clone where clonetype='No insert' and vectorid in"
                + " (select vectorid from vectorproperty where lower(propertytype)=?)";

        if (property == null) {
            sql = "select cloneid,clonename,restriction,vectorid,vectorname,specialtreatment,description"
                    + " from clone where clonetype='No insert'";
        }

        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql + " and restriction in (" + s + ")";
        }

        if (status != null) {
            sql += " and status='" + status + "'";
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CloneInfo> clones = new ArrayList<CloneInfo>();
        try {
            stmt = conn.prepareStatement(sql);
            if (property != null) {
                stmt.setString(1, property.toLowerCase());
            }
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                int cloneid = rs.getInt(1);
                String clonename = rs.getString(2);
                String restriction = rs.getString(3);
                int vectorid = rs.getInt(4);
                String vectorname = rs.getString(5);
                String specialtreatment = rs.getString(6);
                String description = rs.getString(7);
                CloneInfo clone = new CloneInfo(cloneid, clonename, null, null, null, null, null, restriction, null, vectorid, vectorname, null, status, specialtreatment, null, description);
                CloneVector vector = new CloneVector();
                if (isFeature) {
                    List<VectorFeature> features = getVectorFeatures(vectorid);
                    if (features == null) {
                        throw new Exception("Cannot get vector feature for vector " + vectorname);
                    } else {
                        vector.setVectorfeatures(features);
                    }
                }
                if (isProperty) {
                    List<VectorProperty> properties = getVectorProperties(vectorid);
                    if (properties == null) {
                        throw new Exception("Cannot get vector property for vector " + vectorname);
                    } else {
                        vector.setProperty(properties);
                    }
                }
                clone.setVector(vector);
                clones.add(clone);
            }
            return clones;
        } catch (Exception ex) {
            ex.printStackTrace();
            handleError(ex, "Error occured while query clones");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public List<VectorFeature> getVectorFeatures(int vectorid) {
        String sql = "select featureid,maptype,name,description,startpos,endpos"
                + " from vectorfeature where vectorid=" + vectorid + " order by startpos";
        DatabaseTransaction t = null;
        ResultSet rs2 = null;
        try {
            List<VectorFeature> features = new ArrayList();
            t = DatabaseTransaction.getInstance();
            rs2 = t.executeQuery(sql);
            while (rs2.next()) {
                int featureid = rs2.getInt(1);
                String maptype = rs2.getString(2);
                String n = rs2.getString(3);
                String d = rs2.getString(4);
                int start = rs2.getInt(5);
                int end = rs2.getInt(6);
                VectorFeature feature = new VectorFeature(featureid, n, d, start, end, vectorid, maptype);
                features.add(feature);
            }
            return features;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs2);
        }
    }

    public List<VectorProperty> getVectorProperties(int vectorid) {
        String sql = "select propertytype, category, displayvalue from vectorpropertytype"
                + " where propertytype in (select propertytype from vectorproperty"
                + " where vectorid=" + vectorid + ")";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            List<VectorProperty> properties = new ArrayList();
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String propertytype = rs.getString(1);
                String category = rs.getString(2);
                String propertyvalue = rs.getString(3);
                VectorProperty property = new VectorProperty(propertytype, propertyvalue);
                property.setCategory(category);
                properties.add(property);
            }
            return properties;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    public static void main(String args[]) {
        String file = "C:\\dev\\plasmid_support\\PlasmidAnalysis201212\\Core\\cloneid_missing_seq_2.txt";
        String output = "C:\\dev\\plasmid_support\\PlasmidAnalysis201212\\Core\\cloneseq_5.txt";
        DatabaseTransaction dt = null;
        Connection conn = null;

        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            if (conn == null) {
                throw new DatabaseException("Cannot get database connection.");
            }
            CloneManager manager = new CloneManager(conn);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line = null;
            List clones = new ArrayList();
            int count = 0;
            while ((line = in.readLine()) != null) {
                int cloneid = Integer.parseInt(line.trim());
                System.out.println("clone:" + cloneid);
                //String seq = manager.queryReferenceSequenceByCloneid(cloneid);
                String seq = manager.queryCloneSequenceByCloneid(dt, cloneid);
                Dnasequence s = new Dnasequence();
                s.setInsertid(cloneid);
                s.setSequence(seq);
                clones.add(s);
                count++;
                if (count == 100) {
                    writeOutput(output, clones);
                    clones = new ArrayList();
                    count = 0;
                }
            }
            in.close();

            if (clones.size() > 0) {
                writeOutput(output, clones);
            }

            /**
            Clone c = manager.queryCloneByCloneid(5096);
            List names = c.getNames();
            List authors = c.getAuthors();
            GrowthCondition growths = c.getRecommendedGrowthCondition();
            System.out.println(names);
            System.out.println(authors);
            System.out.println(growths);
             */
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }

    public static void writeOutput(String output, List clones) throws Exception {
        OutputStreamWriter f = new FileWriter(output, true);
        for (int i = 0; i < clones.size(); i++) {
            Dnasequence s = (Dnasequence) clones.get(i);
            f.write(s.getInsertid() + "\t" + s.getSequence() + "\n");
        }
        f.close();
    }
}
