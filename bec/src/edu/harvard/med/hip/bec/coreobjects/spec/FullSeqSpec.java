/*
 * FullSeqSpec.java
 *
 * Created on September 20, 2002, 11:49 AM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;


import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.feature.*;
/**
 *
 * @author  htaycher
 */
public class FullSeqSpec extends Spec
{
    public static final int CUT_OFF_VALUE_NOT_FOUND = -1;
    
    //values
    
    public static final String FS_SILENT= "FS_SILENT";
     public static final String FS_CONSERVATIVE= "FS_CONSERVATIVE";
     public static final String FS_NON_CONSERVATIVE= "FS_NUN_CONSERVATIVE"; 
     public static final String FS_FRAMESHIFT= "FS_FRAMESHIFT";
     public static final String FS_STOP= "FS_STOP";
     public static final String FS_N_100= "FS_N_100";
    public static final String FS_N_ROW= "FS_N_ROW";
     
    /** Creates a new instance of EndReadsSpec */
   
    public FullSeqSpec(Hashtable p, String na, int submitter_id)
    {
         super( p,  na, submitter_id,FULL_SEQ_SPEC_INT);
         cleanup_parameters();
    }
    
     public FullSeqSpec(Hashtable p, String na, int submitter_id,int id) 
    {
         super( p,  na, submitter_id, FULL_SEQ_SPEC_INT,id);
         cleanup_parameters();
    }
     
   
    
    public boolean isIgnorByType(int type)
    {
        return false;
    }
 
    
     public static ArrayList getAllSpecs() throws BecDatabaseException
     {
         return getAllSpecsByType(FULL_SEQ_SPEC_INT, true);
     }
     public static ArrayList getAllSpecNames() throws BecDatabaseException
     {
         return getAllSpecsByType(FULL_SEQ_SPEC_INT, false);
     }
     
     public static ArrayList getAllSpecsBySubmitter(int submitter_id) throws BecDatabaseException
     {
         return getAllSpecsByTypeAndSubmitter(Spec.FULL_SEQ_SPEC_INT, submitter_id);
     }
     
     public int getDiscrepancyNumberByType(int quality, int changetype) throws BecDatabaseException 
     {
         switch (changetype)
         {
             case Mutation.TYPE_RNA_SILENT : case Mutation.TYPE_AA_SILENT:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_S_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_S_PASS_L");
             }
             case Mutation.TYPE_RNA_INFRAME_INSERTION: 
             case Mutation.TYPE_RNA_INFRAME :
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_IINS_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_IINS_PASS_L");
             }
             case Mutation.TYPE_RNA_MISSENSE : 
             case Mutation.TYPE_AA_CONSERVATIVE:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_C_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_C_PASS_L");
             }
             case Mutation.TYPE_RNA_NONSENSE:
             case Mutation.TYPE_AA_NONCONSERVATIVE :
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_NC_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_NC_PASS_L");
                 
             }
             case  Mutation.TYPE_RNA_FRAMESHIFT : 
             case Mutation.TYPE_RNA_FRAMESHIFT_DELETION :
             case Mutation.TYPE_RNA_FRAMESHIFT_INSERTION :
             case Mutation.TYPE_AA_FRAMESHIFT:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_FR_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_FR_PASS_L");
             }
             
             case Mutation.TYPE_RNA_INFRAME_DELETION:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_IDEL_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_IDEL_PASS_L");
             }
             case Mutation.TYPE_RNA_INFRAME_STOP_CODON :
             case Mutation.TYPE_RNA_FRAMESHIFT_STOP_CODON :
             case Mutation.TYPE_AA_TRUNCATION:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_TRANC_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_TRANC_PASS_L");
             }
             case Mutation.TYPE_AA_NO_TRANSLATION :
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_NOTRANSLATION_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_NOTRANSLATION_PASS_L");
             }
             case Mutation.TYPE_AA_POST_ELONGATION :
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     return  this.getParameterByNameInt("FS_PELONG_PASS_H");
                 }
                 else 
                     return this.getParameterByNameInt("FS_PELONG_PASS_L");
             }
             default: return CUT_OFF_VALUE_NOT_FOUND;
         }
        
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
     
     public int         getMaximumNumberOfAmbiquousBases() throws BecDatabaseException
     {
         return getParameterByNameInt("FS_N_100");
     }
     public int         getNumberOfConsequativeAmbiquousBases() throws BecDatabaseException
     {
         return getParameterByNameInt("FS_N_ROW");
     }
      //-------------------- mani -----------------------
     public static void main(String [] args) 
     {
        Connection c = null;
  ArrayList specs =null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
             specs =  FullSeqSpec.getAllSpecsBySubmitter(1);
            System.out.print(specs.size());
        }
        catch(Exception e){ System.out.print(specs.size());}
        System.exit(0);
     }
}
