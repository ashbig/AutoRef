/*
 * CloneStorageSet.java
 *
 * Created on June 11, 2003, 3:03 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;
import java.sql.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  dzuo
 */
public class CloneStorageSet {
    protected ArrayList allStorages = null;
    
    /** Creates a new instance of CloneStorageSet */
    public CloneStorageSet() {
        allStorages = new ArrayList();
    }
    
    public void add(CloneStorage c) {
        allStorages.add(c);
    }
    
    public void insert(Connection conn) throws FlexDatabaseException, SQLException {
        String sql = "insert into clonestorage (storageid, storagesampleid, storagetype, storageform, cloneid)"+
                    " values(storageid.nextval,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        for(int i=0; i<allStorages.size(); i++) {
            CloneStorage c = (CloneStorage)allStorages.get(i);
            stmt.setInt(1, c.getSampleid());
            stmt.setString(2, c.getStorageType());
            stmt.setString(3, c.getStorageForm());
            stmt.setInt(4, c.getCloneid());
            DatabaseTransaction.executeUpdate(stmt);
        }
    }
}
