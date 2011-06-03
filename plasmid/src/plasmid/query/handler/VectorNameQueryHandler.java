/*
 * VectorNameQueryHandler.java
 *
 * Created on March 13, 2006, 3:37 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

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
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from clone"+
        " where (vectorid in (select vectorid from vector"+
        " where upper(name) like upper(?)"+
        " or upper(description) like upper(?)"+
        " or upper(comments) like upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) like upper(?)))"+
        " and status='"+Clone.AVAILABLE+"'";
        
         if (clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql + " and clonetype in (" + s + ")";
        }
        
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql + " and restriction in (" + s + ")";
        }

        if (species != null) {
            sql = sql + " and domain='" + species + "'";
        }
        
        return executeQuery(sql, start, end, 4, true);
    }
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        String sql = "select distinct cloneid from "+clonetable+
        " where (vectorid in (select vectorid from vector"+
        " where upper(name) like upper(?)"+
        " or upper(description) like upper(?)"+
        " or upper(comments) like upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) like upper(?)))"+
        " and status='"+Clone.AVAILABLE+"'";
        
         if (clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql + " and clonetype in (" + s + ")";
        }
        
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql + " and restriction in (" + s + ")";
        }

        if (species != null) {
            sql = sql + " and domain='" + species + "'";
        }
        
        return executeQuery(sql, start, end, 4, true);
    }
}
