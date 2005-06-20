<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="orderTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="orderHistoryTitle.jsp" />

<logic:present name="ordermessage">
<p class="text"><bean:write name="ordermessage"/></P>
</logic:present>

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
    <td class="formlabel">PO Number:</td>
    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="ponumber"/></td>
  </tr>
  <tr> 
    <td class="formlabel">Email:</td>
    <td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="email"/></td>
    <td class="formlabel">Phone:</td>
    <td class="text"><bean:write name="<%=Constants.USER_KEY%>" property="phone"/></td>
  </tr>
</table>
<p>&nbsp;</p>
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
    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="shippingAddress"/></td>
    <td class="formlabel">Address:</td>
    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="billingAddress"/></td>
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
    <td class="tablebody"><a href="ViewOrderClones.do?orderid=<bean:write name="<%=Constants.CLONEORDER%>" property="orderid"/>"><bean:write name="<%=Constants.CLONEORDER%>" property="numofclones"/></a></td>
    <td align="right" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforclones"/></td>
  </tr> 
   <tr> 
    <td class="tablebody">Number of collections:</td>
    <td class="tablebody"><bean:write name="<%=Constants.CLONEORDER%>" property="numofcollection"/></td>
    <td align="right" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforcollection"/></td>
  </tr> 
   <tr> 
    <td class="tablebody">Shipping and handling:</td>
    <td align="right" colspan="2" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="costforshipping"/></td>
  </tr> 
  <tr> 
    <td class="tableheader">Total price:</td>
    <td align="right" colspan="2" class="tablebody">$<bean:write name="<%=Constants.CLONEORDER%>" property="price"/></td>
  </tr>
</table>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>