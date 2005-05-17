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
<table width="800" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="searchByRefseqTitle.jsp" />
      <html:form action="RefseqSearchContinue.do" enctype="multipart/form-data">
        <html:hidden property="species"/>
      <table width="100%" height="118" border="0" align="center">
        <tr> 
          <td height="10" colspan="6" class="tableheader"><strong>Select search 
            type</strong></td>
        </tr>
        <tr> 
          <td width="3%" height="26">&nbsp;</td>
          <td height="26" colspan="2"> <html:select property="searchType"> <html:options name="nameTypes"/> 
            </html:select> </td>
          <td height="26" colspan="2">&nbsp;</td>
          <td width="24%" height="26">&nbsp;</td>
        </tr>
        <tr> 
          <td height="9" colspan="6">&nbsp;</td>
        </tr>
        <tr> 
          <td height="10" colspan="6" class="tableheader"><strong>Enter space 
            or new line separated term to search (maximum of 100)</strong></td>
        </tr>
        <tr> 
          <td height="100">&nbsp; </td>
          <td height="100" colspan="4"> <html:textarea property="searchString" cols="30" rows="5"/> 
          </td>
          <td height="100">&nbsp;</td>
        </tr>
        <tr> 
          <td height="9" colspan="6">&nbsp;</td>
        </tr>
	<tr>
          <td width="3%" height="26">&nbsp;</td>
	</tr>
	<tr>
          <td width="3%" height="26">&nbsp;</td>
		  <td align="center">
              <input type="submit" name="Submit" value="Search">
           </td>
	</tr>
      </table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

