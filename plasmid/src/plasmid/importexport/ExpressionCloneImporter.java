/*
 * ExpressionCloneImporter.java
 *
 * Created on May 17, 2006, 12:54 PM
 */

package plasmid.importexport;

import java.util.*;
import java.sql.*;
import java.io.*;

import plasmid.coreobject.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class ExpressionCloneImporter {
    private static final String flexurl = "jdbc:oracle:thin:@kotel:1532:flex";
    private static final String flexusername = "flex_production";
    private static final String flexpassword = "3monkeys";
    private static final String plasmidurl = "jdbc:oracle:thin:@128.103.32.228:1521:plasmid";
    private static final String plasmidusername = "plasmid_production";
    private static final String plasmidpassword = "plasmID";
    
    public static final String VER_SEQ = "Insert was fully sequenced in parent vector";
    public static final String VER_PCR = "Verification by PCR";
    public static final String VER_FLORE = "Verification by fluorescence detection in cells";
    public static final String VER_PROTEIN = "Verification by protein expression";
    public static final String VER_RES = "Verification by restriction enzyme digest";
    public static final String NOTDONE = "NOT DONE";
    public static final String PASS = "PASS";
    public static final String FAIL = "FAIL";
    
    private List insertClones;
    
    /** Creates a new instance of ExpressionCloneImporter */
    public ExpressionCloneImporter() {
    }
    
    public List getInsertClones() {return insertClones;}
    
    public List getFlexClones(String status, String vectorname) throws Exception {
        String sql = "select c.cloneid,clonename,mastercloneid,status,comments,"+
        " vectorname,genusspecies,v.PCRRESULT,v.FLORESCENCERESULT,"+
        " v.PROTEINRESULT,v.RESTRICTIONRESULT,v.COLONYRESULT,"+
        " cs.storagecontainerlabel, cs.storagecontainerposition"+
        " from clones c, cloningstrategy s, flexsequence f, clonevalidation v, clonestorage cs"+
        " where c.strategyid=s.strategyid and c.cloneid=v.cloneid(+)"+
        " and c.sequenceid=f.sequenceid and c.cloneid=cs.cloneid"+
        " and c.plasmid is null and c.strategyid in (24, 17, 15, 16, 20, 21, 13)"+
        " and cs.storagetype='Working Storage'";
        
        if(status != null)
            sql += " and c.status='"+status+"'";
        if(vectorname != null)
            sql += " and s.vectorname='"+vectorname+"'";
        
        List clones = new ArrayList();
        DatabaseTransactionNoPool t = new DatabaseTransactionNoPool(flexurl,flexusername,flexpassword);
        ResultSet rs = t.executeQuery(sql);
        while(rs.next()) {
            int cloneid = rs.getInt(1);
            String clonename = rs.getString(2);
            int mastercloneid = rs.getInt(3);
            String s = rs.getString(4);
            String comments = rs.getString(5);
            String v = rs.getString(6);
            String species = rs.getString(7);
            String pcr = rs.getString(8);
            String flore = rs.getString(9);
            String protein = rs.getString(10);
            String restriction = rs.getString(11);
            String colony = rs.getString(12);
            String label = rs.getString(13);
            int position = rs.getInt(14);
            FlexClone c = new FlexClone(cloneid, mastercloneid, v, clonename, s, comments, species,pcr,flore,protein,colony,restriction,label,position);
            clones.add(c);
        }
        DatabaseTransactionNoPool.closeResultSet(rs);
        
        return clones;
    }
    
    public int getCloneAuthorid(String vectorname) {
        return 1;
    }
    
    public List insertClones(List clones, Connection conn) throws Exception {
        String sql = "select distinct cloneid from clonename"+
        " where nametype='"+CloneNameType.HIP_MASTER_CLONE_ID+"'"+
        " and namevalue=?";
        String sql2 = "select c.domain, c.comments from clone c where c.cloneid=?";
        String sql3 = "select insertid from cloneinsert where cloneid=?";
        
        PreparedStatement stmt = conn.prepareStatement(sql);
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        
        CloneManager m = new CloneManager(conn);
        List plasmidClones = new ArrayList();
        List inserts = new ArrayList();
        List names = new ArrayList();
        List clonesWithoutMastercloneid = new ArrayList();
        insertClones = new ArrayList();
        
        int id = getMaxNumber("clone", "cloneid");
        if(id < 0) {
            throw new Exception("Cannot get cloneid from clone table.");
        }
        
        System.out.println("Total clone: "+clones.size());
        for(int i=0; i<clones.size(); i++) {
            FlexClone c = (FlexClone)clones.get(i);
            int plasmidCloneid = 0;
            
            stmt.setString(1, (new Integer(c.getMastercloneid())).toString());
            ResultSet rs = DatabaseTransactionNoPool.executeQuery(stmt);
            if(rs.next()) {
                plasmidCloneid = rs.getInt(1);
            }
            if(plasmidCloneid == 0) {
                //clones.remove(c);
                clonesWithoutMastercloneid.add(c);
                System.out.println("Cannot get plasmid cloneid with master cloneid "+c.getMastercloneid());       
                continue;
            }
            DatabaseTransactionNoPool.closeResultSet(rs);
            
            System.out.println("plasmid cloneid: "+plasmidCloneid);
            stmt2.setInt(1, plasmidCloneid);
            rs = DatabaseTransactionNoPool.executeQuery(stmt2);
            Clone plasmidClone = null;
            if(rs.next()) {
                System.out.println("Get PlasmID record with cloneid: "+plasmidCloneid);
                String domain = rs.getString(1);
                String comments = rs.getString(2);
                
                String pcr = c.getPcr();
                String flore = c.getFlor();
                String protein = c.getProtein();
                String restriction = c.getRestriction();
                String colony = c.getColony();
                
                String isVer = Clone.VERIFIED_NO;
                String vermethod = VER_SEQ;
                if((pcr==null&&flore==null&protein==null&&restriction==null&&colony==null)
                || (pcr.trim().length()==0&&flore.trim().length()==0&&protein.trim().length()==0&&restriction.trim().length()==0&&colony.trim().length()==0)
                || (pcr.trim().equals(NOTDONE)&&flore.trim().equals(NOTDONE)&&protein.trim().equals(NOTDONE)&&restriction.trim().equals(NOTDONE)&&colony.trim().equals(NOTDONE))) {
                    isVer = Clone.VERIFIED_NO;
                } else {
                    if(pcr.trim().equals(PASS)) {
                        isVer = Clone.VERIFIED_YES;
                        vermethod += "; "+VER_PCR;
                    }
                    if(pcr.trim().equals(FAIL))
                        throw new Exception("Wrong clone validation status ("+pcr+") for cloneid "+c.getCloneid());
                    
                    if(flore.trim().equals(PASS)) {
                        isVer = Clone.VERIFIED_YES;
                        vermethod += "; "+VER_FLORE;
                    }
                    if(flore.trim().equals(FAIL))
                        throw new Exception("Wrong clone validation status ("+flore+") for cloneid "+c.getCloneid());
                    
                    if(protein.trim().equals(PASS)){
                        isVer = Clone.VERIFIED_YES;
                        vermethod += "; "+VER_PROTEIN;
                    }
                    if(protein.trim().equals(FAIL))
                        throw new Exception("Wrong clone validation status ("+protein+") for cloneid "+c.getCloneid());
                    
                    if(restriction.trim().equals(PASS)){
                        isVer = Clone.VERIFIED_YES;
                        vermethod += "; "+VER_RES;
                    }
                    if(restriction.trim().equals(FAIL))
                        throw new Exception("Wrong clone validation status ("+restriction+") for cloneid "+c.getCloneid());
                    
                    if(colony.trim().equals(FAIL))
                        throw new Exception("Wrong clone validation status ("+colony+") for cloneid "+c.getCloneid());
                }
                
                plasmidClone = new Clone(id,null,Clone.CDNA,isVer,vermethod,domain,null,Clone.HIP_ONLY,comments,0,c.getVectorname(),null,Clone.AVAILABLE,null,Clone.SOURCE_HIP,null);
                
                VectorManager man = new VectorManager(conn);
                String vectorname = c.getVectorname();
                if("pLP-DS 3xMyc".equals(vectorname))
                    vectorname="pLP-DS3xMyc";
                if("pLP-DS 3xFlag".equals(vectorname))
                    vectorname="pLP-DS3xFlag";
                if("pCITE-GST".equals(vectorname))
                    vectorname="pANT7_cGST";
                int vectorid = man.getVectorid(vectorname);
                if(vectorid <= 0) {
                    //clones.remove(c);
                    //continue;
                    throw new Exception("Cannot get vectorid with vectorname="+c.getVectorname());
                }
                plasmidClone.setVectorid(vectorid);
                
                java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
                fmt.setMaximumIntegerDigits(8);
                fmt.setMinimumIntegerDigits(8);
                fmt.setGroupingUsed(false);
                DefTableManager manager = new DefTableManager();
                String sp = manager.getVocabulary("species", "genusspecies", "code", plasmidClone.getDomain(), conn);
                String tp = manager.getVocabulary("clonetype", "clonetype", "code", plasmidClone.getType(), conn);
                if(sp == null) {
                    if(Clone.NOINSERT.equals(plasmidClone.getDomain())) {
                        sp = Clone.SPECIES_NOINSERT;
                    } else {
                        throw new Exception("Cannot find code for species: "+plasmidClone.getDomain());
                    }
                }
                if(tp == null) {
                    throw new Exception("Cannot find code for clonetype: "+plasmidClone.getType());
                }
                plasmidClone.setName(sp+tp+fmt.format(id));                
                plasmidClones.add(plasmidClone);
                
                CloneName n1 = new CloneName(id, CloneNameType.HIP_CLONE_ID, (new Integer(c.getCloneid())).toString(), "http://kotel.harvard.edu:8080/FLEX/ViewClone.do?cloneid="+c.getCloneid()+"&isDisplay=1");
                CloneName n2 = new CloneName(id, CloneNameType.HIP_MASTER_CLONE_ID, (new Integer(c.getMastercloneid())).toString(), null);
                CloneName n3 = new CloneName(id, CloneNameType.ORIGINAL_CLONE_ID, c.getClonename(), null);
                names.add(n1);
                names.add(n2);
                names.add(n3);
                
                c.setPlasmidCloneid(id);
                insertClones.add(c);
                id++;
            } else {
                throw new Exception("Cannot query clone with cloneid="+plasmidCloneid);
            }
            DatabaseTransactionNoPool.closeResultSet(rs);
            
            stmt3.setInt(1, plasmidCloneid);
            rs = DatabaseTransactionNoPool.executeQuery(stmt3);
            while(rs.next()) {
                int insertid = rs.getInt(1);
                DnaInsert insert = new DnaInsert();
                insert.setInsertid(insertid);
                insert.setCloneid(plasmidClone.getCloneid());
                inserts.add(insert);
            }
            DatabaseTransactionNoPool.closeResultSet(rs);
        }
        DatabaseTransactionNoPool.closeStatement(stmt);
        DatabaseTransactionNoPool.closeStatement(stmt2);
        DatabaseTransactionNoPool.closeStatement(stmt3);
        
        System.out.println("Insert clones.");
        if(!m.insertClones(plasmidClones)) {
            throw new Exception("Error occured while inserting into CLONE table.");
        }
        System.out.println("Insert clone names.");
        if(!m.insertCloneNames(names)) {
            throw new Exception("Error occured while inserting into CLONENAME table");
        }
        System.out.println("Insert clone inserts.");
        if(!m.insertCloneInsertsWithoutInsertInfo(inserts)) {
            throw new Exception("Error occured while inserting into CLONEINSERT table");
        }
        return clonesWithoutMastercloneid;
    }
    
    public void updateFlexCloneStatus(List clones, Connection conn) throws Exception {
        System.out.println("Total Clones: "+clones.size());
        String sql = "update clones set plasmid='Y' where cloneid=?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<clones.size(); i++) {
            FlexClone c = (FlexClone)clones.get(i);
            stmt.setInt(1, c.getCloneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public void writePlateFile(List clones, String filename) throws Exception {
        OutputStreamWriter out = new FileWriter(filename);
        out.write("plate\n");
        out.write("label\tposition\tCLONEID\n");
        
        for(int i=0; i<clones.size(); i++) {
            FlexClone c = (FlexClone)clones.get(i);
            out.write(c.getLabel()+"\t"+c.getPosition()+"\t"+c.getPlasmidCloneid()+"\n");
        }
        out.close();
    }
      
    public void writeClones(List clones, String filename) throws Exception {
        OutputStreamWriter out = new FileWriter(filename);
        out.write("FLEX Clone ID\tMaster Clone ID\n");
        
        for(int i=0; i<clones.size(); i++) {
            FlexClone c = (FlexClone)clones.get(i);
            out.write(c.getCloneid()+"\t"+c.getMastercloneid()+"\n");
        }
        out.close();
    }
       
    public int getMaxNumber(String table, String column) {
        String sql = "select max("+column+") from "+table;
        
        DatabaseTransactionNoPool t = new DatabaseTransactionNoPool(plasmidurl,plasmidusername,plasmidpassword);
        ResultSet rs = null;
        int id = -2;
        try {
            rs = t.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            t.closeResultSet(rs);
        }
        
        return ++id;
    }
    
    public static void main(String args[]) throws Exception {
        String plateFileName = "G:\\plasmid\\Other\\ExpressionPlate.txt";
        String cloneFileName = "G:\\plasmid\\Other\\MissingMasterClones.txt";
        String status = "SUCCESSFUL";
        String vectorname = null;
        
        DatabaseTransactionNoPool t = new DatabaseTransactionNoPool(plasmidurl,plasmidusername,plasmidpassword);
        Connection conn = t.requestConnection();
        DatabaseTransactionNoPool flex = new DatabaseTransactionNoPool(flexurl, flexusername, flexpassword);
        Connection flexconn = flex.requestConnection();
        
        ExpressionCloneImporter imp = new ExpressionCloneImporter();
        try {
            System.out.println("Get clones from FLEX");
            List clones = imp.getFlexClones(status, vectorname);
            System.out.println("Total clones: "+clones.size());
            System.out.println("Insert clones into PlasmID");
            List clonesWithoutMastercloneid = imp.insertClones(clones, conn);
            System.out.println("Insert clones into PlasmID successful.");
            System.out.println("Update FLEX clone status.");
            List insertClones = imp.getInsertClones();
            imp.updateFlexCloneStatus(insertClones, flexconn);
            System.out.println("Update FLEX clone status successful.");
            DatabaseTransactionNoPool.commit(conn);
            DatabaseTransactionNoPool.commit(flexconn);
            System.out.println("Write plate file.");
            imp.writePlateFile(insertClones, plateFileName);
            System.out.println("Write clones file.");
            imp.writeClones(clonesWithoutMastercloneid, cloneFileName);
            System.exit(0);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransactionNoPool.rollback(conn);
            DatabaseTransactionNoPool.rollback(flexconn);
        } finally {
            DatabaseTransactionNoPool.closeConnection(conn);
            DatabaseTransactionNoPool.closeConnection(flexconn);
        }
    }
}
