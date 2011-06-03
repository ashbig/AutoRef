/*
 * VectorNameMatchQueryHandler.java
 *
 * Created on March 13, 2006, 3:51 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

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
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from clone"+
        " where (vectorid in (select vectorid from vector"+
        " where upper(name) = upper(?)"+
        " or upper(description) = upper(?)"+
        " or upper(comments) = upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) = upper(?)))"+
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
        
        return executeQuery(sql, start, end, 4, false);
    }  
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        String sql = "select distinct cloneid from "+clonetable+
        " where (vectorid in (select vectorid from vector"+
        " where upper(name) = upper(?)"+
        " or upper(description) = upper(?)"+
        " or upper(comments) = upper(?))"+
        " or vectorid in (select vectorid from vectorsynonym"+
        " where upper(vsynonym) = upper(?)))"+
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
        
        return executeQuery(sql, start, end, 4, false);
    } 
}
