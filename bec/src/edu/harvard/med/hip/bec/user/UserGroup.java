/*
 * UserGroup.java
 *
 * Created on June 29, 2001, 5:53 PM
 */

package edu.harvard.med.hip.bec.user;
import edu.harvard.med.hip.bec.Constants;
import edu.harvard.med.hip.bec.database.*;
import java.sql.*;
import java.util.*;

/**
 *
 * @author  yanhuihu
 * @version
 */
public class UserGroup
{
    
    /** Creates new UserGroup */
    public UserGroup()
    {
    }
    
    public static ArrayList getMenu(String groupName)
    {
        ArrayList menulist = new ArrayList();
        String sql = "";
        ResultSet rs = null;
        sql = "SELECT menuitem.menuitem, menuitem.description, menuitem.groupname FROM menuitem, usergroupmenuitem"
        + " WHERE menuitem.menuitemid = usergroupmenuitem.menuitemid and usergroupmenuitem.usergroup= '"+groupName+"' order by menuitem.displayorder";
        //System.out.println(sql);
        try
        {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            
            while (rs.next())
            {
                String item = rs.getString(1);
                String desc = rs.getString(2);
                String mg = rs.getString(3);
                if (mg == null) mg ="None";
                MenuItem group = new MenuItem(item,desc, mg);
                menulist.add(group);
            }
        }catch (SQLException sqlex)
        { sqlex.printStackTrace();
        }catch (BecDatabaseException ex)
        {
            System.err.println("Error: "+ex.getMessage());
        }
        finally
        {DatabaseTransaction.closeResultSet(rs);}
        return menulist;
    }
    //**************************************************************//
    //						Test									//
    //**************************************************************//
    public static void main(String[] args)
    {
        ArrayList result = UserGroup.getMenu("System Admin");
     
        /*
         ListIterator iter = result.listIterator();
        System.out.println("result: "+result);
        while (iter.hasNext())
        {
            String tmp  = (String)iter.next();
            System.out.println("tmp: "+tmp);
        }
        */
        System.exit(0);
    }
    
} // UserGroup
