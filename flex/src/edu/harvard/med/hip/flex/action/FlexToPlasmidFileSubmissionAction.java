/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.crypto.NullCipher;
import javax.servlet.*;
import javax.servlet.http.*;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import org.apache.struts.upload.*;
import org.apache.struts.util.MessageResources;

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.util.*;
import edu.harvard.med.hip.flex.form.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.workflow.*;

import edu.harvard.med.hip.flex.infoimport.*;

import static edu.harvard.med.hip.flex.infoimport.ConstantsImport.ITEM_TYPE;
import static edu.harvard.med.hip.flex.infoimport.plasmidimport.PlasmidImporterDefinitions.IMPORT_ACTIONS;
import static edu.harvard.med.hip.flex.workflow.Workflow.WORKFLOW_TYPE;
import static edu.harvard.med.hip.flex.core.growthcondition.BioDefinitions.BIO_UNITS;
import static edu.harvard.med.hip.flex.core.growthcondition.BioDefinitions.BIO_UNITS;
import edu.harvard.med.hip.flex.infoimport.bioinfo.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.*;
import edu.harvard.med.hip.flex.infoimport.plasmidimport.databasemanipulation.*;

/**
 *
 * @author htaycher
 */
public class FlexToPlasmidFileSubmissionAction extends ResearcherAction 
{
    
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public synchronized ActionForward flexPerform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException
    {
             
        ActionErrors errors = new ActionErrors();
        FlexToPlasmidFileForm requestForm= ((FlexToPlasmidFileForm)form);
        String forwardName = requestForm.getForwardName();
        IMPORT_ACTIONS cur_process = IMPORT_ACTIONS.valueOf(forwardName);
          User user = ((User)request.getSession().getAttribute(Constants.USER_KEY));
          ItemsImporter imp = new ItemsImporter();
        imp.setUser(user);
        String map_name= null;   
        try 
        {
             request.setAttribute("forwardName", String.valueOf(forwardName));
             AccessManager manager = AccessManager.getInstance();
             user.setUserEmail( manager.getEmail(user.getUsername()));
              FLEXtoPLASMIDImporter pi = new FLEXtoPLASMIDImporter();
             Connection plasmid_connection=null;
        
             switch(cur_process) 
              {
                 
                 case PLASMID_DICTIONARY_TABLE_POPULATE:
                 {
                    try        {              plasmid_connection = pi.getPLASMIDConnection();            }
                    catch(Exception e)        {             return (mapping.findForward("error"));        }
                    DatabaseTable dt = new DatabaseTable(); 
                     InputStream input = requestForm.getPLATE().getInputStream();
                    DatabaseTable displayTable = dt.insertNewData(plasmid_connection, input );
                    plasmid_connection.commit();  
                    request.setAttribute("displayTable",displayTable);
                    return (mapping.findForward("confirm"));
                 }
                 case  PLASMID_DICTIONARY_TABLE_SHOW_CONTENT :
                 case PLASMID_DICTIONARY_TABLE_STRUCTURE:
                 {
                    String tablename = requestForm.getTablename();
                    try        {              plasmid_connection = pi.getPLASMIDConnection();            }
                    catch(Exception e)        {             return (mapping.findForward("error"));        }
                    DatabaseTable displayTable = getInfo(cur_process, tablename.toUpperCase(), plasmid_connection);
                    request.setAttribute("displayTable",displayTable);
                    return (mapping.findForward("confirm"));
                     
                 }
               case CREATE_SUBMISSION_FILES_SUBMITTED:
                  {
                      
                      ImportRunner creater= new FLEXtoPLASMIDImporter();
                      creater.setUser(user);
                      creater.setDataFilesMappingSchema(requestForm.getPLATE().getInputStream());
                      creater.setProcessType(ConstantsImport.PROCESS_NTYPE.TRANSFER_FLEX_TO_PLASMID_CREATE_FILES);
                      String param=  requestForm.getHoststrain();
                      if (!(param== null || param.trim().length()==0))
                      {
                          ((FLEXtoPLASMIDImporter) creater).setHoststrain1(requestForm.getHoststrain());
                         ((FLEXtoPLASMIDImporter) creater).setHoststrainIsInUse1(requestForm.getHoststrainIsInUse());
                         if ( requestForm.getHoststrainDescription() != null && requestForm.getHoststrainDescription().trim().length()!=0)
                         {
                             ((FLEXtoPLASMIDImporter) creater).setHoststrainDescription1(requestForm.getHoststrainDescription());
                         }
                      }
                      param=  requestForm.getHoststrain1();
                      if ( !(param == null || param.trim().length()==0))
                      {
                        ((FLEXtoPLASMIDImporter) creater).setHoststrain2( requestForm.getHoststrain1());
                         ((FLEXtoPLASMIDImporter) creater).setHoststrainIsInUse2(requestForm.getHoststrainIsInUse1());
                         if ( requestForm.getHoststrainDescription1() != null && requestForm.getHoststrainDescription1().trim().length()!=0)
                         {
                            ((FLEXtoPLASMIDImporter) creater).setHoststrainDescription2(requestForm.getHoststrainDescription1());
                     
                         }
                      }
                    ((FLEXtoPLASMIDImporter) creater).setRestriction(requestForm.getRestriction());
                     ((FLEXtoPLASMIDImporter) creater).setMarker(requestForm.getMarker());
                     ((FLEXtoPLASMIDImporter) creater).setGrowthCondition1(requestForm.getGrowthCondition1());
                     ((FLEXtoPLASMIDImporter) creater).setIsRecomendedGC1(requestForm.getIsRecomendedGC1());
                     if (! requestForm.getGrowthCondition2().equals( BIO_UNITS.NONE.toString()))
                     {
                         ((FLEXtoPLASMIDImporter) creater).setGrowthCondition2(requestForm.getGrowthCondition2());
                          ((FLEXtoPLASMIDImporter) creater).setIsRecomendedGC2(requestForm.getIsRecomendedGC2());
                     }
                     ((FLEXtoPLASMIDImporter) creater).setCloneStatus(requestForm.getCloneStatus()); 
                         ((FLEXtoPLASMIDImporter) creater).setCollections(requestForm.getCloneCollections());
                         ((FLEXtoPLASMIDImporter) creater).setHosttype(requestForm.getHosttype());
                        // this is not on UI yet
                          ((FLEXtoPLASMIDImporter) creater).setCloneStatus( requestForm.getCloneStatus());
                        creater.setInputData(ITEM_TYPE.ITEM_TYPE_PLATE_LABELS, requestForm.getItemids());
                        java.lang.Thread t = new java.lang.Thread(creater);            t.start();
                        request.setAttribute("message",
                        "Information is uploading. It can take some time based on number of clones. The e-mail notification will be sent to you upon completion.");
                         request.setAttribute("title","Clone information transfer from ACE to FLEX");
                        return mapping.findForward("proccessing");
                  }
                
              }
              
             
             
             return (mapping.findForward("nothing"));    
             
        } 
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
    
    private DatabaseTable        getInfo(IMPORT_ACTIONS cur_process, 
            String tablename, Connection plasmid_connection ) throws Exception
    {
       DatabaseTable dt = new DatabaseTable(tablename);   
       List<String> result ;
        switch(cur_process) 
         {
            case  PLASMID_DICTIONARY_TABLE_SHOW_CONTENT :
             {
                dt.getContent(plasmid_connection);
                return dt;
             }
             case PLASMID_DICTIONARY_TABLE_STRUCTURE:
             {
                dt.getStructure(plasmid_connection);
                return dt;
                
             }
            default :return null;
         }
        
    }

}
