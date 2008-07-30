/*
 * GrowthconditionTO.java
 *
 * Created on July 16, 2007, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package transfer;

import java.io.Serializable;

/**
 * 
 * @author dzuo
 */
public class GrowthconditionTO implements Serializable {

    private String name;
    private String hosttype;
    private String markers;
    private String antibioticselection;
    private String growthcondition;
    private String comments;

    // private Collection<CloneTO> cloneCollection;
    /**
     * Creates a new instance of GrowthconditionTO
     */
    public GrowthconditionTO() {
    }

    /**
     * Creates a new instance of GrowthconditionTO with the specified values.
     * 
     * @param name the name of the GrowthconditionTO
     */
    public GrowthconditionTO(String name) {
        this.name = name;
    }

    /**
     * Creates a new instance of GrowthconditionTO with the specified values.
     * 
     * @param name the name of the GrowthconditionTO
     * @param hosttype the hosttype of the GrowthconditionTO
     * @param markers the markers of the GrowthconditionTO
     * @param antibioticselection the antibioticselection of the GrowthconditionTO
     */
    public GrowthconditionTO(String name, String hosttype, String markers, String antibioticselection) {
        this.name = name;
        this.hosttype = hosttype;
        this.markers = markers;
        this.antibioticselection = antibioticselection;
    }

    /**
     * Gets the name of this GrowthconditionTO.
     * 
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this GrowthconditionTO to the specified value.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the hosttype of this GrowthconditionTO.
     * 
     * @return the hosttype
     */
    public String getHosttype() {
        return this.hosttype;
    }

    /**
     * Sets the hosttype of this GrowthconditionTO to the specified value.
     * 
     * @param hosttype the new hosttype
     */
    public void setHosttype(String hosttype) {
        this.hosttype = hosttype;
    }

    /**
     * Gets the markers of this GrowthconditionTO.
     * 
     * @return the markers
     */
    public String getMarkers() {
        return this.markers;
    }

    /**
     * Sets the markers of this GrowthconditionTO to the specified value.
     * 
     * @param markers the new markers
     */
    public void setMarkers(String markers) {
        this.markers = markers;
    }

    /**
     * Gets the antibioticselection of this GrowthconditionTO.
     * 
     * @return the antibioticselection
     */
    public String getAntibioticselection() {
        return this.antibioticselection;
    }

    /**
     * Sets the antibioticselection of this GrowthconditionTO to the specified value.
     * 
     * @param antibioticselection the new antibioticselection
     */
    public void setAntibioticselection(String antibioticselection) {
        this.antibioticselection = antibioticselection;
    }

    /**
     * Gets the growthcondition of this GrowthconditionTO.
     * 
     * @return the growthcondition
     */
    public String getGrowthcondition() {
        return this.growthcondition;
    }

    /**
     * Sets the growthcondition of this GrowthconditionTO to the specified value.
     * 
     * @param growthcondition the new growthcondition
     */
    public void setGrowthcondition(String growthcondition) {
        this.growthcondition = growthcondition;
    }

    /**
     * Gets the comments of this GrowthconditionTO.
     * 
     * @return the comments
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * Sets the comments of this GrowthconditionTO to the specified value.
     * 
     * @param comments the new comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }
}
