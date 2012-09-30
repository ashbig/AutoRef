/*
 * ViewCartAction.java
 *
 * Created on May 6, 2005, 12:33 PM
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.Constants;
import plasmid.form.ViewCartForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author  DZuo
 */
public class ViewCartAction extends Action {
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
        
        List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
        
        if(shoppingcart == null || shoppingcart.size() == 0) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        } else {
            OrderProcessManager manager = new OrderProcessManager();
            manager.processShoppingCartItems(shoppingcart);
            List clones = manager.getClones();
            List collections = manager.getCollections();
            List cloneids = manager.getCloneids();
            List collectionNames = manager.getCollectionNames();
            
            String isbatch = ((ViewCartForm)form).getIsBatch();
            List batchorders = (List)request.getSession().getAttribute(Constants.BATCH_ORDER_CLONES);
            List removelist = new ArrayList();
            List newShoppingcart = manager.getShoppingCartClones(cloneids, clones, batchorders, isbatch, removelist);
            List newShoppingcartCollections = manager.getShoppingCartCollections(collectionNames, collections, removelist);
            if(!removelist.isEmpty()) {
                manager.removeShoppingCartItems(removelist);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general","Some items are removed from your shopping cart due to status change."));
            }
            
            if(newShoppingcart == null || newShoppingcartCollections == null) {
                if(Constants.DEBUG)
                    System.out.println("Database error occured.");
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Database error occured."));
                return (mapping.findForward("error"));
            }
            
            ((ViewCartForm)form).setCloneCountList(newShoppingcart);
            ((ViewCartForm)form).setCollectionCountList(newShoppingcartCollections);
            
            request.setAttribute("cart", newShoppingcart);
            request.setAttribute("collectioncart", newShoppingcartCollections);
            
            return (mapping.findForward("success"));
        }
    }
}

