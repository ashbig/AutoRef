/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import database.DatabaseTransaction;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DZuo
 */
public class VariableDAO {
    public static List<String> getVariableTypes() {
        String sql = "select vartype from vartype";
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<String> types = new ArrayList<String>();
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String type = rs.getString(1);
                types.add(type);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return types;
    }
}
