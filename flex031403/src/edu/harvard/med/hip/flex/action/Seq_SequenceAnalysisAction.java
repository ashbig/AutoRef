/*
 * Seq_SequenceAnalysisAction.java
 *
 * Created on November 1, 2002, 10:47 AM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;

import java.sql.*;
import java.io.IOException;
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
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import  edu.harvard.med.hip.flex.seqprocess.core.sequence.*;
import  edu.harvard.med.hip.flex.seqprocess.engine.*;
/**
 *
 * @author  htaycher
 */
public class Seq_SequenceAnalysisAction extends WorkflowAction
{
    
     public ActionForward flexPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
       // String forwardName = ((Seq_SequenceAnalysisRequestForm)form).getForwardName();
        int refseqid = ((Seq_SequenceAnalysisRequestForm)form).getRefseqid();
        int ginumber = ((Seq_SequenceAnalysisRequestForm)form).getGI();
       String expsequence = (String) request.getAttribute("FULLSEQUENCE");
       TheoreticalSequence theoretical_sequence = null;
        String errmessage = null;
      
         System.out.println(ginumber);  System.out.println(refseqid);
        try
        {
            //get theoretical sequence 
            if (refseqid == -1 && ginumber != -1)
            {
                 System.out.println(ginumber);
                theoretical_sequence = TheoreticalSequence.findSequenceByGi(String.valueOf(ginumber));
                errmessage = "<li>No sequence with GI number: "+ ginumber+" exists.</li>";
            }
            else
            {
                 System.out.println(refseqid);
                theoretical_sequence = new TheoreticalSequence (refseqid);
                errmessage = "<li>No sequence with id: "+ refseqid+" exists.</li>";
            }
             System.out.println(theoretical_sequence);
            if ( theoretical_sequence == null)
            {
                System.out.println("al");
               errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errmessage));
               saveErrors(request, errors);
               System.out.println(mapping);
               System.out.println(mapping.getInput());
              
               return new ActionForward(mapping.getInput());
               
            }
            refseqid = theoretical_sequence.getId();
            
            //submit fullsequence if it was submitted
            if ( expsequence != null)
            {
               
                    FullSequence full_sequence = new FullSequence(expsequence,refseqid);
                    
                    DatabaseTransaction t = DatabaseTransaction.getInstance();
                    Connection conn = t.requestConnection();
                    full_sequence.insert(conn);
                   
                    FullSequenceAnalysis seq_for_analysis =
                               new  FullSequenceAnalysis
                                (  
                                    full_sequence, 
                                    theoretical_sequence,
                                    conn                                    
                                );
                    seq_for_analysis.analize();
                    ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
                    request.setAttribute("refseqid", new Integer(refseqid) );
                    request.setAttribute("fullsequences",seq);
                    return (mapping.findForward("sequence_found"));
   
            }
            else
            {
                System.out.println("l");
                ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
                 System.out.println(seq.size());
                if (seq == null || seq.size() == 0)
                {
                    request.setAttribute("sequenceexists","NO");
                    errors.add(ActionErrors.GLOBAL_ERROR,
                            new ActionError("No experimental sequence exists for this reference sequence."));
                    return (mapping.findForward("sequence_not_found"));
                }
                request.setAttribute("refseqid", new Integer(refseqid) );
                request.setAttribute("fullsequences",seq);
                return (mapping.findForward("sequence_found"));
            }
            
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        
    }
    
}
