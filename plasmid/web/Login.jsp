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
<jsp:include page="loginTitle.jsp" />
<table width="800" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="registrationTitle.jsp" />
      <html:form action="Login.do">
<logic:present name="registrationSuccessful">
<bean:message key="registration.successful"/>
</logic:present>
<table width="100%" border="0">
  <tr> 
    <td width="12%" valign="baseline" class="formlabel">Email:</td>
    <td colspan="2">
        <input name="textfield" type="text" class="text" size="30">
    </td>
  </tr>
  <tr> 
    <td valign="baseline" class="formlabel">Password:</td>
    <td width="28%">
        <input name="textfield2" type="text" class="text" size="30" maxlength="20">
        <input name="Submit" type="submit" class="text" value="Login">
    </td>
    <td width="60%" class="text">Find Password</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

