/*
 * CloneLinker.java
 *
 * Created on June 17, 2003, 1:44 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;
/**
 *
 * @author  dzuo
 */
public class CloneLinker {
    protected int id;
    protected String name;
    protected String sequence;
    
    /** Creates a new instance of CloneLinker */
    public CloneLinker() {
    }
    
    public CloneLinker(CloneLinker c) {
        if(c != null) {
            this.id = c.getId();
            this.name = c.getName();
            this.sequence = c.getSequence();
        }
    }
    
    public CloneLinker(int id) {
        this.id = id;
    }
    
    public CloneLinker(int id, String name, String sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
    }
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getSequence() {return sequence;}
    
    public void         setName(String v) { name = v;}
    public void         setSequence(String v) { sequence = v;}
    
    public static void            insertLinkers(ArrayList linkers,Connection conn) 
            throws Exception
    {
        String sql_linker = "insert into linker (LINKERID  ,LINKERNAME  ,LINKERSEQUENCE  )"+
        " values(linkerid.nextval,?,?)";
  
         PreparedStatement stmt_linker = null;
       CloneLinker  linker = null;
        try {
            stmt_linker = conn.prepareStatement(sql_linker);
            for(int l_count = 0; l_count < linkers.size(); l_count ++)
            {
                linker = (CloneLinker) linkers.get( l_count);
    ////?????            stmt_linker.setInt(1, linker.getId());
                stmt_linker.setString(1, linker.getName());
                stmt_linker.setString(2, linker.getSequence());
                DatabaseTransaction.executeUpdate(stmt_linker);
             }
           
        } catch (Exception ex) {
            throw new Exception("Error occured while inserting Linker info. \n "+ex.getMessage());
            
        }
         finally
         {
              DatabaseTransaction.closeStatement(stmt_linker);
         }
      
    }
    
    
    public static List  getAllLinkers() throws FlexDatabaseException
   {
       String sql = "select LINKERID  ,LINKERNAME  ,LINKERSEQUENCE from linker ";
       return getLinkersBySQL(sql);
   }
   
   private static List getLinkersBySQL(String sql) throws FlexDatabaseException 
   {
     
        ResultSet rs = null;
        List linkerList = new ArrayList();
         CloneLinker linker = null;
        try 
        { 
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) 
            {
                linker = new CloneLinker( rs.getInt("LINKERID"), rs.getString("LINKERNAME"), rs.getString("LINKERSEQUENCE"));
                linkerList.add(linker);
            }
            return linkerList;
        } catch (Exception ex)
        {
            throw new FlexDatabaseException(ex);
        } 
        finally 
        {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
}
