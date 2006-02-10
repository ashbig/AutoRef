/*
 * RefseqNameQueryHandler.java
 *
 * Created on November 7, 2005, 3:13 PM
 */

package plasmid.query.handler;

import java.util.*;

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
    
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null);
    }
        
    public void doQuery() throws Exception {
        doQuery(null, null, null);
    }  
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column) throws Exception {
        String sql = "select distinct cloneid from dnainsert where refseqid in"+
        " (select refid from refseqname where upper(nametype)=upper('"+nametype+"')"+
        " and upper(namevalue) = upper(?))";
        executeQuery(sql, restrictions, clonetypes, species, start, end, column);
    }
}
