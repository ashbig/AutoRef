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

<table width="100%" border="0">
  <tr> 
    <td width="25%" class="formlabel">Result type:</td>
    <td colspan="2" class="itemtext"><bean:write name="enterResultsForm" property="resultType"/></td>
  </tr>
  <tr> 
    <td width="25%" class="formlabel">Result file:</td>
    <td colspan="2" class="itemtext"><bean:write name="enterResultsForm" property="resultFile.fileName" /></td>
  </tr>
  <tr> 
    <td width="25%" class="formlabel">Container:</td>
    <td colspan="2" class="itemtext"><bean:write name="enterResultsForm" property="label" /></td>
  </tr>
</table>
      
<html:form action="ConfirmResults.do">
<p>
<table width="100%" border="0">
  <tr>
    <td class="tableheader">Position</td>
    <td class="tableheader">Well</td>
    <td class="tableheader">Type</td>
    <td class="tableheader">Result</td>
  </tr>
  <logic:iterate name="enterResultsForm" property="container.samples" 
    id="curSample" indexId="i" type="plasmid.coreobject.Sample">
  <tr class="tableinfo">
    <td width="25%" class="itemtext"><bean:write name="curSample" property="position"/></td>
    <td width="25%" class="itemtext"><bean:write name="curSample" property="well"/></td>
    <td width="25%" class="itemtext"><bean:write name="curSample" property="type"/></td>
    <td width="25%" class="itemtext"><bean:write name="enterResultsForm" property='<%="result["+ i +"]" %>'/></td>
  </tr> 
  </logic:iterate>
  <tr>
    <td width="25%" class="formlabel">&nbsp;</td>
    <td colspan="2">
        <html:submit styleClass="itemtext" value="Submit"/>
    </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

