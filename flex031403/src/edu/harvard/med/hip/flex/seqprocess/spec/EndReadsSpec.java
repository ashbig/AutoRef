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
    
    public static final String END_READS_SPEC = "END_READS_SPEC";
    public static final int    END_READS_SPEC_INT = 0;
    
    /** Creates a new instance of EndReadsSpec */
   
    public EndReadsSpec(Hashtable p, String na) throws FlexDatabaseException
    {
         super( p,  na, END_READS_SPEC);
    }
    
     public EndReadsSpec(Hashtable p, String na, int id) throws FlexDatabaseException
    {
         super( p,  na, END_READS_SPEC, id);
    }
    public EndReadsSpec(int id) throws FlexDatabaseException
    {
              super(id, END_READS_SPEC);
    }
    
    
 
    
     public static ArrayList getAllSpecs() throws FlexDatabaseException
     {
         return getAllSpecs(END_READS_SPEC);
     }
     
     public String getPhredParams()
     {
         String res = "";
         res += super.getParameterByNameInt("PHRED_CUTOFF");
         if (getParameterByNameInt("PHRED_TRIM") != -1) res += " -trim ";
         if (getParameterByNameInt("PHRED_TRIM_ALT") != -1) res += " -trim_alt ";
         return res;
     }
     
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
     
}
