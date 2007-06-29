/*
 * PublicInfo.java
 *
 * Created on June 6, 2007, 12:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.core;

/**
 *
 * @author htaycher
 */
//Copyright 2003 - 2005, 2006 President and Fellows of Harvard College. All Rights Reserved.-->
/*
 * PublicInfoItem.java
 *
 * Created on April 23, 2003, 5:40 PM
 */

import java.sql.*;
import java.util.*;
import sun.jdbc.rowset.*;
import java.sql.*;
import javax.sql.*;
import edu.harvard.med.hip.flex.database.*;
/**
 *
 * @author  htaycher
 *item of public info for reference sequence
 */
public class PublicInfoItem
{
    public static final String NAMETYPE = "NAMETYPE";
    public static final String NAMEVALUE = "NAMEVALUE";
    public static final String NAMEURL = "NAMEURL";
    public static final String DESCRIPTION = "DESCRIPTION";
    
   
    
    
    private String   m_name = null;
    private String   m_value = null;
    private String   m_description = null;
    private String   m_url = null;
    /** Creates a new instance of PublicInfoItem */
    public PublicInfoItem(String a, String b, String c, String d)
    {
        m_name = a;
        m_value = b;
        m_description = c;
        m_url = d;
    }
    public PublicInfoItem(String a, String b)
    {
        m_name = a;
        m_value = b;
       
    }
    public PublicInfoItem()    {}
    
    public  void   setName (String v){   m_name  = v;}
    public  void   setValue (String v){   m_value  = v;}
    public  void   setDescription (String v){   m_description  = v;}
    public  void   setUrl (String v){   m_url  = v;}
    
    public String   getName (){ return m_name  ;}
    public String   getValue (){ return m_value  ;}
    public String   getDescription (){ return m_description  ;}
    public String   getUrl (){ return m_url  ;}
    
   public static void   insertPublicInfo( Connection conn, String table_name, 
           Collection public_info, int owner_id, String owner_column_name,
           boolean isGetOutOnFirstException, ArrayList error_messages) 
           throws FlexDatabaseException
    {
        PublicInfoItem info_item = null;
        String sql = "insert into "+table_name+" ("+
                owner_column_name+ " ,nametype,namevalue,nameurl,description) "+
        " values(?,?,?,?,?)";
        PreparedStatement stmt = null;
        
        try 
        {
            stmt = conn.prepareStatement(sql);
             for (Iterator iter = public_info.iterator (); iter.hasNext (); )
             {
                info_item = (PublicInfoItem)iter.next ();
                stmt.setInt(1, owner_id);
                stmt.setString(2, info_item.getName());
                stmt.setString(3, info_item.getValue());
                stmt.setString(4, info_item.getUrl());
                stmt.setString(5, info_item.getDescription());
                
                DatabaseTransaction.executeUpdate(stmt);
             }
         }
       catch (Exception e)
        {
            if (isGetOutOnFirstException) 
                throw new FlexDatabaseException ("Cannot submit info "+ e.getMessage());
            else
                error_messages.add("Cannot submit info "+ e.getMessage());
        } 
    }
   
     public static ArrayList  getPublicInfo( Connection conn, String table_name, 
            int owner_id, String owner_column_name)        throws FlexDatabaseException
     {
   
     // public info stuff
            ArrayList public_info = new ArrayList();
            RowSet rs = null;
            PublicInfoItem info_item = null;
        
            String sql = "select nametype, namevalue, nameurl, description from "+table_name+" where "+owner_column_name+ " = "+owner_id;
            try
            {
                
                rs = DatabaseTransaction.getInstance().executeQuery(sql);
                while(rs.next())
                {
                    info_item = new PublicInfoItem();
                    info_item.setDescription(rs.getString("DESCRIPTION"));
                    info_item.setName(rs.getString("nametype"));
                    info_item.setUrl(rs.getString("nameurl"));
                    info_item.setValue(rs.getString("namevalue"));
                    public_info.add(info_item);
                }
                return public_info;
                
            }
                catch(Exception e)
                {
                    throw new FlexDatabaseException("Cannot get info for owner id " + owner_id + e.getMessage());
                }
     }
            
            
    public static  PublicInfoItem  getPublicInfoByName( String info_name, 
           Collection public_info)
    {
         PublicInfoItem info_item = null;
         for (Iterator iter = public_info.iterator (); iter.hasNext (); )
         {
              info_item = (PublicInfoItem)iter.next ();
              if (info_item.getName().equalsIgnoreCase(info_name)) return info_item;
              
         }
         return null;
    }
    
   public ArrayList    sorPublicInfo(ArrayList  public_info)
  {
      Collections.sort( public_info , new Comparator() 
            {
                public int compare(Object o1, Object o2) 
                {
                    PublicInfoItem  ocast1 = (PublicInfoItem)o1;
                    PublicInfoItem ocast2 = (PublicInfoItem)o2;
                    if( !ocast1.getName().equalsIgnoreCase(ocast2.getName()))
                        return ocast1.getName().compareTo(ocast2.getName());
                    return ocast1.getValue().compareTo(ocast2.getValue());
                 }
                /** Note: this comparator imposes orderings that are
                 * inconsistent with equals. */
                public boolean equals(java.lang.Object obj)
                {      return false;  }
                // compare
            } );
     return public_info;
  }
}
