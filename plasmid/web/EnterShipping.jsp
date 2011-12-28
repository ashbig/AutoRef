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
</head>

<body>
<jsp:include page="homeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="orderHistoryTitle.jsp" />

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
    <td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="email"/></td>
    <td class="formlabel">Phone:</td>
    <td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="phone"/></td>
  </tr>
  <tr> 
    <td class="formlabel">Total Price:</td>
    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="totalPriceString"/></td>
    <td class="formlabel">Price Adjustment (enter negative number for refund):</td>
    <td class="text"><html:text styleClass="text" maxlength="50" property="adjustment"/></td>
  </tr>
  <tr> 
    <td class="formlabel" colspan="4">Reason for Adjustment:</td>
  </tr>
  <tr> 
    <td class="text" colspan="4"><html:textarea rows="5" cols="60" property="reason"/></td>
  </tr>
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
<p align="center"><html:submit styleClass="text" value="Process"/></P>
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
    <td class="tablebody"><a href="ViewOrderClones.do?type=<%=Constants.ORDER_CLONE%>&orderid=<bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/>"><bean:write name="<%=Constants.CLONEORDER%>" property="numofclones"/></a></td>
    <td align="right" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforclones"/></td>
  </tr> 
   <tr> 
    <td class="tablebody">Number of collections:</td>
    <td class="tablebody"><a href="ViewOrderCollections.do?orderid=<bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/>"><bean:write name="<%=Constants.CLONEORDER%>" property="numofcollection"/></a></td>
    <td align="right" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforcollection"/></td>
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
</html>
