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
<html:form action="/GetInput.do" focus="researcherBarcode">
<table>
    <tr>
    <td><b>Enter your ID:</b></td>
    <td><html:text property="researcherBarcode" size="40"/></td>
    </tr>

    <tr>
    <td><b>Enter the source plate barcode:</b></td>
    <td><html:text property="sourcePlate" size="40"/></td>
    </tr>

    <tr>
    <td><b>Select the location for the destination plate:</b></td>
    <td><html:select property="destLocation">
        <html:options
        collection="locations"
        property="id"
        labelProperty="type"
        />
        </html:select>
    </td>

    <tr>
    <td></td><td><html:submit property="submit" value="Create"/></td>
    </tr>
</table>
</html:form>
<logic:present name="queueItems">
<p>
<b>The following containers are available:</b>
<table>
<tr>
    <th>ID</th><th>Label</th>
</tr>

<logic:iterate id="queueItem" name="queueItems" scope="session">
<tr>
    <td><bean:write name="queueItem" property="item.id"/></td>
    <td><bean:write name="queueItem" property="item.label"/></td>
</tr>
</logic:iterate>
</table>
</logic:present>

</body>
</html>

