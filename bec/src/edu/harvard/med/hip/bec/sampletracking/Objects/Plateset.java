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
package edu.harvard.med.hip.bec.sampletracking.Objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.sql.*;
import java.util.*;

public class Plateset
{
   
    
    private int m_id;
    private ArrayList m_containers = null;
    private ArrayList m_container_ids = null;
    
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
/*    public Plateset(int id, int cid_5p, int cid_3pf, int cid_3pc) {
        m_id = id;
        m_containerid_5p = cid_5p;
        m_containerid_3pfusion = cid_3pf;
        m_containerid_3pclosed = cid_3pc;
    }
 */
    /**
     * Constructor.
     *
     * @param cid_5p the containerID for 5p oligo plate.
     * @param cid_3pf the containerID for 3p fusion oligo plate.
     * @param cid_3pc the containerID for 3p closed oligo plate.
     *
     * @return A Plateset object.
     */
    public Plateset(ArrayList ids, int mode)
    {
        
        if (mode == Constants.TYPE_OBJECTS)
        {
            m_containers = ids;
            for(int i = 0; i < m_containers.size(); i++)
            {
                m_container_ids.add( new Integer( ((Container) m_containers.get(i)).getId()));
            }
        }
        else if (mode == Constants.TYPE_ID)
            m_container_ids = ids;
        
        try
        {
            m_id = BecIDGenerator.getID("platesetid");
        }
        catch(BecDatabaseException sqlex)
        {
            System.err.println(sqlex);
        }
    }
    
    /**
     * Constructor.
     *
     * @param id The plateset id.
     * @param cid_5p the containerID for 5p oligo plate.
     * @param cid_3pf the containerID for 3p fusion oligo plate.
     * @param cid_3pc the containerID for 3p closed oligo plate.
     * @param cid_mgc the container id for MGC plate.
     *
     * @return A Plateset object.
     */
    public Plateset(int id, ArrayList ids, int mode)
    {
        m_id = id;
        if (mode == Constants.TYPE_OBJECTS)
        {
            m_containers = ids;
            for(int i = 0; i < m_containers.size(); i++)
            {
                m_container_ids.add( new Integer( ((Container) m_containers.get(i)).getId()));
            }
        }
        else if (mode == Constants.TYPE_ID)
            m_container_ids = ids;
    }
    
   
    
    /**
     * Constructor.
     *
     * @param platesetid the ID for plateset.
     *
     * @return A Plateset object.
     */
    public Plateset(int id) throws BecDatabaseException
    {
        m_id = id;
        String sql = "SELECT * FROM plateset WHERE platesetid="+id;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        
        try
        {
            while(rs.next())
            {
               // m_containerid_5p = rs.getInt("CONTAINERID_5p");
               
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while initializing plateset\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
      
    /**
     * return the platesetid.
     *
     * @return The platesetID.
     */
    public int getId()    {        return m_id;    }
    
   
    public ArrayList getContainers() throws BecDatabaseException,BecUtilException
    {
        if(m_containers == null && m_container_ids != null)
        {
            for (int ind = 0; ind <m_container_ids.size(); ind++)
            {
               
                int id = ((Integer)m_container_ids.get(ind)).intValue();
                Container cont = new Container( id );
                m_containers.add(cont);
            }
            
        }
        return m_containers;
    }
    
     public ArrayList getContainerIds() throws BecDatabaseException    { return m_container_ids;    }
    /**
     * Returns true if the given Plateset is the same; returns false otherwise.
     *
     * @param plateser The given plateset for comparison.
     *
     * @return True if the given sample is the same, false otherwise.
     */
    public boolean isSame(Plateset plateset)    {        return (m_id == plateset.getId());    }
    
    /**
     * insert PlateSet record for a set of 5p, 3pfustion
     * and 3pclosed oligoplates into the plateset table
     * @param conn The Connection object
     */
    public void insert(Connection conn) throws BecDatabaseException
    {
       /* 
        String sql = "INSERT INTO plateset\n" +
        "(platesetid, containerid_5p, containerid_3pfusion, containerid_3pclosed, containerid_mgc)" +
        " VALUES(" + id + "," + containerid_5p + "," + fusion +
        "," + close + "," + mgc + ")";
        
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
        */
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
    public void insert(Connection conn, ArrayList cont_ids) throws BecDatabaseException
    {
        /*
        
        String sql = "INSERT INTO plateset\n" +
        "(platesetid, containerid_5p, containerid_3pfusion, containerid_3pclosed)" +
        " VALUES(" + id + "," + containerId_5p + "," + fusion +
        "," + close + ")";
        
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            //System.out.println("inserting...");
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
         **/
    } // insertPlateset
    
    /**
     * Returns the Plateset object that the given container belongs to.
     *
     * @param container The container used to find the plateset.
     * @return The Plateset object that the given container belongs to.
     * @exception BecdatabaseException.
     */
    public static Plateset findPlateset(Container c) throws BecDatabaseException
    {
        String sql = "select * from plateset "+
        "where containerid_5p = "+c.getId()+
        " or containerid_3pfusion = "+c.getId() +
        " or containerid_3pclosed = "+c.getId() +
        " or containerid_mgc = "+c.getId();
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = DatabaseTransaction.getInstance().executeQuery(sql);
        Plateset plateset = null;
        
        try
        {
            if(rs.next())
            {
                int platesetid = rs.getInt("PLATESETID");
                ArrayList ids = null;
                //int containerid_mgc = rs.getInt("CONTAINERID_MGC");
                plateset = new Plateset(platesetid, ids, Constants.TYPE_ID);
            }
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException("Error occured while finding platesetid\n"+sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return plateset;
    }
    
    //**************************************************************//
    //				Test				//
    //**************************************************************//
    
    public static void main(String args[])
    {
        
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            
            System.out.println("Insert into plateset:");
           // ps.insert(conn, 50, 55, 58, 56);
           // conn.rollback();
           // conn.close();
           // System.out.println("Done!");
        } catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        } 
    } //main
}

