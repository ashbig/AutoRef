/**
 * $Id: BecIDGenerator.java,v 1.1 2003-03-14 21:13:30 Elena Exp $
 *
 * File     	: BecIDGenerator.java
 * Date     	: 04182001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.bec.util;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.bec.database.*;

/**
 * This class gets the next primary key from the database
 * for any given table.
 */
public class BecIDGenerator {
    /**
     * This is a static method to get the primary key for the given table
     * from the database.
     *
     * @param sequenceName The given sequence that the primary key is
     * 	    generated from.
     * @return An integer representing the primary key.
     * @exception BecDatabaseException.
     */
    public static int getID(String sequenceName) throws BecDatabaseException {
        int id = 0;
        String sql = "select "+sequenceName+".nextval as id from dual";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        RowSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                
                id = rs.getInt("ID");
            }
        } catch(SQLException sqlE) {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }
    
     /**
     * This is a static method to get the primary key of the last record in the 
      * for the given table  from the database (primary key is sequence).
     *
     * @param sequenceName The given sequence that the primary key is
     * 	    generated from.
     * @return An integer representing the primary key.
     * @exception BecDatabaseException.
     */
    public static int getCurrentId(String sequenceName) throws BecDatabaseException {
        int id = 0;
        String sql = "select "+sequenceName+".currval as id from dual";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        RowSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                
                id = rs.getInt("ID");
            }
        } catch(SQLException sqlE) {
            throw new BecDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }
    
    
      /** Find how many MGC containers alredy exists in the BecDatabase
     *  because label format is  MGC+number of MGC clone taged to 6 degits (MGC000001)
     */
    public static int getCount(String tableName) throws BecDatabaseException
    {
        int count = 0;
        String sql = "select count(*) as count from " + tableName;
        RowSet rs = null;
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while(rs.next()) {
                count = rs.getInt("COUNT");
            }
        }catch (Exception e){ return -1;}
        return count;
    }
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
        try {
            int id = BecIDGenerator.getID("platesetid");
            System.out.println(id);
            id = BecIDGenerator.getCurrentId("platesetid");
            System.out.println(id);
            id = BecIDGenerator.getCount("mgcclone");
            System.out.println(id);
        } catch (BecDatabaseException e) {
            System.out.println(e);
        }
    }
}