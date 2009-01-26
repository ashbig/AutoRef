/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.sql.Connection;

/**
 *
 * @author DZuo
 */
public class SlideDAO {
    private Connection conn;

    public SlideDAO() {}
    
    public SlideDAO(Connection conn) {
        this.conn = conn;
    }
    
    public void addSlideResult() throws DaoException {
        
    }
    
    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
