/*
 * MergeCartAction.java
 *
 * Created on May 18, 2005, 12:18 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;

import plasmid.form.MergeCartForm;
import plasmid.Constants;
import plasmid.coreobject.ShoppingCartItem;

/**
 *
 * @author  DZuo
 */
public class MergeCartAction extends Action {
    
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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();

        String merge = ((MergeCartForm)form).getMerge();
        List currentCart = (List)request.getSession().getAttribute(Constants.CART);
        List saveCart = (List)request.getSession().getAttribute("databaseCart");
        request.getSession().removeAttribute("databaseCart");
        
        if(merge.equals("discartCurrentCart")) {
            request.getSession().setAttribute(Constants.CART, saveCart);
        } else if(merge.equals("merge")) {
            List newCart = ShoppingCartItem.mergeCart(currentCart, saveCart);
            request.getSession().setAttribute(Constants.CART, newCart);
        } 
                    
        return (mapping.findForward("success"));        
    }    
}


