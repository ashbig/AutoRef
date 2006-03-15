/*
 * AuthorNameMatchQueryHandler.java
 *
 * Created on March 15, 2006, 1:58 PM
 */

package plasmid.query.handler;

import java.util.*;

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
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from cloneauthor"+
        " where authorid in (select authorid from authorinfo"+
        " where upper(authorname) = upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, true);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select distinct cloneid from cloneauthor"+
        " where authorid in (select authorid from authorinfo"+
        " where upper(authorname) = upper(?))"+
        " and cloneid in ("+
        " select cloneid from "+clonetable+")";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, true);
    }
    
}
