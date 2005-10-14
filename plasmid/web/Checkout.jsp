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
<table width="800" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="checkoutTitle.jsp" />

      <html:form action="ProcessCheckout.do">
<table width="100%" border="0">
  <tr> 
    <td width="12%" class="formlabel">Name:</td>
    <td colspan="3"><bean:write name="<%=Constants.USER_KEY%>" property="username"/></td>
  </tr>
  <tr> 
    <td width="12%" class="formlabel">Email:</td>
    <td width="40%"><bean:write name="<%=Constants.USER_KEY%>" property="email"/></td>
    <td width="12%" class="formlabel">Phone:</td>
    <td width="36%"><bean:write name="<%=Constants.USER_KEY%>" property="phone"/></td>
  </tr>
  <tr> 
    <td width="12%" height="24" class="formlabel">Order Date:</td>
    <td width="40%"><bean:write name="date"/></td>
    <td width="12%" class="formlabel">PO Number:</td>
    <td width="36%">
        <input name="ponumber" type="text" size="40" value="<bean:write name="<%=Constants.USER_KEY%>" property="ponumber"/>">
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
    <logic:present name="shpping">
    <td><input name="shippingto" type="text" value="<bean:write name="shipping" property="name"/>"></td>
    </logic:present>
    <logic:notPresent name="shpping">
    <td><html:text property="shippingto"/></td>
    </logic:notPresent>
    <td class="formlabel">Name:</td>
    <logic:present name="billing">
    <td><input name="billingto" type="text" value="<bean:write name="billing" property="name"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingto"/></td>
    </logic:notPresent>
  </tr>
  <tr> 
    <td class="formlabel">Organization:</td>
    <logic:present name="shipping">
    <td><input name="organization" type="text" value="<bean:write name="shipping" property="organization"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="organization"/></td>
    </logic:notPresent>
    <td class="formlabel">Organization:</td>
    <logic:present name="billing">
    <td><input name="billingorganization" type="text" value="<bean:write name="billing" property="organization"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingorganization"/></td>
    </logic:notPresent>
  </tr>
  <tr> 
    <td class="formlabel">Street:</td>
    <logic:present name="shipping">
    <td><input name="addressline1" type="text" value="<bean:write name="shipping" property="addressline1"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="addressline1"/></td>
    </logic:notPresent>
    <td class="formlabel">Street:</td>
    <logic:present name="billing">
    <td><input name="billingaddressline1" type="text" value="<bean:write name="billing" property="addressline1"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingaddressline1"/></td>
    </logic:notPresent>
  </tr>
  <tr> 
    <td class="formlabel">Street (continued):</td>
    <logic:present name="shipping">
    <td><input name="addressline2" type="text" value="<bean:write name="shipping" property="addressline2"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="addressline2"/></td>
    </logic:notPresent>
    <td class="formlabel">Street (continued):</td>
    <logic:present name="billing">
    <td><input name="billingaddressline2" type="text" value="<bean:write name="billing" property="addressline2"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingaddressline2"/></td>
    </logic:notPresent>
  </tr>
  <tr> 
    <td class="formlabel">City:</td>
    <logic:present name="shipping">
    <td><input name="city" type="text" value="<bean:write name="shipping" property="city"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="city"/></td>
    </logic:notPresent>
    <td class="formlabel">City:</td>
    <logic:present name="billing">
    <td><input name="billingcity" type="text" value="<bean:write name="billing" property="city"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingcity"/></td>
    </logic:notPresent>
  </tr>
  <tr> 
    <td class="formlabel">State:</td>
    <logic:present name="shipping">
    <td><input name="state" type="text" value="<bean:write name="shipping" property="state"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="state"/></td>
    </logic:notPresent>
    <td class="formlabel">State:</td>
    <logic:present name="billing">
    <td><input name="billingstate" type="text" value="<bean:write name="billing" property="state"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingstate"/></td>
    </logic:notPresent>
  </tr>
  <tr> 
    <td class="formlabel">Zip code:</td>
    <logic:present name="shipping">
    <td><input name="zipcode" type="text" value="<bean:write name="shipping" property="zipcode"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="zipcode"/></td>
    </logic:notPresent>
    <td class="formlabel">Zip code:</td>
    <logic:present name="billing">
    <td><input name="billingzipcode" type="text" value="<bean:write name="billing" property="zipcode"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingzipcode"/></td>
    </logic:notPresent>
  </tr>
  <tr>
    <td class="formlabel">Country:</td>
    <logic:present name="shipping">
    <td><input name="country" type="text" value="<bean:write name="shipping" property="country"/>"></td>
    </logic:present>
    <logic:notPresent name="shipping">
    <td><html:text property="country"/></td>
    </logic:notPresent>
    <td class="formlabel">Country:</td>
    <logic:present name="billing">
    <td><input name="billingcountry" type="text" value="<bean:write name="billing" property="country"/>"></td>
    </logic:present>
    <logic:notPresent name="billing">
    <td><html:text property="billingcountry"/></td>
    </logic:notPresent>
  </tr>
</table>

<p>Order Information:</p>
<table width="100%" border="0">
  <tr> 
    <td width="40%" class="tableheader">Item</td>
    <td width="30%" class="tableheader">Quantity</td>
    <td width="30%" class="tableheader">Price</td>
  </tr>
    <logic:iterate name="items" id="item">
   <tr> 
    <td class="tablebody"><bean:write name="item" property="name"/></td>
    <td class="tablebody"><bean:write name="item" property="quantity"/></td>
    <td class="tablebody"><bean:write name="item" property="price"/></td>
  </tr> 
  </logic:iterate>
  <tr> 
    <td class="tableheader">Total:</td>
    <td class="tableheader"><bean:write name="quantity"/></td>
    <td class="tableheader"><bean:write name="price"/></td>
  </tr>
</table>
  <html:submit value="Check Out">
    </html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>
