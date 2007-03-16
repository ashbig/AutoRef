/*
 * VectorFeatureQueryHandler.java
 *
 * Created on March 15, 2006, 1:57 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class VectorFeatureQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of VectorFeatureQueryHandler */
    public VectorFeatureQueryHandler() {
    }
    
    public VectorFeatureQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select cloneid from clone"+
        " where vectorid in (select vectorid from vectorfeature"+
        " where upper(name) like upper(?)"+
        " or upper(description) like upper(?)"+
        " or upper(maptype) like upper(?))"+
        " or vectorid in (select vectorid from vectorproperty"+
        " where upper(propertytype) like upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 4, true);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select cloneid from "+clonetable+
        " where vectorid in (select vectorid from vectorfeature"+
        " where upper(name) like upper(?)"+
        " or upper(description) like upper(?)"+
        " or upper(maptype) like upper(?))"+
        " or vectorid in (select vectorid from vectorproperty"+
        " where upper(propertytype) like upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 4, true);
    }
    
}
