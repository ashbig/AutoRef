/*
 * Seq_ViewFullSequenceAction.java
 *
 * Created on November 4, 2002, 2:41 PM
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author  htaycher
 */

/*
 * File : ViewFlexSequenceAction.java
 * Classes : ViewFlexSequenceAction
 *
 * Description :
 *
 *      The action called to display a full sequence to the user, the sequence id
 *      must be in the request under <code>Constants.FULL_SEQUENCE_ID_KEY</code>.
 *
 *
 
 */


import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import edu.harvard.med.hip.flex.*;
import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.seqprocess.core.sequence.*;

public class Seq_ViewFullSequenceAction extends FlexAction
{
    
    
    
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        String flexId = null;
        FullSequence displaySeq = null;
        try
        {
            flexId = request.getParameter(Constants.FULL_SEQUENCE_ID_KEY);
            displaySeq = new FullSequence(Integer.parseInt(flexId));
            
        } catch (FlexDatabaseException fde)
        {
              /*
               * if we can't get the info out of the database for the
               * requested sequence, create an error and report it back to the
               * calling page
               */
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", fde));
            
        } catch(NumberFormatException nfe)
        {
              /*
               * If the number number is mall formated, report error back to
               * the calling page.
               */
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.number.format", nfe));
            
        }
        finally
        {
            // if any error were found, save and return them
            if(! errors.empty())
            {
                saveErrors(request,errors);
                return new ActionForward(mapping.getInput());
            }
            
        }
        
        
        // format the sequence in fasta and make the cds part red
        String seqText = displaySeq.getText();
        StringBuffer formatedHTMLSeq = new StringBuffer();
        // keep track of the sequence index
        int seqIndex = 1;
        /*
        for(int i = 0;i<seqText.length();i++)
        {
            
            if(seqIndex == displaySeq.getCdsstart())
            {
                formatedHTMLSeq.append("<FONT COLOR=\"red\">");
            }
            if(seqIndex == displaySeq.getCdsstop()+1)
            {
                formatedHTMLSeq.append("</FONT>");
            }
            
            if(seqIndex%FlexSequence.FASTA_BASES_PER_LINE == 0)
            {
                formatedHTMLSeq.append("\n");
            }
            
            formatedHTMLSeq.append(seqText.charAt(i));
            seqIndex++;
            
        }
         **/
        formatedHTMLSeq.append(displaySeq.getText());
        // put the formated sequence into the request
        request.setAttribute(Constants.COLOR_SEQUENCE_KEY,  formatedHTMLSeq.toString().trim());
        // put the sequence object into the requset as well
        request.setAttribute(Constants.FULL_SEQUENCE_KEY, displaySeq);
        
        return mapping.findForward("success");
    } //end flexPerform
    
} // End class Seq_ViewFullSequenceAction


