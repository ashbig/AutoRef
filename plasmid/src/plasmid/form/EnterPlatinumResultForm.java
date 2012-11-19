/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package plasmid.form;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author DZuo
 */
public class EnterPlatinumResultForm extends ActionForm {
    private String orderid;
    private String researcher;
    private String status;
    private List sequences;
    private List results;
    private List methods;
    private String submit;
    private int alength;
    private double pid;
    private String orderids;

    public EnterPlatinumResultForm() {
        super();
        resetSequencesAndResults();
        alength=100;
        pid=90.0;
    }
    
    public void resetSequencesAndResults() {
        this.sequences = new ArrayList();
        this.results = new ArrayList();
        this.methods = new ArrayList();
    }
        
    /**
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if(sequences != null) {
            sequences = new ArrayList(sequences.size());
        }
        if(results != null) {
            results = new ArrayList(results.size());
        }
        if(methods != null) {
            methods = new ArrayList(methods.size());
        }
    }*/
   
    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getResearcher() {
        return researcher;
    }

    public void setResearcher(String researcher) {
        this.researcher = researcher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSequence(int i) {
        return (String)getSequences().get(i);
    }
    
    public void setSequence(int i, String s) {
        this.getSequences().set(i, s);
    }
    
    public String getResult(int i) {
        return (String)getResults().get(i);
    }
    
    public void setResult(int i, String s) {
        this.getResults().set(i, s);
    }
    
    public String getMethod(int i) {
        return (String)getMethods().get(i);
    }
    
    public void setMethod(int i, String s) {
        this.getMethods().set(i, s);
    }

    public List getSequences() {
        return sequences;
    }

    public void setSequences(List sequences) {
        this.sequences = sequences;
    }

    public List getResults() {
        return results;
    }

    public void setResults(List results) {
        this.results = results;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public List getMethods() {
        return methods;
    }

    public void setMethods(List methods) {
        this.methods = methods;
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

    public String getOrderids() {
        return orderids;
    }

    public void setOrderids(String orderids) {
        this.orderids = orderids;
    }
}
