
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import plasmid.coreobject.CloneOrder;
import plasmid.coreobject.UserAddress;
import plasmid.database.DatabaseTransaction;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lab User
 */
public class ReportManager {

    private List<String> orderids;

    public ReportManager() {
        orderids = new ArrayList<String>();
    }
    
    public List<CloneOrder> queryCloneOrders() {
        String sql = "select orderid, shippingaddress, numofclones, numofcollection"
                + " from cloneorder where orderdate between to_date('09/30/12', 'MM/DD/YY')"
                + " and to_date('09/30/13', 'MM/DD/YY')";
        List<CloneOrder> orders = new ArrayList<CloneOrder>();
        DatabaseTransaction t = null;
        ResultSet rs = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int orderid = rs.getInt(1);
                System.out.println(orderid);
                String address = rs.getString(2);
                int clones = rs.getInt(3);
                int collections = rs.getInt(4);
                CloneOrder order = new CloneOrder();
                order.setOrderid(orderid);
                order.setShippingInfo(parseAddress(address, orderid));
                order.setNumofclones(clones);
                order.setNumofcollection(collections);
                orders.add(order);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return orders;
    }

    public void printReport(List<CloneOrder> orders, String filename) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filename));
            out.println("Order ID\tStreet\tStreet (cont)\tCity\tState\tZip Code\tCountry\tInstitution\tNumber of Clones\tNumber of Collections");
            for (CloneOrder order : orders) {
                UserAddress address = order.getShippingInfo();
                out.print(order.getOrderid() + "\t");
                out.print(address.getAddressline1() + "\t");
                out.print(address.getAddressline2() + "\t");
                out.print(address.getCity() + "\t");
                out.print(address.getState() + "\t");
                out.print(address.getZipcode() + "\t");
                out.print(address.getCountry() + "\t");
                out.print(address.getOrganization() + "\t");
                out.print(order.getNumofclones() + "\t");
                out.println(order.getNumofcollection());
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printErrorOrders(String filename) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(filename));
            for (String order : orderids) {
                out.println(order);
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private UserAddress parseAddress(String addressString, int orderid) throws Exception {
        UserAddress address = new UserAddress();
        String[] s1 = addressString.split("\n");
        address.setOrganization(s1[0]);
        address.setAddressline1(s1[1]);
        String[] s2 = s1[3].split(", ");
        if (s2.length == 1) {
            s2 = s1[2].split(", ");
            address.setCountry(s1[3]);
        } else {
            address.setAddressline2(s1[2]);
            address.setCountry(s1[4]);
        }
        address.setCity(s2[0]);
        String[] s3 = s2[1].split(" ");
        address.setState(s3[0]);
        try {
            address.setZipcode(s3[1]);
        } catch (Exception ex) {
            ex.printStackTrace();
            orderids.add("" + orderid);
        }
        return address;
    }

    public static final void main(String args[]) {
        String filename = "D:\\prod\\report\\clone_dist_geo_201310.txt";
        String errorFilename = "D:\\prod\\report\\clone_dist_geo_201310_error.txt";
        ReportManager manager = new ReportManager();
        List<CloneOrder> orders = manager.queryCloneOrders();
        if (orders == null) {
            System.out.println("no orders found.");
        } else {
            manager.printReport(orders, filename);
            manager.printErrorOrders(errorFilename);
        }
        System.exit(0);
    }

    /**
     * @return the orderids
     */
    public List<String> getOrderids() {
        return orderids;
    }

    /**
     * @param orderids the orderids to set
     */
    public void setOrderids(List<String> orderids) {
        this.orderids = orderids;
    }
}
