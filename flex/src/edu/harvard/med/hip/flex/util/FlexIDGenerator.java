/**
 * $Id: FlexIDGenerator.java,v 1.8 2003-11-17 21:10:08 dzuo Exp $
 *
 * File     	: FlexIDGenerator.java
 * Date     	: 04182001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.util;

import java.util.*;
import java.math.BigDecimal;
import java.sql.*;
import javax.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 * This class gets the next primary key from the database
 * for any given table.
 */
public class FlexIDGenerator {
    /**
     * This is a static method to get the primary key for the given table
     * from the database.
     *
     * @param sequenceName The given sequence that the primary key is
     * 	    generated from.
     * @return An integer representing the primary key.
     * @exception FlexDatabaseException.
     */
    public static int getID(String sequenceName) throws FlexDatabaseException {
        int id = 0;
        String sql = "select "+sequenceName+".nextval as id from dual";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        RowSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                
                id = rs.getInt("ID");
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
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
     * @exception FlexDatabaseException.
     */
    public static int getCurrentId(String sequenceName) throws FlexDatabaseException {
        int id = 0;
        String sql = "select "+sequenceName+".currval as id from dual";
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        RowSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {
                
                id = rs.getInt("ID");
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }
    
    
      /** Find how many MGC containers alredy exists in the FlexDatabase
     *  because label format is  MGC+number of MGC clone taged to 6 degits (MGC000001)
     */
    public static int getCount(String tableName) throws FlexDatabaseException
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
    
    public static int getMaxid(String tableName, String idColumn) throws FlexDatabaseException, SQLException {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        String sql = "select max("+idColumn+") from "+tableName;
        ResultSet rs = t.executeQuery(sql);
        int id = 0;
        if(rs.next()) {
            id = rs.getInt(1);
        } else {
            throw new FlexDatabaseException("Error occured while executing query: "+sql);
        }
        DatabaseTransaction.closeResultSet(rs);
        return id;
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
        try {
            int id = FlexIDGenerator.getID("platesetid");
            System.out.println(id);
            id = FlexIDGenerator.getCurrentId("platesetid");
            System.out.println(id);
            id = FlexIDGenerator.getCount("mgcclone");
            System.out.println(id);
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
    }
}