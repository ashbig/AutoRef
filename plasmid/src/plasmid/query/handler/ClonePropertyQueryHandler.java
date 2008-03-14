/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.query.handler;

import java.util.*;

/**
 *
 * @author DZuo
 */
public class ClonePropertyQueryHandler extends GeneQueryHandler {
    private String propertytype;
    
    /** Creates a new instance of CloneNameQueryHandler */
    public ClonePropertyQueryHandler() {
    }
    
    public ClonePropertyQueryHandler(String propertytype, List terms) {
        super(terms);
        setPropertytype(propertytype);
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
        String sql = "select distinct cloneid from cloneproperty where upper(propertytype) = '"+propertytype.toUpperCase()+"' and upper(propertyvalue) = upper(?)";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, false, isGrowth);
    }    
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, String clonetable) throws Exception {
        String sql = "select distinct cloneid from cloneproperty"+
                " where upper(propertytype) = '"+propertytype.toUpperCase()+
                "' and upper(propertyvalue) = upper(?)"+
                " and cloneid in (select cloneid from "+clonetable+")";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column, status, 1, false);
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }
    
}
