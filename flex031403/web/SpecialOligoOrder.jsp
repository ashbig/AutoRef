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

<html:hidden property="projectid"/>
<html:hidden property="workflowid"/>

<dl>
    <dt class="prompt">Is full plate required?
        <dd><html:radio property="isFullPlate" value="true"/>Yes
        <dd><html:radio property="isFullPlate" value="false"/>No
    <dt class="prompt">Group sequences into small, medium and large queues?
        <dd><html:radio property="isGroupBySize" value="true"/>Yes
        <dd><html:radio property="isGroupBySize" value="false"/>No
<dl>
<html:submit/>
</html:form>

</body>
</html:html>

