<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Clone" %> 

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>
<div class="gridContainer clearfix">
<body>
<jsp:include page="signinMenuBar.jsp" />
<table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
	<%--<jsp:include page="orderHistoryTitle.jsp" />--%>

<p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">&nbsp;</td>
    <td class="tableheader">Collection Name</td>
    <td class="tableheader">Use Restriction</td>
    <td class="tableheader">Price For Member</td>
    <td class="tableheader">Price For Non-Member</td>
    <td class="tableheader">Quantity</td>
  </tr>

  <% int j=0;%>
  <logic:iterate name="orderClones" id="collection">
  <tr class="tableinfo"> 
    <td><%=++j%></td>
    <td><a class="itemtext" href="ViewOrderClones.do?type=<%=Constants.ORDER_COLLECTION%>&orderid=<bean:write name="orderid"/>&collectionName=<bean:write name="collection" property="name"/>"><bean:write name="collection" property="name"/></a></td>
    <td><bean:write name="collection" property="restriction"/></td>
    <td><bean:write name="collection" property="displayMemberPrice"/></td>
    <td><bean:write name="collection" property="displayPrice"/></td>
    <td><bean:write name="collection" property="quantity"/></td>
  </tr>
  </logic:iterate>
</table>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</div>
</html>

