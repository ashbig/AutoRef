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
<title><bean:message key="flex.name"/> : <bean:message key="flex.name"/>Manage workflow</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>
<%@ include file="define_user_access_level.jsp" %>

    <h2><bean:message key="flex.name"/> : Workflow Managment</h2>
<hr>
<html:errors/>

<p>
<p><b>Please select one of the following:</b></p>

    <ul> 
        <li><a onclick="javascript:return false;" href="/FLEX/AddWorkflowItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.CREATE_PROJECT_INPUT%>" ><i>Not active link (under development): </i><%= ConstantsImport.PROCESS_NTYPE.CREATE_PROJECT_INPUT.getTitle()%>  </a> </li>
        <li>Create new workflow</li>
        <ul>
        <li><a onclick="javascript:return false;" href="/FLEX/AddWorkflowItems.do?forwardName=<%=  ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_INPUT %>"><i>Not active link (under development): </i><%=  ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_INPUT.getTitle() %>   </a> </li>
        <li><a href="/FLEX/AddWorkflowItems.do?forwardName=<%= ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE_INPUT %>"><%= ConstantsImport.PROCESS_NTYPE.CREATE_NEW_WORKFLOW_FROM_TEMPLATE_INPUT.getTitle() %>  </a> </li>
        </ul>
        <li><a href="/FLEX/AddWorkflowItems.do?forwardName=<%=  ConstantsImport.PROCESS_NTYPE.ADD_WORKFLOW_TO_PROJECT_INPUT %>"><%=  ConstantsImport.PROCESS_NTYPE.ADD_WORKFLOW_TO_PROJECT_INPUT.getTitle() %>   </a> </li>  
        <P></p>
     
    </ul>
   
<p>
    

</body>
</html:html>
