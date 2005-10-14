/*
 * PAGenbankQueryHandler.java
 *
 * Created on May 9, 2005, 2:45 PM
 */

package plasmid.query.handler;

import java.util.List;

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
    
    public void doQuery() throws Exception {
        String sql = "select distinct cloneid from dnainsert where upper(targetgenbank) = upper(?)";
        executeQuery(sql);
    }
    
}
