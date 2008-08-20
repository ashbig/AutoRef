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
import edu.harvard.med.hip.flex.infoimport.*;
import static  edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE;
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
    private boolean     i_is_submit = true;
    /** Creates a new instance of PublicInfoItem */
    public PublicInfoItem(String a, String b, String c, String d)
    {
        m_name = a;
        m_value = b;
        m_description = c;
        m_url = d;
    }
     public PublicInfoItem(String a, String b, String c, String d, boolean e)
    {
        this( a,b, c,d);
        i_is_submit = e;
    }
    public PublicInfoItem(String a, String b)
    {
        m_name = a;
        m_value = b;
       
    }
    public PublicInfoItem()    {}
    public String       toString()
    {
        if ( ! i_is_submit) return  m_name +" "+m_value+" "+m_description  ;
        return  m_name +" "+m_value+" "+m_description +" "+ i_is_submit;
    }
    public boolean equals(PublicInfoItem obj)
    {
        if ( !obj.getName().equalsIgnoreCase(m_name)) return false;
        if ( !obj.getValue().equalsIgnoreCase(m_value)) return false;
        return true;
    }
    
    public static boolean contains (ArrayList p_info, PublicInfoItem obj)
    {
        PublicInfoItem obj2 = null;
        for (int count = 0; count < p_info.size(); count++)
        {
            obj2 = (PublicInfoItem) p_info.get(count);
            if ( obj2.equals(obj))
                return true;
        }
        return false;
    }
  
    
    
    public  void   setName (String v){   m_name  = v;}
    public  void   setValue (String v){   m_value  = v;}
    public  void   setDescription (String v){   m_description  = v;}
    public  void   setUrl (String v){   m_url  = v;}
    public void    setIsSubmit(boolean v){ i_is_submit= v;}
    
    public String   getName (){ return m_name  ;}
    public String   getValue (){ return m_value  ;}
    public String   getDescription (){ return m_description  ;}
    public String   getUrl (){ return m_url  ;}
    public boolean  isSubmit(){ return i_is_submit;}
    
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
                if ( ! info_item.isSubmit() ) continue;
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
        finally{DatabaseTransaction.closeStatement(stmt);}
    }
   
     public static ArrayList  getPublicInfo( String table_name, 
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
    
    
    public static  boolean  isAnyPublicInfoForSubmission(  Collection public_info)
    {
         PublicInfoItem info_item = null;
         for (Iterator iter = public_info.iterator (); iter.hasNext (); )
         {
              info_item = (PublicInfoItem)iter.next ();
              if (info_item.isSubmit()) return true;
              
         }
         return false;
    }
    
    
   public static ArrayList    sorPublicInfo(ArrayList  public_info)
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
   
    public static String    toString(ArrayList<PublicInfoItem>  public_info,
            boolean isURL, boolean isDescription )
   {
        StringBuffer result = new StringBuffer();
        for ( PublicInfoItem pb: public_info)
        {
            if ( pb.isSubmit()   )
            {
                result.append(pb.getName()+":");
                result.append(pb.getValue());
                if ( isDescription )result.append("/Description:"+pb.getDescription());
                if ( isURL )result.append("/URL:"+pb.getUrl());
                result.append("|");
            }
        }
        return result.toString();
    }
    public static ArrayList restorePublicInfo(int owner_id, ITEM_TYPE owner_type) throws Exception
     {
         
         ArrayList info = new ArrayList();
         PublicInfoItem p_info = null;
        String sql = "select nametype,namevalue,nameurl,description from " +
               owner_type.getNameTableNamePerOwner()+
                " where "+ owner_type.getNameTableIdColumnNamePerOwner()+"="+owner_id;
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) 
            {
                p_info = new PublicInfoItem();
                p_info.setName(rs.getString("nametype"));
                 p_info.setUrl(rs.getString("nameurl"));
                 p_info.setValue(rs.getString("namevalue"));
                 p_info.setDescription(rs.getString("description"));
                
                info.add(p_info);
             }
            return info;
        } catch (Exception ex) {
            System.out.println(ex);
            throw new Exception(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
       
    }
    
      
      ////////////////////////////////////////////////////
    public static void main(String[] args)
  {
        try
        {
      ArrayList r = PublicInfoItem.restorePublicInfo(258460 , ConstantsImport.ITEM_TYPE.ITEM_TYPE_CLONEID);
      ArrayList authors = edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportAuthor.restoreAuthors(258460, ConstantsImport.ITEM_TYPE.ITEM_TYPE_CLONEID);
      edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportAuthor at = 
              edu.harvard.med.hip.flex.infoimport.coreobjectsforimport.ImportAuthor.restoreAuthor(12);
        
        }
        catch(Exception e){}
    }
}
