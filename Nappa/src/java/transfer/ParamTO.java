/*
 * ParamTO.java
 *
 * Created on April 30, 2007, 2:03 PM
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
public class ParamTO implements Serializable {
    private long paramid;
    private String paramname;
    private String paramvalue;
    private String paramunit;
    
    private ConfigTO config;
    private ProcessexecutionTO execution;
    
    /** Creates a new instance of ParamTO */
    public ParamTO() {
    }

    public long getParamid() {
        return paramid;
    }

    public void setParamid(long paramid) {
        this.paramid = paramid;
    }

    public String getParamname() {
        return paramname;
    }

    public void setParamname(String paramname) {
        this.paramname = paramname;
    }

    public String getParamvalue() {
        return paramvalue;
    }

    public void setParamvalue(String paramvalue) {
        this.paramvalue = paramvalue;
    }

    public String getParamunit() {
        return paramunit;
    }

    public void setParamunit(String paramunit) {
        this.paramunit = paramunit;
    }

    public ConfigTO getConfig() {
        return config;
    }

    public void setConfig(ConfigTO config) {
        this.config = config;
    }

    public ProcessexecutionTO getExecution() {
        return execution;
    }

    public void setExecution(ProcessexecutionTO execution) {
        this.execution = execution;
    }
    
}
