/*
 * ContainerProcessObject.java
 *
 * Created on June 8, 2001, 2:40 PM
 *
 * This class represents a plateset object that gets executed during a process.
 */

package edu.harvard.med.hip.flex.process;

import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ContainerProcessObject extends ProcessObject {
    /**
     * Constructor.
     *
     * @param id The id of the processed object.
     * @param executionid The execution id of the processed object.
     * @param iotype The input/output type of the  processed object.
     *
     * @return A ProcessObject object.
     */
    public ContainerProcessObject(int id, int executionid, String iotype) {
        super(id, executionid, iotype);
    }

    /**
     * Constructor.
     *
     * @param id The id of the processed object.
     * @param iotype The input/output type of the  processed object.
     *
     * @return A ProcessObject object.
     */
    public ContainerProcessObject(int id, String iotype) {
        super(id, iotype);
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
                     "(executionid, inputoutputflag, containerid) "+
                     "values("+executionid+",'"+iotype+"',"+id+")";
        DatabaseTransaction.executeUpdate(sql,conn);
    }
    
    /**
     * Update the platesetid in the database.
     *
     * @param platesetid The value to be updated.
     * @param conn The database connection.
     */
    public void updatePlateset(int platesetid, Connection conn) throws FlexDatabaseException {
        String sql = "update processobject "+
                    " set platesetid="+platesetid+
                    " where executionid="+executionid+
                    " and inputoutputflag='"+iotype+"'"+
                    " and containerid="+id;
        DatabaseTransaction.executeUpdate(sql, conn);
    }

    //**********************************************************//
    //              Testing                                     //
    //**********************************************************//
    public static void main(String[] args) {
        ContainerProcessObject seqobject = new ContainerProcessObject(1, 28, "I");
        Connection c = null;
      
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            c = t.requestConnection();
            seqobject.insert(c);
        } catch (FlexDatabaseException ex) {
            System.err.println(ex);
        } finally {
            try {
                c.close();
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }
    }
}

