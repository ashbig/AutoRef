/*
 * GetGenesForm.java
 *
 * Created on January 17, 2002, 2:55 PM
 */

package edu.harvard.med.hip.metagene.form;

/**
 *
 * @author  dzuo
 * @version 
 */
public class GetGenesForm extends ActionForm {
    private int diseaseTerm;
    private int stat;
    private int number;

    /** Creates new GetGenesForm */
    public GetGenesForm() {
    }
    
    public int getDiseaseTerm() {
        return diseaseTerm;
    }
    
    public void setDiseaseTerm(int diseaseTerm) {
        this.diseaseTerm = diseaseTerm;
    }
    
    public int getStat() {
        return stat;
    }
    
    public void setStat(int stat) {
        this.stat = stat;
    }
    
    public int getNumber() {
        this.number = number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
}
