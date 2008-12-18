/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.importexport;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import plasmid.blast.BlastWrapper;
import plasmid.coreobject.Clone;
import plasmid.coreobject.Dnasequence;
import plasmid.database.DatabaseManager.CloneManager;
import plasmid.database.DatabaseTransaction;
import plasmid.query.coreobject.CloneInfo;
import plasmid.util.StringConvertor;

/**
 *
 * @author DZuo
 */
public class PlasmidExporter {
    public Map getClones(List restrictions, String species, String collection) throws Exception {
        String sql = "select cloneid from clone where status="+Clone.AVAILABLE;
               
        if(restrictions != null) {
            String s = StringConvertor.convertFromListToSqlString(restrictions);
            sql += " and restricition in ("+s+")";
        }
        
        if(species != null) {
            sql += " and domain='"+species+"'";
        }
        
        if(collection != null) {
            sql += " and cloneid in (select cloneid from clonecollection where name='"+collection+"')";
        }
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        List cloneids = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                int id = rs.getInt(1);
                cloneids.add(new Integer(id));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception("Cannot get clones.\n"+ex.getMessage());
        } 
        
        CloneManager m = new CloneManager();
        Map clones = m.queryClonesByCloneid(cloneids, true, false, false);
        
        return clones;
    }
    
    public void writeClones(Collection clones, PrintWriter out) {
        Iterator iter = clones.iterator();
        while(iter.hasNext()) {
            CloneInfo clone = (CloneInfo)iter.next();
            out.println(clone.getFastaID());
            out.println(Dnasequence.convertToFasta(clone.getInsertseqs()));
        }
    }
    
    public static final void main(String args[]) {
        String file = BlastWrapper.BLAST_FILE_PATH+"plasmid";
        
        PlasmidExporter p = new PlasmidExporter();
        try {
            System.out.println("Get clones.");
            Map clonemap = p.getClones(null, null, null);
            Collection clones = clonemap.values();
            PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
            p.writeClones(clones, out);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
