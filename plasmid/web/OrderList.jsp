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

<logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
<html:form action="ViewOrderHistory.do">
<table width="100%" border="0">
  <tr>
    <td width="14%" class="formlabel">Choose order status:</td>
    <td width="13%">
        <html:select styleClass="formlabel" property="status">
            <html:option value="<%=CloneOrder.ALL%>"/>
            <html:option value="<%=CloneOrder.PENDING%>"/>
        </html:select>
    </td>
    <td width="73%">
        <html:submit value="Display" styleClass="formlabel"/>
    </td>
  </tr>
</table>
</html:form>
</logic:equal>

<table width="100%" border="0">
  <tr>
    <td width="9%" class="tableheader">Order ID</td>
    <td width="14%" class="tableheader">Order Date</td>
    <td width="12%" class="tableheader">Status</td>
    <td width="17%" class="tableheader">Number of Clones</td>
    <td width="21%" class="tableheader">Number of Collections</td>
    <td width="12%" class="tableheader">Total Price</td>
    
    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
    <td class="tableheader">User</td>
    </logic:equal>
  </tr>
  <logic:iterate id="order" name="<%=Constants.ORDERS%>"> 
  <tr>
    <td class="tableinfo"><a href="ViewOrderDetail.do?orderid=<bean:write name="order" property="orderid"/>"><bean:write name="order" property="orderid"/></a></td>
    <td class="tableinfo"><bean:write name="order" property="orderDate"/></td>
    <td class="tableinfo"><bean:write name="order" property="status"/></td>
    <td class="tableinfo"><bean:write name="order" property="numofclones"/></td>
    <td class="tableinfo"><bean:write name="order" property="numofcollection"/></td>
    <td class="tableinfo"><bean:write name="order" property="price"/></td>

    <logic:equal name="<%=Constants.USER_KEY%>" property="isinternal" value="<%=User.INTERNAL%>">
    <td class="tableinfo"><bean:write name="order" property="name"/></td>
    </logic:equal>
  </tr>
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
</html>
