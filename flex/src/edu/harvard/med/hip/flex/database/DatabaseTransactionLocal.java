/*
 * DatabaseTransactionLocal.java
 *
 * Created on March 6, 2007, 3:09 PM
 */

package edu.harvard.med.hip.flex.database;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import javax.naming.*;
import javax.sql.*;
import sun.jdbc.rowset.*;
/**
 *
 * @author  htaycher
 */


public class DatabaseTransactionLocal
{
 
 
    public static final String ACE_url = "jdbc:oracle:thin:@bighead:1521:bec";//
    public static final String ACE_username = "bec";
    public static final String ACE_password = "bec";
 
    public static final String FLEX_url = "jdbc:oracle:thin:@kotel:1532:flex";
    public static final String FLEX_username = "flex_production";
    public static final String FLEX_password = "3monkeys";


    private  String url = FLEX_url;
    private   String username = FLEX_username;
    private   String password = FLEX_password;
    // singleton instance.
    private static DatabaseTransactionLocal instance = null;

    // the datasource to get the pooled connections from
    private static DataSource ds = null;

    // Private constructor method. Autocommit is set to false.
    protected DatabaseTransactionLocal(DataSource ds)
    {
        this.ds = ds;
    } // end constructor

    public DatabaseTransactionLocal()
    {
    }
    public DatabaseTransactionLocal(String url, String username, String password)
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static void init(DataSource ds)
    {
        instance = new DatabaseTransactionLocal(ds);
    }

    public static DatabaseTransactionLocal getInstance()
    throws FlexDatabaseException
    {
        if (instance == null)
        {
            return new DatabaseTransactionLocal();
        }

        return instance;

    } // end getInstance()

     public static DatabaseTransactionLocal getInstance(String url, String username, String password)
    throws FlexDatabaseException
    {
        if (instance == null)
        {
            return new DatabaseTransactionLocal( url,  username,  password);
        }

        return instance;

    } // end getInstance()




    public Connection requestConnection(boolean autocommit) throws FlexDatabaseException
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
            if ( connection != null) DatabaseTransactionLocal.closeConnection(connection);
            throw new FlexDatabaseException("Cannot get Connection.\n" + e.getMessage());
        }
    }


    public Connection requestConnection() throws FlexDatabaseException
    {

        return requestConnection(false);

    } // end requestConnection()

   public static int executeUpdate(PreparedStatement ps)
    throws FlexDatabaseException
    {
        try
        {
            return ps.executeUpdate();
        } catch(SQLException sqlE)
        {
            throw new FlexDatabaseException(sqlE.getMessage()+"\nSQL: "+ps);
        }
    } // end executeUpdate()


    public static int executeUpdate(String sql, Connection conn)
    throws FlexDatabaseException
    {
        int retVal = 0;
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            retVal = stmt.executeUpdate(sql);
        } catch (Exception e)
        {
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally
        {
            closeStatement(stmt);
        }
        return retVal;

    } // end executeUpdate()

    public CachedRowSet executeQuery(String sql) throws FlexDatabaseException
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
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+sql);
        } finally
        {
            // release database resources
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(conn);
        }
        return crs;
    } //end executeSQL

    public static CachedRowSet executeQuery(String sql, Connection conn) throws FlexDatabaseException
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
	            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+sql);
	        } finally
	        {
	            // release database resources
	            closeResultSet(rs);
	            closeStatement(stmt);
	           // closeConnection(conn);
	        }
	        return crs;
	    } //end executeSQL


    public static CachedRowSet executeQuery(PreparedStatement stmt)
    throws FlexDatabaseException
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
            throw new FlexDatabaseException(e.getMessage()+"\nSQL: "+stmt);
        } finally
        {
            closeResultSet(results);
        }
        return crs;
    } // end executeQuery()


    public static void setupPreparedStatement(PreparedStatement stmt,
    Hashtable h) throws FlexDatabaseException
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
                throw new FlexDatabaseException("No such parameter defined.");
            }
        } catch (SQLException e)
        {
            throw new FlexDatabaseException(e.getMessage());
        }
    }

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


    public static void closeStatement(Statement stmt)
    {
        try
        {
            stmt.close();
        } catch(Throwable t)
        {}
    }

    public static void closeResultSet(ResultSet rs)
    {
        try
        {
            rs.close();
        } catch(Throwable t)
        {}
    }

    public static void commit(Connection conn)
    {
        try
        {
            conn.commit();
        } catch(Throwable t)
        {}
    }

    public static void rollback(Connection conn)
    {
        try
        {
            conn.rollback();
        } catch(Throwable t)
        {}
    }

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

    public static void main(String args[]) 
    {


    

ResultSet rs = null;

        DatabaseTransactionLocal dt = null;
        Connection conn= null;
        PreparedStatement ps= null;

        try
        {
            System.out.println("usergroup: ");
            dt = DatabaseTransactionLocal.getInstance();
            String sql = "select count(containerid)  from containerheader";
            //String sql2 = "select * from userprofile where username='htaycher'";
             conn = dt.requestConnection( );

            ps = conn.prepareStatement(sql);
           rs = DatabaseTransactionLocal.executeQuery(ps);
            while(rs.next())
                System.out.println("usergroup: " + rs.getInt(1));



      
        } catch(FlexDatabaseException fde)
        {
            fde.printStackTrace();
        }
        catch(SQLException sqlE)
        {
            sqlE.printStackTrace();
        }
        finally
        {
            DatabaseTransactionLocal.closeResultSet(rs);
            if ( conn != null) DatabaseTransactionLocal.closeConnection(conn);
           
        }



        System.out.println("End of Main");
        System.exit(0);
    }
}
  

    

