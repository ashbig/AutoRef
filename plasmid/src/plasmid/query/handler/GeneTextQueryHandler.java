/*
 * GeneTextQueryHandler.java
 *
 * Created on March 10, 2006, 6:13 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class GeneTextQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GeneTextQueryHandler */
    public GeneTextQueryHandler() {
    }
    
    public GeneTextQueryHandler(List terms) {
        super(terms);
    } 
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from cloneinsert where insertid in (select insertid from dnainsert where upper(geneid) like upper(?)"+
        " or upper(name) like upper(?) or upper(description) like upper(?))"+
        " union (select distinct cloneid from clonegene where upper(geneid) like upper(?))"+
        " union (select distinct cloneid from clonegenbank where upper(accession) like upper(?))"+
        " union (select distinct cloneid from clonegi where upper(gi) like upper(?))"+
        " union (select distinct cloneid from clonesymbol where upper(symbol) like upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 7, true);
     }    
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
    }
    
}
