/*
 * SampleLineage.java
 *
 * Created on May 24, 2005, 2:36 PM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class SampleLineage {
    private int executionid;
    private int from;
    private int to;
    private Sample sampleFrom;
    private Sample sampleTo;
    
    /** Creates a new instance of SampleLineage */
    public SampleLineage() {
    }
    
    public SampleLineage(int executionid, int from, int to) {
        this.executionid = executionid;
        this.from = from;
        this.to = to;
    }
    
    public SampleLineage(Sample sfrom, Sample sto) {
        this.sampleFrom = sfrom;
        this.sampleTo = sto;
    }
    
    public int getExecutionid() {return executionid;}
    public int getFrom() {return from;}
    public int getTo() {return to;}
    
    public void setExecutionid(int id) {this.executionid = id;}
    public void setFrom(int i) {this.from = i;}
    public void setTo(int i) {this.to = i;}
    
    public Sample getSampleFrom() {return sampleFrom;}
    public Sample getSampleTo() {return sampleTo;}
}
