/*
 * Project.java
 *
 * This class represents a project which corresponds to the project table in FLEXGene database.
 *
 * Created on August 10, 2001, 4:32 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Project {
    private String name;
    private String description;
    private String version;
    
    // Stores all the workflows belonging to this project. The element in
    // the Vector is a ProjectWorkflow object.
    private Vector workflows;
    
    /** Creates new Project */
    public Project() {
    }

    /**
     * Constructor.
     * 
     * @param id The primary key in the database.
     * @return The Project object.
     * @exception FlexDatabaseException.
     */
    public Project(int id) throws FlexDatabaseException {       
        String sql = "select * from project where projectid = "+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                name = rs.getString("NAME");
                description = rs.getString("DESCRIPTION");
                version = rs.getString("VERSION");
            }
            
            sql = "select * from projectworkflow where projectid = "+id;
            rs = t.executeQuery(sql);
            
            workflows = new Vector();         
            while(rs.next()) {
                int workflowid = rs.getInt("WORKFLOWID");
                String code = rs.getString("CODE");
                Workflow workflow = new ProjectWorkflow(code, workflowid);
                workflows.addElement(workflow);
            }                    
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Return all the workflow records for this project.
     *
     * @return A Vector of ProjectWorkflow objects.
     */
    public Vector getWorkflows() {
        return workflows;
    }
    
    /**
     * Return the name of the project.
     *
     * @return The name of the project.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Return the project description.
     *
     * @return The project description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Return the project version.
     *
     * @return The project version.
     */
    public String getVersion() {
        return version;
    }
    
    public static void main(String []  args) {
        try {
            Project project = new Project(1);
            System.out.println("Project name is: "+project.getName());
            System.out.println("Project description is: "+project.getDescription());
            System.out.println("Project version is: "+project.getVersion());
            
            List workflows = project.getWorkflows();
            Iterator iter = workflows.iterator();
            while(iter.hasNext()) {
                Workflow workflow = (Workflow)iter.next();
                System.out.println("Workflow name is: "+workflow.getName());
                System.out.println("Workflow description is: "+workflow.getDescription());

                Protocol curr = new Protocol(22);
                System.out.println("Current protocol is: "+curr.getProcessname()); 
                
                List nextProtocols = workflow.getNextProtocol(curr);
                Iterator i = nextProtocols.iterator();
                while(i.hasNext()) {
                    Protocol p = (Protocol)i.next();
                    System.out.println("Next protocol is: "+p.getProcessname());
                }
            }
        } catch(FlexDatabaseException ex) {
            System.out.println(ex);
        }
    }
}
