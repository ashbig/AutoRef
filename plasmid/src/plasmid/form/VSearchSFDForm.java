/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import plasmid.coreobject.*;
/**
 *
 * @author jasonx
 */
public class VSearchSFDForm extends ActionForm {
    
    private String VN = null;
    private String search = "Y";
    private String submit = null;
    private List results = null;
    private User user = null;


    public String getVN() {return VN;}
    public void setVN(String string) {VN = string.trim();}
    public String getSearch() {return search;}
    public void setSearch(String string) {search = string.trim();}
    public String getSubmit() {return submit;}
    public void setSubmit(String s) {this.submit = s;}
    public List getResults() {return results;}
    public void setResults(List l) {this.results = l;}
    public User getUser() {return user;}
    public void setUser(User u) {this.user = u;}
    
   public VSearchSFDForm() {
       super();
       // TODO Auto-generated constructor stub
   }

   public void reset(ActionMapping mapping, HttpServletRequest request) {
       reset();
   }
   
   public void reset() {
       VN = null;
       search = "Y";
       results = null;
       submit = null;
   }
   
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
       ActionErrors errors = new ActionErrors();
       if (getVN() == null || getVN().length() < 1) {
           errors.add("VN", new ActionError("error.VN.required"));
           // TODO: add 'error.VN.required' key to your resources
       }
       return errors;
   }
}
