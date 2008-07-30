/*
 * ResearcherTO.java
 *
 * Created on April 25, 2007, 1:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;

/**
 *
 * @author dzuo
 */
public class ResearcherTO implements Serializable {
    private String name;
    private String email;
    private String password;
    private String usergroup;
    private String id;
    
    /** Creates a new instance of ResearcherTO */
    public ResearcherTO() {
    }

    public ResearcherTO(String name, String email, String pass, String usergroup, String id) {
        this.setName(name);
        this.setEmail(email);
        this.setPassword(pass);
        this.setUsergroup(usergroup);
        this.setId(id);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }
    
}
