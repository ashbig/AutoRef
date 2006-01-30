<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

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
      <html:form action="RefseqSearch.do" enctype="multipart/form-data">
        <table width="100%" border="0" align="center">
          <tr> 
            <td height="10" colspan="3" valign="middle" class="tableheader"><strong>Select 
              species</strong></td>
          </tr>
          <tr> 
            <td width="3%" height="26">&nbsp;</td>
            <td width="31%" height="26"> <html:select property="species"> 
                <html:options name="species"/> </html:select> </td>
            <td width="66%" height="26">&nbsp;</td>
          </tr>
          </table>
<p>
<table width="100%" border="0">
  <tr> 
    <td colspan="6" class="tableheader">Please choose vector type</td>
  </tr>
  <% int i=0; %>
  <logic:iterate name="types" id="type">
  <% if(i>0 && i%3 == 0) {%>
  </tr>
  <% } %>
  <% if(i%3 == 0) {%>
  <tr> 
    <td>&nbsp;</td>
  <% } %>
    <td class="underbullet"> <input type="checkbox" name="checkbox32" value="checkbox">
      <bean:write name="type"/> </td>
  <% i++; %>
  </logic:iterate>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>

  <tr> 
    <td>&nbsp;</td>
<td><input type="submit" name="Submit" value="Search"></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

