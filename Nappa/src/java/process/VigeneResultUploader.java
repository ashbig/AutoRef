/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package process;

import transfer.MicrovigeneslideTO;
import dao.ContainerDAO;
import dao.DaoException;
import io.MicrovigeneFileParser;
import io.NappaIOException;
import java.io.File;
import java.io.FileInputStream;
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

    public MicrovigeneslideTO readFile(InputStream input) throws ProcessException {
        MicrovigeneFileParser parser = new MicrovigeneFileParser(input);
        try {
            MicrovigeneslideTO vslide = parser.parseFile();
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
    
    public void populateResultForSlide(MicrovigeneslideTO vslide, SlideTO slide) throws ProcessException {
        vslide.setSlideid(slide.getSlideid());
        Collection<MicrovigeneresultTO> spots = vslide.getSpots();
        for(MicrovigeneresultTO spot:spots) {
            int pos = vslide.calculatePosition(spot.getMainRow(), spot.getMainCol(), spot.getSubRow(), spot.getSubCol());
            SampleTO s = slide.getContainer().getSample(pos);
            if(s == null) {
                throw new ProcessException("VigeneResultUploader: Cannot find sample at position "+pos+" for slide "+slide.getBarcode());
            }
           
            if(spot.getGeneID()!=null && spot.getGeneID().trim().length()>0 && !s.getName().equals(spot.getGeneID())) {
                throw new ProcessException("VigeneResultUploader: The sample in the Microvigene file doesn't match the sample in the database at the following position: Main Row="+spot.getMainRow()+";Main Col="+spot.getMainCol()+";Sub Row="+spot.getSubRow()+";Sub Col="+spot.getSubCol());
            }
            
            spot.setType(ResultTO.TYPE_MICROVIGENE);
            spot.setValue(""+spot.getVoltotal());
            spot.setSampleid(s.getSampleid());
            spot.setSample(s);
            spot.setSlideid(slide.getSlideid());
            s.addResult(spot);
        }
    }
    
    public static final void main(String args[]) {
        String filename = "D:\\dev\\Test\\59_Nov7_441_122308_1306_3580 20um_allcolumns.txt";
        
        VigeneResultUploader loader = new VigeneResultUploader();
        try {
            InputStream input = new FileInputStream(new File(filename));
            MicrovigeneslideTO vslide = loader.readFile(input);
            SlideTO slide = loader.getSlide("FT-B3-59");
            loader.populateResultForSlide(vslide, slide);
            
            System.out.println("Date="+vslide.getDate());
            System.out.println("Version="+vslide.getVersion());
            System.out.println("Main row="+vslide.getMainRow());
            System.out.println("Main col="+vslide.getMainCol());
            System.out.println("Sub row="+vslide.getSubRow());
            System.out.println("Sub col="+vslide.getSubCol());
            
            Collection<MicrovigeneresultTO> spots = vslide.getSpots();
            for(MicrovigeneresultTO spot:spots) {
                System.out.println("sample="+spot.getGeneID()+";Result="+spot.getValue()+";Vol="+spot.getVoltotal());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
