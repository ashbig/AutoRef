/*
 * PSIManager.java
 *
 * Created on April 1, 2008, 9:13 AM
 */
package plasmid.importexport.psi;

import java.io.File;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import plasmid.database.DatabaseTransaction;

/**
 *
 * @author  DZuo
 */
public class PSIManager {

    /** Creates a new instance of PSIManager */
    public PSIManager() {
    }

    public void exportPSIclones(String file) throws Exception {
        String sql = "select n.namevalue, c.cloneid, c.clonename, v.vectorid, v.name, i.insertid, i.description, ip1.propertyvalue, ip2.propertyvalue, a.authorname " +
                "from clonename n, clone c, dnainsert i, cloneinsert ci, insertproperty ip1, insertproperty ip2, vector v, cloneauthor ca, authorinfo a " +
                "where c.cloneid=ci.cloneid " +
                "and ci.insertid=i.insertid " +
                "and i.insertid=ip1.insertid(+) " +
                "and ip1.propertytype(+)='CDS Start' " +
                "and i.insertid=ip2.insertid(+) " +
                "and ip2.propertytype(+)='CDS Stop' " +
                "and c.vectorid=v.vectorid " +
                "and c.cloneid=ca.cloneid " +
                "and ca.authorid=a.authorid " +
                "and ca.authortype='PSI Center' " +
                "and c.cloneid=n.cloneid (+) " +
                "and n.nametype(+)='TargetDB ID' ";
        String sql2 = "select seqorder,seqtext from seqtext t, dnasequence d where t.sequenceid=d.sequenceid and d.insertid=? order by seqorder";

        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        PrintWriter out = null;
        try {
            out = new PrintWriter(new File(file));
            out.println("Target ID\tClone ID\tClone Name\tVector ID\tVector Name\tDescription\tInsert Sequence\tCDS Start\tCDS Stop\tPSI Center");
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt = conn.prepareStatement(sql2);
            rs = t.executeQuery(sql);
            while (rs.next()) {
                String targetid = rs.getString(1);
                int cloneid = rs.getInt(2);
                String clonename = rs.getString(3);
                int vectorid = rs.getInt(4);
                String vectorname = rs.getString(5);
                int insertid = rs.getInt(6);
                String description = rs.getString(7);
                int start = rs.getInt(8);
                int end = rs.getInt(9);
                String author = rs.getString(10);

                stmt.setInt(1, insertid);
                rs2 = DatabaseTransaction.executeQuery(stmt);
                String seq = "";
                while (rs2.next()) {
                    int seqorder = rs2.getInt(1);
                    String seqtext = rs2.getString(2);
                    seq += seqtext;
                }
                
                if(start==0) {
                    start = 1;
                    end = seq.length();
                } 
                
                out.println(targetid+"\t"+cloneid+"\t"+clonename+"\t"+vectorid+"\t"+vectorname+"\t"+description+"\t"+seq+"\t"+start+"\t"+end+"\t"+author);
            }
            out.close();
        } catch (Exception ex) {
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static void main (String args[]) {
        String file = "G:\\dev\\test\\plasmid\\clone_for_rutgers.txt";
        PSIManager m = new PSIManager();
        try {
            m.exportPSIclones(file);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.exit(0);
    }
}
