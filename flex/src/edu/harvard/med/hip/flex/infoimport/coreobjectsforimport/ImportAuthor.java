/*
 * ImportAuthor.java
 *
 * Created on October 5, 2007, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;


import  edu.harvard.med.hip.flex.core.*;
import java.util.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import  edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author htaycher
 */
public class ImportAuthor
{
     public static final String   AUTHOR_FNNAME = "SUBMITTER_FNNAME";	
     public static final String   AUTHOR_LNNAME = "SUBMITTER_LNNAME";	
     public static final String   AUTHOR_TEL = "SUBMITTER_TEL";
     public static final String   AUTHOR_FAX = "SUBMITTER_FAX";
     public static final String   AUTHOR_EMAIL ="SUBMITTER_EMAIL";
     public static final String   AUTHOR_ORGANIZATION_NAME="SUBMITTER_ORGANIZATION_NAME";
     public static final String   AUTHOR_ADDRESS="SUBMITTER_ADDRESS";
     public static final String   AUTHOR_WWW="SUBMITTER_WWW";
     public static final String   AUTHOR_DESCRIPTION="SUBMITTER_DESCRIPTION";
     public static final String   AUTHOR_TYPE ="SUBMITTER_TYPE";

     private int             m_id = -1;
     private String          m_f_name = null;
     private String          m_l_name = null;
     private String          m_tel = null;
     private String          m_fax = null;
     private String          m_email = null;
     private String          m_org_name = null;
     private String          m_address = null;
     private String          m_www = null;
     private String          m_description = null;
     private String         m_name  = null;
     private String         m_type = null;
    /** Creates a new instance of ImportAuthor */
    public ImportAuthor() {
    }
    
     public void        setName(String v){ m_name = v;}
     public void        setFNName(String v){  m_f_name = v;}
     public void        setFLName  (String v){  m_l_name = v;}
     public void        setTel  (String v){  m_tel = v;}
     public void        setFax  (String v){  m_fax = v;}
     public void        setEMail  (String v){  m_email = v;}
     public void        setOrgName  (String v){  m_org_name = v;}
     public void        setAdress  (String v){  m_address = v;}
     public void        setWWW  (String v){  m_www = v;}
     public void        setDescription  (String v){  m_description = v;}
     public void        setType(String v){ m_type= v;}
    public void         setId(int v){ m_id = v;}
    
    
    public String        getFNName(){ return m_f_name ;}
    public String        getFLName  (){ return m_l_name ;}
    public String        getTel  (){ return m_tel ;}
    public String        getFax  (){ return m_fax ;}
    public String        getEMail  (){ return m_email ;}
    public String        getOrgName  (){ return m_org_name ;}
    public String        getAdress  (){ return m_address ;}
    public String        getWWW  (){ return m_www ;}
    public String        getDescription  (){ return m_description ;}
    public String       getName(){ return m_name;}
    public int          getId(){ return m_id;}
    public String       getType(){ return m_type;}
    
    public ArrayList     convertToPublicInfoItem (int index)
    {
        PublicInfoItem p_info = null;
        ArrayList result = new ArrayList();
        result.add( new PublicInfoItem(AUTHOR_FNNAME + index, m_f_name, null, null, true));
        result.add( new PublicInfoItem(AUTHOR_LNNAME + index ,m_l_name, null, null, true));	
         result.add( new PublicInfoItem(AUTHOR_TEL + index  ,m_tel, null, null, true));
         result.add( new PublicInfoItem(AUTHOR_FAX + index  ,m_fax, null, null, true));
         result.add( new PublicInfoItem(AUTHOR_EMAIL + index ,m_email, null, null, true));
        result.add( new PublicInfoItem(AUTHOR_ORGANIZATION_NAME + index ,m_org_name, null, null, true));
         result.add( new PublicInfoItem(AUTHOR_ADDRESS + index ,m_address, null, null, true));
         result.add( new PublicInfoItem(AUTHOR_WWW + index ,m_www, null, null, true));
         result.add( new PublicInfoItem( AUTHOR_DESCRIPTION + index ,m_description, null, null, true));
         return result;
    
    }
    
    
     public void insert(Connection conn) throws Exception
     {
         
        // verify that author does not exists yet by author name
        if ( m_name == null )throw new Exception("Wrong author info: author name is empty");
        int id =  isInDatabase() ;
        if (id != -1) {this.setId(id); return;}
        if (m_id == -1) id = FlexIDGenerator.getID("authorid");
        this.setId(id);
         
        String sql = 	"insert into authorinfo (AUTHORID ,AUTHORNAME";
         String sql_values =  " values ("+m_id+",'"+m_name+"'";
       
         if ( m_f_name != null)
         {   sql+=" ,FIRSTNAME";    sql_values+=",'"+m_f_name+"' ";     }
         if (m_l_name != null )
         {   sql+=" ,LASTNAME"; sql_values+=",'"+m_l_name+"' ";  }
         if ( m_tel != null)
         { sql+=" ,TEL"; sql_values+=",'"+m_tel+"' ";    }
         if(m_fax!= null)
         { sql+=" ,FAX "; sql_values+=",'"+m_fax+"' ";   }
         if(m_email!= null)
         { sql+=" , AUTHOREMAIL"; sql_values+=",'"+m_email+"' ";   }
         if(m_address!= null)
         { sql+=" , ADDRESS"; sql_values+=",'"+m_address+"' ";   }
         if(m_www!= null)
         { sql+=" ,WWW "; sql_values+=",'"+m_www+"' ";   }
           if(m_description!= null)
         { sql+=" ,DESCRIPTION "; sql_values+=",'"+m_description+"' ";   }
           if(m_org_name!= null)
         { sql+=" ,ORGANIZATIONNAME"; sql_values+=",'"+m_org_name+"' ";   }
           
        sql=sql+")"+sql_values+")";
        DatabaseTransaction.executeUpdate(sql,conn);
     }
     
     public int         isInDatabase() throws FlexDatabaseException
     {
        
        String sql = null;
        if (m_f_name != null && m_l_name!= null) 
            sql = "select  AUTHORID from authorinfo " +
                "where UPPER(authorname)='"+m_name.toUpperCase() +"' and UPPER(FIRSTNAME)='"+
                m_f_name.toUpperCase()+"' and UPPER(LASTNAME)='"+m_l_name.toUpperCase()+"'";
        else  if (m_f_name != null && m_l_name == null) 
            sql = "select  AUTHORID  from authorinfo " +
                "where UPPER(authorname)='"+m_name.toUpperCase() +"' and UPPER(FIRSTNAME)='"+
                m_f_name.toUpperCase()+"' and LASTNAME is null";
        else if (m_f_name == null && m_l_name != null) 
            sql = "select  AUTHORID  from authorinfo " +
                "where UPPER(authorname)='"+m_name.toUpperCase() +"' and FIRSTNAME is null and UPPER(LASTNAME)='"+m_l_name.toUpperCase()+"'";
        else if (m_f_name == null && m_l_name == null) 
            sql = "select  AUTHORID  from authorinfo " +
                "where UPPER(authorname)='"+m_name.toUpperCase() +"' and FIRSTNAME is null and LASTNAME is null";
       
        RowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next()) 
            {
               return crs.getInt("AUTHORID");
            }
            else
                return -1;
        } catch (Exception ex) {
            throw new FlexDatabaseException("Error occured while querying author information\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
      
     }
}
