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
import plasmid.query.handler.*;
import plasmid.util.*;
import plasmid.database.*;
import plasmid.Constants;
import plasmid.database.DatabaseManager.*;
import com.jscape.inet.sftp.Sftp;

/**
 *
 * @author  DZuo
 */
public class OrderProcessManager {

    public static final String USA = "USA";
    public static final int PLATESIZE = 96;
    public static final String BATCH_ORDER_FILE_DILIM = ",";
    private List clones;
    private List collections;
    private List cloneids;
    private List collectionNames;

    /** Creates a new instance of OrderProcessManager */
    public OrderProcessManager() {
    }

    public List getClones() {
        return clones;
    }

    public List getCollections() {
        return collections;
    }

    public List getCloneids() {
        return cloneids;
    }

    public List getCollectionNames() {
        return collectionNames;
    }

    public void setAddToCartStatus(Collection cloneList, int cloneid, boolean b) {
        if (cloneList == null) {
            return;
        }
        Iterator iter = cloneList.iterator();
        while (iter.hasNext()) {
            CloneInfo c = (CloneInfo) iter.next();
            if (c.getCloneid() == cloneid) {
                c.setIsAddedToCart(b);
            }
        }
    }

    public List checkDistribition(User user, List items) {
        if (items == null || items.size() == 0) {
            return new ArrayList();
        }
        List cloneids = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.CLONE)) {
                cloneids.add(s.getItemid());
            }
        }

        List collectionNames = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.COLLECTION)) {
                collectionNames.add(s.getItemid());
            }
        }

        List restrictions = UserManager.getUserRestrictions(user);
        if (restrictions == null) {
            restrictions = new ArrayList();
        }
        restrictions.add(Clone.NO_RESTRICTION);

        DatabaseTransaction t = null;
        Connection conn = null;
        List restrictedClones = null;
        List restrictedCollections = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneManager manager = new CloneManager(conn);
            restrictedClones = manager.findRestrictedClones(cloneids, restrictions);
            CollectionManager m2 = new CollectionManager(conn);
            restrictedCollections = m2.findRestrictedCollections(collectionNames, restrictions);
            if (restrictedCollections != null) {
                restrictedClones.addAll(restrictedCollections);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        return restrictedClones;
    }

    public List checkShippingRestriction(List items, String country) {
        if (USA.equals(country)) {
            return new ArrayList();
        }
        if (items == null || items.size() == 0) {
            return new ArrayList();
        }
        List cloneids = new ArrayList();
        List collectionNames = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.CLONE)) {
                cloneids.add(s.getItemid());
            } else if (s.getType().equals(ShoppingCartItem.COLLECTION)) {
                collectionNames.add(s.getItemid());
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
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }

        List restrictedCollections = CollectionManager.findShippingRestrictedCollections(collectionNames);
        restrictedClones.addAll(restrictedCollections);

        return restrictedClones;
    }

    public void processShoppingCartItems(List items) {
        clones = new ArrayList();
        collections = new ArrayList();
        cloneids = new ArrayList();
        collectionNames = new ArrayList();

        if (items == null || items.size() == 0) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.CLONE)) {
                clones.add(s);
                cloneids.add(s.getItemid());
            } else {
                collections.add(s);
                collectionNames.add(s.getItemid());
            }
        }
    }

    public int getTotalCloneQuantity() {
        if (clones == null) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < clones.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem) clones.get(i);
            n = n + item.getQuantity();
        }

        return n;
    }

    public int getTotalCollectionQuantity() {
        if (collections == null) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < collections.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem) collections.get(i);
            n = n + item.getQuantity();
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
        for (int i = 0; i < collectionList.size(); i++) {
            CollectionInfo info = (CollectionInfo) collectionList.get(i);
            price = price + calculator.calculatePriceForCollection(info, group);
        }

        return price;
    }

    public int getTotalQuantity() {
        int n = getTotalCloneQuantity();
        int m = getTotalCollectionQuantity();

        return n + m;
    }

    public List updateShoppingCartForClones(List clones, String cloneid, List shoppingcartCopy) {
        List newShoppingcart = new ArrayList();
        try {
            for (int i = 0; i < clones.size(); i++) {
                CloneInfo cloneInfo = (CloneInfo) clones.get(i);
                if (cloneInfo.getCloneid() != Integer.parseInt(cloneid)) {
                    ShoppingCartItem s = new ShoppingCartItem(0, (new Integer(cloneInfo.getCloneid())).toString(), 1, ShoppingCartItem.CLONE);
                    shoppingcartCopy.add(s);
                    newShoppingcart.add(cloneInfo);
                }
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        }

        return newShoppingcart;
    }

    public List updateShoppingCartForCollections(List collections, String collectionName, List shoppingcartCopy) {
        List newShoppingcart = new ArrayList();

        try {
            for (int i = 0; i < collections.size(); i++) {
                CollectionInfo info = (CollectionInfo) collections.get(i);
                if (!info.getName().equals(collectionName)) {
                    ShoppingCartItem s = new ShoppingCartItem(0, info.getName(), 1, ShoppingCartItem.COLLECTION);
                    shoppingcartCopy.add(s);
                    newShoppingcart.add(info);
                }
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        }

        return newShoppingcart;
    }

    public List getShoppingCartClones(List cloneids, List clones, List batchorders, String isbatch) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            CloneManager manager = new CloneManager(conn);
            Map found = manager.queryClonesByCloneid(cloneids, true, true, false);
            List newShoppingcart = new ArrayList();
            for (int i = 0; i < clones.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem) clones.get(i);
                String cloneid = item.getItemid();
                int quantity = item.getQuantity();
                CloneInfo cloneInfo = (CloneInfo) found.get(cloneid);
                cloneInfo.setQuantity(quantity);
                if ("Y".equals(isbatch)) {
                    for (int j = 0; j < batchorders.size(); j++) {
                        BatchOrder b = (BatchOrder) batchorders.get(j);
                        if (b.getCloneid() == Integer.parseInt(cloneid)) {
                            cloneInfo.setTargetPlate(b.getPlate());
                            cloneInfo.setTargetWell(b.getWell());
                            break;
                        }
                    }
                }
                newShoppingcart.add(cloneInfo);
            }

            return newShoppingcart;
        } catch (Exception ex) {
            if (Constants.DEBUG) {
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
            for (int i = 0; i < collections.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem) collections.get(i);
                String itemid = item.getItemid();
                int quantity = item.getQuantity();
                CollectionInfo info = (CollectionInfo) foundCollections.get(itemid);
                info.setQuantity(quantity);
                newShoppingcartCollections.add(info);
            }

            return newShoppingcartCollections;
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public List getOrderClones(int orderid, User user, boolean isWorkingStorage, String isBatch) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager m = new CloneOrderManager(conn);
            List clones = null;
            if (User.INTERNAL.equals(user.getIsinternal())) {
                if ("Y".equals(isBatch)) {
                    clones = m.queryBatchOrderClones(orderid, null);
                } else {
                    clones = m.queryOrderClones(orderid, null);
                }
            } else {
                if ("Y".equals(isBatch)) {
                    clones = m.queryBatchOrderClones(orderid, user);
                } else {
                    clones = m.queryOrderClones(orderid, user);
                }
            }

            if (clones == null) {
                if (Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                }
                return null;
            }

            List items = new ArrayList();
            for (int i = 0; i < clones.size(); i++) {
                OrderClones c = (OrderClones) clones.get(i);
                items.add((new Integer(c.getCloneid())).toString());
            }

            CloneManager manager = new CloneManager(conn);
            Map found = manager.queryClonesByCloneid(items, true, true, isWorkingStorage, true, null, null, null, null);

            List orderClones = new ArrayList();
            for (int i = 0; i < clones.size(); i++) {
                OrderClones item = (OrderClones) clones.get(i);
                int quantity = item.getQuantity();
                String cloneid = ((new Integer(item.getCloneid())).toString());
                CloneInfo cloneInfo = (CloneInfo) found.get(cloneid);
                cloneInfo.setQuantity(quantity);
                cloneInfo.setTargetPlate(item.getPlate());
                cloneInfo.setTargetWell(item.getWell());
                orderClones.add(cloneInfo);
            }

            return orderClones;
        } catch (Exception ex) {
            if (Constants.DEBUG) {
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

            if (clones == null) {
                if (Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                }
                return null;
            }

            for (int i = 0; i < clones.size(); i++) {
                CloneInfo clone = (CloneInfo) clones.get(i);
                clone.setQuantity(1);
            }

            return clones;
        } catch (Exception ex) {
            if (Constants.DEBUG) {
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
            if (User.INTERNAL.equals(user.getIsinternal())) {
                clones = m.queryOrderCollections(orderid, null);
            } else {
                clones = m.queryOrderCollections(orderid, user);
            }

            if (clones == null) {
                if (Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                }
                return null;
            }

            List items = new ArrayList();
            for (int i = 0; i < clones.size(); i++) {
                OrderClones c = (OrderClones) clones.get(i);
                items.add(c.getCollectionname());
            }

            CollectionManager manager = new CollectionManager(conn);
            Map found = manager.getCollections(conn, items);

            List orderClones = new ArrayList();
            for (int i = 0; i < clones.size(); i++) {
                OrderClones item = (OrderClones) clones.get(i);
                int quantity = item.getQuantity();
                String name = item.getCollectionname();
                CollectionInfo info = (CollectionInfo) found.get(name);
                info.setQuantity(quantity);
                orderClones.add(info);
            }

            return orderClones;
        } catch (Exception ex) {
            if (Constants.DEBUG) {
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
            if (!man.removeShoppingCart(user.getUserid())) {
                DatabaseTransaction.rollback(conn);
                if (Constants.DEBUG) {
                    System.out.println("Cannot save shopping cart");
                }
                return false;
            }
            if (!man.addShoppingCart(user.getUserid(), items)) {
                DatabaseTransaction.rollback(conn);
                if (Constants.DEBUG) {
                    System.out.println("Cannot save shopping cart");
                }
                return false;
            }
            DatabaseTransaction.commit(conn);
            return true;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
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

        if (addresses == null) {
            return null;
        }
        for (int i = 0; i < addresses.size(); i++) {
            UserAddress a = (UserAddress) addresses.get(i);
            if (UserAddress.SHIPPING.equals(a.getType())) {
                return a;
            }
        }

        return null;
    }

    public UserAddress getBillingAddress(User user) throws Exception {
        List addresses = processUserAddress(user);

        if (addresses == null) {
            return null;
        }
        for (int i = 0; i < addresses.size(); i++) {
            UserAddress a = (UserAddress) addresses.get(i);
            if (UserAddress.BILLING.equals(a.getType())) {
                return a;
            }
        }

        return null;
    }

    protected List processUserAddress(User user) throws Exception {
        DatabaseTransaction t = DatabaseTransaction.getInstance();
        Connection conn = null;
        List addresses = null;

        try {
            conn = t.requestConnection();
            UserManager manager = new UserManager(conn);
            addresses = manager.getUserAddresses(user.getUserid());
        } catch (Exception ex) {
            throw ex;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return addresses;
    }

    public int processOrder(CloneOrder order, User user, List addresses) {
        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            UserManager m1 = new UserManager(conn);
            if (!m1.removeShoppingCart(user.getUserid())) {
                DatabaseTransaction.rollback(conn);
                if (Constants.DEBUG) {
                    System.out.println(m1.getErrorMessage());
                    System.out.println("Cannot save shopping cart");
                }
                return -1;
            }

            CloneOrderManager m = new CloneOrderManager(conn);
            int orderid = 0;
            if (order.getIsBatch().equals("Y")) {
                orderid = m.addBatchCloneOrder(order, user);
            } else {
                orderid = m.addCloneOrder(order, user);
            }
            if (orderid <= 0) {
                DatabaseTransaction.rollback(conn);
                if (Constants.DEBUG) {
                    System.out.println(m.getErrorMessage());
                    System.out.println("Cannot save clone order to the database.");
                }
                return -1;
            }

            if (addresses != null) {
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
                if (a == null) {
                    DatabaseTransaction.rollback(conn);
                    if (Constants.DEBUG) {
                        System.out.println(man.getErrorMessage());
                        System.out.println("cannot get user addresses.");
                    }
                    return -1;
                }

                if (a.size() > 0) {
                    if (!man.updateUserAddresses(user.getUserid(), addresses)) {
                        DatabaseTransaction.rollback(conn);
                        if (Constants.DEBUG) {
                            System.out.println(man.getErrorMessage());
                            System.out.println("Cannot update user addresses.");
                        }
                        return -1;
                    }
                } else {
                    if (!man.addUserAddresses(user.getUserid(), addresses)) {
                        DatabaseTransaction.rollback(conn);
                        if (Constants.DEBUG) {
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
            if (Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return -1;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public List getAllOrders(User user, String status) {
        if (user == null) {
            if (Constants.DEBUG) {
                System.out.println("user is null.");
            }
            return null;
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        List orders = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            CloneOrderManager manager = new CloneOrderManager(conn);

            if (user.INTERNAL.equals(user.getIsinternal())) {
                orders = manager.queryCloneOrders(null, status);
            } else {
                orders = manager.queryCloneOrders(user, status);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println("Cannot get database connection.");
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return orders;
    }

    public CloneOrder getCloneOrder(User user, int orderid) {
        if (user == null) {
            if (Constants.DEBUG) {
                System.out.println("user is null.");
            }
            return null;
        }

        DatabaseTransaction t = null;
        Connection conn = null;
        CloneOrderManager manager = null;
        CloneOrder order = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            manager = new CloneOrderManager(conn);
            if (user.INTERNAL.equals(user.getIsinternal())) {
                order = manager.queryCloneOrder(null, orderid);
            } else {
                order = manager.queryCloneOrder(user, orderid);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(manager.getErrorMessage());
                System.out.println(ex);
            }
            DatabaseTransaction.closeConnection(conn);
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return order;
    }

    public CloneOrder getCloneOrder(int orderid) {
        DatabaseTransaction t = null;
        Connection conn = null;
        CloneOrderManager manager = null;
        CloneOrder order = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            manager = new CloneOrderManager(conn);
            order = manager.queryCloneOrder(null, orderid);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(manager.getErrorMessage());
                System.out.println(ex);
            }
            DatabaseTransaction.closeConnection(conn);
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return order;
    }

    public String findEmail(int userid) {
        DatabaseTransaction t = null;
        Connection conn = null;
        UserManager manager = null;
        String email = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            manager = new UserManager(conn);
            email = manager.findUserEmailById(userid);
        } catch (Exception ex) {
            if (Constants.DEBUG) {
                System.out.println(manager.getErrorMessage());
                System.out.println(ex);
            }
            DatabaseTransaction.closeConnection(conn);
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
        return email;
    }

    public void writeCloneList(List clones, PrintWriter out, boolean isWorkingStorage) {
        writeCloneList(clones, out, isWorkingStorage, true);
    }

    public void writeCloneList(List clones, PrintWriter out, boolean isWorkingStorage, boolean isQuantity) {
        if (isWorkingStorage) {
            out.print("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tGrowth Condition\tSelection Markers\tMutation\tDiscrepancy\tSpecies\tSpecial Treatment");
            if (isQuantity) {
                out.print("\tQuantity");
            }
            out.println("\tContainer\tWell\tPosition");
        } else {
            if (isQuantity) {
                out.println("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tGrowth Condition\tSelection Markers\tMutation\tDiscrepancy\tSpecies\tSpecial Treatment\tQuantity");
            } else {
                out.println("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tGrowth Condition\tSelection Markers\tMutation\tDiscrepancy\tSpecies\tSpecial Treatment");
            }
        }

        for (int i = 0; i < clones.size(); i++) {
            CloneInfo c = (CloneInfo) clones.get(i);
            if (Clone.NOINSERT.equals(c.getType())) {
                out.print(c.getName() + "\t" + c.getType() + "\t\t\t\t\t\t\t" + c.getVectorname() + "\t" + c.getRecommendedGrowthCondition().getName() + "\t");

                List selections = c.getSelections();
                for (int n = 0; n < selections.size(); n++) {
                    CloneSelection cs = (CloneSelection) selections.get(n);
                    out.print(cs.getHosttype() + ": " + cs.getMarker() + ";");
                }

                out.print("\t\t\t\t"+c.getSpecialtreatment());

                if (isQuantity) {
                    out.print("\t" + c.getQuantity());
                }

                if (isWorkingStorage) {
                    out.println("\t" + c.getPlate() + "\t" + c.getWell() + "\t" + c.getPosition());
                } else {
                    out.println();
                }
            } else {
                List inserts = c.getInserts();
                for (int j = 0; j < inserts.size(); j++) {
                    DnaInsert insert = (DnaInsert) inserts.get(j);
                    out.print(c.getName() + "\t" + c.getType() + "\t" + insert.getGeneid() + "\t" + insert.getName() + "\t" + insert.getDescription() + "\t" + insert.getTargetgenbank() + "\t" + insert.getTargetseqid() + "\t" + insert.getFormat() + "\t" + c.getVectorname() + "\t" + c.getRecommendedGrowthCondition().getName() + "\t");

                    List selections = c.getSelections();
                    for (int n = 0; n < selections.size(); n++) {
                        CloneSelection cs = (CloneSelection) selections.get(n);
                        out.print(cs.getHosttype() + ": " + cs.getMarker() + ";");
                    }

                    out.print("\t" + insert.getHasmutation() + "\t" + insert.getHasdiscrepancy()+"\t"+insert.getSpecies()+"\t"+c.getSpecialtreatment());

                    if (isQuantity) {
                        out.print("\t" + c.getQuantity());
                    }

                    if (isWorkingStorage) {
                        out.println("\t" + c.getPlate() + "\t" + c.getWell() + "\t" + c.getPosition());
                    } else {
                        out.println();
                    }
                }
            }
        }
    }

    public List groupClonesByTargetPlate(List clones) {
        List groups = new ArrayList();
        Collections.sort(clones, new CloneInfoTargetPlateWellComparator());

        String lastPlate = null;
        String currentPlate = null;
        List l = null;
        for (int i = 0; i < clones.size(); i++) {
            CloneInfo c = (CloneInfo) clones.get(i);
            currentPlate = c.getTargetPlate();
            if (currentPlate.equals(lastPlate)) {
                l.add(c);
            } else {
                if (lastPlate != null) {
                    groups.add(l);
                }
                l = new ArrayList();
                l.add(c);
                lastPlate = currentPlate;
            }
        }
        groups.add(l);

        return groups;
    }

    public List groupClonesByGrowth(List clones) {
        List groups = new ArrayList();
        Collections.sort(clones, new CloneGrowthComparator());

        int lastid = 0;
        int currentid = 0;
        List l = null;
        for (int i = 0; i < clones.size(); i++) {
            Clone c = (CloneInfo) clones.get(i);
            currentid = c.getRecommendedGrowthCondition().getGrowthid();
            if (currentid == lastid) {
                l.add(c);
            } else {
                if (l != null) {
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
            if (manager.updateOrderStatus(orderid, status)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
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
            if (manager.updateAllOrderStatus(orders)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
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
            while (rs.next()) {
                String n = rs.getString(1);
                c.add(n);
            }
        } catch (Exception ex) {
            if (Constants.DEBUG) {
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
            if (manager.updateOrderWithShipping(order)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public void printBioTracyWorklist(List clones, String path, String filename, String isBatch, Sftp ftp) throws Exception {
        if (clones == null || filename == null) {
            return;
        //OutputStreamWriter f = new FileWriter(path+filename);
        }
        OutputStreamWriter f = null;
        if(ftp==null) {
            f = new FileWriter(new File(path+filename));
        } else {
            f = new OutputStreamWriter(ftp.getOutputStream(path + filename, 0, false));
        }
        
        f.write(filename + "\n\n\n");
        for (int i = 0; i < clones.size(); i++) {
            CloneInfo clone = (CloneInfo) clones.get(i);
            String tube = clone.getPlate();
            f.write(tube);
            if ("Y".equals(isBatch)) {
                String well = clone.getTargetWell();
                if (well == null) {
                    throw new Exception("Cannot get valid well for clone: " + clone.getName());
                }
                f.write("," + well);
            }
            f.write("\n");
        }
        f.close();
    }

    public void printBioTracySummary(List groups, String filename, String worklistFilenameRoot, Sftp ftp) throws Exception {
        if (groups == null || filename == null) {
            return;
        }
        OutputStreamWriter f = null;
        if(ftp == null) {
            f = new FileWriter(filename);
        } else {
            f = new OutputStreamWriter(ftp.getOutputStream(filename, 0, false));
        }
        
        f.write("File Name\tHost Type\tSelection Condition\tGrowth Condition\tComments\n");
        for (int i = 0; i < groups.size(); i++) {
            List clones = (List) groups.get(i);
            CloneInfo clone = (CloneInfo) clones.get(0);
            GrowthCondition g = clone.getRecommendedGrowthCondition();
            String fn = worklistFilenameRoot + "_" + (i + 1) + ".txt";
            f.write(fn + "\t" + g.getHosttype() + "\t" + g.getSelection() + "\t" + g.getCondition() + "\t" + g.getComments() + "\n");
        }
        f.close();
    }

    public List getAllOrderContainersForUser(User user) {
        DatabaseTransaction t = null;
        Connection conn = null;
        List containers = new ArrayList();

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            List orders = manager.queryCloneOrders(user, null);
            if (orders == null) {
                throw new Exception("Error occured while querying database for orders.");
            }
            for (int i = 0; i < orders.size(); i++) {
                CloneOrder order = (CloneOrder) orders.get(i);
                containers.addAll(order.getShippedContainersAsList());
            }
            return containers;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public String formOrderText(CloneOrder order) {
        StringBuffer sf = new StringBuffer();
        sf.append("===========================================================\n");
        sf.append("Order ID: " + order.getOrderid() + "\n");
        sf.append("Order Date: " + order.getOrderDate() + "\n");
        sf.append("Order Status: " + order.getStatus() + "\n");
        sf.append("PO Number: " + order.getPonumber() + "\n\n");

        sf.append("Following are your ordered items:\n");
        sf.append("Number of clones: " + order.getNumofclones() + "\t$" + order.getCostforclones() + "\n");
        sf.append("Number of collections: " + order.getNumofcollection() + "\t$" + order.getCostforcollection() + "\n");
        sf.append("Cost for shipping and handling:\t$" + order.getCostforshipping() + "\n");
        sf.append("Total cost:\t$" + order.getPrice() + "\n\n");

        sf.append("Shipping To:\n");
        sf.append(order.getShippingTo() + "\n");
        sf.append(order.getShippingAddress() + "\n");
        sf.append("\nBilling To:\n");
        sf.append(order.getBillingTo() + "\n");
        sf.append(order.getBillingAddress() + "\n\n");

        if (order.getShippingmethod() != null) {
            sf.append("Shipping Method: " + order.getShippingmethod() + "\n");
        }
        if (order.getShippingaccount() != null) {
            sf.append("Shipping Account: " + order.getShippingaccount() + "\n");
        }
        if (order.getShippingdate() != null) {
            sf.append("Shipping Date: " + order.getShippingdate() + "\n");
        }
        if (order.getTrackingnumber() != null) {
            sf.append("Tracking Number: " + order.getTrackingnumber() + "\n");
        }
        if (order.getShippedContainers() != null) {
            sf.append("Containers Shipped: " + order.getShippedContainers() + "\n");
        }
        sf.append("\n===========================================================\n");
        return sf.toString();
    }

    public void sendOrderFailEmail(CloneOrder order, String email) {
        String subject = "order " + order.getOrderid();
        String text = "Your order " + order.getOrderid() + " was not processed successfully. Please contact us at plasmidhelp@hms.harvard.edu to solve the problem.\n";

        try {
            Mailer.sendMessage(email, Constants.EMAIL_FROM, Constants.EMAIL_FROM, subject, text);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void sendOrderInvalidePaymentEmail(CloneOrder order, String email) {
        String subject = "order " + order.getOrderid();
        String text = "Your order " + order.getOrderid() + " was not processed successfully. Your payment was not valid. Please contact us at plasmidhelp@hms.harvard.edu to solve the problem.\n";

        try {
            Mailer.sendMessage(email, Constants.EMAIL_FROM, Constants.EMAIL_FROM, subject, text);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void sendOrderCancelEmail(CloneOrder order, String email) {
        String subject = "order " + order.getOrderid();
        String text = "Your order " + order.getOrderid() + " has been cancelled. Please contact us at plasmidhelp@hms.harvard.edu for any questions. Thanks for using PlasmID.\n";

        try {
            Mailer.sendMessage(email, Constants.EMAIL_FROM, Constants.EMAIL_FROM, subject, text);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void sendOrderEmail(CloneOrder order, String email) {
        String subject = "order " + order.getOrderid();
        String text = "Thank you for placing a clone request at PlasmID. "+
                "Clones are sent as glycerol stocks (most U.S. orders) "+
                "or as purified DNA (most overseas orders).  For orders "+
                "of <96 clones, if you are in our Expedited MTA network "+
                "we will ship within 10 business days of your order. "+
                "For out-of-network orders, we will ship your clones within "+
                "10 days after receipt of the signed MTA.  For orders of >96 "+
                "plasmids, please contact us for an estimated shipment time.\n";
        text += "\n" + formOrderText(order);
        text += "\n" + "Please sign in at PlasmID to view order status, " +
                "track your shipment, download clone information, cancel a request, " +
                "or view detailed information about the clones, " +
                "including growth conditions for the clones.\n\n" +
                "Thank you,\n" +
                "The DF/HCC DNA Resource Core PlasmID Respository\n" +
                "The Protein Structure Initiative Material Repository (PSI-MR)\n" +
                "http://plasmid.med.harvard.edu/PLASMID/\n" +
                "http://www.hip.harvard.edu/PSIMR\n\n" +
                "If you have further questions, please contact us at plasmidhelp@hms.harvard.edu\n";

        try {
            Mailer.sendMessage(email, Constants.EMAIL_FROM, subject, text);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public List getCloneOrders(String orderids, String orderDateFrom, String orderDateTo,
            String shippingDateFrom, String shippingDateTo, String status, String lastnames,
            String organization, String sort, String provider) {
        return getCloneOrders(orderids, orderDateFrom, orderDateTo, shippingDateFrom, shippingDateTo, status, lastnames, organization, sort, provider, false);
    }

    public List getCloneOrders(String orderids, String orderDateFrom, String orderDateTo,
            String shippingDateFrom, String shippingDateTo, String status, String lastnames,
            String organization, String sort, String provider, boolean isPI) {
        DatabaseTransaction t = null;
        Connection conn = null;
        StringConvertor sc = new StringConvertor();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            List orderidList = null;
            if (orderids != null && orderids.trim().length() > 0) {
                orderidList = sc.convertFromStringToList(orderids.trim(), ",");
            }
            if (orderDateFrom != null && orderDateFrom.trim().length() == 0) {
                orderDateFrom = null;
            }
            if (orderDateTo != null && orderDateTo.trim().length() == 0) {
                orderDateTo = null;
            }
            if (shippingDateFrom != null && shippingDateFrom.trim().length() == 0) {
                shippingDateFrom = null;
            }
            if (shippingDateTo != null && shippingDateTo.trim().length() == 0) {
                shippingDateTo = null;
            }
            if (CloneOrder.ALL.equals(status)) {
                status = null;
            }
            List lastnameList = null;
            if (lastnames != null && lastnames.trim().length() > 0) {
                lastnameList = sc.convertFromStringToCapList(lastnames.trim(), ",");
            }
            List groups = null;
            boolean isMtamember = false;
            boolean isMember = true;
            if (Constants.MTAMEMBER.equals(organization)) {
                isMtamember = true;
            } else {
                if (!Constants.ALL.equals(organization)) {
                    groups = new ArrayList();
                    for (int i = 0; i < User.MEMBER.length; i++) {
                        groups.add(User.MEMBER[i]);
                    }
                    if (Constants.NONMEMBER.equals(organization)) {
                        isMember = false;
                    }
                }
            }

            String sortby = null;
            if (Constants.SORTBY_ORDERDATE.equals(sort)) {
                sortby = "orderdate";
            }
            if (Constants.SORTBY_ORDERID.equals(sort)) {
                sortby = "orderid";
            }
            if (Constants.SORTBY_SHIPDATE.equals(sort)) {
                sortby = "shippingdate";
            }
            if (Constants.SORTBY_STATUS.equals(sort)) {
                sortby = "orderstatus";
            }
            if (Constants.SORTBY_USERNAME.equals(sort)) {
                sortby = "lastname";
            }
            List cloneorders = manager.queryCloneOrders(orderidList, orderDateFrom, orderDateTo, shippingDateFrom, shippingDateTo, status, lastnameList, groups, isMember, sortby, provider, isPI, isMtamember);
            return cloneorders;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
                System.out.println(ex);
            }
            return null;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public void printInvoice(PrintWriter out, List orders) {
        out.println("Name\t# Clones\t# Collections\tTotal Cost\tPO Number\tDate Shipped\tOrder #\tBilling Information\tPI Name\tPI Email");
        for (int i = 0; i < orders.size(); i++) {
            CloneOrder order = (CloneOrder) orders.get(i);
            out.println(order.getName() + "\t" + order.getNumofclones() + "\t" + order.getNumofcollection() + "\t$" + order.getPrice() + "\t" + order.getPonumber() + "\t" + order.getShippingdate() + "\t" + order.getOrderid() + "\tBill To: " + order.getBillingTo() + ", " + order.getBillingAddress().replaceAll("\n", " ") + "\t" + order.getPiname() + "\t" + order.getPiemail());
        }
    }

    public void printReport(PrintWriter out, List orders) {
        out.println("Order ID\tOrder Date\tClone ID\tSpecies Specific ID\tGene Symbol\tGene Name\tStatus\t# Clones\t# Collections\tShipping Information\tUser\tUser Email\tPI Name\tPI Institution\tPI Department\tPI Email");
        for (int i = 0; i < orders.size(); i++) {
            CloneOrder order = (CloneOrder) orders.get(i);
            List items = order.getItems();
            boolean isItem = false;
            for (int j = 0; j < items.size(); j++) {
                CloneInfo clone = (CloneInfo) items.get(j);
                if (j == 0) {
                    out.println(order.getOrderid() + "\t" + order.getOrderDate() + "\t" + clone.getName() + "\t" + clone.getGeneID() + "\t" + clone.getGeneSymbol() + "\t" + clone.getCloneDescription() + "\t" + order.getStatus() + "\t" + order.getNumofclones() + "\t" + order.getNumofcollection() + "\t" + order.getShippingTo() + ", " + order.getShippingAddress().replaceAll("\n", " ") + "\t" + order.getName() + "\t" + order.getEmail() + "\t" + order.getPiname() + "\t" + order.getPiinstitution() + "\t" + order.getPidepartment() + "\t" + order.getPiemail());
                    isItem = true;
                } else {
                    out.println("\t\t" + clone.getName() + "\t" + clone.getGeneID() + "\t" + clone.getGeneSymbol() + "\t" + clone.getCloneDescription() + "\t\t\t\t\t\t\t\t\t\t");
                }
            }
            if (!isItem) {
                out.println(order.getOrderid() + "\t" + order.getOrderDate() + "\t\t\t\t\t" + order.getStatus() + "\t" + order.getNumofclones() + "\t" + order.getNumofcollection() + "\t" + order.getShippingTo() + ", " + order.getShippingAddress().replaceAll("\n", " ") + "\t" + order.getName() + "\t" + order.getEmail() + "\t" + order.getPiname() + "\t" + order.getPiinstitution() + "\t" + order.getPidepartment() + "\t" + order.getPiemail());
            }
        }
    }

    public List parseBatchOrderFile(InputStream input) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String line = null;
        List clones = new ArrayList();

        while ((line = in.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, BATCH_ORDER_FILE_DILIM);

            String cloneid = st.nextToken();
            if (cloneid == null || cloneid.trim().length() <= 0) {
                continue;
            }
            String plate = st.nextToken();
            if (plate == null || plate.trim().length() <= 0) {
                throw new Exception("Invalid plate for clone: " + cloneid);
            }
            String well = st.nextToken();
            if (well == null || well.trim().length() <= 0) {
                throw new Exception("Invalid well for clone: " + cloneid);
            }
            try {
                PlatePositionConvertor.convertWellFromA8_12toInt(well.trim());
            } catch (Exception ex) {
                throw new Exception("Invalid well for clone: " + cloneid);
            }

            BatchOrder clone = new BatchOrder(0, 0, plate.trim(), well.trim(), cloneid.trim());
            clones.add(clone);
        }
        in.close();

        return clones;
    }

    public List checkDuplicateClonesAndWells(List clones) {
        List l = new ArrayList();
        List duplicateClones = new ArrayList();

        for (int i = 0; i < clones.size(); i++) {
            BatchOrder clone = (BatchOrder) clones.get(i);
            if (clone.cloneExist(l) || clone.plateWellExist(l)) {
                duplicateClones.add(clone);
            }
            l.add(clone);
        }
        return duplicateClones;
    }

    public List getCloneidFromBatchOrder(List clones) {
        List ids = new ArrayList();

        for (int i = 0; i < clones.size(); i++) {
            BatchOrder c = (BatchOrder) clones.get(i);
            String id = c.getOriginalCloneid();
            ids.add(id);
        }

        return ids;
    }

    public Map queryBatchOrderClones(String cloneType, List clones, List restrictions, List noFoundClones) throws Exception {
        String type = null;

        if (Constants.BATCH_ORDER_PLASMIDID.equals(cloneType)) {
            type = Constants.CLONE_SEARCH_PLASMIDCLONEID;
        } else {
            type = Constants.CLONE_SEARCH_OTHERCLONEID;
        }

        List cloneids = getCloneidFromBatchOrder(clones);
        GeneQueryHandler handler = StaticQueryHandlerFactory.makeGeneQueryHandler(type, cloneids);

        if (handler == null) {
            throw new Exception("Wrong clone ID type.");
        }

        handler.doQuery(restrictions, null, null, -1, -1, null, Clone.AVAILABLE, true);
        Map founds = handler.getFound();
        noFoundClones.addAll(handler.getNofound());

        return founds;
    }

    public List processBatchOrderClones(List clones, Map foundClones, List shoppingcart) throws Exception {
        List l = new ArrayList();

        for (int i = 0; i < clones.size(); i++) {
            BatchOrder b = (BatchOrder) clones.get(i);
            List found = (ArrayList) foundClones.get(b.getOriginalCloneid());

            if (found == null) {
                throw new Exception("Cannot find clone with cloneid: " + b.getOriginalCloneid());
            }

            Clone c = (Clone) found.get(0);
            b.setCloneid(c.getCloneid());
            CloneInfo cinfo = new CloneInfo(b.getOriginalCloneid(), c);
            cinfo.setTargetPlate(b.getPlate());
            cinfo.setTargetWell(b.getWell());
            l.add(cinfo);

            ShoppingCartItem item = new ShoppingCartItem(0, (new Integer(c.getCloneid())).toString(), 1, ShoppingCartItem.CLONE);
            ShoppingCartItem.addToCart(shoppingcart, item);
        }

        return l;
    }

    public void removeCloneFromBatchOrder(List clones, String cloneid) {
        if (clones == null) {
            return;
        }
        for (int i = 0; i < clones.size(); i++) {
            BatchOrder b = (BatchOrder) clones.get(i);
            if (b.getCloneid() == Integer.parseInt(cloneid)) {
                clones.remove(i);
            }
        }
    }

    public static boolean isRestricted(Clone clone, User user) {
        List restrictions = new ArrayList();
        restrictions.add(Clone.NO_RESTRICTION);
        if (user != null) {
            List ress = UserManager.getUserRestrictions(user);
            restrictions.addAll(ress);
        }
        String restriction = ((CloneInfo) clone).getRestriction();

        for (int i = 0; i < restrictions.size(); i++) {
            String res = (String) restrictions.get(i);
            if (restriction.trim().equals(res.trim())) {
                return true;
            }
        }
        return false;
    }

    public List getMTAs(User user, List items) throws Exception {
        if (items == null || items.size() == 0) {
            return new ArrayList();
        }
        List cloneids = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.CLONE)) {
                cloneids.add(s.getItemid());
            }
        }

        List collectionNames = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.COLLECTION)) {
                collectionNames.add(s.getItemid());
            }
        }

        CloneManager manager = new CloneManager();
        List mtas = manager.getMTAs(cloneids, collectionNames, user);
        if (mtas == null) {
            throw new Exception(manager.getErrorMessage());
        }

        return mtas;
    }

    public List getMTAs(List items) throws Exception {
        if (items == null || items.size() == 0) {
            return new ArrayList();
        }
        List cloneids = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.CLONE)) {
                cloneids.add(s.getItemid());
            }
        }

        List collectionNames = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            ShoppingCartItem s = (ShoppingCartItem) items.get(i);
            if (s.getType().equals(ShoppingCartItem.COLLECTION)) {
                collectionNames.add(s.getItemid());
            }
        }

        CloneManager manager = new CloneManager();
        List mtas = manager.getMTAs(cloneids, collectionNames);
        if (mtas == null) {
            throw new Exception(manager.getErrorMessage());
        }

        return mtas;
    }

    public boolean validatePonumber(String ponumber) {
        if (ponumber == null || ponumber.trim().length() < 1 || ponumber.trim().toUpperCase().equals(Constants.PAYPAL.toUpperCase())) {
            return false;
        }

        StringTokenizer st = new StringTokenizer(ponumber);
        String s1 = null;
        if (st.hasMoreTokens()) {
            s1 = st.nextToken();
            if (s1.toUpperCase().equals("CREDIT")) {
                if (st.hasMoreTokens()) {
                    s1 = st.nextToken();
                    if (s1.toUpperCase().equals("CARD")) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}