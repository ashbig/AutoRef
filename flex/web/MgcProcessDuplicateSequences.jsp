<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>
<%@ page import="java.util.*" %> 
<%@ page import="edu.harvard.med.hip.flex.util.*" %> 

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Full Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Process plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="MgcDeleteDuplicatedSequences.do">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="processname" value="<bean:write name="processname"/>">

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
    <td><bean:write name="sequences_count" /></td>
    </tr>
   <tr>
    <td class="prompt">Number of duplicates:</td>
    <td><bean:write name="number_of_duplicates" /></td>
    </tr>
    <tr>
    <td class="prompt">Keep duplicate sequences on queue?</td>
    <td>
         
       <html:radio property="isCleanupDuplicates" value="false"/>Yes
       <html:radio property="isCleanupDuplicates" value="true"/>No
    </td>
   </tr> 
</table>
<P>
<p>
    <html:submit property="submit" value="Submit"/>
<p>
<P>
<h4>Id of duplicated sequences:</h4>
<P>


<table>
<logic:iterate name="duplicatedSequences" id ="sequence" >
<tr>
    <td> &nbsp;   <bean:write name = "sequence" property="id" /> </td>
</tr>
</logic:iterate>
</table>

</html:form>

</body>
</html>