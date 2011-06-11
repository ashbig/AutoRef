/*
 * GeneTextQueryMatchHandler.java
 *
 * Created on March 13, 2006, 3:05 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class GeneTextQueryMatchHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GeneTextQueryMatchHandler */
    public GeneTextQueryMatchHandler() {
    }    
    
    public GeneTextQueryMatchHandler(List terms) {
        super(terms);
    } 
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select cloneid from clone where cloneid in ("+
        "select distinct cloneid from cloneinsert"+
        " where insertid in (select insertid from dnainsert where upper(geneid) = upper(?)"+
        " or upper(name) = upper(?))"+
        " union (select distinct cloneid from clonesymbol where upper(symbol) = upper(?))"+
        " ) and status='"+Clone.AVAILABLE+"'";
        
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
        
        return executeQuery(sql, start, end, 3, false);
    } 
        
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        return null;
    }       
}
