/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.action;

import java.util.*;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import plasmid.form.Plate96To384MappingForm;
import plasmid.coreobject.*;
import plasmid.Constants;
import plasmid.process.*;
import plasmid.coreobject.Process;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class Plate384To96InputAction extends InternalUserAction{
    
    /** Creates a new instance of WorklistInputAction */
    public Plate384To96InputAction() {
    }
    
    public ActionForward internalUserPerform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        
        String plateA = ((Plate96To384MappingForm)form).getPlateA();
        String plateB = ((Plate96To384MappingForm)form).getPlateB();
        String plateC = ((Plate96To384MappingForm)form).getPlateC();
        String plateD = ((Plate96To384MappingForm)form).getPlateD();
        String plate = ((Plate96To384MappingForm)form).getPlate();
        
        User user = (User)request.getSession().getAttribute(Constants.USER_KEY);
        
        List srcLabels = new ArrayList();
        srcLabels.add(plate);
        List destLabels = new ArrayList();
        destLabels.add(plateA);
        destLabels.add(plateB);
        destLabels.add(plateC);
        destLabels.add(plateD);
        
        ContainerProcessManager manager = new ContainerProcessManager();
        StringConvertor sc = new StringConvertor();
        
        try {
            List srcContainers = manager.getContainers(srcLabels, true);
            
            if(srcContainers == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot get containers from database."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            //System.out.println(srcLabels);
            //System.out.println("src containers: "+srcContainers);
            List notFound = manager.getNofoundContainers(srcLabels, srcContainers);
            if(notFound.size()>0) {
                String s = sc.convertFromListToString(notFound);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notfound", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List destContainers = manager.getContainers(destLabels, false);
            if(destContainers == null) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Cannot get containers from database."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            //System.out.println(destLabels);
            //System.out.println("dest containers: "+destContainers);
            List notFoundDest = manager.getNofoundContainers(destLabels, destContainers);
            if(notFoundDest.size()>0) {
                String s = sc.convertFromListToString(notFoundDest);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notfound", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List emptySrcContainers = manager.checkEmptyContainers(srcContainers, Container.EMPTY);
            if(emptySrcContainers.size()>0) {
                String s = sc.convertFromListToString(emptySrcContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.empty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List notEmptyDestContainers = manager.checkEmptyContainers(destContainers, Container.FILLED);
            if(notEmptyDestContainers.size()>0) {
                String s = sc.convertFromListToString(notEmptyDestContainers);
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.container.notempty", s));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            MappingCalculator calculator = StaticMappingCalculatorFactory.generateMappingCalculator(StaticMappingCalculatorFactory.PLATE384_TO_96, srcContainers, destContainers, Sample.WORKING_GLYCEROL);
            if(!calculator.isMappingValid()) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                new ActionError("error.general", "Container types are not compatable for mapping."));
                saveErrors(request, errors);
                return (new ActionForward(mapping.getInput()));
            }
            
            List worklist = calculator.calculateMapping();
            ContainerMapper mapper = new ContainerMapper(worklist);
            destContainers = mapper.mapContainer();
            List lineages = mapper.getWorklist();
            List samples = new ArrayList();
            
            for(int i=0; i<destContainers.size(); i++) {
                Container c = (Container)destContainers.get(i);
                samples.addAll(c.getSamples());
            }
            
            manager.setSampleids(samples);
            List newSampleLineages = manager.setSampleToidsForLineages(samples, lineages);
            if(newSampleLineages == null) {
                throw new Exception("Error occured while setting sampleids for sample lineages.");
            }
            
            ProcessExecution execution = new ProcessExecution(0, ProcessExecution.COMPLETE, null, Process.REVERSE_PLATE_CONDENSATION, user.getFirstname()+" "+user.getLastname(), null);
            execution.setLineages(newSampleLineages);
            execution.setInputObjects(srcContainers);
            execution.setOutputObjects(destContainers);
            
            if(!manager.persistData(null,destContainers,execution,null)) {
                throw new Exception("Error occured while inserting into database.");
            }
            
            request.setAttribute("sucmesg", "Plates are successfully created.");
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.database"));
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        
        form.reset(mapping, request);
        return mapping.findForward("success");
    }
}
