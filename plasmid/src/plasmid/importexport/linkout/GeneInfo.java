/*
 * GeneInfo.java
 *
 * Created on February 26, 2007, 11:56 AM
 */

package plasmid.importexport.linkout;

/**
 *
 * @author  DZuo
 */
public class GeneInfo {
    private String geneid;
    private String species;
    private String gi;
    
    /** Creates a new instance of GeneInfo */
    public GeneInfo() {
    }
 
    public GeneInfo(String geneid, String species) {
        this.geneid=geneid;
        this.species=species;
    }
    
    public String getGeneid() {return geneid;}
    public String getSpecies() {return species;}
    public String getHtmlSpecies() {
        return species.replaceAll(" ", "%20");
    }
    
    public String getGi() {return gi;}
    public void setGi(String s) {this.gi = s;}
}
