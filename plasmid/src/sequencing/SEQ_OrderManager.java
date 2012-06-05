/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequencing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import plasmid.Constants;
import plasmid.coreobject.Invoice;
import plasmid.database.DatabaseManager.DefTableManager;
import plasmid.database.DatabaseManager.TableManager;
import plasmid.database.DatabaseTransaction;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class SEQ_OrderManager extends TableManager {

    public SEQ_OrderManager() {
        super();
    }

    /** Creates a new instance of CloneOrderManager */
    public SEQ_OrderManager(Connection conn) {
        super(conn);
    }

    public Invoice queryInvoice(int invoiceid) throws Exception {
        String sql = "select invoicenumber, to_char(invoicedate,'YYYY-MM-dd'),"
                + " price, adjustment, payment, paymentstatus, nvl(paymenttype, ' '), account,"
                + " nvl(comments,' '), nvl(reason, ' '), piname, institution, isharvard"
                + " from seqinvoice where invoiceid=" + invoiceid;

        DatabaseTransaction t = null;
        ResultSet rs = null;
        Invoice invoice = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                String invoicenumber = rs.getString(1);
                String invoicedate = rs.getString(2);
                double price = rs.getDouble(3);
                double adjustment = rs.getDouble(4);
                double payment = rs.getDouble(5);
                String paymentstatus = rs.getString(6);
                String paymenttype = rs.getString(7);
                String account = rs.getString(8);
                String comments = rs.getString(9);
                String reason = rs.getString(10);
                String piname = rs.getString(11);
                String institution = rs.getString(12);
                String isharvard = rs.getString(13);
                invoice = new Invoice(invoicenumber, invoicedate, price, adjustment, payment, paymentstatus, paymenttype, 0, reason, account);
                invoice.setComments(comments);
                invoice.setInvoiceid(invoiceid);
                invoice.setPiname(piname);
                invoice.setInstitution(institution);
                invoice.setIsharvard(isharvard);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query invoice.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return invoice;
    }

    public List queryInvoices(List invoicenums, String invoiceDateFrom, String invoiceDateTo,
            String invoiceMonth, String invoiceYear, List lastnames, List ponumbers,
            String paymentstatus, String isinternal, String institution1, String sort) {
        String sql = "select invoiceid, to_char(invoicedate,'YYYY-MM-dd'), price,"
                + " adjustment, payment, paymentstatus, nvl(paymenttype,' '), account,"
                + " nvl(comments,' '), nvl(reason,' '), piname, institution, invoicenumber, isharvard"
                + " from seqinvoice where invoiceid>0";

        if (invoicenums != null) {
            sql += " and lower(invoicenumber) in (" + StringConvertor.convertFromListToSqlString(invoicenums).toLowerCase() + ")";
        }
        if (invoiceDateFrom != null) {
            sql += " and invoicedate>=To_DATE('" + invoiceDateFrom + "', 'MM/DD/YYYY')";
        }
        if (invoiceDateTo != null) {
            sql += " and invoicedate<=To_DATE('" + invoiceDateTo + "', 'MM/DD/YYYY')";
        }
        if (invoiceMonth != null) {
            sql += " and to_char(invoicedate, 'FMMonth') = '" + invoiceMonth + "'";
        }
        if (invoiceYear != null) {
            sql += " and to_char(invoicedate, 'YYYY')= '" + invoiceYear + "'";
        }
        if (lastnames != null) {
            sql += " and upper(pilastname) in (" + StringConvertor.convertFromListToSqlString(lastnames) + ")";
        }
        if (ponumbers != null) {
            sql += " and account in (" + StringConvertor.convertFromListToSqlString(ponumbers) + ")";
        }
        if (paymentstatus != null) {
            sql += " and paymentstatus='" + paymentstatus + "'";
        }
        if (institution1 != null) {
            sql += " and institution='" + institution1 + "'";
        }
        if (isinternal != null) {
            if (Constants.YES.equals(isinternal)) {
                sql += " and isharvard='Y'";
            } else {
                sql += " and isharvard='N'";
            }
        }
        if (Constants.INVOICE_SORT_BY_DATE.equals(sort)) {
            sql += "order by invoicedate";
        } else if (Constants.INVOICE_SORT_BY_INSTITUTION.equals(sort)) {
            sql += "order by institution";
        } else if (Constants.INVOICE_SORT_BY_PI.equals(sort)) {
            sql += "order by piname";
        } else if (Constants.INVOICE_SORT_BY_PO.equals(sort)) {
            sql += "order by account";
        } else {
            sql = sql + " order by invoicenumber";
        }

        DatabaseTransaction t = null;
        ResultSet rs = null;

        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            List invoices = new ArrayList();
            while (rs.next()) {
                int invoiceid = rs.getInt(1);
                String invoicedate = rs.getString(2);
                double price = rs.getDouble(3);
                double adjustment = rs.getDouble(4);
                double payment = rs.getDouble(5);
                String status = rs.getString(6);
                String paymenttype = rs.getString(7);
                String account = rs.getString(8);
                String comments = rs.getString(9);
                String reason = rs.getString(10);
                String piname = rs.getString(11);
                String institution = rs.getString(12);
                String invoicenumber = rs.getString(13);
                String isharvard = rs.getString(14);
                Invoice invoice = new Invoice(invoicenumber, invoicedate, price, adjustment, payment, status, paymenttype, 0, reason, account);
                invoice.setComments(comments);
                invoice.setInvoiceid(invoiceid);
                invoice.setPiname(piname);
                invoice.setInstitution(institution);
                invoice.setIsharvard(isharvard);
                invoices.add(invoice);
            }
            return invoices;
        } catch (Exception ex) {
            System.out.println(ex);
            handleError(ex, "Cannot query invoice.");
            return null;
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
    }

    public List queryInvoices(List invoiceids) throws Exception {
        String sql = "select invoicenumber, to_char(invoicedate,'YYYY-MM-dd'),"
                + " price, adjustment, payment, paymentstatus, nvl(paymenttype, ' '), account,"
                + " nvl(comments,' '), nvl(reason, ' '), piname, institution, isharvard"
                + " from SEQinvoice where invoiceid=?";

        DatabaseTransaction t = null;
        Connection connection = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        List invoices = new ArrayList();
        try {
            t = DatabaseTransaction.getInstance();
            connection = t.requestConnection();
            stmt = connection.prepareStatement(sql);
            for (int i = 0; i < invoiceids.size(); i++) {
                int invoiceid = (Integer) invoiceids.get(i);
                stmt.setInt(1, invoiceid);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    String invoicenumber = rs.getString(1);
                    String invoicedate = rs.getString(2);
                    double price = rs.getDouble(3);
                    double adjustment = rs.getDouble(4);
                    double payment = rs.getDouble(5);
                    String paymentstatus = rs.getString(6);
                    String paymenttype = rs.getString(7);
                    String account = rs.getString(8);
                    String comments = rs.getString(9);
                    String reason = rs.getString(10);
                    String piname = rs.getString(11);
                    String institution = rs.getString(12);
                    String isharvard = rs.getString(13);
                    Invoice invoice = new Invoice(invoicenumber, invoicedate, price, adjustment, payment, paymentstatus, paymenttype, 0, reason, account);
                    invoice.setComments(comments);
                    invoice.setInvoiceid(invoiceid);
                    invoice.setPiname(piname);
                    invoice.setInstitution(institution);
                    invoice.setIsharvard(isharvard);
                    invoices.add(invoice);
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query invoice.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeConnection(connection);
        }
        return invoices;
    }

    public boolean updateInvoice(int invoiceid, String paymentstatus, String accountnum, double payment, String comments) {
        String sql = "update invoice set paymentstatus=?,"
                + " account=?, payment=?, comments=? where invoiceid=?";
        String sql2 = "update cloneorder set ponumber=?"
                + " where orderid in (select orderid from seqorderxseqinvoice where invoiceid=?)";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, paymentstatus);
            stmt.setString(2, accountnum);
            stmt.setDouble(3, payment);
            stmt.setString(4, comments);
            stmt.setInt(5, invoiceid);
            DatabaseTransaction.executeUpdate(stmt);

            stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, accountnum);
            stmt2.setInt(2, invoiceid);
            DatabaseTransaction.executeUpdate(stmt2);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
        return true;
    }

    public SEQ_Order queryCloneOrder(int orderid) throws Exception {
        String sql = "select c.orderid,to_char(c.orderdate, 'YYYY-MM-DD'),c.ponumber,"
                + " billingaddress,samples,pifirstname,pilastname,piemail,billingemail,username,"
                + " cost,paymentmethod,service,institution,affiliation,userid,isharvard"
                + " from seqorder c where c.orderid=" + orderid;

        String sql2 = "select invoiceid from seqorderxseqinvoice where orderid=" + orderid;

        DatabaseTransaction t = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        SEQ_Order order = null;
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            if (rs.next()) {
                String date = rs.getString(2);
                String ponumber = rs.getString(3);
                String billingaddress = rs.getString(4);
                int samples = rs.getInt(5);
                String pifirstname = rs.getString(6);
                String pilastname = rs.getString(7);
                String piemail = rs.getString(8);
                String billingemail = rs.getString(9);
                String username = rs.getString(10);
                double cost = rs.getDouble(11);
                String paymentmethod = rs.getString(12);
                String service = rs.getString(13);
                String institution = rs.getString(14);
                String affiliation = rs.getString(15);
                int userid = rs.getInt(16);
                String isharvard = rs.getString(17);

                order = new SEQ_Order(orderid, date, billingemail, billingaddress, pifirstname,
                        pilastname, piemail, username, ponumber, samples, cost, paymentmethod, service,
                        institution, affiliation, isharvard, userid);

                rs2 = t.executeQuery(sql2);
                if (rs2.next()) {
                    int invoiceid = rs2.getInt(1);
                    order.setInvoiceid(invoiceid);
                }
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query cloneorder.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
            DatabaseTransaction.closeResultSet(rs2);
        }
        return order;
    }

    public List<SEQ_Order> queryCloneOrders(int invoiceid) throws Exception {
        String sql = "select c.orderid,to_char(c.orderdate, 'YYYY-MM-DD'),c.ponumber,"
                + " billingaddress,samples,pifirstname,pilastname,piemail,billingemail,username,"
                + " cost,paymentmethod,service,institution,affiliation,userid,isharvard"
                + " from seqorder c, seqorderxseqinvoice sc where c.orderid=sc.orderid"
                + " and sc.invoiceid = " + invoiceid;

        DatabaseTransaction t = null;
        ResultSet rs = null;
        List<SEQ_Order> orders = new ArrayList<SEQ_Order>();
        try {
            t = DatabaseTransaction.getInstance();
            rs = t.executeQuery(sql);
            while (rs.next()) {
                int orderid = rs.getInt(1);
                String date = rs.getString(2);
                String ponumber = rs.getString(3);
                String billingaddress = rs.getString(4);
                int samples = rs.getInt(5);
                String pifirstname = rs.getString(6);
                String pilastname = rs.getString(7);
                String piemail = rs.getString(8);
                String billingemail = rs.getString(9);
                String username = rs.getString(10);
                double cost = rs.getDouble(11);
                String paymentmethod = rs.getString(12);
                String service = rs.getString(13);
                String institution = rs.getString(14);
                String affiliation = rs.getString(15);
                int userid = rs.getInt(16);
                String isharvard = rs.getString(17);

                SEQ_Order order = new SEQ_Order(orderid, date, billingemail, billingaddress, pifirstname,
                        pilastname, piemail, username, ponumber, samples, cost, paymentmethod, service,
                        institution, affiliation, isharvard, userid);
                order.setInvoiceid(invoiceid);
                orders.add(order);
            }
        } catch (Exception ex) {
            handleError(ex, "Cannot query cloneorder.");
            throw new Exception(ex);
        } finally {
            DatabaseTransaction.closeResultSet(rs);
        }
        return orders;
    }

    public boolean updateInvoice(int invoiceid, String paymentstatus, String accountnum, double payment,
            double adjustment, String comments, String reason) {
        String sql = "update seqinvoice set paymentstatus=?,"
                + " account=?, payment=?, adjustment=?, comments=?, reason=? where invoiceid=?";
        String sql2 = "update seqorder set ponumber=?"
                + " where orderid in (select orderid from seqorderxseqinvoice where invoiceid=?)";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, paymentstatus);
            stmt.setString(2, accountnum);
            stmt.setDouble(3, payment);
            stmt.setDouble(4, adjustment);
            stmt.setString(5, comments);
            stmt.setString(6, reason);
            stmt.setInt(7, invoiceid);
            DatabaseTransaction.executeUpdate(stmt);

            stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, accountnum);
            stmt2.setInt(2, invoiceid);
            DatabaseTransaction.executeUpdate(stmt2);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
        return true;
    }

    public void insertSeqOrders(List<SEQ_Order> orders) throws SEQ_Exception {
        String sql = "insert into seqorder(orderid,userid,billingemail,billingaddress,pifirstname,pilastname,"
                + "piemail,username,ponumber,orderdate,samples,cost,paymentmethod,service,institution,"
                + "affiliation,isharvard) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        int errorOrderid = 0;
        try {
            stmt = conn.prepareStatement(sql);
            for (SEQ_Order order : orders) {
                errorOrderid = order.getOrderid();
                stmt.setInt(1, order.getOrderid());
                stmt.setInt(2, order.getUserid());
                stmt.setString(3, order.getBillingemail());
                stmt.setString(4, order.getBillingaddress());
                stmt.setString(5, order.getPifirstname());
                stmt.setString(6, order.getPilastname());
                stmt.setString(7, order.getPiemail());
                stmt.setString(8, order.getUsername());
                stmt.setString(9, order.getPonumber());

                DateFormat formater = new SimpleDateFormat("MM/dd/yy");
                java.util.Date parsedUtilDate = formater.parse(order.getOrderdate());
                java.sql.Date sqlDate = new java.sql.Date(parsedUtilDate.getTime());
                stmt.setDate(10, sqlDate);

                stmt.setInt(11, order.getSamples());
                stmt.setDouble(12, order.getCost());
                stmt.setString(13, order.getPaymentmethod());
                stmt.setString(14, order.getService());
                stmt.setString(15, order.getInstitution());
                stmt.setString(16, order.getAffiliation());
                stmt.setString(17, order.getIsharvard());
                DatabaseTransaction.executeUpdate(stmt);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SEQ_Exception("Cannot insert order: " + errorOrderid);
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }

    public void insertInvoices(List<Invoice> invoices) throws SEQ_Exception {
        String sql = "insert into seqinvoice"
                + " (invoiceid, invoicenumber, price, adjustment, payment, paymentstatus, paymenttype,"
                + " account, updatedby, updatedon, reason,invoicedate,piname,institution,pilastname,isharvard)"
                + " values(?,?,?,?,?,?,?,?,'System',sysdate,?,sysdate,?,?,?,?)";
        String sql2 = "insert into seqorderxseqinvoice(orderid,invoiceid) values(?,?)";
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        try {
            stmt = conn.prepareStatement(sql);
            stmt2 = conn.prepareStatement(sql2);

            for (Invoice invoice : invoices) {
                int invoiceid = DefTableManager.getNextid("seqinvoiceid");
                stmt.setInt(1, invoiceid);
                stmt.setString(2, invoice.getInvoicenum());
                stmt.setDouble(3, invoice.getPrice());
                stmt.setDouble(4, invoice.getAdjustment());
                stmt.setDouble(5, invoice.getPayment());
                stmt.setString(6, invoice.getPaymentstatus());
                stmt.setString(7, invoice.getPaymenttype());
                stmt.setString(8, invoice.getAccountnum());
                stmt.setString(9, invoice.getReasonforadj());
                stmt.setString(10, invoice.getPiname());
                stmt.setString(11, invoice.getInstitution());
                stmt.setString(12, invoice.getPilastname());
                stmt.setString(13, invoice.getIsharvard());
                DatabaseTransaction.executeUpdate(stmt);

                List<SEQ_Order> orders = invoice.getSeqorder();
                for (SEQ_Order order : orders) {
                    stmt2.setInt(1, order.getOrderid());
                    stmt2.setInt(2, invoiceid);
                    DatabaseTransaction.executeUpdate(stmt2);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SEQ_Exception("Cannot add invoice to the database for order.");
        } finally {
            DatabaseTransaction.closeStatement(stmt);
            DatabaseTransaction.closeStatement(stmt2);
        }
    }

    public void updateOrder(int orderid, String billingAddress, String billingEmail) throws SEQ_Exception {
        String sql = "update seqorder set billingaddress=?,billingemail=? where orderid=?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, billingAddress);
            stmt.setString(2, billingEmail);
            stmt.setInt(3, orderid);
            DatabaseTransaction.executeUpdate(stmt);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SEQ_Exception("Error occured while updating the database.");
        } finally {
            DatabaseTransaction.closeStatement(stmt);
        }
    }
}
