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
import plasmid.coreobject.Sample;
import plasmid.coreobject.Container;

/**
 *
 * @author  DZuo
 */
public class Importer {
    //public static final String filepath = "G:\\plasmid\\HIP_PA\\";
    //public static final String filepath = "G:\\plasmid\\HIP_Human\\";
    //public static final String filepath = "G:\\plasmid\\Other\\";
    //public static final String filepath = "G:\\plasmid\\Yeast\\";
    //public static final String filepath = "G:\\plasmid\\Howley\\";
    //public static final String filepath = "G:\\plasmid\\Howley_05_7_20\\";
    //public static final String filepath = "G:\\plasmid\\NEEL_05_7_20\\";    
    //(not)public static final String filepath = "G:\\plasmid\\TRC_import\\";     
    //(?)public static final String filepath = "G:\\plasmid\\Howley_Neel_plate\\import\\"; 
    //public static final String filepath = "G:\\plasmid\\Yeast_batch2_20050902\\";  
    //public static final String filepath = "G:\\plasmid\\vector_set\\";  
    //public static final String filepath = "G:\\plasmid\\Yersinia_pestis\\";  
    //public static final String filepath = "G:\\plasmid\\korsmeyer_vector\\"; 
    //public static final String filepath = "G:\\plasmid\\elledge\\"; 
    //public static final String filepath = "G:\\plasmid\\korsmeyer_vector\\"; 
    //public static final String filepath = "G:\\plasmid\\decaprio_checked\\";
    //public static final String filepath = "G:\\plasmid\\VC\\";  
    //public static final String filepath = "G:\\plasmid\\FT\\";  
    //public static final String filepath = "G:\\plasmid\\howleypluslivingston_from_access\\"; 
    //public static final String filepath = "G:\\plasmid\\Yeast_plate_2006_01\\"; 
    //public static final String filepath = "G:\\plasmid\\Yeast_plate_2006_02\\";  
    //public static final String filepath = "G:\\plasmid\\human_plate_not_in_plasmid_200602\\"; 
    //public static final String filepath = "G:\\plasmid\\human_plate_in_plasmid_200602\\"; 
    //public static final String filepath = "G:\\plasmid\\FT_2nd_batch\\"; 
    //public static final String filepath = "G:\\plasmid\\SHIV_HIPvector_IMPORT_MARCH2006\\";
    //public static final String filepath = "G:\\plasmid\\Kinase_dead_200603\\";
    //public static final String filepath = "G:\\plasmid\\Yeast_batch3_2006_04\\";
    //public static final String filepath = "G:\\plasmid\\YPTB_2006_04\\";
    //public static final String filepath = "G:\\plasmid\\VC_batch2_2006_05\\";
    //public static final String filepath = "G:\\plasmid\\VC_batch2_2006_05_partial_plates\\";
    //public static final String filepath = "G:\\plasmid\\KAMIL EXPORT SET\\";
    //public static final String filepath = "G:\\plasmid\\Master_CloneID_Update\\";
    //public static final String filepath = "G:\\plasmid\\Expression_1\\"; cloneid>25967
    //public static final String filepath = "G:\\plasmid\\Expression_2\\"; cloneid<=39217
    //public static final String filepath = "G:\\plasmid\\FT_3rd_batch\\";
    //public static final String filepath = "G:\\plasmid\\Aventis_2006_08\\fusion\\";
    //public static final String filepath = "G:\\plasmid\\Aventis_2006_08\\closed\\";
    //public static final String filepath = "G:\\plasmid\\TRC_import\\import1\\";
    //public static final String filepath = "G:\\plasmid\\TRC_import\\import2\\";
    //public static final String filepath = "G:\\plasmid\\TRC_import\\import3\\";
    //public static final String filepath = "G:\\plasmid\\TRC_import\\import4\\";
    //public static final String filepath = "G:\\plasmid\\TRC_import\\import5\\";
    //public static final String filepath = "G:\\plasmid\\TRC_import\\import6\\";
    //public static final String filepath = "G:\\plasmid\\VC_batch3_2006_08\\";
    //public static final String filepath = "G:\\plasmid\\Expression_VC_2006_08_plate\\";
    //public static final String filepath = "G:\\plasmid\\HGTI\\";
    //public static final String filepath = "G:\\plasmid\\MDLH2 import set\\";
    //public static final String filepath = "G:\\plasmid\\GAP_GEF\\";
    //public static final String filepath = "G:\\plasmid\\Expression_VC_2006_11_14_plate\\";
    //public static final String filepath = "G:\\plasmid\\FT_expression_11_16_2006_plate\\";
    //public static final String filepath = "G:\\plasmid\\bryanvought_nov_2006\\";
    //public static final String filepath = "G:\\plasmid\\BA_2006_12\\";
    //public static final String filepath = "G:\\plasmid\\Yeast_2006_12\\";
    //public static final String filepath = "G:\\plasmid\\BA2_2007_02\\";
    //public static final String filepath = "G:\\plasmid\\Velkov_2007_01\\";
    //public static final String filepath = "G:\\plasmid\\ORFome_2007_02\\";
    //public static final String filepath = "G:\\plasmid\\YP_plate_200704\\";
    //public static final String filepath = "G:\\plasmid\\Kinase_200704\\";
    //public static final String filepath = "G:\\plasmid\\plate_outside_200704\\";
    //public static final String filepath = "G:\\plasmid\\Outside_200704\\";
    //public static final String filepath = "G:\\plasmid\\BC1000_1520_2007_04\\";
    //public static final String filepath = "G:\\plasmid\\Kinase_dead_200603\\plate\\";
    //public static final String filepath = "G:\\plasmid\\Yeast_ref_200707\\";
    //public static final String filepath = "G:\\plasmid\\Yeast_ref_flexsequenceidimport_200707\\";
    //public static final String filepath = "G:\\plasmid\\Aventis_2007_08\\";
    //public static final String filepath = "G:\\plasmid\\Aventis2_2007_08_plate\\";
    //public static final String filepath = "G:\\plasmid\\ArchivePlates\\";
    //public static final String filepath = "G:\\plasmid\\kerneyluna_for_import_Aug2007\\";
    //public static final String filepath = "G:\\plasmid\\ORFeome_HIP_Nov_2007\\";
    //public static final String filepath = "G:\\plasmid\\PSI_vector\\";
    //public static final String filepath = "G:\\plasmid\\PSI_200801\\";
    //public static final String filepath = "G:\\plasmid\\ORFeome_Mar_2008\\";
    //public static final String filepath = "G:\\plasmid\\PSI_200803\\";
    //public static final String filepath = "G:\\plasmid\\donnelly_vector_mar_2008\\";
    //public static final String filepath = "G:\\plasmid\\PSI_200804\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_1\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_2\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_3\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_4\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_5\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_6\\";
    //public static final String filepath = "G:\\plasmid\\TRC_2008_05\\Ref_7\\";
    //public static final String filepath = "G:\\plasmid\\import files CESG vector clones\\";
    //public static final String filepath = "G:\\plasmid\\donnelly_vector_mar_2008\\author\\";
    //public static final String filepath = "G:\\plasmid\\PSI_200806\\";
    //public static final String filepath = "G:\\plasmid\\Vector_2008_06\\";
    //public static final String filepath = "G:\\plasmid\\PSI_2008_07\\";
    //public static final String filepath = "G:\\plasmid\\PSI_2008_07_2\\";
    //public static final String filepath = "C:\\dev\\plasmidsupport\\import\\batch9\\";
    //public static final String filepath = "C:\\dev\\plasmidsupport\\Orfeome201103\\import\\";
    //public static final String filepath = "C:\\dev\\plasmidsupport\\Orfeome201104\\import\\";
    //public static final String filepath = "C:\\dev\\plasmid_support\\wilson_201201\\import\\";
    //public static final String filepath = "C:\\dev\\plasmid_support\\wilson_201201\\import_empty_vector\\";
    //public static final String filepath = "C:\\dev\\plasmid_support\\OC_missing\\import\\";
    //public static final String filepath = "C:\\dev\\plasmid_support\\mgc_import_201111\\import3\\";
    public static final String filepath = "C:\\dev\\plasmid_support\\ccsb_201210\\import\\";
     
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
            System.out.println("table: "+table.getTableName());
            
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
                    iimp.importCloneInsert(table, cimp.getIdmap(), rimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.CLONEINSERTONLY)) {
                    System.out.println("Importing CLONEINSERTONLY.");
                    cimp.importCloneInsert(table);
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.REFSEQ)) {
                    System.out.println("Importing REFSEQ.");
                    rimp.importRefseq(table);
                }
                /*
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
                    plateImp.importPlateAndSample(table, cimp.getIdmap());
                    //plateImp.importSample(table, cimp.getIdmap());
                }
                if(table.getTableName().trim().equalsIgnoreCase(ImportTable.PLATE_384)) {
                    System.out.println("Importing Archive PLATE");
                    plateImp.importPlateAndSample(table, cimp.getIdmap(), Sample.DEEP_ARCHIVE_GLYCEROL, Container.PLATE_384);
                    //plateImp.importSample(table, cimp.getIdmap());
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
        
                //DatabaseTransaction.rollback(conn);
        DatabaseTransaction.commit(conn);
        DatabaseTransaction.closeConnection(conn);
        return true;
    }
    
    public void updateDNAInsert() {
        String sql= "select d.insertid, r.namevalue from dnainsert d, refseqname r where r.refid=d.refseqid and r.nametype='Protein GenBank Accession' and d.insertid>25413";
        String sql2= "select d.insertid, r.namevalue from dnainsert d, refseqname r where r.refid=d.refseqid and r.nametype='Protein GI' and d.insertid>25413";
        String sql3 = "update dnainsert set targetgenbank=? where insertid=?";
        String sql4 = "update dnainsert set targetseqid=? where insertid=?";
        
        DatabaseTransaction t = null;
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            stmt1 = conn.prepareStatement(sql3);
            stmt2 = conn.prepareStatement(sql4);
            
            ResultSet rs = t.executeQuery(sql);
            while(rs.next()) {
                int insertid = rs.getInt(1);
                String id = rs.getString(2);
                stmt1.setString(1, id);
                stmt1.setInt(2, 1);
                DatabaseTransaction.executeUpdate(stmt1);
                System.out.println("update insert: "+insertid+"with "+id);
            }
            DatabaseTransaction.closeResultSet(rs);
            
            rs = t.executeQuery(sql2);
            while(rs.next()) {
                int insertid = rs.getInt(1);
                String id = rs.getString(2);
                stmt2.setString(1, id);
                stmt2.setInt(2, 1);
                DatabaseTransaction.executeUpdate(stmt2);
                System.out.println("update insert: "+insertid+"with "+id);
            }
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.commit(conn);
        } catch (Exception ex) {
            System.out.println(ex);
            DatabaseTransaction.rollback(conn);
        } finally {
            DatabaseTransaction.closeStatement(stmt1);
            DatabaseTransaction.closeStatement(stmt2);
            DatabaseTransaction.closeConnection(conn);
        }
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
         
        //imp.updateDNAInsert();
        System.exit(0);
    }
}