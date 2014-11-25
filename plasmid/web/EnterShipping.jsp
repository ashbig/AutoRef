<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.User" %>

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
                <td width="83%" align="left" valign="top">
                    <%--<jsp:include page="orderHistoryTitle.jsp" />--%>

                    <html:form action="EnterShipping.do">
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
                                <td class="formlabel">&nbsp;</td>
                                <td class="text">&nbsp;</td>
                                <td class="formlabel">New PO/Billing Number:</td>
                                <td class="text"><html:text styleClass="text" maxlength="100" property="newAccount"/></td>
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
                                <td class="formlabel">User Group:</td>
                                <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="usergroup"/></td>
                                <td class="formlabel">DF/HCC Member:</td>
                                <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="ismember"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">PI Name:</td>
                                <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="piname"/></td>
                                <td class="formlabel">PI Email:</td>
                                <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="piname"/></td>
                            </tr>
                            <tr> 
                                <td class="formlabel">Total Price:</td>
                                <td class="text" colspan="3"><bean:write name="<%=Constants.CLONEORDER%>" property="totalPriceString"/></td>
                            </tr>
                        </table>

                    <p class="text">Check shipped items:</p>
                        <table width="100%" border="0">
                            <tr> 
                                <td class="tableheader">&nbsp;</td>
                                <td class="tableheader">Item</td>
                                <td class="tableheader">Quantity</td>
                                <td class="tableheader">Platinum Result</td>
                                <td class="tableheader">Shipping Status</td>
                            </tr>
                            <logic:iterate name="<%=Constants.CLONEORDER%>" property="clones" id="clone">
                                <tr> 
                                    <td class="tablebody">
                                        <html:multibox property="shipped"><bean:write name="clone" property="cloneid"/></html:multibox>
                                    </td>
                                    <td class="tablebody"><bean:write name="clone" property="clonename"/></td>
                                    <td class="tablebody"><bean:write name="clone" property="quantity"/></td>
                                    <logic:present name="clone" property="validation">
                                    <td class="tablebody"><bean:write name="clone" property="validation.result"/></td>
                                    </logic:present>
                                    <logic:notPresent name="clone" property="validation">
                                        <td class="tablebody">NA</td>
                                    </logic:notPresent>
                                    <td class="tablebody"><bean:write name="clone" property="shippedString"/></td>
                                </tr> 
                            </logic:iterate>
                        </table>

                        <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                            <p class="text">Shipping Information</P>
                            <table width="100%" border="0">
                                <tr> 
                                    <td width="20%" class="formlabel">Shipping Methods:</td>
                                    <td width="30%" class="text">
                                        <html:select property="shippingMethod">
                                            <html:options name="shippingMethods"/>
                                        </html:select>
                                    </td>
                                    <td width="20%" class="formlabel">Shipping Date:</td>
                                    <td width="30%" class="text"><html:text property="shippingDate"/></td>
                                </tr>
                                <tr> 
                                    <td class="formlabel">Shipping Account:</td>
                                    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingaccount"/></td>
                                    <td class="formlabel">Tracking Number:</td>
                                    <td class="text"><html:text maxlength="50" property="trackingNumber"/></td>
                                </tr>
                                <tr>
                                    <td class="formlabel">Who Shipped:</td>
                                    <td class="text"><bean:write name="processShippingForm" property="whoShipped"/></td>
                                    <td class="formlabel">Shipping Status:</td>
                                    <td class="text">
                                        <html:select property="shippingStatus">
                                            <html:options name="allStatus"/>
                                        </html:select>
                                    </td>
                                </tr>
                                <tr> 
                                    <td class="formlabel" colspan="4">Comments:</td>
                                </tr>
                                <tr> 
                                    <td class="text" colspan="4"><html:textarea rows="5" cols="60" property="comments"/></td>
                                </tr>
                            </table>
                            <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                            <p align="center"><html:submit styleClass="text" value="Continue"/></P>
                            </logic:equal>
                        </html:form>

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
    <td class="formlabel">&nbsp;</td>
    <td class="text">&nbsp;</td>
    <td class="formlabel">Email:</td>
    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="billingemail"/></td>
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
                                    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
                                    <td class="tablebody"><bean:write name="shipment" property="who"/></td>
                                    <td class="tablebody">
                                            <html:form action="GeneratePackingSlip.do">
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="orderid"/>
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="orderDate"/>
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="ponumber"/>
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="email"/>
                                                <html:hidden name="<%=Constants.CLONEORDER%>" property="phone"/>
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

    </td>
  </tr>
</table>
        <jsp:include page="footer.jsp" /></body>
    </div>
</html>
