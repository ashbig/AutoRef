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
    
    
    /** Creates a new instance of EndReadsSpec */
   
    public EndReadsSpec(Hashtable p, String na) throws FlexDatabaseException
    {
         super( p,  na, END_READS_SPEC);
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
         res += super.getParameterByName("PHRED_CUTOFF");
         if (getParameterByName("PHRED_TRIM") != -1) res += " -trim ";
         if (getParameterByName("PHRED_TRIM_ALT") != -1) res += " -trim_alt ";
         return res;
     }
}
