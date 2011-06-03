/*
 * AuthorNameMatchQueryHandler.java
 *
 * Created on March 15, 2006, 1:58 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class AuthorNameMatchQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of AuthorNameMatchQueryHandler */
    public AuthorNameMatchQueryHandler() {
    }
    
    public AuthorNameMatchQueryHandler(List terms) {
        super(terms);
    }
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from cloneauthor"+
        " where authorid in (select authorid from authorinfo"+
        " where upper(authorname) = upper(?))"+
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
        String sql = "select distinct cloneid from cloneauthor"+
        " where authorid in (select authorid from authorinfo"+
        " where upper(authorname) = upper(?))"+
        " and cloneid in ("+
        " select cloneid from "+clonetable+
        " where status='"+Clone.AVAILABLE+"'";
        
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
}
