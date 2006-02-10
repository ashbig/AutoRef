/*
 * VectorQueryHandler.java
 *
 * Created on February 2, 2006, 9:55 AM
 */

package plasmid.query.handler;

import java.util.*;
import java.sql.*;
import plasmid.util.StringConvertor;
import plasmid.database.DatabaseManager.*;
import plasmid.database.*;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class VectorQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of VectorQueryHandler */
    public VectorQueryHandler() {
    }
    
    /**
     * Query database to get a list of Clone objects.
     *
     * @param properties List of vector properties.
     * @param restrictions List of clone restrictions.
     * @param species Clone species. If it is null, it represents all species.
     * @param status Clone status. If it is null, it represents all status.
     * @return A list of Clone objects. Return null if any exception occurs.
     */
    public List queryClones(List properties, List restrictions, String species, String status) {
        if(properties == null)
            return null;
        
        String s = StringConvertor.convertFromListToSqlString(properties);
        String sql = "select * from clone where vectorid in"+
        " (select vectorid from vectorproperty where propertytype in ("+s+"))";
        
        if(restrictions != null && restrictions.size()>0) {
            String res = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql+" and restriction in ("+res+")";
        }
        
        if(species != null) {
            sql = sql+" and domain='"+species+"'";
        }
        
        if(status != null) {
            sql = sql+" and status='"+status+"'";
        }
        
        return executeQueryClones(sql);
    }
     
    /**
     * Query database to get a list of Clone IDs.
     *
     * @param vectors List of vector names.
     * @param restrictions List of clone restrictions.
     * @param species Clone species. If it is null, it represents all species.
     * @param status Clone status. If it is null, it represents all status.
     * @return A list of Clone IDs as String. Return null if any exception occurs.
     */
    
    public List queryCloneids(List vectors, List restrictions, String species, String status) {
        if(vectors == null)
            return null;
        
        String s = StringConvertor.convertFromListToSqlString(vectors);
        String sql = "select cloneid from clone where vectorname in ("+s+"))";
        
        if(restrictions != null && restrictions.size()>0) {
            String res = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql+" and restriction in ("+res+")";
        }
        
        if(species != null) {
            sql = sql+" and domain='"+species+"'";
        }
        
        if(status != null) {
            sql = sql+" and status='"+status+"'";
        }
        
        return executeQueryClones(sql);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {
    }
    
    public void doQuery() throws Exception {
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column) throws Exception {
    }
}
