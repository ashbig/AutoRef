/*
 * FileViewBean.java
 *
 * Created on October 10, 2007, 10:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package bean;

import dao.FilereferenceDAO;
import java.util.Map;
import javax.faces.context.FacesContext;
import transfer.FilereferenceTO;

/**
 *
 * @author dzuo
 */
public class FileViewBean {
    private int fileid;
    private FilereferenceTO file;
    
    /** Creates a new instance of FileViewBean */
    public FileViewBean() {
    }
    
    public void setFile(FilereferenceTO file) {
        this.file = file;
    }

    public FilereferenceTO getFile() {
        return file;
    }
    
    public String viewFile() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            Map requestMap = context.getExternalContext().getRequestParameterMap();
            fileid = Integer.parseInt((String)requestMap.get("fileid"));
            FilereferenceTO f = FilereferenceDAO.getFilereference(fileid);
            setFile(f);
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "viewfile";
    }

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }
}
