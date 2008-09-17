/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import dao.ReagentDAO;
import java.util.List;
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
    private List<ReagentTO> mastermix;
    
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

    public String viewAllMastermix() {
        loadMastermix();
        return "viewmastermix";
    }
    
    private void loadMastermix() {
        try {
            List<ReagentTO> reagents = ReagentDAO.getReagents(ReagentTO.TYPE_MASTERMIX);
            setMastermix(reagents);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public List<ReagentTO> getMastermix() {
        if(mastermix==null)
            loadMastermix();
        return mastermix;
    }

    public void setMastermix(List<ReagentTO> mastermix) {
        this.mastermix = mastermix;
    }
}
