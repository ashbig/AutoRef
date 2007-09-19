/*
 * CloneInfoSet.java
 *
 * Created on June 17, 2003, 3:02 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.query.handler.QueryManager;

/**
 *
 * @author  dzuo
 */
public class CloneInfoSet {
    protected ArrayList allCloneInfo;
    protected String criteria;
    
    public void setCriteria(String s) {this.criteria = s;}
    
    public ArrayList getAllCloneInfo() {return allCloneInfo;}
    
    /** Creates a new instance of CloneInfoSet */
    public CloneInfoSet() {
    }
    
    public void restoreByCloneid(List cloneids) throws Exception {
        String sql = "select c.cloneid, c.clonename, c.clonetype, c.mastercloneid, c.comments, c.status,"+
        " c.sequenceid, f.genusspecies, f.cdsstart, f.cdsstop, f.cdslength, f.gccontent,"+
        " cd.constructid, cd.oligoid_5p, cd.oligoid_3p, cd.constructtype, cd.projectid, cd.workflowid,"+
        " c.strategyid, cs.strategyname, cs.vectorname, cs.linkerid_5p, cs.linkerid_3p"+
        " from clones c, flexsequence f, constructdesign cd, cloningstrategy cs"+
        " where c.constructid=cd.constructid"+
        " and f.sequenceid=cd.sequenceid"+
        " and c.strategyid=cs.strategyid"+
        " and c.cloneid=?"+
        " order by c.cloneid";
        
        restore(sql, cloneids);
    }
    
    public void restoreBySequenceid(List seqids) throws Exception {
        String sql = "select c.cloneid, c.clonename, c.clonetype, c.mastercloneid, c.comments, c.status,"+
        " c.sequenceid, f.genusspecies, f.cdsstart, f.cdsstop, f.cdslength, f.gccontent,"+
        " cd.constructid, cd.oligoid_5p, cd.oligoid_3p, cd.constructtype, cd.projectid, cd.workflowid,"+
        " c.strategyid, cs.strategyname, cs.vectorname, cs.linkerid_5p, cs.linkerid_3p"+
        " from clones c, flexsequence f, constructdesign cd, cloningstrategy cs"+
        " where c.constructid=cd.constructid"+
        " and f.sequenceid=cd.sequenceid"+
        " and c.strategyid=cs.strategyid"+
        " and c.sequenceid=?";
        
        if(QueryManager.SEQUENCE_VERIFIED.equals(criteria)) {
            sql += " and c.status='"+CloneInfo.SEQ_VERIFIED+"'";
        }
        
        sql += " order by c.sequenceid";
        
        restore(sql, seqids);
    }
    
