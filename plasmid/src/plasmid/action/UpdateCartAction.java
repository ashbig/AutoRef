/*
 * UpdateCartAction.java
 *
 * Created on May 10, 2005, 12:43 PM
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
import plasmid.coreobject.*;
import plasmid.form.ViewCartForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author  DZuo
 */
public class UpdateCartAction extends Action {
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
        //List cloneCountList = ((ViewCartForm)form).getCloneCountList();
        //List collectionCountList = ((ViewCartForm)form).getCollectionCountList();
        String ret = ((ViewCartForm)form).getSubmitButton();
        String itemid = ((ViewCartForm)form).getItemid();
        String type = ((ViewCartForm)form).getType();
        String isBatch = ((ViewCartForm)form).getIsBatch();
        request.getSession().setAttribute(Constants.CART_STATUS, Constants.UPDATED);
        List batchOrderClones = (List)request.getSession().getAttribute(Constants.BATCH_ORDER_CLONES);
        
        if(!("Save Cart".equals(ret)) && (shoppingcart == null || shoppingcart.size() == 0 )) {
            shoppingcart = new ArrayList();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            //System.out.println("1");
            return (mapping.findForward("success_empty"));
        } else {
            List shoppingcartCopy = new ArrayList();
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            
            OrderProcessManager m = new OrderProcessManager();
            m.processShoppingCartItems(shoppingcart);
            List cloneids = m.getCloneids();
            List collectionNames = m.getCollectionNames();
            
            if(cloneids.size() == 0 && collectionNames.size() == 0) {
                shoppingcart = new ArrayList();
                request.getSession().setAttribute(Constants.CART, shoppingcart);
                return (mapping.findForward("success_empty"));
            }
            
            List clones = m.getShoppingCartClones(cloneids, m.getClones(), batchOrderClones, isBatch, new ArrayList());
            List collections = m.getShoppingCartCollections(collectionNames, m.getCollections(), new ArrayList());
            
            if(clones == null && !("Save Cart".equals(ret))) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Error occured while updating shopping cart."));
                return (mapping.findForward("error"));
            }
            if(collections == null && !("Save Cart".equals(ret))) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Error occured while updating shopping cart."));
                return (mapping.findForward("error"));
            }
            
            List newShoppingcartClones = new ArrayList();
            List newShoppingcartCollections = new ArrayList();
            
            if("Remove".equals(ret)) {
                if(ShoppingCartItem.CLONE.equals(type)) {
                    newShoppingcartClones = m.updateShoppingCartForClones(clones, itemid, shoppingcartCopy);
                    newShoppingcartCollections = m.updateShoppingCartForCollections(collections, null, shoppingcartCopy);
                } else {
                    newShoppingcartClones = m.updateShoppingCartForClones(clones, "0", shoppingcartCopy);
                    newShoppingcartCollections = m.updateShoppingCartForCollections(collections, itemid, shoppingcartCopy);
                }
                
                if("Y".equals(isBatch)) {
                    m.removeCloneFromBatchOrder(batchOrderClones, itemid);
                    request.getSession().setAttribute(Constants.BATCH_ORDER_CLONES, batchOrderClones);
                }
            } else {
                newShoppingcartClones = m.updateShoppingCartForClones(clones, "0", shoppingcartCopy);
                newShoppingcartCollections = m.updateShoppingCartForCollections(collections, null, shoppingcartCopy);
            }
            
            if(newShoppingcartClones == null && !("Save Cart".equals(ret))) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Error occured while updating shopping cart."));
                return (mapping.findForward("error"));
            } else if(newShoppingcartCollections == null && !("Save Cart".equals(ret))) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Error occured while updating shopping cart."));
                return (mapping.findForward("error"));
            } else if(newShoppingcartClones.size() == 0 && newShoppingcartCollections.size() == 0) {
                shoppingcart = new ArrayList();
                request.getSession().setAttribute(Constants.CART, shoppingcart);
                return (mapping.findForward("success_empty"));
            } else {
                //((ViewCartForm)form).setCloneCountList(newShoppingcartClones);
                //((ViewCartForm)form).setCollectionCountList(newShoppingcartCollections);
                
                request.setAttribute("cart", newShoppingcartClones);
                request.setAttribute("collectioncart", newShoppingcartCollections);
                request.getSession().setAttribute(Constants.CART, shoppingcartCopy);
            }
            if("Check Out".equals(ret)) {
                request.setAttribute("isbatch",  isBatch);
                return (mapping.findForward("success_checkout"));
            }
            if("Save Cart".equals(ret)) {
                if(!m.saveShoppingCart(user, shoppingcartCopy)) {
                    errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.database.error","Error occured while saving shopping cart to the user account."));
                    return (mapping.findForward("error"));
                }
                
                request.getSession().setAttribute(Constants.CART_STATUS, Constants.SAVED);
                return (mapping.findForward("success_save"));
            }
            
            request.getSession().setAttribute(Constants.CART_STATUS, Constants.UPDATED);
            return (mapping.findForward("success"));
        }
    }
}