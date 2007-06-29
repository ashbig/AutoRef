/*
 * ImportConstruct.java
 *
 * Created on June 22, 2007, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.infoimport.coreobjectsforimport;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.core.*;
/**
 *
 * @author htaycher
 */
public class ImportConstruct 
{
     public static final String CONSTRUCT_TYPE_CLOSED="CLOSED";
    public static final String CONSTRUCT_TYPE_FUSION="FUSION";
    
     public static final String CONSTRUCT_SIZE_LARGE="LARGE";
    public static final String CONSTRUCT_SIZE_MEDIUM="MEDIUM";
    public static final String CONSTRUCT_SIZE_SMALL = "SMALL";
    
    private int             m_id =-1;
    private int             m_sequence_id = -1;
    private String          m_type = Construct.CLOSED;
    private String          m_size_class = "SMALL";
   
    /** Creates a new instance of ImportConstruct */
    public ImportConstruct() {    }
    public ImportConstruct(  String type  )
    {
        m_type = type;
    }
    public      void        setType(String v){m_type = v;}
    public      void        setSequenceId(int v){m_sequence_id = v;}
    
    public      void        insert(Connection conn, int projectid, int workflowid) throws Exception
    {
         if (m_id == -1)        m_id = FlexIDGenerator.getID("constructid");
      
            String sql = "INSERT INTO ConstructDesign (constructid, sequenceid, " +
        " constructtype, constructsizeclass, constructpairid,  projectid, workflowid)\n" +
        " VALUES(" + m_id+ "," +m_sequence_id+ ",'" + m_type + "','"  + m_size_class + "',constructpairid.nextval,"
         + projectid+","+workflowid+")";
          DatabaseTransaction.executeUpdate(sql,conn);
      
    }
    
    public static String        defineConstructSize(int v)
    {
        if ( v > 0 && v < 2000) return CONSTRUCT_SIZE_SMALL;
        if ( v >= 2000 && v < 4000) return CONSTRUCT_SIZE_MEDIUM;
        if (v > 4000 )return CONSTRUCT_SIZE_LARGE;
        return null;
    }
    
}
