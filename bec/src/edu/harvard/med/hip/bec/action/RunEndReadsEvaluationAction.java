/*
 * RunEndReadsEvaluationAction.java
 *
 * Created on April 7, 2003, 1:48 PM
 */

/*
 * CreatEndReadContainersAction.java
 *
 * Created on April 4, 2003, 3:34 PM
 */

package edu.harvard.med.hip.bec.action;

/**
 *
 * @author  htaycher
 */
import java.util.*;

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

public class RunEndReadsEvaluationAction extends ResearcherAction
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
        
        // The form holding the status changes made by the user
        ArrayList master_container_ids = null;//get from form
        int     specid_penalty_values = -1;//get from form
        int    specid_maximum_values = -1;//get from form
        boolean isRunPolymorphismFinder = false;//
        int     specid_polymorphism = -1;
        
        
        // The database connection used for the transaction
        Connection conn = null;
        ArrayList master_plates = new ArrayList();
        
        try
        {
           
            //request object
            ArrayList processes = new ArrayList();
            Request actionrequest = new Request(BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                                new java.util.Date(),
                                user.getId(),
                                processes,
                                Constants.TYPE_OBJECTS);
           
            // conncection to use for transactions
            conn = DatabaseTransaction.getInstance().requestConnection();
            
            // create a new processes Object per each master plate in
            // er container out
            
       
            
             /*
              * finally we must insert all new objects
              */
           
            // commit the transaction
            conn.commit();
            // if we get here, we are error free
            //request.setAttribute(Constants.APPROVED_SEQUENCE_LIST_KEY, "");
            return mapping.findForward("success");
            
        }
        
        catch(Exception ex)
        {
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.process.error", ex));
            DatabaseTransaction.rollback(conn);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            DatabaseTransaction.rollback(conn);
            return (mapping.findForward("error"));
        }
        finally
        {
            DatabaseTransaction.closeConnection(conn);
        }
        
    }
    
    
    
    
     /*-	new request-
        new process(es) records
         1.	new ER Container
         2.	sample lineage
         3.	new processObject per ER container
         4.	new plateset record per master platenaming file for sequencing
      *
      *Master plate status – in ER
      * function create new ER containers , rearray file for each plate and naming file for each plate
      */
  
   
}

    
