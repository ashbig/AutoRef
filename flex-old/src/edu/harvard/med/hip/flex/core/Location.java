/**
 * $Id: Location.java,v 1.2 2001-07-09 16:00:57 jmunoz Exp $
 *
 * File     	: Location.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 *
 * Revision     : 06-05-2001  Wendy Mar
 *                added getId(String locationType) method
 *                added main test method.
 */

package edu.harvard.med.hip.flex.core;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.Vector;

/**
 * Represents the location object corresponding to the
 * containerlocation table.
 */
public class Location {
    public static final String WORKBENCH = "WORKBENCH";
    public static final String DESTROYED = "DESTROYED";
    public static final String REFRIGERATOR = "REFRIGERATOR";
    public static final String FREEZER = "FREEZER";
    public static final String UNAVAILABLE = "UNAVAILABLE";
    
    private int id;
    private String type;
    private String description;
    
    /**
     * default constructor
     */
    public Location() {
    }
    
    /**
     * Constructor.
     *
     * @param id The location id.
     * @param type The location type.
     * @param description The location description.
     *
     * @return The location object.
     */
    public Location(int id, String type, String description) {
        this.id = id;
        this.type = type;
        this.description = description;
    }
    
    /**
     * Constructor.
     *
     * @param id The location id.
     * @return The location object.
     * @exception FlexDatabaseException, FlexCoreException
     */
    public Location(int id) throws FlexDatabaseException, FlexCoreException {
        this.id = id;
        restore();
    }
    
    /**
     * Return the location id.
     *
     * @return The location id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * @param locationType .
     *
     * @return The location id.
     */
    public int getId(String locationType) throws FlexDatabaseException {
        int locationId = -1;
        String sql = "select locationid from containerlocation where locationtype = '" + locationType + "'";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                locationId = rs.getInt(1);
            } else {
                return locationId;
            }     
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return locationId;
    }
    
    /**
     * Return the location type.
     *
     * @return The location type.
     */
    public String getType() {
        return type;
    }

    /**
     * Return the description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Static method to get all the locations from the database
     * and returns as a Vector of Location object.
     *
     * @return A Vector of Location object.
     * @exception FlexDatabaseException.
     */
    public static Vector getLocations() throws FlexDatabaseException {
        Vector locations = new Vector();
        String sql = "select * from containerlocation";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                int id = rs.getInt("LOCATIONID");
                String type = rs.getString("LOCATIONTYPE");
                String description = rs.getString("LOCATIONDESCRIPTION");
                Location l = new Location(id, type, description);
                locations.addElement(l);
            }  
        } catch (SQLException e) {
            throw new FlexDatabaseException(e.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
       return locations;
    }
    
    // Restore the data from the database by location id.
    private void restore() throws FlexDatabaseException, FlexCoreException {
        String sql = "select * from containerlocation where locationid="+id;

        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try {
            if (rs.next()) {
                type = rs.getString("LOCATIONTYPE");
                description = rs.getString("LOCATIONDESCRIPTION");
            } else {
                throw new FlexCoreException("No record found in the database for location id: "+id);
            } 
        } catch (SQLException ex) {
            throw new FlexDatabaseException(ex+"\nSQL: "+sql);
        }
    }
        
    //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[]) throws FlexDatabaseException {
        //Location loc = new Location(5, "FREEZER", "");
        Location loc = new Location();
        
        int locId = loc.getId("FREEZER"); 
        System.out.println("The location Id for FREEZER is: " + locId);
       
        try {
            loc = new Location(2);
            System.out.println("Location ID: "+loc.getId());
            System.out.println("Location type: "+loc.getType());
            System.out.println("Location description: "+loc.getDescription());
        } catch (Exception e) {
            System.out.println(e);
        }
    } //main
}


