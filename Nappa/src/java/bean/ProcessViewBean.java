/*
 * ProcessViewBean.java
 *
 * Created on October 30, 2007, 11:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package bean;

import controller.ProcessController;
import dao.ContainerDAO;
import dao.FilereferenceDAO;
import dao.ReagentDAO;
import java.io.Serializable;
import java.util.Map;
import javax.faces.context.FacesContext;
import transfer.ContainerheaderTO;
import transfer.SlideTO;
import transfer.FilereferenceTO;
import transfer.ProcessexecutionTO;
import transfer.ProcessobjectTO;
import transfer.ReagentTO;

/**
 *
 * @author dzuo
 */
public class ProcessViewBean implements Serializable {

    private ProcessexecutionTO p;

    /** Creates a new instance of ProcessViewBean */
    public ProcessViewBean() {
    }

    public String viewProcess() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            int executionid = Integer.parseInt((String) map.get("executionid"));

            setP(ProcessController.getProcess(executionid, true, false));
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "viewprocess";
    }

    public String viewObject() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            Map session = context.getExternalContext().getSessionMap();
            Map request = context.getExternalContext().getRequestMap();
            int objectid = Integer.parseInt((String) map.get("objectid"));
            String objecttype = (String) map.get("objecttype");

            if (objecttype.equals(ProcessobjectTO.getTYPE_FILEREFERENCE())) {
                FileViewBean bean = new FileViewBean();
                request.put("FileViewBean", bean);
                FilereferenceTO file = FilereferenceDAO.getFilereference(objectid);
                bean.setFile(file);
                return "viewfile";
            }

            if (objecttype.equals(ProcessobjectTO.getTYPE_CONTAINERHEADER())) {
                ContainerViewBean bean = (ContainerViewBean) session.get("ContainerViewBean");
                if (bean == null) {
                    bean = new ContainerViewBean();
                    session.put("ContainerViewBean", bean);
                }
                ContainerheaderTO c = ContainerDAO.getContainer(objectid, true, true, true, true);
                bean.setContainerheader(c);
                return bean.viewContainerNoID();
            }

            if (objecttype.equals(ProcessobjectTO.getTYPE_SLIDE())) {
                ContainerViewBean bean = (ContainerViewBean) session.get("ContainerViewBean");
                if (bean == null) {
                    bean = new ContainerViewBean();
                    session.put("ContainerViewBean", bean);
                }
                SlideTO c = ContainerDAO.getSlideWithBlocks(objectid);
                bean.setSlide(c);
                return bean.viewSlideNoID();
            }

            if (objecttype.equals(ProcessobjectTO.getTYPE_REAGENT())) {
                ReagentViewBean bean = new ReagentViewBean();
                request.put("ReagentViewBean", bean);
                ReagentTO reagent = ReagentDAO.getReagent(objectid);
                bean.setReagent(reagent);
                return "viewreagent";
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    public ProcessexecutionTO getP() {
        return p;
    }

    public void setP(ProcessexecutionTO p) {
        this.p = p;
    }
}
