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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="signinMenuBar.jsp" />
<table id='content' width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
	<%--<jsp:include page="searchByRefseqTitle.jsp" />--%>
      <html:form action="RefseqSearch.do" enctype="multipart/form-data">
        <table width="100%" height="118" border="0" align="center">
          <tr> 
            <td height="10" colspan="3" valign="middle" class="tableheader"><strong>Select 
              species</strong></td>
          </tr>
          <tr> 
            <td width="3%" height="26">&nbsp;</td>
            <td width="31%" height="26"> <html:select property="species" style="width: 210px;"> <html:options name="allSpecies"/> </html:select> </td>
            <td width="66%" height="26">&nbsp;</td>
          </tr>
          <tr bgcolor="#FFFFFF"> 
            <td height="10" colspan="3" class="homeMainText"> <p>&nbsp;</p></td>
          </tr>

          <tr bgcolor="#FFFFFF"> 
            <td height="4" colspan="3" class="homeMainText">&nbsp;</td>
          </tr>
          <tr bgcolor="#FFFFFF"> 
            <td height="4" class="homeMainText">&nbsp;</td>
            <td height="4" align="center" class="homeMainText">
            <input type="submit" name="Submit" value="Continue">
            </td>
            <td height="4" class="homeMainText">&nbsp;</td>
          <tr><td>&nbsp;</td></tr>
          </tr>
          <tr><td colspan='3' class='mainbodytexthead' style='color:red;'>If you are looking for Human or Mouse constructs we strongly encourage you to use <a href='GeneSearch.xhtml'>this tool</a> instead.</td></tr>
        </table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</div>
</html>

