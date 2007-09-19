/*
 * ProjectWorkflowProtocolInfo.java
 *
 * Created on August 30, 2007, 11:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.workflow;

import java.io.*;
import java.util.*;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.process.*;
/**
 *
 * @author htaycher
 */
public class ProjectWorkflowProtocolInfo {
    
    /** Creates a new instance of ProjectWorkflowProtocolInfo */
    private static ProjectWorkflowProtocolInfo instance = null;

    public static Hashtable s_projects = null;
    public static Hashtable s_workflows = null;
    public static Hashtable s_protocols_id = null;
    public static Hashtable s_protocols_name = null;
  
    /**
     * Protected constructor.
     *
     */
    private ProjectWorkflowProtocolInfo() 
    {
       //get data
        try
        {
            s_projects = new Hashtable();
            s_workflows = new Hashtable();
            s_protocols_id = new Hashtable();
             s_protocols_name = new Hashtable();
            getProjectInfo();
            getWorkflowsInfo();
            getProtocolsInfo();
            populateSubProtocolsInfo();
            connectProjectWorkflows();
            connectWorkflowProtocols();
            //populate protocols by name
            Iterator iter = s_protocols_id.values().iterator();
            Protocol protocol = null;
            while(iter.hasNext())
            {
                protocol = (Protocol)iter.next();
                s_protocols_name.put(protocol.getProcessname(), protocol);
            }
        }
        catch(Exception e)
        {
            
        }
 
    }
    
    
    public  Hashtable            getProjects(){ return s_projects ;}
    public  Hashtable           getWorkflows(){ return s_workflows ;}
    public  Hashtable           getProtocolsByID(){ return s_protocols_id ;}
    public  Hashtable           getProtocolsByName(){ return s_protocols_name ;}
    
