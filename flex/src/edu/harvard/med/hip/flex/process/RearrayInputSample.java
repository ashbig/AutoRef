/*
 * RearrayInputSample.java
 *
 * This class stores the input information.
 *
 * Created on May 15, 2003, 1:44 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 *
 * @author  dzuo
 */
public class RearrayInputSample {
    protected String sourcePlate;
    protected String sourceWell;
    protected String destPlate;
    protected String destWell;
    protected String clone;
    
    /** Creates a new instance of RearrayInputSample */
    public RearrayInputSample(String s1, String s2, String s3, String s4, boolean isClone) {
        if(isClone) {
            this.clone = s1;
            this.sourcePlate = s2;
        } else {
            this.sourcePlate = s1;
            this.sourceWell = s2;
        }        
        this.destPlate = s3;
        this.destWell = s4;        
    }
   
    public String getSourcePlate() {return sourcePlate;}
    public String getSourceWell() {return sourceWell;}
    public String getDestPlate() {return destPlate;}
    public String getDestWell() {return destWell;}
    public String getClone() {return clone;}
    
    /********************************************************************************
     *                          Test                                    
     ********************************************************************************/
    
    public static void main(String args[]) {
        RearrayInputSample s1 = new RearrayInputSample("SourcePlate1", "SA1", "DestPlate1", "DB2", false);
        RearrayInputSample s2 = new RearrayInputSample("Clone1", "SourcePlate2", null, null, true);
        RearrayInputSample s3 = new RearrayInputSample("Clone2", "SourcePlate3", "DestPlate3", "DB3", true);
        System.out.println("s1:\t"+s1.getSourcePlate()+"\t"+s1.getSourceWell()+"\t"+s1.getDestPlate()+"\t"+s1.getDestWell()+"\t"+s1.getClone());
        System.out.println("s2:\t"+s2.getSourcePlate()+"\t"+s2.getSourceWell()+"\t"+s2.getDestPlate()+"\t"+s2.getDestWell()+"\t"+s2.getClone());
        System.out.println("s3:\t"+s3.getSourcePlate()+"\t"+s3.getSourceWell()+"\t"+s3.getDestPlate()+"\t"+s3.getDestWell()+"\t"+s3.getClone());   
    }
}
