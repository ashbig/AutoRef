/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.coreobject;

/**
 *
 * @author DZuo
 */
public class OrderCloneValidation {
    public static final String RESULT_PASS = "Pass";
    public static final String RESULT_FAIL_LOWSCORE = "Fail: Low PHRED";
    public static final String RESULT_FAIL_MISMATCH = "Fail: Mismatch";
    public static final String RESULT_MANUAL = "Manual";
    public static final String RESULT_MANUAL_NO_CLONE_SEQ = "Manual: No Clone Sequence";
    public static final String RESULT_MANUAL_NO_READ_SEQ = "Manual: No Read Sequence";
    public static final String WORKFLOW_INITIAL = "Initial validation";
    public static final String WORKFLOW_TROUBLESHOOTING = "Trouble shooting";
    
    private int orderid;
    private int cloneid;
    private String method;
    private String sequence;
    private String result;
    private String who;
    private String when;
    private int userid;
    private int phred;
    private String readname;
    private String read;
    private String clonename;
    private String workflow;

    private OrderClones orderclone;
    
    public OrderCloneValidation() {}
    
    public OrderCloneValidation(OrderClones c) {
        orderclone = c;
        this.orderid = c.getOrderid();
        this.cloneid = c.getCloneid();
    }
    
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getOrderid() {
        return orderid;
    }

    public void setOrderid(int orderid) {
        this.orderid = orderid;
    }

    public int getCloneid() {
        return cloneid;
    }

    public void setCloneid(int cloneid) {
        this.cloneid = cloneid;
    }

    public OrderClones getOrderclone() {
        return orderclone;
    }

    public void setOrderclone(OrderClones orderclone) {
        this.orderclone = orderclone;
    }

    /**
     * @return the phred
     */
    public int getPhred() {
        return phred;
    }

    /**
     * @param phred the phred to set
     */
    public void setPhred(int phred) {
        this.phred = phred;
    }

    /**
     * @return the readname
     */
    public String getReadname() {
        return readname;
    }

    /**
     * @param readname the readname to set
     */
    public void setReadname(String readname) {
        this.readname = readname;
    }

    /**
     * @return the read
     */
    public String getRead() {
        if(read==null || read.length()==0)
            return sequence;
        return read;
    }
    
    public String getReadForWeb() {
        String s = getRead();
        if(s==null || s.trim().length()==0)
            s = "NA";
        return s;
    }

    /**
     * @param read the read to set
     */
    public void setRead(String read) {
        this.read = read;
    }

    /**
     * @return the clonename
     */
    public String getClonename() {
        return clonename;
    }

    /**
     * @param clonename the clonename to set
     */
    public void setClonename(String clonename) {
        this.clonename = clonename;
    }

    /**
     * @return the workflow
     */
    public String getWorkflow() {
        return workflow;
    }
    
    public String getWorkflowStringForWeb() {
        if(workflow==null || workflow.trim().length()==0)
            workflow="NA";
        return workflow;
    }

    /**
     * @param workflow the workflow to set
     */
    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }
}
