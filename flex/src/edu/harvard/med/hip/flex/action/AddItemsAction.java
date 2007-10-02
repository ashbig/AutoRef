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
public class AddItemsAction extends AdminAction {
    
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
    throws ServletException, IOException {
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
                return (mapping.findForward("add_items"));
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
                   
                     default: return null;
                 }
                 
             }
                 
                 
            
        } catch (Exception e) {
            request.setAttribute(Action.EXCEPTION_KEY, e);
            return (mapping.findForward("error"));
        }
    }
    
    
}


