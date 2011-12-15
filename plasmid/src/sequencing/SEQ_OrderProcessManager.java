/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sequencing;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import plasmid.Constants;
import plasmid.coreobject.Invoice;
import plasmid.database.DatabaseTransaction;
import plasmid.util.Mailer;
import plasmid.util.PdfEditor;
import plasmid.util.StringConvertor;

/**
 *
 * @author Dongmei
 */
public class SEQ_OrderProcessManager {

    public Invoice getInvoice(int invoiceid) throws Exception {
        SEQ_OrderManager manager = new SEQ_OrderManager();
        Invoice invoice = manager.queryInvoice(invoiceid);
        return invoice;
    }

    public List getInvoices(String invoicenums, String invoiceDateFrom, String invoiceDateTo,
            String invoiceMonth, String invoiceYear, String pinames, String ponumbers,
            String paymentstatus, String isinternal, String institution1, String sortby) {
        DatabaseTransaction t = null;
        Connection conn = null;
        StringConvertor sc = new StringConvertor();
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
            SEQ_OrderManager manager = new SEQ_OrderManager();
            List invoicenumList = null;
            if (invoicenums != null && invoicenums.trim().length() > 0) {
                invoicenumList = new ArrayList();
                List l = sc.convertFromStringToList(invoicenums.trim(), ",");
                for (int i = 0; i < l.size(); i++) {
                    String s = (String) l.get(i);
                    s = "SEQ_" + s;
                    invoicenumList.add(s);
                }
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
            List invoices = manager.queryInvoices(invoicenumList, invoiceDateFrom, invoiceDateTo,
                    invoiceMonth, invoiceYear, lastnameList, ponumberList, paymentstatus,
                    isinternal, institution1, sortby);
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

    public List getInvoices(List invoiceids) throws Exception {
        SEQ_OrderManager manager = new SEQ_OrderManager();
        return manager.queryInvoices(invoiceids);
    }

    public SEQ_Order getCloneOrder(int orderid) {
        DatabaseTransaction t = null;
        Connection conn = null;
        SEQ_OrderManager manager = null;
        SEQ_Order order = null;

        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();

            manager = new SEQ_OrderManager(conn);
            order = manager.queryCloneOrder(orderid);
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

    public Invoice getSummeryInvoice(List<Invoice> invoices) {
        double totalPrice = 0;
        double totalAdjustment = 0;
        double totalPayment = 0;
        for (Invoice invoice : invoices) {
            totalPrice += invoice.getPrice();
            totalAdjustment += invoice.getAdjustment();
            totalPayment += invoice.getPayment();
        }
        Invoice invoice = new Invoice();
        invoice.setPrice(totalPrice);
        invoice.setAdjustment(totalAdjustment);
        invoice.setPayment(totalPayment);
        return invoice;
    }

    public void displayInvoice(OutputStream file, SEQ_Order order, Invoice invoice) {
        if (SEQ_Order.ISHARVARD_Y.equals(order.getIsharvard())) {
            printInternalInvoice(file, order, invoice);
        } else {
            printExternalInvoice(file, order, invoice);
        }
    }

    public void printInvoices(OutputStream file, List invoices) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            for (int i = 0; i < invoices.size(); i++) {
                Invoice invoice = (Invoice) invoices.get(i);
                SEQ_Order order = invoice.getSeqorder();
                if (SEQ_Order.ISHARVARD_Y.equals(order.getIsharvard())) {
                    printInternalInvoiceContent(document, order, invoice);
                } else {
                    printExternalInvoiceContent(document, order, invoice);
                }
                document.newPage();
            }
            document.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printExternalInvoice(OutputStream file, SEQ_Order order, Invoice invoice) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            printExternalInvoiceContent(document, order, invoice);
            document.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printExternalInvoiceContent(Document document, SEQ_Order order, Invoice invoice) throws DocumentException {
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
        table.addCell(PdfEditor.makeSmall("Dep.BCMP Room C- 214"));
        table.addCell(PdfEditor.makeSmall("Order Date:\t" + order.getOrderdate()));
        table.addCell(PdfEditor.makeSmall("240 Longwood Ave."));
        table.addCell(PdfEditor.makeSmall("Order By:\t" + order.getUsername()));
        table.addCell(PdfEditor.makeSmall("Boston, MA 02115"));
        table.addCell(PdfEditor.makeSmall("PI:\t" + order.getPiname()));
        table.addCell(PdfEditor.makeSmall("(617)432-1210"));
        table.addCell(PdfEditor.makeSmall("PI email:\t" + order.getPiemail()));
        table.addCell(PdfEditor.makeSmall("Attn: Elmira Dhroso"));
        table.addCell(PdfEditor.makeSmall("Grant or PO Number:\t" + order.getPonumber()));
        document.add(table);

        document.add(PdfEditor.makeTitle(" "));
        document.add(PdfEditor.makeSmallBold("Bill To:"));
        document.add(PdfEditor.makeSmall(order.getBillingaddress()));
        document.add(PdfEditor.makeSmall("Billing email: " + order.getBillingemail()));

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
        table.addCell(PdfEditor.makeSmall("Samples"));
        cell = new PdfPCell(PdfEditor.makeSmall("" + order.getSamples()));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCost()));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        table.addCell(PdfEditor.makeSmall("Total price"));
        cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCost()));
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
        document.add(PdfEditor.makeSmallBold("Make checkes payable to: Harvard Medical School. " +
                "Include invoice number on check. Payments must be made in U.S. funds drawn on a U.S. bank. " +
                "If you pay through wire transfer, please include wire transfer fee in the total amount."));

