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
      <html:form action="VectorSearch.do" enctype="multipart/form-data">

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
      <td width="96%"><input type="submit" name="Submit3" value="Display Results"></td>
    </tr>
  </table>
  </form>
<table width="100%" border="0">
  <tr> 
    <td colspan="6" class="tableheader">Limit your search result by choosing the 
      vectors that you want to display</td>
  </tr>
  <tr> 
    <td width="3%">&nbsp;</td>
    <td width="19%" class="underbullet"> <input type="checkbox" name="checkbox32" value="checkbox">
      pDONR201 </td>
    <td width="18%" class="underbullet"> <input type="checkbox" name="checkbox322" value="checkbox">
      pDONR221</td>
    <td width="19%" class="underbullet"> <input type="checkbox" name="checkbox323" value="checkbox">
      pDNR-Dual</td>
    <td width="21%" class="underbullet">&nbsp;</td>
    <td width="20%" class="underbullet">&nbsp;</td>
  </tr>
  <tr> 
    <td>&nbsp;</td>
    <td class="underbullet"> <input type="checkbox" name="checkbox326" value="checkbox">
      pLKO.1</td>
    <td class="underbullet"> <input type="checkbox" name="checkbox324" value="checkbox">
      pBY011</td>
    <td colspan="3" class="underbullet"> <input type="checkbox" name="checkbox325" value="checkbox">
      pGEX2tk-Cre</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

