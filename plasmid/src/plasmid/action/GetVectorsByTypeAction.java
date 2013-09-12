/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.coreobject.Clone;
import plasmid.coreobject.ShoppingCartItem;
import plasmid.coreobject.User;
import plasmid.database.DatabaseManager.UserManager;
import plasmid.form.GetVectorsByTypeForm;
import plasmid.form.ViewCartForm;
import plasmid.process.QueryProcessManager;
import plasmid.query.coreobject.CloneInfo;
import plasmid.query.handler.GeneQueryHandler;
import plasmid.query.handler.VectorQueryHandler;

/**
 *
 * @author dongmei
 */
public class GetVectorsByTypeAction extends PlasmidAction {

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an
     * <code>ActionForward</code> instance describing where and how control
     * should be forwarded, or
     * <code>null</code> if the response has already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward plasmidPerform(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        List<ShoppingCartItem> shoppingcart = (List) request.getSession().getAttribute(Constants.CART);

        ViewCartForm f = (ViewCartForm) request.getSession().getAttribute("viewCartForm");
        if (f != null) {
            f.setIsBatch(null);
        }

        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        restrictions.add(Clone.NON_PROFIT);
        if (user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }

        String type = ((GetVectorsByTypeForm) form).getType();

        QueryProcessManager manager = new QueryProcessManager();
        List<CloneInfo> clones = manager.getVectorsByProperty(type, Clone.AVAILABLE, restrictions);

        if (clones == null) {
            if (Constants.DEBUG) {
                System.out.println("query failed.");
            }

            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.failed"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }

        if (clones.isEmpty()) {
            return (mapping.findForward("empty"));
        }

        GeneQueryHandler handler = new VectorQueryHandler();
        handler.setInCartClones(clones, shoppingcart);
        request.getSession().setAttribute("directFounds", clones);

        return (mapping.findForward("success"));
    }
}
