/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import core.Vigeneslide;
import dao.ContainerDAO;
import dao.DaoException;
import io.MicrovigeneFileParser;
import io.NappaIOException;
import java.io.InputStream;
import java.util.Collection;
import transfer.MicrovigeneresultTO;
import transfer.ResultTO;
import transfer.SampleTO;
import transfer.SlideTO;

/**
 *
 * @author DZuo
 */
public class VigeneResultUploader {

    public Vigeneslide readFile(InputStream input) throws ProcessException {
        MicrovigeneFileParser parser = new MicrovigeneFileParser(input);
        try {
            Vigeneslide vslide = parser.parseFile();
            return vslide;
        } catch (NappaIOException ex) {
            throw new ProcessException(ex.getMessage());
        }
    }
    
    public SlideTO getSlide(String label) throws ProcessException {
        try {
            SlideTO slide = ContainerDAO.getSlide(label, true, false, false, false, false);
            return slide;
        } catch (DaoException ex) {
            throw new ProcessException("Cannot find slide with barcode: "+label+"\n"+ex.getMessage());
        }
    }
    
    public void populateResultForSlide(Vigeneslide vslide, SlideTO slide) throws ProcessException {
        Collection<MicrovigeneresultTO> spots = vslide.getSpots();
        for(MicrovigeneresultTO spot:spots) {
            int pos = vslide.calculatePosition(spot.getMainRow(), spot.getMainCol(), spot.getSubRow(), spot.getSubCol());
            SampleTO s = slide.getContainer().getSample(pos);
            if(s == null) {
                throw new ProcessException("VigeneResultUploader: Cannot find sample at position "+pos+" for slide "+slide.getBarcode());
            }
            if(!s.getName().equals(spot.getGeneID())) {
                throw new ProcessException("VigeneResultUploader: The sample in the Microvigene file doesn't match the sample in the database at the following position: Main Row="+spot.getMainRow()+";Main Col="+spot.getMainCol()+";Sub Row="+spot.getSubRow()+";Sub Col="+spot.getSubCol());
            }
            
            spot.setType(ResultTO.TYPE_MICROVIGENE);
            spot.setValue(""+spot.getVoltotal());
            spot.setSampleid(s.getSampleid());
            spot.setSample(s);
            s.addResult(spot);
        }
    }
    
}
