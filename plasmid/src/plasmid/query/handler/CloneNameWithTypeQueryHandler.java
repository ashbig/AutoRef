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
public class CloneNameWithTypeQueryHandler extends GeneQueryHandler {
    private String nametype;
    
    /** Creates a new instance of CloneNameQueryHandler */
    public CloneNameWithTypeQueryHandler() {
    }
    
    public CloneNameWithTypeQueryHandler(String nametype, List terms) {
        super(terms);
        setNametype(nametype);
    }
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from clonename where upper(nametype) = '"+nametype.toUpperCase()+"' and upper(namevalue) = upper(?)"+
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
        String sql = "select distinct cloneid from clonename"+
                " where upper(nametype) = '"+nametype.toUpperCase()+
                "' and upper(namevalue) = upper(?)"+
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

    public String getNametype() {
        return nametype;
    }

    public void setNametype(String nametype) {
        this.nametype = nametype;
    }
    
}
