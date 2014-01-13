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
 * $Revision: 1.7 $
 * $Date: 2013-08-22 18:54:03 $
 * $Author: dz4 $
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
 *    Feb-21-2002: DZ - Modified the API to use Java's DataSource class for
 *                  connection pooling to get rid of poolman.
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
package plasmid.database;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import javax.naming.InitialContext;

import javax.sql.*;


import sun.jdbc.rowset.*;

/**
 *
 * The DatabaseTransaction class basically servers as a wrapper
 * around JDBC.
 *
 * DatabaseTransaction is implemented as a singleton.
 *
 */
public class DatabaseTransaction {

    static final Logger logger = Logger.getLogger(DatabaseTransaction.class.getName());
    private static DatabaseTransaction pool = null;
    private static DataSource dataSource = null;


    public synchronized static DatabaseTransaction getInstance() {
        if (pool == null) {
            pool = new DatabaseTransaction();
        }
        return pool;
    }

    private DatabaseTransaction() {
        try {
            //logger.info("Attempting to lookup the database info from the IC");
            //System.out.println("Attempting to lookup the database info from the IC");
            InitialContext ic = new InitialContext();
            dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/plasmid");
            //logger.info("DT creation worked");
            //System.out.println("DT creation worked");
        } catch (Exception ex) {
            ex.printStackTrace();
            //System.out.println("cannot get database connection.\n" + ex.getMessage());
            //logger.warning("cannot get database connection.\n" + ex.getMessage());
        }
    }

    /**
     * Requests a Connection from the pool.
     *
     * @param   autoCommit <code>boolean</code> whether or not the connection
     *          should use autocommit.  True for autocommit, false otherwise.
     *
     * @return A <code>Connection</code> object from the pool.
     *
     * @throws DatabaseException
     */
    public Connection requestConnection(boolean autoCommit)
            throws DatabaseException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(autoCommit);
            return conn;
        } catch (SQLException sqlE) {
            DatabaseTransaction.closeConnection(conn);
            throw new DatabaseException("Cannot get Connection.\n" + sqlE.getMessage());

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
     * @throws DatabaseException
     */
    public Connection requestConnection() throws DatabaseException {

        return requestConnection(false);

    } // end requestConnection()

    /**
     * Executes an update, insert or delete.  The return value is the number
     * of rows affected.
     *
     * @throws DatabaseException
     */
    public static int executeUpdate(PreparedStatement ps)
            throws DatabaseException {
        try {
            return ps.executeUpdate();
        } catch (SQLException sqlE) {
            throw new DatabaseException(sqlE.getMessage() + "\nSQL: " + ps);
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
            throws DatabaseException {
        int retVal = 0;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            retVal = stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new DatabaseException(e.getMessage() + "\nSQL: " + sql);
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
     * @throws DatabaseException
     */
    public CachedRowSet executeQuery(String sql) throws DatabaseException {
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


        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage() + "\nSQL: " + sql);
        } finally {
            // release database resources
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
        return crs;
    } //end executeSQL

    /**
     * This method executes the requested prepared statement.
     *
     * @param   stmt  The <code>PreparedStatement</code> to execute
     *
     * @return <code>CachedRowSet</code> with result from query
     *
     * @throws  DatabaseException.
     */
    public static CachedRowSet executeQuery(PreparedStatement stmt)
            throws DatabaseException {
        CachedRowSet crs = null;
        ResultSet results = null;
        try {
            crs = new CachedRowSet();
            results = stmt.executeQuery();
            crs.populate(results);



        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage() + "\nSQL: " + stmt);
        } finally {
            closeResultSet(results);
        }
        return crs;
    } // end executeQuery()

    /**
     * Set the parameters for the <code>PreparedStatement</code> object.
     *
     * @param stmt  The <code>PreparedStatement</code> to set values for
     * @param h     The <code>HashTable</code> of values used to set values in
     *              the prepared statement
     *
     * @throws DatabaseException
     */
    public static void setupPreparedStatement(PreparedStatement stmt,
            Hashtable h) throws DatabaseException {
        String type = (String) h.get("type");
        int index = ((Integer) h.get("index")).intValue();
        Object value = h.get("value");

        try {
            if (type.equals("double")) {
                stmt.setDouble(index, ((Double) value).doubleValue());
            } else if (type.equals("int")) {
                stmt.setInt(index, ((Integer) value).intValue());
            } else if (type.equals("boolean")) {
                stmt.setBoolean(index, ((Boolean) value).booleanValue());
            } else if (type.equals("string")) {
                stmt.setString(index, (String) value);
            } else if (type.equals("date")) {
                stmt.setDate(index, (java.sql.Date.valueOf((String) value)));
            } else {
                throw new DatabaseException("No such parameter defined.");
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    /**
     * Closes the given <code>Statement</code> ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void closeConnection(Connection conn) {
        try {
            conn.close();

        } catch (Throwable t) {
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
        } catch (Throwable t) {
        }
    }

    /**
     * Closes a <code>ResultSet</code> ignoring all exceptions.
     *
     * @param rs The <code>ResultSet</code> to close
     */
    public static void closeResultSet(ResultSet rs) {
        try {
            rs.close();
        } catch (Throwable t) {
        }
    }

    /**
     * Commits a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void commit(Connection conn) {
        try {
            conn.commit();
        } catch (Throwable t) {
        }
    }

    /**
     * Rolls back a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (Throwable t) {
        }
    }

    /**
     * Makes a string ready for oracle by replacing the ' with ''.
     * 
     * All info entered by the user should be passed to this method 
     * before going to the dabase.
     * 
     * @param string String to convert.
     *
     * @return String ready for oracle insert or where clause.
     */
    public static String prepareString(String string) {
        StringBuffer stringBuff = new StringBuffer(string);
        int quoteIndex = 0;
        int curIndex = 0;

        quoteIndex = string.indexOf("'");
        while (quoteIndex != -1) {
            int offset = quoteIndex + curIndex++;
            stringBuff.insert(offset, "'");
            quoteIndex = string.indexOf("'", quoteIndex + 1);
        }
        return stringBuff.toString();
    }

    public static CachedRowSet executeQuery(String sql, Connection conn) throws DatabaseException {

        CachedRowSet crs = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            crs = new CachedRowSet();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            crs.populate(rs);


        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage() + "\nSQL:" + sql);
        } finally {
            // release database resources
            closeResultSet(rs);
            closeStatement(stmt);
        // closeConnection(conn);
        }
        return crs;
    } //end executeSQL
} // end class DatabaseTransaction

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
