/*
 * Importer.java
 *
 * Created on April 7, 2005, 3:45 PM
 */

package plasmid.importexport;

import java.io.*;
import java.util.*;
import java.sql.*;
import plasmid.Constants;
import plasmid.database.*;

/**
 *
 * @author  DZuo
 */
public class Importer {
    public static final String filepath = "G:\\plasmid\\";
    
    private List tables;
    private String error;
    
    public void setError(String s) {this.error = s;}
    public String getError() {return error;}
    
    /** Creates a new instance of Importer */
    public Importer() {
    }
    
    public boolean readAllFiles() {
        ImportFileReader reader = new ImportFileReader();
        File f = new File(filepath);
        try {
            String[] files = f.list();
            
            if(files == null) {
                setError("Cannot open directory: "+filepath);
                return false;
            }
            
            tables = new ArrayList();
            for(int i=0; i<files.length; i++) {
                String file = files[i];
                if(!reader.readFile(file)) {
                    setError(reader.getErrorMessage());
                    return false;
                }
                
                ImportTable table = reader.getTable();
                tables.add(table);
            }
        } catch (Exception ex) {
            setError("Cannot read files under directory: "+filepath);
            
            if(Constants.DEBUG) {
                System.out.println(getError()+"\n"+ex.getMessage());
            }
            return false;
        }
        return true;
    }
    
    public boolean performImport() {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            setError("Cannot get database connection.");
            
            if(Constants.DEBUG) {
                System.out.println(getError()+"\n"+ex.getMessage());
            }
            return false;
        }
        
        VectorImporter vimp = new VectorImporter(conn);
        AuthorImporter aimp = new AuthorImporter(conn);
        PublicationImporter pimp = new PublicationImporter(conn);
        CloneImporter cimp = new CloneImporter(conn);
        GrowthConditionImporter gimp = new GrowthConditionImporter(conn);
        InsertImporter iimp = new InsertImporter(conn);
        RefseqImporter rimp = new RefseqImporter(conn);
        for(int i=0; i<tables.size(); i++) {
            ImportTable table = (ImportTable)tables.get(i);
            
            try {
                if(table.getTableName().equalsIgnoreCase(ImportTable.VECTOR)) {
                    vimp.importVector(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.VECTORFEATURE)) {
                    vimp.importVectorFeature(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.VECTORPROPERTY)) {
                    vimp.importVectorProperty(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.AUTHOR)) {
                    aimp.importAuthor(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.VECTORAUTHOR)) {
                    vimp.importVectorAuthor(table, aimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.PUBLICATION)) {
                    pimp.importPublication(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.VECTORPUBLICATION)) {
                    vimp.importVectorPublication(table, pimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONE)) {
                    cimp.importClone(table, vimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.GROWTHCONDITION)) {
                    gimp.importGrowthCondition(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONEGROWTH)) {
                    cimp.importCloneGrowth(table, gimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONESELECTION)) {
                    cimp.importCloneSelection(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONEHOST)) {
                    cimp.importCloneHost(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONENAMETYPE)) {
                    cimp.importCloneNameType(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONENAME)) {
                    cimp.importCloneName(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONEINSERT)) {
                    iimp.importCloneInsert(table, cimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.REFSEQ)) {
                    rimp.importRefseq(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.INSERTREFSEQ)) {
                    rimp.importInsertRefseq(table, iimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.REFSEQNAMETYPE)) {
                    rimp.importRefseqNameType(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.REFSEQNAME)) {
                    rimp.importRefseqName(table);
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONEAUTHOR)) {
                    cimp.importCloneAuthor(table, aimp.getIdmap());
                }
                if(table.getTableName().equalsIgnoreCase(ImportTable.CLONEPUBLICATION)) {
                    cimp.importClonePublication(table, pimp.getIdmap());
                }
            } catch (Exception ex) {
                setError("Error occured during import.");
                
                if(Constants.DEBUG) {
                    System.out.println(getError()+"\n"+ex.getMessage());
                }
                return false;
            }
        }

        return true;
    }
}