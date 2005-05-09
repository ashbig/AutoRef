/*
 * PAGIQueryHandler.java
 *
 * Created on May 9, 2005, 2:50 PM
 */

package plasmid.query.handler;

import java.util.List;

/**
 *
 * @author  DZuo
 */
public class PAGIQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of PAGIQueryHandler */
    public PAGIQueryHandler() {
    }
   
    public PAGIQueryHandler(List terms) {
        super(terms);
    }
    
    public void doQuery() throws Exception {
        String sql = "select distinct cloneid from dnainsert where targetseqid = ?";
        executeQuery(sql);
    }     
}
