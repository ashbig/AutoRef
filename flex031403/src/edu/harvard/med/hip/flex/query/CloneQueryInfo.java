/*
 * CloneQueryInfo.java
 *
 * Created on October 2, 2002, 1:51 PM
 */

package edu.harvard.med.hip.flex.query;

/**
 *
 * @author  dzuo
 * @version 
 */
public class CloneQueryInfo extends QueryInfo {
    private String clonename;
    private String result;
    private String pubhit;
    
    /** Creates new CloneQueryInfo */
    public CloneQueryInfo() {
    }
    
    public CloneQueryInfo(int id, String gi, String genbankAcc, int plateid, String label, int well) {
        super(id, gi, genbankAcc, plateid, label, well);
    }
    
    public void setClonename(String clonename) {
        this.clonename = clonename;
    }
    
    public String getClonename() {
        return clonename;
    }
    
    public void setResult(String result) {
        this.result = result;
    }
    
    public String getResult() {
        return result;
    }
    
    public void setPubhit(String pubhit) {
        this.pubhit = pubhit;
    }
    
    public String getPubhit() {
        return pubhit;
    }
}
