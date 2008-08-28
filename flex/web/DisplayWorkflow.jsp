<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="edu.harvard.med.hip.flex.workflow.*" %>
<html>
<head>
    <title><bean:message key="flex.name"/> : Workflow Information</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h3><bean:message key="flex.name"/> : Workflow Information</h3>
 
<html:errors/>
<p>
    
    
<%
Workflow w = (Workflow ) request.getAttribute("workflow");%>
<table width="100%" align="center">
         <tr>
        <td class="prompt">Workflow Id</td>
        <td><bean:write name="workflow" property="id"/></td>
        </tr>
        <tr>
        <td class="prompt">Workflow Name:</td>
        <td><bean:write name="workflow" property="name"/></td>
        </tr>
        <tr>
        <td class="prompt">Workflow Type:</td>
        <td><%= w.getWorkflowType().getTitle() %></td>
        </tr>
         <logic:notEmpty  name="workflow" property="properties">
         <bean:define id="workflowproperties" name="workflow" property="properties"/>

        <tr><td class="prompt">Workflow properties:</td>&nbsp;<td></td></tr>
        <tr><td colspan="2"> <table>
        <tr>
            <th>Name</th>
            <th>Value</th></tr>
            <logic:iterate name="workflowproperties" id="property">
	                <tr>
	                  <td><bean:write name="property" property="name"/></td>
                <td><bean:write name="property" property="value"/></td>
                
	                </tr>
        </logic:iterate>
        
        
    </table></td></tr>
        </logic:notEmpty>
        <tr><td class="prompt"><P><P>Workflow protocols</td><td>&nbsp;</td></tr>
       <tr> <td colspan="2"> <%= w.getHTMLView() %></td></tr>
        
           </table>

   
    
</body>
</html>
