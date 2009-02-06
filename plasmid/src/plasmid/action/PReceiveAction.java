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
import org.apache.struts.upload.FormFile;

import plasmid.form.PReceiveForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.database.*;
import plasmid.database.DatabaseManager.*;
import plasmid.database.DatabaseManager.UserManager;

public class PReceiveAction extends Action {

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
        ActionForward af = null;
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_KEY);

        PReceiveForm vif = (PReceiveForm) form;
        String sAction = vif.getSubmit();
        int cloneid = vif.getCloneid();

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager cm = new CloneManager(conn);
            VectorManager vm = new VectorManager(conn);

            if (sAction.equals("Find")) {
                Clone c = cm.queryCloneByCloneid(cloneid);
                if (c == null) {
                    errors.add("PRF", new ActionError("failed.CID.empty"));
                } else {
                    request.setAttribute("Clone", c);
                }
                List hs = vm.getHSs("");
                session.removeAttribute("HS");
                if ((hs != null) && (hs.size() > 0)) {
                    session.setAttribute("HS", hs);
                }    
            } else if (sAction.equals("Submit")) { //Submit
                cm.updateCloneSubmission(cloneid, user.getUserid(),
                        vif.getStatus(), vif.getHs(), vif.getRestriction(), vif.getFile().getFileName(),
                        vif.getSender(), vif.getSdate(), vif.getReceiver(), vif.getRdate());

                FormFile mtaf = vif.getFile();
                String filename = mtaf.getFileName().trim();
                String fPath = getServlet().getServletContext().getRealPath("/") + "PlasmidRepository/file/mta";
                if ((filename != null) && (filename.length() > 0)) {
                    File mtaFile = new File(fPath, filename);
                    FileOutputStream mfOS = new FileOutputStream(mtaFile);
                    mfOS.write(mtaf.getFileData());
                    mfOS.flush();
                    mfOS.close();
                }
            }

            DatabaseTransaction.commit(conn);

        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        af = new ActionForward(mapping.getInput());
        saveErrors(request, errors);
        return (af);
    }
}
