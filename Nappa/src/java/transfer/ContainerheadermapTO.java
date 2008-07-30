/*
 * ContainerheadermapTO.java
 *
 * Created on March 22, 2007, 10:02 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author dzuo
 */
public class ContainerheadermapTO {
    private static final String CAT_PROGRAM = "Program container";
    
    private Long chmid;
    private String label;
    private String category;
    private String format;
    private boolean isControl;
    private ContainertypeTO containertype;

    //protected Collection<ContainerheadermaplineageTO> sources;
    private Collection<ContainercellTO> containercellmapCollection;
    
    /** Creates a new instance of ContainerheadermapTO */
    public ContainerheadermapTO() {
    }
    
    public ContainerheadermapTO(long id, String lable, String category, String format, boolean isControl, ContainertypeTO type) {
        this.setChmid(id);
        this.setLabel(getLabel());
        this.setCategory(category);
        this.setFormat(format);
        this.setIsControl(isControl);
        this.containertype = type;
        
       // this.sources = new ArrayList<ContainerheadermaplineageTO>();
        this.setContainercellmapCollection(new ArrayList<ContainercellTO>());
    }
    
    public ContainerheadermapTO(String lable, String category, String format, boolean isControl, ContainertypeTO type) {
        this.setLabel(getLabel());
        this.setCategory(category);
        this.setFormat(format);
        this.setIsControl(isControl);
        this.containertype = type;
        
       // this.sources = new ArrayList<ContainerheadermaplineageTO>();
        this.setContainercellmapCollection(new ArrayList<ContainercellTO>());
    }

    public Long getChmid() {
        return chmid;
    }

    public void setChmid(Long chmid) {
        this.chmid = chmid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Collection<ContainercellTO> getContainercellmapCollection() {
        return containercellmapCollection;
    }

    public void setContainercellmapCollection(Collection<ContainercellTO> containercellmapCollection) {
        this.containercellmapCollection = containercellmapCollection;
    }

    public boolean getIsControl() {
        return isIsControl();
    }

    public void setIsControl(boolean isControl) {
        this.isControl = isControl;
    }
    
    public void addContainercellmap(ContainercellTO cell) {
        this.getContainercellmapCollection().add(cell);
    }

    public static String getCAT_PROGRAM() {
        return CAT_PROGRAM;
    }

    public boolean isIsControl() {
        return isControl;
    }

    public ContainertypeTO getContainertype() {
        return containertype;
    }

    public void setContainertype(ContainertypeTO containertype) {
        this.containertype = containertype;
    }
}
