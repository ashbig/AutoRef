/*
 * DirectGiQueryHandler.java
 *
 * Created on May 3, 2005, 2:22 PM
 */

package plasmid.query.handler;

import java.util.*;

import plasmid.coreobject.*;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class DirectGiQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of DirectGiQueryHandler */
    public DirectGiQueryHandler() {
    }
    
    public DirectGiQueryHandler(List terms) {
        super(terms);
    } 
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from cloneinsert where insertid in (select insertid from dnainsert where targetseqid = ?)"+
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
