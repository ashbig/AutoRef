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
public class AddNewPlatesFromFileAction extends AdminAction 
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
              
              request.setAttribute("forwardName", String.valueOf(forwardName));

              System.out.println(forwardName +" "+researcher_barcode +" "+ plateType +" "+sampleType 
    +" "+plateLocation +" "+ isPutOnQueue +" "+ isDefineConstructSizeBySequence+" "+
    isCheckTargetSequenceInFLEX+" "+isFillInCLoneTables
    +" "+ mapFile.getFileName()+" "+inputFile .getFileName()+" "+inputSequence .getFileName()+" "+ inputGene.getFileName()+" "+inputAuthor.getFileName());


                 researcher = new Researcher(researcher_barcode);
                 
                  importer.setUser(user);
                importer.setResearcher(researcher);
                importer.setPlateType("96 WELL PLATE");
                importer.setProjectId(requestForm.getProjectid());
                importer.setWorkFlowId(requestForm.getWorkflowid());
                importer.setProtocolId(requestForm.getProcessid());
                importer.setNumberOfWellsInContainer( Integer.parseInt((String)request.getAttribute("number_of_wells")));
                importer.isCheckInFLEXDatabase(requestForm.getIsCheckTargetSequenceInFLEX());
                importer.isFillInClonesTables(requestForm.getIsFillInCLoneTables());
                importer.isPutOnQueue(requestForm.getIsPutOnQueue());
                importer.isDefineContructTypeByNSequence(requestForm.getIsDefineConstructSizeBySequence());
                importer.setSampleBioType(requestForm.getSampleType());
                importer.setDataFilesMappingSchema(requestForm.getMapFile().getInputStream());
                importer.setProcessType(ConstantsImport.PROCESS_IMPORT_OUTSIDE_CONTAINERS_INTO_FLEX);
                importer.setPlatesLocation(requestForm.getLocation());   
                importer.isGetFLEXSequenceFromNCBI(requestForm.getIsGetFLEXSequenceFromNCBI());
                importer.isFLEXSequenceIDGI(requestForm.getIsFLEXSequenceIDGI());


                  if(inputSequence ==null && inputGene==null && inputAuthor==null)
                  {
                        importer.setInputData(FileStructure.STR_FILE_TYPE_ONE_FILE_SUBMISSION, inputFile.getInputStream());
                  }
                  else
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
                  }
               //    importer.run();
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
   

