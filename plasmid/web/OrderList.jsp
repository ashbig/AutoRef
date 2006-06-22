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

<html:form action="ViewOrderHistory.do">
<table width="100%" border="0">
  <tr>
    <td width="14%" class="formlabel">Choose order status:</td>
    <td width="13%">
        <html:select styleClass="itemtext" property="status">
            <html:option value="<%=CloneOrder.ALL%>"/>
            <html:option value="<%=CloneOrder.PENDING%>"/>
            <html:option value="<%=CloneOrder.PENDING_MTA%>"/>
            <html:option value="<%=CloneOrder.INPROCESS%>"/>
            <html:option value="<%=CloneOrder.SHIPPED%>"/>
            <html:option value="<%=CloneOrder.CANCEL%>"/>
        </html:select>
    </td>
    <td width="73%">
        <html:submit value="Display" styleClass="itemtext"/>
    </td>
  </tr>
</table>
</html:form>

<html:form action="ChangeOrderStatus.do">

<table width="100%" border="0">
  <tr>
    <td width="9%" class="tableheader">Order ID</td>
    <td width="15%" class="tableheader">Order Date</td>
    <td width="15%" class="tableheader">Status</td>
    <td width="9%" class="tableheader">Number of Clones</td>
    <td width="9%" class="tableheader">Number of Collections</td>
    <td width="9%" class="tableheader">Total Price</td>
    <td width="14%" class="tableheader">User</td>
    <td width="20%" class="tableheader">User Email</td>
  </tr>

  <% int i=0; %>
  <logic:iterate id="order" name="<%=Constants.ORDERS%>"> 
  <tr>
    <td class="tableinfo"><a href="ViewOrderDetail.do?orderid=<bean:write name="order" property="orderid"/>"><bean:write name="order" property="orderid"/></a></td>
    <td class="tableinfo"><bean:write name="order" property="orderDate"/></td>
    <logic:equal name="order" property="beforeInprocess" value="-1">
    <td class="tableinfo">
        <select styleClass="itemtext" name='<%="status["+(i)+"]"%>' value="<bean:write name="order" property="status"/>">
            <logic:equal name="order" property="status" value="<%=CloneOrder.PENDING%>">
            <option value="<%=CloneOrder.PENDING%>" selected/><%=CloneOrder.PENDING%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.PENDING%>">
            <option value="<%=CloneOrder.PENDING%>"/><%=CloneOrder.PENDING%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.PENDING_MTA%>">
            <option value="<%=CloneOrder.PENDING_MTA%>" selected/><%=CloneOrder.PENDING_MTA%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.PENDING_MTA%>">
            <option value="<%=CloneOrder.PENDING_MTA%>"/><%=CloneOrder.PENDING_MTA%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.INPROCESS%>">
            <option value="<%=CloneOrder.INPROCESS%>" selected/><%=CloneOrder.INPROCESS%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.INPROCESS%>">
            <option value="<%=CloneOrder.INPROCESS%>" /><%=CloneOrder.INPROCESS%>
            </logic:notEqual>
        </select>
    </td>
    </logic:equal>
    <logic:notEqual name="order" property="beforeInprocess" value="-1">
        <td class="tableinfo"><bean:write name="order" property="status"/></td>
    </logic:notEqual>
    <input type="hidden" name='<%="orderid["+(i)+"]"%>' value="<bean:write name="order" property="orderid"/>">

    <td class="tableinfo"><bean:write name="order" property="numofclones"/></td>
    <td class="tableinfo"><bean:write name="order" property="numofcollection"/></td>
    <td class="tableinfo"><bean:write name="order" property="totalPriceString"/></td>
    <td class="tableinfo"><bean:write name="order" property="name"/></td>
    <td class="tableinfo"><bean:write name="order" property="email"/></td>
  </tr>
  <% i++; %>
  </logic:iterate>    
    
  <tr>
    <td align="middle" colspan="6">
        <html:submit property="orderListButton" styleClass="itemtext" value="Process Orders"/>&nbsp;&nbsp;
        <html:submit property="orderListButton" styleClass="itemtext" value="<%=Constants.BUTTON_CREATE_INVOICE%>"/>
    </td>
  </tr>
</table>
</html:form>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
<HEAD>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</HEAD>
</html>
