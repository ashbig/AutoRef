/*
 * DirectContainerMapBean.java
 *
 * Created on October 29, 2007, 11:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package bean;

import controller.MapContainerController;
import controller.PrepDnaController;
import controller.PrintSlideController;
import controller.StaticMapContainerControllerFactory;
import dao.ReagentDAO;
import dao.ResearcherDAO;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import transfer.ContainerheaderTO;
import transfer.ProcessprotocolTO;
import transfer.ReagentTO;
import transfer.SampleTO;
import util.Constants;
import util.StringConvertor;

/**
 *
 * @author dzuo
 */
public class DirectContainerMapBean implements Serializable {

    private String protocol;
    private String srcLabels;
    private String destLabels;
    private String mmix;
    private UploadedFile file;
    private UploadedFile logfile;
    private String message;
    private boolean status;
    private boolean showcontainer;
    private boolean showfile;
    private boolean showmmix;
    private boolean showlogfile;
    private boolean shownumofslide;
    private boolean showsrc;
    private String format;
    private int numofslide;
    private int executionid;
    
    /** Creates a new instance of DirectContainerMapBean */
    public DirectContainerMapBean() {
        reset();
    }

    public void reset() {
        setSrcLabels(null);
        setDestLabels(null);
        setMmix(null);
        setMessage(null);
        setStatus(false);
        setShowcontainer(false);
        setShowfile(false);
        setShowmmix(false);
        setShowlogfile(false);
        setShownumofslide(false);
        setShowsrc(true);
        setNumofslide(90);
        setFormat(Constants.MAPFILE_NUMBER);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSrcLabels() {
        return srcLabels;
    }

    public void setSrcLabels(String srcLabels) {
        this.srcLabels = srcLabels;
    }

    public String getDestLabels() {
        return destLabels;
    }

    public void setDestLabels(String destLabels) {
        this.destLabels = destLabels;
    }

    public String getMmix() {
        return mmix;
    }

    public void setMmix(String mmix) {
        this.mmix = mmix;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isShowcontainer() {
        return showcontainer;
    }

    public void setShowcontainer(boolean showcontainer) {
        this.showcontainer = showcontainer;
    }

    public int getExecutionid() {
        return executionid;
    }

    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }

    public boolean getShowfile() {
        return showfile;
    }

    public void setShowfile(boolean showfile) {
        this.showfile = showfile;
    }

    public boolean getShowmmix() {
        return showmmix;
    }

    public void setShowmmix(boolean showmmix) {
        this.showmmix = showmmix;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String s) {
        this.format = s;
    }

    public UploadedFile getLogfile() {
        return logfile;
    }

    public void setLogfile(UploadedFile logfile) {
        this.logfile = logfile;
    }

    public boolean isShowlogfile() {
        return showlogfile;
    }

    public void setShowlogfile(boolean showlogfile) {
        this.showlogfile = showlogfile;
    }

    public int getNumofslide() {
        return numofslide;
    }

    public void setNumofslide(int numofslide) {
        this.numofslide = numofslide;
    }

    public boolean isShownumofslide() {
        return shownumofslide;
    }

    public void setShownumofslide(boolean shownumofslide) {
        this.shownumofslide = shownumofslide;
    }

    public boolean isShowsrc() {
        return showsrc;
    }

    public void setShowsrc(boolean showsrc) {
        this.showsrc = showsrc;
    }

    public List getProtocols() {
        List<SelectItem> protocols = new ArrayList<SelectItem>();
        protocols.add(new SelectItem(ProcessprotocolTO.GROW_CULTURE));
        protocols.add(new SelectItem(ProcessprotocolTO.GENERATE_GLYCEROL));
        protocols.add(new SelectItem(ProcessprotocolTO.DNA_PREP));
        protocols.add(new SelectItem(ProcessprotocolTO.TRANSFER_FROM_96_TO_384));
        protocols.add(new SelectItem(ProcessprotocolTO.PRINT_SLIDES));
        return protocols;
    }

    public List getFileFormats() {
        List<SelectItem> formats = new ArrayList<SelectItem>();
        formats.add(new SelectItem(Constants.MAPFILE_NUMBER));
        formats.add(new SelectItem(Constants.MAPFILE_ALPHA));
        return formats;
    }

    public void changeProtocol(ValueChangeEvent event) throws AbortProcessingException {
        reset();

        String p = (String) event.getNewValue();
        setProtocol(p);

        if (ProcessprotocolTO.TRANSFER_FROM_96_TO_384.equals(protocol)) {
            setShowsrc(true);
            setShowfile(true);
            setShowmmix(true);
            setShowlogfile(false);
            setShownumofslide(false);
        } else if (ProcessprotocolTO.PRINT_SLIDES.equals(protocol)) {
            setShowfile(true);
            setShowlogfile(true);
            setShownumofslide(true);
            setShowsrc(false);
            setShowmmix(false);
        } else {
            setShowsrc(true);
            setShowfile(false);
            setShowmmix(false);
            setShowlogfile(false);
            setShownumofslide(false);
        }
        FacesContext.getCurrentInstance().renderResponse();
    }

    public void doMapping() {
        setStatus(true);
        setShowcontainer(false);
        setMessage("Containers are successfully generated.");

        try {
            List slabels = StringConvertor.convertFromStringToList(getSrcLabels(), "\n\t\b ");
            List dlabels = StringConvertor.convertFromStringToList(getDestLabels(), "\n\t\b ");
            String sampletype = null;
            String sampleform = null;
            String samplename = SampleTO.getNAME_GENERAL();
            if (ProcessprotocolTO.GROW_CULTURE.equals(getProtocol())) {
                sampletype = SampleTO.getTYPE_CULTURE();
                sampleform = SampleTO.getFORM_CULTURE();
            } else if (ProcessprotocolTO.GENERATE_GLYCEROL.equals(getProtocol())) {
                sampletype = SampleTO.getTYPE_GLYCEROL();
                sampleform = SampleTO.getFORM_GLYCEROL();
            } else if (ProcessprotocolTO.DNA_PREP.equals(getProtocol()) || ProcessprotocolTO.TRANSFER_FROM_96_TO_384.equals(getProtocol())) {
                sampletype = SampleTO.getTYPE_DNA();
                sampleform = SampleTO.getFORM_DNA();
            } else if (ProcessprotocolTO.PRINT_SLIDES.equals(getProtocol())) {
                sampletype = SampleTO.getTYPE_DNA();
                sampleform = SampleTO.getFORM_DNA();
            } else {
                throw new Exception("Invalid process.");
            }
            String location = ContainerheaderTO.getLOCATION_FREEZER();

            InputStream fileinput = null;
            InputStream fileinputcopy = null;
            String filename = null;
            if(file != null) {
                filename = file.getName();
                fileinput = file.getInputStream();
                fileinputcopy = file.getInputStream();
            }
            
            MapContainerController controller = StaticMapContainerControllerFactory.makeMapContainerController(slabels, dlabels, filename, fileinput, fileinputcopy, protocol);
            if (controller == null) {
                throw new Exception("Invalid process.");
            }
            if (ProcessprotocolTO.TRANSFER_FROM_96_TO_384.equals(getProtocol())) {
                List<String> reagents = StringConvertor.convertFromStringToList(getMmix(), "\n\t\b ");
                ((PrepDnaController) controller).findMmixes(reagents);
            }

            controller.setIsNumber(false);
            if (showfile) {
                if (Constants.MAPFILE_NUMBER.equals(getFormat())) {
                    controller.setIsNumber(true);
                }
            }

            String username = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            controller.setSampleform(sampleform);
            controller.setSamplename(samplename);
            controller.setSampletype(sampletype);
            controller.setProtocol(new ProcessprotocolTO(protocol, null, null));
            controller.setWho(ResearcherDAO.getResearcher(username));
            controller.setLocation(location);
            InputStream logfileinput = null;
            InputStream logfileinputcopy = null;
            if (ProcessprotocolTO.PRINT_SLIDES.equals(getProtocol())) {
                logfileinput = logfile.getInputStream();
                logfileinputcopy = logfile.getInputStream();
                ((PrintSlideController) controller).setLogfile(getLogfile().getName());
                ((PrintSlideController) controller).setLogfileInput(logfileinput);
                ((PrintSlideController) controller).setLogfileInputCopy(logfileinputcopy);
                ((PrintSlideController) controller).setIsNumber(true);
                ((PrintSlideController) controller).setNumofslides(getNumofslide());
                ((PrintSlideController) controller).parseLogfile();
            }

            controller.doProcess();
            controller.persistProcess();

            setExecutionid(controller.getPe().getExecutionid());
            setShowcontainer(true);
            setSrcLabels(null);
            setDestLabels(null);
            setMmix(null);
            
            if(fileinput != null)
                fileinput.close();
            if(fileinputcopy != null)
                fileinputcopy.close();
            if(logfileinput != null)
                logfileinput.close();
            if(logfileinputcopy != null)
                logfileinputcopy.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage(ex.getMessage());
        }
    }
}
