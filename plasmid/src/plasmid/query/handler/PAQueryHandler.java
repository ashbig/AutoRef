/*
 * PAQueryHandler.java
 *
 * Created on May 9, 2005, 12:31 PM
 */

package plasmid.query.handler;

import java.util.List;

/**
 *
 * @author  DZuo
 */
public class PAQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of PAQueryHandler */
    public PAQueryHandler() {
    }
    
    public PAQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        String sql = "select distinct cloneid from dnainsert where upper(geneid) = upper(?)";
        executeQuery(sql);
    }
    
}
