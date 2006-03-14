/*
 * GeneTextQueryMatchHandler.java
 *
 * Created on March 13, 2006, 3:05 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class GeneTextQueryMatchHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GeneTextQueryMatchHandler */
    public GeneTextQueryMatchHandler() {
    }    
    
    public GeneTextQueryMatchHandler(List terms) {
        super(terms);
    } 
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from dnainsert where upper(geneid) = upper(?)"+
        " or upper(name) = upper(?) or upper(description) = upper(?)";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 3, false);
    }    
}
