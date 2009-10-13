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
	<jsp:include page="platinumServiceTitle.jsp" />
      <html:form action="OrderValidationInput.do">
<p class="homeMainText">Please enter the following information.</p>
<html:errors/>
<table width="100%" border="0">
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Validation Method:</td>
    <td width="70%" align="left" valign="baseline"> 
                <html:select property="method" styleClass="text">
                    <html:option value="">-- Please Select --</html:option>
                    <html:options name="validationMethods"/>
                </html:select>   
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Validation Status:</td>
    <td width="70%" align="left" valign="baseline"> 
                <html:select property="status" styleClass="text">
                    <html:option value="">-- Please Select --</html:option>
                    <html:options name="validationStatus"/>
                </html:select>   
    </td>
  </tr>
  <tr class="formlabel"> 
    <td width="30%" align="left" valign="baseline">Researcher:</td>
    <td width="70%" align="left" valign="baseline" class="text"> 
        <bean:write name="enterPlatinumResultForm" property="researcher" />
    </td>
  </tr>
</table>

<p class="homeMainText">Please enter the validation results:</p>
<table width="100%" border="0">
<% int i=0; %>
<logic:iterate name="<%=Constants.CLONEORDER%>" property="clones" id="clone">
  <tr>
    <td class="tableheader">PlasmID Clone ID:</td>
    <td class="tableheader"><bean:write name="clone" property="clone.name"/></td>
  </tr>
  <tr align="top,top">
    <td class="formlabel">Enter sequence:</td>
    <td class="text"><html:textarea rows="5" cols="50" name="enterPlatinumResultForm" property='<%="sequence["+i+"]"%>'/></td>
  </tr>
  <tr>
    <td class="formlabel">Select validation Result:</td>
    <td class="text"><html:select name="enterPlatinumResultForm" property='<%="result["+i+"]"%>'>
                    <html:option value="">-- Please Select --</html:option>
                    <html:options name="validationResults"/>
                </html:select>   
            </td>
  </tr>
  <tr>
      <td colspan="2" class="formlabel">Validation history:</td>
  </tr>
  <tr>
      <td colspan="2">
          <logic:equal name="clone" property="hasValidations" value="1">
          <table width="100%" border="1">
          <tr>
            <td class="tableheader">Sequence</td>
            <td class="tableheader">Validation Result</td>
            <td class="tableheader">Validation Method</td>
            <td class="tableheader">Researcher</td>
            <td class="tableheader">Date</td>
          </tr>

          <logic:iterate name="clone" property="validations" id="v">
          <tr class="tableinfo"> 
            <td><bean:write name="v" property="sequence"/></td>
            <td><bean:write name="v" property="result"/></td>
            <td><bean:write name="v" property="method"/></td>
            <td><bean:write name="v" property="who"/></td>
            <td><bean:write name="v" property="when"/></td>
          </logic:iterate>
        </table>
        </logic:equal>
        <logic:equal name="clone" property="hasValidations" value="0">
            <p class="text">No validation history.</p>
        </logic:equal>
      </td>
  </tr>
  <tr>
      <td colspan="2">&nbsp;</td>
  </tr>
  <% i++; %>
  </logic:iterate>
</table>

<table>
  <tr class="formlabel"> 
    <td width="30%" align="right" valign="baseline">
        <html:submit styleClass="text" value="Submit"/>
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

