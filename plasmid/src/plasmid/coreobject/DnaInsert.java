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
    private String name;
    private String description;
    private String species;
    private String format;
    private String source;
    private int cloneid;
    
    /** Creates a new instance of DnaInsert */
    public DnaInsert() {
    }
    
    public DnaInsert(int insertid, int order, int size, String name, String description,
    String species, String format, String source, int cloneid) {
        this.insertid=insertid;
        this.order=order;
        this.size=size;
        this.name=name;
        this.description=description;
        this.species=species;
        this.format=format;
        this.source=source;
        this.cloneid=cloneid;
    }
    
    public int getInsertid() {return insertid;}
    public int getOrder() {return order;}
    public int getSize() {return size;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getSpecies() {return species;}
    public String getFormat() {return format;}
    public String getSource() {return source;}
    public int getCloneid() {return cloneid;}
    
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
}
