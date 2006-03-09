/*
 * AdvancedSearchForm.java
 *
 * Created on February 27, 2006, 4:47 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import java.util.*;

import plasmid.Constants;

/**
 *
 * @author  DZuo
 */
public class AdvancedSearchForm extends ActionForm {
    private String geneName;
    private String geneNameOp;
    private boolean geneNameAndOr;
    private String vectorName;
    private String vectorNameOp;
    private boolean vectorNameAndOr;
    private String vectorFeature;
    private String vectorFeatureOp;
    private boolean vectorFeatureAndOr;
    private String authorName;
    private String authorNameOp;
    private boolean authorNameAndOr;
    private String pmid;
    private String pmidOp;
    private boolean pmidAndOr;
    
    public String getGeneName() {return geneName;}
    public void setGeneName(String s) {this.geneName = s;}
    
    public String getGeneNameOp() {return geneNameOp;}
    public void setGeneNameOp(String s) {this.geneNameOp = s;}
    
    public boolean getGeneNameAndOr() {return geneNameAndOr;}
    public void setGeneNameAndOr(boolean b) {this.geneNameAndOr = b;}
    
    public String getVectorName() {return vectorName;}
    public void setVectorName(String s) {this.vectorName = s;}
    
    public String getVectorNameOp() {return vectorNameOp;}
    public void setVectorNameOp(String s) {this.vectorNameOp = s;}
    
    public boolean getVectorNameAndOr() {return vectorNameAndOr;}
    public void setVectorNameAndOr(boolean b) {this.vectorNameAndOr = b;}
    
    public String getVectorFeature() {return vectorFeature;}
    public void setVectorFeature(String s) {this.vectorFeature = s;}
    
    public String getVectorFeatureOp() {return vectorFeatureOp;}
    public void setVectorFeatureOp(String s) {this.vectorFeatureOp = s;}
    
    public boolean getVectorFeatureAndOr() {return vectorFeatureAndOr;}
    public void setVectorFeatureAndOr(boolean b) {this.vectorFeatureAndOr = b;}
    
    public String getAuthorName() {return authorName;}
    public void setAuthorName(String s) {this.authorName = s;}
    
    public String getAuthorNameOp() {return authorNameOp;}
    public void setAuthorNameOp(String s) {this.authorNameOp = s;}
    
    public boolean getAuthorNameAndOr() {return authorNameAndOr;}
    public void setAuthorNameAndOr(boolean b) {this.authorNameAndOr = b;}
    
    public String getPmid() {return pmid;}
    public void setPmid(String s) {this.pmid = s;}
    
    public String getPmidOp() {return pmidOp;}
    public void setPmidOp(String s) {this.pmidOp = s;}
    
    public boolean getPmidAndOr() {return pmidAndOr;}
    public void setPmidAndOr(boolean b) {this.pmidAndOr = b;}
    
    /** Creates a new instance of AdvancedSearchForm */
    public AdvancedSearchForm() {
    }
    
}
