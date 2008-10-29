/*
 * GrowthConditionImporter.java
 *
 * Created on April 14, 2005, 11:50 AM
 */

package plasmid.importexport;

import plasmid.coreobject.*;
import plasmid.coreobject.VectorSynonym;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public class GrowthConditionImporter {
    private Map idmap;
    private GrowthConditionManager manager;
    
    /** Creates a new instance of GrowthConditionImporter */
    public GrowthConditionImporter(Connection conn) {
        manager = new GrowthConditionManager(conn);
    }
    
    public Map getIdmap() {return idmap;}
    
    public void importGrowthCondition(ImportTable table) throws Exception {
        idmap = new HashMap();
        
        List conditions = new ArrayList();
        List synonyms = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            GrowthCondition g = new GrowthCondition();
            int id = DefTableManager.getNextid("growthid");
            g.setGrowthid(id);
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("name".equalsIgnoreCase(columnName)) {
                    idmap.put(columnInfo, new Integer(id));
                    g.setName(columnInfo);
                }
                if("hosttype".equalsIgnoreCase(columnName)) {
                    g.setHosttype(columnInfo);
                }
                if("antibioticselection".equalsIgnoreCase(columnName)) {
                    g.setSelection(columnInfo);
                }
                if("growthcondition".equalsIgnoreCase(columnName)) {
                    g.setCondition(columnInfo);
                }
                if("comments".equalsIgnoreCase(columnName)) {
                    g.setComments(columnInfo);
                }
            }
            conditions.add(g);
        }
        
        if(!manager.insertGrowthConditions(conditions)) {
            throw new Exception("Error occured while inserting into GROWTHCONDITION table.");
        }
    }    
}
