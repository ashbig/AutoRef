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
<html:form action="/EnterSrcForSequencingConfirm.do">
<input type="hidden" name="projectid" value="<bean:write name="projectid"/>">
<input type="hidden" name="workflowid" value="<bean:write name="workflowid"/>">
<input type="hidden" name="workflowname" value="<%= request.getAttribute("workflowname")%>" >
<input type="hidden" name="projectname" value="<bean:write name="projectname"/>">
<input type="hidden" name="researcherid" value="<bean:write name="researcherid"/>">

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

    <logic:present name="plate1">
    <input type="hidden" name="plate1" value="<bean:write name="plate1" property="id"/>">
    <tr>
    <td class="prompt">1st glycerol plate barcode:</td>
    <td><bean:write name="plate1" property="label"/></td>
    </tr>
    </logic:present>

    <logic:present name="plate2">
    <input type="hidden" name="plate2" value="<bean:write name="plate2" property="id"/>">
    <tr>
    <td class="prompt">2nd glycerol plate barcode:</td>
    <td><bean:write name="plate2" property="label" /></td>
    </tr>
    </logic:present>

    <logic:present name="plate3">
    <input type="hidden" name="plate3" value="<bean:write name="plate3" property="id" />">
    <tr>
    <td class="prompt">3rd glycerol plate barcode:</td>
    <td><bean:write name="plate3" property="label"/></td>
    </tr>

    </logic:present>
    <logic:present name="plate4">
    <input type="hidden" name="plate4" value="<bean:write name="plate4" property="id" />">
    <tr>
    <td class="prompt">4th glycerol plate barcode:</td>
    <td><bean:write name="plate4" property="label" /></td>
    </tr>
    </logic:present>

    <tr>
    <td class="prompt">Rows on source plates:</td>
    <td>
        <logic:equal name="row" value="both">
        Both
        </logic:equal>
        <logic:equal name="row" value="ae">
        A, E
        </logic:equal>
        <logic:equal name="row" value="bf">
        B, F
        </logic:equal>
    </td>
    </tr>
    <input type="hidden" name="row" value="<bean:write name="row"/>">

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

</body>
</html>

