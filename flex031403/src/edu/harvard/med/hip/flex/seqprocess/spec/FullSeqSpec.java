/*
 * FullSeqSpec.java
 *
 * Created on September 20, 2002, 11:49 AM
 */

package edu.harvard.med.hip.flex.seqprocess.spec;


import java.util.*;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author  htaycher
 */
public class FullSeqSpec extends Spec
{
    public static final String FULL_SEQ_SPEC = "FULL_SEQ_SPEC";
    public static final int FULL_SEQ_SPEC_INT = 1;
    
    //values
    public static final String FS_SILENT= "FS_SILENT";
     public static final String FS_CONSERVATIVE= "FS_CONSERVATIVE";
     public static final String FS_NON_CONSERVATIVE= "FS_NUN_CONSERVATIVE"; 
     public static final String FS_FRAMESHIFT= "FS_FRAMESHIFT";
     public static final String FS_STOP= "FS_STOP";
     public static final String FS_N_100= "FS_N_100";
    public static final String FS_N_ROW= "FS_N_ROW";
    /** Creates a new instance of EndReadsSpec */
   
    public FullSeqSpec(Hashtable p, String na)
    {
         super( p,  na, FULL_SEQ_SPEC);
    }
    
     public FullSeqSpec(Hashtable p, String na,int id) 
    {
         super( p,  na, FULL_SEQ_SPEC,id);
    }
     
    public FullSeqSpec(int id) throws FlexDatabaseException
    {
         super( id, FULL_SEQ_SPEC);
    }
    
 
    
     public static ArrayList getAllSpecs() throws FlexDatabaseException
     {
         return getAllSpecs(FULL_SEQ_SPEC);
     }
     
     protected void cleanup_parameters()
     {
           String k = null;
         
         for (Enumeration e = m_params.keys() ; e.hasMoreElements() ;)
         {
                k = (String)e.nextElement();
             
                if ( k.length() < 4)
               {
                   m_params.remove(k);
                   continue;
               }
                if (! k.substring(0,3).toUpperCase().equalsIgnoreCase("FS_") )
                {
                    m_params.remove(k);
                    
                }
                
         }
     }
     
}
