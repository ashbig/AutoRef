/*
 * Userprofile.java
 *
 * Created on January 30, 2002, 12:03 PM
 */

package edu.harvard.med.hip.metagene.user;

/**
 *
 * @author  dzuo
 * @version 
 */
public class User {
    private String username;
    private String password;
    private String remindtext;
    private String firstname;
    private String lastname;
    private String workphone;
    private String useremail;
    private String institute;
    
    /** Creates new Userprofile */
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(String username, String password, String remindtext, String firstname, String lastname, String workphone, String useremail, String institute) {
        this.username = username;
        this.password = password;
        this.remindtext = remindtext;
        this.firstname = firstname;
        this.lastname = lastname;
        this.workphone = workphone;
        this.useremail = useremail;
        this.institute = institute;
    }
}
