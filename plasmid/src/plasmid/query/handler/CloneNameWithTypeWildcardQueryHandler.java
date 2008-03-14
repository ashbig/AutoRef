/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.query.handler;

import java.util.List;

/**
 *
 * @author DZuo
 */
public class CloneNameWithTypeWildcardQueryHandler extends GeneQueryHandler {
    private String nametype;
    
    /** Creates a new instance of CloneNameQueryHandler */
    public CloneNameWithTypeWildcardQueryHandler() {
    }
    
    public CloneNameWithTypeWildcardQueryHandler(String nametype, List terms) {
        super(terms);
        setNametype(nametype);
    }
    
    public void doQuery() throws Exception {
        doQuery(null, null, null, null);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null, status);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status) throws Exception {
        doQuery(restrictions,clonetypes,species,start,end,column,status,false);
    }
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
        String sql = "select distinct cloneid from clonename where upper(nametype) = '"+nametype.toUpperCase()+"'";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 0, false, isGrowth);
    }    
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select distinct cloneid from clonename"+
                " where upper(nametype) = '"+nametype.toUpperCase()+"'"+
                " and cloneid in (select cloneid from "+clonetable+")";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 0, false);
    }

    public String getNametype() {
        return nametype;
    }

    public void setNametype(String nametype) {
        this.nametype = nametype;
    }
    
}
