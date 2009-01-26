/*
 * PrintSlideController.java
 *
 * Created on April 25, 2007, 1:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import io.FileRepository;
import io.NappaIOException;
import io.Plate384ToSlideLogFileParser;
import io.ProgramMappingFileParserException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import process.Container384ToSlideMapper;
import process.ContainerMapper;
import transfer.ContainerheaderTO;
import transfer.FilereferenceTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.SampleTO;
import transfer.SlideTO;

/**
 *
 * @author dzuo
 */
public class PrintSlideController extends FileMapContainerController {
    private String logfile;
    private InputStream logfileInput;
    private InputStream logfileInputCopy;
    private String programname;
    private String startdate;
    private int numofslides;
    private int startnum;
    
    /** Creates a new instance of PrintSlideController */
    public PrintSlideController() {
    }
    
    public PrintSlideController(List slabels, List dlabels, String galfile, InputStream galfileinput, InputStream galfileinputcopy) {
        super(slabels, dlabels, galfile, galfileinput, galfileinputcopy);
    }
    
    public PrintSlideController(List slabels, List dlabels, String galfile, InputStream galfileinput, InputStream galfileinputcopy, String logfile, InputStream logfileinput, InputStream logfileinputcopy) {
        super(slabels, dlabels, galfile, galfileinput, galfileinputcopy);
        setLogfile(logfile);
        setLogfileInput(logfileinput);
        setLogfileInputCopy(logfileinputcopy);
    }
    
    public String getLogfile() {
        return logfile;
    }
    
    public void setLogfile(String logfile) {
        this.logfile = logfile;
    }
    
    public String getProgramname() {
        return programname;
    }
    
    public void setProgramname(String programname) {
        this.programname = programname;
    }
    
    public String getStartdate() {
        return startdate;
    }
    
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
    
    public ContainerMapper getContainerMapper() {
        return new Container384ToSlideMapper(getMapfileInputCopy());
    }
    
    public void setContainerLineages() {
        Collection<ContainerheaderTO> srcContainers = mapper.getSrcContainers();
        Collection<ContainerheaderTO> destContainers = mapper.getDestContainers();
        
        int m=1;
        for(ContainerheaderTO dest:destContainers){
            int n=0;
            for(ContainerheaderTO src:srcContainers) {
                n++;
                src.setObjecttype(ProcessobjectTO.getTYPE_CONTAINERHEADER());
                src.setIoflag(ProcessobjectTO.getIO_INPUT());
                src.setLevel(m);
                src.setOrder(n);
                getPe().addProcessobject(src);
            }
            
            dest.setObjecttype(ProcessobjectTO.getTYPE_CONTAINERHEADER());
            dest.setIoflag(ProcessobjectTO.getIO_OUTPUT());
            dest.setLevel(m);
            dest.setOrder(m);
            getPe().addProcessobject(dest);
        }
    }
    
    public void addOtherObjects() throws ControllerException {
        super.addOtherObjects();
        FilereferenceTO logfileref = new FilereferenceTO(getLogfile().substring(getLogfile().lastIndexOf("\\")+1), FilereferenceTO.MAPFILEPATH, FilereferenceTO.TYPE_MAPPING);
        try {
            FileRepository.uploadFile(logfileref, getLogfileInput());
        } catch (NappaIOException ex){
            throw new ControllerException(ex.getMessage());
        }
        logfileref.setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
        logfileref.setIoflag((ProcessobjectTO.getIO_INPUT()));
        getPe().addProcessobject(logfileref);
        
        getFiles().add(logfileref);
        
        List addObjects = new ArrayList();
        List removeObjects = new ArrayList();
        for(ContainerheaderTO c:getMapper().getDestContainers()) {
            for(int i=0; i<getNumofslides(); i++) {
                int num = getStartnum()+i;
                SlideTO s = new SlideTO(num, c.getBarcode()+"-"+num, null, getProgramname(), getStartdate(), c);
                c.addSlide(s);
            }
            
            for(ProcessobjectTO object:getPe().getObjects()) {
                if(object.getObjectname().equals(c.getBarcode())) {
                    removeObjects.add(object);
                    int order = 1;
                    for(SlideTO slide:c.getSlides()) {
                        slide.setObjecttype(ProcessobjectTO.getTYPE_SLIDE());
                        slide.setIoflag(ProcessobjectTO.getIO_OUTPUT());
                        slide.setLevel(object.getLevel());
                        slide.setOrder(order);
                        addObjects.add(slide);
                        order++;
                    }
                }
            }
        }
        
        getPe().getObjects().removeAll(removeObjects);
        getPe().getObjects().addAll(addObjects);
    }
    
