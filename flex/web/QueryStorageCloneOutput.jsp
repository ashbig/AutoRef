<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>
<%@ page import="edu.harvard.med.hip.flex.query.handler.QueryManager" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>

<logic:iterate id="clone" name="clones">
<p>
<table>
    <tr>
        <td class="prompt">Clone ID:</td><td><bean:write name="clone" property="cloneid"/></td>
        <td class="prompt">Clone type:</td><td><bean:write name="clone" property="clonetype"/></td>
    </tr>
    <tr>
        <td class="prompt">Version:</td><td><bean:write name="clone" property="constructtype"/></td>
        <td class="prompt">Vector:</td><td><bean:write name="clone" property="cloningstrategy.clonevector.name"/></td>
    </tr>
    <tr>
        <td class="prompt">Sequence ID:</td><td><bean:write name="clone" property="refsequenceid"/></td>
        <td class="prompt">Status:</td><td><bean:write name="clone" property="status"/></td>
    </tr>
</table>

<p>
The following storages are available:
<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th>Sample ID</th><th>Plate</th><th>Well</th><th>Storage Type</th><th>Storage Form</th>
    </tr>

<logic:iterate name="clone" property="storages" id="storage">
    <tr>
        <td><bean:write name="storage" property="sampleid"/></td>
        <td><bean:write name="storage" property="label"/></td>
        <td><bean:write name="storage" property="position"/></td>
        <td><bean:write name="storage" property="storageType"/></td>
        <td><bean:write name="storage" property="storageForm"/></td>
    </tr>
</logic:iterate>
</table>
<p>
<hr>
</logic:iterate>

</body>
</html>