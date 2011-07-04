/*
 * OrderProcessManager.java
 *
 * Created on May 20, 2005, 9:23 AM
 */
package plasmid.process;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
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
import java.text.SimpleDateFormat;

/**
 *
 * @author  DZuo
 */
public class OrderProcessManager {

    public static final String PLATINUM_VALIDATION_METHOD_END_SEQ = "End read sequence validation";
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

    public int getNumOfClonesInCollections() {
        if (collections == null) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < collections.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem) collections.get(i);
            //n = n + item.getQuantity()*item.getCloneCount();
            n = n + item.getCloneCount();
        }

        return n;
    }

    public double getTotalClonePrice(User user) {
        ClonePriceCalculator calculator = new ClonePriceCalculator();
        return calculator.calculateClonePrice(getTotalCloneQuantity(), user);
    }

    public double getTotalCollectionPrice(List collectionList, User user) {
        ClonePriceCalculator calculator = new ClonePriceCalculator();

        double price = 0.0;
        for (int i = 0; i < collectionList.size(); i++) {
            CollectionInfo info = (CollectionInfo) collectionList.get(i);
            price = price + calculator.calculatePriceForCollection(info, user);
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
            Map foundCollections = man.getCollections(conn, collectionNames, true);
            List newShoppingcartCollections = new ArrayList();
            for (int i = 0; i < collections.size(); i++) {
                ShoppingCartItem item = (ShoppingCartItem) collections.get(i);
                String itemid = item.getItemid();
                int quantity = item.getQuantity();
                CollectionInfo info = (CollectionInfo) foundCollections.get(itemid);
                info.setQuantity(quantity);
                newShoppingcartCollections.add(info);
                item.setCloneCount(info.getCloneCount());
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

    public int getTotalNumOfClonesInCollections() {
        int count = 0;
        for (int i = 0; i < collections.size(); i++) {
            ShoppingCartItem item = (ShoppingCartItem) collections.get(i);
            int cloneCount = item.getCloneCount();
            int quantity = item.getQuantity();
            count = count + cloneCount * quantity;
        }

        return count;
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
            Map found = manager.queryClonesByCloneid(items, true, true, isWorkingStorage, true, null, null, null, null, false, false);

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
            out.print("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tGrowth Condition\tSelection Markers\tMutation\tDiscrepancy\tSpecies\tSpecial MTA");
            if (isQuantity) {
                out.print("\tQuantity");
            }
            out.println("\tContainer\tWell\tPosition");
        } else {
            if (isQuantity) {
                out.println("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tGrowth Condition\tSelection Markers\tMutation\tDiscrepancy\tSpecies\tSpecial MTA\tQuantity");
            } else {
                out.println("Clone ID\tClone Type\tGene ID\tGene Symbol\tGene Name\tReference Sequence Genbank Accession\tReference Sequence GI\tInsert Format\tVector\tGrowth Condition\tSelection Markers\tMutation\tDiscrepancy\tSpecies\tSpecial MTA");
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

                out.print("\t\t\t\t" + c.getSpecialtreatment());

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

                    out.print("\t" + insert.getHasmutation() + "\t" + insert.getHasdiscrepancy() + "\t" + insert.getSpecies() + "\t" + c.getSpecialtreatment());

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

    public boolean updateShipping(CloneOrder order, Invoice invoice) {
        DatabaseTransaction t = null;
        Connection conn = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            if (manager.updateOrderWithShipping(order)) {
                int invoiceid = manager.addInvoice(invoice);
                if (invoiceid > 0) {
                    invoice.setInvoiceid(invoiceid);
                    DatabaseTransaction.commit(conn);
                    return true;
                } else {
                    DatabaseTransaction.rollback(conn);
                    return false;
                }
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
        if (ftp == null) {
            f = new FileWriter(new File(path + filename));
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
        if (ftp == null) {
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

    public void sendOrderInvalidePaymentEmail(int orderid, String email) {
        String subject = "order " + orderid;
        String text = "Your order " + orderid + " was not processed successfully because we didn't received a valid payment. We have cancelled this order. Please contact us at plasmidhelp@hms.harvard.edu if you have any questions.\n";

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
        String subject = "Your PlasmID order confirmation (order ID: " + order.getOrderid() + ")";
        String text =
                "Thank you for placing a clone request at PlasmID. " +
                "Please note your order ID number and save this email for your future reference. " +
                "Your current order status is listed below along with the details of your order. " +
                "You may check the progress of your order at any time by logging into your " +
                "account and then selecting 'View Orders.' " +
                "Orders of < 48 clones typically ship within 7-10 days. " +
                "We ask that you please allow additional time for large orders " +
                "or when requesting QC testing. Orders will ship as glycerol stocks " +
                "when allowed by local law. Some international orders may ship as purified DNA " +
                "or stabilized in special tubes to comply with local import law or to " +
                "accommodate slower shipping routes. Please contact plasmidhelp@hms.harvard.edu " +
                "with any questions or concerns.\n\n" +
                "Order Status Key\n\n" +
                "\tPending:\tNo additional information required. Your order is in line to be filled ASAP.\n" +
                "\tIn Process:\tYour order is currently being filled.\n" +
                "\tPending MTA:\tPlease download the required MTA from this web page http://plasmid.med.harvard.edu/PLASMID/TermAndCondition.jsp, then sign and return\n" +
                "\tPending AQIS:\tPlease provide import documents\n" +
                "\tPending for Payment:\tOur system is waiting for confirmation that your credit card was processed. Please log in to your account for up to date order status.\n";
        text += "\n" + formOrderText(order);
        text += "\n" + "Please sign in at PlasmID to view order status, " +
                "track your shipment, download clone information, cancel a request, " +
                "or view detailed information about the clones, " +
                "including growth conditions for the clones.\n\n" +
                "Thank you,\n" +
                "The DF/HCC DNA Resource Core PlasmID Respository\n" +
                "http://plasmid.med.harvard.edu/PLASMID/\n\n" +
                "If you have further questions, please contact us at plasmidhelp@hms.harvard.edu\n";

        try {
            Mailer.sendMessage(email, Constants.EMAIL_FROM, subject, text);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void sendTroubleshootingEmail(int orderid, String email) {
        String subject = "Troubleshooting Order # " + orderid;
        String text =
                "Dear PlasmID User,\n\n" +
                "I write in regard to your recent order from the PlasmID Repository. " +
                "Unfortunately one or more of the clones in your order has encountered " +
                "a problem that requires troubleshooting. " +
                "This may have occurred because a clone did not pass our Platinum QC testing, " +
                "or because a bacterial culture did not grow as expected. " +
                "To view the results of our QC testing please log into your account, " +
                "select the appropriate order number, and then select \"View Platinum Results.\"\n\n" +
                "In light of these results I write to determine the best way to complete your order. " +
                "In most cases simple isolation on agar can remedy the problem and will add " +
                "only a modest delay to your order. If this approach does not yield success " +
                "we typically have each gene in several vectors or formats and are happy to " +
                "provide an alternate plasmid to complete your order. " +
                "If substitution with an alternate clone or a modest troubleshooting delay " +
                "is not possible we are also happy to adjust the price of your order and " +
                "ship the remaining constructs. Please contact me at your earliest convenience " +
                "so that we may work together to complete your order.\n\n" +
                "Sincerely,\n\n" +
                "The PlasmID Repository Staff\n" +
                "plasmidhelp@hms.harvard.edu\n";
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
            //List groups = null;
            boolean isMtamember = false;
            boolean isMember = false;
            if (Constants.MTAMEMBER.equals(organization)) {
                isMtamember = true;
            } else {
                if (Constants.MEMBER.equals(organization)) {
                    isMember = true;
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
            if (Constants.SORTBY_INSTITUTION.equals(sort)) {
                sortby = "institution";
            }
            List cloneorders = manager.queryCloneOrders(orderidList, orderDateFrom, orderDateTo, shippingDateFrom, shippingDateTo, status, lastnameList, isMember, sortby, provider, isPI, isMtamember);
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

    public List getInvoices(String invoicenums, String invoiceDateFrom, String invoiceDateTo,
            String invoiceMonth, String invoiceYear, String pinames, String ponumbers,
            String paymentstatus, String isinternal, String institution1, String institution2) {
        DatabaseTransaction t = null;
        Connection conn = null;
        StringConvertor sc = new StringConvertor();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            List invoicenumList = null;
            if (invoicenums != null && invoicenums.trim().length() > 0) {
                invoicenumList = sc.convertFromStringToList(invoicenums.trim(), ",");
            }
            if (invoiceDateFrom != null && invoiceDateFrom.trim().length() == 0) {
                invoiceDateFrom = null;
            }
            if (invoiceDateTo != null && invoiceDateTo.trim().length() == 0) {
                invoiceDateTo = null;
            }
            if (invoiceMonth != null && invoiceMonth.trim().length() == 0) {
                invoiceMonth = null;
            }
            if (invoiceYear != null && invoiceYear.trim().length() == 0) {
                invoiceYear = null;
            }
            List lastnameList = null;
            if (pinames != null && pinames.trim().length() > 0) {
                lastnameList = sc.convertFromStringToCapList(pinames.trim(), ",");
            }
            List ponumberList = null;
            if (ponumbers != null && ponumbers.trim().length() > 0) {
                ponumberList = sc.convertFromStringToList(ponumbers.trim(), ",");
            }
            if (Constants.ALL.equals(paymentstatus)) {
                paymentstatus = null;
            }
            if (Constants.ALL.equals(isinternal)) {
                isinternal = null;
            }
            if (institution1 != null && institution1.trim().length() == 0) {
                institution1 = null;
            }
            if (institution2 != null && institution2.trim().length() == 0) {
                institution2 = null;
            }
            List invoices = manager.queryInvoices(invoicenumList, invoiceDateFrom, invoiceDateTo,
                    invoiceMonth, invoiceYear, lastnameList, ponumberList, paymentstatus,
                    isinternal, institution1, institution2);
            return invoices;
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

    public void printBillingReport(PrintWriter out, List orders) {
        out.println("Date_Ordered\tTracking_Number\tPI_Name\tSample_Number\tTotal_Cost\tAccount_Code\tDate_Completed\tOrder_ID\tUser_Name\tInstitution");
        for (int i = 0; i < orders.size(); i++) {
            CloneOrder order = (CloneOrder) orders.get(i);
            int numofsamples = order.getNumofclones() + order.getNumofcollection();
            out.println(order.getOrderDate() + "\t" + order.getTrackingnumber() + "\t" + order.getPiname() + "\t" + numofsamples + "\t$" + order.getPrice() + "\t" + order.getPonumber() + "\t" + order.getShippingdate() + "\t" + order.getOrderid() + "\t" + order.getName() + "\tBill To: " + order.getBillingTo() + ", " + order.getBillingAddress().replaceAll("\n", " "));
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

        Set foundcloneids = handler.doQuery(restrictions, null, null, -1, -1);
        handler.restoreClones(null, true, foundcloneids);
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

    public boolean addCloneValidationResults(List validations, CloneOrder order) {
        DatabaseTransaction t = null;
        Connection conn = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            CloneOrderManager manager = new CloneOrderManager(conn);
            if (manager.addOrderCloneValidation(validations) && manager.updatePlatinumServiceStatus(order)) {
                DatabaseTransaction.commit(conn);
                return true;
            } else {
                DatabaseTransaction.rollback(conn);
                return false;
            }
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            if (Constants.DEBUG) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }

    public Invoice getInvoice(int invoiceid) throws Exception {
        CloneOrderManager manager = new CloneOrderManager();
        Invoice invoice = manager.queryInvoice(invoiceid);
        return invoice;
    }

    public Invoice generateInvoice(CloneOrder order, String reason, double adjustment) {
        int orderid = order.getOrderid();
        String invoicenum = Invoice.INVOICE_PREFIX + orderid;
        double payment = 0.0;
        String paymentstatus = Invoice.PAYMENTSTATUS_UNPAID;
        String paymenttype = "";
        if (Constants.PAYPAL.equals(order.getPonumber())) {
            payment = order.getPrice();
            paymentstatus = Invoice.PAYMENTSTATUS_PAID;
            paymenttype = Constants.PAYPAL;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = dateFormat.format(calendar.getTime());
        Invoice invoice = new Invoice(invoicenum, date, order.getPrice(), adjustment, payment, paymentstatus, paymenttype, orderid, reason, order.getPonumber());
        return invoice;
    }

    public void printExternalInvoice(OutputStream file, CloneOrder order, Invoice invoice) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();

            document.add(PdfEditor.makeTitle("DF/HCC DNA Resource Core Invoice"));
            document.add(PdfEditor.makeTitle(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(PdfEditor.makeSmallBold("Invoice Number:\t" + invoice.getInvoicenum()));
            table.addCell(PdfEditor.makeSmallBold("Invoice Date:\t" + invoice.getInvoicedate()));
            document.add(table);

            document.add(PdfEditor.makeTitle(" "));
            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(PdfEditor.makeSmall("Harvard Medical School"));
            table.addCell(PdfEditor.makeSmallBold("Order ID:\t" + order.getOrderid()));
            table.addCell(PdfEditor.makeSmall("Dept. BCMP-240 Longwood Ave."));
            table.addCell(PdfEditor.makeSmall("Order Date:\t" + order.getOrderDate()));
            table.addCell(PdfEditor.makeSmall("Accounts Receivable"));
            table.addCell(PdfEditor.makeSmall("Order By:\t" + order.getName()));
            table.addCell(PdfEditor.makeSmall("Boston, MA 02115"));
            table.addCell(PdfEditor.makeSmall("PI:\t" + order.getPiname()));
            table.addCell(PdfEditor.makeSmall("(617)432-1210"));
            table.addCell(PdfEditor.makeSmall("PI email:\t" + order.getPiemail()));
            table.addCell(PdfEditor.makeSmall("Attn: Elmira Dhroso"));
            table.addCell(PdfEditor.makeSmall("Grant or PO Number:\t" + order.getPonumber()));
            document.add(table);

            document.add(PdfEditor.makeTitle(" "));
            document.add(PdfEditor.makeSmallBold("Bill To:"));
            document.add(PdfEditor.makeSmall(order.getBillingTo()));
            document.add(PdfEditor.makeSmall(order.getBillingAddress()));
            document.add(PdfEditor.makeSmall(order.getBillingemail()));

            document.add(PdfEditor.makeTitle(" "));
            table = new PdfPTable(3);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(PdfEditor.makeSmallBold("Item"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmallBold("Quantity"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmallBold("Price"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Plasmids"));
            cell = new PdfPCell(PdfEditor.makeSmall("" + order.getNumofclones()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforclones()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Collections"));
            cell = new PdfPCell(PdfEditor.makeSmall("" + order.getNumofcollection()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforcollection()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Platinum service"));
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforplatinum()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Shipping and handling"));
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforshipping()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Total price"));
            cell = new PdfPCell(PdfEditor.makeSmall(order.getTotalPriceString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Adjustment*"));
            cell = new PdfPCell(PdfEditor.makeSmall(invoice.getAdjustmentString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Payment"));
            cell = new PdfPCell(PdfEditor.makeSmall(invoice.getPaymentString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmallBold("Payment Due"));
            cell = new PdfPCell(PdfEditor.makeSmallBold(invoice.getDueString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            document.add(table);
            document.add(PdfEditor.makeSmallItalic("*" + invoice.getReasonforadj()));

            document.add(PdfEditor.makeTitle(" "));
            document.add(PdfEditor.makeSmallBold("Make checkes Payable to: Harvard Medical School. " +
                    "Include invoice number on check. Payments must be made in U.S. funds drawn on a U.S. bank. " +
                    "If you pay through wire transfer, please include wire transfer fee in the total amount."));

            document.add(PdfEditor.makeTitle(" "));
            document.add(PdfEditor.makeSmallBold("Mailing Address:"));
            document.add(PdfEditor.makeSmall("  Harvard Medical School"));
            document.add(PdfEditor.makeSmall("  BCMP-Harvard Cancer Center"));
            document.add(PdfEditor.makeSmall("  C1-214"));
            document.add(PdfEditor.makeSmall("  240 Longwood Ave."));
            document.add(PdfEditor.makeSmall("  Boston, MA 02115"));

            document.add(PdfEditor.makeTitle(" "));
            document.add(PdfEditor.makeSmallItalic("Please see billing memo for more payment information."));

            document.add(PdfEditor.makeTitle(" "));
            document.add(PdfEditor.makeSmallBold("For Invoice Information Contact:"));
            document.add(PdfEditor.makeSmall("  Elmira Dhroso, (617)432-1210"));
            document.add(PdfEditor.makeSmall("  elmira_dhroso@hms.harvard.edu"));

            document.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printInternalInvoice(OutputStream file, CloneOrder order, Invoice invoice) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();

            document.add(PdfEditor.makeTitle("DF/HCC DNA Resource Core Invoice"));
            document.add(PdfEditor.makeTitle(" "));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(PdfEditor.makeSmallBold("Invoice Number:\t" + invoice.getInvoicenum()));
            table.addCell(PdfEditor.makeSmallBold("Invoice Date:\t" + invoice.getInvoicedate()));
            document.add(table);

            document.add(PdfEditor.makeTitle(" "));
            table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.addCell(PdfEditor.makeSmall("Harvard Medical School"));
            table.addCell(PdfEditor.makeSmallBold("Order ID:\t" + order.getOrderid()));
            table.addCell(PdfEditor.makeSmall("Dept. BCMP-240 Longwood Ave."));
            table.addCell(PdfEditor.makeSmall("Order Date:\t" + order.getOrderDate()));
            table.addCell(PdfEditor.makeSmall("Accounts Receivable"));
            table.addCell(PdfEditor.makeSmall("Order By:\t" + order.getName()));
            table.addCell(PdfEditor.makeSmall("Boston, MA 02115"));
            table.addCell(PdfEditor.makeSmall("PI:\t" + order.getPiname()));
            table.addCell(PdfEditor.makeSmall("(617)432-1210"));
            table.addCell(PdfEditor.makeSmall("PI email:\t" + order.getPiemail()));
            table.addCell(PdfEditor.makeSmall("Attn: Elmira Dhroso"));
            table.addCell(PdfEditor.makeSmall("Grant or PO Number:\t" + order.getPonumber()));
            document.add(table);

            document.add(PdfEditor.makeTitle(" "));
            document.add(PdfEditor.makeSmallBold("Bill To:"));
            document.add(PdfEditor.makeSmall(order.getBillingTo()));
            document.add(PdfEditor.makeSmall(order.getBillingAddress()));
            document.add(PdfEditor.makeSmall(order.getBillingemail()));

            document.add(PdfEditor.makeTitle(" "));
            table = new PdfPTable(3);
            table.setWidthPercentage(100);
            PdfPCell cell = new PdfPCell(PdfEditor.makeSmallBold("Item"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmallBold("Quantity"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmallBold("Price"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Plasmids"));
            cell = new PdfPCell(PdfEditor.makeSmall("" + order.getNumofclones()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforclones()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Collections"));
            cell = new PdfPCell(PdfEditor.makeSmall("" + order.getNumofcollection()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforcollection()));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Platinum service"));
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforplatinum()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Shipping and handling"));
            cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCostforshipping()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Total price"));
            cell = new PdfPCell(PdfEditor.makeSmall(order.getTotalPriceString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Adjustment*"));
            cell = new PdfPCell(PdfEditor.makeSmall(invoice.getAdjustmentString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmall("Payment"));
            cell = new PdfPCell(PdfEditor.makeSmall(invoice.getPaymentString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            table.addCell(PdfEditor.makeSmallBold("Payment Due"));
            cell = new PdfPCell(PdfEditor.makeSmallBold(invoice.getDueString()));
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            document.add(table);
            document.add(PdfEditor.makeSmallItalic("*" + invoice.getReasonforadj()));

            document.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayInvoice(OutputStream file, CloneOrder order, Invoice invoice) {
        if (User.isInternalMember(order.getUsergroup())) {
            printInternalInvoice(file, order, invoice);
        } else {
            printExternalInvoice(file, order, invoice);
        }
    }

    public void sendShippingEmails(CloneOrder order, Invoice invoice) throws Exception {
        int orderid = order.getOrderid();
        String to = order.getEmail();
        String subject = "order " + orderid;
        String text = "Your order " + orderid + " has been shipped. Please log in your account for shipping details. If you have requested the Platinum Clone Service, QC data for your order is now available by logging into your account at http://plasmid.med.harvard.edu/PLASMID/Login.jsp.";
        Mailer.sendMessage(to, Constants.EMAIL_FROM, subject, text);

        if (!User.isInternalMember(order.getUsergroup())) {
            String filename = Constants.TMP + "Invoice_" + orderid + ".pdf";
            File f1 = new File(filename);
            OutputStream file = new FileOutputStream(f1);
            printExternalInvoice(file, order, invoice);
            File f2 = new File(Constants.FILE_PATH + "Billing_memo.pdf");
            List files = new ArrayList();
            files.add(f1);
            files.add(f2);

            String billingemail = order.getBillingemail();
            subject = "Invoice for order " + orderid;
            text = "Dear Accounts Payable Representative:\n\n" +
                    "Payment is requested for the attached invoice from the PlasmID Repository" +
                    " at Harvard Medical School.\n\n" +
                    "This invoice was generated upon completion and shipment of your order." +
                    " Any discounts or price modifications should already be reflected on this invoice." +
                    " If you have received this notification in error, or believe that a clerical" +
                    " mistake has occurred please contact us as soon as possible to resolve the issue." +
                    " Please find payment instructions attached to this email and ALWAYS BE SURE TO" +
                    " INCLUDE YOUR INVOICE NUMBER WITH PAYMENT and ADD ANY WIRE TRANSFER FEES TO YOUR TOTAL.\n\n" +
                    "As a part of our continued environmental efforts this email will serve as your" +
                    " sole notification, and no paper copy will be mailed to your facility.\n\n" +
                    "Payment Terms: Net 30\n" +
                    "Enclosures: Invoice, Payment Instructions\n";

            Mailer.sendMessage(billingemail, Constants.EMAIL_FROM, to, subject, text, files);
        }
    }

    public static final void main(String args[]) {
        String filename = "C:\\dev\\test\\plasmid\\invoice.pdf";

        try {
            DatabaseTransaction t = DatabaseTransaction.getInstance();
            Connection c = t.requestConnection();
            UserManager m = new UserManager(c);
            User user = m.findUser("dzuo@hms.harvard.edu");
            DatabaseTransaction.closeConnection(c);

            OrderProcessManager manager = new OrderProcessManager();
            CloneOrder order = manager.getCloneOrder(user, 7429);
            Invoice invoice = manager.generateInvoice(order, "Reason for refund goes here.", -10.0);
            invoice.setInvoiceid(101);

            OutputStream file = new FileOutputStream(new File(filename));
            manager.printInternalInvoice(file, order, invoice);
        //manager.printExternalInvoice(file, order, invoice);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        System.exit(0);
    }
}