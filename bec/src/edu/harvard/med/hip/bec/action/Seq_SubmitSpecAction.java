/*
 * Seq_SubmitSpecAction.java
 *
 * Created on October 7, 2002, 3:33 PM
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

import edu.harvard.med.hip.bec.core.spec.*;
import edu.harvard.med.hip.bec.core.sequence.*;
import edu.harvard.med.hip.bec.core.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.Constants;

public class Seq_SubmitSpecAction  extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        int forwardName = ((Seq_GetSpecForm)form).getForwardName();
        String created_spec = null;
        Connection conn = null;
        ArrayList created_specs = new ArrayList();
        try
        {
            //get system user
            User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
            String username = user.getUsername();
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            String spec_name = null;
            
            Hashtable params = new Hashtable();
            //get all parameters
            Enumeration requestNames = request.getParameterNames();
            while(requestNames.hasMoreElements())
            {
                String name=(String)requestNames.nextElement();
                String value=(String)request.getParameter(name);
                
                if (name.equalsIgnoreCase("SET_NAME"))
                {
                    spec_name = value;
                }
                else
                    params.put( name.toUpperCase(), value);
               
            }
            
            //check for name unique, if not add _1 to user selected name
            String spec_name_suffix = Spec.getNameSuffix(spec_name, forwardName );
            spec_name += spec_name_suffix;
           
            switch ( forwardName)
            {
                case EndReadsSpec.END_READS_SPEC_INT:
                {
                    EndReadsSpec spec = new EndReadsSpec(params, spec_name, username);
                    spec.insert(conn);
                    conn.commit();
                    created_specs.add(spec);
                    request.setAttribute("specs", created_specs);
                    request.setAttribute("message", "The following specification was created.");
                    return (mapping.findForward("end_reads_spec"));
                 }
                case FullSeqSpec.FULL_SEQ_SPEC_INT:
                {
                    FullSeqSpec spec = new FullSeqSpec(params, spec_name, username);
                    spec.insert(conn);
                    conn.commit();
                    created_specs.add(spec);
                    request.setAttribute("specs", created_specs);
                    request.setAttribute("message", "The following specification was created.");
                    return (mapping.findForward("full_seq_spec"));
                }
                case PolymorphismSpec.POLYMORPHISM_SPEC_INT:
                {
                    PolymorphismSpec spec = new PolymorphismSpec(params, spec_name, username);
                    spec.insert(conn);
                    conn.commit();
                    created_specs.add(spec);
                    request.setAttribute("specs", created_specs);
                    request.setAttribute("message", "The following specification was created.");
                    return (mapping.findForward("polymorphism_spec"));
                }
                case Primer3Spec.PRIMER3_SPEC_INT:
                {
                    Primer3Spec spec = new Primer3Spec(params, spec_name, username);
                    spec.insert(conn);
                    conn.commit();
                    created_specs.add(spec);
                    request.setAttribute("specs", created_specs);
                    request.setAttribute("message", "The following specification was created.");
                     return (mapping.findForward("primer3_spec"));
                }
                case OligoPair.UNIVERSAL_PAIR_INT:
                {
                    
                    Oligo o_5p = new Oligo(
                    (String) params.get("FORWARD_SEQUENCE"),
                    Double.parseDouble((String) params.get("FORWARD_TM")),
                    (String) params.get("FORWARD_NAME"),
                    Oligo.OT_UNIVERSAL_5p,
                    Integer.parseInt( (String) params.get("FORWARD_START"))
                    );
                    
                    
                    
                    Oligo o_3p = new Oligo(
                    
                    (String) params.get("REVERSE_SEQUENCE"),
                    Double.parseDouble((String) params.get("REVERSE_TM")),
                    (String) params.get("REVERSE_NAME"),
                    Oligo.OT_UNIVERSAL_3p,
                    Integer.parseInt( (String) params.get("REVERSE_START"))
                    );
                    OligoPair olp = new OligoPair(spec_name, OligoPair.UNIVERSAL_PAIR, o_5p,o_3p);
                    
                    
                   // created_spec = "Spec type: UNIVERSAL OLIGO PAIR \nSet Name: "+spec_name;
                    olp.insert(conn);
                    conn.commit();
                    created_specs.add(olp);
                    request.setAttribute("specs", created_specs);
                    request.setAttribute("message", "The following specification was created.");
                    return (mapping.findForward("universal_pair"));
                }
            }
           return (mapping.findForward("error")); 
        }
        catch(Exception e)
        {
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
        finally
        {
            
            if(errors.size() > 0)
            {
                saveErrors(request,errors);
                
                DatabaseTransaction.rollback(conn);
                return (mapping.findForward("error"));
            } 
            else
            {
                DatabaseTransaction.commit(conn);
            }
            
            DatabaseTransaction.closeConnection(conn);
            
        }
    }
}
