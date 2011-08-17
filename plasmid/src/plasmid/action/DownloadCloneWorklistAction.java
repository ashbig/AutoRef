/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.Constants;
import plasmid.form.ChangeOrderStatusForm;
import plasmid.process.OrderProcessManager;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class DownloadCloneWorklistAction extends InternalUserAction {

    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String orderidString = ((ChangeOrderStatusForm) form).getInprocessOrderidString();

        OrderProcessManager manager = new OrderProcessManager();
        List clones = manager.getOrderClonesForWorklist(orderidString);
          response.setContentType("application/x-msexcel");
            response.setHeader("Content-Disposition", "attachment;filename=Invoice.xls");
            PrintWriter out = response.getWriter();
            manager.printCloneWorlist(out, clones);
            out.close();
            return null;
    }
}
