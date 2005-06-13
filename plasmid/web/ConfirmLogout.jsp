<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
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
        <jsp:include page="logoutTitle.jsp" />
      <html:form action="ConfirmLogout.do">
<p class="text">
You have modified your shopping cart. Would you like to save the changes to your account?
<table width="100%" border="0">
    <tr>
        <td class="text"><html:submit property="confirm" value="Yes"/></td>
        <td class="text"><html:submit property="confirm" value="No"/></td>
    </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

