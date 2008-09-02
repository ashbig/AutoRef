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
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
/**
 *
 * @author  dzuo
 * @version
 */
public class Workflow {
    protected int id = -1;
    protected String name;
    protected String description;
    protected Vector flow;
    protected WORKFLOW_TYPE    m_workflow_type;
    protected Collection<PWPItem>       m_properties;
    
    public enum WORKFLOW_TYPE
    {
        REGULAR("Regular workflow"),
        REARRAY ("Rearray workflow"),
        TRANSFER_TO_EXPRESSION ("Transfer clones to expression vector");
        
         WORKFLOW_TYPE(String title ){i_title=title; }
         public String getTitle(){ return i_title;}
        private String i_title = "Not known";
       
    };
    
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
    public static final int HIP_INGA = 33;
    
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
    public static final int EXPRESSION_WORKFLOW = 30;
    
    public static final int IMPORT_EXTERNAL_CLONE = 31;
    public static final int MGC_GATEWAY_CLOSED = 32;
    public static final int REARRAY_EXP_WORKING = 34;
    
    public static final int GATEWAY_WITH_EGEL = 36;
    public static final int GATEWAY_WITH_INFUSION = 37;
    public static final int GATEWAY_LONG_PRIMER_WITH_EGEL = 45;
    public static final int MGC_GATEWAY_INFUSION_FUSION = 46;
    /*
     * public static final int TRANSFER_TO_EXP_JP1520 = 35;
    public static final int TRANSFER_TO_EXP_PLP_DS_3xFlag = 38;
    public static final int TRANSFER_TO_EXP_PLP_DS_3xMyc = 39;
    public static final int TRANSFER_TO_EXP_pCITE_GST = 40;
    public static final int TRANSFER_TO_EXP_pDEST17 = 41;
    public static final int TRANSFER_TO_EXP_pBY011 = 42;
    public static final int TRANSFER_TO_EXP_pLDNT7_nFLAG = 43;
    public static final int TRANSFER_TO_EXP_pDEST_GST = 44;
    public static final int TRANSFER_TO_EXP_pLENTI62_V5_Dest = 59;
    */
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
        if (ProjectWorkflowProtocolInfo.getInstance().getWorkflows() != null
                && ProjectWorkflowProtocolInfo.getInstance().getWorkflows().get(String.valueOf(id)) != null) {
            Workflow w = (Workflow)ProjectWorkflowProtocolInfo.getInstance().getWorkflows().get(String.valueOf(id));
            this.description = w.getDescription();
            this.name = w.getName();
            this.flow = w.getFlow();
            m_workflow_type= w.getWorkflowType();
            m_properties = w.getProperties();
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
    public Workflow(int id, String name, String description, WORKFLOW_TYPE workflow_type, Collection p) {
        this.id = id;
        this.name = name;
        this.description = description;
        m_workflow_type=workflow_type;
        m_properties = p;
    }
    
    public  Workflow(Workflow template, String name, int vectorid)
            throws Exception
    {
        this.name = name;
        this.id = FlexIDGenerator.getID("workflowid");
        m_workflow_type = template.getWorkflowType();
        this.flow = new Vector(template.getFlow());
        CloneVector vec =  CloneVector.getCloneVectorByID(vectorid);
        for (PWPItem item : template.getProperties())
        {
            if ( item.getName().equals("VECTOR_ID"))
            {
                PWPItem nitem = new PWPItem(-1,this.id,-1,"VECTOR_ID", String.valueOf(vectorid));
                this.addProperty(nitem);
            }
            else if (item.getName().equals("VECTOR_NAME"))
            {
                PWPItem nitem = new PWPItem(-1,this.id,-1,"VECTOR_NAME", vec.getName());
                this.addProperty(nitem);
            }
            else if ( item.getName().equals("CLONING_STRATEGY_ID"))
            { //artifact
            }
            else // should never be here - > exception
            {
                throw new Exception ("Cannot clone workflow (unrecognized property) " + template.getName());
            }
                
        }
    }
    /**
     * Return the workflow id.
     *
     * @return The workflow id.
     */
    public int getId() {        return id;    }
    public Collection<PWPItem>   getProperties(){ return m_properties;}
    public void       addProperty(PWPItem p)
    { if (m_properties == null) m_properties=new ArrayList();
      m_properties.add(p);}
    
    /**
     * Return the next protocol name for the given protocol name.
     *
     * @param protocol The given protocol.
     * @return The next protocol.
     */
    public Vector getNextProtocol(Protocol protocol) {
        Enumeration enu = flow.elements();
        
        while(enu.hasMoreElements()) {
            
            FlowRecord r = (FlowRecord)enu.nextElement();
            if(r.isEqual(protocol)) {
                return r.getNext();
            }
            
        }
        
        return null;
    }
     public Vector getNextProtocol()
     {
        Enumeration enu = flow.elements();
        
        while(enu.hasMoreElements())
        {
            
            FlowRecord r = (FlowRecord)enu.nextElement();
            return r.getNext();
        }
        
        return null;
    }
    
     
     public String getHTMLView ()
     {
        Iterator iter;Protocol prot;
        StringBuffer html_string = new StringBuffer();
        FlowRecord r ; Protocol pr;
        int count = 1;   
         if ( flow != null && flow.size()>0)
         {
          for (int c = 0; c < flow.size(); c++)
          {
             r = ((FlowRecord)flow.get(c));
             if ( c == 0 )
             { 
                pr = r.getCurrent();
                html_string.append(" <ul><li>"+pr.getProcessname()+"</li>");
                
             }
             html_string.append( r.getHTMLView());
             count+=r.getNext().size();
          }
         }
        while(count-->0)
        {html_string.append( "</ul>");}
        
        return html_string.toString();
    }
    /**
     * Return the previous protocol name for the given protocol name.
     *
     * @param protocol The given protocol.
     * @return The next protocol.
     */
    public Vector getPreviousProtocol(Protocol protocol) {
        Enumeration enu = flow.elements();
        FlowRecord prev = null;
        while(enu.hasMoreElements()) {
            FlowRecord r = (FlowRecord)enu.nextElement();
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
    public String getName() {        return name;    }
    
    /**
     * Return the workflow description.
     *
     * @return The workflow description.
     */
    public String getDescription() {        return description;    }
    
    /**
     * Return the entire workflow.
     *
     * @return The entire workflow.
     */
    public Vector getFlow() {        return flow;    }
    public void             setFlow(Vector v) { flow = v;    }
    public void             setWorkflowType(String workflow_type){m_workflow_type=WORKFLOW_TYPE.valueOf(workflow_type);}
    public WORKFLOW_TYPE           getWorkflowType(){ return m_workflow_type;}
    public static List       getAllWorkflows(WORKFLOW_TYPE wtype, Collection workflows) 
    {
        Iterator <Workflow> iter = workflows.iterator();
        List req_workflows = new ArrayList();
        Workflow cur_w ;
        while(iter.hasNext())
        {
            cur_w = (Workflow)iter.next();
            if ( cur_w.getWorkflowType().equals( wtype))
            {
                req_workflows.add(cur_w);
            }
        }
        return req_workflows;
    }
    
    public static Vector getAllWorkflows() throws FlexDatabaseException {
        
        Vector workflows = new Vector();
        if (ProjectWorkflowProtocolInfo.getInstance().getWorkflows() != null) 
        {
            workflows = new Vector(ProjectWorkflowProtocolInfo.getInstance().getWorkflows().values());
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
                WORKFLOW_TYPE workflow_type = WORKFLOW_TYPE.valueOf(rs.getString("workflowtype"));
                Workflow w = new Workflow(workflowid, name, description,workflow_type, null);
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
    
    public void insert (Connection conn)throws FlexDatabaseException
    {
       String temp;
       String sql = "insert into workflow (workflowid, name,workflowtype) ";
       if ( id==-1 )  
           sql +=" values (workflowid.nextval,'"+ name+"','"+m_workflow_type.toString() +"')";
       else
          sql +=" values ("+id+",'"+ name+"','"+m_workflow_type.toString() +"')";
       
       String sql_w_task1 =
              "insert into workflowtask   (workflowtaskid, currentprotocolid, nextprotocolid,workflowid) values  "
+" (workflowtaskid.nextval,";
               String sql_w_task2=",(select workflowid from workflow where name='"+name+"'))";
        String sql_properties="insert into setup_properties (projectid, workflowid,protocolid, propertyname,propertyvalue) "
   +" values (-1,(select workflowid from workflow where name='"+name+"'),-1," ;   
    
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        //    System.out.println(sql);
            stmt.executeUpdate(sql);
           
            for (PWPItem item  : m_properties)
            {
                temp = sql_properties+"'" + item.getName()+"','"+item.getValue()+"')";
             // System.out.println(temp);
                stmt.executeUpdate(temp);
            }
            
            Iterator iter = flow.iterator();
            FlowRecord r;Protocol n;
            while (iter.hasNext())
            {
                r = (FlowRecord)iter.next();
                for (int cc = 0; cc < r.getNext().size();cc++)
                {
                    n = (Protocol) r.getNext().get(cc);
                  temp = sql_w_task1+ r.getCurrent().getId()+"," + n.getId() +sql_w_task2;
          //    System.out.println(temp);
                   stmt.executeUpdate(temp);
                }
             }
            
            
        } catch (Exception sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
    
    
    
    public static void main(String [] args) {
        Connection conn=null;
        try {
              //    DatabaseTransaction t = DatabaseTransaction.getInstance();
           //   conn = t.requestConnection();
       
            ProjectWorkflowProtocolInfo.getInstance();
            List<Workflow> ww = Workflow.getAllWorkflows();
            Comparator genericObject = new BeanClassComparator("name");
 
            Collections.sort(ww, genericObject);
         //    Workflow flow = new Workflow(2);ww
         //    System.out.println(flow.getHTMLView());
             
         //    Workflow neww = new Workflow(flow,"new111vector",134 );
         //    System.out.println(neww.getHTMLView());
         //  neww.insert(conn);
            System.exit(0);
        } catch (Exception e)
        {
            DatabaseTransaction.rollback(conn);
             
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
