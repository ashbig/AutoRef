<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Process" %> 
<%@ page import="plasmid.Constants" %> 
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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="internalHomeTitle.jsp" />
<table width="100%" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
        <%--<jsp:include page="generateWorklistTitle.jsp" />--%>
<html:errors/>

<p class="alert">Please note that the mapping between the source plates and the destination
plates is the same as your input order.</p>

      <html:form action="MultipleWorklistInput.do">
<input type="hidden" name="processname" value="<bean:write name="generateWorklistForm" property="processname"/>">
<table width="100%" border="0">
  <tr> 
    <td width="30%" class="formlabel">Process:</td>
    <td colspan="2" class="itemtext" ><bean:write name="generateWorklistForm" property="processname"/></td>
  </tr>
  <logic:present name="<%=Constants.PROTOCOLS %>">
  <tr> 
    <td width="30%" height="37" class="formlabel">Select protocol:</td>
    <td colspan="2">
        <html:select property="protocol" styleClass="itemtext">
          <html:options collection="<%=Constants.PROTOCOLS %>" property="name"/>
        </html:select>
    </td>
  </tr>
  </logic:present>
  <tr> 
    <td width="30%" height="91" valign="top" class="formlabel">Source containers: <br>(separated by new line or tab)</td>
    <td colspan="2">
        <html:textarea styleClass="itemtext" property="srcContainerList" rows="5" cols="50"/>
    </td>
  </tr>  
  <tr> 
    <td colspan="3" valign="top" class="formlabel">Destination containers: (separated by new line or tab)</td>
  </tr>
  <tr> 
    <td width="30%" height="98" valign="top" class="formlabel">Working copies:</td>
    <td height="98" valign="top" class="itemtext">
        <html:textarea styleClass="itemtext" property="destContainerListWorking" rows="5" cols="50"/>
    </td>
    <td  height="98" valign="top" class="formlabel">
        <html:text property="volumnWorking" styleClass="itemtext" size="10"/>Volumn (in microliter)
    </td>
  </tr>
  <tr> 
    <td width="30%" height="98" valign="top" class="formlabel">Local archive copies:</td>
    <td height="98" valign="top" class="itemtext">
        <html:textarea styleClass="itemtext" property="destContainerListArchive" rows="5" cols="50"/>
    </td>
    <td height="98" valign="top" class="formlabel">
        <html:text property="volumnArchive" styleClass="itemtext" size="10"/>Volumn (in microliter)
    </td>
  </tr>
  <tr> 
    <td colspan="3" class="formlabel"><html:checkbox property="isBiobank">Check here if you need BioBank copy</html:checkbox></td>
  </tr>
  <tr> 
    <td width="30%" height="98" valign="top" class="formlabel">BioBank copies:</td>
    <td height="98" valign="top" class="itemtext">
        <html:textarea styleClass="itemtext" property="destContainerListBiobank" rows="5" cols="50"/>
    </td>
    <td height="98" valign="top" class="formlabel">
        <html:text property="volumnBiobank" styleClass="itemtext" size="10"/>Volumn (in microliter)
    </td>
  </tr>
  <tr> 
    <td width="30%" height="98" valign="top" class="formlabel">Reservoir for glycerol stock:</td>
    <td height="98" valign="top" class="itemtext">
        <html:select property="glyceroltype" styleClass="itemtext">
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
    <td height="98" valign="top" class="formlabel">
        <html:text property="volumnGlycerol" styleClass="itemtext" size="10"/>Volumn (in microliter)
    </td>
  </tr>
  <tr> 
    <td width="30%">&nbsp;</td>
    <td colspan="2"> <html:submit styleClass="itemtext" value="Generate Worklist"/> </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</div>
</html>

