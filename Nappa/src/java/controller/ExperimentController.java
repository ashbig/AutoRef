/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.ContainerDAO;
import dao.VariableDAO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import transfer.ContainerheaderTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.VariableTO;

/**
 *
 * @author DZuo
 */
public class ExperimentController extends ProcessController {
    private List<String> labels;
    private List<VariableTO> variables;
    private Collection<ContainerheaderTO> slides;
    
    public ExperimentController() {
        super();
    }
    
    public ExperimentController(ProcessprotocolTO p, ResearcherTO r, Date d, String outcome) {
        super(p,r,d,outcome);
    }
    
    public static List<String> getVariableTypes() {
        return VariableDAO.getVariableTypes();
    }
    
    public List<String> getSlideinfo() throws ControllerException {
        List<String> invalidLabels = new ArrayList<String>();
        invalidLabels.addAll(labels);
        
        try {
            slides = ContainerDAO.getSlides(labels, false, false, false, false);
            List<String> validLabels = new ArrayList<String>();
          
            for(ContainerheaderTO c:slides) {
                if(ContainerheaderTO.getSTATUS_GOOD().equals(c.getStatus()))
                    validLabels.add(c.getBarcode());
            }
            invalidLabels.removeAll(validLabels);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ControllerException("Error getting slide information.\n"+ex.getMessage());
        }
        return invalidLabels;
    }
    
    public void doSpecificProcess() throws ControllerException {
        for(ContainerheaderTO c:slides) {
            c.setObjecttype(ProcessobjectTO.getTYPE_SLIDE());
            c.setIoflag(ProcessobjectTO.getIO_BOTH());
            getPe().addProcessobject(c);
        }
        
        getPe().setVariables(getVariables());
    }
    
    public void persistSpecificProcess(Connection conn) throws ControllerException {
        return;
    }

    public List<VariableTO> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableTO> variables) {
        this.variables = variables;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public Collection<ContainerheaderTO> getSlides() {
        return slides;
    }

    public void setSlides(Collection<ContainerheaderTO> slides) {
        this.slides = slides;
    }
}
