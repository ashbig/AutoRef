/*
 * GeneidQueryHandler.java
 *
 * Created on April 29, 2005, 12:46 PM
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
public class GeneidQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GeneidQueryHandler */
    public GeneidQueryHandler() {
    }
    
    public GeneidQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
        
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from clonegene where geneid = ?";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status);
    }        
}
