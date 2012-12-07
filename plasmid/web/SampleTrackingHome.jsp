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
        <jsp:include page="sampleTrackingHomeTitle.jsp" />
<html:errors/>

<ul>
    <li class="text"><a href="LabelPlates.jsp">Create New Container Labels</a>
    <li class="text"><a href="SelectProcess.jsp">Generate Worklist</a>
    <li class="text"><a href="GetResultTypes.do">Enter Results</a>
    <li class="text"><a href="FindClones.jsp">Find Clones</a>
    <li class="text"><a href="UpdateCloneStatusInput.jsp">Update Clone Status</a>
    <li class="text"><a href="UpdateCloneStorageInput.jsp">Update Clone Storage</a>
    <li class="text"><a href="PrepareContainerUpload.do">Upload Containers</a>
    <li class="text"><a href="Plate96To384Input.jsp">Generate Condensed Archive Plates</a>
    <li class="text"><a href="Plate384To96Input.jsp">Reverse Plate Condensation</a>
</ul>
<table width="100%" border="0">
        <tr> 
          <td height="15" class="mainbodytext">Vector Collection</td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetAllVectors.do?PSI=false" class="leftsubnavtext">all empty vectors</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=viral%20production" class="leftsubnavtext">viral production</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=RNA%20interference" class="leftsubnavtext">RNA interference</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=mammalian%20expression" class="leftsubnavtext">mammalian expression</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=drosophila%20in%20vitro%20and%20in%20vivo%20expression" class="leftsubnavtext">drosophila in vitro and in vivo expression</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=bacterial%20expression" class="leftsubnavtext">bacterial expression</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=baculovirus/insect%20cell%20expression" class="leftsubnavtext">baculovirus/insect cell expression</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=cloning%20vector" class="leftsubnavtext">cloning vector</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=mutagenesis%20vector" class="leftsubnavtext">mutagenesis vector</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=Yeast%20Expression" class="leftsubnavtext">Yeast Expression</a></td>
        </tr>
        <tr> 
          <td height="15" class="lftsubtxt">- <a href="GetVectorsByType.do?type=fluorescent%20marker" class="leftsubnavtext">fluorescent marker</a></td>
        </tr>
        <!--<tr> 
          <td height="15" class="lftsubtxt">- <a href="GetAllVectors.do?PSI=true" class="leftsubnavtext">PSI empty vectors</a></td>
        </tr>-->
      </table>
    </td>
  </tr>
</table>
<jsp:include page="footer.jsp" />
</body>
</html>

