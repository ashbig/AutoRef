<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="plasmid.coreobject.Process" %> 
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
<jsp:include page="internalHomeTitle.jsp" />
<table width="1000" height="406" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>
    <td width="83%" align="left" valign="top">
        <jsp:include page="generateWorklistTitle.jsp" />
<html:errors/>

<p class="alert">Please note that the mapping between the source plates and the destination
plates is the same as your input order.</p>

      <html:form action="WorklistInput.do">

<table width="100%" border="0">
  <tr> 
    <td width="20%" class="formlabel">Process:</td>
    <td width="80%" colspan="2" class="itemtext" ><bean:write name="generateWorklistForm" property="processname"/></td>
  </tr>
  <logic:present name="<%=Constants.PROTOCOLS %>">
  <tr> 
    <td width="20%" height="37" class="formlabel">Select protocol:</td>
    <td colspan="2">
        <html:select property="protocol" styleClass="itemtext">
          <html:options collection="<%=Constants.PROTOCOLS %>" property="name"/>
        </html:select>
    </td>
  </tr>
  </logic:present>
  <tr> 
    <td width="20%" height="91" valign="top" class="formlabel">Source containers: <br>(separated by white space)</td>
    <td colspan="2">
        <html:textarea styleClass="itemtext" property="srcContainerList" rows="5"/>
    </td>
  </tr>
  <tr> 
    <td width="20%" height="98" valign="top" class="formlabel">Destination containers: <br>(separated by white space)</td>
    <td colspan="2" class="itemtext">
        <html:textarea styleClass="itemtext" property="destContainerList" rows="5"/>
    </td>
  </tr>
  <tr> 
    <td width="20%" height="29" class="formlabel">Volumn (in microliter):</td>
    <td colspan="2">
        <html:text property="volumn" styleClass="itemtext" size="30"/>
    </td>
  </tr>
  <logic:equal name="generateWorklistForm" property="processname" value="<%=Process.GENERATE_GLYCEROL%>">
  <tr> 
    <td width="20%" colspan="3" height="29" class="formlabel"><html:checkbox property="tube"/>Please check here if the destination containers are tubes</td>
  </tr>
  </logic:equal>
  <logic:equal name="generateWorklistForm" property="processname" value="<%=Process.CULTURE%>">
  <tr> 
    <td width="20%" colspan="3" height="29" class="formlabel"><html:checkbox property="tube"/>Please check here if the source containers are tubes</td>
  </tr>
  </logic:equal>
  <tr> 
    <td width="20%">&nbsp;</td>
    <td colspan="2"> <html:submit styleClass="itemtext" value="Generate Worklist"/> </td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

