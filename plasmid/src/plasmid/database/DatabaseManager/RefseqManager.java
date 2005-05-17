/*
 * RefseqManager.java
 *
 * Created on April 14, 2005, 3:04 PM
 */

package plasmid.database.DatabaseManager;

import java.sql.*;
import java.util.*;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class RefseqManager extends TableManager {
    
    /** Creates a new instance of RefseqManager */
    public RefseqManager(Connection conn) {
       super(conn);
    }
    
    public boolean insertRefseqs(List seqs) {
        if(seqs == null)
            return true;
        
        String sql = "insert into referencesequence"+
                    " (refseqid,type,name,description,cdsstart,cdsstop,species)"+
                    " values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<seqs.size(); i++) {
                Refseq c = (Refseq)seqs.get(i);
                stmt.setInt(1, c.getRefseqid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getName());
                stmt.setString(4, c.getDescription());
                stmt.setInt(5, c.getCdsstart());
                stmt.setInt(6, c.getCdsstop());
                stmt.setString(7, c.getSpecies());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into REFERENCESEQUENCE table");
            return false;
        }
        return true;
    }   
     
    public boolean insertInsertRefseqs(List seqs) {
        if(seqs == null)
            return true;
        
        String sql = "insert into insertrefseq"+
                    " (refseqid,insertid,startonrefseq,endonrefseq,hasdiscrepancy,discrepancy,comments)"+
                    " values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<seqs.size(); i++) {
                InsertRefseq c = (InsertRefseq)seqs.get(i);
                stmt.setInt(1, c.getRefseqid());
                stmt.setInt(2, c.getInsertid());
                stmt.setInt(3, c.getStart());
                stmt.setInt(4, c.getStop());
                stmt.setString(5, c.getHasDiscrepancy());
                stmt.setString(6, c.getDiscrepancy());
                stmt.setString(7, c.getComments());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into INSERTREFSEQ table");
            return false;
        }
        return true;
    }       
    
    public boolean insertRefseqNametypes(List types) {
        if(types == null)
            return true;
        
        String sql = "insert into refseqnametype"+
                    " (refseqtype,genusspecies,nametype,use)"+
                    " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<types.size(); i++) {
                RefseqNameType c = (RefseqNameType)types.get(i);
                stmt.setString(1, c.getRefseqtype());
                stmt.setString(2, c.getSpecies());
                stmt.setString(3, c.getNametype());
                stmt.setString(4, c.getUse());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into REFSEQNAMETYPE table");
            return false;
        }
        return true;
    }       
        
    public boolean insertRefseqNames(List names) {
        if(names == null)
            return true;
        
        String sql = "insert into refseqname"+
                    " (refid,nametype,namevalue,nameurl)"+
                    " values(?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<names.size(); i++) {
                RefseqName c = (RefseqName)names.get(i);
                stmt.setInt(1, c.getRefseqid());
                stmt.setString(2, c.getNametype());
                stmt.setString(3, c.getNamevalue());
                stmt.setString(4, c.getNameurl());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into REFSEQNAME table");
            return false;
        }
        return true;
    }   
    
    public List queryNameTypes(String species, String seqType, String use) {
        String sql = "select distinct nametype"+
                    " from refseqnametype"+
                    " where genusspecies=?"+
                    " and refseqtype=?"+
                    " and use in (?,?)";
        List types = new ArrayList();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, species);
            stmt.setString(2, seqType);
            stmt.setString(3, use);
            stmt.setString(4, RefseqNameType.BOTH);
            
            rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                String type = rs.getString(1);
                types.add(type);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying REFSEQNAMETYPE table.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return types;
    }
        
    public List queryNameTypes(String species, String use) {
        String sql = "select distinct nametype"+
                    " from refseqnametype"+
                    " where genusspecies=?"+
                    " and use in (?,?)";
        List types = new ArrayList();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, species);
            stmt.setString(2, use);
            stmt.setString(3, RefseqNameType.BOTH);
            
            rs = DatabaseTransaction.executeQuery(stmt);
            while(rs.next()) {
                String type = rs.getString(1);
                types.add(type);
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying REFSEQNAMETYPE table.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
        }
        
        return types;
    }
}
