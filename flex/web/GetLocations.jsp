<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create DNA from glycerol</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create DNA from glycerol</h2>
<hr>
<html:errors/>
<p>
<html:form action="/GetLocations.do">
<input type="hidden" name="workflowid" value=<bean:write name="workflowid"/>>
<input type="hidden" name="projectid" value=<bean:write name="projectid"/>> 
<table>
    <logic:iterate id="oldContainer" name="EnterSourcePlateAction.oldContainers">
    <bean:define id="srcLocation" name="oldContainer" property="location.type"/>
    <tr>
    <td class="label">Source plate barcode:</td>
    <td><bean:write name="oldContainer" property="label"/></td>
    <td class="prompt">Location:</td>
    <td><html:select property="srcLocations">
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