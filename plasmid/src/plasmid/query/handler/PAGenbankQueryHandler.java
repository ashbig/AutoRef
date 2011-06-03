/*
 * PAGenbankQueryHandler.java
 *
 * Created on May 9, 2005, 2:45 PM
 */

package plasmid.query.handler;

import java.util.List;
import java.util.Set;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class PAGenbankQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of PAGenbankQueryHandler */
    public PAGenbankQueryHandler() {
    }
    
    public PAGenbankQueryHandler(List terms) {
        super(terms);
    }
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from cloneinsert where insertid in (select insertid from dnainsert where upper(targetgenbank) = upper(?))"+
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
