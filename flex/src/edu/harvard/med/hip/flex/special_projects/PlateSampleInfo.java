/*
 * PlateSampleInfo.java
 *
 * Created on April 16, 2003, 3:47 PM
 */

package edu.harvard.med.hip.flex.special_projects;

/**
 *
 * @author  dzuo
 */
public class PlateSampleInfo {
    String sequence;
    String containerlabel;
    String well;
    String oligo5plabel;
    String oligo5pwell;
    String oligo3plabel;
    String oligo3pwell;
    int seqid;
    int containerid;
    int position;
    int oligo5p;
    int oligo3p;
    int oligo3f;
    int oligo3c;
    
    /** Creates a new instance of PlateSampleInfo */
    public PlateSampleInfo(String sequence, String containerlabel, String well,String oligo5plabel, String oligo5pwell,String oligo3plabel,String oligo3pwell) {
        this.sequence=sequence;
        this.containerlabel=containerlabel;
        this.well=well;
        this.oligo5plabel=oligo5plabel;
        this.oligo5pwell=oligo5pwell;
        this.oligo3plabel=oligo3plabel;
        this.oligo3pwell=oligo3pwell;
    }
    
    public void setSeqid(int id) {this.seqid = id;}
    public void setContainerid(int id) {this.containerid=id;}
    public void setPosition(int id) {this.position = id;}
    public void setOligo5p(int id) {this.oligo5p=id;}
    public void setOligo3p(int id) {this.oligo3p = id;}
    public void setOligo3f(int id) {this.oligo3f=id;}
    public void setOligo3c(int id) {this.oligo3c=id;}
    
    public String getSequence() {return sequence;}
    public String getLabel() {return containerlabel;}
    public String getWell() {return well;}
    public int getSeqid() {return seqid;}
    public int getContainerid() {return containerid;}
    public int getPosition() {return position;}
    public int getOligo5p() {return oligo5p;}
    public int getOligo3p() {return oligo3p;}
    public String getOligo5plabel() {return oligo5plabel;}
    public String getOligo5pwell() {return oligo5pwell;}
    public String getOligo3plabel() {return oligo3plabel;}
    public String getOligo3pwell() {return oligo3pwell;}
}
