/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import dao.ReagentDAO;
import java.util.Map;
import javax.faces.context.FacesContext;
import transfer.ReagentTO;

/**
 *
 * @author dzuo
 */
public class ReagentViewBean {
    private ReagentTO reagent;
    private int reagentid;
    
    public String viewReagent() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            reagentid = Integer.parseInt((String)requestMap.get("reagentid"));
            reagent = ReagentDAO.getReagent(reagentid);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "viewreagent";
    }

    public int getReagentid() {
        return reagentid;
    }

    public void setReagentid(int reagentid) {
        this.reagentid = reagentid;
    }
    
    public ReagentTO getReagent() {
        return reagent;
    }

    public void setReagent(ReagentTO reagent) {
        this.reagent = reagent;
    }
}
