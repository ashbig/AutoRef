/*
 * ChechYeastMapping.java
 *
 * Created on June 24, 2003, 11:02 AM
 */

package edu.harvard.med.hip.flex.special_projects;

import java.util.*;
import java.io.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.utility.Logger;
import edu.harvard.med.hip.flex.util.*;

/**
 *
 * @author  dzuo
 */
public class ChechYeastMapping {
    
    /** Creates a new instance of ChechYeastMapping */
    public ChechYeastMapping() {
    }
    
    public List readFile(String file) {
        String line;
        List clones = new ArrayList();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            
            while((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                
                try {
                    if(st.hasMoreTokens()) {
                        String orf = (String)st.nextToken();
                        String label = (String)st.nextToken();
                        int well = Integer.parseInt((String)st.nextToken());
                        String destLabel = (String)st.nextToken();
                        int destWell = Integer.parseInt((String)st.nextToken());
                        
                        OrfInfo clone = new OrfInfo(orf, label, well, destLabel, destWell);
                        clones.add(clone);
                        
                    }
                } catch (NoSuchElementException ex) {
                    System.out.println(ex);
                }
            }
            in.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return clones;
    }
    
    public void updateSamples(List clones) {
        String sql = "select c.containerid from containerheader c, sample s"+
        " where c.containerid=s.containerid"+
        " and c.label=?"+
        " and s.containerposition=?";
        String sql2 = "insert into sample values"+
        " (?,'EMPTY', ?, ?, null, null, 'G', null)";
        String sql3 = "insert into containercell values (?, ?, ?)";
        String sql4 = "select containerid from containerheader where label=?";
        
        DatabaseTransaction t = null;
        Connection c = null;
        PreparedStatement st = null;
        PreparedStatement st2 = null;
        PreparedStatement st3 = null;
        PreparedStatement st4 = null;
        ResultSet rs = null;
        ResultSet rs4 = null;
        int count=1;
        try {
            t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            st = c.prepareStatement(sql);
            st2 = c.prepareStatement(sql2);
            st3 = c.prepareStatement(sql3);
            st4 = c.prepareStatement(sql4);
            
            for(int i=0; i<clones.size(); i++) {
                OrfInfo clone = (OrfInfo)clones.get(i);
                String label = clone.getLabel();
                int well = clone.getWell();
                st.setString(1, label);
                st.setInt(2, well);
                rs = DatabaseTransaction.executeQuery(st);
                if(!rs.next()) {
                    st4.setString(1, label);
                    rs4 = DatabaseTransaction.executeQuery(st4);
                    if(rs4.next()) {
                        int id = rs4.getInt(1);
                        int sampleid = FlexIDGenerator.getID("SAMPLEID");
                        st2.setInt(1, sampleid);
                        st2.setInt(2, id);
                        st2.setInt(3, well);
                        DatabaseTransaction.executeUpdate(st2);
                        st3.setInt(1, id);
                        st3.setInt(2, well);
                        st3.setInt(3, sampleid);
                        DatabaseTransaction.executeUpdate(st3);
                        System.out.println(count+": update: "+id+"\t"+label+"\t"+well);
                        count++;
                    } else { throw new Exception("Cannot find record.");}
                }
            }
            DatabaseTransaction.commit(c);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(c);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs4);
            DatabaseTransaction.closeStatement(st);
            DatabaseTransaction.closeStatement(st2);
            DatabaseTransaction.closeStatement(st3);
            DatabaseTransaction.closeStatement(st4);
            DatabaseTransaction.closeConnection(c);
        }
    }
    
