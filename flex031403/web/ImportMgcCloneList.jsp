<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Import Mgc Clone Master List into FLEXGene</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Import Mgc Clone Master List into FLEXGene</h2>
<hr>
<html:errors/>
<p><i>This page allows you to upload the MGC clone information and its sequences  into the
database from file: <b>mgc clone distribution file</b>. 
The <b>mgc clone distribution file</b> contains IMAGE_cloneID, MGC_ID, source_collection, source_plate, 
source_row, source_column, libr_id, species(only human are uploaded), vector, rearray_collection, 
rearray_plate, rearray_row, rearray_column. The mgc id is the unique identifier
for each clone. It will be used for quering ncbi to get clone sequnce.

<p>
<html:form action="/ImportMgcCloneInfo.do" enctype="multipart/form-data"> 

<table>
<tr>
    <td class="prompt">Please select the mgc clone distribution file:</td>
    <td><html:file property="mgcCloneFile" /></td>
    <td>[<a href="ViewMgcSampleFile.jsp">sample file</a>]</td>
</tr>



</table>
<html:submit/>
</html:form>


</body>
</html:html>
