/*
 * FileMapContainerController.java
 *
 * Created on July 13, 2007, 11:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import dao.DaoException;
import dao.FilereferenceDAO;
import io.FileRepository;
import io.NappaIOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import process.Container96To384Mapper;
import process.ContainerMapper;
import transfer.ContainerheaderTO;
import transfer.FilereferenceTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.ResearcherTO;
import transfer.SampleTO;

/**
 *
 * @author dzuo
 */
public class FileMapContainerController extends MapContainerController {
    private String mapfile;
    private InputStream mapfileInput;
    private InputStream mapfileInputCopy;
    private FilereferenceTO mapfileref;
    private List <FilereferenceTO> files;
    
    /**
     * Creates a new instance of FileMapContainerController
     */
    public FileMapContainerController() {
        super();
    }
    
    public FileMapContainerController(List slabels, List dlabels, String mapfile, InputStream input, InputStream inputcopy) {
        super(slabels, dlabels);
        this.setMapfile(mapfile);
        this.setMapfileInput(input);
        this.setMapfileInputCopy(inputcopy);
        this.setFiles(new ArrayList<FilereferenceTO>());
    }
    
    public void addOtherObjects() throws ControllerException {
            setMapfileref(new FilereferenceTO(getMapfile().substring(getMapfile().lastIndexOf("\\")+1), FilereferenceTO.PATH, FilereferenceTO.TYPE_MAPPING));
            try {
                FileRepository.uploadFile(getMapfileref(), getMapfileInput());
            } catch (NappaIOException ex) {
                throw new ControllerException(ex.getMessage());
            }
            
            getMapfileref().setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
            getMapfileref().setIoflag(ProcessobjectTO.getIO_INPUT());
            getPe().addProcessobject(getMapfileref());
            getFiles().add(getMapfileref());
    }
 
    public void persistOthers(Connection conn) throws ControllerException {
        try {
            FilereferenceDAO dao = new FilereferenceDAO(conn);
            dao.addFilereferences(getFiles());
        } catch (DaoException ex) {
            throw new ControllerException("DaoException: "+ex.getMessage());
        }
    }
    
    public ContainerMapper getContainerMapper() {
        return new Container96To384Mapper(getMapfileInputCopy());
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
        dlabels.add("HsxXG002552-1");
        dlabels.add("HsxXG002552-2");
        
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
        
        //ContainertypeTO ctype = new ContainertypeTO(ContainertypeTO.TYPE_PLATE384, null, 16, 24);
        String sampletype = SampleTO.getTYPE_GLYCEROL();
        String sampleform = SampleTO.getFORM_GLYCEROL();
        String samplename = SampleTO.getNAME_GENERAL();
        ProcessprotocolTO protocol = new ProcessprotocolTO(ProcessprotocolTO.TRANSFER_FROM_96_TO_384, null, null);
        ResearcherTO researcher = new ResearcherTO("dzuo", null, null, null, "dzuo");
        String location = ContainerheaderTO.getLOCATION_FREEZER();
        
        FileMapContainerController controller = new FileMapContainerController(slabels, dlabels, file, fileinput, fileinputcopy);
        controller.setIsNumber(true);
        //controller.setContainertype(ctype);
        controller.setSampleform(sampleform);
        controller.setSamplename(samplename);
        controller.setSampletype(sampletype);
        controller.setProtocol(protocol);
        controller.setWho(researcher);
        controller.setLocation(location);
        
        try {
            controller.doProcess();
            controller.persistProcess();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public String getMapfile() {
        return mapfile;
    }

    public void setMapfile(String logfile) {
        this.mapfile = logfile;
    }

    public FilereferenceTO getMapfileref() {
        return mapfileref;
    }

    public void setMapfileref(FilereferenceTO logfileref) {
        this.mapfileref = logfileref;
    }

    public List<FilereferenceTO> getFiles() {
        return files;
    }

    public void setFiles(List<FilereferenceTO> files) {
        this.files = files;
    }

    public InputStream getMapfileInput() {
        return mapfileInput;
    }

    public void setMapfileInput(InputStream mapfileInput) {
        this.mapfileInput = mapfileInput;
    }

    public InputStream getMapfileInputCopy() {
        return mapfileInputCopy;
    }

    public void setMapfileInputCopy(InputStream mapfileInputCopy) {
        this.mapfileInputCopy = mapfileInputCopy;
    }
}
