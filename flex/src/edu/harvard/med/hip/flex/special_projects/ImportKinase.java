/*
 * ImportKinase.java
 *
 * Created on May 14, 2003, 1:18 PM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.io.*;
import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class ImportKinase {
    
    /** Creates a new instance of ImportKinase */
    public ImportKinase() {
    }
    
    public Container performImport(InputStreamReader input, Connection conn) throws Exception {
        int threadid = FlexIDGenerator.getID("threadid");
        String label = Container.getLabel("H", "MG", threadid, null);
        Location l = new Location(Location.CODE_FREEZER);
        Container c = new Container("96 WELL PLATE", l, label+".1", threadid);
        
        BufferedReader in = new BufferedReader(input);
        
        String line = null;
        while((line = in.readLine()) != null){
            System.out.println(line);
            StringTokenizer st = new StringTokenizer(line, "\t");
            String info[] = new String[4];
            int i = 0;
            while(st.hasMoreTokens()) {
                info[i] = st.nextToken();
                i++;
            }
            
            Sample s = new Sample(Sample.ISOLATE, Integer.parseInt(info[1]), c.getId(), Integer.parseInt(info[3]), -1, Sample.GOOD);
            c.addSample(s);
        }
        in.close();
        
        c.insert(conn);
        return c;
    }
    
    public List readCloneFile(String file) throws Exception {
        List clones = new ArrayList();
        BufferedReader in = new BufferedReader(new FileReader(file));
        
        String line = null;
        while((line = in.readLine()) != null){
            System.out.println(line);
            StringTokenizer st = new StringTokenizer(line, "!");
            
            int cloneid = Integer.parseInt((String)st.nextToken());
            String clonename = (String)st.nextToken();
            int mastercloneid = Integer.parseInt((String)st.nextToken());
            int seqid = Integer.parseInt((String)st.nextToken());
            int constructid = Integer.parseInt((String)st.nextToken());
            String genbank = (String)st.nextToken();
            String gi = (String)st.nextToken();
            String clonetype = (String)st.nextToken();
            
            String result = st.nextToken();
            if(result.equals("NA"))
                result = null;
            
            String five = st.nextToken();
            if(five.equals("NA"))
                five = null;
            
            String three = st.nextToken();
            if(three.equals("NA"))
                three = null;
            
            String sequence = (String)st.nextToken();
            CloneInfo clone = new CloneInfo(cloneid,clonename, mastercloneid, seqid, constructid, genbank, gi, clonetype, result,five, three, sequence);
            //CloneInfo clone = new CloneInfo(cloneid,clonename, mastercloneid, seqid, constructid, null, null, null, null,null, null, null);
            clones.add(clone);
        }
        in.close();
        
        return clones;
    }
    
    public void insertClones(List clones) {
        String sql = "insert into clones(cloneid,clonename,clonetype,mastercloneid,sequenceid,constructid,strategyid,status,comments)"+
        " values(?, ?, 'Master', ?, ?, ?, 4, 'SEQUENCE VERIFIED', ?)";
        String sql2 = "insert into clonesequencing values"+
        " (?, 'COMPLETE', 'Agencourt', null, sysdate, null, ?)";
        String sql3 = "insert into clonesequence values"+
        " (?, 'FULL SEQUENCE', ?, null, ?, null, null, null, ?, null, ?,?,?)";
        String sql4 = "insert into clonesequencetext(sequenceid, sequenceorder, sequencetext)\n"+
        " values(?,?,?)";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);
            stmt3 = conn.prepareStatement(sql3);
            stmt4 = conn.prepareStatement(sql4);
            
            for(int n=0; n<clones.size(); n++) {
                CloneInfo clone = (CloneInfo)clones.get(n);
                int cloneid = clone.getCloneid();
                String clonename = clone.getClonename();
                int mastercloneid = clone.getMastercloneid();
                int seqid = clone.getSeqid();
                int constructid = clone.getConstructid();
                String genbank = clone.getGenbank();
                String gi = clone.getGi();
                String clonetype = clone.getClonetype();
                String result = clone.getResult();
                String five = clone.getFive();
                String three = clone.getThree();
                String sequencetext = clone.getSequence();
                
                stmt.setInt(1, cloneid);
                stmt.setString(2, clonename);
                stmt.setInt(3, mastercloneid);
                stmt.setInt(4, seqid);
                stmt.setInt(5, constructid);
                stmt.setString(6, clonetype);
                DatabaseTransaction.executeUpdate(stmt);
                
                int sequencingid = FlexIDGenerator.getID("sequencingid");
                stmt2.setInt(1, sequencingid);
                stmt2.setInt(2, cloneid);
                DatabaseTransaction.executeUpdate(stmt2);
                
                int sequenceid = FlexIDGenerator.getID("clonesequenceid");
                stmt3.setInt(1, sequenceid);
                stmt3.setString(2, genbank);
                stmt3.setString(3, result);
                stmt3.setInt(4, sequencingid);
                stmt3.setString(5, three);
                stmt3.setString(6, five);
                stmt3.setString(7, gi);
                DatabaseTransaction.executeUpdate(stmt3);
                
                int i=0;
                while(sequencetext.length()-4000*i>4000) {
                    String text = sequencetext.substring(4000*(i), 4000*(i+1)).toUpperCase();
                    stmt4.setInt(1, sequenceid);
                    stmt4.setInt(2,  i+1);
                    stmt4.setString(3, text);
                    DatabaseTransaction.executeUpdate(stmt4);
                    
                    i++;
                }
                String text = sequencetext.substring(4000*(i));
                stmt4.setInt(1, sequenceid);
                stmt4.setInt(2,  i+1);
                stmt4.setString(3, text);
                DatabaseTransaction.executeUpdate(stmt4);
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeStatement(stmt3);
            DatabaseTransaction.closeStatement(stmt4);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void insertRejectedClones(List clones) {
        String sql = "insert into clones(cloneid,clonename,clonetype,mastercloneid,sequenceid,constructid,strategyid,status,comments)"+
        " values(?, ?, 'Master', ?, ?, ?, 4, 'NOT SEQUENCE VERIFIED', null)";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql);
            
            for(int n=0; n<clones.size(); n++) {
                CloneInfo clone = (CloneInfo)clones.get(n);
                int cloneid = clone.getCloneid();
                String clonename = clone.getClonename();
                int mastercloneid = clone.getMastercloneid();
                int seqid = clone.getSeqid();
                int constructid = clone.getConstructid();
                
                stmt.setInt(1, cloneid);
                stmt.setString(2, clonename);
                stmt.setInt(3, mastercloneid);
                stmt.setInt(4, seqid);
                stmt.setInt(5, constructid);
                DatabaseTransaction.executeUpdate(stmt);                
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public class CloneInfo {
        private int cloneid;
        private String clonename;
        private int mastercloneid;
        private int seqid;
        private int constructid;
        private String genbank;
        private String gi;
        private String clonetype;
        private String result;
        private String five;
        private String three;
        private String sequence;
        
        public CloneInfo(int cloneid, String clonename, int mastercloneid, int seqid, int constructid, String genbank, String gi, String clonetype, String result, String five, String three, String sequence) {
            this.cloneid = cloneid;
            this.clonename = clonename;
            this.mastercloneid = mastercloneid;
            this.seqid = seqid;
            this.constructid = constructid;
            this.genbank = genbank;
            this.gi = gi;
            this.clonetype = clonetype;
            this.result = result;
            this.five = five;
            this.three = three;
            this.sequence = sequence;
        }
        
        public int getCloneid() {return cloneid;}
        public String getClonename() {return clonename;}
        public int getSeqid() {return seqid;}
        public String getGenbank() {return genbank;}
        public String getGi() {return gi;}
        public String getClonetype() {return clonetype;}
        public String getFive() {return five;}
        public String getThree() {return three;}
        public String getSequence() {return sequence;}
        public int getMastercloneid() {return mastercloneid;}
        public int getConstructid() {return constructid;}
        public String getResult() {return result;}
    }
    
    public void importClone() {
    }
    
    public static void main(String [] args) {
        /**
        String file = "G:\\kinase_plate3_rearray.txt";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            FileReader input = new FileReader(file);
            FileConvert convert = new FileConvert();
            ImportKinase importer = new ImportKinase();
            Container c = importer.performImport(input, conn);
            DatabaseTransaction.commit(conn);
            System.out.println("New Container: "+c.getId());
            System.out.println("New Container Label: "+c.getLabel());
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } catch (IOException ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
      */
        String file = "G:\\kinase_clones_import.txt";
        //String file = "G:\\kinase_reject_import.txt";
        ImportKinase k = new ImportKinase();
        try {
            List clones = k.readCloneFile(file);
            k.insertClones(clones);
            //k.insertRejectedClones(clones);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        System.exit(0);
    }
}

