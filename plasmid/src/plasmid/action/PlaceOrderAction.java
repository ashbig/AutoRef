/*
 * PlaceOrderAction.java
 *
 * Created on June 6, 2005, 3:45 PM
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
import plasmid.form.CheckoutForm;
import plasmid.process.OrderProcessManager;
import plasmid.util.Mailer;

import javax.servlet.http.*;
import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;

/**
 *
 * @author  DZuo
 */
public class PlaceOrderAction extends Action {
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
        
        try {
            Enumeration names = request.getParameterNames();
            String str = "cmd=_notify-validate";
            String invoice = "";
            String payment_gross = "";
            String receiver_email = "";
            String payment_status = "";
            String txn_id = "";
            
            while(names.hasMoreElements()) {
                String name =  (String)names.nextElement();
                String value = request.getParameter(name);
                System.out.println(name+":"+value);
                if("invoice".equals(name))
                    invoice = value;
                if("payment_gross".equals(name))
                    payment_gross = value;
                if("receiver_email".equals(name))
                    receiver_email = value;
                if("payment_status".equals(name))
                    payment_status = value;
                if("txn_id".equals(name))
                    txn_id = value;
                str = str + "&" + name + "=" + URLEncoder.encode(value, "ISO-8859-1" ) ;
            }
            
            
            /* Extract form data */
            URL u = new URL("https://www.sandbox.paypal.com/cgi-bin/webscr") ;
            URLConnection uc = u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" ) ;
            PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.println(str);
            pw.close();
            
            BufferedReader in = new BufferedReader(
            new InputStreamReader(uc.getInputStream()));
            String ipnres = in.readLine();
            in.close();
            
            int orderid = Integer.parseInt(invoice);
            OrderProcessManager manager = new OrderProcessManager();
            CloneOrder order = manager.getCloneOrder(orderid);
            String email = manager.findEmail(order.getUserid());
            
            if(CloneOrder.PENDING_PAYMENT.equals(order.getStatus())) {
                if (ipnres.equals("VERIFIED" )) {
                    boolean isok = checkData(payment_gross,receiver_email,payment_status,txn_id,order);
                    if(isok) {
                        boolean b = manager.updateOrderStatus(orderid, CloneOrder.PENDING);
                        if(b) {
                            manager.sendOrderEmail(order, email);
                        } else {
                            manager.sendOrderFailEmail(order, email);
                        }
                    }
                }
                if (ipnres.equals("INVALID" )) {
                    boolean b = manager.updateOrderStatus(orderid, CloneOrder.INVALIDE_PAYMENT);
                    manager.sendOrderInvalidePaymentEmail(order, email);
                }
            }
            request.getSession().setAttribute(Constants.CLONEORDER, order);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        return (mapping.findForward("success"));
    }
    
    public boolean checkData(String payment_gross,String receiver_email,String payment_status,String txn_id,CloneOrder order) {
        double gross = Double.parseDouble(payment_gross);
        double total = Double.parseDouble(order.getTotalPriceString());
        if(gross == total && receiver_email.equals(Constants.PAYPALEMAIL) && payment_status.equals("Completed")) {
            return true;
        }
        return false;
    }
}