/*
 * SaveContainerInfoForm.java
 *
 * Created on February 5, 2002, 1:09 PM
 */

package edu.harvard.med.hip.flex.form;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

/**
 *
 * @author  dzuo
 * @version 
 */
public class SaveContainerInfoForm extends ActionForm {
    private int id;
    private int executionid;
    private boolean sampleid = false;
    private boolean type = false;
    private boolean position = false;
    private boolean status = false;
    private boolean result = false;
    private boolean sequenceid = false;
    private boolean cdsstart = false;
    private boolean cdsstop = false;
    private boolean cdslength = false;
    private boolean gccontent = false;
    private boolean sequencetext = false;
    private boolean cds = false;
    private boolean isEmpty = false;
    private boolean gi = false;
    private boolean genesymbol = false;
    private boolean pcr = false;
    private boolean agar = false;
    private boolean culture = false;
    private boolean panumber = false;
    private boolean cloneid = false;
    
    /** Creates new SaveContainerInfoForm */
    public SaveContainerInfoForm() {
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setExecutionid(int executionid) {
        this.executionid = executionid;
    }
    
    public int getExecutionid() {
        return executionid;
    }
    
    public boolean getSampleid() {
        return sampleid;
    }
    
    public void setSampleid(boolean sampleid) {
        this.sampleid = sampleid;
    }
    
    public boolean getType() {
        return type;
    }
    
    public void setType(boolean type) {
        this.type = type;
    }
    
    public boolean getPosition() {
        return position;
    }
    
    public void setPosition(boolean position) {
        this.position = position;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getResult() {
        return result;
    }
    
    public void setResult(boolean result) {
        this.result = result;
    }
    
    public boolean getSequenceid() {
        return sequenceid;
    }
    
    public void setSequenceid(boolean sequenceid) {
        this.sequenceid = sequenceid;
    }
    
    public boolean getCdsstart() {
        return cdsstart;
    }
    
    public void setCdsstart(boolean cdsstart) {
        this.cdsstart = cdsstart;
    }
    
    public boolean getCdsstop() {
        return cdsstop;
    }
    
    public void setCdsstop(boolean cdsstop) {
        this.cdsstop = cdsstop;
    }
    
    public boolean getCdslength() {
        return cdslength;
    }
    
    public void setCdslength(boolean cdslength) {
        this.cdslength = cdslength;
    }
    
    public boolean getGccontent() {
        return gccontent;
    }
    
    public void setGccontent(boolean gccontent) {
        this.gccontent = gccontent;
    }
    
    public boolean getSequencetext() {
        return sequencetext;
    }
    
    public void setSequencetext(boolean sequencetext) {
        this.sequencetext = sequencetext;
    }
    
    public boolean getCds() {
        return cds;
    }
    
    public void setCds(boolean cds) {
        this.cds = cds;
    }
    
    public boolean getIsEmpty() {
        return isEmpty;
    }
    
    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
    
    public boolean getGi() {
        return gi;
    }
    
    public void setGi(boolean gi) {
        this.gi = gi;
    }
    
    public boolean getGenesymbol() {
        return genesymbol;
    }
    
    public void setGenesymbol(boolean genesymbol) {
        this.genesymbol = genesymbol;
    }
    
    public boolean getPcr() {
        return pcr;
    }
        
    public void setPcr(boolean pcr) {
        this.pcr = pcr;
    }
    
    public boolean getAgar() {
        return agar;
    }
    
    public void setAgar(boolean agar) {
        this.agar = agar;
    }
    
    public boolean getCulture() {
        return culture;
    }
    
    public void setCulture(boolean culture) {
        this.culture = culture;
    }

    public boolean getPanumber() {
        return panumber;
    }
    
    public void setPanumber(boolean panumber) {
        this.panumber = panumber;
    }
    
    public boolean getCloneid() {
        return cloneid;
    }
    
    public void setCloneid(boolean b) {
        this.cloneid = b;
    }
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        sampleid = false;
        type = false;
        position = false;
        status = false;
        result = false;
        sequenceid = false;
        cdsstart = false;
        cdsstop = false;
        cdslength = false;
        gccontent = false;
        sequencetext = false;
        cds = false;
        isEmpty = false;
        gi = false;
        genesymbol = false;
        pcr = false;
        agar = false;
        culture = false;
        panumber = false;
        cloneid = false;
    }    
}
