/*
 * menuitem.java
 *
 * Created on July 3, 2001, 5:22 PM
 */

package edu.harvard.med.hip.flex.user;

/**
 *
 * @author  yanhuihu
 * @version 
 */
public class MenuItem {
    
    private String item;
    private String description;
    

    /** Creates new menuitem */
    /**
     * Constructor.
     *
     * @param item: menuitem
     * @param description: description of the menuitems 
     * @return A Menuitem object.
     */
    public MenuItem(String item,String desc){
        this.item = item;
        this.description = desc;
    }
    
    /**
     * Return menu item.
     *
     * @return the string of menu item.
     */
    public String getMenuItem() {
        return item;
    }
    
    /**
     * Return desciption.
     *
     * @return description of a menuitem.
     */
    public String getDescription() {
        return description;
    }
 
    /**
     * set the menu item
     *
     * @param menuitem
     */
    public void setMenuItem(String menuItem) {
        this.item = menuItem;
    }
    
    /**
     * set the description
     *
     * @param The description
     */
    public void setDescription(String desc) {
        this.description = desc;
    }
}
