/*
 * CloneGeneBuilder.java
 *
 * Created on April 27, 2005, 9:17 AM
 */

package plasmid.importexport;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.util.*;

/**
 *
 * @author  DZuo
 */
public class CloneGeneBuilder {
    
    /** Creates a new instance of CloneGeneBuilder */
    public CloneGeneBuilder() {
    }
    
    public void buildDnainsertWithGene(Connection conn, int lastInsertid) throws Exception {
        String sqlUpdate = "update dnainsert set geneid=?,name=?,description=?,hasupdate='Y' where insertid=?";
 /**       String sql = "select distinct d.insertid, g.geneid, g.genesymbol, g.genename"+
        " from dnainsert d, referencesequence r, refseqname n,"+
        " sequencerecord s, gene g"+
        " where d.refseqid=r.refseqid"+
        " and r.refseqid=n.refid"+
        " and n.nametype='"+RefseqNameType.GENBANK+"'"+
        " and n.namevalue=s.accession"+
        " and s.geneid=g.geneid"+
        " and d.insertid>"+lastInsertid;
  **/        
        String sql = "select distinct d.insertid, g.geneid, g.symbol, g.description"+
        " from dnainsert d, referencesequence r, refseqname n,"+
        " sequencerecord s, gene g"+
        " where d.refseqid=r.refseqid"+
        " and r.refseqid=n.refid"+
        " and n.nametype='"+RefseqNameType.GENBANK+"'"+
        " and n.namevalue=s.accession"+
        " and s.geneid=g.geneid"+
        " and d.insertid in (select insertid from dnainsert where hasupdate='N')";
  
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt = conn.prepareStatement(sqlUpdate);
        ResultSet rs = t.executeQuery(sql);
        while(rs.next()) {
            int insertid = rs.getInt(1);
            String geneid = rs.getString(2);
            String symbol = rs.getString(3);
            String name = rs.getString(4);
            
            if("null".equals(geneid)) geneid=null;
            if("null".equals(symbol)) symbol=null;
            if("null".equals(name)) name=null;
            
            stmt.setString(1, geneid);
            stmt.setString(2, symbol);
            stmt.setString(3, name);
            stmt.setInt(4, insertid);
            DatabaseTransaction.executeUpdate(stmt);
        }
        
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public void buildDnainsertWithRefseqSymbol(Connection conn, int lastInsertid) throws Exception {
        //String sql = "select insertid from dnainsert where geneid is null and insertid>"+lastInsertid;
         
        String sql = "select insertid from dnainsert where hasupdate='N'";
        String sql2 = "select r.name"+
        " from dnainsert d, referencesequence r"+
        " where d.refseqid=r.refseqid"+
        " and d.insertid=?";
        String sql3 = "select g.geneid, g.symbol, g.description"+
        " from gene g, genesymbol s"+
        " where g.geneid=s.geneid"+
        " and s.symbol=?";
        String sql4 = "update dnainsert set geneid=?,name=?,description=?,hasupdate='Y' where insertid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        PreparedStatement stmt4 = conn.prepareStatement(sql4);
        ResultSet rs = t.executeQuery(sql);
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        while(rs.next()) {
            int insertid = rs.getInt(1);
            stmt2.setInt(1, insertid);
            rs2 = DatabaseTransaction.executeQuery(stmt2);
            if(rs2.next()) {
                String name = rs2.getString(1);
                if("null".equals(name)) name=null;
                stmt3.setString(1, name);
                
                rs3 = DatabaseTransaction.executeQuery(stmt3);
                if(rs3.next()) {
                    int geneid = rs3.getInt(1);
                    String geneSymbol = rs3.getString(2);
                    String geneName = rs3.getString(3);
                    
                    if("null".equals(geneSymbol)) geneSymbol=null;
                    if("null".equals(geneName)) geneName=null;
                    
                    stmt4.setInt(1, geneid);
                    stmt4.setString(2, geneSymbol);
                    stmt4.setString(3, geneName);
                    stmt4.setInt(4, insertid);
                    DatabaseTransaction.executeUpdate(stmt4);
                }
                DatabaseTransaction.closeResultSet(rs3);
            }
            DatabaseTransaction.closeResultSet(rs2);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
        DatabaseTransaction.closeStatement(stmt4);
    }
    
    public void buildDnainsertWithRefseq(Connection conn, int lastInsertid) throws Exception {
        //String sql = "select insertid from dnainsert where geneid is null";
        String sql = "select insertid from dnainsert where hasupdate='N'";
        if(lastInsertid > 0)
            sql += " and insertid>"+lastInsertid;
        String sql2 = "select r.name, r.description"+
        " from dnainsert d, referencesequence r"+
        " where d.refseqid=r.refseqid"+
        " and d.insertid=?";
        String sql3 = "update dnainsert set name=?,description=?,hasupdate='Y' where insertid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = t.executeQuery(sql);
        ResultSet rs2 = null;
        while(rs.next()) {
            int insertid = rs.getInt(1);
            List names = new ArrayList();
            List descriptions = new ArrayList();
            stmt2.setInt(1, insertid);
            rs2 = DatabaseTransaction.executeQuery(stmt2);
            while(rs2.next()) {
                String name = rs2.getString(1);
                String description = rs2.getString(2);
                names.add(name);
                descriptions.add(description);
            }
            DatabaseTransaction.closeResultSet(rs2);
            StringConvertor convertor = new StringConvertor();
            String genename = convertor.convertFromListToString(names);
            String desc = convertor.convertFromListToString(descriptions);
            
            if("null".equals(genename)) genename=null;
            if("null".equals(desc)) desc=null;
            
            stmt3.setString(1, genename);
            stmt3.setString(2, desc);
            stmt3.setInt(3, insertid);
            DatabaseTransaction.executeUpdate(stmt3);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
      
    public void buildDnainsertWithTargetGenbank(Connection conn, int lastInsertid) throws Exception {
       /** String sql = "select insertid from dnainsert where geneid is null";
        if(lastInsertid > 0)
            sql += " and insertid>"+lastInsertid;
        **/
        String sql = "select insertid from dnainsert where hasupdate='N'";
        String sql2 = "select g.geneid, g.symbol, g.description"+
        " from dnainsert d, gene g, sequencerecord r"+
        " where d.targetgenbank=r.accession"+
        " and r.geneid=g.geneid"+
        " and d.insertid=?";
        
        String sql3 = "update dnainsert set geneid=?, name=?,description=?,hasupdate='Y' where insertid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = t.executeQuery(sql);
        ResultSet rs2 = null;
        while(rs.next()) {
            int insertid = rs.getInt(1);
            stmt2.setInt(1, insertid);
            rs2 = DatabaseTransaction.executeQuery(stmt2);
            if(rs2.next()) {
                String geneid = rs2.getString(1);
                String name = rs2.getString(2);
                String description = rs2.getString(3);
                
                if("null".equals(geneid)) geneid=null;
                if("null".equals(name)) name = null;
                if("null".equals(description)) description = null;
                
                stmt3.setString(1, geneid);
                stmt3.setString(2, name);
                stmt3.setString(3, description);
                stmt3.setInt(4, insertid);
                DatabaseTransaction.executeUpdate(stmt3);
            }
            DatabaseTransaction.closeResultSet(rs2);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
      
    public void buildCloneGenbank(Connection conn, int lastCloneid) throws Exception {
        String sql = "insert into clonegenbank(cloneid, accession)"+
        " select distinct c.cloneid, s.accession"+
        " from clone c, cloneinsert i, dnainsert d, sequencerecord s"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.geneid=s.geneid"+
        " and d.hasupdate='Y'";
        if(lastCloneid>0)
            sql += " and c.cloneid>"+lastCloneid;
        DatabaseTransaction.executeUpdate(sql, conn);
        
        sql = "select c.cloneid, n.namevalue"+
        " from clone c, cloneinsert i, dnainsert d, referencesequence r, refseqname n"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.refseqid=r.refseqid"+
        " and r.refseqid=n.refid"+
        " and d.hasupdate='Y'"+
        " and n.nametype='"+RefseqNameType.GENBANK+"'"+
        " and c.cloneid>"+lastCloneid;
        String sql2 = "select * from clonegenbank where cloneid=? and accession=?";
        String sql3 = "insert into clonegenbank(cloneid, accession) values(?,?)";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = t.executeQuery(sql);
        while(rs.next()) {
            int cloneid = rs.getInt(1);
            String accession = rs.getString(2);
            
            stmt2.setInt(1, cloneid);
            stmt2.setString(2, accession);
            ResultSet rs2 = DatabaseTransaction.executeQuery(stmt2);
            if(!rs2.next()) {
                stmt3.setInt(1, cloneid);
                stmt3.setString(2, accession);
                DatabaseTransaction.executeUpdate(stmt3);
            }
            
            DatabaseTransaction.closeResultSet(rs2);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
        
    public void buildCloneGi(Connection conn, int lastCloneid) throws Exception {
        String sql = "insert into clonegi(cloneid, gi)"+
        " select distinct c.cloneid, s.gi"+
        " from clone c, cloneinsert i, dnainsert d, sequencerecord s"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.hasupdate='Y'"+
        " and d.geneid=s.geneid";
        if(lastCloneid>0)
            sql += " and c.cloneid>"+lastCloneid;
        DatabaseTransaction.executeUpdate(sql, conn);
        
        sql = "select c.cloneid, n.namevalue"+
        " from clone c, cloneinsert i, dnainsert d, referencesequence r, refseqname n"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.refseqid=r.refseqid"+
        " and r.refseqid=n.refid"+
        " and d.hasupdate='Y'"+
        " and n.nametype='"+RefseqNameType.GI+"'"+
        " and c.cloneid>"+lastCloneid;
        String sql2 = "select * from clonegi where cloneid=? and gi=?";
        String sql3 = "insert into clonegi(cloneid, gi) values(?,?)";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = t.executeQuery(sql);
        while(rs.next()) {
            int cloneid = rs.getInt(1);
            String gi = rs.getString(2);
            
            stmt2.setInt(1, cloneid);
            stmt2.setString(2, gi);
            ResultSet rs2 = DatabaseTransaction.executeQuery(stmt2);
            if(!rs2.next()) {
                stmt3.setInt(1, cloneid);
                stmt3.setString(2, gi);
                DatabaseTransaction.executeUpdate(stmt3);
            }
            
            DatabaseTransaction.closeResultSet(rs2);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
    
    public void buildCloneSymbol(Connection conn, int lastCloneid) throws Exception {
        String sql = "insert into clonesymbol(cloneid, symbol)"+
        " select distinct c.cloneid, s.symbol"+
        " from clone c, cloneinsert i, dnainsert d, genesymbol s"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.hasupdate='Y'"+
        " and d.geneid=s.geneid";
        if(lastCloneid>0)
            sql += " and c.cloneid>"+lastCloneid;
        DatabaseTransaction.executeUpdate(sql, conn);
        
        sql = "select c.cloneid, r.name"+
        " from clone c, cloneinsert i, dnainsert d, referencesequence r"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.refseqid=r.refseqid"+
        " and d.hasupdate='Y'"+
        " and r.name is not null"+
        " and c.cloneid>"+lastCloneid;
        String sql2 = "select * from clonesymbol where cloneid=? and symbol=?";
        String sql3 = "insert into clonesymbol(cloneid, symbol) values(?,?)";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        PreparedStatement stmt2 = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = t.executeQuery(sql);
        while(rs.next()) {
            int cloneid = rs.getInt(1);
            String accession = rs.getString(2);
            
            stmt2.setInt(1, cloneid);
            stmt2.setString(2, accession);
            ResultSet rs2 = DatabaseTransaction.executeQuery(stmt2);
            if(!rs2.next()) {
                stmt3.setInt(1, cloneid);
                stmt3.setString(2, accession);
                DatabaseTransaction.executeUpdate(stmt3);
            }
            
            DatabaseTransaction.closeResultSet(rs2);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt2);
        DatabaseTransaction.closeStatement(stmt3);
    }
    
    public void buildCloneLocus(Connection conn, int lastCloneid) throws Exception {
        String sql = "insert into clonegene(cloneid, geneid)"+
        " select distinct c.cloneid, g.geneid"+
        " from clone c, cloneinsert i, dnainsert d, gene g"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.hasupdate='Y'"+
        " and d.geneid=g.geneid";
        if(lastCloneid>0)
            sql += " and c.cloneid>"+lastCloneid;
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public void buildCloneLocusWithSpecies(Connection conn, String species) throws Exception {
        String sql = "insert into clonegene(cloneid, geneid)"+
        " select distinct c.cloneid, d.geneid"+
        " from clone c, cloneinsert i, dnainsert d"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.species='"+species+"'";
        DatabaseTransaction.executeUpdate(sql, conn);
        
        sql = "insert into clonesymbol(cloneid, symbol)"+
        " select distinct c.cloneid, d.name"+
        " from clone c, cloneinsert i, dnainsert d"+
        " where c.cloneid=i.cloneid"+
        " and i.insertid=d.insertid"+
        " and d.species='"+species+"'";
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public void truncateTables(Connection conn) throws Exception {
        String sql= "truncate table clonegenbank";
        DatabaseTransaction.executeUpdate(sql, conn);
        sql= "truncate table clonegi";
        DatabaseTransaction.executeUpdate(sql, conn);
        sql= "truncate table clonesymbol";
        DatabaseTransaction.executeUpdate(sql, conn);
        sql= "truncate table clonegene";
        DatabaseTransaction.executeUpdate(sql, conn);
    }
    
    public static void main(String args[]) {
        int lastInsertid = 0;
        int lastCloneid = 0;
        DatabaseTransaction t = null;
        Connection conn = null;
        CloneGeneBuilder builder = new CloneGeneBuilder();
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
         
            System.out.println("Building DNAINSERT with target genbank");
            builder.buildDnainsertWithTargetGenbank(conn, lastInsertid);
            DatabaseTransaction.commit(conn);
            System.out.println("Building DNAINSERT with genes");
            builder.buildDnainsertWithGene(conn, lastInsertid);
            DatabaseTransaction.commit(conn);
            System.out.println("Building DNAINSERT with refseq symbol");
            builder.buildDnainsertWithRefseqSymbol(conn, lastInsertid);
            System.out.println("Building DNAINSERT with reference sequences");
            builder.buildDnainsertWithRefseq(conn, lastInsertid);
            DatabaseTransaction.commit(conn);
        
            //System.out.println("Truncate tables");
            //builder.truncateTables(conn);
            //System.out.println("Building CLONEGENBANK");
            //builder.buildCloneGenbank(conn, lastCloneid);
            //System.out.println("Building CLONELOCUS");
            //builder.buildCloneLocus(conn, lastCloneid);
            //System.out.println("Building CLONESYMBOL");
            //builder.buildCloneSymbol(conn, lastCloneid);
            //System.out.println("Building CLONEGI");
            //builder.buildCloneGi(conn, lastCloneid);
            
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
