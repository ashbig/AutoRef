<%@ page language="java" %>
<%@ page errorPage="../ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> :  Populate empty plates</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<h2><bean:message key="flex.name"/> : Populate empty plates </h2>
    <hr>
    <html:errors/>
<html:form action="PopulateEmptyContainers.do" focus="logFile" enctype="multipart/form-data">
   
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowname")%>" >
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
<input type="hidden" name="protocolid" value="<bean:write name="protocolid"/>">
<input type="hidden" name="protocolname" value="<bean:write name="protocolname"/>">
 

    <table>
    <tr><td class="prompt">Project name:</td>  
    <td><bean:write name="projectname"/></td>    </tr>
    
    <tr>    <td class="prompt">Workflow name:</td>
    <td><%= request.getAttribute("workflowname")%></td>    </tr>
    
    <tr>    <td class="prompt">Process name:</td>
    <td><bean:write name="protocolname"/></td>    </tr>
    <tr>   <td class="prompt">Please enter the Norgen log file:</td>    
        <td> &nbsp;<html:file property="logFile" /></td>
    </tr>

    <tr>
       <td class="prompt"><bean:message key="flex.researcher.barcode.prompt"/></td>
       <td><html:password property="researcherBarcode"/></td>
    </tr>
    </table>
    <br>

     <tr> <td colspan=2 align="center"><html:submit property="submit" value="Submit"/></td>
    </tr>

</html:form>

<jsp:include page="../QueueItemsDisplay.jsp" flush="true"/>

</body>
</html>
