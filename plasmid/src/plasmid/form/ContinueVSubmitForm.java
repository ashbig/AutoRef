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

import plasmid.coreobject.User;

/**
 *
 * @author jasonx
 */
public class ContinueVSubmitForm extends ActionForm {
    
    private String VID = null;
    private User user=null;
    private String submit = null;


    public String getVID() {return VID;}
    public void setVID(String string) {VID = string;}
    public User getUser() {return user;}
    public void setUser(User u) {user = u;}
    public String getSubmit() {return submit;}
    public void setSubmit(String s) {this.submit = s;}
    
   public ContinueVSubmitForm() {
       super();
       // TODO Auto-generated constructor stub
   }

   public void reset(ActionMapping mapping, HttpServletRequest request) {
       reset();
   }
   
   public void reset() {
       VID = null;
       user = null;
       submit = null;
   }
   
   public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
       ActionErrors errors = new ActionErrors();
       return errors;
   }
}
