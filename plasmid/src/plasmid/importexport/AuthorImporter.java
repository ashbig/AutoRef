/*
 * AuthorImporter.java
 *
 * Created on April 13, 2005, 3:25 PM
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
public class AuthorImporter {
    private Map idmap;
    private AuthorManager manager;
    
    /** Creates a new instance of AuthorImporter */
    public AuthorImporter(Connection conn) {
        manager = new AuthorManager(conn);
    }
     
    public Map getIdmap() {return idmap;}
    
    public void importAuthor(ImportTable table) throws Exception {
        idmap = new HashMap();
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("authorinfo", "authorid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get authorid from authorinfo table.");
            return;
        }
        
        List authors = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            Authorinfo v = new Authorinfo();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("authorname".equalsIgnoreCase(columnName)) {
                    idmap.put(columnInfo, new Integer(id));
                    v.setName(columnInfo);
                }
                if("firstname".equalsIgnoreCase(columnName))
                    v.setFirstname(columnInfo);
                if("lastname".equalsIgnoreCase(columnName))
                    v.setLastname(columnInfo);
                if("tel".equalsIgnoreCase(columnName))
                    v.setTel(columnInfo);
                if("fax".equalsIgnoreCase(columnName))
                    v.setFax(columnInfo);
                if("authoremail".equalsIgnoreCase(columnName))
                    v.setEmail(columnInfo);
                if("address".equalsIgnoreCase(columnName))
                    v.setAddress(columnInfo);
                if("www".equalsIgnoreCase(columnName))
                    v.setWww(columnInfo);
                if("description".equalsIgnoreCase(columnName))
                    v.setDescription(columnInfo);
                
                v.setAuthorid(id);                
            }
            authors.add(v);
            id++;
        }
        
        if(!manager.insertAuthors(authors)) {
            throw new Exception("Error occured while inserting into AUTHORINFO table.");
            return;
        }
    }   
}
