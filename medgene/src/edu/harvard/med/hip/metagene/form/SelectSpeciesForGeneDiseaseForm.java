/*
 * SelectSpeciesForGeneDiseaseForm.java
 *
 * Created on July 8, 2002, 10:57 AM
 */
package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  hweng
 * @version 
 */
public class SelectSpeciesForGeneDiseaseForm extends ActionForm {
    String species = "Homo sapiens";
    
    public String getSpecies(){
        return species;
    }
    public void setSpecies(String species){
        this.species = species;
    }
    
    
}
