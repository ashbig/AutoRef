<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate</title>
</head>
<body bgcolor="white">

<h2><bean:message key="flex.name"/> : Create Process Plate</h2>
<hr>
<html:errors/>

<html:form action="/SelectProtocol.do">
<table>
    <tr>
    <td><b>Select process protocol:</b></td>
    <td><html:select property="processname">
        <html:options
        collection="CreateProcessPlateAction.protocols"
        property="processname"
        labelProperty="processname"
        />
    </html:select>
    </td>

    <td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

