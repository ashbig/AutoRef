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
import edu.harvard.med.hip.flex.Constants;
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
    public static final int STANDARD_WORKFLOW = 6;
    public static final int CREATOR_WORKFLOW = 5;
    public static final int PSEUDOMONAS_WORKFLOW = 4;
    public static final int MGC_PLATE_HANDLE_WORKFLOW = 7;
    public static final int MGC_GATEWAY_WORKFLOW = 8;
    public static final int MGC_CREATOR_WORKFLOW = 9;
    public static final int YEAST_REVISED_ORF  = 10;
    public static final int YEAST_FAILED_ORF  = 11;
    public static final int CONVERT_FUSION_TO_CLOSE = 12;
    public static final int CONVERT_CLOSE_TO_FUSION = 13;
    public static final int DNA_TEMPLATE_CREATOR = 14;
    //public static final int TEMPLATE_CREATOR_PCRA = 15;
    public static final int GATEWAY_WORKFLOW = 28;
    
    public static final int REARRAY_PLATE = 16;
    public static final int REARRAY_TEMPLATE = 26;
    public static final int REARRAY_OLIGO = 27;
    public static final int REARRAY_WORKING_GLYCEROL = 17;
    public static final int REARRAY_WORKING_DNA = 18;
    public static final int REARRAY_ARCHIVE_DNA = 19;
    public static final int REARRAY_ARCHIVE_GLYCEROL = 20;
    public static final int REARRAY_SEQ_GLYCEROL = 21;
    public static final int REARRAY_SEQ_DNA = 22;
    public static final int REARRAY_DIST_DNA = 23;
    public static final int REARRAY_DIST_GLYCEROL = 24;
    
    public static final int TRANSFER_TO_EXPRESSION = 25;
    
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
        } catch (FlexDatabaseException ex)
        {}
    }
    
    /**
     * Constructor.
     *
     * @param id The workflowid corresponding to the primary key in the database.
     * @exception FlexDatabaseException.
     */
    public Workflow(int id) throws FlexDatabaseException {
        this.id = id;
        if (Constants.s_workflows != null && Constants.s_workflows.get(String.valueOf(id)) != null) {
            Workflow w = (Workflow)Constants.s_workflows.get(String.valueOf(id));
            this.description = w.getDescription();
            this.name = w.getName();
            this.flow = w.getFlow();
            return;
        }
        
        
        flow = new Vector();
        
        String sql = "select name, description from workflow "+
        "where workflowid = "+id;
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        try {
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
     * Constructor.
     *
     * @param id The workflowid.
     * @param name The workflow name.
     * @param description The workflow description.
     * @return The Workflow object.
     */
    public Workflow(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
     * Return the previous protocol name for the given protocol name.
     *
     * @param protocol The given protocol.
     * @return The next protocol.
     */
    public Vector getPreviousProtocol(Protocol protocol) {
        Enumeration enum = flow.elements();
        FlowRecord prev = null;
        while(enum.hasMoreElements()) {
            FlowRecord r = (FlowRecord)enum.nextElement();
            if( prev != null && prev.isEqual(protocol)) {
                return prev.getNext();
            }
            prev = r;
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
    
    public static Vector getAllWorkflows() throws FlexDatabaseException {
        
        Vector workflows = new Vector();
        if (Constants.s_workflows != null) {
            workflows = new Vector(Constants.s_workflows.values());
            sortWorkflowsByName(workflows);
            return workflows;
        }
        
        String sql = "select * from workflow";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        ResultSet rs = t.executeQuery(sql);
        
        
        try {
            while(rs.next()) {
                int workflowid = rs.getInt("WORKFLOWID");
                String name = rs.getString("NAME");
                String description = rs.getString("DESCRIPTION");
                Workflow w = new Workflow(workflowid, name, description);
                workflows.addElement(w);
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return workflows;
    }
    
    
    private static void sortWorkflowsByName(Vector workflows) {
        Collections.sort(workflows, new Comparator() {
            public int compare(Object cont1, Object cont2) {
                Workflow p1 =(Workflow) cont1;
                Workflow p2 = (Workflow) cont2;
                return  p1.getName().compareToIgnoreCase(p2.getName() ) ;
            }
        });
    }
    
    
    public static void main(String [] args) {
        try {
            Workflow flow = new Workflow(Workflow.CONVERT_CLOSE_TO_FUSION);
            System.out.println("Name is: "+flow.getName());
            System.out.println("Description is: "+flow.getDescription());
            
            Protocol curr = new Protocol(45);
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
