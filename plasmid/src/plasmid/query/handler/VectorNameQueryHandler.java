/*
 * VectorNameQueryHandler.java
 *
 * Created on March 13, 2006, 3:37 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class VectorNameQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of VectorNameQueryHandler */
    public VectorNameQueryHandler() {
    }
    
    public VectorNameQueryHandler(List terms) {
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
        " where upper(name) like upper(?)"+
        " or upper(description) like upper(?)"+
        " or upper(comments) like upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) like upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 4, true);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select distinct cloneid from "+clonetable+
        " where vectorid in (select vectorid from vector"+
        " where upper(name) like upper(?)"+
        " or upper(description) like upper(?)"+
        " or upper(comments) like upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) like upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 4, true);
    }
    
}
