/*
 * UserGroup.java
 *
 * Created on June 29, 2001, 5:53 PM
 */

package edu.harvard.med.hip.flex.user;
import edu.harvard.med.hip.flex.Constants;
import edu.harvard.med.hip.flex.database.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  yanhuihu
 * @version 
 */
public class UserGroup {

    /** Creates new UserGroup */
    public UserGroup() {
    }

    public static LinkedList getMenu(String groupName)
    {
        LinkedList menulist = new LinkedList();
        String sql = ""; 
        ResultSet rs = null;  
        sql = "SELECT menuitem.menuitem, menuitem.description FROM menuitem,usergroupmenuitem"
        + " WHERE menuitem.menuitemid = usergroupmenuitem.menuitemid and usergroupmenuitem.usergroup= '"+groupName+"' order by menuitem.displayorder";
        //System.out.println(sql);
        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);

            while (rs.next()) {
                String item = rs.getString(1);
                String desc = rs.getString(2);
                MenuItem group = new MenuItem(item,desc);
                //System.out.println("tmp: "+tmp);
                menulist.addLast (group); 
            }
        }catch (SQLException sqlex) { sqlex.printStackTrace();
        }catch (FlexDatabaseException ex) {
            System.out.println("Error: "+ex.getMessage());
        }
        finally {DatabaseTransaction.closeResultSet(rs);} 
        return menulist;
    }
    //**************************************************************//
    //						Test									//
    //**************************************************************//
    public static void main(String[] args) {
        LinkedList result = UserGroup.getMenu("Customer");
        ListIterator iter = result.listIterator();
        System.out.println("result: "+result);     
            while (iter.hasNext()) {
                String tmp  = (String)iter.next();
                System.out.println("tmp: "+tmp); 
            }
        
            System.exit(0);
    }
    
} // UserGroup
