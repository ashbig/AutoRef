/*
 * WorklistInfo.java
 *
 * Created on November 3, 2005, 9:40 AM
 */

package plasmid.coreobject;

/**
 *
 * @author  DZuo
 */
public class WorklistInfo {
    public static final int NOTCOMMIT = 0;
    public static final int COMMIT = 1;
    public static final String YES = "Y";
    public static final String NO = "N";
    
    private int worklistid;
    private String worklistname;
    private String researchername;
    private String processname;
    private String protocolname;
    private int status;
    private String tube;
   
    /** Creates a new instance of WorklistInfo */
    public WorklistInfo() {
    }
    
    public WorklistInfo(int worklistid, String worklistname, String researchername, String processname, String protocolname, int status, String tube) {
        this.worklistid = worklistid;
        this.worklistname = worklistname;
        this.researchername = researchername;
        this.processname = processname;
        this.protocolname = protocolname;
        this.status = status;
        this.tube = tube;
    }
    
    public int getWorklistid() {return worklistid;}
    public String getWorklistname() {return worklistname;}
    public String getResearchername() {return researchername;}
    public String getProcessname() {return processname;}
    public String getProtocolname() {return protocolname;}
    public int getStatus() {return status;}
    public String getTube() {return tube;}
    
    public void setWorklistid(int id) {this.worklistid = id;}
    public void setWorklistname(String s) {this.worklistname = s;}
    public void setResearchername(String s) {this.researchername = s;}
    public void setProcessname(String s) {this.processname = s;}
    public void setProtocolname(String s) {this.protocolname = s;}
    public void setStatus(int i) {this.status = i;}
    public void setTube(String s) {this.tube = s;}
}