/*
 * PrintLabelsAction.java
 *
 * Created on July 29, 2005, 4:27 PM
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

import plasmid.form.PrintLabelsForm;
import plasmid.Constants;
import plasmid.util.PrintLabel;

/**
 *
 * @author  DZuo
 */
public class PrintLabelsAction extends InternalUserAction{
    
    /** Creates a new instance of PrintLabelsAction */
    public PrintLabelsAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List labels = ((PrintLabelsForm)form).getLabels();        
        request.setAttribute(Constants.LABELS, labels);
        
        for(int i=0; i<labels.size(); i++) {
            String label = (String)labels.get(i);
            String s = PrintLabel.execute(label);
            System.out.println(s);
        }
        
        request.setAttribute(Constants.PRINT_LABEL_MESSAGE, "Labels printed.");
        return mapping.findForward("success");
    }  
}
