/*
 * PASample.java
 *
 * Created on January 22, 2003, 4:05 PM
 */

package edu.harvard.med.hip.flex.query;

/**
 *
 * @author  hweng
 */
public class PASample {
        
    protected int id = -1;
    protected int position;
    protected String result;
    protected String label;    
    protected int cdslen;
    protected String PANumber;
    
    /** Creates a new instance of PASample */
    public PASample(int id, int position, String label, String result,  int cdslen, String PANumber) {
        this.id = id;
        this.position = position;
        this.label = label;
        this.result = result;
        this.cdslen = cdslen;
        this.PANumber = PANumber;
    }
    
    public PASample(int id, int position, String label) {
        this.id = id;
        this.position = position;
        this.label = label;
    }
    
    public int getId(){return id;}
    public int getPosition(){return position;}
    public String getResult(){return result;}
    public String getLabel(){return label;}
    public int getCdslen(){return cdslen;}
    public String getPANumber(){return PANumber;}
    
    public void setResult(String r){this.result = r;}
    public void setCdslen(int len){this.cdslen = len;}
    public void setPANumber(String pa){this.PANumber = pa;}
    
}
