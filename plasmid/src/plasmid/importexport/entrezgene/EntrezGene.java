/*
 * EntrezGene.java
 *
 * Created on February 20, 2007, 1:28 PM
 */

package plasmid.importexport.entrezgene;

import java.sql.*;
import java.io.*;
import java.util.*;

import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class EntrezGene {
    
    /** Creates a new instance of EntrezGene */
    public EntrezGene() {
    }
    
    public static void main(String args[]) {
        String geneInfoFile = "Z:\\DZuo\\EntrezGene\\gene_info";
        String gene2AccFile = "Z:\\DZuo\\EntrezGene\\gene2accession";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        BufferedReader geneInfoInput = null;
        BufferedReader gene2AccInput = null;
        
        EntrezGeneImporter importer = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            /**
            System.out.println("Read gene info file: "+geneInfoFile);
            geneInfoInput = new BufferedReader(new FileReader(geneInfoFile));
            System.out.println("Import gene info...");
            importer = EntrezGeneImporterFactory.getEntrezGeneImporter(EntrezGeneImporterFactory.GENE);
            importer.importInfo(geneInfoInput, conn);
            DatabaseTransaction.commit(conn);
            geneInfoInput.close();
            System.out.println("Import gene info finished");
            */
            System.out.println("Read sequence info file: "+gene2AccFile);
            gene2AccInput = new BufferedReader(new FileReader(gene2AccFile));
            System.out.println("Import sequence info...");
            importer = EntrezGeneImporterFactory.getEntrezGeneImporter(EntrezGeneImporterFactory.SEQUENCE);
            importer.importInfo(gene2AccInput, conn);
            DatabaseTransaction.commit(conn);
            gene2AccInput.close();
            System.out.println("Import sequence info finished");
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
