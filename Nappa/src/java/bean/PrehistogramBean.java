/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import controller.HistogramController;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.rosuda.JRI.Rengine;
import rpack.RengineManager;
import transfer.ContainerheaderTO;
import transfer.ProcessprotocolTO;
import transfer.SampleTO;
import transfer.SlideTO;
import util.Constants;

/**
 *
 * @author DZuo
 */
public class PrehistogramBean implements Serializable {
    private String barcode;
    private List<SlideTO> slides;
    private String message;
    private HistogramController controller;
    private boolean showSlides;
    private String histogramFile;
    
    public PrehistogramBean() {
        barcode = null;
        message = null;
        showSlides = false;
        controller = new HistogramController();
    }
    
    public void findSlides() {
        slides = null;
        setShowSlides(false);
        try {
            slides = controller.findSlides(barcode, ProcessprotocolTO.UPLOAD_VIGENE_RESULT, false);
        } catch (Exception ex) {
            ex.printStackTrace();
            setMessage("Cannot find slides.");
        }
        if(slides != null && slides.size()>0)
            setShowSlides(true);
    }
    
    public String viewHistogram() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map map = context.getExternalContext().getRequestParameterMap();
        int slideid = Integer.parseInt((String) map.get("slideid"));
        String slidebarcode = (String) map.get("slidebarcode");
        int executionid = Integer.parseInt((String) map.get("executionid"));
        
        Rengine re = null;
        try {
            List<SampleTO> samples = controller.getSamples(executionid, slideid);
            List<String> controls = ContainerheaderTO.getControls(samples);
            
            controller.printHistogramInputFile(samples, Constants.TMP + slideid, slidebarcode);
            
            re = RengineManager.createRengine();
            setHistogramFile(slideid+".jpg");
            controller.printHistogramOutputFile(re, 4, 1, Constants.R_TMP+slideid, Constants.R_OUTPUT_DIR+getHistogramFile(),HistogramController.FILE_FORMAT_JPG,controls);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            RengineManager.stopRengine(re);
        }

        return "viewHistogram";
    }
    
    public String viewAllHistogram() {
        Rengine re = null;
        try {
            slides = controller.findSlides(barcode, ProcessprotocolTO.UPLOAD_VIGENE_RESULT, true);
            SlideTO slide = (SlideTO)slides.get(0);
            List<SampleTO> samples = slide.getContainer().getSamples();
            List<String> controls = ContainerheaderTO.getControls(samples);
            
            controller.printHistogramInputFile(slides, Constants.TMP + barcode);
            
            re = RengineManager.createRengine();
            setHistogramFile(barcode+".pdf");
            controller.printHistogramOutputFile(re, 4, slides.size(), Constants.R_TMP+barcode, Constants.R_OUTPUT_DIR+getHistogramFile(),HistogramController.FILE_FORMAT_PDF,controls);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            RengineManager.stopRengine(re);
        }

        return "viewAllHistogram";
    }
    
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<SlideTO> getSlides() {
        return slides;
    }

    public void setSlides(List<SlideTO> slides) {
        this.slides = slides;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HistogramController getController() {
        return controller;
    }

    public void setController(HistogramController controller) {
        this.controller = controller;
    }

    public boolean isShowSlides() {
        return showSlides;
    }

    public void setShowSlides(boolean showSlides) {
        this.showSlides = showSlides;
    }

    public String getHistogramFile() {
        return histogramFile;
    }

    public void setHistogramFile(String histogramFile) {
        this.histogramFile = histogramFile;
    }
    
    public String getHistogramFileURL() {
        return Constants.REPOSITORY_URL+Constants.R_OUTPUT_URL+getHistogramFile();
    }
}
