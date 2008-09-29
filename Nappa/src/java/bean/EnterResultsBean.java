/*
 * EnterResultsBean.java
 *
 * Created on December 10, 2007, 3:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bean;

import controller.EnterResultController;
import core.Well;
import dao.ContainerDAO;
import dao.ResearcherDAO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import transfer.ContainerheaderTO;
import transfer.ResulttypeTO;
import transfer.SampleTO;
import util.StringConvertor;

/**
 *
 * @author dzuo
 */
public class EnterResultsBean {
    private static final int ROWNUM = 8;
    private static final int COLNUM = 12;
    
    private List<ResulttypeTO> resultypes;
    private String resulttype;
    private String labelstring;
    private EnterResultController controller;
    private boolean shownofound;
    private DataModel containerModel;
    private String message = null;
    
    /** Creates a new instance of EnterResultsBean */
    public EnterResultsBean() {
        reset();
    }
    
    public void reset() {
        setShownofound(false);
        setController(new EnterResultController());
        setLabelstring(null);
        setContainerModel(null);
    }
    
    public List<SelectItem> getResulttypes() {
        if(getResultypes() == null) {
            try {
                setResultypes(ContainerDAO.getResulttypes());
            } catch (Exception ex) {
                System.out.println(ex);
                return null;
            }
        }
        
        List<SelectItem> types = new ArrayList<SelectItem>();
        for(ResulttypeTO t:resultypes) {
            SelectItem i = new SelectItem(t.getType());
            types.add(i);
        }
        return types;
    }
    
    public String parsefiles() {
        setMessage(null);
        try {
            String username = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            List slabels = StringConvertor.convertFromStringToList(getLabelstring(), "\n");
            getController().setLabels(slabels);
            getController().setResulttype(getResulttype());
            getController().setWho(ResearcherDAO.getResearcher(username));
            setShownofound(false);
            getController().readFiles();
            getController().processFilenames();
            getController().retrieveContainers();
            getController().doProcess();
            
            if(getController().getNofoundLabels().size()>0) {
                setShownofound(true);
            }
            
            Collection<ContainerheaderTO> containers = getController().getContainers();
            List containerResults = new ArrayList<ContainerResultViewBean>();
            for(ContainerheaderTO c:containers) {
                List mappingsInTable = convertContainerToDatamodel(c);
                
                List l = new ArrayList();
                for(int i=0; i<getCOLNUM(); i++) {
                    l.add(new Integer(i+1));
                }
                containerResults.add(new ContainerResultViewBean(c.getBarcode(), new ListDataModel(l), new ListDataModel(mappingsInTable)));
            }
            
            setContainerModel(new ListDataModel(containerResults));
        } catch (Exception ex) {
            setMessage(ex.getMessage());
            return null;
        }
        
        return "next";
    }
    
    public String enterResults() {
        setMessage("Results were entered successfully.");
        
        try {
            getController().persistProcess();
        } catch (Exception ex) {
            setMessage(ex.getMessage());
        }
        
        reset();
        return "successful";
    }
    
    public List convertContainerToDatamodel(ContainerheaderTO c) {
        List mappingsInTable = new ArrayList<List>();
        for(int i=0; i<getROWNUM(); i++) {
            List<SampleTO> cols = new ArrayList<SampleTO>();
            for(int j=0; j<getCOLNUM(); j++) {
                SampleTO s = c.getSample(Well.convertWellToVPos(i+1, j+1, getROWNUM()));
                cols.add(s);
            }
            mappingsInTable.add(cols);
        }
        return mappingsInTable;
    }
    
    public String getContainerbarcode() {
        String platelabel = null;
        
        if(getContainerModel().isRowAvailable()) {
            
            ContainerResultViewBean containerresult = (ContainerResultViewBean)getContainerModel().getRowData();
            
            platelabel = (String)containerresult.getBarcode();
            
        }
        return platelabel;
    }
    
    public List<ResulttypeTO> getResultypes() {
        return resultypes;
    }
    
    public void setResultypes(List<ResulttypeTO> resultypes) {
        this.resultypes = resultypes;
    }
    
    public String getResulttype() {
        return resulttype;
    }
    
    public void setResulttype(String resulttype) {
        this.resulttype = resulttype;
    }
    
    public String getLabelstring() {
        return labelstring;
    }
    
    public void setLabelstring(String labelstring) {
        this.labelstring = labelstring;
    }
    
    public static int getROWNUM() {
        return ROWNUM;
    }
    
    public static int getCOLNUM() {
        return COLNUM;
    }
    
    public EnterResultController getController() {
        return controller;
    }
    
    public void setController(EnterResultController controller) {
        this.controller = controller;
    }
    
    public boolean isShownofound() {
        return shownofound;
    }
    
    public void setShownofound(boolean shownofound) {
        this.shownofound = shownofound;
    }
    
    public DataModel getContainerModel() {
        return containerModel;
    }
    
    public void setContainerModel(DataModel containerModel) {
        this.containerModel = containerModel;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
