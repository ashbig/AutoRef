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

<table width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td> 
    <td width="83%" align="left" valign="top">
	<jsp:include page="viewContainersTitle.jsp" />--%>
      <html:form action="ViewContainers.do">
<html:errors/>
      <table width="100%" height="118" border="0" align="center">
        <tr> 
            <td height="10" colspan="2" class="tableheader"><strong>Please enter container labels (separate labels with white space or new line)</strong></td>
        </tr>
        <tr> 
          <td height="100">&nbsp; </td>
          <td height="100"> <html:textarea property="labelString" cols="30" rows="5"/> 
          </td>
        </tr>
	<tr>
          <td>&nbsp;</td>
          <td class="text"><html:submit value="Find Containers"/></td>
	</tr>
      </table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</div>
</html>

