/*
 * DisplayLinksForm.java
 *
 * Created on February 6, 2002, 4:44 PM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  dzuo
 * @version 
 */
public class DisplayLinksForm extends ActionForm {
    private int hipGeneId;
    private String geneSymbol;

    /** Creates new DisplayLinksForm */
    public DisplayLinksForm() {
    }

    public int getHipGeneId() {
        return hipGeneId;
    }
    public String getGeneSymbol(){
        return geneSymbol;
    }    
    
    public void setHipGeneId(int hipGeneId) {
        this.hipGeneId = hipGeneId;
    }
    public void setGeneSymbol(String geneSymbol){
        this.geneSymbol = geneSymbol;
    }
}
