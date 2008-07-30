/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import controller.SlideDesignController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import transfer.SlidelayoutTO;
import transfer.SlidetemplateTO;

/**
 *
 * @author dzuo
 */
public class TemplateDesignBean {

    private SlideDesignController controller;
    private boolean design;
    private String name;
    private String description;
    private String layoutname;
    private String message;
    private SlidetemplateTO template;
    private List<SlidetemplateTO> templates;
    private HtmlDataTable fromDataTable;
    private HtmlDataTable toDataTable;

    public TemplateDesignBean() {
        this.controller = new SlideDesignController();
    }

    public List<SelectItem> getAllLayouts() {
        List<SelectItem> layoutList = new ArrayList<SelectItem>();

        try {
            List<SlidelayoutTO> layouts = controller.getLayouts();
            for (SlidelayoutTO l : layouts) {
                SelectItem i = new SelectItem(l.getName(), l.getName());
                layoutList.add(i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Cannot get layouts.");
        }
        return layoutList;
    }

    public String designTemplate() {
        if(getName() == null || getName().trim().length()==0) {
            setMessage("Please enter a valid template name.");
            return null;
        }
        
        try {
            if(controller.checkTemplateName(getName().trim())) {
                setMessage("Template name has been used: "+getName());
                return null;
            }
                        
            String username = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();

            SlidelayoutTO layout = controller.getLayout(layoutname);
            controller.designSlideLayoutMap(layout);
            controller.setControls(layout);
            setDesign(true);
            setTemplate(controller.designSlideTemplate(layout, name, description, username));
            putLayoutViewBeanInSession(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured");
            return null;
        }
        return "templateview";
    }

    private void putLayoutViewBeanInSession(boolean editcontrol) {
        FacesContext context = FacesContext.getCurrentInstance();
        Map session = (Map) context.getExternalContext().getSessionMap();
        LayoutViewBean bean = (LayoutViewBean) session.get("layoutViewBean");
        if (bean == null) {
            bean = new LayoutViewBean();
        }
        bean.setLayout(template.getLayout());
        bean.setEditcontrol(editcontrol);
        session.put("layoutViewBean", bean);
    }

    public String saveTemplate() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map session = (Map) context.getExternalContext().getSessionMap();
            LayoutViewBean bean = (LayoutViewBean) session.get("layoutViewBean");
            if(bean == null) {
                setMessage("Session expired. Please start over again.");
                return null;
            }
            
            template.setLayout(bean.getLayout());
            controller.saveTemplate(template);
            setTemplates(new ArrayList<SlidetemplateTO>());
            getTemplates().add(template);
            return "templatelist";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while saving template.");
            return null;
        }
    }

    public String queryTemplate() {
        setDesign(false);
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map map = context.getExternalContext().getRequestParameterMap();
            String templatename = (String) map.get("name");
            template = controller.getTemplate(templatename);
            controller.designSlideLayoutMap(template.getLayout());
            controller.setControls(template.getLayout());
            putLayoutViewBeanInSession(false);

            return "templateview";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while getting the template");
            return null;
        }
    }

    public String findTemplates() {
        try {
            templates = controller.getTemplates(name);
            return "templatelist";
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Error occured while searching templates with name: " + name);
            return null;
        }
    }

    public boolean isDesign() {
        return design;
    }

    public void setDesign(boolean design) {
        this.design = design;
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

    public SlideDesignController getController() {
        return controller;
    }

    public void setController(SlideDesignController controller) {
        this.controller = controller;
    }

    public String getLayoutname() {
        return layoutname;
    }

    public void setLayoutname(String layoutname) {
        this.layoutname = layoutname;
    }

    public SlidetemplateTO getTemplate() {
        return template;
    }

    public void setTemplate(SlidetemplateTO template) {
        this.template = template;
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

    public List<SlidetemplateTO> getTemplates() {
        return templates;
    }

    public void setTemplates(List<SlidetemplateTO> templates) {
        this.templates = templates;
    }
}
