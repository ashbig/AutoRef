/*
 * RefseqNameQueryHandler.java
 *
 * Created on November 7, 2005, 3:13 PM
 */

package plasmid.query.handler;

import java.util.*;
import plasmid.coreobject.Clone;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class RefseqNameQueryHandler extends GeneQueryHandler {
    String nametype = null;
    
    public void setNametype(String s) {this.nametype = s;}
    public String getNametype() {return nametype;}
    
    /** Creates a new instance of RefseqNameQueryHandler */
    public RefseqNameQueryHandler() {
    }
     
    public RefseqNameQueryHandler(List terms) {
        super(terms);
    }
    
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from cloneinsert "+
        " where insertid in (select insertid from dnainsert where refseqid in"+
        " (select refid from refseqname where upper(nametype)=upper('"+nametype+"')"+
        " and upper(namevalue) = upper(?)))"+
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
        return null;
    }
}
