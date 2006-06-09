/*
 * PlasmidCloneidQueryHandler.java
 *
 * Created on March 9, 2006, 2:05 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class PlasmidCloneidQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of PlasmidCloneidQueryHandler */
    public PlasmidCloneidQueryHandler() {
    }
    
    public PlasmidCloneidQueryHandler(List terms) {
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
        String sql = "select cloneid from clone where upper(clonename) = upper(?)";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, false, isGrowth);
    }   
}
