/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import controller.AddReagentsController;
import dao.ContainerDAO;
import dao.ReagentDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import transfer.ContainerheaderTO;
import transfer.ReagentTO;
import transfer.SampleTO;

/**
 *
 * @author DZuo
 */
public class AddControlsBean {
    private String message;
    private ContainerheaderTO container;
    private String label;
    private Collection<SampleTO> samples;
    //private List<List> containerTable;
    private DataModel plateModel;
    private DataModel headerModel;
    
    public AddControlsBean() {
        setMessage("");
    }
    
    public List<SelectItem> getControlreagents() {
        List<SelectItem> controlreagents = new ArrayList<SelectItem>();

        try {
            List<ReagentTO> reagents = ReagentDAO.getReagents(ReagentTO.getTYPE_CONTROL());
            controlreagents.add(new SelectItem(ReagentTO.NOT_SELECTED, ReagentTO.NOT_SELECTED));
            for (ReagentTO r : reagents) {
                SelectItem i = new SelectItem((new Integer(r.getReagentid())).toString(), r.getName());
                //SelectItem i = new SelectItem(r.getName(), r.getName());
                controlreagents.add(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Cannot get controls from database.");
        }
        return controlreagents;
    }

    public String viewContainer() {
        try {
            ContainerheaderTO c = ContainerDAO.getContainer(getLabel(), true, true, true, true);
            setContainer(c);
            setSamples(c.getSamples());
            setModels(c);
            
            return "addControls";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage(ex.getLocalizedMessage());
            return null;
        }
    }
    
    public void setModels(ContainerheaderTO c) {
            List containertable = TableModelBean.convertContainerToDatamodel(c);
            setPlateModel(new ListDataModel(containertable));
            
            List l = new ArrayList();
            for(int i=0; i<c.getContainertype().getNumofcol(); i++) {
                l.add(new Integer(i+1));
            }
            setHeaderModel(new ListDataModel(l));
    }
    
    public SampleTO getRowLabel() {
        SampleTO rowLabel = null;
        
        if(getPlateModel().isRowAvailable()) {
            
            List list = (List)getPlateModel().getRowData();
            
            rowLabel = (SampleTO)list.get(0);
            
        }
        
        return rowLabel;
    }
    
    public SampleTO getPlateValue() {
        SampleTO plateValue = null;
        
        if(getPlateModel().isRowAvailable() && getHeaderModel().isRowAvailable()) {
            
            List list = (List)getPlateModel().getRowData();
            
            plateValue = (SampleTO)list.get(getHeaderModel().getRowIndex());
            
        }
        return plateValue;
    }
    
    public String saveControls() {
        AddReagentsController controller = new AddReagentsController();
        try {
            List<ReagentTO> samples = controller.getControlSamples(getSamples());
            controller.persistControlSamples(samples);
            return "success";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage(ex.getMessage());
            return null;
        }
    }
    
    public boolean isSample() {
        if(getPlateValue() == null)
            return false;
        
        return true;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ContainerheaderTO getContainer() {
        return container;
    }

    public void setContainer(ContainerheaderTO container) {
        this.container = container;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DataModel getPlateModel() {
        if(plateModel==null)
            setModels(getContainer());
        return plateModel;
    }

    public void setPlateModel(DataModel plateModel) {
        this.plateModel = plateModel;
    }

    public DataModel getHeaderModel() {
        return headerModel;
    }

    public void setHeaderModel(DataModel headerModel) {
        this.headerModel = headerModel;
    }

    public Collection<SampleTO> getSamples() {
        return samples;
    }

    public void setSamples(Collection<SampleTO> samples) {
        this.samples = samples;
    }
}
