/*
 * VectorSearchForm.java
 *
 * Created on February 2, 2006, 2:30 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class VectorSearchForm extends ActionForm {    
    private String species;
    private List vectortype;
    
    /** Creates a new instance of VectorSearchForm */
    public VectorSearchForm() {
    }
    
    public void setSpecies(String s) {this.species = s;}
    public String getSpecies() {return species;}
    
    public void setVectortype(int i, Boolean s) {this.vectortype.set(i, s);}
    public Boolean getVectortype(int i) {return (Boolean)vectortype.get(i);}
    
    public void setVectortype(List l) {
        vectortype = new ArrayList();
        for(int i=0; i<l.size(); i++) {
            vectortype.add(new Boolean(false));
        }
    }
    public List getVectortype() {return vectortype;}
        
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(vectortype == null)
            return;
        
        for(int i=0; i<vectortype.size(); i++) {
            vectortype.set(i, new Boolean(false));
        }
    }
}
