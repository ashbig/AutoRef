/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.Institution;

/**
 *
 * @author Dongmei
 */
public class AddInstitutionsForm extends ActionForm {
    private String institutionString;
    private String [] institution;
    private String [] country;
    private String [] category;

    public void initiate(List<String> institutionList) {
        int n = institutionList.size();
        institution = new String[n];
        country = new String[n];
        category = new String[n];
        
        int i=0;
        for(String s:institutionList) {
            institution[i]=s;
            i++;
        }
        
        for(int m=0; m<n; m++) {
            country[m] = "USA";
            category[m] = Institution.CATEGORY_US_INSTITUTION;
        }
    }
    
    public String getInstitutionString() {
        return institutionString;
    }

    public void setInstitutionString(String institutions) {
        this.institutionString = institutions;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((institutionString == null) || (institutionString.trim().length() < 1))
            errors.add("institutions", new ActionError("error.institutions.required"));
        
        return errors;
    }

    public String getCountry(int i) {
        return country[i];
    }

    public void setCountry(int i, String country) {
        this.country[i] = country;
    }

    public String getCategory(int i) {
        return category[i];
    }

    public void setCategory(int i, String category) {
        this.category[i] = category;
    }

    public String getInstitution(int i) {
        return institution[i];
    }

    public void setInstitution(int i, String institution) {
        this.institution[i] = institution;
    }
    
    public String[] getAllInstitution() {
        return institution;
    }
    
    public String[] getAllCategory() {
        return category;
    }
    
    public String[] getAllCountry() {
        return country;
    }
}

