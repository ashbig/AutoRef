<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title>FLEXGene : Create Process Plate </title>
</head>
<body bgcolor="white">

<h2>FlexGene : Create Process Plate</h2>
<hr>
<html:errors/>
<table width=100% align=center>
    <tr>
    <td><b>Source Plate ID:</b></td>
    <td><bean:write name="oldContainer" property="label"/></td>
    <td><b>Source Plate Location:</b></td>
    <td><bean:write name="sLocation" property="type"/></td>
    </tr>

    <tr>
    <td><b>Destination Plate ID:</b></td>
    <td><bean:write name="newContainer" property="label"/></td>
    <td><b>Destination Plate Location:</b></td>
    <td><bean:write name="dLocation" property="type"/></td>
    </tr>

</table>

</body>
</html>

