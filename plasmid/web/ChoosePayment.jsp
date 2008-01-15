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
<p class="text"><i>
<b>Pay Using a Credit Card at PayPal</b><br>

Please note that you do not need to create a PayPal account to pay by credit card:  see the bottom right-hand side of the PayPal page for a link to pay by credit card without signing in or registering.

Click the following button to pay by PayPal (you will then be returned to PlasmID to complete your request).
</p>
<html:errors/>
<form action="https://www.sandbox.paypal.com/us/cgi-bin/webscr" method="post">
<input type="hidden" name="cmd" value="_xclick">
<input type="hidden" name="invoice" value="<bean:write name="checkoutForm" property="orderid"/>">
<input type="hidden" name="no_shipping" value="1">
<input type="hidden" name="business" value="dzuo_1198005536_biz@hms.harvard.edu">
<input type="hidden" name="item_name" value="PlasmID Order: <bean:write name="checkoutForm" property="orderid"/>">
<input type="hidden" name="currency_code" value="USD">
<input type="hidden" name="amount" value="<bean:write name="checkoutForm" property="totalPrice"/>">
<input type="hidden" name="return" value="http://dev.plasmid.med.harvard.edu/PLASMID/ViewOrderDetail.jsp">
<input type="hidden" name="notify_url" value="http://dev.plasmid.med.harvard.edu/PLASMID/PlaceOrder.do">
<input type="hidden" name="cancel_return" value="http://dev.plasmid.med.harvard.edu/PLASMID/CancelOrderPaypal.do">
<input type="image" src="http://www.paypal.com/en_US/i/btn/x-click-but01.gif" name="submit" alt="Make payments with PayPal - it's fast, free and secure!">
</form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>
