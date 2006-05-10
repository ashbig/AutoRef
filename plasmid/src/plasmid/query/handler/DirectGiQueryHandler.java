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
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
        
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from cloneinsert where insertid in (select insertid from dnainsert where targetseqid = ?)";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status);
    }  
    
}
