/*
 * ConstructProcessObject.java
 *
 * Created on June 8, 2001, 2:40 PM
 *
 * This class represents a construct object that gets executed during a process.
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ConstructProcessObject extends ProcessObject {
    /**
     * Constructor.
     *
     * @param id The id of the processed object.
     * @param executionid The execution id of the processed object.
     * @param iotype The input/output type of the  processed object.
     *
     * @return A ProcessObject object.
     */
    public ConstructProcessObject(int id, int executionid, String iotype) {
        super(id, executionid, iotype);
    }
    
    /**
     * Insert the record into processobject table.
     *
     * @param conn The <code>Connectoin</code> to do the insert with
     *
     * @exception FlexDatabaseException.
     */
    public void insert(Connection conn)throws FlexDatabaseException {
        String sql = "insert into processobject "+
                     "(executionid, inputoutputflag, constructid) "+
                     "values("+executionid+",'"+iotype+"',"+id+")";
        DatabaseTransaction.executeUpdate(sql,conn);
    }

    //**********************************************************//
    //              Testing                                     //
    //**********************************************************//
    public static void main(String[] args) {
        ConstructProcessObject seqobject = new ConstructProcessObject(1, 28, "I");
        Connection c = null;
      
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            seqobject.insert(c);
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
        } finally {
            try {
                c.close();
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
}

