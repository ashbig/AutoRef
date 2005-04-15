/*
 * CloneVector.java
 *
 * Created on March 31, 2005, 11:36 AM
 */

package plasmid.coreobject;

import java.util.*;
import java.lang.*;

import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class CloneVector {
    private int vectorid;
    private String name;
    private String description;
    private String form;
    private String type;
    private int size;
    private String mapfilename;
    private String seqfilename;
    private String comments;
    
    private List synonyms;
    private List property;
    private Map vectorfeatures;
    private List vectorparents;
    private List publications;
    private List authors;
    
    /** Creates a new instance of CloneVector */
    public CloneVector() {
    }
    
    public CloneVector(int vectorid, String name, String description, String form, String type,
        int size, String mapfilename,  String seqfilename, String comments) {
            this.vectorid = vectorid;
            this.name = name;
            this.description = description;
            this.form = form;
            this.type = type;
            this.size = size;
            this.mapfilename = mapfilename;
            this.seqfilename = seqfilename;
            this.comments = comments;
    }
    
    public int getVectorid() {return vectorid;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getForm() {return form;}
    public String getType() {return type;}
    public int getSize() {return size;}
    public String getMapfilename() {return mapfilename;}
    public String getSeqfilename() {return seqfilename;}
    public String getComments() {return comments;}
    public void setVectorid(int id) {this.vectorid = id;}
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setForm(String s) {this.form = s;}
    public void setType(String s) {this.type = s;}
    public void setSize(int i) {this.size = i;}
    public void setMapfilename(String s) {this.mapfilename=s;}
    public void setSeqfilename(String s) {this.seqfilename=s;}
    public void setComments(String s) {this.comments=s;}
    
    public List getSynonyms() {return synonyms;}
    public void setSynonyms(List l) {this.synonyms = l;}
    
    public List getProperty() {return property;}
    public void setProperty(List l) {this.property = l;}
    
    public Map getVectorfeatures() {return vectorfeatures;}
    public void setVectorfeatures(Map l) {this.vectorfeatures = l;}
    
    public List getVectorfeaturesByType(String type) {
        List l = new ArrayList();
        if(type == null || vectorfeatures == null)
            return l;
        
        return (List)vectorfeatures.get(type);
    }
    
    public void addVectorfeatures(String type, List l) {
        if(l == null || l.size() == 0 || type == null)
            return;
        
        vectorfeatures.put(type, l);
    }
    
    public List getVectorparents() {return vectorparents;}
    public void setVectorparents(List l) {this.vectorparents = l;}
    
    public List getPublications() {return publications;}
    public void setPublications(List l) {this.publications = l;}
    
    public List getAuthors() {return authors;}
    public void setAuthors(List l) {this.authors = l;}
    
    public String getSynonymString() {
        StringConvertor sc = new StringConvertor();
        return sc.converFromListToString(synonyms);
    }
    
    public String getPropertyString() {
        StringConvertor sc = new StringConvertor();
        return sc.converFromListToString(property);
    }
    
    public static void main(String args[]) {
        CloneVector v = new CloneVector();
        List l = new ArrayList();
        l.add("abc");
        l.add("def");
        l.add("mnh");
        v.setSynonyms(l);
        System.out.println(v.getSynonymString());
        System.exit(0);
    }
}
