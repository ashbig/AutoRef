/**
 * $Id: Protocol.java,v 1.48 2007-09-19 15:44:57 Elena Exp $
 *
 * File     : FlexProcessException.java
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.math.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.workflow.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;
import edu.harvard.med.hip.flex.Constants;
/**
 * Represents the protocol object corresponding to the
 * protocol table.
 */
public class Protocol {
    /*
     * the protocol strings
     */
    public static final String APPROVE_SEQUENCES = "Approve sequences";
    public static final String CUSTOMER_REQUEST = "customer request";
    public static final String IDENTIFY_SEQUENCES_FROM_UNIGENE=
    "Identify sequences from unigene";
    public static final String DESIGN_CONSTRUCTS= "Design constructs";
    
    public static final String MGC_DESIGN_CONSTRUCTS= "Design constructs and rearray DNA template plates";
    public static final String GENERATE_OLIGO_ORDERS= "Generate oligo orders";
    public static final String RECEIVE_OLIGO_PLATES= "Receive oligo plates";
    public static final String DILUTE_OLIGO_PLATE = "Dilute oligo plate";
    public static final String GENERATE_PCR_PLATES = "Generate step1 PCR plates";
    public static final String GENERATE_STEP2_PCR_PLATES = "Generate step2 PCR plates";
    public static final String RUN_PCR_GEL = "Run PCR gel";
    public static final String GENERATE_FILTER_PLATES="Generate filter plates";
    public static final String GENERATE_BP_REACTION_PLATES=
    "Generate BP reaction plates";
    public static final String GENERATE_TRANSFORMATION_PLATES =
    "Generate transformation plates";
    public static final String GENERATE_AGAR_PLATES=
    "Generate agar plates";
    public static final String GENERATE_CULTURE_BLOCKS_FOR_ISOLATES=
    "Generate culture blocks for isolates";
    public static final String GENERATE_DNA_PLATES="Generate DNA plates";
    public static final String GENERATE_GLYCEROL_PLATES=
    "Generate glycerol plates";
    public static final String GENERATE_SEQUENCING_PCR_PLATES=
    "Generate sequencing PCR plates";
    public static final String GENERATE_SEQUENCING_DNA_PLATES =
    "Generate sequencing DNA plates";
    public static final String SUBMIT_SEQUENCING_ORDERS=
    "Submit sequencing orders";
    public static final String RECEIVE_SEQUENCING_RESULTS=
    "Receive sequencing results";
    public static final String PERFORM_TRANSFORMATION =
    "Perform transformation";
    public static final String ENTER_PCR_GEL_RESULTS =
    "Enter PCR gel results";
    public static final String ENTER_AGAR_PLATE_RESULTS =
    "Enter agar plate results";
    public static final String ENTER_DNA_GEL_RESULTS =
    "Enter DNA gel results";
    public static final String GENERATE_CAPTURE_REACTION =
    "Generate infusion reaction plate";
    public static final String GENERATE_GRID_PLATE =
    "Generate agar plates (48 well grid plate)";
    public static final String PICK_COLONY =
    "Generate culture blocks for isolates from 48 well plates";
    public static final String ENTER_CULTURE_RESULTS = "Enter culture results";
    public static final String CREATE_CULTURE_FROM_MGC =
    "Create culture block from master template plate";
    public static final String CREATE_GLYCEROL_FROM_CULTURE =
    "Create glycerol stock from MGC culture block";
    public static final String ENTER_MGC_CULTURE_RESULTS =
    "Enter template culture result";
    public static final String CREATE_GLYCEROL_FROM_REARRAYED_CULTURE =
    "Create glycerol stock from rearrayed culture block";
    public static final String CREATE_DNA_FROM_REARRAYED_CULTURE =
    "Create DNA template plate from rearrayed culture block";
    public static final String IMPORT_MGC_REQUEST =
    "Import MGC request";
    public static final String CREATE_DNA_FROM_MGC_CULTURE =
    "Create DNA plate from MGC culture block";
    
    //yeast revised orf
    public static final String REARRAY_PLATES_BASED_ON_SEQUENCING_RESULTS =
    "Rearray plates based on sequencing results";
    public static final String REARRAY_CULTURE_PLATES_BASED_ON_SEQUENCING_RESULTS =
    "Rearray culture plates based on sequencing results";
    public static final String REARRAY_PCR_PLATES =
    "Rearray PCR plates";
    public static final String REARRAY_TRANSFORMATION_PLATES =
    "Rearray transformation plates";
    public static final String GENERATE_SEQUENCING_GLYCEROL_PLATES =
    "Generate sequencing glycerol plates";
    public static final String REARRAY_TO_DNA_TEMPLATE =
    "Rearray to DNA template plate";
    public static final String REARRAY_GLYCEROL = "Rearray glycerol plate";
    
