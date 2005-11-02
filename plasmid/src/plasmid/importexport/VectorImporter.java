/*
 * VectorImporter.java
 *
 * Created on April 12, 2005, 12:22 PM
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
public class VectorImporter {
    private Map idmap;
    private VectorManager vm;
    
    /** Creates a new instance of VectorImporter */
    public VectorImporter(Connection conn) {
        vm = new VectorManager(conn);
    }
    
    public Map getIdmap() {return idmap;}
    
    public void importVector(ImportTable table) throws Exception {
        idmap = new HashMap();
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("vector", "vectorid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get vectorid from vector table.");
        }
        
        List vectors = new ArrayList();
        List synonyms = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            CloneVector v = new CloneVector();
            v.setVectorid(id);
            
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                if("name".equalsIgnoreCase(columnName)) {
                    idmap.put(columnInfo, new Integer(id));
                    v.setName(columnInfo);
                }
                if("description".equalsIgnoreCase(columnName))
                    v.setDescription(columnInfo);
                if("form".equalsIgnoreCase(columnName))
                    v.setForm(columnInfo);
                if("type".equalsIgnoreCase(columnName))
                    v.setType(columnInfo);
                if("sizeinbp".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null)
                        v.setSize(Integer.parseInt(columnInfo));
                }
                if("mapfilename".equalsIgnoreCase(columnName))
                    v.setMapfilename(columnInfo);
                if("sequencefilename".equalsIgnoreCase(columnName))
                    v.setSeqfilename(columnInfo);
                if("comments".equalsIgnoreCase(columnName))
                    v.setComments(columnInfo);
                
                if("synonyms".equalsIgnoreCase(columnName) && columnInfo != null) {
                    StringTokenizer st = new StringTokenizer(columnInfo, ",");
                    List l = new ArrayList();
                    while(st.hasMoreTokens()) {
                        String s = st.nextToken();
                        VectorSynonym synonym = new VectorSynonym(id, s);
                        l.add(synonym);
                    }
                    synonyms.addAll(l);
                }
            }
            vectors.add(v);
            id++;
        }
        
        if(!vm.insertVectors(vectors)) {
            throw new Exception("Error occured while inserting into VECTOR table.");
        }
        if(!vm.insertSynonyms(synonyms)) {
            throw new Exception("Error occured while inserting into VECTORSYNONYM table.");
        }
    }
    
    public void importVectorFeature(ImportTable table) throws Exception {
        DefTableManager m = new DefTableManager();
        int id = m.getMaxNumber("vectorfeature", "featureid", DatabaseTransaction.getInstance());
        if(id == -1) {
            throw new Exception("Cannot get featureid from vectorfeature table.");
        }
        
        List features = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            VectorFeature v = new VectorFeature();
            v.setFeatureid(id);
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
               //System.out.println(columnName);
                //System.out.println(columnInfo);
                if("name".equalsIgnoreCase(columnName)) {
                    v.setName(columnInfo);
                }
                if("description".equalsIgnoreCase(columnName))
                    v.setDescription(columnInfo);
                if("startpos".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null)
                        v.setStart(Integer.parseInt(columnInfo));
                }
                if("endpos".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null)
                        v.setStop(Integer.parseInt(columnInfo));
                }
                if("maptype".equalsIgnoreCase(columnName))
                    v.setMaptype(columnInfo);
                if("vectorname".equalsIgnoreCase(columnName)) {
                    v.setVectorid(((Integer)idmap.get(columnInfo)).intValue());
                }
            }
            features.add(v);
            id++;
        }
        
        if(!vm.insertVectorFeatures(features)) {
            throw new Exception("Error occured while inserting into VECTORFEATURE table");
        }
    }
    
    public void importVectorProperty(ImportTable table) throws Exception {
        List properties = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            VectorProperty v = new VectorProperty();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName);
                //System.out.println(columnInfo);
                if("propertytype".equalsIgnoreCase(columnName)) {
                    v.setPropertyType(columnInfo);
                }
                if("vectorname".equalsIgnoreCase(columnName)) {
                    v.setVectorid(((Integer)idmap.get(columnInfo)).intValue());
                }
            }
            properties.add(v);
        }
        
        if(!vm.insertProperties(properties)) {
            throw new Exception("Error occured while inserting into VECTORPROPERTY table");
        }
    }
    
    public void importVectorParent(ImportTable table) throws Exception {
        List parents = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            VectorParent v = new VectorParent();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName+"\t"+columnInfo);
                if("parentname".equalsIgnoreCase(columnName)) {
                    v.setParentvectorname(columnInfo);
                }
                if("comments".equalsIgnoreCase(columnName)) {
                    v.setComments(columnInfo);
                }
                if("vectorname".equalsIgnoreCase(columnName)) {
                    v.setVectorid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("parentvectorname".equalsIgnoreCase(columnName)) {
                    if(columnInfo != null)
                        v.setParentvectorid(((Integer)idmap.get(columnInfo)).intValue());
                }
            }
            parents.add(v);
        }
        
        if(!vm.insertParents(parents)) {
            throw new Exception("Error occured while inserting into VECTORPARENT table");
        }
    }
    
    public void importVectorAuthor(ImportTable table, Map authoridmap) throws Exception {
        List authors = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            VectorAuthor v = new VectorAuthor();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                //System.out.println(columnName+"\t"+columnInfo);
                if("authortype".equalsIgnoreCase(columnName)) {
                    v.setType(columnInfo);
                }
                if("creationdate".equalsIgnoreCase(columnName)) {
                    v.setDate(columnInfo);
                }
                if("vectorname".equalsIgnoreCase(columnName)) {
                    v.setVectorid(((Integer)idmap.get(columnInfo)).intValue());
                    //System.out.println("vectorid: "+columnInfo+"\t"+idmap.get(columnInfo));
                }
                if("authorname".equalsIgnoreCase(columnName)) {
                    int authorid = 0;
                    if(authoridmap == null) {
                        AuthorManager man = new AuthorManager(vm.getConnection());
                        authorid = man.getAuthorid(columnInfo);
                    } else {
                        Integer aid = (Integer)authoridmap.get(columnInfo);
                        if(aid == null) {
                            AuthorManager man = new AuthorManager(vm.getConnection());
                            authorid = man.getAuthorid(columnInfo);
                        } else {
                            authorid = aid.intValue();
                        }
                    }
                    
                    if(authorid <= 0) {
                        throw new Exception("Cannot get authorid with authorname: "+columnInfo);
                    }
                    v.setAuthorid(authorid);
                    //System.out.println("authorid: "+columnInfo+"\t"+authorid);
                }
            }
            authors.add(v);
        }
        
        if(!vm.insertVectorAuthors(authors)) {
            throw new Exception("Error occured while inserting into VECTORAUTHOR table");
        }
    }
    
    public void importVectorPublication(ImportTable table, Map pubidmap) throws Exception {
        List publications = new ArrayList();
        List columns = table.getColumnNames();
        List contents = table.getColumnInfo();
        for(int n=0; n<contents.size(); n++) {
            VectorPublication v = new VectorPublication();
            List row = (List)contents.get(n);
            for(int i=0; i<columns.size(); i++) {
                String columnName = (String)columns.get(i);
                String columnInfo = (String)row.get(i);
                System.out.println(columnName+"\t"+columnInfo);
                if("vectorname".equalsIgnoreCase(columnName)) {
                    v.setVectorid(((Integer)idmap.get(columnInfo)).intValue());
                }
                if("publicationid".equalsIgnoreCase(columnName)) {
                    v.setPublicationid(((Integer)pubidmap.get(columnInfo)).intValue());
                }
            }
            publications.add(v);
        }
        
        if(!vm.insertVectorPublications(publications)) {
            throw new Exception("Error occured while inserting into VECTORPUBLICATION table");
        }
    }
}
