/**
 * $Id: FlexDefPopulator.java,v 1.1 2001-07-06 19:28:56 jmunoz Exp $
 *
 * File     	: FlexDefPopulator.java
 * Date     	: 06072001
 * Author	: Dongmei Zuo
 */

package edu.harvard.med.hip.flex.util;

import edu.harvard.med.hip.flex.database.*;
import java.util.Vector;
import java.sql.*;

/**
 * This class gets all the data from the vocabulary table.
 */
public class FlexDefPopulator {
    /**
     * This is a static method to get the data from given vocabulary table.
     *
     * @param table The vocabulary table name.
     * @return A Vector containing all the data in the table.
     * @exception FlexDatabaseException.
     */
    public static Vector getData(String table) throws FlexDatabaseException {
        Vector v = new Vector();
        String sql = "select * from "+table;
        
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        
        ResultSet rs = t.executeQuery(sql);
        try {
            while (rs.next()) {                
                String s = rs.getString(1);
                v.addElement(s);
            }
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE+"\nSQL: "+sql);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return v;
    }
    
    //******************************************************//
    //			Test				//
    //******************************************************//
    public static void main(String [] args) {
        try {
            Vector v = FlexDefPopulator.getData("species");
            System.out.println(v);
        } catch (FlexDatabaseException e) {
            System.out.println(e);
        }
    }
}