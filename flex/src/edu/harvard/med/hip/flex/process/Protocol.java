/**
 * $Id: Protocol.java,v 1.22 2002-05-24 16:55:08 Elena Exp $
 *
 * File     : FlexProcessException.java
 * Date     : 04162001
 * Author	: Wendy Mar, Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.math.*;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import javax.sql.*;
import sun.jdbc.rowset.*;

/**
 * Represents the protocol object corresponding to the
 * protocol table.
 */
public class Protocol {
    /*
     * the protocol strings
     */
    public static final String APPROVE_SEQUENCES = "approve sequences";
    public static final String IDENTIFY_SEQUENCES_FROM_UNIGENE=
        "identify sequences from unigene";
    public static final String DESIGN_CONSTRUCTS= "design constructs";
    public static final String GENERATE_OLIGO_ORDERS= "generate oligo orders";
    public static final String RECEIVE_OLIGO_PLATES= "receive oligo plates";
    public static final String DILUTE_OLIGO_PLATE = "dilute oligo plate";
    public static final String GENERATE_PCR_PLATES = "generate step1 PCR plates";
    public static final String GENERATE_STEP2_PCR_PLATES = "generate step2 PCR plates";
    public static final String RUN_PCR_GEL = "run PCR gel";
    public static final String GENERATE_FILTER_PLATES="generate filter plates";
    public static final String GENERATE_BP_REACTION_PLATES=
        "generate BP reaction plates";
    public static final String GENERATE_TRANSFORMATION_PLATES =
        "generate transformation plates";
    public static final String GENERATE_AGAR_PLATES=
        "generate agar plates";
    public static final String GENERATE_CULTURE_BLOCKS_FOR_ISOLATES=
        "generate culture blocks for isolates";
    public static final String GENERATE_DNA_PLATES="generate DNA plates";
    public static final String GENERATE_GLYCEROL_PLATES=
        "generate glycerol plates";
    public static final String GENERATE_SEQUENCING_PCR_PLATES=
        "generate sequencing PCR plates";
    public static final String GENERATE_SEQUENCING_DNA_PLATES = 
        "generate sequencing DNA plates";
    public static final String SUBMIT_SEQUENCING_ORDERS=
        "submit sequencing orders";
    public static final String RECEIVE_SEQUENCING_RESULTS=
        "receive sequencing results";
    public static final String PERFORM_TRANSFORMATION = 
        "perform transformation";
    public static final String ENTER_PCR_GEL_RESULTS = 
        "enter PCR gel results";
    public static final String ENTER_AGAR_PLATE_RESULTS = 
        "enter agar plate results";
    public static final String ENTER_DNA_GEL_RESULTS = 
        "enter DNA gel results";
    public static final String GENERATE_CAPTURE_REACTION = 
        "generate capture reaction plate";
    public static final String GENERATE_GRID_PLATE = 
        "generate agar plates (48 well grid plate)";
    public static final String PICK_COLONY = 
        "generate culture blocks for isolates from 48 well plates";
    public static final String ENTER_CULTURE_RESULTS = "enter culture results";
    public static final String CREATE_CULTURE_FROM_MGC = 
        "Create culture block from MGC master plate";
    public static final String CREATE_GLYCEROL_FROM_CULTURE = 
        "Create glycerol stock from MGC culture block";
    public static final String ENTER_MGC_CULTURE_RESULTS = 
        "Enter MGC culture result";
    public static final String CREATE_GLYCEROL_FROM_REARRAYED_CULTURE = 
        "Create glycerol stock from rearrayed culture block";
    public static final String CREATE_DNA_FROM_REARRAYED_CULTURE = 
        "Create DNA template plate from rearrayed culture block";
    public static final String IMPORT_MGC_REQUEST = 
        "Import MGC request";
    
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
           String sql = "select protocolid, processcode, processname " +
        "from processprotocol " +
        "where protocolid = " + id;
        
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
    
    /**
     * Constructor
     *
     * @param processname The processname of the protocol
     *
     * @exception FlexDatabaseException
     */
    public Protocol(String processname) throws FlexDatabaseException {
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
