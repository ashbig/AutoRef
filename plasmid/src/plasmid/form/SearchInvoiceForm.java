/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plasmid.form;

import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.ActionForm;

/**
 *
 * @author Dongmei
 */
public class SearchInvoiceForm extends ActionForm {
    private String invoicenums;
    private String invoiceDateFrom;
    private String invoiceDateTo;
    private String invoiceMonth;
    private String invoiceYear;
    private String pinames;
    private String ponumbers;
    private String pstatus;
    private String isinternal;
    private String institution1;
    private String institution2;
    private String sortby;
    private int selectedInvoices[];
    private String submitButton;

    public void reset() {
        selectedInvoices = new int[0];
    }
    
    public void resetAttributes() {
        invoicenums = null;
        invoiceDateFrom = null;
        invoiceDateTo = null;
        invoiceMonth = null;
        invoiceYear = null;
        pinames = null;
        ponumbers = null;
        pstatus = null;
        isinternal = null;
        institution1 = null;
        institution2 = null;
    }

    public String getInvoicenums() {
        return invoicenums;
    }

    public void setInvoicenums(String invoicenums) {
        this.invoicenums = invoicenums;
    }

    public String getInvoiceDateFrom() {
        return invoiceDateFrom;
    }

    public void setInvoiceDateFrom(String invoiceDateFrom) {
        this.invoiceDateFrom = invoiceDateFrom;
    }

    public String getInvoiceDateTo() {
        return invoiceDateTo;
    }

    public void setInvoiceDateTo(String invoiceDateTo) {
        this.invoiceDateTo = invoiceDateTo;
    }

    public String getInvoiceMonth() {
        return invoiceMonth;
    }

    public void setInvoiceMonth(String invoiceMonth) {
        this.invoiceMonth = invoiceMonth;
    }

    public String getInvoiceYear() {
        return invoiceYear;
    }

    public void setInvoiceYear(String invoiceYear) {
        this.invoiceYear = invoiceYear;
    }

    public String getPinames() {
        return pinames;
    }

    public void setPinames(String pinames) {
        this.pinames = pinames;
    }

    public String getPonumbers() {
        return ponumbers;
    }

    public void setPonumbers(String ponumbers) {
        this.ponumbers = ponumbers;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String paymentstatus) {
        this.pstatus = paymentstatus;
    }

    public String getIsinternal() {
        return isinternal;
    }

    public void setIsinternal(String isinternal) {
        this.isinternal = isinternal;
    }

    public String getInstitution1() {
        return institution1;
    }

    public void setInstitution1(String institution1) {
        this.institution1 = institution1;
    }

    public String getInstitution2() {
        return institution2;
    }

    public void setInstitution2(String institution2) {
        this.institution2 = institution2;
    }

    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    public int[] getSelectedInvoices() {
        return selectedInvoices;
    }

    public void setSelectedInvoices(int [] selectedInvoices) {
        this.selectedInvoices = selectedInvoices;
    }

    public String getSubmitButton() {
        return submitButton;
    }

    public void setSubmitButton(String submitButton) {
        this.submitButton = submitButton;
    }
}