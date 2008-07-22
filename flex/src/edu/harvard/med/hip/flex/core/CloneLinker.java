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
    
    public void         setId(int v) {id = v;}
    public void         setName(String v) { name = v;}
    public void         setSequence(String v) { sequence = v;}
    
    public String toString()
    {
        return "Linker ID: "+ id+" linker name: "+ name +" linker sequence: "+ sequence;
    }
    public static void            insertLinkers(Collection linkers,Connection conn) 
            throws Exception
    {
        String sql_linker = "insert into linker (LINKERID  ,LINKERNAME  ,LINKERSEQUENCE  )"+
        " values(linkerid.nextval,?,?)";
  
         PreparedStatement stmt_linker = null;
       CloneLinker  linker = null;
        try {
            stmt_linker = conn.prepareStatement(sql_linker);
            Iterator iter = linkers.iterator();
            while( iter.hasNext())
            {
                linker = (CloneLinker) iter.next();
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
       String sql = "select LINKERID  ,LINKERNAME  ,LINKERSEQUENCE from linker order by linkerid  ";
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
   
    public static void main(String[] args)
  {
           ArrayList display_items = new ArrayList(); 
         ArrayList header = new ArrayList(); 
          List items = null;
          String no_items_header = null;
         String title= ""; ArrayList item_description = null;
         
         try
         {
         CloneLinker linker = null;
               items = CloneLinker.getAllLinkers();
               Iterator iter = items.iterator();
               if( items != null && items.size() > 0 )
               {  
                       header.add(  "Linker ID");
                       header.add(  "Linker Name");
                       header.add(  "Linker Sequence" ) ;
               } 
               else
                no_items_header = "No linkers available";
               while(iter.hasNext()){
                   linker = (CloneLinker)iter.next();
                   item_description= new ArrayList();
                   item_description.add(String.valueOf( linker .getId()));
                   item_description.add(linker.getName());
                   item_description.add(linker.getSequence());
                   display_items.add(item_description);
               }
               title="Currently Available Linkers ";
         }catch(Exception e){}
     }
}
