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
                    <%--<jsp:include page="orderHistoryTitle.jsp" />--%>

                    <logic:present name="ordermessage">
                        <p class="text"><bean:write name="ordermessage"/></p>
                    </logic:present>
                    <html:errors/>

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
                        <tr> 
                            <td class="formlabel">Last Updatec On:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="updatedon"/></td>
                            <td class="formlabel">Last Updatec By:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="updatedby"/></td>
                        </tr>
                        <tr> 
                            <td class="formlabel">Shipping Method:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingmethod"/></td>
                            <td class="formlabel">Shipping Account:</td>
                            <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingaccount"/></td>
                        </tr>
                        <tr>
                            <td class="formlabel"></td>
                            <logic:equal name="<%=Constants.CLONEORDER%>" property="invoiceid" value="0">
                                <td class="text"></td>
                            </logic:equal>
                            <logic:notEqual name="<%=Constants.CLONEORDER%>" property="invoiceid" value="0">
                                <td class="text">
                                    <html:form action="ViewInvoiceDetail.do">
                                        <input type="hidden" name="invoiceid" value="<bean:write name="<%=Constants.CLONEORDER%>" property="invoiceid"/>"/>
                                        <input type="hidden" name="orderid" value="<bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/>"/>
                                        <input type="hidden" name="isdownload" value="1"/>
                                        <html:submit value="View Invoice"/>
                                    </html:form>
                                </td>
                            </logic:notEqual>
                        </tr>
                    </table>

                    <p>&nbsp;</p>
                    <table width="100%" border="0">
                        <tr> 
                            <td colspan="2" class="featuretext">Shipping To:</td>
                            <td class="featuretext">Billing To:</td>
                            <td class="featuretext">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <html:form action="EditBilling.do">
                                        <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                        <html:submit styleClass="text" value="Edit Billing Information"/>
                                    </html:form>
                                </logic:equal>
                            </td>
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

                    <p class="text">Shipments:</P>
                        <logic:equal name="<%=Constants.CLONEORDER%>" property="displayShipment" value="true">
                    <table width="100%" border="0">
                        <tr> 
                            <td class="tableheader">Date</td>
                            <td class="tableheader">Method</td>
                            <td class="tableheader">Account</td>
                            <td class="tableheader">#Clones</td>
                            <td class="tableheader">Tracking#</td>
                            <td class="tableheader">Comments</td>
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                <td class="tableheader">Who</td>
                                <td class="tableheader">&nbsp;</td>
                                </logic:equal>
                        </tr>
                        <logic:iterate name="<%=Constants.CLONEORDER%>" property="shipments" id="shipment">
                            <tr> 
                                <td class="tablebody"><bean:write name="shipment" property="date"/></td>
                                <td class="tablebody"><bean:write name="shipment" property="method"/></td>
                                <td class="tablebody"><bean:write name="shipment" property="account"/></td>
                                <td class="tablebody"><bean:write name="shipment" property="numOfClones"/></td>
                                <td class="tablebody"><bean:write name="shipment" property="trackingnumber"/></td>
                                <td class="tablebody"><bean:write name="shipment" property="comments"/></td>
                                    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <td class="tablebody"><bean:write name="shipment" property="who"/></td>
                                    <td class="tablebody">
                                            <html:form action="PrintPackingSlip.do">
                                                <html:hidden name="shipment" property="shipmentid"/>
                                                <html:submit styleClass="text" value="Generate Packing Slip"/>
                                            </html:form>
                                    </td>
                                    </logic:equal>
                            </tr>
                        </logic:iterate>
                    </table>
                    </logic:equal>
                    <logic:equal name="<%=Constants.CLONEORDER%>" property="displayShipment" value="false">
                    <p class="text">There is no shipment for this order.</p>
                    </logic:equal>

                    <p>
                    <table width="100%" border="0">
                        <tr>
                            <td width="20%" align="left" class="text">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isplatinum" value="Yes">
                                        <html:form action="EnterPlatinumResult.do">
                                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                            <html:submit styleClass="text" value="Enter Platinum Results"/>
                                        </html:form>     
                                    </logic:equal>
                                </logic:equal>
                            </td>
                            <td width="20%" align="left" class="text">  
                                <logic:equal name="<%=Constants.CLONEORDER%>" property="isplatinum" value="Yes">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="hasPlatinumResult" value="true">
                                        <html:form action="ViewPlatinumResult.do">
                                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                            <html:submit styleClass="text" value="View Platinum Results"/>
                                        </html:form>
                                    </logic:equal>
                                </logic:equal>
                            </td>             
                            <td width="20%" align="left" class="text">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isProcessShipping" value="true">
                                        <html:form action="GenerateFedexLabel.do" target="_blank">
                                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                            <html:submit styleClass="text" value="Generate FedEx Label"/>
                                        </html:form>
                                    </logic:equal>
                                </logic:equal>
                            </td>    
                            <td width="20%" align="left" class="text">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isProcessShipping" value="true">
                                        <html:form action="ProcessShipping.do">
                                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                            <html:submit styleClass="text" value="Process Shipping"/>
                                        </html:form>
                                    </logic:equal>
                                </logic:equal>
                            </td> 
                            <td width="20%" align="left" class="text">
                                <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <logic:equal name="<%=Constants.CLONEORDER%>" property="isPartiallyShipped" value="true">
                                        <html:form action="CompleteOrderInput.do">
                                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                            <html:submit styleClass="text" value="Complete Order"/>
                                        </html:form>
                                    </logic:equal>
                                </logic:equal>
                            </td> 
                        </tr>
                    </table>

                </td>
            </tr>
        </table>
        <jsp:include page="footer.jsp" /></body>
</div>
</html>
