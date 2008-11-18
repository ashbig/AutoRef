/*
 * ProjectWorkflowProtocolInfo.java
 *
 * Created on August 30, 2007, 11:10 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package edu.harvard.med.hip.flex.workflow;

import java.util.*;

import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.core.*;
import java.sql.*;
import javax.sql.*;
import edu.harvard.med.hip.flex.process.*;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;

/**
 *
 * @author htaycher
 */
public class ProjectWorkflowProtocolInfo {

    /** Creates a new instance of ProjectWorkflowProtocolInfo */
    public static final String PWP_SEPARATOR=".";
    private static ProjectWorkflowProtocolInfo instance = null;
    public static Hashtable<String,Project> s_projects = null;
    public static Hashtable<String, Workflow> s_workflows = null;
    public static Hashtable<String, Protocol> s_protocols_id = null;
    public static Hashtable<String, Protocol> s_protocols_name = null;
    public static Hashtable<String, String> s_project_protocol_workflow_properties = null;

    /**
     * Protected constructor.
     *
     */
    
    private ProjectWorkflowProtocolInfo() {
        //get data
        try {
            s_projects = new Hashtable();
            s_workflows = new Hashtable();
            s_protocols_id = new Hashtable();
            s_protocols_name = new Hashtable();
            s_project_protocol_workflow_properties = new Hashtable();
            getProjectInfo();
            getWorkflowsInfo();
            getProtocolsInfo();
            populateSubProtocolsInfo();
            connectProjectWorkflows();
            connectWorkflowProtocols();
            getProperties();
            
        } catch (Exception e) {
        }

    }

    public Hashtable getProjects() {
        return s_projects;
    }
public String getProjectCode(int projectid)
{
    String result = null;
    Project pr = s_projects.get( String.valueOf(projectid));
    result = pr.getCode();
    return result;
}
    public Hashtable getWorkflows() {
        return s_workflows;
    }

    public Hashtable getProtocolsByID() {
        return s_protocols_id;
    }

    public Hashtable getProtocolsByName() {
        return s_protocols_name;
    }

    public Hashtable<String, String> getPWPProperties() {
        return s_project_protocol_workflow_properties;
    }

    /**
     * Gets the instance of ProjectWorkflowProtocolInfo.
     *
     * @return the single ProjectWorkflowProtocolInfo instance.
     */
    public static ProjectWorkflowProtocolInfo getInstance() {
        if (instance == null) {
            instance = new ProjectWorkflowProtocolInfo();
        }
        return instance;
    }
    public static void reloadProjectWorkflowProtocolInfo()
    {
        instance = new ProjectWorkflowProtocolInfo();
       
    }

