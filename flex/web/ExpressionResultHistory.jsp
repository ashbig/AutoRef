<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Expression Result History </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Expression Result History</h2>
<hr>
<html:errors/>
<p>

<table>
    <tr>
        <td class="prompt">Sample ID:</td>
        <td><bean:write name="sample" property="id"/></td>
    </tr>
    <tr>
        <td class="prompt">Result Type:</td>
        <td><bean:write name="resulttype"/></td>
    </tr>
</table>

<p>
<TABLE border="1" cellpadding="2" cellspacing="0">
    <tr class="headerRow">
        <th>Result</th>
        <th>Researcher</th>
        <th>Process Date</th>
    </tr>

    <logic:iterate id="result" name="results">
    <tr>
        <td><flex:write name="result" property="value"/></td>
        <td><flex:write name="result" property="process.researcher.name"/></td>
        <td><flex:write name="result" property="process.date"/></td>
    </tr>
    </logic:iterate>
</table>

</body>
</html>


