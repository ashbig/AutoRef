<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : Special Oligo Order</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Special Oligo Order</h2>
<hr>
<html:errors/>

<p>
<html:form action="/SpecialOligoOrder.do" focus="isFullPlate"> 

<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">

<p>
<table border=0>
    <tr>
        <td>Number of small sequences on the queue:</td>
        <td><bean:write name="small" /></td>
    </tr>
    <tr>
        <td>Number of medium sequences on the queue:</td>
        <td><bean:write name="medium" /></td>
    </tr>
    <tr>
        <td>Number of large sequences on the queue:</td>
        <td><bean:write name="large" /></td>
    </tr>
</table>

<p>
<dl>
    <dt class="prompt">Is full plate required?
        <dd><html:radio property="isFullPlate" value="true"/>Yes
        <html:radio property="isFullPlate" value="false"/>No
    <dt class="prompt">Group sequences into small, medium and large queues?
        <dd><html:radio property="isGroupBySize" value="true"/>Yes
        <html:radio property="isGroupBySize" value="false"/>No
<dl>
<html:submit/>
</html:form>

</body>
</html:html>

