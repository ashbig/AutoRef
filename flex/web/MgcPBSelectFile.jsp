<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Print Barcodes for MGC Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Print Barcodes for MGC Plates</h2>
<hr>
<html:errors/>
<p>


<p>
<html:form action="/MgcDisplayNewPlates.do" enctype="multipart/form-data"> 
This page allows you to print barcodes for MGC plates based on <b>mgc clone distribution file</b>.
<p><p>
<table>
<tr>
    <td class="prompt">Please select the mgc clone distribution file:</td>
    <td><html:file property="mgcCloneFile" /></td>
    <td>[<a href="ViewMgcSampleFile.jsp">sample file</a>]</td>
</tr>



</table>
<P><P>
<html:submit/>
</html:form>


</body>
</html:html>
