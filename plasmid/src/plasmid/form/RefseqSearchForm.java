/*
 * RefseqSearchForm.java
 *
 * Created on April 19, 2005, 2:41 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

import plasmid.Constants;
import plasmid.coreobject.*;
import plasmid.query.handler.GeneQueryHandler;

/**
 *
 * @author  DZuo
 */
public class RefseqSearchForm extends ActionForm {    
    protected String species;
    protected String refseqType;
    protected String searchType;
    protected String searchString;
    protected boolean cdna;
    protected boolean shrna;
    protected boolean genomicfragment;
    protected boolean tfbindsite;
    protected boolean genome;
    protected boolean fusion;
    protected boolean closed;
    protected String marker;
    protected boolean pdonr201;
    protected boolean pdonr221;
    protected boolean pdnrdual;
    protected boolean plk;
    protected boolean pby011;
    protected boolean pgex2tk;
    protected int pagesize;
    protected int page;
    protected String displayPage;
    protected String sortby;
    protected String cloneid;
    protected String button;
    protected String forward;
    protected String display;
    
    public String getSpecies() {return species;}
    public String getRefseqType() {return refseqType;}
    public String getSearchType() {return searchType;}
    public String getSearchString() {return searchString;}
    public boolean getCdna() {return cdna;}
    public boolean getShrna() {return shrna;}
    public boolean getGenomicfragment() {return genomicfragment;}
    public boolean getTfbindsite() {return tfbindsite;}
    public boolean getGenome() {return genome;}
    public boolean getFusion() {return fusion;}
    public boolean getClosed() {return closed;}
    public String getMarker() {return marker;}
    public boolean getPdonr201() {return pdonr201;}
    public boolean getPdonr221() {return pdonr221;}
    public boolean getPdnrdual() {return pdnrdual;}
    public boolean getPlk() {return plk;}
    public boolean getPby011() {return pby011;}
    public boolean getPgex2tk() {return pgex2tk;}
    public int getPagesize() {return pagesize;}
    public int getPage() {return page;}
    public String getDisplayPage() {return displayPage;}
    public String getSortby() {return sortby;}
    public String getCloneid() {return cloneid;}
    public String getButton() {return button;}
    public String getForward() {return forward;}
    public String getDisplay() {return display;}
    
    public void setSpecies(String s) {this.species = s;}
    public void setRefseqType(String s) {this.refseqType = s;}
    public void setSearchType(String s) {this.searchType = s;}
    public void setSearchString(String s) {this.searchString = s;}
    public void setCdna(boolean s) {this.cdna = s;}
    public void setShrna(boolean s) {this.shrna = s;}
    public void setGenomicfragment(boolean s) {this.genomicfragment = s;}
    public void setTfbindsite(boolean s) {this.tfbindsite = s;}
    public void setGenome(boolean b) {this.genome = b;}
    public void setFusion(boolean s) {this.fusion = s;}
    public void setClosed(boolean s) {this.closed = s;}
    public void setMarker(String s) {this.marker = s;}
    public void setPdonr201(boolean s) {this.pdonr201=s;}
    public void setPdonr221(boolean s) {this.pdonr221=s;}
    public void setPdnrdual(boolean s) {this.pdnrdual=s;}
    public void setPlk(boolean s) {this.plk=s;}
    public void setPby011(boolean s) {this.pby011=s;}
    public void setPgex2tk(boolean s) {this.pgex2tk=s;}
    public void setPagesize(int i) {this.pagesize = i;}
    public void setPage(int i) {this.page = i;}
    public void setDisplayPage(String s) {this.displayPage = s;}
    public void setSortby(String s) {this.sortby = s;}
    public void setCloneid(String id) {this.cloneid = id;}
    public void setButton(String s) {this.button = s;}
    public void setForward(String s) {this.forward = s;}
    public void setDisplay(String s) {this.display = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {     
        cdna = false;
        shrna = false;
        genomicfragment = false;
        tfbindsite = false;
        genome = false;
        sortby = null;
        button = null;
    }
    
    public void resetValues() {
        species = DnaInsert.HUMAN;
        //refseqType = RefseqNameType.SYMBOL;
        searchType = GeneQueryHandler.SYMBOL;
        searchString = null;
        marker = "All";
        page = 1;
        pagesize = Constants.PAGESIZE;
        displayPage = "indirect";
        display = "symbol";
    }
    
    public void resetBooleanValues() {
        cdna = true;
        shrna = true;
        genomicfragment = true;
        tfbindsite = true;
        genome = true;
    }
}