    public void checkOrf(List orfs) throws Exception {
        String logfile = "G:\\yeast_orf_wrong.txt";
        String outfile = "G:\\yeast_orf_dup.txt";
        
        String sql = "select n.namevalue from containerheader c, sample s, constructdesign cd, name n"+
        " where c.containerid=s.containerid"+
        " and s.constructid=cd.constructid"+
        " and cd.sequenceid=n.sequenceid"+
        " and n.nametype='SGD'"+
        " and c.label=?"+
        " and s.containerposition=?";
        
        Logger logger = new Logger(logfile);
        logger.start();
        Logger out = new Logger(outfile);
        out.start();
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        
        String last = null;
        for(int i=0; i<orfs.size(); i++) {
            OrfInfo info = (OrfInfo)orfs.get(i);
            String label = info.getLabel();
            int well = info.getWell();
            String orf = info.getOrf();
            String destLabel = info.getDestLabel();
            int destWell = info.getDestWell();
            stmt.setString(1, label);
            stmt.setInt(2, well);
            rs = DatabaseTransaction.executeQuery(stmt);
            
            String newOrf = null;
            if(rs.next()) {
                newOrf = rs.getString(1);
                
                if(last != null && newOrf.trim().equals(last.trim())) {
                    out.logging(orf);
                }
                last = newOrf;
                
                if(!newOrf.trim().equals(orf.trim())) {
                    logger.logging(orf+"\t"+label+"\t"+well+"\t"+newOrf);
                    System.out.println(orf+"\t"+label+"\t"+well);
                }
            }
        }
        //DatabaseTransaction.closeResultSet(rs);
        //DatabaseTransaction.closeStatement(stmt);
        //DatabaseTransaction.closeConnection(conn);
        logger.end();
        out.end();
    }
    
    public void checkMap(List orfs) throws Exception {
        String logfile = "G:\\yeast_map_wrong.txt";
        String outfile = "G:\\yeast_map_dup.txt";
        
        String sql = "select c.containerid, s.containerposition"+
        " from containerheader c, sample s"+
        " where c.containerid=s.containerid"+
        " and c.label like 'EPL%'"+
        " and s.constructid in (select constructid from constructdesign where sequenceid in"+
        "(select sequenceid from name where nametype='SGD' and namevalue=?))";

        Logger logger = new Logger(logfile);
        logger.start();
        Logger out = new Logger(outfile);
        out.start();
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        String last = null;
        for(int i=0; i<orfs.size(); i++) {
            OrfInfo info = (OrfInfo)orfs.get(i);
            String label = info.getLabel();
            int well = info.getWell();
            String orf = info.getOrf();
            String destLabel = info.getDestLabel();
            int destWell = info.getDestWell();
            
            if(last != null && orf.trim().equals(last.trim())) {
                out.logging(orf+"\t"+label+"\t"+well+"\t"+destLabel+"\t"+destWell);
            }
            last = orf;
            
            stmt.setString(1, orf);
            rs = DatabaseTransaction.executeQuery(stmt);
            
            boolean found = false;
            String newLabel = null;
            int newWell = 0;
            
            while(rs.next()) {
                newLabel = rs.getString(1).trim();
                newWell = rs.getInt(2);
                if(label.trim().equals(newLabel) && well == newWell) {
                    found = true;
                    //out.logging(orf+"\t"+label+"\t"+well+"\t"+destLabel+"\t"+destWell);
                    break;
                }
            }
            if(!found) {
                logger.logging(orf+"\t"+label+"\t"+well+"\t"+newLabel+"\t"+newWell);
                System.out.println(orf+"\t"+label+"\t"+well);
                //out.logging(orf+"\t"+newLabel+"\t"+newWell+"\t"+destLabel+"\t"+destWell);
            }
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
        logger.end();
        out.end();
    }
    
    public class OrfInfo {
        private String orf;
        private String label;
        private int well;
        private String destLabel;
        private int destWell;
        
        public OrfInfo(String orf, String label, int well, String destLabel, int destWell) {
            this.orf = orf;
            this.label = label;
            this.well = well;
            this.destLabel = destLabel;
            this.destWell = destWell;
        }
        
        public String getOrf() {return orf;}
        public String getLabel() {return label;}
        public int getWell() {return well;}
        public String getDestLabel() {return destLabel;}
        public int getDestWell() {return destWell;}
    }
    
    public static void main(String args[]) {
        String file = "G:\\yeast_1c.txt";
        
        try {
            ChechYeastMapping map = new ChechYeastMapping();
            List orfs = map.readFile(file);
            map.checkOrf(orfs);
           // map.checkMap(orfs);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        //map.updateSamples(orfs);
    }
}
