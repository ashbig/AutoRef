/*
 * $Id: DatabaseTransaction.java,v 1.2 2001-04-25 18:37:06 dongmei_zuo Exp $
 *
 * File     : DatabaseTransaction.java 
 * Date     : 04162001
 * Author	: Dongmei Zuo
 *
 * Revision	: 04-23-2001
 *		  Add getConnection() and disconnect() methods. [dzuo]
 */

package flex.ApplicationCode.Java.database;

import java.sql.*;
import java.util.*;

import flex.ApplicationCode.Java.util.*;

/**
 * The DatabaseTransaction class basically servers as a wrapper
 * around the JDBC Connection class.  
 */
public class DatabaseTransaction {
	private Connection connection = null;
    	private String     url        = "jdbc:oracle:oci8:@";    
	private String username = "flex_owner";
	private String pswd = "flex";
//    	private Properties props      = null;

	private static DatabaseTransaction instance = null;

	/**
	 * This class is implemented as a Singleton.
	 * Returns a DatabaseTransaction object.
	 *
	 * @return A DatabaseTransaction object.
	 */
	public static DatabaseTransaction getInstance() throws FlexDatabaseException {
		if (instance == null) {
			instance = new DatabaseTransaction();
		}

		return instance;
	}
	
	/**
	 * Return the Connection object of this transaction.
	 *
	 * @return The Connection object.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Disconnect the current database connection.
	 *
	 * @exception FlexDatabaseException.
	 */
	public void disconnect() throws FlexDatabaseException {
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			throw new FlexDatabaseException(e.getMessage());
		}
	}
	
	/**
	 * Executes a sql statement and returns the result
	 * as a Vector. For update statement, returns null.
	 *
	 * @param sql The sql statement to be executed.
	 *
	 * @return A Vector containing the result.
	 * @exception throws FlexDatabaseException.
	 */	
	public Vector executeSql(String sql) throws FlexDatabaseException {		
		try {
			Statement stmt = connection.createStatement();

			if(stmt.execute(sql)) {
				ResultSet results = stmt.getResultSet();
				Vector v = processResultSet(results);
				stmt.close();				
				return v;
			}
			
			stmt.close();
			return null;
		} catch (SQLException e) {
		 	throw new FlexDatabaseException(e.getMessage());
		}
	}
	
	/**
	 * This method executes the prepared statement. It handles both
	 * update statement and select statement.
	 *
	 * @param sql The sql statement to be executed.
	 * @param v The parameters that the PreparedStatement object need
	 *          to be set to.
	 * @return A Vector will be retured for select statement. Null will
	 *         be retured for update statement or select statement but
	 *	     no results returned from query.
	 * @exception throws FlexDatabaseException.
	 */
	public Vector executePreparedSql(String sql, Vector items) throws FlexDatabaseException{
		Vector resultVector = new Vector();

		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
				
			Enumeration e = items.elements();
			while (e.hasMoreElements()) {
				Vector params = (Vector)e.nextElement();
				Enumeration enum = params.elements();

				while (enum.hasMoreElements()) {
					Hashtable h = (Hashtable)enum.nextElement();
					setParameter(stmt, h);
				}
	
				if(stmt.execute()) {
					try {
						ResultSet results = stmt.getResultSet();
						Vector result = processResultSet(results);
						resultVector.addAll(result);
					} catch (SQLException exception) {
						throw new FlexDatabaseException(exception.getMessage());
					}
				}
			}
			stmt.close();
		} catch (SQLException e) {
			throw new FlexDatabaseException(e.getMessage());
		}

		if (resultVector.isEmpty()) {
			return null;
		} else {
			return resultVector;
		}
	}


	/**
	 * Commits the changes by calling the JDBC commit.
	 * @exception throws FlexDatabaseException.
	 */			
	public void commit() throws FlexDatabaseException {
		try {
			connection.commit();
		} catch (SQLException e) {
			throw new FlexDatabaseException(e.getMessage());
		}
	}

	/**
	 * Abort the changes by calling the JDBC rollback.
	 * @exception throws FlexDatabaseException.
	 */
	public void abort() throws FlexDatabaseException {
		try {
			connection.rollback();
		} catch (SQLException e) {
			throw new FlexDatabaseException(e.getMessage());
		}
	}

	//**********************************************************//
	//							    //
	//		Private Utility Methods		            //
	//					     		    //
	//**********************************************************//

	// Private constructor method. Autocommit is set to false.
	protected DatabaseTransaction() throws FlexDatabaseException {
	    try {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(url, username, pswd);
                connection.setAutoCommit(false);
            }
            catch( SQLException e ) {
                throw new FlexDatabaseException(e.getMessage());
            }
	}

	// Set the parameters for the PreparedStatement object.
	private void setParameter(PreparedStatement stmt, Hashtable h) throws FlexDatabaseException {
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
	
	// Process the ResultSet object to reture the results as a Vector.
	private Vector processResultSet(ResultSet results) throws FlexDatabaseException {
		try {
			ResultSetMetaData meta = results.getMetaData();
			int cols = meta.getColumnCount();
			Vector v = new Vector();
	
			while (results.next()) {
				Hashtable h = new Hashtable(cols);
				int i;
				
				for (i = 1; i<=cols; i++) {
					Object o = results.getObject(i);

					if(o == null) 
						o = "No Value";

					h.put(meta.getColumnLabel(i), o);
				}
				v.addElement(h);
			}
			return v;
		} catch (SQLException e) {
			throw new FlexDatabaseException(e.getMessage());
		} catch (NullPointerException exception) {
			throw new FlexDatabaseException(exception.getMessage());
		}
	}

	public static void main (String args[]) {
		try {
			DatabaseTransaction t = DatabaseTransaction.getInstance();

			Connection c = t.getConnection();
			if (c == null) 
				System.out.println("Error: Connection is null");
			
			//Testing executeSql()
			System.out.println("select from processprotocol:");
			String sql = "select * from processprotocol";
			Vector result = t.executeSql(sql);
			Enumeration enum = result.elements();
			while(enum.hasMoreElements()) {
				Hashtable h = (Hashtable)enum.nextElement();
				Enumeration colEnum = h.keys();
				while(colEnum.hasMoreElements()) {
					Object k = colEnum.nextElement();
					System.out.print(h.get(k)+"\t");
				}
				System.out.println();
			}
			
			//Testing executePreparedSql()
			System.out.println("Insert into processprotocol:");
			sql = "insert into processprotocol "+
				"(protocolid, processcode, processname)\n"+
			    "values(?, ?, ?)";

			Vector v = new Vector();
			Vector params = new Vector();
			Hashtable param1 = ParamHashtable.getParam(1, "int", new Integer(3));
			Hashtable param2 = ParamHashtable.getParam(2, "string", "Create BP Plate");
			Hashtable param3 = ParamHashtable.getParam(3, "string", "create_bp_plate");
			params.addElement(param1);
			params.addElement(param2);
			params.addElement(param3);
			v.addElement(params);

			t.executePreparedSql(sql, v);

			System.out.println("Select from processprotocol:");
			sql = "select * from processprotocol where protocolid=3";
			Vector res = t.executeSql(sql);
			Enumeration enum1 = res.elements();
			while(enum1.hasMoreElements()) {
				Hashtable h = (Hashtable)enum1.nextElement();
				Enumeration colEnum = h.keys();
				while(colEnum.hasMoreElements()) {
					Object k = colEnum.nextElement();
					System.out.print(h.get(k)+"\t");
				}
				System.out.println();
			}		

	      	t.abort();

			System.out.println("Select from processprotocol after abort:");
			sql = "select * from processprotocol";
			Vector res1 = t.executeSql(sql);
			Enumeration enum2 = res1.elements();
			while(enum2.hasMoreElements()) {
				Hashtable h = (Hashtable)enum2.nextElement();
				Enumeration colEnum = h.keys();
				while(colEnum.hasMoreElements()) {
					Object k = colEnum.nextElement();
					System.out.print(h.get(k)+"\t");
				}
				System.out.println();
			}

			t.disconnect();
		} catch (FlexDatabaseException e) {
			System.out.println(e);
		}		
	}
}











