/*
 * CDNALibrary.java
 *
 * This class mapps to CDNALIBRARY table in the database.
 *
 * Created on July 11, 2001, 5:13 PM
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.Vector;

/**
 *
 * @author  dzuo
 * @version 
 */
public class CDNALibrary {
    private int id;
    private String description;
    
    /** Creates new CDNALibrary 
     * 
     * @param id The cdnalibray id.
     * @param description The cdnalibrary description.
     * @return The CDNALibrary object.
     */
    public CDNALibrary(int id, String description) {
        this.id = id;
        this.description = description;
    }

    /**
     * Constructor.
     *
     * @param id The cdnalibrary id.
     * @return The CDNALibrary object.
     */
     public CDNALibrary(int id) {
        String sql = "select * from cdnalibrary where cdnalibraryid="+id;
        ResultSet rs = null;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                this.id = rs.getInt(1);
                this.description = rs.getString(2);               
            }  
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
     }
     
    /**
     * Set the library id.
     *
     * @param id The value to be set to.
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Return the library id.
     *
     * @return The library id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Set the library description.
     *
     * @param description The value to be set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Return the library description.
     *
     * @return The library description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets all the libraries from the database.
     *
     * @return All the libraries from the database.
     * @exception FlexDatabaseException.
     */
    public static Vector getAllLibraries() throws FlexDatabaseException {
        Vector v = new Vector();
        String sql = "select * from cdnalibrary";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                int id = rs.getInt(1);
                String description = rs.getString(2);
                CDNALibrary l = new CDNALibrary(id, description);
                v.addElement(l);
            }  
        } catch (SQLException e) {
            throw new FlexDatabaseException(e.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return v;
    }
    
    public static void main(String [] args) {
        try {
            Vector v = CDNALibrary.getAllLibraries();
            
            for(int i=0; i<v.size(); i++) {
                CDNALibrary l = (CDNALibrary)v.elementAt(i);
                System.out.println(l.getId()+"\t"+l.getDescription());
            }
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }      
        
        CDNALibrary l = new CDNALibrary(2);
        System.out.println(l.getId()+"\t"+l.getDescription());
    }
}
