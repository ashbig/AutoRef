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
    
    
    /** Creates a new instance of EndReadsSpec */
   
    public Primer3Spec(Hashtable p, String na) throws FlexDatabaseException
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
  
}
