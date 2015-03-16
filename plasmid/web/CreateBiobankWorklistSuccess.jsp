<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="plasmid.Constants" %> 
<%@ page import="plasmid.coreobject.Clone" %> 

<html>
<head>
<title>PlasmID Database</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
 <link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
        <link href="boilerplate.css" rel="stylesheet" type="text/css" />
        <script type="text/javascript" src="respond.min.js"></script>
</head>
     <div class="gridContainer clearfix">


<body>
<jsp:include page="signinMenuBar.jsp" />
<table id='content' width="100% height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
	<%--<jsp:include page="orderHistoryTitle.jsp" />--%>

<p class="text">
The following files have been created in the network drive:
</P>
<p class="text">
<logic:iterate name="files" id="file">
<bean:write name="file"/><br>
</logic:iterate>
</P>

    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
     </div>
</html>
