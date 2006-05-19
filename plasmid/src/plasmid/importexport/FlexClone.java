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
    private String pcr;
    private String flor;
    private String protein;
    private String colony;
    private String restriction;
    private String label;
    private int position;
    private int plasmidCloneid;
    
    /** Creates a new instance of FlexClone */
    public FlexClone() {
    }
    
    public FlexClone(int cloneid, int mastercloneid, String vectorname, String clonename,
    String status, String comments, String species,String pcr, String flor,String protein,
    String colony, String restriction, String label, int position) {
        this.cloneid = cloneid;
        this.mastercloneid = mastercloneid;
        this.vectorname = vectorname;
        this.clonename = clonename;
        this.status = status;
        this.comments = comments;
        this.species = species;
        this.pcr = pcr;
        this.flor = flor;
        this.protein = protein;
        this.colony = colony;
        this.restriction = restriction;
        this.label = label;
        this.position = position;
    }
    
    public int getCloneid() {return cloneid;}
    public int getMastercloneid() {return mastercloneid;}
    public String getVectorname() {return vectorname;}
    public String getClonename() {return clonename;}
    public String getStatus() {return status;}
    public String getComments() {return comments;}
    public String getSpecies() {return species;}
    public String getPcr() {return pcr;}
    public String getFlor() {return flor;}
    public String getProtein() {return protein;}
    public String getColony() {return colony;}
    public String getRestriction() {return restriction;}
    public String getLabel() {return label;}
    public int getPosition() {return position;}
    public int getPlasmidCloneid() {return plasmidCloneid;}
    
    public void setPlasmidCloneid(int id) {this.plasmidCloneid = id;}
}
