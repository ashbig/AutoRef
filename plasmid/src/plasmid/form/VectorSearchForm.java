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
import plasmid.coreobject.CloneVector;

/**
 *
 * @author  DZuo
 */
public class VectorSearchForm extends ActionForm {
    private String species;
    private List vectortype;
    private Map types;
    private List vectorname;
    private String display;
    private List clones;
    private String logicOperator[];
    private List vectornameBoolean;
    private List vectors;
    
    /** Creates a new instance of VectorSearchForm */
    public VectorSearchForm() {
    }
    
    public void setSpecies(String s) {       
        if(Constants.ALL.equals(s)) 
            s = null;
            
        this.species = s;
    }
    public String getSpecies() {return species;}
    
    public void setVectortype(int i, boolean b) {
        vectortype.set(i, new Boolean(b));
    }
    
    public boolean getVectortype(int i) {
        return ((Boolean)vectortype.get(i)).booleanValue();
    }
    
    public void setTypes(Map types) {this.types = types;}
    public Map getTypes() {return types;}
    
    public void setVectorname(int i, String s) {this.vectorname.set(i, s);}
    public String getVectorname(int i) {return (String)vectorname.get(i);}
    
    public void setVectornames(Collection s) {
        vectorname = new ArrayList();
        Iterator iter = s.iterator();
        while(iter.hasNext()) {
            CloneVector v = (CloneVector)iter.next();
            String n = v.getName();
            vectorname.add(n);
        }
    }
    
    public List getVectornames() {return vectorname;}
    
    public void setVectors(Collection s) {
        vectors = new ArrayList();
        vectors.addAll(s);
    }
    
    public List getVectors() {return vectors;}
    
    public void setVectornameBoolean(int i, boolean b) {
        vectornameBoolean.set(i, new Boolean(b));
    }
    
    public boolean getVectornameBoolean(int i) {
        return ((Boolean)vectornameBoolean.get(i)).booleanValue();
    }
    
    public String getDisplay() {return display;}
    public void setDisplay(String s) {this.display = s;}
    
    public List getClones() {return clones;}
    public void setClones(List l) {this.clones = l;}
    
    //   public int getPagesize() {return pagesize;}
    //   public void setPagesize(int i) {this.pagesize = i;}
    
    //   public int getPage() {return page;}
    //   public void setPage(int i) {this.page = i;}
    
    public String getLogicOperator(int i) {return logicOperator[i];}
    public void setLogicOperator(int i, String s) {this.logicOperator[i] = s;}
    
    public void resetVectortypes(Map types) {
        vectortype = new ArrayList();
        Set keys = types.keySet();
        Iterator iter = keys.iterator();
        int i=0;
        while(iter.hasNext()) {
            String s = (String)iter.next();
            List l = (List)types.get(s);
            int j=0;
            while(j<l.size()) {
                vectortype.add(i, new Boolean(false));
                i++;
                j++;
            }
        }
    }
    
    public void resetLogicOperators(int m) {
        logicOperator = new String[m];
        for(int i=0; i<m; i++) {
            logicOperator[i] = Constants.AND;
        }
    }
    
    public void resetVectornameBooleanValues(Collection vectors) {
        Iterator iter = vectors.iterator();
        vectornameBoolean = new ArrayList();
        int i = 0;
        while(iter.hasNext()) {
            CloneVector v = (CloneVector)iter.next();
            vectornameBoolean.add(i, new Boolean(false));
            i++;
        }
    }
    
    /**
     * Reset all properties to their default values.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(vectortype != null) {
            for(int i=0; i<vectortype.size(); i++) {
                vectortype.set(i, new Boolean(false));
            }
        }
        
        if(vectornameBoolean != null) {
            for(int i=0; i<vectornameBoolean.size(); i++) {
                vectornameBoolean.set(i, new Boolean(false));
            }
        }
    }
}
