/*
 * GeneQueryHandler.java
 *
 * Created on April 15, 2005, 2:10 PM
 */

package plasmid.query.handler;

import java.util.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public abstract class GeneQueryHandler {
    protected Map found;
    protected List nofound;
    protected List terms;
    
    /** Creates a new instance of GeneQueryHandler */
    public GeneQueryHandler() {
    }
    
    public GeneQueryHandler(List terms) {
        this.terms = terms;
        this.found = new HashMap();
        this.nofound = new ArrayList();
    }
    
    public Map getFound() {return found;}
    public List getNofound() {return nofound;}
    public List getTerms() {return terms;}

    public abstract void doQuery() throws Exception;
}
