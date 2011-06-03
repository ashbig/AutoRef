/*
 * CloneNameQueryHandler.java
 *
 * Created on February 24, 2006, 11:07 AM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

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
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from clonename where upper(namevalue) = upper(?)"+
                " and cloneid in (select cloneid from clone where status='"+Clone.AVAILABLE+"'";
        
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
        
        sql = sql+")";
        
        return executeQuery(sql, start, end, 1, false);
    }    
        
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        return null;
    }
}
