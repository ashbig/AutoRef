<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Query Results</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<H2><bean:message key="flex.name"/> : Query Results</h2>
<hr>
<html:errors/>
<p>


<h3>Search Parameters:</h3>
<table>
<logic:iterate name="params" id="param">
<tr>
    <td class="prompt"><small><bean:write name="param" property="name"/></small></td>
    <td><small><bean:write name="param" property="value"/></small></td>
</tr>
</logic:iterate>
</table>

<p>
<TABLE border=1>
    <tr bgcolor="#9bbad6">
    <th>Search Term</th><th>Reason</th>
    </tr>
    <logic:iterate name="results" id="result">
    <tr>
                <td><bean:write name="result" property="searchTerm"/></td>
                <td><bean:write name="result" property="reason"/></td> 
    </TR>
    </logic:iterate>
</table>

</body>
</html>