/*
 * DirectGenbankQueryHandler.java
 *
 * Created on May 3, 2005, 12:34 PM
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
public class DirectGenbankQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of DirectGenbankQueryHandler */
    public DirectGenbankQueryHandler() {
    }
    
    public DirectGenbankQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null);
    }   
    
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {
        String sql = "select distinct cloneid from dnainsert where upper(targetgenbank) = upper(?)";
        executeQuery(sql,restrictions,clonetypes,species);
    }
}
