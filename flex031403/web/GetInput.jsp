<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plate </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Process Plate</h2>
<hr>
<html:errors/>
<p>
<html:form action="/GetInput.do" focus="researcherBarcode">
<table>
    <tr>
    <td class="prompt"><bean:message key="flex.researcher.barcode.prompt"/></td>
    <td><html:text property="researcherBarcode" size="40"/></td>
    </tr>

    <tr>
    <td>Enter the source plate barcode:</td>
    <td><html:text property="sourcePlate" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Select the location for the destination plate:</td>
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
<h3>The following containers are available:</h3>
<table border="1" cellpadding="2" cellspacing="0">
<tr class="headerRow">
    <th>ID</th><th>Label</th>
</tr>

<logic:iterate id="queueItem" name="queueItems" scope="session">
<flex:row oddStyleClass="oddRow" evenStyleClass="evenRow">
    <td><bean:write name="queueItem" property="item.id"/></td>
    <td><bean:write name="queueItem" property="item.label"/></td>
</flex:row>
</logic:iterate>
</table>
</logic:present>

</body>
</html>

