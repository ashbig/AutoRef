/*
 * CloneRearrayParamSetAction.java
 *
 * Created on June 6, 2003, 4:29 PM
 */

package edu.harvard.med.hip.flex.action;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.lang.Thread;
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
import org.apache.struts.upload.*;

import edu.harvard.med.hip.flex.form.GenericRearrayForm;
import edu.harvard.med.hip.flex.core.Location;
import edu.harvard.med.hip.flex.workflow.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.user.*;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.core.*;

/**
 *
 * @author  dzuo
 */
public class CloneRearrayParamSetAction extends RearrayParamSetAction {
    protected void setSampleTypeAndProtocol(ActionForm form, RearrayManager manager) {
        int workflow = ((GenericRearrayForm)form).getWorkflow();
        
        if(Workflow.REARRAY_ARCHIVE_GLYCEROL == workflow) {
            manager.setProtocolName(Protocol.REARRAY_ARCHIVE_GLYCEROL);
            manager.setSampleType(Sample.ISOLATE);
            manager.setDestStorageType(StorageType.ARCHIVE);
            manager.setDestStorageForm(StorageForm.GLYCEROL);
        } else if(Workflow.REARRAY_DIST_GLYCEROL == workflow) {
            manager.setProtocolName(Protocol.REARRAY_DIST_GLYCEROL);
            manager.setSampleType(Sample.ISOLATE);
            manager.setDestStorageType(StorageType.DIST);
            manager.setDestStorageForm(StorageForm.GLYCEROL);
        } else if(Workflow.REARRAY_SEQ_GLYCEROL == workflow) {
            manager.setProtocolName(Protocol.REARRAY_SEQ_GLYCEROL);
            manager.setSampleType(Sample.ISOLATE);
        } else if(Workflow.REARRAY_WORKING_GLYCEROL == workflow) {
            manager.setProtocolName(Protocol.REARRAY_WORKING_GLYCEROL);
            manager.setSampleType(Sample.ISOLATE);
            manager.setDestStorageType(StorageType.WORKING);
            manager.setDestStorageForm(StorageForm.GLYCEROL);
        } else if(Workflow.REARRAY_ARCHIVE_DNA == workflow) {
            manager.setProtocolName(Protocol.REARRAY_ARCHIVE_DNA);
            manager.setSampleType(Sample.DNA);
            manager.setDestStorageType(StorageType.ARCHIVE);
            manager.setDestStorageForm(StorageForm.DNA);
        } else if(Workflow.REARRAY_DIST_DNA == workflow) {
            manager.setProtocolName(Protocol.REARRAY_DIST_DNA);
            manager.setSampleType(Sample.DNA);
            manager.setDestStorageType(StorageType.DIST);
            manager.setDestStorageForm(StorageForm.DNA);
        } else if(Workflow.REARRAY_SEQ_DNA == workflow) {
            manager.setProtocolName(Protocol.REARRAY_SEQ_DNA);
            manager.setSampleType(Sample.DNA);
        } else if(Workflow.REARRAY_WORKING_DNA == workflow) {
            manager.setProtocolName(Protocol.REARRAY_WORKING_DNA);
            manager.setSampleType(Sample.DNA);
            manager.setDestStorageType(StorageType.WORKING);
            manager.setDestStorageForm(StorageForm.DNA);
        }
    }
    
    protected void setStorageFormAndType(ActionForm form, RearrayManager manager) {
        String sourceFormat = ((GenericRearrayForm)form).getSourceFormat();
        
        if("workingGlycerol".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.GLYCEROL);
            manager.setStorageType(StorageType.WORKING);
        } else if("workingDna".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.DNA);
            manager.setStorageType(StorageType.WORKING);
        } else if("archiveGlycerol".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.GLYCEROL);
            manager.setStorageType(StorageType.ARCHIVE);
        } else if("archiveDna".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.DNA);
            manager.setStorageType(StorageType.ARCHIVE);
        } else if("glycerol".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.GLYCEROL);
            manager.setStorageType(StorageType.ORIGINAL);
        } else if("dna".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.DNA);
            manager.setStorageType(StorageType.ORIGINAL);
        } else if("currentGlycerol".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.GLYCEROL);
            manager.setStorageType(StorageType.CURRENT);
        } else if("currentDna".equals(sourceFormat)) {
            manager.setStorageForm(StorageForm.DNA);
            manager.setStorageType(StorageType.CURRENT);
        }
    }
    
    protected void addToStorage(ActionForm form, List containers, String storageType, String storageForm) {
        int workflow = ((GenericRearrayForm)form).getWorkflow();
        ThreadedCloneStorageManager manager = new ThreadedCloneStorageManager(containers, storageType, storageForm);
        
        if(Workflow.REARRAY_SEQ_GLYCEROL != workflow && Workflow.REARRAY_SEQ_DNA != workflow) {
            new Thread(manager).start();
        }
    }
}
