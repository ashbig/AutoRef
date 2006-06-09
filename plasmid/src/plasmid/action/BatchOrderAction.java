/*
 * BatchOrderAction.java
 *
 * Created on June 5, 2006, 1:30 PM
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
import org.apache.struts.upload.*;

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.form.*;
import plasmid.process.OrderProcessManager;

/**
 *
 * @author  DZuo
 */
public class BatchOrderAction extends UserAction {
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
    public ActionForward userPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        String cloneType = ((BatchOrderForm)form).getCloneType();
        FormFile orderFile = ((BatchOrderForm)form).getOrderFile();
        
        InputStream input = null;
        try {
            input = orderFile.getInputStream();
        } catch (Exception ex) {
            if(Constants.DEBUG)
                System.out.println("Error occured while reading the file.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Error occured while reading the file."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        OrderProcessManager m = new OrderProcessManager();
        List clones = null;
        try {
            clones = m.parseBatchOrderFile(input);
        } catch (Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", ex.getMessage()));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(clones == null) {
            if(Constants.DEBUG)
                System.out.println("Error occured while parsing the file.");
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general","Error occured while parsing the file."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        List duplicateClones = m.checkDuplicateClonesAndWells(clones);
        if(duplicateClones.size()>0) {
            String error = "<br>The following clones have duplication in cloneid or well position:<br>";
            for(int i=0; i<duplicateClones.size(); i++) {
                BatchOrder c = (BatchOrder)duplicateClones.get(i);
                error += c.getOriginalCloneid()+","+c.getPlate()+","+c.getWell()+"<br>";
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", error));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        if(user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        
        List noFoundClones = new ArrayList();
        Map foundClones = null;
        try {
            foundClones = m.queryBatchOrderClones(cloneType, clones, restrictions, noFoundClones);
        } catch (Exception ex) {
            if(Constants.DEBUG)
                System.out.println(ex);
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Error occured while searching database for clones"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        if(foundClones == null) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Error occured while searching database for clones"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        if(noFoundClones.size()>0) {
            String error = "<br>The following clones have not been found in the database:<br>";
            for(int i=0; i<noFoundClones.size(); i++) {
                String s = (String)noFoundClones.get(i);
                error += s+"<br>";
            }
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", error));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        if(foundClones.size() != clones.size()) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Clones found in the database don't match clones in the file."));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
        if(shoppingcart != null && shoppingcart.size() > 0) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "You have clones in your shopping cart. Please empty your shpping cart before continue."));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        shoppingcart = new ArrayList();
        List processedClones = null;
        try {
            processedClones = m.processBatchOrderClones(clones, foundClones, shoppingcart);
        } catch (Exception ex) {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.general", "Error occured while processing clones"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        List groups = m.groupClonesByTargetPlate(processedClones);
        for(int i=0; i<groups.size(); i++) {
            List plate = (ArrayList)groups.get(i);
            List growths = m.groupClonesByGrowth(plate);
            if(growths.size()>1) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "The clones on plate "+((CloneInfo)plate.get(0)).getTargetPlate()+" have different growth conditions."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
        }
        
        request.getSession().setAttribute(Constants.CART, shoppingcart);        
        request.getSession().setAttribute(Constants.BATCH_ORDER_CLONES, clones);
        request.setAttribute("cart", processedClones);
        ViewCartForm f = new ViewCartForm();
        f.setIsBatch("Y");
        request.getSession().setAttribute("viewCartForm", f);
        
        return (mapping.findForward("success"));
    }
}