    /////
    private void getProperties() throws FlexDatabaseException {
        String sql = "select projectid, workflowid, protocolid, propertyname, propertyvalue  from setup_properties  ";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        String key;PWPItem pitem;
        try {
            while (rs.next())
            {
               // key = rs.getString("projectid") +PWP_SEPARATOR+ rs.getString("workflowid") + PWP_SEPARATOR+rs.getString("protocolid") +PWP_SEPARATOR+ rs.getString("propertyname");
                int pid = rs.getInt("projectid");
                int wid = rs.getInt("workflowid");
                int ppid = rs.getInt("protocolid");
                pitem = new PWPItem(pid,wid,ppid,rs.getString("propertyname"),rs.getString("propertyvalue"));
                
                s_project_protocol_workflow_properties.put(pitem.getKey(), rs.getString("propertyvalue"));
                if (pid==-1 && ppid==-1 && wid != -1)
                {
                    Workflow w = s_workflows.get(String.valueOf(wid));
                    w.addProperty(pitem);
                }
                else if (pid!=-1 && ppid==-1 && wid == -1)
                {
                     Project p = s_projects.get(String.valueOf(pid));
                    p.addProperty(pitem);
                }
                else if (pid==-1 && ppid!=-1 && wid == -1)
                {
                    
                }
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

    }

    private void getProjectInfo() throws FlexDatabaseException {
        String sql = "select * from project order by name";
        Project p = null;
        String name = null;String code = null;
        String description = null;
        String version = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);

        try {
            while (rs.next()) {
                int projectid = rs.getInt("PROJECTID");
                name = rs.getString("NAME");
                description = rs.getString("DESCRIPTION");
                version = rs.getString("VERSION");
                code = rs.getString("code");
                p = new Project(projectid, name, description, version,code, 1);
                s_projects.put(String.valueOf(p.getId()), p);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

    }

    public  static void addWorkflows(Workflow wf) throws FlexDatabaseException 
    {
            s_workflows.put(String.valueOf(wf.getId()), wf);
    }
    public  static void addProject(Project p) throws FlexDatabaseException 
    {
            s_projects.put(String.valueOf(p.getId()), p);
    }
    
    private static void getWorkflowsInfo() throws FlexDatabaseException {
        String sql = "select workflowid, name, description, workflowtype from workflow order by workflowid ";
        String name = null;
        String description = null;
        int workflowid = -1;
        Workflow wf = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        WORKFLOW_TYPE workflow_type;
        ResultSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                name = rs.getString("NAME");
                description = rs.getString("DESCRIPTION");
                workflowid = rs.getInt("workflowid");
                workflow_type = WORKFLOW_TYPE.valueOf(rs.getString("workflowtype"));
                wf = new ProjectWorkflow(null, workflowid, name, description,workflow_type,null);
                s_workflows.put(String.valueOf(workflowid), wf);
            }

        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

    }

    private void getProtocolsInfo() throws FlexDatabaseException {
        String sql = "select protocolid, processcode, processname from processprotocol order by protocolid   ";

        String processcode = null;
        String processname = null;
        int protocolid = -1;
        Protocol prot = null;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                processcode = rs.getString("processcode");
                processname = rs.getString("processname");
                protocolid = rs.getInt("protocolid");
                prot = new Protocol(protocolid, processcode, processname, 1);
                s_protocols_id.put(String.valueOf(protocolid), prot);
                s_protocols_name.put(prot.getProcessname(), prot);
            }

        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }

    }

