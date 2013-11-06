/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.bean;

import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import plasmid.Constants;
import plasmid.coreobject.User;

/**
 *
 * @author Lab User
 */
@ManagedBean(name = "plasmidBean")
@SessionScoped
public class PlasmidBean {

    /**
     * @return the user
     */
    public User getUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        User user = (User) map.get(Constants.USER_KEY);
        return user;
    }
    
    public boolean isUserExist() {
        if(getUser() == null) {
            return false;
        }
        return true;
    }
    
    public boolean isUserInternal() {
        User user = getUser();
        if(user != null && User.INTERNAL.equals(user.getIsinternal())) {
            return true;
        }
        return false;
    }
}
