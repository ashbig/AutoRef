/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import controller.ExperimentController;
import dao.ResearcherDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import transfer.ProcessexecutionTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.VariableTO;
import util.StringConvertor;

/**
 *
 * @author DZuo
 */
public class ExperimentBean implements Serializable {
    private Date date;
    private String labels;
    private String type;
    private String value;
    private String extra;
    private List<VariableTO> variables;
    private boolean showVariables;
    private String message;
    private List<String> barcodes;

    public ExperimentBean() {
        reset();
    }
    
    public void reset() {
        variables = new ArrayList<VariableTO>();
        message = null;
        labels = null;
        date = null;
        value = null;
        extra = null;
        showVariables = false;
    }
    
    public List<SelectItem> getVariableTypes() {
        List<String> types = ExperimentController.getVariableTypes();
        List<SelectItem> items = new ArrayList<SelectItem>();
        for(String t:types) {
            items.add(new SelectItem(t, t));
        }
        return items;
    }
    
    public String addVariable() {
        if(value == null || value.trim().length()==0) {
            message = "Please enter a variable value.";
            return null;
        }
            
        VariableTO v = new VariableTO(type,value,extra);
        this.variables.add(v);
        setValue(null);
        setExtra(null);
        return null;
    }
    
    public String performExperiment() {
        if(this.date == null) {
            message = "Please enter a valid date.";
            return null;
        }
        if(this.labels == null || this.labels.trim().length()==0) {
            message = "Please enter at least one valid barcode.";
            return null;
        }
        
        try {
            setBarcodes((List<String>) StringConvertor.convertFromStringToList(labels, null));
            String username = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            ResearcherTO researcher = ResearcherDAO.getResearcher(username);
            ProcessprotocolTO protocol = new ProcessprotocolTO(ProcessprotocolTO.SET_EXP, null, null);
            ExperimentController controller = new ExperimentController(protocol, researcher, date, ProcessexecutionTO.getOUTCOME_SUCCESS());
            controller.setVariables(variables);
            controller.setLabels(getBarcodes());
            List<String> invalidLabels = controller.getSlideinfo();
            if(invalidLabels.size()>0) {
                setMessage("The following slides are not printed: "+StringConvertor.convertFromListToString(invalidLabels));
                return null;
            }
            controller.doProcess();
            controller.persistProcess();
            setMessage("The following experiement is successfully stored.");
        } catch (Exception ex) {
            setMessage(ex.getMessage());
            return null;
        }
        
        return "successful";
    }
    
    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public List<VariableTO> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableTO> variables) {
        this.variables = variables;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isShowVariables() {
        if(variables.size()>0)
            return true;
        else
            return false;
    }

    public void setShowVariables(boolean showVariables) {
        this.showVariables = showVariables;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(List<String> barcodes) {
        this.barcodes = barcodes;
    }

}
