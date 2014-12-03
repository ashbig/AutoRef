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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="signinMenuBar.jsp" />
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
        <%--<jsp:include page="loginTitle.jsp" />--%>
      <html:form action="MergeCart.do">
<p class="text">You have a saved shopping cart with items in your account. Please choose from the following:
<table width="100%" border="0">
  <tr> 
    <td height="21" colspan="2" class="itemtext"> 
    <html:radio property="merge" value="merge"/>
      Add existing shopping cart items to the save shopping cart</td>
  </tr>
  <tr> 
    <td height="24" colspan="2" class="itemtext"> 
    <html:radio property="merge" value="discartSavedCart"/>
      Discart saved shopping cart items</td>
  </tr>
  <tr> 
    <td height="21" colspan="2" class="itemtext"> 
    <html:radio property="merge" value="discartCurrentCart"/>
      Discart current shopping cart items</td>
  </tr>
  <tr> 
    <td width="8%" height="41">&nbsp; </td>
    <td width="92%"><html:submit styleClass="itemtext" value="Continue"/></td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</div>
</html>

