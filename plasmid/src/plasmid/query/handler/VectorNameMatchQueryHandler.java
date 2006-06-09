/*
 * VectorNameMatchQueryHandler.java
 *
 * Created on March 13, 2006, 3:51 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class VectorNameMatchQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of VectorNameMatchQueryHandler */
    public VectorNameMatchQueryHandler() {
    }
    
    public VectorNameMatchQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);    
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from clone"+
        " where vectorid in (select vectorid from vector"+
        " where upper(name) = upper(?)"+
        " or upper(description) = upper(?)"+
        " or upper(comments) = upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) = upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 4, false);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select distinct cloneid from "+clonetable+
        " where vectorid in (select vectorid from vector"+
        " where upper(name) = upper(?)"+
        " or upper(description) = upper(?)"+
        " or upper(comments) = upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) = upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 4, false);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
    }
    
}
