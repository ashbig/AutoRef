<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Full Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Process full plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="MgcOrderOligo.do">
<html:hidden property="projectid" />
<html:hidden property="forwardName" />
<html:hidden property="workflowid" />
<html:hidden property="processname" />



<table>
    <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname" /></td>
    </tr>

    <tr>
    <td class="prompt">Workflow name:</td>
    <td><bean:write name="workflowname" /></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="processname" /></td>
    </tr>
    <tr>
    <td class="prompt">Number of clones:</td>
    <td><bean:write name="sequence_count" /></td>
    </tr>
<tr>
    <td class="prompt">Number of full plates:</td>
    <td><bean:write name="full_plates" /></td>
    </tr>

 
    <% 
//not full plates exists
if ( !request.getAttribute("wells_on_not_full_palte").toString().equals("0"))
{
   %> 
<tr>
    <td class="prompt">Number of clones on not full plate:</td>
    <td><bean:write name="wells_on_not_full_plate" /></td>
</tr>
<tr>   
    <td class="prompt">Is full plate required?</td>
    <td><html:radio property="isFullPlate" value="true"/>Yes
        <html:radio property="isFullPlate" value="false"/>No
    </td>
</tr> 
  
<%
}
%>
</table>
<p><P>
    <html:submit property="submit" value="Submit"/>
   

</html:form>

</body>
</html>