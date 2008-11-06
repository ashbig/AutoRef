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

<html:errors/>

<p class="mainbodytexthead">
              Shipping Options:
              <ol class="mainbodytexthead">
                  <li>Include a FedEx account number at check-out and we will charge your account.</li>
                  <li>If you don't provide a FedEx account number, we will apply a standard shipping charge at check-out.</li>
                  <ul>  
                      <li>$10.00 for domestic orders</li>
                      <li>$20.00 for international orders</li>
                </ul>
              </ol>
</p>
<p class="mainbodytexthead">
          <i>If you do not want to complete your order now, you can save your cart and return to check-out another time (remember to sign in to see your saved cart).</i>
</p>
<p class="mainbodytexthead">
              If you have any questions, please contact <a href="mailto:plasmidhelp@hms.harvard.edu">PlasmID help</a>.   
</p>

<html:form action="EnterAddress.do">

<p class="text">Order Summary:</p>
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
    <td class="tableheader">Total price (before shipping charge):</td>
    <td align="right" colspan="2" class="tablebody">$<bean:write name="checkoutForm" property="totalPrice"/></td>
  </tr>
</table>

<p class="text">Shipping Information:</p>
<table width="100%" border="0">
  <tr> 
    <td class="formlabel" width="20%">Choose shipping method:</td>
    <td class="text">
        <html:select property="shippingMethod">
            <html:options name="shippingMethods"/>
        </html:select>
    </td>
  </tr>
  <tr> 
    <td class="formlabel">Enter shipping account number (ONLY FedEx number. We do not accept other couriers):</td>
    <td class="text">
        <html:text size="50"property="accountNumber"/>
    </td>
  </tr>
  <tr> 
    <td></td>
    <td class="text">
        <html:submit value="Continue"/>
    </td>
  </tr>
</table>
    </html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>
