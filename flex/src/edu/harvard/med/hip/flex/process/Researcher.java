/**
 * $Id: Researcher.java,v 1.2 2001-05-24 12:13:05 dongmei_zuo Exp $
 *
 * File     	: Researcher.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.process;

import java.util.*;
import java.sql.*;
import java.math.BigDecimal;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;

import sun.jdbc.rowset.*;

/**
 * Represents the researcher object corresponding to researcher table.
 */
public class Researcher {
    private int id;
    private String name;
    private String barcode;
    private String isActive;
    
    /**
     * Constructor.
     *
     * @param id The researcher id.
     * @param name The researcher name.
     * @param barcode The researcher barcode.
     * @param isActive Whether the researcher is active or not.
     * @return The Researcher object.
     */
    public Researcher(int id, String name, String barcode, String isActive) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.isActive = isActive;
    }
    
    /**
     * Constructor.
     *
     * @param barcode The researcher's barcode.
     * @return The Researcher object.
     * @exception FlexDatabaseException, FlexProcessException.
     */
    public Researcher(String barcode) throws FlexDatabaseException, FlexProcessException {
        this.barcode = barcode;
        String sql ="select * from researcher where researcherbarcode = '"+barcode+"'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        CachedRowSet crs = null;
        crs = t.executeQuery(sql);
        
        if(crs.size()==0) {
            throw new FlexProcessException("Cannot initialize Researcher with barcode: "+barcode);
        }
        
        try {
            while(crs.next()) {
                
                id = crs.getInt("RESEARCHERID");
                name = crs.getString("RESEARCHERNAME");
                barcode = crs.getString("RESEARCHERBARCODE");
                isActive = crs.getString("ACTIVEFLAG_YN").trim();
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
    }
    
    /**
     * Return the researcher id.
     *
     * @return The researcher id.
     */
    public int getId() {
        return id;
    }
    
    /**
     * Return the researcher name.
     *
     * @return The researcher name.
     */
    public String getName() {
        return name;
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
        Connection conn = null;
        try {
            String sql ="insert into researcher values (1, 'Tester', 'AB0000', 'Y')";
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            t.executeUpdate(sql,conn);
            Researcher r = new Researcher("AB0000");
            System.out.println(r.getId());
            System.out.println(r.getName());
            conn.rollback();
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        } catch (FlexProcessException e) {
            System.out.println(e);
        } catch (SQLException sqlE) {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
}
