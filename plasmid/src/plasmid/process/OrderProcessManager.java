/*
 * OrderProcessManager.java
 *
 * Created on May 20, 2005, 9:23 AM
 */

package plasmid.process;

import java.util.*;
import java.sql.*;
import java.io.*;

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
    public static final String USA = "USA";
    public static final int PLATESIZE = 96;
    private List clones;
    private List collections;
    private List cloneids;
    private List collectionNames;
    
    /** Creates a new instance of OrderProcessManager */
    public OrderProcessManager() {
    }
    
    public List getClones() {return clones;}
    public List getCollections() {return collections;}
    public List getCloneids() {return cloneids;}
    public List getCollectionNames() {return collectionNames;}
    
    public void setAddToCartStatus(Collection cloneList, int cloneid, boolean b) {
        if(cloneList == null)
            return;
        
        Iterator iter = cloneList.iterator();
        while(iter.hasNext()) {
            CloneInfo c = (CloneInfo)iter.next();
            if(c.getCloneid() == cloneid) {
                c.setIsAddedToCart(b);
            }
        }
    }
    
    public List checkDistribition(User user, List items) {
        if(items == null || items.size() == 0)
            return new ArrayList();
        
        List cloneids = new ArrayList();
        for(int i=0; i<items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)items.get(i);
            if(s.getType().equals(ShoppingCartItem.CLONE)) {
                cloneids.add(s.getItemid());
            }
        }
        
        List restrictions = UserManager.getUserRestrictions(user);
        if(restrictions == null) {
            restrictions = new ArrayList();
        }
        restrictions.add(Clone.NO_RESTRICTION);
        
        DatabaseTransaction t = null;
        Connection conn = null;
        List restrictedClones = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            restrictedClones = manager.findRestrictedClones(cloneids, restrictions);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        return restrictedClones;
    }
    
    public List checkShippingRestriction(List items, String country) {
        if(USA.equals(country))
            return new ArrayList();
        
        if(items == null || items.size() == 0)
            return new ArrayList();
        
        List cloneids = new ArrayList();
        for(int i=0; i<items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)items.get(i);
            if(s.getType().equals(ShoppingCartItem.CLONE)) {
                cloneids.add(s.getItemid());
            }
        }
        
        DatabaseTransaction t = null;
        Connection conn = null;
        List restrictedClones = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            restrictedClones = manager.findShippingRestrictedClones(cloneids);
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        
        return restrictedClones;
    }
    
    public void processShoppingCartItems(List items) {
        clones = new ArrayList();
        collections = new ArrayList();
        cloneids = new ArrayList();
        collectionNames = new ArrayList();
        
        if(items == null || items.size() == 0)
            return;
        
        for(int i=0; i<items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem)items.get(i);
            if(s.getType().equals(ShoppingCartItem.CLONE)) {
                clones.add(s);
                cloneids.add(s.getItemid());
            } else {
                collections.add(s);
                collectionNames.add(s.getItemid());
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
    
    public double getTotalCollectionPrice(List collectionList, String group) {
        ClonePriceCalculator calculator = new ClonePriceCalculator();
        
        double price = 0.0;
        for(int i=0; i<collectionList.size(); i++) {
            CollectionInfo info = (CollectionInfo)collectionList.get(i);
            price = price+calculator.calculatePriceForCollection(info, group);
        }
        
        return price;
    }
    
    public int getTotalQuantity() {
        int n = getTotalCloneQuantity();
        int m = getTotalCollectionQuantity();
        
        return n+m;
    }
    
    public List updateShoppingCartForClones(List cloneids, List clones, List cloneCountList, List shoppingcartCopy) {
        List newShoppingcart = new ArrayList();
        
        try {
            for(int i=0; i<cloneCountList.size(); i++) {
                String count = (String)cloneCountList.get(i);
                if(Integer.parseInt(count) > 0) {
                    String cloneid = (String)cloneids.get(i);
                    CloneInfo cloneInfo = (CloneInfo)clones.get(i);
                    cloneInfo.setQuantity(Integer.parseInt(count));
                    ShoppingCartItem s = new ShoppingCartItem(0, cloneid, Integer.parseInt(count), ShoppingCartItem.CLONE);
                    shoppingcartCopy.add(s);
                    newShoppingcart.add(cloneInfo);
                }
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        }
        
        return newShoppingcart;
    }
    
    public List updateShoppingCartForCollections(List collectionNames, List collections, List collectionCountList, List shoppingcartCopy) {
        List newShoppingcart = new ArrayList();
        
        try {
            for(int i=0; i<collectionCountList.size(); i++) {
                String count = (String)collectionCountList.get(i);
                if(Integer.parseInt(count) > 0) {
                    String collectionName = (String)collectionNames.get(i);
                    CollectionInfo info = (CollectionInfo)collections.get(i);
                    info.setQuantity(Integer.parseInt(count));
                    ShoppingCartItem s = new ShoppingCartItem(0, collectionName, Integer.parseInt(count), ShoppingCartItem.COLLECTION);
                    shoppingcartCopy.add(s);
                    newShoppingcart.add(info);
                }
            }
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        }
        
        return newShoppingcart;
    }
    
    public List getShoppingCartClones(List cloneids, List clones) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            CloneManager manager = new CloneManager(conn);
            Map found = manager.queryClonesByCloneid(cloneids, true, true, false);
            List newShoppingcart = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)clones.get(i);
                String cloneid = item.getItemid();
                int quantity = item.getQuantity();
                CloneInfo cloneInfo = (CloneInfo)found.get(cloneid);
                cloneInfo.setQuantity(quantity);
                newShoppingcart.add(cloneInfo);
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
    
    public List getShoppingCartCollections(List collectionNames, List collections) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            
            CollectionManager man = new CollectionManager(conn);
            Map foundCollections = man.getCollections(conn, collectionNames);
            List newShoppingcartCollections = new ArrayList();
            for(int i=0; i<collections.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem)collections.get(i);
                String itemid = item.getItemid();
                int quantity = item.getQuantity();
                CollectionInfo info = (CollectionInfo)foundCollections.get(itemid);
                info.setQuantity(quantity);
                newShoppingcartCollections.add(info);
            }
            
            return newShoppingcartCollections;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getOrderClones(int orderid, User user, boolean isWorkingStorage) {
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
            Map found = manager.queryClonesByCloneid(items, true, true, isWorkingStorage, true, null, null, null, Clone.AVAILABLE);
            
            List orderClones = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                OrderClones item = (OrderClones)clones.get(i);
                int quantity = item.getQuantity();
                String cloneid = ((new Integer(item.getCloneid())).toString());
                CloneInfo cloneInfo = (CloneInfo)found.get(cloneid);
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
    
    public List getOrderClonesForCollection(String collectionname, User user, boolean isWorkingStorage) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CollectionManager m = new CollectionManager(conn);
            CollectionInfo info = m.getCollection(collectionname, true, isWorkingStorage);
            List clones = info.getClones();
            
            if(clones == null) {
                if(Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                }
                return null;
            }
            
            for(int i=0; i<clones.size(); i++) {
                CloneInfo clone = (CloneInfo)clones.get(i);
                clone.setQuantity(1);
            }
            
            return clones;
        } catch (Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public List getOrderCollections(int orderid, User user, boolean isWorkingStorage) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager m = new CloneOrderManager(conn);
            List clones = null;
            if(User.INTERNAL.equals(user.getIsinternal())) {
                clones = m.queryOrderCollections(orderid, null);
            } else {
                clones = m.queryOrderCollections(orderid, user);
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
                items.add(c.getCollectionname());
            }
            
            CollectionManager manager = new CollectionManager(conn);
            Map found = manager.getCollections(conn, items);
            
            List orderClones = new ArrayList();
            for(int i=0; i<clones.size(); i++) {
                OrderClones item = (OrderClones)clones.get(i);
                int quantity = item.getQuantity();
                String name = item.getCollectionname();
                CollectionInfo info = (CollectionInfo)found.get(name);
                info.setQuantity(quantity);
                orderClones.add(info);
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
                /**
                 * if(!man.updatePonumber(user.getPonumber(), user.getUserid())) {
                 * DatabaseTransaction.rollback(conn);
                 * if(Constants.DEBUG) {
                 * System.out.println(man.getErrorMessage());
                 * System.out.println("cannot update ponumber.");
                 * }
                 * return -1;
                 * }*/
                
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
                DatabaseTransaction.closeConnection(conn);
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
                DatabaseTransaction.closeConnection(conn);
                return null;
            }
        }
        
        DatabaseTransaction.closeConnection(conn);
        return order;
    }
    
    public void writeCloneList(List clones, PrintWriter out, boolean isWorkingStorage) {
        writeCloneList(clones, out, isWorkingStorage, true);
    }
    
    public void writeCloneList(List clones, PrintWriter out, boolean isWorkingStorage, boolean isQuantity) {
        if(isWorkingStorage) {
            out.print("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tSelection Markers\tMutation\tDiscrepancy");
            if(isQuantity) {
                out.print("\tQuantity");
            }
            out.println("\tContainer\tWell\tPosition\tSpecial Treatment");
        } else {
            if(isQuantity) {
                out.println("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tSelection Markers\tMutation\tDiscrepancy\tQuantity");
            } else {
                out.println("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tSelection Markers\tMutation\tDiscrepancy");
            }
        }
        
        for(int i=0; i<clones.size(); i++) {
            CloneInfo c = (CloneInfo)clones.get(i);
            if(Clone.NOINSERT.equals(c.getType())) {
                out.print(c.getName()+"\t"+c.getType()+"\t\t\t\t\t\t\t"+c.getVectorname()+"\t");
                
                List selections = c.getSelections();
                for(int n=0; n<selections.size(); n++) {
                    CloneSelection cs = (CloneSelection)selections.get(n);
                    out.print(cs.getHosttype()+": "+cs.getMarker()+";");
                }
                
                out.print("\t\t");
                
                if(isQuantity) {
                    out.print("\t"+c.getQuantity());
                }
                
                if(isWorkingStorage) {
                    out.println("\t"+c.getPlate()+"\t"+c.getWell()+"\t"+c.getPosition()+"\t"+c.getSpecialtreatment());
                } else {
                    out.println();
                }
            } else {
                List inserts = c.getInserts();
                for(int j=0; j<inserts.size(); j++) {
                    DnaInsert insert = (DnaInsert)inserts.get(j);
                    out.print(c.getName()+"\t"+c.getType()+"\t"+insert.getGeneid()+"\t"+insert.getName()+"\t"+insert.getDescription()+"\t"+insert.getTargetgenbank()+"\t"+insert.getTargetseqid()+"\t"+insert.getFormat()+"\t"+c.getVectorname()+"\t");
                    
                    List selections = c.getSelections();
                    for(int n=0; n<selections.size(); n++) {
                        CloneSelection cs = (CloneSelection)selections.get(n);
                        out.print(cs.getHosttype()+": "+cs.getMarker()+";");
                    }
                    
                    out.print("\t"+insert.getHasmutation()+"\t"+insert.getHasdiscrepancy());
                    
                    if(isQuantity) {
                        out.print("\t"+c.getQuantity());
                    }
                    
                    if(isWorkingStorage) {
                        out.println("\t"+c.getPlate()+"\t"+c.getWell()+"\t"+c.getPosition()+"\t"+c.getSpecialtreatment());
                    } else {
                        out.println();
                    }
                }
            }
        }
    }
    
    public List groupClonesByGrowth(List clones) {
        List groups = new ArrayList();
        Collections.sort(clones, new CloneGrowthComparator());
        int lastid = 0;
        int currentid = 0;
        List l = null;
        for(int i=0; i<clones.size(); i++) {
            Clone c = (CloneInfo)clones.get(i);
            currentid = c.getRecommendedGrowthCondition().getGrowthid();
            if(currentid == lastid) {
                l.add(c);
            } else {
                if(l != null) {
                    groups.add(l);
                }
                l = new ArrayList();
                l.add(c);
                lastid = currentid;
            }
        }
        groups.add(l);
        
        return groups;
    }
    
    public boolean updateOrderStatus(int orderid, String status) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            if(manager.updateOrderStatus(orderid, status)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch(Exception ex) {
            DatabaseTransaction.rollback(conn);
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public boolean updateAllOrderStatus(List orders) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            if(manager.updateAllOrderStatus(orders)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch(Exception ex) {
            DatabaseTransaction.rollback(conn);
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public static List getCountryList() {
        List c = new ArrayList();
        String sql = "select name from country order by name";
        
        DatabaseTransaction t = null;
        ResultSet rs = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while(rs.next()) {
                String n = rs.getString(1);
                c.add(n);
            }
        } catch(Exception ex) {
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return c;
    }
    
    public boolean updateShipping(CloneOrder order) {
        DatabaseTransaction t = null;
        Connection conn = null;
        
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            if(manager.updateOrderWithShipping(order)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch(Exception ex) {
            DatabaseTransaction.rollback(conn);
            if(Constants.DEBUG) {
                System.out.println(ex);
            }
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
    
    public void printBioTracyWorklist(List clones, String filename) throws Exception {
        if(clones == null || filename == null)
            return;
        
        OutputStreamWriter f = new FileWriter(filename);
        for(int i=0; i<clones.size(); i++) {
            CloneInfo clone = (CloneInfo)clones.get(i);
            String tube = clone.getPlate();
            f.write(tube+"\n");
        }
        f.close();
    }
    
    public void printBioTracySummary(List groups, String filename, String worklistFilenameRoot) throws Exception {
        if(groups == null || filename == null)
            return;
        
        OutputStreamWriter f = new FileWriter(filename);
        f.write("File Name\tHost Type\tSelection Condition\tGrowth Condition\tComments\n");
        for(int i=0; i<groups.size(); i++) {
            List clones = (List)groups.get(i);
            CloneInfo clone = (CloneInfo)clones.get(0);
            GrowthCondition g = clone.getRecommendedGrowthCondition();
            String fn = worklistFilenameRoot+"_"+(i+1)+".txt";
            f.write(fn+"\t"+g.getHosttype()+"\t"+g.getSelection()+"\t"+g.getCondition()+"\t"+g.getComments()+"\n");
        }
        f.close();
    }
}