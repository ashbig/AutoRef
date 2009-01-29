/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import core.Vigeneslide;
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
import transfer.ProcessprotocolTO;
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

    @Override
    public void doSpecificProcess() throws ControllerException {
        VigeneResultUploader loader = new VigeneResultUploader();
        try {
            Vigeneslide vslide = loader.readFile(file);
            SlideTO slide = loader.getSlide(getLabel());
            loader.populateResultForSlide(vslide, slide);

            List<ResultTO> results = new ArrayList<ResultTO>();
            Collection<SampleTO> samples = slide.getContainer().getSamples();
            for (SampleTO s : samples) {
                Collection<ResultTO> rs = s.getResults();
                results.addAll(rs);
            }
            getPe().setResults(results);
            getPe().setProtocol(new ProcessprotocolTO(ProcessprotocolTO.UPLOAD_VIGENE_RESULT, null, null));

            FilereferenceTO fileref = new FilereferenceTO(getFilename(), FilereferenceTO.RESULTFILEPATH, FilereferenceTO.TYPE_RESULT);
            FileRepository.uploadFile(fileref, filecopy);
            fileref.setObjecttype(ProcessobjectTO.getTYPE_FILEREFERENCE());
            fileref.setIoflag(ProcessobjectTO.getIO_INPUT());
            getPe().addProcessobject(fileref);
            setFilerefs(new ArrayList<FilereferenceTO>());
            getFilerefs().add(fileref);
        } catch (Exception ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    @Override
    public void persistSpecificProcess(Connection conn) throws ControllerException {
        try {
            FilereferenceDAO dao = new FilereferenceDAO(conn);
            dao.addFilereferences(getFilerefs());
            //add Vigeneslide and spot info
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
}
