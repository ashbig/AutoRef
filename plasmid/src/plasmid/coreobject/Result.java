/*
 * Result.java
 *
 * Created on May 31, 2005, 11:14 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class Result {
    public static final String AGAR = "AGAR";
    public static final String CULTURE = "CULTURE";
    public static final String GROW = "Grow";
    public static final String NOTGROW = "Not Grow";
    public static final String WEAKGROW = "Weak Grow";
    
    private int resultid;
    private int sampleid;
    private int executionid;
    private String resulttype;
    private String resultvalue;
    
    /** Creates a new instance of Result */
    public Result() {
    }
 
    public Result(int resultid, int sampleid, int executionid, String resulttype, String resultvalue) {
        this.resultid = resultid;
        this.sampleid = sampleid;
        this.executionid = executionid;
        this.resulttype = resulttype;
        this.resultvalue = resultvalue;
    }
    
    public int getResultid() {return resultid;}
    public int getSampleid() {return sampleid;}
    public int getExecutionid() {return executionid;}
    public String getResulttype() {return resulttype;}
    public String getResultvalue() {return resultvalue;}
    
    public void setResultid(int id) {this.resultid = id;}
    public void setSampleid(int id) {this.sampleid = id;}
    public void setExecutionid(int id) {this.executionid = id;}
    public void setResulttype(String s) {this.resulttype = s;}
    public void setResultvalue(String s) {this.resultvalue = s;}
}
