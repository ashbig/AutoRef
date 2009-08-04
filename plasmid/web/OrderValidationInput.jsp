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
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
<link href="plasmidstyle.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="homeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="menuHome.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
	<jsp:include page="registrationTitle.jsp" />
      <html:form action="OrderValidationInput.do">
<p class="homeMainText">Please enter the following information. (* indicates required 
  field) </p>
<html:errors/>
<table width="100%" border="0">
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Validation Method:</td>
    <td width="70%" align="left" valign="baseline"> 
                <html:select property="method" styleClass="text">
                    <html:option value="">-- Please Select --</html:option>
                    <html:options name="methods"/>
                </html:select>   
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Source:</td>
    <td width="70%" align="left" valign="baseline"> 
        <html:text maxlength="50" property="source" size="30" styleClass="text"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Comments:</td>
    <td width="70%" align="left" valign="baseline"> 
        <html:textarea cols="50" rows="10" property="comments" styleClass="text"/>
    </td>
  </tr>
</table>

<p>Please enter the validation results:</p>
<table width="100%" border="0">
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Validation Method:</td>
    <td width="70%" align="left" valign="baseline"> 
                <html:select property="method" styleClass="text">
                    <html:option value="">-- Please Select --</html:option>
                    <html:options name="methods"/>
                </html:select>   
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Source:</td>
    <td width="70%" align="left" valign="baseline"> 
        <html:text maxlength="50" property="source" size="30" styleClass="text"/>
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Comments:</td>
    <td width="70%" align="left" valign="baseline"> 
        <html:textarea cols="50" rows="10" property="comments" styleClass="text"/>
    </td>
  </tr>
</table>

<table>
  <tr class="formlabel"> 
    <td width="30%" align="right" valign="baseline">
        <html:submit styleClass="text" value="Register"/>
    </td>
    <td width="70%" align="left" valign="baseline">
        <html:reset styleClass="text" value="Clear"/>
    </td>
  </tr>
</table>
  
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" /></body>
<HEAD>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Expires" CONTENT="-1">
</HEAD>
</html>

