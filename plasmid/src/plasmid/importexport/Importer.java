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
    //public static final String filepath = "G:\\plasmid\\HIP_PA\\";
    //public static final String filepath = "G:\\plasmid\\HIP_Human\\";
    //public static final String filepath = "G:\\plasmid\\Other\\";
    //public static final String filepath = "G:\\plasmid\\Yeast\\";
    public static final String filepath = "G:\\plasmid\\Howley\\";
    
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
                if(!reader.readFile(filepath+file)) {
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
        PlateImporter plateImp = new PlateImporter(conn);
        for(int i=0; i<tables.size(); i++) {
            ImportTable table = (ImportTable)tables.get(i);
            
            try {
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.VECTOR)) {
                    System.out.println("Importing VECTOR.");
                    vimp.importVector(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.VECTORFEATURE)){
                    System.out.println("Importing VECTORFEATURE.");
                    vimp.importVectorFeature(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.VECTORPROPERTY)) {
                    System.out.println("Importing VECTORPROPERTY.");
                    vimp.importVectorProperty(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.VECTORPARENT)) {
                    System.out.println("Importing VECTORPARENT.");
                    vimp.importVectorParent(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.AUTHOR)) {
                    System.out.println("Importing AUTHOR.");
                    aimp.importAuthor(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.VECTORAUTHOR)) {
                    System.out.println("Importing VECTORAUTHOR.");
                    vimp.importVectorAuthor(table, aimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.PUBLICATION)) {
                    System.out.println("Importing PUBLICATION.");
                    pimp.importPublication(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.VECTORPUBLICATION)) {
                    System.out.println("Importing VECTORPUBLICATION.");
                    vimp.importVectorPublication(table, pimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONE)) {
                    System.out.println("Importing CLONE.");
                    cimp.importClone(table, vimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.GROWTHCONDITION)) {
                    System.out.println("Importing GROWTHCONDITION.");
                    gimp.importGrowthCondition(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEGROWTH)) {
                    System.out.println("Importing CLONEGROWTH.");
                    cimp.importCloneGrowth(table, gimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONESELECTION)) {
                    System.out.println("Importing CLONESELECTION.");
                    cimp.importCloneSelection(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEHOST)) {
                    System.out.println("Importing CLONEHOST.");
                    cimp.importCloneHost(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONENAMETYPE)) {
                    System.out.println("Importing CLONENAMETYPE.");
                    cimp.importCloneNameType(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONENAME)) {
                    System.out.println("Importing CLONENAME.");
                    cimp.importCloneName(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEINSERT)) {
                    System.out.println("Importing CLONEINSERT.");
                    iimp.importCloneInsert(table, cimp.getIdmap(), rimp.getIdmap(), rimp.getMaxseqid());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.REFSEQ)) {
                    System.out.println("Importing REFSEQ.");
                    rimp.importRefseq(table, iimp.getMaxseqid());
                }
                /**
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.INSERTREFSEQ)) {
                    System.out.println("Importing INSERTREFSEQ.");
                    rimp.importInsertRefseq(table, iimp.getIdmap());
                }
                 **/
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.REFSEQNAMETYPE)) {
                    System.out.println("Importing REFSEQNAMETYPE.");
                    rimp.importRefseqNameType(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.REFSEQNAME)) {
                    System.out.println("Importing REFSEQNAME.");
                    rimp.importRefseqName(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEAUTHOR)) {
                    System.out.println("Importing CLONEAUTHOR.");
                    cimp.importCloneAuthor(table, aimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEPUBLICATION)) {
                    System.out.println("Importing CLONEPUBLICATION.");
                    cimp.importClonePublication(table, pimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEPROPERTY)) {
                    System.out.println("Importing CLONEPROPERTY.");
                    cimp.importCloneProperty(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONECOLLECTION)) {
                    System.out.println("Importing CLONECOLLECTION.");
                    cimp.importCloneCollection(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.INSERTPROPERTY)) {
                    System.out.println("Importing INSERTPROPERTY.");
                    iimp.importInsertProperty(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.PLATE)) {
                    System.out.println("Importing PLATE");
                    plateImp.importPlateAndSample(table);
                }
            } catch (Exception ex) {
                setError("Error occured during import.");
                
                if(Constants.DEBUG) {
                    System.out.println(getError()+"\n"+ex.getMessage());
                }
                DatabaseTransaction.rollback(conn);
                DatabaseTransaction.closeConnection(conn);
                return false;
            }
        }
        
        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        return true;
    }
    
    public static void main(String args[]) {
        Importer imp = new Importer();
        if(!imp.readAllFiles()) {
            System.out.println(imp.getError());
            System.exit(0);
        }
        if(imp.performImport()) {
            System.out.println("Import successful.");
        } else {
            System.out.println(imp.getError());
            System.out.println("Import failed.");
        }
        System.exit(0);
    }
}