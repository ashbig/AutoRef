/*
 * SubProtocol.java
 *
 * Represents the subprotocol table in the database.
 *
 * Created on July 19, 2001, 1:48 PM
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.database.*;
import java.sql.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SubProtocol {
    private String name;
    private String description;
    
    /**
     * Constructor.
     */
    public SubProtocol(String name) {
        this.name = name;
    }
    
    /** Creates new SubProtocol */
    public SubProtocol(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Constructor.
     */
    public SubProtocol(int protocolid, String name) {
        this.name = name;
                
        String sql = "select * from subprotocol"+
                     " where protocolid = "+protocolid+
                     " and subprotocolname='"+name+"'";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if(rs.next()) {
                this.description = rs.getString("SUBPROTOCOLDESCRIPTION");
            }
        } catch (SQLException ex) {
            //System.out.println(ex);
        } catch (FlexDatabaseException ex) {
            //System.out.println(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
            
    /**
     * Return the protocol name.
     *
     * @return The protocol name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Return the protocol description.
     *
     * @return The protocol description.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the protocol name.
     *
     * @param name The value to be set to.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Set the protocol description.
     *
     * @param description The value to be set to.
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    public static void main(String [] args) {
        SubProtocol p = new SubProtocol(6, "M.B.2.1");
        System.out.println("Subprotocol: "+p.getDescription());
    }
}
