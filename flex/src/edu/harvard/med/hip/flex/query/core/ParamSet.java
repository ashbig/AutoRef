/*
 * ParamSet.java
 *
 * Created on November 4, 2003, 9:59 AM
 */

package edu.harvard.med.hip.flex.query.core;

import java.util.*;
import java.sql.*;

import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  DZuo
 */
public class ParamSet {
    List params;
    
    /** Creates a new instance of ParamSet */
    public ParamSet(List params) {
        this.params = params;
    }
    
    public void persist(Connection conn, int searchid) throws FlexDatabaseException, SQLException {
        String sql = "insert into param (paramname, paramvalue, searchid)"+
        " values (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        for(int i=0; i<params.size(); i++) {
            Param param = (Param)params.get(i);
            stmt.setString(1, param.getName());
            stmt.setString(2, param.getValue());
            stmt.setInt(3, searchid);
            DatabaseTransaction.executeUpdate(stmt);
        }
        DatabaseTransaction.closeStatement(stmt);
    }
    
    public static void main(String args[]) {
        List params = new ArrayList();
        params.add(new Param(Param.BLASTLENGTH, "100"));
        params.add(new Param(Param.BLASTPID, "70%"));
        ParamSet pset = new ParamSet(params);
        SearchRecord sr = new SearchRecord("Test1", SearchRecord.GI, SearchRecord.INPROCESS, "dzuo");
                
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            sr.setSearchid(1);
            sr.persist(conn);
            pset.persist(conn, 1);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeConnection(conn);
            System.exit(0);
        }
    }
}
