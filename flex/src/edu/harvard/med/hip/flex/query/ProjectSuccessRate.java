/*
 * SuccessRateOverAll.java
 *
 * Created on February 13, 2003, 3:15 PM
 */

package edu.harvard.med.hip.flex.query;

import java.util.Vector;
/**
 *
 * @author  hweng
 */
public class ProjectSuccessRate {
    
    int projectid;
    String projectname;
    int workflowid;
    String workflowname;
    String cloneFormat;
    Vector success_rates;    
    
    /** Creates a new instance of SuccessRateOverAll */
    public ProjectSuccessRate(Vector success_rates, int projectid, int workflowid, String cloneFormat) {
        this.projectid = projectid;
        this.workflowid = workflowid;
        this.cloneFormat = cloneFormat;
        this.success_rates = success_rates;
    }
    

    public int getProjectid(){
        return projectid;
    }
    public String getProjectname(){
        return projectname;
    }
    public int getWorkflowid(){
        return workflowid;
    }
    public String getWorkflowname(){
        return workflowname;
    }
    public String getCloneFormat(){
        return cloneFormat;
    }
    public Vector getSuccess_rates(){
        return this.success_rates;
    }
    
    public void setProjectname(String name){
        this.projectname = name;
    }
    public void setWorkflowname(String name){
        this.workflowname = name;
    }
    public void setSuccessrates(Vector success_rates) {
        this.success_rates = success_rates;
    }
    
    
    /***************************************************************/
    public Vector getReverseSuccessRates(){
        Vector success_rates_reverse = new Vector();
        for(int i = success_rates.size()-1; i >= 0 ; i--)
            success_rates_reverse.add(success_rates.elementAt(i));
        return success_rates_reverse;
    }

    /** get the overall success rate up to the given step so far
     * @param step      the given step
     * @return  the overall success rate
     */
    public double getOverallSucRate(String step){
                
        int k = -1; int not_done = 0;
        for(int i = 0; i < success_rates.size(); i++){
            if (step.equalsIgnoreCase(((SuccessRate)success_rates.elementAt(i)).getStep())){
                k = i;
                break;
            }
        }
        if(success_rates.size()==1)    // vector of success_rates only contains SuccessRate with step of 'INIT'
            return 100.0;
        if(k == -1)                           // no such step is found in Vector of success_rates
            return -1.0;
        int success = ((SuccessRate)success_rates.elementAt(k)).getSuccess();
        for(int i = k; i < success_rates.size()-1; i++){
            not_done += (((SuccessRate)success_rates.elementAt(i+1)).getSuccess()
                             - ((SuccessRate)success_rates.elementAt(i)).getTotal());
        }
        int total = ((SuccessRate)success_rates.lastElement()).getTotal() - not_done;
       
        if (total == 0) 
            return 0.0;
        
        double rate = success * 1.0 / total;  
        long temp =  (long)(rate * 10000 + ( rate > 0 ? .5 : -.5 ));
        return (double) temp / 100;    

    }
    
    /** get the samples not done yet at the given step 
     * @param step      the given step
     * @return          the collection of samples not done
     */
    public Vector getNotDoneSamplesInStep(String step){

        int k = -1; 
        for(int i = 0; i < success_rates.size(); i++){
            if (step.equalsIgnoreCase(((SuccessRate)success_rates.elementAt(i)).getStep())){
                k = i;
                break;
            }
        }

        if(k == -1)                           // no such step is found in Vector of success_rates
            return new Vector();
        if(k == success_rates.size() - 1)   // the step is at INIT
            return new Vector();
        Vector notDoneSamples = substract(((SuccessRate)success_rates.elementAt(k+1)).getSuccess_samples(),
                                          ((SuccessRate)success_rates.elementAt(k)).getTotal_samples());
        return notDoneSamples;
    
    }
    
        
    public Vector substract(Vector v1, Vector v2){
        Vector v = new Vector();
        for(int i = 0; i < v1.size(); i++){
            if(!v2.contains(v1.elementAt(i)))
                v.add(v1.elementAt(i));
        }
        return v;
    }
    
    
}