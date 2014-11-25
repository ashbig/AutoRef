<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.CloneOrder" %>
<%@ page import="plasmid.coreobject.User" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
        <script type="text/JavaScript"    Language="JavaScript">
            function setCheckbox() {
                for(i=0; i<document.searchInvoiceForm.selectedInvoices.length; i++) {
                    document.searchInvoiceForm.selectedInvoices[i].checked=document.searchInvoiceForm.invoiceCheckbox.checked;
                }
            }
        </script>
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="seq_menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="seq_invoiceTitle.jsp" />--%>
                    
                    <p>
                    <html:form action="SEQ_ViewSelectedInvoices.do">
                        <table width="100%" border="0">
                            <tr>
                                <td class="tableheader"><input type="checkbox" name="invoiceCheckbox" onclick="setCheckbox()" /></td>
                                <td class="tableheader"><a href="SEQ_SearchInvoice.do?sortby=<%=Constants.INVOICE_SORT_BY_ID%>">Invoice</a></td>
                                <td class="tableheader"><a href="SEQ_SearchInvoice.do?sortby=<%=Constants.INVOICE_SORT_BY_DATE%>">Date</a></td>
                                <td class="tableheader"><a href="SEQ_SearchInvoice.do?sortby=<%=Constants.INVOICE_SORT_BY_PI%>">PI</a></td>
                                <td class="tableheader"><a href="SEQ_SearchInvoice.do?sortby=<%=Constants.INVOICE_SORT_BY_INSTITUTION%>">Institution</a></td>
                                <td class="tableheader">Price</td>
                                <td class="tableheader">Adjustment</td>
                                <td class="tableheader">Payment</td>
                                <td class="tableheader">Due</td>
                                <td class="tableheader"><a href="SEQ_SearchInvoice.do?sortby=<%=Constants.INVOICE_SORT_BY_PO%>">PO/Billing</a></td>
                                <td class="tableheader">&nbsp;</td>
                                <td class="tableheader">&nbsp;</td>
                            </tr>
                            
                            <logic:iterate id="invoice" name="<%=Constants.INVOICES%>"> 
                                <tr>
                                    <td class="tableinfo"><html:multibox property="selectedInvoices"><bean:write name="invoice" property="invoiceid"/></html:multibox></td>
                                    <td class="tableinfo"><a href="SEQ_ViewInvoiceDetail.do?invoiceid=<bean:write name="invoice" property="invoiceid"/>&isdownload=0"><bean:write name="invoice" property="invoicenum"/></a></td>
                                    <td class="tableinfo"><bean:write name="invoice" property="invoicedate"/></td>
                                    <td class="tableinfo"><bean:write name="invoice" property="piname"/></td>
                                    <td class="tableinfo"><bean:write name="invoice" property="institution"/></td>
                                    <td class="tableinfo" align="right"><bean:write name="invoice" property="priceString"/></td>
                                    <td class="tableinfo" align="right"><bean:write name="invoice" property="adjustmentString"/></td>
                                    <td class="tableinfo" align="right"><bean:write name="invoice" property="paymentString"/></td>
                                    <td class="tableinfo" align="right"><bean:write name="invoice" property="dueString"/></td>
                                    <td class="tableinfo"><bean:write name="invoice" property="accountnum"/></td>
                                    <td class="tableinfo"><a href="SEQ_EnterInvoicePayment.do?invoiceid=<bean:write name="invoice" property="invoiceid"/>&returnToList=true">Enter Payment</a></td>
                                    <td class="tableinfo"><a target="_blank" href="SEQ_ViewInvoiceDetail.do?invoiceid=<bean:write name="invoice" property="invoiceid"/>&orderid=<bean:write name="invoice" property="orderid"/>&isdownload=1">View Invoice</a></td>
                                </tr>
                            </logic:iterate>    
                            <tr>
                                <td class="tableinfo">&nbsp;</td>
                                <td class="tableinfo">Total</td>
                                <td class="tableinfo">&nbsp;</td>
                                <td class="tableinfo">&nbsp;</td>
                                <td class="tableinfo">&nbsp;</td>
                                <td class="tableinfo" align="right"><bean:write name="<%=Constants.INVOICE_SUM%>" property="priceString"/></td>
                                <td class="tableinfo" align="right"><bean:write name="<%=Constants.INVOICE_SUM%>" property="adjustmentString"/></td>
                                <td class="tableinfo" align="right"><bean:write name="<%=Constants.INVOICE_SUM%>" property="paymentString"/></td>
                                <td class="tableinfo" align="right"><bean:write name="<%=Constants.INVOICE_SUM%>" property="dueString"/></td>
                                <td class="tableinfo">&nbsp;</td>
                                <td class="tableinfo">&nbsp;</td>
                                <td class="tableinfo">&nbsp;</td>
                            </tr>
                        </table>
                        <html:submit styleClass="text" property="submitButton" value="<%=Constants.INVOICE_BUTTON_VIEW_SELECT_INVOICE%>"/>
                        <html:submit styleClass="text" property="submitButton" value="<%=Constants.INVOICE_BUTTON_VIEW_ALL_INVOICE%>"/>
                        <html:submit styleClass="text" property="submitButton" value="<%=Constants.INVOICE_BUTTON_EMAIL_SELECT_INVOICE%>"/>
                        <html:submit styleClass="text" property="submitButton" value="<%=Constants.INVOICE_BUTTON_EMAIL_ALL_INVOICE%>"/>
                        <html:submit styleClass="text" property="submitButton" value="<%=Constants.INVOICE_BUTTON_EMAIL_ALL_USER_SELECT_INVOICE%>"/>
                        <html:submit styleClass="text" property="submitButton" value="<%=Constants.INVOICE_BUTTON_EMAIL_ALL_USER_ALL_INVOICE%>"/>
                    </html:form>
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
    <HEAD>
        <META HTTP-EQUIV="Pragma" CONTENT="no-cache">
        <META HTTP-EQUIV="Expires" CONTENT="-1">
    </HEAD>
</div>
</html>
