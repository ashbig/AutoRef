/*
 * ProcessexecutionTO.java
 *
 * Created on April 25, 2007, 1:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import core.Processobjectlineageinfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dzuo
 */
public class ProcessexecutionTO implements Serializable {
    private static final String OUTCOME_SUCCESS = "SUCCESSFUL";
    
    private int executionid;
    private ProcessprotocolTO protocol;
    private Date when;
    private ResearcherTO who;
    private String outcome;
    private ConfigTO config;
    private List<ParamTO> params;
    private List<ProcessobjectTO> objects;
    private List<SamplelineageTO> slineages;
    private List<ResultTO> results;
    //private List<ContainerlineageTO> clineages;
    
    /**
     * Creates a new instance of ProcessexecutionTO
     */
    public ProcessexecutionTO() {
        this.setObjects(new ArrayList<ProcessobjectTO>());
        this.setSlineages(new ArrayList<SamplelineageTO>());
        this.setResults(new ArrayList<ResultTO>());
        //this.setClineages(new ArrayList<ContainerlineageTO>());
    }
    
    public ProcessexecutionTO(ProcessprotocolTO protocol, Date date, ResearcherTO who, String outcome) {
        this.setProtocol(protocol);
        this.setWhen(date);
        this.setWho(who);
        this.setOutcome(outcome);
        this.setObjects(new ArrayList<ProcessobjectTO>());
        this.setSlineages(new ArrayList<SamplelineageTO>());
        this.setResults(new ArrayList<ResultTO>());
        //this.setClineages(new ArrayList<ContainerlineageTO>());
    }
    
    public int getExecutionid() {
        return executionid;
    }
    
    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }
    
    public ProcessprotocolTO getProtocol() {
        return protocol;
    }
    
    public void setProtocol(ProcessprotocolTO protocol) {
        this.protocol = protocol;
    }
    
    public Date getWhen() {
        return when;
    }
    
    public void setWhen(Date when) {
        this.when = when;
    }
    
    public ResearcherTO getWho() {
        return who;
    }
    
    public void setWho(ResearcherTO who) {
        this.who = who;
    }
    
    public String getOutcome() {
        return outcome;
    }
    
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
    
    public ConfigTO getConfig() {
        return config;
    }
    
    public void setConfig(ConfigTO config) {
        this.config = config;
    }
    
    public List<ParamTO> getParams() {
        return params;
    }
    
    public void setParams(List<ParamTO> params) {
        this.params = params;
    }
    
    public void addProcessobject(ProcessobjectTO p) {
        this.getObjects().add(p);
    }
    
    public List<ProcessobjectTO> getObjects() {
        return objects;
    }
    
    public void setObjects(List<ProcessobjectTO> objects) {
        this.objects = objects;
    }
    
    public static String getOUTCOME_SUCCESS() {
        return OUTCOME_SUCCESS;
    }
    
    public List<SamplelineageTO> getSlineages() {
        return slineages;
    }
    
    public void setSlineages(List<SamplelineageTO> slineages) {
        this.slineages = slineages;
    }
    
    public void addSamplelineage(SamplelineageTO l) {
        this.getSlineages().add(l);
    }
    
    public List<Processobjectlineageinfo> getObjectlineages() {
        int level = -1;
        List<Processobjectlineageinfo> lineages = new ArrayList<Processobjectlineageinfo>();
        Processobjectlineageinfo lineage = null;
        
        for(ProcessobjectTO o:getObjects()) {
            if(o.getLevel() == level) {
                addToLineage(lineage, o);
            } else {
                lineage = new Processobjectlineageinfo();
                addToLineage(lineage, o);
                lineages.add(lineage);
                level = o.getLevel();
            }
        }
        return lineages;
    }
    
    public void addToLineage(Processobjectlineageinfo lineage, ProcessobjectTO o) {
        if(o.getIoflag().equals(ProcessobjectTO.getIO_INPUT())) {
            lineage.addToFrom(o);
        } else if(o.getIoflag().equals(ProcessobjectTO.getIO_OUTPUT())) {
            lineage.addToTo(o);
        } else {
            lineage.addToFrom(o);
            lineage.addToTo(o);
        }
    }

    public List<ResultTO> getResults() {
        return results;
    }

    public void setResults(List<ResultTO> results) {
        this.results = results;
    }
    
    public void addResult(ResultTO r) {
        getResults().add(r);
    }
}
