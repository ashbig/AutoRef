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
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="orderTitle.jsp" />
<table width="100%" id='content' border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
	<%--<jsp:include page="searchByVectorTitle.jsp" />--%>
      <html:form action="VectorSearchContinue.do">

<table width="100%" height="58" border="0" align="center">
<tr>
    <td class="alertheader"><p>We found <bean:write name="numberOfClones"/> clones for your search. You can limit 
        your search by vector names. To view all the clones, please click &quot;Display 
        Results&quot; button directly.</p>
      </td>
  </tr>  
</table>
  <table width="100%" border="0">
    <tr>
      <td width="4%">&nbsp;</td>
      <td width="96%"><html:submit property="display" value="<%=Constants.BUTTON_DISPLAY_ALL%>"/></td>
    </tr>
  </table>

<table width="100%" border="1">
  <tr> 
    <td colspan="2" class="tableheader">Limit your search result by choosing the 
      vectors that you want to display</td>
  </tr>
  <logic:iterate name="vectorSearchForm" property="vectors" id="vector" indexId="i">
  <tr>
    <td class="underbullet"> <html:checkbox property='<%="vectornameBoolean["+i+"]"%>'/>
    <a target="_blank" href="GetVectorDetail.do?vectorid=<bean:write name="vector" property="vectorid"/>">
      <bean:write name="vector" property="name"/></a> </td>
    <td class="underbullet"><bean:write name="vector" property="description"/> </td>
  </logic:iterate>
  </tr>
</table>

<table>
  <tr> 
    <td>&nbsp;</td>
    <td><html:submit property="display" value="<%=Constants.BUTTON_DISPLAY%>"/></td>
  </tr>
</table>

      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</div>
</html>

