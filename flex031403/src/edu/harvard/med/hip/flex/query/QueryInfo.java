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
    private int id;
    private String gi;
    private String geneName;
    private String genbankAcc;
    private String geneSymbol;
    private String panumber;
    private String label;
    private int well;
    private String type;
    private String fivep;
    private String threep;
    private String project;
    private String workflow;
    private String result;
    private String status;
    
    /** Creates new QueryInfo */
    public QueryInfo() {
    }

    public QueryInfo(int id, String gi, String genbankAcc, String label, int well) {
        this.id = id;
        this.gi = gi;
        this.genbankAcc = genbankAcc;
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
}
