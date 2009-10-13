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
<jsp:include page="homeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="platinumServiceTitle.jsp" />
        
<html:errors/>

<p>
<table width="100%" border="0">
  <tr> 
    <td width="20%" class="formlabel">Validation Status:</td>
    <td class="text"><bean:write name="<%=Constants.CLONEORDER%>" property="platinumServiceStatus"/> </td>
  </tr>
</table>

<p class="homeMainText">Validation History</p>
  
<logic:iterate name="<%=Constants.CLONEORDER%>" property="clones" id="c">
<table width="100%" border="0">
  <tr>
    <td width="20%" class="formlabel">PlasmID Clone ID: </td>
    <td class="text"><bean:write name="c" property="clone.name"/></td>
  </tr>
</table>

<logic:equal name="c" property="hasValidations" value="1">
<table width="100%" border="1">
  <tr>
    <td class="tableheader">Sequence</td>
    <td class="tableheader">Validation Result</td>
    <td class="tableheader">Validation Method</td>
    <td class="tableheader">Researcher</td>
    <td class="tableheader">Date</td>
  </tr>
  
  <logic:iterate name="c" property="validations" id="v">
  <tr class="tableinfo"> 
    <td><bean:write name="v" property="sequence"/></td>
    <td><bean:write name="v" property="result"/></td>
    <td><bean:write name="v" property="method"/></td>
    <td><bean:write name="v" property="who"/></td>
    <td><bean:write name="v" property="when"/></td>
  </logic:iterate>
</table>
</logic:equal>
<logic:equal name="c" property="hasValidations" value="0">
            <p class="text">No validation history.</p>
</logic:equal>
</logic:iterate>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
<HEAD>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</HEAD>
</html>

