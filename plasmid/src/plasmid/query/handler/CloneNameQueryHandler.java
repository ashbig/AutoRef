/*
 * CloneNameQueryHandler.java
 *
 * Created on February 24, 2006, 11:07 AM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class CloneNameQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of CloneNameQueryHandler */
    public CloneNameQueryHandler() {
    }
    
    public CloneNameQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,start,end,column,status,false);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
        String sql = "select distinct cloneid from clonename where upper(namevalue) = upper(?)";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, false, isGrowth);
    }    
    
}
