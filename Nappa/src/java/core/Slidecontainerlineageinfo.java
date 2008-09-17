/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import transfer.LayoutcontainerTO;

/**
 *
 * @author dzuo
 */
public class Slidecontainerlineageinfo implements Serializable {
    private List<LayoutcontainerTO> from;
    private List<LayoutcontainerTO> to;

    public Slidecontainerlineageinfo() {
        this.from = new ArrayList<LayoutcontainerTO>();
        this.to = new ArrayList<LayoutcontainerTO>();
    }

    public void addToFrom(LayoutcontainerTO c) {
        this.from.add(c);
    }
    
    public void addToTo(LayoutcontainerTO c) {
        this.to.add(c);
    }
    
    public boolean foundFromObject(LayoutcontainerTO c) {
        for(LayoutcontainerTO obj:from) {
            if(obj.getOrder() == c.getOrder()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean foundToObject(LayoutcontainerTO c) {
        for(LayoutcontainerTO obj:to) {
            if(obj.getOrder() == c.getOrder()) {
                return true;
            }
        }
        return false;
    }
    
    public List<LayoutcontainerTO> getFrom() {
        return from;
    }

    public void setFrom(List<LayoutcontainerTO> from) {
        this.from = from;
    }

    public List<LayoutcontainerTO> getTo() {
        return to;
    }

    public void setTo(List<LayoutcontainerTO> to) {
        this.to = to;
    }
}
