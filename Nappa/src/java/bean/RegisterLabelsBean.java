/*
 * RegisterLabelsBean.java
 *
 * Created on October 18, 2007, 2:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bean;

import controller.RegisterLabelsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.model.SelectItem;
import transfer.ContainerheaderTO;
import transfer.ContainertypeTO;
import util.StringConvertor;

/**
 *
 * @author dzuo
 */
public class RegisterLabelsBean {
    private RegisterLabelsController controller;
    private List<ContainertypeTO> containertypes;
    private String containertype;
    private String labels;
    private boolean status;
    private String message;
    
    /** Creates a new instance of RegisterLabelsBean */
    public RegisterLabelsBean() {
        setStatus(false);
        controller = new RegisterLabelsController();
    }
    
    public ContainertypeTO getContainertypeObject() {
        for(ContainertypeTO container:containertypes) {
            if(container.getType().equals(getContainertype()))
                return container;
        }
        return null;
    }
    
    public void setContainertypes(List<ContainertypeTO> containertypes) {
        this.containertypes = containertypes;
    }
    
    
    public List<SelectItem> getContainertypes() {
        if(containertypes == null) {
            try {
                setContainertypes(getController().getContainertypes());
            } catch (Exception ex) {
                System.out.println(ex);
                return null;
            }
        }
        
        List<SelectItem> types = new ArrayList<SelectItem>();
        for(ContainertypeTO t:containertypes) {
            SelectItem i = new SelectItem(t.getType());
            types.add(i);
        }
        return types;
    }
    
    public String getContainertype() {
        return containertype;
    }
    
    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }
    
    public RegisterLabelsController getController() {
        return controller;
    }
    
    public void setController(RegisterLabelsController controller) {
        this.controller = controller;
    }
    
    public void registerLables() {
        status = true;
        List<String> l = null;
        try {
            l = StringConvertor.convertFromStringToList(getLabels().trim(), "\n");
            Collection <ContainerheaderTO> containers = getController().checkContainers(l);
            if(containers.size()>0) {
                String s = "The following labels exist in the system:\n";
                for(ContainerheaderTO c:containers) {
                    s += c.getBarcode()+"\n";
                }
                throw new Exception(s);
            }
            
            getController().registerContainers(l, getContainertypeObject());
            setMessage("Labels are regstered successfully.");
            setLabels(null);
        } catch (Exception ex) {
            System.out.println(ex);
            setMessage(ex.getMessage());
        }
    }
    
    public String getLabels() {
        return labels;
    }
    
    public void setLabels(String labels) {
        this.labels = labels;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String msg) {
        this.message = msg;
    }
}
