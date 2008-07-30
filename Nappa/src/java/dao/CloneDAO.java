/*
 * CloneDAO.java
 *
 * Created on July 16, 2007, 3:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package dao;

import database.DatabaseTransaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import transfer.CloneTO;

/**
 *
 * @author dzuo
 */
public class CloneDAO extends ReagentDAO {
    
    /** Creates a new instance of CloneDAO */
    public CloneDAO() {
    }
    
    public CloneDAO(Connection c) {
        super(c);
    }
    
    public void addSpecificReagents(List clones) throws DaoException {
        String sql = "insert into clone (cloneid,vectorname,growthname,srccloneid,source,genbank,gi,geneid,symbol)"+
                " values(?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            for(int i=0; i<clones.size(); i++) {
                CloneTO c = (CloneTO)clones.get(i);
                stmt.setInt(1, c.getReagentid());
                stmt.setString(2, c.getVectorname().getName());
                stmt.setString(3, c.getGrowthname().getName());
                stmt.setString(4,c.getSrccloneid());
                stmt.setString(5, c.getSource());
                stmt.setString(6, c.getGenbank());
                stmt.setString(7, c.getGi());
                stmt.setString(8, c.getGeneid());
                stmt.setString(9, c.getSymbol());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            throw new DaoException("Error occured while inserting into clone table."+ex.getMessage());
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
}
