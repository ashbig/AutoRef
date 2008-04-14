/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.harvard.med.hip.flex.process;

import edu.harvard.med.hip.flex.core.Container;
import edu.harvard.med.hip.flex.core.Containerlabelmap;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author DZuo
 */
public class OneToManyDirectContainerMapper extends OneToOneContainerMapper {
    protected int numOfDest;
    protected List containerlabelmaps;
    
    /**
     * Constructor.
     *
     * @return The ContainerMapper object.
     */
    public OneToManyDirectContainerMapper() {
        super();
        containerlabelmaps = new ArrayList();
    }
    
    protected int getNumOfDest() {
        return numOfDest;
    }

    public void setNumOfDest(int numOfDest) {
        this.numOfDest = numOfDest;
    }
    
    protected void addToContainerlabelmap(Container container, int daughterbarcodeid) {
        Containerlabelmap m = new Containerlabelmap(container.getRoot(), daughterbarcodeid);
        this.getContainerlabelmaps().add(m);
    }

    public List getContainerlabelmaps() {
        return containerlabelmaps;
    }

    public void setContainerlabelmaps(List containerlabelmaps) {
        this.containerlabelmaps = containerlabelmaps;
    }
}
