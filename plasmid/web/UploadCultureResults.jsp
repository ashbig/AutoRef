<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Container" %> 

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
<jsp:include page="internalHomeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
        <jsp:include page="uploadCultureResultsTitle.jsp" />
<html:errors/>
      <html:form action="UploadCultureResults.do" focus="label" enctype="multipart/form-data">
<table width="100%" border="0">
  <tr>
    <td width="18%" class="formlabel">Container label:</td>
    <td width="47%">
        <html:text property="label" stypleClass="text" size="30"/>
    </td>
    <td width="35%">&nbsp;</td>
  </tr>
  <tr>
    <td class="formlabel">Upload the OD reading file:</td>
    <td>
        <html:file styleClass="text" property="filename" />
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td class="formlabel">Researcher ID:</td>
    <td>
        <html:text property="researcherid" stypleClass="text" size="30"/>
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td height="66">&nbsp;</td>
    <td>
        <html:submit styleClass="text"/>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

