/*
 * VectorQueryHandler.java
 *
 * Created on February 2, 2006, 9:55 AM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.util.StringConvertor;
import plasmid.coreobject.Clone;

/**
 *
 * @author  DZuo
 */
public class VectorQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of VectorQueryHandler */
    public VectorQueryHandler() {
        super();
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
    public List queryClones(Set vectorids, List restrictions, String species, String status) {
        if(vectorids == null || vectorids.size() == 0)
            return new ArrayList();
        
        List vectoridList = new ArrayList(vectorids);
        String vs = StringConvertor.convertFromListToSqlList(vectoridList);
        String sql = "select * from clone where vectorid in ("+vs+")";
        
        sql = appendSql(sql, restrictions, species, status);
        
        return executeQueryClones(sql);
    }
    
    public List queryClonesByVectornames(Set vectornames, List restrictions, String species, String status) {
        if(vectornames == null || vectornames.size() == 0)
            return new ArrayList();
        
        List vectoridList = new ArrayList(vectornames);
        String vs = StringConvertor.convertFromListToSqlString(vectoridList);
        String sql = "select * from clone where vectorname in ("+vs+")";
                
        sql = appendSql(sql, restrictions, species, status);
       
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
        
        sql = appendSql(sql, restrictions, species, status);
        
        return executeQueryClones(sql);
    }
    
    public void doQuery() throws Exception {
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
    }
    
    private String appendSql(String sql, List restrictions, String species, String status) {
        if(restrictions != null && restrictions.size()>0) {
            String res = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql+" and restriction in ("+res+")";
        }
        
        if(species != null) {
            sql = sql+" and domain in('"+species+"', '"+Clone.NOINSERT+"')";
        }
        
        if(status != null) {
            sql = sql+" and status='"+status+"'";
        }
        
        return sql;
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
    }
    
}
