/*
 * EnterSrcForGlycerolAndSeqOneRowAction.java
 *
 * Created on January 30, 2004, 12:53 PM
 */

package edu.harvard.med.hip.flex.action;

import edu.harvard.med.hip.flex.form.CreateGlycerolAndSeqForm;
import edu.harvard.med.hip.flex.process.*;

import org.apache.struts.action.ActionForm;
import java.util.Vector;

/**
 *
 * @author  DZuo
 */
public class EnterSrcForGlycerolAndSeqOneRowAction extends EnterSrcForGlycerolAndSeqAction{
    
    /** Creates a new instance of EnterSrcForGlycerolAndSeqOneRowAction */
    public EnterSrcForGlycerolAndSeqOneRowAction() {
    }
     
    // Get the source containers from the form bean.
    protected Vector getContainers(ActionForm form) {
        String plate1 = ((CreateGlycerolAndSeqForm)form).getPlate1();
        String plate2 = ((CreateGlycerolAndSeqForm)form).getPlate2();
        String plate3 = ((CreateGlycerolAndSeqForm)form).getPlate3();
        String plate4 = ((CreateGlycerolAndSeqForm)form).getPlate4();
        String plate5 = ((CreateGlycerolAndSeqForm)form).getPlate5();
        String plate6 = ((CreateGlycerolAndSeqForm)form).getPlate6();
        String plate7 = ((CreateGlycerolAndSeqForm)form).getPlate7();
        String plate8 = ((CreateGlycerolAndSeqForm)form).getPlate8();
        
        Vector containers = new Vector();
        if(plate1 != null && plate1.length() != 0)
            containers.addElement(plate1);
        if(plate2 != null && plate2.length() != 0)
            containers.addElement(plate2);
        if(plate3 != null && plate3.length() != 0)
            containers.addElement(plate3);
        if(plate4 != null && plate4.length() != 0)
            containers.addElement(plate4);
        if(plate5 != null && plate5.length() != 0)
            containers.addElement(plate5);
        if(plate6 != null && plate6.length() != 0)
            containers.addElement(plate6);
        if(plate7 != null && plate7.length() != 0)
            containers.addElement(plate7);
        if(plate8 != null && plate8.length() != 0)
            containers.addElement(plate8);
        
        return containers;
    }  
        
    protected ContainerMapper getContainerMapper(String processname, ActionForm form) throws FlexProcessException {
        String row = ((CreateGlycerolAndSeqForm)form).getRow();
        GlycerolAndSeqContainerMapper mapper = new GlycerolAndSeqContainerMapper();
        mapper.setRow(row);
        String isMappingFile = ((CreateGlycerolAndSeqForm)form).getIsMappingFile();
        
        if("Yes".equals(isMappingFile)) {
            mapper.setIsMappingFile(true);
        } else {
            mapper.setIsMappingFile(false);
        }
        
        return mapper;
    }    
}
