/*
 * File : DatabaseTransaction.java
 * Classes : DatabaseTransaction
 *
 * Description :
 *
 *    A high level API for accessing a pooled data source.
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.11 $
 * $Date: 2001-06-22 14:47:23 $
 * $Author: dongmei_zuo $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 22, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    May-22-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */

package edu.harvard.med.hip.flex.database;


import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import javax.sql.*;
import com.codestudio.sql.*;
import sun.jdbc.rowset.*;

/**
 *
 * The DatabaseTransaction class basically servers as a wrapper
 * around JDBC.
 *
 * DatabaseTransaction is implemented as a singleton.
 *
 * @author     $Author: dongmei_zuo $
 * @version    $Revision: 1.11 $ $Date: 2001-06-22 14:47:23 $
 */

public class DatabaseTransaction {
    
    // singleton instance.
    private static DatabaseTransaction instance = null;
    
    // the datasource to get the pooled connections from
    private static DataSource ds = null;
    
    // start up poolman
    static {
        PoolMan.start();
    }
    // Private constructor method. Autocommit is set to false.
    protected DatabaseTransaction() throws FlexDatabaseException {
        try {
            // look up the Datasource
            ds = PoolMan.findDataSource("jndi-walldb");
        }
        catch( SQLException e ) {
            System.out.println("unable to find datasource");
            try {
                Thread.sleep(2000);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            FlexDatabaseException fde =
            new FlexDatabaseException("Cannot instantiate DatabaseTransaction object.\n"+e.getMessage());
            
            throw fde;
        }
        
    } // end constructor
    
    
    /**
     * This class is implemented as a Singleton.
     * Returns a <code>DatabaseTransaction</code> object.
     *
     * @return A <code>DatabaseTransaction</code> object.
     */
    public static DatabaseTransaction getInstance()
    throws FlexDatabaseException {
        if (instance == null) {
            instance = new DatabaseTransaction();
        }
        
        return instance;
        
    } // end getInstance()
    
    
    /**
     * Requests a Connection from the pool.
     *
     * @param   autoCommit <code>boolean</code> whether or not the connection
     *          should use autocommit.  True for autocommit, false otherwise.
     *
     * @return A <code>Connection</code> object from the pool.
     *
     * @throws FlexDatabaseException
     */
    public Connection requestConnection(boolean autoCommit)
    throws FlexDatabaseException {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(autoCommit);
            return conn;
        } catch(SQLException sqlE) {
            DatabaseTransaction.closeConnection(conn);
            throw new FlexDatabaseException("Cannot get Connection.\n"+sqlE.getMessage());
            
        }
        
    } // end requestConnection()
    
    
    /**
     * Requests a Connection from the pool with autocommit turned off.
     *
     * @param   autoCommit boolean whether or not the connection should use
     *          autocommit.  True for autocommit, false otherwise.
     *
     * @return A Connection object from the pool.
     *
     * @throws FlexDatabaseException
     */
    public Connection requestConnection() throws FlexDatabaseException {
        
        return requestConnection(false);
        
    } // end requestConnection()
    
    
    /**
     * Executes an update, insert or delete.  The return value is the number
     * of rows affected.
     *
     * @throws FlexDatabaseException
     */
    public static int executeUpdate(PreparedStatement ps)
    throws FlexDatabaseException{
        try {
            return ps.executeUpdate();
        } catch(SQLException sqlE) {
            throw new FlexDatabaseException(sqlE.getMessage()+"\nSQL: "+ps);
        }
    } // end executeUpdate()
    
    
    /**
     * executes an update, insert or delete with the given query string and
     * database connection.  Returns the number of rows affected
     *
     * @param   sql     The query string to execute.
     * @param   conn    The <code>Connection</code> to execute the query with.
     *
     * @return number or rows affected.
     */
    public static int executeUpdate(String sql, Connection conn)
    throws FlexDatabaseException {
        int retVal = 0;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            retVal = stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally {
            closeStatement(stmt);
        }
        return retVal;
        
    } // end executeUpdate()
    
    
    /**
     * Executes a sql statement and returns a Row set object with the results
     * from the query.
     *
     * The current implementation uses a CachedRowSet which may need to be
     * changed to a JDBCRowSet if too many rows are returned.
     *
     * @param sql The sql String to execute.
     *
     * @return <code>CachedRowSet</code> with the data from the query.
     *
     * @throws FlexDatabaseException
     */
    public CachedRowSet executeQuery(String sql) throws FlexDatabaseException {
        Connection conn = null;
        CachedRowSet crs = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            
            conn = requestConnection();
            crs = new CachedRowSet();
            stmt = conn.createStatement();
            
            rs = stmt.executeQuery(sql);
            crs.populate(rs);
            return crs;
            
        } catch (SQLException e) {
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally {
            // release database resources
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
    } //end executeSQL
    
    
    
    /**
     * This method executes the requested prepared statement.
     *
     * @param   stmt  The <code>PreparedStatement</code> to execute
     *
     * @return <code>CachedRowSet</code> with result from query
     *
     * @throws  FlexDatabaseException.
     */
    public static CachedRowSet executeQuery(PreparedStatement stmt)
    throws FlexDatabaseException{
        CachedRowSet crs = null;
        ResultSet results = null;
        try {
            crs = new CachedRowSet();
            results = stmt.executeQuery();
            crs.populate(results);
            return crs;
            
            
        } catch (SQLException e) {
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+stmt);
        } finally {
            closeResultSet(results);
        }
    } // end executeQuery()
    
    
    /**
     * Set the parameters for the <code>PreparedStatement</code> object.
     *
     * @param stmt  The <code>PreparedStatement</code> to set values for
     * @param h     The <code>HashTable</code> of values used to set values in
     *              the prepared statement
     *
     * @throws FlexDatabaseException
     */
    public static void setupPreparedStatement(PreparedStatement stmt,
    Hashtable h) throws FlexDatabaseException {
        String type = (String)h.get("type");
        int index = ((Integer)h.get("index")).intValue();
        Object value = h.get("value");
        
        try {
            if(type.equals("double")) {
                stmt.setDouble(index, ((Double)value).doubleValue());
            } else if(type.equals("int")) {
                stmt.setInt(index, ((Integer)value).intValue());
            } else if (type.equals("boolean")) {
                stmt.setBoolean(index, ((Boolean)value).booleanValue());
            } else if (type.equals("string")) {
                stmt.setString(index, (String)value);
            } else if (type.equals("date")) {
                stmt.setDate(index, (java.sql.Date.valueOf((String)value)));
            } else {
                throw new FlexDatabaseException("No such parameter defined.");
            }
        } catch (SQLException e) {
            throw new FlexDatabaseException(e.getMessage());
        }
    }
    
