/*
 * Seq_SubmitSpecAction.java
 *
 * Created on October 7, 2002, 3:33 PM
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

public class Seq_SubmitSpecAction  extends ResearcherAction
{
    
    
    public ActionForward flexPerform(ActionMapping mapping,
                                    ActionForm form,
                                    HttpServletRequest request,
                                    HttpServletResponse response)
                                    throws ServletException, IOException
    {
        ActionErrors errors = new ActionErrors();
        String forwardName = ((Seq_GetSpecForm)form).getForwardName();
        String created_spec = null;
        try
        {
            
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection conn = t.requestConnection();
            
            String spec_name = null;
           
            Hashtable params = new Hashtable();
            //get all parameters
            Enumeration requestNames = request.getParameterNames();
            while(requestNames.hasMoreElements())
            {
                String name=(String)requestNames.nextElement();
                String value=(String)request.getParameter(name);
                if ( !name.equalsIgnoreCase("submit") && !name.equalsIgnoreCase("set_name") ||
                    !name.equalsIgnoreCase("forwardName")  )
                {    
                    params.put( name, value);
                    System.out.println(name +"-"+value);
                }
                if (name.equalsIgnoreCase("SET_NAME")) spec_name = value;
            }

             System.out.println(spec_name);
            
            if ( forwardName.equals(EndReadsSpec.END_READS_SPEC) )
            {
                EndReadsSpec spec = new EndReadsSpec(params, spec_name);
                spec.insert(conn);
            }
            else if (forwardName.equals(FullSeqSpec.FULL_SEQ_SPEC) )
            {
                FullSeqSpec spec = new FullSeqSpec(params, spec_name);
                spec.insert(conn);
            }
            else if( forwardName.equals(Primer3Spec.PRIMER3_SPEC ) )
            {
                Primer3Spec spec = new Primer3Spec (params, spec_name);
                spec.insert(conn);
            }
            else if( forwardName.equals(OligoPair.UNIVERSAL_PAIR ) )
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
                
                
                created_spec = "Spec type: UNIVERSAL OLIGO PAIR \nSet Name: "+spec_name;
                olp.insert(conn);  
                conn.commit();
            }
            request.setAttribute("created_spec", created_spec);                 
            return (mapping.findForward("success"));
        }
        catch(Exception e)
        {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    
    }
    
}
