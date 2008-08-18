/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.coreobject.MTA;
import plasmid.form.CheckoutForm;

/**
 *
 * @author DZuo
 */
public class DisplayMTAAction extends UserAction {
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward userPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        String isBatch = ((CheckoutForm)form).getIsBatch();
        request.setAttribute("isbatch", isBatch);
        
        String[] mtas = ((CheckoutForm)form).getIsagreeList();
       
        if(mtas == null || mtas.length==0)
            return (new ActionForward(mapping.getInput()));
        
        for(int i=0; i<mtas.length; i++) {
            if(mtas[i].equals(MTA.ISAGREE_N))
                return (mapping.findForward("fail"));
        }
        
        return (mapping.findForward("success"));
    }
}