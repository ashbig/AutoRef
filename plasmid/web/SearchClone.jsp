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
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>--%>
    <td width="83%" align="left" valign="top">
	<%--<jsp:include page="searchByCloneTitle.jsp" />--%>
      <html:form action="SearchClone.do">
      <table width="100%" height="118" border="0" align="center">
        <tr> 
          <td height="10" colspan="6" class="tableheader"><strong>Select search 
            type</strong></td>
        </tr>
        <tr> 
          <td width="3%" height="26">&nbsp;</td>
          <td height="26" colspan="2"> <html:select property="searchType"> 
            <html:option value="<%=Constants.CLONE_SEARCH_PLASMIDCLONEID%>"/> 
            <html:option value="<%=Constants.CLONE_SEARCH_OTHERCLONEID%>"/> 
            </html:select> </td>
          <td height="26" colspan="2">&nbsp;</td>
          <td width="24%" height="26">&nbsp;</td>
        </tr>
        <tr> 
          <td height="9" colspan="6">&nbsp;</td>
        </tr>
        <tr> 
          <td height="10" colspan="6" class="tableheader"><strong>Enter the search terms below (maximum of 100).
            Use space or start a new line to separate terms.</strong></td>
        </tr>
        <tr> 
          <td height="100">&nbsp; </td>
          <td height="100" colspan="4"> <html:textarea property="searchString" cols="30" rows="5"/> 
          </td>
          <td height="100">&nbsp;</td>
        </tr>
    </table>
    <table>
	<tr>
          <td width="3%" height="26">&nbsp;</td>
		  <td align="center">
              <html:submit value="Search"/>
           </td>
	</tr>
      </table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</div>
</html>

