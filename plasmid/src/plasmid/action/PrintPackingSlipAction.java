/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.action;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.OrderClones;
import plasmid.form.PrintPackingSlipForm;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author Lab User
 */
public class PrintPackingSlipAction extends InternalUserAction {

    /**
     * Does the real work for the perform method which must be overriden by the
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
        String[] shipped = ((PrintPackingSlipForm)form).getShipped();
        
        CloneOrder order = (CloneOrder) request.getSession().getAttribute(Constants.CLONEORDER);
        List<OrderClones> clones = order.getClones();
        for(OrderClones clone:clones) {
            clone.setShipped(false);
            for(int i=0; i<shipped.length; i++) {
                int cloneid = Integer.parseInt(shipped[i]);
                if(cloneid==clone.getCloneid()) {
                    clone.setShipped(true);
                }
            }
        }
        
        //write to pdf file in browser
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Order_" + order.getOrderid() + ".pdf");
        OrderProcessManager manager = new OrderProcessManager();
        manager.printPackingSlip(response.getOutputStream(), order);
        return mapping.findForward(null);
    }
}