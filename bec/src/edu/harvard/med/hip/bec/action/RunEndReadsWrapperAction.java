/*
 * RunEndReadsWrapper.java
 *
 * Created on April 7, 2003, 1:53 PM
 */

package edu.harvard.med.hip.bec.action;

import java.sql.*;
import java.io.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.bec.coreobjects.spec.*;
import edu.harvard.med.hip.bec.coreobjects.sequence.*;
import edu.harvard.med.hip.bec.coreobjects.oligo.*;
import edu.harvard.med.hip.bec.database.*;
import edu.harvard.med.hip.bec.form.*;
import edu.harvard.med.hip.bec.user.*;
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

/**
 *
 * @author  htaycher
 */
public class RunEndReadsWrapperAction extends ResearcherAction
{
    
    
    public ActionForward becPerform(ActionMapping mapping,
                        ActionForm form,
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ServletException, IOException
    {
        // place to store errors
        ActionErrors errors = new ActionErrors();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        // start new thread here
        
        
        //create request object
        //create processexecution objects per plate
        //change master plate status
        //extract naming file and submit it to the wrapper module
        return null;
    }
    
}
