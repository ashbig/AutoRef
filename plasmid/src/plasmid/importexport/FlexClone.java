/*
 * FlexClone.java
 *
 * Created on May 17, 2006, 12:57 PM
 */

package plasmid.importexport;

/**
 *
 * @author  DZuo
 */
public class FlexClone {
    private int cloneid;
    private String clonename;
    private int mastercloneid;
    private String status;
    private String comments;
    private String vectorname;
    private String species;
    
    /** Creates a new instance of FlexClone */
    public FlexClone() {
    }
    
    public FlexClone(int cloneid, int mastercloneid, String vectorname, String clonename,
    String status, String comments, String species) {
        this.cloneid = cloneid;
        this.mastercloneid = mastercloneid;
        this.vectorname = vectorname;
        this.clonename = clonename;
        this.status = status;
        this.comments = comments;
        this.species = species;
    }
    
    public int getCloneid() {return cloneid;}
    public int getMastercloneid() {return mastercloneid;}
    public String getVectorname() {return vectorname;}
    public String getClonename() {return clonename;}
    public String getStatus() {return status;}
    public String getComments() {return comments;}
    public String getSpecies() {return species;}
}
