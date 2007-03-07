/*
 * EntrezGeneImporter.java
 *
 * Created on February 20, 2007, 12:21 PM
 */

package plasmid.importexport.entrezgene;

import plasmid.database.*;

import java.sql.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author  DZuo
 */
public abstract class EntrezGeneImporter {
    
    /** Creates a new instance of EntrezGeneImporter */
    public EntrezGeneImporter() {
    }
    
    public void importInfo(BufferedReader in, Connection conn) throws Exception {
        String line = null;
        int count = 0;
        int totalcount = 0;
        List genes = new ArrayList();
        boolean isAddedToDb = true;
        
        while((line = in.readLine()) != null) {
            genes.add(line);
            count++;
            totalcount++;
            System.out.println("count: "+totalcount);
            
            if(isAddedToDb == true) {
                isAddedToDb = false;
            }
            
            if(count == 1000 && isAddedToDb == false) {
                importInfoToDb(genes, conn);
                count = 0;
                genes = new ArrayList();
                isAddedToDb = true;
            }
        }
        
        if(isAddedToDb == false) {
            importInfoToDb(genes, conn);
        }
        System.out.println("Total genes: "+totalcount);
    }
    
    public abstract void importInfoToDb(List info, Connection conn) throws Exception;
    
}
