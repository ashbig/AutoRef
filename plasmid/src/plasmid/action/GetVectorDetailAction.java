/*
 * GetVectorDetailAction.java
 *
 * Created on May 3, 2005, 10:43 AM
 */

package plasmid.action;

import java.io.*;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.Constants;
import plasmid.form.VectorDetailForm;
import plasmid.coreobject.*;

/**
 *
 * @author  DZuo
 */
public class GetVectorDetailAction extends Action {

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
    public ActionForward perform(ActionMapping mapping,
    ActionForm form,
    HttpServletRequest request,
    HttpServletResponse response)
    throws ServletException, IOException { 
        
        // get the parameters specified by the customer
        ActionErrors errors = new ActionErrors();
        int vectorid = ((VectorDetailForm)form).getVectorid();
        DatabaseTransaction dt = null;
        Connection conn = null;
        try {
            dt = DatabaseTransaction.getInstance();
            conn = dt.requestConnection();
            VectorManager manager = new VectorManager(conn);
            CloneVector v = manager.queryCloneVector(vectorid);
            if(v == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.database.error","Database error occured."));
                return (mapping.findForward("error")); 
            }
            String filename = printVectorXML(v);
            
        System.out.println("3:"+filename);
            request.setAttribute("vector", v);
            request.setAttribute("vectorfilename", filename);
            return mapping.findForward("success");
        } catch (Throwable th) {
            if(Constants.DEBUG)
                System.out.println(th);
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database.error","Database error occured."));
            return (mapping.findForward("error")); 
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }    
    
    private String printVectorXML(CloneVector v) throws IOException {
        char[] cs = {'#', '%', '&', '*', '{', '}', '\\', ':', '<', '>', '?', '/', '+'};
        String name = v.getName();
        System.out.println("1:"+name);
        for(int i=0; i<cs.length; i++) {
            char c = cs[i];
            name.replace(c, '-');
        }
        System.out.println("2:"+name);
        
        String file = Constants.FILE_PATH+"vector/"+name+".xml";
        PrintWriter out = new PrintWriter(new FileWriter(new File(file)));
        out.print(v.getVectorMapXML());
        out.close();
        return name+".xml";
    }
}
