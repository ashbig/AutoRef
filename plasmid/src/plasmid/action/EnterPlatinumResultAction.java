/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.form.CloneValidationForm;
import plasmid.form.ViewPlatinumResultForm;

/**
 *
 * @author DZuo
 */
public class EnterPlatinumResultAction extends InternalUserAction {
    
    /** Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     *
     */
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderid = ((ViewPlatinumResultForm)form).getOrderid();
        CloneValidationForm form1 = new CloneValidationForm();
        form1.setOrderids(orderid);
        request.getSession().setAttribute("cloneValidationForm", form1);
        return mapping.findForward("success");
    }
}
