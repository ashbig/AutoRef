/*
 * PrepDnaController.java
 *
 * Created on October 19, 2007, 3:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import dao.ReagentDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import process.Container96To384MapperWithMMix;
import process.ContainerMapper;
import transfer.ContainerheaderTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ReagentTO;
import transfer.ResearcherTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class PrepDnaController extends FileMapContainerController {
    protected List<ReagentTO> mmixes;
    
    /** Creates a new instance of PrepDnaController */
    public PrepDnaController() {
    }
    
    public PrepDnaController(List slabels, List dlabels, String logfile, InputStream logfileinput, InputStream logfileinputcopy) {
        super(slabels, dlabels, logfile, logfileinput, logfileinputcopy);
    }
    
    public boolean isValid(List<String> mixes) {
        if(getSrcLabels().size() == mixes.size())
            return true;
        return false;
    }
    
    public void findMmixes(List<String> mixes) throws ControllerException {
        try {
            mmixes = ReagentDAO.getReagentList(mixes);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }
    
    public void addOtherObjects() throws ControllerException {
        super.addOtherObjects();
        
        Iterator iter1 = getSrcLabels().iterator();
        Iterator iter2 = mmixes.iterator();
        while(iter2.hasNext()) {
            String label = (String)iter1.next();
            ReagentTO reagent = (ReagentTO)iter2.next();
            ProcessobjectTO obj = findInputObject(label);
            if(obj == null) {
                throw new ControllerException("Cannot find container: "+label);
            }
            reagent.setObjecttype(ProcessobjectTO.getTYPE_REAGENT());
            reagent.setIoflag(ProcessobjectTO.getIO_INPUT());
            reagent.setLevel(obj.getLevel());
            reagent.setOrder(obj.getOrder());
            getPe().addProcessobject(reagent);
        }
    }
    
    public ProcessobjectTO findInputObject(String label) {
        List<ProcessobjectTO> objs = getPe().getObjects();
        for(ProcessobjectTO obj:objs) {
            if(obj.getObjecttype().equals(ProcessobjectTO.getTYPE_CONTAINERHEADER())) {
                if(obj.getIoflag().equals(ProcessobjectTO.getIO_INPUT())) {
                    ContainerheaderTO container = (ContainerheaderTO)obj;
                    if(label.equals(container.getBarcode()))
                        return container;
                }
            }
        }
        return null;
    }
    
    public ContainerMapper getContainerMapper() {
        return new Container96To384MapperWithMMix(getMapfileInputCopy(), mmixes);
    }
    
    public static void main(String args[]) {
        List slabels = new ArrayList();
        slabels.add("HsxXG002541");
        slabels.add("HsxXG002542");
        slabels.add("HsxXG002543");
        slabels.add("HsxXG002544");
        slabels.add("HsxXG002545");
        slabels.add("HsxXG002546");
        slabels.add("HsxXG002547");
        slabels.add("HsxXG002548");
        
        List dlabels = new ArrayList();
        dlabels.add("HsxXG002552-7");
        dlabels.add("HsxXG002552-8");
        
        String file = "C:\\dev\\test\\nappa\\384well.txt";
        InputStream fileinput = null;
        InputStream fileinputcopy = null;
        try {
            fileinput = new FileInputStream(new File(file));
            fileinputcopy = new FileInputStream(new File(file));
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        
        List reagents = new ArrayList<String>();
        reagents.add("MMix1");
        reagents.add("MMix2");
        reagents.add("MMix3");
        reagents.add("MMix4");
        reagents.add("MMix1");
        reagents.add("MMix2");
        reagents.add("MMix3");
        reagents.add("MMix4");
        
        String sampletype = SampleTO.getTYPE_GLYCEROL();
        String sampleform = SampleTO.getFORM_GLYCEROL();
        String samplename = SampleTO.getNAME_GENERAL();
        ProcessprotocolTO protocol = new ProcessprotocolTO(ProcessprotocolTO.TRANSFER_FROM_96_TO_384, null, null);
        ResearcherTO researcher = new ResearcherTO("dzuo", null, null, null, "dzuo");
        String location = ContainerheaderTO.getLOCATION_FREEZER();
        
        PrepDnaController controller = new PrepDnaController(slabels, dlabels, file, fileinput, fileinputcopy);
        controller.setIsNumber(true);
        //controller.setContainertype(ctype);
        controller.setSampleform(sampleform);
        controller.setSamplename(samplename);
        controller.setSampletype(sampletype);
        controller.setProtocol(protocol);
        controller.setWho(researcher);
        controller.setLocation(location);
        
        try {
            if(!controller.isValid(reagents))
                throw new Exception("Number of master mixes doesn't match number of source plates.");
            controller.findMmixes(reagents);
            controller.doProcess();
            controller.persistProcess();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
