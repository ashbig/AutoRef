<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
    <title><bean:message key="flex.name"/> : rearray</title>
    <LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>

<body>
    
<html:form action="RearrayPlates.do"  enctype="multipart/form-data">
    <h2><bean:message key="flex.name"/> : Process Rearray</h2>
    <hr>
    <html:errors/>
   
<logic:present name="projectid">
    <input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
    <input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
</logic:present>
<logic:present name="workflowid">
    <input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
    <input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowname")%>">
   <input type="hidden" name="processname" value="<%= request.getAttribute("processname")%>">
</logic:present>





<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>   
     <td class="prompt">Process name:</td>
    <td><%= request.getAttribute("processname")%></td>
    </tr>   
    <tr>
       <p> <td class="prompt">Please enter the rearray file:</td>    
        <td><html:file property="requestFile" /></td>
    </tr>
    <tr>
        <td class="prompt">Number of wells on plate:</td>    
        <td ><input type="text" name="wellsOnPlate" value = "96" align="right"/></td>
    </tr>

    <tr>
    <td class="prompt">Is full plate required?</td>
    <td>
        <html:radio property="isFullPlate" value="true"/>Yes
        <html:radio property="isFullPlate" value="false"/>No
    </td>
</tr>
<tr>
    <td class="prompt">Put sequences on queue for processing?</td>
        <td >
            <input type="radio" name="isPutOnQueue"  value="true">Yes
            <input type="radio" name="isPutOnQueue" checked value="false">No
        </td>
</tr>
<tr>
     <td class="prompt">Is controls requered?</td>
     <td>
        <html:radio property="isControls" value="true"/>Yes
        <html:radio property="isControls" value="false"/>No
    </td>
</tr>
<tr>
     <td class="prompt">Is sort by saw-tooth patern?</td>
         <td>
        <html:radio property="isSortBySawToothpatern" value="true"/>Yes
        <html:radio property="isSortBySawToothpatern" value="false"/>No
    </td>
</tr>
    </table>

        <b>Choose sequences that you want to group together:</b> 
        (sequences you don't choose will be grouped separately)
        <dl>
        <dd><html:checkbox property="small" />small genes (0 <= CDS < 2000)
        <dd><html:checkbox property="medium" />medium genes (2000 <= CDS < 4000)
        <dd><html:checkbox property="large" />large genes (CDS >= 4000)
        </dl>
    <br>

    <html:submit/>

</html:form>