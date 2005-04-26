/*
 * QueryManager.java
 *
 * Created on February 1, 2005, 2:40 PM
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class QueryManager {
    private String species;
    private String queryType;
    private ArrayList queryList;
    
    /** Creates a new instance of QueryManager */
    public QueryManager() {
    }

    public QueryManager(String species, String queryType, ArrayList queryList) {
        this.species = species;
        this.queryType = queryType;
        this.queryList = queryList;
    }
    
    public String getSpecies() {return species;}
    public String getQueryType() {return queryType;}
    public ArrayList getQueryList() {return queryList;}
    
    public void setSpecies(String species) {this.species = species;}
    public void setQueryType(String queryType) {this.queryType = queryType;}
    public void setQueryList(ArrayList queryList) {this.queryList = queryList;}
    
    public void queryByClone(String species, String queryType, ArrayList queryList) {
        this.species = species;
        this.queryType = queryType;
        this.queryList = queryList;
        queryByClone();
    }
    
    public void queryByClone() {
        if(queryList == null || queryList.size() == 0) {
            return;
        }
        
        String sql = "select c.*, v.*"+
                    " from clone c, vector v"+
                    " where c.vectorid=v.vectorid"+
                    " and c.?=?";
        
        for(int i=0; i<queryList.size(); i++) {
            String queryTerm = (String)queryList.get(i);
        }
    }
    
    public void queryByGene() {
        if(queryList == null || queryList.size() == 0) {
            return;
        }
                
        String sql = "select c.*, v.*"+
                    " from clone c, vector v"+
                    " where c.vectorid=v.vectorid"+
                    " and c.?=?";
        
        for(int i=0; i<queryList.size(); i++) {
            String queryTerm = (String)queryList.get(i);
        }
    }
}
