/**
 * $Id: FlexIDGenerator.java,v 1.4 2001-05-24 14:49:04 dongmei_zuo Exp $
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
            throw new FlexDatabaseException(sqlE);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return id;
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
        try {
            int id = FlexIDGenerator.getID("sampleid");
            System.out.println(id);
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
    }
}