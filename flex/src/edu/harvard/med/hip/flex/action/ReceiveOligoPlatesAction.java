/*
 * ReceiveOligoPlatesAction.java
 *
 * Created on June 18, 2001, 5:58 PM
 */

package edu.harvard.med.hip.flex.action;

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

import edu.harvard.med.hip.flex.core.*;
import edu.harvard.med.hip.flex.process.*;
import edu.harvard.med.hip.flex.database.*;
import edu.harvard.med.hip.flex.form.ReceiveOligoOrdersForm;
/**
 *
 * @author  Wendy
 * @version
 */
public class ReceiveOligoPlatesAction extends ResearcherAction {
    
    /** Creates new ReceiveOligoPlatesAction */
    public ReceiveOligoPlatesAction() {
    }
    
    /**
     * Does the real work for the perform method which must be overriden by the
     * Child classes.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward flexPerform(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        ActionErrors errors = new ActionErrors();
        String barcode = ((ReceiveOligoOrdersForm)form).getResearcherBarcode();
        String oligoPlateIds = ((ReceiveOligoOrdersForm)form).getOligoPlateIds();
        String receiveDate = ((ReceiveOligoOrdersForm)form).getReceiveDate();
        Researcher researcher = null;
        
        // Validate the researcher barcode.
        try {
            researcher = new Researcher(barcode);
        } catch (FlexProcessException ex) {
            System.out.println(ex);
            errors.add("researcherBarcode", new ActionError("error.researcher.invalid.barcode", barcode));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        } catch (FlexDatabaseException ex) {
            System.out.println(ex);
            request.setAttribute(Action.EXCEPTION_KEY, ex);
            return (mapping.findForward("error"));
        }
        
        
     //   LinkedList oligoPlateIdList = parseOligoPlateId(oligoPlateIds);

       ReceiveOligoOrdersForm formProper = (ReceiveOligoOrdersForm) form;
       //formProper.setOligoPlateIds(oligoPlateIds);
        
        
     //   System.out.println("Total labels entered: "+ oligoPlateIdList.size());
        // validate the input oligo plate labels
        //    try{
        //        if(!plateExist(oligoPlateIdList)) {
        //            errors.add("oligoPlateIds", new ActionError("error.plate.invalid.barcode", oligoPlateIds));
        //            saveErrors(request, errors);
        //            return (new ActionForward(mapping.getInput()));
        //        }
        //    } catch(FlexDatabaseException sqlex) {
        //        System.out.println("Invalid oligo plate barcode input! " +sqlex);
        //    }
        
        
        //request.getSession().setAttribute("protocol", protocol);
       
        List ids = formProper.getOligoPlateList();
        request.getSession().setAttribute("plateList",ids);
        
        
        return (mapping.findForward("success"));
        //return null;
        // return (mapping.findForward("error"));
    } //flexPerform
    
    
    // Validate the source plate barcode.
    private boolean plateExist(LinkedList oligoPlateList) throws FlexDatabaseException {
        boolean isExist = true;
        ResultSet rs = null;
        
        for(int i=0; i<oligoPlateList.size(); i++) {
            String plateId = (String)oligoPlateList.get(i);
            String sql = "SELECT containerid FROM containerheader\n"
            + "WHERE label = '" + plateId + "'";
            
            System.out.println("The plate label is: "+ plateId);
            try {
                DatabaseTransaction t = DatabaseTransaction.getInstance();
                rs = t.executeQuery(sql);
                if (!rs.next()) {
                    isExist = false;
                }
            }catch (SQLException sqlex) {
                sqlex.printStackTrace();
                throw new FlexDatabaseException(sqlex+"\nSQL: "+sql);
            } finally {
                DatabaseTransaction.closeResultSet(rs);
            }
        } //for
        
        return isExist;
    } //PlateExist
    
}
