/*
 * VectorManager.java
 *
 * Created on April 1, 2005, 4:02 PM
 */
package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.util.StringConvertor;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class VectorManager extends TableManager {

    /** Creates a new instance of VectorManager */
    public VectorManager(Connection conn) {
        super(conn);
    }

    public boolean insertVector(CloneVector vector) {
        String sql = "insert into vector" +
                " (vectorid, name, description, form, type, sizeinbp," +
                " mapfilename, sequencefilename, comments)" +
                " values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vector.getVectorid());
            stmt.setString(2, vector.getName());
            stmt.setString(3, vector.getDescription());
            stmt.setString(4, vector.getForm());
            stmt.setString(5, vector.getType());
            stmt.setInt(6, vector.getSize());
            stmt.setString(7, vector.getMapfilename());
            stmt.setString(8, vector.getSeqfilename());
            stmt.setString(9, vector.getFullComments());

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTOR table");
            return false;
        }
        return true;
    }

    public boolean insertVectors(List vectors) {
        if (vectors == null) {
            return true;
        }

        String sql = "insert into vector" +
                " (vectorid, name, description, form, type, sizeinbp," +
                " mapfilename, sequencefilename, comments)" +
                " values(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < vectors.size(); i++) {
                CloneVector vector = (CloneVector) vectors.get(i);
                stmt.setInt(1, vector.getVectorid());
                stmt.setString(2, vector.getName());
                stmt.setString(3, vector.getDescription());
                stmt.setString(4, vector.getForm());
                stmt.setString(5, vector.getType());
                stmt.setInt(6, vector.getSize());
                stmt.setString(7, vector.getMapfilename());
                stmt.setString(8, vector.getSeqfilename());
                stmt.setString(9, vector.getFullComments());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTOR table");
            return false;
        }
        return true;
    }

    public boolean updateVector(CloneVector vector, boolean noDesp) {
        if (noDesp) {
            String sql = new String("update vector set name=?, form=?, type=?, sizeinbp=?, comments=?");
            if (vector.getMapfilename().length() > 0) {
                sql = sql + ", mapfilename=?";
            }
            if (vector.getSeqfilename().length() > 0) {
                sql = sql + ", sequencefilename=?";
            }
            sql = sql + " where vectorid=?";
            try {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, vector.getName());
                stmt.setString(2, vector.getForm());
                stmt.setString(3, vector.getType());
                stmt.setInt(4, vector.getSize());
                stmt.setString(5, vector.getFullComments());
                int i = 6;
                if (vector.getMapfilename().length() > 0) {
                    stmt.setString(i++, vector.getMapfilename());
                }
                if (vector.getSeqfilename().length() > 0) {
                    stmt.setString(i++, vector.getSeqfilename());
                }
                stmt.setInt(i, vector.getVectorid());

                DatabaseTransaction.executeUpdate(stmt);
                DatabaseTransaction.closeStatement(stmt);
            } catch (Exception ex) {
                handleError(ex, "Error occured while updating VECTOR table");
                return false;
            }
        } else {
            return updateVector(vector);
        }
        return true;
    }

    public boolean updateVector(CloneVector vector) {
        String sql = new String("update vector set name=?, description=?, form=?, type=?, sizeinbp=?, comments=?");
        if (vector.getMapfilename().length() > 0) {
            sql = sql + ", mapfilename=?";
        }
        if (vector.getSeqfilename().length() > 0) {
            sql = sql + ", sequencefilename=?";
        }
        sql = sql + " where vectorid=?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, vector.getName());
            stmt.setString(2, vector.getDescription());
            stmt.setString(3, vector.getForm());
            stmt.setString(4, vector.getType());
            stmt.setInt(5, vector.getSize());
            stmt.setString(6, vector.getFullComments());
            int i = 7;
            if (vector.getMapfilename().length() > 0) {
                stmt.setString(i++, vector.getMapfilename());
            }
            if (vector.getSeqfilename().length() > 0) {
                stmt.setString(i++, vector.getSeqfilename());
            }
            stmt.setInt(i, vector.getVectorid());

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating VECTOR table");
            return false;
        }
        return true;
    }

    public boolean updateVSubmission(int vid, int uid) {
        return (removeVSubmission(vid, uid) && insertVSubmission(vid, uid));
    }

    public boolean removeVSubmission(int vid, int uid) {
        if (vid < 1) {
            return true;
        }
        if (uid < 1) {
            return true;
        }
        String sql = new String("delete from vectorsubmission where userid=? and vectorid=?");
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            stmt.setInt(2, vid);

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while deleteing into VECTORSUBMISSION table");
            return false;
        }
        return true;
    }

    public boolean insertVSubmission(int vid, int uid) {
        if ((vid < 1) || (uid < 1)) {
            return true;
        }
        String sql = new String("insert into vectorsubmission (userid, vectorid, submitdate, status) values (?, ?, SYSDATE, 'PENDING')");
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, uid);
            stmt.setInt(2, vid);

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORSUBMISSION table");
            return false;
        }
        return true;
    }

    public boolean updatetVSubmissionStatus(int vid, String s) {
        if ((vid < 1) || (s == null) || (s.length() < 1)) {
            return true;
        }

        String sql = new String("update vectorsubmission set status = ? where vectorid = ?");
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vid);
            stmt.setString(2, s);

            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating VECTORSUBMISSION table");
            return false;
        }
        return true;
    }

    public boolean updateVHS(int vid, List VHS) {
        if ((vid < 1) || (VHS.size() < 1)) {
            return true;
        }

        String sql1 = new String("delete from vectorhost where vectorid=?");
        String sql2 = new String("insert into vectorhost (vectorid, vectorname, hoststrain, isinuse, description) values (?,?,?,?,?)");

        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt1.setInt(1, vid);
            DatabaseTransaction.executeUpdate(stmt1);
            DatabaseTransaction.closeStatement(stmt1);
            for (int i = 0; i < VHS.size(); i++) {
                VectorHostStrain vhs = (VectorHostStrain) VHS.get(i);
                stmt2.setInt(1, vid);
                stmt2.setString(2, vhs.getVectorame());
                stmt2.setString(3, vhs.getHoststrain());
                stmt2.setString(4, vhs.getIsinuse());
                stmt2.setString(5, vhs.getDescription());
                DatabaseTransaction.executeUpdate(stmt2);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating VECTORHOST table");
            return false;
        }
        return true;
    }

    public boolean updateVGC(int vid, List VGC) {
        if ((vid < 1) || (VGC.size() < 1)) {
            return true;
        }

        String sql1 = new String("delete from vectorgrowth where vectorid=?");
        String sql2 = new String("insert into vectorgrowth (vectorid, growthid, vectorname, growthname, isrecommended) values (?,?,?,?,?)");

        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt1.setInt(1, vid);
            DatabaseTransaction.executeUpdate(stmt1);
            DatabaseTransaction.closeStatement(stmt1);
            for (int i = 0; i < VGC.size(); i++) {
                VectorGrowthCondition vgc = (VectorGrowthCondition) VGC.get(i);
                stmt2.setInt(1, vid);
                stmt2.setInt(2, vgc.getGrowthid());
                stmt2.setString(3, vgc.getVectorame());
                stmt2.setString(4, vgc.getGrowthname());
                stmt2.setString(5, vgc.getIsrecommended());
                DatabaseTransaction.executeUpdate(stmt2);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating VECTORGROWTH table");
            return false;
        }
        return true;
    }

    public boolean updateVSM(int vid, List VSM) {
        if ((vid < 1) || (VSM.size() < 1)) {
            return true;
        }

        String sql1 = new String("delete from vectorsel where vectorid=?");
        String sql2 = new String("insert into vectorsel (vectorid, vectorname, hosttype, marker) values (?,?,?,?)");

        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt1.setInt(1, vid);
            DatabaseTransaction.executeUpdate(stmt1);
            DatabaseTransaction.closeStatement(stmt1);
            for (int i = 0; i < VSM.size(); i++) {
                VectorSelectMarker vsm = (VectorSelectMarker) VSM.get(i);
                stmt2.setInt(1, vid);
                stmt2.setString(2, vsm.getVectorname());
                stmt2.setString(3, vsm.getHosttype());
                stmt2.setString(4, vsm.getMarker());
                DatabaseTransaction.executeUpdate(stmt2);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating VECTORSEL table");
            return false;
        }

        return true;
    }

    public boolean updateFeatures(int vid, List features) {
        if ((vid < 1) || (features.size() < 1)) {
            return true;
        }

        String sql1 = new String("delete from vectorfeature where vectorid=?");
        String sql2 = new String("insert into vectorfeature (vectorid, featureid, maptype, name, description, startpos, stoppos) values (?,?,?,?,?,?)");
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt1.setInt(1, vid);
            DatabaseTransaction.executeUpdate(stmt1);
            DatabaseTransaction.closeStatement(stmt1);
            for (int i = 0; i < features.size(); i++) {
                VectorFeature vf = (VectorFeature) features.get(i);
                stmt2.setInt(1, vid);
                stmt2.setInt(2, vf.getFeatureid());
                stmt2.setString(3, vf.getMaptype());
                stmt2.setString(4, vf.getName());
                stmt2.setString(5, vf.getDescription());
                stmt2.setInt(6, vf.getStart());
                stmt2.setInt(7, vf.getStop());
                DatabaseTransaction.executeUpdate(stmt2);
            }
            DatabaseTransaction.closeStatement(stmt2);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating VECTORFEATURE table");
            return false;
        }

        return true;
    }

    public boolean updateSynonyms(int vid, String synonyms) {
        if (vid < 1) {
            return true;
        }

        StringConvertor sc = new StringConvertor();
        List syns = sc.convertFromStringToCapList(synonyms, ",");

        return (updateSynonyms(vid, syns));
    }

    public boolean updateSynonyms(int vid, List synonyms) {
        if (vid < 1) {
            return true;
        }

        return (removeSynonyms(vid) && insertSynonyms(vid, synonyms));
    }

    public boolean removeSynonyms(int vid) {
        if (vid < 1) {
            return true;
        }

        String sql = new String("delete from vectorsynonym where vectorid=?");
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vid);
            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORSYNONYM table");
            return false;
        }
        return true;
    }

    public boolean insertSynonyms(int vid, List synonyms) {
        if (vid < 1) {
            return true;
        }
        if (synonyms == null) {
            return true;
        }

        String sql = "insert into vectorsynonym" +
                " (vectorid, vsynonym)" +
                " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < synonyms.size(); i++) {
                String s = (String) synonyms.get(i);
                stmt.setInt(1, vid);
                stmt.setString(2, s);

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORSYNONYM table");
            return false;
        }
        return true;
    }

    public boolean insertSynonyms(int vid, String synonyms) {
        if (vid < 1) {
            return true;
        }
        if ((synonyms == null) || (synonyms.length() < 1)) {
            return true;
        }

        StringConvertor sc = new StringConvertor();
        List syns = sc.convertFromStringToCapList(synonyms, ",");

        return insertSynonyms(vid, syns);
    }

    public boolean insertSynonyms(List synonyms) {
        if (synonyms == null) {
            return true;
        }

        String sql = "insert into vectorsynonym" +
                " (vectorid, vsynonym)" +
                " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < synonyms.size(); i++) {
                VectorSynonym vs = (VectorSynonym) synonyms.get(i);
                stmt.setInt(1, vs.getVectorid());
                stmt.setString(2, vs.getSynonym());

                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORSYNONYM table");
            return false;
        }
        return true;
    }

    public boolean insertProperty(int vid, String vp) {
        if ((vid < 1) || (vp == null)) {
            return true;
        }

        String sql = "insert into vectorproperty" +
                " (vectorid, propertytype)" +
                " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vid);
            stmt.setString(2, vp);
            DatabaseTransaction.executeUpdate(stmt);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPROPERTY table");
            return false;
        }
        return true;
    }

    public boolean insertProperties(List properties) {
        if (properties == null) {
            return true;
        }

        String sql = "insert into vectorproperty" +
                " (vectorid, propertytype)" +
                " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < properties.size(); i++) {
                VectorProperty vp = (VectorProperty) properties.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setString(2, vp.getPropertyType());
                //System.out.println(vp.getPropertyType());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPROPERTY table");
            return false;
        }
        return true;
    }

    public boolean insertVectorFeatures(List features) {
        if (features == null) {
            return true;
        }

        String sql2 = "insert into vectorfeature" +
                " (name, description, startpos, endpos, vectorid, maptype, featureid)" +
                " values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < features.size(); i++) {
                VectorFeature v = (VectorFeature) features.get(i);
                //System.out.println(v.getName()+","+v.getMaptype());
                stmt2.setString(1, v.getName());
                stmt2.setString(2, v.getDescription());
                stmt2.setInt(3, v.getStart());
                stmt2.setInt(4, v.getStop());
                stmt2.setInt(5, v.getVectorid());
                stmt2.setString(6, v.getMaptype());
                stmt2.setInt(7, v.getFeatureid());
                //System.out.println(v.getMaptype());
                DatabaseTransaction.executeUpdate(stmt2);
            }
            DatabaseTransaction.closeStatement(stmt2);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORFEATURE table");
            return false;
        }
        return true;
    }

    public boolean insertVFTs(List VFTs) {
        if (VFTs == null) {
            return true;
        }
        ResultSet rs = null;
        String sql1 = "select * from featuremaptype where upper(maptype)=?";
        String sql2 = "insert into featuremaptype (maptype) values (?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql1);
            for (int i = 0; i < VFTs.size(); i++) {
                String v = (String) VFTs.get(i);
                stmt.setString(1, v.toUpperCase());
                rs = stmt.executeQuery();
                if ((rs != null) && rs.next()) {
                    VFTs.set(i, "");
                }
            }
            stmt.close();
            stmt = conn.prepareStatement(sql2);
            for (int i = 0; i < VFTs.size(); i++) {
                String v = (String) VFTs.get(i);
                if (v.length() > 0) {
                    stmt.setString(1, v);
                    stmt.executeUpdate();
                }
            }
            stmt.close();

            if (false) {
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                PreparedStatement stmt2 = conn.prepareStatement(sql2);

                for (int i = 0; i < VFTs.size(); i++) {
                    String v = (String) VFTs.get(i);
                    stmt1.setString(1, v.toUpperCase());
                    rs = stmt1.executeQuery();
                    if ((rs != null) && rs.next()) {
                        continue;
                    }

                    stmt2.setString(1, v);
                    stmt2.executeUpdate();
                }
                stmt1.close();
                stmt2.close();
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into FEATUREMAPTYPE table");
            return false;
        }
        return true;
    }

    public boolean insertVFNs(List VFNs) {
        if (VFNs == null) {
            return true;
        }
        ResultSet rs = null;
        String sql1 = "select * from featurename where upper(name)=?";
        String sql2 = "insert into featurename" +
                " (name)" +
                " values(?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < VFNs.size(); i++) {
                String v = (String) VFNs.get(i);
                stmt1.setString(1, v.toUpperCase());
                rs = stmt1.executeQuery();
                if ((rs != null) && rs.next()) {
                    continue;
                }

                stmt2.setString(1, v);
                stmt2.executeUpdate();
            }
            stmt1.close();
            stmt2.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into FEATURENAME table");
            return false;
        }
        return true;
    }

    public boolean insertHSs(List HSs) {
        if (HSs == null) {
            return true;
        }
        ResultSet rs = null;
        String sql1 = "select * from hoststrain where upper(hoststrain)=?";
        String sql2 = "insert into hoststrain" +
                " (hoststrain)" +
                " values(?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < HSs.size(); i++) {
                String v = (String) HSs.get(i);
                stmt1.setString(1, v.toUpperCase());
                rs = stmt1.executeQuery();
                if ((rs != null) && rs.next()) {
                    continue;
                }

                stmt2.setString(1, v);
                stmt2.executeUpdate();
            }
            stmt1.close();
            stmt2.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into HOSTSTRAIN table");
            return false;
        }
        return true;
    }

    public boolean insertHTs(List HTs) {
        if (HTs == null) {
            return true;
        }

        ResultSet rs = null;
        String sql1 = "select * from hosttype where upper(type)=?";
        String sql2 = "insert into hosttype" +
                " (type)" +
                " values(?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < HTs.size(); i++) {
                String v = (String) HTs.get(i);
                stmt1.setString(1, v.toUpperCase());
                rs = stmt1.executeQuery();
                if ((rs != null) && rs.next()) {
                    continue;
                }

                stmt2.setString(1, v);
                stmt2.executeUpdate();
                ;
            }
            stmt1.close();
            stmt2.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into HOSTTYPE table");
            return false;
        }
        return true;
    }

    public List getSMs(String name) {
        String sql = null;
        if ((name == null) || (name.length() < 1)) {
            sql = "select marker from marker";
        } else {
            sql = "select marker from marker where marker like '" + name + "%'";
        }
        List SMs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while (rs.next()) {
                SMs.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return SMs;
    }

    public List getGCs(String name) {
        String sql = null;
        if ((name == null) || (name.length() < 1)) {
            sql = "select growthid, name, hosttype, antibioticselection, growthcondition, comments from growthcondition";
        } else {
            sql = "select growthid, name, hosttype, antibioticselection, growthcondition, comments from growthcondition where growthcondition like '" + name + "%'";
        }
        List GCs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while (rs.next()) {
                GrowthCondition gc = new GrowthCondition(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6));
                GCs.add(gc);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return GCs;
    }

    public List getGCNs(String name) {
        String sql = null;
        if ((name == null) || (name.length() < 1)) {
            sql = "select name from growthcondition";
        } else {
            sql = "select name from growthcondition where growthcondition like '" + name + "%'";
        }
        List GCs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while (rs.next()) {
                GCs.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return GCs;
    }

    public List getHSs(String name) {
        String sql = null;
        if ((name == null) || (name.length() < 1)) {
            sql = "select hoststrain from hoststrain";
        } else {
            sql = "select hoststrain from hoststrain where hoststrain like '" + name + "%'";
        }
        List HSs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while (rs.next()) {
                HSs.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return HSs;
    }

    public List getHTs(String name) {
        String sql = null;
        if ((name == null) || (name.length() < 1)) {
            sql = "select type from hosttype";
        } else {
            sql = "select type from hosttype where type like '" + name + "%'";
        }
        List HTs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            rs = stmt.executeQuery();
            while (rs.next()) {
                HTs.add(rs.getString(1));
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return HTs;
    }

    public List getVSMs(int vid) {
        if (vid < 1) {
            return null;
        }
        String sql = "select hosttype, marker from vectorsel where vectorid=?";
        List VSMs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            stmt.setInt(1, vid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                VectorSelectMarker v = new VectorSelectMarker(vid, rs.getString(1), rs.getString(2));
                VSMs.add(v);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return VSMs;
    }

    public List getVGCs(int vid) {
        if (vid < 1) {
            return null;
        }
        String sql = "select growthid, growthname, isrecommended from vectorgrowth where vectorid=?";
        List VGCs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            stmt.setInt(1, vid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                VectorGrowthCondition vgc = new VectorGrowthCondition(vid, rs.getInt(1), rs.getString(2), rs.getString(3));
                VGCs.add(vgc);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return VGCs;
    }

    public List getVHSs(int vid) {
        if (vid < 1) {
            return null;
        }
        String sql = "select hoststrain, isinuse, description from vectorhost where vectorid=?";
        List VHSs = new ArrayList();

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            stmt.setInt(1, vid);
            rs = stmt.executeQuery();
            while (rs.next()) {
                VectorHostStrain vhs = new VectorHostStrain(vid, rs.getString(1), rs.getString(2), rs.getString(3));
                VHSs.add(vhs);
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return VHSs;
    }

    public boolean insertVPTs(String CAT, List VPTs) {
        if (VPTs == null) {
            return false;
        }
        if (CAT == null) {
            return false;
        }
        ResultSet rs = null;
        String sql1 = "select * from vectorpropertytype where upper(category)='" + CAT.toUpperCase() + "' and upper(propertytype)=?";
        String sql2 = "insert into vectorpropertytype" +
                " (propertytype, category)" +
                " values(?, ?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < VPTs.size(); i++) {
                String v = (String) VPTs.get(i);
                stmt1.setString(1, v.toUpperCase());
                rs = stmt1.executeQuery();
                if ((rs != null) && rs.next()) {
                    continue;
                }

                stmt2.setString(1, v);
                stmt2.setString(2, CAT);
                stmt2.executeUpdate();
            }
            stmt1.close();
            stmt2.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPROPERTYTYPE table");
            return false;
        }
        return true;
    }

    public boolean insertSMs(List SMs) {
        if (SMs == null) {
            return true;
        }
        ResultSet rs = null;
        String sql1 = "select * from marker where upper(marker)=";
        String sql2 = "insert into marker" +
                " (marker)" +
                " values(?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < SMs.size(); i++) {
                String v = (String) SMs.get(i);
                stmt1.setString(1, v.toUpperCase());
                rs = stmt1.executeQuery();
                if ((rs != null) && rs.next()) {
                    continue;
                }

                stmt2.setString(1, v);
                stmt2.executeUpdate();
            }
            stmt1.close();
            stmt2.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into MARKER table");
            return false;
        }
        return true;
    }

    public boolean insertGCs(List GCs) {
        if (GCs == null) {
            return true;
        }
        ResultSet rs = null;
        String sql1 = "select * from growthcondition where upper(name)=?";
        String sql2 = "insert into growthcondition (growthid, name, hosttype, antibioticselection, growthcondition, comments) " +
                " values(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            for (int i = 0; i < GCs.size(); i++) {
                GrowthCondition gc = (GrowthCondition) GCs.get(i);
                stmt1.setString(1, gc.getName().toUpperCase());
                rs = stmt1.executeQuery();
                if ((rs != null) && rs.next()) {
                    continue;
                }

                int gid = DefTableManager.getNextid("growthid");
                stmt2.setInt(1, gid);
                stmt2.setString(2, gc.getName());
                stmt2.setString(3, gc.getHosttype());
                stmt2.setString(4, gc.getSelection());
                stmt2.setString(5, gc.getCondition());
                stmt2.setString(6, gc.getComments());
                stmt2.executeUpdate();
            }
            stmt1.close();
            stmt2.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into GROWTHCONDITION table");
            return false;
        }
        return true;
    }

    public boolean insertParents(List parents) {
        if (parents == null) {
            return true;
        }

        String sql = "insert into vectorparent" +
                " (vectorid, parentvectorname, parentvectorid, comments)" +
                " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < parents.size(); i++) {
                VectorParent vp = (VectorParent) parents.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setString(2, vp.getParentvectorname());

                int parentvectorid = vp.getParentvectorid();
                if (parentvectorid != 0) {
                    stmt.setInt(3, vp.getParentvectorid());
                } else {
                    stmt.setString(3, null);
                }
                stmt.setString(4, vp.getComments());
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPARENT table");
            return false;
        }
        return true;
    }

    public boolean insertVectorAuthors(List authors) {
        if (authors == null) {
            return true;
        }

        String sql = "insert into vectorauthor" +
                " (vectorid, authorid, authortype, creationdate)" +
                " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < authors.size(); i++) {
                VectorAuthor vp = (VectorAuthor) authors.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setInt(2, vp.getAuthorid());
                stmt.setString(3, vp.getType());
                stmt.setString(4, vp.getDate());
                //System.out.println(vp.getVectorid()+"\t"+vp.getAuthorid());
                stmt.executeUpdate();
            }
            stmt.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORAUTHOR table");
            return false;
        }
        return true;
    }

    public boolean insertVectorPublications(List publications) {
        if (publications == null) {
            return true;
        }

        String sql = "insert into vectorpublication" +
                " (vectorid, publicationid)" +
                " values(?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            for (int i = 0; i < publications.size(); i++) {
                VectorPublication vp = (VectorPublication) publications.get(i);
                stmt.setInt(1, vp.getVectorid());
                stmt.setInt(2, vp.getPublicationid());

                stmt.executeUpdate();
            }
            stmt.close();
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into VECTORPUBLICATIOIN table");
            return false;
        }
        return true;
    }

    public CloneVector queryCloneVector(int vectorid) {
        CloneVector v = null;

        String sql = "select name,description,form,type,sizeinbp,mapfilename,sequencefilename,comments" +
                " from vector where vectorid=" + vectorid;
        String sql2 = "select featureid,maptype,name,description,startpos,endpos" +
                " from vectorfeature where vectorid=" + vectorid + " order by maptype";
        String sql3 = "select propertytype from vectorproperty where vectorid=" + vectorid;
        String sql4 = "select vsynonym from vectorsynonym where vectorid=" + vectorid;
        String sql5 = "select v.authorid,v.authortype,v.creationdate,a.authorname" +
                " from vectorauthor v, authorinfo a" +
                " where v.authorid=a.authorid and v.vectorid=" + vectorid;
        String sql6 = "select p.publicationid,p.title,p.pmid" +
                " from publication p, vectorpublication v" +
                " where p.publicationid=v.publicationid and v.vectorid=" + vectorid;
        String sql7 = "select parentvectorname,parentvectorid,comments" +
                " from vectorparent where vectorid=" + vectorid;

        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();

            ResultSet rs = t.executeQuery(sql);
            if (rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                String form = rs.getString(3);
                String type = rs.getString(4);
                int size = rs.getInt(5);
                String mapfilename = rs.getString(6);
                String seqfilename = rs.getString(7);
                String comments = rs.getString(8);
                v = new CloneVector(vectorid, name, description, form, type, size, mapfilename, seqfilename, comments);

                ResultSet rs2 = t.executeQuery(sql2);
                List features = new ArrayList();
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
                v.setVectorfeatures(features);
                DatabaseTransaction.closeResultSet(rs2);

                ResultSet rs3 = t.executeQuery(sql3);
                List properties = new ArrayList();
                while (rs3.next()) {
                    String p = rs3.getString(1);
                    properties.add(p);
                }
                v.setProperty(properties);
                DatabaseTransaction.closeResultSet(rs3);

                ResultSet rs4 = t.executeQuery(sql4);
                List names = new ArrayList();
                while (rs4.next()) {
                    String synonym = rs4.getString(1);
                    names.add(synonym);
                }
                v.setSynonyms(names);
                DatabaseTransaction.closeResultSet(rs4);

                ResultSet rs5 = t.executeQuery(sql5);
                List authors = new ArrayList();
                while (rs5.next()) {
                    int authorid = rs5.getInt(1);
                    String tp = rs5.getString(2);
                    String date = rs5.getString(3);
                    String n = rs5.getString(4);
                    VectorAuthor author = new VectorAuthor(vectorid, authorid, tp, date, n);
                    authors.add(author);
                }
                v.setAuthors(authors);
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
                v.setPublications(publications);
                DatabaseTransaction.closeResultSet(rs6);

                ResultSet rs7 = t.executeQuery(sql7);
                List parents = new ArrayList();
                while (rs7.next()) {
                    String parentname = rs7.getString(1);
                    int parentid = rs7.getInt(2);
                    String c = rs7.getString(3);
                    VectorParent p = new VectorParent(vectorid, parentname, c, parentid);
                    parents.add(p);
                }
                v.setVectorparents(parents);
                DatabaseTransaction.closeResultSet(rs7);
            } else {
                handleError(null, "Cannot find vector record with vectorid: " + vectorid);
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            handleError(ex, "Cannot find vector record with vectorid: " + vectorid);
        }

        return v;
    }

    public int getVectorid(String name) {
        String sql = "select vectorid from vector where name=?";
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

    public List getVectorsByName(Collection names) {
        if (names == null) {
            return null;
        }

        return (getVectorsByNameAndUser(names, -1));
    }

    public List getVectorsByNameAndUser(Collection names, String uid) {
        if (names == null) {
            return null;
        }

        return (getVectorsByNameAndUser(names, Integer.parseInt(uid)));
    }

    public List getVectorsByNameAndUser(Collection names, int uid) {
        if (names == null) {
            return null;
        }

        return (getVectorsByNameExt(names, false, uid));
    }

    public List getVectorsByNameLike(Collection names) {
        if (names == null) {
            return null;
        }

        return (getVectorsByNameAndUserLike(names, -1));
    }

    public List getVectorsByNameAndUserLike(Collection names, String uid) {
        if (names == null) {
            return null;
        }

        return (getVectorsByNameAndUserLike(names, Integer.parseInt(uid)));
    }

    public List getVectorsByNameAndUserLike(Collection names, int uid) {
        if (names == null) {
            return null;
        }

        return (getVectorsByNameExt(names, true, uid));
    }

    private List getVectorsByNameExt(Collection names, boolean bLike, int uid) {
        List vectors = new ArrayList();
        String sql = null;
        if (bLike) {
            sql = "select a.vectorid,a.description,a.form,a.type,a.sizeinbp,a.mapfilename," +
                    " a.sequencefilename,a.comments,nvl(b.userid, 0) userid,nvl(b.status, '') status, a.name " +
                    " from vector a left join vectorsubmission b on a.vectorid=b.vectorid " +
                    " where name like ?";
        } else {
            sql = "select a.vectorid,a.description,a.form,a.type,a.sizeinbp,a.mapfilename," +
                    " a.sequencefilename,a.comments,nvl(b.userid, 0) userid,nvl(b.status, '') status, a.name " +
                    " from vector a left join vectorsubmission b on a.vectorid=b.vectorid " +
                    " where name=?";
        }

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = null;
            Iterator iter = names.iterator();
            String name = null;
            while (iter.hasNext()) {
                name = (String) iter.next();
                if (bLike) {
                    stmt.setString(1, "%" + name + "%");
                } else {
                    stmt.setString(1, name);
                }

                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    int vectorid = rs.getInt(1);
                    String description = rs.getString(2);
                    String form = rs.getString(3);
                    String type = rs.getString(4);
                    int size = rs.getInt(5);
                    String mapfilename = rs.getString(6);
                    String seqfilename = rs.getString(7);
                    String comments = rs.getString(8);
                    int userid = rs.getInt(9);
                    String status = rs.getString(10);
                    String vname = rs.getString(11);
                    if (status == null) {
                        status = "";
                    }
                    if (status.equals(Constants.PENDING)) {
                        if (userid != uid) {
                            status = new String(Constants.PENDING_X);
                        }
                    }
                    CloneVector v = new CloneVector(vectorid, vname, description, form, type, size, mapfilename, seqfilename, comments, status, userid);
                    vectors.add(v);
                }
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return vectors;
    }

    public List getForms() {
        return getForms(null, false);
    }

    public List getFormsByNames(Collection formnames) {
        return getForms(formnames, false);
    }

    public List getFormsByNamesLike(Collection formnames) {
        return getForms(formnames, true);
    }

    private List getForms(Collection formnames, boolean bLike) {
        List forms = new ArrayList();
        String sql = new String("select form from vectorform");
        ResultSet rs = null;
        PreparedStatement stmt = null;

        if ((formnames != null) && (formnames.size() > 0)) {
            if (bLike) {
                sql = new String(sql + " where form like ?");
            } else {
                sql = new String(sql + " where form = ?");
            }
            try {
                stmt = conn.prepareStatement(sql);
                Iterator iter = formnames.iterator();
                String formname = null;
                while (iter.hasNext()) {
                    if ((formnames != null) && (formnames.size() > 0)) {
                        if (bLike) {
                            formname = "%" + (String) iter.next() + "%";
                        } else {
                            formname = (String) iter.next();
                        }
                    }
                    stmt.setString(1, formname);
                    rs = DatabaseTransaction.executeQuery(stmt);
                    while (rs.next()) {
                        String sForm = rs.getString(1);
                        forms.add(sForm);
                    }
                }
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            } finally {
                DatabaseTransaction.closeResultSet(rs);
                DatabaseTransaction.closeStatement(stmt);
            }
        } else {
            try {
                stmt = conn.prepareStatement(sql);
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    String sForm = rs.getString(1);
                    forms.add(sForm);
                }
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            } finally {
                DatabaseTransaction.closeResultSet(rs);
                DatabaseTransaction.closeStatement(stmt);
            }
        }

        return forms;
    }

    public List getFeatures(int vid) {
        if (vid < 1) {
            return null;
        }

        List features = new ArrayList();
        String sql = new String("select featureid,name,description,startpos,endpos,maptype from vectorfeature where vectorid=?");
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, vid);

            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                VectorFeature vf = new VectorFeature(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getInt(4), rs.getInt(5), vid, rs.getString(6));
                features.add(vf);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }

        return features;
    }

    public List getFeatureNames() {
        List featurenames = new ArrayList();
        String sql = new String("select name from featurename order by name");
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                featurenames.add(rs.getString(1));
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        return featurenames;
    }

    public List getFeatureTypes() {
        List featuretypes = new ArrayList();
        String sql = new String("select maptype from featuremaptype order by maptype");
        ResultSet rs = null;
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                featuretypes.add(rs.getString(1));
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        return featuretypes;
    }

    public List getTypes() {
        return getTypes(null, false);
    }

    public List getTypesByNames(Collection typenames) {
        return getTypes(typenames, false);
    }

    public List getTypesByNamesLike(Collection typenames) {
        return getTypes(typenames, true);
    }

    private List getTypes(Collection typenames, boolean bLike) {
        List types = new ArrayList();
        String sql = new String("select type from vectortype");
        ResultSet rs = null;
        PreparedStatement stmt = null;

        if ((typenames != null) && (typenames.size() > 0)) {
            if (bLike) {
                sql = new String(sql + " where type like ?");
            } else {
                sql = new String(sql + " where type = ?");
            }
            try {
                stmt = conn.prepareStatement(sql);
                Iterator iter = typenames.iterator();
                String typename = null;
                while (iter.hasNext()) {
                    typename = (String) iter.next();
                    if (bLike) {
                        stmt.setString(1, "%" + typename + "%");
                    } else {
                        stmt.setString(1, typename);
                    }

                    rs = DatabaseTransaction.executeQuery(stmt);
                    while (rs.next()) {
                        String sProperty = rs.getString(1);
                        types.add(sProperty);
                    }
                }
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            } finally {
                DatabaseTransaction.closeResultSet(rs);
                DatabaseTransaction.closeStatement(stmt);
            }
        } else {
            try {
                stmt = conn.prepareStatement(sql);
                rs = DatabaseTransaction.executeQuery(stmt);
                while (rs.next()) {
                    String sProperty = rs.getString(1);
                    types.add(sProperty);
                }

                DatabaseTransaction.closeResultSet(rs);
                DatabaseTransaction.closeStatement(stmt);
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
            } finally {
                DatabaseTransaction.closeResultSet(rs);
                DatabaseTransaction.closeStatement(stmt);
            }
        }

        return types;
    }

    public CloneVector getVectorByName(String name) {
        String sql = "select vectorid,description,form,type,sizeinbp,mapfilename," +
                " sequencefilename,comments from vector where name=?";

        if (name == null) {
            return null;
        }

        CloneVector vector = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = null;
            stmt.setString(1, name);
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                int vectorid = rs.getInt(1);
                String description = rs.getString(2);
                String form = rs.getString(3);
                String type = rs.getString(4);
                int size = rs.getInt(5);
                String mapfilename = rs.getString(6);
                String seqfilename = rs.getString(7);
                String comments = rs.getString(8);
                vector = new CloneVector(vectorid, name, description, form, type, size, mapfilename, seqfilename, comments);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }

        return vector;
    }

    public CloneVector getVectorByID(String id) {
        if ((id == null) || (id.length() < 1)) {
            return null;
        }

        return getVectorByID(Integer.parseInt(id));
    }

    public CloneVector getVectorByID(int id) {
        if (id < 1) {
            return null;
        }
        String sql = "select a.name,a.description,a.form,a.type,a.sizeinbp,a.mapfilename," +
                " a.sequencefilename,a.comments,nvl(b.userid, 0) userid,nvl(b.status, '') status " +
                " from vector a left join vectorsubmission b on a.vectorid=b.vectorid " +
                " where a.vectorid=?";
        CloneVector vector = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = null;
            stmt.setInt(1, id);
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                String name = rs.getString(1);
                String description = rs.getString(2);
                String form = rs.getString(3);
                String type = rs.getString(4);
                int size = rs.getInt(5);
                String mapfilename = rs.getString(6);
                String seqfilename = rs.getString(7);
                String comments = rs.getString(8);
                int userid = rs.getInt(9);
                String status = rs.getString(10);
                if (status == null) {
                    status = "";
                }
                vector = new CloneVector(id, name, description, form, type, size, mapfilename, seqfilename, comments, status, userid);
                sql = "select VSYNONYM from vectorsynonym where vectorid=?";
                stmt = conn.prepareStatement(sql);
                rs = null;
                stmt.setInt(1, id);
                rs = DatabaseTransaction.executeQuery(stmt);
                List s = new ArrayList();
                while (rs.next()) {
                    s.add(rs.getString(1));
                }
                vector.setSynonyms(s);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }

        return vector;
    }

    public CloneVector getVectorByAnyName(String name) {
        if (name == null) {
            return null;
        }
        CloneVector vector = null;
        /*
        CloneVector vector = this.getVectorByName(name);
        if(vector != null)
        return vector;
        
        String sql = "select vectorid,description,form,type,sizeinbp,mapfilename," +
        " sequencefilename,comments from vector where vectorid in ("+
        " select vectorid from vectorsynonym where vsynonym=?)";
         */
        String sql = new String("select a.vectorid,a.description,a.form,a.type,a.sizeinbp," +
                "a.mapfilename,a.sequencefilename,a.comments, b.vsynonym " +
                "from vector a left join vectorsynonym b on a.vectorid = b.vectorid " +
                "where upper(trim(a.name)) like '?' or upper(trim(b.vsynonym)) like '?'");

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = null;
            stmt.setString(1, name.trim().toUpperCase());
            rs = DatabaseTransaction.executeQuery(stmt);
            if (rs.next()) {
                int vectorid = rs.getInt(1);
                String description = rs.getString(2);
                String form = rs.getString(3);
                String type = rs.getString(4);
                int size = rs.getInt(5);
                String mapfilename = rs.getString(6);
                String seqfilename = rs.getString(7);
                String comments = rs.getString(8);
                vector = new CloneVector(vectorid, name, description, form, type, size, mapfilename, seqfilename, comments);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }

        return vector;
    }

    public static Map getAllVectorPerpertyTypes() {
        Map types = new TreeMap();
        String sql = "select category,propertytype,displayvalue from vectorpropertytype order by category, displayvalue";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            String lastCategory = null;
            List l = null;
            while (rs.next()) {
                String category = rs.getString(1);
                String type = rs.getString(2);
                String value = rs.getString(3);
                if (lastCategory != null && lastCategory.equals(category)) {
                    l.add(new VectorProperty(type, value));
                } else {
                    l = new ArrayList();
                    l.add(new VectorProperty(type, value));
                    lastCategory = category;
                    types.put(category, l);
                }
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

        return types;
    }

    public Map getVectorPerperties(int vid) {
        if (vid < 1) {
            return null;
        }
        Map vps = new TreeMap();
        String sql = "select a.category,a.propertytype,a.displayvalue,nvl(b.vectorid, 0) from vectorpropertytype a left join vectorproperty b on a.propertytype=b.propertytype and b.vectorid=? order by a.category,a.displayvalue";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = null;
            stmt.setInt(1, vid);
            rs = DatabaseTransaction.executeQuery(stmt);
            String lastCategory = null;
            List l = null;
            while (rs.next()) {
                String category = rs.getString(1);
                String type = rs.getString(2);
                String value = rs.getString(3);
                int vvid = rs.getInt(4);
                if (lastCategory != null && lastCategory.equals(category)) {
                    l.add(new VectorProperty(vvid, type, value));
                } else {
                    l = new ArrayList();
                    l.add(new VectorProperty(vvid, type, value));
                    lastCategory = category;
                    vps.put(category, l);
                }
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

        return vps;
    }

    public List getPerpertiesByCat(String c) {
        if ((c == null) || (c.length() < 1)) {
            return null;
        }
        String cat = VectorProperty.ASSAY;
        if (c.equals("A")) {
            cat = VectorProperty.ASSAY;
        } else if (c.equals("C")) {
            cat = VectorProperty.CLONING;
        } else if (c.equals("E")) {
            cat = VectorProperty.EXPRESSION;
        } else {
            return null;
        }

        List vps = new ArrayList();
        String sql = "select a.propertytype,a.displayvalue from vectorpropertytype a  where a.category=? order by a.displayvalue";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = null;
            stmt.setString(1, cat);
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                String type = rs.getString(1);
                String value = rs.getString(2);
                vps.add(new VectorProperty(-1, type, value));
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

        return vps;
    }

    public List getVectorPerpertiesByCat(int vid, String c) {
        if ((vid < 1) || (c == null) || (c.length() < 1)) {
            return null;
        }
        String cat = VectorProperty.ASSAY;
        if (c.equals("A")) {
            cat = VectorProperty.ASSAY;
        } else if (c.equals("C")) {
            cat = VectorProperty.CLONING;
        } else if (c.equals("E")) {
            cat = VectorProperty.EXPRESSION;
        } else {
            return null;
        }

        List vps = new ArrayList();
        String sql = "select a.category,a.propertytype,a.displayvalue,nvl(b.vectorid, 0) from vectorpropertytype a left join vectorproperty b on a.propertytype=b.propertytype and b.vectorid=? where a.category=? order by a.category,a.displayvalue";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            rs = null;
            stmt.setInt(1, vid);
            stmt.setString(2, cat);
            rs = DatabaseTransaction.executeQuery(stmt);
            while (rs.next()) {
                String type = rs.getString(2);
                String value = rs.getString(3);
                int vvid = rs.getInt(4);
                vps.add(new VectorProperty(vvid, type, value));
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

        return vps;
    }

    public static Set getVectoridByProperty(String property) {
        String sql = "select vectorid from vectorproperty where propertytype='" + property + "'";
        Set vectorids = new TreeSet();
        DatabaseTransaction t = null;
        try {
            t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt(1);
                vectorids.add((new Integer(id)).toString());
            }
            DatabaseTransaction.closeResultSet(rs);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        }

        return vectorids;
    }

    public static void main(String args[]) {
        DatabaseTransaction t = null;
        Connection conn = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            VectorManager manager = new VectorManager(conn);
            /**
            CloneVector v = manager.queryCloneVector(3);
            System.out.println(v.getName());
            System.out.println(v.getDescription());
            System.out.println(v.getComments());
            System.out.println(v.getForm());
            System.out.println(v.getType());
            System.out.println(v.getSize());
            System.out.println(v.getMapfilename());
            System.out.println(v.getSeqfilename());
            System.out.println(v.getPropertyString());
            System.out.println(v.getSynonymString());
            List features = v.getVectorfeatures();
            for(int i=0; i<features.size(); i++) {
            VectorFeature f = (VectorFeature)features.get(i);
            System.out.println(f.getDescription());
            System.out.println(f.getFeatureid());
            System.out.println(f.getMaptype());
            System.out.println(f.getName());
            System.out.println(f.getStart());
            System.out.println(f.getStop());
            }
            List authors = v.getAuthors();
            for(int i=0; i<authors.size(); i++) {
            VectorAuthor a = (VectorAuthor)authors.get(i);
            System.out.println(a.getAuthorid());
            System.out.println(a.getDate());
            System.out.println(a.getName());
            System.out.println(a.getType());
            }
            List publications = v.getPublications();
            for(int i=0; i<publications.size(); i++) {
            Publication p = (Publication)publications.get(i);
            System.out.println(p.getPmid());
            System.out.println(p.getPublicationid());
            System.out.println(p.getTitle());
            }
            List parents = v.getVectorparents();
            for(int i=0; i<parents.size(); i++) {
            VectorParent p = (VectorParent)parents.get(i);
            System.out.println(p.getParentvectorid());
            System.out.println(p.getParentvectorname());
            System.out.println(p.getComments());
            }
             **/
            Map types = VectorManager.getAllVectorPerpertyTypes();
            Set keys = types.keySet();
            Iterator iter = keys.iterator();
            while (iter.hasNext()) {
                String category = (String) iter.next();
                List l = (List) types.get(category);
                for (int i = 0; i < l.size(); i++) {
                    String type = (String) l.get(i);
                    System.out.println(category + ": " + type);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}
