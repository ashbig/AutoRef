<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Enter Expression Plate Result </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Enter Expression Plate Result</h2>
<hr>
<html:errors/>
<p>
<logic:present name="plateBarcode">
<ul>
<li><red>Results have been entered sucessfully for plate <bean:write name="plateBarcode"/>.</red>
</ul>
</logic:present>
<p>
<html:form action="/EnterExpressionPlateBarcode.do" focus="newPlate">

<table>
    <tr>
    <td class="prompt">Enter the expression plate barcode:</td>
    <td><html:text property="newPlate" size="40"/></td>
    </tr>    
    <tr>
    <td class="prompt">Enter the researcher barcode:</td>
    <td><html:password property="researcherBarcode" size="30"/></td>
    </tr>
    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

