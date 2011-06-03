/*
 * PlasmidCloneidQueryHandler.java
 *
 * Created on March 9, 2006, 2:05 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

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
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select cloneid from clone where upper(clonename) = upper(?)"+
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
        
        return executeQuery(sql, start, end, 1, false);
    }  
        
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        return null;
    } 
}
