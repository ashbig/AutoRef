/*
 * UpdateManager.java
 *
 * Created on April 18, 2006, 12:54 PM
 */

package plasmid.importexport;

import plasmid.database.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public class UpdateManager {
    
    /** Creates a new instance of UpdateManager */
    public UpdateManager() {
    }
    
    public void updateCloneid() {        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        String sql = "select s1.sampleid, s1.cloneid, s2.sampleid"+
        " from sample s1, sample s2, samplelineage sl"+
        " where s1.sampleid=sl.sampleid_from"+
        " and s2.sampleid=sl.sampleid_to"+
        " and s2.cloneid is null"+
        " and s2.sampletype='Working Glycerol'";
        String sqlUpdate = "update sample set cloneid=? where sampleid=?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sqlUpdate);
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int s = rs.getInt(1);
                int cloneid = rs.getInt(2);
                int sampleid = rs.getInt(3);
                
                if(cloneid <= 0) {
                    throw new Exception("Invalid clone for sample: "+s);
                }
                
                stmt.setInt(1, cloneid);
                stmt.setInt(2, sampleid);
                System.out.print("Update sample "+sampleid+" with clone "+cloneid+"...");
                DatabaseTransaction.executeUpdate(stmt);
                System.out.println("successful");
            }
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            System.out.println(ex);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static void main (String args[]) {
        UpdateManager manager = new UpdateManager();
        manager.updateCloneid();
    }
}
