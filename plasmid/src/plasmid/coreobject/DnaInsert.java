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
    private String geneid;
    private String name;
    private String description;
    private String targetseqid;
    private String targetgenbank;
    
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
        
    public DnaInsert(int insertid, int order, int size, String species, String format, String source, 
    int cloneid, String geneid, String name, String description, String targetseqid, String targetgenbank) {
        this.insertid=insertid;
        this.order=order;
        this.size=size;
        this.species=species;
        this.format=format;
        this.source=source;
        this.cloneid=cloneid;
        this.geneid = geneid;
        this.name = name;
        this.description = description;
        this.targetseqid = targetseqid;
        this.targetgenbank = targetgenbank;
    }
    
    public int getInsertid() {return insertid;}
    public int getOrder() {return order;}
    public int getSize() {return size;}
    public String getSpecies() {return species;}
    public String getFormat() {return format;}
    public String getSource() {return source;}
    public int getCloneid() {return cloneid;}
    public String getGeneid() {return geneid;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public String getTargetseqid() {return targetseqid;}
    public String getTargetgenbank() {return targetgenbank;}
    
    public void setInsertid(int id) {this.insertid = id;}
    public void setOrder(int order) {this.order = order;}
    public void setSize(int i) {this.size = i;}
    public void setSpecies(String s) {this.species = s;}
    public void setFormat(String s) {this.format = s;}
    public void setSource(String s) {this.source = s;}
    public void setCloneid(int cloneid) {this.cloneid=cloneid;}
    public void setGeneid(String id) {this.geneid = id;}
    public void setName(String s) {this.name = s;}
    public void setDescription(String s) {this.description = s;}
    public void setTargetseqid(String s) {this.targetseqid = s;}
    public void setTargetgenbank(String s) {this.targetgenbank = s;}
}
