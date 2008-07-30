/*
 * ImportReagentsController.java
 *
 * Created on October 24, 2007, 4:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package controller;

import dao.ContainerDAO;
import dao.DaoException;
import dao.FilereferenceDAO;
import dao.ReagentDAO;
import io.CloneFileParserException;
import io.FileRepository;
import io.NappaIOException;
import io.ReagentFileParser;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import process.ContainerManager;
import process.ReagentImporter;
import transfer.ContainerheaderTO;
import transfer.FilereferenceTO;
import transfer.ProcessobjectTO;

/**
 *
 * @author dzuo
 */
public abstract class ImportReagentsController extends ProcessController {
    private InputStream file;
    private InputStream filecopy;
    private ReagentImporter importer;
    private FilereferenceTO filereference;
    private String sampletype;
    private String sampleform;
    private String location;
    private String filename;
    
    /** Creates a new instance of ImportReagentsController */
    public ImportReagentsController() {
    }
    
    abstract public ReagentFileParser makeFileParser();
    
    public void doSpecificProcess() throws ControllerException {
        ReagentFileParser parser = makeFileParser();
        
        try {
            parser.parseFile(getFile());
            Set labels = parser.getLabels();
            List missinglabels = new ArrayList();
            missinglabels.addAll(labels);
            Collection<ContainerheaderTO> containers = ContainerManager.checkContainers(missinglabels, ContainerheaderTO.getSTATUS_EMPTY(), true, false, false);
            if(missinglabels.size()>0) {
                throw new ControllerException("The following labels are not valid: "+missinglabels);
            }
            
            List reagents = parser.getReagents();
            setImporter(makeReagentImporter(reagents));
            getImporter().setContainers((List)containers);
            getImporter().setSampletype(getSampletype());
            getImporter().setSampleform(getSampleform());
            getImporter().setLocation(getLocation());
            getImporter().populateContainers();
            
            setFilereference(new FilereferenceTO(getFilename().substring(getFilename().lastIndexOf("\\")+1), FilereferenceTO.PATH, FilereferenceTO.TYPE_IMPORT));
            FileRepository.uploadFile(getFilereference(), getFilecopy());
            getFilereference().setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
            getFilereference().setIoflag(ProcessobjectTO.getIO_INPUT());
            getPe().addProcessobject(getFilereference());
            for(ContainerheaderTO c:getImporter().getContainers()) {
                c.setObjecttype(ProcessobjectTO.getTYPE_CONTAINERHEADER());
                c.setIoflag(ProcessobjectTO.getIO_OUTPUT());
                getPe().addProcessobject(c);
            }
        } catch (CloneFileParserException ex) {
            throw new ControllerException("Cannot parse clone/control file.\n"+ex.getMessage());
        } catch (NappaIOException ex) {
            throw new ControllerException("Error occured while uploading the file to the server.\n"+ex.getMessage());
        } catch (DaoException ex) {
            throw new ControllerException("Error occured while updating database.\n"+ex.getMessage());
        } catch (Exception ex) {
            throw new ControllerException("Error occured.\n"+ex.getMessage());
        }
    }
    
    public void persistSpecificProcess(Connection conn) throws ControllerException {
        List reagents = getImporter().getReagentList();
        List containers = getImporter().getContainers();
        
        try {
            FilereferenceDAO dao = new FilereferenceDAO(conn);
            List <FilereferenceTO> files = new ArrayList<FilereferenceTO>();
            files.add(getFilereference());
            dao.addFilereferences(files);
            
            ReagentDAO dao1 = makeReagentDAO(conn);
            dao1.addReagents(reagents);
            
            ContainerDAO dao2 = new ContainerDAO(conn);
            dao2.addContainers(getImporter().getContainers(), true, true);
        } catch (DaoException ex) {
            throw new ControllerException("DaoException: "+ex.getMessage());
        }
    }
    
    public ReagentImporter makeReagentImporter(List reagents) {
        return new ReagentImporter(reagents);
    }

    public ReagentDAO makeReagentDAO(Connection conn) {
        return new ReagentDAO(conn);
    }
    
    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public ReagentImporter getImporter() {
        return importer;
    }

    public void setImporter(ReagentImporter importer) {
        this.importer = importer;
    }

    public FilereferenceTO getFilereference() {
        return filereference;
    }

    public void setFilereference(FilereferenceTO filereference) {
        this.filereference = filereference;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public InputStream getFilecopy() {
        return filecopy;
    }

    public void setFilecopy(InputStream filecopy) {
        this.filecopy = filecopy;
    }
}
