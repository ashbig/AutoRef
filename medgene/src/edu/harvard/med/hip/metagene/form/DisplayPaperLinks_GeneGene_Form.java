/**
 * DisplayPaperLinks_GeneGene_Form.java
 *
 * Created on September 13, 2002, 11:50 AM
 */

package edu.harvard.med.hip.metagene.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  hweng
 */
public class DisplayPaperLinks_GeneGene_Form extends ActionForm{
    private String source_gene_index;
    private int target_gene_locusid = -1;
    private String target_gene_name;    
    
    public String getSource_gene_index(){
        return source_gene_index;
    }
    public String getTarget_gene_name(){
        return target_gene_name;
    }
    public int getTarget_gene_locusid(){
        return target_gene_locusid;
    }
    
    public void setSource_gene_index(String index){
        this.source_gene_index = index;
    }
    public void setTarget_gene_name(String name){
        this.target_gene_name = name;
    }
    public void setTarget_gene_locusid(int locusid){
        this.target_gene_locusid = locusid;
    }

    
}
