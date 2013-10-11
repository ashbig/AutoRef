package dao;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;
import java.util.*;
import javax.sql.rowset.CachedRowSet;

/**
 *
 * The DatabaseTransaction class basically servers as a wrapper
 * around JDBC.
 *
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 * //////////  This version of DatabaseTransaction is used only for local code developing and testing /////////////////
 * ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 *
 * @author     $Author: dz4 $
 * @version    $Revision: 1.1 $ $Date: 2013-09-06 15:53:31 $
 */
public class DatabaseTransaction {    ///////// need to change DatabaseTransaction2 ///////////
    
    public static Connection connection = null;
   /**
    private String url = "jdbc:oracle:thin:@//127.0.0.1:1521/HMSDEV.med.harvard.edu";
    private String username = "devplasmid";
    private String password = "quozubvuod3";
 */  
   
    private String url = "jdbc:oracle:thin:@//127.0.0.1:1522/RITGAPPS.med.harvard.edu";
    private String username = "plasmid";
    private String password = "orvayraddod2"; 

    /** Creates new DatabaseManager */
    public DatabaseTransaction() {
    }
    /**
     * Create new DatabaseManager by providing the url, username and password.
     *
     * @param url The url of the database instance.
     * @param username The username of the database user.
     * @param password The password of the database user.
     *
     * @return The DatabaseManager object.
     */
    public DatabaseTransaction(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DatabaseTransaction getInstance(){
        return new DatabaseTransaction();
    }

    /**
     * Connect to the database.
     *
     * @return Connection object.
     */
    public Connection requestConnection() throws DaoException {
        try {
            String driverName = "oracle.jdbc.driver.OracleDriver";
            Class.forName(driverName);
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            return connection;
        }
        catch( SQLException e ) {
            DatabaseTransaction.closeConnection(connection);
            throw new DaoException("Cannot get Connection.\n" + e.getMessage());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            throw new DaoException("Cannot get database drive.");
        }
    }

    /**
     * Executes an update, insert or delete.  The return value is the number
     * of rows affected.
     *
     * @throws DatabaseException
     */
    public static int executeUpdate(PreparedStatement ps)
    throws DaoException{
        try {
            return ps.executeUpdate();
        } catch(SQLException sqlE) {
            throw new DaoException(sqlE.getMessage()+"\nSQL: "+ps);
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
    throws DaoException {
        int retVal = 0;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            retVal = stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new DaoException(e.getMessage()+"\nSQL: "+sql);
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
    public CachedRowSet executeQuery(String sql) throws DaoException {
        Connection conn = null;
        CachedRowSet crs = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = requestConnection();
            crs = new CachedRowSetImpl();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            crs.populate(rs);

        } catch (SQLException e) {
            throw new DaoException(e.getMessage()+"\nSQL: "+sql);
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
    throws DaoException{
        CachedRowSet crs = null;
        ResultSet results = null;
        try {
            crs = new CachedRowSetImpl();
            results = stmt.executeQuery();
            crs.populate(results);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage()+"\nSQL: "+stmt);
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
    Hashtable h) throws DaoException {
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
                throw new DaoException("No such parameter defined.");
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
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
        } catch(Throwable t){
            t.printStackTrace();
        }
    }
    /**
     * Closes a <code>ResultSet</code> ignoring all exceptions.
     *
     * @param rs The <code>ResultSet</code> to close
     */
    public static void closeResultSet(ResultSet rs) {
        try {
   if(rs != null)
             rs.close();
        } catch(Throwable t){
            t.printStackTrace();
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
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Rolls back a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void rollback(Connection conn)  {
        try {
            conn.rollback();
        } catch(Throwable t) {
            t.printStackTrace();
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
        while (quoteIndex !=-1) {
            int offset = quoteIndex + curIndex++;
            stringBuff.insert(offset, "'");
            quoteIndex = string.indexOf("'",quoteIndex+1);
        }
        return stringBuff.toString();
    }
 
    //////////////////////////////////////////////////////////////////////////////////////////////
    public static void main(String args[]) throws DaoException, SQLException{
        ResultSet rs = null;
        ResultSet rs2 = null;
        Connection conn1 = null;
        Connection conn2 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        // test execute sql
        DatabaseTransaction manager = DatabaseTransaction.getInstance();
 
        try {
            String sql1 = "select usergroup from userprofile where username='hweng'";

            conn1 = manager.requestConnection();

            ps1 = conn1.prepareStatement(sql1);
            rs = DatabaseTransaction.executeQuery(ps1);
            while(rs.next())
                System.out.println("usergroup: " + rs.getString(1));

            rs2 = manager.executeQuery("select name from project");
            while(rs2.next())
                System.out.println("project: "+rs.getString(1));
        } catch(DaoException fde) {
            fde.printStackTrace();
        }
        catch(SQLException sqlE) {
            sqlE.printStackTrace();
        }
        finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
            DatabaseTransaction.closeStatement(ps1);
            DatabaseTransaction.closeConnection(conn1);
 
        }
 
        System.out.println("End of Main");
        System.exit(0);
    } // end main()

} // end class DatabaseTransaction2
/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */