/*
 * GenbankQueryHandler.java
 *
 * Created on April 15, 2005, 2:09 PM
 */

package plasmid.query.handler;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class GenbankQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GenbankQueryHandler */
    public GenbankQueryHandler() {
    }
    
    public GenbankQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        if(terms == null || terms.size() == 0)
            return;
        
        String sql = "select distinct cloneid from refseqgenbank where accession = ?";
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = null;
        for(int i=0; i<terms.size(); i++) {
            String term = (String)terms.get(i);
            stmt.setString(1, term);
            rs = DatabaseTransaction.executeQuery(stmt);
            List clones = new ArrayList();
            while(rs.next()) {
                int cloneid = rs.getInt(1);
                clones.add(new Integer(cloneid));
            }
            if(clones.size() > 0)
                found.put(term, clones);
            else
                nofound.add(term);
        }
        DatabaseTransaction.closeResultSet(rs);
        DatabaseTransaction.closeStatement(stmt);
        DatabaseTransaction.closeConnection(conn);
    }
    
}
