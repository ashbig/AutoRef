/*
 * CloneInfo.java
 *
 * Created on June 17, 2003, 2:18 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class CloneInfo extends CDNASequence {
    public static final String SEQ_VERIFIED = "SEQUENCE VERIFIED";
    public static final String NOT_SEQ_VERIFIED = "NOT SEQUENCE VERIFIED";
    public static final String IN_PROCESS = "IN PROCESS";
    public static final String SUCESSFUL = "SUCESSFUL";
    public static final String FAIL = "FAIL";
    public static final String MASTER_CLONE = "Master";
    public static final String EXPRESSION_CLONE = "Expression";
    
    protected int cloneid;
    protected String clonename;
    protected String clonetype;
    protected int mastercloneid;
    protected String comment;
    protected String status;
    protected int refsequenceid;
    protected String species;
    protected int constructid;
    protected int oligoid5p;
    protected int oligoid3p;
    protected String constructtype;
    protected int projectid;
    protected int workflowid;
    protected CloningStrategy cloningstrategy;
    protected NameInfo nameinfo;
    protected List storages;
    protected String flexstatus;
    
    protected String pubhit;
    protected String resultexpect;
    protected String resultpubhit;
    protected String matchexpect;
    protected String matchpubhit;
    protected String keyword;
    
    /** Creates a new instance of CloneInfo */
    public CloneInfo() {
    }
    
    public CloneInfo(int cdsstart, int cdsstop, String sequencetext) {
        super(cdsstart, cdsstop, sequencetext);
    }
    
    public int getCloneid() {return cloneid;}
    public String getClonename() {return clonename;}
    public String getClonetype() {return clonetype;}
    public int getMastercloneid() {return mastercloneid;}
    public String getComment() {return comment;}
    public String getStatus() {return status;}
    public int getRefsequenceid() {return refsequenceid;}
    public String getSpecies() {return species;}
    public int getConstructid() {return constructid;}
    public int getOligoid5p() {return oligoid5p;}
    public int getOligoid3p() {return oligoid3p;}
    public String getConstructtype() {return constructtype;}
    public int getProjectid() {return projectid;}
    public int getWorkflowid() {return workflowid;}
    public CloningStrategy getCloningstrategy() {return cloningstrategy;}
    public NameInfo getNameinfo() {return nameinfo;}
    public List getStorages() {return storages;}
    public String getPubhit() {return pubhit;}
    public String getResultexpect() {return resultexpect;}
    public String getResultpubhit() {return resultpubhit;}
    public String getMatchexpect() {return matchexpect;}
    public String getMatchpubhit() {return matchpubhit;}
    public String getKeyword() {return keyword;}
    public String getFlexstatus() {return flexstatus;}
    
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
    public void setClonename(String clonename) {this.clonename=clonename;}
    public void setClonetype(String clonetype) {this.clonetype=clonetype;}
    public void setMastercloneid(int mastercloneid) {this.mastercloneid = mastercloneid;}
    public void setComment(String comment) {comment = comment;}
    public void setStatus(String status) {this.status = status;}
    public void setRefsequenceid(int refsequenceid) {this.refsequenceid = refsequenceid;}
    public void setSpecies(String species) {this.species = species;}
    public void setConstructid(int constructid) {this.constructid = constructid;}
    public void setOligoid5p(int oligoid5p) {this.oligoid5p = oligoid5p;}
    public void setOligoid3p(int oligoid3p) {this.oligoid3p = oligoid3p;}
    public void setConstructtype(String constructtype) {this.constructtype = constructtype;}
    public void setProjectid(int projectid) {this.projectid = projectid;}
    public void setWorkflowid(int workflowid) {this.workflowid = workflowid;}
    public void setCloningstrategy(CloningStrategy cloningstrategy) {this.cloningstrategy = cloningstrategy;}
    public void setNameinfo(NameInfo nameinfo) {this.nameinfo = nameinfo;}
    public void setStorages(List storages) {this.storages=storages;}
    public void setPubhit(String s) {pubhit=s;}
    public void setResultexpect(String s) {resultexpect=s;}
    public void setResultpubhit(String s) {resultpubhit=s;}
    public void setMatchexpect(String s) {matchexpect=s;}
    public void setMatchpubhit(String s) {matchpubhit=s;}
    public void setKeyword(String s) {keyword=s;}
    public void setFlexstatus(String s) {this.flexstatus = s;}
    
    public void restoreClone(int id) throws Exception {
        String sql = "select c.cloneid, c.clonename, c.clonetype, c.mastercloneid, c.comments, c.status,"+
        " c.sequenceid, f.genusspecies, cd.constructid,  cd.constructtype, cd.projectid, cd.workflowid,"+
        " c.strategyid, cs.strategyname, cs.vectorname, cs.linkerid_5p, cs.linkerid_3p,"+
        " cl.pubhit, cl.resultexpect, cl.resultpubhit, cl.matchexpect, cl.matchpubhit,"+
        " l5.linkersequence, l3.linkersequence, f.flexstatus"+
        " from clones c, flexsequence f, constructdesign cd, cloningstrategy cs, clonesequencing g, clonesequence cl, linker l5, linker l3"+
        " where c.constructid=cd.constructid"+
        " and f.sequenceid=cd.sequenceid"+
        " and c.strategyid=cs.strategyid"+
        " and g.sequencingid=cl.sequencingid"+
        " and g.cloneid=c.cloneid"+
        " and cs.linkerid_5p=l5.linkerid"+
        " and cs.linkerid_3p=l3.linkerid"+
        " and c.cloneid="+id;
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            if(rs.next()) {
                cloneid = rs.getInt(1);
                clonename = rs.getString(2);
                clonetype = rs.getString(3);
                mastercloneid = rs.getInt(4);
                comment = rs.getString(5);
                status = rs.getString(6);
                refsequenceid = rs.getInt(7);
                species = rs.getString(8);
                constructid = rs.getInt(9);
                constructtype = rs.getString(10);
                projectid = rs.getInt(11);
                workflowid = rs.getInt(12);
                
                int strategyid = rs.getInt(13);
                String strategyname = rs.getString(14);
                String vectorname = rs.getString(15);
                int linkerid5p = rs.getInt(16);
                int linkerid3p = rs.getInt(17);
                
                pubhit = rs.getString(18);
                resultexpect = rs.getString(19);
                resultpubhit = rs.getString(20);
                matchexpect = rs.getString(21);
                matchpubhit = rs.getString(22);
                
                String linker5p = rs.getString(23);
                String linker3p = rs.getString(24);
                flexstatus = rs.getString(25);
                
                CloneVector cv = new CloneVector(vectorname);
                CloneLinker l5p = new CloneLinker(linkerid5p, null, linker5p);
                CloneLinker l3p = new CloneLinker(linkerid3p, null, linker3p);
                CloningStrategy st = new CloningStrategy(strategyid, strategyname, cv, l5p, l3p);
                setCloningstrategy(st);
            }
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        restoreClonestorage();
        restoreSequencetext();
    }
    
    public void restoreClonestorage() {
        List clonestorages = new ArrayList();
        String sql = "select s.storageid, s.storagesampleid, s.storagetype, s.storageform, s.cloneid,"+
        " s.storagecontainerposition, s.storagecontainerlabel, s.storagecontainerid from clonestorage s"+
        " where s.cloneid="+cloneid;
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int storageid = rs.getInt(1);
                int sampleid = rs.getInt(2);
                String storagetype = rs.getString(3);
                String storageform = rs.getString(4);
                int cloneid = rs.getInt(5);
                int position = rs.getInt(6);
                String label = rs.getString(7);
                int containerid = rs.getInt(8);
                CloneStorage storage = new CloneStorage(storageid,sampleid, storagetype, storageform, cloneid);
                storage.setPosition(position);
                storage.setLabel(label);
                storage.setContainerid(containerid);
                clonestorages.add(storage);
            }
            
            setStorages(clonestorages);
        } catch (Exception ex) {
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    public void restoreSequencetext() {
        String sql = "select sequencetext from clonesequencetext where sequenceid="+
        " (select max(sequenceid) from clonesequence c, clonesequencing cs"+
        " where c.sequencingid=cs.sequencingid"+
        " and cs.cloneid="+cloneid+") order by sequenceorder";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        String stext = "";
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                stext = stext+rs.getString(1);
            }
            setSequencetext(stext);
        } catch (Exception ex) {
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    //*******************************************************************************
    //              Internal Classes
    //*******************************************************************************
    public static class CloneidComparator implements Comparator {
        
        public CloneidComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((CloneInfo)p1).getCloneid();
            int id2 = ((CloneInfo)p2).getCloneid();
            
            if(id1 < id2)
                return -1;
            
            if(id1 == id2)
                return 0;
            
            if(id1 > id2)
                return 1;
            
            return -1;
        }
    }
    
    public static class ClonenameComparator implements Comparator {
        
        public ClonenameComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getClonename();
            String s2 = ((CloneInfo)p2).getClonename();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static class RefseqidComparator implements Comparator {
        
        public RefseqidComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            int id1 = ((CloneInfo)p1).getRefsequenceid();
            int id2 = ((CloneInfo)p2).getRefsequenceid();
            
            if(id1 < id2)
                return -1;
            
            if(id1 == id2)
                return 0;
            
            if(id1 > id2)
                return 1;
            
            return -1;
        }
    }
    
    public static class ConstructtypeComparator implements Comparator {
        
        public ConstructtypeComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getConstructtype();
            String s2 = ((CloneInfo)p2).getConstructtype();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static class GenesymbolComparator implements Comparator {
        
        public GenesymbolComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getNameinfo().getGenesymbol();
            String s2 = ((CloneInfo)p2).getNameinfo().getGenesymbol();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static class GenbankComparator implements Comparator {
        
        public GenbankComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getNameinfo().getGenbank();
            String s2 = ((CloneInfo)p2).getNameinfo().getGenbank();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static class CloneAccComparator implements Comparator {
        
        public CloneAccComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getNameinfo().getCloneAcc();
            String s2 = ((CloneInfo)p2).getNameinfo().getCloneAcc();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static class SGDComparator implements Comparator {
        
        public SGDComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getNameinfo().getSgd();
            String s2 = ((CloneInfo)p2).getNameinfo().getSgd();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static class PAnumberComparator implements Comparator {
        
        public PAnumberComparator() {
        }
        
        public int compare(Object p1, Object p2) {
            String s1 = ((CloneInfo)p1).getNameinfo().getPanumber();
            String s2 = ((CloneInfo)p2).getNameinfo().getPanumber();
            
            if(s1 == null && s2 == null)
                return 0;
            
            if(s1 == null)
                return 1;
            
            if(s2 == null)
                return -1;
            
            return s1.compareTo(s2);
        }
    }
    
    public static void main(String args[]) {
        CloneInfo c = new CloneInfo();
        try {
            //c.setCloneid(10148);
            //c.restoreClonestorage();
            c.restoreClone(10148);
            System.out.println("cloneid: "+c.getCloneid());
            System.out.println("getClonename: "+c.getClonename());
            System.out.println("getClonetype: "+c.getClonetype());
            System.out.println("getMastercloneid: "+c.getMastercloneid());
            System.out.println("getComment: "+c.getComment());
            System.out.println("getStatus: "+c.getStatus());
            System.out.println("getRefsequenceid: "+c.getRefsequenceid());
            System.out.println("getSpecies: "+c.getSpecies());
            System.out.println("getConstructtype: "+c.getConstructtype());
            System.out.println("getPubhit: "+c.getPubhit());
            System.out.println("getResultexpect: "+c.getResultexpect());
            System.out.println("getMatchexpect: "+c.getMatchexpect());
            System.out.println("getMatchpubhit: "+c.getMatchpubhit());
            List storages = c.getStorages();
            for(int i=0; i<storages.size(); i++) {
                CloneStorage s = (CloneStorage)storages.get(i);
                System.out.println("\t"+s.getStorageForm());
                System.out.println("\t"+s.getStorageType());
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
