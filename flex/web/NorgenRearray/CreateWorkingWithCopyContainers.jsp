<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : Create Process Plate</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="GenerateGlycerolContainers.do" focus="sourcePlate">
    <h2><bean:message key="flex.name"/> : Create Process Plate </h2>
    <hr>
    <html:errors/>
 <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowname")%>" >
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">  
<input type="hidden" name="protocolname" value="<bean:write name="protocolname"/>">  
<input type="hidden" name="protocolid" value="<bean:write name="protocolid"/>">  



    <p>

    <table>
    <tr><td class="prompt">Project name:</td>  
    <td><bean:write name="projectname"/></td>    </tr>
    
    <tr>    <td class="prompt">Workflow name:</td>
    <td><%= request.getAttribute("workflowname")%></td>    </tr>
    
    <tr>    <td class="prompt">Process name:</td>
    <td><bean:write name="protocolname"/></td>    </tr>
    
   <tr>
    <td class="prompt">Enter the source plate barcode:</td>
    <td><html:text property="sourcePlate" size="40"/></td>
    </tr>
   <tr>
    <td class="prompt">Enter number of copies you would like to make: </td>
    <td><html:text property="numberOfPlates"  /></td>
    </tr>
     

     <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="../QueueItemsDisplay.jsp" flush="true"/>

</body>
</html>
