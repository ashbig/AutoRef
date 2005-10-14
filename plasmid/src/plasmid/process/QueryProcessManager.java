/*
 * QueryProcessManager.java
 *
 * Created on October 13, 2005, 3:12 PM
 */

package plasmid.process;

import java.util.*;
import java.sql.*;
import java.io.*;

import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.util.*;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;

/**
 *
 * @author  DZuo
 */
public class QueryProcessManager {
    
    /** Creates a new instance of QueryProcessManager */
    public QueryProcessManager() {
    }
    
    public List getAllEmptyVectors(List clonetypes, List restrictions) {
        DatabaseTransaction t = null;
        Connection conn = null;
        List found = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            List cloneids = manager.queryCloneidsByCloneType(clonetypes);
            Map clones = manager.queryAvailableClonesByCloneid(cloneids, false, true, false, restrictions, clonetypes);
            Set ks = clones.keySet();
            Iterator iter = ks.iterator();
            while(iter.hasNext()) {
                String k = (String)iter.next();
                CloneInfo c = (CloneInfo)clones.get(k);
                if(c != null) {
                    found.add(c);
                }
            }
            return found;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
