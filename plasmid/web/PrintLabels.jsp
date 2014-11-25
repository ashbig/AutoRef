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
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
        <%--<jsp:include page="labelPlatesTitle.jsp" />--%>
<html:errors/>
<logic:present name="<%=Constants.PRINT_LABEL_MESSAGE%>">
<p class="text"><bean:write name="<%=Constants.PRINT_LABEL_MESSAGE%>"/>
</logic:present>

      <html:form action="PrintLabels.do">
<table width="100%" border="0">
  <tr> 
    <td colspan="2" class="text">Here are the labels:</td>
    <td width="56%">&nbsp;</td>
  </tr>
  <% int i=0; %>
  <logic:iterate name="<%=Constants.LABELS%>" id="label">
  <tr> 
    <td width="4%">&nbsp;</td>
    <td width="40%" class="itemtext"><bean:write name="label"/></td>
    <td>&nbsp;</td>
  </tr>
  <input type="hidden" name='<%="label["+i+"]"%>' value="<bean:write name="label"/>">
  <% i++; %>
  </logic:iterate>
  <tr> 
    <td width="4%">&nbsp;</td>
    <td width="40%" class="itemtext">
        <html:submit value="Print Labels"/>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</div>
</html>

