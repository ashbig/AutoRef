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
import edu.harvard.med.hip.flex.Constants;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Project 
{
    public final static int HUMAN = 1;
    public final static int YEAST = 2;
    public final static int PSEUDOMONAS = 3;
    public final static int CLONTECH = 4;
    public final static int BREASTCANCER = 5;
    public final static int KINASE = 6;
    public final static int MGC_PROJECT = 7;
    public final static int HTF = 9;
    public final static int NIDDK = 10;
    public final static int YP = 11;
    public final static int RZPD_WALL = 13;
    public final static int FT = 14;
    public final static int AVENTIS = 15;
    public final static int VC = 16;
    public final static int KINASE_MUT = 17;
    public final static int Bacillus_anthracis = 18;
    public final static int Yersinia_pseudotuberculosis = 19;
    public final static int YEAST_DBD = 20;
    public final static int ORFEOME_pENTR223 = 21;
    
    public final static String HUMANSTRING = "Human";
    public final static String YEASTSTRING = "Yeast";
    public final static String PSEUDOMONASSTRING = "Pseudomonas";
    
    public final static String PROJECT_NAME_Human       ="Human" ;
public final static String PROJECT_NAME_Yeast="Yeast";
public final static String PROJECT_NAME_Pseudomonas="Pseudomonas";
public final static String PROJECT_NAME_CLONTECH="CLONTECH";
public final static String PROJECT_NAME_BC="Breast Cancer";
public final static String PROJECT_NAME_HUMANKINASE="Human Kinase";
public final static String PROJECT_NAME_MGC="MGC Project";
public final static String PROJECT_NAME_PCANSER="Prostate Cancer";
public final static String PROJECT_NAME_HTF="Human Transcription Factor";
public final static String PROJECT_NAME_NIDDK="NIDDK Diabetis";
public final static String PROJECT_NAME_YP="Yersinia pestis";
public final static String PROJECT_NAME_NIDDKE_DH="NIDDK Diabetis - Human";
public final static String PROJECT_NAME_RZPD="RZPD - Wall";
public final static String PROJECT_NAME_FT="Francisella tularensis";
public final static String PROJECT_NAME_Aventis="Aventis";
public final static String PROJECT_NAME_VC="Vibrio cholerae";
public final static String PROJECT_NAME_KMUT="Kinase Mutagenesis";
public final static String PROJECT_NAME_BAS_ANTH="Bacillus anthracis";
public final static String PROJECT_NAME_YPT="Yersinia pseudotuberculosis";
public final static String PROJECT_NAME_YEAST_DB="Yeast DBD";
public final static String PROJECT_NAME_PSI="PSI";
public final static String PROJECT_NAME_ORF="ORFeome collaboration";
       
    private int id;
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
    public Project(int id) throws FlexDatabaseException 
    {  
        //try to get from memory
       
        if (ProjectWorkflowProtocolInfo.getInstance().getProjects() != null 
                && ProjectWorkflowProtocolInfo.getInstance().getProjects().get(String.valueOf(id) ) != null)
        {
            
            Project p = (Project)ProjectWorkflowProtocolInfo.getInstance().getProjects().get(String.valueOf(id) );
            this.id = id;
            this.name = p.getName();
            this.workflows = p.getWorkflows();
            this.description = p.getDescription();
            this.version = p.getVersion();
            return ;
        }
        
       
        String sql = "select * from project where projectid = "+id;
   System.out.println(sql);
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                name = rs.getString("NAME");
                description = rs.getString("DESCRIPTION");
                version = rs.getString("VERSION");
            }
            this.id = id;
            populateWorkflows();
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    
    public Project(String project_name) 
    {  
        //try to get from memory
        Project p = null;
        if (ProjectWorkflowProtocolInfo.getInstance().getProjects() != null )
        {
            Iterator iter = ProjectWorkflowProtocolInfo.getInstance().getProjects().values().iterator();
             while(iter.hasNext())
             {
                 p = (Project)iter.next();
                 if ( p.getName().equals(project_name))
                 {
                    this.id = p.getId();
                    this.name = p.getName();
                    this.workflows = p.getWorkflows();
                    this.description = p.getDescription();
                    this.version = p.getVersion();
                    return ;
                  }
             }
        }
        return;
    }
    /**
     * Constructor.
     *
     * @param id The project id.
     * @param name The project name.
     * @param description The project description.
     * @param version The project version.
     * @exception The FlexDatabaseException.
     */
    public Project(int id, String name, String description, String version) throws FlexDatabaseException {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        populateWorkflows();
    }
     public Project(int id, String name, String description, String version, int mode) throws FlexDatabaseException {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
    }
 
    /**
     * Constructor.
     *
     * @param id The project id.
     * @param name The project name.
     * @param description The project description.
     * @param version The project version.
     * @param workflows The workflows belonging to this project
     */
    public Project(int id, String name, String description, String version, Vector workflows) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.version = version;
        this.workflows = workflows;
    }
    
    /**
     * Return the project id.
     *
     * @return The project id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return all the workflow records for this project.
     *
     * @return A Vector of ProjectWorkflow objects.
     */
    public Vector getWorkflows() {
        return workflows;
    }
     public void setWorkflows(Vector v) {         workflows = v;    }
    
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

    /**
     * Return a workflow for a given workflow id.
     *
     * @param workflowid The work flow id.
     * @return A Workflow object.
     */
    public Workflow getWorkflow(int workflowid) {
        Iterator iter = workflows.iterator();
        while(iter.hasNext()) {
            Workflow workflow = (Workflow)iter.next();
            
            if(workflow.getId() == workflowid) {
                return workflow;
            }
        }
        
        return null;
    }

    /**
     * Return a workflow for a given workflow id.
     *
     * @param workflow The workflow used to find the matching workflow.
     * @return A Workflow object.
     */
    public Workflow getWorkflow(Workflow workflow) {
        Iterator iter = workflows.iterator();
        while(iter.hasNext()) {
            Workflow wf = (Workflow)iter.next();
            
            if(wf.getId() == workflow.getId()) {
                return wf;
            }
        }
        
        return null;
    }
    
    /**
     * Return all the projects in the database as a Vector.
     *
     * @return All the projects in the database as a Vector.
     * @exception The FlexDatabaseException.
     */
    public  static Vector getAllProjects() throws FlexDatabaseException {
        
        Vector projects = new Vector();  
        if (ProjectWorkflowProtocolInfo.getInstance().getProjects() != null)
        {
            
           projects= new Vector( ProjectWorkflowProtocolInfo.getInstance().getProjects().values()  );
           // sortProjectsByName(projects);
           return projects;
        }
        else
        {
            String sql = "select * from project order by name";
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            ResultSet rs = t.executeQuery(sql);

            try
            {                   
                while(rs.next())
                {
                    int projectid = rs.getInt("PROJECTID");
                 //   String name = rs.getString("NAME");
                //    String description = rs.getString("DESCRIPTION");
                //    String version = rs.getString("VERSION");
                //    Project p = new Project(projectid, name, description, version,1);
                    Project p = new Project(projectid);
                    projects.addElement(p);
                }
              } catch(SQLException sqlE) {
                throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(rs);
            }

        }
        
        return projects;
    }

    private static void sortProjectsByName(Vector projects)
    {
        Collections.sort(projects, new Comparator()
       {
            public int compare(Object cont1, Object cont2)
            {
                Project p1 =(Project) cont1;
                Project p2 = (Project) cont2;
                return  p1.getName().compareToIgnoreCase(p2.getName() ) ;
           }
       });
    }
    /**
     * Populate the workflow record from the database for this project.
     *
     * @exception The FlexDatabaseException.
     */
    protected void populateWorkflows() throws FlexDatabaseException 
    {
        String sql = "select * from projectworkflow where projectid = "+id;
   System.out.println(sql);
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
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
    
    
    
    
    //**************************************************************//
    //                  Testing Methods                             //
    //**************************************************************//
    
    public static void main(String []  args) {
      /*  try {
            
       
        } catch(FlexDatabaseException ex) {
            System.out.println(ex);
        }*/
    }
}
