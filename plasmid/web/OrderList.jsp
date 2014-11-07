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

<html:form action="ViewOrderHistory.do?start=1">
<table width="100%" border="0">
  <tr>
    <td width="40%" class="formlabel">Choose order status:
        <html:select styleClass="itemtext" property="status">
            <html:option value="<%=CloneOrder.PENDING%>"/>
            <html:option value="<%=CloneOrder.INPROCESS%>"/>
            <html:option value="<%=CloneOrder.TROUBLESHOOTING%>"/>
            <html:option value="<%=CloneOrder.PARTIALLY_SHIPPED%>"/>
            <html:option value="<%=CloneOrder.ALL_INPROGRESS%>">All In Progress</html:option>>
            <html:option value="<%=CloneOrder.SHIPPED%>"/>
            <html:option value="<%=CloneOrder.CANCEL%>"/>
            <html:option value="<%=CloneOrder.PENDING_MTA%>"/>
            <html:option value="<%=CloneOrder.PENDING_PAYMENT%>"/>
            <html:option value="<%=CloneOrder.PENDING_AQIS%>"/>
            <html:option value="<%=CloneOrder.INVALIDE_PAYMENT%>"/>
        </html:select>
    </td>
    <td width="40" class="formlabel">Sort by:
        <html:select styleClass="itemtext" property="sortby">
            <html:option value="<%=CloneOrder.COL_ORDERID%>">Order ID</html:option>
            <html:option value="<%=CloneOrder.COL_ORDERDATE%>">Order Date</html:option>
            <%--<html:option value="<%=CloneOrder.COL_PLATINUM%>">Platinum Service</html:option>--%>
            <html:option value="<%=CloneOrder.COL_NUMOFCLONES%>">Number of Clones</html:option>
            <html:option value="<%=CloneOrder.COL_USERLASTNAME%>">User</html:option>
            <html:option value="<%=CloneOrder.COL_UPDATEDON%>">Last Updated On</html:option>
            <html:option value="<%=CloneOrder.COL_UPDATEDBY%>">Last Updated By</html:option>
        </html:select>
            <html:radio styleClass="itemtext" property="sorttype" value="<%=Constants.SORT_ASC%>">Asc</html:radio>
            <html:radio styleClass="itemtext" property="sorttype" value="<%=Constants.SORT_DESC%>">Desc</html:radio>
    </td>
    <td width="20">
        <html:submit value="Display" styleClass="itemtext"/>
    </td>
  </tr>
</table>
</html:form>

<html:form action="ChangeOrderStatus.do">

<table width="100%" border="0">
  <tr>
    <td class="tableheader">Order ID</td>
    <td class="tableheader">Order Date</td>
    <td class="tableheader">Status</td>
    <%--<td class="tableheader">Platinum Service</td>--%>
    <td class="tableheader">Number of Clones</td>
    <td class="tableheader">Number of Collections</td>
    <td class="tableheader">Total Price</td>
    <td class="tableheader">User</td>
    <td class="tableheader">User Email</td>
    <td class="tableheader">Last Updated On</td>
    <td class="tableheader">Last Updated By</td>
    <td class="tableheader">Days Since Order</td>
  </tr>

  <% int i=0; %>
  <% int cloneNumber = 0; %>
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
            <logic:equal name="order" property="status" value="<%=CloneOrder.PENDING_AQIS%>">
            <option value="<%=CloneOrder.PENDING_AQIS%>" selected/><%=CloneOrder.PENDING_AQIS%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.PENDING_AQIS%>">
            <option value="<%=CloneOrder.PENDING_AQIS%>"/><%=CloneOrder.PENDING_AQIS%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.PENDING_PAYMENT%>">
            <option value="<%=CloneOrder.PENDING%>" selected/><%=CloneOrder.PENDING_PAYMENT%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.PENDING_PAYMENT%>">
            <option value="<%=CloneOrder.PENDING%>"/><%=CloneOrder.PENDING_PAYMENT%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.TROUBLESHOOTING%>">
            <option value="<%=CloneOrder.TROUBLESHOOTING%>" selected/><%=CloneOrder.TROUBLESHOOTING%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.TROUBLESHOOTING%>">
            <option value="<%=CloneOrder.TROUBLESHOOTING%>"/><%=CloneOrder.TROUBLESHOOTING%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.INPROCESS%>">
            <option value="<%=CloneOrder.INPROCESS%>" selected/><%=CloneOrder.INPROCESS%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.INPROCESS%>">
            <option value="<%=CloneOrder.INPROCESS%>" /><%=CloneOrder.INPROCESS%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.CANCEL%>">
            <option value="<%=CloneOrder.CANCEL%>" selected/><%=CloneOrder.CANCEL%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.CANCEL%>">
            <option value="<%=CloneOrder.CANCEL%>" /><%=CloneOrder.CANCEL%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.PARTIALLY_SHIPPED%>">
            <option value="<%=CloneOrder.PARTIALLY_SHIPPED%>" selected/><%=CloneOrder.PARTIALLY_SHIPPED%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.PARTIALLY_SHIPPED%>">
            <option value="<%=CloneOrder.PARTIALLY_SHIPPED%>" /><%=CloneOrder.PARTIALLY_SHIPPED%>
            </logic:notEqual>
            <logic:equal name="order" property="status" value="<%=CloneOrder.INVALIDE_PAYMENT%>">
            <option value="<%=CloneOrder.PARTIALLY_SHIPPED%>" selected/><%=CloneOrder.INVALIDE_PAYMENT%>
            </logic:equal>
            <logic:notEqual name="order" property="status" value="<%=CloneOrder.INVALIDE_PAYMENT%>">
            <option value="<%=CloneOrder.PARTIALLY_SHIPPED%>" /><%=CloneOrder.INVALIDE_PAYMENT%>
            </logic:notEqual>
        </select>
    </td>
    </logic:equal>
    <logic:notEqual name="order" property="beforeInprocess" value="-1">
        <td class="tableinfo"><bean:write name="order" property="status"/></td>
    </logic:notEqual>
    <input type="hidden" name='<%="orderid["+(i)+"]"%>' value="<bean:write name="order" property="orderid"/>">

    <%--<td class="tableinfo"><bean:write name="order" property="isplatinum"/></td>--%>
    <td class="tableinfo"><bean:write name="order" property="numofclones"/></td>
    <td class="tableinfo"><bean:write name="order" property="numofcollection"/></td>
    <td class="tableinfo"><bean:write name="order" property="totalPriceString"/></td>
    <td class="tableinfo"><bean:write name="order" property="name"/></td>
    <td class="tableinfo"><bean:write name="order" property="email"/></td>
    <td class="tableinfo"><bean:write name="order" property="updatedon"/></td>
    <td class="tableinfo"><bean:write name="order" property="updatedby"/></td>
    <td class="tableinfo"><bean:write name="order" property="dayssinceorder"/></td>

  </tr>
  <% i++; %>
  </logic:iterate>    
    
  <tr>
    <td align="middle" colspan="6">
        <html:submit property="orderListButton" styleClass="itemtext" value="Process Orders"/>&nbsp;&nbsp;
        <html:submit property="orderListButton" styleClass="itemtext" value="<%=Constants.BUTTON_CREATE_INVOICE%>"/>&nbsp;&nbsp;
        <html:submit property="orderListButton" styleClass="itemtext" value="<%=Constants.BUTTON_GENERATE_REPORT%>"/>&nbsp;&nbsp;
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
