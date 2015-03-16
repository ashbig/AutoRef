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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="internalHomeTitle.jsp" />
<table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
        <%--<jsp:include page="enterResultsTitle.jsp" />--%>
<html:errors/>
<logic:present name="message">
   <p class="text"><bean:write name="message"/>
</logic:present>
      <html:form action="SelectResultType.do">
<table width="100%" border="0">
  <tr> 
    <td width="20%" class="formlabel">Please select the result type:</td>
    <td colspan="2">
        <html:select property="resultType" styleClass="itemtext">
          <html:options name="<%=Constants.RESULTTYPES%>"/>
        </html:select>
    </td>
  </tr>
  <tr> 
    <td width="20%" class="formlabel">&nbsp;</td>
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

