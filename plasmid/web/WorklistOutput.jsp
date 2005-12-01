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
</head>

<body>
<jsp:include page="internalHomeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
        <jsp:include page="generateWorklistTitle.jsp" />
<html:errors/>
      <html:form action="WorklistInput.do">

<table width="100%" border="0">
  <tr> 
    <td width="20%" class="formlabel">Process:</td>
    <td width="80%" colspan="2" class="itemtext" ><bean:write name="generateWorklistForm" property="processname"/></td>
  </tr>
  <tr> 
    <td width="20%" class="formlabel">Protocol:</td>
    <td colspan="2" class="itemtext"><bean:write name="generateWorklistForm" property="protocol"/></td>
  </tr>
  <tr> 
    <td width="20%" valign="top" class="formlabel">Source containers:</td>
    <td colspan="2" class="itemtext">
        <logic:iterate name="srcLabels" id="srcLabel">
            <bean:write name="srcLabel"/><br>
        </logic:iterate>
    </td>
  </tr>
  <tr> 
    <td width="20%" valign="top" class="formlabel">Destination containers:</td>
    <td colspan="2" class="itemtext">
        <logic:iterate name="destLabels" id="destLabel">
            <bean:write name="destLabel"/><br>
        </logic:iterate>
    </td>
  </tr>
  <tr> 
    <td width="20%" class="formlabel">Volumn (in microliter):</td>
    <td colspan="2" class="itemtext">
        <bean:write name="generateWorklistForm" property="volumn"/>
    </td>
  </tr>
  <tr> 
    <td colspan="3" width="20%" class="formlabel">The following files have been created and emailed to you:</td>
  </tr>
  <tr> 
    <td width="20%"></td>
    <td colspan="2" class="itemtext">
        <logic:iterate name="filenames" id="filename">
            <bean:write name="filename"/><br>
        </logic:iterate>
    </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

