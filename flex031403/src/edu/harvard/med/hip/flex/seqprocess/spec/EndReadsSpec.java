/*
 * EndReadsSpec.java
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
public class EndReadsSpec extends Spec
{
    
   
    
    /** Creates a new instance of EndReadsSpec */
   
    public EndReadsSpec(Hashtable p, String na, String submitter) 
    {
        super( p,  na, submitter, END_READS_SPEC_INT);
        cleanup_parameters();
    }
    
     public EndReadsSpec(Hashtable p, String na, String submitter,int id) 
    {
         super( p,  na, submitter,END_READS_SPEC_INT, id);
         cleanup_parameters();
    }
    
      
 
    
     public static ArrayList getAllSpecs() throws FlexDatabaseException
     {
         return getAllSpecsByType(END_READS_SPEC_INT, true);
     }
     
     public static ArrayList getAllSpecNames() throws FlexDatabaseException
     {
         return getAllSpecsByType(END_READS_SPEC_INT, false);
     }
     
     public static ArrayList getAllSpecsBySubmitter(String submitter) throws FlexDatabaseException
     {
         return getAllSpecsByTypeAndSubmitter(Spec.END_READS_SPEC_INT, submitter);
     }
     
     public String getPhredParams() throws FlexDatabaseException
     {
         String res = "";
         res += super.getParameterByNameInt("PHRED_CUTOFF");
         if (getParameterByNameInt("PHRED_TRIM") != -1) res += " -trim ";
         if (getParameterByNameInt("PHRED_TRIM_ALT") != -1) res += " -trim_alt ";
         return res;
     }
     
     
     
     /*
     protected void cleanup_parameters()
     {
          String k = null;
          for (Enumeration eq = m_params.keys() ; eq.hasMoreElements() ;)
         {
              k = (String)eq.nextElement();
             System.out.println( k +"_"+ m_params.get(k));
                 }
                
         for (Enumeration e = m_params.keys() ; e.hasMoreElements() ;)
         {
             try
             {
                k = (String)e.nextElement();
                System.out.println(k);
               if ( k.length() < 3)
               {
                   m_params.remove(k);
                   continue;
               }
                if ( ! k.substring(0,2).equalsIgnoreCase("E_") )
                {
                    m_params.remove(k);
                }
                //analysis
                if ( m_params.get("E_isLowScore").equals("0"))
                {
                    
                        m_params.remove("E_ER_HQ_SILENT");
                        m_params.remove("E_ER_LQ_SILENT");
                        m_params.remove("E_ER_HQ_CONSERVATIVE");
                        m_params.remove("E_ER_LQ_CONSERVATIVE");
                        m_params.remove("E_ER_HQ_NON_CONSERVATIVE" );
                        m_params.remove("E_ER_LQ_NON_CONSERVATIVE");
                        m_params.remove("E_ER_HQ_FRAMESHIFT");
                        m_params.remove("E_ER_LQ_FRAMESHIFT" );
                        m_params.remove("E_ER_HQ_STOP");
                        m_params.remove("E_ER_LQ_STOP");
                }
              else if ( m_params.get("E_isLowScore").equals("1"))
              {
                    m_params.remove("E_ER_HIGH_QUAL");
                    m_params.remove("E_ER_LOW_QUAL");
              }
                //trimming
              if ( m_params.get("E_isTrim").equals("0"))//end trims
              {
                  m_params.remove("E_ER_VECTOR" );
                  if ( m_params.get("E_isPhredTrimMode").equals("0") )
                        m_params.remove("E_ER_PHRED_TRIM_PR");
              }
              else if ( m_params.get("E_isTrim").equals("1"))
              {
                    if (m_params.get("E_TRIM_P").equals("1") )
                    {
                         //  "E_TRIM_1_BASES" 
                          // "E_TRIM_1_AMB" 
                          m_params.remove("E_TRIM_2_BASE");
                         m_params.remove("E_TRIM_2_AMB" );
                         m_params.remove("E_TRIM_2_CONF" );
                     m_params.remove("E_TRIM_3_BASE");

                    }
                     if ( m_params.get("E_TRIM_P").equals("2") )
                     {
                         //"E_TRIM_2_BASE" size="5" value="25">
                         //"E_TRIM_2_AMB" 
                         //"E_TRIM_2_CONF" 
                         
                         
                          m_params.remove("E_TRIM_1_BASES" );
                           m_params.remove("E_TRIM_1_AMB" );
                           m_params.remove("E_TRIM_3_BASE");
                     }
                      if ( m_params.get("E_TRIM_P").equals("3") ) 
                      {
                        m_params.remove("E_TRIM_1_BASES" );
                         m_params.remove(  "E_TRIM_1_AMB" );
                           
                        m_params.remove(   "E_TRIM_2_BASE");
                         m_params.remove("E_TRIM_2_AMB" );
                        m_params.remove( "E_TRIM_2_CONF" );
                     //"E_TRIM_3_BASE"
                      }
                      if ( m_params.get("E_TRIM_R").equals("1") )
                      {
                          //  "E_TRIM_4_BASE" 
                          //  "E_TRIM_4_FBASE" 
                           // "E_TRIM_4_AMB" 
                                m_params.remove( "E_TRIM_6_BASE" );
                              m_params.remove("E_TRIM_6_FBASE" );
                              m_params.remove("E_TRIM_6_CONF" );
                            
                              m_params.remove("E_TRIM_5_BASE" );
                            m_params.remove("E_TRIM_5_AMB");
                      }
                      if (m_params.get("E_TRIM_R").equals("2") )
                      {
                         // "E_TRIM_5_BASE" 
                         // "E_TRIM_5_AMB"
                          
                            m_params.remove("E_TRIM_4_BASE" );
                             m_params.remove( "E_TRIM_4_FBASE"); 
                              m_params.remove("E_TRIM_4_AMB" );
                                m_params.remove( "E_TRIM_6_BASE"); 
                              m_params.remove("E_TRIM_6_FBASE" );
                              m_params.remove("E_TRIM_6_CONF" );
                      }
                      if (m_params.get("E_TRIM_R").equals("3") )
                      {
                          
                              m_params.remove("E_TRIM_4_BASE" );
                              m_params.remove("E_TRIM_4_FBASE" );
                              m_params.remove("E_TRIM_4_AMB" );
                              m_params.remove("E_TRIM_5_BASE" );
                              m_params.remove("E_TRIM_5_AMB");
                            
                            
                          //  "E_TRIM_6_BASE" 
                          //  "E_TRIM_6_FBASE" 
                          //  "E_TRIM_6_CONF" 
                      }
              }
             }
             catch(Exception e1)
             {
                 System.out.println(e1.getMessage());
             }
                 for (Enumeration e1 = m_params.keys() ; e.hasMoreElements() ;)
         {
             System.out.println( (String)e1.nextElement());
                 }
                
         }
     }
      **/
      protected void cleanup_parameters()
     {
         String k = null;
         
             try
             {
                cleanup_parameters("ER_");
             
                //analysis
               /* if ( m_params.get("E_isLowScore").equals("0"))
                {
                    
                        m_params.remove("E_ER_HQ_SILENT");
                        m_params.remove("E_ER_LQ_SILENT");
                        m_params.remove("E_ER_HQ_CONSERVATIVE");
                        m_params.remove("E_ER_LQ_CONSERVATIVE");
                        m_params.remove("E_ER_HQ_NON_CONSERVATIVE" );
                        m_params.remove("E_ER_LQ_NON_CONSERVATIVE");
                        m_params.remove("E_ER_HQ_FRAMESHIFT");
                        m_params.remove("E_ER_LQ_FRAMESHIFT" );
                        m_params.remove("E_ER_HQ_STOP");
                        m_params.remove("E_ER_LQ_STOP");
                }
                **/
               if ( m_params.get("E_isLowScore").equals("0"))
              {
                    m_params.remove("E_ER_HIGH_QUAL");
                    m_params.remove("E_ER_LOW_QUAL");
              }
                //trimming
              if ( m_params.get("E_isTrim").equals("0"))//no trimming
              {
                  m_params.remove("E_ER_VECTOR" );
                  m_params.remove("E_isPhredTrimMode");
                  m_params.remove("E_ER_PHRED_TRIM_PR");
              }
              else
              {
                  if ( m_params.get("E_isPhredTrimMode").equals("0") )//phred trimming using
                  {
                        m_params.remove("E_ER_PHRED_TRIM_PR");
                  }
              }
          
            
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
     public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            Hashtable h = new Hashtable();
          
         h.put("ER_LQ_STOP","50");
h.put("ER_LOW_QUAL","3");
h.put("ER_LQ_FRAMESHIFT","50");
h.put("isTrim","1");
h.put("ER_HQ_NON_CONSERVATIVE","18");
h.put("ER_PHRED_TRIM_PR","0.05");
h.put("ER_LQ_NON_CONSERVATIVE","3");
h.put("ER_HQ_CONSERVATIVE","3");
h.put("ER_HIGH_QUAL","2");
h.put("B1","Submit");
h.put("ER_LQ_CONSERVATIVE","1");
h.put("ER_VECTOR","");
h.put("SET_NAME","default");
h.put("ER_LQ_SILENT","1");
h.put("ER_PHRED_CUT_OFF","20");
h.put("ER_HQ_SILENT","2");
h.put("ER_PHRED_LOW_CUT_OFF","10");
h.put("ER_HQ_STOP","100");
h.put("isPhredTrimMode","1");
h.put("ER_HQ_FRAMESHIFT","100");
h.put("forwardName","1");
//check for name unique, if not add _1 to user selected name
            String spec_name_suffix = "";
            spec_name_suffix = Spec.getNameSuffix("default", Spec.END_READS_SPEC_INT) ;
            String spec_name = "default"+spec_name_suffix;
            
            EndReadsSpec s = new EndReadsSpec(h,spec_name,"htaycher");
          
            //s.insert(c);      
           // c.commit();
           
          
      //       Primer3Spec s = new Primer3Spec(9);
         /*
           ArrayList a = Spec.getAllSpecsByType(Primer3Spec.PRIMER3_SPEC_INT);
              // a = Spec.getAllSpecs(EndReadsSpec.END_READS_SPEC);
               
               for (int count = 0; count < a.size() ; count++)
    {
	Primer3Spec spec = (Primer3Spec) a.get(count);
               spec.getParameterByNameString("p_primer_min".toUpperCase());
               spec.getParameterByNameInt("p_primer_opt".toUpperCase());
        }*/
        }
        catch(Exception e)
        {}
        System.exit(0);
     }
   
}
