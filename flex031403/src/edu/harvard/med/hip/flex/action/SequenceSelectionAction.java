/*
 * SequenceSelectionAction.java
 *
 * Created on May 31, 2001, 5:47 PM
 *
 * This class performs the flex database search for the sequences user submitted.
 */

package edu.harvard.med.hip.flex.action;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
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
import java.util.Collections;

/**
 *
 * @author  Dongmei Zuo
 * @version
 */
public class SequenceSelectionAction extends FlexAction {
    
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
        Vector seqs = (Vector)request.getSession().getAttribute("searchResult");
        Hashtable searchResult = new Hashtable();
        for(int i=0; i<seqs.size(); i++) {
            FlexSequence seq = (FlexSequence)seqs.elementAt(i);
            searchResult.put(seq.getGi(), seq);
        }
        
        String [] selections = ((CustomerRequestForm)form).getCheckOrder();
        if(selections == null) {
            return (mapping.findForward("empty"));
        }
        
        Vector newSequences = new Vector();
        Vector badSequences = new Vector();
        Vector sameSequence = new Vector();
        Hashtable homologs = new Hashtable();
        Hashtable cdsMatchSequences = new Hashtable();
        Hashtable sequences = new Hashtable();
        Vector submittedSequences = new Vector();
        
        try {
            for(int i=0; i<selections.length; i++) {
                String gi = selections[i];
                FlexSequence sequence = (FlexSequence)searchResult.get(gi);
                submittedSequences.addElement(sequence);
                
                //for the new sequences, need more info from genbank.
                if(sequence.getFlexstatus().equals(FlexSequence.NEW)) {
                    sequence.setSequenceInfo(gi);
                    //if the sequence quality is questionable, put it aside.
                    if(FlexSequence.QUESTIONABLE.equals(sequence.getQuality())) {
                        badSequences.addElement(sequence);
                    } else {
                        FlexSeqAnalyzer analyzer = new FlexSeqAnalyzer(sequence);
                        if(analyzer.findSame()) {
                            Vector sseqs = analyzer.getSameSequence();
                            
                            //Add the new sequence information to the database.
                            ((FlexSequence)sseqs.elementAt(0)).addPublicInfo(sequence.getPublicInfo());
                            sequence.setId(((FlexSequence)sseqs.elementAt(0)).getId());
                            sequence.setFlexstatus(((FlexSequence)sseqs.elementAt(0)).getFlexstatus());
                            sameSequence.addElement(sequence);
                            sequences.put(gi, sequence);
                        } else {
                            if(analyzer.findHomolog()) {
                                Homologs h = new Homologs();
                                h.setHomolog(analyzer.getHomolog());
                                h.setBlastResults(analyzer.getBlastResults());
                                
                                if(((int)(analyzer.getBlastResults().getPercentIdentity())) == 1 &&
                                ((int)analyzer.getBlastResults().getPercentAlignment()) == 1) {
                                    cdsMatchSequences.put(gi, h);
                                } else {
                                    homologs.put(gi, h);
                                }
                                
                                Enumeration enum = analyzer.getHomolog().elements();
                                while(enum.hasMoreElements()) {
                                    FlexSequence seq = (FlexSequence)enum.nextElement();
                                    sequences.put(seq.getGi(), seq);
                                }
                            } else {
                                newSequences.addElement(sequence);
                                sequences.put(gi, sequence);
                            }
                        }
                    }
                } else {
                    if(FlexSequence.QUESTIONABLE.equals(sequence.getQuality())) {
                        badSequences.addElement(sequence);
                    } else {
                        sameSequence.addElement(sequence);
                        sequences.put(gi, sequence);
                    }
                }
            }
            
            int count = 0;
            
            if(newSequences.size() > 0) {
                request.setAttribute("newSequences", newSequences);
                count++;
            }
            if(badSequences.size() > 0) {
                request.setAttribute("badSequences", badSequences);
                count++;
            }
            if(sameSequence.size() > 0) {
                request.setAttribute("sameSequence", sameSequence);
                count++;
            }
            if(homologs.size() > 0) {
                request.setAttribute("homologs", homologs);
                count++;
            }
            if(cdsMatchSequences.size() > 0) {
                request.setAttribute("cdsMatchSequences", cdsMatchSequences);
                count++;
            }
            if(submittedSequences.size() > 0) {
                request.setAttribute("submittedSequences", submittedSequences);
            }
            
            request.getSession().setAttribute("sequences", sequences);
            if(count == 0) {
                return (mapping.findForward("empty"));
            } else {
                return (mapping.findForward("success"));
            }
        } catch (Exception ex) {
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
    }
}
