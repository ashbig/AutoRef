/*
 * InstitutionUpdater.java
 *
 * Created on July 24, 2008, 12:20 PM
 */

package plasmid;

import plasmid.database.*;
import plasmid.coreobject.User;

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author  DZuo
 */
public class InstitutionUpdater {
    
    /** Creates a new instance of InstitutionUpdater */
    public InstitutionUpdater() {
    }
    
    public List updateWithPI() throws Exception {
        //String sql = "select p.institution, u.userid, u.piname from userprofile u, pi p where u.piname=p.name and u.piemail=p.email";
        String sql = "select p.institution, u.userid, u.piname from userprofile u, pi p where u.piname=p.name and u.piemail=p.email and u.userid in" +
        "(100,105,119,123,126,142,147,167,168,180,192,196,198,29,10,30,54,56,401,406,412,439,441,445,446,451,456,457,473,480,483,225,235,244,254,282,298,304,307,342,368,378,379,570,581,587,610,614,762,772,775,780,807,826,827,846,671,673,712,886,920,1075,1089,912,938,857,1083,1106,1114,865,872,1138,1167,1029,1163,1164,1180,1279,1297,1331,1431,1322,1392,1190,1196,1274,1301,1418,1442,1226,1355,1458,1463,1263,1305,1364,1440,1251,1321,1346,1371,1384,1456,1470,1474,1476,1202,1247,1309,1373,1397,195,58,59,81,82,388,393,440,489,513,533,280,294,331,360,590,765,783,788,808,824,838,840,665,668,694,890,1036,950,976,1065,1161,1171,1153,1360,1439,1357,1469,1286,1350,1207,1362,1455,1192,1327,1395)";
        return update(sql);
    }
    
    public List updateWithUser() throws Exception {
        //String sql = "select institution, userid, piname from userprofile where piname is null";
        String sql = "select institution, userid, piname from userprofile where piname is null and userid in" +
        "(100,105,119,123,126,142,147,167,168,180,192,196,198,29,10,30,54,56,401,406,412,439,441,445,446,451,456,457,473,480,483,225,235,244,254,282,298,304,307,342,368,378,379,570,581,587,610,614,762,772,775,780,807,826,827,846,671,673,712,886,920,1075,1089,912,938,857,1083,1106,1114,865,872,1138,1167,1029,1163,1164,1180,1279,1297,1331,1431,1322,1392,1190,1196,1274,1301,1418,1442,1226,1355,1458,1463,1263,1305,1364,1440,1251,1321,1346,1371,1384,1456,1470,1474,1476,1202,1247,1309,1373,1397,195,58,59,81,82,388,393,440,489,513,533,280,294,331,360,590,765,783,788,808,824,838,840,665,668,694,890,1036,950,976,1065,1161,1171,1153,1360,1439,1357,1469,1286,1350,1207,1362,1455,1192,1327,1395)";
        return update(sql);
    }
    
    public List update(String sql) throws Exception {
        String sql2 = "select standard from standardinstitution where nonstandard=?";
        String sql3 = "update userprofile set institution=? where userid=?";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql2);
        PreparedStatement stmt3 = conn.prepareStatement(sql3);
        ResultSet rs = t.executeQuery(sql);
        List users = new ArrayList();
        while(rs.next()) {
            String institution = rs.getString(1);
            int userid = rs.getInt(2);
            String pi = rs.getString(3);
            if(institution==null || institution.trim().length()<1) {
                User u = new User();
                u.setUserid(userid);
                u.setInstitution(institution);
                u.setPiname(pi);
                users.add(u);
                continue;
            }
            
            stmt.setString(1, institution.trim());
            ResultSet rs2 = DatabaseTransaction.executeQuery(stmt);
            if(rs2.next()) {
                String standard = rs2.getString(1);
                if(standard==null || standard.trim().length()<1) {
                    User u = new User();
                    u.setUserid(userid);
                    u.setInstitution(institution);
                    u.setPiname(pi);
                    users.add(u);
                } else {
                    stmt3.setString(1, standard.trim());
                    stmt3.setInt(2, userid);
                    DatabaseTransaction.executeUpdate(stmt3);
                }
            } else {
                User u = new User();
                u.setUserid(userid);
                u.setInstitution(institution);
                u.setPiname(pi);
                users.add(u);
            }
            DatabaseTransaction.closeResultSet(rs2);
        }
        DatabaseTransaction.closeStatement(stmt3);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeConnection(conn);
        
        return users;
    }
    
    public void writeFile(List users, String filename) throws Exception {
        OutputStreamWriter out = new FileWriter(filename);
        for(int i=0; i<users.size(); i++) {
            User user = (User)users.get(i);
            out.write(user.getUserid()+"\t"+user.getInstitution()+"\t"+user.getPiname()+"\n");
        }
        out.close();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String file = "G:\\dev\\test\\plasmid\\institution.txt";
        
        InstitutionUpdater updater = new InstitutionUpdater();
        try {
            List users = updater.updateWithPI();
            users.addAll(updater.updateWithUser());
            updater.writeFile(users, file);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}
