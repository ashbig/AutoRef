/*
 * GiQueryHandler.java
 *
 * Created on May 3, 2005, 2:31 PM
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
public class GiQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GiQueryHandler */
    public GiQueryHandler() {
    }
    
    public GiQueryHandler(List terms) {
        super(terms);
    } 
    
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {        
        doQuery(restrictions,clonetypes,species,-1,-1, null);
    }
        
    public void doQuery() throws Exception {
        doQuery(null, null, null);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column) throws Exception {
        String sql = "select distinct cloneid from clonegi where gi = ?";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column); 
    }     
}
