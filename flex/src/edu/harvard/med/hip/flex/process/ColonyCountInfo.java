/*
 * ColonyCountInfo.java
 * 
 * This class stores all the information about the colony counts obtained from
 * the colony picker.
 *
 * Created on March 22, 2002, 12:51 PM
 */

package edu.harvard.med.hip.flex.process;

/**
 *
 * @author  dzuo
 * @version 
 */
public class ColonyCountInfo {
    private int well;
    private int found;
    private int pick;
    
    /** Creates new ColonyCountInfo */
    public ColonyCountInfo(int well, int found, int pick) {
        this.well = well;
        this.found = found;
        this.pick = pick;
    }

    /**
     * Accessor method for well.
     * @return The well number.
     */
    public int getWell() {
        return well;
    }
    
    /**
     * Accessor method for found.
     *
     * @return How many colonies found by colony picker.
     */
    public int getFound() {
        return found;
    }
    
    /**
     * Accessor method for pick.
     * @return How many colonies the colony picker picked.
     */
    public int getPick() {
        return pick;
    }
}
