/*
 * OrderProcessManager.java
 *
 * Created on May 20, 2005, 9:23 AM
 */

package plasmid.process;

import java.util.*;
import java.sql.*;

import plasmid.coreobject.*;
import plasmid.query.coreobject.CloneInfo;
import plasmid.util.*;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;

/**
 *
 * @author  DZuo
 */
public class OrderProcessManager {
    public static final int PLATESIZE = 96;
    private List clones;
    private List collections;
    
    /** Creates a new instance of OrderProcessManager */
    public OrderProcessManager() {
    }
    
    public List getClones() {return clones;}
    public List getCollections() {return collections;}
    
    public void processShoppingCartItems(List items) {
        clones = new ArrayList();
        collections = new ArrayList();
        
        if(items == null || items.size() == 0)
            return;
        
        for(int i=0; i<items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)items.get(i);
            if(s.getType().equals(ShoppingCartItem.CLONE)) {
                clones.add(s);
            } else {
                collections.add(s);
            }
        }
    }
    
    public int getTotalCloneQuantity() {
        if(clones == null)
            return 0;
        
        int n = 0;
        for(int i=0; i<clones.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem)clones.get(i);
            n = n+item.getQuantity();
        }
        
        return n;
    }
    
    public int getTotalCollectionQuantity() {
        if(collections == null)
            return 0;
        
        int n = 0;
        for(int i=0; i<collections.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem)collections.get(i);
            n = n+item.getQuantity();
        }
        
        return n;
    }
    
    public double getTotalClonePrice(int platesize, String group) {
        ClonePriceCalculator calculator = new ClonePriceCalculator();
        return calculator.calculateClonePrice(getTotalCloneQuantity(), platesize, group);
    }
    
    public double getTotalCollectionPrice(String group) {
        return 0.0;
    }
    
    public int getTotalQuantity() {
        int n = getTotalCloneQuantity();
        int m = getTotalCollectionQuantity();
        
        return n+m;
    }
    
    public List updateShoppingCart(List items, List shoppingcart, List cloneCountList, List shoppingcartCopy) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            Map found = manager.queryClonesByCloneid(items, true, true);
            List newShoppingcart = new ArrayList();
            
            for(int i=0; i<shoppingcart.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)shoppingcart.get(i);
                String count = (String)cloneCountList.get(i);
                if(Integer.parseInt(count) > 0) {
                    String cloneid = item.getItemid();
                    Clone clone = (Clone)found.get(cloneid);
                    CloneInfo cloneInfo = new CloneInfo(clone);
                    cloneInfo.setQuantity(Integer.parseInt(count));
                    ShoppingCartItem s = new ShoppingCartItem(0, item.getItemid(), Integer.parseInt(count), item.getType());
                    shoppingcartCopy.add(s);
                    newShoppingcart.add(cloneInfo);
                }
            }
            
            return newShoppingcart;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getOrderClones(int orderid, User user) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager m = new CloneOrderManager(conn);
            List clones = null;
            if(User.INTERNAL.equals(user.getIsinternal())) {
                clones = m.queryOrderClones(orderid, null);
            } else {
                clones = m.queryOrderClones(orderid, user);
            }
            
            if(clones == null) {
                if(Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                }
                return null;
            }
            
            List items = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                OrderClones c = (OrderClones)clones.get(i);
                items.add((new Integer(c.getCloneid())).toString());
            }
            
            CloneManager manager = new CloneManager(conn);
            Map found = manager.queryClonesByCloneid(items, true, true);;
            
            List orderClones = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                OrderClones item = (OrderClones)clones.get(i);
                int quantity = item.getQuantity();
                String cloneid = ((new Integer(item.getCloneid())).toString());
                Clone clone = (Clone)found.get(cloneid);
                CloneInfo cloneInfo = new CloneInfo(clone);
                cloneInfo.setQuantity(quantity);
                orderClones.add(cloneInfo);
            }
            
            return orderClones;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public boolean saveShoppingCart(User user, List items) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            UserManager man = new UserManager(conn);
            if(!man.removeShoppingCart(user.getUserid())) {
                DatabaseTransaction.rollback(conn);
                if(Constants.DEBUG)
                    System.out.println("Cannot save shopping cart");
                return false;
            }
            if(!man.addShoppingCart(user.getUserid(), items)) {
                DatabaseTransaction.rollback(conn);
                if(Constants.DEBUG)
                    System.out.println("Cannot save shopping cart");
                return false;
            }
            DatabaseTransaction.commit(conn);
            return true;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public UserAddress getShippingAddress(User user) throws Exception {
        List addresses = processUserAddress(user);
        
        if(addresses == null)
            return null;
        
        for(int i=0; i<addresses.size(); i++) {
            UserAddress a = (UserAddress)addresses.get(i);
            if(UserAddress.SHIPPING.equals(a.getType()))
                return a;
        }
        
        return null;
    }
    
    public UserAddress getBillingAddress(User user) throws Exception {
        List addresses = processUserAddress(user);
        
        if(addresses == null)
            return null;
        
        for(int i=0; i<addresses.size(); i++) {
            UserAddress a = (UserAddress)addresses.get(i);
            if(UserAddress.BILLING.equals(a.getType()))
                return a;
        }
        
        return null;
    }
    
    protected List processUserAddress(User user) throws Exception {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = t.requestConnection();
        UserManager manager = new UserManager(conn);
        List addresses = manager.getUserAddresses(user.getUserid());
        DatabaseTransaction.closeConnection(conn);
        return addresses;
    }
    
    public int processOrder(CloneOrder order, User user, List addresses) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            UserManager m1 = new UserManager(conn);
            if(!m1.removeShoppingCart(user.getUserid())) {
                DatabaseTransaction.rollback(conn);
                if(Constants.DEBUG) {
                    System.out.println(m1.getErrorMessage());
                    System.out.println("Cannot save shopping cart");
                }
                return -1;
            }
            
            CloneOrderManager m = new CloneOrderManager(conn);
            int orderid = m.addCloneOrder(order, user);
            if(orderid < 0) {
                DatabaseTransaction.rollback(conn);
                if(Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                    System.out.println("Cannot save clone order to the database.");
                }
                return -1;
            }
            
            if(addresses != null) {
                UserManager man = new UserManager(conn);
                
                if(!man.updatePonumber(user.getPonumber(), user.getUserid())) {
                    DatabaseTransaction.rollback(conn);
                    if(Constants.DEBUG) {
                        System.out.println(man.getErrorMessage());
                        System.out.println("cannot update ponumber.");
                    }
                    return -1;
                }
                
                List a = man.getUserAddresses(user.getUserid());
                if(a == null) {
                    DatabaseTransaction.rollback(conn);
                    if(Constants.DEBUG) {
                        System.out.println(man.getErrorMessage());
                        System.out.println("cannot get user addresses.");
                    }
                    return -1;
                }
                
                if(a.size()>0) {
                    if(!man.updateUserAddresses(user.getUserid(), addresses)) {
                        DatabaseTransaction.rollback(conn);
                        if(Constants.DEBUG) {
                            System.out.println(man.getErrorMessage());
                            System.out.println("Cannot update user addresses.");
                        }
                        return -1;
                    }
                } else {
                    if(!man.addUserAddresses(user.getUserid(), addresses)){
                        DatabaseTransaction.rollback(conn);
                        if(Constants.DEBUG) {
                            System.out.println(man.getErrorMessage());
                            System.out.println("Cannot add user addresses.");
                        }
                        return -1;
                    }
                }
            }
            DatabaseTransaction.commit(conn);
            return orderid;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return -1;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getAllOrders(User user, String status) {
        if(user == null) {
            if(Constants.DEBUG) {
                System.out.println("user is null.");
            }
            return null;
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return null;
        }
        
        CloneOrderManager manager = new CloneOrderManager(conn);
        List orders = null;
        
        if(user.INTERNAL.equals(user.getIsinternal())) {
            orders = manager.queryCloneOrders(null, status);
        } else {
            orders = manager.queryCloneOrders(user, status);
        }
        
        DatabaseTransaction.closeConnection(conn);
        return orders;
    }
    
    public CloneOrder getCloneOrder(User user, int orderid) {
        if(user == null) {
            if(Constants.DEBUG) {
                System.out.println("user is null.");
            }
            return null;
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return null;
        }
        
        CloneOrderManager manager = new CloneOrderManager(conn);
        CloneOrder order = null;
        if(user.INTERNAL.equals(user.getIsinternal())) {
            try {
                order = manager.queryCloneOrder(null, orderid);
            } catch (Exception ex) {
                if(Constants.DEBUG) {
                    System.out.println(manager.getErrorMessage());
                    System.out.println(ex);
                }
                return null;
            }
        } else {
            try {
                order = manager.queryCloneOrder(user, orderid);
            } catch (Exception ex) {
                if(Constants.DEBUG) {
                    System.out.println(manager.getErrorMessage());
                    System.out.println(ex);
                }
                return null;
            }
        }
        
        DatabaseTransaction.closeConnection(conn);
        return order;
    }
}