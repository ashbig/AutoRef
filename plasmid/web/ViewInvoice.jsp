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
                    <jsp:include page="menuHome.jsp" />
                </td>--%>
                <td width="100%" align="left" valign="top">
                    <%--<jsp:include page="invoiceTitle.jsp" />--%>
                    
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
                            <html:form action="EnterInvoicePayment.do">
                                <input type="hidden" name="invoiceid" value="<bean:write name="<%=Constants.INVOICE%>" property="invoiceid"/>"/>
                                <td class="text"><html:submit styleClass="text" value="Enter Payment"/></td>
                            </html:form>
                            <html:form action="ViewInvoiceDetail.do">
                                <input type="hidden" name="invoiceid" value="<bean:write name="<%=Constants.INVOICE%>" property="invoiceid"/>"/>
                                <input type="hidden" name="orderid" value="<bean:write name="<%=Constants.INVOICE%>" property="orderid"/>"/>
                                <input type="hidden" name="isdownload" value="1"/>
                                <td class="text"><html:submit property="button" styleClass="text" value="<%=Constants.INVOICE_BUTTON_VIEW_INVOICE%>"/></td>
                                <td class="text"><html:submit property="button" styleClass="text" value="<%=Constants.INVOICE_BUTTON_EMAIL_INVOICE%>"/></td>
                                <td class="text"><html:submit property="button" styleClass="text" value="<%=Constants.INVOICE_BUTTON_EMAIL_All_USER_INVOICE%>"/></td>
                            </html:form>
                        </tr>
                    </table>
                    
                    <p>Order Information</p>
                    <table width="100%" border="0">
                        <tr> 
                            <td class="formlabel">Order ID:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/></td>
                            <td class="formlabel">Order Date:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="orderDate"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Order Status:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="status"/></td>
                            <td class="formlabel">PO/Billing Number:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="ponumber"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Platinum Service:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="isplatinum"/></td>
                            <td class="formlabel">Platinum Service Status:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="platinumServiceStatus"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Email:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="email"/></td>
                            <td class="formlabel">Phone:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="phone"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">PI Name:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="piname"/></td>
                            <td class="formlabel">PI Email:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="piemail"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">User Group:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="usergroup"/></td>
                            <td class="formlabel">DF/HCC Member:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="ismemberString"/></td>
                        </tr>
                    </table>
                    
                    <p class="text">Shipping Information</P>
                    <table width="100%" border="0">
                        <tr> 
                            <td width="20%" class="formlabel">Shipping Method:</td>
                            <td width="30%" class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingmethod"/></td>
                            <td class="formlabel">Shipping Date:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingdate"/></td>
                        </tr>
                        <tr> 
                            <td width="20%" class="formlabel">Shipping Account:</td>
                            <td width="30%" class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingaccount"/></td>
                            <td width="20%" class="formlabel">Tracking Number:</td>
                            <td width="30%" class="text"><a target="_blank" href="http://www.fedex.com/Tracking?ascend_header=1&clienttype=dotcom&cntry_code=us&language=english&tracknumbers=<bean:write name="<%=Constants.CLONEORDER%>" property="trackingnumber"/>"><bean:write name="<%=Constants.CLONEORDER%>" property="trackingnumber"/></a></td>
                        </tr>
                        <tr>   <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                            <td class="formlabel">Who Shipped:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="whoshipped"/></td>
                            </logic:equal>
                        </tr>
                        <tr> 
                            <td colspan="4" class="formlabel">Comments:</td>
                        </tr>
                        <tr> 
                            <td colspan="4" class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="comments"/></td>
                        </tr>
                        <logic:present name="<%=Constants.CLONEORDER%>" property="shippedContainers">
                            <tr> 
                                <td colspan="4" class="text">
                                    <html:form action="ViewContainers.do">
                                        <input type="hidden" name="labelString" value="<bean:write name="<%=Constants.CLONEORDER%>" property="shippedContainers"/>">
                                        <html:submit styleClass="text" value="View Containers"/>
                                    </html:form>
                                </td>
                            </tr>
                        </logic:present>
                        <tr>
                            <td colspan="2" align="center" class="text">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isplatinum" value="Yes">
                                        
                                        <logic:equal name="<%=Constants.CLONEORDER%>" property="enterPlatinumBasedOnStatus" value="<%=CloneOrder.PLATINUM_ENTER_RESULTS%>">
                                            <html:form action="EnterPlatinumResult.do">
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                                <html:submit styleClass="text" value="Enter Platinum Results"/>
                                            </html:form>
                                        </logic:equal>
                                        
                                        <logic:equal name="<%=Constants.CLONEORDER%>" property="enterPlatinumBasedOnStatus" value="<%=CloneOrder.PLATINUM_DISPLAY_RESULTS%>">
                                            <html:form action="ViewPlatinumResult.do">
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                                <html:submit styleClass="text" value="View Platinum Results"/>
                                            </html:form>
                                        </logic:equal>
                                        
                                    </logic:equal>
                                </logic:equal>
                                
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.EXTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isplatinum" value="Yes">
                                        <logic:equal name="<%=Constants.CLONEORDER%>" property="displayPlatinumBasedOnStatus" value="<%=CloneOrder.PLATINUM_DISPLAY_RESULTS%>">
                                            <html:form action="ViewPlatinumResult.do">
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                                <html:submit styleClass="text" value="View Platinum Results"/>
                                            </html:form>
                                        </logic:equal>
                                    </logic:equal>
                                </logic:equal>
                            </td>
                            <td colspan="2" align="center" class="text">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isProcessShipping" value="true">
                                        <html:form action="ProcessShipping.do">
                                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                            <html:submit styleClass="text" value="Process Shipping"/>
                                        </html:form>
                                    </logic:equal>
                                </logic:equal>
                            </td>
                        </tr>
                    </table>
                    
                    <p>
                    <table width="100%" border="0">
                        <tr> 
                            <td colspan="2" class="featuretext">Shipping To:</td>
                            <td colspan="2" class="featuretext">Billing To:</td>
                        </tr>
                        <tr> 
                            <td width="17%" class="formlabel">Name:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingTo"/></td>
                            <td class="formlabel">Name:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="billingTo"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Address:</td>
                            <td class="text"><pre><bean:write name="<%=Constants.CLONEORDER%>" property="shippingAddress"/></pre></td>
                            <td class="formlabel">Address:</td>
                            <td class="text"><pre><bean:write name="<%=Constants.CLONEORDER%>" property="billingAddress"/></pre></td>
                        </tr>
                        <tr> 
                            <td>&nbsp;</td>
                            <td>&nbsp;</td>
                            <td class="formlabel">Email:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="billingemail"/></td>
                        </tr>
                    </table>
                    
                    <p>&nbsp;</p>
                    
                    <p class="text">Order Information:</p>
                    <table width="100%" border="0">
                        <tr> 
                            <td class="tableheader">Item</td>
                            <td class="tableheader">Quantity</td>
                            <td class="tableheader">Price</td>
                        </tr>
                        <tr> 
                            <td class="tablebody">Number of clones:</td>
                            <td class="tablebody"><a href="ViewOrderClones.do?type=<%=Constants.ORDER_CLONE%>&orderid=<bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/>&isBatch=<bean:write name="<%=Constants.CLONEORDER%>" property="isBatch"/>"><bean:write name="<%=Constants.CLONEORDER%>" property="numofclones"/></a></td>
                            <td align="right" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforclones"/></td>
                        </tr> 
                        <tr> 
                            <td class="tablebody">Number of collections:</td>
                            <td class="tablebody"><a href="ViewOrderCollections.do?orderid=<bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/>"><bean:write name="<%=Constants.CLONEORDER%>" property="numofcollection"/></a></td>
                            <td align="right" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforcollection"/></td>
                        </tr> 
                        <tr> 
                            <td class="tablebody">Platinum service:</td>
                            <td align="right" colspan="2" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforplatinum"/></td>
                        </tr>
                        <tr> 
                            <td class="tablebody">Shipping and handling:</td>
                            <td align="right" colspan="2" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="shipping"/></td>
                        </tr> 
                        <tr> 
                            <td class="tableheader">Total price:</td>
                            <td align="right" colspan="2" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="totalPriceString"/></td>
                        </tr>
                    </table>
                    
                </td>
            </tr>
        </table>
    <jsp:include page="footer.jsp" /></body>
</div>
</html>
