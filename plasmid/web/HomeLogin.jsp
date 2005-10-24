<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

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
	<jsp:include page="loginTitle.jsp" />
      <html:form action="Logon.do" enctype="multipart/form-data">
<p class="homeMainText">Please log in below if you are a registered user: </p>
<table width="100%" border="0">
  <tr class="formlabel"> 
    <td width="29%" align="right" valign="baseline">Email:</td>
    <td width="71%" align="left" valign="baseline"> 
      <html:text property="useremail" size="30"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="29%" align="right" valign="baseline">Password:</td>
    <td width="71%" align="left" valign="baseline"> 
      <html:text property="password" size="30"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="29%" align="right" valign="baseline">
        <input type="submit" name="Submit" value="Submit">
    </td>
    <td width="71%" align="left" valign="baseline">
        <input type="reset" name="Submit2" value="Reset">
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

