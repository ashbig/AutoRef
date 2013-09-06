/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author dongmei
 */
@ManagedBean(name = "logoutBean")
@RequestScoped
public class LogoutBean implements Serializable {

    private static final long serialVersionUID = 916055190609044881L;

    /**
     * Default constructor.
     */
    public LogoutBean() {
    }

    /**
     * Logs the current user out by invalidating the session.
     * @return &quot;logout&quot; which is used by the {@literal faces-config.xml}
     * to redirect back to the {@literal index.xhtml} page.
     */
    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        session.invalidate();
        return "Logout.xhtml";
    }
}
