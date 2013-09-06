/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package blast;

import java.io.Serializable;

/**
 *
 * @author DZuo
 */
public class BlastInfo implements Serializable {
    private String queryid;
    private String subjectid;
    private double pid;
    private int alength;
    private int mismatch;
    private int gap;
    private int qstart;
    private int qend;
    private int sstart;
    private int send;
    private double evalue;
    private double score;
    
    public BlastInfo() {}
    
    public BlastInfo(String queryid, String subid, double pid, int length,
            int mismatch, int gap, int qstart, int qend, int sstart, int send,
            double evalue, double score) {
        this.queryid = queryid;
        this.subjectid = subid;
        this.pid = pid;
        this.alength = length;
        this.mismatch = mismatch;
        this.gap = gap;
        this.qstart = qstart;
        this.qend = qend;
        this.sstart = sstart;
        this.send = send;
        this.evalue = evalue;
        this.score = score;
    }

    public String getQueryid() {
        return queryid;
    }

    public void setQueryid(String queryid) {
        this.queryid = queryid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public double getPid() {
        return pid;
    }

    public void setPid(double pid) {
        this.pid = pid;
    }

    public int getAlength() {
        return alength;
    }

    public void setAlength(int alength) {
        this.alength = alength;
    }

    public int getMismatch() {
        return mismatch;
    }

    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public int getQstart() {
        return qstart;
    }

    public void setQstart(int qstart) {
        this.qstart = qstart;
    }

    public int getQend() {
        return qend;
    }

    public void setQend(int qend) {
        this.qend = qend;
    }

    public int getSstart() {
        return sstart;
    }

    public void setSstart(int sstart) {
        this.sstart = sstart;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public double getEvalue() {
        return evalue;
    }

    public void setEvalue(double evalue) {
        this.evalue = evalue;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
