/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;
import util.StringConvertor;

/**
 *
 * @author dongmei
 */
public class Refseq implements Serializable {

    private int id;
    private int geneid;
    private String symbol;
    private String status;
    private String accession;
    private String gi;
    private String proteinacc;
    private String proteingi;
    private String cds;
    private String proteinseq;
    private int cdslength;
    private int proteinlength;
    private int longest;
    private String sequence;
    private int hasSequence;
    private int hasCds;
    private int hasProteinseq;
    private int taxid;

    public Refseq() {
    }

    public Refseq(int id, int geneid, String symbol, String status, String accession, String gi, String proteinacc,
            String proteingi, String cds, String proteinseq, int cdslength, int proteinlength, int longest) {
        this.id = id;
        this.geneid = geneid;
        this.symbol = symbol;
        this.status = status;
        this.accession = accession;
        this.gi = gi;
        this.proteinacc = proteinacc;
        this.proteingi = proteingi;
        this.cds = cds;
        this.proteinseq = proteinseq;
        this.cdslength = cdslength;
        this.proteinlength = proteinlength;
        this.longest = longest;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
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
     * @return the gi
     */
    public String getGi() {
        return gi;
    }

    /**
     * @param gi the gi to set
     */
    public void setGi(String gi) {
        this.gi = gi;
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
    public String getProteingi() {
        return proteingi;
    }

    /**
     * @param proteingi the proteingi to set
     */
    public void setProteingi(String proteingi) {
        this.proteingi = proteingi;
    }

    /**
     * @return the cds
     */
    public String getCds() {
        if (cds != null) {
            return cds.toUpperCase();
        }
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
        if (proteinseq != null) {
            return proteinseq.toUpperCase();
        }
        return proteinseq;
    }

    /**
     * @param proteinseq the proteinseq to set
     */
    public void setProteinseq(String proteinseq) {
        this.proteinseq = proteinseq;
    }

    /**
     * @return the cdslength
     */
    public int getCdslength() {
        return cdslength;
    }

    /**
     * @param cdslength the cdslength to set
     */
    public void setCdslength(int cdslength) {
        this.cdslength = cdslength;
    }

    /**
     * @return the proteinlength
     */
    public int getProteinlength() {
        return proteinlength;
    }

    /**
     * @param proteinlength the proteinlength to set
     */
    public void setProteinlength(int proteinlength) {
        this.proteinlength = proteinlength;
    }

    /**
     * @return the longest
     */
    public int getLongest() {
        return longest;
    }

    /**
     * @param longest the longest to set
     */
    public void setLongest(int longest) {
        this.longest = longest;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLongestString() {
        if (longest == 1) {
            return "Yes";
        }
        return "No";
    }

    public boolean isBooleanLongest() {
        return (longest == 1);
    }

    public String getCdsFasta() {
        try {
            return StringConvertor.convertToFasta(getCds(), 0);
        } catch (Exception ex) {
            return "";
        }
    }

    public String getProteinseqFasta() {
        try {
            return StringConvertor.convertToFasta(getProteinseq(), 0);
        } catch (Exception ex) {
            return "";
        }
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
     * @return the hasSequence
     */
    public int getHasSequence() {
        return hasSequence;
    }

    /**
     * @param hasSequence the hasSequence to set
     */
    public void setHasSequence(int hasSequence) {
        this.hasSequence = hasSequence;
    }

    /**
     * @return the hasCds
     */
    public int getHasCds() {
        return hasCds;
    }

    /**
     * @param hasCds the hasCds to set
     */
    public void setHasCds(int hasCds) {
        this.hasCds = hasCds;
    }

    /**
     * @return the hasProteinseq
     */
    public int getHasProteinseq() {
        return hasProteinseq;
    }

    /**
     * @param hasProteinseq the hasProteinseq to set
     */
    public void setHasProteinseq(int hasProteinseq) {
        this.hasProteinseq = hasProteinseq;
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
}
