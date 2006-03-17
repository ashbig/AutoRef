/*
 * PmidQueryHandler.java
 *
 * Created on March 15, 2006, 1:58 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class PmidQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of PmidQueryHandler */
    public PmidQueryHandler() {
    }
    
    public PmidQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        String sql = "select distinct cloneid from clonepublication"+
        " where publicationid in (select publicationid from publication"+
        " where upper(PMID) = upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, false);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select distinct cloneid from clonepublication"+
        " where publicationid in (select publicationid from publication"+
        " where upper(PMID) = upper(?))"+
        " and cloneid in ("+
        " select cloneid from "+clonetable+")";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, false);
    }
    
}
