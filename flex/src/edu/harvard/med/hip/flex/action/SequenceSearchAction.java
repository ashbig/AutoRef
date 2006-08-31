/*
 * SequenceSearchAction.java
 *
 * Created on May 30, 2001, 5:47 PM
 *
 * This class performs the genbank search when user submits the search string.
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.io.IOException;
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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;

/**
 *
 * @author  Dongmei Zuo
 * @version 
 */
public class SequenceSearchAction extends FlexAction {

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
    public ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException {
        String searchString = ((CustomerRequestForm)form).getSearchString(); 
        String species = ((CustomerRequestForm)form).getSpecies();
        if(!"all".equals(species)) {
            searchString = searchString + "+AND+" + species;
        }
        
        try {
            GenbankGeneFinder finder = new GenbankGeneFinder();
            Vector returnedSequences = finder.search(searchString);
            Vector sequences = new Vector();
            Enumeration enu = returnedSequences.elements();
            while(enu.hasMoreElements()) {
                GenbankSequence sequence = (GenbankSequence)enu.nextElement();
                String accession = sequence.getAccession();
                String gi = sequence.getGi();
                String desc = sequence.getDescription();
                FlexSequence newSequence = sequence.toFlexSequence();
                sequences.addElement(newSequence);
            }
            
            //Sort the sequences based on the flex status.
            Collections.sort(sequences, new FlexSeqStatusComparator());
            
            if(sequences.size()==0) {
                return (mapping.findForward("emptysearchresult"));
            }
                               
            HttpSession session = request.getSession();
            session.setAttribute("searchResult", sequences);
            
            return (mapping.findForward("success"));
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
}
