<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Process" %> 
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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="internalHomeTitle.jsp" />
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
        <%--<jsp:include page="plate96To384Title.jsp" />--%>
<html:errors/>
<logic:present name="sucmesg">
<p class="formlabel"><bean:write name="sucmesg"/></p>
</logic:present>

<p class="alert">Please note that the layout of the plates shown below should match the layout of the plates put on the robot deck.</p>

      <html:form action="Plate96To384Input.do">

<table width="100%" border="0">
    <tr>
        <td colspan="3" class="formlabel">Please enter the 96-well source plate barcodes:</td>
    <tr>
    <tr>
        <td width="20%">&nbsp;</td>
        <td width="20%"><html:text property="plateA" styleClass="itemtext"/></td>
        <td align="left"><html:text property="plateB" styleClass="itemtext"/></td>
    <tr>
    <tr>
        <td width="20%">&nbsp;</td>
        <td width="20%"><html:text property="plateC" styleClass="itemtext"/></td>
        <td align="left"><html:text property="plateD" styleClass="itemtext"/></td>
    <tr>
    <tr>
        <td colspan="3" class="formlabel">Please enter the 384-well destination plate barcode:</td>
    <tr>
    <tr>
        <td width="20%">&nbsp;</td>
        <td colspan="2"><html:text property="plate" styleClass="itemtext"/></td>
    <tr>
    <tr>
        <td width="20%">&nbsp;</td>
        <td colspan="2"><html:submit styleClass="itemtext" value="Generate Archive Stock"/></td>
    <tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</div>
</html>