    protected void restore(String sql, List cloneids) throws Exception {
        String sql2 = "select nametype,namevalue from name where sequenceid=?";
        String sql3 = "select nametype,namevalue from clonename where cloneid=?";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        allCloneInfo = new ArrayList();
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            
            for(int i=0; i<cloneids.size(); i++) {
                String cloneid = (String)cloneids.get(i);
                stmt.setInt(1, Integer.parseInt(cloneid));
                rs = DatabaseTransaction.executeQuery(stmt);
                while(rs.next()) {
                    int id = rs.getInt(1);
                    String clonename = rs.getString(2);
                    String clonetype = rs.getString(3);
                    int mastercloneid = rs.getInt(4);
                    String comment = rs.getString(5);
                    String status = rs.getString(6);
                    int sequenceid = rs.getInt(7);
                    String species = rs.getString(8);
                    int cdsstart = rs.getInt(9);
                    int cdsstop = rs.getInt(10);
                    int gccontent = rs.getInt(12);
                    int constructid = rs.getInt(13);
                    int oligoid_5p = rs.getInt(14);
                    int oligoid_3p = rs.getInt(15);
                    String constructtype = rs.getString(16);
                    int projectid = rs.getInt(17);
                    int workflowid = rs.getInt(18);
                    int strategyid = rs.getInt(19);
                    String strategyname = rs.getString(20);
                    String vectorname = rs.getString(21);
                    int linkerid_5p = rs.getInt(22);
                    int linkerid_3p = rs.getInt(23);
                    
                    CloneInfo info = new CloneInfo(cdsstart, cdsstop, null);
                    info.setCloneid(id);
                    info.setClonename(clonename);
                    info.setClonetype(clonetype);
                    info.setComment(comment);
                    info.setConstructid(constructid);
                    info.setConstructtype(constructtype);
                    info.setMastercloneid(mastercloneid);
                    info.setOligoid3p(oligoid_3p);
                    info.setOligoid5p(oligoid_5p);
                    info.setProjectid(projectid);
                    info.setRefsequenceid(sequenceid);
                    info.setSpecies(species);
                    info.setStatus(status);
                    info.setWorkflowid(workflowid);
                    CloneVector cv = new CloneVector(vectorname);
                    CloneLinker l5p = new CloneLinker(linkerid_5p);
                    CloneLinker l3p = new CloneLinker(linkerid_3p);
                    CloningStrategy st = new CloningStrategy(strategyid, strategyname, cv, l5p, l3p);
                    info.setCloningstrategy(st);
                    
                    stmt2.setInt(1, sequenceid);
                    rs2 = DatabaseTransaction.executeQuery(stmt2);
                    String gi = null;
                    String genesymbol = null;
                    String genbank = null;
                    String locusid = null;
                    String panumber = null;
                    String sgd = null;
                    while(rs2.next()) {
                        String nametype = rs2.getString(1);
                        String namevalue = rs2.getString(2);
                        if(FlexSequence.GI.equals(nametype)) {
                            gi = namevalue;
                        }
                        if(FlexSequence.GENBANK_ACCESSION.equals(nametype)) {
                            genbank = namevalue;
                        }
                        if(FlexSequence.GENE_SYMBOL.equals(nametype)) {
                            genesymbol = namevalue;
                        }
                        if(FlexSequence.LOCUS_ID.equals(nametype)) {
                            locusid = namevalue;
                        }
                        if(FlexSequence.PANUMBER.equals(nametype)) {
                            panumber = namevalue;
                        }
                        if(FlexSequence.SGD.equals(nametype)) {
                            sgd = namevalue;
                        }
                    }
                    
                    stmt3.setInt(1, Integer.parseInt(cloneid));
                    rs3 = DatabaseTransaction.executeQuery(stmt3);
                    String cloneAcc = null;
                    String cloneGi = null;
                    while(rs3.next()) {
                        String nametype = rs3.getString(1);
                        String namevalue = rs3.getString(2);
                        
                        if(FlexSequence.GENBANK_ACCESSION.equals(nametype)) {
                            cloneAcc = namevalue;
                        }
                        if(FlexSequence.GI.equals(nametype)) {
                            cloneGi = namevalue;
                        }
                    }
                    
                    NameInfo ni = new NameInfo(gi, genesymbol, genbank, locusid, panumber, sgd);
                    ni.setCloneAcc(cloneAcc);
                    ni.setCloneGi(cloneGi);
                    info.setNameinfo(ni);
                    allCloneInfo.add(info);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeResultSet(rs3);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    
    
    public static void main(String args[]) {
        List clones = new ArrayList();
        clones.add("110892");
        clones.add("110893");
        clones.add("110894");
        clones.add("110891");
        clones.add("111103");
        
        List seqids = new ArrayList();
        seqids.add("17918");
        //seqids.add("19142");
        //seqids.add("18000");
        //seqids.add("17973");
        
        CloneInfoSet info = new CloneInfoSet();
        try {
            info.restoreByCloneid(clones);
            //info.restoreBySequenceid(seqids);
            List allClones = info.getAllCloneInfo();
            printAllClones(allClones);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void printAllClones(List allClones) {
        for(int i=0; i<allClones.size(); i++) {
            CloneInfo clone = (CloneInfo)allClones.get(i);
            System.out.println("Clone ID:\t"+clone.getCloneid());
            System.out.println("getClonename:\t"+clone.getClonename());
            System.out.println("getClonetype:\t"+clone.getClonetype());
            System.out.println("getMastercloneid:\t"+clone.getMastercloneid());
            System.out.println("getComment:\t"+clone.getComment());
            System.out.println("getStatus:\t"+clone.getStatus());
            System.out.println("getRefsequenceid:\t"+clone.getRefsequenceid());
            System.out.println("getSpecies:\t"+clone.getSpecies());
            System.out.println("getConstructid:\t"+clone.getConstructid());
            System.out.println("getOligoid5p:\t"+clone.getOligoid5p());
            System.out.println("getOligoid3p:\t"+clone.getOligoid3p());
            System.out.println("getConstructtype:\t"+clone.getConstructtype());
            System.out.println("getProjectid:\t"+clone.getProjectid());
            System.out.println("getWorkflowid:\t"+clone.getWorkflowid());
            System.out.println("stategyid:\t"+clone.getCloningstrategy().getId());
            System.out.println("getName:\t"+clone.getCloningstrategy().getName());
            System.out.println("getClonevector:\t"+clone.getCloningstrategy().getClonevector());
            System.out.println("getLinker5p:\t"+clone.getCloningstrategy().getLinker5p());
            System.out.println("getLinker3p:\t"+clone.getCloningstrategy().getLinker3p());
            System.out.println("gi:\t"+clone.getNameinfo().getGi());
            System.out.println("getGenesymbol:\t"+clone.getNameinfo().getGenesymbol());
            System.out.println("getGenbank:\t"+clone.getNameinfo().getGenbank());
            System.out.println("getLocusid:\t"+clone.getNameinfo().getLocusid());
            System.out.println("-----");
                        
            clone.restoreClonestorage();
            List storages = clone.getStorages();
            for(int k=0; k<storages.size(); k++) {
                CloneStorage storage = (CloneStorage)storages.get(k);
                System.out.println(storage.getSampleid());
            }
        }
    }
}
