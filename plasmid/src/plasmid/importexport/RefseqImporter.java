/*
 * RefseqImporter.java
 *
 * Created on April 14, 2005, 3:10 PM
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
public class RefseqImporter {
    private Map idmap;
    private RefseqManager manager;
    private DnasequenceManager manager2;
    
    /** Creates a new instance of RefseqImporter */
    public RefseqImporter(Connection conn) {
        manager = new RefseqManager(conn);
        manager2 = new DnasequenceManager(conn);
    }

    public Map getIdmap() {return idmap;}
    
    public void importRefseq(ImportTable table) throws Exception {
        idmap = new HashMap();
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("referencesequence", "refseqid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get refseqid from referencesequence table.");
        }
        
        int insertseqid = m.getMaxNumber("dnasequence", "sequenceid", DatabaseTransaction.getInstance());
        if(insertseqid == -1) {
            throw new Exception("Cannot get sequenceid from dnasequence table.");
        }        
                
        List seqs = new ArrayList();
        List dnaseqs = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            Refseq c = new Refseq();
            c.setRefseqid(id);
            
            Dnasequence seq = new Dnasequence();
            seq.setSequenceid(insertseqid);
            
            id++;
            insertseqid++;
            
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("refseqid".equalsIgnoreCase(columnName)) {
                    idmap.put(columnInfo, new Integer(id));
                }
                if("type".equalsIgnoreCase(columnName)) {
                    c.setType(columnInfo);
                }
                if("name".equalsIgnoreCase(columnName)) {
                    c.setName(columnInfo);
                }
                if("description".equalsIgnoreCase(columnName)) {
                    c.setDescription(columnInfo);
                }
                if("cdsstart".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null) 
                        c.setCdsstart(Integer.parseInt(columnInfo));
                }
                if("cdsstop".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null) 
                        c.setCdsstop(Integer.parseInt(columnInfo));
                }
                if("species".equalsIgnoreCase(columnName)) {
                    c.setSpecies(columnInfo);
                }                   
                if("sequence".equalsIgnoreCase(columnName)) {                   
                    seq.setReferenceid(id);
                    seq.setType(Dnasequence.REFERENCE);
                    seq.setSequence(columnInfo);
                }         
            }
            seqs.add(c);
            dnaseqs.add(seq);
        }
        
        if(!manager.insertRefseqs(seqs)) {
            throw new Exception("Error occured while inserting into REFERENCESEQUENCE table.");
        }
        if(!manager2.insertDnasequences(dnaseqs)) {
            throw new Exception("Error occured while inserting into DNASEQUENCE and SEQTEXT table");
        } 
    }
        
    public void importInsertRefseq(ImportTable table, Map insertidmap) throws Exception {
        List seqs = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            InsertRefseq c = new InsertRefseq();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("refseqid".equalsIgnoreCase(columnName)) {
                    c.setRefseqid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("insertid".equalsIgnoreCase(columnName)) {
                    c.setInsertid(((Integer)insertidmap.get(columnInfo)).intValue());
                }               
                if("startonrefseq".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null) 
                        c.setStart(Integer.parseInt(columnInfo));
                }
                if("endonrefseq".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null) 
                        c.setStop(Integer.parseInt(columnInfo));
                }
                if("hasdiscrepancy".equalsIgnoreCase(columnName)) {
                    c.setHasDiscrepancy(columnInfo);
                }   
                if("discrepancy".equalsIgnoreCase(columnName)) {
                    c.setDiscrepancy(columnInfo);
                }        
                if("comments".equalsIgnoreCase(columnName)) {
                    c.setComments(columnInfo);
                }      
            }
            seqs.add(c);
        }
        
        if(!manager.insertInsertRefseqs(seqs)) {
            throw new Exception("Error occured while inserting into INSERTREFSEQ table.");
        }
    }
        
    public void importRefseqNameType(ImportTable table) throws Exception {
        List types = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            RefseqNameType c = new RefseqNameType();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);              
                if("refseqtype".equalsIgnoreCase(columnName)) {
                    c.setRefseqtype(columnInfo);
                }
                if("genusspecies".equalsIgnoreCase(columnName)) {
                    c.setSpecies(columnInfo);
                }   
                if("nametype".equalsIgnoreCase(columnName)) {
                    c.setNametype(columnInfo);
                }        
                if("use".equalsIgnoreCase(columnName)) {
                    c.setUse(columnInfo);
                }      
            }
            types.add(c);
        }
        
        if(!manager.insertRefseqNametypes(types)) {
            throw new Exception("Error occured while inserting into REFSEQNAMETYPE table.");
        }
    }
            
    public void importRefseqName(ImportTable table) throws Exception {
        List names = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            RefseqName c = new RefseqName();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);              
                if("refid".equalsIgnoreCase(columnName)) {
                    c.setRefseqid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("nametype".equalsIgnoreCase(columnName)) {
                    c.setNametype(columnInfo);
                }   
                if("namevalue".equalsIgnoreCase(columnName)) {
                    c.setNamevalue(columnInfo);
                }        
                if("nameurl".equalsIgnoreCase(columnName)) {
                    c.setNameurl(columnInfo);
                }      
            }
            names.add(c);
        }
        
        if(!manager.insertRefseqNames(names)) {
            throw new Exception("Error occured while inserting into REFSEQNAME table.");
        }
    }
}
