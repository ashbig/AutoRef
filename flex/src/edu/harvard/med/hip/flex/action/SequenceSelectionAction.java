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
        Hashtable searchResult = (Hashtable)request.getSession().getAttribute("searchResult");
        String [] selections = ((CustomerRequestForm)form).getCheckOrder();
        if(selections == null) {         
            return (mapping.findForward("empty"));
        }
             
        Hashtable goodSequences = new Hashtable();
        Hashtable badSequences = new Hashtable();
        Hashtable sameSequence = new Hashtable();
        Hashtable homologs = new Hashtable();
        Hashtable sequences = new Hashtable();
        
        try {
            for(int i=0; i<selections.length; i++) {
                String gi = selections[i];
                FlexSequence sequence = (FlexSequence)searchResult.get(gi);
            
                //for the new sequences, need more info from genbank.
                if(sequence.getFlexstatus().equals("NEW")) {
                    setSequenceInfo(sequence, gi);
                
                    //if the sequence quality is questionable, put it aside.
                    if("QUESTIONABLE".equals(sequence.getQuality())) {
                        badSequences.put(gi, sequence);
                    } else {
                        FlexSeqAnalyzer analyzer = new FlexSeqAnalyzer(sequence);
                        if(analyzer.findSame()) {
                            sameSequence.put(gi, analyzer.getSameSequence());
                            sequences.put(gi, sequence);
                        } else {
                            if(analyzer.findHomolog()) {
                                Homologs h = new Homologs();
                                h.setHomolog(analyzer.getHomolog());
                                h.setBlastResults(analyzer.getBlastResults());
                                homologs.put(gi, h);
                                
                                Enumeration enum = analyzer.getHomolog().elements();
                                while(enum.hasMoreElements()) {
                                    FlexSequence seq = (FlexSequence)enum.nextElement();
                                    sequences.put(seq.getGi(), seq);
                                }
                            } else {
                                goodSequences.put(gi, sequence);
                                sequences.put(gi, sequence);
                            }
                        }
                    }
                } else {
                    goodSequences.put(gi, sequence);
                    sequences.put(gi, sequence);
                }
            }
            
            int count = 0;
            
            if(goodSequences.size() > 0) {
                request.setAttribute("goodSequences", goodSequences);
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
            
            request.getSession().setAttribute("sequences", sequences);
            if(count == 0) {
                return (mapping.findForward("empty"));
            } else {
                return (mapping.findForward("success"));
            }
        } catch (Exception ex) {
            return (mapping.findForward("failure"));
        }    
    }
    
    //call the parser with sequence gid, and set sequence values.
    private void setSequenceInfo(FlexSequence sequence, String gi) throws FlexUtilException {
        GenbankGeneFinder finder = new GenbankGeneFinder();
        Hashtable h = finder.searchDetail(gi);
        sequence.setSpecies((String)h.get("species"));
        int start = ((Integer)h.get("start")).intValue();
        int stop = ((Integer)h.get("stop")).intValue();
        sequence.setCdsstart(start);
        sequence.setCdsstop(stop);
        sequence.setCdslength(stop-start+1);
        sequence.setSequencetext((String)h.get("sequencetext"));
        if(start==-1 || stop == -1) {
            sequence.setCdslength(0);
            sequence.setQuality("QUESTIONABLE");
        }
        else {
            sequence.setQuality("GOOD");
        }
    }    
}
