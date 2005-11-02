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

/**
 *
 * @author  DZuo
 */
public class RefseqSearchForm extends ActionForm {
    private String species;
    private String refseqType;
    private String searchType;
    private String searchString;
    private boolean cdna;
    private boolean shrna;
    private boolean genomicfragment;
    private boolean tfbindsite;
    private boolean fusion;
    private boolean closed;
    private String marker;
    private boolean pdonr201;
    private boolean pdonr221;
    private boolean pdnrdual;
    private boolean plk;
    private boolean pby011;
    private boolean pgex2tk;
    private int pagesize = 25;
    private int page = 1;
    private String displayPage;
    private String sortby;
    private String cloneid;
    private String button;
    
    public String getSpecies() {return species;}
    public String getRefseqType() {return refseqType;}
    public String getSearchType() {return searchType;}
    public String getSearchString() {return searchString;}
    public boolean getCdna() {return cdna;}
    public boolean getShrna() {return shrna;}
    public boolean getGenomicfragment() {return genomicfragment;}
    public boolean getTfbindsite() {return tfbindsite;}
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
    
    public void setSpecies(String s) {this.species = s;}
    public void setRefseqType(String s) {this.refseqType = s;}
    public void setSearchType(String s) {this.searchType = s;}
    public void setSearchString(String s) {this.searchString = s;}
    public void setCdna(boolean s) {this.cdna = s;}
    public void setShrna(boolean s) {this.shrna = s;}
    public void setGenomicfragment(boolean s) {this.genomicfragment = s;}
    public void setTfbindsite(boolean s) {this.tfbindsite = s;}
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
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        species = null;
        refseqType = null;
        searchType = null;
        searchString = null;
        cdna = false;
        shrna = false;
        genomicfragment = false;
        tfbindsite = false;
        fusion = false;
        closed=false;
        marker = "All";
        pdonr201 = false;
        pdonr221 = false;
        pdnrdual = false;
        plk = false;
        pby011 = false;
        pgex2tk = false;
        pagesize = 25;
    }
}

