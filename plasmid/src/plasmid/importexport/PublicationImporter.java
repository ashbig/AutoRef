/*
 * PublicationImporter.java
 *
 * Created on April 14, 2005, 10:52 AM
 */

package plasmid.importexport;

import plasmid.coreobject.*;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import java.util.*;
import java.sql.*;

/**
 *
 * @author  DZuo
 */
public class PublicationImporter {
    private Map idmap;
    private PublicationManager manager;
    
    /** Creates a new instance of AuthorImporter */
    public PublicationImporter(Connection conn) {
        manager = new PublicationManager(conn);
    }
    
    public Map getIdmap() {return idmap;}
    
    public void importPublication(ImportTable table) throws Exception {
        idmap = new HashMap();
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("publication", "publicationid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get publicationid from publication table.");
        }
        
        List publications = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            Publication p = new Publication();
            p.setPublicationid(id);
            List row = (List)contents.get(n);
            boolean isnew = true;
            
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("publicationid".equalsIgnoreCase(columnName)) {
                    int pubid = 0;
                    
                    try {
                        PublicationManager man = new PublicationManager(manager.getConnection());
                        pubid = man.getPublicationid(columnInfo);
                    } catch (Exception ex) {
                        throw new Exception("Error occured while trying to find matching publication id with: "+columnInfo+" from database.");
                    }
                    
                    if(pubid > 0) {
                        idmap.put(columnInfo, new Integer(pubid));
                        isnew = false;
                    } else {
                        idmap.put(columnInfo, new Integer(id));
                    }
                }
                if("title".equalsIgnoreCase(columnName))
                    p.setTitle(columnInfo);
                if("pmid".equalsIgnoreCase(columnName))
                    p.setPmid(columnInfo);
            }
            if(isnew) {
                publications.add(p);
                id++;
            }
        }
        
        if(!manager.insertPublications(publications)) {
            throw new Exception("Error occured while inserting into PUBLICATION table.");
        }
    }
}
