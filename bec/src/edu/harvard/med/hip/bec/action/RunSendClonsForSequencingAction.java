/*
 * RunSendColenForSequencingAction.java
 *
 * Created on April 8, 2003, 2:31 PM
 * this action is called
 1. prepare information for user master plates + number of not sequences isolates
 2. for creating sequencing plates for sequencing for outside facility
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
import edu.harvard.med.hip.bec.util.*;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.sampletracking.mapping.*;
import edu.harvard.med.hip.bec.sampletracking.objects.*;

public class RunSendClonsForSequencingAction extends ResearcherAction
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
        int       mode = -1;//get from form
       
        /*
        
        // The database connection used for the transaction
        Connection conn = null;
        ArrayList master_plates = new ArrayList();
        
        try
        {
            //get master plates from db
            for (int count =0; count < master_container_ids.size(); count++)
            {
                master_plates.add(new Container(Integer.parseInt( (String)master_container_ids.get(count))));
            }
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
            Container newcontainer = null;
            ProcessObject processObj = null;ProcessExecution process =null;
            for (int count = 0; count < master_plates.size(); count++)
            {
                Container master_plate = (Container) master_plates.get(count);
                master_plate.updateStatus(Container.STATUS_ER_PROCESS, conn);
                 // Process object
                process = new ProcessExecution( BecIDGenerator.BEC_OBJECT_ID_NOTSET,
                            ProcessDefinition.CODE_END_READ_CONTAINER_CREATIONS,
                            ProcessDefinition.END_READ_CONTAINER_CREATIONS,
                            actionrequest.getId(),
                            null,
                            Constants.TYPE_ID) ;
                processes.add(process);
                if (isForward)
                {
                    newcontainer = createEndReadPlate(master_plate, true, false, process.getExecutionId(),  true ,  files );
                    setPrimer(newcontainer, forward_primerid);
                    newcontainer.setStatus(Container.STATUS_ER_PROCESS);
                    newcontainer.insert(conn);
                    processObj =   new ProcessObject(newcontainer.getId(), process.getExecutionId(), ProcessObject.OUTPUT, ProcessObject.OBJECT_TYPE_CONTAINER);
                    processObj.insert(conn);
                }
                 if (isReverse)
                {
                    newcontainer = createEndReadPlate(master_plate, false, true, process.getExecutionId(),  true ,  files );
                    setPrimer(newcontainer, reverse_primerid);
                    newcontainer.setStatus(Container.STATUS_ER_PROCESS);
                    newcontainer.insert(conn);
                    processObj =   new ProcessObject(newcontainer.getId(), process.getExecutionId(), ProcessObject.OUTPUT, ProcessObject.OBJECT_TYPE_CONTAINER);
                    processObj.insert(conn);
                }
                //process object for master plate
                processObj =   new ProcessObject(master_plate.getId(), process.getExecutionId(), ProcessObject.INPUT, ProcessObject.OBJECT_TYPE_CONTAINER);
                processObj.insert(conn);
                
            }
            
             //          finally we must insert all new objects
              
            actionrequest.insert(conn);
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
        */ return null;
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
      
    public static Container createEndReadPlate(Container master_container,
                    boolean forward, boolean reverse, int executionid ,
                    boolean write_rearrayfile , ArrayList files )
                    throws BecDatabaseException,IOException,BecUtilException
    {
        ContainerCopyer cp =new ContainerCopyer();
        cp.setExecutionId(executionid);
        File fl = null;
        
        
        String label = null;
        if ( reverse)
        {
            //   cp.setDestinationContainerWellNumber()  ;
            cp.setContainerType(Container.TYPE_ER_REVERSE_CONTAINER)  ;
            label = master_container.labelParsing()[0]+master_container.labelParsing()[1]+Container.SUF_ER_REVERSE_CONTAINER;
             cp.doMapping( master_container, label, write_rearrayfile );
            //wrtie naming file
            fl =  ActionExecution.writeNamingFile((Container)cp.getNewContainers().get(0),".R00",  null);
            if (fl != null) files.add(fl);
            //write robot file
            fl = RearrayFileEntry.createRearrayFile(cp.getRearrayFileEntries());
            if (fl != null) files.add(fl);
        }
        if (forward )
        {
            cp.setContainerType(Container.TYPE_ER_FORWARD_CONTAINER)  ;
            label = master_container.labelParsing()[0]+master_container.labelParsing()[1]+Container.SUF_ER_FORWARD_CONTAINER;
             cp.doMapping( master_container, label, write_rearrayfile ) ;
            //wrtie naming file
            fl =  ActionExecution.writeNamingFile((Container)cp.getNewContainers().get(0),".F00",  null);
            if (fl != null) files.add(fl);
            //write robot file
            fl = RearrayFileEntry.createRearrayFile(cp.getRearrayFileEntries());
            if (fl != null) files.add(fl);
        }
        return (Container) cp.getNewContainers().get(0);
    }
    
    
    //function sets oligo id for each sample to universal primer id
    private void setPrimer(Container newcontainer, int primerid)
    {
        for (int count = 0; count < newcontainer.getSamples().size(); count++)
        {
            Sample s = (Sample) newcontainer.getSamples().get(count);
            s.setOligoid(primerid);
        }
    }
      **/
}

