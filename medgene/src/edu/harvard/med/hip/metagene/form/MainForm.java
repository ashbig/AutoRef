/*
 * MainForm.java
 *
 * Created on December 6, 2001, 3:03 PM
 */

package edu.harvard.med.hip.metagene.form;

/**
 *
 * @author  dzuo
 * @version 
 */
public class MainForm extends ActionForm {
    private String geneDiseaseSelect = "geneDisease";
    
    /** Creates new MainForm */
    public MainForm() {
    }

    public String getGeneDiseaseSelect() {
        return geneDiseaseSelect;
    }
    
    public void setGeneDiseaseSelect(String geneDiseaseSelect) {
        this.geneDiseaseSelect = geneDiseaseSelect;
    }
}
