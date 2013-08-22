/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import java.util.List;
import org.apache.struts.action.ActionForm;
import plasmid.Constants;
import plasmid.blast.BlastWrapper;
import plasmid.process.BlastManager;

/**
 *
 * @author DZuo
 */
public class BlastForm extends ActionForm {
    private String program;
    private String database;
    private String sequence;
    private boolean isLowcomp;
    private int alength;
    private double pid;
    private int pagesize;
    private int page;
    private List infos;
    private String inputformat;
            
    private String queryid;
    private String subjectid;
    private String clonename;
    private String description;
    
    public BlastForm() {
        reset();
    }
    
    public void reset() {
        program = BlastWrapper.PROGRAM_BLASTN;
        database = BlastWrapper.DATABASE_ALL;
        sequence = "";
        isLowcomp = true;
        alength = 100;
        pid = 90.0;
        pagesize = Constants.PAGESIZE;
        page = 1;
        infos = null;
        inputformat = BlastManager.INPUT_SEQUENCE;
    }   
    
    public int getTotal() {
        return infos.size();
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    
    public boolean isIsLowcomp() {
        return isLowcomp;
    }

    public void setIsLowcomp(boolean isLowcomp) {
        this.isLowcomp = isLowcomp;
    }

    public int getAlength() {
        return alength;
    }

    public void setAlength(int alength) {
        this.alength = alength;
    }

    public double getPid() {
        return pid;
    }

    public void setPid(double pid) {
        this.pid = pid;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List getInfos() {
        return infos;
    }

    public void setInfos(List infos) {
        this.infos = infos;
    }

    public String getQueryid() {
        return queryid;
    }

    public void setQueryid(String queryid) {
        this.queryid = queryid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getClonename() {
        return clonename;
    }

    public void setClonename(String clonename) {
        this.clonename = clonename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputformat() {
        return inputformat;
    }

    public void setInputformat(String inputformat) {
        this.inputformat = inputformat;
    }
}
