/*
 * CloneImporter.java
 *
 * Created on October 2, 2002, 3:56 PM
 */

package edu.harvard.med.hip.flex.infoimport;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import java.util.*;
import java.io.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class CloneImporter {
    private ArrayList clones = null;
    private String message = null;
    
    /** Creates new ImportCloneInfo */
    public CloneImporter() {
    }
    
    public boolean readFile(String file) {
        clones = new ArrayList();
        String line;
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String ignore = in.readLine();
            
            while((line = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, "\t");
                
                try {
                    if(st.hasMoreTokens()) {
                        int sampleid = Integer.parseInt((String)st.nextToken());
                        int sequenceid = Integer.parseInt((String)st.nextToken());
                        String pubhit = (String)st.nextToken();
                        
                        int remove = pubhit.indexOf(".");
                        if(remove != -1) {
                            pubhit = pubhit.substring(0, remove);
                        }
                        
                        String result = null;
                        if(st.hasMoreTokens()) {
                            result = (String)st.nextToken();
                        }
                        Clone clone = new Clone(pubhit, result, sequenceid, sampleid);
                        clones.add(clone);
                        
                    }
                } catch (NoSuchElementException ex) {
                    setMessage(ex.getMessage());
                    return false;
                }
            }
            in.close();
        } catch (Exception ex) {
            setMessage(ex.getMessage());
            return false;
        }
        
        return true;
    }
    
    public boolean performImport(String file, DatabaseTransaction dt) {
        if(!readFile(file)) {
            setMessage(getMessage());
            return false;
        }
        
        if(clones.isEmpty()) {
            return true;
        }
        
        String sql = "insert into clone values(?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dt.requestConnection();
            stmt = conn.prepareStatement(sql);
            for (int i=0; i<clones.size(); i++) {
                Clone c = (Clone)(clones.get(i));
                int cloneid = getCloneid(dt);
                if(cloneid == -1) {
                    setMessage("Cannot get the next clone id from database.");
                    return false;
                }
                
                String cloneName = getCloneName(dt, c.getSampleid(), cloneid);
                stmt.setInt(1, cloneid);
                stmt.setString(2, cloneName);
                stmt.setString(3, c.getPubhit());
                stmt.setString(4, c.getResult());
                stmt.setInt(5, c.getSequenceid());
                stmt.setInt(6, c.getSampleid());
                dt.executeUpdate(stmt);
            }
        } catch (FlexDatabaseException ex) {
            dt.rollback(conn);
            setMessage(ex.getMessage());
            return false;
        } catch (SQLException ex) {
            dt.rollback(conn);
            setMessage(ex.getMessage());
            return false;
        } finally {
            dt.closeStatement(stmt);
            dt.closeConnection(conn);
        }
        
        dt.commit(conn);
        return true;
    }
    
    public ArrayList getClones() {
        return clones;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    private int getCloneid(DatabaseTransaction dt) {
        String sql = "select cloneid.nextval from dual";
        RowSet rs = null;
        int id = -1;
        try {
            rs = dt.executeQuery(sql);
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            dt.closeResultSet(rs);
        }
        
        return id;
    }
    
    private String getCloneName(DatabaseTransaction dt, int sampleid, int cloneid) {
        String sql = "select c.constructtype, pw.code"+
                    " from constructdesign c, sample s, project p, projectworkflow pw"+
                    " where c.constructid=s.constructid"+
                    " and c.projectid=p.projectid"+
                    " and p.projectid=pw.projectid"+
                    " and s.sampleid="+sampleid;
        RowSet rs = null;
        String type = null;
        String code = null;
        String name = null;
        try {
            rs = dt.executeQuery(sql);
            if(rs.next()) {
                type = rs.getString(1);
                code = rs.getString(2);
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            return null;
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            dt.closeResultSet(rs);
        }
        
        if(Construct.FUSION.equals(type)) {
            type = "F";
        } else {
            type = "C";
        }
        
        java.text.NumberFormat fmt = java.text.NumberFormat.getInstance();
        fmt.setMaximumIntegerDigits(6);
        fmt.setMinimumIntegerDigits(6);
        fmt.setGroupingUsed(false);
        name = code+"CL"+type+fmt.format(cloneid);
        
        return name;
    }
    
    public static void main(String arg[]) {
        String file = "E:\\flexDev\\cloneInfo.txt";
        DatabaseTransaction dt = null;
        
        try {
            dt = DatabaseTransaction.getInstance();
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        }
        
        System.out.println("Start import...");
        CloneImporter importer = new CloneImporter();
        if(importer.performImport(file, dt)) {
            System.out.println("Import successful!");
        }
        else {
            System.out.println("Import failed because: ");
            System.out.println(importer.getMessage());
        }
        System.exit(0);
    }
}