    /**
     * Closes the given <code>Statement</code> ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void closeConnection(Connection conn) {
        try{
            
            conn.close();
            
        } catch(Throwable t) {
            System.out.println("error clossing connection : ");
            t.printStackTrace();
        }
    }
    
    
    /**
     * Closes a statment ignoring all exceptions.
     *
     * @param stmt The <code>Statement</code> to close.
     */
    public static void closeStatement(Statement stmt) {
        try {
            stmt.close();
        } catch(Throwable t){}
    }
    
    /**
     * Closes a <code>ResultSet</code> ignoring all exceptions.
     *
     * @param rs The <code>ResultSet</code> to close
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch(Throwable t){}
    }
    
    /**
     * Commits a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void commit(Connection conn) {
        try {
            conn.commit();
        } catch(Throwable t) {}
    }
    
    
    /**
     * Rolls back a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch(Throwable t) {}
    }
    
    public static void main(String args[]) {
        RowSet rs = null;
        
        DatabaseTransaction dt = null;
        Connection conn = null;
        PreparedStatement ps = null;
        
        
        
        // test execute sql
        try {
            
            dt = DatabaseTransaction.getInstance();
            for (int i = 0 ; i < 300 ; i++) {
                System.out.println("i: " + i);
               edu.harvard.med.hip.flex.process.Process.findProcess(new edu.harvard.med.hip.flex.core.Container(200),new edu.harvard.med.hip.flex.process.Protocol(8));
               // rs = dt.executeQuery("select * from dual");
               
            }
        } catch(FlexDatabaseException fde) {
            fde.printStackTrace();
       } catch(edu.harvard.med.hip.flex.core.FlexCoreException core) {
            core.printStackTrace();
        
        }
        finally {
            DatabaseTransaction.closeResultSet(rs);
            
        }
        
        
        
        System.out.println("End of Main");
    } // end main()
    
    
} // end class DatabaseTransaction

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
