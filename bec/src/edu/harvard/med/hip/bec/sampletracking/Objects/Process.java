/**
 * $Id: Process.java,v 1.1 2003-03-27 17:45:42 Elena Exp $
 *
 * File     	: Process.java
 * Date     	: 04162001
 * define actual action that was requested by user
 **/
package edu.harvard.med.hip.bec.sampletracking.Objects;

import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.core.spec.*;
import edu.harvard.med.hip.bec.*;
import java.util.*;
import java.sql.*;

/**
 * This class represents a process.
 */
public class Process
{
    public static final int     STATUS_NOT_STARTED = -1;
    public static final int     STATUS_INPROCESS = 3;
    public static final int     STATUS_INITIATED = 2;
    public static final int     STATUS_FINISHED = 1;
    
    private int             m_executionid = -1;
    private String          m_process_code = null;//for plate labeling
    private int             m_processdefinition_id = -1;
    private String          m_request_id = null;
    
    
    private int             m_status = STATUS_NOT_STARTED;
    private java.util.Date  m_date = null;
    //specs for the process to execute (can be none)
    private static   ArrayList   m_specs = null;
    private ArrayList       m_spec_ids = null;

    public Process(int id) throws BecDatabaseException
    {
      
    }
    
    protected Process(int id, String code, int processdef, int requestid,
                       ArrayList specar, int mode) throws BecDatabaseException
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
    public String          getRequestId (){ return m_request_id  ;}
    public int             getStatus (){ return m_status  ;}
    public java.util.Date  getDate (){ return m_date  ;}
    //specs for the process to execute (can be none)
    public static   ArrayList   getSpecs (){ return m_specs  ;}
    public ArrayList       getSpecIds (){ return m_spec_ids  ;}
    
    public void insert (Connection conn) throws BecDatabaseException
    {
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
