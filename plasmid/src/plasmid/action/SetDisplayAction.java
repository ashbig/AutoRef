/*
 * SetDisplayAction.java
 *
 * Created on April 29, 2005, 10:12 AM
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
import plasmid.form.RefseqSearchForm;
import plasmid.coreobject.*;
import plasmid.util.*;
import plasmid.query.handler.*;

/**
 *
 * @author  DZuo
 */
public class SetDisplayAction extends Action {
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
        
        String cdna = ((RefseqSearchForm)form).getCdna();
        String shrna = ((RefseqSearchForm)form).getShrna();
        String fusion = ((RefseqSearchForm)form).getFusion();
        String closed = ((RefseqSearchForm)form).getClosed();
        String marker = ((RefseqSearchForm)form).getMarker();
        String pdonr201 = ((RefseqSearchForm)form).getPdonr201();
        String pdonr221 = ((RefseqSearchForm)form).getPdonr221();
        String pdnrdual = ((RefseqSearchForm)form).getPdnrdual();
        String plk = ((RefseqSearchForm)form).getPlk();
        String pby011 = ((RefseqSearchForm)form).getPby011();
        String pgex2tk = ((RefseqSearchForm)form).getPgex2tk();
        
        int pagesize = ((RefseqSearchForm)form).getPagesize();
        int page = ((RefseqSearchForm)form).getPage();
        String displayPage = ((RefseqSearchForm)form).getDisplayPage();
        String species = ((RefseqSearchForm)form).getSpecies();
        String refseqType = ((RefseqSearchForm)form).getRefseqType();
        
        request.setAttribute("pagesize", new Integer(pagesize));
        request.setAttribute("page",  new Integer(page));
        request.setAttribute("displayPage", displayPage);
        request.setAttribute("species", species);
        request.setAttribute("refseqType", refseqType);
        
        String button = ((RefseqSearchForm)form).getButton();
        
        if(button != null && button.equals("Add To Cart")) {
            String cloneid = ((RefseqSearchForm)form).getCloneid();
            if(Integer.parseInt(cloneid) <= 0) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Invalid clone ID."));
                return (mapping.findForward("error"));
            }
            
            List shoppingcart = (List)request.getSession().getAttribute(Constants.CART);
            ShoppingCartItem item = new ShoppingCartItem(0, Integer.parseInt(cloneid), 1);
            ShoppingCartItem.addToCart(shoppingcart, item);
            request.getSession().setAttribute(Constants.CART, shoppingcart);
            request.getSession().setAttribute(Constants.CART_STATUS, Constants.UPDATED);
            return (mapping.findForward("success"));
        }
        
        List clones = null;
        if(displayPage.equals("indirect")) {
            clones = (List)request.getSession().getAttribute("found");
        } else {
            clones = (List)request.getSession().getAttribute("directFounds");
        }
        
        String sortby = ((RefseqSearchForm)form).getSortby();
        if("searchterm".equals(sortby))
            Collections.sort(clones, new CloneSearchTermComparator());
        if("cloneid".equals(sortby))
            Collections.sort(clones, new ClonenameComparator());
        if("clonetype".equals(sortby))
            Collections.sort(clones, new ClonetypeComparator());
        if("geneid".equals(sortby))
            Collections.sort(clones, new GeneidComparator());
        if("targetseq".equals(sortby))
            Collections.sort(clones, new TargetSeqidComparator());
        if("insertmutation".equals(sortby))
            Collections.sort(clones, new InsertMutationComparator());
        if("insertdiscrepancy".equals(sortby))
            Collections.sort(clones, new InsertDiscrepancyComparator());
        if("insertformat".equals(sortby))
            Collections.sort(clones, new InsertFormatComparator());
        if("vectorname".equals(sortby))
            Collections.sort(clones, new VectorNameComparator());
        if("selection".equals(sortby))
            Collections.sort(clones, new SelectionMarkerComparator());
        if("restriction".equals(sortby))
            Collections.sort(clones, new CloneRestrictionComparator());
        
        if(displayPage.equals("indirect")) {
            request.getSession().setAttribute("found", clones);
        } else {
            request.getSession().setAttribute("directFounds", clones);
        }
        
        return (mapping.findForward("success"));
    }
}
