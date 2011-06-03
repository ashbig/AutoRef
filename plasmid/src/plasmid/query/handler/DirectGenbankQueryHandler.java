/*
 * DirectGenbankQueryHandler.java
 *
 * Created on May 3, 2005, 12:34 PM
 */

package plasmid.query.handler;

import java.util.*;

import plasmid.coreobject.*;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class DirectGenbankQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of DirectGenbankQueryHandler */
    public DirectGenbankQueryHandler() {
    }
    
    public DirectGenbankQueryHandler(List terms) {
        super(terms);
    }
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sq = "select distinct cloneid from cloneinsert where insertid in"+
                " (select insertid from dnainsert where upper(targetgenbank) = upper(?))"+
                " and cloneid in (select cloneid from clone where status='"+Clone.AVAILABLE+"'";
        
         if (clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sq = sq + " and clonetype in (" + s + ")";
        }
        
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sq = sq + " and restriction in (" + s + ")";
        }

        if (species != null) {
            sq = sq + " and domain='" + species + "'";
        }
        
        sq = sq+")";
        
        return executeQuery(sq, start, end, 1, false);
    }      
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        return null;
    }
}
