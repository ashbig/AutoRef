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
    
    
    //values
    
    public static final String FS_SILENT= "FS_SILENT";
     public static final String FS_CONSERVATIVE= "FS_CONSERVATIVE";
     public static final String FS_NON_CONSERVATIVE= "FS_NUN_CONSERVATIVE"; 
     public static final String FS_FRAMESHIFT= "FS_FRAMESHIFT";
     public static final String FS_STOP= "FS_STOP";
     public static final String FS_N_100= "FS_N_100";
    public static final String FS_N_ROW= "FS_N_ROW";
     
    /** Creates a new instance of EndReadsSpec */
   
    public FullSeqSpec(Hashtable p, String na, String submitter)
    {
         super( p,  na, submitter,FULL_SEQ_SPEC_INT);
         cleanup_parameters();
    }
    
     public FullSeqSpec(Hashtable p, String na, String submitter,int id) 
    {
         super( p,  na, submitter, FULL_SEQ_SPEC_INT,id);
         cleanup_parameters();
    }
     
   
    
 
    
     public static ArrayList getAllSpecs() throws FlexDatabaseException
     {
         return getAllSpecsByType(FULL_SEQ_SPEC_INT, true);
     }
     public static ArrayList getAllSpecNames() throws FlexDatabaseException
     {
         return getAllSpecsByType(FULL_SEQ_SPEC_INT, false);
     }
     
     public static ArrayList getAllSpecsBySubmitter(String submitter) throws FlexDatabaseException
     {
         return getAllSpecsByTypeAndSubmitter(Spec.FULL_SEQ_SPEC_INT, submitter);
     }
     
     
     //cleans up not neaded parameters submitted by html form
     protected void cleanup_parameters()
     {
         try
        {
            cleanup_parameters("FS_");
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
     
     
      //-------------------- mani -----------------------
     public static void main(String [] args) 
     {
        Connection c = null;
  ArrayList specs =null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
             specs =  FullSeqSpec.getAllSpecsBySubmitter("htaycher");
            System.out.print(specs.size());
        }
        catch(Exception e){ System.out.print(specs.size());}
        System.exit(0);
     }
}
