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
    private String m_group; 

    /** Creates new menuitem */
    /**
     * Constructor.
     *
     * @param item: menuitem
     * @param description: description of the menuitems 
     * @return A Menuitem object.
     */
    public MenuItem(String item,String desc, String group){
        this.item = item;
        this.description = desc;
        m_group = group;
    }
    
    /**
     * Return menu item.
     *
     * @return the string of menu item.
     */
    public String getMenuItem() {
        return item;
    }
    
     public String getMenuGroup() {
        return m_group;
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
     * set the menu item
     *
     * @param menuitem
     */
    public void setMenuGroup(String group) {
        m_group = group;
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
