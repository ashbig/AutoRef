/*
 * ReagentFileParser.java
 *
 * Created on October 24, 2007, 4:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package io;

import core.ReagentInfo;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author dzuo
 */
public abstract class ReagentFileParser {
    private static final String NA = "NA";
    
    private List<ReagentInfo> reagents;
    private Set<String> labels;
    
    /** Creates a new instance of ReagentFileParser */
    public ReagentFileParser() {
        this.setReagents(new ArrayList<ReagentInfo>());
        this.setLabels(new TreeSet<String>());
    }

    public static String getNA() {
        return NA;
    }

    public List<ReagentInfo> getReagents() {
        return reagents;
    }

    public void setReagents(List<ReagentInfo> reagents) {
        this.reagents = reagents;
    }

    public Set<String> getLabels() {
        return labels;
    }

    public void setLabels(Set<String> labels) {
        this.labels = labels;
    }
    
    public abstract void parseFile(InputStream input) throws CloneFileParserException;
}
