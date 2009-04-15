/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bean;

import controller.LowNormController;
import controller.ControllerException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import transfer.SlideTO;

/**
 *
 * @author DZuo
 */
public class LowNormBean {
    private String barcode;
    private int from;
    private int to;
    private List<SelectItem> foundSlides;
    private List<SelectItem> selectedSlides;
    private List<String> slides1;
    private List<String> slides2;
    private boolean background;
    private List<SelectItem> controls;
    private List<String> selectedControls;
    private double percentile;
    private boolean histogram;
    private boolean variation;
    private String name;
    
    private LowNormController controller;
    private String message;
    
    public LowNormBean() {
        this.foundSlides = new ArrayList<SelectItem>();
        this.selectedSlides = new ArrayList<SelectItem>();
        controller = new LowNormController();
        message = "";
        from=1;
        to=90;
        this.background = false;
        populateControlList();
        percentile = 75.0;
        histogram = false;
        variation = false;
        name = "";
    }
    
    public void findSlides() {
        try {
            controller.findSlides(barcode);
        } catch (ControllerException ex) {
            System.out.println(ex.getMessage());
            message = "Cannot get slides.";
            return;
        }
        
        foundSlides = populateItems(controller.getFoundSlides());
    }
    
    public void addSlides() {
        controller.transferSlides(controller.getFoundSlides(), controller.getSelectedSlides(),getSlides1());
        foundSlides = populateItems(controller.getFoundSlides());
        selectedSlides = populateItems(controller.getSelectedSlides());
    }
    
    public void removeSlides() {
        controller.transferSlides(controller.getSelectedSlides(), controller.getFoundSlides(),getSlides2());
        foundSlides = populateItems(controller.getFoundSlides());
        selectedSlides = populateItems(controller.getSelectedSlides());
    }
    
    private List<SelectItem> populateItems(List<SlideTO>slides) {
        if(slides == null || slides.size()==0)
            return new ArrayList<SelectItem>();
        
        List<SelectItem> items = new ArrayList<SelectItem>();
        
        for(SlideTO s:slides) {
            SelectItem item = new SelectItem(""+s.getSlideid(),s.getBarcode());
            items.add(item);
        }
        return items;
    }
    
    public void populateControlList() {
        List<String> allcontrols = controller.getControlList();
        controls = new ArrayList<SelectItem>();
        for(String c:allcontrols) {
            SelectItem i = new SelectItem(c);
            controls.add(i);
        }
    }
    
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public LowNormController getController() {
        return controller;
    }

    public void setController(LowNormController controller) {
        this.controller = controller;
    }

    public List<SelectItem> getFoundSlides() {
        return foundSlides;
    }

    public void setFoundSlides(List<SelectItem> foundSlides) {
        this.foundSlides = foundSlides;
    }

    public List<SelectItem> getSelectedSlides() {
        return selectedSlides;
    }

    public void setSelectedSlides(List<SelectItem> selectedSlides) {
        this.selectedSlides = selectedSlides;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getSlides1() {
        return slides1;
    }

    public void setSlides1(List<String> slides1) {
        this.slides1 = slides1;
    }

    public List<String> getSlides2() {
        return slides2;
    }

    public void setSlides2(List<String> slides2) {
        this.slides2 = slides2;
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public List<SelectItem> getControls() {
        return controls;
    }

    public void setControls(List<SelectItem> controls) {
        this.controls = controls;
    }

    public List<String> getSelectedControls() {
        return selectedControls;
    }

    public void setSelectedControls(List<String> selectedControls) {
        this.selectedControls = selectedControls;
    }

    public double getPercentile() {
        return percentile;
    }

    public void setPercentile(double percentile) {
        this.percentile = percentile;
    }

    public boolean isHistogram() {
        return histogram;
    }

    public void setHistogram(boolean histogram) {
        this.histogram = histogram;
    }

    public boolean isVariation() {
        return variation;
    }

    public void setVariation(boolean variation) {
        this.variation = variation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
