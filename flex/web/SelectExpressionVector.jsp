<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Expression Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Expression Plate</h2>
<hr>
<html:errors/>
<p>
<logic:present name="plateBarcode">
<ul>
<li><red>Plate <bean:write name="plateBarcode"/> has been sucessfully created and results have been entered.</red>
</ul>
</logic:present>
<p>
<html:form action="/CreateExpressionPlate.do" focus="sourcePlate">

<table>
    <tr>
    <td class="prompt">Enter the master plate barcode:</td>
    <td><html:text property="sourcePlate" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Choose species:</td>
    <td><html:select property="project">
        <html:options
        name="projects"/>
        </html:select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Choose cloning strategy:</td>
    <td><html:select property="cloningStrategy">
        <html:options
        collection="cloningStrategies"
        property="id"
        labelProperty="name"
        />
        </html:select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Enter the researcher barcode:</td>
    <td><html:password property="researcherBarcode" size="30"/></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Create Expresson Plate"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

