<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Invoice" %>

<html>
    <head>
        <title>PlasmID Database</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">
    
    <body>
        <jsp:include page="signinMenuBar.jsp" />
        <table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
            <tr> 
                <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
                    <jsp:include page="seq_menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="seq_invoiceTitle.jsp" />--%>
                    
                    <logic:present name="ordermessage">
                        <p class="text"><bean:write name="ordermessage"/></p>
                    </logic:present>
                    <html:errors/>
                    
                    <html:form action="SEQ_UpdateInvoicePayment.do" focus="payment">
                        <html:hidden name="<%=Constants.INVOICE%>" property="invoiceid"/>
                        <html:hidden name="<%=Constants.INVOICE%>" property="price"/>
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
                                <td class="formlabel">Price:</td>
                                <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="priceString"/></td>
                                <td class="formlabel">Payment Due:</td>
                                <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="dueString"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Adjustment (enter negative number for refund):</td>
                                <td class="text">$<html:text styleClass="text" name="<%=Constants.INVOICE%>" property="adjustment"/></td>
                                <td class="formlabel">PO/Billing Number:</td>
                                <td class="text"><html:text name="<%=Constants.INVOICE%>" property="accountnum"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Payment:</td>
                                <td class="text">$<html:text styleClass="text" name="<%=Constants.INVOICE%>" property="payment"/></td>
                                <td class="formlabel">Payment Status:</td>
                                <td class="text"><bean:write name="<%=Constants.INVOICE%>" property="paymentstatus"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel" valign="top">Reason For Adjustment:</td>
                                <td colspan="3" class="text"><html:textarea cols="60" rows="10" name="<%=Constants.INVOICE%>" property="reasonforadj"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel" valign="top">Comments:</td>
                                <td colspan="3" class="text"><html:textarea cols="60" rows="10" name="<%=Constants.INVOICE%>" property="comments"/></td>
                            </tr>
                            <tr>
                                <td>&nbsp;</td>
                                <td class="formlabel"><html:submit value="Submit"/></td>
                            </tr>
                        </table>
                    </html:form>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
</div>
</html>
