/*
 * Clone.java
 *
 * Created on October 2, 2002, 3:57 PM
 */

package edu.harvard.med.hip.flex.core;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.*;

import java.util.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class Clone {
    private int cloneid;
    private String clonename;
    private String pubhit;
    private String result;
    private int sequenceid;
    private int sampleid;
    
    /** Creates new Clone */
    public Clone(String pubhit, String result, int sequenceid, int sampleid) {
        this.pubhit = pubhit;
        this.result = result;
        this.sequenceid = sequenceid;
        this.sampleid = sampleid;
    }

    public int getCloneid() {
        return cloneid;
    }
    
    public String getClonename() {
        return clonename;
    }
    
    public String getPubhit() {
        return pubhit;
    }
    
    public String getResult() {
        return result;
    }
    
    public int getSequenceid() {
        return sequenceid;
    }
    
    public int getSampleid() {
        return sampleid;
    }
}
