/*
 * AddItemsAction.java
 *
 * Created on August 22, 2007, 3:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.action;

/**
 *
 * @author htaycher
 */

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
 * @author  dzuo
 * @version
 */
public class AddItemsAction extends WorkflowAction {
    
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
        AddItemsForm requestForm= ((AddItemsForm)form);
        int forwardName = requestForm.getForwardName();
        FormFile inputFile = ((AddItemsForm)form).getInputFile();
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        ItemsImporter imp = new ItemsImporter();
        imp.setUser(user);
        String map_name= null;   
        try 
        {
             request.setAttribute("forwardName", String.valueOf(forwardName));
           
              if ( forwardName > 0)
              {
                  return (mapping.findForward("add_items"));
              }
             else 
             {
                 ConstantsImport.fillInNames();
                 imp.setProcessType(forwardName);
                  switch (forwardName)
                 {
                     case -ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE:
                     {
                          imp.setProcessType(ConstantsImport.PROCESS_IMPORT_INTO_NAMESTABLE) ;
                          imp.setInputData(FileStructure.STR_FILE_TYPE_INPUT_FOR_NAME_TABLE, inputFile.getInputStream());
                          imp.run();
                          return (mapping.findForward("confirm_add_items"));
                     }
                     case -ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES:
                     {
                        map_name= FlexProperties.getInstance().getProperty("flex.repository.basedir")
                        + FlexProperties.getInstance().getProperty("ADD_CLONING_STRATEGY_MAP");
                        imp.setDataFilesMappingSchema(map_name);
                        imp.setProcessType(ConstantsImport.PROCESS_IMPORT_CLONING_STRATEGIES) ;
                        imp.setInputData(FileStructure.STR_FILE_TYPE_CLONING_STRATEGY, inputFile.getInputStream());
                        imp.run();
                         return (mapping.findForward("confirm_add_items"));
                     }
                     case -ConstantsImport.PROCESS_IMPORT_LINKERS:
                     {
                         map_name= FlexProperties.getInstance().getProperty("flex.repository.basedir")+
                                 
                                 FlexProperties.getInstance().getProperty("ADD_LINKER_MAP");
                        imp.setDataFilesMappingSchema(map_name);
                         imp.setProcessType(ConstantsImport.PROCESS_IMPORT_LINKERS) ;
                         imp.setInputData(FileStructure.STR_FILE_TYPE_LINKER_INFO, inputFile.getInputStream());
                         imp.run();
                          return (mapping.findForward("confirm_add_items"));
                     }
                     case -ConstantsImport.PROCESS_IMPORT_VECTORS:
                     {
                          map_name= FlexProperties.getInstance().getProperty("flex.repository.basedir")
                          + FlexProperties.getInstance().getProperty("ADD_VECTOR_MAP");
                            imp.setDataFilesMappingSchema(map_name);
                          imp.setProcessType(ConstantsImport.PROCESS_IMPORT_VECTORS) ;
                          imp.setInputData(FileStructure.STR_FILE_TYPE_VECTOR_INFO, inputFile.getInputStream());
                            FormFile vector_feature = ((AddItemsForm)form).getInputFile1();
                          imp.setInputData(FileStructure.STR_FILE_TYPE_VECTOR_FEATURE_INFO, vector_feature.getInputStream());
                            imp.run();
                            return (mapping.findForward("confirm_add_items"));
                     }
                    case -ConstantsImport.PROCESS_PUT_PLATES_FOR_SEQUENCING:
                     {
                         String seq_facility  = ((AddItemsForm)form).getFacilityName();
                         String  container_labels = ((AddItemsForm)form).getPlateLabels();//get from form
        
                         
                         ArrayList containers = Algorithms.splitString(container_labels.trim(), null);
                         if (containers == null || containers.size() == 0 )
                        {
                           errors.add(ActionErrors.GLOBAL_ERROR,  new ActionError("error.container.querry.parameter", "Please enter plate labels"));
                            saveErrors(request,errors);
                            return new ActionForward(mapping.getInput());
                        }
                        String plate_names = putPlatesForSequencing(containers, seq_facility);
                        request.setAttribute("plate_names", plate_names);
                        return (mapping.findForward("confirm_add_items"));
                     }
                   
                     default: return null;
                 }
                 
             }
                 
                 
            
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
    
    private  String  putPlatesForSequencing(ArrayList container_labels, 
            String sequencing_facility_name) throws Exception
    {
        ArrayList messages = new ArrayList();
        String sql_plate_labels =edu.harvard.med.hip.flex.util.Algorithms.convertArrayToSQLString(container_labels);
        String sql_not_submitted_plates = 
                "select  label from containerheader where containerid in"
+" (select distinct containerid from sample where sampleid in "
+" (select sequencingsampleid from clonesequencing where sequencingsampleid in" 
+" (select sampleid from sample where containerid in "
+" (select containerid from containerheader where  label in ( "+ sql_plate_labels + " )))))";


        String sql_insert = "insert into clonesequencing select sequencingid.nextval, 'IN PROCESS', '"
                +sequencing_facility_name +"', sysdate, null, sampleid, cloneid from sample where sampletype = 'ISOLATE'"
                +" and containerid in (select containerid from containerheader where label in (";//+sql_labels +  "))";
        
        sun.jdbc.rowset.CachedRowSet crs = null;
           ArrayList temp = new ArrayList();
          
        try 
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            crs = t.executeQuery(sql_not_submitted_plates);
            while(crs.next()) 
            {
                temp.add(crs.getString("label"));
            }
        } catch (SQLException sqlE) {
            throw new FlexDatabaseException("Error occured while checking for previously submitted plates\n"+sql_not_submitted_plates);
        } finally {
            DatabaseTransaction.closeResultSet(crs);
        }
        
        // now get not processed plates
        ArrayList not_processed_plates = new ArrayList();
        for ( int count = 0; count < container_labels.size(); count++)
        {
            if ( !temp.contains( (String)container_labels.get(count) ) )
            {
                not_processed_plates.add(container_labels.get(count));
               System.out.println("add "+(String)container_labels.get(count));  
            }
        }
        
        if (not_processed_plates.size()==0)
              return "All plates ("+sql_plate_labels+") have been processed before";
   
        sql_plate_labels =Algorithms.convertArrayToSQLString(not_processed_plates);
        Connection conn = null; 
        try
        {
            conn = DatabaseTransaction.getInstance().requestConnection();
            sql_insert +=sql_plate_labels+  "))";
            DatabaseTransaction.executeUpdate(sql_insert,conn);
             return "FLEX is notified that plates \n"+ sql_plate_labels +" have been sequenced." ;
        } catch (Exception sqlE) {
            throw new FlexDatabaseException("Error occured while notifing FLEX that plates were sequenced\n"+sql_insert);
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
     
    
  
}


