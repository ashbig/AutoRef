/*
 * Primer3Settings.java
 *
 * Created on September 13, 2002, 3:00 PM
 */

package edu.harvard.med.hip.bec.coreobjects.spec;



import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
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
   
    
    /** Creates a new instance of EndReadsSpec */
   
    public Primer3Spec(Hashtable p, String na, int submitter_id,int id) 
    {
         super( p,  na, submitter_id,PRIMER3_SPEC_INT, id);
         cleanup_parameters();
    }
    
    public Primer3Spec(Hashtable p, String na, int submitter_id) 
    {
         super( p,  na, submitter_id,PRIMER3_SPEC_INT);
         cleanup_parameters();
    }
    
  
    
 
    
     public static ArrayList getAllSpecs() throws BecDatabaseException
     {
         return getAllSpecsByType(PRIMER3_SPEC_INT, true);
     }
     
     public static ArrayList getAllSpecNames() throws BecDatabaseException
     {
         return getAllSpecsByType(PRIMER3_SPEC_INT, false);
     }
     public static ArrayList getAllSpecsBySubmitter(int submitter_id) throws BecDatabaseException
     {
         return getAllSpecsByTypeAndSubmitter(Spec.PRIMER3_SPEC_INT, submitter_id);
     }
     
     protected void cleanup_parameters()    
     {    
         try
         {
            cleanup_parameters("P_");
        }
         catch(Exception e1)
         {
             System.out.println(e1.getMessage());
         }
     }
   //-------------------- mani -----------------------
     public static void main(String [] args) {
        Connection c = null;
        int oligoid = 1;
        
        try {
            /*
           
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            Hashtable h = new Hashtable();
          h.put("FORWARD","OOO");
            h.put("P_PRIMER_OPT","1");
           h.put("P_PRIMER_GC_MIN","1");
           
            h.put("P_DOWNSTREAM_DISTANCE","1");
            h.put("P_PRIMER_TM_OPT","1");
       
            h.put("P_NUMBER_OF_STRANDS","1");
            h.put("P_SINGLE_READ_LENGTH","1");
            h.put("P_PRIMER_MIN","1");
            h.put("P_PRIMER_GC_MAX","1");
            h.put("P_PRIMER_TM_MIN","1");
            h.put("P_PRIMER_MAX","1");
            h.put("P_PRIMER_TM_MAX","1");
            h.put("P_BUFFER_WINDOW_LEN","1");
            h.put("P_PRIMER_GC_OPT","1");

h.put("P_UPSTREAM_DISTANCE","1");

            Primer3Spec s = new Primer3Spec(h,"default","htaycher");
          
            s.insert(c);      
            c.commit();
           
          
      //       Primer3Spec s = new Primer3Spec(9);
         */
           ArrayList a = Primer3Spec.getAllSpecs();
              // a = Spec.getAllSpecs(EndReadsSpec.END_READS_SPEC);
         /*      
               for (int count = 0; count < a.size() ; count++)
    {
	Primer3Spec spec = (Primer3Spec) a.get(count);
               spec.getParameterByNameString("p_primer_min".toUpperCase());
               spec.getParameterByNameInt("p_primer_opt".toUpperCase());
        }
          **/
        }
        catch(Exception e)
        {}
        System.exit(0);
     }
     
     public boolean validateParameters()
     {
         return true;
     }
     
}
