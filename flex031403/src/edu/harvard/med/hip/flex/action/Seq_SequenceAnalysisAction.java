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
       String searchTerm = ((Seq_SequenceAnalysisRequestForm)form).getSearchType();
        int searchValue = ((Seq_SequenceAnalysisRequestForm)form).getSearchValue();
       String expsequence = ((Seq_SequenceAnalysisRequestForm)form).getFullsequence();//(String) request.getAttribute("FULLSEQUENCE");
       TheoreticalSequence theoretical_sequence = null;
        String errmessage = null;
      
      
        try
        {
           
            //get theoretical sequence 
            if (searchTerm.equalsIgnoreCase(FullSequenceAnalysis.SEARCH_BY_GI ))
            {
                
                 theoretical_sequence = TheoreticalSequence.findSequenceByGi(searchValue);
                 
                errmessage = "<li>No sequence with GI number: "+ searchValue+" exists.</li>";
           
            }
            else
            {
               
                theoretical_sequence = new TheoreticalSequence (searchValue);
                errmessage = "<li>No sequence with id: "+ searchValue+" exists.</li>";
            }
          
            if ( theoretical_sequence == null || 
                theoretical_sequence.getText() == null || 
                    theoretical_sequence.getText().equals(""))
            {
                System.out.println("get out");
               errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.serchterm.wrong"));
               saveErrors(request, errors);
               return new ActionForward(mapping.getInput());
               
            }
            int refseqid = theoretical_sequence.getId();
            
            //submit fullsequence if it was submitted
            if ( expsequence != null && !expsequence.equals("") )
            {
              
                    FullSequence full_sequence = new FullSequence(expsequence,refseqid);
                    
                    DatabaseTransaction t = DatabaseTransaction.getInstance();
                    Connection conn = t.requestConnection();
                    full_sequence.insert(conn);
                    conn.commit();
                    FullSequenceAnalysis seq_for_analysis =
                               new  FullSequenceAnalysis
                                (  
                                    full_sequence, 
                                    theoretical_sequence,
                                    conn                                    
                                );
                    seq_for_analysis.analizeUsingNeedle();
            }
            ArrayList seq = TheoreticalSequence.getFullSequences(refseqid);
            System.out.println("aaa"+seq.size());
            if (seq == null || seq.size() == 0)
            {
                System.out.println("aaa"+seq.size());
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.sequenceproject.nosequence"));
                saveErrors(request, errors);
                return new ActionForward(mapping.getInput());
            }
            System.out.println("aaa"+seq.size());
            request.setAttribute("refseqid", new Integer(theoretical_sequence.getId()) );
            request.setAttribute("fullsequences",seq);
            return (mapping.findForward("sequence_found"));
  
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        
    }
    
}
