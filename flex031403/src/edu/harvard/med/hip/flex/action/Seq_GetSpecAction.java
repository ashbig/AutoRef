/*
 * Seq_GetSpec.java
 *
 * Created on October 7, 2002, 12:51 PM
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author  htaycher
*/



import java.util.*;

import java.sql.*;
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

import edu.harvard.med.hip.flex.seqprocess.spec.*;
import edu.harvard.med.hip.flex.seqprocess.core.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.*;

public class Seq_GetSpecAction extends ResearcherAction
{
    
    
    public ActionForward flexPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        String forwardName = ((Seq_GetSpecForm)form).getForwardName();
        
        try
        {
            if ( forwardName.equals(EndReadsSpec.END_READS_SPEC) ||
                forwardName.equals(FullSeqSpec.FULL_SEQ_SPEC) ||
                forwardName.equals(Primer3Spec.PRIMER3_SPEC ) )
            {
                ArrayList specs = Spec.getAllSpecs(forwardName);

                request.setAttribute("specs", specs);
                request.setAttribute("forwardName", forwardName);
            }
            if ( forwardName.equals(EndReadsSpec.END_READS_SPEC) )
                return (mapping.findForward("end_reads_spec"));
            else if (forwardName.equals(FullSeqSpec.FULL_SEQ_SPEC) )
                return (mapping.findForward("full_seq_spec"));
            else if( forwardName.equals(Primer3Spec.PRIMER3_SPEC ) )
                return (mapping.findForward("primer3_spec"));
            else if( forwardName.equals(OligoPair.UNIVERSAL_PAIR ) )
            {
                OligoPair op = null;
                ArrayList o_pairs = op.getOligoPairsByType(OligoPair.UNIVERSAL_PAIR);
                request.setAttribute("specs", o_pairs);
                return (mapping.findForward("universal_pair"));
            }
            
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
    }
}
