/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.blast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import plasmid.query.coreobject.CloneInfo;

/**
 *
 * @author DZuo
 */
public class BlastHit implements Serializable {
    private String queryid;
    private String subjectid;
    private double maxpid;
    private int maxpidLength;
    private int maxAlength;
    private double maxAlengthpid;
    private List blastinfos;

    private CloneInfo cloneinfo;

    public BlastHit() {
        this.blastinfos = new ArrayList();
    }
    
    public BlastHit(String queryid, String subjectid) {
        setQueryid(queryid);
        setSubjectid(subjectid);
        this.blastinfos = new ArrayList();
    }
    
    public void addBlastinfo(BlastInfo info) {
        blastinfos.add(info);
    }
    
    public void calculateSummary() {
        maxpid = 0.0;
        maxAlength = 0;
        
        for(int i=0; i<getBlastinfos().size(); i++) {
            BlastInfo info = (BlastInfo)getBlastinfos().get(i);
            double pid = info.getPid();
            int length = info.getAlength();
            if(pid>maxpid) {
                maxpid = pid;
                maxpidLength = length; 
            }
            if(length>maxAlength) {
                maxAlength = length;
                maxAlengthpid = pid;
            }
        }
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

    public double getMaxpid() {
        return maxpid;
    }

    public void setMaxpid(double maxpid) {
        this.maxpid = maxpid;
    }

    public List getBlastinfos() {
        return blastinfos;
    }

    public void setBlastinfos(List blastinfos) {
        this.blastinfos = blastinfos;
    }

    public int getMaxpidLength() {
        return maxpidLength;
    }

    public void setMaxpidLength(int maxpidLength) {
        this.maxpidLength = maxpidLength;
    }

    public int getMaxAlength() {
        return maxAlength;
    }

    public void setMaxAlength(int maxAlength) {
        this.maxAlength = maxAlength;
    }

    public double getMaxAlengthpid() {
        return maxAlengthpid;
    }

    public void setMaxAlengthpid(double maxAlengthpid) {
        this.maxAlengthpid = maxAlengthpid;
    }

    public CloneInfo getCloneinfo() {
        return cloneinfo;
    }

    public void setCloneinfo(CloneInfo cloneinfo) {
        this.cloneinfo = cloneinfo;
    }
    
}
