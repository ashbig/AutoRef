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
 * $Revision: 1.1 $
 * $Date: 2004-07-23 17:18:25 $
 * $Author: Elena $
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



package edu.harvard.med.hip.bec.database;


import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

import javax.naming.*;
import javax.sql.*;


import sun.jdbc.rowset.*;


 /*
 * The DatabaseTransaction class basically servers as a wrapper
 * around JDBC.
 *
 * DatabaseTransaction is implemented as a singleton.
 *
 * @author     $Author: Elena $
 * @version    $Revision: 1.1 $ $Date: 2004-07-23 17:18:25 $
 */

public class DBTrBECTest
{
    
    //modify for web application
    
    
    
    private  String url = "jdbc:oracle:thin:@bighead:1521:bec";//
    private   String username = "bec";
    private   String password = "bec";
    // singleton instance.
    private static DBTrBECTest instance = null;

    // the datasource to get the pooled connections from
    private static DataSource ds = null;

    // Private constructor method. Autocommit is set to false.
    protected DBTrBECTest(DataSource ds)
    {
        this.ds = ds;
    } // end constructor
    /** Creates new DatabaseManager */
    public DBTrBECTest()
    {
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
    public DBTrBECTest(String url, String username, String password)
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static void init(DataSource ds)
    {
        instance = new DBTrBECTest(ds);
    }

    /**
     * This class is implemented as a Singleton.
     * Returns a <code>DatabaseTransaction</code> object.
     *
     * @return A <code>DatabaseTransaction</code> object.
     */
    public static DBTrBECTest getInstance()
    throws BecDatabaseException
    {
        if (instance == null)
        {
            return new DBTrBECTest();
        }

        return instance;

    } // end getInstance()

     public static DBTrBECTest getInstance(String url, String username, String password)
    throws BecDatabaseException
    {
        if (instance == null)
        {
            return new DBTrBECTest( url,  username,  password);
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
     * @throws BecDatabaseException
     */



    public Connection requestConnection(boolean autocommit) throws BecDatabaseException
    {
       Connection connection = null;
        try
        {
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
             connection = DriverManager.getConnection(url, username, password);
             connection.setAutoCommit(autocommit);
            return connection;
        }
        catch( SQLException e )
        {
            DatabaseTransaction.closeConnection(connection);
            throw new BecDatabaseException("Cannot get Connection.\n" + e.getMessage());
        }
    }


    /**
     * Requests a Connection from the pool with autocommit turned off.
     *
     * @param   autoCommit boolean whether or not the connection should use
     *          autocommit.  True for autocommit, false otherwise.
     *
     * @return A Connection object from the pool.
     *
     * @throws BecDatabaseException
     */
    public Connection requestConnection() throws BecDatabaseException
    {

        return requestConnection(false);

    } // end requestConnection()


    /**
     * Executes an update, insert or delete.  The return value is the number
     * of rows affected.
     *
     * @throws BecDatabaseException
     */
    public static int executeUpdate(PreparedStatement ps)
    throws BecDatabaseException
    {
        try
        {
            return ps.executeUpdate();
        } catch(SQLException sqlE)
        {
            throw new BecDatabaseException(sqlE.getMessage()+"\nSQL: "+ps);
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
    throws BecDatabaseException
    {
        int retVal = 0;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            retVal = stmt.executeUpdate(sql);
        } catch (Exception e)
        {
            throw new BecDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally
        {
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
     * @throws BecDatabaseException
     */
    public CachedRowSet executeQuery(String sql) throws BecDatabaseException
    {
        Connection conn = null;
        CachedRowSet crs = null;
        Statement stmt = null;
        ResultSet rs = null;
        try
        {

            conn = requestConnection();
            crs = new CachedRowSet();
            stmt = conn.createStatement();

            rs = stmt.executeQuery(sql);
            crs.populate(rs);


        } catch (SQLException e)
        {
            throw new BecDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally
        {
            // release database resources
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
        return crs;
    } //end executeSQL

    public static CachedRowSet executeQuery(String sql, Connection conn) throws BecDatabaseException
	    {

	        CachedRowSet crs = null;
	        Statement stmt = null;
	        ResultSet rs = null;
	        try
	        {

	            crs = new CachedRowSet();
	            stmt = conn.createStatement();

	            rs = stmt.executeQuery(sql);
	            crs.populate(rs);


	        } catch (SQLException e)
	        {
	            throw new BecDatabaseException(e.getMessage()+"\nSQL: "+sql);
	        } finally
	        {
	            // release database resources
	            closeResultSet(rs);
	            closeStatement(stmt);
	           // closeConnection(conn);
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
     * @throws  BecDatabaseException.
     */
    public static CachedRowSet executeQuery(PreparedStatement stmt)
    throws BecDatabaseException
    {
        CachedRowSet crs = null;
        ResultSet results = null;
        try
        {
            crs = new CachedRowSet();
            results = stmt.executeQuery();
            crs.populate(results);



        } catch (SQLException e)
        {
            throw new BecDatabaseException(e.getMessage()+"\nSQL: "+stmt);
        } finally
        {
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
     * @throws BecDatabaseException
     */
    public static void setupPreparedStatement(PreparedStatement stmt,
    Hashtable h) throws BecDatabaseException
    {
        String type = (String)h.get("type");
        int index = ((Integer)h.get("index")).intValue();
        Object value = h.get("value");

        try
        {
            if(type.equals("double"))
            {
                stmt.setDouble(index, ((Double)value).doubleValue());
            } else if(type.equals("int"))
            {
                stmt.setInt(index, ((Integer)value).intValue());
            } else if (type.equals("boolean"))
            {
                stmt.setBoolean(index, ((Boolean)value).booleanValue());
            } else if (type.equals("string"))
            {
                stmt.setString(index, (String)value);
            } else if (type.equals("date"))
            {
                stmt.setDate(index, (java.sql.Date.valueOf((String)value)));
            } else
            {
                throw new BecDatabaseException("No such parameter defined.");
            }
        } catch (SQLException e)
        {
            throw new BecDatabaseException(e.getMessage());
        }
    }

    /**
     * Closes the given <code>Statement</code> ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void closeConnection(Connection conn)
    {
        try
        {
            conn.close();

        } catch(Throwable t)
        {
            t.printStackTrace();
        }
    }


    /**
     * Closes a statment ignoring all exceptions.
     *
     * @param stmt The <code>Statement</code> to close.
     */
    public static void closeStatement(Statement stmt)
    {
        try
        {
            stmt.close();
        } catch(Throwable t)
        {}
    }

    /**
     * Closes a <code>ResultSet</code> ignoring all exceptions.
     *
     * @param rs The <code>ResultSet</code> to close
     */
    public static void closeResultSet(ResultSet rs)
    {
        try
        {
            rs.close();
        } catch(Throwable t)
        {}
    }

    /**
     * Commits a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void commit(Connection conn)
    {
        try
        {
            conn.commit();
        } catch(Throwable t)
        {}
    }


    /**
     * Rolls back a connection ignoring all exceptions.
     *
     * @param conn The <code>Connection</code> to close.
     */
    public static void rollback(Connection conn)
    {
        try
        {
            conn.rollback();
        } catch(Throwable t)
        {}
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
    public static String prepareString(String string)
    {
        StringBuffer stringBuff = new StringBuffer(string);
        int quoteIndex = 0;
        int curIndex = 0;

        quoteIndex = string.indexOf("'");
        while (quoteIndex !=-1)
        {
            int offset = quoteIndex + curIndex++;
            stringBuff.insert(offset, "'");
            quoteIndex = string.indexOf("'",quoteIndex+1);
        }
        return stringBuff.toString();
    }

    public static void main(String args[]) throws BecDatabaseException, SQLException
    {
        
    
    /*
       
ResultSet rs = null;

        DatabaseTransaction dt = null;
        Connection conn1 = null;
        Connection conn2 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;

        try
        {
            System.out.println("usergroup: ");
            dt = DatabaseTransaction.getInstance();
            String sql1 = "select count(containerid)  from containerheader";
            //String sql2 = "select * from userprofile where username='htaycher'";
               String url1 = "jdbc:oracle:thin:@kotel:1532:flex";
             String username1 = "flex_production";
             String password1 = "3monkeys";
            conn1 = dt.requestConnection( );

            ps1 = conn1.prepareStatement(sql1);
           rs = DatabaseTransaction.executeQuery(ps1);
            while(rs.next())
                System.out.println("usergroup: " + rs.getInt(1));




              username1 = "flex_test";
              password1 = "flex";
         //  dt = DatabaseTransaction.getInstance(url1,  username1,  password1);
            conn2 = dt.requestConnection( );

            ps1 = conn2.prepareStatement(sql1);
          // rs = DatabaseTransaction.executeQuery(ps1);
            while(rs.next())
                System.out.println("usergroup: " + rs.getInt(1));

        } catch(BecDatabaseException fde)
        {
            fde.printStackTrace();
        }
        catch(SQLException sqlE)
        {
            sqlE.printStackTrace();
        }
        finally
        {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(ps2);
            DatabaseTransaction.closeConnection(conn1);
            DatabaseTransaction.closeStatement(ps1);
            DatabaseTransaction.closeConnection(conn2);

        }


*/
        System.out.println("End of Main");
        System.exit(0);
    } // end main()


} // end class DatabaseTransaction
