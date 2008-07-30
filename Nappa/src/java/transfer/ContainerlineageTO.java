/*
 * ContainerlineageTO.java
 *
 * Created on October 16, 2007, 11:23 AM
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
public class ContainerlineageTO implements Serializable {
    private int executionid;
    private ContainerheaderTO from;
    private ContainerheaderTO to;
    private int order;
    
    /** Creates a new instance of ContainerlineageTO */
    public ContainerlineageTO() {
    }

    public ContainerlineageTO(int id, ContainerheaderTO from, ContainerheaderTO to, int order) {
        this.executionid = id;
        this.from = from;
        this.to = to;
        this.order = order;
    }
    
    public int getExecutionid() {
        return executionid;
    }

    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }

    public ContainerheaderTO getFrom() {
        return from;
    }

    public void setFrom(ContainerheaderTO from) {
        this.from = from;
    }

    public ContainerheaderTO getTo() {
        return to;
    }

    public void setTo(ContainerheaderTO to) {
        this.to = to;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    
}
