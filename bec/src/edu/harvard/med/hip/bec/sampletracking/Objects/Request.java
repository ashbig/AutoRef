/*
 * RequestDescription.java
 *
 * Created on March 27, 2003, 11:00 AM
 */

package edu.harvard.med.hip.bec.sampletracking.objects;

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
    
    public static final int REQUEST_FINISHED = 1;
    public static final int REQUEST_NOT_KNOWN = 0;
    public static final int REQUEST_NOT_FINISHED = -1;
    
    private int m_id = -1;
    private java.util.Date m_submissiondate = null;
    private int m_submitter_id = BecIDGenerator.BEC_OBJECT_ID_NOTSET;
    private User m_submitter = null;
    private ArrayList m_process_ids = null;
    private ArrayList m_processes = null;
    private int   m_isFinished = REQUEST_NOT_KNOWN;
    
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
         m_submissiondate = d;
         m_submitter  = u;
         m_submitter_id = u.getId();
         if (mode == Constants.TYPE_ID)
            m_process_ids  = pr;
         else if (mode == Constants.TYPE_OBJECTS)
         {
             m_process_ids = new ArrayList();
            m_processes  =pr;
            ProcessExecution pre = null;
            for(int i = 0; i < m_processes.size(); i++)
            {
                pre = (ProcessExecution) m_processes.get(i);
                m_process_ids.add( new Integer( pre.getId()));
            }
           
         }
    }
    
    public Request(int id,  java.util.Date d, int u, ArrayList pr, int mode) throws BecDatabaseException
    {
       if ( id == BecIDGenerator.BEC_OBJECT_ID_NOTSET) 
            m_id = BecIDGenerator.getID("requestid");
        else
            m_id = id;
         m_submissiondate = d;
         m_submitter_id = u;
         
        if (mode == Constants.TYPE_ID)
            m_process_ids  = pr;
         else if (mode == Constants.TYPE_OBJECTS)
         {
             m_process_ids = new ArrayList();
            m_processes  =pr;
            ProcessExecution pre = null;
            for(int i = 0; i < m_processes.size(); i++)
            {
                pre = (ProcessExecution) m_processes.get(i);
                m_process_ids.add( new Integer( pre.getId()));
            }
        }
    }
    
    //getters
    public int getId (){ return m_id  ;}
    public java.util.Date getSubmissionDate(){ return m_submissiondate;}
    public int getSubmitterId (){ return m_submitter_id  ;}
    public User getSubmitter (){ return m_submitter  ;} 
    public ArrayList getProcessIds (){ return m_process_ids  ;}
    public ArrayList getProcesses (){ return m_processes  ;}
    public void      addProcess(ProcessExecution p)
    { 
        if (p.getRequestId() == -1) p.setRequestId( this.getId());
        m_processes.add(p);
    }
    
    public static ArrayList getAllRequestsByUser(User u)
    {
        return getAllRequestsByUserId(  u.getId() );
    }
    
     public static ArrayList getAllRequestsByUserId( int u)
    {
        return null;
    }
     
     
    public  int isFinished( int requestId)
    {
        
        return m_isFinished;
    }
    
    public void insert(Connection conn) throws BecDatabaseException
    {
        if (  m_processes == null)
            throw new BecDatabaseException("No process attached to the request");
        String sql = "insert into request(requestid, submissiondate, researcherid)"+
        " values ("+m_id +",sysdate,"+m_submitter_id + ")";
        
      
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            for (int ind = 0 ; ind < m_processes.size() ;ind++)
            {
                ProcessExecution pr = (ProcessExecution)m_processes.get(ind);
                System.out.print("l-"+ind);
                pr.insert(conn);
                System.out.print(ind);
            }
            
           
        } 
        catch (Exception sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } 
        finally
        {
          DatabaseTransaction.closeStatement(stmt);
          
        }
    }
    
    
}
