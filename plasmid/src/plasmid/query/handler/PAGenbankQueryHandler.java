/*
 * PAGenbankQueryHandler.java
 *
 * Created on May 9, 2005, 2:45 PM
 */

package plasmid.query.handler;

import java.util.List;

/**
 *
 * @author  DZuo
 */
public class PAGenbankQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of PAGenbankQueryHandler */
    public PAGenbankQueryHandler() {
    }
    
    public PAGenbankQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null);
    }
        
    public void doQuery() throws Exception {
        doQuery(null, null, null);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column) throws Exception {
        String sql = "select distinct cloneid from dnainsert where upper(targetgenbank) = upper(?)";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column);
    }
}
