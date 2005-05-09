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
    private String cdna = "on";
    private String shrna = "on";
    private String fusion = "on";
    private String closed="on";
    private String marker = "All";
    private String pdonr201 = "on";
    private String pdonr221 = "on";
    private String pdnrdual = "on";
    private String plk = "on";
    private String pby011 = "on";
    private String pgex2tk = "on";
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
    public String getCdna() {return cdna;}
    public String getShrna() {return shrna;}
    public String getFusion() {return fusion;}
    public String getClosed() {return closed;}
    public String getMarker() {return marker;}
    public String getPdonr201() {return pdonr201;}
    public String getPdonr221() {return pdonr221;}
    public String getPdnrdual() {return pdnrdual;}
    public String getPlk() {return plk;}
    public String getPby011() {return pby011;}
    public String getPgex2tk() {return pgex2tk;}
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
    public void setCdna(String s) {this.cdna = s;}
    public void setShrna(String s) {this.shrna = s;}
    public void setFusion(String s) {this.fusion = s;}
    public void setClosed(String s) {this.closed = s;}
    public void setMarker(String s) {this.marker = s;}
    public void setPdonr201(String s) {this.pdonr201=s;}
    public void setPdonr221(String s) {this.pdonr221=s;}
    public void setPdnrdual(String s) {this.pdnrdual=s;}
    public void setPlk(String s) {this.plk=s;}
    public void setPby011(String s) {this.pby011=s;}
    public void setPgex2tk(String s) {this.pgex2tk=s;}
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
        cdna = "on";
        shrna = "on";
        fusion = "on";
        closed="on";
        marker = "All";
        pdonr201 = "on";
        pdonr221 = "on";
        pdnrdual = "on";
        plk = "on";
        pby011 = "on";
        pgex2tk = "on";
        pagesize = 25;
    }
}

