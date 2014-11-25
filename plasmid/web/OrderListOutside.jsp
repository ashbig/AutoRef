<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.CloneOrder" %>
<%@ page import="plasmid.coreobject.User" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
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

<table width="100%" border="0">
  <tr>
    <td class="tableheader">Order ID</td>
    <td class="tableheader">Order Date</td>
    <td class="tableheader">Status</td>
    <td class="tableheader">Platinum Service</td>
    <td class="tableheader">Number of Clones</td>
    <td class="tableheader">Number of Collections</td>
    <td class="tableheader">Total Price</td>
    <td class="tableheader">Action</td>
  </tr>

  <% int i=0; %>
  <logic:iterate id="order" name="<%=Constants.ORDERS%>"> 
  <tr>
    <td class="tableinfo"><a href="ViewOrderDetail.do?orderid=<bean:write name="order" property="orderid"/>"><bean:write name="order" property="orderid"/></a></td>
    <td class="tableinfo"><bean:write name="order" property="orderDate"/></td>
    <td class="tableinfo"><bean:write name="order" property="status"/></td>
    <td class="tableinfo"><bean:write name="order" property="isplatinum"/></td>
    <td class="tableinfo"><bean:write name="order" property="numofclones"/></td>
    <td class="tableinfo"><bean:write name="order" property="numofcollection"/></td>
    <td class="tableinfo"><bean:write name="order" property="totalPriceString"/></td>
    <logic:equal name="order" property="beforeCancell" value="-1">
        <html:form action="CancelOrder.do">
        <html:hidden name="order" property="orderid"/>
        <td class="tableinfo"><html:submit styleClass="itemtext" value="<%=Constants.BUTTON_CANCEL_ORDER%>"/></td>
        </html:form>
    </logic:equal>    
    <logic:notEqual name="order" property="beforeCancell" value="-1">
        <td class="tableinfo">&nbsp;</td>
    </logic:notEqual>
  </tr>
  <% i++; %>
  </logic:iterate>    
</table>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
<HEAD>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</HEAD>
</div>
</html>
