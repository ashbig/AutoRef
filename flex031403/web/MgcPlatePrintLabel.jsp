<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : New Mgc Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : New Mgc Plates</h2>
<hr>
<html:errors/>
<p><i>This page allows you to print barcodes for <b>New</b> MGC plates. 

<p>
<html:form action="/PrintMgcCloneBarcodes.do" enctype="multipart/form-data"> 

<table>
<tr>
    <td class="prompt">Please select the mgc clone distribution file:</td>
    <td><html:file property="mgcCloneFile" /></td>
    
</tr>



</table>
<html:submit/>
</html:form>


</body>
</html:html>
