/*
 * CloneInfo.java
 *
 * Created on March 26, 2003, 3:52 PM
 */

package edu.harvard.med.hip.cloneOrder.core;
import edu.harvard.med.hip.cloneOrder.database.*;
import java.sql.*;
import java.io.*;
/**
 *
 * @author  hweng
 */
public class CloneInfo {
    
    protected String flexId;
    protected int seqId;
    protected String geneSymbol;
    protected String genbankAcc;
    protected int gi;
    protected String sugenId;
    protected String cloneType;
    protected String cloneVector;
    protected String plateId;
    protected int wellIdNumber;
    protected String wellIdPosition;
    protected String sequence;
    
    /** Creates a new instance of CloneInfo */
    public CloneInfo(String plateId, String wellIdPosition, String flexId, String geneSymbol, String genbankAcc, int gi, 
        String sugenId, String cloneType) {
        this.plateId = plateId;
        this.wellIdPosition = wellIdPosition;
        this.flexId = flexId;
        this.seqId = seqId;
        this.geneSymbol = geneSymbol;
        this.genbankAcc = genbankAcc;
        this.gi = gi;
        this.sugenId = sugenId;
        this.cloneType = cloneType;
    }
    
    public CloneInfo(String geneSymbol, String sequence){
        this.geneSymbol = geneSymbol;
        this.sequence = sequence;
    }
    
    public CloneInfo(){
    }
    
    public String getPlateId(){
        return plateId;
    }
    
    public String getWellIdPosition(){
        return wellIdPosition;
    }
    
    public String getFlexId(){
        return flexId;
    }
    public String getGeneSymbol(){
        return geneSymbol;
    }
    public String getGenbankAcc(){
        return genbankAcc;
    }
    public int getGi(){
        return gi;
    }
    public String getSugenId(){
        return sugenId;
    }
    public String getCloneType(){
        return cloneType;
    }
/*
    public String getSeqFromGeneSymbol(String geneSymbol){
                
        DBManager manager = new DBManager();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return "";
        }
        
        String sql = "SELECT distinct sequence from clone_info where genesymbol = '" + geneSymbol + "'";
        String seq = "";
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);            
            while(rs.next()) {
                seq = rs.getString(1);
            }
            rs.close();
            stmt.close();
        }catch (SQLException ex) {
            System.out.println(ex);
        }finally {
            manager.disconnect(conn);            
        }
        
        String sequence = "";
        int i = 0;
        int k = seq.trim().length() / 60;
        for(i = 0; i < k; i++){
            sequence += seq.substring(60*i, 60*(i+1)) + "\n";
        }
        sequence += seq.substring(60*i, seq.trim().length());
        return sequence;
    }
    */
        
    public String getSeqFromFlexID(String flexid){
                
        DBManager2 manager = new DBManager2();
        Connection conn = manager.connect();
        
        if (conn == null) {
            System.out.println("Cannot connect to the database.");
            return "";
        }
        
        String sql = "SELECT sequence from clone_info where flexid = '" + flexid + "'";
        String seq = "";
        
        try{
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);                 
            String line;
            while(rs.next()) {
                seq = rs.getString(1);
            }
            //System.out.println(seq.length());
            rs.close();
            stmt.close();
        }catch (SQLException ex) {
            System.out.println(ex);        
        }finally {
            manager.disconnect(conn);            
        }

        if(seq != null){
            String sequence = "";
            int i = 0;
            int k = seq.trim().length() / 60;
            for(i = 0; i < k; i++){
                sequence += seq.substring(60*i, 60*(i+1)) + "\n";
            }
            sequence += seq.substring(60*i, seq.trim().length());
            return sequence;
        }
        else
            return "No sequence information available currently.";
        
    }
        
    
    public static void main(String[] args){
        CloneInfo c = new CloneInfo();
        String seq = c.getSeqFromFlexID("FLH000086.01L ");
        System.out.println(seq.trim().length());
        System.out.println("done");
        
    }
}
