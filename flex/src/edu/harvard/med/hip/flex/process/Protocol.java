/**
 * $Id: Protocol.java,v 1.5 2001-06-12 17:18:33 dongmei_zuo Exp $
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
    private int id;
    private String processcode;
    private String processname;
    private Vector subprotocol = new Vector();
 
    /**
     * Constructor.
     *
     * @param id The protocol id.
     *
     * @return The Protocol object.
     */
    public Protocol(int id) {
        this.id = id;
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
        String sql = "select protocolid, processcode " +
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
        "select subprotocolname from subprotocol where protocolid="+id;
        DatabaseTransaction t =
        DatabaseTransaction.getInstance();
        RowSet rs = t.executeQuery(sql);
        try {
            while(rs.next()) {
                String name = rs.getString("SUBPROTOCOLNAME");
                subprotocol.addElement(name);
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
     * Return subprotocol.
     *
     * @return The subprotocol as a Vector.
     */
    public Vector getSubprotocol() {
        return subprotocol;
    }
    
    public static void main(String [] args) throws Exception {
        Protocol test = new Protocol("approve sequences");
        
        
    }
}
