/*
 * QueryInfo.java
 *
 * Created on September 9, 2002, 4:01 PM
 */

package edu.harvard.med.hip.flex.query;

/**
 *
 * @author  dzuo
 * @version 
 */
public class QueryInfo {
    protected int id;
    protected String gi;
    protected String geneName;
    protected String genbankAcc;
    protected String geneSymbol;
    protected String panumber;
    protected String label;
    protected int plateid;
    protected int well;
    protected String type;
    protected String fivep;
    protected String threep;
    protected String project;
    protected String workflow;
    protected String result;
    protected String status;
    protected String sequence;
    
    /** Creates new QueryInfo */
    public QueryInfo() {
    }

    public QueryInfo(int id, String gi, String genbankAcc, int plateid, String label, int well) {
        this.id = id;
        this.gi = gi;
        this.genbankAcc = genbankAcc;
        this.plateid = plateid;
        this.label = label;
        this.well = well;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getGi() {
       return gi;
    }
    
    public void setGi(String gi) {
        this.gi = gi;
    }
    
    public String getGeneName() {
        return geneName;
    }
    
    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }
    
    public String getGenbankAcc() {
        return genbankAcc;
    }
    
    public void setGenbankAcc(String genbankAcc) {
        this.genbankAcc = genbankAcc;
    }
    
    public String getGeneSymbol() {
        return geneSymbol;
    }
    
    public void setGeneSymbol(String geneSymbol) {
        this.geneSymbol = geneSymbol;
    }
    
    public String getPanumber() {
        return panumber;
    }
    
    public void setPanumber(String panumber) {
        this.panumber = panumber;
    }
    
    public int getPlateid() {
        return plateid;
    }
    
    public void setPlateid(int plateid) {
        this.plateid = plateid;
    }
    
    public String getLabel() {
        return label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    public int getWell() {
        return well;
    }
    
    public void setWell(int well) {
        this.well = well;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getFivep() {
        return fivep;
    }
    
    public void setFivep(String fivep) {
        this.fivep = fivep;
    }
    
    public String getThreep() {
        return threep;
    }
    
    public void setThreep(String threep) {
        this.threep = threep;
    }
    
    public String getProject() {
        return project;
    }
    
    public void setProject(String project) {
        this.project = project;
    }
    
    public String getWorkflow() {
        return workflow;
    }
    
    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSequence() {
        return sequence;
    }
    
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
}
