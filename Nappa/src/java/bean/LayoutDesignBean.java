/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import controller.SlideDesignController;
import core.Slidecontainerlineageinfo;
import dao.ProgramDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import process.SlideDesignManager;
import transfer.LayoutcontainerTO;
import transfer.ProgramdefinitionTO;
import transfer.SlidelayoutTO;

/**
 *
 * @author dzuo
 */
public class LayoutDesignBean implements Serializable {
    private SlideDesignController controller;
    private String name;
    private String description;
    private String program1;
    private String program2;
    private String message;
    private SlidelayoutTO layout;
    private HtmlDataTable fromDataTable;
    private HtmlDataTable toDataTable;
    private boolean design;

    private List<SlidelayoutTO> layouts;
    
    public LayoutDesignBean() {
        controller = new SlideDesignController();
    }
    
    public List<SelectItem> getAllPrograms() {
        ProgramDAO dao = new ProgramDAO();
        List<SelectItem> programNames = new ArrayList<SelectItem>();

        try {
            List<ProgramdefinitionTO> programs = dao.getAllPrograms();
            for (ProgramdefinitionTO p : programs) {
                SelectItem i = new SelectItem(p.getName(), p.getName());
                programNames.add(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Cannot get programs.");
        }
        return programNames;
    }

    public void setControls(ValueChangeEvent event) throws AbortProcessingException {
        LayoutcontainerTO selected = null;
        if (getFromDataTable().isRowAvailable()) {
            selected = (LayoutcontainerTO) getFromDataTable().getRowData();
        }
        if (getToDataTable().isRowAvailable()) {
            selected = (LayoutcontainerTO) getToDataTable().getRowData();
        }

        if (selected.isIscontrol()) {
            selected.setIscontrol(false);
        } else {
            selected.setIscontrol(true);
        }
        for (Slidecontainerlineageinfo info : getLayout().getLineageinfo()) {
            List<LayoutcontainerTO> from = info.getFrom();
            List<LayoutcontainerTO> to = info.getTo();

            boolean isfrom = false;
            for (LayoutcontainerTO c : from) {
                if (c == selected) {
                    isfrom = true;
                    break;
                }
            }

            if (isfrom) {
                if (selected.isIscontrol()) {
                    int count = 0;
                    for (LayoutcontainerTO c : from) {
                        if (c.isIscontrol()) {
                            count++;
                        }
                    }
                    if (count != 0 && count == from.size()) {
                        setContainerSampletype(to, true);
                    }
                } else {
                    setContainerSampletype(to, false);
                }
                break;
            }

            boolean isto = false;
            for (LayoutcontainerTO c : to) {
                if (c == selected) {
                    isto = true;
                    break;
                }
            }
            if (isto) {
                int countcheck = 0;
                int countuncheck = 0;
                for (LayoutcontainerTO c : to) {
                    if (c.isIscontrol()) {
                        countcheck++;
                    } else {
                        countuncheck++;
                    }
                }

                if (countcheck != 0 && countcheck == to.size()) {
                    setContainerSampletype(from, true);
                } else if(countuncheck !=0 && countuncheck == to.size()) {
                    setContainerSampletype(from, false);
                }
                break;
            }
        }
        SlideDesignManager manager = new SlideDesignManager();
        List<LayoutcontainerTO> containers = new ArrayList<LayoutcontainerTO>();
        containers.add(selected);
        manager.changeControls(containers);
        putLayoutViewBeanInSession();

        FacesContext.getCurrentInstance().renderResponse();
    }

    private void setContainerSampletype(List<LayoutcontainerTO> containers, boolean b) {
        for (LayoutcontainerTO c : containers) {
            c.setIscontrol(b);
        }
    }
    
    private void putLayoutViewBeanInSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map session = (Map) context.getExternalContext().getSessionMap();
        LayoutViewBean bean = (LayoutViewBean) session.get("layoutViewBean");
        if (bean == null) {
            bean = new LayoutViewBean();
        }
        bean.setLayout(layout);
        bean.setEditcontrol(false);
        session.put("layoutViewBean", bean);
    }

    public String designLayout() {
        if(getName()==null || getName().trim().length()==0) {
            setMessage("Please enter a valid layout name.");
            return null;
        }
        
        setDesign(true);
        try {
            if(controller.checkLayoutName(getName().trim())) {
                setMessage("Layout name has been used: "+getName());
                return null;
            }
                        
            String username = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            layout = controller.designSlideLayout(getName().trim(), getDescription(), username, getProgram1(), getProgram2());
            controller.designSlideLayoutMap(layout);
            putLayoutViewBeanInSession();

            return "layoutview";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while designing the layout");
            return null;
        }
    }

    public String saveLayout() {
        try {
            controller.saveLayout(layout);
            layouts = new ArrayList<SlidelayoutTO>();
            layouts.add(layout);
            return "layoutlist";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while saving layout.");
            return null;
        }
    }

    public String queryLayout() {
        setDesign(false);
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            String layoutname = (String)map.get("name");
            layout = controller.getLayout(layoutname);
            controller.designSlideLayoutMap(layout);
            controller.setControls(layout);
            putLayoutViewBeanInSession();

            return "layoutview";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while designing the layout");
            return null;
        }
    }
    
    public String findLayouts() {
        try {
            layouts = controller.getLayouts(name);
            return "layoutlist";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while searching layouts with name: "+name);
            return null;
        }
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

    public String getProgram1() {
        return program1;
    }

    public void setProgram1(String program1) {
        this.program1 = program1;
    }

    public String getProgram2() {
        return program2;
    }

    public void setProgram2(String program2) {
        this.program2 = program2;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SlidelayoutTO getLayout() {
        return layout;
    }

    public void setLayout(SlidelayoutTO layout) {
        this.layout = layout;
    }

    public HtmlDataTable getFromDataTable() {
        return fromDataTable;
    }

    public void setFromDataTable(HtmlDataTable fromDataTable) {
        this.fromDataTable = fromDataTable;
    }

    public HtmlDataTable getToDataTable() {
        return toDataTable;
    }

    public void setToDataTable(HtmlDataTable toDataTable) {
        this.toDataTable = toDataTable;
    }

    public SlideDesignController getController() {
        return controller;
    }

    public void setController(SlideDesignController controller) {
        this.controller = controller;
    }

    public boolean isDesign() {
        return design;
    }

    public void setDesign(boolean design) {
        this.design = design;
    }

    public List<SlidelayoutTO> getLayouts() {
        return layouts;
    }

    public void setLayouts(List<SlidelayoutTO> layouts) {
        this.layouts = layouts;
    }
}
