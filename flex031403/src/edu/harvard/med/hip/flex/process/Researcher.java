/**
 * $Id: Researcher.java,v 1.16 2002-06-11 19:33:58 Elena Exp $
 *
 * File     	: Researcher.java
 * Date     	: 04262001
 * Author	: Dongmei Zuo
 *
 * Modified     : Wendy Mar
 * Date         : 06-07-2001
 *              Added a new constructor for the default user: SYSTEM
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
    
    public static final String          SYSTEM = "DEFAULT";
    
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
        
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                id = rs.getInt("RESEARCHERID");
                name = rs.getString("RESEARCHERNAME");
                barcode = rs.getString("RESEARCHERBARCODE");
                isActive = rs.getString("ACTIVEFLAG_YN").trim();
            } else {
                throw new FlexProcessException("Cannot find Researcher with barcode: "+barcode);
            }
            
            
            
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Constructs a new researcher based on the id of the researcher.
     *
     * @param id The researcher id
     */
    public Researcher(int id) throws FlexDatabaseException{
        DatabaseTransaction dt = DatabaseTransaction.getInstance();
        ResultSet rs = null;
        try {
            
            rs =
            dt.executeQuery("select RESEARCHERID, RESEARCHERBARCODE, "+
            "ACTIVEFLAG_YN, RESEARCHERNAME " +
            "from RESEARCHER where Researcherid = " + id);
            if(rs.next()) {
                
                this.id=rs.getInt("RESEARCHERID");
                
                this.barcode=rs.getString("RESEARCHERBARCODE");
                
                this.isActive=rs.getString("ACTIVEFLAG_YN");
                
                this.name=rs.getString("RESEARCHERNAME");
            } else {
                DatabaseTransaction.closeResultSet(rs);
                throw new FlexDatabaseException("Researcher with id " + id + " does not exist");
                
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    /**
     * default constructor
     */
    public Researcher() {
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
    
    /**
     * @param name .
     *
     * @return The researcher id of the given name.
     */
    public int getId(String name) throws FlexDatabaseException {
        int Id = -1;
        String sql = "select researcherid from researcher where researchername = '" + name + "'";
        ResultSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                Id = rs.getInt(1);
            } else {
                return Id;
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return Id;
    }
    
    /**
     * Gets the reseacher's barcode.
     *
     * @return the researcher's barcode.
     */
    public String getBarcode() {
        return this.barcode;
    }

    /**
     * Return true if the given name exists in the database; false otherwise.
     *
     * @return true if the given researcher name exists in the database; 
     * return false otherwise.
     * @exception FlexDatabaseException.
     */
    public static boolean nameExists(String name) throws FlexDatabaseException {
        String sql ="select * from researcher where researchername = '"+name+"'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }
    
    /**
     * Return true if the barcode is valid; false otherwise.
     *
     * @param barcode The barcode to be validated.
     * @return True if the barcode is valid; false otherwise.
     */
    public static boolean isValid(String barcode) throws FlexDatabaseException {
        String sql ="select * from researcher where researcherbarcode = '"+barcode+"'";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        ResultSet rs = t.executeQuery(sql);
        try{
            if(rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    /**
     * Add the researcher to the database.
     *
     * @param researcherName The researcher name.
     * @param researcherBarcode The researcher barcode.
     * @param conn The Connection object.
     *
     * @return true if added successfully; false otherwise.
     */
    public static boolean addResearcher(String researcherName, String researcherBarcode, Connection conn) {
        String sql = "insert into researcher "+
                     "(researcherid, researchername, researcherbarcode, activeflag_yn) "+
                     "values (researcherid.nextval, '"+researcherName+"', '"+
                     researcherBarcode+"','Y')";
        
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException sqlE) {
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }      
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
            
            if(Researcher.addResearcher("test", "test", conn)) {
                System.out.println("Add researcher successful.");
            } else {
                System.out.println("Add researcher failed.");
            }
            
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
            System.out.println(sqlE);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        try{
            Researcher r = new Researcher();
            int Id = r.getId("SYSTEM");
            System.out.println("The Id for user SYSTEM is " + Id);
        } catch (FlexDatabaseException e){
            System.out.println(e);
        }
        
        try {
            Researcher r = new Researcher(null);
            System.out.println("Error");
        } catch (FlexProcessException e) {
            System.out.println(e);
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
        
        try {
            if(Researcher.isValid(""))
                System.out.println("Testing isValid method - ERROR");
            else {
                System.out.println("Testing isValid method - OK");
            }
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
    } //main
    
}
