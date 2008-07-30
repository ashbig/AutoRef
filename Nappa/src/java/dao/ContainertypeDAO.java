/*
 * ContainertypeDAO.java
 *
 * Created on February 23, 2007, 3:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dao;

/**
 *
 * @author DZuo
 */
/*
 * ContainertypeDAO.java
 *
 * Created on February 23, 2007, 2:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import database.DatabaseException;
import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.DataModel;
import transfer.ContainertypeTO;

/**
 *
 * @author DZuo
 */
public class ContainertypeDAO {
    private Connection connection;
    private DataModel model;
    
    private int batchSize = 20;
    private int firstItem = 0;
    
    /** Creates a new instance of ContainertypeDAO */
    public ContainertypeDAO() {
    }
    
    public ContainertypeDAO(Connection conn) {
        this.connection = conn;
    }
    
    public List getContainertypes() throws DaoException {
        String sql = "select * from containertype";
        ResultSet rs = null;
        DatabaseTransaction t = null;
        List types = new ArrayList();
        
        try{
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String type = rs.getString(1);
                String desc = rs.getString(2);
                int row = rs.getInt(3);
                int col = rs.getInt(4);
                ContainertypeTO ct = new ContainertypeTO(type,desc,row,col);
                types.add(ct);
            }
        } catch (Exception ex) {
            throw new DaoException(ex.getMessage());
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        
        return types;
    }
}
