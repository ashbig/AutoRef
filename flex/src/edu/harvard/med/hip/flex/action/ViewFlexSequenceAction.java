/*
 * File : ViewFlexSequenceAction.java
 * Classes : ViewFlexSequenceAction
 *
 * Description :
 *
 *      The action called to display a sequence to the user, the sequence id
 *      must be in the request under <code>Constants.FLEX_SEQUENCE_ID_KEY</code>.
 *
 *
 * Author : Juan Munoz (jmunoz@3rdmill.com)
 *
 * See COPYRIGHT file for copyright information
 *
 *
 * The following information is used by CVS
 * $Revision: 1.4 $
 * $Date: 2001-07-09 18:16:42 $
 * $Author: jmunoz $
 *
 ******************************************************************************
 *
 * Revision history (Started on May 30, 2001) :
 *
 *    Add entries here when updating the code. Remember to date and insert
 *    your 3 letters initials.
 *
 *    05-30-2001 : JMM - Class created.
 *
 */

/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */


package edu.harvard.med.hip.flex.action;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;

/**
 * Class description - JavaDoc 1 liner.
 *
 * Class description - Full description
 *
 * @author     $Author: jmunoz $
 * @version    $Revision: 1.4 $ $Date: 2001-07-09 18:16:42 $
 */

public class ViewFlexSequenceAction extends FlexAction {
    
    
    /**
     * Does the real work for the perform method.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        String flexId = null;
        FlexSequence displaySeq = null;
          try {
            flexId = request.getParameter(Constants.FLEX_SEQUENCE_ID_KEY);
            displaySeq = new FlexSequence(Integer.parseInt(flexId));
            displaySeq.restore(displaySeq.getId());
          } catch (FlexDatabaseException fde) {
              /*
               * if we can't get the info out of the database for the 
               * requested sequence, create an error and report it back to the
               * calling page
               */
              errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error", fde));
              
          } catch(NumberFormatException nfe) {
              /*
               * If the number number is mall formated, report error back to 
               * the calling page.
               */
              errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.number.format", nfe));
                
          } finally {
              // if any error were found, save and return them
              if(! errors.empty()) {
                  saveErrors(request,errors);
                  return new ActionForward(mapping.getInput());
              }
              
          }
            
            
          // format the sequence in fasta and make the cds part red
            String seqText = displaySeq.getSequencetext();
            StringBuffer formatedHTMLSeq = new StringBuffer();
            // keep track of the sequence index
            int seqIndex = 1;
            for(int i = 0;i<seqText.length();i++) {
                
                if(seqIndex == displaySeq.getCdsstart()) {
                    formatedHTMLSeq.append("<FONT COLOR=\"red\">");
                }
                if(seqIndex == displaySeq.getCdsstop()+1) {
                    formatedHTMLSeq.append("</FONT>");
                }
                
                if(seqIndex%FlexSequence.FASTA_BASES_PER_LINE == 0) {
                    formatedHTMLSeq.append("\n");
                }
                
                formatedHTMLSeq.append(seqText.charAt(i));
                seqIndex++;
                
            }
            
            // put the formated sequence into the request
            request.setAttribute(Constants.FASTA_COLOR_SEQUENCE_KEY,
                formatedHTMLSeq.toString().trim());
            
            
            // put the FLEX sequence object into the requset as well
            request.setAttribute(Constants.FLEX_SEQUENCE_KEY, displaySeq);
        
            return mapping.findForward("success");
    } //end flexPerform
    
} // End class ViewFlexSequenceAction


/*
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
|<---            this code is formatted to fit into 80 columns             --->|
 */
