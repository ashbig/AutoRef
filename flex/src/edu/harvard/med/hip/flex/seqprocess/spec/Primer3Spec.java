/*
 * Primer3Settings.java
 *
 * Created on September 13, 2002, 3:00 PM
 */

package edu.harvard.med.hip.flex.seqprocess.spec;



import  edu.harvard.med.hip.flex.util.*;
import  edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;
/**
 *
 * @author  htaycher
 * represent settings for 3 oligo calculator
 *direct mapping to db
 */
public class Primer3Spec extends Spec
{
     public static final String PRIMER3_SPEC = "PRIMER3_SPEC";
    public static final int PRIMER3_SPEC_INT = 3;
    
    /** Creates a new instance of EndReadsSpec */
   
    public Primer3Spec(Hashtable p, String na, int id) 
    {
         super( p,  na, PRIMER3_SPEC, id);
    }
    
    public Primer3Spec(Hashtable p, String na) 
    {
         super( p,  na, PRIMER3_SPEC);
    }
    
    public Primer3Spec(int id) throws FlexDatabaseException
    {
         super( id, PRIMER3_SPEC);
    }
    
 
    
     public static ArrayList getAllSpecs() throws FlexDatabaseException
     {
         return getAllSpecs(PRIMER3_SPEC);
     }
     protected void cleanup_parameters()    
     {    
         String k = null;
         
         for (Enumeration e = m_params.keys() ; e.hasMoreElements() ;)
         {
                k = (String)e.nextElement();
                if (k.length() < 3)
                {
                    m_params.remove(k);
                    continue;
                }
                if (! k.substring(0,2).equalsIgnoreCase("P_"))
                {
                    m_params.remove(k);
                }
                
         }
     

     }
   //-------------------- mani -----------------------
     public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            Hashtable h = new Hashtable();
           /*
          h.put("FORWARD","OOO");
            h.put("p_PRIMER_OPT","1");
           h.put("p_PRIMER_GC_MIN","1");
           
            h.put("p_DOWNSTREAM_DISTANCE","1");
            h.put("p_PRIMER_TM_OPT","1");
       
            h.put("p_NUMBER_OF_STRANDS","1");
            h.put("p_SINGLE_READ_LENGTH","1");
            h.put("p_PRIMER_MIN","1");
            h.put("p_PRIMER_GC_MAX","1");
            h.put("p_PRIMER_TM_MIN","1");
            h.put("p_PRIMER_MAX","1");
            h.put("p_PRIMER_TM_MAX","1");
            h.put("p_BUFFER_WINDOW_LEN","1");
            h.put("p_PRIMER_GC_OPT","1");

h.put("p_UPSTREAM_DISTANCE","1");

            Primer3Spec s = new Primer3Spec(h,"default");
           s.cleanup_parameters();
            s.insert(c);      
            c.commit();
           
          
      //       Primer3Spec s = new Primer3Spec(9);
         */
           ArrayList a = Spec.getAllSpecs(Primer3Spec.PRIMER3_SPEC);
              // a = Spec.getAllSpecs(EndReadsSpec.END_READS_SPEC);
               
               for (int count = 0; count < a.size() ; count++)
    {
	Primer3Spec spec = (Primer3Spec) a.get(count);
               spec.getParameterByNameString("p_primer_min".toUpperCase());
               spec.getParameterByNameInt("p_primer_opt".toUpperCase());
        }
        }
        catch(Exception e)
        {}
        System.exit(0);
     }
     
}
