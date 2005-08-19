/*
 * ProcessExecution.java
 *
 * Created on May 31, 2005, 9:51 AM
 */

package plasmid.coreobject;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ProcessExecution {
    public static final String INPUT = "I";
    public static final String OUTPUT = "O";
    public static final String BOTH = "B";
    public static final String COMPLETE = "Complete";
    
    private int executionid;
    private String status;
    private String date;
    private String processname;
    private String researchername;
    private String protocolname;
    
    private List lineages;
    private List inputObjects;
    private List outputObjects;
    private List ioObjects;
    private List results;
    
    /** Creates a new instance of ProcessExecution */
    public ProcessExecution() {
    }
    
    public ProcessExecution(int executionid, String status, String date, String processname, String reseachername, String protocolname) {
        this.executionid = executionid;
        this.status = status;
        this.date = date;
        this.processname = processname;
        this.researchername = researchername;
        this.protocolname = protocolname;
    }
    
    public int getExecutionid() {return executionid;}
    public String getStatus() {return status;}
    public String getDate() {return date;}
    public String getProcessname() {return processname;}
    public String getResearchername() {return researchername;}
    public String getProtocolname() {return protocolname;}
    
    public void setExecutionid(int id) {this.executionid = id;}
    public void setStatus(String s) {this.status = s;}
    public void setDate(String s) {this.date = s;}
    public void setProcessname(String s) {this.processname = s;}
    public void setResearchername(String s) {this.researchername = s;}
    public void setProtocolname(String s) {this.protocolname = s;}
    
    public List getLineages() {return lineages;}
    public void setLineages(List l) {this.lineages = l;}
    
    public List getInputObjects() {return inputObjects;}
    public void setInputObjects(List l) {this.inputObjects = l;}
    
    public List getOutputObjects() {return outputObjects;}
    public void setOutputObjects(List l) {this.outputObjects = l;}
    
    public List getIoObjects() {return ioObjects;}
    public void setIoObjects(List l) {this.ioObjects = l;}
    
    public List getResults() {return results;}
    public void setResults(List l) {this.results = l;}
}