    public void parseLogfile() throws ControllerException {
        Plate384ToSlideLogFileParser parser = new Plate384ToSlideLogFileParser();
        try {
            parser.parseLogfile(getLogfileInputCopy());
            setSrcLabels(parser.getPlates());
            setProgramname(parser.getProgramname());
            setStartdate(parser.getStartdate());
        } catch (ProgramMappingFileParserException ex) {
            throw new ControllerException(ex.getMessage());
        } 
    }
    
    public static void main(String args[]) {
        /** List slabels = new ArrayList();
        slabels.add("plate384-1");
        slabels.add("plate384-2");
        slabels.add("plate384-3");
        slabels.add("plate384-4");
        slabels.add("plate384-5");
        slabels.add("plate384-6");
        slabels.add("plate384-7");
        slabels.add("plate384-8");
        slabels.add("plate384-9");
        slabels.add("plate384-10");
        slabels.add("Registration1");
         */
        List dlabels = new ArrayList();
        dlabels.add("slide15");
        
        String galfile = "C:\\dev\\test\\nappa\\testrunGenie131.gal";
        String file = "C:\\dev\\test\\nappa\\testlog.txt";
        InputStream galfileinput = null;
        InputStream galfileinputcopy = null;
        InputStream fileinput = null;
        InputStream fileinputcopy = null;
        
        try {
            galfileinput = new FileInputStream(new File(galfile));
            galfileinputcopy = new FileInputStream(new File(galfile));
            fileinput = new FileInputStream(new File(file));
            fileinputcopy = new FileInputStream(new File(file));
        } catch (Exception ex) {
            System.out.println();
            System.exit(1);
        }
        
        //ContainertypeTO ctype = new ContainertypeTO(ContainertypeTO.TYPE_SLIDE, null, 16, 24);
        String sampletype = SampleTO.getTYPE_GLYCEROL();
        String sampleform = SampleTO.getFORM_GLYCEROL();
        String samplename = SampleTO.getNAME_GENERAL();
        ProcessprotocolTO protocol = new ProcessprotocolTO(ProcessprotocolTO.TRANSFER_FROM_96_TO_384, null, null);
        ResearcherTO researcher = new ResearcherTO("dzuo", null, null, null, "dzuo");
        String location = ContainerheaderTO.getLOCATION_FREEZER();
        
        PrintSlideController controller = new PrintSlideController(null, dlabels, galfile, galfileinput, galfileinputcopy, file, fileinput, fileinputcopy);
        controller.setIsNumber(true);
        //controller.setContainertype(ctype);
        controller.setSampleform(sampleform);
        controller.setSamplename(samplename);
        controller.setSampletype(sampletype);
        controller.setProtocol(protocol);
        controller.setWho(researcher);
        controller.setLocation(location);
        controller.setNumofslides(90);
        
        try {
            controller.parseLogfile();
            controller.doProcess();
            controller.persistProcess();
            galfileinput.close();
            fileinput.close();
            fileinputcopy.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public int getNumofslides() {
        return numofslides;
    }
    
    public void setNumofslides(int numofslides) {
        this.numofslides = numofslides;
    }

    public InputStream getLogfileInput() {
        return logfileInput;
    }

    public void setLogfileInput(InputStream logfileInput) {
        this.logfileInput = logfileInput;
    }

    public InputStream getLogfileInputCopy() {
        return logfileInputCopy;
    }

    public void setLogfileInputCopy(InputStream logfileInputCopy) {
        this.logfileInputCopy = logfileInputCopy;
    }

    public int getStartnum() {
        return startnum;
    }

    public void setStartnum(int startnum) {
        this.startnum = startnum;
    }
}
