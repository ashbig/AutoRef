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
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="checkoutTitle.jsp" />

<p class="text">Please confirm the following information (use back button to make changes):</P>
<html:errors/>
      <html:form action="PlaceOrder.do">
<table width="100%" border="0">
  <tr> 
    <td width="12%" class="formlabel">Name:</td>
    <td colspan="3" class="text"><bean:write name="<%=Constants.USER_KEY%>" property="username"/></td>
  </tr>
  <tr> 
    <td width="12%" class="formlabel">Email:</td>
    <td width="40%" class="text"><bean:write name="<%=Constants.USER_KEY%>" property="email"/></td>
    <td width="12%" class="formlabel">Phone:</td>
    <td width="36%" class="text"><bean:write name="<%=Constants.USER_KEY%>" property="phone"/></td>
  </tr>
  <tr> 
    <td width="12%" height="24" class="formlabel">Order Date:</td>
    <td width="40%" class="text"><bean:write name="date"/></td>
    <td width="12%" class="formlabel">PO Number:</td>
    <td width="36%" class="text">
        <bean:write name="checkoutForm" property="ponumber"/>
    </td>
  </tr>
</table>
<p>&nbsp;</p>
<table width="100%" border="0">
  <tr> 
    <td colspan="2" class="featuretext">Shipping Address:</td>
    <td colspan="2" class="featuretext">Billing Address:</td>
  </tr>
  <tr> 
    <td width="17%" class="formlabel">Name:</td>
    <td class="text"><bean:write name="checkoutForm" property="shippingto"/></td>
    <td class="formlabel">Name:</td>
    <td class="text"><bean:write name="checkoutForm" property="billingto"/></td>
  </tr>
  <tr> 
    <td class="formlabel">Organization:</td>
    <td class="text"><bean:write name="checkoutForm" property="organization"/></td>
    <td class="formlabel">Organization:</td>
    <td class="text"><bean:write name="checkoutForm" property="billingorganization"/></td>
  </tr>
  <tr> 
    <td class="formlabel">Street:</td>
    <td class="text"><bean:write name="checkoutForm" property="addressline1"/></td>
    <td class="formlabel">Street:</td>
    <td class="text"><bean:write name="checkoutForm" property="billingaddressline1"/></td>
  </tr>
  <tr> 
    <td class="formlabel">Street (continued):</td>
    <td class="text"><bean:write name="checkoutForm" property="addressline2"/></td>
    <td class="formlabel">Street (continued):</td>
    <td class="text"><bean:write name="checkoutForm" property="billingaddressline2"/></td>
  </tr>
  <tr> 
    <td class="formlabel">City:</td>
    <td class="text"><bean:write name="checkoutForm" property="city"/></td>
    <td class="formlabel">City:</td>
    <td class="text"><bean:write name="checkoutForm" property="billingcity"/></td>
  </tr>
  <tr> 
    <td class="formlabel">State:</td>
    <td class="text"><bean:write name="checkoutForm" property="state"/></td>
    <td class="formlabel">State:</td>
    <td class="text"><bean:write name="checkoutForm" property="billingstate"/></td>
  </tr>
  <tr> 
    <td class="formlabel">Zip code:</td>
    <td class="text"><bean:write name="checkoutForm" property="zipcode"/></td>
    <td class="formlabel">Zip code:</td>
    <td class="text"><bean:write name="checkoutForm"  property="billingzipcode"/></td>
  </tr>
  <tr>
    <td class="formlabel">Country:</td>
    <td class="text"><bean:write name="checkoutForm" property="country"/></td>
    <td class="formlabel">Country:</td>
    <td class="text"><bean:write name="checkoutForm" property="billingcountry"/></td>
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
    <td class="tablebody"><bean:write name="checkoutForm" property="numOfClones"/></td>
    <td align="right" class="tablebody">$<bean:write name="checkoutForm" property="costOfClones"/></td>
  </tr> 
   <tr> 
    <td class="tablebody">Number of collections:</td>
    <td class="tablebody"><bean:write name="checkoutForm" property="numOfCollections"/></td>
    <td align="right" class="tablebody">$<bean:write name="checkoutForm" property="costOfCollections"/></td>
  </tr> 
   <tr> 
    <td class="tablebody">Shipping and handling:</td>
    <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="costForShipping"/></td>
  </tr> 
  <tr> 
    <td class="tableheader">Total price:</td>
    <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="totalPrice"/></td>
  </tr>
</table>
<html:hidden property="saveInfo"/>
  <html:submit value="Place Order"/>
    </html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>