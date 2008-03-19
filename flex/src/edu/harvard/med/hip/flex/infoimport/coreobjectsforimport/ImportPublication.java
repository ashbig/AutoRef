/*
 * ImportPublication.java
 *
 * Created on March 18, 2008, 1:58 PM
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
import edu.harvard.med.hip.flex.infoimport.*;
/**
 *
 * @author htaycher
 */
public class ImportPublication 
{
     public static final String   PUBLICATION_PBMEDID = "PUBMED_ID";
     public static final String   PUBLICATION_TITLE = "TITLE";	
    

     private int             m_id = -1;
     private String          m_pubmedid = null;
     private String          m_title = null;

    /** Creates a new instance of ImportPublication */
    public ImportPublication() {
    }
    
     public void        setId(int v){ m_id = v;}
     public void        setPubMedID(String v){ m_pubmedid = v;}
     public void        setTitle(String v){  m_title = v;}
     
     public String        getPubMedID(){ return m_pubmedid ;}
     public String        getTitle(){ return m_title ;}
      public int        getId(){ return m_id ;}
     
     public int         isInDatabase() throws FlexDatabaseException
     {
        
        if ( m_pubmedid == null || m_title == null)
            return -1;
        String sql = "select  publicationid from publication  where UPPER(PUBMEDID)='"+m_pubmedid.toUpperCase() +"'";
       
        RowSet crs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql);
            if(crs.next()) 
            {
               return crs.getInt("publicationid");
            }
            else
                return -1;
        } catch (Exception ex) {
            throw new FlexDatabaseException("Error occured while querying publication information\n"+ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
      
     }
   
     
      public void insert(Connection conn) throws Exception
     {
         
        // verify that author does not exists yet by author name
          if ( m_pubmedid == null || m_title == null)
            throw new Exception("Wrong publication info: publication title or PubMedID is empty");
        int id =  isInDatabase() ;
        if (id != -1) { this.setId(id); return;}
        if (m_id == -1) id = FlexIDGenerator.getID("publicationid");
        this.setId(id);
         
         String sql = 	"insert into publication (publicationid, title, pubmedid) values ("
                 + m_id +",'"+m_title+"','"+m_pubmedid+"')";
        DatabaseTransaction.executeUpdate(sql,conn);
     }
     
}
