<%@ page language="java" %>
<%@ page errorPage="ProcessError.do"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html>
<head>
<title><bean:message key="flex.name"/> : Create Process Plates</title>
<LINK REL=StyleSheet HREF="FlexStyle.css" TYPE="text/css" MEDIA=screen>
</head>
<body>

<h2><bean:message key="flex.name"/> : Create Process Plates</h2>
<hr>
<html:errors/>
<p>
<html:form action="/EnterSrcForGlycerolAndSeq.do" focus="plate1">
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
    <td class="prompt">Enter 1st culture plate barcode:</td>
    <td><html:text property="plate1" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 2nd culture plate barcode:</td>
    <td><html:text property="plate2" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 3rd culture plate barcode:</td>
    <td><html:text property="plate3" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Enter 4th culture plate barcode:</td>
    <td><html:text property="plate4" size="40"/></td>
    </tr>

    <tr>
    <td class="prompt">Select the rows:</td>
    <td><select name="row">
            <OPTION value="both"/>Both
            <option value="ae"/>A, E
            <option value="bf"/>B, F
        </select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Generate rearray files?</td>
    <td><select name="isMappingFile">
            <option value="Yes"/>Yes
            <option value="No"/>No
        </select>
    </td>
    </tr>

    <tr>
    <td class="prompt">Select the protocol:</td>
    <td><html:select property="subProtocolName">
        <bean:define id="subprotocols" name="SelectProtocolAction.protocol" property="subprotocol"/>
        <html:options
        collection="subprotocols"
        property="name"
        labelProperty="name"
        />
        </html:select>
    </td>
    </tr>

    <tr>
    <td></td><td><html:submit property="submit" value="Continue"/></td>
    </tr>
</table>
</html:form>

<jsp:include page="QueueItemsDisplay.jsp" flush="true" />

</body>
</html>

