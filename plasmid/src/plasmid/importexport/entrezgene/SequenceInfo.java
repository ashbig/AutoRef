/*
 * SequenceInfo.java
 *
 * Created on February 20, 2007, 11:07 AM
 */

package plasmid.importexport.entrezgene;

/**
 *
 * @author  DZuo
 */
public class SequenceInfo {
    private String accession;
    private String gi;
    private String paccession;
    private String pgi;
    private String type;
    private int geneid;
    
    /** Creates a new instance of SequenceInfo */
    public SequenceInfo() {
    }
    
    public SequenceInfo(String accession, String gi, String paccession, String pgi, String type, int geneid) {
        this.accession = accession;
        this.gi = gi;
        this.paccession = paccession;
        this.pgi = pgi;
        this.type = type;
        this.geneid = geneid;
    }
   
    public String getAccession() {return accession;}
    public String getGi() {return gi;}
    public String getPaccession() {return paccession;}
    public String getPgi() {return pgi;}
    public String getType() {return type;}
    public int getGeneid() {return geneid;}
}
