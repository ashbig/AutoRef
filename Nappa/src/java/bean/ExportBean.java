/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import dao.ContainerDAO;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import process.ContainerManager;
import process.StaticViageneTemplateGeneratorFactory;
import process.ViageneTemplateGenerator;
import transfer.ContainerheaderTO;
import transfer.ContainertypeTO;

/**
 *
 * @author DZuo
 */
public class ExportBean {
    private String label;
    private String template;
    private String message;

    public ExportBean() {
        setLabel(null);
        setMessage(null);
        setTemplate(ViageneTemplateGenerator.BLOCKWISE);
    }
    
    public List<SelectItem> getTemplateList() {
        List<SelectItem> templates = new ArrayList<SelectItem>();
        templates.add(new SelectItem(ViageneTemplateGenerator.BLOCKWISE));
        templates.add(new SelectItem(ViageneTemplateGenerator.MULTIPLEROI));
        templates.add(new SelectItem(ViageneTemplateGenerator.ONEGRID));
       
        return templates;
    }

    public void exportFile() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        response.setContentType("Application/plain");
        response.setHeader("Content-Disposition", "inline;filename="+label+".txt");

        try {
            ContainerheaderTO container = ContainerManager.findContainer(label);
            if(container==null)
                throw new Exception("Cannot find slide: "+label);
            
            if (ContainertypeTO.TYPE_SLIDE.equals(container.getType())) {
                container = ContainerDAO.getContainer(container.getContainerid(), true, true, false, false);
                ContainerDAO.setSlidefeatures(container);
                
                ViageneTemplateGenerator generator = StaticViageneTemplateGeneratorFactory.makeTemplateGenerator(template);
                if(generator == null) {
                    throw new Exception("Cannot get the MicroVigene file template.");
                }
                String output = generator.generateTemplate(container);
                
                ServletOutputStream out = response.getOutputStream();
                out.print(output);
                out.flush();
                out.close();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                throw new Exception("Please enter a slide barcode.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage(ex.getMessage());
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
