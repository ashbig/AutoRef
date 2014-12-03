<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css"><link href="layout.css" rel="stylesheet" type="text/css" />
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>

<div class="gridContainer clearfix">


<body>
<jsp:include page="signinMenuBar.jsp" />
<table width="100%" border="0" align="left" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
	<%-- <jsp:include page="addEmtamembersTitle.jsp" /> --%>
      <html:form action="AddEmtaMembers.do">
<html:errors/>
<p class='mainbodytexthead'>Add An Existing Institutions To Our Expedited MTA Network</p>
<p class='mainbodytext'>(Do this only once a signed Expedited Process Agreement is on file.)</p> 
      <table width="100%" height="118" border="0" align="center">
        <tr> 
            <td height="10" colspan="2" class="tableheader"><strong>Please enter institution names (separate each institution with new line)</strong></td>
        </tr>
        <tr> 
          <td height="100">&nbsp; </td>
          <td height="100"> <html:textarea property="institutions" cols="30" rows="5"/> 
          </td>
        </tr>
	<tr>
          <td>&nbsp;</td>
          <td class="text"><html:submit value="Submit"/></td>
	</tr>
      </table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>


