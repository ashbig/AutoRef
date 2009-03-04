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
        List clones = new ArrayList();
        List synonyms = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            Clone c = new Clone();
            int id = DefTableManager.getNextid("cloneid");
            c.setCloneid(id);
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName);
                //System.out.println(columnInfo);
                if("clonename".equalsIgnoreCase(columnName)) {
                    //System.out.println(columnInfo+"="+id);
                    idmap.put(columnInfo, new Integer(id));
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
                    int vectorid = 0;
                    if(vectoridmap == null) {
                        VectorManager man = new VectorManager(manager.getConnection());
                        vectorid = man.getVectorid(columnInfo);
                    } else {
                        Integer vid = (Integer)vectoridmap.get(columnInfo);
                        if(vid == null) {
                            VectorManager man = new VectorManager(manager.getConnection());
                            vectorid = man.getVectorid(columnInfo);
                        } else {
                            vectorid = vid.intValue();
                        }
                    }
                    
                    if(vectorid <= 0) {
                        throw new Exception("Cannot get vectorid with vectorname: "+columnInfo);
                    }
                    c.setVectorid(vectorid);
                    c.setVectorname(columnInfo);
                }
                if("clonemapfilename".equalsIgnoreCase(columnName)) {
                    c.setClonemap(columnInfo);
                }
                if("status".equalsIgnoreCase(columnName)) {
                    c.setStatus(columnInfo);
                }
                if("specialtreatment".equalsIgnoreCase(columnName)) {
                    c.setSpecialtreatment(columnInfo);
                }
                if("source".equalsIgnoreCase(columnName)) {
                    c.setSource(columnInfo);
                }
                if("description".equalsIgnoreCase(columnName)) {
                    c.setDescription(columnInfo);
                }
            }
            
            c.setName(Clone.constructClonename(manager.getConnection(), c));
            clones.add(c);
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
                //System.out.println(columnName+"\t"+columnInfo);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("growthname".equalsIgnoreCase(columnName)) {
                    int growthid = 0;
                    if(growthidmap == null) {
                        GrowthConditionManager man = new GrowthConditionManager(manager.getConnection());
                        growthid = man.getGrowthid(columnInfo);
                    } else {
                        Integer gid = (Integer)growthidmap.get(columnInfo);
                        if(gid == null) {
                            GrowthConditionManager man = new GrowthConditionManager(manager.getConnection());
                            growthid = man.getGrowthid(columnInfo);
                        } else {  
                        growthid = gid.intValue();
                        }
                    }
                    
                    if(growthid <= 0) {
                        throw new Exception("Cannot get growthid from growthname: "+columnInfo);
                    }
                    c.setGrowthid(growthid);
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
            //System.out.println("n: "+n);
            CloneName c = new CloneName();
            List row = (List)contents.get(n);
            //System.out.println(row);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName);
                //System.out.println(columnInfo);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                    //c.setCloneid(Integer.parseInt(columnInfo));
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
                //System.out.println(columnName);
                //System.out.println(columnInfo);
                
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("authorname".equalsIgnoreCase(columnName)) {
                    int authorid = 0;
                    if(authoridmap == null) {
                        AuthorManager man = new AuthorManager(manager.getConnection());
                        authorid = man.getAuthorid(columnInfo);
                    } else {
                        Integer aid = (Integer)authoridmap.get(columnInfo);
                        if(aid == null) {
                            AuthorManager man = new AuthorManager(manager.getConnection());
                            authorid = man.getAuthorid(columnInfo);
                        } else {
                            authorid = aid.intValue();
                        }
                    }
                    
                    if(authorid <= 0) {
                        throw new Exception("Cannot get authorid from authorname: "+columnInfo);
                    }
                    c.setAuthorid(authorid);
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
                    //System.out.println("cloneid: "+c.getCloneid()+"\t"+columnInfo);
                }
                if("publicationid".equalsIgnoreCase(columnName)) {
                    int pubid = 0;
                    if(pubidmap == null) {
                        pubid = Integer.parseInt(columnInfo);
                    } else {
                        pubid = ((Integer)pubidmap.get(columnInfo)).intValue();
                    }
                    
                    if(pubid <= 0) {
                        throw new Exception("Cannot get publicationid from publication: "+columnInfo);
                    }
                    c.setPublicationid(pubid);
                    //System.out.println("publicationid: "+c.getPublicationid()+"\t"+columnInfo);
                }
            }
            pubs.add(c);
        }
        
        if(!manager.insertClonePublications(pubs)) {
            throw new Exception("Error occured while inserting into CLONEPUBLICATION table");
        }
    }
    
    public void importCloneProperty(ImportTable table) throws Exception {
        List names = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneProperty c = new CloneProperty();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
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
        
        if(!manager.insertCloneProperties(names)) {
            throw new Exception("Error occured while inserting into CLONEPROPERTY table");
        }
    }
    
    public void importCloneCollection(ImportTable table) throws Exception {
        List cs = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneCollection c = new CloneCollection();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName);
                //System.out.println(columnInfo);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    c.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("collectionname".equalsIgnoreCase(columnName)) {
                    c.setName(columnInfo);
                }
            }
            cs.add(c);
        }
        
        if(!manager.insertCloneCollections(cs)) {
            throw new Exception("Error occured while inserting into CLONECOLLECTION table");
        }
    }
    
    public void importCloneInsert(ImportTable table) throws Exception {
        List cs = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            DnaInsert insert = new DnaInsert();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName);
                //System.out.println(columnInfo);
                if("cloneid".equalsIgnoreCase(columnName)) {
                    insert.setCloneid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("insertid".equalsIgnoreCase(columnName)) {
                    insert.setInsertid(Integer.parseInt(columnInfo));
                }
            }
            cs.add(insert);
        }
        
        if(!manager.insertCloneInsertsWithoutInsertInfo(cs)) {
            throw new Exception("Error occured while inserting into CLONEINSERT table");
        }
    }
}
