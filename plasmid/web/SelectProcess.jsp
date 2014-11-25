<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Process" %> 

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
        <%--<jsp:include page="generateWorklistTitle.jsp" />--%>
<html:errors/>
      <html:form action="SelectProcess.do">
<table width="100%" border="0">
  <tr> 
    <td width="16%" class="formlabel">Please select the process:</td>
    <td colspan="2">
        <html:select property="processname" styleClass="itemtext">
          <html:option value="<%=Process.CULTURE%>"><%=Process.CULTURE%></html:option>
          <html:option value="<%=Process.GENERATE_GLYCEROL%>"><%=Process.GENERATE_GLYCEROL%></html:option>
          <html:option value="<%=Process.GENERATE_MULTIPLE_GLYCEROL%>"><%=Process.GENERATE_MULTIPLE_GLYCEROL%></html:option>
        </html:select>
    </td>
  </tr>
  <tr> 
    <td width="16%" class="formlabel">&nbsp;</td>
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
</div>
</html>

