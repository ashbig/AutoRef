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
	<jsp:include page="batchOrderTitle.jsp" />
<html:errors/>
      <html:form action="BatchOrder.do" enctype="multipart/form-data">
      <table width="100%" height="118" border="0" align="center">
        <tr> 
          <td height="10" colspan="6" class="tableheader"><strong>Select the type of clone identifier</strong></td>
        </tr>
        <tr> 
          <td width="3%" height="26">&nbsp;</td>
          <td height="26" colspan="2" class="text"> <html:select property="cloneType"> 
            <html:option value="<%=Constants.BATCH_ORDER_PLASMIDID%>"/> 
            <html:option value="<%=Constants.BATCH_ORDER_FLEXID%>"/> 
            </html:select> </td>
          <td height="26" colspan="2">&nbsp;</td>
          <td width="24%" height="26">&nbsp;</td>
        </tr>
        <tr> 
          <td height="9" colspan="6">&nbsp;</td>
        </tr>
        <tr> 
          <td height="10" colspan="6" class="tableheader"><strong>Upload the file</strong>
          </td>
        </tr>
        <tr> 
          <td height="26">&nbsp; </td>
          <td height="26" colspan="4" class="text"> <html:file property="orderFile"/> 
            [<a href="batchOrderExample.txt" target="_blank">example</a>]
          </td>
          <td height="26">&nbsp;</td>
        </tr>
    </table>
    <table>
	<tr>
          <td width="3%" height="26">&nbsp;</td>
		  <td align="center">
              <html:submit value="Submit"/>
           </td>
	</tr>
      </table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
</html>

