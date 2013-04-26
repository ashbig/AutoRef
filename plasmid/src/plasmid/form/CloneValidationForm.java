/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author dongmei
 */
public class CloneValidationForm extends ActionForm {
    private String orderids;
    private String cloneids;
    private String cloneid;
    private String researcher;
    private List statusList;
    private List sequences;
    private List results;
    private List methods;
    private List readnames;
    private List reads;
    private List phreds;
    private List workflows;
    private String submit;
    private int alength;
    private double pid;
    private boolean displaySummary;

    public CloneValidationForm() {
        super();
        resetSequencesAndResults();
        alength=100;
        pid=90.0;
    }
    
    public void resetSequencesAndResults() {
        this.setStatusList(new ArrayList());
        this.setSequences(new ArrayList());
        this.setResults(new ArrayList());
        this.setMethods(new ArrayList());
        this.setReads(new ArrayList());
        this.setReadnames(new ArrayList());
        this.setPhreds(new ArrayList());
        this.setWorkflows(new ArrayList());
        setDisplaySummary(false);
    }

    public String getSequence(int i) {
        return (String)getSequences().get(i);
    }
    
    public void setSequence(int i, String s) {
        this.getSequences().set(i, s);
    }

    public String getStatus(int i) {
        return (String)getStatusList().get(i);
    }
    
    public void setStatus(int i, String s) {
        this.getStatusList().set(i, s);
    }
    
    public String getResult(int i) {
        return (String)getResults().get(i);
    }
    
    public void setResult(int i, String s) {
        this.getResults().set(i, s);
    }
    
    public String getMethod(int i) {
        return (String)getMethods().get(i);
    }
    
    public void setMethod(int i, String s) {
        this.getMethods().set(i, s);
    }
    
    public String getReadname(int i) {
        return (String)getReadnames().get(i);
    }
    
    public void setReadname(int i, String s) {
        this.getReadnames().set(i, s);
    }
    
    public String getRead(int i) {
        return (String)getReads().get(i);
    }
    
    public void setRead(int i, String s) {
        this.getReads().set(i, s);
    }
    
    public String getPhred(int i) {
        return (String)getPhreds().get(i);
    }
    
    public void setPhred(int i, String s) {
        this.getPhreds().set(i, s);
    }
    
    public String getWorkflow(int i) {
        return (String)getWorkflows().get(i);
    }
    
    public void setWorkflow(int i, String s) {
        this.getWorkflows().set(i, s);
    }

    /**
     * @return the cloneid
     */
    public String getCloneid() {
        return cloneid;
    }

    /**
     * @param cloneid the cloneid to set
     */
    public void setCloneid(String cloneid) {
        this.cloneid = cloneid;
    }

    /**
     * @return the researcher
     */
    public String getResearcher() {
        return researcher;
    }

    /**
     * @param researcher the researcher to set
     */
    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    /**
     * @return the sequences
     */
    public List getSequences() {
        return sequences;
    }

    /**
     * @param sequences the sequences to set
     */
    public void setSequences(List sequences) {
        this.sequences = sequences;
    }

    /**
     * @return the results
     */
    public List getResults() {
        return results;
    }

    /**
     * @param results the results to set
     */
    public void setResults(List results) {
        this.results = results;
    }

    /**
     * @return the methods
     */
    public List getMethods() {
        return methods;
    }

    /**
     * @param methods the methods to set
     */
    public void setMethods(List methods) {
        this.methods = methods;
    }

    /**
     * @return the submit
     */
    public String getSubmit() {
        return submit;
    }

    /**
     * @param submit the submit to set
     */
    public void setSubmit(String submit) {
        this.submit = submit;
    }

    /**
     * @return the alength
     */
    public int getAlength() {
        return alength;
    }

    /**
     * @param alength the alength to set
     */
    public void setAlength(int alength) {
        this.alength = alength;
    }

    /**
     * @return the pid
     */
    public double getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(double pid) {
        this.pid = pid;
    }

    /**
     * @return the cloneids
     */
    public String getCloneids() {
        return cloneids;
    }

    /**
     * @param cloneids the cloneids to set
     */
    public void setCloneids(String cloneids) {
        this.cloneids = cloneids;
    }

    /**
     * @return the readnames
     */
    public List getReadnames() {
        return readnames;
    }

    /**
     * @param readnames the readnames to set
     */
    public void setReadnames(List readnames) {
        this.readnames = readnames;
    }

    /**
     * @return the reads
     */
    public List getReads() {
        return reads;
    }

    /**
     * @param reads the reads to set
     */
    public void setReads(List reads) {
        this.reads = reads;
    }

    /**
     * @return the phreds
     */
    public List getPhreds() {
        return phreds;
    }

    /**
     * @param phreds the phreds to set
     */
    public void setPhreds(List phreds) {
        this.phreds = phreds;
    }

    /**
     * @return the orderids
     */
    public String getOrderids() {
        return orderids;
    }

    /**
     * @param orderids the orderids to set
     */
    public void setOrderids(String orderids) {
        this.orderids = orderids;
    }

    /**
     * @return the workflows
     */
    public List getWorkflows() {
        return workflows;
    }

    /**
     * @param workflows the workflows to set
     */
    public void setWorkflows(List workflows) {
        this.workflows = workflows;
    }

    /**
     * @return the statusList
     */
    public List getStatusList() {
        return statusList;
    }

    /**
     * @param statusList the statusList to set
     */
    public void setStatusList(List statusList) {
        this.statusList = statusList;
    }

    /**
     * @return the displaySummary
     */
    public boolean isDisplaySummary() {
        return displaySummary;
    }

    /**
     * @param displaySummary the displaySummary to set
     */
    public void setDisplaySummary(boolean displaySummary) {
        this.displaySummary = displaySummary;
    }
}
