/**
 * $Id: ProcessExecution.java,v 1.1 2003-04-07 18:46:50 Elena Exp $
 *
 * File     	: Process.java
 * Date     	: 04162001
 * define actual action that was requested by user
 **/
package edu.harvard.med.hip.bec.sampletracking.objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import java.sql.*;

/**
 * This class represents a process.
 */
public class ProcessExecution
{
    public static final int     STATUS_NOT_STARTED = -1;
    public static final int     STATUS_INPROCESS = 3;
    public static final int     STATUS_INITIATED = 2;
    public static final int     STATUS_FINISHED = 1;
    
    private int             m_executionid = -1;
    private String          m_process_code = null;//for plate labeling
    private int             m_processdefinition_id = -1;
    private int          m_request_id = -1;
    
    
    private int             m_status = STATUS_NOT_STARTED;
    private java.util.Date  m_date = null;
    //specs for the process to execute (can be none)
    private static   ArrayList   m_specs = null;
    private ArrayList       m_spec_ids = null;

    public ProcessExecution(int id) throws BecDatabaseException
    {
      
    }
    
    public ProcessExecution(int id, String code, int processdef, int requestid, ArrayList specar, int mode) throws BecDatabaseException
    {
       if (id == BecIDGenerator.BEC_OBJECT_ID_NOTSET)
            m_executionid = BecIDGenerator.getID("executionid");
        else
            m_executionid = id;
       //m_date = ;
       m_process_code = code;
       m_processdefinition_id = processdef;
       if (mode == Constants.TYPE_OBJECTS)
        {
            m_specs = specar;
            for(int i = 0; i < m_specs.size(); i++)
            {
                m_spec_ids.add( new Integer( ((Spec) m_specs.get(i)).getId()));
            }
        }
        else if (mode == Constants.TYPE_ID)
            m_spec_ids = specar;
        
    }
    
    
    public int             getExecutionId (){ return m_executionid  ;}
    public String          getProcessCode (){ return m_process_code  ;}//for plate labeling
    public int             getProcessDefinitionId (){ return m_processdefinition_id  ;}
    public int              getRequestId (){ return m_request_id  ;}
    public int             getStatus (){ return m_status  ;}
    public java.util.Date  getDate (){ return m_date  ;}
    //specs for the process to execute (can be none)
    public static   ArrayList   getSpecs (){ return m_specs  ;}
    public ArrayList       getSpecIds (){ return m_spec_ids  ;}
    
    
    
    public void              setRequestId (int v){  m_request_id = v ;}
    
    
    public void insert (Connection conn) throws BecDatabaseException
    {
       // private ArrayList       m_spec_ids;
        String sql ;
        if (m_spec_ids == null)
        {
            sql = "insert into processexecution "+
            "(executionid, processcode, requestid, processdefinitionid, executiondate, status)"+
            " values ("+m_executionid +","+ m_process_code +","+m_request_id 
            +","+ m_processdefinition_id +","+m_date +","+ m_status +","+m_request_id +")";
        }
        else
        {
            sql = "insert into processexecution "+
            "(executionid, processcode, requestid, processdefinitionid, executiondate, status,spec)"+
            " values ("+m_executionid +","+ m_process_code +","+m_request_id 
            +","+ m_processdefinition_id +","+m_date +","+ m_status +","+m_request_id +")";
        
        }
 
    
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
                    
        } catch (SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally
        {
            DatabaseTransaction.closeStatement(stmt);
        }
    
    }
    
    public void execute (Connection conn) throws BecDatabaseException
    {
    }
    
   
  
    //******************************************************/
    //			Test				//
    //******************************************************//
    
    // This test also includes testing for ProcessObject.java and its subclasses.
    public static void main(String [] args)
    {
        
        
       
    }
}
