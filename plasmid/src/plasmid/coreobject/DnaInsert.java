/*
 * DnaInsert.java
 *
 * Created on April 1, 2005, 2:13 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class DnaInsert {
    private int insertid;
    private int order;
    private int size;
    private String species;
    private String format;
    private String source;
    private int cloneid;
    
    /** Creates a new instance of DnaInsert */
    public DnaInsert() {
    }
    
    public DnaInsert(int insertid, int order, int size,
    String species, String format, String source, int cloneid) {
        this.insertid=insertid;
        this.order=order;
        this.size=size;
        this.species=species;
        this.format=format;
        this.source=source;
        this.cloneid=cloneid;
    }
    
    public int getInsertid() {return insertid;}
    public int getOrder() {return order;}
    public int getSize() {return size;}
    public String getSpecies() {return species;}
    public String getFormat() {return format;}
    public String getSource() {return source;}
    public int getCloneid() {return cloneid;}
    
    public void setInsertid(int id) {this.insertid = id;}
    public void setOrder(int order) {this.order = order;}
    public void setSize(int i) {this.size = i;}
    public void setSpecies(String s) {this.species = s;}
    public void setFormat(String s) {this.format = s;}
    public void setSource(String s) {this.source = s;}
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
}
