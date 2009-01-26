/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import controller.AddReagentsController;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import transfer.ReagentTO;

/**
 *
 * @author DZuo
 */
public class AddReagentBean {

    private String type;
    private String name;
    private String description;
    private String message;

    public AddReagentBean() {
        setMessage(null);
        setName(null);
        setDescription(null);
        setType(ReagentTO.TYPE_MASTERMIX);
    }

    public List<SelectItem> getReagenttypes() {
        List<SelectItem> types = new ArrayList<SelectItem>();
        types.add(new SelectItem(ReagentTO.TYPE_MASTERMIX, ReagentTO.TYPE_MASTERMIX));
        types.add(new SelectItem(ReagentTO.getTYPE_CONTROL(), ReagentTO.getTYPE_CONTROL()));
        return types;
    }

    public void addReagent() {
        if(getName()==null || getName().trim().length()==0)
            return;
        
        getName().trim();
        AddReagentsController controller = new AddReagentsController();

        try {
            if (controller.isReagentExist(getName())) {
                setMessage("Reagent exists.");
            } else {
                ReagentTO reagent = new ReagentTO(getName(), getType(), getDescription());
                controller.addReagent(reagent);
                setMessage("Reagent is added.");
                setName(null);
                setDescription(null);
                setType(ReagentTO.TYPE_MASTERMIX);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured.\n" + ex.getMessage());
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
