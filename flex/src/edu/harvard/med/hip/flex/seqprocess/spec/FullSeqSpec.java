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
    public static final String FULL_SEQ_SPEC = "FULL_SEQ_SPEC";
    
    
    /** Creates a new instance of EndReadsSpec */
   
    public FullSeqSpec(Hashtable p, String na) throws FlexDatabaseException
    {
         super( p,  na, FULL_SEQ_SPEC);
    }
    public FullSeqSpec(int id) throws FlexDatabaseException
    {
         super( id, FULL_SEQ_SPEC);
    }
    
 
    
     public static ArrayList getAllSpecs() throws FlexDatabaseException
     {
         return getAllSpecs(FULL_SEQ_SPEC);
     }
}
