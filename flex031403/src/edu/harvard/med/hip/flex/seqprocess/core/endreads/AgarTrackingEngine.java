/*
 * AgarTracking.java
 *
 * Created on September 24, 2002, 4:14 PM
 */

package edu.harvard.med.hip.flex.seqprocess.core.endreads;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.seqprocess.spec.*;
/**
 *
 * @author  htaycher
 */
public class AgarTrackingEngine
{
    public static final int     FORMAT_OPEN =0;
    public static final int     FORMAT_CLOSE = 1;
    
    
    private int         m_id;//engine id
    private int         m_agar_id = - 1;//– identifies the agar
    private ArrayList   m_isolates = null;// – refers to four Isolate objects
   
    private int         m_current_index = -1;//– id of isolate that is currently processing for full sequencing
 
    private int         m_refsequence_id = -1;//– can be extracted from the original sample
    private int         m_format = FORMAT_OPEN;//sequence format
    
    private SpecGroup   m_spec_group = null;	
    private int         m_spec_group_id = -1;	

    /** Creates a new instance of AgarTracking */
    public AgarTrackingEngine()
    {
    }
    /** Creates a new instance of AgarTracking */
    public AgarTrackingEngine(int id)throws FlexDatabaseException
    {
    }
    
    
    public void insert(Connection conn) throws FlexDatabaseException
    {
    }
    
    public int        getId(){ return  m_id;}//engine id
    public int        getAgarId(){ return  m_agar_id; }//– identifies the agar
    public ArrayList  getIsolatesId(){ return  m_isolates ;}// – refers to four Isolate objects
    public int        getCurrentIsolateId(){ return  m_current_index ;}//– id of isolate that is currently processing for full sequencing
    public int        getRefSeqId(){ return  m_refsequence_id;}//– can be extracted from the original sample
    public int        getFormat(){ return  m_format;}//sequence format
    public SpecGroup  getSpecGroup(){ return  m_spec_group ;}	
    public int        getSpecGroupId(){ return  m_spec_group_id ;}	
    
    public void assignRank()
    {
        //sort isolate tracking by score
      
        Collections.sort(m_isolates, new Comparator()
        {
            public int compare(Object o1, Object o2)
            {
                IsolateTrackingEngine is1 = (IsolateTrackingEngine) o1;
                IsolateTrackingEngine is2 = (IsolateTrackingEngine) o2;
                return is1.getScore() - is2.getScore();
            }
            /** Note: this comparator imposes orderings that are
             * inconsistent with equals. */
            public boolean equals(java.lang.Object obj)
            {      return false;  }
            // compare
        } );
        //assign rank
        for (int count = 0; count < m_isolates.size(); count++)
        {
            IsolateTrackingEngine is = (IsolateTrackingEngine) m_isolates.get(count);
            if (is.getScore() < IsolateTrackingEngine.MIN_ALLOWED_SCORE)
                is.setRank(0);
            else
                is.setRank(count+1);
        }
    }
    
    public void updateCurrentIndex(int ind)throws FlexDatabaseException
    {
        m_current_index = ind;
    }
  //------------------------- private ----------------------  
     
}
