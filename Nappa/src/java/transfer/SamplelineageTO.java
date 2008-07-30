/*
 * SamplelineageTO.java
 *
 * Created on May 1, 2007, 3:18 PM
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
public class SamplelineageTO implements Serializable {
    private SampleTO from;
    private SampleTO to;
    
    /** Creates a new instance of SamplelineageTO */
    public SamplelineageTO() {
    }

    public SamplelineageTO(SampleTO from, SampleTO to) {
        this.from = from;
        this.to = to;
    }
    
    public SampleTO getFrom() {
        return from;
    }

    public void setFrom(SampleTO from) {
        this.from = from;
    }

    public SampleTO getTo() {
        return to;
    }

    public void setTo(SampleTO to) {
        this.to = to;
    }
    
}
