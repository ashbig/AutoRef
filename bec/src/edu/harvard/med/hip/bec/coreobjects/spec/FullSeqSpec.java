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
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
/**
 *
 * @author  htaycher
 */
public class FullSeqSpec extends Spec
{
    public static final int CUT_OFF_VALUE_NOT_FOUND = -1000;
    
    
     public static final int MODE_PASS = 1;
      public static final int MODE_FAIL = 0;
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
     
     public int getDiscrepancyNumberByType(int quality, int changetype,int mode) throws BecDatabaseException 
     {
         switch (changetype)
         {
             case Mutation.MACRO_SPECTYPE_SILENT:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_S_PASS_H");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_S_FAIL_H");
                 }
                 else 
                 {
                     if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_S_PASS_L");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_S_FAIL_L");
                 }
                  
             }
             case Mutation.MACRO_SPECTYPE_INFRAME :
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_IINS_PASS_H");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_IINS_FAIL_H");
                 }
                 else 
                 {
                      if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_IINS_PASS_L");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_IINS_FAIL_L");
                 }
                  
             }
             case Mutation.MACRO_SPECTYPE_CONSERVATIVE:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                      if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_C_PASS_H");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_C_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_C_PASS_L");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_C_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_NONCONSERVATIVE :
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                    if ( mode == MODE_PASS)
                        return  this.getParameterByNameInt("FS_NC_PASS_H");
                     else if( mode == MODE_FAIL)
                        return  this.getParameterByNameInt("FS_NC_FAIL_H");
                 }
                 else 
                 {
                        if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NC_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NC_FAIL_L");
                 }
             }
             case  Mutation.MACRO_SPECTYPE_FRAMESHIFT:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                     if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_FR_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_FR_FAIL_H");
                 }
                 else 
                 {
                     if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_FR_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_FR_FAIL_L");
                 }
             }
             
             case Mutation.MACRO_SPECTYPE_INFRAME_DELETION:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_IDEL_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_IDEL_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_IDEL_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_IDEL_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_TRANCATION:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_TRANC_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_TRANC_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_TRANC_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_TRANC_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_NO_TRANSLATION:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NOTRANSLATION_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NOTRANSLATION_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NOTRANSLATION_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NOTRANSLATION_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_POST_ELONGATION:
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_PELONG_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_PELONG_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_PELONG_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_PELONG_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_LINKER_5_SUBSTITUTION  : 
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_5S_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_5S_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_5S_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_5S_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_LINKER_3_SUBSTITUTION  :  
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_3S_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_3S_FAIL_H");
                 }
                 else
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_3S_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_3S_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_LINKER_5_INS_DEL  :   
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_5DI_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_5DI_FAIL_H");
                 }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_5DI_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_5DI_FAIL_L");
                 }
             }
             case Mutation.MACRO_SPECTYPE_LINKER_3_INS_DEL  :   
             {
                 if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_3DI_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_3DI_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_3DI_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_3DI_FAIL_L");
                 }
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
             FullSeqSpec spec = (FullSeqSpec) Spec.getSpecById(12);
             
            System.out.println( spec.getDiscrepancyNumberByType(0, Mutation.MACRO_SPECTYPE_SILENT,0)  );
              System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_SILENT,0)  );
                System.out.println( spec.getDiscrepancyNumberByType(0, Mutation.MACRO_SPECTYPE_SILENT,1)  );
                  System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_SILENT,1)  );
                    System.out.println( spec.getDiscrepancyNumberByType(2, Mutation.MACRO_SPECTYPE_SILENT,1)  );
                    
                    
                    
            System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_NONCONSERVATIVE,0)  );
            System.out.println( spec.getDiscrepancyNumberByType(2, Mutation.MACRO_SPECTYPE_POST_ELONGATION,0)  );
            System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_SILENT,1)  );
            System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_TRANCATION,0)  );    
            System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_NONCONSERVATIVE,1)  );
            
            System.out.println( spec.getDiscrepancyNumberByType(0, Mutation.MACRO_SPECTYPE_LINKER_5_SUBSTITUTION,0)  );
            System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_LINKER_5_SUBSTITUTION,1)  );
            System.out.println( spec.getDiscrepancyNumberByType(2, Mutation.MACRO_SPECTYPE_POST_ELONGATION,1)  );
            System.out.println( spec.getDiscrepancyNumberByType(0, Mutation.MACRO_SPECTYPE_LINKER_5_INS_DEL,1)  );
            System.out.println( spec.getDiscrepancyNumberByType(1, Mutation.MACRO_SPECTYPE_LINKER_5_INS_DEL,0)  );    
            System.out.println( spec.getDiscrepancyNumberByType(2, Mutation.MACRO_SPECTYPE_LINKER_5_INS_DEL,0)  );
        }
        catch(Exception e){ System.out.print(specs.size());}
        System.exit(0);
     }
}
