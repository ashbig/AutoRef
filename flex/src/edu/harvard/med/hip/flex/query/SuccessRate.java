/*
 * SuccessRate.java
 *
 * Created on January 10, 2003, 11:40 AM
 */

package edu.harvard.med.hip.flex.query;

import java.util.Vector;
/**
 *
 * @author  hweng
 */
public class SuccessRate {
    
    protected Vector total_samples;           //contains sequenceids for all experimental samples
    protected Vector success_samples;         //contains sequenceids for all successful experimental samples
    protected Vector notDone_samples;         //contains sequenceids for all samples not going through this step
    protected String step;       
    protected double curr_step_success_rate;          // the success rate for this step
    protected double overall_step_success_rate;       // the overall success rate up to this step   
    
    
    /** Creates a new instance of SuccessRate */
    public SuccessRate(Vector total_samples, Vector success_samples, String step) {
        this.total_samples = total_samples;
        this.success_samples = success_samples;
        this.step = step;        
        
    }
    
    public int getTotal(){
        return total_samples.size();
    }
    public int getSuccess(){
        return success_samples.size();        
    }    
    public String getStep(){
        return step;
    }
    public Vector getTotal_samples(){
        return total_samples;
    }
    public Vector getSuccess_samples(){
        return success_samples;
    }
    public Vector getNotDone_samples(){
        return notDone_samples;
    }
    public double getOverall_step_success_rate(){
        return overall_step_success_rate;
    }
    public int getNotdone(){
        return notDone_samples.size();
    }
    public Vector getFailed_samples(){
        return substract(total_samples, success_samples);
    }
 
    
    public void setTotal_samples(Vector total_samples){
        this.total_samples = total_samples;
    }
    public void setSuccess_samples(Vector success_samples){
        this.success_samples = success_samples;
    }
    public void setNotDone_samples(Vector notDone_samples){
        this.notDone_samples = notDone_samples;
    }
    public void setOverall_step_success_rate(double rate){
        this.overall_step_success_rate = rate;
    }

    
    
    /** get success rate for the current step
     *  @return success rate
     */
    public double getCurr_step_success_rate(){                
        if (total_samples.size() == 0) 
            return 0.0;
        curr_step_success_rate = (new Double(getSuccess()).doubleValue()) / getTotal();  
        long temp =  (long)(curr_step_success_rate * 10000 + ( curr_step_success_rate > 0 ? .5 : -.5 ));
        return (double) temp / 100;        
    }
    
    public int getFail(){
        return getTotal() - getSuccess();
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
