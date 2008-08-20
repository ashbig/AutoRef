/*
 * Nametype.java
 *
 * Created on October 29, 2001, 5:32 PM
 */

package edu.harvard.med.hip.flex.core;

import edu.harvard.med.hip.flex.database.*;
import java.util.*;
import java.sql.*;

/**
 * This class maps to the nametype table in the database.
 *
 * @author  dzuo
 * @version
 */
public class Nametype 
{
    // name tables names
    public enum TABLE_NAME_NAMETYPE
    {
        SAMPLE_NAMETYPE,
        NAMETYPE,
        CONTAINERHEADER_NAMETYPE,
        SPECIES,
        FLEXSTATUS,
        SAMPLETYPE,
        CONTAINERTYPE,
        CLONEAUTHORTYPE;
    }
            
    private String name = null;
    private String m_description = null;
    
    /** Creates new Nametype */
    public Nametype(String name)
    {
        this.name = name;
    }
    public Nametype(String name, String description)
    {
        this( name);
        m_description = description;
    }
    
    /**
     * Query the database to get all the nametypes.
     *
     * @return A list of Nametype objects.
     */
    public static Vector getAllNametypes() 
    {
        Vector result = new Vector();
        String sql = "select * from nametype order by nametype";
        ResultSet rs = null;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) 
            {
                //String name = rs.getString("NAMETYPE");
                
                Nametype nt = new Nametype(rs.getString("NAMETYPE"), rs.getString("DISPLAYTITLE"));
                result.addElement(nt);
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return result;
    }

    /**
     * Return true if the given nametype exists in the database; return 
     * false otherwise.
     *
     * @param nametype The nametype to be validated.
     * @return True if the given nametype exists in the database; return
     * false otherwise.
     */
    public static boolean exists(String nametype) {
        String sql = "select nametype from nametype where nametype='"+nametype+"'";
        ResultSet rs = null;
        boolean ret = false;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                ret = true;
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }     
        
        return ret;
    }
    
    /**
     * Add the nametype to the database.
     *
     * @param nametype The value to be added to the database.
     * @param conn The database connection.
     */
    public static boolean addNametype(String nametype, Connection conn) {
        String sql = "insert into nametype values ('"+nametype+"')";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException sqlE) {
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }      
    }
    
    /**
     * Return the name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }
    public String getDescription() {
        return m_description;
    }
    public void setDescription(String v) { m_description = v;}
   
    public static ArrayList getInfoFromNamesTable( TABLE_NAME_NAMETYPE cur_name_type ) throws Exception
    {
       String sql = null ;
       ArrayList nametypes = new ArrayList();
       switch(cur_name_type)
       {
           case SAMPLE_NAMETYPE:{ sql = " select  nametype, displaytitle as description from " + cur_name_type.toString() +" order by nametype";break;}
           case NAMETYPE:{ sql = " select  nametype, displaytitle as description from " + cur_name_type.toString() +" order by nametype";break;}
           case CONTAINERHEADER_NAMETYPE:{ sql = "  select  nametype, displaytitle as description from  " + cur_name_type.toString() +" order by nametype";break;}
       case SPECIES:{ sql = "select  genusspecies as nametype from  " + cur_name_type.toString() +" order by nametype ";break;}
           case FLEXSTATUS:{ sql = " select  flexstatus as nametype from  " + cur_name_type.toString() +" order by nametype ";break;}
           case SAMPLETYPE:{ sql = " select  sampletype as nametype from  " + cur_name_type.toString() +" order by nametype ";break;}
        case CONTAINERTYPE:{ sql = " select  containertype as nametype from  " + cur_name_type.toString() +" order by nametype";break;}
           case CLONEAUTHORTYPE:{ sql = " select  nametype, displaytitle as description from " + cur_name_type.toString() +" order by nametype";break;}
       }
       if (sql == null) throw new Exception("Not known table.");
       
       ResultSet rs = null;
       Nametype  nametype = null;
        try 
        {
            rs  = DatabaseTransaction.getInstance().executeQuery(sql);
            while( rs.next()) 
            {
                nametype = new Nametype( rs.getString("nametype"));
                 switch(cur_name_type)
                {
                     case SAMPLE_NAMETYPE: case NAMETYPE:  
                     case CONTAINERHEADER_NAMETYPE: case CLONEAUTHORTYPE:
                    {
                         nametype.setDescription( rs.getString("description"));
                    }
                 }
                nametypes.add(nametype);
              
            }
            return nametypes;
        }
        catch(Exception ee)
        {
            throw new Exception (ee.getMessage());
        }
        finally         {              DatabaseTransaction.closeResultSet(rs);         }
      
    }
  
    public static void main(String [] args) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            
             t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            if(Nametype.addNametype("DUMMY", conn)) {
                System.out.println("Name type has been added successfully.");
            } else {
                System.out.println("Name type cannot be added to the database.");
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            //DatabaseTransaction.rollback(conn);
            //DatabaseTransaction.closeConnection(conn);
        }        
    }        
}