    /**
     * Gets the instance of ProjectWorkflowProtocolInfo.
     *
     * @return the single ProjectWorkflowProtocolInfo instance.
     */
    public static ProjectWorkflowProtocolInfo getInstance() 
    {
        if(instance == null) {
            instance = new ProjectWorkflowProtocolInfo();
        }
        return instance;
    } 
    
    
    /////
    private void        getProjectInfo() throws FlexDatabaseException
    {
         String sql = "select * from project order by name";
         Project p = null;String name = null; String description = null; String version =null;
         DatabaseTransaction t = DatabaseTransaction.getInstance();
         ResultSet rs = t.executeQuery(sql);
System.out.println(sql);

            try
            {                   
                while(rs.next())
                {
                    int projectid = rs.getInt("PROJECTID");
                    name = rs.getString("NAME");
                    description = rs.getString("DESCRIPTION");
                    version = rs.getString("VERSION");
                    p = new Project(projectid, name, description, version,1);
                    s_projects.put(String.valueOf( p.getId()), p);
                }
              } catch(SQLException sqlE) {
                throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(rs);
            }

        }
        
    
     private void        getWorkflowsInfo() throws FlexDatabaseException
    {
          String sql = "select workflowid, name, description from workflow order by workflowid ";
   String name = null; String description = null; int workflowid = -1;Workflow wf = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try 
        {
            while(rs.next())
            {
                name = rs.getString("NAME");
                description = rs.getString("DESCRIPTION");
                workflowid=rs.getInt("workflowid");
                wf = new ProjectWorkflow( null, workflowid,  name,  description);
                s_workflows.put(String.valueOf(workflowid), wf);
            }
            
        }
        catch(SQLException sqlE) 
        {
                throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(rs);
            }

        }
     
     
     private void        getProtocolsInfo() throws FlexDatabaseException
     {
         String sql = "select protocolid, processcode, processname from processprotocol order by protocolid   ";
   
        String processcode = null; String processname = null; int protocolid = -1;Protocol prot = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try 
        {
            while(rs.next())
            {
                processcode = rs.getString("processcode");
                processname = rs.getString("processname");
                protocolid=rs.getInt("protocolid");
                prot = new Protocol(  protocolid,  processcode, processname, 1);
                s_protocols_id.put(String.valueOf(protocolid), prot);
            }
            
        }
        catch(SQLException sqlE) 
        {
                throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(rs);
            }

    }
     
     
 private void populateSubProtocolsInfo() throws FlexDatabaseException 
 {
        String sql ="select protocolid, subprotocolname, subprotocoldescription from subprotocol order by protocolid";
        DatabaseTransaction t =        DatabaseTransaction.getInstance();
        String name = null;String description = null; SubProtocol subProtocol = null;int protocolid = -1;
        Protocol protocol = null;
        RowSet rs = t.executeQuery(sql);
        try
        {
            while(rs.next()) 
            {
                 protocolid = rs.getInt("protocolid");
                 name = rs.getString("SUBPROTOCOLNAME");
                 description = rs.getString("SUBPROTOCOLDESCRIPTION");
                 subProtocol = new SubProtocol(name, description);
                 // put into protocol
                 protocol= (Protocol)s_protocols_id.get(String.valueOf(protocolid));
                 if ( protocol != null) protocol.addSubProtocol(subProtocol);
              
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Cannot populate subprotocol.\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
 
 private void       connectProjectWorkflows()throws Exception
 {
      Vector workflows = new Vector();         
      
     
        String sql ="select projectid, workflowid, code from projectworkflow order by projectid, workflowid";
        DatabaseTransaction t =        DatabaseTransaction.getInstance();
        String code = null;int projectid = -1;int workflowid = -1;
        RowSet rs = t.executeQuery(sql);Workflow workflow = null;Project cur_project = null;
        ProjectWorkflow  pwf =null;
        try
        {
            while(rs.next()) 
            {
                 if (rs.getInt("projectid") != projectid)
                 {
                     workflows = new Vector();   
                     projectid = rs.getInt("projectid");
                     cur_project = (Project)s_projects.get(String.valueOf(projectid));
                     cur_project.setWorkflows(workflows);
                 }
            
                 workflowid = rs.getInt("workflowid");
                 projectid = rs.getInt("projectid");
                 code = rs.getString("code");
                 // get workflow by id
                 pwf = (ProjectWorkflow)s_workflows.get(String.valueOf(workflowid));
                 if (pwf == null)throw new Exception("Cannot find workflow with id: "+workflowid);
                 workflow = new ProjectWorkflow(code, workflowid, pwf.getName(),pwf.getDescription());
                 workflows.addElement(workflow);
              }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Cannot populate subprotocol.\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
 }
    
 private void       connectWorkflowProtocols()throws Exception
 {
      Vector workflows = new Vector();         
    
        String sql ="select workflowid,currentprotocolid, nextprotocolid from workflowtask order by workflowid, currentprotocolid";
        DatabaseTransaction t =        DatabaseTransaction.getInstance();
         
        RowSet rs = t.executeQuery(sql);
        Workflow wf = null;
        Vector flow = null;int currentProtocol = -1;int workflowid = -1;int nextProtocol = -1;Protocol protocol = null;
        FlowRecord fr = null; Vector next_protocols = null; int current = -1;
        try
        {
            rs = t.executeQuery(sql);
            
            Vector v = new Vector();
            while(rs.next())
            {
                if ( workflowid != rs.getInt("workflowid")) // new workflow
                {
                    workflowid= rs.getInt("workflowid");
                    current = -1;
                    flow= new Vector();
                    wf = (Workflow) s_workflows.get(String.valueOf(workflowid));
                    wf.setFlow(flow);
                    next_protocols = new Vector();
                }
                
                currentProtocol = rs.getInt("CURRENTPROTOCOLID");
                nextProtocol = rs.getInt("NEXTPROTOCOLID");
                if(current == -1) 
                {
                    current = currentProtocol;
                    protocol = (Protocol)s_protocols_id.get(String.valueOf(nextProtocol));
                    next_protocols.addElement(protocol);
                    protocol = (Protocol)s_protocols_id.get(String.valueOf(current));
                    fr = new FlowRecord(protocol, next_protocols);
                    flow.addElement(fr);
                } 
                else
                {
                    if(current == currentProtocol) {
                         protocol = (Protocol)s_protocols_id.get(String.valueOf(nextProtocol));
                         next_protocols.addElement(protocol);
                    } 
                    else 
                    {
                        current = currentProtocol;
                        next_protocols = new Vector();
                         protocol = (Protocol)s_protocols_id.get(String.valueOf(nextProtocol));
                         next_protocols.addElement(protocol);
                         protocol = (Protocol)s_protocols_id.get(String.valueOf(current));
                        fr = new FlowRecord(protocol, next_protocols);
                        flow.addElement(fr);
                    }
                }
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
 }
  public static void main(String [] args) {
  
      ProjectWorkflowProtocolInfo prf = ProjectWorkflowProtocolInfo.getInstance();
      Hashtable tr = prf.getWorkflows();
      Hashtable re = prf.getProjects();
      System.out.println(re.size());
  }
}
