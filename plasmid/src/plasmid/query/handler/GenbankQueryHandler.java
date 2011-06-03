/*
 * GenbankQueryHandler.java
 *
 * Created on April 15, 2005, 2:09 PM
 */

package plasmid.query.handler;

import java.util.*;

import plasmid.coreobject.*;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class GenbankQueryHandler extends GeneQueryHandler {
    
    /** Creates a new instance of GenbankQueryHandler */
    public GenbankQueryHandler() {
    }
    
    public GenbankQueryHandler(List terms) {
        super(terms);
    }
    /**
    public void doQuery(List restrictions, List clonetypes, String species) throws Exception {
        doQuery(restrictions,clonetypes,species,-1,-1, null);
    }
        
    public void doQuery() throws Exception {
        doQuery(null, null, null);
    }  
    */
    public Set doQuery(List restrictions, List clonetypes, String species, int start, int end) throws Exception {
        String sql = "select distinct cloneid from clonegenbank where upper(accession) = upper(?)"+
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
    
    public static void main(String args[]) {
        List terms = new ArrayList();
        terms.add("X73608 ");
        terms.add("XM_094142");
        terms.add("XM_095725.4");
        terms.add("BT007249");
        terms.add("BC006571");
        terms.add("BC000665");
        
        GeneQueryHandler handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.GENBANK, terms);
        try {
            Set cloneids = handler.doQuery(null,null,null,-1,-1);
            handler.restoreClones("cloneid", true, cloneids);
            Map found = handler.getFound();
            Set keys = found.keySet();
            Iterator iter = keys.iterator();
            while(iter.hasNext()) {
                String term = (String)iter.next();
                System.out.println("===============================================");
                System.out.println("Search term: "+term);
                
                List clones = (ArrayList)found.get(term);
                for(int i=0; i<clones.size(); i++) {
                    Clone c = (Clone)clones.get(i);
                    System.out.println("Clone ID: "+c.getCloneid());
                    System.out.println("Clone name: "+c.getName());
                    System.out.println("Clone type: "+c.getType());
                    System.out.println("Vector id: "+c.getVectorid());
                    System.out.println("Vector name: "+c.getVectorname());
                    System.out.println("Is verified"+c.getVerified());
                    System.out.println("Verification method: "+c.getVermethod());
                    
                    List inserts = c.getInserts();
                    for(int j=0; j<inserts.size(); j++) {
                        DnaInsert insert = (DnaInsert)inserts.get(j);
                        System.out.println("Insert description: "+insert.getDescription());
                        System.out.println("Insert format: "+insert.getFormat());
                        System.out.println("Gene id: "+insert.getGeneid());
                        System.out.println("Insert ID: "+insert.getInsertid());
                        System.out.println("Insert name: "+insert.getName());
                        System.out.println("Insert order: "+insert.getOrder());
                        System.out.println("Insert size: "+insert.getSize());
                        System.out.println("Insert source: "+insert.getSource());
                        System.out.println("Insert species: "+insert.getSpecies());
                    }
                    
                    List selections = c.getSelections();
                    for(int k=0; k<selections.size(); k++) {
                        CloneSelection selection = (CloneSelection)selections.get(k);
                        System.out.println("Selection host: "+selection.getHosttype());
                        System.out.println("Selection marker: "+selection.getMarker());
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.exit(0);
    }     
    
    public void doQuery(List restrictions, List clonetypes, String species, int start, int end, String column, String status, boolean isGrowth) throws Exception {
    }
    
}
