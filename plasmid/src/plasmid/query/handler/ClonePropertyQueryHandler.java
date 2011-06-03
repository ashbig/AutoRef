/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

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
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from cloneproperty where upper(propertytype) = '"+propertytype.toUpperCase()+"' and upper(propertyvalue) = upper(?)"+
                " and cloneid in (select cloneid from clone where status='"+Clone.AVAILABLE+"'";
        
         if (clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql + " and clonetype in (" + s + ")";
        }
        
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql + " and restriction in (" + s + ")";
        }

        if (species != null) {
            sql = sql + " and domain='" + species + "'";
        }
        
        sql = sql+")";
        
        return executeQuery(sql, start, end, 1, false);
    }    
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end, String clonetable) throws Exception {
        String sql = "select distinct cloneid from cloneproperty"+
                " where upper(propertytype) = '"+propertytype.toUpperCase()+
                "' and upper(propertyvalue) = upper(?)"+
                " and cloneid in (select cloneid from "+clonetable+
                " where status='"+Clone.AVAILABLE+"'";
        
         if (clonetypes != null) {
            String s = StringConvertor.convertFromListToSqlString(clonetypes);
            sql = sql + " and clonetype in (" + s + ")";
        }
        
        if (restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql = sql + " and restriction in (" + s + ")";
        }

        if (species != null) {
            sql = sql + " and domain='" + species + "'";
        }
        
        sql = sql+")";
        
        return executeQuery(sql, start, end, 1, false);
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }
    
}
