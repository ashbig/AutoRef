/*
 * ViewCartAction.java
 *
 * Created on May 6, 2005, 12:33 PM
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

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.form.ViewCartForm;

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
            List c = new ArrayList();
            for(int i=0; i<shoppingcart.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)shoppingcart.get(i);
                c.add(item.getItemid());
            }
            
            DatabaseTransaction t = null;
            Connection conn = null;
            try {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                CloneManager manager = new CloneManager(conn);
                Map found = manager.queryClonesByCloneid(c, true, true, false);
                List newShoppingcart = new ArrayList();
                
                for(int i=0; i<shoppingcart.size(); i++) {
                    ShoppingCartItem item = (ShoppingCartItem)shoppingcart.get(i);
                    String cloneid = item.getItemid();
                    int quantity = item.getQuantity();
                    CloneInfo cloneInfo = (CloneInfo)found.get(cloneid);
                    cloneInfo.setQuantity(quantity);
                    
                    newShoppingcart.add(cloneInfo);
                }
                
                ((ViewCartForm)form).setCloneCountList(newShoppingcart);
                
                request.setAttribute("cart", newShoppingcart);
                return (mapping.findForward("success"));
            } catch (Exception ex) {
                if(Constants.DEBUG)
                    System.out.println(ex);
                
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Database error occured."));
                return (mapping.findForward("error"));
            } finally {
                DatabaseTransaction.closeConnection(conn);
            }
        }
    }
}

