/*
 * GeneTextQueryHandler.java
 *
 * Created on March 10, 2006, 6:13 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class GeneTextQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GeneTextQueryHandler */
    public GeneTextQueryHandler() {
    }
    
    public GeneTextQueryHandler(List terms) {
        super(terms);
    } 
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select cloneid from clone where cloneid in ("+
        "select distinct cloneid from cloneinsert where insertid in (select insertid from dnainsert where upper(geneid) like upper(?)"+
        " or upper(name) like upper(?) or upper(description) like upper(?))"+
        " union (select distinct cloneid from clonegene where upper(geneid) like upper(?))"+
        " union (select distinct cloneid from clonegenbank where upper(accession) like upper(?))"+
        " union (select distinct cloneid from clonegi where upper(gi) like upper(?))"+
        " union (select distinct cloneid from clonesymbol where upper(symbol) like upper(?))"+
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
        
        return executeQuery(sql, start, end, 7, true);
     }    
        
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        return null;
    }
}
