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

Special oligo order can handle partial oligo plate ordering for non-MGC project.
You can choose to group the oligos together for different size groups.
<p>
<html:form action="/SpecialOligoOrder.do" focus="isFullPlate"> 

<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">

<p>
<table border=0>
    <tr>
        <td><b>Number of small sequences on the queue:</b></td>
        <td><bean:write name="small" /></td>
    </tr>
    <tr>
        <td><b>Number of medium sequences on the queue:</b></td>
        <td><bean:write name="medium" /></td>
    </tr>
    <tr>
        <td><b>Number of large sequences on the queue:</b></td>
        <td><bean:write name="large" /></td>
    </tr>
</table>

<p>
<dl>
    <dt><b>Is full plate required?</b>
        <dd><html:radio property="isFullPlate" value="true"/>Yes
        <html:radio property="isFullPlate" value="false"/>No
<p>
    <dt><b>Choose sequences that you want to group together:</b> 
        (sequences you don't choose will be grouped separately)
        <dd><html:checkbox property="small" />small genes (0 <= CDS < 2000)
        <dd><html:checkbox property="medium" />medium genes (2000 <= CDS < 4000)
        <dd><html:checkbox property="large" />large genes (CDS >= 4000)
<dl>
<p>
<html:submit/>
</html:form>

</body>
</html:html>

