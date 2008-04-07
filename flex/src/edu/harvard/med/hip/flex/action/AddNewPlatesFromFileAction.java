/*
 * AddNewPlatesAction.java
 *
 * Created on August 28, 2007, 10:24 AM
 *
 * To change this template, choose Tools | Template Manager
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
import edu.harvard.med.hip.flex.infoimport.bioinfo.*;
import edu.harvard.med.hip.flex.infoimport.file_mapping.*;
/**
 *
 * @author htaycher
 */
public class AddNewPlatesFromFileAction extends WorkflowAction 
{
    
   public synchronized ActionForward flexPerform(ActionMapping mapping,
                        ActionForm form,
                        HttpServletRequest request,
                        HttpServletResponse response)
                        throws ServletException, IOException 
   {
        ActionErrors errors = new ActionErrors();
        AddNewPlatesFromFileForm requestForm= (AddNewPlatesFromFileForm)form;
        int forwardName = requestForm.getForwardName();
         User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
         try 
         {
            switch (forwardName)
            {
                case ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX:

                  {
                  
                       Protocol  protocol = new Protocol(  Protocol.CREATE_CULTURE_FROM_MGC);
                   
                        Workflow workflow = new Workflow(Workflow.MGC_PLATE_HANDLE_WORKFLOW);
                        Project project = new Project(Project.PROJECT_NAME_MGC);

                        request.setAttribute("projectid", new Integer(project.getId()));
                        request.setAttribute("projectname", project.getName());

                        request.setAttribute("workflowname", workflow.getName());
                        request.setAttribute("workflowid", new Integer(Workflow.MGC_PLATE_HANDLE_WORKFLOW));
                        request.setAttribute("processname", Protocol.UPLOAD_CONTAINERS_FROM_FILE);
                        request.setAttribute("processid", new Integer(protocol.getId()));
                        request.setAttribute("submissintype", new Integer(OutsidePlatesImporter.SUBMISSION_TYPE_MGC));
                       return (mapping.findForward("submit_mgc_plates"));
                 
              }
            case -ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX:
            {
                Researcher researcher = null;
                String researcher_barcode = requestForm.getResearcherBarcode();
                OutsidePlatesImporter importer = new OutsidePlatesImporter();
                String plateType = requestForm.getPlateType();//"96 Well Plate";
                String sampleType = requestForm.getSampleType();
                int plateLocation = requestForm.getLocation();
 

              boolean isPutOnQueue = requestForm.getIsPutOnQueue();
              boolean isDefineConstructSizeBySequence= requestForm.getIsDefineConstructSizeBySequence();
              boolean isCheckTargetSequenceInFLEX= requestForm.getIsCheckTargetSequenceInFLEX();
              boolean isFillInCLoneTables= requestForm.getIsFillInCLoneTables();
              
 FormFile mapFile = requestForm.getMapFile();
 FormFile inputFile = requestForm.getInputFile();
 FormFile  inputSequence = requestForm.getInputFile1();
 FormFile inputGene= requestForm.getInputGene();
 FormFile inputAuthor= requestForm.getInputAuthor();
 FormFile inputAuthorConnection= requestForm.getInputAuthorConnection();
 FormFile inputPublication= requestForm.getInputPublication();
 FormFile inputPublicationConnection= requestForm.getInputPublicationConnection();

 request.setAttribute("forwardName", String.valueOf(forwardName));

 int project_id = requestForm.getProjectid();
 int workflow_id =  requestForm.getWorkflowid();
 int protocol_id = requestForm.getProcessid();

                 researcher = new Researcher(researcher_barcode);
                 
                  importer.setUser(user);
                importer.setResearcher(researcher);
                importer.setPlateType("96 WELL PLATE");
                importer.setProjectId(project_id);
                importer.setWorkFlowId(workflow_id);
                importer.setProtocolId(protocol_id);
              
                int submission_type = requestForm.getSubmissionType();
                importer.setNumberOfWellsInContainer( requestForm.getNumberOfWells());
                importer.isCheckInFLEXDatabase(requestForm.getIsCheckTargetSequenceInFLEX());
                importer.isFillInClonesTables(isFillInCLoneTables);
                importer.isPutOnQueue(isPutOnQueue);
                importer.isDefineContructTypeByNSequence(isDefineConstructSizeBySequence);
                importer.setSampleBioType(sampleType );
                importer.setDataFilesMappingSchema(mapFile.getInputStream());
                importer.setProcessType(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
                importer.setPlatesLocation(plateLocation);   
                importer.isGetFLEXSequenceFromNCBI(requestForm.getIsGetFLEXSequenceFromNCBI() );
                importer.isFLEXSequenceIDGI(requestForm.getIsFLEXSequenceIDGI());
                importer.isInsertControlNegativeForEmptyWell(requestForm.getIsInsertControlNegativeForEmptyWell());
                
                 switch  (submission_type)
               {
                   case  OutsidePlatesImporter.SUBMISSION_TYPE_PSI:
                   {
                      if ( inputSequence== null || inputFile == null  )
                            throw new Exception("You need to submit sequence information file and plate mapping information file. Check input");
                       importer.setInputData(FileStructure.STR_FILE_TYPE_PLATE_MAPPING, inputFile.getInputStream());
                      importer.setInputData(FileStructure.STR_FILE_TYPE_SEQUENCE_INFO, inputSequence.getInputStream());
                      if ( inputGene != null) importer.setInputData(FileStructure.STR_FILE_TYPE_GENE_INFO, inputGene.getInputStream());
                      if ( ( inputAuthor!= null && inputAuthorConnection== null ) ||
                               (inputAuthor== null && inputAuthorConnection != null))
                          throw new Exception("Problem with submitted files: check for files that contain author info!");
                      if ( inputAuthor!= null) importer.setInputData(FileStructure.STR_FILE_TYPE_AUTHOR_INFO, inputAuthor.getInputStream());
                      if ( inputAuthorConnection != null) importer.setInputData(FileStructure.STR_FILE_TYPE_AUTHOR_CONNECTION, inputAuthorConnection.getInputStream());
                      if ( inputPublication!= null) importer.setInputData(FileStructure.STR_FILE_TYPE_PUBLICATION_INFO,inputPublication.getInputStream());
                      if ( inputPublicationConnection!= null) importer.setInputData(FileStructure.STR_FILE_TYPE_PUBLICATION_CONNECTION, inputPublicationConnection.getInputStream());
                       break;
                   }
                    case  OutsidePlatesImporter.SUBMISSION_TYPE_MGC:
                    case  OutsidePlatesImporter.SUBMISSION_TYPE_ONE_FILE:
                    {
                        importer.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, inputFile.getInputStream());
                        break;
                    }
                  case  OutsidePlatesImporter.SUBMISSION_TYPE_REFSEQUENCE_LOCATION_FILES:
                  {
                        if ( inputSequence== null || inputFile == null  )
                                        throw new Exception("You need to submit sequence information file and plate mapping information file. Check input.");
                        importer.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, inputFile.getInputStream());
                        importer.setInputData(FileStructure.STR_FILE_TYPE_REFERENCE_SEQUENCE_INFO, inputSequence.getInputStream());
                    break;
                  }
            }
            importer.setSubmissionType(submission_type);
            java.lang.Thread t = new java.lang.Thread(importer);
            t.start();
             return mapping.findForward("confirm_add_plates");
          }
        default: return null;
        }
           
        } 
        catch (Exception e)
        {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
}
   

