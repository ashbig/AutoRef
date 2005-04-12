/*
 * FullSeqSpec.java
 *
 * Created on September 20, 2002, 11:49 AM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;


import java.util.*;
import edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import edu.harvard.med.hip.bec.*;
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
              case Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_CDS :
              {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NCDS_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NCDS_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NCDS_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NCDS_FAIL_L");
                 } //N substitution – gene region
              }
            case Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_LINKER5 :
            {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N5SUB_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N5SUB_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N5SUB_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N5SUB_FAIL_L");
                 } 
              }//N substitution – linker region
            case Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_LINKER3 : 
            {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N3SUB_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N3SUB_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N3SUB_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N3SUB_FAIL_L");
                 } 
              }//N substitution – linker region
            case Mutation.MACRO_SPECTYPE_N_INSERTION_LINKER5 : 
            {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N5INS_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N5INS_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N5INS_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N5INS_FAIL_L");
                 } 
              }//N substitution – linker region//N substitution – linker region
            case Mutation.MACRO_SPECTYPE_N_INSERTION_LINKER3 : 
            {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N3INS_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N3INS_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_N3INS_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_N3INS_FAIL_L");
                 } 
              }//N substitution – linker region

            case Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_START_CODON :
                {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NSTART_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NSTART_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NSTART_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NSTART_FAIL_L");
                 } 
              }//N substitution – linker region//N substitution start codon
            case Mutation.MACRO_SPECTYPE_N_SUBSTITUTION_STOP_CODON :
                {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NSTOP_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NSTOP_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NSTOP_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NSTOP_FAIL_L");
                 } 
              }//N substitution – linker region//N substitution stop codon
            case Mutation.MACRO_SPECTYPE_N_FRAMESHIFT_INSERTION : 
                {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NFRAME_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NFRAME_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NFRAME_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NFRAME_FAIL_L");
                 } 
              }//N substitution – linker region// N frameshift (introduced only by N)
            case Mutation.MACRO_SPECTYPE_N_INFRAME_INSERTION : 
                {
                  if  (quality == Mutation.QUALITY_NOTKNOWN || quality == Mutation.QUALITY_HIGH) 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NINFRAME_PASS_H");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NINFRAME_FAIL_H");
                  }
                 else 
                 {
                       if ( mode == MODE_PASS)
                            return  this.getParameterByNameInt("FS_NINFRAME_PASS_L");
                        else if( mode == MODE_FAIL)
                            return  this.getParameterByNameInt("FS_NINFRAME_FAIL_L");
                 } 
              }//N substitution – linker region
             default: return CUT_OFF_VALUE_NOT_FOUND;
         }
        
     }
     
     
      public boolean isIgnorPolymorphismByType( int changetype) throws BecDatabaseException 
     {
         switch (changetype)
         {
             case Mutation.MACRO_SPECTYPE_SILENT:
             {
                if ( this.getParameterByName("FS_S_POLM") == null)
                        return  false;
                else 
                        return true;
             }
             case Mutation.MACRO_SPECTYPE_INFRAME :
             {
                 if  (this.getParameterByName("FS_IINS_POLM") == null) 
                    return false;
                 else 
                     return true;
                    
             }
             case Mutation.MACRO_SPECTYPE_CONSERVATIVE:
             {
                 if  (this.getParameterByName("FS_C_POLM")== null)
                     return false;
                 else
                     return true;
             }
             case Mutation.MACRO_SPECTYPE_NONCONSERVATIVE :
             {
                  if  (this.getParameterByName("FS_NC_POLM") == null)
                     return false;
                 else
                     return true;
             }
             case  Mutation.MACRO_SPECTYPE_FRAMESHIFT:
             {
                  if  (this.getParameterByName("FS_FR_POLM") == null)
                     return false;
                 else
                     return true;
             }
             case Mutation.MACRO_SPECTYPE_INFRAME_DELETION:
             {
                  if  (this.getParameterByName("FS_IDEL_POLM") == null)
                     return false;
                 else
                     return true;
             }
             case Mutation.MACRO_SPECTYPE_TRANCATION:
             {
                  if  (this.getParameterByName("FS_TRANC_POLM") == null)
                     return false;
                 else
                     return true;
             }
             case Mutation.MACRO_SPECTYPE_NO_TRANSLATION:
             {
                  if  (this.getParameterByName("FS_NOTRANSLATION_POLM") == null)
                     return false;
                 else
                     return true;
             }
             case Mutation.MACRO_SPECTYPE_POST_ELONGATION:
             {
                  if  (this.getParameterByName("FS_PELONG_POLM") == null)
                     return false;
                 else
                     return true;
             }
             default: return false;
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
      
 
   protected  String print_parameter_definitions(String param_separator)  throws Exception
      {
          StringBuffer sf = new StringBuffer();
          sf.append(" Maximum acceptable number of discrepancies (gene region)" + param_separator);
 
 sf.append("Base Confidence:"+Constants.TAB_DELIMETER+"High"+Constants.TAB_DELIMETER+"Low"+Constants.TAB_DELIMETER+"High"+Constants.TAB_DELIMETER+"Low" + param_separator);
 sf.append("Silent mutation"+Constants.TAB_DELIMETER  + this.getParameterByNameInt("FS_S_PASS_H") +Constants.TAB_DELIMETER  +    this.getParameterByNameInt("FS_S_PASS_L")  +Constants.TAB_DELIMETER   );  
 sf.append(   this.getParameterByNameInt("FS_S_PASS_L")+"\t"  +  this.getParameterByNameInt("FS_S_FAIL_L")+ param_separator);
 
 sf.append("Conservative substitution"  +Constants.TAB_DELIMETER );
 sf.append(this.getParameterByNameInt("FS_C_PASS_H")+Constants.TAB_DELIMETER  + this.getParameterByNameInt("FS_C_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_C_PASS_L")+Constants.TAB_DELIMETER  +  this.getParameterByNameInt("FS_C_FAIL_L") + param_separator);
 
 sf.append("Nonconservative substitution"      +Constants.TAB_DELIMETER);
 sf.append(this.getParameterByNameInt("FS_NC_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_NC_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_NC_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_NC_FAIL_L")+ param_separator);
 
 
 sf.append("Frameshift"    +Constants.TAB_DELIMETER );  
 sf.append(this.getParameterByNameInt("FS_FR_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_FR_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_FR_PASS_L")+Constants.TAB_DELIMETER  + this.getParameterByNameInt("FS_FR_FAIL_L")+ param_separator);
 
 sf.append("Inframe deletion" +Constants.TAB_DELIMETER  );  
 sf.append( this.getParameterByNameInt("FS_IDEL_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_IDEL_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_IDEL_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_IDEL_FAIL_L")+ param_separator);
 
 sf.append("Inframe insertion"   +Constants.TAB_DELIMETER  );     
 sf.append(this.getParameterByNameInt("FS_IINS_PASS_H")+Constants.TAB_DELIMETER  +  this.getParameterByNameInt("FS_IINS_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_IINS_PASS_L")+Constants.TAB_DELIMETER  +  this.getParameterByNameInt("FS_IINS_FAIL_L")+ param_separator);
 
 sf.append("Truncation"+Constants.TAB_DELIMETER  );       
 sf.append(this.getParameterByNameInt("FS_TRANC_PASS_H")+Constants.TAB_DELIMETER  + this.getParameterByNameInt("FS_TRANC_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_TRANC_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_TRANC_FAIL_L")+ param_separator);
 
 sf.append("No translation (e.g., no ATG)"  +Constants.TAB_DELIMETER  );  
 sf.append( this.getParameterByNameInt("FS_NOTRANSLATION_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_NOTRANSLATION_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_NOTRANSLATION_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_NOTRANSLATION_FAIL_L")+ param_separator);
 
 sf.append("Post-elongation(e.g., no stop codon)"+Constants.TAB_DELIMETER  );             
 sf.append(  this.getParameterByNameInt("FS_PELONG_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_PELONG_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append(  this.getParameterByNameInt("FS_PELONG_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_PELONG_FAIL_L")+ param_separator);
 
 sf.append("Maximum acceptable number of discrepancies (linker region):" + param_separator); 
 
 sf.append("Base Confidence"+ Constants.TAB_DELIMETER +"High"+ Constants.TAB_DELIMETER +"Low"+ Constants.TAB_DELIMETER +"High"+ Constants.TAB_DELIMETER +"Low"+ param_separator); 
 sf.append("5' substitution"  +Constants.TAB_DELIMETER  );    
 sf.append(this.getParameterByNameInt("FS_5S_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_5S_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_5S_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_5S_FAIL_L")+ param_separator);
 
 sf.append("5' deletion/insertion"+Constants.TAB_DELIMETER  );     
 sf.append( this.getParameterByNameInt("FS_5DI_PASS_H")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_5DI_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_5DI_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_5DI_FAIL_L")+ param_separator);
 
 sf.append("3' substitution"+Constants.TAB_DELIMETER  );        
 sf.append(this.getParameterByNameInt("FS_3S_PASS_H")+Constants.TAB_DELIMETER +this.getParameterByNameInt("FS_3S_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_3S_PASS_L")+Constants.TAB_DELIMETER  +this.getParameterByNameInt("FS_3S_FAIL_L")+ param_separator);
 
 sf.append("3' deletion/insertion"+Constants.TAB_DELIMETER  );          
  
 sf.append( this.getParameterByNameInt("FS_3DI_PASS_H")+Constants.TAB_DELIMETER+ this.getParameterByNameInt("FS_3DI_FAIL_H")+Constants.TAB_DELIMETER );
 sf.append( this.getParameterByNameInt("FS_3DI_PASS_L")+Constants.TAB_DELIMETER+this.getParameterByNameInt("FS_3DI_FAIL_L")+ param_separator);
 
 
 
          return sf.toString();
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
             FullSeqSpec spec = (FullSeqSpec) Spec.getSpecById(20);
             System.out.println( spec.getParameterByNameString("FS_S_POLM"));
        /*     
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
        */
         }
        catch(Exception e){ System.out.print(specs.size());}
        System.exit(0);
     }
     
   
     
}
