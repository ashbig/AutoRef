/**
 * A representation of a set of three oligo plates:
 * 5p, 3pfusion and 3pclosed.
 *
 * @author  Wendy Mar
 * @date    05-29-2001
 * @file    Plateset.java
 * @version
 *
 * modified 07-01-2001 wmar:    added new constructor
 */

package edu.harvard.med.hip.flex.core;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import java.sql.*;


public class Plateset {
    
    protected int id;
    protected int containerid_5p;
    protected int containerid_3pfusion;
    protected int containerid_3pclosed;
    
    /**
     * Constructor.
     *
     * @param id The plateset id.
     * @param cid_5p the containerID for 5p oligo plate.
     * @param cid_3pf the containerID for 3p fusion oligo plate.
     * @param cid_3pc the containerID for 3p closed oligo plate.
     *
     * @return A Plateset object.
     */
    public Plateset(int id, int cid_5p, int cid_3pf, int cid_3pc) {
        this.id = id;
        this.containerid_5p = cid_5p;
        this.containerid_3pfusion = cid_3pf;
        this.containerid_3pclosed = cid_3pc;
    }
    
    /**
     * Constructor.
     *
     * @param cid_5p the containerID for 5p oligo plate.
     * @param cid_3pf the containerID for 3p fusion oligo plate.
     * @param cid_3pc the containerID for 3p closed oligo plate.
     *
     * @return A Plateset object.
     */
    public Plateset(int cid_5p, int cid_3pf, int cid_3pc) {
        this.containerid_5p = cid_5p;
        this.containerid_3pfusion = cid_3pf;
        this.containerid_3pclosed = cid_3pc;
        
        try{
            this.id = FlexIDGenerator.getID("platesetid");
        }catch(FlexDatabaseException sqlex){
            System.out.println(sqlex);
        }
    }
    
    /**
     * Constructor.
     *
     * @param platesetid the ID for plateset.
     *
     * @return A Plateset object.
     */
    public Plateset(int id) throws FlexDatabaseException {
        this.id = id;
        String sql = "SELECT * FROM plateset WHERE platesetid="+id;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
       
        try {
            while(rs.next()) {
                this.containerid_5p = rs.getInt("CONTAINERID_5p");
                this.containerid_3pfusion = rs.getInt("CONTAINERID_3PFUSION");
                this.containerid_3pclosed = rs.getInt("CONTAINERID_3PCLOSED");
                
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while initializing plateset\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Constructor
     * @return a plateset object
     */
    public Plateset() {
    }
    
    /**
     * return the platesetid.
     *
     * @return The platesetID.
     */
    public int getId() {
        return id;
    }
    
    /**
     * return the 5p containerid.
     *
     * @return The 5p containerid.
     */
    public int getContainerId_5p() {
        return containerid_5p;
    }
    
    /**
     * return the 3p fusion containerid.
     *
     * @return The 3p fusion containerid.
     */
    public int getContainerId_3pfusion() {
        return containerid_3pfusion;
    }
    
    /**
     * return the 3p closed containerid.
     *
     * @return The 3p closed containerid.
     */
    public int getContainerId_3pclosed() {
        return containerid_3pclosed;
    }

    /**
     * Return the 5p container.
     *
     * @return The 5p container.
     * @exception FlexCoreException.
     * @exception FlexDatabaseException.
     */
    public Container getFivepContainer() throws FlexCoreException, FlexDatabaseException {
        return (new Container(containerid_5p));
    } 

    /**
     * Return the 3p fusion container.
     *
     * @return The 3p fusion container.
     * @exception FlexCoreException.
     * @exception FlexDatabaseException.
     */
    public Container getThreepOpenContainer() throws FlexCoreException, FlexDatabaseException {
        return (new Container(containerid_3pfusion));
    } 

    /**
     * Return the 3p closed container.
     *
     * @return The 3p closed container.
     * @exception FlexCoreException.
     * @exception FlexDatabaseException.
     */
    public Container getThreepClosedContainer() throws FlexCoreException, FlexDatabaseException {
        return (new Container(containerid_3pclosed));
    } 
    
    /**
     * Returns true if the given Plateset is the same; returns false otherwise.
     *
     * @param plateser The given plateset for comparison.
     *
     * @return True if the given sample is the same, false otherwise.
     */
    public boolean isSame(Plateset plateset) {
        return (this.id == plateset.id);
    }
    
    /**
     * insert PlateSet record for a set of 5p, 3pfustion
     * and 3pclosed oligoplates into the plateset table
     * @param conn The Connection object
     */
    public void insert(Connection conn) throws FlexDatabaseException {
        
        String sql = "INSERT INTO plateset\n" +
        "(platesetid, containerid_5p, containerid_3pfusion, containerid_3pclosed)" +
        " VALUES(" + id + "," + containerid_5p + "," + containerid_3pfusion +
        "," + containerid_3pclosed + ")";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
        
    } // insert
    
    /**
     * insert PlateSet record for a set of 5p, 3pfustion
     * and 3pclosed oligoplates into the plateset table
     * @param conn The Connection object
     * @param id The platesetId
     * @param containerid_5p
     * @param containerid_3s
     * @param containerid_3op
     */
    public void insert(Connection conn, int id,int containerId_5p, int containerId_3op, int containerId_3s) throws FlexDatabaseException {
        
        String sql = "INSERT INTO plateset\n" +
        "(platesetid, containerid_5p, containerid_3pfusion, containerid_3pclosed)" +
        " VALUES(" + id + "," + containerId_5p + "," + containerId_3op +
        "," + containerId_3s + ")";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            System.out.println("inserting...");
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }      
    } // insertPlateset
    
    /**
     *
     */
    public static int findPlatesetId (int containerid) throws FlexDatabaseException{
        
        String sql = "SELECT platesetid FROM containerheader WHERE containerid ="+ containerid;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        int platesetid = -1;
        try {
            while(rs.next()) {
                
                platesetid = rs.getInt("PLATESETID");
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while finding platesetid\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return platesetid;
    }
    
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    public static void main(String args[]) {
        Plateset ps = new Plateset();
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            
            System.out.println("Insert into plateset:");
            ps.insert(conn, 50, 55, 58, 56);
            conn.rollback();
            conn.close();
            System.out.println("Done!");
        } catch(FlexDatabaseException exception){
            System.out.println(exception.getMessage());
        } catch (SQLException sqlE) {
            System.err.println(sqlE.getMessage());
        } 
    } //main       
}

