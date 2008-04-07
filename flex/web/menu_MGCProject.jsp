<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/flex.tld" prefix="flex" %>

<%@ page import="edu.harvard.med.hip.flex.Constants" %>
<%@ page import="edu.harvard.med.hip.flex.infoimport.*" %>

<html:html locale="true">
<head>
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/>MGS Project</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<%@ include file="define_user_access_level.jsp" %>
    <h2><bean:message key="flex.name"/> :  MGS Project</h2>
<hr>
<html:errors/>

<p>
<p><b>Please select one of the following:</b></p>
     
<ul>      
    <% if (user_level >= WADMIN){%> 
    <li><a href="/FLEX/AddNewPlatesFromFile.do?forwardName=1" target="display">Import MGC Master List</a></li>
    <li>    <a href="/FLEX/GetProjects.do?forwardName=MGC_REQUEST_IMPORT" target="display">Import MGC Request</a> </li>
    
    <%}%>
    
    <% if (user_level >= RESEARCHER){%>    
    <li><a href="/FLEX/GetProjects.do?forwardName=MGC_PLATE_HANDLE" target="display">MGC Plate Handle</a></li>
    <%}%>
</ul>

  


</body>
</html:html>
