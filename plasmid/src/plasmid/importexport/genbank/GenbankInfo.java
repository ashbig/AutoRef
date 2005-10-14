/*
 * GenbankInfo.java
 *
 * Created on August 3, 2005, 12:28 PM
 */

package plasmid.importexport.genbank;

/**
 *
 * @author  DZuo
 */
public class GenbankInfo {
    private String definition;
    private String accession;
    private String gi;
    private String organism;
    private String geneid;
    private String genesymbol;
    private int cdsstart;
    private int cdsstop;
    private String sequencetext;
    
    /** Creates a new instance of GenbankInfo */
    public GenbankInfo() {
    }
    
    public GenbankInfo(String definition, String accession, String gi, String organism, 
    String geneid, String genesymbol, int cdsstart, int cdsstop, String sequencetext) {
        this.definition = definition;
        this.accession = accession;
        this.gi = gi;
        this.organism = organism;
        this.geneid = geneid;
        this.genesymbol = genesymbol;
        this.cdsstart = cdsstart;
        this.cdsstop = cdsstop;
        this.sequencetext = sequencetext;
    }
    
    public String getDefinition() {return definition;}
    public String getAccession() {return accession;}
    public String getGi() {return gi;}
    public String getOrganism() {return organism;}
    public String getGeneid() {return geneid;}
    public String getGenesymbol() {return genesymbol;}
    public int getCdsstart() {return cdsstart;}
    public int getCdsstop() {return cdsstop;}
    public String getSequencetext() {return sequencetext;}
}
