/*
 * BioGap.java
 *
 * Created on April 16, 2003, 11:58 AM
 */

package edu.harvard.med.hip.bec.coreobjects.sequence;

import  edu.harvard.med.hip.bec.util.*;
import  edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  htaycher
 */
public class BioGap
{
    
    private int m_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private int m_resultid = -1;
    private int m_start = -1;
    private int m_end = -1;
    private int m_refsequence_id = -1;
    
    /** Creates a new instance of BioGap */
    public BioGap()
    {
    }
    
    public int getId (){ return m_id ;}
    public int getResultId (){ return m_resultid ;}
    public int getStart (){ return m_start ;}
    public int getEnd (){ return m_end ;}
    public int getRefsequenceId (){ return m_refsequence_id ;}
    
    
    public void setId (int v){   m_id =  v ;}
    public void setResultId (int v){   m_resultid = v ;}
    public void setStart (int v){   m_start = v ;}
    public void setEnd (int v){   m_end = v ;}
    public void setRefsequenceId (int v){   m_refsequence_id = v ;}
    
    
     public void insert(Connection conn) throws BecDatabaseException
    {
     }
     
     public static ArrayList getGapsBySequenceId(int id)throws BecDatabaseException
     {
         return null;
     }
    
}
