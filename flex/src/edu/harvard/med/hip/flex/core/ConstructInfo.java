/*
 * ConstructInfo.java
 *
 * Created on March 2, 2004, 2:14 PM
 */

package edu.harvard.med.hip.flex.core;

import java.util.*;

/**
 *
 * @author  DZuo
 */
public class ConstructInfo {
    public static final String MASTER_CLONE_OBTAINED = "MASTER CLONE OBTAINED";
    public static final String IN_SEQUENCING_PROCESS = "IN SEQUENCING PROCESS";
    public static final String CLONE_OBTAINED = "CLONE OBTAINED";
    public static final String SEQUENCE_VERIFIED_CLONES_OBTAINED = "SEQUENCE VERIFIED CLONES OBTAINED";
    public static final String SEQUENCE_REJECTED = "SEQUENCE REJECTED";
    public static final String IN_CLONING_PROCESS = "IN CLONING PROCESS";
    
    public static final int MASTER_CLONE_OBTAINED_ID = 2;
    public static final int IN_SEQUENCING_PROCESS_ID = 3;
    public static final int CLONE_OBTAINED_ID = 1;
    public static final int SEQUENCE_VERIFIED_CLONES_OBTAINED_ID = 4;
    public static final int SEQUENCE_REJECTED_ID = 5;
    public static final int FAILED_CLONING_ID = 6;
    
    private int constructid;
    private int oligoid5p;
    private int oligoid3p;
    private String constructType;
    private int projectid;
    private String projectName;
    private int workflowid;
    private String workflowName;
    private String status;
    private List clones;
    
    /** Creates a new instance of ConstructInfo */
    public ConstructInfo() {
        clones = new ArrayList();
    }
    
    public ConstructInfo(int constructid, int oligoid5p, int oligoid3p, String constructType, int projectid, String projectName, int workflowid, String workflowName, String status) {
        this.constructid = constructid;
        this.oligoid5p = oligoid5p;
        this.oligoid3p = oligoid3p;
        this.constructType = constructType;
        this.projectid = projectid;
        this.projectName = projectName;
        this.workflowid = workflowid;
        this.workflowName = workflowName;
        this.status = status;
        clones = new ArrayList();
    }
    
    public int getConstructid() {return constructid;}
    public int getOligoid5p() {return oligoid5p;}
    public int getOligoid3p() {return oligoid3p;}
    public String getConstructType() {return constructType;}
    public int getProjectid() {return projectid;}
    public String getProjectName() {return projectName;}
    public int getWorkflowid() {return workflowid;}
    public String getWorkflowName() {return workflowName;}
    public String getStatus() {return status;}
    public List getClones() {return clones;}
    
    public void setClones(List clones) {this.clones = clones;}
    
    public void addClone(CloneInfo clone) {
        clones.add(clone);
    }
    
    public int getNumOfClones() {
        return clones.size();
    }
}
