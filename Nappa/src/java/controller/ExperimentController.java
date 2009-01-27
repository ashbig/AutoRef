/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.VariableDAO;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.SlideTO;
import transfer.VariableTO;

/**
 *
 * @author DZuo
 */
public class ExperimentController extends ProcessController {
    private List<String> labels;
    private List<VariableTO> variables;
    private List<SlideTO> slides;
    
    public ExperimentController() {
        super();
    }
    
    public ExperimentController(ProcessprotocolTO p, ResearcherTO r, Date d, String outcome) {
        super(p,r,d,outcome);
    }
    
    public static List<String> getVariableTypes() {
        return VariableDAO.getVariableTypes();
    }
    
    public List<String> getSlideinfo() {
        List<String> invalidLabels = new ArrayList<String>();
        //get slide with sample from db
        //return list of invalid labels
        return invalidLabels;
    }
    
    public void doSpecificProcess() throws ControllerException {
        getPe().setVariables(getVariables());
    }
    
    public void persistSpecificProcess(Connection conn) throws ControllerException {
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

    public List<SlideTO> getSlides() {
        return slides;
    }

    public void setSlides(List<SlideTO> slides) {
        this.slides = slides;
    }
}
