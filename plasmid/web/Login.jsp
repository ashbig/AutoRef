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
        <jsp:include page="loginTitle.jsp" />
      <html:form action="Logon.do">
<logic:present name="registrationSuccessful">
<bean:message key="registration.successful"/>
</logic:present>
<p>
<html:errors/>
<table width="100%" border="0">
  <tr> 
    <td width="12%" valign="baseline" class="formlabel">Email:</td>
    <td>
        <html:text property="email" styleClass="text" size="30"/>
    </td>
    <td valign="baseline" width="60%" class="text"><a href="PrepareRegistration.do">Create New Account</a></td>
  </tr>
  <tr> 
    <td valign="baseline" class="formlabel">Password:</td>
    <td width="28%">
        <html:password property="password" styleClass="text" size="30" maxlength="20"/>
        <html:submit styleClass="text" value="Login"/>
    </td>
    <td valign="baseline" width="60%" class="text"><a href="FindPassword.jsp">Find Password</a></td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

