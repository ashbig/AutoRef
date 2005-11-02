/*
 * PlateManager.java
 *
 * Created on May 23, 2005, 1:20 PM
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
public class PlateManager extends TableManager {
    
    /** Creates a new instance of PlateManager */
    public PlateManager(Connection conn) {
        super(conn);
    }
    
    public boolean insertPlateAndSample(List plates) {
        List s = new ArrayList();
        if(insertPlate(plates)) {
            for(int i=0; i<plates.size(); i++) {
                Container c = (Container)plates.get(i);
                List samples = c.getSamples();
                if(samples != null && samples.size()>0) {
                    s.addAll(samples);
                }
            }
            if(insertSample(s)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    public boolean insertPlate(List plates) {
        if(plates == null || plates.size() == 0)
            return true;
        
        String sql = "insert into containerheader(containerid,containertype,"+
        " label,oricontainerid,location,capacity,status)"+
        " values(?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<plates.size(); i++) {
                Container c = (Container)plates.get(i);
                stmt.setInt(1, c.getContainerid());
                stmt.setString(2, c.getType());
                stmt.setString(3, c.getLabel());
                stmt.setString(4, c.getOricontainerid());
                stmt.setString(5, c.getLocation());
                stmt.setInt(6, c.getCapacity());
                stmt.setString(7, c.getStatus());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into CONTAINERHEADER table");
            return false;
        }
        return true;
    }
    
    public boolean insertSample(List samples) {
        if(samples == null || samples.size() == 0)
            return true;
        
        String sql = "insert into sample(sampleid,sampletype,status_gb,cloneid,"+
        " position,positionx,positiony,containerid,containerlabel,result)"+
        " values(?,?,?,?,?,?,?,?,?,?)";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<samples.size(); i++) {
                Sample s = (Sample)samples.get(i);
                
                stmt.setInt(1, s.getSampleid());
                stmt.setString(2, s.getType());
                stmt.setString(3, s.getStatus());
                int cloneid = s.getCloneid();
                if(cloneid>0)
                    stmt.setInt(4, s.getCloneid());   
                else
                    stmt.setString(4, null);
                stmt.setInt(5, s.getPosition());
                stmt.setString(6, s.getPositionx());
                stmt.setString(7, s.getPositiony());       
                stmt.setInt(8, s.getContainerid());
                stmt.setString(9, s.getContainerlabel());
                stmt.setString(10, s.getResult());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while inserting into SAMPLE table");
            return false;
        }
        return true;
    }
    
    public boolean updatePlates(List plates, String status, String location) {
        if(plates == null || plates.size() == 0)
            return true;
        
        String sql = "update containerheader"+
        " set status=?, location=?"+
        " where containerid=?";
        
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<plates.size(); i++) {
                Container c = (Container)plates.get(i);
                stmt.setString(1, status);
                stmt.setString(2, location);
                stmt.setInt(3, c.getContainerid());
                
                DatabaseTransaction.executeUpdate(stmt);
            }
            DatabaseTransaction.closeStatement(stmt);
        } catch (Exception ex) {
            handleError(ex, "Error occured while updating CONTAINERHEADER table");
            return false;
        }
        return true;
    }
    
    public List queryContainers(List labels, boolean isSampleRestore) {
        if(labels == null)
            return null;

        List containers = new ArrayList();              
        String sql = "select containerid,containertype,oricontainerid,location,capacity,status"+
        " from containerheader where label=?";
        String sql2 = "select sampleid,sampletype,status_gb,cloneid,position,positionx,positiony,containerlabel,result"+
        " from sample where containerid=?";
        
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            
            for(int i=0; i<labels.size(); i++) {
                String label = (String)labels.get(i);
                stmt.setString(1, label);
                rs = DatabaseTransaction.executeQuery(stmt);
                if(rs.next()) {
                    int containerid = rs.getInt(1);
                    String type = rs.getString(2);
                    String oricontainerid = rs.getString(3);
                    String location = rs.getString(4);
                    int capacity = rs.getInt(5);
                    String st = rs.getString(6);
                    Container c = new Container(containerid, type, label,oricontainerid,location,capacity,st);
                    containers.add(c);
                    
                    if(isSampleRestore) {
                        stmt2 = conn.prepareStatement(sql2);
                        stmt2.setInt(1, containerid);
                        rs2 = DatabaseTransaction.executeQuery(stmt2);
                        while(rs2.next()) {
                            int sampleid = rs2.getInt(1);
                            String sampletype = rs2.getString(2);
                            String status = rs2.getString(3);
                            int cloneid = rs2.getInt(4);
                            int position = rs2.getInt(5);
                            String x = rs2.getString(6);
                            String y = rs2.getString(7);
                            String l = rs2.getString(8);
                            String result = rs2.getString(9);
                            Sample s = new Sample(sampleid, sampletype, status, cloneid, position, x, y, containerid,l);
                            s.setResult(result);
                            c.addSample(s);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Error occured while querying from CONTAINER and SAMPLE table");
            return null;
        }
        return containers;
    }
    
    public static void main(String args[]) {       
        List labels = new ArrayList();
        labels.add("HMG000406");
        labels.add("HMG000407");
        labels.add("HMG000408");
        
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            System.out.println(ex);
            System.exit(0);
        }
        
        PlateManager manager = new PlateManager(conn);
        List containers = manager.queryContainers(labels, true);
        System.out.println(containers);
    }
}
