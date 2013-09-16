/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core;

import java.io.Serializable;

/**
 *
 * @author dongmei
 */
public class TemplateAnalysis implements Serializable {

    private int id;
    private int refseqid;
    private int templateid;
    private String analysistype;
    private double pid;
    private int alength;
    private int mismatch;
    private int gap;
    private double evalue;
    private double score;
    private int mutation;
    private double coverage;
    private String blastOutput;

    private Refseq refseq;
    private Template template;
    
    public TemplateAnalysis() {}
    
    public TemplateAnalysis(int id, int refseqid, int templateid, String analysistype, double pid, int alength,
            int mismatch, int gap, double evalue, double score, int mutation, double coverage) {
        this.id = id;
        this.refseqid = refseqid;
        this.templateid = templateid;
        this.analysistype = analysistype;
        this.pid = pid;
        this.alength = alength;
        this.mismatch = mismatch;
        this.gap = gap;
        this.evalue = evalue;
        this.score = score;
        this.mutation = mutation;
        this.coverage = coverage;
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
     * @return the refseqid
     */
    public int getRefseqid() {
        return refseqid;
    }

    /**
     * @param refseqid the refseqid to set
     */
    public void setRefseqid(int refseqid) {
        this.refseqid = refseqid;
    }

    /**
     * @return the templateid
     */
    public int getTemplateid() {
        return templateid;
    }

    /**
     * @param templateid the templateid to set
     */
    public void setTemplateid(int templateid) {
        this.templateid = templateid;
    }

    /**
     * @return the pid
     */
    public double getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(double pid) {
        this.pid = pid;
    }

    /**
     * @return the alength
     */
    public int getAlength() {
        return alength;
    }

    /**
     * @param alength the alength to set
     */
    public void setAlength(int alength) {
        this.alength = alength;
    }

    /**
     * @return the mismatch
     */
    public int getMismatch() {
        return mismatch;
    }

    /**
     * @param mismatch the mismatch to set
     */
    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    /**
     * @return the gap
     */
    public int getGap() {
        return gap;
    }

    /**
     * @param gap the gap to set
     */
    public void setGap(int gap) {
        this.gap = gap;
    }

    /**
     * @return the evalue
     */
    public double getEvalue() {
        return evalue;
    }

    /**
     * @param evalue the evalue to set
     */
    public void setEvalue(double evalue) {
        this.evalue = evalue;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @return the mutation
     */
    public int getMutation() {
        return mutation;
    }

    /**
     * @param mutation the mutation to set
     */
    public void setMutation(int mutation) {
        this.mutation = mutation;
    }

    /**
     * @return the coverage
     */
    public double getCoverage() {
        return coverage;
    }

    /**
     * @param coverage the coverage to set
     */
    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    /**
     * @return the refseq
     */
    public Refseq getRefseq() {
        return refseq;
    }

    /**
     * @param refseq the refseq to set
     */
    public void setRefseq(Refseq refseq) {
        this.refseq = refseq;
    }

    /**
     * @return the template
     */
    public Template getTemplate() {
        return template;
    }

    /**
     * @param template the template to set
     */
    public void setTemplate(Template template) {
        this.template = template;
    }

    /**
     * @return the analysistype
     */
    public String getAnalysistype() {
        return analysistype;
    }

    /**
     * @param analysistype the analysistype to set
     */
    public void setAnalysistype(String analysistype) {
        this.analysistype = analysistype;
    }

    /**
     * @return the blastOutput
     */
    public String getBlastOutput() {
        return blastOutput;
    }

    /**
     * @param blastOutput the blastOutput to set
     */
    public void setBlastOutput(String blastOutput) {
        this.blastOutput = blastOutput;
    }
}
