/*
 * PlateSuccessInfo.java
 *
 * Created on February 21, 2003, 4:38 PM
 */

package edu.harvard.med.hip.flex.query;

/**
 *
 * @author  hweng
 */
public class PlateSuccessInfo {
    
    private String label;
    private int success;
    private int fail;
    
    public PlateSuccessInfo(String label, int success, int fail){
        this.label = label;
        this.success = success;
        this.fail = fail;
    }
    
    public String getLabel(){
        return label;
    }
    public int getSuccess(){
        return success;
    }
    public int getFail(){
        return fail;
    }
    public int getTotal(){
        return success + fail;
    }
    public double getRate(){
        if ((success + fail) == 0) 
            return 0.0;
        double rate = 1.0 * success / (success + fail);  
        long temp =  (long)(rate * 10000 + (rate > 0 ? .5 : -.5 ));
        return (double) temp / 100;     
    }
    public void setSuccess(int s){
        success = s;
    }
    public void setFail(int f){
        fail = f;
    }
    public void increSuccess(){
        success ++;
    }
    public void increFail(){
        fail ++;
    }
}
