/*
 * ConfigTO.java
 *
 * Created on April 25, 2007, 1:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author dzuo
 */
public class ConfigTO implements Serializable {
    private String name;
    private String description;
    private ResearcherTO who;
    private Date when;
    private String status;
    private String stage;
    private ProcessprotocolTO protocol;
    
    /** Creates a new instance of ConfigTO */
    public ConfigTO() {
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

    public ResearcherTO getWho() {
        return who;
    }

    public void setWho(ResearcherTO who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public ProcessprotocolTO getProtocol() {
        return protocol;
    }

    public void setProtocol(ProcessprotocolTO protocol) {
        this.protocol = protocol;
    }
    
}
