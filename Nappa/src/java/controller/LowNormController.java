/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dao.ContainerDAO;
import transfer.SlideTO;
import dao.DaoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import transfer.ReagentTO;

/**
 *
 * @author DZuo
 */
public class LowNormController implements Serializable {
    private List<SlideTO> foundSlides;
    private List<SlideTO> selectedSlides;
    
    public LowNormController() {
        foundSlides = new ArrayList<SlideTO>();
        selectedSlides = new ArrayList<SlideTO>();
    }
    
    public void findSlides(String root) throws ControllerException {
        try {
            foundSlides = ContainerDAO.getSlidesWithRootid(root);
        } catch (DaoException ex) {
            throw new ControllerException(ex.getMessage());
        }
    }

    public void transferSlides(List<SlideTO> from, List<SlideTO> to, List<String> barcodes) {
        for(String s:barcodes) {
            SlideTO slide = findSlide(from, s);
            if(slide != null) {
                from.remove(slide);
                SlideTO slide1 = findSlide(to, s);
                if(slide1 == null) {
                    to.add(slide);
                }
            }
        }
    }
    
    private SlideTO findSlide(List<SlideTO> slides, String s) {
        if(slides == null || slides.size()==0)
            return null;
        
        for(SlideTO slide:slides) {
            if(slide.getSlideid()==Integer.parseInt(s)) {
                return slide;
            }
        }
        return null;
    }
    
    public List<String> getControlList() {
        List<String> controls = new ArrayList<String>();
        controls.add(ReagentTO.NON_SPOTS);
        return controls;
    }
    
    public List<SlideTO> getFoundSlides() {
        return foundSlides;
    }

    public void setFoundSlides(List<SlideTO> foundSlides) {
        this.foundSlides = foundSlides;
    }

    public List<SlideTO> getSelectedSlides() {
        return selectedSlides;
    }

    public void setSelectedSlides(List<SlideTO> selectedSlides) {
        this.selectedSlides = selectedSlides;
    }
}
