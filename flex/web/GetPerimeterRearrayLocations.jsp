<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Cell Culture Perimeter Rearray </title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Cell Culture Perimeter Rearray</h2>
<hr>
<html:errors/>
<p>
<html:form action="/GetPerimeterRearrayLocations.do">
<input type="hidden" name="workflowid" value=<bean:write name="workflowid"/>>
<input type="hidden" name="projectid" value=<bean:write name="projectid"/>> 
<table>
    <tr>
    <td class="label">Process name:</td>
    <td><bean:write name="processname"/></td>
    </tr>

    <logic:iterate id="oldContainers" name="EnterSourcePlateAction.oldContainer">
    <tr>
    <td class="label">Source plate barcode:</td>
    <td><bean:write name="oldContainers" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="sourceLocations">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select></td>
    </tr>
    </logic:iterate>

    <logic:iterate id="newContainer" name="EnterSourcePlateAction.newContainers">
    <tr>
    <td class="label">Destination plate barcode:</td>
    <td><bean:write name="newContainer" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="destLocations">
        <html:options
        collection="EnterSourcePlateAction.locations"
        property="id"
        labelProperty="type"
        />
        </html:select>
    </td>
    </tr>
    </logic:iterate>    

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>