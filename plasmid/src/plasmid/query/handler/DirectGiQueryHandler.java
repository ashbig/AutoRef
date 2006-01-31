/*
 * DirectGiQueryHandler.java
 *
 * Created on May 3, 2005, 2:22 PM
 */

package plasmid.query.handler;

import java.util.*;
import java.sql.*;
import javax.sql.*;

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class DirectGiQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of DirectGiQueryHandler */
    public DirectGiQueryHandler() {
    }
    
    public DirectGiQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {
        String sql = "select distinct cloneid from dnainsert where targetseqid = ?";
        executeQuery(sql, restrictions, clonetypes, species);
    }
}
