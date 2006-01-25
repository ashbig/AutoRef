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

public class Seq_GetSpecAction extends BecAction
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
            request.setAttribute("forwardName", new Integer(forwardName));
            request.setAttribute(Constants.JSP_CURRENT_LOCATION, getPageLocation(forwardName));
            request.setAttribute(Constants.JSP_TITLE, getPageTitle(forwardName));
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
                     case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:
                        return (mapping.findForward("slidingwindow_spec"));

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
                case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:
                {
                     specs = SlidingWindowTrimmingSpec.getAllSpecs();
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("slidingwindow_spec"));
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
                case Spec.TRIM_SLIDING_WINDOW_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:
                {
                     specs = SlidingWindowTrimmingSpec.getAllSpecsBySubmitter( user.getId());
                    request.setAttribute("specs", specs);
                    return (mapping.findForward("slidingwindow_spec"));
                }
 // delete spec
                case  Spec.SPEC_DELETE_SPEC:
                {
                    String title = "Delete Configuration";                   String additional_jsp = null;
                     int specid = -1;
                    String spec_id = (String)request.getParameter( "er_specid");
                    if ( spec_id == null) spec_id = (String)request.getParameter( "be_specid");
                    if ( spec_id == null) spec_id = (String) request.getParameter("primer_specid");
                    if ( spec_id == null) spec_id = (String)request.getParameter("polym_specid");
                    if ( spec_id == null) spec_id = (String) request.getParameter("sl_specid");
                    if ( spec_id != null )    specid =  Integer.parseInt( spec_id);
                    if ( specid > 0 )
                    {
                        Connection conn= DatabaseTransaction.getInstance().requestConnection();
                        Spec.deleteSpecById( specid, conn );
                        conn.commit();
                         if(conn != null)            DatabaseTransaction.closeConnection(conn);
                        additional_jsp = "The configuration was deleted.";
                    }
                    else
                    {
                       additional_jsp="Please select configuration you would like to delete.";
                    }
                    request.setAttribute( Constants.JSP_TITLE, title);
                    request.setAttribute( Constants.ADDITIONAL_JSP, additional_jsp);
                    return (mapping.findForward("processing"));
                }


                  case Constants.AVAILABLE_SPECIFICATION_INT:
                {
                    Spec spec = null;
                    String mapping_forw = null;
                    int specid =  Integer.parseInt( (String)request.getParameter( "er_specid"));
                    if ( specid > 0 )
                    {
                        spec = Spec.getSpecById( specid );
                        mapping_forw = "end_reads_spec";
                    }
                    else
                    {
                          specid = Integer.parseInt( (String)request.getParameter( "be_specid"));
                          if ( specid > 0 )
                        {
                            spec = Spec.getSpecById( specid );
                            mapping_forw = "full_seq_spec";
                       }
                        else
                        {
                             specid = Integer.parseInt( (String) request.getParameter("primer_specid"));
                              if ( specid > 0 )
                            {
                                spec = Spec.getSpecById( specid );
                                mapping_forw = "primer3_spec";
                            }
                            else
                            {
                                specid = Integer.parseInt( (String)request.getParameter("polym_specid"));
                                if ( specid > 0 )
                                {
                                    spec = Spec.getSpecById( specid );
                                    mapping_forw = "polymorphism_spec";
                                }
                                else
                                {
                                    specid = Integer.parseInt( (String) request.getParameter("sl_specid"));
                                    if ( specid > 0 )
                                    {
                                        spec = Spec.getSpecById( specid );
                                        mapping_forw = "slidingwindow_spec";
                                    }
                                }
                            }
                        }
                    }

                    if ( spec== null || mapping_forw== null) {}
                    else       specs.add(spec);

                    request.setAttribute("specs", specs);
                    return (mapping.findForward(mapping_forw));
                }

            }

        }
        catch (Exception e)
        {
            //System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        return (mapping.findForward("error"));
    }
    
    
    private String              getPageTitle(int forwardName)
    {
        switch(forwardName)
        {
            case Spec.END_READS_SPEC_INT: return"Clone Ranking ";
            case Spec.FULL_SEQ_SPEC_INT:return "Acceptance Criteria";
            case Primer3Spec.PRIMER3_SPEC_INT:return"Primer Designer Settings";
            case PolymorphismSpec.POLYMORPHISM_SPEC_INT:return"Polymorphism Detector Settings";
            case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:return"Sequence Trimming Settings";
            case Spec.END_READS_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Clone Ranking ";
            case Spec.FULL_SEQ_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Acceptance Criteria";
            case Spec.PRIMER3_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Primer Designer Settings";
            case Spec.POLYMORPHISM_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Polymorphism Detector Settings";
            case Spec.TRIM_SLIDING_WINDOW_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Sequence Trimming Settings";
            case  Spec.SPEC_DELETE_SPEC:return"Delete Specification";
            case Constants.AVAILABLE_SPECIFICATION_INT:return"Process Configurations";
            default: return "";
      
        }
    }
    private String              getPageLocation(int forwardName)
    {
        switch(forwardName)
        {
            case Spec.END_READS_SPEC_INT: return"Home > Analysis Settings > All Available Clone Ranking Settings";
            case Spec.FULL_SEQ_SPEC_INT:return "Home > Analysis Settings > All Available Clone Acceptance Criteria";
            case Primer3Spec.PRIMER3_SPEC_INT:return"Home > Analysis Settings > All Available Primer Designer Settings";
            case PolymorphismSpec.POLYMORPHISM_SPEC_INT:return"Home > Analysis Settings > All Available Polymorphism Detector Settings";
            case SlidingWindowTrimmingSpec.TRIM_SLIDING_WINDOW_SPEC_INT:return"Home > Analysis Settings > All Available Sequence Trimming Settings";
            case Spec.END_READS_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Home > Analysis Settings > User Clone Ranking Settings";
            case Spec.FULL_SEQ_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Home > Analysis Settings > User Clone Acceptance Criteria ";
            case Spec.PRIMER3_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Home > Analysis Settings > User Primer Designer Settings";
            case Spec.POLYMORPHISM_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Home > Analysis Settings > User  Polymorphism Detector Settings";
            case Spec.TRIM_SLIDING_WINDOW_SPEC_INT * Spec.SPEC_SHOW_USER_ONLY_SPECS:return"Home > Analysis Settings > User Sequence Trimming Settings";
            case  Spec.SPEC_DELETE_SPEC:return"Home > Analysis Settings > Delete Specification";
            case Constants.AVAILABLE_SPECIFICATION_INT:return"Hame > View > Process Configurations";
            default: return "";
        }
    }

}
