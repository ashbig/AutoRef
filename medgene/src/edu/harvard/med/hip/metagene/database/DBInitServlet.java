/*
 * DBInitServlet.java
 *
 * Created on April 23, 2002, 2:26 PM
 */

package edu.harvard.med.hip.metagene.database;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import java.sql.SQLException;
import java.sql.Connection;
import javax.sql.DataSource;

import org.apache.struts.util.GenericDataSource;

/**
 *
 * @author  hweng
 */
public class DBInitServlet extends HttpServlet {
    
    /** Creates a new instance of DBInitServlet */
    public DBInitServlet() {
    }
    
    public void init(ServletConfig config) throws ServletException{
        super.init(config);
        try{
            GenericDataSource ds = new GenericDataSource();
            ds.setDriverClass(getInitParameter("driverClass"));
            ds.setUrl(getInitParameter("jdbcURL"));
            ds.setMinCount(Integer.parseInt(getInitParameter("minCount")));
            ds.setMaxCount(Integer.parseInt(getInitParameter("maxCount")));
            ds.setAutoCommit(false);
            ds.open();
            ConnectionPool.init(ds);
        } catch(SQLException e){
            e.printStackTrace();
            throw new ServletException("Unable to open datasource.");
        }
    }
         
    
}
