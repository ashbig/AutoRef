/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import transfer.MicrovigeneslideTO;
import dao.DaoException;
import dao.FilereferenceDAO;
import io.FileRepository;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import process.VigeneResultUploader;
import transfer.FilereferenceTO;
import transfer.ProcessobjectTO;
import transfer.ResultTO;
import transfer.SampleTO;
import transfer.SlideTO;

/**
 *
 * @author Dongmei
 */
public class UploadVigeneResultController extends ProcessController implements Serializable {
    private InputStream file;
    private InputStream filecopy;
    private String filename;
    private List<FilereferenceTO> filerefs;
    private String label;
    private InputStream imagefile;
    private String imagefilename;

    @Override
    public void doSpecificProcess() throws ControllerException {
        VigeneResultUploader loader = new VigeneResultUploader();
        try {
            MicrovigeneslideTO vslide = loader.readFile(file);
            SlideTO slide = loader.getSlide(getLabel());
            loader.populateResultForSlide(vslide, slide);

            List<ResultTO> results = new ArrayList<ResultTO>();
            Collection<SampleTO> samples = slide.getContainer().getSamples();
            for (SampleTO s : samples) {
                Collection<ResultTO> rs = s.getResults();
                results.addAll(rs);
            }
            getPe().setResults(results);

            FilereferenceTO fileref = new FilereferenceTO(getFilename().substring(getFilename().lastIndexOf("\\")+1), FilereferenceTO.RESULTFILEPATH, FilereferenceTO.TYPE_RESULT);
            FileRepository.uploadFile(fileref, filecopy);
            fileref.setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
            fileref.setIoflag(ProcessobjectTO.getIO_INPUT());
            getPe().addProcessobject(fileref);
            setFilerefs(new ArrayList<FilereferenceTO>());
            getFilerefs().add(fileref);
            
            FilereferenceTO fileref1 = new FilereferenceTO(getImagefilename().substring(getFilename().lastIndexOf("\\")+1), FilereferenceTO.RESULTFILEPATH, FilereferenceTO.TYPE_SLIDE_IMAGE);
            FileRepository.uploadFile(fileref1, imagefile);
            fileref1.setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
            fileref1.setIoflag(ProcessobjectTO.getIO_INPUT());
            getPe().addProcessobject(fileref1);
            getFilerefs().add(fileref1);
            
            ProcessobjectTO o = new ProcessobjectTO(ProcessobjectTO.getTYPE_SLIDE(), ProcessobjectTO.getIO_BOTH());
            o.setObjectid(slide.getSlideid());
            o.setObjectname(slide.getBarcode());
            o.setVslide(vslide);
            getPe().addProcessobject(o);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    @Override
    public void persistSpecificProcess(Connection conn) throws ControllerException {
        try {
            FilereferenceDAO dao = new FilereferenceDAO(conn);
            dao.addFilereferences(getFilerefs());
        } catch (DaoException ex) {
            throw new ControllerException("DaoException: " + ex.getMessage());
        }
    }

    public InputStream getFile() {
        return file;
    }

    public void setFile(InputStream file) {
        this.file = file;
    }

    public InputStream getFilecopy() {
        return filecopy;
    }

    public void setFilecopy(InputStream filecopy) {
        this.filecopy = filecopy;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    public List<FilereferenceTO> getFilerefs() {
        return filerefs;
    }

    public void setFilerefs(List<FilereferenceTO> filerefs) {
        this.filerefs = filerefs;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public InputStream getImagefile() {
        return imagefile;
    }

    public void setImagefile(InputStream imagefile) {
        this.imagefile = imagefile;
    }

    public String getImagefilename() {
        return imagefilename;
    }

    public void setImagefilename(String imagefilename) {
        this.imagefilename = imagefilename;
    }
}
