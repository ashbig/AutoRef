<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Import Finished Clones From ACE to FLEX</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Import Finished Clones From ACE to FLEX</h2>
<hr>
<html:errors/>

<p>
<html:form action="/ImportSeqResults.do" focus="cloneList" method="POST" enctype="multipart/form-data">
<table border=0 cellspacing=10 cellpadding=2>

    <tr>
    <td></td>
    <td><html:radio property="importType" value="nonfile">
        Enter all the clone IDs: (separated by spaces)
        </html:radio>
    </td>
    <td><html:radio property="importType" value="file">
        Upload the file containing all the clone IDs: (separated by spaces)
        </html:radio>
    </td>
    </tr>

    <tr>
    <td></td>
    <td><html:textarea property="cloneList" rows="10"/>
    </td>
    <td valign="top"><html:file property="cloneListFile" />
    </td>
    </tr>
</table>

<p>
<table border=0 cellspacing=10 cellpadding=2>
    <tr>
    <td class="prompt">Final clone status:</td>
    <td><html:select property="cloneType">
        <html:option value="Successful">Successful</html:option>
        <html:option value="Fail">Fail</html:option>
    </html:select></td>
    <td></td>
    </tr>

    <tr>
    <td></td>
    <td><html:submit property="submit" value="Continue"/></td>
    <td><html:reset/></td>
    </tr>
</table>
</html:form>

</body>
</html:html>