    public static final String REARRAY_WORKING_GLYCEROL =
    "Rearray clones to working glycerol stocks";
    public static final String REARRAY_WORKING_DNA =
    "Rearray clones to working DNA plates";
    public static final String REARRAY_ARCHIVE_DNA =
    "Rearray clones to DNA archive storage plates";
    public static final String REARRAY_ARCHIVE_GLYCEROL =
    "Rearray clones to glycerol archive storage plates";
    public static final String REARRAY_SEQ_GLYCEROL =
    "Rearray clones to sequencing glycerol stocks";
    public static final String REARRAY_SEQ_DNA =
    "Rearray clones to sequencing DNA plates";
    public static final String REARRAY_DIST_DNA =
    "Rearray clones to distribution DNA plates";
    public static final String REARRAY_DIST_GLYCEROL =
    "Rearray clones to distribution glycerol stocks";
    public static final String REARRAY_EXP_WORKING = 
    "Rearray expression clones to working glycerol stocks";
    public static final String REARRAY_EXP_WORKING_DNA = 
    "Rearray expression clones to working DNA";
    public static final String CREATE_EXPRESSION_PLATE = "Create expression plate";
    public static final String ENTER_EXPRESSION_RESULT = "Enter expression result";
    public static final String REARRAY_OLIGO = "Rearray oligo plates";
     public static final String UPLOAD_CONTAINERS_FROM_FILE = "Upload containers from file";
   
    
    
    
    public static final int CREATE_EXPRESSION_PLATE_CODE = 55;
    public static final int ENTER_EXPRESSION_RESULT_CODE = 56;
    public static final int CREATE_EXP_DNA_CODE = 57;
    
    public static final String CREATE_SEQ_PLATES = "Create rearrayed sequencing plates";
    
    public static final String CREATE_EXP_DNA = "Create expression DNA plate";
    public static final String CREATE_TRANSFECTION = "Create transfection plates";
    public static final String ENTER_CULTURE_FILE = "Enter culture results by uploading file";
    public static final String ENTER_DNA_RESULT = "Enter DNA results";
    public static final String GENERATE_CRE_PLATE = "Generate cre reaction plate";
    public static final String ENTER_EGEL = "Enter PCR e-gel";
    public static final String GENERATE_LR_PLATE = "Generate LR reaction plate";
    
    public static final String PLATE_CONDENSATION = "384 well plate condensation";
    
    private int id;
    private String processcode;
    private String processname;
    private Vector subprotocol = new Vector();
    
