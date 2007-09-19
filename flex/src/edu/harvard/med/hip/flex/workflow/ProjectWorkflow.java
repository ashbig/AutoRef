/*
 * ProjectWorkflow.java
 *
 * This class maps to the ProjectWorkflow table in the FLEXGene database.
 *
 * Created on August 10, 2001, 4:45 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.sql.*;
import java.util.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
        
/**
 *
 * @author  dzuo
 * @version 
 */
public class ProjectWorkflow extends Workflow {
    protected String code;
    
    /** Creates new ProjectWorkflow */
    public ProjectWorkflow() {
        super();
    }

    /**
     * Constructor.
     *
     * @param code The project workflow code.
     * @param id The workflow id.
     * @return The ProjectWorkflow object.
     * @exception FlexDatabaseException.
     */
    public ProjectWorkflow(String code, int id) throws FlexDatabaseException {
        super(id);
        this.code = code;
    }
    public void           setCode(String v){ code = v;}
    public ProjectWorkflow(String code, int workflowid, String name, String description) 
    {
        super(workflowid,  name,  description );
        this.code = code;
    }
    /**
     * Constructor.
     *
     * @param projectid The projectid.
     * @param workflowid The workflowid.
     * @return The ProjectWorkflow object.
     * @exception The FlexDatabaseException.
     */
    public ProjectWorkflow(int projectid, int workflowid) throws FlexDatabaseException {
        super(workflowid);
        
        String sql = "select code from projectworkflow "+
                     "where projectid = "+projectid+
                     " and workflowid = "+workflowid;
 System.out.println(sql);
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                code = rs.getString("CODE");
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
                        
    /**
     * Return the project workflow code.
     *
     * @return The project workflow code.
     */
    public String getCode() {
        return code;
    }
    
    public static void main(String [] args) {
        try {
            ProjectWorkflow flow = new ProjectWorkflow(1, 1);
            System.out.println("Name is: "+flow.getName());
            System.out.println("Description is: "+flow.getDescription());
            System.out.println("Code is: "+flow.getCode());
            
            Protocol curr = new Protocol(22);
            System.out.println("Current protocol is: "+curr.getProcessname());

            List nexts = flow.getNextProtocol(curr);
            Iterator iter = nexts.iterator();
            while(iter.hasNext()) {
                Protocol next = (Protocol)iter.next();
                System.out.println("Next protocol is: "+next.getProcessname());
            }
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        }
    }            
}
