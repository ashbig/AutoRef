/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transfer;

import core.Slidecontainerlineageinfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzuo
 */
public class SlidelayoutTO implements Serializable {
    public static final String STATUS_ACTIVE = "Active";
    
    private String name;
    private String description;
    private String date;
    private String researcher;
    private String status;
    private String program1;
    private String program2;
    
    private List<Slidecontainerlineageinfo> lineageinfo;
    private LayoutcontainerTO slide;
    private List<LayoutcontainerTO> controls;

    public SlidelayoutTO(String name, String description, String date, String researcher, String status, String program1, String program2) {
        this.setName(name);
        this.setDescription(description);
        this.setDate(date);
        this.setResearcher(researcher);
        this.setStatus(status);
        this.setProgram1(program1);
        this.setProgram2(program2);
        this.lineageinfo = new ArrayList<Slidecontainerlineageinfo>();
    }
    
    private void loadControls() {
        controls = new ArrayList<LayoutcontainerTO>();
        for(Slidecontainerlineageinfo info:lineageinfo) {
            List<LayoutcontainerTO> from = info.getFrom();
            
            for(LayoutcontainerTO c:from) {
                if(c.isIscontrol()) {
                    controls.add(c);
                }
            }
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Slidecontainerlineageinfo> getLineageinfo() {
        return lineageinfo;
    }

    public void setLineageinfo(List<Slidecontainerlineageinfo> lineageinfo) {
        this.lineageinfo = lineageinfo;
    }

    public LayoutcontainerTO getSlide() {
        return slide;
    }

    public void setSlide(LayoutcontainerTO slide) {
        this.slide = slide;
    }

    public List<LayoutcontainerTO> getControls() {
        if(controls == null) {
            loadControls();
        }
        return controls;
    }

    public void setControls(List<LayoutcontainerTO> controls) {
        this.controls = controls;
    }

    public String getProgram1() {
        return program1;
    }

    public void setProgram1(String program1) {
        this.program1 = program1;
    }

    public String getProgram2() {
        return program2;
    }

    public void setProgram2(String program2) {
        this.program2 = program2;
    }
}
