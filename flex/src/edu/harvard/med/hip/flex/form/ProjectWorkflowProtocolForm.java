/*
 * ProjectWorkflowProtocol.java
 *
 * Created on December 16, 2002, 5:14 PM
 */

package edu.harvard.med.hip.flex.form;

/**
 *
 * @author  htaycher
 */

    
 

import org.apache.struts.action.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ProjectWorkflowProtocolForm extends ActionForm 
{
    private String m_forwardName = null;
    private int         m_projectid = -1;
    private String      m_projectname = null;
    private int         m_workflowid = -1;
    private String      m_workflowname = null;
   
    private String      m_processname = null;
    private int         m_processid = -1;
    
   public void         setForwardName(String forwardName)            {        m_forwardName = forwardName;    }
    public String       getForwardName()                               {        return m_forwardName;    }
    
    public void         setWorkflowname(String name)    {        m_workflowname = name;    }
    public String       getWorkflowname()    {        return m_workflowname;    }
    
    public void         setProcessname(String name)    {        m_processname = name;    }
    public String       getProcessname()    {        return m_processname;    }
    
    public void         setProjectname(String name)    {        m_projectname = name;    }
    public String       getProjectname()    {        return m_projectname;    }
    
    public void         setProjectid(int projectid)    {        m_projectid = projectid;   }
    public int          getProjectid()    {        return m_projectid;    }
    
    public void         setWorkflowid(int workflowid)    {        m_workflowid = workflowid;    }
    public int          getWorkflowid()   {        return m_workflowid;    }
    
    public void         setProcessid(int workflowid)    {        m_processid = workflowid;    }
    public int          getProcessid()   {        return m_processid;    }
    
}
