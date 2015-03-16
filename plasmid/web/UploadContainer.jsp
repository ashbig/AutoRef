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
<link href="SpryMenuBarHorizontal.css" rel="stylesheet" type="text/css" />
<link href="boilerplate.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="respond.min.js"></script>
</head>
<div class="gridContainer clearfix">

<body>
<jsp:include page="internalHomeTitle.jsp" />
<table id='content' width="100%" border="0" align="center" bordercolor="#FFFFFF" bgcolor="#FFFFFF">
  <tr> 
    <%--<td width="17%" height="202" align="left" valign="top" bgcolor="#CCCCCC" class="leftsectiontitle"> 
      <jsp:include page="sampleTrackingMenu.jsp" />
	</td>--%>
    <td width="100%" align="left" valign="top">
        <%--<jsp:include page="uploadContainerTitle.jsp" />--%>
<html:errors/>
      <html:form action="UploadContainer.do" focus="label" enctype="multipart/form-data">
          <p class="text">Please use this page to upload any type of new containers, including 96 well, 384 well and individual tubes.</p>
          <ul class="text">
              <li>File format: tab delimited text file with three columns (container label, well, PlasmID clone ID).</li>
              <li>Please include the column header.</li>
              <li>Each file can only have one type of container, but you can put multiple containers in one file as long as they are the same type.</li>
              <li>Please remove all the empty wells.</li>
              <li>The well location for individual tube is always A1 or A01.</li>
              <li>Sample files: <a href="SampleFileContainer.txt" target="_blank">96 well container</a>, <a href="SampleFileTube.txt" target="_blank">tube</a>.</li>
          </ul>
          
<table width="100%" border="0">
  <tr>
    <td width="18%" class="formlabel">Container type</td>
    <td width="47%">
        <html:select property="containertype" styleClass="text">
            <html:options name="containertypes"/>
        </html:select>
    </td>
    <td width="35%">&nbsp;</td>
  </tr>
  <tr>
    <td width="18%" class="formlabel">Sample type</td>
    <td width="47%">
        <html:select property="sampletype" styleClass="text">
            <html:options name="sampletypes"/>
        </html:select>
    </td>
    <td width="35%">&nbsp;</td>
  </tr>
  <tr>
    <td width="18%" class="formlabel">Container locations</td>
    <td width="47%">
        <html:select property="location" styleClass="text">
            <html:options name="locations"/>
        </html:select>
    </td>
    <td width="35%">&nbsp;</td>
  </tr>
  <tr>
    <td class="formlabel">Upload the container file:</td>
    <td>
        <html:file styleClass="text" property="file" />
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td height="66">&nbsp;</td>
    <td>
        <html:submit styleClass="text"/>
    </td>
    <td>&nbsp;</td>
  </tr>
</table>
      </html:form></td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</div>
</html>

