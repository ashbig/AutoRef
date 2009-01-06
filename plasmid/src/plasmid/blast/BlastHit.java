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
public class BlastHit extends CloneInfo implements Serializable {
    private String queryid;
    private String subjectid;
    private double maxpid;
    private int maxpidLength;
    private int maxAlength;
    private double maxAlengthpid;
    private List blastinfos;

    public BlastHit() {
        super();
        this.blastinfos = new ArrayList();
    }
    
    public BlastHit(String queryid, String subjectid) {
        setQueryid(queryid);
        setSubjectid(subjectid);
        this.blastinfos = new ArrayList();
    }
    
    public void setCloneinfo(CloneInfo c) {
        this.setCloneid(c.getCloneid());
        this.setName(c.getName());
        this.setType(c.getType());
        this.setVerified(c.getVerified());
        this.setVermethod(c.getVermethod());
        this.setDomain(c.getDomain());
        this.setSubdomain(c.getSubdomain());
        this.setRestriction(c.getRestriction());
        this.setComments(c.getComments());
        this.setVectorid(c.getVectorid());
        this.setVectorname(c.getVectorname());
        this.setClonemap(c.getClonemap());
        this.setStatus(c.getStatus());
        this.setSpecialtreatment(c.getSpecialtreatment());
        this.setSource(c.getSource());
        this.setDescription(c.getDescription());
        this.setAuthors(c.getAuthors());
        this.setGrowths(c.getGrowths());
        this.setHosts(c.getHosts());
        this.setInserts(c.getInserts());
        this.setNames(c.getNames());
        this.setProperties(c.getProperties());
        this.setPublications(c.getPublications());
        this.setSelections(c.getSelections());
        this.setSynonyms(c.getSynonyms());
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
}
