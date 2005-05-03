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
      <html:form action="SetDisplay.do" enctype="multipart/form-data">
<input type="hidden" name="displayPage" value="<bean:write name="displayPage"/>">

<table width="100%" height="58" border="0" align="center">
<tr>
    <td class="alertheader">We found <bean:write name="totalCount"/> clones for your search. You can limit your 
      search by choosing the following criteria or view all of them by pressing 
      the button.</td>
  </tr>
  
</table>
<table width="100%" border="0">
  <tr> 
    <td colspan="6" class="tableheader">Choose clone type</td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td width="19%" class="underbullet"><html:checkbox property="cdna"/>
        cDNA </td>
    <td width="18%" class="underbullet"><html:checkbox property="shrna"/>
        shRNA </td>
    <td width="19%" class="underbullet"></td>
    <td width="21%" class="underbullet"></td>
    <td width="20%" class="underbullet"></td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="6" class="tableheader">Choose insert format</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td class="underbullet"><html:checkbox property="fusion"/>
      Fusion </td>
    <td class="underbullet"><html:checkbox property="closed"/>
      Closed </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td colspan="3">&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="6" class="tableheader">Choose selectable marker</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><html:select property="marker">
            <html:options name="markers"/>
        </html:select></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="6" class="tableheader">Choose vector</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td class="underbullet"> <html:checkbox property="pdonr201"/>
      pDONR201 </td>
    <td class="underbullet"> <html:checkbox property="pdonr221"/>
      pDONR221</td>
    <td class="underbullet"> <html:checkbox property="pdnrdual"/>
      pDNR-Dual</td>
    <td class="underbullet">&nbsp;</td>
    <td class="underbullet">&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td class="underbullet"> <html:checkbox property="plk"/>
      pLKO.1</td>
    <td class="underbullet"> <html:checkbox property="pby011"/>
      pBY011</td>
    <td colspan="3" class="underbullet"> <html:checkbox property="pgex2tk"/>
      pGEX2tk-Cre</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td colspan="6" class="tableheader">Choose page size</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td> <html:select property="pagesize"> <html:option value="25">25</html:option> 
            <html:option value="50">50</html:option> <html:option value="75">75</html:option> 
            <html:option value="100">100</html:option> </html:select> </td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td><form name="form4" method="post" action="">
        <input type="submit" name="Submit" value="Display Results">
      </form></td>
    <td>&nbsp;</td>
    <td colspan="3">&nbsp;</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

