<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>FLEXGene : Create Process Plate</title>
</head>
<body bgcolor="white">

<h2>FlexGene : Create Process Plate</h2>
<hr>

<html:form action="/SelectProtocol.do">
<table>
    <tr>
    <td><b>Select process protocol:</b></td>
    <td><select name="protocol">
        <logic:iterate id="oneProtocol" name="protocols" scope="request">
            <option value="<bean:write name="oneProtocol" property="id"/>"><bean:write name="oneProtocol" property="processname"/>
        </logic:iterate>
    </td>

    <td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

