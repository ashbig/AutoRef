/*
 * VectorSearchForm.java
 *
 * Created on February 2, 2006, 2:30 PM
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
public class VectorSearchForm extends ActionForm {
    private String species;
    private boolean vectortype[][];
    private List vectorname;
    private String display;
    private List clones;
    private int pagesize;
    private int page;
    private String logicOperator;
    
    /** Creates a new instance of VectorSearchForm */
    public VectorSearchForm() {
    }
    
    public void setSpecies(String s) {this.species = s;}
    public String getSpecies() {return species;}
    
    public void setVectortype(int i, int j, boolean s) {
        this.vectortype[i][j] = s;
    }
    public boolean getVectortype(int i, int j) {
        return vectortype[i][j];
    }
    
    public void setVectorname(int i, String s) {this.vectorname.set(i, s);}
    public String getVectorname(int i) {return (String)vectorname.get(i);}
    
    public void setVectortypes(int m, int n) {
        vectortype = new boolean[m][n];
        for(int i=0; i<m; i++) {
            for(int j=0; j<n; j++) {
                vectortype[i][j] = false;
            }
        }
    }
    public boolean[][] getVectortype() {return vectortype;}
    
    public void setVectorname(Set s) {
        vectorname = new ArrayList();
        Iterator iter = s.iterator();
        while(iter.hasNext()) {
            String n = (String)iter.next();
            vectorname.add(n);
        }
    }
    
    public List getVectorname() {return vectorname;}
    
    public String getDisplay() {return display;}
    public void setDisplay(String s) {this.display = s;}
    
    public List getClones() {return clones;}
    public void setClones(List l) {this.clones = l;}
    
    public int getPagesize() {return pagesize;}
    public void setPagesize(int i) {this.pagesize = i;}
    
    public int getPage() {return page;}
    public void setPage(int i) {this.page = i;}
    
    public String getLogicOperator() {return logicOperator;}
    public void setLogicOperator(String s) {this.logicOperator = s;}
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(vectortype != null) {
            for(int i=0; i<vectortype.length; i++) {
                boolean [] b = vectortype[i];
                for(int j=0; j<vectortype[i].length; j++) {
                    vectortype[i][j] = false;
                }
            }
        }
        
        logicOperator = Constants.AND;
    }
}