    /**
     * Constructor.
     *
     * @param id The protocol id.
     */
    public Protocol(int id) throws FlexDatabaseException {
        
        
        if (ProjectWorkflowProtocolInfo.getInstance().getProtocolsByID() != null 
                && ProjectWorkflowProtocolInfo.getInstance().getProtocolsByID().get (String.valueOf(id)) != null) {
            Protocol pr = (Protocol)ProjectWorkflowProtocolInfo.getInstance().getProtocolsByID() .get(String.valueOf(id));
            
            this.id = id;
            this.processcode = pr.getProcesscode();
            this.processname = pr.getProcessname();
            this.subprotocol = pr.getSubprotocol();
            return;
        }
        
        String sql = "select protocolid, processcode, processname " +
        "from processprotocol " +
        "where protocolid = " + id;
   System.out.println(sql);     
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        // only one result should be returned if any
        //Vector protocolVect = t.executeSql(sql);
        CachedRowSet protocolRowSet = t.executeQuery(sql);
        // only one protocol should be found
        if(protocolRowSet.size() == 1) {
            
            try {
                protocolRowSet.next();
                
        /*
         * if a record is found, assign values to the object
         * and find the sub protocols
         */
                
                this.id = protocolRowSet.getInt("PROTOCOLID");
                
                this.processcode = protocolRowSet.getString("PROCESSCODE");
                this.processname = protocolRowSet.getString("PROCESSNAME");
            } catch(SQLException sqlE) {
                throw new FlexDatabaseException("Cannot initialize protocol with process name: "+processname+"\n"+sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(protocolRowSet);
            }
            
            this.processname = processname;
            
            populateSubProtocols();
        } else {
            throw new FlexDatabaseException("No database record found for " + id);
        }
    }
    
    /**
     * Constructor.
     *
     * @param id The protocol id.
     * @param processcode The process code of the protocol.
     * @param processname The process name of the protocol.
     *
     * @return The Protocol object.
     * @exception FlexDatabaseException.
     */
    public Protocol(int id, String processcode, String processname) throws FlexDatabaseException {
        this.id = id;
        this.processcode = processcode;
        this.processname = processname;
        
        // populate the sub protocols
        populateSubProtocols();
        
        
    }
     public Protocol(int id, String processcode, String processname, int mode) throws FlexDatabaseException
     {
        this.id = id;
        this.processcode = processcode;
        this.processname = processname;
    
    }
    public void addSubProtocol(SubProtocol  v){  if( subprotocol== null) subprotocol = new Vector();  subprotocol .add(v);}
   /**
     * Constructor.
     *
     * @param id The protocol id.
     * @param processcode The process code of the protocol.
     * @param processname The process name of the protocol.
     * @param subprotocol The subprotocol used for this protocol.
     * @return The Protocol object.
     */
    public Protocol(int id, String processcode, String processname, Vector subprotocol) {
        this.id = id;
        this.processcode = processcode;
        this.processname = processname;
        this.subprotocol = subprotocol;
    }
    
    /**
     * Constructor
     *
     * @param processname The processname of the protocol
     *
     * @exception FlexDatabaseException
     */
    public Protocol(String processname) throws FlexDatabaseException {
        
        
        if (ProjectWorkflowProtocolInfo.getInstance().getProtocolsByName() != null 
                && ProjectWorkflowProtocolInfo.getInstance().getProtocolsByName().get(processname) != null) {
            
            Protocol pr = (Protocol)ProjectWorkflowProtocolInfo.getInstance().getProtocolsByName().get(processname);
            
            this.id = pr.getId();
            this.processcode = pr.getProcesscode();
            this.processname = pr.getProcessname();
            this.subprotocol = pr.getSubprotocol();
            return;
        }
        
        String sql = "select protocolid, processcode, processname " +
        "from processprotocol " +
        "where processname = '" + processname +"'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        // only one result should be returned if any
        //Vector protocolVect = t.executeSql(sql);
        CachedRowSet protocolRowSet = t.executeQuery(sql);
        // only one protocol should be found
        if(protocolRowSet.size() == 1) {
            
            try {
                protocolRowSet.next();
                
        /*
         * if a record is found, assign values to the object
         * and find the sub protocols
         */
                
                this.id = protocolRowSet.getInt("PROTOCOLID");
                
                this.processcode = protocolRowSet.getString("PROCESSCODE");
                this.processname = protocolRowSet.getString("PROCESSNAME");
            } catch(SQLException sqlE) {
                throw new FlexDatabaseException("Cannot initialize protocol with process name: "+processname+"\n"+sqlE+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(protocolRowSet);
            }
            
            this.processname = processname;
            
            populateSubProtocols();
        } else {
            throw new FlexDatabaseException("No database record found for " + processname);
        }
    }
    
    
    /**
     * Helper method to populate subprotocols
     *
     */
    private void populateSubProtocols() throws FlexDatabaseException {
        String sql =
        "select subprotocolname, subprotocoldescription from subprotocol where protocolid="+id;
   DatabaseTransaction t =
        DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        try {
            while(rs.next()) {
                String name = rs.getString("SUBPROTOCOLNAME");
                String description = rs.getString("SUBPROTOCOLDESCRIPTION");
                SubProtocol subProtocol = new SubProtocol(name, description);
                subprotocol.addElement(subProtocol);
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Cannot populate subprotocol.\n"+sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Return the protocol id.
     *
     * @return The protocol id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the process code.
     *
     * @return The process code.
     */
    public String getProcesscode() {
        return processcode;
    }
    
    /**
     * Return the process name.
     *
     * @return The process name.
     */
    public String getProcessname() {
        return this.processname;
    }
    
    /**
     * Return subprotocol.
     *
     * @return The subprotocol as a Vector.
     */
    public Vector getSubprotocol() {
        return subprotocol;
    }
    
    /**
     * string representation of protocol.
     *
     * @return processname of the protocol
     */
    public String toString() {
        return this.processname;
    }
    
    public static void main(String [] args) throws Exception {
        Protocol test = new Protocol("generate agar plates (48 well grid plate)");
        Vector subProtocol = test.getSubprotocol();
        for(int i=0; i<subProtocol.size(); i++) {
            SubProtocol p = (SubProtocol)subProtocol.elementAt(i);
            System.out.println("Name: "+p.getName());
            System.out.println("Description: "+p.getDescription());
        }
    }
}
