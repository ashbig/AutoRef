<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Sequencing Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Sequencing Plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="/EnterSrcForSequencing.do" focus="plate1">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowname")%>" >
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">

<table>
 <tr>
    <td class="prompt">Project name:</td>
    <td><bean:write name="projectname"/></td>
    </tr>
    <tr>
    <td class="prompt">Workflow name:</td>
    <td><%= request.getAttribute("workflowname")%></td>
    </tr>
    <tr>
    <td class="prompt">Process name:</td>
    <td><bean:write name="SelectProtocolAction.protocol" property="processname"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 1st glycerol plate barcode:</td>
    <td><html:text property="plate1" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 2nd glycerol plate barcode:</td>
    <td><html:text property="plate2" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 3rd glycerol plate barcode:</td>
    <td><html:text property="plate3" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 4th glycerol plate barcode:</td>
    <td><html:text property="plate4" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Select the rows:</td>
    <td><select name="row">
            <option name="both"/>Both
            <option name="ae"/>A, E
            <option name="bf"/>B, F
        </select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Enter researcher barcode:</td>
    <td><html:text property="researcher" size="40"/></td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

