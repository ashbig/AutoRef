/*
 * DownloadClonesForm.java
 *
 * Created on June 23, 2005, 4:08 PM
 */

package plasmid.form;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author  DZuo
 */
public class DownloadClonesForm extends ActionForm {
    private int orderid;
    private String type;
    private String collectionName;
    private String button;
    private String isBatch;
    
    /** Creates a new instance of DownloadClonesForm */
    public DownloadClonesForm() {
    }
    
    public int getOrderid() {return orderid;}
    public String getType() {return type;}
    public String getCollectionName() {return collectionName;}
    public String getButton() {return button;}
    public String getIsBatch() {return isBatch;}
    
    public void setOrderid(int id) {this.orderid = id;}
    public void setType(String s) {this.type = s;}
    public void setCollectionName(String s) {this.collectionName = s;}
    public void setButton(String s) {this.button = s;}
    public void setIsBatch(String s) {this.isBatch = s;}
}
