/*
 * Seq_GetSpec.java
 *
 * Created on October 7, 2002, 12:51 PM
 */

package edu.harvard.med.hip.bec.action;

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

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;

public class Seq_GetSpecAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
        ArrayList specs = new ArrayList();
        
        
        
        try 
        {
           
            //get system user
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
          
            String username = user.getUsername();
        //    request.setAttribute("forwardName", new Integer(forwardName));
            
            //show spec by id
            if ( forwardName >= Spec.SPEC_SHOW_SPEC)
            {
                int spec_id = forwardName / Spec.SPEC_SHOW_SPEC;
              
                 Spec config = Spec.getSpecById(spec_id);
                 specs.add(config);
                request.setAttribute("specs", specs);
                switch  (config.getType())
                {
                    case EndReadsSpec.END_READS_SPEC_INT:
                        return (mapping.findForward("end_reads_spec"));
                    case FullSeqSpec.FULL_SEQ_SPEC_INT:
                        return (mapping.findForward("full_seq_spec"));
                    case Primer3Spec.PRIMER3_SPEC_INT:
                        return (mapping.findForward("primer3_spec"));
                    case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                        return (mapping.findForward("polymorphism_spec"));
                 }
            }
            
            switch  (forwardName)
            {
                case Spec.END_READS_SPEC_INT:
                {
                    specs = EndReadsSpec.getAllSpecs();
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("end_reads_spec"));
                }
                case Spec.FULL_SEQ_SPEC_INT:
                {
                     specs = FullSeqSpec.getAllSpecs();
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("full_seq_spec"));
                }
                case Primer3Spec.PRIMER3_SPEC_INT:
                {
                     specs = Primer3Spec.getAllSpecs();
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("primer3_spec"));
                }
                case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                {
                     specs = PolymorphismSpec.getAllSpecs();
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("polymorphism_spec"));
                }
                case Spec.END_READS_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:
                {
                    specs = EndReadsSpec.getAllSpecsBySubmitter( user.getId());
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("end_reads_spec"));
                }
                case Spec.FULL_SEQ_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:
                {
                     specs = FullSeqSpec.getAllSpecsBySubmitter(user.getId());
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("full_seq_spec"));
                }
                case Spec.PRIMER3_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:
                {
                     specs = Primer3Spec.getAllSpecsBySubmitter( user.getId());
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("primer3_spec"));
                }
                case Spec.POLYMORPHISM_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:
                {
                     specs = PolymorphismSpec.getAllSpecsBySubmitter( user.getId());
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("polymorphism_spec"));
                }
              /*
               case OligoPair.UNIVERSAL_PAIR_INT:
                {
                    OligoPair op = null;
                    ArrayList o_pairs = op.getOligoPairsByType(OligoPair.UNIVERSAL_PAIR);
                    request.setAttribute("specs", o_pairs);
                    return (mapping.findForward("universal_pair"));
                }
               **/
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
