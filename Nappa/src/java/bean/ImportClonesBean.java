/*
 * ImportClonesBean.java
 *
 * Created on September 12, 2007, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bean;

import controller.ImportClonesController;
import controller.ImportControlsController;
import controller.ImportReagentsController;
import dao.ResearcherDAO;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import transfer.ContainerheaderTO;
import transfer.FilereferenceTO;
import transfer.ProcessobjectTO;
import transfer.ProcessprotocolTO;
import transfer.SampleTO;
import util.Constants;

/**
 *
 * @author dzuo
 */
public class ImportClonesBean implements Serializable {
    private UploadedFile file = null;
   // private String containertype = null;
    private String sampletype = SampleTO.getTYPE_GLYCEROL();
    private String sampleform = SampleTO.getFORM_GLYCEROL();
    private String location;
    
    private ImportReagentsController c = null;
    
    //1=import success; 0=no import; -1=import fail
    private int importstatus;
    private String importmessage;
    
    private List<ContainerheaderTO> outputcontainers;
    private FilereferenceTO inputfile;
    private String format;
    private String idtype;
    
    /** Creates a new instance of ImportClonesBean */
    public ImportClonesBean() {
        setFormat(Constants.FILE_CLONE);
        setIdtype(Constants.ID_GENENAME);
        setImportstatus(0);
    }
    
    public List getFileFormats() {
        List<SelectItem> formats = new ArrayList<SelectItem>();
        formats.add(new SelectItem(Constants.FILE_CLONE));
        formats.add(new SelectItem(Constants.FILE_CONTROL));
        return formats;
    }
    
    public List getIds() {
        List<SelectItem> formats = new ArrayList<SelectItem>();
        formats.add(new SelectItem(Constants.ID_GENENAME));
        formats.add(new SelectItem(Constants.ID_CLONEID));
        formats.add(new SelectItem(Constants.ID_GENEID));
        formats.add(new SelectItem(Constants.ID_GENBANK));
        formats.add(new SelectItem(Constants.ID_GI));
        return formats;
    }
    
    public String runImport() {
        setImportstatus(-1);
        setImportmessage("Import failed.");
        
        try {
            if(Constants.FILE_CLONE.equals(getFormat()))
                c = new ImportClonesController();
            else if (Constants.FILE_CONTROL.equals(getFormat()))
                c = new ImportControlsController();
            else
                throw new Exception("Error.");
            
            InputStream in = new BufferedInputStream(getFile().getInputStream());
            InputStream incopy = new BufferedInputStream(getFile().getInputStream());
            String fullname = getFile().getName();
            
            String researcherid = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).getUserPrincipal().getName();
            getC().setFile(in);
            getC().setFilecopy(incopy);
            getC().setFilename(fullname);
            getC().setSampleform(getSampleform());
            getC().setSampletype(getSampletype());
            getC().setLocation(ContainerheaderTO.getLOCATION_FREEZER());
            getC().setProtocol(new ProcessprotocolTO(ProcessprotocolTO.IMPORT_PLATES,null,null));
            getC().setWho(ResearcherDAO.getResearcher(researcherid));
            getC().setIdtype(idtype);
            getC().doProcess();
            getC().persistProcess();
            
            in.close();
            incopy.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            setImportmessage(ex.getMessage());
            return null;
        }
        
        List<ProcessobjectTO> objects = getC().getPe().getObjects();
        outputcontainers = new ArrayList<ContainerheaderTO>();
        for(ProcessobjectTO o:objects) {
            if(o.getIoflag().equals(o.getIO_INPUT())) {
                setInputfile((FilereferenceTO)o);
            } else {
                getOutputcontainers().add((ContainerheaderTO)o);
            }
        }
        
        setImportstatus(1);
        setImportmessage("Import successful.");
        return null;
    }
    
    public UploadedFile getFile() {
        return file;
    }
    
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public String getSampletype() {
        return sampletype;
    }
    
    public void setSampletype(String sampletype) {
        this.sampletype = sampletype;
    }
    
    public String getSampleform() {
        return sampleform;
    }
    
    public void setSampleform(String sampleform) {
        this.sampleform = sampleform;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public ImportReagentsController getC() {
        return c;
    }
    
    public void setC(ImportReagentsController c) {
        this.c = c;
    }
    
    public int getImportstatus() {
        return importstatus;
    }
    
    public void setImportstatus(int importstatus) {
        this.importstatus = importstatus;
    }
    
    public String getImportmessage() {
        return importmessage;
    }
    
    public void setImportmessage(String importmessage) {
        this.importmessage = importmessage;
    }
    
    public List<ContainerheaderTO> getOutputcontainers() {
        return outputcontainers;
    }
    
    public void setOutputcontainers(List<ContainerheaderTO> outputcontainers) {
        this.outputcontainers = outputcontainers;
    }
    
    public FilereferenceTO getInputfile() {
        return inputfile;
    }
    
    public void setInputfile(FilereferenceTO inputfile) {
        this.inputfile = inputfile;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIdtype() {
        return idtype;
    }

    public void setIdtype(String idtype) {
        this.idtype = idtype;
    }
}
