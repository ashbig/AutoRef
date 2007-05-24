<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Transfer clone information from ACE to FLEX</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Transfer clone information from ACE to FLEX</h2>
<hr>
<html:errors/>
<p>This page allows you to transfer clone information from ACE to FLEX. 
The submitted file must contains clone ID only . 

<P>Transfer  of clone information may take some time.
The e-mail notification will be sent to you upon completion.
<p>
<P>
<html:form action="/ACEtoFLEXImporter.do" enctype="multipart/form-data"> 

<table>
<tr>
    <td class="prompt">Please select the clone information file:</td>
    <td><html:file property="mgcCloneFile" />  </td>
   <!-- <td>[<a href="ViewMgcSampleFile.jsp">sample file</a>]</td>-->
</tr>



</table>
<P><P>
<html:submit/>
</html:form>


</body>
</html:html>