        document.add(PdfEditor.makeTitle(" "));
        document.add(PdfEditor.makeSmallBold("If your payment due is zero, please DO NOT send payment and regard this as your receipt."));
        
        document.add(PdfEditor.makeTitle(" "));
        document.add(PdfEditor.makeSmallBold("Mailing Address:"));
        document.add(PdfEditor.makeSmall("  Harvard Medical School"));
        document.add(PdfEditor.makeSmall("  Dep.BCMP Room C- 214"));
        document.add(PdfEditor.makeSmall("  240 Longwood Ave."));
        document.add(PdfEditor.makeSmall("  Boston, MA 02115"));
        document.add(PdfEditor.makeSmall("  617-432-1210"));

        document.add(PdfEditor.makeTitle(" "));
        document.add(PdfEditor.makeSmallItalic("Please see billing memo for more payment information."));

        document.add(PdfEditor.makeTitle(" "));
        document.add(PdfEditor.makeSmallBold("For Invoice Information Contact:"));
        document.add(PdfEditor.makeSmall("  Elmira Dhroso, (617)432-1210"));
        document.add(PdfEditor.makeSmall("  elmira_dhroso@hms.harvard.edu"));
    }

    public void printInternalInvoice(OutputStream file, SEQ_Order order, Invoice invoice) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            printInternalInvoiceContent(document, order, invoice);
            document.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printInternalInvoiceContent(Document document, SEQ_Order order, Invoice invoice) throws DocumentException {
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
        table.addCell(PdfEditor.makeSmall("Dep.BCMP Room C- 214"));
        table.addCell(PdfEditor.makeSmall("Order Date:\t" + order.getOrderdate()));
        table.addCell(PdfEditor.makeSmall("240 Longwood Ave."));
        table.addCell(PdfEditor.makeSmall("Order By:\t" + order.getUsername()));
        table.addCell(PdfEditor.makeSmall("Boston, MA 02115"));
        table.addCell(PdfEditor.makeSmall("PI:\t" + order.getPiname()));
        table.addCell(PdfEditor.makeSmall("(617)432-1210"));
        table.addCell(PdfEditor.makeSmall("PI email:\t" + order.getPiemail()));
        table.addCell(PdfEditor.makeSmall("Attn: Elmira Dhroso"));
        table.addCell(PdfEditor.makeSmall("Grant or PO Number:\t" + order.getPonumber()));
        document.add(table);

        document.add(PdfEditor.makeTitle(" "));
        document.add(PdfEditor.makeSmallBold("Bill To:"));
        document.add(PdfEditor.makeSmall(order.getBillingaddress()));
        document.add(PdfEditor.makeSmall("Billing email: " + order.getBillingemail()));

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
        table.addCell(PdfEditor.makeSmall("Samples"));
        cell = new PdfPCell(PdfEditor.makeSmall("" + order.getSamples()));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCost()));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);
        table.addCell(PdfEditor.makeSmall("Total price"));
        cell = new PdfPCell(PdfEditor.makeSmall("$" + order.getCost()));
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
    }

    public void printInvoice(OutputStream file, Invoice invoice) {
        List invoices = new ArrayList();
        invoices.add(invoice);
        printInvoices(file, invoices);
    }

    public void emailInvoices(List invoices, boolean isOther) throws Exception {
        for (int i = 0; i < invoices.size(); i++) {
            Invoice invoice = (Invoice) invoices.get(i);
            emailInvoice(invoice.getSeqorder(), invoice, isOther);
        }
    }

    public void emailInvoice(SEQ_Order order, Invoice invoice, boolean isOther) throws Exception {
        String filename = Constants.TMP + "Invoice_" + order.getOrderid() + ".pdf";
        File f1 = new File(filename);
        OutputStream file = new FileOutputStream(f1);
        printInvoice(file, invoice);
        File f2 = new File(Constants.FILE_PATH + "Billing_memo.pdf");
        List files = new ArrayList();
        files.add(f1);
        files.add(f2);

        String billingemail = order.getBillingemail();
        List ccs = new ArrayList();
        ccs.add(Constants.EMAIL_FROM);
        if (isOther) {
            ccs.add(order.getPiemail());
        }
        String subject = "Invoice for order " + invoice.getOrderid();
        String text = "Dear Accounts Payable Representative:\n\n" +
                "Payment is requested for the attached invoice from the DF/HCC DNA Resource Core" +
                " at Harvard Medical School.\n\n" +
                "This invoice was generated upon completion of your sequencing order." +
                " Any discounts or price modifications should already be reflected on this invoice." +
                " If you have received this notification in error, or believe that a clerical" +
                " mistake has occurred please contact us as soon as possible to resolve the issue." +
                " Please find payment instructions attached to this email and ALWAYS BE SURE TO" +
                " INCLUDE YOUR INVOICE NUMBER WITH PAYMENT and ADD ANY WIRE TRANSFER FEES TO YOUR TOTAL.\n\n" +
                "As a part of our continued environmental efforts this email will serve as your" +
                " sole notification, and no paper copy will be mailed to your facility.\n\n" +
                "Payment Terms: Net 30\n" +
                "Enclosures: Invoice, Payment Instructions\n";

        Mailer.sendMessages(billingemail, Constants.EMAIL_FROM, ccs, subject, text, files);
    }

    public List<Invoice> generateInvoices(List<SEQ_Order> orders) {
        List<Invoice> invoices = new ArrayList<Invoice>();
        for (SEQ_Order order : orders) {
            Invoice invoice = new Invoice();
            invoice.setInvoicenum("SEQ_" + order.getOrderid());
            invoice.setPaymentstatus(Invoice.PAYMENTSTATUS_UNPAID);
            invoice.setPaymenttype(order.getPaymentmethod());
            invoice.setPrice(order.getCost());
            if (SEQ_Order.PAYMENTMETHOD_CREDIT.equals(order.getPaymentmethod())) {
                invoice.setPaymentstatus(Invoice.PAYMENTSTATUS_PAID);
                invoice.setPaymenttype(Invoice.PAYMENTTYPE_CREDITCARD);
                invoice.setPayment(order.getCost());
            }
            invoice.setAccountnum(order.getPonumber());
            invoice.setComments("");
            invoice.setReasonforadj("");
            invoice.setOrderid(order.getOrderid());
            invoices.add(invoice);
        }
        return invoices;
    }

    public void uploadSeqOrders(InputStream in) throws SEQ_Exception {
        SEQ_FileReader reader = new SEQ_FileReader();
        List<SEQ_Order> orders = reader.readSeqOrders(in);
        List<Invoice> invoices = generateInvoices(orders);

        DatabaseTransaction t = null;
        Connection conn = null;
        try {
            t = DatabaseTransaction.getInstance();
            conn = t.requestConnection();
        } catch (Exception ex) {
            throw new SEQ_Exception("Cannot get database connection.");
        }

        if (conn == null) {
            throw new SEQ_Exception("Cannot get database connection.");
        }

        SEQ_OrderManager manager = new SEQ_OrderManager(conn);
        try {
            manager.insertSeqOrders(orders);
            manager.insertInvoices(invoices);
            DatabaseTransaction.commit(conn);
        } catch (SEQ_Exception ex) {
            DatabaseTransaction.rollback(conn);
            throw ex;
        } catch (Exception ex) {
            DatabaseTransaction.rollback(conn);
            ex.printStackTrace();
            throw new SEQ_Exception("Error occured while updating database.");
        } finally {
            DatabaseTransaction.closeConnection(conn);
        }
    }
}
