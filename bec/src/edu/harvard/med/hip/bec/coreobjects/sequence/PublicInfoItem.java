/*
 * PublicInfoItem.java
 *
 * Created on April 23, 2003, 5:40 PM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import java.sql.*;
import edu.harvard.med.hip.bec.database.*;
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
    
    public static final String GI = "GI";
    public static final String LOCUS_ID = "LOCUS_ID";
    public static final String GENBANK_ACCESSION = "GENBANK_ACCESSION";
    public static final String UNIGENE_SID = "UNIGENE_SID";
    public static final String GENE_SYMBOL = "GENE_SYMBOL";
    public static final String PANUMBER = "PANUMBER";
        
    public static final String SGD  = "SGD";               
    public static final String ALT_GENE_NAME    = "ALT_GENE_NAME";   
    public static final String ALT_PROTEIN_NAME = "ALT_PROTEIN_NAME";   
    public static final String GENE_NAME    = "GENE_NAME";       
    public static final String MGC_ID     = "MGC_ID";         
    public static final String IMAGE_ID = "IMAGE_ID";
    
    
    
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
    
    public PublicInfoItem()
    {}
    
    public  void   setName (String v){   m_name  = v;}
    public  void   setValue (String v){   m_value  = v;}
    public  void   setDescription (String v){   m_description  = v;}
    public  void   setUrl (String v){   m_url  = v;}
    public String   getName (){ return m_name  ;}
    public String   getValue (){ return m_value  ;}
    public String   getDescription (){ return m_description  ;}
    public String   getUrl (){ return m_url  ;}
    
   public static String   getCloneAdditionalId (int sequence_id, String name_value)throws Exception
    {
        
         String res = "";ResultSet rs = null;
         DatabaseTransaction t = DatabaseTransaction.getInstance();
         String sql="select namevalue from name where sequenceid in "
            +" (select becsequenceid from view_flexbecsequenceid where flexsequenceid="+sequence_id
            +" ) and nametype='"+name_value+"'";
            rs = t.executeQuery(sql);
           if(rs.next())
                 res = rs.getString("namevalue"); 
           return res;
       
    }
}
