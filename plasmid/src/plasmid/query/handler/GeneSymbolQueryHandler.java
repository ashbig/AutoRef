/*
 * GeneSymbolQueryHandler.java
 *
 * Created on April 29, 2005, 12:39 PM
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
public class GeneSymbolQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GeneSymbolQueryHandler */
    public GeneSymbolQueryHandler() {
    }
    
    public GeneSymbolQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        String sql = "select distinct cloneid from clonesymbol where upper(symbol) = upper(?)";
        executeQuery(sql);
    }   
}
