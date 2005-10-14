/*
 * LabelPlatesAction.java
 *
 * Created on July 29, 2005, 11:13 AM
 */

package plasmid.action;


import java.util.*;
import java.io.*;
import java.sql.*;
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

import plasmid.form.*;
import plasmid.coreobject.Container;
import plasmid.process.ContainerProcessManager;
import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class LabelPlatesAction extends InternalUserAction{
    
    /** Creates a new instance of LabelPlatesAction */
    public LabelPlatesAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String type = ((LabelPlatesForm)form).getType();
        int number = ((LabelPlatesForm)form).getNumber();
        String suffix = ((LabelPlatesForm)form).getSuffix();
        
        if(number <= 0) {           
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.labelplate.number"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        
        List containers = new ArrayList();
        for(int i=0; i<number; i++) {
            Container c = new Container(0, type, null, null, Container.UNAVAILABLE, Container.getCapacity(type), Container.EMPTY);
            containers.add(c);
        }
        
        ContainerProcessManager manager = new ContainerProcessManager();
        if(!manager.setContaineridAndLabels(containers, suffix)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot set containerid and label.");
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", "Cannot set containerid and label"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        if(!manager.persistData(containers, null)) {
            if(Constants.DEBUG) {
                System.out.println("Cannot insert data into database.");
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error", "Cannot insert data into database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        List labels = new ArrayList();
        for(int i=0; i<containers.size(); i++) {
            Container c = (Container)containers.get(i);
            String l = c.getLabel();
            labels.add(l);
        }

        request.setAttribute(Constants.LABELS, labels);
        return mapping.findForward("success");
    }   
}
