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
        <jsp:include page="sampleTrackingHomeTitle.jsp" />
<html:errors/>

<ul>
    <li class="text"><a href="LabelPlates.jsp">Create New Container Labels</a>
    <li class="text"><a href="SelectProcess.jsp">Generate Worklist</a>
    <li class="text"><a href="GetResultTypes.do">Enter Results</a>
    <li class="text"><a href="FindClones.jsp">Find Clones</a>
    <li class="text"><a href="Plate96To384Input.jsp">Generate Condensed Archive Plates</a>
    <li class="text"><a href="Plate384To96Input.jsp">Reverse Plate Condensation</a>
</ul>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

