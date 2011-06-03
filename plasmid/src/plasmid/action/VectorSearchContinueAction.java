/*
 * VectorSearchContinueAction.java
 *
 * Created on February 7, 2006, 11:05 AM
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

import plasmid.form.VectorSearchForm;
import plasmid.form.RefseqSearchForm;
import plasmid.Constants;
import plasmid.process.QueryProcessManager;
import plasmid.coreobject.Clone;
import plasmid.coreobject.User;
import plasmid.coreobject.CloneVector;

/**
 *
 * @author  DZuo
 */
public class VectorSearchContinueAction extends Action {
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
        
        String display = ((VectorSearchForm)form).getDisplay();
        String species = ((VectorSearchForm)form).getSpecies();
        List clones = ((VectorSearchForm)form).getClones();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
                
        QueryProcessManager manager = new QueryProcessManager();
        
        if(Constants.BUTTON_DISPLAY.equals(display)) {
            List vectornames = ((VectorSearchForm)form).getVectornames();
            List vectors = ((VectorSearchForm)form).getVectors();
            Set checkedVectornames = new TreeSet();
            List checkedVectors = new ArrayList();
            for(int i=0; i<vectornames.size(); i++) {
                boolean vectornameBoolean = ((VectorSearchForm)form).getVectornameBoolean(i);
                if(vectornameBoolean) {
                    String vectorname = (String)vectornames.get(i);
                    checkedVectornames.add(vectorname);
                    CloneVector v = (CloneVector)vectors.get(i);
                    checkedVectors.add(v);
                }
            }
            clones = manager.queryClonesByVector(user, checkedVectornames, species, false);
            
            ((VectorSearchForm)form).setVectornames(checkedVectors);
            ((VectorSearchForm)form).setVectors(checkedVectors);
            ((VectorSearchForm)form).setClones(clones);
            ((VectorSearchForm)form).resetVectornameBooleanValues(checkedVectors);
            request.setAttribute("numberOfClones", new Integer(clones.size()));
            
            return (new ActionForward(mapping.getInput()));
        }
       
        RefseqSearchForm f = new RefseqSearchForm();
        f.setPage(1);
        f.setPagesize(Constants.PAGESIZE);
        f.setDisplayPage("indirect");
        f.setSpecies(species);
        f.setForward("vectorSearchResult");
        request.getSession().setAttribute("refseqSearchForm", f);
        
        List found = manager.queryCloneInfosByClones(clones);
        
        if(found == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.query.notfound"));
            saveErrors(request, errors);
            return (mapping.findForward("error"));
        }
        
        int totalCount = found.size();
        request.getSession().setAttribute("totalCount", new Integer(totalCount));
        request.getSession().setAttribute("numOfFound", new Integer(totalCount));
        request.getSession().setAttribute("found", found);
        
        return (mapping.findForward("success"));
    }
}

