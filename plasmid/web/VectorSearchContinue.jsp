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
</head>

<body>
<jsp:include page="orderTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="searchByVectorTitle.jsp" />
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

<table width="100%" border="0">
  <tr> 
    <td colspan="6" class="tableheader">Limit your search result by choosing the 
      vectors that you want to display</td>
  </tr>
  <logic:iterate name="vectorSearchForm" property="vectornames" id="vector" indexId="i">
  <% if(Integer.parseInt(i.toString())>0 && Integer.parseInt(i.toString())%3 == 0) {%>
  </tr>
  <% } %>
  <% if(Integer.parseInt(i.toString())%3 == 0) {%>
  <tr> 
    <td>&nbsp;</td>
  <% } %>
    <td class="underbullet"> <html:checkbox property='<%="vectornameBoolean["+i+"]"%>'/>
      <bean:write name="vector"/> </td>
  </logic:iterate>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>

  <tr> 
    <td>&nbsp;</td>
<td><html:submit property="display" value="<%=Constants.BUTTON_DISPLAY%>"/></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>

      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

