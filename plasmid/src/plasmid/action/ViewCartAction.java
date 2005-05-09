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
import plasmid.coreobject.Clone;
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
        
        Map shoppingcart = (Map)request.getSession().getAttribute(Constants.CART);
        if(shoppingcart == null || shoppingcart.size() == 0) {
            shoppingcart = new HashMap();
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            return (mapping.findForward("success_empty"));
        } else {            
            Set clones = shoppingcart.keySet();
            List c = new ArrayList(clones);
            
            DatabaseTransaction t = null;
            Connection conn = null;
            try {
                t = DatabaseTransaction.getInstance();
                conn = t.requestConnection();
                CloneManager manager = new CloneManager(conn);
                Map found = manager.queryClonesByCloneid(c, true, true);
                List newShoppingcart = new ArrayList();
                
                Iterator iter = clones.iterator();
                while(iter.hasNext()) {
                    String cloneid =(String)iter.next();
                    Clone clone = (Clone)found.get(cloneid);
                    String quantity = (String)shoppingcart.get(cloneid);
                    CloneInfo cloneInfo = new CloneInfo(clone);
                    cloneInfo.setQuantity(Integer.parseInt(quantity));
                    newShoppingcart.add(cloneInfo);
                }
                
                int quantity[] = ((ViewCartForm)form).getQuantity();
                System.out.println(quantity);
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
    
