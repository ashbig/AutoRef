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
import transfer.SlideTO;

/**
 *
 * @author DZuo
 */
public class VigeneResultUploader {
    private String label;
    
    public void uploadVigeneResult(InputStream input) throws ProcessException {
        MicrovigeneFileParser parser = new MicrovigeneFileParser(input);
        try {
            SlideTO slide = ContainerDAO.getSlide(label, true, false, false, false, false);
            Vigeneslide vslide = parser.parseFile();
        } catch (NappaIOException ex) {
            throw new ProcessException(ex.getMessage());
        } catch (DaoException ex) {
            throw new ProcessException("Cannot find slide with barcode: "+label+"\n"+ex.getMessage());
        }
    }
    
}
