/**
 * $Id: Location.java,v 1.2 2003-04-07 18:47:02 Elena Exp $
 *
 * File     	: Location.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 *
 * Revision     : 06-05-2001  Wendy Mar
 *                added getId(String locationType) method
 *                added main test method.
 *
 * Revision     : 07-10-2001 Wendy Mar
 *                added new constructor
 */

package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import java.sql.*;
import java.util.*;

/**
 * Represents the location object corresponding to the
 * containerlocation table.
 */
public class Location
{
    public static final String WORKBENCH = "WORKBENCH";
    public static final String DESTROYED = "DESTROYED";
    public static final String REFRIGERATOR = "REFRIGERATOR";
    public static final String FREEZER = "FREEZER";
    public static final String UNAVAILABLE = "UNAVAILABLE";
    
    public static final int    CODE_WORKBENCH = 2;
    public static final int    CODE_DESTROYED = 3;
    public static final int    CODE_REFRIGERATOR = 4;
    public static final int    CODE_FREEZER = 5;
    public static final int    CODE_UNAVAILABLE = 1;
    
    private int id;
    private String type;
    private String description;
    
    /**
     * default constructor
     */
    public Location()
    {
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
    public Location(int id, String type, String description)
    {
        this.id = id;
        this.type = type;
        this.description = description;
    }
    
    /**
     * Constructor.
     *
     * @param id The location id.
     * @return The location object.
     * @exception BecDatabaseException, BecCoreException
     */
    public Location(int id) throws BecDatabaseException, BecUtilException
    {
        this.id = id;
        restore();
    }
    
    
    /**
     * Constructor.
     *
     * @param barcode The researcher's barcode.
     * @return The Researcher object.
     * @exception BecDatabaseException, BecProcessException.
     */
    public Location(String locationName) throws BecDatabaseException, BecUtilException
    {
        this.type = locationName;
        String sql ="select * from containerlocation where locationtype = '"+locationName+"'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        ResultSet rs = t.executeQuery(sql);
        try
        {
            if(rs.next())
            {
                id = rs.getInt("LOCATIONID");
                description = rs.getString("LOCATIONDESCRIPTION");
                
            } else
            {
                throw new BecUtilException("Cannot find Location with type: "+locationName);
            }
            
        } catch(SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Return the location id.
     *
     * @return The location id.
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * @param locationType .
     *
     * @return The location id.
     */
    public int getId(String locationType) throws BecDatabaseException
    {
        int locationId = -1;
        String sql = "select locationid from containerlocation where locationtype = '" + locationType + "'";
        ResultSet rs = null;
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next())
            {
                locationId = rs.getInt(1);
            } else
            {
                return locationId;
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return locationId;
    }
    
    /**
     * Return the location type.
     *
     * @return The location type.
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * Return the description.
     *
     * @return The description.
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     * Static method to get all the locations from the database
     * and returns as a Vector of Location object.
     *
     * @return A Vector of Location object.
     * @exception BecDatabaseException.
     */
    public static ArrayList getLocations() throws BecDatabaseException
    {
        ArrayList locations = new ArrayList();
        String sql = "select * from containerlocation";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try
        {
            while (rs.next())
            {
                int id = rs.getInt("LOCATIONID");
                String type = rs.getString("LOCATIONTYPE");
                String description = rs.getString("LOCATIONDESCRIPTION");
                Location l = new Location(id, type, description);
                locations.add(l);
            }
        } catch (SQLException e)
        {
            throw new BecDatabaseException(e.getMessage());
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        return locations;
    }
    
    // Restore the data from the database by location id.
    private void restore() throws BecDatabaseException, BecUtilException
    {
        String sql = "select * from containerlocation where locationid="+id;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try
        {
            if (rs.next())
            {
                type = rs.getString("LOCATIONTYPE");
                description = rs.getString("LOCATIONDESCRIPTION");
            } else
            {
                throw new BecUtilException("No record found in the database for location id: "+id);
            }
        } catch (SQLException ex)
        {
            throw new BecDatabaseException(ex+"\nSQL: "+sql);
        }
    }
    
    //******************************************************//
    //			Testing				//
    //******************************************************//
    public static void main(String args[]) throws BecDatabaseException
    {
        //Location loc = new Location(5, "FREEZER", "");
        Location loc = new Location();
        
        int locId = loc.getId("FREEZER");
        System.out.println("The location Id for FREEZER is: " + locId);
        
        try
        {
            loc = new Location(2);
            System.out.println("Location ID: "+loc.getId());
            System.out.println("Location type: "+loc.getType());
            System.out.println("Location description: "+loc.getDescription());
        } catch (Exception e)
        {
            System.out.println(e);
        }
    } //main
}


