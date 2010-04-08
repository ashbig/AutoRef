<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.Constants" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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
	<jsp:include page="registrationTitle.jsp" />
      <html:form action="SearchOrders.do">
<html:errors/>
<table width="100%" border="0">
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Enter order numbers (separate each order by ","):</td>
    <td width="70%" align="left" valign="baseline"> 
        <html:text maxlength="50" property="orderids" size="30" styleClass="text"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="right" valign="baseline">
        <html:submit styleClass="text" value="Continue"/>
    </td>
    <td width="70%" align="left" valign="baseline">
        <html:reset styleClass="text" value="Clear"/>
    </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
<HEAD>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</HEAD>
</html>
