<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %>
<%@ page import="plasmid.coreobject.CloneOrder" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
    </head>
    
    <body>
        <jsp:include page="homeTitle.jsp" />
        <table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="seq_menuHome.jsp" />
                </td>
                <td width="83%" align="left" valign="top">
                    <jsp:include page="seq_invoiceTitle.jsp" />
                    
                    <p>
                    <logic:present name="ordermessage">
                        <p class="text"><bean:write name="ordermessage"/></p>
                    </logic:present>
                    <html:errors/>
                    
                    
                    <table width="100%" border="0">
                        <tr> 
                            <td class="formlabel">Invoice Number:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="invoicenum"/></td>
                            <td class="formlabel">Invoice Date:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="invoicedate"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">PI:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="piname"/></td>
                            <td class="formlabel">Institution:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="institution"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Payment Status:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="paymentstatus"/></td>
                            <td class="formlabel">PO/Billing Number:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="accountnum"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Price:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="priceString"/></td>
                            <td class="formlabel">Adjustment:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="adjustmentString"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Payment:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="paymentString"/></td>
                            <td class="formlabel">Payment Due:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="dueString"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Comments:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="comments"/></td>
                            <td class="formlabel">Reason For Adjustment:</td>
                            <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="reasonforadj"/></td>
                        </tr>
                        <tr> 
                            <td>&nbsp;</td>
                            <html:form action="SEQ_EnterInvoicePayment.do">
                                <input type="hidden" name="invoiceid" value="<bean:write name="<%=Constants.INVOICE%>" property="invoiceid"/>"/>
                                <td class="text"><html:submit styleClass="text" value="Enter Payment"/></td>
                            </html:form>
                            <html:form action="SEQ_ViewInvoiceDetail.do">
                                <input type="hidden" name="invoiceid" value="<bean:write name="<%=Constants.INVOICE%>" property="invoiceid"/>"/>
                                <input type="hidden" name="orderid" value="<bean:write name="<%=Constants.INVOICE%>" property="orderid"/>"/>
                                <input type="hidden" name="isdownload" value="1"/>
                                <td class="text"><html:submit property="button" styleClass="text" value="<%=Constants.INVOICE_BUTTON_VIEW_INVOICE%>"/></td>
                                <td class="text"><html:submit property="button" styleClass="text" value="<%=Constants.INVOICE_BUTTON_EMAIL_INVOICE%>"/></td>
                                <td class="text"><html:submit property="button" styleClass="text" value="<%=Constants.INVOICE_BUTTON_EMAIL_All_USER_INVOICE%>"/></td>
                            </html:form>
                            <td>&nbsp;</td>
                        </tr>
                    </table>
                    
                    <p class="mainbodytext">Order Information</p>
                    <table width="100%" border="0">
                            <tr>
                                <td class="tableheader">Order ID</td>
                                <td class="tableheader">Order Date</td>
                                <td class="tableheader">User</td>
                                <td class="tableheader">Samples</td>
                                <td class="tableheader">Cost</td>
                                <td class="tableheader">Billing Address</td>
                                <td class="tableheader">Billing Email</td>
                                <td class="tableheader">PI Email</td>
                                <td class="tableheader">Affiliation</td>
                            </tr>
                            
                            <logic:iterate id="order" name="<%=Constants.INVOICE%>" property="seqorder"> 
                                <tr>
                                    <td class="tableinfo"><bean:write name="order" property="orderid"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="orderdate"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="username"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="samples"/></td>
                                    <td class="tableinfo">$<bean:write name="order" property="cost"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="billingaddress"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="billingemail"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="piemail"/></td>
                                    <td class="tableinfo"><bean:write name="order" property="affiliation"/></td>
                                </tr>
                            </logic:iterate>    
                    </table>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
</html>
