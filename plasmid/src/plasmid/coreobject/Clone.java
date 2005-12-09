/*
 * Clone.java
 *
 * Created on March 31, 2005, 4:03 PM
 */

package plasmid.coreobject;

import java.io.*;
import java.util.*;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class Clone implements Serializable {
    public static final String AVAILABLE = "AVAILABLE";
    public static final String CDNA = "cDNA";
    public static final String SHRNA = "shRNA";
    public static final String GENOMIC_FRAGMENT = "genomic fragment";
    public static final String TFBINDSITE = "trxn factor bind site";
    public static final String PROMOTER = "promoter";
    public static final String TRANSPOSON = "transposon";
    public static final String GENE = "gene";
    public static final String GENOME = "genome";
    public static final String NOINSERT = "No insert";
    public static final String NO_RESTRICTION = "No restriction";
    public static final String NON_PROFIT = "Academic and non-profit labs";
    public static final String SPECIES_NOINSERT = "Ev";
    
    protected int cloneid;
    protected String name;
    protected String type;
    protected String verified;
    protected String vermethod;
    protected String domain;
    protected String subdomain;
    protected String restriction;
    protected String comments;
    protected int vectorid;
    protected String vectorname;
    protected String clonemap;
    protected String status;
    protected String specialtreatment;
    protected String source;
    
    protected List synonyms;
    protected List publications;
    protected List authors;
    protected List selections;
    protected List growths;
    protected List names;
    protected List inserts;
    protected List hosts;
    protected List properties;
    
    protected GrowthCondition recommendedGrowthCondition;
    protected CloneVector vector;
    
    /** Creates a new instance of Clone */
    public Clone() {
    }
    
    public Clone(int cloneid, String name, String type, String verified,
    String vermethod, String domain, String subdomain, String restriction, 
    String comments, int vectorid, String vectorname, String clonemap, String status,
    String specialtreatment, String source) {
        this.cloneid = cloneid;
        this.name = name;
        this.type = type;
        this.verified = verified;
        this.vermethod = vermethod;
        this.domain = domain;
        this.subdomain = subdomain;
        this.restriction = restriction;
        this.comments = comments;
        this.vectorid = vectorid;
        this.vectorname = vectorname;
        this.clonemap = clonemap;
        this.status = status;
        this.specialtreatment = specialtreatment;
        this.source = source;
    }
        
    private void writeObject(java.io.ObjectOutputStream out)
     throws IOException {
         out.defaultWriteObject();        
    }
 
     private void readObject(java.io.ObjectInputStream in)
     throws IOException, ClassNotFoundException {
         in.defaultReadObject();
     }
     
    public int getCloneid() {return cloneid;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getVerified() {return verified;}
    public String getVermethod() {return vermethod;}
    public String getDomain() {return domain;}
    public String getSubdomain() {return subdomain;}
    public String getRestriction() {return restriction;}
    public String getComments() {return comments;}
    public int getVectorid() {return vectorid;}
    public String getVectorname() {return vectorname;}
    public String getClonemap() {return clonemap;}
    public String getStatus() {return status;}
    public GrowthCondition getRecommendedGrowthCondition() {return recommendedGrowthCondition;}
    public CloneVector getVector() {return vector;}
    public List getProperties() {return properties;}
    public String getSpecialtreatment() {return specialtreatment;}
    public String getSource() {return source;}
    
    public void setCloneid(int id) {this.cloneid = id;}
    public void setName(String s) {this.name = s;}
    public void setType(String s) {this.type = s;}
    public void setVerified(String s) {this.verified = s;}
    public void setVermethod(String s) {this.vermethod = s;}
    public void setDomain(String s) {this.domain = s;}
    public void setSubdomain(String s) {this.subdomain = s;}
    public void setRestriction(String s) {this.restriction = s;}
    public void setComments(String s) {this.comments = s;}
    public void setVectorname(String s) {this.vectorname = s;}
    public void setVectorid(int id) {this.vectorid = id;}
    public void setClonemap(String s) {this.clonemap = s;}
    public void setStatus(String s) {this.status = s;}
    public void setRecommendedGrowthCondition(GrowthCondition g) {this.recommendedGrowthCondition = g;}
    public void setVector(CloneVector v) {this.vector = v;}
    public void setProperties(List l) {this.properties = l;}
    public void setSpecialtreatment(String s) {this.specialtreatment = s;}
    public void setSource(String s) {this.source = s;}
    
    public List getSynonyms() {return synonyms;}
    public void setSynonyms(List l) {this.synonyms = l;}
   
    public List getPublications() {return publications;}
    public void setPublications(List l) {this.publications = l;}
    
    public List getAuthors() {return authors;}
    public void setAuthors(List l) {this.authors = l;}
    
    public List getSelections() {return selections;}
    public void setSelections(List l) {this.selections = l;}
    
    public List getGrowths() {return growths;}
    public void setGrowths(List l) {this.growths=l;}
    
    public List getNames() {return names;}
    public void setNames(List l) {this.names=l;}
    
    public List getInserts() {return inserts;}
    public void setInserts(List l) {this.inserts = l;}
    
    public List getHosts() {return hosts;}
    public void setHosts(List l) {this.hosts = l;}
    
    public String getSynonymString() {
        StringConvertor sc = new StringConvertor();
        return sc.convertFromListToString(synonyms);
    }
    
    public Set getHosttypes() {        
        Set l = new TreeSet();
        
        if(selections == null) {
            return l;
        }
        
        for(int i=0; i<selections.size(); i++) {
            CloneSelection cs = (CloneSelection)selections.get(i);
            String hosttype = cs.getHosttype();
            l.add(hosttype);
        }
        
        return l;
    }
    
    public List getMarkers(String hosttype) {
        List l = new ArrayList();
        
        if(selections == null || hosttype == null) {
            return l;
        }
        
        for(int i=0; i<selections.size(); i++) {
            CloneSelection cs = (CloneSelection)selections.get(i);
            String ht = cs.getHosttype();
            
            if(hosttype.equals(ht)) {
                l.add(cs.getMarker());
            }
        }
        
        return l;
    }
    
    public String getMarkerString(String hosttype) {
        List markers = getMarkers(hosttype);
        StringConvertor cs = new StringConvertor();
        return cs.convertFromListToString(markers);
    }   
       
    public List getNamesByType(String nametype) {
        List l = new ArrayList();
        
        if(nametype == null || names == null) {
            return l;
        }
        
        for(int i=0; i<names.size(); i++) {
            CloneName cn = (CloneName)names.get(i);
            String nt = cn.getType();
            
            if(nametype.equals(nt)) {
                l.add(cn.getValue());
            }
        }
        
        return l;
    }
    
    public String getNameString(String nametype) {
        List namesByType = getNamesByType(nametype);
        StringConvertor cs = new StringConvertor();
        return cs.convertFromListToString(namesByType);
    }
}
