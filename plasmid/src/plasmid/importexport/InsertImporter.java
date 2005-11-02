/*
 * InsertImporter.java
 *
 * Created on April 14, 2005, 1:28 PM
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
public class InsertImporter {
    private Map idmap;
    private CloneManager manager;
    private DnasequenceManager manager2;
    private int maxseqid = 1;
    
    /** Creates a new instance of InsertImporter */
    public InsertImporter(Connection conn) {
        manager = new CloneManager(conn);
        manager2 = new DnasequenceManager(conn);
    }
    
    public int getMaxseqid() {return maxseqid;}
    
    public Map getIdmap() {return idmap;}
    
    public void importCloneInsert(ImportTable table, Map cloneidmap, Map refseqidmap, int maxid) throws Exception {
        idmap = new HashMap();
        DefTableManager m = new DefTableManager();
        
        int id = m.getMaxNumber("dnainsert", "insertid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get insertid from dnainsert table.");
        }
        
        int insertseqid = m.getMaxNumber("dnasequence", "sequenceid", DatabaseTransaction.getInstance());
        if(insertseqid == -1) {
            throw new Exception("Cannot get sequenceid from dnasequence table.");
        }
        if(maxid > insertseqid)
            insertseqid = maxid;
        
        List inserts = new ArrayList();
        List dnaseqs = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            DnaInsert c = new DnaInsert();
            c.setInsertid(id);
            Dnasequence seq = new Dnasequence();
            seq.setSequenceid(insertseqid);
            
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(i+": "+columnInfo);
                if("insertid".equalsIgnoreCase(columnName)) {
                    idmap.put(columnInfo, new Integer(id));
                }
                if("insertorder".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null)
                        c.setOrder(Integer.parseInt(columnInfo));
                }
                if("sizeinbp".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null)
                        c.setSize(Integer.parseInt(columnInfo));
                }
                if("species".equalsIgnoreCase(columnName)) {
                    c.setSpecies(columnInfo);
                }
                if("format".equalsIgnoreCase(columnName)) {
                    c.setFormat(columnInfo);
                }
                if("source".equalsIgnoreCase(columnName)) {
                    c.setSource(columnInfo);
                }
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)cloneidmap.get(columnInfo)).intValue());
                }
                if("sequence".equalsIgnoreCase(columnName)) {
                    seq.setInsertid(id);
                    seq.setType(Dnasequence.INSERT);
                    seq.setSequence(columnInfo);
                }
                if("geneid".equalsIgnoreCase(columnName)) {
                    c.setGeneid(columnInfo);
                }
                if("name".equalsIgnoreCase(columnName)) {
                    c.setName(columnInfo);
                }
                if("description".equalsIgnoreCase(columnName)) {
                    c.setDescription(columnInfo);
                }
                if("targetseqid".equalsIgnoreCase(columnName)) {
                    c.setTargetseqid(columnInfo);
                }
                if("targetgenbank".equalsIgnoreCase(columnName)) {
                    c.setTargetgenbank(columnInfo);
                }
                if("hasdiscrepancy".equalsIgnoreCase(columnName)) {
                    c.setHasdiscrepancy(columnInfo);
                }
                if("hasmutation".equalsIgnoreCase(columnName)) {
                    c.setHasmutation(columnInfo);
                }
                if("region".equalsIgnoreCase(columnName)) {
                    c.setRegion(columnInfo);
                }
                if("refseqid".equalsIgnoreCase(columnName)) {
                    Integer refseqid = (Integer)refseqidmap.get(columnInfo);
                    
                    if(refseqid != null) {
                        c.setRefseqid(((Integer)refseqid).intValue());
                    }
                }
            }
            inserts.add(c);
            dnaseqs.add(seq);
            id++;
            insertseqid++;
        }
        
        maxseqid = insertseqid;
        if(!manager.insertCloneInserts(inserts)) {
            throw new Exception("Error occured while inserting into DNAINSERT table");
        }
        if(!manager2.insertDnasequences(dnaseqs)) {
            throw new Exception("Error occured while inserting into DNASEQUENCE and SEQTEXT table");
        }
    }
    
    public void importInsertProperty(ImportTable table) throws Exception {
        List names = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            InsertProperty c = new InsertProperty();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName+"\t"+columnInfo);
                if("insertid".equalsIgnoreCase(columnName)) {
                    c.setInsertid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("propertytype".equalsIgnoreCase(columnName)) {
                    c.setType(columnInfo);
                }
                if("propertyvalue".equalsIgnoreCase(columnName)) {
                    c.setValue(columnInfo);
                }
                if("extrainfo".equalsIgnoreCase(columnName)) {
                    c.setExtrainfo(columnInfo);
                }
            }
            names.add(c);
        }
        
        if(!manager.insertInsertProperties(names)) {
            throw new Exception("Error occured while inserting into INSERTPROPERTY table");
        }
    }
}
