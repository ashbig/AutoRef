/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.coreobject;

import java.io.Serializable;

/**
 *
 * @author Lab User
 */
public class Gene2Refseq implements Serializable {
    private int taxid;
    private String accession;
    private int geneid;
    private int gi;
    private String status;
    private String proteinacc;
    private int proteingi;
    private String genomicacc;
    private int genomicgi;
    private int start;
    private int end;
    private String orientation;
    private String assembly;
    private String sequence;
    private String cds;
    private String proteinseq;
    private int cloneCount;

    public int getSeqLength() {
        if(sequence != null)
            return sequence.length();
        return 0;
    }
    
    public int getCdsLength() {
        if(cds != null)
            return cds.length();
        return 0;
    }
    
    public int getProteinLength() {
        if(proteinseq != null)
            return proteinseq.length();
        return 0;
    }
    
    /**
     * @return the taxid
     */
    public int getTaxid() {
        return taxid;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(int taxid) {
        this.taxid = taxid;
    }

    /**
     * @return the accession
     */
    public String getAccession() {
        return accession;
    }

    /**
     * @param accession the accession to set
     */
    public void setAccession(String accession) {
        this.accession = accession;
    }

    /**
     * @return the geneid
     */
    public int getGeneid() {
        return geneid;
    }

    /**
     * @param geneid the geneid to set
     */
    public void setGeneid(int geneid) {
        this.geneid = geneid;
    }

    /**
     * @return the gi
     */
    public int getGi() {
        return gi;
    }

    /**
     * @param gi the gi to set
     */
    public void setGi(int gi) {
        this.gi = gi;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the proteinacc
     */
    public String getProteinacc() {
        return proteinacc;
    }

    /**
     * @param proteinacc the proteinacc to set
     */
    public void setProteinacc(String proteinacc) {
        this.proteinacc = proteinacc;
    }

    /**
     * @return the proteingi
     */
    public int getProteingi() {
        return proteingi;
    }

    /**
     * @param proteingi the proteingi to set
     */
    public void setProteingi(int proteingi) {
        this.proteingi = proteingi;
    }

    /**
     * @return the genomicacc
     */
    public String getGenomicacc() {
        return genomicacc;
    }

    /**
     * @param genomicacc the genomicacc to set
     */
    public void setGenomicacc(String genomicacc) {
        this.genomicacc = genomicacc;
    }

    /**
     * @return the genomicgi
     */
    public int getGenomicgi() {
        return genomicgi;
    }

    /**
     * @param genomicgi the genomicgi to set
     */
    public void setGenomicgi(int genomicgi) {
        this.genomicgi = genomicgi;
    }

    /**
     * @return the start
     */
    public int getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(int start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public int getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * @return the orientation
     */
    public String getOrientation() {
        return orientation;
    }

    /**
     * @param orientation the orientation to set
     */
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    /**
     * @return the assembly
     */
    public String getAssembly() {
        return assembly;
    }

    /**
     * @param assembly the assembly to set
     */
    public void setAssembly(String assembly) {
        this.assembly = assembly;
    }

    /**
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the cds
     */
    public String getCds() {
        return cds;
    }

    /**
     * @param cds the cds to set
     */
    public void setCds(String cds) {
        this.cds = cds;
    }

    /**
     * @return the proteinseq
     */
    public String getProteinseq() {
        return proteinseq;
    }

    /**
     * @param proteinseq the proteinseq to set
     */
    public void setProteinseq(String proteinseq) {
        this.proteinseq = proteinseq;
    }

    /**
     * @return the cloneCount
     */
    public int getCloneCount() {
        return cloneCount;
    }

    /**
     * @param cloneCount the cloneCount to set
     */
    public void setCloneCount(int cloneCount) {
        this.cloneCount = cloneCount;
    }
}
