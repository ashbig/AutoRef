/*
 * CloneImporter.java
 *
 * Created on April 14, 2005, 11:27 AM
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
public class CloneImporter {   
    private Map idmap;
    private CloneManager manager;
    
    /** Creates a new instance of CloneImporter */
    public CloneImporter(Connection conn) {
        this.manager = new CloneManager(conn);
    }
        
    public Map getIdmap() {return idmap;}
    
    public void importClone(ImportTable table, Map vectoridmap) throws Exception {
        idmap = new HashMap();
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("clone", "cloneid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get cloneid from clone table.");
        }
        
        List clones = new ArrayList();
        List synonyms = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            Clone c = new Clone();
            c.setCloneid(id);
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("clonename".equalsIgnoreCase(columnName)) {
                    idmap.put(columnInfo, new Integer(id));
                    c.setName("HIP"+id);
                }
                if("clonetype".equalsIgnoreCase(columnName)) {
                    c.setType(columnInfo);
                }
                if("verified".equalsIgnoreCase(columnName)) {
                    c.setVerified(columnInfo);
                }
                if("vermethod".equalsIgnoreCase(columnName)) {
                    c.setVermethod(columnInfo);
                }
                if("domain".equalsIgnoreCase(columnName)) {
                    c.setDomain(columnInfo);
                }
                if("subdomain".equalsIgnoreCase(columnName)) {
                    c.setSubdomain(columnInfo);
                }
                if("restriction".equalsIgnoreCase(columnName)) {
                    c.setRestriction(columnInfo);
                }
                if("comments".equalsIgnoreCase(columnName)) {
                    c.setComments(columnInfo);
                }
                if("vectorname".equalsIgnoreCase(columnName)) {
                    c.setVectorid(((Integer)vectoridmap.get(columnInfo)).intValue());
                    c.setVectorname(columnInfo);
                }
                if("clonemapfilename".equalsIgnoreCase(columnName)) {
                    c.setClonemap(columnInfo);
                }
                if("status".equalsIgnoreCase(columnName)) {
                    c.setStatus(columnInfo);
                }
            }
            clones.add(c);
            id++;
        }
        
        if(!manager.insertClones(clones)) {
            throw new Exception("Error occured while inserting into CLONE table.");
        }
    }
     
    public void importCloneGrowth(ImportTable table, Map growthidmap) throws Exception {
        List growths = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneGrowth c = new CloneGrowth();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("growthname".equalsIgnoreCase(columnName)) {
                    c.setGrowthid(((Integer)growthidmap.get(columnInfo)).intValue());
                }
                if("isrecommended".equalsIgnoreCase(columnName)) {
                    c.setIsrecommended(columnInfo);
                }
            }
            growths.add(c);
        }
        
        if(!manager.insertCloneGrowths(growths)) {
            throw new Exception("Error occured while inserting into CLONEGROWTH table");
        }
    }    
      
    public void importCloneSelection(ImportTable table) throws Exception {
        List selections = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneSelection c = new CloneSelection();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("hosttype".equalsIgnoreCase(columnName)) {
                    c.setHosttype(columnInfo);
                }
                if("marker".equalsIgnoreCase(columnName)) {
                    c.setMarker(columnInfo);
                }
            }
            selections.add(c);
        }
        
        if(!manager.insertCloneSelections(selections)) {
            throw new Exception("Error occured while inserting into CLONESELECTION table");
        }
    }    
          
    public void importCloneHost(ImportTable table) throws Exception {
        List hosts = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneHost c = new CloneHost();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("hoststrain".equalsIgnoreCase(columnName)) {
                    c.setHoststrain(columnInfo);
                }
                if("isinuse".equalsIgnoreCase(columnName)) {
                    c.setIsinuse(columnInfo);
                }
                if("description".equalsIgnoreCase(columnName)) {
                    c.setDescription(columnInfo);
                }
            }
            hosts.add(c);
        }
        
        if(!manager.insertCloneHosts(hosts)) {
            throw new Exception("Error occured while inserting into HOST table");
        }
    }    
        
    public void importCloneNameType(ImportTable table) throws Exception {
        List types = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneNameType c = new CloneNameType();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("clonetype".equalsIgnoreCase(columnName)) {
                    c.setClonetype(columnInfo);
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
        
        if(!manager.insertCloneNameTypes(types)) {
            throw new Exception("Error occured while inserting into CLONENAMETYPE table");
        }
    }   
        
    public void importCloneName(ImportTable table) throws Exception {
        List names = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneName c = new CloneName();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("nametype".equalsIgnoreCase(columnName)) {
                    c.setType(columnInfo);
                }
                if("namevalue".equalsIgnoreCase(columnName)) {
                    c.setValue(columnInfo);
                }
                if("nameurl".equalsIgnoreCase(columnName)) {
                    c.setUrl(columnInfo);
                }
            }
            names.add(c);
        }
        
        if(!manager.insertCloneNames(names)) {
            throw new Exception("Error occured while inserting into CLONENAME table");
        }
    } 

    public void importCloneAuthor(ImportTable table, Map authoridmap) throws Exception {
        List authors = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneAuthor c = new CloneAuthor();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("authorname".equalsIgnoreCase(columnName)) {
                    c.setAuthorid(((Integer)authoridmap.get(columnInfo)).intValue());
                }
                if("authortype".equalsIgnoreCase(columnName)) {
                    c.setAuthortype(columnInfo);
                }
            }
            authors.add(c);
        }
        
        if(!manager.insertCloneAuthors(authors)) {
            throw new Exception("Error occured while inserting into CLONEAUTHOR table");
        }
    }   
        
    public void importClonePublication(ImportTable table, Map pubidmap) throws Exception {
        List pubs = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            ClonePublication c = new ClonePublication();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("publicationid".equalsIgnoreCase(columnName)) {
                    c.setPublicationid(((Integer)pubidmap.get(columnInfo)).intValue());
                }
            }
            pubs.add(c);
        }
        
        if(!manager.insertClonePublications(pubs)) {
            throw new Exception("Error occured while inserting into CLONEPUBLICATION table");
        }
    } 
}
