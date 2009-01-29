/*
 * ResultTO.java
 *
 * Created on December 5, 2007, 11:04 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class ResultTO implements Serializable {
    public static final String TYPE_CULTURE = "Culture";
    public static final String TYPE_DNA = "DNA";
    public static final String TYPE_MICROVIGENE = "MicroVigene";
    
    private int resultid;
    private String type;
    private String value;
    private int executionid;
    private int sampleid;
    
    private SampleTO sample;
    
    /** Creates a new instance of ResultTO */
    public ResultTO() {
    }

    public ResultTO(int resultid, String type, String value, int executionid, int sampleid) {
        this.setResultid(resultid);
        this.setType(type);
        this.setValue(value);
        this.setExecutionid(executionid);
        this.setSampleid(sampleid);
    }
    
    public int getResultid() {
        return resultid;
    }

    public void setResultid(int resultid) {
        this.resultid = resultid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getExecutionid() {
        return executionid;
    }

    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }

    public int getSampleid() {
        return sampleid;
    }

    public void setSampleid(int sampleid) {
        this.sampleid = sampleid;
    }

    public SampleTO getSample() {
        return sample;
    }

    public void setSample(SampleTO sample) {
        this.sample = sample;
    }
    
}
