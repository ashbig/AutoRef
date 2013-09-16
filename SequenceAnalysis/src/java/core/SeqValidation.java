/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;

/**
 *
 * @author Lab User
 */
public class SeqValidation implements Serializable {
    public static final String RESULT_MATCH = "Match";
    public static final String RESULT_LOWSCORE = "Low PHRED";
    public static final String RESULT_NOMATCH = "No match";
    public static final String RESULT_MANUAL = "Manual";
    
    private String result;
    private String subjectid;
    private double maxpid;
    private int maxpidalength;
    private int maxalength;
    private double maxalengthpid;
    private String accession;
    private String geneid;
    private String symbol;
    private String synonyms;

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * @return the subjectid
     */
    public String getSubjectid() {
        return subjectid;
    }

    /**
     * @param subjectid the subjectid to set
     */
    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    /**
     * @return the maxpid
     */
    public double getMaxpid() {
        return maxpid;
    }

    /**
     * @param maxpid the maxpid to set
     */
    public void setMaxpid(double maxpid) {
        this.maxpid = maxpid;
    }

    /**
     * @return the maxpidalength
     */
    public int getMaxpidalength() {
        return maxpidalength;
    }

    /**
     * @param maxpidalength the maxpidalength to set
     */
    public void setMaxpidalength(int maxpidalength) {
        this.maxpidalength = maxpidalength;
    }

    /**
     * @return the maxalength
     */
    public int getMaxalength() {
        return maxalength;
    }

    /**
     * @param maxalength the maxalength to set
     */
    public void setMaxalength(int maxalength) {
        this.maxalength = maxalength;
    }

    /**
     * @return the maxalengthpid
     */
    public double getMaxalengthpid() {
        return maxalengthpid;
    }

    /**
     * @param maxalengthpid the maxalengthpid to set
     */
    public void setMaxalengthpid(double maxalengthpid) {
        this.maxalengthpid = maxalengthpid;
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
    public String getGeneid() {
        return geneid;
    }

    /**
     * @param geneid the geneid to set
     */
    public void setGeneid(String geneid) {
        this.geneid = geneid;
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

    /**
     * @return the synonyms
     */
    public String getSynonyms() {
        return synonyms;
    }

    /**
     * @param synonyms the synonyms to set
     */
    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }
}
