/*
 * PolimorphismSpec.java
 *
 * Created on February 19, 2003, 11:59 AM
 */



package edu.harvard.med.hip.bec.coreobjects.spec;


import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import edu.harvard.med.hip.bec.util.*;
/**
 *
 * @author  htaycher
 */
public class PolymorphismSpec extends Spec
{
    
    
    //values
    public static final String FS_SILENT= "FS_SILENT";
     public static final String FS_CONSERVATIVE= "FS_CONSERVATIVE";
     public static final String FS_NON_CONSERVATIVE= "FS_NUN_CONSERVATIVE"; 
     public static final String FS_FRAMESHIFT= "FS_FRAMESHIFT";
     public static final String FS_STOP= "FS_STOP";
     public static final String FS_N_100= "FS_N_100";
    public static final String FS_N_ROW= "FS_N_ROW";
    /** Creates a new instance of EndReadsSpec */
   
    public PolymorphismSpec(Hashtable p, String na, int submitter_id)
    {
         super( p,  na, submitter_id, Spec.POLYMORPHISM_SPEC_INT);
         cleanup_parameters();
    }
    
     public PolymorphismSpec(Hashtable p, String na, int submitter_id, int id) 
    {
         super( p,  na, submitter_id, POLYMORPHISM_SPEC_INT,id);
         cleanup_parameters();
    }
     
   
    
 
    
     public static ArrayList getAllSpecs() throws BecDatabaseException
     {
         return getAllSpecsByType(POLYMORPHISM_SPEC_INT, true);
     }
     
      public static ArrayList getAllSpecNames() throws BecDatabaseException
     {
         return getAllSpecsByType(POLYMORPHISM_SPEC_INT, false);
     }
     public static ArrayList getAllSpecsBySubmitter(int submitter_id) throws BecDatabaseException
     {
         return getAllSpecsByTypeAndSubmitter(Spec.POLYMORPHISM_SPEC_INT, submitter_id);
     }
     
     
     //cleans up not neaded parameters submitted by html form
     protected void cleanup_parameters()
     {
         
         try
         {
            cleanup_parameters("PL_");
        }
         catch(Exception e1)
         {
             System.out.println(e1.getMessage());
         }
     }
     
     public boolean validateParameters()
     {
         return true;
     }
     
}
