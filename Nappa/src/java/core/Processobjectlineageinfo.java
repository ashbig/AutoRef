/*
 * Processobjectlineageinfo.java
 *
 * Created on October 29, 2007, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package core;

import java.util.ArrayList;
import java.util.List;
import transfer.ProcessobjectTO;

/**
 *
 * @author dzuo
 */
public class Processobjectlineageinfo {
    private List<ProcessobjectTO> from;
    private List<ProcessobjectTO> to;
    
    /** Creates a new instance of Processobjectlineageinfo */
    public Processobjectlineageinfo() {
        this.from = new ArrayList<ProcessobjectTO>();
        this.to = new ArrayList<ProcessobjectTO>();
    }

    public void addToFrom(ProcessobjectTO c) {
        this.from.add(c);
    }
    
    public void addToTo(ProcessobjectTO c) {
        this.to.add(c);
    }
    
    public boolean foundFromObject(ProcessobjectTO c) {
        for(ProcessobjectTO obj:from) {
            if(obj.getObjectname().equals(c.getObjectname())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean foundToObject(ProcessobjectTO c) {
        for(ProcessobjectTO obj:to) {
            if(c.getObjectname().equals(obj.getObjectname())) {
                return true;
            }
        }
        return false;
    }
    
    public List getFrom() {
        return from;
    }

    public void setFrom(List from) {
        this.from = from;
    }

    public List getTo() {
        return to;
    }

    public void setTo(List to) {
        this.to = to;
    }
}
