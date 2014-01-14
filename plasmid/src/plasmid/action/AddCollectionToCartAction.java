/*
 * AddCollectionToCartAction.java
 *
 * Created on November 8, 2005, 9:59 AM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
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

import plasmid.Constants;
import plasmid.form.RefseqSearchForm;
import plasmid.coreobject.*;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author  DZuo
 */
public class AddCollectionToCartAction extends Action {
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
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        String collectionName = ((RefseqSearchForm)form).getCollectionName();
        int pagesize = ((RefseqSearchForm)form).getPagesize();
        int page = ((RefseqSearchForm)form).getPage();
        String displayPage = ((RefseqSearchForm)form).getDisplayPage();
        
        request.setAttribute("pagesize", new Integer(pagesize));
        request.setAttribute("page",  new Integer(page));
        request.setAttribute("displayPage", displayPage);
        
        if(collectionName == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","You need to select a collection."));
            return (mapping.findForward("error"));
        }
        
        List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
        if(shoppingcart == null) {
            shoppingcart = new ArrayList();
        }
        ShoppingCartItem item = new ShoppingCartItem(0, collectionName, 1, ShoppingCartItem.COLLECTION);
        ShoppingCartItem.addToCart(shoppingcart, item);
        
        request.getSession().setAttribute(Constants.CART, shoppingcart);
        request.getSession().setAttribute(Constants.CART_STATUS, Constants.UPDATED);
        
        return (mapping.findForward("success"));
    }
}
