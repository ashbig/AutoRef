/*
 * RequestDescription.java
 *
 * Created on March 27, 2003, 11:00 AM
 */

package edu.harvard.med.hip.bec.sampletracking.Objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.*;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
import javax.sql.*;
/**
 *
 * @author  htaycher
 */
public class Request
{
    
    
    private int m_id = -1;
    private java.util.Date m_submitiondate = null;
    private int m_submitter_id = -1;
    private User m_submitter = null;
    private ArrayList m_process_ids = null;
    private ArrayList m_processes = null;
    
    /** Creates a new instance of RequestDescription */
    public Request(int id) throws BecDatabaseException
    {
    }
    
    public Request(int id, java.util.Date d, User u, ArrayList pr, int mode) throws BecDatabaseException
    {
        if ( id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) 
            m_id = BecIDGenerator.getID("requestid");
        else
            m_id = id;
         m_submitiondate = d;
         m_submitter  = u;
         if (mode == Constants.TYPE_ID)
            m_process_ids  = pr;
         else if (mode == Constants.TYPE_OBJECTS)
            m_processes  =pr;
    }
    
    public Request(int id,  java.util.Date d, int u, ArrayList pr, int mode) throws BecDatabaseException
    {
       if ( id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) 
            m_id = BecIDGenerator.getID("requestid");
        else
            m_id = id;
         m_submitiondate = d;
         m_submitter_id = u;
         
        if (mode == Constants.TYPE_ID)
            m_process_ids  = pr;
         else if (mode == Constants.TYPE_OBJECTS)
            m_processes  =pr;
    }
    
    //getters
    public int getId (){ return m_id  ;}
    public java.util.Date getSubmitionDate (){ return m_submitiondate;}
    public int getSubmitterId (){ return m_submitter_id  ;}
    public User getSubmitter (){ return m_submitter  ;}
    public ArrayList getProcessIds (){ return m_process_ids  ;}
    public ArrayList getProcesses (){ return m_processes  ;}
    
    
    public static ArrayList getAllRequestsByUser(User u)
    {
        return null;
    }
    
     public static ArrayList getAllRequestsByUserId( int u)
    {
        return null;
    }
     
     
    public static boolean isFinished( int requestId)
    {
        return false;
    }
    
    public void insert(Connection conn) throws BecDatabaseException
    {
    }
    
    
}
