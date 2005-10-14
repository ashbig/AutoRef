/*
 * Refseq.java
 *
 * Created on April 7, 2005, 3:00 PM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class Refseq {
    private int refseqid;
    private String type;
    private String name;
    private String description;
    private int cdsstart;
    private int cdsstop;
    private String species;

    private List names;
    
    /** Creates a new instance of Refseq */
    public Refseq() {
    }
    
    public Refseq(int refseqid, String type, String name, String description,
    int cdsstart, int cdsstop, String species) {
        this.refseqid = refseqid;
        this.type = type;
        this.name = name;
        this.description = description;
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.species = species;
    }
    
    public int getRefseqid() {return refseqid;}
    public String getType() {return type;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public int getCdsstart() {return cdsstart;}
    public int getCdsstop() {return cdsstop;}
    public String getSpecies() {return species;}
    
    public void setRefseqid(int id) {this.refseqid = id;}
    public void setType(String s) {this.type = s;}
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setCdsstart(int i) {this.cdsstart = i;}
    public void setCdsstop(int i) {this.cdsstop = i;}
    public void setSpecies(String s) {this.species = s;}
    
    public List getNames() {return names;}
    public void setNames(List l) {this.names = names;}
    
    public List getNamesByType(String nametype) {
        List l = new ArrayList();
        
        if(names == null || nametype == null) 
            return l;
        
        for(int i=0; i<l.size(); i++) {
            RefseqName name = (RefseqName)l.get(i);
            if(nametype.equals(name.getNametype())) {
                l.add(name);
            }
        }
        
        return l;
    }
}
