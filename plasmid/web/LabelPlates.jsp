<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Container" %> 

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
<jsp:include page="internalHomeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
        <jsp:include page="labelPlatesTitle.jsp" />
<html:errors/>
      <html:form action="LabelPlates.do">
<table width="100%" border="0">
  <tr> 
    <td width="20%" class="formlabel">Choose container type:</td>
    <td colspan="2">
        <html:select property="type" styleClass="itemtext">
          <html:option value="<%=Container.COSTAR_FLT%>"><%=Container.COSTAR_FLT%></html:option>
          <html:option value="<%=Container.COSTAR_RD%>"><%=Container.COSTAR_RD%></html:option>
          <html:option value="<%=Container.GREINER%>"><%=Container.GREINER%></html:option>
          <html:option value="<%=Container.PCR_ON_MP16%>"><%=Container.PCR_ON_MP16%></html:option>
          <html:option value="<%=Container.RESERVOIR_MP16%>"><%=Container.RESERVOIR_MP16%></html:option>
          <html:option value="<%=Container.RESERVOIR_PYR%>"><%=Container.RESERVOIR_PYR%></html:option>
          <html:option value="<%=Container.RK_RIPLATE_DW%>"><%=Container.RK_RIPLATE_DW%></html:option>
          <html:option value="<%=Container.MICRONIC96TUBEMP16%>"><%=Container.MICRONIC96TUBEMP16%></html:option>
        </html:select>
    </td>
  </tr>
  <tr> 
    <td width="20%" class="formlabel">Number of new containers (>0):</td>
    <td colspan="2">
        <html:text property="number" styleClass="itemtext" size="30" value="1"/>
    </td>
  </tr>
  <tr> 
    <td class="formlabel">Add suffix:</td>
    <td width="20%">
        <html:select property="suffix" styleClass="itemtext">
          <html:option value="none">None</html:option>
          <html:option value="crarc">Core Archive (-CrArc)</html:option>
          <html:option value="send">Distribution (-Send)</html:option>
        </html:select>
      </td>
    <td width="64%" class="itemtext">example: 12345678-CrArc</td>
  </tr>
  <tr> 
    <td width="20%" class="formlabel">&nbsp;</td>
    <td colspan="2">
        <html:submit styleClass="itemtext" value="Get Labels"/>
    </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

