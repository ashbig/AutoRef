/*
 * Workflow.java
 *
 * This class represents a workflow which contains a list of
 * protocol names with one following another.
 *
 * Created on June 25, 2001, 4:14 PM
 */

package edu.harvard.med.hip.flex.workflow;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.process.Protocol;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version
 */
public class Workflow {
    protected int id;
    protected String name;
    protected String description;
    protected Vector flow;
    
    public static final int COMMON_WORKFLOW = 1;
    public static final int MGC_PLATE_HANDLE_WORKFLOW = 7;
    public static final int MGC_GATEWAY_WORKFLOW = 8;
    public static final int MGC_CREATOR_WORKFLOW = 9;
    
    /** Creates new Workflow */
    public Workflow() {
        flow = new Vector();
        try {
            //initialize all the flow records.
            Vector next = new Vector();
            next.addElement(new Protocol(Protocol.APPROVE_SEQUENCES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.CUSTOMER_REQUEST), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.DESIGN_CONSTRUCTS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.APPROVE_SEQUENCES), next));
            
           
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_OLIGO_ORDERS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.DESIGN_CONSTRUCTS), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.RECEIVE_OLIGO_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_OLIGO_ORDERS), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.DILUTE_OLIGO_PLATE));
            flow.addElement(new FlowRecord(new Protocol(Protocol.RECEIVE_OLIGO_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_PCR_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.DILUTE_OLIGO_PLATE), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_STEP2_PCR_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_PCR_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.RUN_PCR_GEL));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_STEP2_PCR_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.ENTER_PCR_GEL_RESULTS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.RUN_PCR_GEL), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_FILTER_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.ENTER_PCR_GEL_RESULTS), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_BP_REACTION_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_FILTER_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.PERFORM_TRANSFORMATION));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_BP_REACTION_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_AGAR_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.PERFORM_TRANSFORMATION), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.ENTER_AGAR_PLATE_RESULTS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_AGAR_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.ENTER_AGAR_PLATE_RESULTS), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_DNA_PLATES));
            next.addElement(new Protocol(Protocol.GENERATE_GLYCEROL_PLATES));
            next.addElement(new Protocol(Protocol.GENERATE_SEQUENCING_PCR_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_CULTURE_BLOCKS_FOR_ISOLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.ENTER_DNA_GEL_RESULTS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_DNA_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.GENERATE_SEQUENCING_DNA_PLATES));
            next.addElement(new Protocol(Protocol.GENERATE_GLYCEROL_PLATES));
            next.addElement(new Protocol(Protocol.GENERATE_SEQUENCING_PCR_PLATES));
            flow.addElement(new FlowRecord(new Protocol(Protocol.ENTER_DNA_GEL_RESULTS), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.SUBMIT_SEQUENCING_ORDERS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_SEQUENCING_DNA_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.SUBMIT_SEQUENCING_ORDERS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_GLYCEROL_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.SUBMIT_SEQUENCING_ORDERS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.GENERATE_SEQUENCING_PCR_PLATES), next));
            
            next = new Vector();
            next.addElement(new Protocol(Protocol.RECEIVE_SEQUENCING_RESULTS));
            flow.addElement(new FlowRecord(new Protocol(Protocol.SUBMIT_SEQUENCING_ORDERS), next));
        } catch (FlexDatabaseException ex) {}
    }
    
    /**
     * Constructor.
     *
     * @id The workflowid corresponding to the primary key in the database.
     * @exception FlexDatabaseException.
     */
    public Workflow(int id) throws FlexDatabaseException {
        this.id = id;
        
        flow = new Vector();
        
        String sql = "select name, description from workflow "+
        "where workflowid = "+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                name = rs.getString("NAME");
                description = rs.getString("DESCRIPTION");
            }
            
            sql = "select currentprotocolid, nextprotocolid "+
            "from workflowtask where workflowid = "+id+
            " order by currentprotocolid";
            rs = t.executeQuery(sql);
            
            Vector v = new Vector();
            
            int current = -1;
            while(rs.next()) {
                int currentProtocol = rs.getInt("CURRENTPROTOCOLID");
                int nextProtocol = rs.getInt("NEXTPROTOCOLID");
                
                if(current == -1) {
                    current = currentProtocol;
                    v.addElement(new Protocol(nextProtocol));
                    FlowRecord fr = new FlowRecord(new Protocol(current), v);
                    flow.addElement(fr);
                } else {
                    if(current == currentProtocol) {
                        v.addElement(new Protocol(nextProtocol));
                    } else {
                        current = currentProtocol;
                        v = new Vector();
                        v.addElement(new Protocol(nextProtocol));
                        FlowRecord fr = new FlowRecord(new Protocol(current), v);
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
    
    /**
     * Return the workflow id.
     *
     * @return The workflow id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the next protocol name for the given protocol name.
     *
     * @param protocol The given protocol.
     * @return The next protocol.
     */
    public Vector getNextProtocol(Protocol protocol) {
        Enumeration enum = flow.elements();
        while(enum.hasMoreElements()) {
            FlowRecord r = (FlowRecord)enum.nextElement();
            if(r.isEqual(protocol)) {
                return r.getNext();
            }
        }
        
        return null;
    }
    
    /**
     * Return the name of the workflow.
     *
     * @return The name of the workflow.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Return the workflow description.
     *
     * @return The workflow description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Return the entire workflow.
     *
     * @return The entire workflow.
     */
    public Vector getFlow() {
        return flow;
    }
    
    public static void main(String [] args) {
        try {
            Workflow flow = new Workflow(5);
            System.out.println("Name is: "+flow.getName());
            System.out.println("Description is: "+flow.getDescription());
            
            Protocol curr = new Protocol(39);
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
