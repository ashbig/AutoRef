/*
 * Clone.java
 *
 * Created on March 31, 2005, 4:03 PM
 */

package plasmid.coreobject;

import java.util.*;
import plasmid.util.StringConvertor;

/**
 *
 * @author  DZuo
 */
public class Clone {
    private int cloneid;
    private String name;
    private String type;
    private String originalid;
    private String verified;
    private String vermethod;
    private String domain;
    private String subdomain;
    private String restriction;
    private int vectorid;
    private String vectorname;
    
    private List synonyms;
    private List publications;
    private List authors;
    private List selections;
    private List growths;
    private List names;
    
    /** Creates a new instance of Clone */
    public Clone() {
    }
    
    public Clone(int cloneid, String name, String type, String originalid, String verified,
    String vermethod, String domain, String subdomain, String restriction, int vectorid, String vectorname) {
        this.cloneid = cloneid;
        this.name = name;
        this.type = type;
        this.originalid = originalid;
        this.verified = verified;
        this.vermethod = vermethod;
        this.domain = domain;
        this.subdomain = subdomain;
        this.restriction = restriction;
        this.vectorid = vectorid;
        this.vectorname = vectorname;
    }
    
    public int getCloneid() {return cloneid;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getOriginalid() {return originalid;}
    public String getVerified() {return verified;}
    public String getVermethod() {return vermethod;}
    public String getDomain() {return domain;}
    public String getSubdomain() {return subdomain;}
    public String getRestriction() {return restriction;}
    public int getVectorid() {return vectorid;}
    public String getVectorname() {return vectorname;}
    
    public void setVectorid(int id) {this.vectorid = id;}
    
    public List getSynonyms() {return synonyms;}
    public void setSynonyms(List l) {this.synonyms = l;}
   
    public List getPublications() {return publications;}
    public void setPublications(List l) {this.publications = l;}
    
    public List getAuthors() {return authors;}
    public void setAuthors(List l) {this.authors = authors;}
    
    public List getSelections() {return selections;}
    public void setSelections(List l) {this.selections = l;}
    
    public List getGrowths() {return growths;}
    public void setGrowths(List l) {this.growths=l;}
    
    public List getNames() {return names;}
    public void setNames(List l) {this.names=l;}
    
    public String getSynonymString() {
        StringConvertor sc = new StringConvertor();
        return sc.converFromListToString(synonyms);
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
        return cs.converFromListToString(markers);
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
        return cs.converFromListToString(namesByType);
    }
}