    private void populateSubProtocolsInfo() throws FlexDatabaseException {
        String sql = "select protocolid, subprotocolname, subprotocoldescription from subprotocol order by protocolid";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        String name = null;
        String description = null;
        SubProtocol subProtocol = null;
        int protocolid = -1;
        Protocol protocol = null;
        RowSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                protocolid = rs.getInt("protocolid");
                name = rs.getString("SUBPROTOCOLNAME");
                description = rs.getString("SUBPROTOCOLDESCRIPTION");
                subProtocol = new SubProtocol(name, description);
                // put into protocol
                protocol = (Protocol) s_protocols_id.get(String.valueOf(protocolid));
                if (protocol != null) {
                    protocol.addSubProtocol(subProtocol);
                }
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Cannot populate subprotocol.\n" + sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    private void connectProjectWorkflows() throws Exception {
        Vector workflows = new Vector();


        String sql = "select projectid, workflowid, code from projectworkflow order by projectid, workflowid";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        String code = null;
        int projectid = -1;
        int workflowid = -1;
        RowSet rs = t.executeQuery(sql);
        Workflow workflow = null;
        Project cur_project = null;
        ProjectWorkflow pwf = null;
        try {
            while (rs.next()) {
                if (rs.getInt("projectid") != projectid) {
                    workflows = new Vector();
                    projectid = rs.getInt("projectid");
                    cur_project = (Project) s_projects.get(String.valueOf(projectid));
                    cur_project.setWorkflows(workflows);
                }

                workflowid = rs.getInt("workflowid");
                projectid = rs.getInt("projectid");
                code = rs.getString("code");
                // get workflow by id
                pwf = (ProjectWorkflow) s_workflows.get(String.valueOf(workflowid));
                if (pwf == null) {
                    throw new Exception("Cannot find workflow with id: " + workflowid);
                }
                workflow = new ProjectWorkflow(code, workflowid, pwf.getName(), pwf.getDescription(),pwf.getWorkflowType(),null);
                workflows.addElement(workflow);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Cannot populate subprotocol.\n" + sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    private void connectWorkflowProtocols() throws Exception 
    {
       
        String sql = "select workflowid,currentprotocolid, nextprotocolid from workflowtask order by workflowid, workflowtaskid";
        DatabaseTransaction t = DatabaseTransaction.getInstance();

        RowSet rs = t.executeQuery(sql);
        Workflow wf = null;
        Vector flow = null;
        int currentProtocol = -1;
        int workflowid = -1;
        int nextProtocol = -1;
        Protocol protocol = null;
        FlowRecord fr = null;
        Vector next_protocols = null;
        int current = -1;
        try {
            rs = t.executeQuery(sql);

            Vector v = new Vector();
            while (rs.next()) {
                if (workflowid != rs.getInt("workflowid")) // new workflow
                {
                    
                    workflowid = rs.getInt("workflowid");
                    current = -1;
                    flow = new Vector();
                    wf = (Workflow) s_workflows.get(String.valueOf(workflowid));
                    wf.setFlow(flow);
                    next_protocols = new Vector();
                }

                currentProtocol = rs.getInt("CURRENTPROTOCOLID");
                nextProtocol = rs.getInt("NEXTPROTOCOLID");
                if (current == -1) {
                    current = currentProtocol;
                    protocol = (Protocol) s_protocols_id.get(String.valueOf(nextProtocol));
                    next_protocols.addElement(protocol);
                    protocol = (Protocol) s_protocols_id.get(String.valueOf(current));
                    fr = new FlowRecord(protocol, next_protocols);
                    flow.addElement(fr);
                } else {
                    if (current == currentProtocol) {
                        protocol = (Protocol) s_protocols_id.get(String.valueOf(nextProtocol));
                        next_protocols.addElement(protocol);
                    } else {
                        current = currentProtocol;
                        next_protocols = new Vector();
                        protocol = (Protocol) s_protocols_id.get(String.valueOf(nextProtocol));
                        next_protocols.addElement(protocol);
                        protocol = (Protocol) s_protocols_id.get(String.valueOf(current));
                        fr = new FlowRecord(protocol, next_protocols);
                        flow.addElement(fr);
                    }
                }
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE + "\nSQL: " + sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    public static void main(String[] args) {

        try
      {
             ProjectWorkflowProtocolInfo.reloadProjectWorkflowProtocolInfo();// .getInstance();
   
             ProjectWorkflowProtocolInfo prf = ProjectWorkflowProtocolInfo.getInstance();
             String res = ProjectWorkflowProtocolInfo.getInstance().getProjectCode(6);
             System.out.println(res);
             res = ProjectWorkflowProtocolInfo.getInstance().getProjectCode(23);
             System.out.println(res);
             //Workflow w = new Workflow(65);
       //     Workflow ww = new Workflow(w, "new name", 165);
             
            //  DatabaseTransaction t = DatabaseTransaction.getInstance();
           //  Connection conn = t.requestConnection();
          
            //  Workflow w=  edu.harvard.med.hip.flex.action.AddWorkflowItemsAction.createNewWorkflowFromTemplate
         //(65,159,"newworkflow", "TRANSFER_TO_EXPRESSION", conn);
          System.exit(0);
      }catch(Exception e)
      {
      System.exit(0);
      }
       // Hashtable tr = prf.getWorkflows();
       // Hashtable re = prf.getProjects();
    //    String label_postfix = "112";
      //   StringBuilder sb= new StringBuilder("0000");
     //   sb.insert(4-label_postfix.length(), label_postfix);
     //   String newBarcode="Y"; newBarcode += "." + sb.toString();
     //   System.out.println(newBarcode);
      // StringBuilder sb= new StringBuilder("0000");
      //  sb.replace(4-label_postfix.length(),  sb.length(), label_postfix);
      // String newBarcode="Y"; newBarcode += "." + sb.toString();
        //System.out.println(newBarcode);
    }
}
