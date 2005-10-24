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
<jsp:include page="internalHomeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
        <jsp:include page="enterResultsTitle.jsp" />
<html:errors/>
      <html:form action="UploadResultFile.do"  enctype="multipart/form-data">
<table width="100%" border="0">
  <tr> 
    <td width="25%" class="formlabel">Result type:</td>
    <td colspan="2"><bean:write styleClass="itemtext" name="enterResultsForm" property="resultType"/></td>
  </tr>
  <tr> 
    <td width="25%" class="formlabel">Please upload the file:</td>
    <td colspan="2"><html:file styleClass="itemtext" property="resultFile" /></td>
  </tr>
  <tr> 
    <td width="25%" class="formlabel">&nbsp;</td>
    <td colspan="2">
        <html:submit styleClass="itemtext" value="Continue"/>
    </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

