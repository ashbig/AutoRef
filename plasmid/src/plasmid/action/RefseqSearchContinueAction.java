/*
 * RefseqSearchContinueAction.java
 *
 * Created on April 26, 2005, 11:32 AM
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

import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.form.RefseqSearchForm;
import plasmid.coreobject.*;
import plasmid.util.StringConvertor;
import plasmid.query.handler.*;

/**
 *
 * @author DZuo
 */
public class RefseqSearchContinueAction extends Action {

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
    public ActionForward perform(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getSession().removeAttribute("directFounds");

        ((RefseqSearchForm) form).setDisplayPage("indirect");
        ((RefseqSearchForm) form).setDisplay("symbol");

        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        String species = ((RefseqSearchForm) form).getSpecies();
        String refseqType = ((RefseqSearchForm) form).getRefseqType();
        String searchType = ((RefseqSearchForm) form).getSearchType();
        String searchString = ((RefseqSearchForm) form).getSearchString();
        boolean cdna = ((RefseqSearchForm) form).getCdna();
        boolean shrna = ((RefseqSearchForm) form).getShrna();
        boolean genomicfragment = ((RefseqSearchForm) form).getGenomicfragment();
        boolean tfbindsite = ((RefseqSearchForm) form).getTfbindsite();
        boolean genome = ((RefseqSearchForm) form).getGenome();
        ((RefseqSearchForm) form).setPage(1);
        ((RefseqSearchForm) form).setForward("success");

        List<ShoppingCartItem> shoppingcart = (List) request.getSession().getAttribute(Constants.CART);

        List clonetypes = new ArrayList();
        if (cdna) {
            clonetypes.add(Clone.CDNA);
        }
        if (shrna) {
            clonetypes.add(Clone.SHRNA);
        }
        if (genomicfragment) {
            clonetypes.add(Clone.GENOMIC_FRAGMENT);
        }
        if (tfbindsite) {
            clonetypes.add(Clone.TFBINDSITE);
        }
        if (genome) {
            clonetypes.add(Clone.GENOME);
        }
        if (clonetypes.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.clonetype.empty"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        restrictions.add(Clone.NON_PROFIT);
        if (user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }

        StringConvertor sc = new StringConvertor();
        List searchList = sc.convertFromStringToList(searchString, " \t\n\r\f");

        if (searchList.size() == 0) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.searchstring.invalid"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }

        GeneQueryHandler handler = null;
        List directFoundList = null;
        int totalFoundCloneCount = 0;

        if (GeneQueryHandler.GENBANK.equals(searchType) || GeneQueryHandler.GI.equals(searchType)) {
            ((RefseqSearchForm) form).setDisplay("genbank");
            ((RefseqSearchForm) form).setDisplayPage("direct");

            if (GeneQueryHandler.GENBANK.equals(searchType)) {
                handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.DIRECT_GENBANK, searchList);
            } else {
                handler = StaticQueryHandlerFactory.makeGeneQueryHandler(GeneQueryHandler.DIRECT_GI, searchList);
            }
            if (handler == null) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }

            try {
                Set cloneids = handler.doQuery(restrictions, clonetypes, species, -1, -1);

                if (cloneids.size() > GeneQueryHandler.MAXCLONECOUNT) {
                    return (mapping.findForward("summary"));
                }

                handler.restoreClones(null, true, cloneids);
                directFoundList = handler.convertFoundToCloneinfo();
                searchList = handler.getNofound();
                totalFoundCloneCount = handler.getFoundCloneCount();
                handler.setInCartClones(directFoundList, shoppingcart);
                request.getSession().setAttribute("directFounds", directFoundList);
                request.getSession().setAttribute("numOfDirectFound", new Integer(totalFoundCloneCount));
            } catch (Exception ex) {
                if (Constants.DEBUG) {
                    System.out.println(ex);
                }
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.failed"));
                saveErrors(request, errors);
                return (mapping.findForward("error"));
            }
        }

        handler = StaticQueryHandlerFactory.makeGeneQueryHandler(searchType, searchList);
        if (handler == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }

        try {
            Set cloneids = handler.doQuery(restrictions, clonetypes, species, -1, -1);

            if ((cloneids.size() + totalFoundCloneCount) > GeneQueryHandler.MAXCLONECOUNT) {
                return (mapping.findForward("summary"));
            }

            handler.restoreClones(null, true, cloneids);
            List founds = handler.convertFoundToCloneinfo();
            List nofounds = handler.getNofound();
            totalFoundCloneCount = handler.getFoundCloneCount();
            int numOfNoFounds = nofounds.size();
            handler.setInCartClones(founds, shoppingcart);
            request.getSession().setAttribute("numOfFound", new Integer(totalFoundCloneCount));
            request.getSession().setAttribute("numOfNoFounds", new Integer(numOfNoFounds));
            request.getSession().setAttribute("found", founds);
            request.getSession().setAttribute("nofound", nofounds);
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.failed"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
    }
}
